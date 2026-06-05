/*
 * Copyright (c) 2005 The Warranty Group.
 *
 * This software is the confidential and proprietary information of The Warranty Group 
 * (http://www.thewarrantygroup.com/). You shall not disclose such Confidential Information or use 
 * it without authorization from The Warranty Group.
 */
package com.aon.awg.qcert.domain;

import java.io.Serializable;

public class DealerProduct implements Serializable {

  private static final long serialVersionUID = 1L;
  private Product product;
 
  
  public DealerProduct() {
  }


  public Product getProduct() {
    return product;
  }

  public void setProduct(Product _product) {
    product = _product;
  }

}