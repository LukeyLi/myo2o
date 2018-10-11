package com.imooc.myo2o.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FileUtil {
    private static String seperator = System.getProperty("file.separator");

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

    private static final Random r = new Random();

    public static String getImageBasePath(){
        String os = System.getProperty("os.name");
        String basePath = "";
        if(os.toLowerCase().startsWith("win")){
            basePath = "E:\\";
        }else {
            basePath = "/home/xiangzepro/";
        }
        basePath = basePath.replace("/",seperator);
        return basePath;
    }

    public static String getShopImagePath(long shopId){
        StringBuilder shopImagePathBuilder = new StringBuilder();
        shopImagePathBuilder.append("/upload/items/shop/");
        shopImagePathBuilder.append(shopId);
        shopImagePathBuilder.append("/");

        String shopImagePath = shopImagePathBuilder.toString().replace("/",seperator);
        return shopImagePath;
//        String imagePath = "/upload/item/shop" + shopId + "/";
//        return imagePath;
    }

    public static String getRandomFileName(){
        //生成随机文件名：当前年月日分秒+五位随机数（为了在实际项目中防止文件同名而进行的处理）
        int random =(int)(r.nextDouble() * (99999 - 10000 + 1)) + 10000;  //获取随机数
        String nowTimeStr = df.format(new Date());  //当前时间
        return nowTimeStr + random;
    }

    public static void deleteFile(String storePath){
        File file = new File(getImageBasePath()+ storePath);
        if(file.exists()){
            if(file.exists()){
                if(file.isDirectory()){
                    File files[] = file.listFiles();
                    for(int i = 0; i <files.length; i++){
                        files[i].delete();
                    }
                }
                file.delete();
            }
        }
    }
}
