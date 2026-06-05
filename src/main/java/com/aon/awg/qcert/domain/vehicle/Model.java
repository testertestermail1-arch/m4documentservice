/*
 * Created on Dec 22, 2005
 * $Id: Model.java 1001 2007-02-12 20:20:37Z santiago.vacas $
 */
package com.aon.awg.qcert.domain.vehicle;

import java.io.Serializable;


public class Model implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
	private String description;
	private String code;
	private Make make;

	public Model() {
	}
	
	public Model(long _id) {
	    id = _id;
	}
	
	public Model(Make _make) {
	    make = _make;
	}

	public long getId() {
		return id;
	}
	public void setId(long _id) {
		id = _id;
	}
	

	public String getCode() {
		return code;
	}
	public void setCode(String _code) {
		code = _code;
	}
	

	public String getDescription() {
		return description;
	}
	public void setDescription(String _description) {
		description = _description;
	}
	

	public Make getMake() {
		return make;
	}
	public void setMake(Make _make) {
		make = _make;
	}
	
    public boolean equals(Object _obj) {
        if (_obj==this)
            return true;
        if (!(_obj instanceof Model))
            return false;
        Model that = (Model)_obj;
        return this.getId() == that.getId();
    }
    
    public int hashCode() {
        return (int) getId();
    }
    
    public String toString() {
        return getDescription();
    }
}
