package com.twg.api.service_forms.constants;

public enum UrlOnly {
	
	N,
	Y;
	
	  public static boolean contains(String s)
	  {
	      for(UrlOnly field:values())
	           if (field.name().equals(s)) 
	              return true;
	      return false;
	  } 

}
