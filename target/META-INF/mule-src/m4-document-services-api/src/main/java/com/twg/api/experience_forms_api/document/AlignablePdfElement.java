package com.twg.api.experience_forms_api.document;


public class AlignablePdfElement 
{
	public static final int ALIGN_LEFT = -1;
	public static final int ALIGN_CENTER = 0;
	public static final int ALIGN_RIGHT = 1;
	private int alignment;
	public AlignablePdfElement(int alignment) 
	{
		super();
		this.alignment = alignment;
	}
	
	public int getAlignment() 
	{
		return alignment;
	}
	
	public void setAlignment(int alignment) 
	{
		this.alignment = alignment;
	}
	
	/**
	 * Constructs a <code>String</code> with all attributes in name = value format.
	 * @return a <code>String</code> representation of this object.
	 */
	public String toString() 
	{
		final String TAB = "\n";
		StringBuffer retValue = new StringBuffer();
		retValue.append("AlignablePdfElement ( ").append(super.toString()).append(TAB).append("alignment = ").append(this.alignment).append(TAB).append(" )");
		return retValue.toString();
	}
	
}
