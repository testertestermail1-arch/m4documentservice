/*
 * Copyright (c) 2007 The Warranty Group.
 *
 * This software is the confidential and proprietary information of The Warranty Group 
 * (http://www.thewarrantygroup.com/). You shall not disclose such Confidential Information or use 
 * it without authorization from The Warranty Group.
 */
package com.twg.api.experience_forms_api;

import static com.aon.awg.qcert.utils.QcertCorePdfConstants.getVehicleFooterText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aon.awg.qcert.businesslayer.VehiclePDFOptions;
import com.aon.awg.qcert.domain.Dealer;
import com.aon.awg.qcert.domain.Inventory;
import com.aon.awg.qcert.domain.vehicle.Vehicle;
import com.aon.awg.qcert.domain.vehicle.VehicleVinAttributes;
import com.aon.awg.qcert.report.util.Pair;
import com.aon.awg.qcert.utils.UtilityObjects;
import com.aon.awg.qcert.utils.QcertCorePdfConstants.LineConstants;
import com.aon.awg.qcert.utils.QcertCorePdfConstants.PdfCallingMethods;
import com.aon.awg.qcert.utils.QcertCorePdfConstants.PdfVehicleDetailLabels;
import com.aon.awg.qcert.utils.QcertCorePdfConstants.VehicleDetails;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;


public class VehicleDetailsPdf {

  /* Variables declaration */
  private static final Log LOG = LogFactory.getLog(VehicleDetailsPdf.class);
  private final BaseFont baseFont;
  private final BaseFont boldFont;
  private final BaseFont dealerAddressFont;
  private static final int NORMAL_FONT_SIZE = 10;
  private static final int SMALL_FONT_SIZE = 7;
  private static final double WHEEL_BASE_DEFAULT_VALUE = 0.00;

  
  /* constructor */
  public VehicleDetailsPdf() {
   try {
      baseFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.EMBEDDED);
      boldFont = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
      dealerAddressFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    } catch (DocumentException de) {
      throw new RuntimeException(de);
    }
  }
  

  public byte[] createVehicleDetailsPdf(VehiclePDFOptions _vehicleDetails, Inventory _inventory, Dealer _dealer) throws Exception {
	  byte[] bytearray = null;
	  ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      final StickeringCommon common = new StickeringCommon();
      final PdfReader pdfReader = new PdfReader(common.getPdfTemplateAbsoluteFileName("vehicleSticker_template"));
      final PdfStamper pdfStamper = new PdfStamper(pdfReader,(OutputStream)baos);
      final PdfData pdfData = getPdfData(_inventory, _dealer, PdfCallingMethods.DETAILS_PDF);
      
      //Method calls to generate pdf content.
      stampVehicleDetailsTitle(pdfStamper, pdfData);
      stampVehicleDetails(pdfStamper, pdfData);
      stampVehicleDetailsOptions(pdfStamper, _vehicleDetails);
      stampVehicleDetailsPedigree(pdfStamper, pdfData);
      stampVehicleMileage(pdfStamper, pdfData);
      stampVehicleDealerInfo(pdfStamper, _dealer, pdfData);
      stampVehiclePrice(pdfStamper, doesPrintPrices(_inventory), _dealer, _inventory);
      
      pdfStamper.setFormFlattening(true);
      pdfStamper.close();
      bytearray= baos.toByteArray();
	  return bytearray;
    } catch (IOException ioe) {
      LOG.error("Error creating vehicle view pdf: ", ioe);
      ioe.printStackTrace();
    } catch (DocumentException de) {
      LOG.error("Error creating vehicle view pdf, document exception: ", de);
      de.printStackTrace();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return bytearray;
  }

  private boolean doesPrintPrices(Inventory _inventory) {
    final boolean doesPrintPrices;
    final Integer printPrices = _inventory.getPrintPrices();
    if(Inventory.PRINT_PRICES_ON_STICKER_TRUE.equals(printPrices)){
      doesPrintPrices = true;
    } else if(Inventory.PRINT_PRICES_ON_STICKER_DEFAULT.equals(printPrices)){
      doesPrintPrices = _inventory.getDealer().isPrintPricesSticker();
    } else {
      doesPrintPrices = false; //Default.
    }
    return doesPrintPrices;
  }
  
  
  private void stampVehicleDetailsTitle(PdfStamper _pdfStamper, PdfData _pdfData) throws Exception {
    final AcroFields form = _pdfStamper.getAcroFields();
    form.setField("vehicleTitle", _pdfData.vehicleTitleBuffer.toString());
  }

  
  private void stampVehicleDetails(PdfStamper _pdfStamper, PdfData _pdfData) throws Exception {
    final AcroFields form = _pdfStamper.getAcroFields();
    final List<String> vehicleDetailsList = Arrays.asList(_pdfData.vehicleDetailsBuffer.toString()
        .split(LineConstants.NEW_LINE.toValue()));
    for (String head : vehicleDetailsList) {
      final String key = head.substring(0, head.indexOf(LineConstants.DELIMITER.toString()));
      final String value = head.substring(head.indexOf(LineConstants.DELIMITER.toString()) + 1);
      form.setField(key, value);
    }
  }

  private void stampVehicleDetailsOptions(PdfStamper _pdfStamper, VehiclePDFOptions _vehicleDetails)
      throws Exception {
    final PdfContentByte overContent = _pdfStamper.getOverContent(1);
    Pair<Integer, Integer> currentPosition 
        = new Pair<Integer, Integer>(290, 640); // [x, y].
    
    initializeTextMatrix(overContent, currentPosition, NORMAL_FONT_SIZE);

    currentPosition = showSubSection(overContent, currentPosition, 
        PdfVehicleDetailLabels.FEATURES_INTERIOR.toValue(), 
        _vehicleDetails.getIntFeaturesForPdf(), NORMAL_FONT_SIZE);

    currentPosition = showSubSection(overContent, currentPosition, 
        PdfVehicleDetailLabels.FEATURES_EXTERIOR.toValue(),
        _vehicleDetails.getExtFeaturesForPdf(), NORMAL_FONT_SIZE);

    currentPosition = showSubSection(overContent, currentPosition, 
        PdfVehicleDetailLabels.FEATURES_ENGINERING.toValue(),
        _vehicleDetails.getEngFeaturesForPdf(), NORMAL_FONT_SIZE);

    currentPosition = showSubSection(overContent, currentPosition, 
        PdfVehicleDetailLabels.FEATURES_SAFETY.toValue(),
        _vehicleDetails.getSafetyFeaturesForPdf(), NORMAL_FONT_SIZE);
    
    currentPosition = showSubSection(overContent, currentPosition, 
        PdfVehicleDetailLabels.FEATURES_ADDITIONAL.toValue(),
        _vehicleDetails.getAddedFeaturesForPdf(), NORMAL_FONT_SIZE);

    overContent.endText();
  }

  private void initializeTextMatrix(PdfContentByte _overContent, 
      Pair<Integer, Integer> currentPosition, int _fontSize) {
    _overContent.beginText();
    _overContent.setFontAndSize(baseFont, _fontSize);
    _overContent.setTextMatrix(currentPosition.getFirst(), currentPosition.getSecond());
  }
  

  private Pair<Integer, Integer> showSubSection (PdfContentByte _overContent, 
      Pair<Integer, Integer> currentPosition, String _subSectionTitle, 
      String _featuresSourceData, int _fontSize) {
    showBoldText(_overContent, _subSectionTitle, _fontSize);
    currentPosition = insertNewLine(_overContent, currentPosition, _fontSize);
    final List<String> featuresList 
        = Arrays.asList(_featuresSourceData.split(LineConstants.NEW_LINE.toValue()));
    for(String feature : featuresList) {
      _overContent.showText(feature);
      currentPosition = insertNewLine(_overContent, currentPosition, _fontSize);
    }
    return currentPosition;
  }
  

  private Pair<Integer, Integer> insertNewLine(PdfContentByte _overContent, 
      Pair<Integer, Integer> _currentPosition, int _fontSize) {
    int currentPositionOfY = _currentPosition.getSecond();
    currentPositionOfY -= _fontSize;
    _overContent.setTextMatrix(_currentPosition.getFirst().intValue(), currentPositionOfY);
    return new Pair<Integer, Integer>(_currentPosition.getFirst(), currentPositionOfY);
  }
  
  private void showBoldText(PdfContentByte _overContent, String _text, int _fontSize) {
    _overContent.setFontAndSize(boldFont, _fontSize);
    _overContent.showText(_text);
    _overContent.setFontAndSize(baseFont, _fontSize);
  }
  
  private void stampVehicleDetailsPedigree(PdfStamper _pdfStamper, PdfData _pdfData)
      throws Exception {
    _pdfStamper.getAcroFields().setField("vehiclePedigree", _pdfData.pedigreeBuffer.toString());
  }

  private void stampVehicleMileage(PdfStamper stamp, PdfData _pdfData) throws Exception {
    final AcroFields form = stamp.getAcroFields();
    form.setField("hwyMile", _pdfData.hwyMile);
    form.setField("ctyMile", _pdfData.ctyMile);
  }

  private void stampVehicleDealerInfo(PdfStamper _pdfstamper, final Dealer dealer, PdfData _pdfData) 
      throws Exception {
    final AcroFields form = _pdfstamper.getAcroFields();
    final boolean dearlerAddress = _pdfstamper.getAcroFields().setFieldProperty("dealerAddress", "textfont", dealerAddressFont, null);
    form.setField("dealerName", dealer.getName());
    form.setField("dealerAddress", dealer.getAddress());
    form.setField("dealerWebURL", dealer.getWebsiteURL());
    form.setField("FreeFormData", dealer.getWindowStickerFreeFormData());
  }
 /* private void stampVehicleDealerInfoUpdated(PdfStamper _pdfstamper, final Dealer dealer, PdfData _pdfData) 
	      throws Exception {
	    final AcroFields form = _pdfstamper.getAcroFields();
	    form.setField("dealerName", dealer.getName());
	    if(dealer.getAddress() != null) {
	      if(!dealer.getAddress().isEmpty()) {
	    String[] address=dealer.getAddress().split(",");	    
	    form.setField("dealerAddress1", (address[0]));
	    form.setField("dealerAddress2", "");
	    form.setField("dealerCity", address[1]);
	    String[] zip=address[2].trim().split(" ");
	    form.setField("dealerZIP", zip[0]);
	    form.setField("dealerCountry", zip[1]);
	      }
	    }
	    form.setField("dealerWebURL", dealer.getWebsiteURL());
	    form.setField("FreeFormData", dealer.getWindowStickerFreeFormData());
	  }
*/
  private void stampVehiclePrice(PdfStamper _pdfStamper, boolean _isPrintPrice,
      Dealer _dealer, Inventory _inventory) throws DocumentException, IOException {
    final AcroFields form = _pdfStamper.getAcroFields();
    if (_isPrintPrice) {
      if (_dealer.isDisplayListPrice()) {
        form.setField("List Price Label", _dealer.getListPriceLabel());
        if (_inventory.getListPrice() != null) {
          form.setField("List Price Label price", "$" + _inventory.getListPrice().toString());
        }
      }
      if (_dealer.getBookValueLabel() != null
          && _inventory.getBookValue() != null) {
        form.setField("Book Value Label", _dealer.getBookValueLabel());
        form.setField("Book Value Label Price", "$" + _inventory.getBookValue().toString());
      }

      if (_inventory.getPriceModifier1() != null) {
        form.setField("Price Modifier 1 Label", _dealer.getPriceModifier1Label());
        form.setField("Price Modifier 1 Label Price", "$" 
            + _inventory.getPriceModifier1().toString());
      }

      if (_inventory.getPriceModifier2() != null) {
        form.setField("Price Modifier 2 Label", _dealer.getPriceModifier2Label());
        form.setField("Price Modifier 2 Label Price", "$"
            + _inventory.getPriceModifier2().toString());
      }
      if (_inventory.getPriceModifier3() != null) {
        form.setField("Price Modifier 3 Label", _dealer.getPriceModifier3Label());
        form.setField("Price Modifier 3 Label Price", "$"
            + _inventory.getPriceModifier3().toString());
      }
      if (_inventory.getPriceModifier4() != null) {
        form.setField("Price Modifier 4 Label", _dealer.getPriceModifier4Label());
        form.setField("Price Modifier 4 Label Price", "$"
            + _inventory.getPriceModifier4().toString());
      }
      if (_inventory.getPriceModifier5() != null) {
        form.setField("Price Modifier 5 Label", _dealer.getPriceModifier5Label());
        form.setField("Price Modifier 5 Label Price", "$"
            + _inventory.getPriceModifier5().toString());
      }
      if (_inventory.getFinalPrice() != null) {
        form.setField("finalPrice", "$" + _inventory.getFinalPrice());
        form.setField("Final Price Label", _dealer.getFinalPriceLabel());
      }else {
          form.setField("finalPrice", "NO PRICE");
          form.setField("Final Price Label", _dealer.getFinalPriceLabel());
        }
    } 
  }

  public byte[] createVehicleBrochurePdf(VehiclePDFOptions _vehicleDetails, Inventory _inventory, 
      Dealer _dealer) {
	  byte[] bytearray=null;
	  ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      final StickeringCommon common =  new StickeringCommon();
      final PdfReader pdfReader = new PdfReader(
          common.getPdfTemplateAbsoluteFileName("vehicleBrochure_template"));
      final PdfStamper pdfStamper = new PdfStamper(pdfReader,(OutputStream)baos);

      // Get the pdf data and set the member variables with the data.
      final PdfData pdfData = getPdfData(_inventory, _dealer, PdfCallingMethods.BROCHURE_PDF);

      // Method calls to generate pdf content.
      stampVehicleDetailsTitle(pdfStamper, pdfData);
      stampVehicleDetails(pdfStamper, pdfData);
      stampVehicleDetailsOptionsBrochure(pdfStamper, _vehicleDetails);
      stampVehicleDetailsPedigree(pdfStamper, pdfData);
      stampVehicleDetailsEncyclopedia(pdfStamper, pdfData);
      stampVehicleMileage(pdfStamper, pdfData);
      stampVehicleDisclaimers(pdfStamper);
      stampVehicleDealerInfo(pdfStamper, _dealer, pdfData);
      stampVehiclePrice(pdfStamper, doesPrintPrices(_inventory), _dealer, _inventory);
      pdfStamper.setFormFlattening(true);
      pdfStamper.close();
      bytearray= baos.toByteArray();
	  return bytearray;
    } catch (IOException ioe) {
      LOG.error("Error creating vehicle view pdf: ", ioe);
      ioe.printStackTrace();
    } catch (DocumentException de) {
      LOG.error("Error creating vehicle view pdf, document exception: ", de);
      de.printStackTrace();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return bytearray;
  }

  private void stampVehicleDetailsEncyclopedia(PdfStamper _pdfStamper, PdfData _pdfData) 
      throws Exception {
    final AcroFields form = _pdfStamper.getAcroFields();
    form.setField("vehicleEncyclopedia", _pdfData.encylopediaBuffer.toString());
  }

  private void stampVehicleDisclaimers(PdfStamper _pdfStamper) throws Exception {
    final AcroFields form = _pdfStamper.getAcroFields();
    form.setField("disclaimer", getVehicleFooterText());
    form.setField("disclaimerFooter", getVehicleFooterText());
  }

  /**
   * Used by Vehicle Brochure Pdf.
   */
  private void stampVehicleDetailsOptionsBrochure(PdfStamper _pdfStamper, 
      VehiclePDFOptions _vehicleDetails) throws Exception {
    final PdfContentByte overContent = _pdfStamper.getOverContent(1);
    Pair<Integer, Integer> currentPosition = new Pair<Integer, Integer>(379, 608); // [x, y].
    
    initializeTextMatrix(overContent, currentPosition, SMALL_FONT_SIZE);
           
    currentPosition = showSubSection(overContent, currentPosition, 
        VehicleDetails.LABEL_INTERIOR.toValue(),
        _vehicleDetails.getIntFeaturesForPdf(), SMALL_FONT_SIZE);
    
    currentPosition = showSubSection(overContent, currentPosition, 
        VehicleDetails.LABEL_EXTERIOR.toValue(),
        _vehicleDetails.getExtFeaturesForPdf(), SMALL_FONT_SIZE);
    
    currentPosition = showSubSection(overContent, currentPosition, 
        VehicleDetails.LABEL_ENGINEERING.toValue(),
        _vehicleDetails.getEngFeaturesForPdf(), SMALL_FONT_SIZE);
    
    currentPosition = showSubSection(overContent, currentPosition, 
        VehicleDetails.LABEL_SAFETY.toValue(),
        _vehicleDetails.getSafetyFeaturesForPdf(), SMALL_FONT_SIZE);
    
    currentPosition = showSubSection(overContent, currentPosition, 
        VehicleDetails.LABEL_ADDITIONAL.toValue(),
        _vehicleDetails.getAddedFeaturesForPdf(), SMALL_FONT_SIZE);
    
    overContent.endText();
  }

  /**
   * Generates common data used by both stickering and vehicle brochure pdf's.
   */
  private PdfData getPdfData(Inventory _inventory, Dealer _dealer, 
      PdfCallingMethods _calledByMethod) throws Exception {
    final PdfData pdfData = new PdfData();
    final Vehicle vehicle = _inventory.getVehicle();
    final VehicleVinAttributes vehicleVinAttributes = vehicle.getVinAttributes();
    if (PdfCallingMethods.DETAILS_PDF.equals(_calledByMethod)) {
      pdfData.vehicleTitleBuffer
          .append(LineConstants.NEW_LINE)
          .append(LineConstants.NEW_LINE)
          .append(LineConstants.NEW_LINE);
    }
  
    pdfData.vehicleTitleBuffer
        .append(null!=vehicle.getYear()?vehicle.getYear():"")
        .append(LineConstants.SPACER);
    if(null != vehicle.getModel().getMake().getDescription() ){
    if( "null" != vehicle.getModel().getMake().getDescription().toString() &&
		!("null").equalsIgnoreCase(vehicle.getModel().getMake().getDescription().toString()))
	{
    pdfData.vehicleTitleBuffer
        .append(vehicle.getModel().getMake().getDescription().toUpperCase())
        .append(LineConstants.SPACER);
	}
    }
  
   if (vehicleVinAttributes != null) {
	   if(null != String.valueOf(vehicle.getModel())){
	 if(!("null").equalsIgnoreCase(vehicle.getModel().toString()) && !vehicle.getModel().toString().isEmpty())
	 {
      pdfData.vehicleTitleBuffer
          .append(vehicle.getModel().toString().toUpperCase())
          .append(LineConstants.SPACER);
     }
	   }
	 
      final String vehicleTrim = vehicle.getTrim();
      if (vehicleTrim != null) {
        pdfData.vehicleTitleBuffer
            .append(vehicleTrim.toUpperCase())
            .append(LineConstants.SPACER);
      }
      if( null != vehicleVinAttributes.getBodystyle()){
      if(!("null").equalsIgnoreCase(vehicleVinAttributes.getBodystyle().toString()) && !vehicleVinAttributes.getBodystyle().toString().isEmpty())
      {
        pdfData.vehicleTitleBuffer
          .append(vehicleVinAttributes.getBodystyle().toUpperCase())
          .append(LineConstants.SPACER)
          .append(LineConstants.NEW_LINE);
      }
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("VehicleDetailsPdf.getPdfdata(): Title Buffer::"
          + pdfData.vehicleTitleBuffer.toString());
    }
    // Populate Vehicle Details data.
    if (PdfCallingMethods.BROCHURE_PDF.equals(_calledByMethod) 
        || PdfCallingMethods.DETAILS_PDF.equals(_calledByMethod)) {
      if (vehicleVinAttributes != null) {
        pdfData.vehicleDetailsBuffer
            .append(PdfVehicleDetailLabels.ENGINE.toValue())
            .append(vehicleVinAttributes.getEngineInfo() != null ? vehicleVinAttributes.getEngineInfo():"")
            .append(LineConstants.NEW_LINE);
        pdfData.vehicleDetailsBuffer
            .append(PdfVehicleDetailLabels.TRANSMISSION.toValue())
            .append(vehicleVinAttributes.getTrans() != null ? vehicleVinAttributes.getTrans():"")
            .append(LineConstants.NEW_LINE);
      }
      pdfData.vehicleDetailsBuffer
          .append(PdfVehicleDetailLabels.VIN.toValue())
          .append(_inventory.getVin() != null ? _inventory.getVin():"" )
          .append(LineConstants.NEW_LINE);
      pdfData.vehicleDetailsBuffer
          .append(PdfVehicleDetailLabels.STOCK_NO.toValue())
          .append(_inventory.getStockNo() != null ?_inventory.getStockNo():"")
          .append(LineConstants.NEW_LINE);
     
     pdfData.vehicleDetailsBuffer
          .append(PdfVehicleDetailLabels.MILEAGE.toValue())         
          .append(_inventory.getMileage() == 0? "":_inventory.getMileage())
          .append(LineConstants.NEW_LINE);
    }
    if (PdfCallingMethods.DETAILS_PDF.equals(_calledByMethod)) {
      final String wBase;
      if (vehicleVinAttributes != null) {
        wBase = vehicleVinAttributes.getWheelbase();
        double wheelBaseVal = WHEEL_BASE_DEFAULT_VALUE; // Default value.
        if (StringUtils.isNotBlank(wBase)) {
          final Double wheelBase = new Double(wBase);
          if (wBase.indexOf(".") > 0) {
            wheelBaseVal = (wheelBase.doubleValue());
          } else {
            wheelBaseVal = (wheelBase.doubleValue()) / 100000.00;
          }          
        }
        pdfData.vehicleDetailsBuffer
            .append(PdfVehicleDetailLabels.WHEELBASE.toValue())
            .append(wheelBaseVal == 0.00 ? "":wheelBaseVal)
            .append(LineConstants.NEW_LINE);
        pdfData.vehicleDetailsBuffer
            .append(PdfVehicleDetailLabels.LENGHT.toValue())
            .append(UtilityObjects.nullToNotAvail(vehicleVinAttributes.getLength()))
            .append(LineConstants.NEW_LINE);
        pdfData.vehicleDetailsBuffer
            .append(PdfVehicleDetailLabels.WIDTH.toValue())
            .append(UtilityObjects.nullToNotAvail(vehicleVinAttributes.getWidth()))
            .append(LineConstants.NEW_LINE);
        pdfData.vehicleDetailsBuffer
            .append(PdfVehicleDetailLabels.HEIGHT.toValue())
            .append(UtilityObjects.nullToNotAvail(vehicleVinAttributes.getHeight()))
            .append(LineConstants.NEW_LINE);
        pdfData.vehicleDetailsBuffer
            .append(PdfVehicleDetailLabels.FUEL.toValue())
            .append(UtilityObjects.nullToNotAvail(vehicleVinAttributes.getGas()))
            .append(LineConstants.NEW_LINE);
        pdfData.vehicleDetailsBuffer
            .append(PdfVehicleDetailLabels.SEAT_CAPACITY.toValue())
            .append(UtilityObjects.nullToNotAvail(vehicleVinAttributes.getSeats()))
            .append(LineConstants.NEW_LINE);
      }
    }

    if (PdfCallingMethods.BROCHURE_PDF.equals(_calledByMethod) 
        || PdfCallingMethods.DETAILS_PDF.equals(_calledByMethod)) {
      pdfData.vehicleDetailsBuffer
          .append(PdfVehicleDetailLabels.COLOR.toValue())
          .append(UtilityObjects.nullToNotAvail(vehicle.getColor()))
          .append(LineConstants.NEW_LINE);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("VehicleDetailsPdf.getPdfdata():" + pdfData.vehicleDetailsBuffer.toString());
    }
    // Encylopedia info.
    if (PdfCallingMethods.BROCHURE_PDF.equals(_calledByMethod)) {
      if (vehicleVinAttributes != null) {
        pdfData.encylopediaBuffer.append(UtilityObjects.nullToNotAvail(vehicleVinAttributes.getEncyclopedia()));
      }
    }

    // Pedigree Info.
    if (vehicleVinAttributes != null) {
      pdfData.pedigreeBuffer.append(UtilityObjects.nullToNotAvail(vehicleVinAttributes.getPedigree()));
      pdfData.ctyMile = UtilityObjects.nullToNA(vehicleVinAttributes.getCity());
      pdfData.hwyMile = UtilityObjects.nullToNA(vehicleVinAttributes.getHwy());
    }

    pdfData.dealerName = _dealer.getStickerName();

    if (_dealer.isDisplayAddress()) {
      if (_dealer.getAddress() != null) {
        pdfData.dealerAddressBuffer
            .append(_dealer.getAddress())
            .append(LineConstants.NEW_LINE);
      }
      pdfData.dealerAddressBuffer
          .append(UtilityObjects.nullToEmpty(_dealer.getCity().trim()))
          .append(LineConstants.SPACER)
          .append(UtilityObjects.nullToEmpty(_dealer.getState()))
          .append(LineConstants.SPACER)
          .append(UtilityObjects.nullToEmpty(_dealer.getZip()))
          .append(LineConstants.NEW_LINE);
      pdfData.dealerAddressBuffer
          .append(PdfVehicleDetailLabels.DEALER_PHONE.toValue())
          .append(LineConstants.SPACER)
          .append(UtilityObjects.formatPhoneNo(UtilityObjects.nullToNotAvail(
              _dealer.getSalesPhone()).trim()))
           .append(LineConstants.NEW_LINE);
    }
    return pdfData;
  }
  
  private static class PdfData {
    String dealerName = "";
    String ctyMile = "";
    String hwyMile = "";
    final StringBuilder vehicleTitleBuffer = new StringBuilder();
    final StringBuilder vehicleDetailsBuffer = new StringBuilder();
    final StringBuilder pedigreeBuffer = new StringBuilder();
    final StringBuilder dealerAddressBuffer = new StringBuilder();
    final StringBuilder encylopediaBuffer = new StringBuilder();
  }
}
