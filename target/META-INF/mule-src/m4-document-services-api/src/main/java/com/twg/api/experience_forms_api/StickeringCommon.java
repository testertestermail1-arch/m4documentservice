/*
 * Copyright (c) 2008 The Warranty Group.
 *
 * This software is the confidential and proprietary information of The Warranty Group 
 * (http://www.thewarrantygroup.com/). You shall not disclose such Confidential Information or use 
 * it without authorization from The Warranty Group.
 */
package com.twg.api.experience_forms_api;

import java.io.File;

public class StickeringCommon {

  public String getPdfAbsoluteFileName(String fileName) {
    return "src/main/resources/" + fileName + ".pdf";
  }

  public String getPdfTemplateAbsoluteFileName(String fileName) {
   
    ClassLoader classLoader = new StickeringCommon().getClass().getClassLoader();
	final File inputfile = new File(classLoader.getResource(fileName+".pdf").getFile());
	String file = inputfile.toString();
	return file;
  }
}
