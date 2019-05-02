package com.metasearch.core;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * A class to get airport's data.
 *
 * @author Usman Farooq
 */
@Entity
@Table(name="airports")
@NamedQueries({
    @NamedQuery(name="Airport.findByCodes", query="select a from Airport a where a.code in :codes")
})
public class Airport {
	
	@Id
    private String code;
    private String name;
    
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
    
}
