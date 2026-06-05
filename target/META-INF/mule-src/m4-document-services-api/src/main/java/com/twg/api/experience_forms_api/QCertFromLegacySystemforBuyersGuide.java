package com.twg.api.experience_forms_api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.aon.awg.qcert.domain.DealerProduct;
import com.aon.awg.qcert.domain.Inventory;
import com.aon.awg.qcert.domain.Product;
import com.lowagie.text.DocumentException;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import com.aon.awg.qcert.utils.QcertCorePdfConstants.BuyersGuideDetails;
import com.aon.awg.qcert.utils.QcertCorePdfConstants.PdfVehicleDetailLabels;
import com.aon.awg.qcert.domain.vehicle.Vehicle;


public class QCertFromLegacySystemforBuyersGuide  {
	private static final String SPANISH = "es";

	// setting input file location
	public byte[] getPdfTemplateAbsoluteInputFileName(Inventory _inventory) {

			
		final Vehicle vehicleTemplate= _inventory.getVehicle();
		if(vehicleTemplate.getTemplate()!= null ){
			return vehicleTemplate.getTemplate();
		}
		else{
			ClassLoader classLoader = new QCertFromLegacySystemforBuyersGuide().getClass().getClassLoader();
			final File inputfile = new File(classLoader.getResource("AsIsBasic.pdf").getFile());
			FileInputStream fis;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try {
				fis = new FileInputStream(inputfile);				
		        byte[] buf = new byte[1024];
		        try {
		            for (int readNum; (readNum = fis.read(buf)) != -1;) {
		                bos.write(buf, 0, readNum); 
		            }
		        } catch (IOException ex) {
		        	ex.printStackTrace();
		        }
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	       
			byte[] bytes = bos.toByteArray();
	        return bytes;
		}
	}
	
	
	//setting input file location for Spanish
	public String getPdfTemplateAbsoluteInputFileNameForSpanish() {

		ClassLoader classLoader = new QCertFromLegacySystemforBuyersGuide().getClass().getClassLoader();
		final File inputfile = new File(classLoader.getResource("QCET-SP.pdf").getFile());
		String file = inputfile.toString();
		return file;
	}


	// setting out file location
	public String getPdfTemplateAbsoluteOutputFileName() {

		// directory
		String inputFileLocation = ""
				+ "BuyersGuideFilled.pdf";
		return inputFileLocation;
	}
	// setting out file location for Spanish
		public String getPdfTemplateAbsoluteOutputFileNameForSpanish() {

			// directory
			String inputFileLocation = ""
					+ "QCET-SPFilled.pdf";
			return inputFileLocation;
		}


	public byte[] createBuyersGuidePdf(Inventory _inventory
		    ) throws Exception {
		final Map<BuyersGuideDetails, String> buyersGuideData = getBuyersGuideData(_inventory, "en");
	      
	    final Document document = new Document();
	    document.open();
	    byte[] bytearray=null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			final PdfReader pdfReader = new PdfReader(getPdfTemplateAbsoluteInputFileName(_inventory));
			final PdfStamper pdfStamper = new PdfStamper(pdfReader,(OutputStream)baos);
		    final AcroFields form = pdfStamper.getAcroFields();
		    
		    @SuppressWarnings("unchecked")
			final Map<String, String> formFields = form.getFields();
		    for (Map.Entry<String, String> mapEntry : formFields.entrySet()) {
		      final String fieldName = mapEntry.getKey();
		      final String value = buyersGuideData.get(BuyersGuideDetails.fromString(fieldName));
		      if (value != null) {
		        form.setField(fieldName, value);
		      }
		    }
		    pdfStamper.setFormFlattening(true);
		    pdfStamper.close();
		    bytearray= baos.toByteArray();
			return bytearray;

		   		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (DocumentException de) {
			de.printStackTrace();
		}
		return bytearray;
	}
	
	
	
	public byte[] createBuyersGuidePdfSpanish(Inventory _inventory
		    ) throws Exception {
		final Map<BuyersGuideDetails, String> buyersGuideData = getBuyersGuideData(_inventory, "es");
	    final Document document = new Document();
	    document.open();
	    byte[] bytearray=null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			final PdfReader pdfReader = new PdfReader(getPdfTemplateAbsoluteInputFileNameForSpanish());
			final PdfStamper pdfStamper = new PdfStamper(pdfReader,(OutputStream)baos);
		    final AcroFields form = pdfStamper.getAcroFields();
		    
		    @SuppressWarnings("unchecked")
			final Map<String, String> formFields = form.getFields();
		    for (Map.Entry<String, String> mapEntry : formFields.entrySet()) {
		      final String fieldName = mapEntry.getKey();
		      final String value = buyersGuideData.get(BuyersGuideDetails.fromString(fieldName));
		      if (value != null) {
		        form.setField(fieldName, value);
		      }
		    }
		    pdfStamper.setFormFlattening(true);
		    pdfStamper.close();
		    bytearray= baos.toByteArray();
			return bytearray;
		   	} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (DocumentException de) {
			de.printStackTrace();
		}
		return bytearray;
	}


private Map<BuyersGuideDetails, String> getBuyersGuideData(Inventory _inventory, String language) {  
	final String termMaxMiles;
    final String termMaxMonths;
    final String formDeductibleValue;
    final String pdfFormNum;
    final String platinumMonths;
    final String platinumMiles;
    final DealerProduct dealerProduct = _inventory.getDealerProduct();
    final Product product;
    if (dealerProduct != null && dealerProduct.getProduct() != null) {
        product = dealerProduct.getProduct();

        if(product.isAddOnMile()) {
      	  termMaxMiles = product.getTermMiles() + " ADDITIONAL" ; 
        } else {
      	  termMaxMiles = product.getTermMiles();
        }
        termMaxMonths = product.getTermMonths();
        formDeductibleValue = product.getFormDeductible();
        pdfFormNum = product.getAgrFormNo();
        platinumMonths = product.getPlatinumMonths();
        platinumMiles = product.getPlatinumMiles();
      } else {
        product = null;
        termMaxMiles = "<Specify max. number of miles>";
        termMaxMonths ="<Specify max. number of months>";
        formDeductibleValue = "<Specify deductible value>";
        pdfFormNum = "<Specify form number>";
        platinumMonths = "<Specify max. number of months>";
        platinumMiles = "<Specify max. number of miles>";
      }
    final StringBuilder platinumDuration = new StringBuilder();	
    platinumDuration
    	.append("If vehicle is purchased within the new ")
		.append("vehicle warranty, Platinum coverage ")
		.append("applies for the first ")
		.append(platinumMonths)
		.append(" months from ")
		.append("In-Service Date or ")
		.append(platinumMiles)
		.append(" miles from ")
		.append("'0' miles, whichever occurs first. If ")
		.append("vehicle is purchased after the new ")
		.append("vehicle warranty has expired, Platinum ")
		.append("coverage applies for the first 12 ")
		.append("months from the Agreement Date or ")
		.append("12,000 miles from the current ")
		.append("odometer reading, whichever occurs first.");

    final StringBuilder termsTimeToLive = new StringBuilder();
	if (language.equals(SPANISH)) {
		termsTimeToLive
				.append(termMaxMonths)
				.append(" meses desde ")
				.append(getStartingReferenceEventSpanish(product))
				.append(" o cuando el odometro registre ")
				.append(termMaxMiles)
				.append(" millas mas, lo que ocurra primero.");

	} else {
		termsTimeToLive
				.append(termMaxMonths)
				.append(" months from the ")
				.append(getStartingReferenceEvent(product))
				.append(" or when ")
				.append(termMaxMiles)
				.append(" miles are registered on the odometer whichever occurs first.");
	}

	 final String deductibleValue 
     = PdfVehicleDetailLabels.PRICE_CURRENCY.toValue() + formDeductibleValue;
    
    
        final Map<BuyersGuideDetails, String> dataMap = new HashMap<BuyersGuideDetails, String>();
	    final String percentageCoverage = "100%*";
	    final Vehicle vehicle = _inventory.getVehicle();
	    dataMap.put(BuyersGuideDetails.MAKE, vehicle.getModel().getMake().getDescription());
	    dataMap.put(BuyersGuideDetails.MODEL, vehicle.getModel().getDescription());
	    dataMap.put(BuyersGuideDetails.VEHICLE_YEAR,vehicle.getYear()!=null?vehicle.getYear().toString():"");
	    dataMap.put(BuyersGuideDetails.VIN, _inventory.getVin());
	    dataMap.put(BuyersGuideDetails.DEALER_STOCK_NUMBER, _inventory.getStockNo());	   
	    dataMap.put(BuyersGuideDetails.DURATION,termsTimeToLive.toString());
	    dataMap.put(BuyersGuideDetails.LABOR_COVERED, percentageCoverage);
	    dataMap.put(BuyersGuideDetails.PARTS_COVERED, percentageCoverage);
	    dataMap.put(BuyersGuideDetails.PLATINUM_DURATION, platinumDuration.toString());
	    if (language.equals(SPANISH)) {
	        dataMap.put(BuyersGuideDetails.DEDUCTIBLE,product.getFormDeductible());
	    }
	    else{
	       	dataMap.put(BuyersGuideDetails.DEDUCTIBLE,deductibleValue);
	    }
	    return dataMap;
	  }



private String getStartingReferenceEvent(Product _product) {
    final String DEFAULT_MESSAGE = "Agreement Date";
    final String message;
    if(_product == null) {
      message = DEFAULT_MESSAGE;
    } else {
      int months = 0;  
      try {
         months = Integer.parseInt(_product.getTermMonths());
      } catch (NumberFormatException nfe) {
      }
      if (months >= 60) {
        message = "Coverage Effective Date";
      } else {
        message = DEFAULT_MESSAGE;
      }
    }
    return message;
  }
 
private String getStartingReferenceEventSpanish(Product _product) {
    final String DEFAULT_MESSAGE = "el dia de la firma del contrato";
    final String message;
    if(_product == null) {
      message = DEFAULT_MESSAGE;
    } else {
      int months = 0;  
      try {
         months = Integer.parseInt(_product.getTermMonths());
      } catch (NumberFormatException nfe) {
      }
      if (months >= 60) {
        message = "la fecha efectiva de cobertura";
      } else {
        message = DEFAULT_MESSAGE;
      }
    }
    return message;
  }
}