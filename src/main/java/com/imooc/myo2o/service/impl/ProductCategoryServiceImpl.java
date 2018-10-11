package com.imooc.myo2o.service.impl;

import com.imooc.myo2o.dao.ProductCategoryDao;
import com.imooc.myo2o.dao.ProductDao;
import com.imooc.myo2o.dto.ProductCategoryExecution;
import com.imooc.myo2o.entity.ProductCategory;
import com.imooc.myo2o.enums.ProductCategoryStateEnum;
import com.imooc.myo2o.exceptions.ProductCategoryOperationException;
import com.imooc.myo2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryDao productCategoryDao;
    @Autowired
    private ProductDao productDao;
    @Override
    public List<ProductCategory> getProductCategoryList(Long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }

    @Override
    @Transactional
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException {
        if(productCategoryList !=null && productCategoryList.size() > 0){
            try{
                int effectNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
                if(effectNum <= 0){
                    throw new ProductCategoryOperationException("创建店铺类别失败");
                }else{
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
                }
            }catch (Exception e){
                throw new ProductCategoryOperationException("batchAddProductCategory error"+ e.getMessage());
            }
        }else {
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }

    }

    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException {
       //接触tb_product里的商品与该productcategoryId的关联
        try{
           int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
           if(effectedNum <= 0){
               throw new RuntimeException("商品类别更新失败");
           }
       }catch (Exception e){
           throw new RuntimeException("deleteProductCategory error" + e.getMessage());
       }
        //删除该productCategory
        try{
            int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId,shopId);
            if(effectedNum <= 0){
                throw new ProductCategoryOperationException("商品类别删除失败");
            }else {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        }catch (Exception e){
            throw new ProductCategoryOperationException("deleteProductCategory error"+e.getMessage());
        }
    }
}
