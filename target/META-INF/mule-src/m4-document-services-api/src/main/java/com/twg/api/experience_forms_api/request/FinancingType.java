package com.twg.api.experience_forms_api.request;


import java.text.MessageFormat;

public enum FinancingType 
{
	Loan("L"), Lease("LE"), Cash("C"),OtherLoan("LO"),Balloon("BL"), Finance("F");
	private String code;
	private FinancingType(String code) 
	{
		this.code = code;
	}
	
	@Override
	public String toString() 
	{
		return this.code;
	}
	
	public static FinancingType find(String code) 
	{
		for (FinancingType type : FinancingType.values()) 
		{
			if (type.code.equals(code)) 
			{
				return type;
			}
		}
		throw new IllegalArgumentException(MessageFormat.format("No finance type found with the provided code. [code={0}]", code));
	}
	
}
