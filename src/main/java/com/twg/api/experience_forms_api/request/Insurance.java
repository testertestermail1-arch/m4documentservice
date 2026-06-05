package com.twg.api.experience_forms_api.request;

/**
 * Holds insurance information.
 * @author Juan Berrueta
 */
public class Insurance {
	private String insuranceCarrierName;
	private String insuranceCarrierAddress1;
	private String insuranceCarrierAddress2;
	private String insuranceCarrierCity;
	private String insuranceCarrierState;
	private String insuranceCarrierCountry;
	private String insuranceCarrierPostalCode;
	
	
	public Insurance() 
	{}
	
	/**
	 * Primary insurance information.
	 * @param contentType the errors list to fulfill.
	 * @return Map javadoc comments
	 */
	public String getInsuranceCarrierCountry() {
		return insuranceCarrierCountry;
	}
	public void setInsuranceCarrierCountry(String insuranceCarrierCountry) {
		this.insuranceCarrierCountry = insuranceCarrierCountry;
	}
	public String getInsuranceCarrierName() {
		return insuranceCarrierName;
	}
	public void setInsuranceCarrierName(String insuranceCarrierName) {
		this.insuranceCarrierName = insuranceCarrierName;
	}
	public String getInsuranceCarrierAddress1() {
		return insuranceCarrierAddress1;
	}
	public void setInsuranceCarrierAddress1(String insuranceCarrierAddress1) {
		this.insuranceCarrierAddress1 = insuranceCarrierAddress1;
	}
	public String getInsuranceCarrierAddress2() {
		return insuranceCarrierAddress2;
	}
	public void setInsuranceCarrierAddress2(String insuranceCarrierAddress2) {
		this.insuranceCarrierAddress2 = insuranceCarrierAddress2;
	}
	public String getInsuranceCarrierCity() {
		return insuranceCarrierCity;
	}
	public void setInsuranceCarrierCity(String insuranceCarrierCity) {
		this.insuranceCarrierCity = insuranceCarrierCity;
	}
	public String getInsuranceCarrierState() {
		return insuranceCarrierState;
	}
	public void setInsuranceCarrierState(String insuranceCarrierState) {
		this.insuranceCarrierState = insuranceCarrierState;
	}
	public String getInsuranceCarrierPostalCode() {
		return insuranceCarrierPostalCode;
	}
	public void setInsuranceCarrierPostalCode(String insuranceCarrierPostalCode) {
		this.insuranceCarrierPostalCode = insuranceCarrierPostalCode;
	}
	
	@Override
	public String toString() {
		return "Insurance [insuranceCarrierName=" + insuranceCarrierName + ", insuranceCarrierAddress1="
				+ insuranceCarrierAddress1 + ", insuranceCarrierAddress2=" + insuranceCarrierAddress2
				+ ", insuranceCarrierCity=" + insuranceCarrierCity + ", insuranceCarrierState=" + insuranceCarrierState
				+ ", insuranceCarrierCountry=" + insuranceCarrierCountry + ", insuranceCarrierPostalCode="
				+ insuranceCarrierPostalCode + "]";
	}
}
