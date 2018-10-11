package o2o;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.myo2o.entity.Product;

import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        System.out.println(test());
    }
    public static int test(){
        String productStr ="{\"productName\":\"测试数据的商品\",\"productDesc\":\"测试数据的商品\",\"priority\":\"66\",\"normalPrice\":\"80\",\"promotionPrice\":\"100\",\"productCategory\":{\"productCategoryId\":23},\"productId\":\"16\"}";
        ObjectMapper mapper = new ObjectMapper();
        try {
            Product product = mapper.readValue(productStr,Product.class);
            System.out.println(product.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
