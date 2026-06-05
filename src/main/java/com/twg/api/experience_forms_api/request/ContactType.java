package com.twg.api.experience_forms_api.request;


import java.text.MessageFormat;

/**
 * Holds the contact type information.
 * @author Juan Berrueta
 */
public enum ContactType 
{
	Buyer("B"), CoBuyer("CB"), Lienholder("L");
	private String code;
	private ContactType(String code) 
	{
		this.code = code;
	}
	
	@Override
	public String toString() 
	{
		return this.code;
	}
	
	public static ContactType find(String code) 
	{
		for (ContactType type : ContactType.values()) 
		{
			if (type.code.equals(code)) 
			{
				return type;
			}
		}
		throw new IllegalArgumentException(MessageFormat.format("No contact type found with the provided code. [code={0}]", code));
	}
	
}
