package com.metasearch.core;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * A class to get schedule's data.
 *
 * @author Usman Farooq
 */
@Entity
@Table(name="schedules")
@NamedQueries({
    @NamedQuery(
		name="Schedule.findByCodes", 
		query="select s from Schedule s where s.departureAirportCode = :departureCode and s.arrivalAirportCode = :arrivalCode"
    )
})
@IdClass(Schedule.class) 
public class Schedule implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "departure_airport_code")
    private String departureAirportCode;
	
	@Id
	@Column(name = "arrival_airport_code")
    private String arrivalAirportCode;
	
	@Id
	@Column(name = "provider_code")
    private String providerCode;
	
	@Column(name = "base_price")
    private Float basePrice;
    
    
	/**
	 * @return the departureAirportCode
	 */
	public String getDepartureAirportCode() {
		return departureAirportCode;
	}
	/**
	 * @return the arrivalAirportCode
	 */
	public String getArrivalAirportCode() {
		return arrivalAirportCode;
	}
	/**
	 * @return the provider_code
	 */
	public String getProviderCode() {
		return providerCode;
	}
	/**
	 * @return the basePrice
	 */
	public Float getBasePrice() {
		return basePrice;
	}
	/**
	 * @param departureAirportCode the departureAirportCode to set
	 */
	public void setDepartureAirportCode(String departureAirportCode) {
		this.departureAirportCode = departureAirportCode;
	}
	/**
	 * @param arrivalAirportCode the arrivalAirportCode to set
	 */
	public void setArrivalAirportCode(String arrivalAirportCode) {
		this.arrivalAirportCode = arrivalAirportCode;
	}
	/**
	 * @param providerCode the providerCode to set
	 */
	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}
	/**
	 * @param basePrice the basePrice to set
	 */
	public void setBasePrice(Float basePrice) {
		this.basePrice = basePrice;
	}
}
