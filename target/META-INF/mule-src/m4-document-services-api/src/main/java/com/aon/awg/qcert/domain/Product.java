/*
 * Copyright (c) 2005 The Warranty Group.
 *
 * This software is the confidential and proprietary information of The Warranty Group 
 * (http://www.thewarrantygroup.com/). You shall not disclose such Confidential Information or use 
 * it without authorization from The Warranty Group.
 */
package com.aon.awg.qcert.domain;

import java.io.Serializable;


public class Product implements Serializable {

  private static final long serialVersionUID = 1L;

  private String agrFormNo; 
  private String termMonths; 
  private String termMiles;  
  private String formDeductible; 
  private boolean addOnMile; 
  private String platinumMiles; 
  private String platinumMonths; 
 
  
  public Product() {
  }

  
  public String getPlatinumMiles() {
	  return platinumMiles;
  }
	
  public void setPlatinumMiles(String platinumMiles) {
	  this.platinumMiles = platinumMiles;
  }
	
  public String getPlatinumMonths() {
	  return platinumMonths;
  }
  
  public void setPlatinumMonths(String platinumMonths) {
		this.platinumMonths = platinumMonths;
  }

 

  public String getAgrFormNo() {
    return agrFormNo;
  }

  public void setAgrFormNo(String _agrFormNo) {
    agrFormNo = _agrFormNo;
  }


  public String getTermMonths() {
    return termMonths;
  }

  public void setTermMonths(String _termMonths) {
    termMonths = _termMonths;
  }

  public String getTermMiles() {
    return termMiles;
  }

  public void setTermMiles(String _termMiles) {
    termMiles = _termMiles;
  }
  public String getFormDeductible() {
    return formDeductible;
  }

  public void setFormDeductible(String _formDeductible) {
    formDeductible = _formDeductible;
  }
  
  public boolean isAddOnMile() {
	  return addOnMile;
  }

  public void setAddOnMile(boolean addOnMile) {
	  this.addOnMile = addOnMile;
  }
}
