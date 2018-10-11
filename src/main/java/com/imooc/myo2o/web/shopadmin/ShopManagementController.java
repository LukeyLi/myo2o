package com.imooc.myo2o.web.shopadmin;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.myo2o.dto.ImageHolder;
import com.imooc.myo2o.dto.ShopExecution;
import com.imooc.myo2o.entity.Area;
import com.imooc.myo2o.entity.PersonInfo;
import com.imooc.myo2o.entity.Shop;
import com.imooc.myo2o.entity.ShopCategory;
import com.imooc.myo2o.enums.ShopStateEnum;
import com.imooc.myo2o.exceptions.ShopOperationException;
import com.imooc.myo2o.service.AreaService;
import com.imooc.myo2o.service.ShopCategoryService;
import com.imooc.myo2o.service.ShopService;
import com.imooc.myo2o.util.CodeUtil;
import com.imooc.myo2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;
    @RequestMapping(value = "/getshopmanagementinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopManagementInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        if(shopId <= 0){
            Object currentShopObj = request.getSession().getAttribute("currentShop");
            if(currentShopObj == null){
                modelMap.put("redirect",true);
                modelMap.put("url","/shopadmin/shoplist");
            }else {
                Shop currentShop = (Shop) currentShopObj;
                modelMap.put("redirect",false);
                modelMap.put("shopId",currentShop.getShopId());
            }
        }else {
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop",currentShop);
            modelMap.put("redirect",false);
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshoplist",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopList(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        PersonInfo user = new PersonInfo();
        user.setUserId(1L);
        user.setName("TEST");
        request.getSession().setAttribute("user",user);
        user =(PersonInfo) request.getSession().getAttribute("user");
        user.setUserId(8L);
        long employeeId = user.getUserId();
        List<Shop> shopList = new ArrayList<Shop>();
        try{
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution se = shopService.getShopList(shopCondition,0,100);
            modelMap.put("shopList",se.getShopList());
            modelMap.put("user",user);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success", false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshopbyid",method = RequestMethod.GET)
    @ResponseBody
    private  Map<String, Object> getShopById(HttpServletRequest request){
         Map<String, Object> modelMap = new HashMap<String,Object>();
         Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
         if(shopId > -1){
             try {
                 Shop shop = shopService.getByShopId(shopId);
                 List<Area> areaList = areaService.getAreaList();
                 modelMap.put("shop",shop);
                 modelMap.put("areaList",areaList);
                 modelMap.put("success",true);
             } catch (Exception e) {
                 modelMap.put("success", false);
                 modelMap.put("errMsg",e.toString());
             }
         }else{
             modelMap.put("success",false);
             modelMap.put("errMsg","empty shopId");
         }
         return modelMap;
    }
    @RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopInitInfo(){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
        List<Area> areaList = new ArrayList<Area>();
        try{
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList",shopCategoryList);
            modelMap.put("areaList",areaList);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());

        }
        return modelMap;
    }
    @RequestMapping(value = "/modifyshop",method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入错误的验证码");
            return modelMap;
        }
        // 接收并转换相应的参数，包括店铺信息以及图片的信息
        String shopStr = HttpServletRequestUtil.getString(request,"shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try{
            shop = mapper.readValue(shopStr,Shop.class);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //判断request是否存在上传文件流
        if(commonsMultipartResolver.isMultipart(request)){
            //存在就将request的文件流类型作转化
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
        }
        //修改店铺信息
        if(shop != null && shop.getShopId()!= null){
            ShopExecution se;
            try {
                ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
                if(shopImg == null){
                    se = shopService.modifyShop(shop, null);
                }else{
                    se = shopService.modifyShop(shop,imageHolder);
                }
                if(se.getState() == ShopStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",se.getStateInfo());
                }
            }catch (ShopOperationException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());

            }catch (IOException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());

            }

            return modelMap;
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入店铺id");
            return modelMap;
        }

    }
    @RequestMapping(value = "/registershop",method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> registerShop(HttpServletRequest request){
       Map<String,Object> modelMap = new HashMap<String,Object>();
       if(!CodeUtil.checkVerifyCode(request)){
           modelMap.put("success",false);
           modelMap.put("errMsg","输入错误的验证码");
           return modelMap;
       }
       // 接收并转换相应的参数，包括店铺信息以及图片的信息
        String shopStr = HttpServletRequestUtil.getString(request,"shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try{
            shop = mapper.readValue(shopStr,Shop.class);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //判断request是否存在上传文件流
        if(commonsMultipartResolver.isMultipart(request)){
            //存在就将request的文件流类型作转化
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
    }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","上传图片不能为空");
            return modelMap;
        }
        //注册店铺
        if(shop != null && shopImg != null){
            //越少依赖前端的值
            PersonInfo owner =(PersonInfo) request.getSession().getAttribute("user");

            shop.setOwner(owner);
     //       File shopImgFile = new File(FileUtil.getImageBasePath() + FileUtil.getRandomFileName());
//            try {
//                shopImgFile.createNewFile();
//            } catch (IOException e) {
//                modelMap.put("success",false);
//                modelMap.put("errMsg",e.getMessage());
//                return modelMap;
//            }
//            try {
//                inputStreamToFile(shopImg.getInputStream(),shopImgFile);
//            } catch (IOException e) {
//                modelMap.put("success",false);
//                modelMap.put("errMsg",e.getMessage());
//                return modelMap;
//            }
            try {
                ImageHolder thumbnail = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
                ShopExecution se = shopService.addShop(shop,thumbnail);
                if(se.getState() == ShopStateEnum.CHECK.getState()){
                    modelMap.put("success",true);
                    //该用户可操作的用户列表
                    List<Shop> shopList =(List<Shop>) request.getSession().getAttribute("shopList");
                    if(shopList == null || shopList.size() == 0){
                        shopList = new ArrayList<Shop>();
                    }
                    shopList.add(se.getShop());
                    request.getSession().setAttribute("shopList",shopList);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",se.getStateInfo());
                }
            }catch (ShopOperationException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());

            }catch (IOException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());

            }

            return modelMap;
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入店铺信息");
            return modelMap;
        }

    }

//    private static void inputStreamToFile(InputStream ins, File file){
//        OutputStream os = null;
//
//        try{
//            os = new FileOutputStream(file);
//            int bytesRead = 0;
//            byte[] buffer = new byte[1024];
//            while((bytesRead = ins.read(buffer)) != -1){
//                os.write(buffer,0,bytesRead);
//            }
//        }catch (Exception e){
//            throw new RuntimeException("调用InputStreamToFile产生异常" + e.getMessage());
//        }finally {
//            try{
//                if(os != null){
//                    os.close();
//                }
//                if (ins != null){
//                    ins.close();
//                }
//            }catch (IOException e){
//                throw new RuntimeException("inputStreamToFileguan关闭io产生异常" + e.getMessage());
//            }
//        }
//    }
}
