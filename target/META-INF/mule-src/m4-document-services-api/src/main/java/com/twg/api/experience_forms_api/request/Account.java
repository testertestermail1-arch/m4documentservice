package com.twg.api.experience_forms_api.request;


import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.twg.api.experience_forms_api.DocumentError;

/**
 * Holds dealer information.
 * @author Juan Berrueta
 */

public class Account 
{
	private String accountNumber;
	private String name;
	private Address address;
	private Phone phone;
	private String classificationCode;
	private String salesman;
	private String rfc;
	private String invoiceNumber;
	private String paymentType;
	private String fieldRep;
	private String salesManager;

	
	public String getSalesManager() {
		return salesManager;
	}
	public void setSalesManager(String salesManager) {
		this.salesManager = salesManager;
	}
	
	public String getClassificationCode() 
	{
		return classificationCode;
	}
	public void setClassificationCode(String classificationCode) 
	{
		this.classificationCode = classificationCode;
	}

	/**
	 * Creates the dealer.<br>
	 * This method may be required by the services framework.
	 */
	public Account() 
	{}
	
	public String getAccountNumber() 
	{
		return this.accountNumber;
	}
	
	public String getName() 
	{
		return this.name;
	}
	
	public Address getAddress() 
	{
		return this.address;
	}
		
	public Phone getPhone() 
	{
		return this.phone;
	}
	
	
	/**
	 * Validates the dealer.<br>
	 * The dealer is invalid if any of the following occur:
	 * <ul>
	 * <li>the rdgNumber is null, blank or contains whitespaces only</li>
	 * <li>the accountNumber is null, blank or contains whitespaces only</li>
	 * <li>the name is null, blank or contains whitespaces only</li>
	 * <li>the address is null or invalid</li>
	 * <li>the fieldRep is blank or contains whitespaces only</li>
	 * <li>the phone is invalid</li>
	 * <li>the insuranceNumber is blank or contains whitespaces only</li>
	 * <li>the companyCode is blank or contains whitespaces only</li>
	 * </ul>
	 * @param contentType the errors list to fulfill.
	 * @return Map javadoc comments
	 */
	public Map validate(String contentType) 
	{
		Map<String,String> Accounterrors = new HashMap<String,String>();
		if (StringUtils.isBlank(this.accountNumber)) 
		{
			Accounterrors.put(StringUtils.equals(contentType, "JSON")?"accountNumber":"internalId", "The dealer's internal id cannot be null, blank nor contain whitespaces only.");
		}
		if (StringUtils.isBlank(this.name)) 
		{
			Accounterrors.put("Dealer's name", "The dealer's name cannot be null, blank nor contain whitespaces only.");
		}
		if (this.address == null) 
		{
			Accounterrors.put("Dealer's address", "The dealer's address cannot be null.");
		} 
		else 
		{
			Accounterrors.putAll(this.address.validate(contentType));
		}
		
		if (this.address.getCountryCode() != null
				&& (this.address.getCountryCode().equals("USA") || this.address.getCountryCode().equals("US"))
				&& (this.address.getStateCode() != null && !this.address.getStateCode().equals("PR"))) 
		{
			if (StringUtils.equals(contentType, "XML") && this.phone != null) 
			{
				if ( this.phone.getAreaCode() == null && !StringUtils.isBlank(this.phone.getNumber()))
					{
					if ( !(this.phone.getNumber().matches("\\d{10}")
							|| this.phone.getNumber().matches("\\d{3}[-]\\d{3}[-\\s]\\d{4}")
							|| this.phone.getNumber().matches("\\d{3}[\\s-]\\d{3}[-]\\d{4}")
							|| this.phone.getNumber().matches("\\(\\d{3}\\)[\\s]?\\d{3}-\\d{4}")
							|| this.phone.getNumber().matches("\\d{6}[-]\\d{4}")
							|| this.phone.getNumber().matches("\\d{7}")
							|| this.phone.getNumber().matches("\\d{3}[-\\s]\\d{4}")))
						{
							Accounterrors.put("Dealer's phone", "The dealer's phone is invalid.");
						}
					}
				else if (this.phone.getAreaCode() != null && StringUtils.isBlank(this.phone.getNumber())) 
				{
					Accounterrors.put("Dealer's phone", "The dealer's phone is invalid.");
				}
				else if ((this.phone.getAreaCode() != null && !StringUtils.isBlank(this.phone.getNumber()))
						&& (!this.phone.getAreaCode().toString().matches("\\d{3}")
								|| !(this.phone.getNumber().matches("\\d{7}")
										|| this.phone.getNumber().matches("\\d{3}[-\\s]\\d{4}")))) 
				{
					Accounterrors.put("Dealer's phone", "The dealer's phone is invalid.");
				}
			}
			if (StringUtils.equals(contentType, "JSON") && this.phone != null) 
			{
				if (!StringUtils.isBlank(this.phone.getNumber()) && !(this.phone.getNumber().matches("\\d{10}")
						|| this.phone.getNumber().matches("\\d{3}[-]\\d{3}[-\\s]\\d{4}")
						|| this.phone.getNumber().matches("\\d{3}[\\s-]\\d{3}[-]\\d{4}")
						|| this.phone.getNumber().matches("\\(\\d{3}\\)[\\s]?\\d{3}-\\d{4}")
						|| this.phone.getNumber().matches("\\d{6}[-]\\d{4}")
						|| this.phone.getNumber().matches("\\d{7}")
						|| this.phone.getNumber().matches("\\d{3}[-\\s]\\d{4}")))
				{
					Accounterrors.put("Dealer's phone", "The dealer's phone is invalid.");
				}
			}
			if (!StringUtils.isBlank(this.address.getPostalCode()) && this.address.getPostalCode().matches("\\d{9}"))
			{
				String formattedZip = MessageFormat.format("{0}-{1}", this.address.getPostalCode().substring(0, 5),this.address.getPostalCode().substring(5));
				this.address.setPostalCode(formattedZip);

			}
			if (!StringUtils.isBlank(this.address.getPostalCode())
					&& !(this.address.getPostalCode().matches("\\d{5}[-\\s]\\d{4}")							
							|| this.address.getPostalCode().matches("\\d{5}"))) 
			{
				Accounterrors.put("Dealer's zip/postal code", "The dealer's zip/postal Code is invalid.");
			}
		}
		
		return Accounterrors;
	}
	
	@Override
	public String toString() 
	{
		return new ToStringBuilder(this).append("accountNumber", this.accountNumber).append("name", this.name)
				.append("address", this.address).append("phone", this.phone)
				.append("salesman", this.salesman).append("rfc", this.rfc).append("invoiceNumber", this.invoiceNumber)
				.append("payment", this.paymentType).append("fieldRep", this.fieldRep)
				.append("salesManager", this.salesManager).toString();
	}

	public void setAccountNumber(String accountNumber) 
	{
		// TODO Auto-generated method stub
		this.accountNumber=accountNumber;
	}

	public void setName(String name) 
	{
		// TODO Auto-generated method stub
		this.name=name;
	}

	public void setAddress(Address address) 
	{
		// TODO Auto-generated method stub
		this.address=address;
	}

	public void setPhone(Phone phone) 
	{
		// TODO Auto-generated method stub
		this.phone=phone;
	}
	
	public String getSalesman() {
		return salesman;
	}
	
	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}
	
	public String getRfc() {
		return rfc;
	}
	
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	
	public String getPaymentType() {
		return paymentType;
	}
	
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
	public String getFieldRep() {
		return fieldRep;
	}

	public void setFieldRep(String fieldRep) {
		this.fieldRep = fieldRep;
	}
	
}
