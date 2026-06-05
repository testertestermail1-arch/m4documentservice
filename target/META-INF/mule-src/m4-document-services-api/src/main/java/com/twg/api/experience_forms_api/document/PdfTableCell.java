package com.twg.api.experience_forms_api.document;


public class PdfTableCell extends AlignablePdfElement 
{
	private String value;
	
	/**
	 * Creates a new table cell with the specifield allignment and value
	 * @param allignment one of AlignablePdfElement static values
	 * @param value value of cell
	 */
	public PdfTableCell(int allignment, String value) 
	{
		super(allignment);
		this.value = value;
	}
	
	public String getValue() 
	{
		return value;
	}
	
	public void setValue(String value) 
	{
		this.value = value;
	}
	
	/**
	 * Constructs a <code>String</code> with all attributes in name = value format.
	 * @return a <code>String</code> representation of this object.
	 */
	public String toString() 
	{
		final String TAB = "\n";
		StringBuffer retValue = new StringBuffer();
		retValue.append("PdfTableCell ( ").append(super.toString()).append(TAB).append("value = ").append(this.value).append(TAB).append(" )");
		return retValue.toString();
	}
	
}
