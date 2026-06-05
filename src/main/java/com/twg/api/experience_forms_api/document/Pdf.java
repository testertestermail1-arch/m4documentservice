package com.twg.api.experience_forms_api.document;


import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Holds the pdf response information.
 * @author Juan Berrueta
 */
@XmlAccessorType(XmlAccessType.NONE)
public class Pdf 
{
	private String type;
	private String url;
	private byte[] pdf;
	private String number;
	private Date issued;
	protected Pdf() {}
	public Pdf(String type, String url, byte[] pdf, String number, Date issued) 
	{
		this.type = type;
		this.url = url;
		this.pdf = pdf;
		this.number = number;
		this.issued = issued;
	}
	
	public Pdf(String type, String url, String number, Date issued) 
	{
		this.type = type;
		this.url = url;
		this.pdf = null;
		this.number = number;
		this.issued = issued;
	}
	
}
