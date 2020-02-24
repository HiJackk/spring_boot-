package com.bishe.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bishe.o2o.entity.PersonInfo;
import com.bishe.o2o.entity.Product;
import com.bishe.o2o.entity.Shop;
import com.bishe.o2o.entity.UserProductMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserProductMapDaoTest {
	@Autowired
	private UserProductMapDao userProductMapDao;

	/**
	 * 测试添加功能
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAInsertUserProductMap() throws Exception {
		// 创建用户商品映射信息1
		UserProductMap userProductMap = new UserProductMap();
		PersonInfo customer = new PersonInfo();
		customer.setUserId(1L);
		userProductMap.setUser(customer);
		userProductMap.setOperator(customer);
		Product product = new Product();
		product.setProductId(1L);
		userProductMap.setProduct(product);
		Shop shop = new Shop();
		shop.setShopId(29L);
		userProductMap.setShop(shop);
		userProductMap.setCreateTime(new Date());
		int effectedNum = userProductMapDao.insertUserProductMap(userProductMap);
		// 创建用户商品映射信息2
		UserProductMap userProductMap2 = new UserProductMap();
		userProductMap2.setUser(customer);
		userProductMap2.setOperator(customer);
		Product product2 = new Product();
		product2.setProductId(1L);
		userProductMap2.setProduct(product2);
		Shop shop2 = new Shop();
		shop2.setShopId(29L);
		userProductMap2.setShop(shop2);
		userProductMap2.setCreateTime(new Date());
		effectedNum = userProductMapDao.insertUserProductMap(userProductMap2);
		assertEquals(1, effectedNum);
		// 创建用户商品映射信息3
		UserProductMap userProductMap3 = new UserProductMap();
		userProductMap3.setUser(customer);
		userProductMap3.setOperator(customer);
		Product product3 = new Product();
		product3.setProductId(2L);
		userProductMap3.setProduct(product3);
		Shop shop3 = new Shop();
		shop3.setShopId(29L);
		userProductMap3.setShop(shop3);
		userProductMap3.setCreateTime(new Date());
		effectedNum = userProductMapDao.insertUserProductMap(userProductMap3);
		assertEquals(1, effectedNum);
		// 创建用户商品映射信息4
		UserProductMap userProductMap4 = new UserProductMap();
		userProductMap4.setUser(customer);
		userProductMap4.setOperator(customer);
		Product product4 = new Product();
		product4.setProductId(15L);
		userProductMap4.setProduct(product4);
		Shop shop4 = new Shop();
		shop4.setShopId(28L);
		userProductMap4.setShop(shop4);
		userProductMap4.setCreateTime(new Date());
		effectedNum = userProductMapDao.insertUserProductMap(userProductMap4);
		assertEquals(1, effectedNum);
	}

	/**
	 * 测试查询功能
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBQueryUserProductMap() throws Exception {
		UserProductMap userProductMap = new UserProductMap();
		PersonInfo customer = new PersonInfo();
		// 按顾客名字模糊查询
		customer.setName("测试");
		userProductMap.setUser(customer);
		List<UserProductMap> userProductMapList = userProductMapDao.queryUserProductMapList(userProductMap, 0, 2);
		assertEquals(2, userProductMapList.size());
		int count = userProductMapDao.queryUserProductMapCount(userProductMap);
		assertEquals(4, count);
		// 叠加店铺去查询
		Shop shop = new Shop();
		shop.setShopId(29L);
		userProductMap.setShop(shop);
		userProductMapList = userProductMapDao.queryUserProductMapList(userProductMap, 0, 3);
		assertEquals(3, userProductMapList.size());
		count = userProductMapDao.queryUserProductMapCount(userProductMap);
		assertEquals(3, count);
	}
}
