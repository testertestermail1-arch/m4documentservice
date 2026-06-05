/*
 * Created on Dec 22, 2005
 * $Id: Make.java 1001 2007-02-12 20:20:37Z santiago.vacas $
 */
package com.aon.awg.qcert.domain.vehicle;

import java.io.Serializable;


public class Make implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
	private String description;
	private String code;


	public Make() {
	}
	
	public Make(String _desc, String _code) {
		description = _desc;
		code = _code;
	}
	

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}


	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
    public boolean equals(Object _obj) {
        if (_obj==this)
            return true;
        if (!(_obj instanceof Make))
            return false;
        Make that = (Make)_obj;
        return this.getId() == that.getId();
    }
    
    public int hashCode() {
        return (int) getId();
    }

    public String toString() {
        return getDescription();
    }
}
