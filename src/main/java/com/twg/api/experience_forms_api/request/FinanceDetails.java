package com.twg.api.experience_forms_api.request;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlSchemaType;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.twg.api.experience_forms_api.DocumentError;

/**
 * Holds the charge information.
 * @author Juan Berrueta
 */

public class FinanceDetails {
	
	private String financeType;
	private FinancingType financingTypeEnum;
	private Double baloonResidualValue;
	private Double financedAmount;
	private Double apr;
	private Double paymentAmount;
	private Integer numberOfPayments;
	private Integer numberOfAdvPayments;
	private Integer numberOfSkipPayments;
	private Double msrp;
	private Double nada;
	@XmlSchemaType(name = "date")
	private Date interestStartDate;
	@XmlSchemaType(name = "date")
	private Date firstPaymentDate;
	private String loanNumber;
	private String gapIdNumber;
	private Double leaseCapAmount;
	private Integer period;
	@XmlSchemaType(name = "date")
	private Date startDate;
	@XmlSchemaType(name = "date")
	private Date endDate;
	private Double grossCapCost;
	private Double totalAllowableMilesContract;
	private Date scheduledTerminationDate;
	private Double monthlyPaymentAmount;
	private String isPaymentPlan;
	private String paymentDueDay;
	private Double installmentDownPayment;
	private Double installmentAmount;
	private Integer installmentPaymentTerm;
	private String installmentPaymentFrequency;
	
	public String getPaymentDueDay() {
		return paymentDueDay;
	}

	public Double getInstallmentAmount() {
		return installmentAmount;
	}

	public void setInstallmentAmount(Double installmentAmount) {
		this.installmentAmount = installmentAmount;
	}

	public Double getInstallmentDownPayment() {
		return installmentDownPayment;
	}

	public void setInstallmentDownPayment(Double installmentDownPayment) {
		this.installmentDownPayment = installmentDownPayment;
	}
	
	public Integer getInstallmentPaymentTerm() {
		return installmentPaymentTerm;
	}

	public void setInstallmentPaymentTerm(Integer installmentPaymentTerm) {
		this.installmentPaymentTerm = installmentPaymentTerm;
	}
	
	public String getInstallmentPaymentFrequency() {
		return installmentPaymentFrequency;
	}

	public void setInstallmentPaymentFrequency(String installmentPaymentFrequency) {
		this.installmentPaymentFrequency = installmentPaymentFrequency;
	}

	public void setPaymentDueDay(String paymentDueDay) {
		this.paymentDueDay = paymentDueDay;
	}

	public void setFinanceType(String financeType) 
	{
		this.financeType = financeType;
	}

	public Double getGrossCapCost() 
	{
		return grossCapCost;
	}

	public void setGrossCapCost(Double grossCapCost) 
	{
		this.grossCapCost = grossCapCost;
	}

	public Double getTotalAllowableMilesContract() 
	{
		return totalAllowableMilesContract;
	}

	public void setTotalAllowableMilesContract(Double totalAllowableMilesContract) 
	{
		this.totalAllowableMilesContract = totalAllowableMilesContract;
	}

	public Date getScheduledTerminationDate() 
	{
		return scheduledTerminationDate;
	}

	public void setScheduledTerminationDate(Date scheduledTerminationDate) 
	{
		this.scheduledTerminationDate = scheduledTerminationDate;
	}

	public Double getMonthlyPaymentAmount() 
	{
		return monthlyPaymentAmount;
	}

	public void setMonthlyPaymentAmount(Double monthlyPaymentAmount) 
	{
		this.monthlyPaymentAmount = monthlyPaymentAmount;
	}

	/**
	 * Creates the charge.<br>
	 * This method may be required by the services framework.
	 */
	public FinanceDetails() 
	{}
	
	public String getFinanceType() 
	{
		return this.financeType;
	}
	
	public FinancingType getFinancingTypeEnum() 
	{
		return this.financingTypeEnum;
	}
	
	public Double getFinancedAmount() 
	{
		return this.financedAmount;
	}
	
	public Double getApr() 
	{
		return this.apr;
	}
	
	public Double getPaymentAmount() 
	{
		return this.paymentAmount;
	}
	
	public Integer getNumberOfPayments() 
	{
		return this.numberOfPayments;
	}
	
	public Integer getNumberOfAdvPayments() 
	{
		return this.numberOfAdvPayments;
	}
	
	public Integer getNumberOfSkipPayments() 
	{
		return this.numberOfSkipPayments;
	}
	
	public Double getMsrp() 
	{
		return this.msrp;
	}
	
	public Double getNada() 
	{
		return this.nada;
	}
	
	public Date getInterestStartDate() 
	{
		return this.interestStartDate;
	}
	
	public Date getFirstPaymentDate() 
	{
		return this.firstPaymentDate;
	}
	
	public String getLoanNumber() 
	{
		return this.loanNumber;
	}
	
	public String getGapIdNumber() 
	{
		return this.gapIdNumber;
	}
	
	public Double getLeaseCapAmount() 
	{
		return this.leaseCapAmount;
	}
	
	public Integer getPeriod() 
	{
		return this.period;
	}
	
	public Date getStartDate() 
	{
		return this.startDate;
	}
	
	public Date getEndDate() 
	{
		return this.endDate;
	}
	
	public String getIsPaymentPlan() 
	{
		return isPaymentPlan;
	}
	public void setIsPaymentPlan(String isPaymentPlan) 
	{
		this.isPaymentPlan = isPaymentPlan;
	}
	
	/**
	 * Validates the finance details.<br>
	 * The finance details are invalid if any of the following occur:
	 * <ul>
	 * <li>the financingType is null or invalid</li>
	 * <li>the ballonResidualAmount is less than zero</li>
	 * <li>the financedAmount is less than zero</li>
	 * <li>the apr is less than zero</li>
	 * <li>the paymentAmount is less than zero</li>
	 * <li>the numberOfPayments is less than zero</li>
	 * <li>the numberOfAdvPayments is less than zero</li>
	 * <li>the numberOfSkipPayments is less than zero</li>
	 * <li>the msrp is less than zero</li>
	 * <li>the nada is less than zero</li>
	 * <li>the interestStartDate cannot be a past date</li>
	 * <li>the firstPaymentDate cannot be a past date</li>
	 * <li>the loanNumber is blank or contains whitespaces only</li>
	 * <li>the gapIdNumber is blank or contains whitespaces only</li>
	 * <li>the leaseCapAmount is less than zero</li>
	 * <li>the period is less than zero</li>
	 * <li>the startDate cannot be a past date</li>
	 * <li>the endDate cannot be a past date</li>
	 * </ul>
	 * @param contentType
	 * @return financeErrors
	 */
	public Map validate(String contentType) 
	{
		Map<String,String> financeErrors = new HashMap<String,String>();
		try {
			this.financingTypeEnum = FinancingType.find(this.financeType);
			} 
		catch (Exception exception) 
		{
			financeErrors.put("financeDetails financingType", "The finance details financing type should be one of the following values:"+ArrayUtils.toString(FinancingType.values())+".");
		}
		if (this.baloonResidualValue != null && this.baloonResidualValue < 0) 
		{
			financeErrors.put("financeDetails ballonResidualAmount", "The finance details ballon residual amount cannot be less than zero.");
		}
		if (this.apr != null && this.apr < 0) 
		{
			financeErrors.put("financeDetails apr", "The finance details apr cannot be less than zero.");
		}
		if (this.numberOfPayments != null && this.numberOfPayments < 0) 
		{
			financeErrors.put("financeDetails numberOfPayments", "The finance details number of payments cannot be less than zero.");
		}
		if (this.numberOfAdvPayments != null && this.numberOfAdvPayments < 0) 
		{
			financeErrors.put("financeDetails numberOfAdvPayments", "The finance details number of advanced payments cannot be less than zero.");
		}
		if (this.numberOfSkipPayments != null && this.numberOfSkipPayments < 0) 
		{
			financeErrors.put("financeDetails numberOfSkipPayments", "The finance details number of skip payments cannot be less than zero.");
		}
		if (this.interestStartDate != null && this.interestStartDate.compareTo(Calendar.getInstance().getTime()) < 0) 
		{
			financeErrors.put("financeDetails interestStartDate", "The finance details interest start date cannot be a past date.");
		}
		/*if (StringUtils.isWhitespace(this.loanNumber)) 
		{
			financeErrors.put("financeDetails loanNumber", "The finance details loan number cannot be blank nor contain whitespaces only.");
		}*/
		if (StringUtils.isWhitespace(this.gapIdNumber)) 
		{
			financeErrors.put("financeDetails gapIdNumber", "The finance details gap id number cannot be blank nor contain whitespaces only.");
		}
		if (this.leaseCapAmount != null && this.leaseCapAmount < 0) 
		{
			financeErrors.put("financeDetails leaseCapAmount", "The finance details lease cap amount cannot be less than zero.");
		}
		if (this.period != null && this.period < 0) 
		{
			financeErrors.put("financeDetails period", "The finance details period cannot be less than zero.");
		}
		// TODO: Validate end date >= start date.
		if (this.endDate != null && this.endDate.compareTo(Calendar.getInstance().getTime()) < 0) 
		{
			financeErrors.put("financeDetails endDate", "The finance details end date cannot be a past date.");
		}
		
		return financeErrors;
	}

	public Double getBaloonResidualValue() {
		return baloonResidualValue;
	}

	public void setBaloonResidualValue(Double baloonResidualValue) {
		this.baloonResidualValue = baloonResidualValue;
	}

	public void setFinancingTypeEnum(FinancingType financingTypeEnum) {
		this.financingTypeEnum = financingTypeEnum;
	}

	public void setFinancedAmount(Double financedAmount) {
		this.financedAmount = financedAmount;
	}

	public void setApr(Double apr) {
		this.apr = apr;
	}

	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public void setNumberOfPayments(Integer numberOfPayments) {
		this.numberOfPayments = numberOfPayments;
	}

	public void setNumberOfAdvPayments(Integer numberOfAdvPayments) {
		this.numberOfAdvPayments = numberOfAdvPayments;
	}

	public void setNumberOfSkipPayments(Integer numberOfSkipPayments) {
		this.numberOfSkipPayments = numberOfSkipPayments;
	}

	public void setMsrp(Double msrp) {
		this.msrp = msrp;
	}

	public void setNada(Double nada) {
		this.nada = nada;
	}

	public void setInterestStartDate(Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}

	public void setFirstPaymentDate(Date firstPaymentDate) {
		this.firstPaymentDate = firstPaymentDate;
	}

	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}

	public void setGapIdNumber(String gapIdNumber) {
		this.gapIdNumber = gapIdNumber;
	}

	public void setLeaseCapAmount(Double leaseCapAmount) {
		this.leaseCapAmount = leaseCapAmount;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	
	@Override
	public String toString() 
	{
		return new ToStringBuilder(this).append("financingType", this.financeType).append("ballonResidualAmount", this.baloonResidualValue)
				.append("financedAmount", this.financedAmount).append("apr", this.apr).append("paymentAmount", this.paymentAmount)
				.append("numberOfPayments", this.numberOfPayments).append("numberOfAdvPayments", this.numberOfAdvPayments).append("totalAllowableMilesContract", this.totalAllowableMilesContract)
				.append("numberOfSkipPayments", this.numberOfSkipPayments).append("msrp", this.msrp).append("nada", this.nada)
				.append("interestStartDate", this.interestStartDate).append("firstPaymentDate", this.firstPaymentDate).append("loanNumber", this.loanNumber)
				.append("gapIdNumber", this.gapIdNumber).append("leaseCapAmount", this.leaseCapAmount).append("period", this.period)
				.append("startDate", this.startDate).append("endDate", this.endDate).append("scheduledTerminationDate", this.scheduledTerminationDate).append("isPaymentPlan", this.isPaymentPlan).append("paymentDueDay", this.paymentDueDay).toString();
	}
	
}
