package com.twg.api.experience_forms_api.document;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cryptacular.codec.Base64Decoder;
import org.glassfish.grizzly.utils.BufferInputStream;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfAppearance;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import java.util.Base64;
import java.util.Base64.Decoder;

public class PdfUtil {
	public static final String FORMAT_TYPE_ALL_CAPS = "ALL_CAPS";
	private static final Log log = LogFactory.getLog(PdfUtil.class);
	public static String customerX = "";
	public static String customerY = "";
	public static String customerxBound = "";
	public static String customeryBound = "";
	public static String customerInitialsX = "";
	public static String customerInitialsY = "";
	public static String customerInitialsxBound = "";
	public static String customerInitialsyBound = "";
	public static String customerPageVal = "";
	public static String customerInitialsPageVal = "";
	public static String dealerX = "";
	public static String dealerY = "";
	public static String dealerxBound = "";
	public static String dealeryBound = "";
	public static String dealerInitialsX = "";
	public static String dealerInitialsY = "";
	public static String dealerInitialsxBound = "";
	public static String dealerInitialsyBound = "";
	public static String dealerPageVal = "";
	public static String dealerInitialsPageVal = "";
	public static String coBuyerX = "";
	public static String coBuyerY = "";
	public static String coBuyerxBound = "";
	public static String coBuyeryBound = "";
	public static String coBuyerInitialsX = "";
	public static String coBuyerInitialsY = "";
	public static String coBuyerInitialsxBound = "";
	public static String coBuyerInitialsyBound = "";		
	public static String coBuyerPageVal = "";
	public static String coBuyerInitialsPageVal = "";
	public static String qrCode;
	private static DecimalFormat numberFormat = new DecimalFormat("#,##0");
	
	public static byte[] generatePdfFile(PdfBean pdf, String formatType, InputStream template) throws Exception {

		InputStream inputStreamTemplate = null;
		PdfReader reader = null;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			inputStreamTemplate = template;
			reader = new PdfReader(inputStreamTemplate);
			PdfStamper stamp = new PdfStamper(reader, baos);
			PdfContentByte over = stamp.getOverContent(1);
			AcroFields form = stamp.getAcroFields();
			Font font = new Font(Font.HELVETICA, 7);

			//Changes for PS-20228
			BaseFont accentFont =BaseFont.createFont("ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			form.addSubstitutionFont(accentFont);
			//Check if MONTHS_USED field is found in the template
			boolean monthsUsedFieldFound=false;
			boolean ppMonthsFieldFound=false;
			boolean ppActivationDateFieldFound=false;
			boolean qrCodeStamp=false;
			boolean engineOilTypeFieldFound=false;
		    @SuppressWarnings("unchecked")
			final Map<String, String> formFields = form.getFields();
		    for (Map.Entry<String, String> mapEntry : formFields.entrySet()) {
		      final String fieldName = mapEntry.getKey();
		      if (fieldName.toUpperCase().contains("MONTHS_USED")) {
		    	  monthsUsedFieldFound = true;
		      }
		      if (fieldName.toUpperCase().contains("PP_MONTHS")) {
		    	  ppMonthsFieldFound = true;
		    	  
		      }
		      if (fieldName.toUpperCase().contains("PP_ACTIVATIONDATE")) {
		    	  ppActivationDateFieldFound = true;
		    	  
		      }
		      if(fieldName.toUpperCase().contains("QR_CODE") && !qrCode.equalsIgnoreCase("error"))
		       {
		    	   qrCodeStamp = true;
		       }
		      if(fieldName.toUpperCase().contains("ENGINE_OIL_TYPE"))
		       {
		    	   engineOilTypeFieldFound = true;
		       }
		      
		      
		    }	
		    
//		  //If engine oilType field not found, concat oilType with coverage
//		    if(!engineOilTypeFieldFound && pdf.getFields().get("COVERAGEPLAN") != null && pdf.getFields().get("ENGINE_OIL_TYPE") != null)
//		    {
//		    	pdf.addField("COVERAGEPLAN",pdf.getFields().get("COVERAGEPLAN").concat("/").concat(pdf.getFields().get("ENGINE_OIL_TYPE")));
//		    }
//		    else if(engineOilTypeFieldFound && pdf.getFields().get("COVERAGEPLAN") != null)
//		    {
//		    	pdf.addField("COVERAGEPLAN",pdf.getFields().get("COVERAGEPLAN"));
//		    }
		   	    
		   //If not found, populate MONTHS and MILES fields
		   if (!monthsUsedFieldFound && pdf.getFields().get("MONTHS_USED")!=null){
				pdf.addField("MONTHS", pdf.getFields().get("MONTHS_USED"));
				pdf.addField("MILES",numberFormat.format(Integer.parseInt(pdf.getFields().get("MILES_USED"))));
		   }
		  if(ppMonthsFieldFound && pdf.getFields().get("Bundled")!= "X")
		   {
			   pdf.addField("MONTHS","");
		   }
		  if(ppActivationDateFieldFound && pdf.getFields().get("Bundled")!= "X")
		   {
			   pdf.addField("ACTIVATIONDATE","");
		   }
			   
			Map<String, String> fields = pdf.getFields();
			for (String field : fields.keySet()) {
				form.setField(field, formatField(fields.get(field), formatType));
			}

			// Miles fields
			boolean notFound = true;
			fields = pdf.getMilesFields();
			for (String field : fields.keySet()) {
				if(form.setField(field, formatField(fields.get(field), formatType)) && fields.get(field).equals("X"))
				{
					notFound = false;
					break;
				}
			}

			if (notFound) {
				Map<String, String> others = pdf.getOtherMilesFields();
				for (String other : others.keySet()) {
					form.setField(other, formatField(others.get(other), formatType));
				}
			}

	
			// Terms fields
			notFound = false;
			fields = pdf.getTermFields();
			int count = 0;
			int notTermFound = 0;
			for (String field : fields.keySet()) {
				// In some cases are two fields of terms (24 months / 2 years)
				// for the same term value
				if(fields.get(field).equals("X")){
					count++;
					if (!form.setField(field, formatField(fields.get(field), formatType))) {
						notTermFound++;
					}
				}
			}

			if (notTermFound == count) {
				Map<String, String> others = pdf.getOtherTermFields();
				for (String other : others.keySet()) {
					form.setField(other, formatField(others.get(other), formatType));
				}
			}

			// table
			Map<String, PdfTable> tabs = pdf.getTablefields();
			for (String tabname : tabs.keySet()) {

				log.debug("Getting table:" + tabname);

				PdfTable tab = tabs.get(tabname);
				// create table properties
				PdfPTable table = null;
				if (tab.getHeaders().size() > 0) {
					table = new PdfPTable(tab.getHeaders().size());

					log.debug("Table has columns = " + tab.getHeaders().size());

				} else {
					table = new PdfPTable(tab.getCells().get(0).size());

					log.debug("Table has columns(cells) = " + tab.getCells().get(0).size());

				}
				// table widths
				float[] cellwidths = new float[tab.getHeaders().size()];
				int windex = 0;
				for (PdfTableHeader header : tab.getHeaders()) {
					cellwidths[windex] = header.getWidth();
					windex++;
				}
				if (log.isDebugEnabled()) { // avoids iteration if not in debug mode
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < cellwidths.length; i++) {
						sb.append(cellwidths[i]);
						sb.append(",");
					}
					log.debug("Setting cell widths to " + sb.toString());
				}
				table.setWidths(cellwidths);
				switch (tab.getAlignment()) {
				case (AlignablePdfElement.ALIGN_LEFT):
					table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
					break;
				case (AlignablePdfElement.ALIGN_CENTER):
					table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
					break;
				case (AlignablePdfElement.ALIGN_RIGHT):
					table.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
					break;
				}
				
				// populate headers
				for (PdfTableHeader header : tab.getHeaders()) {
					PdfPCell cell = createNewCell(font, header.getValue(), 1);
					switch (header.getAlignment()) {
					case (AlignablePdfElement.ALIGN_LEFT):
						cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
						break;
					case (AlignablePdfElement.ALIGN_CENTER):
						cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
						break;
					case (AlignablePdfElement.ALIGN_RIGHT):
						cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
						break;
					}
					table.addCell(cell);
				}
				
				// populate data
				Map<Integer, List<PdfTableCell>> cells = tab.getCells();
				for (Integer rowindex : cells.keySet()) {
					List<PdfTableCell> tcells = cells.get(rowindex);
					for (PdfTableCell tc : tcells) {
						log.debug("Adding new cell " + tc.toString());
						PdfPCell cell = createNewCell(font, tc.getValue(), 1);
						switch (tc.getAlignment()) {
						case (AlignablePdfElement.ALIGN_LEFT):
							cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
							break;
						case (AlignablePdfElement.ALIGN_CENTER):
							cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
							break;
						case (AlignablePdfElement.ALIGN_RIGHT):
							cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
							break;
						}
						table.addCell(cell);
					}
				}
				
				table.setTotalWidth(tab.getWidth());
				table.writeSelectedRows(tab.getRowStart(), tab.getRowEnd(), tab.getXpos(), tab.getYpos(), over);
			}
			
			
		//QR Code Stamping

			if(qrCodeStamp)
			{
			BufferedImage images = null;
			byte[] imageByte;
			Decoder decoder = Base64.getDecoder();
			imageByte = decoder.decode(qrCode);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			images = ImageIO.read(bis);
			bis.close();
			
			File outputfile = new File("images.png");
			ImageIO.write(images, "png", outputfile);
			
			Image image = Image.getInstance("images.png");
			
			float[] qrCodePosition = form.getFieldPositions("QR_Code");
			int initialLeft = Math.round(qrCodePosition[1]);
			int initialBottom = Math.round(qrCodePosition[2]);
			int initialRight = Math.round(qrCodePosition[3]);
			int initialTop = Math.round(qrCodePosition[4]);
			
			String left = Integer.toString(initialLeft);
			String right = Integer.toString(initialRight);
			String top = Integer.toString(initialTop);
			String bottom = Integer.toString(initialBottom);
			
			
			
			stamp = stampSignature(pdf, left, bottom, right, top, image, stamp, reader, "1");
			}
			

			// The below section is used to check whether customer Signature needs to be stamped
			if ((customerX != null || customerX != "" || !customerX.trim().isEmpty())
					&& (customerY != null && !customerY.trim().isEmpty() && customerY != "")
					&& (customerPageVal != null || customerPageVal != "" || !customerPageVal.trim().isEmpty())) {
				
				Image image = Image.getInstance(PdfUtil.class.getResource("/Customer_signature.png"));
				// Call to the PdfStamper function for stamping signature
				stamp = stampSignature(pdf, customerX, customerY, customerxBound, customeryBound, image, stamp, reader, customerPageVal);
			}
			
			// The below section is used to check whether dealer Signature needs to be stamped
			if ((dealerX != null || dealerX != "" || !dealerX.trim().isEmpty())
					&& (dealerY != null && !dealerY.trim().isEmpty() && dealerY != "")
					&& (dealerPageVal != null || dealerPageVal != "" || !dealerPageVal.trim().isEmpty())) {
				
				Image image = Image.getInstance(PdfUtil.class.getResource("/Dealer_signature.png"));
				stamp = stampSignature(pdf, dealerX, dealerY, dealerxBound, dealeryBound, image, stamp, reader, dealerPageVal);
			}
			
			// The below section is used to check whether coBuyer Signature needs to be stamped
			if ((coBuyerX != null || coBuyerX != "" || !coBuyerX.trim().isEmpty())
					&& (coBuyerY != null && !coBuyerY.trim().isEmpty() && coBuyerY != "")
					&& (coBuyerPageVal != null || coBuyerPageVal != "" || !coBuyerPageVal.trim().isEmpty())) {
				
				Image image = Image.getInstance(PdfUtil.class.getResource("/CoBuyer_signature.png"));
				stamp = stampSignature(pdf, coBuyerX, coBuyerY, coBuyerxBound, coBuyeryBound, image, stamp, reader, coBuyerPageVal);
			}
			
	    	// The below section is used to check whether customer Initials needs to be stamped
			if ((customerInitialsX != null || customerInitialsX != "" || !customerInitialsX.trim().isEmpty())
					&& (customerInitialsY != null && !customerInitialsY.trim().isEmpty() && customerInitialsY != "")
					&& (customerInitialsPageVal != null || customerInitialsPageVal != "" || !customerInitialsPageVal.trim().isEmpty())) {
				
				Image image = Image.getInstance(PdfUtil.class.getResource("/Customer_initials.png"));
				// Call to the PdfStamper function for stamping signature
				stamp = stampSignature(pdf, customerInitialsX, customerInitialsY, customerInitialsxBound, customerInitialsyBound, image, stamp, reader, customerInitialsPageVal);
			}
			
			// The below section is used to check whether dealer Initials needs to be stamped
			if ((dealerInitialsX != null || dealerInitialsX != "" || !dealerInitialsX.trim().isEmpty())
					&& (dealerInitialsY != null && !dealerInitialsY.trim().isEmpty() && dealerInitialsY != "")
					&& (dealerInitialsPageVal != null || dealerInitialsPageVal != "" || !dealerInitialsPageVal.trim().isEmpty())) {
				
				Image image = Image.getInstance(PdfUtil.class.getResource("/Dealer_initials.png"));
				stamp = stampSignature(pdf, dealerInitialsX, dealerInitialsY, dealerInitialsxBound, dealerInitialsyBound, image, stamp, reader, dealerInitialsPageVal);
			}
			
			// The below section is used to check whether coBuyer Initials needs to be stamped
			if ((coBuyerInitialsX != null || coBuyerInitialsX != "" || !coBuyerInitialsX.trim().isEmpty())
					&& (coBuyerInitialsY != null && !coBuyerInitialsY.trim().isEmpty() && coBuyerInitialsY != "")
					&& (coBuyerInitialsPageVal != null || coBuyerInitialsPageVal != "" || !coBuyerInitialsPageVal.trim().isEmpty())) {
				
				Image image = Image.getInstance(PdfUtil.class.getResource("/CoBuyer_initials.png"));
				stamp = stampSignature(pdf, coBuyerInitialsX, coBuyerInitialsY, coBuyerInitialsxBound, coBuyerInitialsyBound, image, stamp, reader, coBuyerInitialsPageVal);
			}
			
			
			stamp.setFormFlattening(true);
			stamp.close();
			byte[] data = baos.toByteArray();
			baos.flush();
			baos.close();
			return data;
			
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			try {
				if (inputStreamTemplate != null) {
					inputStreamTemplate.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (Exception ex) {
				log.error("Could not close resources");
				log.error(ex.getStackTrace());
			}
		}
	}

	/**
	 * Creates a new PDF cell
	 * 
	 * @param f
	 * @param content
	 * @param colspan
	 * @return
	 */
	private static PdfPCell createNewCell(Font font, String content, int colspan) {
		Paragraph p = new Paragraph();
		p.add(new Chunk(content, font));
		PdfPCell cell = new PdfPCell(p);
		cell.setBorderColor(new Color(0, 0, 0));
		cell.disableBorderSide(PdfPCell.LEFT);
		cell.disableBorderSide(PdfPCell.RIGHT);
		cell.disableBorderSide(PdfPCell.TOP);
		cell.disableBorderSide(PdfPCell.BOTTOM);
		cell.setColspan(colspan);
		cell.setPadding(0);
		return cell;
	}

	/**
	 * Formats the content of PDF as per input parameter formatType
	 * 
	 * @param fieldValue
	 * @param formatType
	 * @return
	 */
	private static String formatField(String fieldValue, String formatType) {
		if (formatType != null && formatType.equals(FORMAT_TYPE_ALL_CAPS)) {
			if (fieldValue != null) {
				return fieldValue.toUpperCase();
			}
		}
		return fieldValue;
	}

	/**
	 * Function to stamp the Signature into the respective Form PDF's
	 * 
	 * @param pdfBean
	 * @param xLeftVar
	 * @param yBottomVar
	 * @param image
	 * @param stamp
	 * @param reader
	 * @param pageNumber
	 * @return
	 */

	private static PdfStamper stampSignature(PdfBean pdfBean, String xLeftVar, String yBottomVar, String xBound, String yBound, Image image,
			PdfStamper stamp, PdfReader reader, String pageNumber) throws Exception {

		try {			
			int xVal 		= Integer.parseInt(xLeftVar);
			int xBoundVal 	= Integer.parseInt(xBound);
			int yVal 		= Integer.parseInt(yBottomVar);
			int yBoundVal 	= Integer.parseInt(yBound);
			float width 	= image.width();//.getScaledWidth();
			float height	= image.height();//getScaledHeight();
			Rectangle location = new Rectangle((float) xVal, (float) yVal, (float) xBoundVal, (float) yBoundVal);

			// Create stamp annotation
			PdfAnnotation stampAnnot = PdfAnnotation.createStamp(stamp.getWriter(), location, null, "ITEXT");
			image.setAbsolutePosition(0,0);

			// Create new PdfContentByte from the stamp writer
			PdfContentByte cb = new PdfContentByte(stamp.getWriter());
			PdfAppearance app = cb.createAppearance(width, height);

			app.addImage(image);
			stampAnnot.setAppearance(PdfName.N, app);
			stampAnnot.setFlags(PdfAnnotation.FLAGS_PRINT);

			PdfContentByte under;
			int total = reader.getNumberOfPages() + 1;
			ArrayList<Integer> a = new ArrayList<Integer>();
			String[] integerStrings = pageNumber.split(",");

			for (int i = 0; i < integerStrings.length; i++) {
				a.add(Integer.parseInt(integerStrings[i]));
			}

			// The below section is for stamping the signature in mentioned page numbers
			for (int i = 1; i < total; i++) {
				if (a.contains(i)) {
					stamp.addAnnotation(stampAnnot, i);
				}
			}
		} catch (Exception ex) {
			System.out.println("Error Occurred : " + ex.getMessage());
			
		}

		return stamp;
	}

}
