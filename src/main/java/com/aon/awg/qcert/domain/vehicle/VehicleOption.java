/*
 * Created on Nov 23, 2005
 * $Id: VehicleOption.java 1461 2008-12-11 14:21:06Z nahuel.dalla.vecchia $
 */
package com.aon.awg.qcert.domain.vehicle;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class VehicleOption implements Cloneable, Serializable {

  private static final long serialVersionUID = 1L;

  //options type constants
  public static final String INTERIOR = "interior";
  public static final String EXTERIOR = "exterior";
  public static final String ENGINEERING = "eng";
  public static final String SAFETY = "safety";
  public static final String ADDITIONAL = "additional";
  public static final Set TYPE_SET;
  public static final String[] STANDARD_OPTIONS = { INTERIOR, EXTERIOR, ENGINEERING, SAFETY };
  public static final int MAX_ADDITIONAL_OPTIONS = 4;

  private long id;
  private String description;
  private boolean selected;
  private String type;
  private Vehicle vehicle;

  static {
    TYPE_SET = new HashSet();
    TYPE_SET.add(INTERIOR);
    TYPE_SET.add(EXTERIOR);
    TYPE_SET.add(ENGINEERING);
    TYPE_SET.add(SAFETY);
    //        TYPE_SET.add(MILEAGE);
    TYPE_SET.add(ADDITIONAL);
  }

  protected VehicleOption() {
  }

  public VehicleOption(String _type, String _description) {
    if (!TYPE_SET.contains(_type))
      throw new IllegalArgumentException("Option Type Undefined.");
    type = _type;
    description = _description;
    selected = true;
  }


  public long getId() {
    return id;
  }

  public void setId(long _id) {
    id = _id;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String _description) {
    description = _description;
  }


  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean _selected) {
    selected = _selected;
  }


  public String getType() {
    return type;
  }

  public void setType(String _type) {
    type = _type;
  }


  public Vehicle getVehicle() {
    return vehicle;
  }

  public void setVehicle(Vehicle vehicle) {
    this.vehicle = vehicle;
  }

  public String getTypeLabel() {
    if (INTERIOR.equals(type))
      return "Interior";
    if (EXTERIOR.equals(type))
      return "Exterior";
    if (ENGINEERING.equals(type))
      return "Engineering";
    if (SAFETY.equals(type))
      return "Safety";
    if (ADDITIONAL.equals(type))
      return "Additional";
    return "";
    
  }

  public boolean equals(Object _obj) {
    if (this == _obj) {
      return true;
    }
    if (!(_obj instanceof VehicleOption)) {
      return false;
    }
    VehicleOption that = (VehicleOption) _obj;
    return this.getType().equals(that.getType())
        && this.getDescription().equals(that.getDescription());
  }

  public int hashCode() {
    return getType().concat(getDescription()).hashCode();
  }

  public String toString() {
    return getType() + "-" + getDescription();
  }

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
