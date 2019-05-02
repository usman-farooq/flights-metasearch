package com.metasearch.core;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * A class to get provider's data.
 *
 * @author Usman Farooq
 */
@Entity
@Table(name="providers")
@NamedQueries({
    @NamedQuery(name="Provider.findByCode", query="select p from Provider p where p.code = :code"),
    @NamedQuery(name="Provider.findByCodes", query="select p from Provider p where p.code in :codes")
})
public class Provider {
	
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
