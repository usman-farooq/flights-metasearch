package com.metasearch.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.metasearch.common.Constant;
import com.metasearch.core.Airport;
import com.metasearch.core.Schedule;
import com.metasearch.db.AirportDAO;
import com.metasearch.db.ScheduleDAO;
import com.metasearch.providers.ProviderPricesTask;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import javax.ws.rs.QueryParam;
import javax.ws.rs.BadRequestException;

@Path("/search")
@Api("/Flight Search")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SearchFlightsResource {

	ThreadPoolExecutor priceCollector = (ThreadPoolExecutor) Executors.newFixedThreadPool(Constant.THREAD_POOL_SIZE);

	private AirportDAO airportDAO;
	private ScheduleDAO scheduleDAO;

	/**
	 * A constructor with dao DAO that supports CRUD methods.
	 * 
	 * @param airportDAO airport dao object
	 * @param scheduleDAO schedule dao object
	 */
	public SearchFlightsResource(AirportDAO airportDAO, ScheduleDAO scheduleDAO) {
		this.airportDAO = airportDAO;
		this.scheduleDAO = scheduleDAO;
	}

	@POST
	@UnitOfWork
	@ApiOperation("Search flight fares for given departure and arrival airports. Expect status code 202 (Accepted) for successfull search request")
	public Response search(
			@QueryParam("departure_airport_code") @NotNull @Size(min = 3, max = 3) String departureAirportCode,
			@QueryParam("arrival_airport_code") @NotNull @Size(min = 3, max = 3) String arrivalAirportCode,
			@QueryParam("departure_date") @NotNull @Size(min = 10, max = 10) String departureDate,
			@Context Jedis jedis) {
		/**
		 * Departure and arrival airport code should not be same
		 */
		if (departureAirportCode.equalsIgnoreCase(arrivalAirportCode)) {
			throw new BadRequestException("Departure and arrival airport can't be same");
		}

		/**
		 * Check if departure and arrival airport codes are supported
		 */
		List<Airport> airports = airportDAO.findAirportsByCode(departureAirportCode, arrivalAirportCode);
		if (airports.size() != 2) {
			throw new BadRequestException("Departure/arrival airports are not supported");
		}

		/**
		 * Get all flight schedules for departure and arrival airport codes At least one
		 * scheduled flight should be available for the route
		 */
		List<Schedule> routeSchedules = scheduleDAO.getSchedulesByCode(departureAirportCode, arrivalAirportCode);
		if (routeSchedules.isEmpty()) {
			throw new BadRequestException("Flights for this route are not available");
		}

		/**
		 * Generate Search Id for flights search
		 */
		Map<String, String> response = new HashMap<>();
		String searchId = UUID.randomUUID().toString();
		response.put("search_id", searchId);

		/**
		 * Cache searchId with count of schedules to track priceCollector thread pool
		 */
		jedis.incrBy(Constant.SEARCHES_CACHE_NAMESPACE + searchId, routeSchedules.size());
		jedis.expire(Constant.SEARCHES_CACHE_NAMESPACE + searchId, Constant.CACHE_EXPIRY_SECONDS);

		/**
		 * Asynchronous request to all providers to collect prices
		 */
		int scheduleNum = 0;
		for (Schedule schedule : routeSchedules) {
			priceCollector.execute(new ProviderPricesTask(searchId, schedule, scheduleNum, jedis.getClient().getHost(), jedis.getClient().getPort()));
			scheduleNum ++;
		}

		return Response.status(Response.Status.ACCEPTED).entity(response).build();
	}
}
