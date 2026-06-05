package com.twg.api.experience_forms_api.document;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PdfTable extends AlignablePdfElement implements Serializable 
{
	private static final long serialVersionUID = 1L;
	private int rowIndex;
	private Map<Integer, List<PdfTableCell>> cells;
	private List<PdfTableHeader> headers;

	/**
	 * rowStart - the first row to be written, zero index rowEnd - the last row to be written + 1. If it is -1 all the rows to the end are written xPos - the x
	 * write coodinate yPos - the y write coodinate
	 */
	private int width = 0, xpos = 0, ypos = 0, rowStart = 0, rowEnd = 0;
	
	/**
	 * Creates a Pdf table with the default allignment (CENTER)
	 */
	public PdfTable() 
	{
		super(AlignablePdfElement.ALIGN_CENTER);
		this.init();
	}
	
	/**
	 * Creates a Pdf table with the specified allignment
	 * @param allignment one of AlignablePdfElement static values
	 */
	public PdfTable(int allignment) 
	{
		super(allignment);
		this.init();
	}
	
	public PdfTable(int alignment, int width, int xpos, int ypos, int rowstart, int rowend) 
	{
		super(alignment);
		this.width = width;
		this.xpos = xpos;
		this.ypos = ypos;
		this.rowStart = rowstart;
		this.rowEnd = rowend;
		this.init();
	}
	
	private void init() 
	{
		this.rowIndex = 0;
		this.cells = new LinkedHashMap<Integer, List<PdfTableCell>>();
		this.headers = new ArrayList<PdfTableHeader>();
	}
	
	/**
	 * Adds a cell to the table
	 * @param cell javadoc comments
	 */
	public void addCell(PdfTableCell cell) 
	{
		if (!this.cells.containsKey(rowIndex)) 
		{
			this.cells.put(rowIndex, new ArrayList<PdfTableCell>());
		}
		this.cells.get(rowIndex).add(cell);
	}
	
	public void addHeader(PdfTableHeader header) 
	{
		this.headers.add(header);
	}
	
	public void nextRow() 
	{
		this.rowIndex++;
	}
	
	public int getRowIndex() 
	{
		return rowIndex;
	}
	
	public void setRowIndex(int rowIndex) 
	{
		this.rowIndex = rowIndex;
	}
	
	public Map<Integer, List<PdfTableCell>> getCells() 
	{
		return cells;
	}
	
	public void setCells(Map<Integer, List<PdfTableCell>> cells) 
	{
		this.cells = cells;
	}
	
	public int getWidth() 
	{
		return width;
	}
	
	public void setWidth(int width) 
	{
		this.width = width;
	}
	
	public int getXpos() 
	{
		return xpos;
	}
	
	public void setXpos(int xstart) 
	{
		this.xpos = xstart;
	}
	
	public int getYpos() 
	{
		return ypos;
	}
	
	public void setYpos(int ystart) 
	{
		this.ypos = ystart;
	}
	
	public int getRowStart() 
	{
		return rowStart;
	}
	
	public void setRowStart(int xend) 
	{
		this.rowStart = xend;
	}
	
	public int getRowEnd() 
	{
		return rowEnd;
	}
	
	public void setRowEnd(int yend) 
	{
		this.rowEnd = yend;
	}
	
	public List<PdfTableHeader> getHeaders() 
	{
		return headers;
	}
	
}
