package com.twg.api.experience_forms_api.request;


import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.twg.api.experience_forms_api.DocumentError;

/**
 * Holds the address information.
 * @author Juan Berrueta
 */

public class Address 
{
	private String address1;
	private String address2;
	private String postalCode;
	private String city;
	private String stateCode;
	private String countryCode;

	/**
	 * Creates the address.<br>
	 * This method may be required by the services framework.
	 */
	public Address() 
	{}
	
	public String getAddress1() 
	{
		return this.address1;
	}
	
	public String getAddress2() 
	{
		return this.address2;
	}
	
	public String getPostalCode() 
	{
		return this.postalCode;
	}
	
	public String getCity() 
	{
		return this.city;
	}
	
	public String getStateCode() 
	{
		return this.stateCode;
	}
	
	public String getCountryCode() 
	{
		return this.countryCode;
	}
	
	public String getAddressString() 
	{
		if (this.address2 != null && this.address1 !=null) 
		{
			return MessageFormat.format("{0} {1}", this.address1, this.address2).trim();
		}
		else if (this.address1 !=null)
		{
			return this.address1.trim();
		}
		else 
			return "";
	}
	
	public String getCompleteAddressString() 
	{
		StringBuffer completeAddress = new StringBuffer();
		completeAddress.append(this.address1 != null ? this.address1.trim() : "").append(this.address2 != null ? " " : "");
		completeAddress.append(this.address2 != null ? this.address2.trim() : "").append(", ");
		completeAddress.append(this.city != null ? this.city.trim() : "").append(", CP. ");
		completeAddress.append(this.postalCode != null ? this.postalCode.trim() : "").append(", ");
		completeAddress.append(this.stateCode != null ? this.stateCode.trim() : "").append(", ");
		completeAddress.append(this.countryCode != null ? this.countryCode.trim() : "");
		return completeAddress.toString().trim();
	}
	
	/**
	 * Validates the address.<br>
	 * The address is invalid if any of the following occur:
	 * <ul>
	 * <li>the address1 is null, blank or contains whitespaces only</li>
	 * <li>the address2 is blank or contains whitespaces only</li>
	 * <li>the postalCode is null, blank or contains whitespaces only</li>
	 * <li>the city is null, blank or contains whitespaces only</li>
	 * <li>the stateCode is null, blank or contains whitespaces only</li>
	 * <li>the countryCode is null, blank or contains whitespaces only</li>
	 * </ul>
	 * @param contentType the errors list to fulfill.
	 * @return Map 
	 */
	public Map validate(String contentType) 
	{
		Map<String,String> addressErrors = new HashMap<String,String>(); 
		if (StringUtils.isBlank(this.address1)) 
		{
			addressErrors.put("address1", "The address' address1 cannot be null, blank nor contain whitespaces only.");
		}
		if (StringUtils.isBlank(this.postalCode)) 
		{
			addressErrors.put(StringUtils.equals(contentType, "JSON")?"postalCode":"zip", "The address postalCode cannot be null, blank nor contain whitespaces only.");
		}
		if (StringUtils.isBlank(this.city)) 
		{
			addressErrors.put(StringUtils.equals(contentType, "JSON")?"city":"city", "The address city cannot be null, blank nor contain whitespaces only.");
		}
		if (StringUtils.isBlank(this.stateCode)) 
		{
			addressErrors.put(StringUtils.equals(contentType, "JSON")?"stateCode":"state", "The address stateCode cannot be null, blank nor contain whitespaces only.");
		}
	return addressErrors;
	}
	
	@Override
	public String toString() 
	{
		return new ToStringBuilder(this).append("address1", this.address1).append("address2", this.address2).append("postalCode", this.postalCode).append("city", this.city)
				.append("stateCode", this.stateCode).append("countryCode", this.countryCode).toString();
	}

	public void setAddress1(String address1) 
	{
		// TODO Auto-generated method stub
		this.address1=address1;
	}

	public void setAddress2(String address2) 
	{
		// TODO Auto-generated method stub
		this.address2=address2;
	}

	public void setPostalCode(String postalCode) 
	{
		// TODO Auto-generated method stub
		this.postalCode=postalCode;
	}

	public void setCity(String city) 
	{
		// TODO Auto-generated method stub
		this.city=city;
	}

	public void setStateCode(String stateCode) 
	{
		// TODO Auto-generated method stub
		this.stateCode=stateCode;
	}

	public void setCountryCode(String countryCode) 
	{
		// TODO Auto-generated method stub
		this.countryCode=countryCode;
	}
	
}
