/*
 * Copyright (c) 2005 The Warranty Group.
 *
 * This software is the confidential and proprietary information of The Warranty Group 
 * (http://www.thewarrantygroup.com/). You shall not disclose such Confidential Information or use 
 * it without authorization from The Warranty Group.
 */
package com.aon.awg.qcert.domain.vehicle;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Vehicle implements Serializable {

  private static final long serialVersionUID = 1L;
  private static final Log LOG = LogFactory.getLog(Vehicle.class);

  private long id;
  private String vin;
  private String color;
 
  private Model model;
  private Integer year;
  private Set options = new LinkedHashSet();
  private VehicleVinAttributes vinAttributes;
  private byte[] template;
  private String defaultTemplate;


  public Vehicle() {
  }

  public Vehicle(String _vin, Integer _year, Make _make) {
    vin = _vin;
    year = _year;
    setModel(new Model(_make));
  }


  public long getId() {
    return id;
  }

  public void setId(long _id) {
    id = _id;
  }


  public String getVin() {
    return vin;
  }

  public void setVin(String _vin) {
    vin = _vin;
  }
  
  public byte[] getTemplate(){
	 
	return template;  
  }
  public void setTemplate(byte[] _template) {
	    template = _template;
	}
  
  public String getdefaultTemplate() {
	    return defaultTemplate;
	  }

	  public void setdefaultTemplate(String _defaultTemplate) {
		  defaultTemplate = _defaultTemplate;
	  }

  public Model getModel() {
    return model;
  }
  

  public void setModel(Model _model) {
    model = _model;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer _year) {
    year = _year;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String _color) {
    color = _color;
  }

 

  public Set getOptions() {
    return options;
  }

  public void setOptions(Set _options) {
    options = _options;
  }

  public boolean addOption(VehicleOption _option) {
    _option.setVehicle(this);
    return getOptions().add(_option);
  }

  public VehicleVinAttributes getVinAttributes() {
    return vinAttributes;
  }

  public void setVinAttributes(VehicleVinAttributes _vinAttributes) {
    vinAttributes = _vinAttributes;
  }

  public String getEngineDescription() {
    if (getVinAttributes() == null) {
      return null;
    }
    final StringBuilder stringBuilder = new StringBuilder(50);
    stringBuilder.append(StringUtils.defaultString(getVinAttributes().getEsize())).append(" ");
    stringBuilder.append(StringUtils.defaultString(getVinAttributes().getCarburetion()))
        .append(" ");
    stringBuilder.append(StringUtils.defaultString(getVinAttributes().getEheadconfig()))
        .append(" ");
    stringBuilder.append(StringUtils.defaultString(getVinAttributes().getEblocktype()));
    stringBuilder.append(StringUtils.defaultString(getVinAttributes().getCylinder()));
    return stringBuilder.toString();
  }

  public String getTrim() {
    return getVinAttributes() == null ? null : getVinAttributes().getTrim();
  }

  public void setTrim(String _trim) {
    if (getVinAttributes() == null)
      setVinAttributes(new VehicleVinAttributes());
    getVinAttributes().setTrim(_trim);
  }

  public String getBodystyle() {
    if (getVinAttributes() == null)
      return null;
    return getVinAttributes().getBodystyle();
  }

  public void setBodystyle(String _bs) {
    if (getVinAttributes() == null)
      setVinAttributes(new VehicleVinAttributes());
    getVinAttributes().setBodystyle(_bs);
  }

  public String getTrans() {
    if (getVinAttributes() == null)
      return null;
    return getVinAttributes().getTrans();
  }

  public void setTrans(String _trans) {
    if (getVinAttributes() == null)
      setVinAttributes(new VehicleVinAttributes());
    getVinAttributes().setTrans(_trans);
  }
  
  public void setDriveTrain(String _driveTrain) {
	  if (getVinAttributes() == null)
		  setVinAttributes(new VehicleVinAttributes());
	  getVinAttributes().setDrivetrain(_driveTrain);
  }
  
  public String getDriveTrain() {
	  if (getVinAttributes() == null)
		  return null;
	  return getVinAttributes().getDrivetrain();
  }
  
  public void setLenght(String _lenght) {
	  if (getVinAttributes() == null)
		  setVinAttributes(new VehicleVinAttributes());
	  getVinAttributes().setLength(_lenght);
  }
  
  public String getLenght() {
	  if (getVinAttributes() == null)
		  return null;
	  return getVinAttributes().getLength();
  }
  
  public void setCity(String _city) {
	  if (getVinAttributes() == null)
		  setVinAttributes(new VehicleVinAttributes());
	  getVinAttributes().setCity(_city);
  }
  
  public String getCity() {
	  if (getVinAttributes() == null)
		  return null;
	  return getVinAttributes().getCity();
  }

  public void setHwy(String _hwy) {
	  if (getVinAttributes() == null)
		  setVinAttributes(new VehicleVinAttributes());
	  getVinAttributes().setHwy(_hwy);
  }
  
  public String getHwy() {
	  if (getVinAttributes() == null)
		  return null;
	  return getVinAttributes().getHwy();
  }

  /**
   * Iterates over the options collection and assigns the vehicle reference if not present.
   */
  public boolean updateOptionsReference() {
    boolean updateRequired = false;
    final Iterator iterator = getOptions().iterator();
    while (iterator.hasNext()) {
      final VehicleOption option = (VehicleOption) iterator.next();
      if (option.getVehicle() == null) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Updating vehicle reference for option '" + option + "'.");
        }
        option.setVehicle(this);
        updateRequired = true;
      }
    }
    return updateRequired;
  }
}
