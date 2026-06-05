package com.twg.api.experience_forms_api.request;


import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.twg.api.experience_forms_api.DocumentError;

/**
 * Holds the form information.
 * @author Juan Berrueta
 */

public class Phone 
{
	private String type;
	private PhoneType typeEnum;
	private String number;
	private Integer areaCode;
	
	/**
	 * Creates the phone.<br>
	 * This method may be required by the services framework.
	 */
	public Phone() 
	{}
	
	public String getType() 
	{
		return this.type;
	}
	
	public PhoneType getTypeEnum() 
	{
		return this.typeEnum;
	}
	
	public String getNumber() 
	{
		return this.number;
	}
	
	public Integer getAreaCode() 
	{
		return this.areaCode;
	}
	
	/**
	 * Validates the phone.<br>
	 * The phone is invalid if any of the following occur:
	 * <ul>
	 * <li>the phone type is null or invalid</li>
	 * <li>the number is null, blank or contains only whitespaces</li>
	 * </ul>
	 * @param errors the errors list to fulfill.
	 */
	
	public void validate(Set<DocumentError> errors) 
	{
		try 
		{
			this.typeEnum = PhoneType.find(this.type);
		}
		catch (Exception exception) 
		{
			errors.add(new DocumentError("phone type", "The phone type should be one of the following values: {0}.", ArrayUtils.toString(PhoneType.values())));
		}
	}
	
	@Override
	public String toString() 
	{
		return new ToStringBuilder(this).append("type", this.type).append("number", this.number).append("areaCode", this.areaCode).toString();
	}

	public void setNumber(String number) 
	{
		// TODO Auto-generated method stub
		this.number=number;
	}

	public void setAreaCode(Integer areaCode) 
	{
		// TODO Auto-generated method stub
		this.areaCode=areaCode;
	}

	public void setType(String type) 
	{
		// TODO Auto-generated method stub
		this.type=type;
		this.typeEnum = PhoneType.find(type);
	}
	
}
