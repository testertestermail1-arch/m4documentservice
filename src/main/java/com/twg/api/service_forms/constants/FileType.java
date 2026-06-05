package com.twg.api.service_forms.constants;

public enum FileType {

	pdf,
	gzip;
	
	  public static boolean contains(String s)
	  {
	      for(FileType field:values())
	           if (field.name().equals(s)) 
	              return true;
	      return false;
	  } 
}
