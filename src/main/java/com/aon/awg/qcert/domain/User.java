/*
 * Copyright (c) 2005 The Warranty Group.
 *
 * This software is the confidential and proprietary information of The Warranty Group 
 * (http://www.thewarrantygroup.com/). You shall not disclose such Confidential Information or use 
 * it without authorization from The Warranty Group.
 */
package com.aon.awg.qcert.domain;

import java.io.Serializable;

public class User implements Serializable {
  

  private static final long serialVersionUID = 1L;
  public static final String QCERTROL_DEALER_USER = "hasQCertDealerUserAccess";
  public static final String QCERTROL_DEALER_ADMIN = "hasQCertDealerAdminAccess";
  public static final String QCERTROL_INTERNAL_ADMIN = "hasQCertInternalAdminAccess";
  public static final String DRS_SITE_ADMINISTRATION = "hasSiteAdministration";

  private long id;
  private Dealer dealer;
  private String email;

//  private transient com.aon.awg.drs.access.businesslayer.User externalUser;
  private boolean isFromQCertDealer;


  public long getId() {
    return id;
  }

  public void setId(long _id) {
    id = _id;
  }

  public Dealer getDealer() {
    return dealer;
  }

  public void setDealer(Dealer _dealer) {
    dealer = _dealer;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String _email) {
    email = _email;
  }

 
  public void setHasSiteAdministration(final boolean b) {
    // must be empty. required by JSF.
  }



  public boolean isFromQCertDealer() {
    return isFromQCertDealer;
  }

  public void setFromQCertDealer(boolean _isFromQCertDealer) {
    isFromQCertDealer = _isFromQCertDealer;
  }

}
