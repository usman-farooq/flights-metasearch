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

public class PollFlightsResourceTest {
	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	@Rule
	public final DropwizardAppRule<FlightMetasearchConfiguration> RULE = new DropwizardAppRule<FlightMetasearchConfiguration>(
			FlightMetasearchApplication.class, ResourceHelpers.resourceFilePath("config.yml"));

	/**
	 * Verify search API and results API responses
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void runValidSearchResultsBodyTest() {
		Client client = new JerseyClientBuilder().build();

		Response response = client.target(String.format("http://localhost:%d/search", RULE.getLocalPort()))
				.queryParam("departure_airport_code", "ADL").queryParam("arrival_airport_code", "JNB")
				.queryParam("departure_date", "2019-07-30").request().post(null);

		Map<String, String> responseMap = new HashMap<String, String>();
		
		String replyString = response.readEntity(String.class);
		try {
			responseMap = MAPPER.readValue(replyString, HashMap.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String searchId = responseMap.get("search_id");

		/**
		 * Verify that search request has been submitted and search id is given back to
		 * pull the search results
		 */
		assertThat(response.getStatus()).isEqualTo(HttpStatus.ACCEPTED_202);
		assertThat(searchId).isNotNull();

		Response results = client.target(String.format("http://localhost:%d/results", RULE.getLocalPort()))
				.queryParam("search_id", searchId).queryParam("offset", "0").request().get();

		Map<String, String> resultMap = new HashMap<String, String>();
		;
		String resultString = results.readEntity(String.class);
		try {
			resultMap = MAPPER.readValue(resultString, HashMap.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/**
		 * Verify that search results API is working and sending back data packet
		 */
		assertThat(results.getStatus()).isEqualTo(HttpStatus.OK_200);
		assertThat(resultMap).extracting("data").isNotNull();
	}
}