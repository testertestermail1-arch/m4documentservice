/*
 * Copyright (c) 2006 The Warranty Group.
 *
 * This software is the confidential and proprietary information of The Warranty Group 
 * (http://www.thewarrantygroup.com/). You shall not disclose such Confidential Information or use 
 * it without authorization from The Warranty Group.
 */
package com.aon.awg.qcert.businesslayer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.aon.awg.qcert.domain.vehicle.VehicleOption;


public class VehicleVinDetails implements Serializable {

  private static final long serialVersionUID = 1L;

  
  private final Map optionTypesMap; // Type <String, String>.

  private String vin = "";

  //Reflective properties, kept in synch with sp columns.
  private String matchkey;
  private String year;
  private String make;
  private String model;
  private String seriesName;
  private String trim;
  private String bodyStyle;
  private String doorct;
  private String vehicleType;
  private String esize;
  private String cylinder;
  private String carburetion;
  private String eblockType;
  private String eheadConfig;
  private String easpiration;
  private String transmission;
  private String driveTrain;
  private String fuel;
  private String wheelbase;
  private String country;
  private String price;
  private String turbocharge;

  private String length;
  private String width;
  private String height;
  private String weight;
  private String gas;
  private String seats;
  private String city;
  private String hwy;
  private String ifeature;
  private String efeature;
  private String engFeature;
  private String sfeature;
  private String pedigree;
  private String encyclopedia;

  public VehicleVinDetails() {
    optionTypesMap = new HashMap(); // Type <String, String>.
    optionTypesMap.put(VehicleOption.INTERIOR, "ifeature");
    optionTypesMap.put(VehicleOption.EXTERIOR, "efeature");
    optionTypesMap.put(VehicleOption.ENGINEERING, "engfeature");
    optionTypesMap.put(VehicleOption.SAFETY, "sfeature");
  }

  public Map getOptionTypesMap() { // Return type <String, String>. 
    return optionTypesMap;
  }
  
  public String getVin() {
    return vin;
  }

  public void setVin(String _vin) {
    vin = _vin;
  }

  public String getMatchkey() {
    return matchkey;
  }

  public void setMatchkey(String _matchkey) {
    matchkey = _matchkey;
  }

  public String getBodystyle() {
    return bodyStyle;
  }

  public void setBodystyle(String _bodystyle) {
    bodyStyle = _bodystyle;
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

  public String getEsize() {
    return esize;
  }

  public void setEsize(String _esize) {
    esize = _esize;
  }

  public String getMake() {
    return make;
  }

  public void setMake(String _make) {
    make = _make;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String _model) {
    model = _model;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String _price) {
    price = _price;
  }

  public String getTrim() {
    return trim;
  }

  public void setTrim(String _trim) {
    trim = _trim;
  }

  public String getTrans() {
    return transmission;
  }

  public void setTrans(String _transmission) {
    transmission = _transmission;
  }

  public String getVehicletype() {
    return vehicleType;
  }

  public void setVehicletype(String _vehicleType) {
    vehicleType = _vehicleType;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String _year) {
    year = _year;
  }

  public String getSeriesname() {
    return seriesName;
  }

  public void setSeriesname(String _seriesName) {
    seriesName = _seriesName;
  }

  public String getCarburetion() {
    return carburetion;
  }

  public void setCarburetion(String _carburetion) {
    carburetion = _carburetion;
  }

  public String getFuel() {
    return fuel;
  }

  public void setFuel(String _fuel) {
    fuel = _fuel;
  }

  public String getWheelbase() {
    return wheelbase;
  }

  public void setWheelbase(String _wheelbase) {
    wheelbase = _wheelbase;
  }

  public String getLength() {
    return length;
  }

  public void setLength(String _length) {
    length = _length;
  }

  public String getWidth() {
    return width;
  }

  public void setWidth(String _width) {
    width = _width;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String _height) {
    height = _height;
  }

  public String getWeight() {
    return weight;
  }

  public void setWeight(String _weight) {
    weight = _weight;
  }

  public String getGas() {
    return gas;
  }

  public void setGas(String _gas) {
    gas = _gas;
  }

  public String getSeats() {
    return seats;
  }

  public void setSeats(String _seats) {
    seats = _seats;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String _city) {
    city = _city;
  }

  public String getEfeature() {
    return efeature;
  }

  public void setEfeature(String _efeature) {
    efeature = _efeature;
  }

  public String getEncyclopedia() {
    return encyclopedia;
  }

  public void setEncyclopedia(String _encyclopedia) {
    encyclopedia = _encyclopedia;
  }

  public String getEngfeature() {
    return engFeature;
  }

  public void setEngfeature(String _engFeature) {
    engFeature = _engFeature;
  }

  public String getHwy() {
    return hwy;
  }

  public void setHwy(String _hwy) {
    hwy = _hwy;
  }

  public String getIfeature() {
    return ifeature;
  }

  public void setIfeature(String _ifeature) {
    ifeature = _ifeature;
  }

  public String getPedigree() {
    return pedigree;
  }

  public void setPedigree(String _pedigree) {
    pedigree = _pedigree;
  }

  public String getSfeature() {
    return sfeature;
  }

  public void setSfeature(String _sfeature) {
    sfeature = _sfeature;
  }

  public String getTurbocharge() {
	return turbocharge;
  }

  public void setTurbocharge(String turbocharge) {
	  this.turbocharge = turbocharge;
  }
  
}