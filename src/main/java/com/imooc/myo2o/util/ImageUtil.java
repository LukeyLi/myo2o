package com.imooc.myo2o.util;

import com.imooc.myo2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;

public class ImageUtil {
    /**
     * s'torePath是文件的路径还是目录的路径
     * 如果storePath是文件路径则删除该文件
     * 如果storePath是目录路径则删除该目录下所有的文件
     * @param storePath
     */
    public static void deleteFileOrPath(String storePath){
        File fileOrPath = new File(FileUtil.getImageBasePath() + storePath);
        if(fileOrPath.exists()){
            if(fileOrPath.isDirectory()){
                File files[] = fileOrPath.listFiles();
                for(int i = 0;i < files.length; i++){
                    files[i].delete();
                }
            }
            fileOrPath.delete();
        }
    }
    public static String generateThumbnail(ImageHolder thumbnail, String targetAddr){
        String realFileName = FileUtil.getRandomFileName();
        String extension = getFileExtension(thumbnail.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        File dest = new File(FileUtil.getImageBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbnail.getImage()).size(200, 200).outputQuality(0.25f).toFile(dest);
        } catch (IOException e) {
            throw new RuntimeException("创建缩略图失败：" + e.toString());
        }
        return relativeAddr;
    }
//    public static String generateThumbnail(CommonsMultipartFile thumbnail,String targetAddr){
//        String realFileName = FileUtil.getRandomFileName();
//        String extension = getFileExtension(thumbnail);
//        makeDirPath(targetAddr);
//        String relativeAddr = targetAddr + realFileName + extension;
//        File dest = new File(FileUtil.getImageBaseP    /**
//     * @param thumbnail spring自带的文件处理对象
//     * @param targetAddr 文件存储路径
//     * @return
//     */
//    public static String generateNormalImg(CommonsMultipartFile thumbnail, String targetAddr){
//        String realFileName = FileUtil.getRandomFileName();
//        String extension = getFileExtension(thumbnail);
//        makeDirPath(targetAddr);
//        String relativeAddr = targetAddr + realFileName + extension;
//        File dest = new File(FileUtil.getImageBasePath() + relativeAddr);
//        try{
//            Thumbnails.of(thumbnail.getInputStream()).size(200, 200).outputQuality(0.25f).toFile(dest);
//        } catch (IOException e) {
//            throw new RuntimeException("创建缩略图失败：" + e.toString());
//        }
//        return relativeAddr;
//    }ath() + relativeAddr);
//        try {
//            Thumbnails.of(thumbnail.getInputStream()).size(200, 200).outputQuality(0.25f).toFile(dest);
//        } catch (IOException e) {
//            throw new RuntimeException("创建缩略图失败：" + e.toString());
//        }
//        return relativeAddr;
//    }

//

    /**
     * 创建目标路径所涉及到的目录，即/home/work/xiangze/xxx.jpg,
     * 那么home work xiangze  这三个文件夹都得自动创建
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr){
        String realFileParentPath = FileUtil.getImageBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if(!dirPath.exists()){
            dirPath.mkdirs();
        }
    }
    private static String getFileExtension(String fileName){

        return fileName.substring(fileName.lastIndexOf("."));
    }
//    private static String getFileExtension(CommonsMultipartFile file){
//        String originalFileName = file.getOriginalFilename();
//        return originalFileName.substring(originalFileName.lastIndexOf("."));
//    }
}
