/*
 * Copyright (c) 2005 The Warranty Group.
 *
 * This software is the confidential and proprietary information of The Warranty Group 
 * (http://www.thewarrantygroup.com/). You shall not disclose such Confidential Information or use 
 * it without authorization from The Warranty Group.
 */
package com.aon.awg.qcert.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;


import com.aon.awg.qcert.domain.vehicle.Vehicle;
import com.aon.awg.qcert.domain.vehicle.VehicleOption;


public class Inventory implements Serializable {

  private static final long serialVersionUID = 1L;

  public static final String STOCK_TYPE_USED = "U";
  public static final String STOCK_TYPE_NEW = "N";

  

  private long id;
  private String stockNo;
  private Date dateReceived;
  private Date dateStatusChanged;
  private Date lastUpdate;
  private Dealer dealer;

  private BigDecimal bookValue;
  private BigDecimal listPrice;
  private BigDecimal priceModifier1;
  private BigDecimal priceModifier2;
  private BigDecimal priceModifier3;
  private BigDecimal priceModifier4;
  private BigDecimal priceModifier5;

  //TODO(nahuel): Declare these constants in their own enum.
  public static final Integer PRINT_PRICES_ON_STICKER_FALSE = Integer.valueOf(0);
  public static final Integer PRINT_PRICES_ON_STICKER_TRUE = Integer.valueOf(1);
  public static final Integer PRINT_PRICES_ON_STICKER_DEFAULT = Integer.valueOf(2);
  private Integer printPrices;

  private boolean postOnWebsite;

  private boolean stickerGenerated;
  private Date lastDateStickerGenerated;
  private User userSticker;

  private boolean brochureGenerated;
  private Date lastDateBrochureGenerated;
  private User userBrochure;

  private boolean buyersGuideGenerated;
  private Date lastDateBuyersGuideGenerated;
  private User userBuyersGuide;

 

  private Date dateAdded;
  private Date dateModified;
 
  private User userAdded;
  private User userModified;
  private User userStatusChanged;
  private String stockType;
  private int mileage;
  private DealerProduct dealerProduct;
  private Vehicle vehicle;
  private boolean multipleVinMatches;

  private long duplicateFlag;

  //Presentation.
  private boolean toSticker;
  private int group;

  public Inventory() {
  }


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public Date getDateReceived() {
    return dateReceived;
  }

  public void setDateReceived(Date dateReceived) {
    this.dateReceived = dateReceived;
  }


  public Date getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
  }


  public String getStockNo() {
    return stockNo;
  }

  public void setStockNo(String stockNo) {
    this.stockNo = stockNo;
  }


  public Dealer getDealer() {
    return dealer;
  }

  public void setDealer(Dealer dealer) {
    this.dealer = dealer;
  }


  public Vehicle getVehicle() {
    return vehicle;
  }

  public void setVehicle(Vehicle vehicle) {
    this.vehicle = vehicle;
  }


  public int getMileage() {
    return mileage;
  }

  public void setMileage(int _mileage) {
    mileage = _mileage;
  }

  public String getMake() {
    if (getVehicle() == null) {
      return "";
    }
    return getVehicle().getModel().getMake().getDescription();
  }

  public String getModel() {
    if (getVehicle() == null) {
      return "";
    }
    return getVehicle().getModel().getDescription();
  }

  public String getVin() {
    if (getVehicle() == null) {
      return "";
    }
    return getVehicle().getVin();
  }

 


  public Integer getYear() {
    if (getVehicle() == null) {
      return null;
    }
    return getVehicle().getYear();
  }

  public String toString() {
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Stock #:").append(stockNo).append(" - Date:").append(dateReceived);
    return stringBuilder.toString();
  }


  public Date getDateStatusChanged() {
    return dateStatusChanged;
  }

  public void setDateStatusChanged(Date _dateStatusChanged) {
    dateStatusChanged = _dateStatusChanged;
  }


  public DealerProduct getDealerProduct() {
    return dealerProduct;
  }

  public void setDealerProduct(DealerProduct _dealerProduct) {
    dealerProduct = _dealerProduct;
  }


  public Date getLastDateStickerGenerated() {
    return lastDateStickerGenerated;
  }

  public void setLastDateStickerGenerated(Date _lastDateStickerGenerated) {
    lastDateStickerGenerated = _lastDateStickerGenerated;
  }


  public User getUserSticker() {
    return userSticker;
  }

  public void setUserSticker(User _userSticker) {
    userSticker = _userSticker;
  }


 
  public boolean isStickerGenerated() {
    return stickerGenerated;
  }

  public void setStickerGenerated(boolean _stickerGenerated) {
    stickerGenerated = _stickerGenerated;
  }


  public BigDecimal getFinalPrice() {
    BigDecimal finalPrice = getListPrice();
    if (finalPrice != null) {
      if (getPriceModifier1() != null) {
        finalPrice = finalPrice.add(getDealer().getPriceModifier1Sign() == 1 ? getPriceModifier1()
            : getPriceModifier1().negate());
      }
      if (getPriceModifier2() != null) {
        finalPrice = finalPrice.add(getDealer().getPriceModifier2Sign() == 1 ? getPriceModifier2()
            : getPriceModifier2().negate());
      }
      if (getPriceModifier3() != null) {
        finalPrice = finalPrice.add(getDealer().getPriceModifier3Sign() == 1 ? getPriceModifier3()
            : getPriceModifier3().negate());
      }
      if (getPriceModifier4() != null) {
        finalPrice = finalPrice.add(getDealer().getPriceModifier4Sign() == 1 ? getPriceModifier4()
            : getPriceModifier4().negate());
      }
      if (getPriceModifier5() != null) {
        finalPrice = finalPrice.add(getDealer().getPriceModifier5Sign() == 1 ? getPriceModifier5()
            : getPriceModifier5().negate());
      }
    }
    return finalPrice;
  }


  public BigDecimal getBookValue() {
    return bookValue;
  }

  public void setBookValue(BigDecimal _bookValue) {
    bookValue = _bookValue;
  }


  public BigDecimal getListPrice() {
    return listPrice;
  }

  public void setListPrice(BigDecimal _listPrice) {
    listPrice = _listPrice;
  }


  public BigDecimal getPriceModifier1() {
    return priceModifier1;
  }

  public void setPriceModifier1(BigDecimal _priceModifier1) {
    priceModifier1 = _priceModifier1;
  }


  public BigDecimal getPriceModifier2() {
    return priceModifier2;
  }

  public void setPriceModifier2(BigDecimal _priceModifier2) {
    priceModifier2 = _priceModifier2;
  }


  public BigDecimal getPriceModifier3() {
    return priceModifier3;
  }

  public void setPriceModifier3(BigDecimal _priceModifier3) {
    priceModifier3 = _priceModifier3;
  }


  public BigDecimal getPriceModifier4() {
    return priceModifier4;
  }

  public void setPriceModifier4(BigDecimal _priceModifier4) {
    priceModifier4 = _priceModifier4;
  }


  public BigDecimal getPriceModifier5() {
    return priceModifier5;
  }

  public void setPriceModifier5(BigDecimal _priceModifier5) {
    priceModifier5 = _priceModifier5;
  }


  public boolean isPostOnWebsite() {
    return postOnWebsite;
  }

  public void setPostOnWebsite(boolean _postOnWebsite) {
    postOnWebsite = _postOnWebsite;
  }


  public Integer getPrintPrices() {
    return printPrices;
  }

  public void setPrintPrices(Integer _printPrices) {
    printPrices = _printPrices;
  }


  public static Inventory createBlank() {
    final Inventory inventory = new Inventory();
    inventory.setPrintPrices(PRINT_PRICES_ON_STICKER_DEFAULT);
    inventory.setVehicle(new Vehicle());
    return inventory;
  }


  public boolean isBrochureGenerated() {
    return brochureGenerated;
  }

  public void setBrochureGenerated(boolean _brochureGenerated) {
    brochureGenerated = _brochureGenerated;
  }


  public boolean isBuyersGuideGenerated() {
    return buyersGuideGenerated;
  }

  public void setBuyersGuideGenerated(boolean _buyersGuideGenerated) {
    buyersGuideGenerated = _buyersGuideGenerated;
  }


  public Date getDateAdded() {
    return dateAdded;
  }

  public void setDateAdded(Date _dateAdded) {
    dateAdded = _dateAdded;
  }


  public Date getDateModified() {
    return dateModified;
  }

  public void setDateModified(Date _dateModified) {
    dateModified = _dateModified;
  }

 
  public User getUserAdded() {
    return userAdded;
  }

  public void setUserAdded(User _userAdded) {
    userAdded = _userAdded;
  }


  public User getUserModified() {
    return userModified;
  }

  public void setUserModified(User _userModified) {
    userModified = _userModified;
  }


  public User getUserStatusChanged() {
    return userStatusChanged;
  }

  public void setUserStatusChanged(User _userStatusChanged) {
    userStatusChanged = _userStatusChanged;
  }


  


  public String getStockType() {
    return stockType;
  }

  public void setStockType(String _stockType) {
    stockType = _stockType;
  }


  public long getDuplicateFlag() {
    return duplicateFlag;
  }

  public void setDuplicateFlag(long _duplicateFlag) {
    duplicateFlag = _duplicateFlag;
  }


 

  public boolean isToSticker() {
    return toSticker;
  }

  public void setToSticker(boolean _toSticker) {
    toSticker = _toSticker;
  }


  public int getGroup() {
    return group;
  }

  public void setGroup(int _group) {
    group = _group;
  }


  public Date getLastDateBrochureGenerated() {
    return lastDateBrochureGenerated;
  }

  public void setLastDateBrochureGenerated(Date _lastDateBrochureGenerated) {
    lastDateBrochureGenerated = _lastDateBrochureGenerated;
  }


  public Date getLastDateBuyersGuideGenerated() {
    return lastDateBuyersGuideGenerated;
  }

  public void setLastDateBuyersGuideGenerated(Date _lastDateBuyersGuideGenerated) {
    lastDateBuyersGuideGenerated = _lastDateBuyersGuideGenerated;
  }


  public User getUserBrochure() {
    return userBrochure;
  }

  public void setUserBrochure(User _userBrochure) {
    userBrochure = _userBrochure;
  }


  public User getUserBuyersGuide() {
    return userBuyersGuide;
  }

  public void setUserBuyersGuide(User _userBuyersGuide) {
    userBuyersGuide = _userBuyersGuide;
  }


  public Collection getOptions(final String _type) {
    return CollectionUtils.select(getVehicle().getOptions(), new Predicate() {
      public boolean evaluate(Object _arg0) {
        return _type.equals(((VehicleOption) _arg0).getType());
      }
    });
  }


  public Collection getOptions(final String _type, final boolean _selected) {
    return CollectionUtils.select(getVehicle().getOptions(), new Predicate() {
      public boolean evaluate(Object _arg0) {
        VehicleOption option = (VehicleOption) _arg0;
        return _type.equals(option.getType()) && _selected == option.isSelected();
      }
    });
  }


  public boolean isMultipleVinMatches() {
    return multipleVinMatches;
  }

  public void setMultipleVinMatches(boolean _multipleVinMatches) {
    multipleVinMatches = _multipleVinMatches;
  }


  public String getPrintPricesDefaultValue() {
    final String val = this.dealer.isPrintPricesSticker() == true ? "YES" : "NO";

    return "Default: " + val;
  }

  public boolean equals(Object _obj) {
    if (this == _obj)
      return true;
    if (!(_obj instanceof Inventory))
      return false;
    final Inventory that = (Inventory) _obj;
    return this.getId() == that.getId();
  }

  public int hashCode() {
    return (int) getId();
  }
}
