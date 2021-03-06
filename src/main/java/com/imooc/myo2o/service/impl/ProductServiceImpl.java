package com.imooc.myo2o.service.impl;

import com.imooc.myo2o.dao.ProductDao;
import com.imooc.myo2o.dao.ProductImgDao;
import com.imooc.myo2o.dto.ImageHolder;
import com.imooc.myo2o.dto.ProductExecution;
import com.imooc.myo2o.entity.Product;
import com.imooc.myo2o.entity.ProductImg;
import com.imooc.myo2o.enums.ProductStateEnum;
import com.imooc.myo2o.exceptions.ProductOperationException;
import com.imooc.myo2o.service.ProductService;
import com.imooc.myo2o.util.FileUtil;
import com.imooc.myo2o.util.ImageUtil;
import com.imooc.myo2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;

    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        //页码转换为数据库的行码，并调用dao取回指定页码的商品列表
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
        List<Product> productList = productDao.queryProductList(productCondition,rowIndex,pageSize);
        //基于同样的查询条件返回该查询条件下的商品总数
        int count = productDao.queryProductCount(productCondition);
        ProductExecution pe = new ProductExecution();
        pe.setProductList(productList);
        pe.setCount(count);
        return pe;
    }

    @Override
    public Product getProductById(long productId) {
        return productDao.queryProductByProductId(productId);
    }



    @Override
    //1.处理缩略图，获取缩略图相对路径并赋值给Product
    //2.往tb_product写入商品信息，获取productId
    //3.结合productId批量处理商品详情图
    //4.将商品详情图列表批量插入Tb_product_img中
    public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList) throws ProductOperationException {
     if(product != null && product.getShop() != null && product.getShop().getShopId() != null) {
         //给商品设置默认属性
         product.setCreateTime(new Date());
         product.setLastEditTime(new Date());
         //默认为上架的状态
         product.setEnableStatus(1);
         //若商品缩略图不为空则添加
         if (thumbnail != null) {
             addThumnail(product, thumbnail);
         }
         try {
             //创建商品信息
             int effectedNum = productDao.insertProduct(product);
             if (effectedNum <= 0) {
                 throw new ProductOperationException("创建商品失败");
             }
         } catch (Exception e) {
             throw new ProductOperationException("创建商品失败:" + e.toString());
         }
         //若商品详情图不为空则添加
         if (productImgHolderList != null && productImgHolderList.size() > 0) {
             addProductImgList(product, productImgHolderList);
         }
         return new ProductExecution(ProductStateEnum.SUCCESS, product);
     }else {
         return new ProductExecution(ProductStateEnum.EMPTY);
     }
    }

    @Override
    @Transactional
    //1,若缩略图参数有值，则处理缩略图
    //若原先存在缩略图则先删除再添加新图，之后获取缩略图相对路径并赋值给product
    //2.若商品详情图列表参数有值，对商品详情图片列表进行同样操作
    //3.将tb_product_img下面的该商品原先的商品详情图记录全部清除
    //4.更新tb_product信息
    public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList) throws ProductOperationException {
        if(product != null && product.getShop()!= null &&product.getShop().getShopId()!= null){
            //给商品设置上默认属性
            product.setLastEditTime(new Date());
            //若商品缩略图不为空且原有缩略图不为空则删除原有缩略图并添加
            if(thumbnail != null){
                //先获取一遍原有信息，因为原来的信息有原图片地址
                Product tempProduct = productDao.queryProductByProductId(product.getProductId());
                if(tempProduct.getImgAddr()!= null){
                    FileUtil.deleteFile(tempProduct.getImgAddr());

                }
                addThumnail(product,thumbnail);
            }
            //如果有新存入的商品详情图，则将原先的删除，并添加新的图片
            if(productImgHolderList != null && productImgHolderList.size() > 0){
                deleteProductImgList(product.getProductId());
                addProductImgList(product,productImgHolderList);
            }
            try{
                //更新商品信息
                int effectedNum = productDao.updateProduct(product);
                if(effectedNum < 0){
                    throw new ProductOperationException("更新商品信息失败");
                }
                return new ProductExecution(ProductStateEnum.SUCCESS,product);
            }catch (Exception e){
                throw new ProductOperationException("更新商品信息失败"+ e.toString());
            }
        }
        else{
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    /**
     * 添加缩略图
     * @param product
     * @param thumbnail
     */
    private void addThumnail(Product product, ImageHolder thumbnail){
        String dest = FileUtil.getShopImagePath(product.getShop().getShopId());
        String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail,dest);
        product.setImgAddr(thumbnailAddr);
    }

    private void deleteProductImgList(long productId){
        List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
        for(ProductImg productImg : productImgList){
            FileUtil.deleteFile(productImg.getImgAddr());
        }
        productImgDao.deleteProductImgByProductId(productId);
    }

    private void addProductImgList(Product product,List<ImageHolder> productImageHolderList){
        String dest = FileUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgList = new ArrayList<ProductImg>();
        //遍历图片一次，添加进productImg实体里
        for(ImageHolder productImgHolder : productImageHolderList){
            String imgAddr = ImageUtil.generateThumbnail(productImgHolder,dest);
            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(imgAddr);
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new Date());
            productImgList.add(productImg);
        }
        //如果确实是有图片需要添加的，执行批量添加操作
        if(productImgList.size() > 0){
            try{
                int effectedNum = productImgDao.batchInsertProductImg(productImgList);
                if(effectedNum <= 0){
                    throw new ProductOperationException("创建商品详情图片失败");
                }
            }catch (Exception e){
                throw new ProductOperationException("创建商品详情图片失败："+ e.toString());
            }
        }
    }
}
