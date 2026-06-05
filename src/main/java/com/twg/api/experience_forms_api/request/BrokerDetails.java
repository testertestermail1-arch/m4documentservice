package com.twg.api.experience_forms_api.request;

/**
 * Broker information.
 * @author komal gupta
 */

public class BrokerDetails{
	private String brokerName;
	private String brokerTaxId;
	private Double brokerPercentage;
	private String cobrokerName;
	private String cobrokerTaxId;
	private Double cobrokerPercentage;
	private String substipulatorName;
	private String substipulatorTaxId;
	private Double substipulatorPercentage;
	private String representativeName;
	private String representativeTaxId;
	private Double representativePercentage;
	private String substipulatorPhone;
	private String representativePhone;
	private String brokerPhone;
	private String cobrokerPhone;
	
	public String getRepresentativeName() {
		return representativeName;
	}
	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}
	public String getRepresentativeTaxId() {
		return representativeTaxId;
	}
	public void setRepresentativeTaxId(String representativeTaxId) {
		this.representativeTaxId = representativeTaxId;
	}
	public Double getRepresentativePercentage() {
		return representativePercentage;
	}
	public void setRepresentativePercentage(Double representativePercentage) {
		this.representativePercentage = representativePercentage;
	}
	public String getSubstipulatorPhone() {
		return substipulatorPhone;
	}
	public void setSubstipulatorPhone(String substipulatorPhone) {
		this.substipulatorPhone = substipulatorPhone;
	}
	public String getRepresentativePhone() {
		return representativePhone;
	}
	public void setRepresentativePhone(String representativePhone) {
		this.representativePhone = representativePhone;
	}
	public String getBrokerPhone() {
		return brokerPhone;
	}
	public void setBrokerPhone(String brokerPhone) {
		this.brokerPhone = brokerPhone;
	}
	public String getCobrokerPhone() {
		return cobrokerPhone;
	}
	public void setCobrokerPhone(String cobrokerPhone) {
		this.cobrokerPhone = cobrokerPhone;
	}
	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}
	public String getBrokerTaxId() {
		return brokerTaxId;
	}
	public void setBrokerTaxId(String brokerTaxId) {
		this.brokerTaxId = brokerTaxId;
	}
	public Double getBrokerPercentage() {
		return brokerPercentage;
	}
	public void setBrokerPercentage(Double brokerPercentage) {
		this.brokerPercentage = brokerPercentage;
	}
	public String getCobrokerName() {
		return cobrokerName;
	}
	public void setCobrokerName(String cobrokerName) {
		this.cobrokerName = cobrokerName;
	}
	public String getCobrokerTaxId() {
		return cobrokerTaxId;
	}
	public void setCobrokerTaxId(String cobrokerTaxId) {
		this.cobrokerTaxId = cobrokerTaxId;
	}
	public Double getCobrokerPercentage() {
		return cobrokerPercentage;
	}
	public void setCobrokerPercentage(Double cobrokerPercentage) {
		this.cobrokerPercentage = cobrokerPercentage;
	}
	public String getSubstipulatorName() {
		return substipulatorName;
	}
	public void setSubstipulatorName(String substipulatorName) {
		this.substipulatorName = substipulatorName;
	}
	public String getSubstipulatorTaxId() {
		return substipulatorTaxId;
	}
	public void setSubstipulatorTaxId(String substipulatorTaxId) {
		this.substipulatorTaxId = substipulatorTaxId;
	}
	public Double getSubstipulatorPercentage() {
		return substipulatorPercentage;
	}
	public void setSubstipulatorPercentage(Double substipulatorPercentage) {
		this.substipulatorPercentage = substipulatorPercentage;
	}
	@Override
	public String toString() {
		return "BrokerDetails [brokerName=" + brokerName + ", brokerTaxId=" + brokerTaxId + ", brokerPercentage="
				+ brokerPercentage + ", cobrokerName=" + cobrokerName + ", cobrokerTaxId=" + cobrokerTaxId
				+ ", cobrokerPercentage=" + cobrokerPercentage + ", substipulatorName=" + substipulatorName
				+ ", substipulatorTaxId=" + substipulatorTaxId + ", substipulatorPercentage=" + substipulatorPercentage
				+ ", representativeName=" + representativeName + ", representativeTaxId=" + representativeTaxId
				+ ", representativePercentage=" + representativePercentage + ", substipulatorPhone="
				+ substipulatorPhone + ", representativePhone=" + representativePhone + ", brokerPhone=" + brokerPhone
				+ ", cobrokerPhone=" + cobrokerPhone + "]";
	}
	
	
}