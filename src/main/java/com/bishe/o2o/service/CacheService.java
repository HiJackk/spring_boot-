package com.bishe.o2o.service;

public interface CacheService {
	/**更新redis数据时，需要先移除相关数据
	 * 依据key前缀删除匹配该模式下的所有key-value 如传入:shopcategory,则shopcategory_allfirstlevel等
	 * 以shopcategory打头的key_value都会被清空
	 * 
	 * @param keyPrefix
	 * @return
	 */
	void removeFromCache(String keyPrefix);
}
