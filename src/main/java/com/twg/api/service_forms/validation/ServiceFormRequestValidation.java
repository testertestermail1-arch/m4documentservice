package com.twg.api.service_forms.validation;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;

import org.json.JSONArray;
import org.json.JSONObject;


import com.twg.api.service_forms.constants.SendSignature;
import com.twg.api.service_forms.constants.StampSignature;
import com.twg.api.service_forms.constants.ServiceFormField;
import com.twg.api.service_forms.constants.UrlOnly;
import com.twg.api.service_forms.exceptions.ValidationException;
import com.twg.api.service_forms.constants.FileType;
import com.twg.api.service_forms.constants.FinanceType;



public class ServiceFormRequestValidation {
	
	
	private static final String EMAIL_PATTERN = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";
	
	private static final String PHONE_PATTERN = "^(?!0000000000)\\d{10}$";

	private static final String requiredMessage = "Required Value is Missing for ";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
	private static final Pattern phonePattern = Pattern.compile(PHONE_PATTERN);
	//private static final Logger logger = Logger.getLogger(ServiceFormRequestValidation.class);
	
	
	public LinkedHashMap validate(LinkedHashMap message) throws Exception {
		// TODO Auto-generated method stub
		LinkedHashMap responseMap = new LinkedHashMap();
		//MuleMessage message = eventContext.getMessage();
		List<String> allErrors = new ArrayList<String>();

		ServiceFormRequest ServiceFormRequest = getServiceFormRequest(message);

		List<String> missingFieldErrors = validateMissingFields(ServiceFormRequest);
		//List<String> formatErrors = validateFieldFormat(ServiceFormRequest);
		//List<String> bizRulesErrors = validateBusinessRules(ServiceFormRequest);

		if (ServiceFormRequest.getErrorMessages().size() > 0) {
			allErrors.addAll(ServiceFormRequest.getErrorMessages());
		}

		if (missingFieldErrors.size() > 0) {
			allErrors.addAll(validateMissingFields(ServiceFormRequest));
		}

		/*if (formatErrors.size() > 0) {
			allErrors.addAll(formatErrors);
		}

		if (bizRulesErrors.size() > 0) {
			allErrors.addAll(bizRulesErrors);
		}*/

		responseMap.put("allErrors",allErrors);
		return responseMap;
	}
	
	
	
	
	/**
	 * perform required values validation
	 * 
	 * @param contractRequest
	 * @param requiredMessage
	 * @return
	 */
	private List<String> validateMissingFields(ServiceFormRequest ServiceFormRequest)
	{
		List<String> errors = new ArrayList<String>();
		

		//mandatory fileds
		if (ServiceFormRequest.getSourceOriginator() == null
				|| ServiceFormRequest.getSourceOriginator().equals("")) {
			errors.add(requiredMessage + "[sourceOriginator]");
		} else if (!isValidString(ServiceFormRequest.getSourceOriginator(),
				ServiceFormField.sourceOriginator.getLength())) {
			errors.add(ServiceFormField.sourceOriginator.getFormat());
		}
		
		
		
		if(ServiceFormRequest.getUrlOnly() ==null ||  ServiceFormRequest.getUrlOnly().equals("")){
			  errors.add(requiredMessage+ "[urlOnly]");
		}else if(ServiceFormRequest.getUrlOnly()!=null && !UrlOnly.contains(ServiceFormRequest.getUrlOnly()) && !ServiceFormRequest.getUrlOnly().equals("")){
			  errors.add(ServiceFormField.urlOnly.getFormat());
	     }
		
		
		
		
		
		if (((ServiceFormRequest.getContractNumber() == null || ServiceFormRequest.getContractNumber().equals("")) && (ServiceFormRequest.getAgreementStatus() != null && ServiceFormRequest.getAgreementStatus().equals("P")) )) {
			errors.add(requiredMessage + "[contractNumber]");
		} else if (!isValidString(ServiceFormRequest.getContractNumber(),
				ServiceFormField.contractNumber.getLength())) {
			errors.add(ServiceFormField.contractNumber.getFormat());
		}	
			
		if ((ServiceFormRequest.getAccountNumber() == null || ServiceFormRequest.getAccountNumber().equals(""))) {
				errors.add(requiredMessage + "[accountNumber]");
		} else if (!isValidString(ServiceFormRequest.getAccountNumber(),
					ServiceFormField.accountNumber.getLength())) {
			errors.add(ServiceFormField.accountNumber.getFormat());
		}		
				
		if ((ServiceFormRequest.getName() == null || ServiceFormRequest.getName().equals(""))) {
			  errors.add(requiredMessage + "[name]");
		} else if (!isValidString(ServiceFormRequest.getName(),
					ServiceFormField.name.getLength())) {
			errors.add(ServiceFormField.name.getFormat());
		}	
			
			
			
		if(!ServiceFormRequest.getErroredFields().contains(ServiceFormField.odometer)) {
				if (ServiceFormRequest.getOdometer() == null) {
					errors.add(requiredMessage + "[odometer]");
		} else if (!isValidInteger(ServiceFormRequest.getOdometer(), ServiceFormField.odometer.getLength(),
				ServiceFormField.odometer.isAllowNegative())) {
				errors.add(ServiceFormField.odometer.getFormat());
		}
		}
		
		if (ServiceFormRequest.getVIN() == null || ServiceFormRequest.getVIN().equals("")) {
			errors.add(requiredMessage + "[VIN]");
		} else if (!isValidVIN(ServiceFormRequest.getVIN(), ServiceFormField.vin.getLength())) {
			errors.add(ServiceFormField.vin.getFormat());
		}
		
		
		if (ServiceFormRequest.getVehicleYear() == null || ServiceFormRequest.getVehicleYear().equals("")) {
			errors.add(requiredMessage + "[vehicleYear]");

		} else if (!isValidInteger(ServiceFormRequest.getVehicleYear(), ServiceFormField.vehicleYear.getLength(),
				ServiceFormField.vehicleYear.isAllowNegative())
				|| ServiceFormRequest.getVehicleYear().toString().length() < ServiceFormField.vehicleYear
						.getLength()) {
			errors.add(ServiceFormField.vehicleYear.getFormat());
		}
		
		
		if(!ServiceFormRequest.getErroredFields().contains(ServiceFormField.deductibleBase)) {
			if (ServiceFormRequest.getDeductibleBase() == null) {
				errors.add(requiredMessage + "[deductibleBase]");
	       } else if (!isValidInteger(ServiceFormRequest.getDeductibleBase(), ServiceFormField.deductibleBase.getLength(),
			ServiceFormField.deductibleBase.isAllowNegative())) {
			errors.add(ServiceFormField.deductibleBase.getFormat());
	       }
	     }
		
		
		
		
		if ((ServiceFormRequest.getCoverage() == null || ServiceFormRequest.getCoverage().equals(""))) {
			  errors.add(requiredMessage + "[coverage]");
		} else if (!isValidString(ServiceFormRequest.getCoverage(),
					ServiceFormField.coverage.getLength())) {
			errors.add(ServiceFormField.coverage.getFormat());
		}
			
			
		
		if(!ServiceFormRequest.getErroredFields().contains(ServiceFormField.termMonths)) {
			if (ServiceFormRequest.getTermMonths() == null) {
				errors.add(requiredMessage + "[termMonths]");
	         } else if (!isValidInteger(ServiceFormRequest.getTermMonths(), ServiceFormField.termMonths.getLength(),
			    ServiceFormField.termMonths.isAllowNegative())) {
			    errors.add(ServiceFormField.termMonths.getFormat());
	           }
	    }
			
		
		
		if ((ServiceFormRequest.getCustomerFirstName() == null || ServiceFormRequest.getCustomerFirstName().equals(""))) {
			  errors.add(requiredMessage + "[customerFirstName]");
		} else if (!isValidString(ServiceFormRequest.getCustomerFirstName(),
					ServiceFormField.customerFirstName.getLength())) {
			errors.add(ServiceFormField.customerFirstName.getFormat());
		}
			
			
		if ((ServiceFormRequest.getCustomerLastName() == null || ServiceFormRequest.getCustomerLastName().equals(""))) {
				  errors.add(requiredMessage + "[customerLastName]");
		} else if (!isValidString(ServiceFormRequest.getCustomerLastName(),
						ServiceFormField.customerLastName.getLength())) {
				errors.add(ServiceFormField.customerLastName.getFormat());
		}
		
				
		if ((ServiceFormRequest.getCustomerAddress1() == null || ServiceFormRequest.getCustomerAddress1().equals(""))) {
				errors.add(requiredMessage + "[customerAddress1]");
		} else if (!isValidString(ServiceFormRequest.getCustomerAddress1(),
					 ServiceFormField.customerAddress1.getLength())) {
				errors.add(ServiceFormField.customerAddress1.getFormat());
		}
					
		
				
		if ((ServiceFormRequest.getCustomerCity() == null || ServiceFormRequest.getCustomerCity().equals(""))) {
				errors.add(requiredMessage + "[customerCity]");
		} else if (!isValidString(ServiceFormRequest.getCustomerCity(),
				   ServiceFormField.customerCity.getLength())) {
			errors.add(ServiceFormField.customerCity.getFormat());
		}
			
			
		if ((ServiceFormRequest.getCustomerStateCode() == null || ServiceFormRequest.getCustomerStateCode().equals(""))) {
				errors.add(requiredMessage + "[customerStateCode]");
		} else if (!isValidString(ServiceFormRequest.getCustomerStateCode(),
				   ServiceFormField.customerStateCode.getLength())) {
			errors.add(ServiceFormField.customerStateCode.getFormat());
		}
			
		
	   
	    if ((ServiceFormRequest.getCustomerPostalCode() == null || ServiceFormRequest.getCustomerPostalCode().equals(""))) {
			errors.add(requiredMessage + "[customerPostalCode]");
	     } else if ( (ServiceFormRequest.getCustomerPostalCode() != null || !ServiceFormRequest.getCustomerPostalCode().equals("")) && (ServiceFormRequest.getCustomerPostalCode().length() < 5 || ServiceFormRequest.getCustomerPostalCode().length() > 10)) {
	 		//errors.add(ServiceFormField.customerPostalCode.getFormat());
	 		
	 		errors.add("Invalid Value Received for [customerPostalCode].Please enter a positive integer with Precision:[5-10]");
	     }
			
			
	   
			
	    if (ServiceFormRequest.getCustomerCountryCode() != null && !ServiceFormRequest.getCustomerCountryCode().equals("")) {
			if (!isValidString(ServiceFormRequest.getCustomerCountryCode(), ServiceFormField.customerCountryCode.getLength())) {
				errors.add(ServiceFormField.customerCountryCode.getFormat());
			}

		}
	 
	    
	    
	   
	  
	    
	    if (ServiceFormRequest.getMsrp() != null && !ServiceFormRequest.getMsrp().equals("")) {
	    	if (!isValidDecimal(ServiceFormRequest.getMsrp(), ServiceFormField.msrp.getLength(),
					ServiceFormField.msrp.getScale(), ServiceFormField.msrp.isAllowNegative())) {
				errors.add(ServiceFormField.msrp.getFormat());
			}
		    

		}
	    
	    
	    //mandatory  
	    
	    
	 
	    
	    
	   
	    
	    
	  /* if ((ServiceFormRequest.getFinanceType() == null || ServiceFormRequest.getFinanceType().equals(""))
				) {
			errors.add(requiredMessage + "[financeType]");
		} else if (!isValidString(ServiceFormRequest.getFinanceType(),
				   ServiceFormField.financeType.getLength())) {
			errors.add(ServiceFormField.financeType.getFormat());
		}*/
	   
	    if(ServiceFormRequest.getProductDetailType() != null && ServiceFormRequest.getProductDetailType() != "" && ServiceFormRequest.getProductDetailType().equals("GAP") && (ServiceFormRequest.getFinanceType() == null || ServiceFormRequest.getFinanceType().equals("")) ) {
			errors.add(requiredMessage + "[financeType]");
		}
		else if(ServiceFormRequest.getFinanceType() != null && !ServiceFormRequest.getFinanceType().equals("") && !FinanceType.contains(ServiceFormRequest.getFinanceType())){
			errors.add(ServiceFormField.financeType.getFormat());
		}
	   
	    
	    if ((ServiceFormRequest.getAddress1() == null || ServiceFormRequest.getAddress1().equals(""))) {
			errors.add(requiredMessage + "[address1]");
	    } else if (!isValidString(ServiceFormRequest.getAddress1(),
				 ServiceFormField.address1.getLength())) {
			errors.add(ServiceFormField.address1.getFormat());
	    }
	    
	    
	    if ((ServiceFormRequest.getCity() == null || ServiceFormRequest.getCity().equals(""))) {
			errors.add(requiredMessage + "[city]");
     	} else if (!isValidString(ServiceFormRequest.getCity(),
				 ServiceFormField.city.getLength())) {
			errors.add(ServiceFormField.city.getFormat());
	    }
	    
	    
	    if ((ServiceFormRequest.getStateCode() == null || ServiceFormRequest.getStateCode().equals(""))) {
			errors.add(requiredMessage + "[stateCode]");
	       } else if (!isValidString(ServiceFormRequest.getStateCode(),
				 ServiceFormField.stateCode.getLength())) {
			errors.add(ServiceFormField.stateCode.getFormat());
	    }
	    
	    
	    
	    if ((ServiceFormRequest.getPostalCode() == null || ServiceFormRequest.getPostalCode().equals(""))) {
			errors.add(requiredMessage + "[postalCode]");
	    } else if (!isValidString(ServiceFormRequest.getPostalCode(),
				 ServiceFormField.postalCode.getLength())) {
			errors.add(ServiceFormField.postalCode.getFormat());
	    }
	    
	    
	    if ((ServiceFormRequest.getVehicleType() == null || ServiceFormRequest.getVehicleType().equals(""))) {
			errors.add(requiredMessage + "[vehicleType]");
	    } else if (!isValidString(ServiceFormRequest.getVehicleType(),
				 ServiceFormField.vehicleType.getLength())) {
			errors.add(ServiceFormField.vehicleType.getFormat());
	    }
	    
	    
	    if ((ServiceFormRequest.getModel() == null || ServiceFormRequest.getModel().equals(""))) {
			errors.add(requiredMessage + "[model]");
	    } else if (!isValidString(ServiceFormRequest.getModel(),
				 ServiceFormField.model.getLength())) {
			errors.add(ServiceFormField.model.getFormat());
	    }
	    
	    if ((ServiceFormRequest.getMake() == null || ServiceFormRequest.getMake().equals(""))) {
			errors.add(requiredMessage + "[make]");
	    } else if (!isValidString(ServiceFormRequest.getMake(),
				 ServiceFormField.make.getLength())) {
			errors.add(ServiceFormField.make.getFormat());
	    }
	    
	    
	    if ((ServiceFormRequest.getInServiceDate() == null || ServiceFormRequest.getInServiceDate().equals(""))) {
			errors.add(requiredMessage + "[inServiceDate]");
	       } else if ((!isValidDate(ServiceFormRequest.getInServiceDate())||(ServiceFormRequest.getInServiceDate().length() > ServiceFormField.inServiceDate.getLength()))) {
				errors.add(ServiceFormField.inServiceDate.getFormat());
	    }
	    
	    if ((ServiceFormRequest.getVehicleClass() == null || ServiceFormRequest.getVehicleClass().equals(""))) {
			 errors.add(requiredMessage + "[vehicleClass]");
	       } else if (!isValidString(ServiceFormRequest.getVehicleClass(),
				 ServiceFormField.vehicleClass.getLength())) {
			errors.add(ServiceFormField.vehicleClass.getFormat());
	     }
	    
	    
	    if ((ServiceFormRequest.getProductClass() == null || ServiceFormRequest.getProductClass().equals(""))) {
			errors.add(requiredMessage + "[productClass]");
	       } else if (!isValidString(ServiceFormRequest.getProductClass(),
				 ServiceFormField.productClass.getLength())) {
			errors.add(ServiceFormField.productClass.getFormat());
	    }
	    
	    
	    if ((ServiceFormRequest.getAgreementStatus() == null || ServiceFormRequest.getAgreementStatus().equals(""))) {
			errors.add(requiredMessage + "[agreementStatus]");
	      } else if (!isValidString(ServiceFormRequest.getAgreementStatus(),
				 ServiceFormField.agreementStatus.getLength())) {
			errors.add(ServiceFormField.agreementStatus.getFormat());
	    }
	    
	    
	    if ((ServiceFormRequest.getFormNumber() == null || ServiceFormRequest.getFormNumber().equals(""))) {
			  errors.add(requiredMessage + "[formNumber]");
	        } else if (!isValidString(ServiceFormRequest.getFormNumber(),
				 ServiceFormField.formNumber.getLength())) {
			errors.add(ServiceFormField.formNumber.getFormat());
	    }
		
		
	    
	    //Optional fields validation
	    if (ServiceFormRequest.getCustomerHomePhoneNumber() != null && !ServiceFormRequest.getCustomerHomePhoneNumber().equals("")) {
	    	if ( (ServiceFormRequest.getCustomerHomePhoneNumber() != null || !ServiceFormRequest.getCustomerHomePhoneNumber().equals("")) && (ServiceFormRequest.getCustomerHomePhoneNumber().length() < 10 || ServiceFormRequest.getCustomerHomePhoneNumber().length() > 15)) {
				 //errors.add(ServiceFormField.customerHomePhoneNumber.getFormat());
				 errors.add("Invalid Value Received for [customerHomePhoneNumber].Please enter a positive integer with Precision:[10-15]");
			}

		}
	    
	    if(ServiceFormRequest.getProductDetailType() != null && !ServiceFormRequest.getProductDetailType().equals("") && ServiceFormRequest.getProductDetailType().equals("GAP"))
	    {
	    	if(ServiceFormRequest.getLienholderName()==null || ServiceFormRequest.getLienholderName().equals("")){
	    		errors.add(requiredMessage + "[lienholderName]");
	    	}
	    }	
	    
	    if (ServiceFormRequest.getLienholderName() != null && !ServiceFormRequest.getLienholderName().equals("")) {
	   	 if (!isValidString(ServiceFormRequest.getLienholderName(), ServiceFormField.lienholderName.getLength())) {
	   			errors.add(ServiceFormField.lienholderName.getFormat());
	   	 	}
	    }
	    
	    if (ServiceFormRequest.getLienholderAddress1() != null && !ServiceFormRequest.getLienholderAddress1().equals("")) {
		   	 if (!isValidString(ServiceFormRequest.getLienholderAddress1(), ServiceFormField.lienholderAddress1.getLength())) {
		   			errors.add(ServiceFormField.lienholderAddress1.getFormat());
		   	 	}
		}
	    
	    if (ServiceFormRequest.getLienholderCity() != null && !ServiceFormRequest.getLienholderCity().equals("")) {
		   	 if (!isValidString(ServiceFormRequest.getLienholderCity(), ServiceFormField.lienholderCity.getLength())) {
		   			errors.add(ServiceFormField.lienholderCity.getFormat());
		   	 	}
		}
	    
	    if (ServiceFormRequest.getLienholderState() != null && !ServiceFormRequest.getLienholderState().equals("")) {
		   	 if (!isValidString(ServiceFormRequest.getLienholderState(), ServiceFormField.lienholderState.getLength())) {
		   			errors.add(ServiceFormField.lienholderState.getFormat());
		   	 	}
		}
	    
	    if (ServiceFormRequest.getLienholderPhoneNumber() != null && !ServiceFormRequest.getLienholderPhoneNumber().equals("")) {
	 			if (!isValidString(ServiceFormRequest.getLienholderPhoneNumber(), ServiceFormField.lienholderPhoneNumber.getLength())) {
	 				errors.add(ServiceFormField.lienholderPhoneNumber.getFormat());
	 			}

	 	}
	    
	    if (ServiceFormRequest.getBaloonResidualValue() != null && !ServiceFormRequest.getBaloonResidualValue().equals("")) {
			if (!isValidDecimal(ServiceFormRequest.getBaloonResidualValue(), ServiceFormField.baloonResidualValue.getLength(),
					ServiceFormField.baloonResidualValue.getScale(), ServiceFormField.baloonResidualValue.isAllowNegative())) {
				errors.add(ServiceFormField.baloonResidualValue.getFormat());
			}

		}
	    
	    
	    if (ServiceFormRequest.getAnnualPercentageRate() != null && !ServiceFormRequest.getAnnualPercentageRate().equals("")) {
	    	if (!isValidInteger(ServiceFormRequest.getAnnualPercentageRate(), ServiceFormField.annualPercentageRate.getLength(),
				    ServiceFormField.annualPercentageRate.isAllowNegative())) {
				    errors.add(ServiceFormField.annualPercentageRate.getFormat());
		      }
		}
	    
	    
	    
	    
	    if(ServiceFormRequest.getFileType()!=null && !FileType.contains(ServiceFormRequest.getFileType()) && !ServiceFormRequest.getFileType().equals("")){
			  errors.add(ServiceFormField.fileType.getFormat());
	     }
		if(ServiceFormRequest.getSendSignature()!=null && (!SendSignature.contains(UpperCase(ServiceFormRequest.getSendSignature()))) && !ServiceFormRequest.getSendSignature().equals("")){
			  errors.add(ServiceFormField.sendSignature.getFormat());
	     }
		if(ServiceFormRequest.getStampSignature()!=null && (!StampSignature.contains(UpperCase(ServiceFormRequest.getStampSignature()))) && !ServiceFormRequest.getStampSignature().equals("")){
			  errors.add(ServiceFormField.stampSignature.getFormat());
	     }
		if (ServiceFormRequest.getQuoteId() != null && !ServiceFormRequest.getQuoteId().equals("")) {
			if (!isValidString(ServiceFormRequest.getQuoteId(), ServiceFormField.quoteId.getLength())) {
				errors.add(ServiceFormField.quoteId.getFormat());
			}

		}
		
		
		if (ServiceFormRequest.getAddress2() != null && !ServiceFormRequest.getAddress2().equals("")) {
			if (!isValidString(ServiceFormRequest.getAddress2(), ServiceFormField.address2.getLength())) {
				errors.add(ServiceFormField.address2.getFormat());
			}

		}
		
		
		if (ServiceFormRequest.getCountryCode() != null && !ServiceFormRequest.getCountryCode().equals("")) {
			if (!isValidString(ServiceFormRequest.getCountryCode(), ServiceFormField.countryCode.getLength())) {
				errors.add(ServiceFormField.countryCode.getFormat());
			}

		}
		if (ServiceFormRequest.getHomePhoneNumber() != null && !ServiceFormRequest.getHomePhoneNumber().equals("")) {
			if ((!isValidString(ServiceFormRequest.getHomePhoneNumber(), ServiceFormField.homePhoneNumber.getLength()))&&(!isPhoneNumberValid(ServiceFormRequest.getHomePhoneNumber()))) {
				errors.add(ServiceFormField.homePhoneNumber.getFormat());
			}

		}
		if (ServiceFormRequest.getMobilePhoneNumber() != null && !ServiceFormRequest.getMobilePhoneNumber().equals("")) {
			if ((!isValidString(ServiceFormRequest.getMobilePhoneNumber(), ServiceFormField.mobilePhoneNumber.getLength()))&&(!isPhoneNumberValid(ServiceFormRequest.getMobilePhoneNumber()))) {
				errors.add(ServiceFormField.mobilePhoneNumber.getFormat());
			}

		}
		if (ServiceFormRequest.getWorkPhoneNumber() != null && !ServiceFormRequest.getWorkPhoneNumber().equals("")) {
			if ((!isValidString(ServiceFormRequest.getWorkPhoneNumber(), ServiceFormField.workPhoneNumber.getLength()))&&(!isPhoneNumberValid(ServiceFormRequest.getWorkPhoneNumber()))) {
				errors.add(ServiceFormField.workPhoneNumber.getFormat());
			}

		}
		if (ServiceFormRequest.getFsmFirstName() != null && !ServiceFormRequest.getFsmFirstName().equals("")) {
			if (!isValidString(ServiceFormRequest.getFsmFirstName(), ServiceFormField.fsmFirstName.getLength())) {
				errors.add(ServiceFormField.fsmFirstName.getFormat());
			}

		}
		if (ServiceFormRequest.getFsmLastName() != null && !ServiceFormRequest.getFsmLastName().equals("")) {
			if (!isValidString(ServiceFormRequest.getFsmLastName(), ServiceFormField.fsmLastName.getLength())) {
				errors.add(ServiceFormField.fsmLastName.getFormat());
			}

		}
	
		
		
		if (ServiceFormRequest.getVehicleCode() != null && !ServiceFormRequest.getVehicleCode().equals("")) {
			if (!isValidString(ServiceFormRequest.getVehicleCode(), ServiceFormField.vehicleCode.getLength())) {
				errors.add(ServiceFormField.vehicleCode.getFormat());
			}

		}
		
		
		if (ServiceFormRequest.getTrim() != null && !ServiceFormRequest.getTrim().equals("")) {
			if (!isValidString(ServiceFormRequest.getTrim(), ServiceFormField.trim.getLength())) {
				errors.add(ServiceFormField.trim.getFormat());
			}

		}
		
		
		if (ServiceFormRequest.getVehiclePurchasePrice() != null && !ServiceFormRequest.getVehiclePurchasePrice().equals("")) {
			if (!isValidDecimal(ServiceFormRequest.getVehiclePurchasePrice(),
					ServiceFormField.vehiclePurchasePrice.getLength(),
					ServiceFormField.vehiclePurchasePrice.getScale(),
					ServiceFormField.vehiclePurchasePrice.isAllowNegative())) {
				errors.add(ServiceFormField.vehiclePurchasePrice.getFormat());
			}

		}
		if (ServiceFormRequest.getFuelType() != null && !ServiceFormRequest.getFuelType().equals("")) {
			if (!isValidString(ServiceFormRequest.getFuelType(), ServiceFormField.fuelType.getLength())) {
				errors.add(ServiceFormField.fuelType.getFormat());
			}

		}
		if (ServiceFormRequest.getDriveTrain() != null && !ServiceFormRequest.getDriveTrain().equals("")) {
			if (!isValidString(ServiceFormRequest.getDriveTrain(), ServiceFormField.driveTrain.getLength())) {
				errors.add(ServiceFormField.driveTrain.getFormat());
			}

		}
		if (ServiceFormRequest.getTurbo() != null && !ServiceFormRequest.getTurbo().equals("")) {
			if (!isValidString(ServiceFormRequest.getTurbo(), ServiceFormField.turbo.getLength())) {
				errors.add(ServiceFormField.turbo.getFormat());
			}

		}
		if (ServiceFormRequest.getSupercharged() != null && !ServiceFormRequest.getSupercharged().equals("")) {
			if (!isValidString(ServiceFormRequest.getSupercharged(), ServiceFormField.supercharged.getLength())) {
				errors.add(ServiceFormField.supercharged.getFormat());
			}

		}
		if (ServiceFormRequest.getHasNavigation() != null && !ServiceFormRequest.getHasNavigation().equals("")) {
			if (!isValidString(ServiceFormRequest.getHasNavigation(), ServiceFormField.hasNavigation.getLength())) {
				errors.add(ServiceFormField.hasNavigation.getFormat());
			}

		}
		if (ServiceFormRequest.getVehicleNewOrUsed() != null && !ServiceFormRequest.getVehicleNewOrUsed().equals("")) {
			if (!isValidString(ServiceFormRequest.getVehicleNewOrUsed(), ServiceFormField.vehicleNewOrUsed.getLength())) {
				errors.add(ServiceFormField.vehicleNewOrUsed.getFormat());
			}

		}
		if (ServiceFormRequest.getExpirationType() != null && !ServiceFormRequest.getExpirationType().equals("")) {
			if (!isValidString(ServiceFormRequest.getExpirationType(), ServiceFormField.expirationType.getLength())) {
				errors.add(ServiceFormField.expirationType.getFormat());
			}

		}
		if (ServiceFormRequest.getMaturityDate() != null && !ServiceFormRequest.getMaturityDate().equals("")) {
			if ((!isValidDate(ServiceFormRequest.getMaturityDate())||(ServiceFormRequest.getMaturityDate().length() > ServiceFormField.maturityDate.getLength()))) {
				errors.add(ServiceFormField.maturityDate.getFormat());
			}

		}
		
		if (ServiceFormRequest.getProductCode() != null && !ServiceFormRequest.getProductCode().equals("")) {
			if (!isValidString(ServiceFormRequest.getProductCode(), ServiceFormField.productCode.getLength())) {
				errors.add(ServiceFormField.productCode.getFormat());
			}

		}
		if (ServiceFormRequest.getProductDetailType() != null && !ServiceFormRequest.getProductDetailType().equals("")) {
			if (!isValidString(ServiceFormRequest.getProductDetailType(), ServiceFormField.productDetailType.getLength())) {
				errors.add(ServiceFormField.productDetailType.getFormat());
			}

		}
		
		if (ServiceFormRequest.getEffectiveDate() != null && !ServiceFormRequest.getEffectiveDate().equals("")) {
			if ((!isValidDate(ServiceFormRequest.getEffectiveDate())||(ServiceFormRequest.getEffectiveDate().length() > ServiceFormField.effectiveDate.getLength()))) {
				errors.add(ServiceFormField.effectiveDate.getFormat());
			}

		}
		
		if (ServiceFormRequest.getExpirationDate() != null && !ServiceFormRequest.getExpirationDate().equals("")) {
			if ((!isValidDate(ServiceFormRequest.getExpirationDate())||(ServiceFormRequest.getExpirationDate().length() > ServiceFormField.expirationDate.getLength()))) {
				errors.add(ServiceFormField.expirationDate.getFormat());
			}

		}
		
		if (ServiceFormRequest.getIsCombo() != null && !ServiceFormRequest.getIsCombo().equals("")) {
			if (!isValidString(ServiceFormRequest.getIsCombo(), ServiceFormField.isCombo.getLength())) {
				errors.add(ServiceFormField.isCombo.getFormat());
			}

		}
		if (ServiceFormRequest.getReportedCustomerCost() != null && !ServiceFormRequest.getReportedCustomerCost().equals("")) {
			if (!isValidDecimal(ServiceFormRequest.getReportedCustomerCost(),
					ServiceFormField.reportedCustomerCost.getLength(),
					ServiceFormField.reportedCustomerCost.getScale(),
					ServiceFormField.reportedCustomerCost.isAllowNegative())) {
				errors.add(ServiceFormField.reportedCustomerCost.getFormat());
			}

		}
		if (ServiceFormRequest.getReportedCustomerCost() != null && !ServiceFormRequest.getReportedCustomerCost().equals("")) {
			if (!isValidDecimal(ServiceFormRequest.getSalesTax(),
					ServiceFormField.salesTax.getLength(),
					ServiceFormField.salesTax.getScale(),
					ServiceFormField.salesTax.isAllowNegative())) {
				errors.add(ServiceFormField.salesTax.getFormat());
			}

		}
		
		if (ServiceFormRequest.getDeductibleReduced() != null && !ServiceFormRequest.getDeductibleReduced().equals("")) {
			if (!isValidInteger(ServiceFormRequest.getDeductibleReduced(), ServiceFormField.deductibleReduced.getLength(),
						ServiceFormField.deductibleReduced.isAllowNegative())) {
					errors.add(ServiceFormField.deductibleReduced.getFormat());
				}
			}
		
		if (ServiceFormRequest.getServiceInterval() != null && !ServiceFormRequest.getServiceInterval().equals("")) {
			if (!isValidInteger(ServiceFormRequest.getServiceInterval(), ServiceFormField.serviceInterval.getLength(),
						ServiceFormField.serviceInterval.isAllowNegative())) {
					errors.add(ServiceFormField.serviceInterval.getFormat());
				}
			}
		if (ServiceFormRequest.getNumberOfServices() != null && !ServiceFormRequest.getNumberOfServices().equals("")) {
			if (!isValidInteger(ServiceFormRequest.getNumberOfServices(), ServiceFormField.numberOfServices.getLength(),
						ServiceFormField.numberOfServices.isAllowNegative())) {
					errors.add(ServiceFormField.numberOfServices.getFormat());
				}
			}
		if (ServiceFormRequest.getTermDistance() != null && !ServiceFormRequest.getTermDistance().equals("")) {
			if (!isValidInteger(ServiceFormRequest.getTermDistance(), ServiceFormField.termDistance.getLength(),
						ServiceFormField.termDistance.isAllowNegative())) {
					errors.add(ServiceFormField.termDistance.getFormat());
				}
			}
		if ((ServiceFormRequest.getAutoRenew() != null && !ServiceFormRequest.getAutoRenew().equals(""))
			      &&  !(ServiceFormRequest.getAutoRenew().equals("Y") || ServiceFormRequest.getAutoRenew().equals("N"))) {
						 errors.add("Invalid Value Received for [autoRenew]. Allowed Length:1 Allowed Values:(Y,N)");
			}		
		if (ServiceFormRequest.getCoverageType() != null && !ServiceFormRequest.getCoverageType().isEmpty()) {
			int length = ServiceFormRequest.getCoverageType().size();
			for (int i = 0; i < length; i++) {
				String coverageTypeValue = ServiceFormRequest.getCoverageType().get(i);
				if (!isValidString(coverageTypeValue, ServiceFormField.coverageType.getLength())) {
					errors.add("Invalid Value Received for [coverageType[" + i + "]]. Allowed Length:255");
					// errors.add(ServiceFormField.coverageType.getFormat());
				}
			}

		}

		if (ServiceFormRequest.getCoverageAmount() != null && !ServiceFormRequest.getCoverageAmount().isEmpty()) {
			int length = ServiceFormRequest.getCoverageAmount().size();
			for (int i = 0; i < length; i++) {
				Double coverageAmountValue = ServiceFormRequest.getCoverageAmount().get(i);
				if (!isValidDecimal(coverageAmountValue, ServiceFormField.coverageAmount.getLength(),
						ServiceFormField.coverageAmount.getScale(),
						ServiceFormField.coverageAmount.isAllowNegative())) {
					errors.add("Invalid Value Received for [coverageAmount[" + i
							+ "]].Please enter a positive value with Precision:(9,2)");
					// errors.add(ServiceFormField.coverageType.getFormat());
				}
			}
		}
		
		if (ServiceFormRequest.getMemberNumber() != null && !ServiceFormRequest.getMemberNumber().equals("")) {
			if (!isValidString(ServiceFormRequest.getMemberNumber(), ServiceFormField.memberNumber.getLength())) {
				errors.add(ServiceFormField.memberNumber.getFormat());
			}

		}
		if (ServiceFormRequest.getSalutation() != null && !ServiceFormRequest.getSalutation().equals("")) {
			if (!isValidString(ServiceFormRequest.getSalutation(), ServiceFormField.salutation.getLength())) {
				errors.add(ServiceFormField.salutation.getFormat());
			}

		}
		if (ServiceFormRequest.getCustomerMiddleName() != null && !ServiceFormRequest.getCustomerMiddleName().equals("")) {
			if (!isValidString(ServiceFormRequest.getCustomerMiddleName(), ServiceFormField.customerMiddleName.getLength())) {
				errors.add(ServiceFormField.customerMiddleName.getFormat());
			}

		}
		if (ServiceFormRequest.getCustomerAddress2() != null && !ServiceFormRequest.getCustomerAddress2().equals("")) {
			if (!isValidString(ServiceFormRequest.getCustomerAddress2(), ServiceFormField.customerAddress2.getLength())) {
				errors.add(ServiceFormField.customerAddress2.getFormat());
			}

		}
		if (ServiceFormRequest.getCustomerWorkPhoneNumber() != null && !ServiceFormRequest.getCustomerWorkPhoneNumber().equals("")) {
			if ((!isValidString(ServiceFormRequest.getCustomerWorkPhoneNumber(), ServiceFormField.customerWorkPhoneNumber.getLength()))&&(!isPhoneNumberValid(ServiceFormRequest.getCustomerWorkPhoneNumber()))) {
				errors.add(ServiceFormField.customerWorkPhoneNumber.getFormat());
			}

		}
		if (ServiceFormRequest.getCustomerMobilePhoneNumber() != null && !ServiceFormRequest.getCustomerMobilePhoneNumber().equals("")) {
			if ((!isValidString(ServiceFormRequest.getCustomerMobilePhoneNumber(), ServiceFormField.customerMobilePhoneNumber.getLength()))&&(!isPhoneNumberValid(ServiceFormRequest.getCustomerMobilePhoneNumber()))) {
				errors.add(ServiceFormField.customerMobilePhoneNumber.getFormat());
			}

		}
		if (ServiceFormRequest.getCustomerEveningPhoneNumber() != null && !ServiceFormRequest.getCustomerEveningPhoneNumber().equals("")) {
			if ((!isValidString(ServiceFormRequest.getCustomerEveningPhoneNumber(), ServiceFormField.customerEveningPhoneNumber.getLength()))&&(!isPhoneNumberValid(ServiceFormRequest.getCustomerEveningPhoneNumber()))) {
				errors.add(ServiceFormField.customerEveningPhoneNumber.getFormat());
			}

		}
		if (ServiceFormRequest.getCustomerEmail() != null && !ServiceFormRequest.getCustomerEmail().equals("")) {
			if ((!isValidEmail(ServiceFormRequest.getCustomerEmail())||(ServiceFormRequest.getCustomerEmail().length() > ServiceFormField.customerEmail.getLength()))) {
				errors.add(ServiceFormField.customerEmail.getFormat());
			}

		}
		
		if (ServiceFormRequest.getCoBuyerFirstName() != null && !ServiceFormRequest.getCoBuyerFirstName().equals("")) {
			if (!isValidString(ServiceFormRequest.getCoBuyerFirstName(), ServiceFormField.coBuyerFirstName.getLength())) {
				errors.add(ServiceFormField.coBuyerFirstName.getFormat());
			}

		}
		if (ServiceFormRequest.getCoBuyerMiddleName() != null && !ServiceFormRequest.getCoBuyerMiddleName().equals("")) {
			if (!isValidString(ServiceFormRequest.getCoBuyerMiddleName(), ServiceFormField.coBuyerMiddleName.getLength())) {
				errors.add(ServiceFormField.coBuyerMiddleName.getFormat());
			}

		}
		if (ServiceFormRequest.getCoBuyerLastName() != null && !ServiceFormRequest.getCoBuyerLastName().equals("")) {
			if (!isValidString(ServiceFormRequest.getCoBuyerLastName(), ServiceFormField.coBuyerLastName.getLength())) {
				errors.add(ServiceFormField.coBuyerLastName.getFormat());
			}

		}
		if (ServiceFormRequest.getCoBuyerAddress1() != null && !ServiceFormRequest.getCoBuyerAddress1().equals("")) {
			if (!isValidString(ServiceFormRequest.getCoBuyerAddress1(), ServiceFormField.coBuyerAddress1.getLength())) {
				errors.add(ServiceFormField.coBuyerAddress1.getFormat());
			}

		}
		if (ServiceFormRequest.getCoBuyerAddress2() != null && !ServiceFormRequest.getCoBuyerAddress2().equals("")) {
			if (!isValidString(ServiceFormRequest.getCoBuyerAddress2(), ServiceFormField.coBuyerAddress2.getLength())) {
				errors.add(ServiceFormField.coBuyerAddress2.getFormat());
			}

		}
		if (ServiceFormRequest.getCoBuyerCity() != null && !ServiceFormRequest.getCoBuyerCity().equals("")) {
			if (!isValidString(ServiceFormRequest.getCoBuyerCity(), ServiceFormField.coBuyerCity.getLength())) {
				errors.add(ServiceFormField.coBuyerCity.getFormat());
			}

		}
		if (ServiceFormRequest.getCoBuyerStateCode() != null && !ServiceFormRequest.getCoBuyerStateCode().equals("")) {
			if (!isValidString(ServiceFormRequest.getCoBuyerStateCode(), ServiceFormField.coBuyerStateCode.getLength())) {
				errors.add(ServiceFormField.coBuyerStateCode.getFormat());
			}

		}
		if (ServiceFormRequest.getCoBuyerPostalCode() != null && !ServiceFormRequest.getCoBuyerPostalCode().equals("")) {
			if (!isValidString(ServiceFormRequest.getCoBuyerPostalCode(), ServiceFormField.coBuyerPostalCode.getLength())) {
				errors.add(ServiceFormField.coBuyerPostalCode.getFormat());
			}

		}
		if (ServiceFormRequest.getCoBuyerCountryCode() != null && !ServiceFormRequest.getCoBuyerCountryCode().equals("")) {
			if (!isValidString(ServiceFormRequest.getCoBuyerCountryCode(), ServiceFormField.coBuyerCountryCode.getLength())) {
				errors.add(ServiceFormField.coBuyerCountryCode.getFormat());
			}

		}
		if (ServiceFormRequest.getCoBuyerHomePhoneNumber() != null && !ServiceFormRequest.getCoBuyerHomePhoneNumber().equals("")) {
			if ((!isValidString(ServiceFormRequest.getCoBuyerHomePhoneNumber(), ServiceFormField.coBuyerHomePhoneNumber.getLength()))&&(!isPhoneNumberValid(ServiceFormRequest.getCoBuyerHomePhoneNumber()))) {
				errors.add(ServiceFormField.coBuyerHomePhoneNumber.getFormat());
			}

		}
		if (ServiceFormRequest.getCoBuyerWorkPhoneNumber() != null && !ServiceFormRequest.getCoBuyerWorkPhoneNumber().equals("")) {
			if ((!isValidString(ServiceFormRequest.getCoBuyerWorkPhoneNumber(), ServiceFormField.coBuyerWorkPhoneNumber.getLength()))&&(!isPhoneNumberValid(ServiceFormRequest.getCoBuyerWorkPhoneNumber()))) {
				errors.add(ServiceFormField.coBuyerWorkPhoneNumber.getFormat());
			}

		}
		if (ServiceFormRequest.getCoBuyerMobilePhoneNumber() != null && !ServiceFormRequest.getCoBuyerMobilePhoneNumber().equals("")) {
			if ((!isValidString(ServiceFormRequest.getCoBuyerMobilePhoneNumber(), ServiceFormField.coBuyerMobilePhoneNumber.getLength()))&&(!isPhoneNumberValid(ServiceFormRequest.getCoBuyerMobilePhoneNumber()))) {
				errors.add(ServiceFormField.coBuyerMobilePhoneNumber.getFormat());
			}

		}
		if (ServiceFormRequest.getCoBuyerEmail() != null && !ServiceFormRequest.getCoBuyerEmail().equals("")) {
			if ((!isValidEmail(ServiceFormRequest.getCoBuyerEmail())||(ServiceFormRequest.getCoBuyerEmail().length() > ServiceFormField.coBuyerEmail.getLength()))) {
				errors.add(ServiceFormField.coBuyerEmail.getFormat());
			}

		}

		
		if (ServiceFormRequest.getLienholderAddress2() != null && !ServiceFormRequest.getLienholderAddress2().equals("")) {
			if (!isValidString(ServiceFormRequest.getLienholderAddress2(), ServiceFormField.lienholderAddress2.getLength())) {
				errors.add(ServiceFormField.lienholderAddress2.getFormat());
			}

		}
		if (ServiceFormRequest.getLienholderNumber() != null && !ServiceFormRequest.getLienholderNumber().equals("")) {
			if (!isValidString(ServiceFormRequest.getLienholderNumber(), ServiceFormField.lienholderNumber.getLength())) {
				errors.add(ServiceFormField.lienholderNumber.getFormat());
			}

		}
		if (ServiceFormRequest.getLienholderAccountNumber() != null && !ServiceFormRequest.getLienholderAccountNumber().equals("")) {
			if (!isValidString(ServiceFormRequest.getLienholderAccountNumber(), ServiceFormField.lienholderAccountNumber.getLength())) {
				errors.add(ServiceFormField.lienholderAccountNumber.getFormat());
			}

		}
		
	
	if (ServiceFormRequest.getFinancedAmount() != null && !ServiceFormRequest.getFinancedAmount().equals("")) {
		if (!isValidDecimal(ServiceFormRequest.getFinancedAmount(),
				ServiceFormField.financedAmount.getLength(),
				ServiceFormField.financedAmount.getScale(),
				ServiceFormField.financedAmount.isAllowNegative())) {
			errors.add(ServiceFormField.financedAmount.getFormat());
		}

	}

	if (ServiceFormRequest.getLoanTotalPayment() != null && !ServiceFormRequest.getLoanTotalPayment().equals("")) {
		if (!isValidDecimal(ServiceFormRequest.getLoanTotalPayment(),
				ServiceFormField.loanTotalPayment.getLength(),
				ServiceFormField.loanTotalPayment.getScale(),
				ServiceFormField.loanTotalPayment.isAllowNegative())) {
			errors.add(ServiceFormField.loanTotalPayment.getFormat());
		}

	}
	if (ServiceFormRequest.getFinanceTermMonths() != null && !ServiceFormRequest.getFinanceTermMonths().equals("")) {
		if (!isValidInteger(ServiceFormRequest.getFinanceTermMonths(), ServiceFormField.financeTermMonths.getLength(),
					ServiceFormField.financeTermMonths.isAllowNegative())) {
				errors.add(ServiceFormField.financeTermMonths.getFormat());
			}
		}
	if (ServiceFormRequest.getNumberOfAdvPayments() != null && !ServiceFormRequest.getNumberOfAdvPayments().equals("")) {
		if (!isValidInteger(ServiceFormRequest.getNumberOfAdvPayments(), ServiceFormField.numberOfAdvPayments.getLength(),
					ServiceFormField.numberOfAdvPayments.isAllowNegative())) {
				errors.add(ServiceFormField.numberOfAdvPayments.getFormat());
			}
		}
	if (ServiceFormRequest.getNumberOfSkipPayments() != null && !ServiceFormRequest.getNumberOfSkipPayments().equals("")) {
		if (!isValidInteger(ServiceFormRequest.getNumberOfSkipPayments(), ServiceFormField.numberOfSkipPayments.getLength(),
					ServiceFormField.numberOfSkipPayments.isAllowNegative())) {
				errors.add(ServiceFormField.numberOfSkipPayments.getFormat());
			}
		}
	if (ServiceFormRequest.getBookValue() != null && !ServiceFormRequest.getBookValue().equals("")) {
		if (!isValidDecimal(ServiceFormRequest.getBookValue(),
				ServiceFormField.bookValue.getLength(),
				ServiceFormField.bookValue.getScale(),
				ServiceFormField.bookValue.isAllowNegative())) {
			errors.add(ServiceFormField.bookValue.getFormat());
		}

	}
	if (ServiceFormRequest.getLoanNumber() != null && !ServiceFormRequest.getLoanNumber().equals("")) {
		if (!isValidString(ServiceFormRequest.getLoanNumber(), ServiceFormField.loanNumber.getLength())) {
			errors.add(ServiceFormField.loanNumber.getFormat());
		}

	}
	if (ServiceFormRequest.getGapIdNumber() != null && !ServiceFormRequest.getGapIdNumber().equals("")) {
		if (!isValidString(ServiceFormRequest.getGapIdNumber(), ServiceFormField.gapIdNumber.getLength())) {
			errors.add(ServiceFormField.gapIdNumber.getFormat());
		}

	}
	if (ServiceFormRequest.getLeaseCapAmount() != null && !ServiceFormRequest.getLeaseCapAmount().equals("")) {
		if (!isValidDecimal(ServiceFormRequest.getLeaseCapAmount(),
				ServiceFormField.leaseCapAmount.getLength(),
				ServiceFormField.leaseCapAmount.getScale(),
				ServiceFormField.leaseCapAmount.isAllowNegative())) {
			errors.add(ServiceFormField.leaseCapAmount.getFormat());
		}

	}

	if (ServiceFormRequest.getPeriod() != null && !ServiceFormRequest.getPeriod().equals("")) {
		if (!isValidInteger(ServiceFormRequest.getPeriod(), ServiceFormField.period.getLength(),
					ServiceFormField.period.isAllowNegative())) {
				errors.add(ServiceFormField.period.getFormat());
			}
		}
	if (ServiceFormRequest.getGrossCapCost() != null && !ServiceFormRequest.getGrossCapCost().equals("")) {
		if (!isValidDecimal(ServiceFormRequest.getGrossCapCost(),
				ServiceFormField.grossCapCost.getLength(),
				ServiceFormField.grossCapCost.getScale(),
				ServiceFormField.grossCapCost.isAllowNegative())) {
			errors.add(ServiceFormField.grossCapCost.getFormat());
		}

	}
	if (ServiceFormRequest.getTotalAllowableMilesContract() != null && !ServiceFormRequest.getTotalAllowableMilesContract().equals("")) {
		if (!isValidInteger(ServiceFormRequest.getTotalAllowableMilesContract(), ServiceFormField.totalAllowableMilesContract.getLength(),
					ServiceFormField.totalAllowableMilesContract.isAllowNegative())) {
				errors.add(ServiceFormField.totalAllowableMilesContract.getFormat());
			}
		}
	if (ServiceFormRequest.getLoanMonthlyPayment() != null && !ServiceFormRequest.getLoanMonthlyPayment().equals("")) {
		if (!isValidDecimal(ServiceFormRequest.getLoanMonthlyPayment(),
				ServiceFormField.loanMonthlyPayment.getLength(),
				ServiceFormField.loanMonthlyPayment.getScale(),
				ServiceFormField.loanMonthlyPayment.isAllowNegative())) {
			errors.add(ServiceFormField.loanMonthlyPayment.getFormat());
		}

	}
	if (ServiceFormRequest.getInterestStartDate() != null && !ServiceFormRequest.getInterestStartDate().equals("")) {
		if ((!isValidDate(ServiceFormRequest.getInterestStartDate())||(ServiceFormRequest.getInterestStartDate().length() > ServiceFormField.interestStartDate.getLength()))) {
			errors.add(ServiceFormField.interestStartDate.getFormat());
		}

	}
	if (ServiceFormRequest.getDateOfFirstPayment() != null && !ServiceFormRequest.getDateOfFirstPayment().equals("")) {
		if ((!isValidDate(ServiceFormRequest.getDateOfFirstPayment())||(ServiceFormRequest.getDateOfFirstPayment().length() > ServiceFormField.dateOfFirstPayment.getLength()))) {
			errors.add(ServiceFormField.dateOfFirstPayment.getFormat());
		}

	}
	if (ServiceFormRequest.getScheduledTerminationDate() != null && !ServiceFormRequest.getScheduledTerminationDate().equals("")) {
		if ((!isValidDate(ServiceFormRequest.getScheduledTerminationDate())||(ServiceFormRequest.getScheduledTerminationDate().length() > ServiceFormField.scheduledTerminationDate.getLength()))) {
			errors.add(ServiceFormField.scheduledTerminationDate.getFormat());
		}

	}

	if (ServiceFormRequest.getPaymentDueDay() != null && !ServiceFormRequest.getPaymentDueDay().equals("")) {
		if (!isValidInteger(ServiceFormRequest.getPaymentDueDay(), ServiceFormField.paymentDueDay.getLength(),
				ServiceFormField.paymentDueDay.isAllowNegative())) {
			errors.add(ServiceFormField.paymentDueDay.getFormat());
		}

	}
	
	if (ServiceFormRequest.getInstallmentDownPayment() != null && !ServiceFormRequest.getInstallmentDownPayment().equals("")) {
		if (!isValidDecimal(ServiceFormRequest.getInstallmentDownPayment(),
				ServiceFormField.installmentDownPayment.getLength(),
				ServiceFormField.installmentDownPayment.getScale(),
				ServiceFormField.installmentDownPayment.isAllowNegative())) {
			errors.add(ServiceFormField.installmentDownPayment.getFormat());
		}

	}
	
	if (ServiceFormRequest.getInstallmentAmount() != null && !ServiceFormRequest.getInstallmentAmount().equals("")) {
		if (!isValidDecimal(ServiceFormRequest.getInstallmentAmount(),
				ServiceFormField.installmentAmount.getLength(),
				ServiceFormField.installmentAmount.getScale(),
				ServiceFormField.installmentAmount.isAllowNegative())) {
			errors.add(ServiceFormField.installmentAmount.getFormat());
		}

	}
	
	if (ServiceFormRequest.getInstallmentPaymentTerm() != null) {
		if (!isValidInteger(ServiceFormRequest.getInstallmentPaymentTerm(), ServiceFormField.installmentPaymentTerm.getLength(),
					ServiceFormField.installmentPaymentTerm.isAllowNegative())) {
				errors.add(ServiceFormField.installmentPaymentTerm.getFormat());
			}
		}
	
	if (ServiceFormRequest.getInstallmentPaymentFrequency() != null && !ServiceFormRequest.getInstallmentPaymentFrequency().equals("")) {
		if (!isValidString(ServiceFormRequest.getInstallmentPaymentFrequency(), ServiceFormField.installmentPaymentFrequency.getLength())) {
			errors.add(ServiceFormField.installmentPaymentFrequency.getFormat());
		}

	}
		return errors;

	}

	
	
	
	
	private String UpperCase(String sendSignature) {
		// TODO Auto-generated method stub
		String upper=sendSignature.toUpperCase();
		return upper;
		
	}




	/**
	 * Create a POJO based on the input contract request
	 * 
	 * @param message
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ValidationException
	 */
	private ServiceFormRequest getServiceFormRequest(LinkedHashMap message)
			throws NoSuchFieldException, SecurityException, IllegalAccessException, InvocationTargetException {
						
		HashMap<String, Object> payLoadMap = (HashMap) message;
		Iterator it = payLoadMap.entrySet().iterator();
		
		ServiceFormRequest ServiceFormRequestNew = new ServiceFormRequest();

		for (Map.Entry<String, Object> entry : payLoadMap.entrySet()) {
			String key = entry.getKey();
			Object object = entry.getValue();

			if ("account".equals(key) || "fsm".equals(key) || "vehicle".equals(key)  || "product".equals(key)|| "holder".equals(key) || "financing".equals(key)  )  {
				setFields((HashMap) entry.getValue(), ServiceFormRequestNew);
			} else {
				setFieldValue(ServiceFormRequestNew, key, object);
			}
			
		}
		 
		return ServiceFormRequestNew;
	}
	
	
	/**
	 * Set fields from the Hashmap values
	 * 
	 * @param categoryMap
	 * @param QuotingServiceRequest
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws ValidationException
	 */
	
	
	
	private void setFields(HashMap<String, Object> categoryMap, ServiceFormRequest ServiceFormRequest)
			throws IllegalAccessException, InvocationTargetException, SecurityException, NoSuchFieldException {
		for (Map.Entry<String, Object> entry : categoryMap.entrySet()) {
			String key = entry.getKey();
			Object object = entry.getValue();
			if ("optionalCoverages".equals(key)) {
				optionalCoverage(categoryMap, ServiceFormRequest);
			} else {
				setFieldValue(ServiceFormRequest, key, object);
			}
		}

	}

	private void optionalCoverage(HashMap<String, Object> optionalCoverageMap, ServiceFormRequest ServiceFormRequest)
			throws IllegalAccessException, InvocationTargetException, SecurityException, NoSuchFieldException {
		JSONObject jsonobject = new JSONObject(optionalCoverageMap);
		JSONArray jsonarray = jsonobject.getJSONArray("optionalCoverages");
		int lengthOfArray = jsonarray.length();
		List<String> coverageType = new ArrayList<String>();
		List<Double> coverageAmount = new ArrayList<Double>();
		for (int i = 0; i < lengthOfArray; i++) {
			JSONObject objectArray = jsonarray.getJSONObject(i);
			String[] elementNames = JSONObject.getNames(objectArray);
			for (String elementName : elementNames) {
				if (elementName.equals("coverageType")) {
					String coverageTypeValue = objectArray.getString(elementName);
					coverageType.add(coverageTypeValue);
					BeanUtils.setProperty(ServiceFormRequest, "coverageType", coverageType);
				} else if (elementName.equals("coverageAmount")) {
					Double coverageAmountValue = objectArray.getDouble(elementName);
					coverageAmount.add(coverageAmountValue);
					BeanUtils.setProperty(ServiceFormRequest, "coverageAmount", coverageAmount);

				}
			}
		}

	}
	
	/**
	 * Set POJO fields based on the request field type
	 * 
	 * @param quotingServiceRequest
	 * @param key
	 * @param object
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws ValidationException
	 */
	private void setFieldValue(ServiceFormRequest ServiceFormRequest, String key, Object object)
			throws IllegalAccessException, InvocationTargetException, NoSuchFieldException, SecurityException {
		Class type = ServiceFormRequest.class.getDeclaredField(key).getType();
		try {

			if (object != null) {
				if (type == Integer.class) {
					if (object instanceof Double) {

						if (BigDecimal.valueOf(((Double) object).doubleValue()).stripTrailingZeros().scale() > 0) {
							throw new ValidationException("Expected integer value but received double value");
						}

						BeanUtils.setProperty(ServiceFormRequest, key,
								Integer.valueOf(((Double) object).intValue()));
					} else {
						BeanUtils.setProperty(ServiceFormRequest, key, (Integer) object);
					}
				} else if (type == Double.class) {
					if (object instanceof Integer) {
						BeanUtils.setProperty(ServiceFormRequest, key,
								Double.valueOf(((Integer) object).doubleValue()));
					} else {
						BeanUtils.setProperty(ServiceFormRequest, key, (Double) object);
					}
				} else {
					BeanUtils.setProperty(ServiceFormRequest, key, (String) object);
				}
			}
		} catch (Exception ex) {
			System.out.println("in errror scenario");
			//logger.log(Level.ERROR, ex.getMessage());
			ServiceFormField field = ServiceFormField.valueOf(key);
			ServiceFormRequest.getErroredFields().add(field);
			ServiceFormRequest.getErrorMessages().add(field.getFormat());
		}
	}
	
	
	/**
	 * Validate String
	 * 
	 * @param input
	 * @param length
	 * @return
	 */
	public boolean isValidString(String input, int length) {
		return (input == null || "".equals(input.trim()) || input.length() <= length);
	}

	/**
	 * Validate VIN
	 * 
	 * @param input
	 * @param length
	 * @return
	 */
	public boolean isValidVIN(String input, int length) {
		return (input != null && input.length() == length);
	}
	
	/**
	 * Validate integer
	 * 
	 * @param input
	 * @param precision
	 * @param allowNegative
	 * @return
	 */
	public boolean isValidInteger(Integer input, int precision, boolean allowNegative) {

		int inputValue = (input != null ? input.intValue() : 0);

		if (inputValue > 0) {
			BigDecimal value = BigDecimal.valueOf(inputValue);
			return (value.precision() <= precision);
		} else if (inputValue < 0 && !allowNegative) {
			return false;
		}

		return true;
	}
	
	/**
	 * Validate decimal format
	 * 
	 * @param double1
	 * @param precision
	 * @param scale
	 * @return
	 */
	public boolean isValidDecimal(Double double1, int precision, int scale, boolean allowNegative) {

		double inputValue = (double1 != null ? double1.doubleValue() : 0.0);

		if (inputValue > 0) {
			BigDecimal value = BigDecimal.valueOf(inputValue).stripTrailingZeros();
			return ((value.scale()) <= scale && (value.precision() - value.scale()) <= (precision - scale));
		} else if (inputValue < 0 && !allowNegative) {
			return false;
		}

		return true;
	}
	
	public boolean isPhoneNumberValid(String phoneNo) {
		// validate phone numbers of format "1234567890"
		if (phoneNo.matches("^\\d{10}"))
			return true;
		// validating phone number with -, . or spaces
		else if (phoneNo.matches("^\\d{3}-\\d{3}-\\d{4}"))
			return true;
		// validating phone number with extension length from 3 to 5
		else if (phoneNo.matches("^\\(\\d{3}\\)\\d{3}-\\d{4}"))
			return true;
		// validating phone number where area code is in braces ()
		else if (phoneNo.matches("^\\(\\d{3}\\)\\s\\d{3}-\\d{4}"))
			return true;
		// return false if nothing matches the input
		else
			return false;
	}
	
	public boolean isValidDate(String dateToValdate) {

		if (dateToValdate != null && !"".equals(dateToValdate.trim())) {
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
			// To make strict date format validation
			formatter.setLenient(false);
			Date parsedDate = null;
			try {
				parsedDate = formatter.parse(dateToValdate);

			} catch (ParseException e) {
				// Handle exception
			}
			return (parsedDate != null);
		}

		return true;

	}
	public boolean isValidEmail(String emailToValidate) {

		if (emailToValidate.matches("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$")) {
			return true;
		}
		else
		{
		return false;
		}

	}

}