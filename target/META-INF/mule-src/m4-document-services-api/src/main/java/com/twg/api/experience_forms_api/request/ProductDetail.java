package com.twg.api.experience_forms_api.request;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.twg.api.experience_forms_api.DocumentError;

/**
 * Holds the product detail information.
 * 
 * @author Juan Berrueta
 */

public class ProductDetail 
{
	private Double reportedCustomerCost;
	private Double salesTax = 0D;
	private Double deductibleAmount = 0D;
	private boolean disappearing;
	private boolean reducing;
	private String coverageName;
	private String coverageCode;
	private int termMonths;
	private int termDistance;
	private String agreementStatus;
	private boolean combo;
	private String productDetailType;
	private String formNumber;
	private String productClass;
	private int numberOfServices;
	private int ppmIntervalMiles;
	private int serviceInterval;
	private Double reducedDeductible;
	private String systemPin;
	private Double totalAllowableMilesContract;
	private String oilType;
	private ArrayList optionalCoverages;
	private String autoRenew;
	private Double GST= 0D;
	private Double PST= 0D;
	private Double netPremium;
	
	public Double getNetPremium() {
		return netPremium;
	}

	public void setNetPremium(Double netPremium) {
		this.netPremium = netPremium;
	}
	/**
	 * Creates the product detail.<br>
	 * This method may be required by the services framework.
	 */
	public ProductDetail() 
	{}
	
	public String getAutoRenew() {
		return autoRenew;
	}

	public void setAutoRenew(String autoRenew) {
		this.autoRenew = autoRenew;
	}
	
	public ArrayList getOptionalCoverages() {
		return optionalCoverages;
	}


	public void setOptionalCoverages(ArrayList optionalCoverages) {
		this.optionalCoverages = optionalCoverages;
	}


	public String getSystemPin() 
	{
		return systemPin;
	}

	public void setSystemPin(String systemPin) 
	{
		this.systemPin = systemPin;
	}

	public Double getReducedDeductible() 
	{
		return reducedDeductible;
	}

	public void setReducedDeductible(Double reducedDeductible) 
	{
		this.reducedDeductible = reducedDeductible;
	}

	public int getPpmIntervalMiles() 
	{
		return ppmIntervalMiles;
	}

	public void setPpmIntervalMiles(int ppmIntervalMiles) 
	{
		this.ppmIntervalMiles = ppmIntervalMiles;
	}

	public int getServiceInterval() 
	{
		return serviceInterval;
	}

	public void setServiceInterval(int serviceInterval) 
	{
		this.serviceInterval = serviceInterval;
	}

	public int getNumberOfServices() 
	{
		return numberOfServices;
	}

	public void setNumberOfServices(int numberOfServices) 
	{
		this.numberOfServices = numberOfServices;
	}

	public String getProductClass() 
	{
		return productClass;
	}

	public void setProductClass(String productClass) 
	{
		this.productClass = productClass;
	}

	public String getFormNumber() 
	{
		return formNumber;
	}

	public void setFormNumber(String formNumber) 
	{
		this.formNumber = formNumber;
	}

	public String getProductDetailType() 
	{
		return productDetailType;
	}

	public void setProductDetailType(String productDetailType) 
	{
		this.productDetailType = productDetailType;
	}

	public boolean isCombo() 
	{
		return combo;
	}

	public void setCombo(boolean combo) 
	{
		this.combo = combo;
	}

	public String getAgreementStatus() 
	{
		if (("F".equals(this.agreementStatus))||("Q".equals(this.agreementStatus))||("P".equals(this.agreementStatus)))
		{
			return "Final";
		}
		else if ("S".equals(this.agreementStatus))
		{
			return "Sample";
		} 	
		return "Blank";
	}

	public void setAgreementStatus(String agreementStatus) 
	{
		//Blank("B"), Sample("S"), Final("P"), None("N");
		this.agreementStatus = agreementStatus;
	}

	public void setDisappearing(boolean disappearing) 
	{
		this.disappearing = disappearing;
	}
	public void setReducing(boolean reducing) 
	{
		this.reducing = reducing;
	}

	public void setTermMonths(int termMonths) 
	{
		this.termMonths = termMonths;
	}


	public Double getReportedCustomerCost() {
		return reportedCustomerCost;
	}

	public void setReportedCustomerCost(Double reportedCustomerCost) {
		this.reportedCustomerCost = reportedCustomerCost;
	}

	public Double getSalesTax() 
	{
		return this.salesTax;
	}

	public Double getDeductibleAmount() 
	{
		return this.deductibleAmount;
	}

	public boolean isDisappearing() 
	{
		return this.disappearing;
	}
	
	public boolean isReducing() 
	{
		return this.reducing;
	}

	public String getCoverageName() 
	{
		return this.coverageName;
	}
	
	public String getCoverageCode() 
	{
		return this.coverageCode;
	}
	
	public void setCoverageCode(String coverageCode) 
	{
		this.coverageCode = coverageCode;
	}
	
	public int getTermMonths() 
	{
		return this.termMonths;
	}

	public Double getPrice() 
	{
		return this.getReportedCustomerCost() + this.getSalesTax();
	}

	public Double getTotalAllowableMilesContract() 
	{
		return totalAllowableMilesContract;
	}

	public void setTotalAllowableMilesContract(Double totalAllowableMilesContract) 
	{
		this.totalAllowableMilesContract = totalAllowableMilesContract;
	}

	public String getOilType() 
	{
		return this.oilType;
	}
	
	/**
	 * Validates the product details.<br>
	 * The product details are invalid if any of the following occur:
	 * <ul>
	 * <li>the type is null or invalid</li>
	 * <li>the productClass is null, blank or contains whitespaces only</li>
	 * <li>the form is null or invalid</li>
	 * <li>the cost is less than zero</li>
	 * <li>the salesTax is less than zero</li>
	 * <li>the deductibleAmount is less than zero</li>
	 * <li>the ppmIntervalMiles is less than zero</li>
	 * <li>the coverageCode is null, blank or contains whitespaces only</li>
	 * <li>the coverageName is null, blank or contains whitespaces only</li>
	 * <li>the termMonths is less than zero</li>
	 * <li>the termMiles is less than zero</li>
	 * </ul>
	 * 
	 * @param contentType
	 * @return productDetailErrors
	 */
	public Map validate(String contentType) 
	{
		// for blank pdf form following chk not needed
		Map<String,String> productDetailErrors = new HashMap<String,String>(); 
		
		if (this.reportedCustomerCost < 0) 
		{
			productDetailErrors.put("cost","The product detail cost cannot be less than zero.");
		}
		
		if (this.totalAllowableMilesContract < 0) 
		{
			productDetailErrors.put("totalAllowableMilesContract","The product detail total allowable miles contract cannot be less than zero.");
		}

		if (this.deductibleAmount != 0 && this.deductibleAmount < 0) 
		{
			productDetailErrors.put("deductible Amount","The product detail deductible amount cannot be less than zero.");
		}
		if (StringUtils.isBlank(this.productClass)) 
		{
			productDetailErrors.put("product Class","The product detail product class cannot be null, blank nor contain whitespaces only.");
		}
		if (StringUtils.isBlank(this.coverageName)) 
		{
			productDetailErrors.put("coverage Name","The product detail coverage name cannot be null, blank nor contain whitespaces only.");
		}
		/*if (StringUtils.isBlank(this.coverageCode)) 
		{
			productDetailErrors.put("coverage Code","The product detail coverage code cannot be null, blank nor contain whitespaces only.");
		}*/
		if (this.termMonths < 0) 
		{
			productDetailErrors.put("termMonths", "The product detail term months cannot be less than zero.");
		}
		if (this.termDistance < 0) 
		{
			productDetailErrors.put("termMiles","The product detail term miles cannot be less than zero.");
		}
		if (this.numberOfServices < 0) 
		{
			productDetailErrors.put("numberOfServices","The product detail total number of maintenance sercices contract cannot be less than zero.");
		}
		if(((this.autoRenew != "") && (this.autoRenew != null)) && (!( this.autoRenew.equals("Y") || this.autoRenew.equals("N"))))
		{
				productDetailErrors.put("autoRenew","The product detail autoRenew cannot have values other than Y or N");
		}
		return productDetailErrors;
	}

	@Override
	public String toString() 
	{
		return new ToStringBuilder(this).append("cost", this.reportedCustomerCost).append("deductibleAmount", this.deductibleAmount)
				.append("ppmIntervalMiles", this.ppmIntervalMiles).append("serviceInterval", this.serviceInterval).append("disappearing", this.disappearing).append("coverageName", this.coverageName).append("totalAllowableMilesContract", this.totalAllowableMilesContract)
				.append("coverageCode", this.coverageCode).append("oilType", this.oilType).append("numberOfServices", this.numberOfServices).append("termMonths", this.termMonths).append("termMiles", this.termDistance).append("autoRenew", this.autoRenew).append("GST", this.GST).append("PST", this.PST).append("netPremium", this.netPremium).toString();
	}

	public Double getGST() {
		return GST;
	}

	public void setGST(Double gST) {
		GST = gST;
	}

	public Double getPST() {
		return PST;
	}

	public void setPST(Double pST) {
		PST = pST;
	}

	public void setSalesTax(Double salesTax) 
	{
		// TODO Auto-generated method stub
		this.salesTax = salesTax;
	}

	public void setDeductibleAmount(Double deductibleAmount) 
	{
		// TODO Auto-generated method stub
		this.deductibleAmount = deductibleAmount;
	}

	public void setCoverageName(String coverageName) 
	{
		// TODO Auto-generated method stub
		this.coverageName = coverageName;
	}

	public int getTermDistance() {
		return termDistance;
	}

	public void setTermDistance(int termDistance) {
		this.termDistance = termDistance;
	}

	public void setOilType(String oilType) 
	{
		this.oilType = oilType;
	}
	
}
