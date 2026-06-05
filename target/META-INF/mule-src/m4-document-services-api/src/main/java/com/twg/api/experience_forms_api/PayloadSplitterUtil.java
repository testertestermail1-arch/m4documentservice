package com.twg.api.experience_forms_api;

import java.util.ArrayList;
import java.util.List;

public class PayloadSplitterUtil {
	
public List<String> splitString(String inputpayload) {
		
		
		String inputpayload1=(String)inputpayload; 
		
		
		
		String payloadPart1 = inputpayload1;
		String payloadPart2 = "";
		
		if(inputpayload1.length() > 2000) {
			payloadPart1 = inputpayload1.substring(0,2000);
			payloadPart2 = inputpayload1.substring(2000);
		}

		List<String> payloadList=new ArrayList<String>();
		
		payloadList.add(payloadPart1);
		payloadList.add(payloadPart2);
		
		return payloadList;
	}

}
