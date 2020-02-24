package com.bishe.o2o.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bishe.o2o.cache.JedisUtil;
import com.bishe.o2o.service.CacheService;

@Service
public class CacheServiceImpl implements CacheService {
	@Autowired
	private JedisUtil.Keys jedisKeys;

	@Override
	public void removeFromCache(String keyPrefix) {
		Set<String> keySet = jedisKeys.keys(keyPrefix + "*");//遍历，删除所有键值对
		for (String key : keySet) {
			jedisKeys.del(key);
		}
	}

}
