package com.twg.api.service_forms.constants;

public enum StampSignature {
	
	TRUE,
	FALSE;
	

	public static boolean contains(String s)
	  {
	      for(StampSignature field:values())
	           if (field.name().equals(s)) 
	              return true;
	      return false;
	  } 

}
