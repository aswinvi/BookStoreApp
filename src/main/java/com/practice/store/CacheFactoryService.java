package com.practice.store;

import static com.practice.store.constants.BookStoreConstants.BASKET_CACHE;

import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.practice.store.model.Basket;

@Service
public class CacheFactoryService {

	private final HazelcastInstance hazelcastInstance;

	public CacheFactoryService(HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
	}

	public Basket getBasketFromCache(String sessionID) {
		IMap<String, Basket> basketCache = hazelcastInstance.getMap(BASKET_CACHE);
		return basketCache.get(sessionID);
	}

	public void addBasketToCache(String sessionID, Basket basket) {
		IMap<String, Basket> basketCache = hazelcastInstance.getMap(BASKET_CACHE);
		basketCache.put(sessionID, basket);
	}

	public void deleteBasketFromCache(String sessionId) {
		IMap<String, Basket> basketCache = hazelcastInstance.getMap(BASKET_CACHE);
		basketCache.remove(sessionId);
	}

}