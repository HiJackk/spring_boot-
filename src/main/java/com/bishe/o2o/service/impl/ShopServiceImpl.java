package com.bishe.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bishe.o2o.dao.ShopAuthMapDao;
import com.bishe.o2o.dao.ShopDao;
import com.bishe.o2o.dto.ImageHolder;
import com.bishe.o2o.dto.ShopExecution;
import com.bishe.o2o.entity.Shop;
import com.bishe.o2o.entity.ShopAuthMap;
import com.bishe.o2o.enums.ShopStateEnum;
import com.bishe.o2o.exceptions.ShopOperationException;
import com.bishe.o2o.service.ShopService;
import com.bishe.o2o.util.ImageUtil;
import com.bishe.o2o.util.PageCalculator;
import com.bishe.o2o.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {
	@Autowired
	private ShopDao shopDao;
	@Autowired
	private ShopAuthMapDao shopAuthMapDao;

	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		// PageCalculator将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的店铺列表
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		// 依据相同的查询条件，返回店铺总数
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution se = new ShopExecution();
		if (shopList != null) {
			se.setShopList(shopList);
			se.setCount(count);
		} else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}

	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);
	}

	@Override
	@Transactional
	public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {
		if (shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		} else {
			// 1.判断是否需要处理图片
			try {
				if (thumbnail != null && thumbnail.getImage() != null && thumbnail.getImageName() != null
						&& !"".equals(thumbnail.getImageName())) {
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					if (tempShop.getShopImg() != null) {
						ImageUtil.deleteFileOrPath(tempShop.getShopImg());
					}
					addShopImg(shop, thumbnail);
				}
				// 2.更新店铺信息
				shop.setLastEditTime(new Date());
				int effectedNum = shopDao.updateShop(shop);
				if (effectedNum <= 0) {
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				} else {
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS, shop);
				}
			} catch (Exception e) {
				throw new ShopOperationException("modifyShop error:" + e.getMessage());
			}
		}
	}

	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {
		// 空值判断
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			// 给店铺信息赋初始值
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			// 添加店铺信息
			int effectedNum = shopDao.insertShop(shop);
			if (effectedNum <= 0) {
				throw new ShopOperationException("店铺创建失败");
			} else {
				if (thumbnail.getImage() != null) {
					// 存储图片
					try {
						addShopImg(shop, thumbnail);
					} catch (Exception e) {
						throw new ShopOperationException("addShopImg error:" + e.getMessage());
					}
					// 更新店铺的图片地址
					effectedNum = shopDao.updateShop(shop);
					if (effectedNum <= 0) {
						throw new ShopOperationException("更新图片地址失败");
					}
					// 执行增加shopAuthMap操作
					ShopAuthMap shopAuthMap = new ShopAuthMap();
					shopAuthMap.setEmployee(shop.getOwner());
					shopAuthMap.setShop(shop);
					shopAuthMap.setTitle("店家");
					shopAuthMap.setTitleFlag(0);
					shopAuthMap.setCreateTime(new Date());
					shopAuthMap.setLastEditTime(new Date());
					shopAuthMap.setEnableStatus(1);
					try {
						effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
						if (effectedNum <= 0) {
							throw new ShopOperationException("授权创建失败");
						} 
					} catch (Exception e) {
						throw new ShopOperationException("insertShopAuthMap error: " + e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			throw new ShopOperationException("addShop error:" + e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK, shop);
	}

	private void addShopImg(Shop shop, ImageHolder thumbnail) {
		// 获取shop图片目录的相对值路径
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		shop.setShopImg(shopImgAddr);
	}
}
