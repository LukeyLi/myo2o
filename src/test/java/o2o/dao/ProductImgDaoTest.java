package o2o.dao;

import com.imooc.myo2o.BaseTest;
import com.imooc.myo2o.dao.ProductImgDao;
import com.imooc.myo2o.entity.ProductImg;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductImgDaoTest extends BaseTest{
    @Autowired
    private ProductImgDao productImgDao;

    @Test

    public void testABatchInsertProductImg() throws Exception{
        ProductImg productImg1 = new ProductImg();
        productImg1.setImgAddr("图片1");
        productImg1.setImgDesc("测试图片1");
        productImg1.setPriority(1);
        productImg1.setCreateTime(new Date());
        productImg1.setProductId(15l);
        ProductImg productImg2 = new ProductImg();
        productImg2.setImgAddr("图片2");
        productImg2.setImgDesc("测试图片2");
        productImg2.setPriority(1);
        productImg2.setCreateTime(new Date());
        productImg2.setProductId(15l);
        List<ProductImg> productImgList = new ArrayList<ProductImg>();
        productImgList.add(productImg1);
        productImgList.add(productImg2);
        int effectedNum = productImgDao.batchInsertProductImg(productImgList);
        assertEquals(2,effectedNum);
    }
    @Test

    public void testBQueryProductImgList(){
        List<ProductImg> productImgList = productImgDao.queryProductImgList(15L);
        assertEquals(2,productImgList.size());
    }
    @Test

    public void testCDeleteProductImgByproductId(){
        long productId = 15l;
        int effectedNum = productImgDao.deleteProductImgByProductId(productId);
        assertEquals(2,effectedNum);
    }
}
