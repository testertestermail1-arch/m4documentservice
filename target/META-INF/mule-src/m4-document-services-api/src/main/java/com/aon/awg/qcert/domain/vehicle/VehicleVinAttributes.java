/*
 * Copyright (c) 2006 The Warranty Group.
 *
 * This software is the confidential and proprietary information of The Warranty Group 
 * (http://www.thewarrantygroup.com/). You shall not disclose such Confidential Information or use 
 * it without authorization from The Warranty Group.
 */
package com.aon.awg.qcert.domain.vehicle;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aon.awg.qcert.businesslayer.VehicleVinDetails;

import com.aon.awg.qcert.utils.UtilityObjects;


public class VehicleVinAttributes implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private static final String BLANK = " ";

  // TODO(nahuel): Use enum instead.
  public static final String KEY_TRIM = "trim";
  public static final String KEY_BODY_STYLE = "bodystyle";
  public static final String KEY_TRANSMISSION = "trans";
  
  public static final String[] ALTERNATIVES_PROPERTIES 
      = { KEY_TRIM, KEY_BODY_STYLE, KEY_TRANSMISSION };

  // Db column size to store drop-downs values. 
  private static final int ALTERNATIVES_MAX_LENGTH = 1500; 
  
  private static final String ALTERNATIVES_FIELD_SEP = "|";
  private static final String ALTERNATIVES_FIELD_SEP_PATTERN = "\\|";
  private static final String ALTERNATIVES_ROW_SEP = "*";
  private static final String ALTERNATIVES_ROW_SEP_PATTERN = "\\*";

  private static final Log LOG = LogFactory.getLog(VehicleVinAttributes.class);

  private long id;

  private String alternatives;
  private String carburetion;
  private String country;
  private String cylinder;
  private String city;
  private String driveTrain;
  private String bodyStyle;
  private String doorct;
  private String easpiration;
  private String eblockType;
  private String eheadConfig;
  private String encyclopedia;
  private String esize;
  private String fuel;
  private String gas;
  private String height;
  private String hwy;
  private String length;
  private String matchKey;
  private String pedigree;
  private String price;
  private String seriesname;
  private String trim;
  private String vehicleType;
  private String wheelBase;
  private String seats;
  private String transmission;
  private String weight;
  private String width;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getBodystyle() {
    return bodyStyle;
  }

  public void setBodystyle(String _bodyStyle) {
    bodyStyle = _bodyStyle;
  }


  public String getCarburetion() {
    return carburetion;
  }

  public void setCarburetion(String _carburetion) {
    carburetion = _carburetion;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String _city) {
    city = _city;
  }


  public String getCountry() {
    return country;
  }

  public void setCountry(String _country) {
    country = _country;
  }


  public String getCylinder() {
    return cylinder;
  }

  public void setCylinder(String _cylinder) {
    cylinder = _cylinder;
  }

  public String getDoorct() {
    return doorct;
  }

  public void setDoorct(String _doorct) {
    doorct = _doorct;
  }


  public String getDrivetrain() {
    return driveTrain;
  }

  public void setDrivetrain(String _driveTrain) {
    driveTrain = _driveTrain;
  }

  public String getEaspiration() {
    return easpiration;
  }

  public void setEaspiration(String _easpiration) {
    easpiration = _easpiration;
  }

  public String getEblocktype() {
    return eblockType;
  }

  public void setEblocktype(String _eblockType) {
    eblockType = _eblockType;
  }

  public String getEheadconfig() {
    return eheadConfig;
  }

  public void setEheadconfig(String _eheadConfig) {
    eheadConfig = _eheadConfig;
  }

  public String getEncyclopedia() {
    return encyclopedia;
  }

  public void setEncyclopedia(String _encyclopedia) {
    encyclopedia = _encyclopedia;
  }

  public String getEsize() {
    return esize;
  }

  public void setEsize(String _esize) {
    esize = _esize;
  }

  public String getFuel() {
    return fuel;
  }

  public void setFuel(String _fuel) {
    fuel = _fuel;
  }

  public String getGas() {
    return gas;
  }

  public void setGas(String _gas) {
    gas = _gas;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String _height) {
    height = _height;
  }

  public String getHwy() {
    return hwy;
  }

  public void setHwy(String _hwy) {
    hwy = _hwy;
  }

  public String getLength() {
    return length;
  }

  public void setLength(String _length) {
    length = _length;
  }

  public String getMatchkey() {
    return matchKey;
  }

  public void setMatchkey(String _matchKey) {
    matchKey = _matchKey;
  }

  public String getPedigree() {
    return pedigree;
  }

  public void setPedigree(String _pedigree) {
    pedigree = _pedigree;
  }
  
  public String getPrice() {
    return price;
  }

  public void setPrice(String _price) {
    price = _price;
  }

  public String getSeats() {
    return seats;
  }

  public void setSeats(String _seats) {
    seats = _seats;
  }

  public String getSeriesname() {
    return seriesname;
  }

  public void setSeriesname(String _seriesname) {
    seriesname = _seriesname;
  }

  public String getTrans() {
    return transmission;
  }

  public void setTrans(String _transmission) {
    transmission = _transmission;
  }

  public String getTrim() {
    return trim;
  }

  public void setTrim(String _trim) {
    trim = _trim;
  }

  public String getVehicletype() {
    return vehicleType;
  }

  public void setVehicletype(String _vehicleType) {
    vehicleType = _vehicleType;
  }

  public String getWeight() {
    return weight;
  }

  public void setWeight(String _weight) {
    weight = _weight;
  }

  public String getWheelbase() {
    return wheelBase;
  }

  public void setWheelbase(String _wheelbase) {
    wheelBase = _wheelbase;
  }

  public String getWidth() {
    return width;
  }

  public void setWidth(String _width) {
    width = _width;
  }

  public String getAlternatives() {
    return alternatives;
  }

  public void setAlternatives(String _alternatives) {
    alternatives = _alternatives;
  }

  public void setAlternatives(List _vehicleVinDetailsList) {
    final StringBuilder alternatives = new StringBuilder(40 * _vehicleVinDetailsList.size());
    final Set trimSet = new HashSet();
    final Set bodyStyleSet = new HashSet();
    final Set transmissionSet = new HashSet();
    for (Iterator it = _vehicleVinDetailsList.iterator(); it.hasNext();) {
      final VehicleVinDetails vehicleVinDetails = (VehicleVinDetails) it.next();
      alternatives.append(vehicleVinDetails.getTrim()).append(ALTERNATIVES_FIELD_SEP);
      trimSet.add(vehicleVinDetails.getTrim());
      
      alternatives.append(vehicleVinDetails.getBodystyle()).append(ALTERNATIVES_FIELD_SEP);
      bodyStyleSet.add(vehicleVinDetails.getBodystyle());
      
      alternatives.append(vehicleVinDetails.getTrans()).append(ALTERNATIVES_ROW_SEP);
      transmissionSet.add(vehicleVinDetails.getTrans());
    }
    alternatives.delete(
        alternatives.length() - ALTERNATIVES_ROW_SEP.length(), alternatives.length());
    fixAlternativesLength(alternatives);
    setAlternatives(alternatives.toString());
    if (trimSet.size() != 1) {
      setTrim(null);
    }
    if (bodyStyleSet.size() != 1) {
      setBodystyle(null);
    }
    if (transmissionSet.size() != 1) {
      setTrans(null);
    }
  }

  private void fixAlternativesLength(StringBuilder _alternatives) {
    if (_alternatives.length() > ALTERNATIVES_MAX_LENGTH) {
      LOG.error("VEHICLE_VIN_DETAILS.ALTERNATIVES FIELD MAX SIZE EXCEEDED (Expected " 
          + ALTERNATIVES_MAX_LENGTH + " but found " + _alternatives.length() + ").");
      // Delete extra alternatives.
      while (_alternatives.length() > ALTERNATIVES_MAX_LENGTH) {
        _alternatives.delete(
            _alternatives.lastIndexOf(ALTERNATIVES_ROW_SEP), _alternatives.length());
      }
    }
  }

  public List retrieveAlternatives() {
    // TODO(nahuel): Use List<VehicleVinDetails> when Xdoclet is deprecated.
    final List result = new ArrayList();
    if (getAlternatives() == null || getAlternatives().equals("OVERRIDED")) {
      return result;
    }
    final String[] rows = getAlternatives().split(ALTERNATIVES_ROW_SEP_PATTERN);
    for (int i = 0; i < rows.length; i++) {
      final VehicleVinDetails vehicleVinDetails = new VehicleVinDetails();
      final String[] rowFields = rows[i].split(ALTERNATIVES_FIELD_SEP_PATTERN);
      for (int j = 0; j < rowFields.length; j++) {
        try {
          PropertyUtils.setSimpleProperty(vehicleVinDetails, ALTERNATIVES_PROPERTIES[j], 
              rowFields[j]);
        } catch (IllegalAccessException e) {
          
        } catch (InvocationTargetException e) {
          
        } catch (NoSuchMethodException e) {
          
        }
      }
      result.add(vehicleVinDetails);
    }
    return result;
  }
  
  public String getEngineInfo() {
    final StringBuilder engineInfo = new StringBuilder();
    engineInfo
        .append(UtilityObjects.nullToEmpty(getEsize()))
        .append(BLANK)
        .append(UtilityObjects.nullToEmpty(getCarburetion()))
        .append(BLANK)
        .append(UtilityObjects.nullToEmpty(getEheadconfig()))
        .append(BLANK)
        .append(UtilityObjects.nullToEmpty(getEblocktype()))
        .append(BLANK)
        .append(UtilityObjects.nullToEmpty(getCylinder()))
        .append(BLANK);
    return engineInfo.toString();
  }
}
