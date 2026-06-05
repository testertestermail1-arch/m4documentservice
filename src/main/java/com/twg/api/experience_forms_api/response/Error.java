package com.twg.api.experience_forms_api.response;

import java.text.MessageFormat;

/**
 * 
 * @author sundaramurthyg
 *
 */
public class Error {
	private String key;
	private String message;
	
	/**
	 * Creates the error.
	 */
	protected Error() {}
	
	/**
	 * Creates the error.
	 * @param key the error's key.
	 * @param defaultMessage the error's default message.
	 * @param parameters the parameters to use while formatting the message.
	 */
	public Error(String key, String defaultMessage, String... parameters) {
		this.key = key;
		// TODO: Internationalization of error messages.
		this.message = MessageFormat.format(defaultMessage, parameters);
	}
	
	public String getKey() {
		return (this.key);
	}
	
	public String getMessage() {
		return (this.message);
	}
	
}
