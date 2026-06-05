/*
 * Copyright (c) 2006 The Warranty Group.
 *
 * This software is the confidential and proprietary information of The Warranty Group 
 * (http://www.thewarrantygroup.com/). You shall not disclose such Confidential Information or use 
 * it without authorization from The Warranty Group.
 */
package com.aon.awg.qcert.businesslayer;

import java.io.Serializable;


public class VehiclePDFOptions implements Serializable {

  private static final long serialVersionUID = 1L;

  private static String DEFAULT_VALUE = "";
  
  private String interiorfeatures = DEFAULT_VALUE;
  private String exteriorfeatures = DEFAULT_VALUE;
  private String safetyfeatures = DEFAULT_VALUE;
  private String engineeringfeatures = DEFAULT_VALUE;
  private String addedfeatures = DEFAULT_VALUE;

  public String getIntFeaturesForPdf() {
    return interiorfeatures.toString();
  }

  public void setIntFeaturesForPdf(String _intFeatures) {
    interiorfeatures = _intFeatures;
  }

  public String getExtFeaturesForPdf() {
    return exteriorfeatures;
  }

  public void setExtFeaturesForPdf(String _exteriorFeatures) {
    exteriorfeatures = _exteriorFeatures;
  }

  public String getEngFeaturesForPdf() {
    return engineeringfeatures;
  }

  public void setEngFeaturesForPdf(String _engFeatures) {
    engineeringfeatures = _engFeatures;
  }

  public String getSafetyFeaturesForPdf() {
    return safetyfeatures;
  }

  public void setSafetyFeaturesForPdf(String _safetyFeatures) {
    safetyfeatures = _safetyFeatures;
  }

  public String getAddedFeaturesForPdf() {
    return addedfeatures;
  }

  public void setAddedFeaturesForPdf(String _addedFeatures) {
    addedfeatures = _addedFeatures;
  }
}
