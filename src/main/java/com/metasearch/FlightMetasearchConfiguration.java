package com.metasearch;

import io.dropwizard.Configuration;

import com.bendb.dropwizard.redis.JedisFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import javax.validation.constraints.*;
import javax.validation.Valid;

public class FlightMetasearchConfiguration extends Configuration {
	@NotNull
	@JsonProperty
	private JedisFactory redis;

	@Valid
	@NotNull
	private DataSourceFactory database = new DataSourceFactory();
	
	@JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

	@JsonProperty("database")
	public DataSourceFactory getDataSourceFactory() {
		return database;
	}

	public void setDatabase(DataSourceFactory database) {
		this.database = database;
	}

	public JedisFactory getJedisFactory() {
		return redis;
	}

	public void setJedisFactory(JedisFactory jedisFactory) {
		this.redis = jedisFactory;
	}

	/**
	 * @return the swaggerBundleConfiguration
	 */
	public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
		return swaggerBundleConfiguration;
	}

	/**
	 * @param swaggerBundleConfiguration the swaggerBundleConfiguration to set
	 */
	public void setSwaggerBundleConfiguration(SwaggerBundleConfiguration swaggerBundleConfiguration) {
		this.swaggerBundleConfiguration = swaggerBundleConfiguration;
	}
	
}
