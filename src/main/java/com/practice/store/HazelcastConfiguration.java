package com.practice.store;

import static com.practice.store.constants.BookStoreConstants.DEFAULT;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

@Configuration
public class HazelcastConfiguration {

	

	@Bean
	public HazelcastInstance hazelcastInstance() {
		Config config = new Config();
		config.setInstanceName("hazelcast-instance");
		config.getMapConfig(DEFAULT).setTimeToLiveSeconds(600); 
		return Hazelcast.newHazelcastInstance(config);
	}
}
