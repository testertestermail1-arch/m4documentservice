package com.twg.api.experience_forms_api.document;


public class PdfTableHeader extends PdfTableCell 
{
	private float width = 0f;
	
	/**
	 * Creates a new table header with the specified parameters
	 * @param allignment one of static values from AlignablePdfElement
	 * @param value display value
	 * @param width width of the column
	 */
	public PdfTableHeader(int allignment, String value, float width) 
	{
		super(allignment, value);
		this.width = width;
	}
	
	public float getWidth() 
	{
		return width;
	}
	
	public void setWidth(float width) 
	{
		this.width = width;
	}
	
}
