package com.twg.api.experience_forms_api.request;


import java.text.MessageFormat;

/**
 * Holds the phone type information.
 * @author Juan Berrueta
 */
public enum PhoneType 
{
	Home("H"), Work("W"), Mobile("M");
	private String code;
	private PhoneType(String code) 
	{
		this.code = code;
	}
	
	@Override
	public String toString() 
	{
		return this.code;
	}
	
	public static PhoneType find(String code) 
	{
		for (PhoneType type : PhoneType.values()) 
		{
			if (type.code.equals(code)) 
			{
				return type;
			}
		}
		throw new IllegalArgumentException(MessageFormat.format("No phone type found with the provided code. [code={0}]", code));
	}
}
