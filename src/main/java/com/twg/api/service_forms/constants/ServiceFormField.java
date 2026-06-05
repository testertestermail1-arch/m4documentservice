package com.twg.api.service_forms.constants;

import java.util.ArrayList;
import java.util.List;

public enum ServiceFormField {
	
	//general fileds 
	sourceOriginator(25), 
	urlOnly(1,"(Y,N)",true),
	contractNumber(25),
	fileType(25,"(pdf,gzip)",true),
	sendSignature(5,"(true,false)",true),
	stampSignature(5,"(true,false)",true),
	quoteId(25),
	
	//account fileds
	accountNumber(50),
	name(100),
	address1(100),
	address2(100),
	city(100),
	stateCode(2),
	postalCode(10),
	countryCode(3),
	homePhoneNumber(15),
	mobilePhoneNumber(15),
	workPhoneNumber(15),
	
	//fsmFields
	fsmFirstName(100),
	fsmLastName(100),
	
	//vehicle fields
	odometer(6,false),
	vin(17),
	vehicleYear(4,false),
	vehicleType(25),
	vehicleCode(150),
	model(100),
	make(100),
	trim(100),
	inServiceDate(10),
	vehicleClass(10),
	vehiclePurchasePrice(11,2,false),
	fuelType(1),
	driveTrain(3),
	turbo(1),
	supercharged(1),
	hasNavigation(1),
	vehicleNewOrUsed(1),
	expirationType(10),
	maturityDate(10),
	
	
	//product 
	//deductibleBase(4,2,false),
	deductibleBase(4,false),
	coverage(255),
	termMonths(3,false), 
	productCode(25),
	productDetailType(3),
	productClass(60),
	agreementStatus(6),
	effectiveDate(10),
	expirationDate(10),
	formNumber(100),
	isCombo(9),
	reportedCustomerCost(9,2,false),
	salesTax(9,2,false),
	deductibleReduced(4,false),
	serviceInterval(6,false),
	numberOfServices(9,false),
	termDistance(6,false),
	coverageType(255),
	coverageAmount(9,2,false),
	systemPin(17),
	//holder fields
	customerFirstName(100),
	customerLastName(100),
	customerAddress1(100),
	customerCity(100),
	customerStateCode(25),
	customerPostalCode(10,false), 
	customerCountryCode(25),
	customerHomePhoneNumber(15, false),
	customerWorkPhoneNumber(15,false),
	customerMobilePhoneNumber(15,false),
	customerEveningPhoneNumber(15,false),
	customerEmail(80),
	memberNumber(255),
	salutation(25),
	customerMiddleName(25),
	customerAddress2(100),
	coBuyerFirstName(100),
	coBuyerMiddleName(25),
	coBuyerLastName(100),
	coBuyerAddress1(100),
	coBuyerAddress2(100),
	coBuyerCity(100),
	coBuyerStateCode(25),
	coBuyerPostalCode(10),
	coBuyerCountryCode(25),
	coBuyerHomePhoneNumber(15,false),
	coBuyerWorkPhoneNumber(15,false),
	coBuyerMobilePhoneNumber(15,false),
	coBuyerEmail(80),
	
	
	
	//customerHomePhoneNumber(15,10,15 ),
	
	//mandatory for GAP
	//finance fields
	lienholderName(100),
	lienholderAddress1(100),
	lienholderCity(100),
	lienholderState(25),
	lienholderPostalCode(10),
	lienholderPhoneNumber(15),
	///financeType(7),
	financeType(9,"(Finance, Cash, Lease, Loan, OtherLoan, Balloon)",true),
	baloonResidualValue(9,2,false),
	annualPercentageRate(4),
	msrp(9,2,false),
	lienholderAddress2(100),
	lienholderNumber(25),
	lienholderAccountNumber(25),
	financedAmount(9,2,false),
	loanTotalPayment(9,2,false),
	financeTermMonths(3,false),
	numberOfAdvPayments(3,false),
	numberOfSkipPayments(3,false),
	bookValue(9,2,false),
	loanNumber(25),
	gapIdNumber(25),
	leaseCapAmount(9,2,false),
	period(3,false),
	grossCapCost(9,2,false),
	totalAllowableMilesContract(5,false),
	loanMonthlyPayment(9,2,false),
	interestStartDate(10),
	dateOfFirstPayment(10),
	scheduledTerminationDate(10),
	paymentDueDay(2,false),
	installmentDownPayment(9,2,false),
	installmentAmount(9,2,false),
	installmentPaymentFrequency(50),
	installmentPaymentTerm(3,false);
	
	ServiceFormField(int length, String format) {
		this.length = length;
		this.format = invalidFormatMessage + " for ["+ this.name() +"]." + " Allowed Length:" + length +" Format:"+ format +"" ;
		this.scale = 0;
		this.choice=false;
		this.allowNegative=false;
		this.minInteger=0;
		this.maxInteger=0;
		this.minDecimal=0;
		this.maxDecimal=0;
	}
	
	ServiceFormField(int length) {
		this.length = length;
		this.format = invalidFormatMessage + " for ["+this.name()+"]." + " Allowed Length:" + length +"";
		this.scale = 0;
		this.choice=false;
		this.allowNegative=false;
		this.minInteger=0;
		this.maxInteger=0;
		this.minDecimal=0;
		this.maxDecimal=0;
	}
	
	ServiceFormField(int length, boolean allowNegativeValue) {
		this.length = length;
		this.format = invalidFormatMessage + " for ["+this.name()+"]." + "Please enter "+(allowNegativeValue?"an ":"a positive ")+ "integer with Precision:" + length + "";
		this.scale = 0;
		this.choice=false;
		this.allowNegative = allowNegativeValue;
		this.minInteger=0;
		this.maxInteger=0;
		this.minDecimal=0;
		this.maxDecimal=0;
	}
	
	ServiceFormField(int length, int minInteger, int maxInteger) {
		this.length = length;
		this.format = invalidFormatMessage + " for ["+this.name()+"]." + " Allowed Length:" + length + "";
		this.scale = 0;
		this.choice=false;
		this.minInteger=minInteger;
		this.maxInteger=maxInteger;
		this.minDecimal=0;
		this.maxDecimal=0;
		this.allowNegative = false;
	}
	
	ServiceFormField(String format) {
		this.length = 0;
		this.format = format;
		this.scale = 0;
		this.choice=false;
		this.minInteger=0;
		this.maxInteger=0;
		this.minDecimal=0;
		this.maxDecimal=0;
		this.allowNegative = false;
	}
	
	ServiceFormField(int length, int scale, double minDecimal, double maxDecimal) {
		this.length = length;
		this.format = invalidFormatMessage + " for ["+this.name()+"]." + " Allowed Precision and Scale:("+length+","+ scale +")";
		this.scale = scale;
		this.choice=false;
		this.allowNegative=false;
		this.minInteger=0;
		this.maxInteger=0;
		this.minDecimal=minDecimal;
		this.maxDecimal=maxDecimal;
	}
	
	ServiceFormField(int length, int scale, boolean allowNegativeValue) {
		this.length = length;
		this.format = invalidFormatMessage + " for ["+this.name()+"]." + "Please enter a "+(allowNegativeValue?"":"positive ") +"value with Precision:("+length+","+ scale +")";
		this.scale = scale;
		this.choice=false;	
		this.allowNegative=false;
		this.minInteger=0;
		this.maxInteger=0;
		this.minDecimal=0;
		this.maxDecimal=0;
	}
	
	ServiceFormField(int length, String format, boolean choice) {
		this.length = length;
		this.format = invalidFormatMessage + " for ["+this.name()+"]." + " Allowed Length:"+length+" Allowed Values:"+format+"";
		this.scale = 0;
		this.choice=choice;
		this.allowNegative=false;
		this.minInteger=0;
		this.maxInteger=0;
		this.minDecimal=0;
		this.maxDecimal=0;
	}

	
	private final int length;
	private final int scale;
	private final String format;
	private final boolean choice;
	private final int minInteger;
	private final int maxInteger;
	private final double minDecimal;
	private final double maxDecimal;
	private final boolean allowNegative;
	
	private static final String invalidFormatMessage = "Invalid Value Received";

	public int getLength() {
		return length;
	}
	
	public String getFormat() {
		return format;
	}
	
	
	public int getScale() {
		return scale;
	}
	
	public boolean isChoice() {
		return choice;
	}
	
	public int getMinInteger() {
		return minInteger;
	}

	public int getMaxInteger() {
		return maxInteger;
	}

	public double getMinDecimal() {
		return minDecimal;
	}

	public double getMaxDecimal() {
		return maxDecimal;
	}

	public boolean isAllowNegative() {
		return allowNegative;
	}
	
	public static List<String> formattedJsonError(String inputString)
	{
		List<String> errors = new ArrayList<String>();
		if(inputString!=null && !"".equals(inputString) && inputString.contains("Schema.json")){
	      for(ServiceFormField field:values()){
	           if (inputString.contains(field.name())) 
	        	   errors.add(field.format);
	      }
	      return errors;
		}
	   return null;
	  } 


}
