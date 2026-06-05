/*
 * Copyright (c) 2009 The Warranty Group.
 *
 * This software is the confidential and proprietary information of The Warranty Group 
 * (http://www.thewarrantygroup.com/). You shall not disclose such Confidential Information or use 
 * it without authorization from The Warranty Group.
 */
package com.aon.awg.qcert.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.aon.awg.qcert.domain.vehicle.VehicleOption;

public class QcertCorePdfConstants {

  public static final String QCERT_OTHER_STATE  = "OT";
  
  /*public enum PdfResources {
    PATH_CACHE(".qcert.pdf.file.documents.cache"),
    PATH_DELIVERABLES(".qcert.pdf.file.documents.deliverables"),
    PATH_IMAGES(".qcert.pdf.img.dir"),
    PATH_TEMPLATES(".qcert.pdf.file.documents.templates");

    private final String propertyFragment;
    
    PdfResources(String _propertyFragment) {
      propertyFragment = _propertyFragment;
    }


    @Override 
    public String toString() {
      throw new UnsupportedOperationException();
    }
  }*/
  
  /*public enum PdfTemplates {  
    BROCHURE("vehicleBrochure_template"),
    TITLE_CHECK("vehicleTitleRpt_template"),
    WINDOW_STICKER("vehicleSticker_template"),   
    BUYERS_GUIDE_GENERIC("AsIsBasic"),
    BUYERS_GUIDE_GENERIC_SPANISH("QCET-SP"),
    BUYERS_GUIDE_GASKETS("AsIsSeals-Gaskets"),
    BUYERS_GUIDE_AS_IS_ONLY("AsIsOnly"),
    BUYERS_GUIDE_IMPLIED_ONLY("ImpliedOnly"),
    BUYERS_GUIDE_IMPLIED_WITH_LW("Implied_with_LW"),
    BUYERS_GUIDE_OREGON("AsIsBasic_good_oregon_06102008"),
    BUYERS_GUIDE_MARKET_LEADER("QCMarket Leader Buyer's Guide"),
    BUYERS_GUIDE_GENERIC_FE("AsIsBasic-FE"),
    BUYERS_GUIDE_AS_IS_PT_WITH_SG("AS-IS_PTwSG"),
    BUYERS_GUIDE_AS_IS_LIFETIME_PT("AsIs_LifetimePT"),
    BUYERS_GUIDE_AS_IS_PT_PL("AS-IS 7-100 PT 3-3 PL"),
    BUYERS_GUIDE_AS_IS_7_125_PT_3_3_PL("AS-IS 7-125 PT 3-3 PL"),
    BUYERS_GUIDE_AS_IS_5_100_PT_3_3_PL("AS-IS 5-100 PT 3-3 PL"),
    BUYERS_GUIDE_AS_IS_BG_PERSTON("AS-IS BG_Preston");
    
    private final String value;
    
    private static Map<String, PdfTemplates> pdfTemplatesMap = new HashMap<String, PdfTemplates>();
    
    static {
      for(PdfTemplates pdfTemplates : PdfTemplates.values()) {
        pdfTemplatesMap.put(pdfTemplates.toValue(), pdfTemplates);
      }
    }
    PdfTemplates(String _value) {
      value = _value;
    }
    
    public String toValue() {
      return value;
    }
    

    public static PdfTemplates fromString(String pdfTemplateAsString) {
      pdfTemplateAsString = pdfTemplateAsString.replace(".pdf", "");
      final PdfTemplates pdfTemplate = pdfTemplatesMap.get(pdfTemplateAsString);
      if(pdfTemplate == null) {
        throw new TypeNotPresentException(pdfTemplateAsString, null);
      }
      return pdfTemplate;
    }
    
    @Override 
    public String toString() {
      throw new UnsupportedOperationException();
    }
  }*/
  
  /*public enum PdfTypes {
    ALL("All"),
    BROCHURE("VehicleBrochure"),
    BUYERS_GUIDE("BuyersGuide"),
    BUYERS_GUIDE_SPANISH("BuyersGuideSpanish"),
    STICKER("VehicleDetails"),
    TITLE_CHECK("TitleCheck");
    
    private final String value;
    
    private static Map<String, PdfTypes> pdfTypesMap = new HashMap<String, PdfTypes>();
    
    static {
      for(PdfTypes pdfType : PdfTypes.values()) {
        pdfTypesMap.put(pdfType.toValue(), pdfType);
      }
    }
    
    PdfTypes(String _value) {
      value = _value;
    }
    
    public String toValue() {
      return value;
    }
    
    public static PdfTypes fromString(String pdfTypesAsString) {
      final PdfTypes pdfType = pdfTypesMap.get(pdfTypesAsString);
      if(pdfType == null) {
        throw new TypeNotPresentException(pdfTypesAsString, null);
      }
      return pdfType;
    }
    
    @Override 
    public String toString() {
      throw new UnsupportedOperationException();
    }
 } */
  
  public enum LineConstants {
    DELIMITER(":"),
    ESCAPE("\\"),
    NEW_LINE("\n"),
    SPACER("   "),
	SPACER_(" ");
    
    private final String lineConstant;
    
    LineConstants(String _lineConstant) {
      lineConstant = _lineConstant;
    }
    
    public String toValue() {
      if(lineConstant.startsWith("\\")) {
        return "\\" + lineConstant;
      } else {
        return lineConstant;
      }
    }
    
    @Override 
    public String toString() {
      return lineConstant;
    }
  }
    
  public enum BuyersGuideDetails {
    DEALER_STOCK_NUMBER("dealerStockNumber"),
    DEDUCTIBLE("deductible"),
    DURATION("duration"),
    FORM_NAME("formName"),
    LABOR_COVERED("laborCovered"),
    MAKE("vehicleMake"),
    MODEL("vehicleModel"),
    PARTS_COVERED("partsCovered"),
    VEHICLE_YEAR("vehicleYear"),
    VIN("VIN"),
    Check_Box1("Check Box1"),
    PLATINUM_DURATION("platinumDuration");
    
    private final String value;
    
    private static Map<String, BuyersGuideDetails> buyersGuideDetailsMap 
        = new HashMap<String, BuyersGuideDetails>();
    
    static {
      for(BuyersGuideDetails buyersGuideDetails : BuyersGuideDetails.values()) {
        buyersGuideDetailsMap.put(buyersGuideDetails.toValue(), buyersGuideDetails);
      }
    }
    
    BuyersGuideDetails(String _value) {
      value = _value;
    }
    
    public String toValue() {
      return value;
    }
    

    public static BuyersGuideDetails fromString(String buyersGuideDetailsAsString) {
      final BuyersGuideDetails buyersGuideDetails 
          = buyersGuideDetailsMap.get(buyersGuideDetailsAsString);
      if(buyersGuideDetails == null) {
        throw new TypeNotPresentException(buyersGuideDetailsAsString, null);
      }
      return buyersGuideDetails;
    }
    
    @Override 
    public String toString() {
      throw new UnsupportedOperationException();
    }
  }

  public enum PdfVehicleDetailLabels {
    COLOR("Color:"),
    DEALER_PHONE("Ph:"),
    DR("R"),
    ENGINE("Engine:"),
    FEATURES_ADDITIONAL("* ------Additional Features ------"),
    FEATURES_ENGINERING("* ------Engineering Features ------"),
    FEATURES_EXTERIOR("* ------Exterior Features ------"),
    FEATURES_INTERIOR("* ------Interior Features ------"),
    FEATURES_SAFETY("* ------Safety Features ------"),
    FUEL("Fuel Capacity:"),
    HEIGHT("Height:"),
    MILEAGE("Mileage:"),
    PRICE_ADMIN("Administrative Fee:"),
    PRICE_BV("Kelly Blue Book Price:"),
    PRICE_CURRENCY("$"),
    PRICE_DISCOUNT("Discount:"),
    PRICE_SALE(" Price:"),
    PRICE_SUGGESTED_RETAIL_PRICE("Suggested Retail Price:"),
    STOCK_NO("Stock No:"),
    LENGHT("Length:"),
    SEAT_CAPACITY("Seating Capacity:"),
    TRANSMISSION("Transmission:"),
    VIN("V.I.N:"),
    WHEELBASE("Wheelbase:"),
    WIDTH("Width:");
 
    private final String label;
    
    PdfVehicleDetailLabels(String _label) {
      label = _label;
    }
    
    public String toValue() {
      return label;
    }
    
    @Override 
    public String toString() {
      throw new UnsupportedOperationException();
    }
  }
  
  public enum VehicleDetails {
    FEATURES_ADDITIONAL(VehicleOption.ADDITIONAL),
    FEATURES_ENGINEERING(VehicleOption.ENGINEERING),
    FEATURES_EXTERIOR(VehicleOption.EXTERIOR),
    FEATURES_INTERIOR(VehicleOption.INTERIOR),
    FEATURES_SAFE(VehicleOption.SAFETY),
    LABEL_ADDITIONAL("* ------ ADDITIONAL FEATURES ------"),
    LABEL_ENGINEERING("* ------ ENGINEERING FEATURES ------"),
    LABEL_EXTERIOR("* ------ EXTERIOR FEATURES ------"),
    LABEL_INTERIOR("* ------ INTERIOR FEATURES ------"),
    LABEL_SAFETY("* ------ SAFETY FEATURES ------");
    
    private final String value;
    
    VehicleDetails(String _value) {
      value =  _value;
    }
    
    public String toValue() {
      return value;
    }
    
    @Override 
    public String toString() {
      throw new UnsupportedOperationException();
    }
  }
  
  /*public enum TitleCheckResources {
    ENCODING_TYPE("UTF-8"),
    //EXPERIAN_URL("http://internal-twg-production.lb.anypointdns.net/experian-api/vehicle-info"),
    //CLIENT_ID("1c03d9b049a84a4986d76d0ddba36a0b"),
    CLIENT_ID(System.getProperty("qcert.experian.url.clientId")),
    CLIENT_SECRET(System.getProperty("qcert.experian.url.clientSecret")),
    //CLIENT_SECRET("0e1c614D5da24207A4F753c9cec6D14F"),
    EXPERIAN_URL_PROTOCOL(System.getProperty("qcert.experian.url.protocol")),
    EXPERIAN_URL_HOSTNAME(System.getProperty("qcert.experian.url.hostname")),
    EXPERIAN_URL_PATH(System.getProperty("qcert.experian.url.path")),
    //PWD("44qmXqp6av"),
    RPT_LEVEL("indicators"),
    RPT_LEVEL_FULL("full");
    //UID("5450006");
   
    private final String value;
    
    TitleCheckResources(String _value) {
      value = _value;
    }
    
    public String toValue() {
      return value;
    }
    
    @Override 
    public String toString() {
      throw new UnsupportedOperationException();
    }
  }*/
  
  public enum PdfCallingMethods {
    BROCHURE_PDF("B"),
    DETAILS_PDF("D"),
    ENCYCLOPEDIA("Encyclopedia"),
    PEDIGREE("Pedigree");
   
    private final String value;
    
    PdfCallingMethods(String _value) {
      value = _value;
    }
    
    public String toValue() {
      return value;
    }
    

    @Override 
    public String toString() {
      throw new UnsupportedOperationException();
    }
  }
  
 /* public static String getEpaDescText() {
    final StringBuilder stringBuilder = new StringBuilder("");
    stringBuilder.append("EPA mileage estimates are for newly manufactured vehicles. Actual ");
    stringBuilder.append("mileage may vary with driving habits, road and vehicle conditions ");
    stringBuilder.append("and vehicle age. Vehicle data, specifications and equipment ");
    stringBuilder.append("information are compiled from publicly available sources believed ");
    stringBuilder.append("by IDI to be reliable. Data is subject to change without notice. ");
    stringBuilder.append("IDI assumes no responsibility for errors and omissions in the ");
    stringBuilder.append("compilation of this data. Window Stickers are prepared by the ");
    stringBuilder.append("dealer solely for the dealer's use and convenience. IDI makes ");
    stringBuilder.append("no representations, express or implied, to any actual or ");
    stringBuilder.append("prospective purchaser of this vehicle as to the existense, ");
    stringBuilder.append("ownership, description, or condition of this vehicle, listed ");
    stringBuilder.append("equipment, accessories, price or warranties. ANY AND ALL ");
    stringBuilder.append("DIFFERENCES BETWEEN THE FOREGOING AND THE ACTUAL VEHICLE ITSELF ");
    stringBuilder.append("AND/OR ANY WARRANTIES OFFERED MUST  BE ADDRESSED PRIOR TO THE SALE ");
    stringBuilder.append("OF THIS VEHICLE.");
    return stringBuilder.toString();
  }*/
  
  private static String COPYRIGHT = "\u00a9";

  public static String getVehicleFooterText() {
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    final StringBuilder stringBuilder = new StringBuilder("");
    stringBuilder.append(COPYRIGHT);
    stringBuilder.append(" ");
    stringBuilder.append(String.valueOf(currentYear));
    stringBuilder.append(" TWD. Use without license by TWD is prohibited. Vehicle data, ");
    stringBuilder.append("specifications and equipment information are compiles from publicly ");
    stringBuilder.append("available sources believed by TWD to be reliable. Data is subject to ");
    stringBuilder.append("change without notice. TWD assumes no responsibility for errors and ");
    stringBuilder.append("omissions in the compilation of data. Window Stickers are prepared by ");
    stringBuilder.append("the dealer solely by the dealer's use and convenience. TWD makes no ");
    stringBuilder.append("representations, express or implied, to any actual prospective user or ");
    stringBuilder.append("owner of this vehicle as to existence, ownership, description, or ");
    stringBuilder.append("condition of the vehicle, listed equipment, accessories, price or ");
    stringBuilder.append("warranties. Fuel economy estimates are based on newly manufactured ");
    stringBuilder.append("vehicles. Actual fuel economy may vary with vehicle condition/age and ");
    stringBuilder.append("driving conditions. ANY AND ALL DIFFERENCES BETWEEN THE FOREGOING AND ");
    stringBuilder.append("THE ACTUAL VEHICLE ITSELF AND/OR ANY WARRANTIES OFFRED MUST BE ");
    stringBuilder.append("ADDRESSED PRIOR TO THE SALE OF THIS VEHICLE."); 
    return stringBuilder.toString();
  }
  
  private QcertCorePdfConstants() {}
}
