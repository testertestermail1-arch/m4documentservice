/*
 * Created on Jul 24, 2006
 * $Id: UtilityObjects.java 1906 2012-06-18 19:12:56Z raul.cruz $
 */
package com.aon.awg.qcert.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class UtilityObjects {


 public static String convertToYESNO(String _str){
     if (_str .equals("Y")) {
         return "YES";
     }
     return "NO";
 }

 

public static BigDecimal nullToZero(BigDecimal val){
		
	return val==null?new BigDecimal("0.00"):val;
}



public static String nullToEmpty(String val){
	
	return val!=null?val:"";
}


public static String nullToNotAvail(String val){
	if (val == null || "".equals(val))
	   return "Not Available";
	return val;
}

public static String nullToNA(String val){
	if (val == null || "".equals(val))
	   return "NA";
	return val;
}

public static String formatAS400Date(Date date, int length) {
	 String formattedDate = new String();
	 if (date != null) {
		SimpleDateFormat sdf = null;
		if (length == 8) {
		 sdf = new SimpleDateFormat("yyyyMMdd");
		 formattedDate = sdf.format(date);
		} else if (length == 7) {
			sdf = new SimpleDateFormat("yyMMdd");
			formattedDate = sdf.format(date);
			Calendar calDate = Calendar.getInstance();	
			calDate.setTime(date);
			if (calDate.get(Calendar.YEAR) > 1999 && calDate.get(Calendar.YEAR) < 2100) {
				formattedDate = "1" + formattedDate;
				}
				
		}
		
	}
	return formattedDate;

}


public static String getXMLValue(String _xmlString, String _tag,
			String _element) {
	String elementValue = "";
	try {
		InputStream vehicleReportXML = new ByteArrayInputStream(
				_xmlString.getBytes());
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(vehicleReportXML);
		Element elem = doc.getDocumentElement();
		NodeList nl = elem.getElementsByTagName(_tag);
		Element vinDecode = (Element) nl.item(0);
		elementValue = vinDecode.getAttribute(_element);
		return elementValue;
	} catch (Exception e) {

	}
	return "";
}

 public static String getFormattedDate(String dt,String inputformat,String outformat) {
 	try{
 	System.out.println("Date for parsing...."+ dt +":"+inputformat+":"+outformat);
 	SimpleDateFormat sdfInput   =   new SimpleDateFormat() ;  
     SimpleDateFormat sdfOutput = new SimpleDateFormat(outformat);
     //DateFormat dtf = DateFormat.getDateInstance();
     //Date date=dtf.parse(inputformat);
     sdfInput.applyLocalizedPattern(inputformat);
       Date date = sdfInput.parse (inputformat) ;  
   	//return sdfOutput.format(date);
     return sdfOutput.format(date);
 	} catch (Exception e){
 	e.printStackTrace();

 	}
 	return "";
  }
 
 public static String buildPadding(int width,String stringToPad,String pad,String paddingStyle){

 	int curStrLen= stringToPad.length();
 	int padWidth = width - curStrLen;
 	String paddedString = null;
 	StringBuffer sbuf = new StringBuffer(padWidth);

 	for (int i = 0; i < padWidth; ++i){

 		sbuf.append(pad);
 	}
 	
 	if (paddingStyle.equals("L")){
 		paddedString =sbuf.toString()+ stringToPad;
 	}else{
 		paddedString = stringToPad + sbuf.toString();
 	}
 	return paddedString;
}
 
 public static String formatPhoneNo(String _phoneNo){
 String formattedPhoneNo=_phoneNo;
 String  areaCode="";
 String phNoSub1,phNoSub2="";
 
 if (!"Not Available".equals(_phoneNo)){
	 if (_phoneNo.length()==10){
	 	areaCode = _phoneNo.substring(0,3);
	 	phNoSub1=_phoneNo.substring(3,6);
	 	phNoSub2=_phoneNo.substring(6);
	 	formattedPhoneNo ="("+areaCode+") "+phNoSub1+"-"+phNoSub2;
	 	//System.out.println("Area Code" + areaCode+" " + "phno :" + phNoSub1 +":"+phNoSub2);
	  }
 }
 
 return formattedPhoneNo;
 }
 
    
	//check for the empty Or null or empty charecter
	public static String checkforEmpty(String value) {
		if(("").equals(value) || value.isEmpty() )
		{
			return "NO";
		}
		return value;
	}
	
	
	
	public static String checkforValue(String value) {
		if(("").equals(value) || null == value || "N".equalsIgnoreCase(value))
		{
			return "NO";
		}
		return value;
	}  
}
