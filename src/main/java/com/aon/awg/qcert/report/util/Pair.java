/*
 * Copyright (c) 2008 The Warranty Group.
 *
 * This software is the confidential and proprietary information of The Warranty Group 
 * (http://www.thewarrantygroup.com/). You shall not disclose such Confidential Information or use 
 * it without authorization from The Warranty Group.
 */
package com.aon.awg.qcert.report.util;


public class Pair<T, V> {
  private final T firstValue;
  private final V secondValue;

  public Pair(T _firstValue, V _secondValue) {
    firstValue = _firstValue;
    secondValue = _secondValue;
  }

  public T getFirst() {
    return firstValue;
  }

  public V getSecond() {
    return secondValue;
  }

  @Override
  public String toString() {
    return "(" + firstValue + ", " + secondValue + ")";
  }
}
