/*
 * Copyright (c) 2005 The Warranty Group. This software is the confidential and proprietary
 * information of The Warranty Group (http://www.thewarrantygroup.com/). You shall not disclose such
 * Confidential Information or use it without authorization from The Warranty Group.
 */
package com.aon.awg.qcert.domain;

import java.io.Serializable;

public class Dealer implements Serializable {


  private static final long serialVersionUID = 1763577488949824369L;

  public static final int DEAL_NO_STRATEGY = 1;
  public static final int STOCK_NO_STRATEGY = 2;
  private String name;
  private String stickerName;
  private String address;
  private String city;
  private String State;
  private String zip;
  private String salesPhone;

 
  private boolean printPrices;
  private String bookValueLabel;
  private String listPriceLabel;
  private String finalPriceLabel;
  private String priceModifier1Label;
  private String priceModifier2Label;
  private String priceModifier3Label;
  private String priceModifier4Label;
  private String priceModifier5Label;


  private String websiteURL;
  private boolean displayAddress;
  private boolean displayListPrice;
  private boolean printPricesSticker = false;

  /*
   * priceModifier#Sign options: -1: discount 1: addition
   */
  private int priceModifier1Sign;
  private int priceModifier2Sign;
  private int priceModifier3Sign;
  private int priceModifier4Sign;
  private int priceModifier5Sign;
  private String windowStickerFreeFormData;
  


  public Dealer() {
  }

  public String getSalesPhone() {
    return salesPhone;
  }

  public void setSalesPhone(String _salesPhone) {
    salesPhone = _salesPhone;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStickerName() {
    if (stickerName == null || stickerName.equals("")) {
      return name;
    }
    return stickerName;
  }

  public void setStickerName(String _stickerName) {
    stickerName = _stickerName;
  }


  public boolean isPrintPrices() {
    return printPrices;
  }

  public void setPrintPrices(boolean _printPrices) {
    printPrices = _printPrices;
  }


  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }


  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }


  public String getState() {
    return State;
  }

  public void setState(String state) {
    State = state;
  }


  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }


  public int getPriceModifier1Sign() {
    return priceModifier1Sign;
  }

  public void setPriceModifier1Sign(int _priceModifier1Sign) {
    priceModifier1Sign = _priceModifier1Sign;
  }


  public int getPriceModifier2Sign() {
    return priceModifier2Sign;
  }

  public void setPriceModifier2Sign(int _priceModifier2Sign) {
    priceModifier2Sign = _priceModifier2Sign;
  }


  public int getPriceModifier3Sign() {
    return priceModifier3Sign;
  }

  public void setPriceModifier3Sign(int _priceModifier3Sign) {
    priceModifier3Sign = _priceModifier3Sign;
  }


  public int getPriceModifier4Sign() {
    return priceModifier4Sign;
  }

  public void setPriceModifier4Sign(int _priceModifier4Sign) {
    priceModifier4Sign = _priceModifier4Sign;
  }

  public int getPriceModifier5Sign() {
    return priceModifier5Sign;
  }

  public void setPriceModifier5Sign(int _priceModifier5Sign) {
    priceModifier5Sign = _priceModifier5Sign;
  }

  public String getBookValueLabel() {
    return bookValueLabel;
  }

  public void setBookValueLabel(String _bookValueLabel) {
    bookValueLabel = _bookValueLabel;
  }

  public String getFinalPriceLabel() {
    return finalPriceLabel;
  }

  public void setFinalPriceLabel(String _finalPriceLabel) {
    finalPriceLabel = _finalPriceLabel;
  }

  public String getListPriceLabel() {
    return listPriceLabel;
  }

  public void setListPriceLabel(String _listPriceLabel) {
    listPriceLabel = _listPriceLabel;
  }

 

  public String getPriceModifier1Label() {
    return priceModifier1Label;
  }

  public void setPriceModifier1Label(String _priceModifier1Label) {
    priceModifier1Label = _priceModifier1Label;
  }

  public String getPriceModifier2Label() {
    return priceModifier2Label;
  }

  public void setPriceModifier2Label(String _priceModifier2Label) {
    priceModifier2Label = _priceModifier2Label;
  }

  public String getPriceModifier3Label() {
    return priceModifier3Label;
  }

  public void setPriceModifier3Label(String _priceModifier3Label) {
    priceModifier3Label = _priceModifier3Label;
  }


  public String getPriceModifier4Label() {
    return priceModifier4Label;
  }

  public void setPriceModifier4Label(String _priceModifier4Label) {
    priceModifier4Label = _priceModifier4Label;
  }

  public String getPriceModifier5Label() {
    return priceModifier5Label;
  }

  public void setPriceModifier5Label(String _priceModifier5Label) {
    priceModifier5Label = _priceModifier5Label;
  }

 

  public String getWebsiteURL() {
    return websiteURL;
  }

  public void setWebsiteURL(String _websiteURL) {
    websiteURL = _websiteURL;
  }

  public boolean isDisplayAddress() {
    return displayAddress;
  }

  public void setDisplayAddress(boolean _displayAddress) {
    displayAddress = _displayAddress;
  }


  public String toString() {
    return getName();
  }

 
  public boolean isDisplayListPrice() {
    return displayListPrice;
  }

  public void setDisplayListPrice(boolean _displayListPrice) {
    displayListPrice = _displayListPrice;
  }

  public boolean isPrintPricesSticker() {
    return printPricesSticker;
  }

  public void setPrintPricesSticker(boolean printPricesSticker) {
    this.printPricesSticker = printPricesSticker;
  }

 
  public String getWindowStickerFreeFormData() {
    return windowStickerFreeFormData;
  }

  public void setWindowStickerFreeFormData(final String _windowStickerFreeFormData) {
    windowStickerFreeFormData = _windowStickerFreeFormData;
  }
  
  
}
