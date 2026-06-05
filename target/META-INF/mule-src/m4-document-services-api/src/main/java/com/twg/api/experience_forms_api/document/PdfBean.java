package com.twg.api.experience_forms_api.document;


import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Map;

public class PdfBean 
{
	private String inputFileLocation = null, inputFileName = null, outputFileLocation = null, outputFileName = null;
	private Map<String, String> fields = null;
	private Map<String, PdfTable> tablefields = null;
	private Map<String, String> termFields = null;
	private Map<String, String> otherTermFields = null;
	private Map<String, String> milesFields = null;
	private Map<String, String> otherMilesFields = null;
	
	public PdfBean() 
	{
		this.fields = new Hashtable<String, String>();
		this.tablefields = new Hashtable<String, PdfTable>();
		this.termFields = new Hashtable<String, String>();
		this.otherTermFields = new Hashtable<String, String>();
		this.milesFields = new Hashtable<String, String>();
		this.otherMilesFields = new Hashtable<String, String>();
	}
	
	public void addField(String name, String value) 
	{
		if (value == null)
			value = "";
		this.fields.put(name, value);
	}
	
	public void addField(String name, Object value) 
	{
		if (value == null)
			value = "";
		this.fields.put(name, value.toString());
	}
	
	public void addField(String name, BigDecimal value) 
	{
		this.fields.put(name, value.toString());
	}
	
	public void addTable(String name, PdfTable tab) 
	{
		this.tablefields.put(name, tab);
	}
	
	public void addTableCell(String name, PdfTableCell cell) 
	{
		if (!tablefields.containsKey(name)) 
		{
			this.tablefields.put(name, new PdfTable());
		}
		this.tablefields.get(name).addCell(cell);
	}
	
	public void addTableHeader(String name, PdfTableHeader header) 
	{
		if (!tablefields.containsKey(name)) 
		{
			this.tablefields.put(name, new PdfTable());
		}
		this.tablefields.get(name).addHeader(header);
	}
	
	public void nextRow(String name) 
	{
		if (tablefields.containsKey(name)) 
		{
			this.tablefields.get(name).nextRow();
		}
	}
	
	public Map<String, String> getFields() 
	{
		return fields;
	}
	
	public Map<String, PdfTable> getTablefields() 
	{
		return tablefields;
	}
	
	public String getInputFileLocation() 
	{
		return inputFileLocation;
	}
	
	public void setInputFileLocation(String inputFileLocation) 
	{
		this.inputFileLocation = inputFileLocation;
	}
	
	public String getInputFileName() 
	{
		return inputFileName;
	}
	
	public void setInputFileName(String inputFileName) 
	{
		this.inputFileName = inputFileName;
	}
	
	public String getOutputFileLocation() 
	{
		return outputFileLocation;
	}
	
	public void setOutputFileLocation(String outputFileLocation) 
	{
		this.outputFileLocation = outputFileLocation;
	}
	
	public String getOutputFileName() 
	{
		return outputFileName;
	}
	
	public void setOutputFileName(String outputFileName) 
	{
		this.outputFileName = outputFileName;
	}
	
	public void addTermField(String name, String value) 
	{
		if (value == null)
			value = "";
		this.termFields.put(name, value);
	}
	
	public void addTermField(String name, Object value) 
	{
		if (value == null)
			value = "";
		this.termFields.put(name, value.toString());
	}
	
	public void addTermField(String name, BigDecimal value) 
	{
		this.termFields.put(name, value.toString());
	}
	
	public Map<String, String> getTermFields() 
	{
		return termFields;
	}
	
	public void addMilesField(String name, String value) 
	{
		if (value == null)
			value = "";
		this.milesFields.put(name, value);
	}
	
	public void addMilesField(String name, Object value) 
	{
		if (value == null)
			value = "";
		this.milesFields.put(name, value.toString());
	}
	
	public void addMilesField(String name, BigDecimal value) 
	{
		this.milesFields.put(name, value.toString());
	}
	
	public Map<String, String> getMilesFields() 
	{
		return milesFields;
	}
	
	public void addOtherMilesField(String name, String value) 
	{
		if (value == null)
			value = "";
		this.otherMilesFields.put(name, value);
	}
	
	public void addOtherMilesField(String name, Object value) 
	{
		if (value == null)
			value = "";
		this.otherMilesFields.put(name, value.toString());
	}
	
	public void addOtherMilesField(String name, BigDecimal value) 
	{
		this.otherMilesFields.put(name, value.toString());
	}
	
	public Map<String, String> getOtherMilesFields() 
	{
		return otherMilesFields;
	}
	
	public void addOtherTermField(String name, String value) 
	{
		if (value == null)
			value = "";
		this.otherTermFields.put(name, value);
	}
	
	public void addOtherTermField(String name, Object value) 
	{
		if (value == null)
			value = "";
		this.otherTermFields.put(name, value.toString());
	}
	
	public void addOtherTermField(String name, BigDecimal value) 
	{
		this.otherTermFields.put(name, value.toString());
	}
	
	public Map<String, String> getOtherTermFields() 
	{
		return otherTermFields;
	}
	
	/**
	 * Constructs a <code>String</code> with all attributes in name = value format.
	 * @return a <code>String</code> representation of this object.
	 */
	public String toString() 
	{
		final String TAB = "\n";
		StringBuffer retValue = new StringBuffer();
		retValue.append("PurchasePdfBean ( ").append(super.toString()).append(TAB).append("inputFileLocation = ").append(this.inputFileLocation).append(TAB)
				.append("inputFileName = ").append(this.inputFileName).append(TAB).append("outputFileLocation = ").append(this.outputFileLocation).append(TAB)
				.append("outputFileName = ").append(this.outputFileName).append(TAB).append("fields = ").append(this.fields).append(TAB)
				.append("termFields = ").append(this.termFields).append(TAB).append("otherTermFields = ").append(this.otherTermFields).append(TAB)
				.append("milesFields = ").append(this.milesFields).append(TAB).append("otherMilesFields = ").append(this.otherMilesFields).append(TAB)
				.append("tablefields = ").append(this.tablefields).append(TAB).append(" )");
		
		return retValue.toString();
	}
	
}
