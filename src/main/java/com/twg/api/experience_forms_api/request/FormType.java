package com.twg.api.experience_forms_api.request;


import java.text.MessageFormat;

/**
 * Holds the form type information.
 * @author Juan Berrueta
 */
public enum FormType 
{
	Blank("B"), Sample("S"), Final("P"), None("N");
	
	private String code;
	
	private FormType(String code) 
	{
		this.code = code;
	}
	
	@Override
	public String toString() 
	{
		return this.code;
	}
	
	public static FormType find(String code) 
	{
		for (FormType type : FormType.values()) 
		{
			if (type.code.equals(code)) 
			{
				return type;
			}
		}
		throw new IllegalArgumentException(MessageFormat.format("No form type found with the provided code. [code={0}]", code));
	}
	
}
