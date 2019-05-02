package com.metasearch.resources;

import com.metasearch.FlightMetasearchConfiguration;
import com.metasearch.FlightMetasearchApplication;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Rule;
import org.junit.Test;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import io.dropwizard.jackson.Jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

public class SearchFlightsResourceTest {
	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	@Rule
	public final DropwizardAppRule<FlightMetasearchConfiguration> RULE = new DropwizardAppRule<FlightMetasearchConfiguration>(
			FlightMetasearchApplication.class, ResourceHelpers.resourceFilePath("config.yml"));

	/**
	 * Verify that request validation is working fine
	 * 
	 * If departure airport code and arrival airport code are same then API should
	 * return error message
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void runRequestValidationTest() {
		Client client = new JerseyClientBuilder().build();

		Response response = client.target(String.format("http://localhost:%d/search", RULE.getLocalPort()))
				.queryParam("departure_airport_code", "ADL").queryParam("arrival_airport_code", "ADL")
				.queryParam("departure_date", "2019-07-30").request().post(null);

		Map<String, String> responseMap = new HashMap<String, String>();
		;
		String replyString = response.readEntity(String.class);
		try {
			responseMap = MAPPER.readValue(replyString, HashMap.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST_400);
		assertThat(responseMap.get("message")).isEqualTo("Departure and arrival airport can't be same");
	}

	/**
	 * Verify that request validation is working fine
	 * 
	 * If dummy airport codes are entered then give error message that provided
	 * airports are not supported
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void runNotSupportedAirportsTest() {
		Client client = new JerseyClientBuilder().build();

		Response response = client.target(String.format("http://localhost:%d/search", RULE.getLocalPort()))
				.queryParam("departure_airport_code", "AAA").queryParam("arrival_airport_code", "BBB")
				.queryParam("departure_date", "2019-07-30").request().post(null);

		Map<String, String> responseMap = new HashMap<String, String>();
		;
		String replyString = response.readEntity(String.class);
		try {
			responseMap = MAPPER.readValue(replyString, HashMap.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST_400);
		assertThat(responseMap.get("message")).isEqualTo("Departure/arrival airports are not supported");
	}

	/**
	 * Verify search API is working fine
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void runSearchInitiatedTest() {
		Client client = new JerseyClientBuilder().build();

		Response response = client.target(String.format("http://localhost:%d/search", RULE.getLocalPort()))
				.queryParam("departure_airport_code", "ADL").queryParam("arrival_airport_code", "JNB")
				.queryParam("departure_date", "2019-07-30").request().post(null);

		Map<String, String> responseMap = new HashMap<String, String>();
		;
		String replyString = response.readEntity(String.class);
		try {
			responseMap = MAPPER.readValue(replyString, HashMap.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String searchId = responseMap.get("search_id");

		assertThat(response.getStatus()).isEqualTo(HttpStatus.ACCEPTED_202);
		assertThat(searchId).isNotNull();
	}
}