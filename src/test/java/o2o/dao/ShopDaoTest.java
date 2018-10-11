package o2o.dao;

import com.imooc.myo2o.BaseTest;
import com.imooc.myo2o.dao.ShopDao;
import com.imooc.myo2o.entity.Area;
import com.imooc.myo2o.entity.PersonInfo;
import com.imooc.myo2o.entity.Shop;
import com.imooc.myo2o.entity.ShopCategory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopDaoTest extends BaseTest {
    @Autowired
    private ShopDao shopDao;

    @Test
    public void testQueryShopList(){
        Shop shopCondition = new Shop();
        PersonInfo owner = new PersonInfo();
        owner.setUserId(8L);
        shopCondition.setOwner(owner);
        List<Shop> shopList = shopDao.queryShopList(shopCondition,0,5);
        int count = shopDao.queryShopCount(shopCondition);
        System.out.println("店铺列表大小:" + shopList.size());
        System.out.println("店铺总数 ：" + count);
        ShopCategory sc = new ShopCategory();
        sc.setShopCategoryId(14l);
        shopCondition.setShopCategory(sc);
        shopList = shopDao.queryShopList(shopCondition,0,2);
        System.out.println("店铺列表大小 ：" + shopList.size());
        count = shopDao.queryShopCount(shopCondition);
        System.out.println("店铺总数 ：" + count);

    }

    @Test
    @Ignore
    public void testQueryByShopId(){
        long shopId = 15;
        Shop shop = shopDao.queryByShopId(shopId);
        System.out.println("areaId:" + shop.getArea().getAreaId());
        System.out.println("areaName:" + shop.getArea().getAreaName());
    }

    @Test
    @Ignore
    public void TestInsertShop(){
        Shop shop = new Shop();
        shop.setOwnerId(1L);
        Area area = new Area();
        area.setAreaId(3L);
        ShopCategory sc = new ShopCategory();
        sc.setShopCategoryId(10L);
        shop.setShopName("mytest1");
        shop.setShopDesc("mytest1");
        shop.setShopAddr("testaddr1");
        shop.setPhone("13810524526");
        shop.setShopImg("test1");
        shop.setLongitude(1D);
        shop.setLatitude(1D);
        shop.setCreateTime(new Date());
        shop.setLastEditTime(new Date());
        shop.setEnableStatus(0);
        shop.setAdvice("审核中");
        shop.setArea(area);
        shop.setShopCategory(sc);
        int effectedNum = shopDao.insertShop(shop);
        assertEquals(1, effectedNum);
    }
    @Test
    @Ignore
    public void testUpdateShop(){
        Shop shop = new Shop();
        shop.setShopId(34L);
        shop.setShopDesc("测试描述");
        shop.setShopAddr("测试地址");
        int effectedNum = shopDao.updateShop(shop);
        assertEquals(1,effectedNum);
    }
}
