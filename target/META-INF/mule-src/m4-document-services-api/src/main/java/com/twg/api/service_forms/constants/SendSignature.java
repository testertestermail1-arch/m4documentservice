package com.twg.api.service_forms.constants;

public enum SendSignature {
	
	TRUE,
	FALSE;
	

	public static boolean contains(String s)
	  {
	      for(SendSignature field:values())
	           if (field.name().equals(s)) 
	              return true;
	      return false;
	  } 

}
