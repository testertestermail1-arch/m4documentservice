package com.twg.api.experience_forms_api.request;


import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Holds the request information.
 * 
 * @author Juan Berrueta
 */

public class FormRequest 
{
	private Account account;
	private Vehicle vehicle;
	private Insurance insurance;
	private String quoteId;
	private String formsMessageId;
	private static Boolean isRequestTypeJSON;
	private String contractNumber;
	private CallingApp callingApp;
	private Set<ProductDetail> productDetails;
	private ProductDetail productDetail;
	private String fsmLastName;
	private FinanceDetails financeDetails;
	private Contact buyer;
	private Contact coBuyer;
	private Contact lienHolder;
	private String formPDFLocation;
	private String outputFileName;
	private Set<KeyValuePair> fillers;
	private String fsmFirstName;
	private String expirationType;
	private String folderName;
	private BrokerDetails brokerDetails;
	
	public BrokerDetails getBrokerDetails() {
		return brokerDetails;
	}

	public void setBrokerDetails(BrokerDetails brokerDetails) {
		this.brokerDetails = brokerDetails;
	}

	
	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getFsmFirstName() 
	{
		return fsmFirstName;
	}

	public void setFsmFirstName(String fsmFirstName) 
	{
		this.fsmFirstName = fsmFirstName;
	}

	public String getFsmLastName() 
	{
		return fsmLastName;
	}

	public void setFsmLastName(String fsmLastName) 
	{
		this.fsmLastName = fsmLastName;
	}
	public void setProductDetail(ProductDetail productDetail) 
	{
		this.productDetail = productDetail;
	}

	public String getQuoteId() 
	{
		return quoteId;
	}
	public void setQuoteId(String quoteId) 
	{
		this.quoteId = quoteId;
	}
	public FinanceDetails getFinanceDetails() 
	{
		return financeDetails;
	}

	public void setFinanceDetails(FinanceDetails financeDetails) 
	{
		this.financeDetails = financeDetails;
	}

	public Set<KeyValuePair> getFillers() 
	{
		return fillers;
	}
	
	public void setFillers(Set<KeyValuePair> fillers) 
	{
		this.fillers = fillers;
	}
	
	public String getOutputFileName() 
	{
		/*String fileName = "";
		
		if ("Final".equals(((ProductDetail)productDetails.toArray()[0]).getAgreementStatus())) 
		{
                fileName = MessageFormat.format("wlsconnect{0}{1}{2}.pdf", this.getQuoteId(), ((ProductDetail)productDetails.toArray()[0]).getAgreementStatus(), this.getContractNumber());
		}
		else 
		{
                fileName = MessageFormat.format("wlsconnect{0}{1}.pdf", this.getQuoteId(), ((ProductDetail)productDetails.toArray()[0]).getAgreementStatus());
		}
		this.outputFileName = fileName;*/
		
		return outputFileName;
	}	


	public void setOutputFileName(String outputFileName) 
	{
		//Incoming File name will be ignored and file name will be constructed per existing standards
		String fileName = outputFileName;
		/*if ("Final".equals(((ProductDetail)productDetails.toArray()[0]).getAgreementStatus())) 
		{
            fileName = MessageFormat.format("wlsconnect{0}{1}{2}.pdf", this.getQuoteId(), ((ProductDetail)productDetails.toArray()[0]).getAgreementStatus(), this.getContractNumber());
		}
		else
		{
            fileName = MessageFormat.format("wlsconnect{0}{1}.pdf", this.getQuoteId(), (productDetail.getFormNumber());
		}*/
	
	
		if((this.getContractNumber())!= null && !(this.getContractNumber().isEmpty()) && this.getContractNumber() != ""){
			if (account.getAddress() != null && account.getAddress().getCountryCode() != null
					&& account.getAddress().getCountryCode().equals("MEX")) {
				fileName = MessageFormat.format("{0}-{1}-{2}-C-{3}-{4}-1.pdf", this.getContractNumber(),((ProductDetail) productDetails.toArray()[0]).getFormNumber(), account.getAccountNumber(),((ProductDetail) productDetails.toArray()[0]).getProductDetailType(),this.formsMessageId);
			}else {
				fileName = MessageFormat.format("{0}-{1}-{2}-C-{3}-1.pdf", this.getContractNumber(), ((ProductDetail)productDetails.toArray()[0]).getFormNumber(), account.getAccountNumber(),((ProductDetail)productDetails.toArray()[0]).getProductDetailType() );
			}		
			this.outputFileName = fileName;
		}
		else
		{
			fileName = MessageFormat.format("Quote-{0}-{1}-C-{2}-1-{3}.pdf", ((ProductDetail)productDetails.toArray()[0]).getFormNumber(), account.getAccountNumber(),((ProductDetail)productDetails.toArray()[0]).getProductDetailType(), this.formsMessageId);
			this.outputFileName = fileName;
		}
	}

	public String getFormPDFLocation() 
	{
		return formPDFLocation;
	}

	public void setFormPDFLocation(String formPDFLocation) 
	{
		this.formPDFLocation = formPDFLocation;
	}

	/**
	 * Creates the request.<br>
	 * This method may be required by the services framework.
	 */
	public FormRequest() 
	{}

	public Account getAccount() 
	{
		return this.account;
	}

	public Vehicle getVehicle() 
	{
		return this.vehicle;
	}

	public String getContractNumber() 
	{
		return this.contractNumber;
	}

	public Set<ProductDetail> getProductDetails() 
	{
		return this.productDetails;
	}
	
	public ProductDetail getProductDetail() 
	{
		Iterator iter = productDetails.iterator();
		ProductDetail productDetail = (ProductDetail) iter.next();
		return productDetail;
	}
	
	public CallingApp getCallingApp() 
	{
		return this.callingApp;
	}

	public Contact getBuyer() 
	{
		return this.buyer;
	}

	public Contact getCoBuyer() 
	{
		return this.coBuyer;
	}

	public Contact getLienHolder() 
	{
		return this.lienHolder;
	}
	

	/**
	 * Returns the contact in the set that has the given type.
	 * 
	 * @param type
	 *            the contact type.
	 * @return the first contact found or null if there wasn't any.
	 * @throws IllegalArgumentException
	 *             if the provided type is null.
	 */

	public Insurance getInsurance() {
		return insurance;
	}

	public void setInsurance(Insurance insurance) {
		this.insurance = insurance;
	}

	/**
	 * Validates the request.<br>
	 * The request is invalid if any of the following occur:
	 * <ul>
	 * <li>the quoteId is null, blank or contains whitespaces only</li>
	 * <li>the callingApp is null or invalid</li>
	 * <li>the dealer is null or invalid</li>
	 * <li>the contractNumber is null, blank or contains whitespaces only</li>
	 * <li>the productDetails are null, empty or contains an invalid product
	 * detail</li>
	 * <li>there is an isCombo WAR or PPM product without its counterpart
	 * isCombo PPM or WAR respectively</li>
	 * <li>the form is not blank and the vehicle is null or invalid</li>
	 * <li>the form is not blank and the contacts are null, empty or contain an
	 * invalid contact</li>
	 * <li>the form is not blank and there is not a buyer contact</li>
	 * <li>the form is not blank and there is not a co-buyer contact</li>
	 * <li>the form is not blank and there is not a lienholder contact</li>
	 * <li>the form is not blank and the finance details are null or invalid
	 * </li>
	 * </ul>
	 * 
	 * @param contentType
	 * @return errors
	 */
    public Map<String, String> validate(String contentType) 
	{
		Map<String, String> errors = new HashMap<String, String>();
		errors.putAll(getAccount().validate(contentType));
		errors.putAll(getVehicle().validate(contentType));
		if (null != getProductDetail().getProductDetailType() && !getProductDetail().getProductDetailType().equals("GAP")){
				if(getFinanceDetails().getFinanceType() != null && !getFinanceDetails().getFinanceType().equals(""))   {
			errors.putAll(getFinanceDetails().validate(contentType));
			}}
		if( null != getProductDetail().getProductDetailType() && (getProductDetail().getProductDetailType().equals("GAP")) && (getFinanceDetails().getFinanceType() == null || getFinanceDetails().getFinanceType().equals("")) ) {
			errors.put("financeDetails financeType", "The finance details financeType is mandatory when productDetailType is GAP");
		}
		if(null != getProductDetail().getProductDetailType() && getProductDetail().getProductDetailType().equals("GAP")) 
			if (getFinanceDetails().getFinanceType() != null && !getFinanceDetails().getFinanceType().equals("") ) {
			errors.putAll(getFinanceDetails().validate(contentType));
			} 
		errors.putAll(getCallingApp().validate(contentType));
		
		if(null != getProductDetail() && null != getProductDetail().getProductDetailType() && !(getProductDetail().getProductDetailType().isEmpty())
				&& getProductDetail().getProductDetailType().equals("GAP") && contentType.equals("XML")){
			String coverage[] = null;
			String coverageAmount[] = null;
			String amountFinanced[] = null;
			String paymentAmountError = "";
			String financedAmountError = "";
			Double maxCoverage = null;
			Double minCoverage = null;
			Double maxFinanced = null;
			Double minFinanced = null;
			
			try {
				 
					if(null != getProductDetail().getCoverageCode() && !getProductDetail().getCoverageCode().isEmpty()) {
						
							if(getProductDetail().getCoverageCode().contains("AF")) {
								
								coverageAmount= getProductDetail().getCoverageCode().split("AF");
								coverage= coverageAmount[0].split("-");				
								
						amountFinanced = coverageAmount[1].split("-");
						minFinanced = Double.parseDouble(amountFinanced[0].replaceAll("[^0-9]", ""));
						maxFinanced = Double.parseDouble(amountFinanced[1].replaceAll("[^0-9]", "")) * 1000;
						if (coverage.length > 1) {
							minCoverage = Double.parseDouble(coverage[0].replaceAll("[^0-9]", ""));
							maxCoverage = Double.parseDouble(coverage[1].replaceAll("[^0-9]", "")) * 1000;
						}
							}else {
								coverage= getProductDetail().getCoverageCode().split("-");
									

							minCoverage= Double.parseDouble(coverage[0].replaceAll("[^0-9]",""));
							maxCoverage= Double.parseDouble(coverage[1].replaceAll("[^0-9]",""))*1000;
						 }
				} else {
								if(null != getProductDetail().getCoverageName() && getProductDetail().getCoverageName().contains("AF")) {
									coverageAmount= getProductDetail().getCoverageName().split("AF");
									coverage= coverageAmount[0].split("-");
						amountFinanced = coverageAmount[1].split("-");
						minFinanced = Double.parseDouble(amountFinanced[0].replaceAll("[^0-9]", ""));
						maxFinanced = Double.parseDouble(amountFinanced[1].replaceAll("[^0-9]", "")) * 1000;
						if (coverage.length > 1) {
							maxCoverage = Double.parseDouble(coverage[1].replaceAll("[^0-9]", "")) * 1000;
							minCoverage = Double.parseDouble(coverage[0].replaceAll("[^0-9]", ""));
								}
					} else {
									coverage = getProductDetail().getCoverageName().split("-");
									

								maxCoverage= Double.parseDouble(coverage[1].replaceAll("[^0-9]",""))*1000;
								minCoverage= Double.parseDouble(coverage[0].replaceAll("[^0-9]",""));
						}
					
				}
				Double paymentAmount = getFinanceDetails().getPaymentAmount();
                DecimalFormat df = new DecimalFormat("#.##");
				if (minCoverage != null && maxCoverage != null && paymentAmount != null) {
				     if(minCoverage > paymentAmount || maxCoverage < paymentAmount) {
						paymentAmountError = "Total of Payments " + df.format(paymentAmount)
								+ " is not within the range of the coverage that was selected : " + getProductDetail().getCoverageName();
			}
			}
				else if (minCoverage != null && maxCoverage != null && paymentAmount == null) {
					paymentAmountError = "Total of Payments is not within the range of the coverage that was selected : " + getProductDetail().getCoverageName();
				}
				Double financedAmount = getFinanceDetails().getFinancedAmount();
				if (minFinanced != null && maxFinanced != null && financedAmount != null) {
					if (minFinanced > financedAmount || maxFinanced < financedAmount) {
						financedAmountError = "Financed Amount " + df.format(financedAmount)
								+ " is not within the range of the coverage that was selected : " + getProductDetail().getCoverageName();
					}
				}
				else if (minFinanced != null && maxFinanced != null && financedAmount == null) {
					financedAmountError = "Financed Amount is not within the range of the coverage that was selected : " + getProductDetail().getCoverageName();
				}
				if (paymentAmountError != "" && financedAmountError != "") {
					errors.put("Product Information", (paymentAmountError + " and/or " + financedAmountError));
				} else if (paymentAmountError != "" || financedAmountError != "") {
					errors.put("Product Information", (paymentAmountError + financedAmountError));
				}
			} catch (Exception e) {
				errors.put("error in product Information",
						" Product Coverage Code, Expected Range for Total of Payment and/or Amount Financed is missing/invalid format in Coverage Name.");
				
			}			
		}

		if(getBuyer() !=null)
			errors.putAll(getBuyer().validate(contentType));
		else 
			errors.put("Buyer Information", "Buyer contact details cannot be empty.");
		if(getCoBuyer() != null )
		{
			boolean validate=false;
			if(getCoBuyer().getFullName() != null && !getCoBuyer().getFullName().equals("") && !getCoBuyer().getFullName().equals(null))
				validate=true;
			if(getCoBuyer().getEmail() != null && !getCoBuyer().getEmail().equals("") && !getCoBuyer().getEmail().equals(null))
				validate=true;
			if(getCoBuyer().getAddress().getAddress1() != null && !getCoBuyer().getAddress().getAddress1().equals("") && !getCoBuyer().getAddress().getAddress1().equals(null))
				validate=true;
			if(getCoBuyer().getAddress().getAddress2() != null && !getCoBuyer().getAddress().getAddress2().equals("") && !getCoBuyer().getAddress().getAddress2().equals(null))
				validate=true;
			if(getCoBuyer().getAddress().getCity() != null && !getCoBuyer().getAddress().getCity().equals("") && !getCoBuyer().getAddress().getCity().equals(null))
				validate=true;
			if(getCoBuyer().getAddress().getPostalCode() != null && !getCoBuyer().getAddress().getPostalCode().equals("") && !getCoBuyer().getAddress().getPostalCode().equals(null))
				validate=true;
			if(getCoBuyer().getAddress().getStateCode() != null && !getCoBuyer().getAddress().getStateCode().equals("") && !getCoBuyer().getAddress().getStateCode().equals(null))
				validate=true;
			if (validate == true)
				errors.putAll(getCoBuyer().validate(contentType));
		}
		
		if(getLienHolder() !=null)
		{
			boolean validate=false;
			if(getLienHolder().getFullName() != null && !getLienHolder().getFullName().equals("") && !getLienHolder().getFullName().equals(null))
				validate=true;
			if(getLienHolder().getEmail() != null && !getLienHolder().getEmail().equals("") && !getLienHolder().getEmail().equals(null))
				validate=true;
			if(getLienHolder().getAddress().getAddress1() != null && !getLienHolder().getAddress().getAddress1().equals("") && !getLienHolder().getAddress().getAddress1().equals(null))
				validate=true;
			if(getLienHolder().getAddress().getAddress2() != null && !getLienHolder().getAddress().getAddress2().equals("") && !getLienHolder().getAddress().getAddress2().equals(null))
				validate=true;
			if(getLienHolder().getAddress().getCity() != null && !getLienHolder().getAddress().getCity().equals("") && !getLienHolder().getAddress().getCity().equals(null))
				validate=true;
			if(getLienHolder().getAddress().getPostalCode() != null && !getLienHolder().getAddress().getPostalCode().equals("") && !getLienHolder().getAddress().getPostalCode().equals(""))
				validate=true;
			if(getLienHolder().getAddress().getStateCode() != null && !getLienHolder().getAddress().getStateCode().equals("") && !getLienHolder().getAddress().getStateCode().equals(""))
				validate=true;
			if (getAccount().getAddress() != null && getAccount().getAddress().getCountryCode() != null
					&& (getAccount().getAddress().getCountryCode().equals("USA") || getAccount().getAddress().getCountryCode().equals("US"))) 
			{
				getLienHolder().getAddress().setCountryCode("USA");
			}
			if (validate == true)
			errors.putAll(getLienHolder().validate(contentType));
		}
		Set<ProductDetail> productDetails = getProductDetails();
		for (Iterator iterator = productDetails.iterator(); iterator.hasNext();) 
		{
			ProductDetail productDetail = (ProductDetail) iterator.next();
			errors.putAll(productDetail.validate(contentType));
		}
		return errors;
	}


	@Override
	public String toString() {
		return "FormRequest [account=" + account + ", vehicle=" + vehicle + ", quoteId=" + quoteId + ", formsMessageId="
				+ formsMessageId + ", contractNumber=" + contractNumber + ", callingApp=" + callingApp
				+ ", productDetails=" + productDetails + ", productDetail=" + productDetail + ", fsmLastName="
				+ fsmLastName + ", financeDetails=" + financeDetails + ", buyer=" + buyer + ", coBuyer=" + coBuyer
				+ ", lienHolder=" + lienHolder + ", formPDFLocation=" + formPDFLocation + ", outputFileName="
				+ outputFileName + ", fillers=" + fillers + ", fsmFirstName=" + fsmFirstName + ", brokerDetails=" + brokerDetails + "]";
	}

	/**
	 * Return from Id from Fillers
	 * 
	 * @return Form ID used to generate the PDF
	 */

	public double getTotalPrice() 
	{
		double totalPrice=0;
		for (Iterator<ProductDetail> iterator = getProductDetails().iterator(); iterator.hasNext();) 
		{
			ProductDetail product = iterator.next();
			totalPrice += product.getPrice();
		}
		return totalPrice;
	}

	public void setAccount(Account dealer) 
	{
		// TODO Auto-generated method stub
		this.account = dealer;
	}

	public void setVehicle(Vehicle vehicle) 
	{
		// TODO Auto-generated method stub
		this.vehicle = vehicle;
	}

	public void setContractNumber(String contractNumber) 
	{
		// TODO Auto-generated method stub
		this.contractNumber = contractNumber;
	}

	public void setBuyer(Contact buyer) 
	{
		// TODO Auto-generated method stub
		this.buyer = buyer;
	}

	public void setCoBuyer(Contact coBuyer) 
	{
		// TODO Auto-generated method stub
		this.coBuyer = coBuyer;
	}

	public void setLienHolder(Contact lienHolder) 
	{
		// TODO Auto-generated method stub
		this.lienHolder = lienHolder;
	}

	public void setCallingApp(CallingApp callingApp) 
	{
		// TODO Auto-generated method stub
		this.callingApp = callingApp;
	}

	public void setProductDetails(Set<ProductDetail> productDetails) 
	{
		// TODO Auto-generated method stub
		this.productDetails = productDetails;
	}
	
	
	
	public static boolean getIsRequestTypeJSON() {
		return isRequestTypeJSON;
	}

	public void setIsRequestTypeJSON(Boolean isRequestTypeJSON) {
		this.isRequestTypeJSON = isRequestTypeJSON;
	}
	
	public String getFormsMessageId() {
		return formsMessageId;
	}

	public void setFormsMessageId(String formsMessageId) {
		this.formsMessageId = formsMessageId;
	}

	public String getFormId(String textToCompare) 
	{
		if (getFillers() != null){
			for (Iterator<KeyValuePair> iter = getFillers().iterator();iter.hasNext();) 
			{
				KeyValuePair value = iter.next();
				// if text to compare is NULL we take the first value form fillers
				if ((textToCompare == null) || (textToCompare.equals(value.getKey())))
				{				 
					return MessageFormat.format("{0}.pdf",value.getValue());
				}
			}		
		}
		
		return null;
	}

	public String getFsmName()
	{
		return (fsmFirstName!=null?fsmFirstName+" ":"").concat(fsmLastName!=null?fsmLastName:"");
	}
	
	public ProductDetail getProductDetailsByType(ProductType type) 
	{
		Validate.notNull(type);
		for (ProductDetail productDetail : this.productDetails) 
		{
			if (type.equals(productDetail.getProductDetailType())) 
			{
				return productDetail;
			}
		}
		return null;
	}

	public String getExpirationType() {
		return expirationType;
	}

	public void setExpirationType(String expirationType) {
		this.expirationType = expirationType;
	}
}
