package com.practice.store;

import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.practice.store.model.Basket;

@Service
public class CacheFactoryService {

	    private final HazelcastInstance hazelcastInstance;

	    public CacheFactoryService (HazelcastInstance hazelcastInstance) {
	        this.hazelcastInstance = hazelcastInstance;
	    }

	    public Basket getBasketFromCache(String sessionID) {
	        IMap<String, Basket> basketCache = hazelcastInstance.getMap("basketCache");
	        return basketCache.get(sessionID);
	    }

	    public void addBasketToCache(String sessionID, Basket basket) {
	        IMap<String, Basket> basketCache = hazelcastInstance.getMap("basketCache");
	        basketCache.put(sessionID, basket); 
	    }
	    
	    public void deleteBasketFromCache(String sessionId) {
	        IMap<String, Basket> basketCache = hazelcastInstance.getMap("basketCache");
	        basketCache.remove(sessionId); 
	    }

	}