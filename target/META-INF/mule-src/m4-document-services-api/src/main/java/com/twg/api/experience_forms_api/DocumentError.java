package com.twg.api.experience_forms_api;


import java.text.MessageFormat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


@XmlAccessorType(XmlAccessType.NONE)
public class DocumentError 
{
	private String key;
	private String message;

	protected DocumentError() {}
	

	public DocumentError(String key, String defaultMessage, String... parameters) 
	{
		this.key = key;
		// TODO: Internationalization of error messages.
		this.message = MessageFormat.format(defaultMessage, parameters);
	}
	
	public String getKey() 
	{
		return (this.key);
	}
	
	public String getMessage() 
	{
		return (this.message);
	}
	
}
