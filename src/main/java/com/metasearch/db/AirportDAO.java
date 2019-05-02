package com.metasearch.db;

import com.metasearch.core.Airport;
import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import static java.util.Arrays.asList;
import org.hibernate.SessionFactory;

public class AirportDAO extends AbstractDAO<Airport> {
	
	public AirportDAO(final SessionFactory factory) {
		super(factory);
	}

	/**
	 * A method to retrieve airports from database.
	 * @param source Departure airport code
	 * @param destination Arrival airport code
	 * @return list of airports in the database base on source and destination airport.
	 */
	@SuppressWarnings("unchecked")
	public List<Airport> findAirportsByCode(String source, String destination) {
		return list(namedQuery("Airport.findByCodes").setParameter("codes",
				asList(source, destination)));
	}
}
