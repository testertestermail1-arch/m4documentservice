package com.twg.api.experience_forms_api.request;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.twg.api.experience_forms_api.DocumentError;

/**
 * Holds the calling app information.
 * @author Juan Berrueta
 */

public class CallingApp 
{
	private String appId;
	private boolean persist;
	private String sourceSystem;
	private String businessUnit;
	private ServiceSecurity serviceSecurity;
	
	/**
	 * Creates the calling app.<br>
	 * This method may be required by the services framework.
	 */
	public CallingApp() 
	{}
	
	public String getAppId() 
	{
		return this.appId;
	}
	
	public ServiceSecurity getServiceSecurity() 
	{
	return this.serviceSecurity;
	}
	
	public boolean isPersist() 
	{
		return this.persist;
	}
	
	public boolean getPersist() 
	{
		return this.persist;
	}
	
	public String getSourceSystem() 
	{
		return this.sourceSystem;
	}
	
	public String getBusinessUnit() 
	{
		return this.businessUnit;
	}
	
	/**
	 * Validates the calling app.<br>
	 * The calling app is invalid if any of the following occur:
	 * <ul>
	 * <li>the appId is null, blank or contains whitespaces only</li>
	 * <li>the sourceSystem is null, blank or contains whitespaces only</li>
	 * <li>the businessUnit is blank or contains whitespaces only</li>
	 * </ul>
	 * @param contentType
	 * @return callinAppErrors
	 */
	
	public Map validate(String contentType) 
	{
		Map<String,String> callinAppErrors = new HashMap<String,String>();
		if (StringUtils.isBlank(this.appId) && contentType.equals("XML")) 
		{
			callinAppErrors.put("callingApp appId", "The calling app id cannot be null, blank nor contain whitespaces only.");
		}
		if (StringUtils.isBlank(this.sourceSystem)) 
		{
			callinAppErrors.put("callingApp sourceSystem", "The calling app's source system cannot be null, blank nor contain whitespaces only.");
		}
		if (StringUtils.isWhitespace(this.businessUnit) && contentType.equals("XML")) 
		{
			callinAppErrors.put("callingApp businessUnit", "The calling app's business unit cannot be blank nor contain whitespaces only.");
		}
		return callinAppErrors;
	}
	
	@Override
	public String toString() 
	{
		return new ToStringBuilder(this).append("serviceSecurity", this.serviceSecurity).append("appId", this.appId).append("persist", this.persist)
				.append("sourceSystem", this.sourceSystem).append("businessUnit", this.businessUnit).toString();
	}

	public void setSourceSystem(String sourceSystem) 
	{
		// TODO Auto-generated method stub
		this.sourceSystem=sourceSystem;
	}

	public void setAppId(String appId) 
	{
		// TODO Auto-generated method stub
		this.appId=appId;
	}

	public void setPersist(boolean persist) 
	{
		// TODO Auto-generated method stub
		this.persist=persist;
	}
	
	public void setServiceSecurity(ServiceSecurity serviceSecurity) 
	{
		this.serviceSecurity=serviceSecurity;
	}
}
