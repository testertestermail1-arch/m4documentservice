/*
 * Copyright (c) 2007 The Warranty Group.
 *
 * This software is the confidential and proprietary information of The Warranty Group 
 * (http://www.thewarrantygroup.com/). You shall not disclose such Confidential Information or use 
 * it without authorization from The Warranty Group.
 */
package com.aon.awg.qcert.domain;

import java.io.Serializable;

import com.aon.awg.qcert.utils.UtilityObjects;


public class VehIndicators implements Serializable {

	  private static final long serialVersionUID = 1l;
	  

	  private String abandoned;
	  private String body;
	  private String canadianReg;
	  private String country;
	  private String cnt;
	  private String damage;
	  private String duplicateTitle;
	  private String engine;
	  private String exceedsMechanicalLimits;
	  private String failedEmission;
	  private String frameDamage;
	  private String greyMkt;
	  private String idType;
	  private boolean criticalIndicatorYesFlag;
	  private String junk;
	  private String last_Date;
	  private String lastOdometer;
	  private String lastState;
	  private String lastTitle;
	  private String lemon;
	  private String mfg;
	  private String mfgCode;
	  private String model;
	  private String nam;
	  private String odometerIndependentSource;
	  private String otherOdometerBrand;
	  private final boolean override;
	  private String rebuilt;
	  private String recycled;
	  private String resultCode; // "0" is VIN has been decoded properly.
	  private String resultMessage;
	  private String rollBack;
	  private String salvage;
	  private String seriesCode;
	  private String theft;
	  private String theftRecovered;
	  private String titleRegStorm;
	  private String usedForRentalFleet;
	  private String vid;
	  private String vin;
	  private String water_Damage;
	  private String year;
	  
	  
	  
	  
	  public VehIndicators(boolean _override) {
	    override = _override;
	  }

	  public String getUsedForRentalFleet() {
	    return usedForRentalFleet;
	  }

	  public void setUsedForRentalFleet(String _usedForRentalFleet) {
	    usedForRentalFleet = UtilityObjects.convertToYESNO(_usedForRentalFleet);
	  }
	  
	  public String getVin() {
	    return vin;
	  }

	  public void setVin(String _vin) {
	    vin = _vin;
	  }

	  public String getVid() {
	    return vid;
	  }

	  public void setVid(String _vid) {
	    vid = _vid;
	  }

	  public String getIdType() {
	    return idType;
	  }

	  public void setIdType(String _idType) {
	    idType = _idType;
	  }

	  public String getCount() {
	    return cnt;
	  }

	  public void setCount(String _cnt) {
	    cnt = _cnt;
	  }

	  public String getLast_State() {
	    return lastState;
	  }

	  public void setLastState(String _lastState) {
	    lastState = _lastState;
	  }

	  public String getLast_Title() {
	    return lastTitle;
	  }

	  public void setLastTitle(String _lastTitle) {
	    lastTitle = _lastTitle;
	  }

	  public String getLast_Date() {
	    return last_Date;
	  }

	  public void setLastDate(String _lastDate) {
	   last_Date = _lastDate;
	  }

	  public String getLastOdometer() {
	    return lastOdometer;
	  }

	  public void setLastOdometer(String _lastOdometer) {
	    lastOdometer = _lastOdometer;
	  }

	  public String getRollBack() {
	    return rollBack;
	  }

	  public void setRollBack(String _rollBack) {
	    rollBack = _rollBack;
	  }

	  public String getSalvage() {
	    return salvage;
	  }

	  public void setSalvage(String _salvage) {
	    salvage = UtilityObjects.convertToYESNO(_salvage);
	  }

	  public String getEml() {
	    return exceedsMechanicalLimits;
	  }

	  public void setEml(String _exceedsMechanicalLimits) {
	    exceedsMechanicalLimits = UtilityObjects.convertToYESNO(_exceedsMechanicalLimits);
	  }

	  public String getFailedEmission() {
	    return failedEmission;
	  }

	  public void setFailedEmission(String _failedEmission) {
	    failedEmission = UtilityObjects.convertToYESNO(_failedEmission);
	  }

	  public String getDuplicateTitle() {
	    return duplicateTitle;
	  }

	  public void setDuplicateTitle(String _duplicateTitle) {
	    duplicateTitle = UtilityObjects.convertToYESNO(_duplicateTitle);
	  }

	  public String getNam() {
	    return nam;
	  }

	  public void setNam(String _nam) {
	    //nam = UtilityObjects.convertToYESNO(_nam);
	    nam = _nam;
	  }

	  public String getDamage() {
	    return damage;
	  }

	  public void setDamage(String _damage) {
	    damage = UtilityObjects.convertToYESNO(_damage);
	  }

	  public String getLemon() {
	    return lemon;
	  }

	  public void setLemon(String _lemon) {
	    lemon = UtilityObjects.convertToYESNO(_lemon);
	  }

	  public String getOtherOdometerBrand() {
	    return otherOdometerBrand;
	  }

	  public void setOtherOdometerBrand(String _otherOdometerBrand) {
	    otherOdometerBrand = UtilityObjects.convertToYESNO(_otherOdometerBrand);
	  }

	  public String getTheft() {
	    return theft;
	  }

	  public void setTheft(String _theft) {
	    theft = UtilityObjects.convertToYESNO(_theft);
	  }

	  public String getTheftRecovered() {
	    return theftRecovered;
	  }

	  public void setTheftRecovered(String _theftRecovered) {
	    theftRecovered = UtilityObjects.convertToYESNO(_theftRecovered);
	  }

	  public String getOdometerIndpdntSrc() {
	    return odometerIndependentSource;
	  }

	  public void setOdometerIndpdntSrc(String _odometerIndependentSource) {
	    odometerIndependentSource = UtilityObjects.convertToYESNO(_odometerIndependentSource);
	  }

	  public String getWaterDamage() {
	    return water_Damage;
	  }

	  public void setWaterDamage(String _waterDamage) {
	    water_Damage = UtilityObjects.convertToYESNO(_waterDamage);
	  }

	  public String getAbandoned() {
	    return abandoned;
	  }

	  public void setAbandoned(String _abandoned) {
	    abandoned = UtilityObjects.convertToYESNO(_abandoned);
	  }

	  public String getJunk() {
	    return junk;
	  }

	  public void setJunk(String _junk) {
	    junk = UtilityObjects.convertToYESNO(_junk);
	  }

	  public String getRecycled() {
	    return recycled;
	  }

	  public void setRecycled(String _recycled) {
	    recycled = UtilityObjects.convertToYESNO(_recycled);
	  }

	  public String getGreyMkt() {
	    return greyMkt;
	  }

	  public void setGreyMkt(String _greyMkt) {
	    greyMkt = UtilityObjects.convertToYESNO(_greyMkt);
	  }

	  public String getRebuilt() {
	    return rebuilt;
	  }

	  public void setRebuilt(String _rebuilt) {
	    rebuilt = UtilityObjects.convertToYESNO(_rebuilt);
	  }

	  public String getCanadianReg() {
	    return canadianReg;
	  }

	  public void setCanadianReg(String _canadianReg) {
	    canadianReg = UtilityObjects.convertToYESNO(_canadianReg);
	  }

	  public String getTitleRegStorm() {
	    return titleRegStorm;
	  }

	  public void setTitleRegStorm(String _titleRegStorm) {
	    titleRegStorm = UtilityObjects.convertToYESNO(_titleRegStorm);
	  }

	  public String getBody() {
	    return body;
	  }

	  public void setBody(String _body) {
	    body = _body;
	  }

	  public String getCountry() {
	    return country;
	  }

	  public void setCountry(String _country) {
	    country = _country;
	  }

	  public String getEngine() {
	    return engine;
	  }

	  public void setEngine(String _engine) {
	    engine = _engine;
	  }

	  public String getMfg() {
	    return mfg;
	  }

	  public void setMfg(String _mfg) {
	    mfg = _mfg;
	  }

	  public String getMfgCode() {
	    return mfgCode;
	  }

	  public void setMfgCode(String _mfgCode) {
	    mfgCode = _mfgCode;
	  }

	  public String getModel() {
	    return model;
	  }

	  public void setModel(String _model) {
	    model = _model;
	  }

	  public String getResultCode() {
	    return resultCode;
	  }

	  public void setResultCode(String _resultCode) {
	    resultCode = _resultCode;
	  }

	  public String getResultMessage() {
	    return resultMessage;
	  }

	  public void setResultMessage(String _resultMessage) {
	    resultMessage = _resultMessage;
	  }

	  public String getSeriesCode() {
	    return seriesCode;
	  }

	  public void setSeriesCode(String _seriesCode) {
	    seriesCode = _seriesCode;
	  }

	  public String getYear() {
	    return year;
	  }

	  public void setYear(String _year) {
	    year = _year;
	  }

	  public boolean isAnyCriticalIndicatorYes() {
	    return override ? false : criticalIndicatorYesFlag;
	  }
	  
	  public void setFrameDamage(String _frameDamage) {
		  frameDamage = UtilityObjects.convertToYESNO(_frameDamage);
	  }
	  
	  public String getFrameDamage() {
		return frameDamage;
	  }
	
}
