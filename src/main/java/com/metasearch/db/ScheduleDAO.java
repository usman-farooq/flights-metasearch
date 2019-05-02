package com.metasearch.db;

import com.metasearch.core.Schedule;
import java.util.List;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class ScheduleDAO extends AbstractDAO<Schedule> {
	
	public ScheduleDAO(final SessionFactory factory) {
		super(factory);
	}

	/**
	 * A method to retrieve schedules from database.
	 * @param departure Departure airport code
	 * @param arrival Arrival airport code
	 * @return list of schedules in the database based on departure and arrival airports.
	 */
	@SuppressWarnings("unchecked")
	public List<Schedule> getSchedulesByCode(String departure, String arrival) {
		return list(namedQuery("Schedule.findByCodes").setParameter("departureCode", departure)
				.setParameter("arrivalCode", arrival));
	}
}
