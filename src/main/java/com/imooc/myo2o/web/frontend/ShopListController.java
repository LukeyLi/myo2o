package com.imooc.myo2o.web.frontend;

import com.imooc.myo2o.dto.ShopExecution;
import com.imooc.myo2o.entity.Area;
import com.imooc.myo2o.entity.Shop;
import com.imooc.myo2o.entity.ShopCategory;
import com.imooc.myo2o.service.AreaService;
import com.imooc.myo2o.service.ShopCategoryService;
import com.imooc.myo2o.service.ShopService;
import com.imooc.myo2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/frontend")
public class ShopListController {
    @Autowired
    private AreaService areaService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private ShopService shopService;

    @RequestMapping(value = "/listshopspageinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listShopsPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        long parentId = HttpServletRequestUtil.getLong(request,"parentId");
        List<ShopCategory> shopCategoryList = null;
        if(parentId != -1){
            //如果parentId 存在，则取出该一级ShopCategory下的二级ShopCategory列表
            try {
                ShopCategory shopCategoryCondition = new ShopCategory();
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);
                shopCategoryCondition.setParent(parent);
                shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
            }catch(Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
                return modelMap;
            }
        }else {
            //如果parentId不存在，则取出所有一级shopCategory用户再首页选择的是全部商店列表
            try{
                shopCategoryList = shopCategoryService.getShopCategoryList(null);
            }catch(Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
                return modelMap;
            }
        }
        modelMap.put("shopCategoryList",shopCategoryList);
        List<Area> areaList = null;
        try{
            //获取区域列表信息
            areaList = areaService.getAreaList();
            modelMap.put("areaList",areaList);
            modelMap.put("success",true);
        }catch(Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        return modelMap;
    }

    /**
     * 获取指定查询条件下的店铺列表
     * @return
     */
    @RequestMapping(value = "/listshops", method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listShops(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        //获取页码
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");

        if((pageIndex > -1) &&(pageSize > -1)) {
            long parentId = HttpServletRequestUtil.getLong(request,"parentId");
            long shopCategoryId = HttpServletRequestUtil.getLong(request,"shopCategoryId");
            long areaId = HttpServletRequestUtil.getLong(request, "areaId");
            String shopName = HttpServletRequestUtil.getString(request, "shopName");
            Shop shopCondition = compactShopCondition4Search(parentId,shopCategoryId,areaId,shopName);

            ShopExecution se = shopService.getShopList(shopCondition,pageIndex,pageSize);
            modelMap.put("shopList",se.getShopList());
            modelMap.put("count",se.getCount());
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty pageSize or pageIndex");
        }
        return modelMap;
    }

    private Shop compactShopCondition4Search(long parentId,long shopCategoryId,long areaId,String ShopName){
        Shop shopCondition = new Shop();
        if(parentId != -1l){
            //查询某个一级shopCategory下面所有的二级shopCategory里面的店铺列表
            ShopCategory parentCategory = new ShopCategory();
            ShopCategory childCategory = new ShopCategory();
            parentCategory.setShopCategoryId(parentId);
            childCategory.setParent(parentCategory);
            shopCondition.setShopCategory(childCategory);
        }
        if(shopCategoryId != -1L){
            //查询某个二级ShopCategory下面的店铺列表
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if(areaId != -1l){
            //查询位于某个区域Id下的店铺列表
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }
        if(ShopName != null){
            //查询名字里包含shopName的店铺列表
            shopCondition.setShopName(ShopName);
        }
        //前端展示的店铺都是审核成功的店铺
        shopCondition.setEnableStatus(1);
        return shopCondition;
    }
}