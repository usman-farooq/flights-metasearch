package com.metasearch.core;

/**
 * A class to construct prices response
 *
 * @author Usman Farooq
 */
public class FlightPrice implements Comparable<FlightPrice> {
	
	private String provider;
    private Integer price;
    
    public FlightPrice(String provider, Integer price) {
    	this.provider = provider;
    	this.price = price;
    }
    
    public FlightPrice(String[] cachedValue) {
    	this.provider = cachedValue[0];
    	this.price = Integer.valueOf(cachedValue[1]);
    }
    
	/**
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}
	/**
	 * @return the price
	 */
	public Integer getPrice() {
		return price;
	}
	/**
	 * @param provider the provider to set
	 */
	public void setProvider(String provider) {
		this.provider = provider;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(Integer price) {
		this.price = price;
	}
	
	@Override
	public int compareTo(FlightPrice fp) {
		return this.getPrice().compareTo(fp.getPrice());
	}
}
