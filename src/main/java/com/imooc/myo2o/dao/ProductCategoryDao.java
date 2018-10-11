package com.imooc.myo2o.dao;

import com.imooc.myo2o.entity.ProductCategory;
import com.imooc.myo2o.entity.ProductImg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductCategoryDao {
    /**
     * 通过shop id 查询店铺商品类别
     * @param shopId
     * @return
     */
    List<ProductCategory> queryProductCategoryList(Long shopId);

    /**
     * 批量添加商品图片详情
     * @param productImgList
     * @return
     */
    int batchInsertProductImg(List<ProductImg> productImgList);
    /**
     * 批量新增商品类别
     * @param productCategoryList
     * @return
     */
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);

    /**
     * 删除指定商品类别
     * @param productCategoryId
     * @param shopId
     * @return
     */
    int deleteProductCategory(@Param("productCategoryId")long productCategoryId,@Param("shopId")long shopId);


}
