package com.metasearch;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

import static org.assertj.core.api.Assertions.assertThat;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;
import javax.ws.rs.client.Client;

public class FlightMetasearchIntegrationTest {
	@Rule
	public final DropwizardAppRule<FlightMetasearchConfiguration> RULE = new DropwizardAppRule<FlightMetasearchConfiguration>(
			FlightMetasearchApplication.class, ResourceHelpers.resourceFilePath("config.yml"));

	/**
	 * Integration to test whether server is running or not 
	 * and to check if API is responding back to the call
	 */
	@Test
	public void runServerTest() {
		Client client = new JerseyClientBuilder().build();
		
		Response response = client.target(String.format("http://localhost:%d/search", RULE.getLocalPort())).request().post(null);
		
		String responseBody = response.readEntity(String.class);
		
		assertThat(response.getStatus()).isNotNull();
		assertThat(responseBody).isNotNull();
	}
}