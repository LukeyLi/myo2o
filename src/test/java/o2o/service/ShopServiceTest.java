package o2o.service;

import com.imooc.myo2o.BaseTest;
import com.imooc.myo2o.dto.ImageHolder;
import com.imooc.myo2o.dto.ShopExecution;
import com.imooc.myo2o.entity.Area;
import com.imooc.myo2o.entity.Shop;
import com.imooc.myo2o.entity.ShopCategory;
import com.imooc.myo2o.enums.ShopStateEnum;
import com.imooc.myo2o.exceptions.ShopOperationException;
import com.imooc.myo2o.service.ShopService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ShopServiceTest extends BaseTest {
    @Autowired
    private ShopService shopService;
    @Test
	public void testGetShopList(){
		Shop shopCondition = new Shop();
		ShopCategory sc = new ShopCategory();
		sc.setShopCategoryId(14L);
		shopCondition.setShopCategory(sc);
		ShopExecution se = shopService.getShopList(shopCondition,1,2);
		System.out.println("店铺列表数：" + se.getShopList().size());
		System.out.println("店铺总数：" +  se.getCount());
	}
	@Test
	@Ignore
	public void testModifyShop()throws ShopOperationException,FileNotFoundException {
		Shop shop = new Shop();
		shop.setShopId(80L);
		shop.setShopName("测试修改店铺名称");
		File shopImg = new File("/upload/items/shop/80/test.png");
		InputStream is = new FileInputStream(shopImg);
		ImageHolder thumbnail = new ImageHolder(shopImg.getName(),is);
		ShopExecution shopExecution = shopService.modifyShop(shop, thumbnail);
		System.out.println("新的图片地址：" + shopExecution.getShop().getShopImg());
	}

    @Ignore
    @Test
	public void testAddShop() throws Exception {
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

		File shopImg = new File("E:\\index.jpg");
        InputStream is = new FileInputStream(shopImg);
		ImageHolder thumbnail = new ImageHolder(shopImg.getName(),is);
		ShopExecution se = shopService.addShop(shop,thumbnail);
		assertEquals(ShopStateEnum.CHECK.getState(),se.getState());
	}

}
