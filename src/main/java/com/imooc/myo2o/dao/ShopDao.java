package com.imooc.myo2o.dao;

import com.imooc.myo2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopDao {

    /**
     * 分页查询店铺，可输入的条件有：店铺名(模糊)，店铺状态，店铺类别，区域id，owner
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,
                             @Param("rowIndex") int rowIndex,@Param("pageSize")int pageSize);
    /**
     * 通过shop Id 查询店铺
     * @param shopId
     * @return
     */
    Shop queryByShopId(Long shopId);

    /**
     * 返回queryShopList总数
     * @param shopCondition
     * @return
     */
    int queryShopCount(@Param("shopCondition")Shop shopCondition);
    /**
     * 更新店铺信息
     * @param shop
     * @return
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺信息
     * @param shop
     * @return
     */
    int updateShop(Shop shop);
}
