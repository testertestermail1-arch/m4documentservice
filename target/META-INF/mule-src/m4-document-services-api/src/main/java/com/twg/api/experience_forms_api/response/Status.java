package com.twg.api.experience_forms_api.response;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author sundaramurthyg
 *
 */
public class Status {
	protected boolean error;
	protected Set<Error> errors;
	
	/**
	 * Creates a status that represents that the call was processed correctly.
	 */
	public Status() {
		this.error = false;
		this.errors = new HashSet<Error>();
	}
	
	/**
	 * Creates a status representing one or more errors.
	 * @param errors the errors that occurred.
	 */
	public Status(Set<Error> errors) {
		this.error = true;
		this.errors = errors;
	}
	
}
