package com.metasearch;

import com.bendb.dropwizard.redis.JedisBundle;
import com.bendb.dropwizard.redis.JedisFactory;
import com.metasearch.core.Airport;
import com.metasearch.core.Provider;
import com.metasearch.core.Schedule;
import com.metasearch.db.AirportDAO;
import com.metasearch.db.ScheduleDAO;
import com.metasearch.resources.PollFlightsResource;
import com.metasearch.resources.SearchFlightsResource;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

/**
 * Application's main class
 *
 * @author Usman Farooq
 */
public class FlightMetasearchApplication extends Application<FlightMetasearchConfiguration> {

	/**
	 * Hibernate bundle
	 */
	private final HibernateBundle<FlightMetasearchConfiguration> hibernateBundle = new HibernateBundle<FlightMetasearchConfiguration>(
			Airport.class, Provider.class, Schedule.class) {
		
		@Override
		public DataSourceFactory getDataSourceFactory(FlightMetasearchConfiguration configuration) {
			return configuration.getDataSourceFactory();
		}
	};

	public static void main(final String[] args) throws Exception {
		new FlightMetasearchApplication().run(args);
	}

	@Override
	public String getName() {
		return "FlightMetasearch";
	}

	@Override
	public void initialize(final Bootstrap<FlightMetasearchConfiguration> bootstrap) {
		// Add Migration bundle
		bootstrap.addBundle(new MigrationsBundle<FlightMetasearchConfiguration>() {
			@Override
			public DataSourceFactory getDataSourceFactory(FlightMetasearchConfiguration configuration) {
				return configuration.getDataSourceFactory();
			}
		});

		// Add Hibernate bundle
		bootstrap.addBundle(hibernateBundle);

		// Add Jedis bundle
		bootstrap.addBundle(new JedisBundle<FlightMetasearchConfiguration>() {
			@Override
			public JedisFactory getJedisFactory(FlightMetasearchConfiguration configuration) {
				return configuration.getJedisFactory();
			}
		});
		
		// Add Swagger bundle
		bootstrap.addBundle(new SwaggerBundle<FlightMetasearchConfiguration>() {
	        @Override
	        protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(FlightMetasearchConfiguration configuration) {
	            return configuration.swaggerBundleConfiguration;
	        }
	    });
	}

	@Override
	public void run(final FlightMetasearchConfiguration configuration, final Environment environment) {

		// Airports DAO is created
        final AirportDAO airportDAO = new AirportDAO(hibernateBundle.getSessionFactory());
        
        // Schedules DAO is created
        final ScheduleDAO scheduleDAO = new ScheduleDAO(hibernateBundle.getSessionFactory());
        
        // The resource to manipulate flight searches is registered.
        // airport and schedule DAO are passed as an argument.
		environment.jersey().register(new SearchFlightsResource(airportDAO, scheduleDAO));
		environment.jersey().register(new PollFlightsResource());
	}

}
