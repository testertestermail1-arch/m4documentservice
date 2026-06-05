package com.twg.api.service_forms.validation;

import java.util.ArrayList;
import java.util.List;

import com.twg.api.service_forms.constants.ServiceFormField;

public class ServiceFormRequest {
	
	
	
	@Override 
	public String toString() {
		return "ServiceFormRequest [sourceOriginator=" + sourceOriginator + ", urlOnly=" + urlOnly+ ", fileType=" + fileType+ ", quoteId=" + quoteId+ ", contractNumber=" + contractNumber + ", sendSignature=" + sendSignature + ", stampSignature=" + stampSignature
				//Account fields
				+ ", accountNumber=" + accountNumber + ", name=" + name+ ", address1=" + address1+ ", address2=" + address2+ ", city=" + city
				+ ", stateCode=" + stateCode+ ", postalCode=" + postalCode+ ", countryCode=" + countryCode+ ", homePhoneNumber=" + homePhoneNumber+ ", workPhoneNumber=" + workPhoneNumber+ ", mobilePhoneNumber=" + mobilePhoneNumber
				
				//FSM FIELDS
				+ ", fsmFirstName=" + fsmFirstName + ", fsmLastName=" + fsmLastName 												
				
				//vehicle fields
				+ ", vehicleType=" + vehicleType+ ", vehicleCode=" + vehicleCode+ ", model=" + model+ ", make=" + make+ ", trim=" + trim+ ", inServiceDate=" + inServiceDate
				+ ", vehicleYear=" + vehicleYear + ", odometer=" + odometer + ", VIN=" + VIN
				+ ", vehicleClass=" + vehicleClass+ ", vehiclePurchasePrice=" + vehiclePurchasePrice+ ", fuelType=" + fuelType+ ", driveTrain=" + driveTrain+ ", turbo=" + turbo+ ", supercharged=" + supercharged
				+ ", commercialUsage=" + commercialUsage+ ", hasNavigation=" + hasNavigation+ ", vehicleNewOrUsed=" + vehicleNewOrUsed+ ", expirationType=" + expirationType+ ", maturityDate=" + maturityDate
												
				//product fileds
				+ ", productCode=" + productCode+ ", productDetailType=" + productDetailType+ ", productClass=" + productClass+ ", agreementStatus=" + agreementStatus+ ", effectiveDate=" + effectiveDate+ ", expirationDate=" + expirationDate
				+ ", formNumber=" + formNumber+ ", isCombo=" + isCombo+ ", reportedCustomerCost=" + reportedCustomerCost+ ", salesTax=" + salesTax+ ", deductibleBase=" + deductibleBase+ ", deductibleReduced=" + deductibleReduced
				+ ", serviceInterval=" + serviceInterval+ ", numberOfServices=" + numberOfServices+ ", coverage=" + coverage+ ", termMonths=" + termMonths+ ", termDistance=" + termDistance + ", autoRenew=" + autoRenew + ", systemPin=" + systemPin
				
				//optionalCoverages
				 + ", coverageType=" + coverageType + ", coverageAmount=" + coverageAmount	
				
				
				//customer holder fileds
				+  ", memberNumber=" + memberNumber+ ", salutation=" + salutation + ", customerMiddleName=" + customerMiddleName+ ", customerAddress2=" + customerAddress2
				+  ", customerFirstName=" + customerFirstName+ ", customerLastName=" + customerLastName + ", customerAddress1=" + customerAddress1+ ", customerCity=" + customerCity+ ", customerStateCode=" + customerStateCode
				+  ", customerPostalCode=" + customerPostalCode+ ", customerCountryCode=" + customerCountryCode + ", customerHomePhoneNumber=" + customerHomePhoneNumber+ ", customerWorkPhoneNumber=" + customerWorkPhoneNumber+ ", customerMobilePhoneNumber=" + customerMobilePhoneNumber
				+  ", customerEveningPhoneNumber=" + customerEveningPhoneNumber+ ", customerEmail=" + customerEmail + ", coBuyerFirstName=" + coBuyerFirstName+ ", coBuyerMiddleName=" + coBuyerMiddleName+ ", coBuyerLastName=" + coBuyerLastName
				+  ", coBuyerAddress1=" + coBuyerAddress1+ ", coBuyerAddress2=" + coBuyerAddress2 + ", coBuyerCity=" + coBuyerCity+ ", coBuyerStateCode=" + coBuyerStateCode+ ", coBuyerPostalCode=" + coBuyerPostalCode
				+  ", coBuyerCountryCode=" + coBuyerCountryCode+ ", coBuyerHomePhoneNumber=" + coBuyerHomePhoneNumber + ", coBuyerWorkPhoneNumber=" + coBuyerWorkPhoneNumber+ ", coBuyerMobilePhoneNumber=" + coBuyerMobilePhoneNumber+ ", coBuyerEmail=" + coBuyerEmail
				
				//FINANCE FILEDS
				+ ", lienholderName=" + lienholderName + ", lienholderAddress1=" + lienholderAddress1  + ", lienholderAddress2=" + lienholderAddress2  + ", lienholderCity=" + lienholderCity+ ", lienholderState=" + lienholderState
				+ ", lienholderPostalCode=" + lienholderPostalCode + ", lienholderPhoneNumber=" + lienholderPhoneNumber + ", lienholderNumber=" + lienholderNumber + ", lienholderAccountNumber=" + lienholderAccountNumber 
				+ ", financedAmount=" + financedAmount + ", annualPercentageRate=" + annualPercentageRate + ", financeType=" + financeType + ", baloonResidualValue=" + baloonResidualValue 
				+ ", loanTotalPayment=" + loanTotalPayment + ", financeTermMonths=" + financeTermMonths + ", numberOfAdvPayments=" + numberOfAdvPayments+ ", numberOfSkipPayments=" + numberOfSkipPayments + ", msrp=" + msrp 
				+ ", bookValue=" + bookValue + ", interestStartDate=" + interestStartDate + ", dateOfFirstPayment=" + dateOfFirstPayment + ", loanNumber=" + loanNumber 
				+ ", gapIdNumber=" + gapIdNumber + ", leaseCapAmount=" + leaseCapAmount + ", period=" + period + ", startDate=" + startDate 
				+ ", endDate=" + endDate + ", grossCapCost=" + grossCapCost + ", totalAllowableMilesContract=" + totalAllowableMilesContract + ", scheduledTerminationDate=" + scheduledTerminationDate 
                + ", loanMonthlyPayment=" + loanMonthlyPayment + ", paymentDueDay=" + paymentDueDay + ", installmentDownPayment=" + installmentDownPayment + ", installmentAmount=" + installmentAmount + " , installmentPaymentFrequency=" + installmentPaymentFrequency + ", installmentPaymentTerm=" + installmentPaymentTerm + "]";
	       }
	

	//getter and setter methods
	
	public String getSourceOriginator() {
		return sourceOriginator;
	}
	public void setSourceOriginator(String sourceOriginator) {
		this.sourceOriginator = sourceOriginator;
	}
	public String getUrlOnly() {
		return urlOnly;
	}
	public void setUrlOnly(String urlOnly) {
		this.urlOnly = urlOnly;
	}
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public String getSendSignature() {
		return sendSignature;
	}
	public void setsendSignature(String sendSignature) {
		this.sendSignature = sendSignature;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getOdometer() {
		return odometer;
	}
	public void setOdometer(Integer odometer) {
		this.odometer = odometer;
	}
	public String getVIN() {
		return VIN;
	}
	public void setVIN(String vIN) {
		VIN = vIN;
	}
	public Integer getVehicleYear() {
		return vehicleYear;
	}
	public void setVehicleYear(Integer vehicleYear) {
		this.vehicleYear = vehicleYear;
	}
	public Integer  getDeductibleBase() {
		return deductibleBase;
	}
	public void setDeductibleBase(Integer deductibleBase) {
		this.deductibleBase = deductibleBase;
	}
	public String getCoverage() {
		return coverage;
	}
	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}
	public Integer getTermMonths() {
		return termMonths;
	}
	public void setTermMonths(Integer termMonths) {
		this.termMonths = termMonths;
	}
	public String getAutoRenew() {
		return autoRenew;
	}
	public void setAutoRenew(String autoRenew) {
		this.autoRenew = autoRenew;
	}
	public String getSystemPin() {
		return systemPin;
	}
	public void setSystemPin(String systemPin) {
		this.systemPin = systemPin;
	}
	public String getCustomerFirstName() {
		return customerFirstName;
	}
	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}
	public String getCustomerLastName() {
		return customerLastName;
	}
	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}
	public String getCustomerAddress1() {
		return customerAddress1;
	}
	public void setCustomerAddress1(String customerAddress1) {
		this.customerAddress1 = customerAddress1;
	}
	public String getCustomerCity() {
		return customerCity;
	}
	public void setCustomerCity(String customerCity) {
		this.customerCity = customerCity;
	}
	public String getCustomerStateCode() {
		return customerStateCode;
	}
	public void setCustomerStateCode(String customerStateCode) {
		this.customerStateCode = customerStateCode;
	}
	public String getCustomerPostalCode() {
		return customerPostalCode;
	}
	public void setCustomerPostalCode(String customerPostalCode) {
		this.customerPostalCode = customerPostalCode;
	}
	public String getCustomerCountryCode() {
		return customerCountryCode;
	}
	public void setCustomerCountryCode(String customerCountryCode) {
		this.customerCountryCode = customerCountryCode;
	}
	public String getCustomerHomePhoneNumber() {
		return customerHomePhoneNumber;
	}
	public void setCustomerHomePhoneNumber(String customerHomePhoneNumber) {
		this.customerHomePhoneNumber = customerHomePhoneNumber;
	}
	public String getLienholderName() {
		return lienholderName;
	}
	public void setLienholderName(String lienholderName) {
		this.lienholderName = lienholderName;
	}
	public String getLienholderAddress1() {
		return lienholderAddress1;
	}
	public void setLienholderAddress1(String lienholderAddress1) {
		this.lienholderAddress1 = lienholderAddress1;
	}
	public String getLienholderCity() {
		return lienholderCity;
	}
	public void setLienholderCity(String lienholderCity) {
		this.lienholderCity = lienholderCity;
	}
	public String getLienholderState() {
		return lienholderState;
	}
	public void setLienholderState(String lienholderState) {
		this.lienholderState = lienholderState;
	}
	public String getLienholderPostalCode() {
		return lienholderPostalCode;
	}
	public void setLienholderPostalCode(String lienholderPostalCode) {
		this.lienholderPostalCode = lienholderPostalCode;
	}
	public String getLienholderPhoneNumber() {
		return lienholderPhoneNumber;
	}
	public void setLienholderPhoneNumber(String lienholderPhoneNumber) {
		this.lienholderPhoneNumber = lienholderPhoneNumber;
	}
	public String getFinanceType() {
		return financeType;
	}
	public void setFinanceType(String financeType) {
		this.financeType = financeType;
	}
	public Double  getBaloonResidualValue() {
		return baloonResidualValue;
	}
	public void setBaloonResidualValue(Double baloonResidualValue) {
		this.baloonResidualValue = baloonResidualValue;
	}
	public Integer getAnnualPercentageRate() {
		return annualPercentageRate;
	}
	public void setAnnualPercentageRate(Integer annualPercentageRate) {
		this.annualPercentageRate = annualPercentageRate;
	}
	public Double getMsrp() {
		return msrp;
	}
	public void setMsrp(Double msrp) {
		this.msrp = msrp;
	}
	
	// variables
	
	private List<ServiceFormField> erroredFields = new ArrayList<ServiceFormField>();
	private List<String> errorMessages = new ArrayList<String>();
	private List<String> coverageType = new ArrayList<String>();
	private List<Double> coverageAmount = new ArrayList<Double>();
	
	
	
	public List<ServiceFormField> getErroredFields() {
		return erroredFields;
	}
	public void setErroredFields(List<ServiceFormField> erroredFields) {
		this.erroredFields = erroredFields;
	}
	public List<String> getErrorMessages() {
		return errorMessages;
	}
	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}


   
	public String getFileType() {
		return fileType;
	}



	public void setFileType(String fileType) {
		this.fileType = fileType;
	}



	public String getQuoteId() {
		return quoteId;
	}



	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}



	public String getAddress1() {
		return address1;
	}



	public void setAddress1(String address1) {
		this.address1 = address1;
	}



	public String getAddress2() {
		return address2;
	}



	public void setAddress2(String address2) {
		this.address2 = address2;
	}



	public String getCity() {
		return city;
	}



	public void setCity(String city) {
		this.city = city;
	}



	public String getStateCode() {
		return stateCode;
	}



	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}



	public String getPostalCode() {
		return postalCode;
	}



	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}



	public String getCountryCode() {
		return countryCode;
	}



	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}



	public String getHomePhoneNumber() {
		return homePhoneNumber;
	}



	public void setHomePhoneNumber(String homePhoneNumber) {
		this.homePhoneNumber = homePhoneNumber;
	}



	public String getWorkPhoneNumber() {
		return workPhoneNumber;
	}



	public void setWorkPhoneNumber(String workPhoneNumber) {
		this.workPhoneNumber = workPhoneNumber;
	}



	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}



	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}



	public String getFsmFirstName() {
		return fsmFirstName;
	}



	public void setFsmFirstName(String fsmFirstName) {
		this.fsmFirstName = fsmFirstName;
	}



	public String getFsmLastName() {
		return fsmLastName;
	}



	public void setFsmLastName(String fsmLastName) {
		this.fsmLastName = fsmLastName;
	}



	public String getVehicleType() {
		return vehicleType;
	}



	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}



	public String getVehicleCode() {
		return vehicleCode;
	}



	public void setVehicleCode(String vehicleCode) {
		this.vehicleCode = vehicleCode;
	}



	public String getModel() {
		return model;
	}



	public void setModel(String model) {
		this.model = model;
	}



	public String getMake() {
		return make;
	}



	public void setMake(String make) {
		this.make = make;
	}



	public String getTrim() {
		return trim;
	}



	public void setTrim(String trim) {
		this.trim = trim;
	}



	public String getInServiceDate() {
		return inServiceDate;
	}



	public void setInServiceDate(String inServiceDate) {
		this.inServiceDate = inServiceDate;
	}



	public String getVehicleClass() {
		return vehicleClass;
	}



	public void setVehicleClass(String vehicleClass) {
		this.vehicleClass = vehicleClass;
	}



	public Double getVehiclePurchasePrice() {
		return vehiclePurchasePrice;
	}



	public void setVehiclePurchasePrice(Double vehiclePurchasePrice) {
		this.vehiclePurchasePrice = vehiclePurchasePrice;
	}



	public String getFuelType() {
		return fuelType;
	}



	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}



	public String getDriveTrain() {
		return driveTrain;
	}



	public void setDriveTrain(String driveTrain) {
		this.driveTrain = driveTrain;
	}



	public String getTurbo() {
		return turbo;
	}



	public void setTurbo(String turbo) {
		this.turbo = turbo;
	}



	public String getSupercharged() {
		return supercharged;
	}



	public void setSupercharged(String supercharged) {
		this.supercharged = supercharged;
	}



	public String getCommercialUsage() {
		return commercialUsage;
	}



	public void setCommercialUsage(String commercialUsage) {
		this.commercialUsage = commercialUsage;
	}



	public String getHasNavigation() {
		return hasNavigation;
	}



	public void setHasNavigation(String hasNavigation) {
		this.hasNavigation = hasNavigation;
	}



	public String getVehicleNewOrUsed() {
		return vehicleNewOrUsed;
	}



	public void setVehicleNewOrUsed(String vehicleNewOrUsed) {
		this.vehicleNewOrUsed = vehicleNewOrUsed;
	}



	public String getExpirationType() {
		return expirationType;
	}



	public void setExpirationType(String expirationType) {
		this.expirationType = expirationType;
	}



	public String getMaturityDate() {
		return maturityDate;
	}



	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}



	public String getProductCode() {
		return productCode;
	}



	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}



	public String getProductDetailType() {
		return productDetailType;
	}



	public void setProductDetailType(String productDetailType) {
		this.productDetailType = productDetailType;
	}



	public String getProductClass() {
		return productClass;
	}



	public void setProductClass(String productClass) {
		this.productClass = productClass;
	}



	public String getAgreementStatus() {
		return agreementStatus;
	}



	public void setAgreementStatus(String agreementStatus) {
		this.agreementStatus = agreementStatus;
	}



	public String getEffectiveDate() {
		return effectiveDate;
	}



	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}



	public String getExpirationDate() {
		return expirationDate;
	}



	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}



	public String getFormNumber() {
		return formNumber;
	}



	public void setFormNumber(String formNumber) {
		this.formNumber = formNumber;
	}



	public String getIsCombo() {
		return isCombo;
	}



	public void setIsCombo(String isCombo) {
		this.isCombo = isCombo;
	}



	public Double getReportedCustomerCost() {
		return reportedCustomerCost;
	}



	public void setReportedCustomerCost(Double reportedCustomerCost) {
		this.reportedCustomerCost = reportedCustomerCost;
	}



	public Double getSalesTax() {
		return salesTax;
	}



	public void setSalesTax(Double salesTax) {
		this.salesTax = salesTax;
	}



	public Integer getDeductibleReduced() {
		return deductibleReduced;
	}



	public void setDeductibleReduced(Integer deductibleReduced) {
		this.deductibleReduced = deductibleReduced;
	}



	public Integer getServiceInterval() {
		return serviceInterval;
	}



	public void setServiceInterval(Integer serviceInterval) {
		this.serviceInterval = serviceInterval;
	}



	public Integer getNumberOfServices() {
		return numberOfServices;
	}



	public void setNumberOfServices(Integer numberOfServices) {
		this.numberOfServices = numberOfServices;
	}



	public Integer getTermDistance() {
		return termDistance;
	}



	public void setTermDistance(Integer termDistance) {
		this.termDistance = termDistance;
	}



	public List<String> getCoverageType() {
		return coverageType;
	}



	public void setCoverageType(List<String> coverageType) {
		this.coverageType = coverageType;
	}



	public List<Double> getCoverageAmount() {
		return coverageAmount;
	}



	public void setCoverageAmount(List<Double> coverageAmount) {
		this.coverageAmount = coverageAmount;
	}
	



	public String getMemberNumber() {
		return memberNumber;
	}



	public void setMemberNumber(String memberNumber) {
		this.memberNumber = memberNumber;
	}



	public String getSalutation() {
		return salutation;
	}



	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}



	public String getCustomerMiddleName() {
		return customerMiddleName;
	}



	public void setCustomerMiddleName(String customerMiddleName) {
		this.customerMiddleName = customerMiddleName;
	}



	public String getCustomerAddress2() {
		return customerAddress2;
	}



	public void setCustomerAddress2(String customerAddress2) {
		this.customerAddress2 = customerAddress2;
	}



	public String getCustomerWorkPhoneNumber() {
		return customerWorkPhoneNumber;
	}



	public void setCustomerWorkPhoneNumber(String customerWorkPhoneNumber) {
		this.customerWorkPhoneNumber = customerWorkPhoneNumber;
	}



	public String getCustomerMobilePhoneNumber() {
		return customerMobilePhoneNumber;
	}



	public void setCustomerMobilePhoneNumber(String customerMobilePhoneNumber) {
		this.customerMobilePhoneNumber = customerMobilePhoneNumber;
	}



	public String getCustomerEveningPhoneNumber() {
		return customerEveningPhoneNumber;
	}



	public void setCustomerEveningPhoneNumber(String customerEveningPhoneNumber) {
		this.customerEveningPhoneNumber = customerEveningPhoneNumber;
	}



	public String getCustomerEmail() {
		return customerEmail;
	}



	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}



	public String getCoBuyerFirstName() {
		return coBuyerFirstName;
	}



	public void setCoBuyerFirstName(String coBuyerFirstName) {
		this.coBuyerFirstName = coBuyerFirstName;
	}



	public String getCoBuyerMiddleName() {
		return coBuyerMiddleName;
	}



	public void setCoBuyerMiddleName(String coBuyerMiddleName) {
		this.coBuyerMiddleName = coBuyerMiddleName;
	}



	public String getCoBuyerLastName() {
		return coBuyerLastName;
	}



	public void setCoBuyerLastName(String coBuyerLastName) {
		this.coBuyerLastName = coBuyerLastName;
	}



	public String getCoBuyerAddress1() {
		return coBuyerAddress1;
	}



	public void setCoBuyerAddress1(String coBuyerAddress1) {
		this.coBuyerAddress1 = coBuyerAddress1;
	}



	public String getCoBuyerAddress2() {
		return coBuyerAddress2;
	}



	public void setCoBuyerAddress2(String coBuyerAddress2) {
		this.coBuyerAddress2 = coBuyerAddress2;
	}



	public String getCoBuyerCity() {
		return coBuyerCity;
	}



	public void setCoBuyerCity(String coBuyerCity) {
		this.coBuyerCity = coBuyerCity;
	}



	public String getCoBuyerStateCode() {
		return coBuyerStateCode;
	}



	public void setCoBuyerStateCode(String coBuyerStateCode) {
		this.coBuyerStateCode = coBuyerStateCode;
	}



	public String getCoBuyerPostalCode() {
		return coBuyerPostalCode;
	}



	public void setCoBuyerPostalCode(String coBuyerPostalCode) {
		this.coBuyerPostalCode = coBuyerPostalCode;
	}



	public String getCoBuyerCountryCode() {
		return coBuyerCountryCode;
	}



	public void setCoBuyerCountryCode(String coBuyerCountryCode) {
		this.coBuyerCountryCode = coBuyerCountryCode;
	}



	public String getCoBuyerHomePhoneNumber() {
		return coBuyerHomePhoneNumber;
	}



	public void setCoBuyerHomePhoneNumber(String coBuyerHomePhoneNumber) {
		this.coBuyerHomePhoneNumber = coBuyerHomePhoneNumber;
	}



	public String getCoBuyerWorkPhoneNumber() {
		return coBuyerWorkPhoneNumber;
	}



	public void setCoBuyerWorkPhoneNumber(String coBuyerWorkPhoneNumber) {
		this.coBuyerWorkPhoneNumber = coBuyerWorkPhoneNumber;
	}



	public String getCoBuyerMobilePhoneNumber() {
		return coBuyerMobilePhoneNumber;
	}



	public void setCoBuyerMobilePhoneNumber(String coBuyerMobilePhoneNumber) {
		this.coBuyerMobilePhoneNumber = coBuyerMobilePhoneNumber;
	}



	public String getCoBuyerEmail() {
		return coBuyerEmail;
	}



	public void setCoBuyerEmail(String coBuyerEmail) {
		this.coBuyerEmail = coBuyerEmail;
	}



	public String getLienholderAddress2() {
		return lienholderAddress2;
	}



	public void setLienholderAddress2(String lienholderAddress2) {
		this.lienholderAddress2 = lienholderAddress2;
	}



	public String getLienholderNumber() {
		return lienholderNumber;
	}



	public void setLienholderNumber(String lienholderNumber) {
		this.lienholderNumber = lienholderNumber;
	}



	public String getLienholderAccountNumber() {
		return lienholderAccountNumber;
	}



	public void setLienholderAccountNumber(String lienholderAccountNumber) {
		this.lienholderAccountNumber = lienholderAccountNumber;
	}



	public Double getFinancedAmount() {
		return financedAmount;
	}



	public void setFinancedAmount(Double financedAmount) {
		this.financedAmount = financedAmount;
	}



	public Double getLoanTotalPayment() {
		return loanTotalPayment;
	}



	public void setLoanTotalPayment(Double loanTotalPayment) {
		this.loanTotalPayment = loanTotalPayment;
	}



	public Integer getFinanceTermMonths() {
		return financeTermMonths;
	}



	public void setFinanceTermMonths(Integer financeTermMonths) {
		this.financeTermMonths = financeTermMonths;
	}



	public Integer getNumberOfAdvPayments() {
		return numberOfAdvPayments;
	}



	public void setNumberOfAdvPayments(Integer numberOfAdvPayments) {
		this.numberOfAdvPayments = numberOfAdvPayments;
	}



	public Double getBookValue() {
		return bookValue;
	}



	public void setBookValue(Double bookValue) {
		this.bookValue = bookValue;
	}



	public String getInterestStartDate() {
		return interestStartDate;
	}



	public void setInterestStartDate(String interestStartDate) {
		this.interestStartDate = interestStartDate;
	}



	public String getDateOfFirstPayment() {
		return dateOfFirstPayment;
	}



	public void setDateOfFirstPayment(String dateOfFirstPayment) {
		this.dateOfFirstPayment = dateOfFirstPayment;
	}



	public String getLoanNumber() {
		return loanNumber;
	}



	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}



	public String getGapIdNumber() {
		return gapIdNumber;
	}



	public void setGapIdNumber(String gapIdNumber) {
		this.gapIdNumber = gapIdNumber;
	}



	public Double getLeaseCapAmount() {
		return leaseCapAmount;
	}



	public void setLeaseCapAmount(Double leaseCapAmount) {
		this.leaseCapAmount = leaseCapAmount;
	}



	public Integer getPeriod() {
		return period;
	}



	public void setPeriod(Integer period) {
		this.period = period;
	}



	public String getStartDate() {
		return startDate;
	}



	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}



	public String getEndDate() {
		return endDate;
	}



	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}



	public Double getGrossCapCost() {
		return grossCapCost;
	}



	public void setGrossCapCost(Double grossCapCost) {
		this.grossCapCost = grossCapCost;
	}



	public Integer getTotalAllowableMilesContract() {
		return totalAllowableMilesContract;
	}



	public void setTotalAllowableMilesContract(Integer totalAllowableMilesContract) {
		this.totalAllowableMilesContract = totalAllowableMilesContract;
	}



	public String getScheduledTerminationDate() {
		return scheduledTerminationDate;
	}



	public void setScheduledTerminationDate(String scheduledTerminationDate) {
		this.scheduledTerminationDate = scheduledTerminationDate;
	}



	public Double getLoanMonthlyPayment() {
		return loanMonthlyPayment;
	}



	public void setLoanMonthlyPayment(Double loanMonthlyPayment) {
		this.loanMonthlyPayment = loanMonthlyPayment;
	}
	
	public Integer getNumberOfSkipPayments() {
		return numberOfSkipPayments;
	}


	public void setNumberOfSkipPayments(Integer numberOfSkipPayments) {
		this.numberOfSkipPayments = numberOfSkipPayments;
	}
	
	public String getStampSignature() {
		return stampSignature;
	}


	public void setStampSignature(String stampSignature) {
		this.stampSignature = stampSignature;
	}
	
	
	
	   public Integer getPaymentDueDay() {
		return paymentDueDay;
	}


	public void setPaymentDueDay(Integer paymentDueDay) {
		this.paymentDueDay = paymentDueDay;
	}

		//request fields
		//general
	    private String sourceOriginator;
		private String urlOnly;
		private String fileType;
		private String quoteId;
		private String contractNumber;
		private String sendSignature;
		private String stampSignature;
		
		
		//account fields
	    private String accountNumber;
		private String name;	
		private String address1;
		private String address2;
		private String city;
		private String stateCode;
		private String postalCode;
		private String countryCode;
		private String homePhoneNumber;
		private String workPhoneNumber;
		private String mobilePhoneNumber;
		
		//fsm fileds
		private String fsmFirstName;
		private String fsmLastName;
		
		//vehicle fields 
		private String vehicleType;
		private String vehicleCode;
		private String model;
		private String make;
		private String trim;
		private String inServiceDate;
		private Integer odometer;
		private String VIN;
		private String vehicleClass;
	    private Integer vehicleYear;
		private Double vehiclePurchasePrice;
		private String fuelType;
		private String driveTrain;
		private String turbo;
		private String supercharged;
		private String commercialUsage;
		private String hasNavigation;
		private String vehicleNewOrUsed;
		private String expirationType;
		private String maturityDate;
		
		
		//product fields
		private String  productCode;
		private String productDetailType;
		private String productClass;
		private String agreementStatus;
		private String effectiveDate;
		private String expirationDate;
		private String formNumber;
		private String isCombo;
		private Double reportedCustomerCost;

		private Double salesTax;
		private Integer deductibleBase;
		private Integer deductibleReduced;
		private Integer serviceInterval;
		private Integer numberOfServices;

		private String coverage;
	    private Integer termMonths;
	    private Integer termDistance;
	    private String autoRenew; 
	    private String systemPin;
	    
	   
	   // private String coverageType;
	   // private Double coverageAmount;
		
		
	   


		//holder fields 
	    private String memberNumber;
	    private String salutation;
		private String customerFirstName;
	    private String customerMiddleName;
		private String customerLastName;
		private String customerAddress1;
		private String customerAddress2;
		private String customerCity;
		private String customerStateCode;
		private String customerPostalCode;
		private String customerCountryCode;
		private String customerHomePhoneNumber;
		private String customerWorkPhoneNumber;
		private String customerMobilePhoneNumber;

	private String customerEveningPhoneNumber;
	private String customerEmail;
	private String coBuyerFirstName;
	private String coBuyerMiddleName;
	private String coBuyerLastName;
	private String coBuyerAddress1;
	private String coBuyerAddress2;
	private String coBuyerCity;
	private String coBuyerStateCode;
	private String coBuyerPostalCode;
	private String coBuyerCountryCode;
	private String coBuyerHomePhoneNumber;
	private String coBuyerWorkPhoneNumber;
	private String coBuyerMobilePhoneNumber;
	private String coBuyerEmail;
	
	
	//finance fields 
	private String lienholderName;
	private String lienholderAddress1;
	private String lienholderAddress2;
	private String lienholderCity;
	private String lienholderState;
	private String lienholderPostalCode;
	private String lienholderPhoneNumber;
	private String lienholderNumber;
	private String lienholderAccountNumber;
	private String financeType;
	private Double baloonResidualValue;
	private Double financedAmount;
	private Integer annualPercentageRate;
	private Double loanTotalPayment;
	private Integer financeTermMonths;
	private Integer numberOfAdvPayments;
	

	private Integer numberOfSkipPayments;
	private Double msrp;
	private Double bookValue;
	private String interestStartDate;
	private String dateOfFirstPayment;
	private String loanNumber;
	private String gapIdNumber;
	private Double leaseCapAmount;
	private Integer period;
	private String startDate;
	private String endDate;
	private Double grossCapCost;
	private Integer totalAllowableMilesContract;
	private String scheduledTerminationDate;
	private Double loanMonthlyPayment;
	private Integer paymentDueDay;
	private Double installmentDownPayment;
	private Double installmentAmount;
	private String installmentPaymentFrequency;
	private Integer installmentPaymentTerm;
	
	

	

	public String getInstallmentPaymentFrequency() {
		return installmentPaymentFrequency;
	}


	public void setInstallmentPaymentFrequency(String installmentPaymentFrequency) {
		this.installmentPaymentFrequency = installmentPaymentFrequency;
	}


	public Integer getInstallmentPaymentTerm() {
		return installmentPaymentTerm;
	}


	public void setInstallmentPaymentTerm(Integer installmentPaymentTerm) {
		this.installmentPaymentTerm = installmentPaymentTerm;
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
}
