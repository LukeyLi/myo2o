package com.imooc.myo2o.service.impl;

import com.imooc.myo2o.dao.ShopCategoryDao;
import com.imooc.myo2o.entity.ShopCategory;
import com.imooc.myo2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondiion) {

        return shopCategoryDao.queryShopCategory(shopCategoryCondiion);
    }
}
