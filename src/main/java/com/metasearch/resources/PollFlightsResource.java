package com.metasearch.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.metasearch.common.Constant;
import com.metasearch.core.FlightPrice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.Size;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import javax.ws.rs.QueryParam;

@Path("/results")
@Api("/Flight Results")
@Consumes(MediaType.APPLICATION_JSON)
public class PollFlightsResource {

	@Inject
	public PollFlightsResource() {
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Retrieve search results against porvided search_id")
	public Response search(@QueryParam("search_id") @NotNull @Size(min = 10, max = 50) String searchId,
			@QueryParam("offset") Long cursor, @Context Jedis jedis) {
		/**
		 * Check if search id is valid and cached data is available
		 */
		String searchesCache = jedis.get(Constant.SEARCHES_CACHE_NAMESPACE + searchId);
		if (null == searchesCache) {
			throw new BadRequestException("Provided search id is not valid");
		}

		Map<String, Object> response = new HashMap<String, Object>();
		List<FlightPrice> priceList = new ArrayList<FlightPrice>();
		
		Long totalResults = jedis.llen(Constant.PRICES_CACHE_NAMESPACE + searchId);
		if (null == cursor)
			cursor = 0l;
		
		/**
		 * To avoid duplicated response, send count of total pulled records as cursor
		 * if search is still in progress then send has_more as true otherwise false
		 */
		if (totalResults.intValue() > 0 && cursor.compareTo(totalResults) < 0) {
			List<String> results = jedis.lrange(Constant.PRICES_CACHE_NAMESPACE + searchId, cursor, totalResults);
			for (String entity : results) {
				priceList.add(new FlightPrice(entity.split(":")));
			}
		}
		cursor = totalResults;
		response.put("offset", cursor);
		
		// Sort priceList in ascending order
		Collections.sort(priceList);
		
		response.put("data", priceList);
		if ("0".equals(searchesCache)) {
			response.put("has_more", false);
		} else {
			response.put("has_more", true);
		}

		return Response.status(Response.Status.OK).entity(response).build();
	}
}
