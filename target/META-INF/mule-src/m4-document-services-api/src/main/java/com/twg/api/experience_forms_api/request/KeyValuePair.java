package com.twg.api.experience_forms_api.request;


/**
 * Holds the key value pair information.<br>
 * This is not currently being used.
 * @author Juan Berrueta
 */

public class KeyValuePair
{
	
	private String key;
	private String value;
	/**
	 * Creates the value pair.<br>
	 * This method may be required by the services framework.
	 */
	public KeyValuePair() 
	{}

	public String getKey() 
	{
		return key;
	}

	public void setKey(String key) 
	{
		this.key = key;
	}

	public String getValue() 
	{
		return value;
	}

	public void setValue(String value) 
	{
		this.value = value;
	}
}
