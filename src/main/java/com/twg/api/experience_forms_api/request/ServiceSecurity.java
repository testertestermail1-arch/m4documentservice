package com.twg.api.experience_forms_api.request;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.twg.api.experience_forms_api.DocumentError;

/**
 * Holds the service security information.<br>
 * This is not currently being used.
 * @author Juan Berrueta
 */
public class ServiceSecurity 
{
	private String token;
	private String username;
	
	/**
	 * Creates the service security.<br>
	 * This method may be required by the services framework.
	 */
	public ServiceSecurity() 
	{}
	
	public String getToken() 
	{
		return this.token;
	}
	
	public String getUsername() 
	{
		return this.username;
	}
	
	public String setToken(String token) 
	{
		return this.token=token;
	}
	
	public String setUsername(String username) 
	{
		return this.username=username;
	}
	
	/**
	 * Validates the security service.
	 * @param contentType
	 * @return serviceSecurityErrors
	 */
	public Map validate(String contentType)
	{
		Map<String,String> serviceSecurityErrors = new HashMap<String,String>();
		if (StringUtils.isBlank(this.username)) 
		{
			serviceSecurityErrors.put("username", "The calling app's username cannot be null, blank nor contain whitespaces only.");
		}
		if (StringUtils.isBlank(this.token)) 
		{
			serviceSecurityErrors.put("token", "The calling app's token cannot be null, blank nor contain whitespaces only.");
		}
		return serviceSecurityErrors;
	}
	
	@Override
	public String toString() 
	{
		return new ToStringBuilder(this).append("token", this.token).append("username", this.username).toString();
	}
	
}
