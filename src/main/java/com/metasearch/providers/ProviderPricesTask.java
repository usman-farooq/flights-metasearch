package com.metasearch.providers;

import java.util.logging.Logger;

import com.metasearch.common.Constant;
import com.metasearch.core.Schedule;

import redis.clients.jedis.Jedis;

public class ProviderPricesTask implements Runnable {
	private static final Logger LOG = Logger.getLogger(ProviderPricesTask.class.getName());

	private String searchId;
	private Schedule provider;
	private int scheduleNum;
	private String redisHost;
	private int redisPort;

	public ProviderPricesTask(String searchId, Schedule provider, int scheduleNum, String redisHost, int redisPort) {
		this.searchId = searchId;
		this.provider = provider;
		this.scheduleNum = scheduleNum;
		this.redisHost = redisHost;
		this.redisPort = redisPort;
	}

	public void run() {
		try {
			/**
			 * Calculate prices and save to Redis
			 */
			LOG.info("Processing prices for provider: " + provider.getProviderCode());
			int price = new Double(provider.getBasePrice() + (Math.random() * 0.3 * provider.getBasePrice())).intValue();
			
			Jedis jedis = new Jedis(redisHost, redisPort);
			jedis.rpush(Constant.PRICES_CACHE_NAMESPACE + searchId, provider.getProviderCode() + ":" + price);
			if(scheduleNum == 0) {
				jedis.expire(Constant.PRICES_CACHE_NAMESPACE + searchId, Constant.CACHE_EXPIRY_SECONDS);
			}
			jedis.decr(Constant.SEARCHES_CACHE_NAMESPACE + searchId);
			jedis.close();
			
		} catch (Exception e) {
			LOG.info("Exception while calculating flight prices: " + e.getMessage());
			e.printStackTrace();
		}
	}
}