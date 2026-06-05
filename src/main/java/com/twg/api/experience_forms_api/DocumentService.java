package com.twg.api.experience_forms_api;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import com.twg.api.experience_forms_api.document.Pdf;
import com.twg.api.experience_forms_api.document.PdfUtil;
import com.twg.api.experience_forms_api.request.Account;
import com.twg.api.experience_forms_api.request.Address;
import com.twg.api.experience_forms_api.request.BrokerDetails;
import com.twg.api.experience_forms_api.request.CallingApp;
import com.twg.api.experience_forms_api.request.Contact;
import com.twg.api.experience_forms_api.request.FinanceDetails;
import com.twg.api.experience_forms_api.request.FinancingType;
import com.twg.api.experience_forms_api.request.FormRequest;
import com.twg.api.experience_forms_api.request.Insurance;
import com.twg.api.experience_forms_api.request.KeyValuePair;
import com.twg.api.experience_forms_api.request.Phone;
import com.twg.api.experience_forms_api.request.ProductDetail;
import com.twg.api.experience_forms_api.request.ServiceSecurity;
import com.twg.api.experience_forms_api.request.Vehicle;

public class DocumentService  {
	ArrayList<Map> phoneNumbers = new ArrayList<>();
	ArrayList<Map> phoneNumbersB = new ArrayList<>();
	ArrayList<Map> phoneNumbersCB = new ArrayList<>();
	ArrayList<Map> phoneNumbersL = new ArrayList<>();
	String chassisTermMonth;
	String manufacturerTermMonth;
    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	public LinkedHashMap validate(LinkedHashMap jsonpayload,Boolean isRequestJSONType, String pdfByetArray,LinkedHashMap xmlpayload,LinkedHashMap dwlVars,LinkedHashMap qrCodeMap,LinkedHashMap signatureValues) throws Exception {
        
		LinkedHashMap responseMap = new LinkedHashMap();
		Map legacyFormMap = (Map)xmlpayload;
		Map fswFormMap =(Map) jsonpayload;
		phoneNumbersB = (ArrayList) dwlVars.get("phoneB");
		phoneNumbersCB = (ArrayList)dwlVars.get("phoneCB");
		phoneNumbersL = (ArrayList) dwlVars.get("phoneL");
		PdfUtil pdfUtil = new PdfUtil();
		String formsMessageId = (String) dwlVars.get("formsMessageId");
		String stampSignature =(String) dwlVars.get("stampSignatureVar");
		String expirationType = (String) dwlVars.get("expirationType");
		chassisTermMonth = (String) dwlVars.get("bscTermMonth");
		manufacturerTermMonth =(String) dwlVars.get("ptTermMonth");
		
		// QR Code Stamping
		if ((String) dwlVars.get("qrCode") != null && (String) dwlVars.get("qrCode") != "") {
			pdfUtil.qrCode = (String) dwlVars.get("qrCode");
		} else {
			pdfUtil.qrCode = "error";
		}
		

		// The below section checks whether Signature Stamping is enabled
		if (stampSignature.trim().equalsIgnoreCase("TRUE")) {

			// The below section checks whether Customer Stamping is available
			if (signatureValues.get("customerValues") != null
					&& signatureValues.get("customerValues") != "") {

				// Storing the customerfield values to a list
				ArrayList<LinkedHashMap> customerSignValues = (ArrayList<LinkedHashMap>) signatureValues.get("customerValues");

				if (!customerSignValues.isEmpty() && customerSignValues.size() != 0) {
					LinkedHashMap<String, String> customerMapValues = customerSignValues.get(0);
					// Assigning positioning values and page number values
					pdfUtil.customerX = customerMapValues.get("customerX");
					pdfUtil.customerY = customerMapValues.get("customerY");
					pdfUtil.customerxBound = customerMapValues.get("customerxBound");
					pdfUtil.customeryBound = customerMapValues.get("customeryBound");
					pdfUtil.customerPageVal = customerMapValues.get("customerPageNumber");
				} else {
					// Assigning the Customer values as blank when customer data is not available
					pdfUtil.customerX = "";
					pdfUtil.customerY = "";
					pdfUtil.customerxBound = "";
					pdfUtil.customeryBound = "";
					pdfUtil.customerPageVal = "";
				}
			} else {
				pdfUtil.customerX = "";
				pdfUtil.customerY = "";
				pdfUtil.customerxBound = "";
				pdfUtil.customeryBound = "";
				pdfUtil.customerPageVal = "";
			}
			
			// The below section checks whether Customer Initials is available
			if (signatureValues.get("customerInitialsValues") != null
					&& signatureValues.get("customerInitialsValues") != "") {

				// Storing the customerfield values to a list
				ArrayList<LinkedHashMap> customerSignValues = (ArrayList<LinkedHashMap>) signatureValues.get("customerInitialsValues");

				if (!customerSignValues.isEmpty() && customerSignValues.size() != 0) {
					LinkedHashMap<String, String> customerMapValues = customerSignValues.get(0);
					// Assigning positioning values and page number values
					pdfUtil.customerInitialsX = customerMapValues.get("customerInitialsX");
					pdfUtil.customerInitialsY = customerMapValues.get("customerInitialsY");
					pdfUtil.customerInitialsxBound = customerMapValues.get("customerInitialsxBound");
					pdfUtil.customerInitialsyBound = customerMapValues.get("customerInitialsyBound");
					pdfUtil.customerInitialsPageVal = customerMapValues.get("customerInitialsPageNumber");
				} else {
					// Assigning the Customer values as blank when customer data is not available
					pdfUtil.customerInitialsX = "";
					pdfUtil.customerInitialsY = "";
					pdfUtil.customerInitialsxBound = "";
					pdfUtil.customerInitialsyBound = "";
					pdfUtil.customerInitialsPageVal = "";
				}
			} else {
				pdfUtil.customerInitialsX = "";
				pdfUtil.customerInitialsY = "";
				pdfUtil.customerInitialsxBound = "";
				pdfUtil.customerInitialsyBound = "";
				pdfUtil.customerInitialsPageVal = "";
			}
			
			// Condition to check whether Dealer Signature is available
			if (signatureValues.get("dealerValues") != null
					&& signatureValues.get("dealerValues") != "") {

				// Storing the dealerfield values to a list
				ArrayList<LinkedHashMap> dealerSignValues =(ArrayList<LinkedHashMap>) signatureValues.get("dealerValues");

				if (!dealerSignValues.isEmpty() && dealerSignValues.size() != 0) {
					LinkedHashMap<String, String> dealerMapValues = dealerSignValues.get(0);
					pdfUtil.dealerX = dealerMapValues.get("dealerX");
					pdfUtil.dealerY = dealerMapValues.get("dealerY");
					pdfUtil.dealerxBound = dealerMapValues.get("dealerxBound");
					pdfUtil.dealeryBound = dealerMapValues.get("dealeryBound");
					pdfUtil.dealerPageVal = dealerMapValues.get("dealerPageNumber");
				} else {
					// Assigning the Dealer values as blank when dealer data is not available
					pdfUtil.dealerX = "";
					pdfUtil.dealerY = "";
					pdfUtil.dealerxBound = "";
					pdfUtil.dealeryBound = "";
					pdfUtil.dealerPageVal = "";
				}
			} else {
				pdfUtil.dealerX = "";
				pdfUtil.dealerY = "";
				pdfUtil.dealerxBound = "";
				pdfUtil.dealeryBound = "";
				pdfUtil.dealerPageVal = "";
			}

			// Condition to check whether Dealer Initials is available
			if (signatureValues.get("dealerInitialsValues") != null
					&& signatureValues.get("dealerInitialsValues") != "") {

				// Storing the dealerfield values to a list
				ArrayList<LinkedHashMap> dealerSignValues = (ArrayList<LinkedHashMap>) signatureValues.get("dealerInitialsValues");

				if (!dealerSignValues.isEmpty() && dealerSignValues.size() != 0) {
					LinkedHashMap<String, String> dealerMapValues = dealerSignValues.get(0);
					pdfUtil.dealerInitialsX = dealerMapValues.get("dealerInitialsX");
					pdfUtil.dealerInitialsY = dealerMapValues.get("dealerInitialsY");
					pdfUtil.dealerInitialsxBound = dealerMapValues.get("dealerInitialsxBound");
					pdfUtil.dealerInitialsyBound = dealerMapValues.get("dealerInitialsyBound");
					pdfUtil.dealerInitialsPageVal = dealerMapValues.get("dealerInitialsPageNumber");
				} else {
					// Assigning the Dealer values as blank when dealer data is not available
					pdfUtil.dealerInitialsX = "";
					pdfUtil.dealerInitialsY = "";
					pdfUtil.dealerInitialsxBound = "";
					pdfUtil.dealerInitialsyBound = "";
					pdfUtil.dealerInitialsPageVal = "";
				}
			} else {
				pdfUtil.dealerInitialsX = "";
				pdfUtil.dealerInitialsY = "";
				pdfUtil.dealerInitialsxBound = "";
				pdfUtil.dealerInitialsyBound = "";
				pdfUtil.dealerInitialsPageVal = "";
			}

			// Condition to check whether CoBuyer Signature is available
			if (signatureValues.get("coBuyerValues") != null
					&& signatureValues.get("coBuyerValues") != "") {

				// Storing the coBuyerfield values to a list
				ArrayList<LinkedHashMap> coBuyerSignValues = (ArrayList<LinkedHashMap>) signatureValues.get("coBuyerValues");

				if (!coBuyerSignValues.isEmpty() && coBuyerSignValues.size() != 0) {
					LinkedHashMap<String, String> coBuyerMapValues = coBuyerSignValues.get(0);
					pdfUtil.coBuyerX = coBuyerMapValues.get("coBuyerX");
					pdfUtil.coBuyerY = coBuyerMapValues.get("coBuyerY");
					pdfUtil.coBuyerxBound = coBuyerMapValues.get("coBuyerxBound");
					pdfUtil.coBuyeryBound = coBuyerMapValues.get("coBuyeryBound");
					pdfUtil.coBuyerPageVal = coBuyerMapValues.get("coBuyerPageNumber");
				} else {
					// Assigning the CoBuyer values as blank when coBuyer data is not available
					pdfUtil.coBuyerX = "";
					pdfUtil.coBuyerY = "";
					pdfUtil.coBuyerxBound = "";
					pdfUtil.coBuyeryBound = "";
					pdfUtil.coBuyerPageVal = "";
				}
			} else {
				pdfUtil.coBuyerX = "";
				pdfUtil.coBuyerY = "";
				pdfUtil.coBuyerxBound = "";
				pdfUtil.coBuyeryBound = "";
				pdfUtil.coBuyerPageVal = "";
			}

			// Condition to check whether CoBuyer Initials is available
			if (signatureValues.get("coBuyerInitialsValues") != null
					&& signatureValues.get("coBuyerInitialsValues") != "") {

				// Storing the coBuyerfield values to a list
				ArrayList<LinkedHashMap> coBuyerSignValues =(ArrayList<LinkedHashMap>) signatureValues.get("coBuyerInitialsValues");

				if (!coBuyerSignValues.isEmpty() && coBuyerSignValues.size() != 0) {
					LinkedHashMap<String, String> coBuyerMapValues = coBuyerSignValues.get(0);
					pdfUtil.coBuyerInitialsX = coBuyerMapValues.get("coBuyerInitialsX");
					pdfUtil.coBuyerInitialsY = coBuyerMapValues.get("coBuyerInitialsY");
					pdfUtil.coBuyerInitialsxBound = coBuyerMapValues.get("coBuyerInitialsxBound");
					pdfUtil.coBuyerInitialsyBound = coBuyerMapValues.get("coBuyerInitialsyBound");
					pdfUtil.coBuyerInitialsPageVal = coBuyerMapValues.get("coBuyerInitialsPageNumber");
				} else {
					// Assigning the CoBuyer values as blank when coBuyer data is not available
					pdfUtil.coBuyerInitialsX = "";
					pdfUtil.coBuyerInitialsY = "";
					pdfUtil.coBuyerInitialsxBound = "";
					pdfUtil.coBuyerInitialsyBound = "";
					pdfUtil.coBuyerInitialsPageVal = "";
				}
			} else {
				pdfUtil.coBuyerInitialsX = "";
				pdfUtil.coBuyerInitialsY = "";
				pdfUtil.coBuyerInitialsxBound = "";
				pdfUtil.coBuyerInitialsyBound = "";
				pdfUtil.coBuyerInitialsPageVal = "";
			}

		} else {
			// Assigning all the signature related values as blank when stamp Signature is
			// disabled
			pdfUtil.customerX = "";
			pdfUtil.customerY = "";
			pdfUtil.customerxBound = "";
			pdfUtil.customeryBound = "";
			pdfUtil.customerPageVal = "";
			pdfUtil.coBuyerX = "";
			pdfUtil.coBuyerY = "";
			pdfUtil.coBuyerxBound = "";
			pdfUtil.coBuyeryBound = "";
			pdfUtil.coBuyerPageVal = "";
			pdfUtil.dealerX = "";
			pdfUtil.dealerY = "";
			pdfUtil.dealerxBound = "";
			pdfUtil.dealeryBound = "";
			pdfUtil.dealerPageVal = "";
			pdfUtil.customerInitialsX = "";
			pdfUtil.customerInitialsY = "";
			pdfUtil.customerInitialsxBound = "";
			pdfUtil.customerInitialsyBound = "";
			pdfUtil.customerInitialsPageVal = "";
			pdfUtil.coBuyerInitialsX = "";
			pdfUtil.coBuyerInitialsY = "";
			pdfUtil.coBuyerInitialsxBound = "";
			pdfUtil.coBuyerInitialsyBound = "";
			pdfUtil.coBuyerInitialsPageVal = "";
			pdfUtil.dealerInitialsX = "";
			pdfUtil.dealerInitialsY = "";
			pdfUtil.dealerInitialsxBound = "";
			pdfUtil.dealerInitialsyBound = "";
			pdfUtil.dealerInitialsPageVal = "";
		}
		
		Boolean isRequestTypeJSON = isRequestJSONType;
		String pdfTemplateAsString = pdfByetArray;
		byte[] decodedBytes = Base64.decodeBase64(pdfTemplateAsString);
		InputStream template = new ByteArrayInputStream(decodedBytes);
		byte[] InputByteArrayStream = null;

		// FormRequest contains all the input data mapped to appropriate entities.
		FormRequest formRequest = new FormRequest();
		Map<String, String> errors = new HashMap<String, String>();
		
		if (fswFormMap != null) {
			formRequest = populateJsonRequestData(fswFormMap, formRequest, formsMessageId, isRequestTypeJSON,
					expirationType);
			errors = formRequest.validate("JSON");
		
		} else {
			formRequest = populateXmlRequestData(legacyFormMap, formRequest, formsMessageId, isRequestTypeJSON,
					expirationType);
			errors = formRequest.validate("XML");
		}

		if (errors.size() > 0) {
			responseMap.put("validationErrors", errors);
		} else {
			responseMap.put("validationErrors", errors);
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

			LegacyDocumentMapper documentMapper = new LegacyDocumentMapper();
			// Create and receive 'form' (form is the completed contract form in PDF)
			// PDF is received as a stream and then converted to byte array data
			InputByteArrayStream = documentMapper.getForm(formRequest, template);
		}

		responseMap.put("formFileName", formRequest.getOutputFileName());
		responseMap.put("accountNumber", formRequest.getAccount().getAccountNumber());
		responseMap.put("folderName", formRequest.getFolderName());
		responseMap.put("InputByteArrayStream",InputByteArrayStream);
		return responseMap;
	}

	private FormRequest populateXmlRequestData(Map legacyFormMap, FormRequest formRequest, String formsMessageId,
			Boolean isRequestTypeJSON, String expirationType) throws Exception {

		// Set key values such as quoteId and contract number
		formRequest.setContractNumber((String) (legacyFormMap.get("contractNumber")));
		formRequest.setQuoteId((String) (legacyFormMap.get("quoteId")));

		formRequest.setFormsMessageId(formsMessageId);
		formRequest.setExpirationType(expirationType);
		formRequest.setIsRequestTypeJSON(isRequestTypeJSON);

		// Create an entity with Dealer's information
		Account account = populateXMLAccountDetails(legacyFormMap);
		formRequest.setAccount(account);

		// Create an entity with Vehicle information
		Vehicle vehicle = populateXMLVehicleDetails(legacyFormMap);
		formRequest.setVehicle(vehicle);

		// Create an entity for CallingApp details
		CallingApp callingApp = populateXMLCallingAppDetails(legacyFormMap);
		formRequest.setCallingApp(callingApp);

		// Create an entity for Product Details
		Set<ProductDetail> productDetails = populateXMLProductDetails(legacyFormMap);
		formRequest.setProductDetails(productDetails);

		// Create an entity for Finance Details
		FinanceDetails financeDetails = populateXMLFinanceDetails(legacyFormMap);
		formRequest.setFinanceDetails(financeDetails);

		// Create entities with Contact Details based on type
		populateXMLContact(legacyFormMap, formRequest);
		formRequest.setOutputFileName(formRequest.getOutputFileName());

		Set<KeyValuePair> fillerDetails = populateXMLFillers(legacyFormMap);
		formRequest.setFillers(fillerDetails);
		return formRequest;
	}

	// Populate XML Contact Detail based on type
	private void populateXMLContact(Map legacyFormMap, FormRequest formRequest) {
		// Create entities with Contact Details
		ArrayList<Map> contacts = (ArrayList) (legacyFormMap.get("contacts"));

		if (legacyFormMap.get("contacts") != null && !legacyFormMap.get("contacts").equals(null)
				&& !legacyFormMap.get("contacts").equals("")) {
			for (Map contact : contacts) {
				if (contact != null && !contact.isEmpty() && !contact.equals(null) && !contact.equals("")) {
					if (contact.get("type") != null && contact.get("type").equals("B")) {
						Contact buyer = populateXMLContactDetails(legacyFormMap, contact);
						buyer.setType("B");
						formRequest.setBuyer(buyer);
					}
					if (contact.get("type") != null && contact.get("type").equals("CB")) {
						Contact coBuyer = new Contact();
						coBuyer = populateXMLContactDetails(legacyFormMap, contact);
						coBuyer.setType("CB");
						formRequest.setCoBuyer(coBuyer);
					}
					if (contact.get("type") != null && contact.get("type").equals("L")) {
						// LienHolder Details Start
						Contact lienHolder = new Contact();
						lienHolder = populateXMLContactDetails(legacyFormMap, contact);
						lienHolder.setType("L");
						formRequest.setLienHolder(lienHolder);
					}
				}
			}
		}
	}

	// Populate XML Contact Details in form Request
	private Contact populateXMLContactDetails(Map legacyFormMap, Map contact) {
		Contact contactDetails = new Contact();

		contactDetails.setFirstName((String) contact.get("firstName"));
		contactDetails.setLastName((String) contact.get("lastName"));
		contactDetails.setMiddleInitialName((String) contact.get("middleInitialName"));
		contactDetails.setEmail((String) contact.get("email"));
		contactDetails.setMemberNumber((String) contact.get("memberNumber"));
		Address address = new Address();

		if (contact.get("address") != null && !contact.get("address").equals(null)
				&& !contact.get("address").equals("")) {
			address.setAddress1((String) ((Map) contact.get("address")).get("address1"));
			address.setAddress2((String) ((Map) contact.get("address")).get("address2"));
			address.setPostalCode((String) ((Map) contact.get("address")).get("zip"));
			address.setCity((String) ((Map) contact.get("address")).get("city"));
			address.setStateCode((String) ((Map) contact.get("address")).get("state"));
			address.setCountryCode((String) ((Map) contact.get("address")).get("country"));
		}

		contactDetails.setAddress(address);

		if (contact.get("type").equals("B")) {
			phoneNumbers = phoneNumbersB;
		}
		if (contact.get("type").equals("CB")) {
			phoneNumbers = phoneNumbersCB;
		}
		if (contact.get("type").equals("L")) {
			phoneNumbers = phoneNumbersL;
		}
		Set<Phone> phones = new HashSet();
		if (phoneNumbers != null) {
			for (Map phoneNumber : phoneNumbers) {
				Phone phone = new Phone();
				if (phoneNumber != null) {
					if (phoneNumber.get("type") != null && !phoneNumber.get("type").equals(null)
							&& !phoneNumber.get("type").equals("")) {
						phone.setType(phoneNumber.get("type").toString());
					}
					if (phoneNumber.get("number") != null && !phoneNumber.get("number").equals(null)
							&& !phoneNumber.get("number").equals("")) {
						phone.setNumber(phoneNumber.get("number").toString());
					}
					if (phoneNumber.get("areaCode") != null && !phoneNumber.get("areaCode").equals(null)
							&& !phoneNumber.get("areaCode").equals("")) {
						phone.setAreaCode(Integer.parseInt(phoneNumber.get("areaCode").toString()));

					}
				}
				phones.add(phone);
			}
		}
		contactDetails.setPhones(phones);
		return contactDetails;
	}

	// Populate XML Account Details in form Request
	private Account populateXMLAccountDetails(Map legacyFormMap) {
		// Create a new account object
		Account account = new Account();

		// Retrieve 'account' Map object from Request
		if (legacyFormMap.get("dealer") != null && !legacyFormMap.get("dealer").equals(null)
				&& !legacyFormMap.get("dealer").equals("")) {

			Map dealerMap = (Map) legacyFormMap.get("dealer");
			account.setAccountNumber((String) dealerMap.get("internalId"));
			account.setName((String) dealerMap.get("name"));
			account.setFieldRep((String) dealerMap.get("fieldRep"));

			// Dealer's address Information
			Address dealerAddress = new Address();

			if (dealerMap.get("address") != null && !dealerMap.get("address").equals("")
					&& !dealerMap.get("address").equals(null)) {
				Map addressMap = (Map) dealerMap.get("address");
				dealerAddress.setAddress1((String) addressMap.get("address1"));
				dealerAddress.setAddress2((String) addressMap.get("address2"));
				dealerAddress.setPostalCode((String) addressMap.get("zip"));
				dealerAddress.setCity((String) addressMap.get("city"));
				dealerAddress.setStateCode((String) addressMap.get("state"));
				dealerAddress.setCountryCode((String) addressMap.get("country"));
			}
			account.setAddress(dealerAddress);

			// Create an entity with Dealer's phone details.
			Phone dealerPhone = new Phone();
			Map phoneMap = (Map) dealerMap.get("phone");

			// Retrieve and set the phone number when phone section is found and number not
			// empty
			if (phoneMap != null && phoneMap.get("number") != null)
				dealerPhone.setNumber((String) phoneMap.get("number"));

			// Retrieve and set the area code when phone section is found and area code is
			// not empty
			if (phoneMap != null && phoneMap.get("areaCode") != null && !phoneMap.get("areaCode").equals("")
					&& !phoneMap.get("areaCode").equals(null))
				dealerPhone.setAreaCode(Integer.parseInt((String) phoneMap.get("areaCode")));

			account.setPhone(dealerPhone);
		}
		return account;
	}

	// Populate XML Finance Details in form Request
	private FinanceDetails populateXMLFinanceDetails(Map legacyFormMap) throws Exception {
		FinanceDetails financeDetails = new FinanceDetails();

		if (legacyFormMap.get("financeDetails") != null && !legacyFormMap.get("financeDetails").equals(null)
				&& !legacyFormMap.get("financeDetails").equals("")) {
			Map financeDetailsMap = (Map) legacyFormMap.get("financeDetails");

			financeDetails.setFinanceType((String) financeDetailsMap.get("financingType"));
			financeDetails.setLoanNumber((String) financeDetailsMap.get("loanNumber"));
			financeDetails.setGapIdNumber((String) financeDetailsMap.get("gapIdNumber"));

			if (financeDetailsMap.get("ballonResidualAmount") == null
					|| financeDetailsMap.get("ballonResidualAmount").equals(null)
					|| financeDetailsMap.get("ballonResidualAmount").equals(""))
				financeDetails.setBaloonResidualValue(0.0);
			else
				financeDetails.setBaloonResidualValue(
						(Double.parseDouble(financeDetailsMap.get("ballonResidualAmount").toString())));

			if (financeDetailsMap.get("apr") == null || financeDetailsMap.get("apr").equals(null)
					|| financeDetailsMap.get("apr").equals(""))
				financeDetails.setApr(0.0);
			else
				financeDetails.setApr((Double.parseDouble(financeDetailsMap.get("apr").toString())));

			if (financeDetailsMap.get("financedAmount") == null || financeDetailsMap.get("financedAmount").equals(null)
					|| financeDetailsMap.get("financedAmount").equals(""))
				financeDetails.setFinancedAmount(null);
			else
				financeDetails
						.setFinancedAmount((Double.parseDouble(financeDetailsMap.get("financedAmount").toString())));

			if (financeDetailsMap.get("paymentAmount") == null || financeDetailsMap.get("paymentAmount").equals(null)
					|| financeDetailsMap.get("paymentAmount").equals(""))
				financeDetails.setPaymentAmount(null);
			else
				financeDetails
						.setPaymentAmount((Double.parseDouble(financeDetailsMap.get("paymentAmount").toString())));

			if (financeDetailsMap.get("monthlyPaymentAmount") == null
					|| financeDetailsMap.get("monthlyPaymentAmount").equals(null)
					|| financeDetailsMap.get("monthlyPaymentAmount").equals(""))
				financeDetails.setMonthlyPaymentAmount(0.0);
			else
				financeDetails.setMonthlyPaymentAmount(
						(Double.parseDouble(financeDetailsMap.get("monthlyPaymentAmount").toString())));

			if (financeDetailsMap.get("msrp") == null || financeDetailsMap.get("msrp").equals(null)
					|| financeDetailsMap.get("msrp").equals(""))
				financeDetails.setMsrp(0.0);
			else
				financeDetails.setMsrp((Double.parseDouble(financeDetailsMap.get("msrp").toString())));

			if (financeDetailsMap.get("nada") == null || financeDetailsMap.get("nada").equals(null)
					|| financeDetailsMap.get("nada").equals(""))
					{
						financeDetails.setNada(0.0);
					}
			else
				financeDetails.setNada((Double.parseDouble(financeDetailsMap.get("nada").toString())));

			if (financeDetailsMap.get("leaseCapAmount") == null || financeDetailsMap.get("leaseCapAmount").equals(null)
					|| financeDetailsMap.get("leaseCapAmount").equals(""))
				financeDetails.setLeaseCapAmount(0.0);
			else
				financeDetails
						.setLeaseCapAmount((Double.parseDouble(financeDetailsMap.get("leaseCapAmount").toString())));

			if (financeDetailsMap.get("grossCapCost") == null || financeDetailsMap.get("grossCapCost").equals(null)
					|| financeDetailsMap.get("grossCapCost").equals(""))
				financeDetails.setGrossCapCost(0.0);
			else
				financeDetails.setGrossCapCost((Double.parseDouble(financeDetailsMap.get("grossCapCost").toString())));

			if (financeDetailsMap.get("totalAllowableMilesContract") == null
					|| financeDetailsMap.get("totalAllowableMilesContract").equals(null)
					|| financeDetailsMap.get("totalAllowableMilesContract").equals(""))
				financeDetails.setTotalAllowableMilesContract(0.0);
			else
				financeDetails.setTotalAllowableMilesContract(
						(Double.parseDouble(financeDetailsMap.get("totalAllowableMilesContract").toString())));

			if (financeDetailsMap.get("numberOfPayments") == null
					|| financeDetailsMap.get("numberOfPayments").equals(null)
					|| financeDetailsMap.get("numberOfPayments").equals(""))
				financeDetails.setNumberOfPayments(0);
			else
				financeDetails
						.setNumberOfPayments(Integer.parseInt(financeDetailsMap.get("numberOfPayments").toString()));

			if (financeDetailsMap.get("numberOfAdvPayments") == null
					|| financeDetailsMap.get("numberOfAdvPayments").equals(null)
					|| financeDetailsMap.get("numberOfAdvPayments").equals(""))
				financeDetails.setNumberOfAdvPayments(0);
			else
				financeDetails.setNumberOfAdvPayments(
						Integer.parseInt(financeDetailsMap.get("numberOfAdvPayments").toString()));

			if (financeDetailsMap.get("numberOfSkipPayments") == null
					|| financeDetailsMap.get("numberOfSkipPayments").equals(null)
					|| financeDetailsMap.get("numberOfSkipPayments").equals(""))
				financeDetails.setNumberOfSkipPayments(0);
			else
				financeDetails.setNumberOfSkipPayments(
						Integer.parseInt(financeDetailsMap.get("numberOfSkipPayments").toString()));

			if (financeDetailsMap.get("period") == null || financeDetailsMap.get("period").equals(null)
					|| financeDetailsMap.get("period").equals(""))
				financeDetails.setPeriod(0);
			else
				financeDetails.setPeriod(Integer.parseInt(financeDetailsMap.get("period").toString()));

			if (financeDetailsMap.get("startDate") != null && !financeDetailsMap.get("startDate").equals("")) {
				financeDetails.setStartDate(
						new SimpleDateFormat("yyyy-MM-dd").parse((String) financeDetailsMap.get("startDate")));
			}

			if (financeDetailsMap.get("interestStartDate") != null
					&& !financeDetailsMap.get("interestStartDate").equals("")) {
				financeDetails.setInterestStartDate(
						new SimpleDateFormat("yyyy-MM-dd").parse((String) financeDetailsMap.get("interestStartDate")));
			}

			if (financeDetailsMap.get("firstPaymentDate") != null
					&& !financeDetailsMap.get("firstPaymentDate").equals("")) {
				financeDetails.setFirstPaymentDate(
						new SimpleDateFormat("yyyy-MM-dd").parse((String) financeDetailsMap.get("firstPaymentDate")));
			}

			if (financeDetailsMap.get("endDate") != null && !financeDetailsMap.get("endDate").equals("")) {
				financeDetails.setEndDate(
						new SimpleDateFormat("yyyy-MM-dd").parse((String) financeDetailsMap.get("endDate")));
			}
		}

		return financeDetails;
	}

	// Populate XML Vehicle Details in form Request
	private Vehicle populateXMLVehicleDetails(Map legacyFormMap) throws Exception {

		Vehicle vehicle = new Vehicle();

		if (legacyFormMap.get("vehicle") != null && !legacyFormMap.get("vehicle").equals(null)
				&& !legacyFormMap.get("vehicle").equals("")) {
			Map vehicleMap = (Map) legacyFormMap.get("vehicle");
			Map modelMap = (Map) vehicleMap.get("model");

			if (modelMap != null) {
				vehicle.setVehicleCode((String) modelMap.get("code"));
				vehicle.setModel((String) modelMap.get("name"));
				if (modelMap.get("make") != null && !modelMap.get("make").equals(null)
						&& !modelMap.get("make").equals("")) {
					vehicle.setMake((String) ((Map) modelMap.get("make")).get("name"));
				}
			}

			if (vehicleMap.get("inServiceDate") != null && !vehicleMap.get("inServiceDate").equals("")) {
				vehicle.setInServiceDate(
						new SimpleDateFormat("yyyy-MM-dd").parse((String) vehicleMap.get("inServiceDate")));
			}

			vehicle.setVehicleClass((String) vehicleMap.get("vehicleClass"));
			vehicle.setVehicleType((String) vehicleMap.get("type"));
			vehicle.setVin((String) vehicleMap.get("vin"));
			vehicle.setYear(String.valueOf(vehicleMap.get("year")));

			if (vehicleMap.get("odometer") != null && !vehicleMap.get("odometer").equals("")
					&& !vehicleMap.get("odometer").equals(null)) {
				vehicle.setOdometer(Integer.parseInt((String) vehicleMap.get("odometer")));
			}

			if (vehicleMap.get("odometerAtPurchase") != null && !vehicleMap.get("odometerAtPurchase").equals("")
					&& !vehicleMap.get("odometerAtPurchase").equals(null)) {
				vehicle.setOdometerAtPurchase(Integer.parseInt((String) vehicleMap.get("odometerAtPurchase")));
			}
			
			if (vehicleMap.get("price") != null && !vehicleMap.get("price").equals("")
					&& !vehicleMap.get("price").equals(null)) {
				vehicle.setVehiclePurchasePrice(Double.parseDouble(vehicleMap.get("price").toString()));
			}
			//set value for trim
			if (vehicleMap.get("trim") != null && ! vehicleMap.get("trim").equals(null)
					&& !vehicleMap.get("trim").equals(""))
			{
				vehicle.setTrim(vehicleMap.get("trim").toString());
			}


			vehicle.setDiesel((String) vehicleMap.get("isDiesel")!=null ? ((String) vehicleMap.get("isDiesel")).equals("true") : false);
			vehicle.setFourByFour((String) vehicleMap.get("isFourByFour")!=null?((String) vehicleMap.get("isFourByFour")).equals("true"):false);
			vehicle.setTurbo((String) vehicleMap.get("isTurbo")!=null?((String) vehicleMap.get("isTurbo")).equals("true"):false);
			vehicle.setNewCar((String) vehicleMap.get("isNew")!=null?((String) vehicleMap.get("isNew")).equals("true"):false);
			vehicle.setCar((String) vehicleMap.get("isCar")!=null?((String) vehicleMap.get("isCar")).equals("true"):false);
			vehicle.setTruck((String) vehicleMap.get("isTruck")!=null?((String) vehicleMap.get("isTruck")).equals("true"):false);
			vehicle.setCommercialUsage((String) vehicleMap.get("isCommercial")!=null?((String) vehicleMap.get("isCommercial")).equals("true"):false);
			vehicle.setNavigation((String) vehicleMap.get("hasNavigation")!=null?((String) vehicleMap.get("hasNavigation")).equals("true"):false);
			vehicle.setHybrid((String) vehicleMap.get("isHybrid")!=null?((String) vehicleMap.get("isHybrid")).equals("true"):false);
		}
		return vehicle;
	}

	// Populate XML Calling App Details in form Request
	private CallingApp populateXMLCallingAppDetails(Map legacyFormMap) throws Exception {

		// Create a new object to store callingApp details
		Map callingAppMap = new HashMap();
		Map serviceSecurityMap = new HashMap();

		// Retrieve callingAPP map from the request;
		callingAppMap = (Map) legacyFormMap.get("callingApp");
		if (callingAppMap != null) {
			serviceSecurityMap = (Map) callingAppMap.get("serviceSecurity");
		}
		CallingApp callingApp = new CallingApp();
		if (callingAppMap != null) {
		callingApp.setAppId((String) callingAppMap.get("appId"));

		if (callingAppMap.get("persist") != null && !callingAppMap.get("persist").equals(null)
				&& !callingAppMap.get("persist").equals("")) {
			callingApp.setPersist(callingAppMap.get("persist").equals("true"));
		}

		if (callingAppMap.get("sourceSystem") != null && !callingAppMap.get("sourceSystem").equals(null)
				&& !callingAppMap.get("sourceSystem").equals("")) {
			callingApp.setSourceSystem(((String) callingAppMap.get("sourceSystem")));
		}
		}
		

		if (serviceSecurityMap != null) {

			ServiceSecurity serviceSecurity = new ServiceSecurity();

			if (serviceSecurityMap.get("token") != null && !serviceSecurityMap.get("token").equals(null)
					&& !serviceSecurityMap.get("token").equals("")) {
				serviceSecurity.setToken((String) serviceSecurityMap.get("token"));
			}
			if (serviceSecurityMap.get("username") != null && !serviceSecurityMap.get("username").equals(null)
					&& !serviceSecurityMap.get("username").equals("")) {
				serviceSecurity.setUsername((String) serviceSecurityMap.get("username"));
			}
			callingApp.setServiceSecurity(serviceSecurity);
		}
		return callingApp;
	}

	// Populate XML Filler Details in form Request
	private Set<KeyValuePair> populateXMLFillers(Map legacyFormMap) throws Exception {

		// Create an entity with ProductDetails
		Set<KeyValuePair> fillerDetails = new HashSet<>();
		ArrayList<Map> fillerItems = (ArrayList) (legacyFormMap.get("filler"));

		if (null != fillerItems) {
			for (Map fillerItem : fillerItems) {

				KeyValuePair filler = new KeyValuePair();
				if (fillerItem != null && !fillerItem.equals(null)
						&& !fillerItem.equals("")) {
					if (fillerItem.get("key") != null && !fillerItem.get("key").equals(null)
							&& !fillerItem.get("key").equals("")) {
						filler.setKey(fillerItem.get("key").toString());
					}
					if (fillerItem.get("value") != null && !fillerItem.get("value").equals(null)
							&& !fillerItem.get("value").equals("")) {
						filler.setValue(fillerItem.get("value").toString());
					}
				}
				if (fillerItem != null && !fillerItem.equals(null)
						&& !fillerItem.equals("")) {
					fillerDetails.add(filler);
				}
				filler = null;
			}
		}
		return fillerDetails;
	}

	// Populate XML Product Details in form Request
	private Set<ProductDetail> populateXMLProductDetails(Map legacyFormMap) throws Exception {

		// Create an entity with ProductDetails
		Set<ProductDetail> productDetails = new HashSet<>();
		ProductDetail productDetail = null;
		String ppmCompleteCoverageName = null;
		String ppmOilType = null;
		String ppmCoverageName = null;
		String[] ppmCoverageArray = null;

		// Create entities with Product Details
		if (legacyFormMap.get("product") != null && !legacyFormMap.get("product").equals(null)
				&& !legacyFormMap.get("product").equals("")) {
			ArrayList<Map> products = (ArrayList) (legacyFormMap.get("product"));
			productDetail = new ProductDetail();
			Map productDetailMap = products.get(0);

			if (((String) ((Map) productDetailMap.get("form")).get("number")) != null
					&& !(((String) ((Map) productDetailMap.get("form")).get("number")).equals(null))
					&& !(((String) ((Map) productDetailMap.get("form")).get("number")).equals("")))
				productDetail.setFormNumber((String) ((Map) productDetailMap.get("form")).get("number"));

			if (productDetailMap.get("type") != null && !(productDetailMap.get("type").equals(null))
					&& !(productDetailMap.get("type").equals("")))
				productDetail.setProductDetailType((String) productDetailMap.get("type"));

			if (productDetailMap.get("cost") == null || productDetailMap.get("cost").equals(null)
					|| productDetailMap.get("cost").equals(""))
				productDetail.setReportedCustomerCost(0.0);
			else
				productDetail.setReportedCustomerCost((Double.parseDouble(productDetailMap.get("cost").toString())));

			if (productDetailMap.get("salesTax") == null || productDetailMap.get("salesTax").equals(null)
					|| productDetailMap.get("salesTax").equals(""))
				productDetail.setSalesTax(0.0);
			else
				productDetail.setSalesTax((Double.parseDouble((String) productDetailMap.get("salesTax"))));

			if (productDetailMap.get("deductibleAmount") == null
					|| productDetailMap.get("deductibleAmount").equals(null)
					|| productDetailMap.get("deductibleAmount").equals(""))
				productDetail.setDeductibleAmount(0.0);
			else
				productDetail
						.setDeductibleAmount((Double.parseDouble((String) productDetailMap.get("deductibleAmount"))));

			// PS-14996 changes (from line no. 807 to 823)

			Set<KeyValuePair> fillerDetails = populateXMLFillers(legacyFormMap);
			String reducingAmt = null;
			String reducingAmtValue = null;
			for (KeyValuePair amt : fillerDetails) {
				if (amt.getKey().equalsIgnoreCase("DeductibleLabel")) {
					reducingAmt = amt.getValue();
					break;
				}
			}
			if (reducingAmt != null) {
				if (reducingAmt.contains("Reducing")) {
					reducingAmtValue = reducingAmt.substring(reducingAmt.indexOf("/") + 2,
							reducingAmt.indexOf("R") - 1);
					productDetail.setReducedDeductible(Double.parseDouble(reducingAmtValue));
				} else {
					productDetail.setReducedDeductible(0.0);
				}
			}

			if (productDetailMap.get("disappearing") != null && !(productDetailMap.get("disappearing").equals(null))
					&& !(productDetailMap.get("disappearing").equals("")))
				productDetail
						.setDisappearing("TRUE".equals(((String) productDetailMap.get("disappearing")).toUpperCase())
								|| "YES".equals(((String) productDetailMap.get("disappearing")).toUpperCase())
								|| "Y".equals(((String) productDetailMap.get("disappearing")).toUpperCase()));

			if (productDetailMap.get("reducing") != null && !(productDetailMap.get("reducing").equals(null))
					&& !(productDetailMap.get("reducing").equals("")))
				productDetail.setReducing("TRUE".equals(((String) productDetailMap.get("reducing")).toUpperCase())
						|| "YES".equals(((String) productDetailMap.get("reducing")).toUpperCase())
						|| "Y".equals(((String) productDetailMap.get("reducing")).toUpperCase()));

			if (productDetailMap.get("coverageName") != null && !(productDetailMap.get("coverageName").equals(null))
					&& !(productDetailMap.get("coverageName").equals("")))
				productDetail.setCoverageName((String) productDetailMap.get("coverageName"));

			if (productDetailMap.get("termMonths") == null || productDetailMap.get("termMonths").equals(null)
					|| productDetailMap.get("termMonths").equals(""))
				productDetail.setTermMonths(0);
			else
				productDetail.setTermMonths(Integer.parseInt(productDetailMap.get("termMonths").toString()));

			if (productDetailMap.get("termMiles") == null || productDetailMap.get("termMiles").equals(null)
					|| productDetailMap.get("termMiles").equals(""))
				productDetail.setTermDistance(0);
			else
				productDetail.setTermDistance(Integer.parseInt(productDetailMap.get("termMiles").toString()));

			if (productDetailMap.get("ppmIntervalMiles") == null
					|| productDetailMap.get("ppmIntervalMiles").equals(null)
					|| productDetailMap.get("ppmIntervalMiles").equals(""))
				productDetail.setPpmIntervalMiles(0);
			else
				productDetail
						.setPpmIntervalMiles(Integer.parseInt(productDetailMap.get("ppmIntervalMiles").toString()));
			productDetail.setAgreementStatus((String) ((Map) productDetailMap.get("form")).get("type"));

			if (productDetailMap.get("type").equals("PPM")) {
				if (productDetailMap.get("deductibleAmount") == null
						|| productDetailMap.get("deductibleAmount").equals(null)
						|| productDetailMap.get("deductibleAmount").equals(""))
					productDetail.setNumberOfServices(0);
				else
					productDetail
							.setNumberOfServices(Integer.parseInt(productDetailMap.get("deductibleAmount").toString()));
			}

			if (productDetailMap.get("type").equals("PPM")) {
				if (productDetailMap.get("deductibleAmount") == null
						|| productDetailMap.get("deductibleAmount").equals(null)
						|| productDetailMap.get("deductibleAmount").equals(""))
					productDetail.setServiceInterval(0);
				else
					productDetail
							.setServiceInterval(Integer.parseInt(productDetailMap.get("deductibleAmount").toString()));
			}

			if (productDetailMap.get("totalAllowableMilesContract") == null
					|| productDetailMap.get("totalAllowableMilesContract").equals(null)
					|| productDetailMap.get("totalAllowableMilesContract").equals(""))
				productDetail.setTotalAllowableMilesContract(0.0);
			else
				productDetail.setTotalAllowableMilesContract(
						(Double.parseDouble(productDetailMap.get("totalAllowableMilesContract").toString())));
			productDetail.setProductClass((String) productDetailMap.get("productClass"));
			productDetail.setSystemPin((String) productDetailMap.get("systemPin"));

			if (productDetailMap.get("coverageCode") != null && !(productDetailMap.get("coverageCode").equals(null))
					&& !(productDetailMap.get("coverageCode").equals("")))
				productDetail.setCoverageCode((String) productDetailMap.get("coverageCode"));
			if (productDetailMap.get("type").equals("PPM")) {
				if (productDetailMap.get("coverageName") != null && !(productDetailMap.get("coverageName").equals(null))
						&& !(productDetailMap.get("coverageName").equals("")))
					ppmCompleteCoverageName = (String) productDetailMap.get("coverageName");

				if(ppmCompleteCoverageName.contains("-") && !ppmCompleteCoverageName.contains("Class")) {
				ppmCoverageArray = ppmCompleteCoverageName.split("-");

				if (ppmCoverageArray.length == 3) {
					ppmCoverageName = ppmCoverageArray[1].trim();
					ppmOilType = ppmCoverageArray[2].trim();
				} else {
					ppmCoverageName = ppmCoverageArray[0].trim();
					ppmOilType = ppmCoverageArray[1].trim();
				}
				productDetail.setCoverageName(ppmCoverageName);
				productDetail.setOilType(ppmOilType);
				}
			}
			productDetails.add(productDetail);
		}

		// Create entities with Product Details
		if (((ArrayList) (legacyFormMap.get("product"))).size() > 1) {
			// System.out.println("size of list "+(((ArrayList)
			// (legacyFormMap.get("product"))).size()));
			if (legacyFormMap.get("product") != null && !legacyFormMap.get("product").equals(null)
					&& !legacyFormMap.get("product").equals("")) {
				ArrayList<Map> products = (ArrayList) (legacyFormMap.get("product"));
				productDetail = new ProductDetail();
				Map productDetailMap = products.get(1);

				if (((String) ((Map) productDetailMap.get("form")).get("number")) != null
						&& !(((String) ((Map) productDetailMap.get("form")).get("number")).equals(null))
						&& !(((String) ((Map) productDetailMap.get("form")).get("number")).equals("")))
					productDetail.setFormNumber((String) ((Map) productDetailMap.get("form")).get("number"));

				if (productDetailMap.get("type") != null && !(productDetailMap.get("type").equals(null))
						&& !(productDetailMap.get("type").equals("")))
					productDetail.setProductDetailType((String) productDetailMap.get("type"));

				if (productDetailMap.get("cost") == null || productDetailMap.get("cost").equals(null)
						|| productDetailMap.get("cost").equals(""))
					productDetail.setReportedCustomerCost(0.0);
				else
					productDetail
							.setReportedCustomerCost((Double.parseDouble(productDetailMap.get("cost").toString())));

				if (productDetailMap.get("salesTax") == null || productDetailMap.get("salesTax").equals(null)
						|| productDetailMap.get("salesTax").equals(""))
					productDetail.setSalesTax(0.0);
				else
					productDetail.setSalesTax((Double.parseDouble((String) productDetailMap.get("salesTax"))));
				if (productDetailMap.get("type").equals("PPM")) {
					if (productDetailMap.get("deductibleAmount") == null
							|| productDetailMap.get("deductibleAmount").equals(null)
							|| productDetailMap.get("deductibleAmount").equals(""))
						productDetail.setNumberOfServices(0);
					else
						productDetail
								.setNumberOfServices(Integer.parseInt(productDetailMap.get("deductibleAmount").toString()));
				}

				if (productDetailMap.get("type").equals("PPM")) {
					if (productDetailMap.get("deductibleAmount") == null
							|| productDetailMap.get("deductibleAmount").equals(null)
							|| productDetailMap.get("deductibleAmount").equals(""))
						productDetail.setServiceInterval(0);
					else
						productDetail
								.setServiceInterval(Integer.parseInt(productDetailMap.get("deductibleAmount").toString()));
				}

				if (productDetailMap.get("deductibleAmount") == null
						|| productDetailMap.get("deductibleAmount").equals(null)
						|| productDetailMap.get("deductibleAmount").equals(""))
					productDetail.setDeductibleAmount(0.0);
				else
					productDetail.setDeductibleAmount(
							(Double.parseDouble((String) productDetailMap.get("deductibleAmount"))));

				if (productDetailMap.get("reducingAmount") == null
						|| productDetailMap.get("reducingAmount").equals(null)
						|| productDetailMap.get("reducingAmount").equals(""))
					productDetail.setReducedDeductible(0.0);
				else
					productDetail.setReducedDeductible(
							(Double.parseDouble((String) productDetailMap.get("reducingAmount"))));

				if (productDetailMap.get("disappearing") != null && !(productDetailMap.get("disappearing").equals(null))
						&& !(productDetailMap.get("disappearing").equals("")))
					productDetail.setDisappearing(
							"TRUE".equals(((String) productDetailMap.get("disappearing")).toUpperCase())
									|| "YES".equals(((String) productDetailMap.get("disappearing")).toUpperCase())
									|| "Y".equals(((String) productDetailMap.get("disappearing")).toUpperCase()));

				if (productDetailMap.get("reducing") != null && !(productDetailMap.get("reducing").equals(null))
						&& !(productDetailMap.get("reducing").equals("")))
					productDetail.setReducing("TRUE".equals(((String) productDetailMap.get("reducing")).toUpperCase())
							|| "YES".equals(((String) productDetailMap.get("reducing")).toUpperCase())
							|| "Y".equals(((String) productDetailMap.get("reducing")).toUpperCase()));

				if (productDetailMap.get("coverageName") != null && !(productDetailMap.get("coverageName").equals(null))
						&& !(productDetailMap.get("coverageName").equals("")))
					productDetail.setCoverageName((String) productDetailMap.get("coverageName"));

				if (productDetailMap.get("termMonths") == null || productDetailMap.get("termMonths").equals(null)
						|| productDetailMap.get("termMonths").equals(""))
					productDetail.setTermMonths(0);
				else
					productDetail.setTermMonths(Integer.parseInt(productDetailMap.get("termMonths").toString()));

				if (productDetailMap.get("termMiles") == null || productDetailMap.get("termMiles").equals(null)
						|| productDetailMap.get("termMiles").equals(""))
					productDetail.setTermDistance(0);
				else
					productDetail.setTermDistance(Integer.parseInt(productDetailMap.get("termMiles").toString()));

				if (productDetailMap.get("ppmIntervalMiles") == null
						|| productDetailMap.get("ppmIntervalMiles").equals(null)
						|| productDetailMap.get("ppmIntervalMiles").equals(""))
					productDetail.setPpmIntervalMiles(0);
				else
					productDetail
							.setPpmIntervalMiles(Integer.parseInt(productDetailMap.get("ppmIntervalMiles").toString()));
				productDetail.setAgreementStatus((String) ((Map) productDetailMap.get("form")).get("type"));

				if (productDetailMap.get("totalAllowableMilesContract") == null
						|| productDetailMap.get("totalAllowableMilesContract").equals(null)
						|| productDetailMap.get("totalAllowableMilesContract").equals(""))
					productDetail.setTotalAllowableMilesContract(0.0);
				else
					productDetail.setTotalAllowableMilesContract(
							(Double.parseDouble(productDetailMap.get("totalAllowableMilesContract").toString())));
				productDetail.setProductClass((String) productDetailMap.get("productClass"));
				productDetail.setSystemPin((String) productDetailMap.get("systemPin"));

				if (productDetailMap.get("coverageCode") != null && !(productDetailMap.get("coverageCode").equals(null))
						&& !(productDetailMap.get("coverageCode").equals("")))
					productDetail.setCoverageCode((String) productDetailMap.get("coverageCode"));

				if (productDetailMap.get("type").equals("PPM")) {
					if (productDetailMap.get("coverageName") != null && !(productDetailMap.get("coverageName").equals(null))
							&& !(productDetailMap.get("coverageName").equals("")))
						ppmCompleteCoverageName = (String) productDetailMap.get("coverageName");
					if(ppmCompleteCoverageName.contains("-")) {
					ppmCoverageArray = ppmCompleteCoverageName.split("-");

					if (ppmCoverageArray.length == 3) {
						ppmCoverageName = ppmCoverageArray[1];
						ppmOilType = ppmCoverageArray[2];
					} else {
						ppmCoverageName = ppmCoverageArray[0];
						ppmOilType = ppmCoverageArray[1];
					}
					productDetail.setCoverageName(ppmCoverageName);
					productDetail.setOilType(ppmOilType);
					}
				}

				productDetails.add(productDetail);
			}
		}

		return productDetails;
	}

	private FormRequest populateJsonRequestData(Map fswFormMap, FormRequest formRequest, String formsMessageId,
			Boolean isRequestTypeJSON, String expirationType) throws Exception {

		// Create an entity with Dealer information
		Account account = new Account();
		account = populateJSONAccountDetails(fswFormMap);
		formRequest.setAccount(account);
		formRequest.setQuoteId((String) (fswFormMap.get("quoteId")));
		formRequest.setFormsMessageId(formsMessageId);
		formRequest.setExpirationType(expirationType);
		formRequest.setIsRequestTypeJSON(isRequestTypeJSON);
		formRequest.setFolderName((String) (fswFormMap.get("folderName")));

		// Create an entity with Vehicle information
		Vehicle vehicle = new Vehicle();
		vehicle = populateJSONVehicleDetails(fswFormMap);
		formRequest.setVehicle(vehicle);
		formRequest.setContractNumber((String) (fswFormMap.get("contractNumber")));
		CallingApp callingApp = new CallingApp();
		callingApp.setSourceSystem((String) (fswFormMap.get("sourceOriginator")));
		formRequest.setCallingApp(callingApp);

		// Create entities with Product Details
		Set<ProductDetail> productDetails = new HashSet<>();
		productDetails = populateJSONProductDetails(fswFormMap);
		formRequest.setProductDetails(productDetails);

		// Create entities with Finance Details
		FinanceDetails finance = new FinanceDetails();
		finance = populateJSONFinanceDetails(fswFormMap);
		formRequest.setFinanceDetails(finance);

		// Buyer Details Start
		Contact buyer = new Contact();
		buyer = populateJSONBuyerDetails(fswFormMap);
		formRequest.setBuyer(buyer);

		// CoBuyer Details Start
		Contact coBuyer = new Contact();
		coBuyer = populateJSONCoBuyerDetails(fswFormMap);
		formRequest.setCoBuyer(coBuyer);

		// LienHolder Details Start
		Contact lienHolder = new Contact();
		lienHolder = populateJSONLienHolderDetails(fswFormMap);
		formRequest.setLienHolder(lienHolder);

		// FSM Details
		Map fsm = new HashMap();
		fsm = (HashMap) fswFormMap.get("fsm");
		formRequest.setFsmFirstName((String) fsm.get("fsmFirstName"));
		formRequest.setFsmLastName((String) fsm.get("fsmLastName"));

		// insurance Details
		Insurance insurance = new Insurance();
		insurance = populateJSONInsuranceDetails(fswFormMap);
		formRequest.setInsurance(insurance);
		
		//broker details
		BrokerDetails brokerDetail = new BrokerDetails();
		brokerDetail = populateJSONBrokerDetails(fswFormMap);
		formRequest.setBrokerDetails(brokerDetail);
				

		formRequest.setOutputFileName(formRequest.getOutputFileName());

		
		return formRequest;
	}

	// Populate JSON Account Details in form Request
	private Account populateJSONAccountDetails(Map fswFormMap) {
		Account account = new Account();

		if (fswFormMap.get("account") != null && !fswFormMap.get("account").equals(null)
				&& !fswFormMap.get("account").equals("")) {
			Map accountDetails = new HashMap();
			accountDetails = (HashMap) fswFormMap.get("account");
			account.setAccountNumber((String) accountDetails.get("accountNumber"));
			account.setName((String) accountDetails.get("name"));
			Address dealerAddress = new Address();
			dealerAddress.setAddress1((String) accountDetails.get("address1"));
			dealerAddress.setAddress2((String) accountDetails.get("address2"));
			dealerAddress.setPostalCode((String) accountDetails.get("postalCode"));

			//substring of postalcode for Enterprise forms alone
			/*if(accountDetails.get("accountNumber") != null &&  !accountDetails.get("accountNumber").equals(null) && !accountDetails.get("accountNumber").equals("") && accountDetails.get("accountNumber").toString().startsWith("ENT-") &&
			   accountDetails.get("postalCode") != null && ! accountDetails.get("postalCode").equals(null) && !accountDetails.get("postalCode").equals("")   && ((String) accountDetails.get("postalCode")).length() > 5)
			{
			   dealerAddress.setPostalCode(((String) accountDetails.get("postalCode")).substring(0, 5));
			}else
			   dealerAddress.setPostalCode((String) accountDetails.get("postalCode"));*/

			dealerAddress.setCity((String) accountDetails.get("city"));
			dealerAddress.setStateCode((String) accountDetails.get("stateCode"));
			dealerAddress.setCountryCode((String) accountDetails.get("countryCode"));
			account.setAddress(dealerAddress);

			// Create Dealers Phone information
			Phone dealerPhone = new Phone();
			dealerPhone.setNumber((String) accountDetails.get("workPhoneNumber"));
			account.setPhone(dealerPhone);
			account.setSalesman((String) accountDetails.get("salesman"));
			account.setRfc((String) accountDetails.get("rfc"));
			account.setPaymentType((String) accountDetails.get("paymentType"));
			account.setInvoiceNumber((String) accountDetails.get("invoiceNumber"));
			account.setSalesManager((String) accountDetails.get("salesManager"));
		}
		return account;
	}

	private FinanceDetails populateJSONFinanceDetails(Map fswFormMap) throws Exception {
		FinanceDetails financeDetails = new FinanceDetails();
		if (fswFormMap.get("financing") != null && !fswFormMap.get("financing").equals(null)
				&& !fswFormMap.get("financing").equals("")) {
			Map financeDetailsMap = (Map) fswFormMap.get("financing");
			financeDetails.setFinanceType((String) financeDetailsMap.get("financeType"));
			try{
				financeDetails.setFinancingTypeEnum(FinancingType.find((String) financeDetailsMap.get("financeType")));
			}
			catch (Exception exception){
				System.out.println("no valid finance type found");
				
			}
			financeDetails.setLoanNumber((String) financeDetailsMap.get("loanNumber"));
			financeDetails.setGapIdNumber((String) financeDetailsMap.get("gapIdNumber"));

			if (financeDetailsMap.get("baloonResidualValue") == null
					|| financeDetailsMap.get("baloonResidualValue").equals(null)
					|| financeDetailsMap.get("baloonResidualValue").equals(""))
				financeDetails.setBaloonResidualValue(0.0);
			else
				financeDetails.setBaloonResidualValue(
						(Double.parseDouble(financeDetailsMap.get("baloonResidualValue").toString())));

			if (financeDetailsMap.get("annualPercentageRate") == null
					|| financeDetailsMap.get("annualPercentageRate").equals(null)
					|| financeDetailsMap.get("annualPercentageRate").equals(""))
				financeDetails.setApr(0.0);
			else
				financeDetails.setApr((Double.parseDouble(financeDetailsMap.get("annualPercentageRate").toString())));

			if (financeDetailsMap.get("financedAmount") == null || financeDetailsMap.get("financedAmount").equals(null)
					|| financeDetailsMap.get("financedAmount").equals(""))
				financeDetails.setFinancedAmount(0.0);
			else
				financeDetails
						.setFinancedAmount((Double.parseDouble(financeDetailsMap.get("financedAmount").toString())));

			if (financeDetailsMap.get("loanTotalPayment") == null
					|| financeDetailsMap.get("loanTotalPayment").equals(null)
					|| financeDetailsMap.get("loanTotalPayment").equals(""))
				financeDetails.setPaymentAmount(0.0);
			else
				financeDetails
						.setPaymentAmount((Double.parseDouble(financeDetailsMap.get("loanTotalPayment").toString())));

			if (financeDetailsMap.get("loanMonthlyPayment") == null
					|| financeDetailsMap.get("loanMonthlyPayment").equals(null)
					|| financeDetailsMap.get("loanMonthlyPayment").equals(""))
				financeDetails.setMonthlyPaymentAmount(0.0);
			else
				financeDetails.setMonthlyPaymentAmount(
						(Double.parseDouble(financeDetailsMap.get("loanMonthlyPayment").toString())));

			if (financeDetailsMap.get("msrp") == null || financeDetailsMap.get("msrp").equals(null)
					|| financeDetailsMap.get("msrp").equals(""))
				financeDetails.setMsrp(0.0);
			else
				financeDetails.setMsrp((Double.parseDouble(financeDetailsMap.get("msrp").toString())));

			if (financeDetailsMap.get("nada") == null || financeDetailsMap.get("nada").equals(null)
					|| financeDetailsMap.get("nada").equals(""))
					{
						financeDetails.setNada(0.0);
					}
			else
				financeDetails.setNada((Double.parseDouble(financeDetailsMap.get("nada").toString())));

			if (financeDetailsMap.get("leaseCapAmount") == null || financeDetailsMap.get("leaseCapAmount").equals(null)
					|| financeDetailsMap.get("leaseCapAmount").equals(""))
				financeDetails.setLeaseCapAmount(0.0);
			else
				financeDetails
						.setLeaseCapAmount((Double.parseDouble(financeDetailsMap.get("leaseCapAmount").toString())));

			if (financeDetailsMap.get("grossCapCost") == null || financeDetailsMap.get("grossCapCost").equals(null)
					|| financeDetailsMap.get("grossCapCost").equals(""))
				financeDetails.setGrossCapCost(0.0);
			else
				financeDetails.setGrossCapCost((Double.parseDouble(financeDetailsMap.get("grossCapCost").toString())));

			if (financeDetailsMap.get("totalAllowableMilesContract") == null
					|| financeDetailsMap.get("totalAllowableMilesContract").equals(null)
					|| financeDetailsMap.get("totalAllowableMilesContract").equals(""))
				financeDetails.setTotalAllowableMilesContract(0.0);
			else
				financeDetails.setTotalAllowableMilesContract(
						(Double.parseDouble(financeDetailsMap.get("totalAllowableMilesContract").toString())));

			if (financeDetailsMap.get("numberOfPayments") == null
					|| financeDetailsMap.get("numberOfPayments").equals(null)
					|| financeDetailsMap.get("numberOfPayments").equals(""))
				financeDetails.setNumberOfPayments(0);
			else
				financeDetails
						.setNumberOfPayments(Integer.parseInt(financeDetailsMap.get("numberOfPayments").toString()));

			if (financeDetailsMap.get("numberOfAdvPayments") == null
					|| financeDetailsMap.get("numberOfAdvPayments").equals(null)
					|| financeDetailsMap.get("numberOfAdvPayments").equals(""))
				financeDetails.setNumberOfAdvPayments(0);
			else
				financeDetails.setNumberOfAdvPayments(
						Integer.parseInt(financeDetailsMap.get("numberOfAdvPayments").toString()));

			if (financeDetailsMap.get("numberOfSkipPayments") == null
					|| financeDetailsMap.get("numberOfSkipPayments").equals(null)
					|| financeDetailsMap.get("numberOfSkipPayments").equals(""))
				financeDetails.setNumberOfSkipPayments(0);
			else
				financeDetails.setNumberOfSkipPayments(
						Integer.parseInt(financeDetailsMap.get("numberOfSkipPayments").toString()));

			if (financeDetailsMap.get("period") == null || financeDetailsMap.get("period").equals(null)
					|| financeDetailsMap.get("period").equals(""))
				financeDetails.setPeriod(0);
			else
				financeDetails.setPeriod(Integer.parseInt(financeDetailsMap.get("period").toString()));

			if (financeDetailsMap.get("startDate") != null && !financeDetailsMap.get("startDate").equals("")) {
				financeDetails.setStartDate(
						new SimpleDateFormat("yyyy-MM-dd").parse((String) financeDetailsMap.get("startDate")));
			}

			if (financeDetailsMap.get("interestStartDate") != null
					&& !financeDetailsMap.get("interestStartDate").equals("")) {
				financeDetails.setInterestStartDate(
						new SimpleDateFormat("yyyy-MM-dd").parse((String) financeDetailsMap.get("interestStartDate")));
			}

			if (financeDetailsMap.get("dateOfFirstPayment") != null
					&& !financeDetailsMap.get("dateOfFirstPayment").equals("")) {
				financeDetails.setFirstPaymentDate(
						new SimpleDateFormat("yyyy-MM-dd").parse((String) financeDetailsMap.get("dateOfFirstPayment")));
			}

			if (financeDetailsMap.get("endDate") != null && !financeDetailsMap.get("endDate").equals("")) {
				financeDetails.setEndDate(
						new SimpleDateFormat("yyyy-MM-dd").parse((String) financeDetailsMap.get("endDate")));
			}

			if (financeDetailsMap.get("scheduledTerminationDate") != null
					&& !financeDetailsMap.get("scheduledTerminationDate").equals("")) {
				financeDetails.setScheduledTerminationDate(new SimpleDateFormat("yyyy-MM-dd")
						.parse((String) financeDetailsMap.get("scheduledTerminationDate")));
			}
			if (financeDetailsMap.get("isPaymentPlan") != null && !financeDetailsMap.get("isPaymentPlan").equals(null)
					&& !financeDetailsMap.get("isPaymentPlan").equals(""))
				financeDetails.setIsPaymentPlan((String) financeDetailsMap.get("isPaymentPlan"));
			
			if (financeDetailsMap.get("installmentPaymentTerm") != null && !financeDetailsMap.get("installmentPaymentTerm").equals(null)
					&& !financeDetailsMap.get("installmentPaymentTerm").equals(""))
				financeDetails.setInstallmentPaymentTerm(Integer.parseInt(financeDetailsMap.get("installmentPaymentTerm").toString()));
			
			if (financeDetailsMap.get("installmentPaymentFrequency") != null && !financeDetailsMap.get("installmentPaymentFrequency").equals(null)
					&& !financeDetailsMap.get("installmentPaymentFrequency").equals(""))
				financeDetails.setInstallmentPaymentFrequency((String) financeDetailsMap.get("installmentPaymentFrequency"));


//set PaymentDueDay
			
			if (financeDetailsMap.get("startDate") != null && !financeDetailsMap.get("startDate").equals("")
					&& (financeDetailsMap.get("paymentDueDay") == null
							|| financeDetailsMap.get("paymentDueDay").equals(null)
							|| financeDetailsMap.get("paymentDueDay").equals(""))) {
				String date = this.formatDate(
						new SimpleDateFormat("yyyy-MM-dd").parse((String) financeDetailsMap.get("startDate")));
				String[] out = date.split("/");
				int startDay = Integer.parseInt(out[1]);
				financeDetails.setPaymentDueDay(getOrdinalValue(startDay));
			}
			else if (financeDetailsMap.get("paymentDueDay") != null && !financeDetailsMap.get("paymentDueDay").equals(null) && !financeDetailsMap.get("paymentDueDay").equals("")) {
			financeDetails.setPaymentDueDay(getOrdinalValue(Integer.parseInt((String) financeDetailsMap.get("paymentDueDay").toString())));
				
			}
			else {
				DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");	
				LocalDate localDate = LocalDate.parse(LocalDate.now().toString(),myFormatObj);
				int startDay=  localDate.getDayOfMonth();
				financeDetails.setPaymentDueDay(getOrdinalValue(startDay));
			}
			
			if (financeDetailsMap.get("installmentDownPayment") == null || financeDetailsMap.get("installmentDownPayment").equals(null)
					|| financeDetailsMap.get("installmentDownPayment").equals(""))
				financeDetails.setInstallmentDownPayment(0.00);
			else
				financeDetails.setInstallmentDownPayment((Double.parseDouble(financeDetailsMap.get("installmentDownPayment").toString())));

			if (financeDetailsMap.get("installmentAmount") == null || financeDetailsMap.get("installmentAmount").equals(null)
					|| financeDetailsMap.get("installmentAmount").equals(""))
				financeDetails.setInstallmentAmount(0.00);
			else
				financeDetails.setInstallmentAmount((Double.parseDouble(financeDetailsMap.get("installmentAmount").toString())));
		}
		return financeDetails;
	}
	private String formatDate(Date date) 
	{
		return date != null ? this.dateFormat.format(date) : null;
	}

	// set ordinal numbers like 1st,2nd,3rd,11th
	public String getOrdinalValue(int day) {

		int modTen = day % 10;
		int modeHundred = day % 100;
		String value = "";

		if (modTen == 1 && modeHundred != 11) {
			value = day + "st";
		} else if (modTen == 2 && modeHundred != 12) {
			value = day + "nd";
		} else if (modTen == 3 && modeHundred != 13) {
			value = day + "rd";
		} else {
			value = day + "th";
		}
		return value;
	}
	// Populate JSON Vehicle Details in form Request
	private Vehicle populateJSONVehicleDetails(Map fswFormMap) throws Exception {
		Vehicle vehicle = new Vehicle();
		if (fswFormMap.get("vehicle") != null && !fswFormMap.get("vehicle").equals(null)
				&& !fswFormMap.get("vehicle").equals("")) {
			Map vehicleDetails = new HashMap();
			vehicleDetails = (Map) fswFormMap.get("vehicle");
			vehicle.setModel((String) vehicleDetails.get("model"));
			vehicle.setMake((String) vehicleDetails.get("make"));
			vehicle.setVehicleClass((String) vehicleDetails.get("vehicleClass"));
			vehicle.setVehicleType((String) vehicleDetails.get("vehicleType"));
			vehicle.setChassisWarrantyTermMonth(chassisTermMonth);
			vehicle.setManufacturerWarrantyTermMonth(manufacturerTermMonth);
			vehicle.setVehicleRegistrationNumber((String) vehicleDetails.get("vehicleRegistrationNumber"));

			if (vehicleDetails.get("inServiceDate") != null && !vehicleDetails.get("inServiceDate").equals(null)
					&& !vehicleDetails.get("inServiceDate").equals("")) {
				vehicle.setInServiceDate(
						new SimpleDateFormat("yyyy-MM-dd").parse((String) vehicleDetails.get("inServiceDate")));
			}
			vehicle.setVin((String) vehicleDetails.get("VIN"));

			if (vehicleDetails.get("vehicleYear") != null && !vehicleDetails.get("vehicleYear").equals(null)
					&& !vehicleDetails.get("vehicleYear").equals("")) {
				vehicle.setYear(String.valueOf(vehicleDetails.get("vehicleYear")));
			}
			if (vehicleDetails.get("odometer") != null && !vehicleDetails.get("odometer").equals(null)
					&& !vehicleDetails.get("odometer").equals(""))
				vehicle.setOdometer(Integer.parseInt(vehicleDetails.get("odometer").toString()));
			//set value for trim
			if (vehicleDetails.get("trim") != null && ! vehicleDetails.get("trim").equals(null)
					&& !vehicleDetails.get("trim").equals(""))
			{
				vehicle.setTrim(vehicleDetails.get("trim").toString());
			}

			vehicle.setTurbo(StringUtils.containsIgnoreCase(((String) (vehicleDetails.get("turbo"))), "Y"));
			vehicle.setCar(
					StringUtils.containsIgnoreCase(((String) (vehicleDetails.get("vehicleType"))), "C"));
			vehicle.setTruck(
					StringUtils.containsIgnoreCase(((String) (vehicleDetails.get("vehicleType"))), "T"));

			//set value for  vehiclepurchase price
			if (vehicleDetails.get("vehiclePurchasePrice") != null && ! vehicleDetails.get("vehiclePurchasePrice").equals(null)
					&& !vehicleDetails.get("vehiclePurchasePrice").equals(""))
			{
				vehicle.setVehiclePurchasePrice(Double.parseDouble(vehicleDetails.get("vehiclePurchasePrice").toString()));
			}

			if (vehicleDetails.get("vehicleNewOrUsed") == null || vehicleDetails.get("vehicleNewOrUsed").equals(null)
					|| vehicleDetails.get("vehicleNewOrUsed").equals(""))
				vehicle.setNewCar(true);
			else if (StringUtils.containsIgnoreCase(((String) (vehicleDetails.get("vehicleNewOrUsed"))), "U"))
				vehicle.setNewCar(false);
			else
				vehicle.setNewCar(true);

			if (vehicleDetails.get("commercialUsage") == null || vehicleDetails.get("commercialUsage").equals(null)
					|| vehicleDetails.get("commercialUsage").equals(""))
				vehicle.setCommercialUsage(false);
			else if (StringUtils.containsIgnoreCase(((String) (vehicleDetails.get("commercialUsage"))), "N"))
				vehicle.setCommercialUsage(false);
			else
				vehicle.setCommercialUsage(true);

			if (vehicleDetails.get("hasNavigation") == null || vehicleDetails.get("hasNavigation").equals(null)
					|| vehicleDetails.get("hasNavigation").equals(""))
				vehicle.setNavigation(false);
			else if (StringUtils.containsIgnoreCase(((String) (vehicleDetails.get("hasNavigation"))), "N"))
				vehicle.setNavigation(false);
			else
				vehicle.setNavigation(true);

			// Setting FuelType based on criteria
			String fuelType = ((String) vehicleDetails.get("fuelType"));

			if (StringUtils.containsIgnoreCase(fuelType, "Y") || StringUtils.containsIgnoreCase(fuelType, "D"))
				vehicle.setDiesel(true);

			if (StringUtils.containsIgnoreCase(fuelType, "B") || StringUtils.containsIgnoreCase(fuelType, "Y"))
				vehicle.setHybrid(true);
			
			if(((HashMap) (fswFormMap.get("account"))).get("accountNumber").toString().startsWith("ENT-")){
				if (StringUtils.containsIgnoreCase(fuelType, "E"))
					vehicle.setHybrid(true);
			}			
			String driveTrain = ((String) (((Map) fswFormMap.get("vehicle")).get("driveTrain")));

			// Set 4x4 in case driveTrain is AWD, 4FD or 4RD
			if (StringUtils.containsIgnoreCase(driveTrain, "AWD") || StringUtils.containsIgnoreCase(driveTrain, "4FD")
					|| StringUtils.containsIgnoreCase(driveTrain, "4RD"))
				vehicle.setFourByFour(true);
			if (vehicleDetails.get("vehiclePurchaseDate") != null
					&& !vehicleDetails.get("vehiclePurchaseDate").equals("")) {
				vehicle.setVehiclePurchaseDate(
						new SimpleDateFormat("yyyy-MM-dd").parse((String) vehicleDetails.get("vehiclePurchaseDate")));
			}

			vehicle.setVehicleNewOrUsed((String) vehicleDetails.get("vehicleNewOrUsed"));

			if (vehicleDetails.get("registrationDateEMDCS") != null
					&& !vehicleDetails.get("registrationDateEMDCS").equals("")) {
				vehicle.setRegistrationDateEMDCS(
						new SimpleDateFormat("yyyy-MM-dd").parse((String) vehicleDetails.get("registrationDateEMDCS")));
			}

			if (vehicleDetails.get("coverageStartMileage") != null
					&& !vehicleDetails.get("coverageStartMileage").equals("")
					&& !vehicleDetails.get("coverageStartMileage").equals(null)) {
				vehicle.setCoverageStartMileage(
						Double.parseDouble(vehicleDetails.get("coverageStartMileage").toString()));
			}

			vehicle.setTransmission((String) vehicleDetails.get("transmission"));

			if (vehicleDetails.get("ManufacturerWarrantyEndDate") != null && !vehicleDetails.get("ManufacturerWarrantyEndDate").equals(null)
					&& !vehicleDetails.get("ManufacturerWarrantyEndDate").equals("")) {
				vehicle.setManufacturerWarrantyEndDate(
						new SimpleDateFormat("yyyy-MM-dd").parse((String) vehicleDetails.get("ManufacturerWarrantyEndDate")));
			}


			if (vehicleDetails.get("ManufacturerWarrantyEndMileage") != null && !vehicleDetails.get("ManufacturerWarrantyEndMileage").equals(null)
					&& !vehicleDetails.get("ManufacturerWarrantyEndMileage").equals("")) {
				vehicle.setManufacturerWarrantyEndMileage(Double.parseDouble(vehicleDetails.get("ManufacturerWarrantyEndMileage").toString()));
				}

			if (vehicleDetails.get("coverageStartDate") != null
					&& !vehicleDetails.get("coverageStartDate").equals("")) {
				vehicle.setCoverageStartDate(
						new SimpleDateFormat("yyyy-MM-dd").parse((String) vehicleDetails.get("coverageStartDate")));
			}

			if (vehicleDetails.get("expirationMileage") != null
					&& !vehicleDetails.get("expirationMileage").equals("")
					&& !vehicleDetails.get("expirationMileage").equals(null)) {
				vehicle.setExpirationMileage(
						Double.parseDouble(vehicleDetails.get("expirationMileage").toString()));
			}

			if (vehicleDetails.get("contractStartDate") != null
					&& !vehicleDetails.get("contractStartDate").equals("")) {
				vehicle.setContractStartDate(
						new SimpleDateFormat("yyyy-MM-dd").parse((String) vehicleDetails.get("contractStartDate")));
			}

			if (vehicleDetails.get("totalBilledCost") != null
					&& !vehicleDetails.get("totalBilledCost").equals("")
					&& !vehicleDetails.get("totalBilledCost").equals(null)) {
				vehicle.setTotalBilledCost(
						Double.parseDouble(vehicleDetails.get("totalBilledCost").toString()));
			}

			if (vehicleDetails.get("totalRetailCost") != null
					&& !vehicleDetails.get("totalRetailCost").equals("")
					&& !vehicleDetails.get("totalRetailCost").equals(null)) {
				vehicle.setTotalRetailCost(
						Double.parseDouble(vehicleDetails.get("totalRetailCost").toString()));
			}

			if (vehicleDetails.get("retailPrice") != null
					&& !vehicleDetails.get("retailPrice").equals("")
					&& !vehicleDetails.get("retailPrice").equals(null)) {
				vehicle.setRetailPrice(
						Double.parseDouble(vehicleDetails.get("retailPrice").toString()));
			}

			if (vehicleDetails.get("customerNumber") != null
					&& !vehicleDetails.get("customerNumber").equals("")
					&& !vehicleDetails.get("customerNumber").equals(null)) {
				vehicle.setCustomerNumber
						(vehicleDetails.get("customerNumber").toString());
			}
			if (vehicleDetails.get("purchasePricesales") != null
					&& !vehicleDetails.get("purchasePricesales").equals("")
					&& !vehicleDetails.get("purchasePricesales").equals(null)) {
				vehicle.setPurchasePricesales(
						Double.parseDouble(vehicleDetails.get("purchasePricesales").toString()));
			}
			if (vehicleDetails.get("engineNumber") != null
					&& !vehicleDetails.get("engineNumber").equals("")
					&& !vehicleDetails.get("engineNumber").equals(null)) {
				vehicle.setEngineNumber(
						(vehicleDetails.get("engineNumber").toString()));
			}

			if (vehicleDetails.get("expirationDate") != null
					&& !vehicleDetails.get("expirationDate").equals("")) {
				vehicle.setExpirationDate(
						new SimpleDateFormat("yyyy-MM-dd").parse((String) vehicleDetails.get("expirationDate")));
			}

			if (vehicleDetails.get("createdDate") != null
					&& !vehicleDetails.get("createdDate").equals("")) {
				vehicle.setCreatedDate(
						new SimpleDateFormat("yyyy-MM-dd").parse((String) vehicleDetails.get("createdDate")));
			}

			if (vehicleDetails.get("customerCost") != null
					&& !vehicleDetails.get("customerCost").equals("")
					&& !vehicleDetails.get("customerCost").equals(null)) {
				vehicle.setCustomerCost(
						Double.parseDouble(vehicleDetails.get("customerCost").toString()));
			}

			if (vehicleDetails.get("gapProductTenure") != null && !(vehicleDetails.get("gapProductTenure").equals(null))
					&& !(vehicleDetails.get("gapProductTenure").equals(""))) {
				vehicle.setGapProductTenure((String) vehicleDetails.get("gapProductTenure"));
			}


		}
		return vehicle;
	}

    // Populate JSON Insurance Details in form Request
		private Insurance populateJSONInsuranceDetails(Map fswFormMap) {
			Insurance insurance = new Insurance();

			if (fswFormMap.get("insurance") != null && !fswFormMap.get("insurance").equals(null)
					&& !fswFormMap.get("insurance").equals("")) {
				Map insureDetails = new HashMap();
				insureDetails = (HashMap) fswFormMap.get("insurance");
				insurance.setInsuranceCarrierName((String) insureDetails.get("insuranceCarrierName"));
				insurance.setInsuranceCarrierAddress1((String)insureDetails.get("insuranceCarrierAddress1"));
				insurance.setInsuranceCarrierAddress2((String) (insureDetails.get("insuranceCarrierAddress2")));
				insurance.setInsuranceCarrierCity((String) insureDetails.get("insuranceCarrierCity"));
				insurance.setInsuranceCarrierState((String) insureDetails.get("insuranceCarrierState"));
				insurance.setInsuranceCarrierCountry((String) insureDetails.get("insuranceCarrierCountry"));
				insurance.setInsuranceCarrierPostalCode((String) insureDetails.get("insuranceCarrierPostalCode"));

			}
			return insurance;
		}

	// Populate JSON Buyer Details in form Request
	private Contact populateJSONBuyerDetails(Map fswFormMap) {
		Contact buyer = new Contact();
		if (fswFormMap.get("holder") != null && !fswFormMap.get("holder").equals(null)
				&& !fswFormMap.get("holder").equals("")) {
			Map Buyerdetails = new HashMap();
			Buyerdetails = (Map) fswFormMap.get("holder");
			buyer.setType("B");
			buyer.setSalutation((String) Buyerdetails.get("salutation"));

			buyer.setFirstName((String) Buyerdetails.get("customerFirstName"));
			buyer.setLastName((String) Buyerdetails.get("customerLastName"));
			buyer.setMiddleInitialName((String) Buyerdetails.get("customerMiddleName"));
			buyer.setMiddleName((String) Buyerdetails.get("customerMiddleName"));

			buyer.setEmail((String) Buyerdetails.get("customerEmail"));
			buyer.setMemberNumber((String) Buyerdetails.get("memberNumber"));
			Address buyerAddress = new Address();
			buyerAddress.setAddress1((String) Buyerdetails.get("customerAddress1"));
			buyerAddress.setAddress2((String) Buyerdetails.get("customerAddress2"));
			buyerAddress.setPostalCode((String) Buyerdetails.get("customerPostalCode"));

			//substring of customer postalcode for Enterprise forms alone
			/*if( (fswFormMap.get("account") != null && !fswFormMap.get("account").equals(null)  && !fswFormMap.get("account").equals("") ) &&
				( (((HashMap) (fswFormMap.get("account"))).get("accountNumber")) != null && ! (((HashMap) (fswFormMap.get("account"))).get("accountNumber")).equals(null) &&
				  !(((HashMap) (fswFormMap.get("account"))).get("accountNumber")).equals("")  && ((HashMap) (fswFormMap.get("account"))).get("accountNumber").toString().startsWith("ENT-") ) &&
					Buyerdetails.get("customerPostalCode") != null && ! Buyerdetails.get("customerPostalCode").equals(null)
					&& !Buyerdetails.get("customerPostalCode").equals("") && ((String) Buyerdetails.get("customerPostalCode")).length() > 5)
			{
			   buyerAddress.setPostalCode(((String) Buyerdetails.get("customerPostalCode")).substring(0, 5));
			}else
			   buyerAddress.setPostalCode((String) Buyerdetails.get("customerPostalCode"));*/

			buyerAddress.setCity((String) Buyerdetails.get("customerCity"));
			buyerAddress.setStateCode((String) Buyerdetails.get("customerStateCode"));
			buyerAddress.setCountryCode((String) Buyerdetails.get("customerCountryCode"));
			buyer.setAddress(buyerAddress);
			Set<Phone> buyerPhone = new HashSet();

			Phone BuyerHomephone = new Phone();
			BuyerHomephone.setType("H");
			BuyerHomephone.setNumber((String) Buyerdetails.get("customerHomePhoneNumber"));
			buyerPhone.add(BuyerHomephone);

			Phone buyerWorkphone = new Phone();
			buyerWorkphone.setType("W");
			buyerWorkphone.setNumber((String) Buyerdetails.get("customerWorkPhoneNumber"));
			buyerPhone.add(buyerWorkphone);

			Phone buyerMobilephone = new Phone();
			buyerMobilephone.setType("M");
			buyerMobilephone.setNumber((String) Buyerdetails.get("customerMobilePhoneNumber"));
			buyerPhone.add(buyerMobilephone);
			buyer.setPhones(buyerPhone);
		}
		return buyer;
	}

	// Populate JSON CoBuyer Details in form Request
	private Contact populateJSONCoBuyerDetails(Map fswFormMap) {
		Contact coBuyer = new Contact();
		if (fswFormMap.get("holder") != null && !fswFormMap.get("holder").equals(null)
				&& !fswFormMap.get("holder").equals("")) {
			Map coBuyerDetails = new HashMap();
			coBuyerDetails = (Map) fswFormMap.get("holder");
			coBuyer.setType("CB");
			coBuyer.setFirstName((String) coBuyerDetails.get("coBuyerFirstName"));
			coBuyer.setLastName((String) coBuyerDetails.get("coBuyerLastName"));
			coBuyer.setMiddleInitialName((String) coBuyerDetails.get("coBuyerMiddleName"));
			coBuyer.setEmail((String) coBuyerDetails.get("coBuyerEmail"));

			Address coBuyerAddress = new Address();
			coBuyerAddress.setAddress1((String) coBuyerDetails.get("coBuyerAddress1"));
			coBuyerAddress.setAddress2((String) coBuyerDetails.get("coBuyerAddress2"));
			coBuyerAddress.setPostalCode((String) coBuyerDetails.get("coBuyerPostalCode"));
			coBuyerAddress.setCity((String) coBuyerDetails.get("coBuyerCity"));
			coBuyerAddress.setStateCode((String) coBuyerDetails.get("coBuyerStateCode"));
			coBuyerAddress.setCountryCode((String) coBuyerDetails.get("coBuyerCountryCode"));
			coBuyer.setAddress(coBuyerAddress);

			Set<Phone> coBuyerPhone = new HashSet();
			Phone coBuyerHomephone = new Phone();
			coBuyerHomephone.setType("H");
			coBuyerHomephone.setNumber((String) coBuyerDetails.get("coBuyerHomePhoneNumber"));
			coBuyerPhone.add(coBuyerHomephone);

			Phone coBuyerWorkphone = new Phone();
			coBuyerWorkphone.setType("W");
			coBuyerWorkphone.setNumber((String) coBuyerDetails.get("coBuyerWorkPhoneNumber"));
			coBuyerPhone.add(coBuyerWorkphone);

			Phone coBuyerMobilephone = new Phone();
			coBuyerMobilephone.setType("M");
			coBuyerMobilephone.setNumber((String) coBuyerDetails.get("coBuyerMobilePhoneNumber"));
			coBuyerPhone.add(coBuyerMobilephone);

			coBuyer.setPhones(coBuyerPhone);
		}

		return coBuyer;
	}

	// Populate JSON Lien Holder Details in form Request
	private Contact populateJSONLienHolderDetails(Map fswFormMap) {
		Contact lienHolder = new Contact();
		if (!fswFormMap.get("financing").equals(null) && !fswFormMap.get("financing").equals("")) {
			Map lienHolderDetails = (Map) fswFormMap.get("financing");
			lienHolder.setType("L");
			lienHolder.setFirstName((String) lienHolderDetails.get("lienholderName"));

			Address lienHolderAddress = new Address();
			lienHolderAddress.setAddress1((String) lienHolderDetails.get("lienholderAddress1"));
			lienHolderAddress.setAddress2((String) lienHolderDetails.get("lienholderAddress2"));
			lienHolderAddress.setPostalCode((String) lienHolderDetails.get("lienholderPostalCode"));

			//substring of lineholdeer postalcode for Enterprise forms alone
			/*if( (fswFormMap.get("account") != null && !fswFormMap.get("account").equals(null)  && !fswFormMap.get("account").equals("") ) &&
					( (((HashMap) (fswFormMap.get("account"))).get("accountNumber")) != null && ! (((HashMap) (fswFormMap.get("account"))).get("accountNumber")).equals(null) &&
					  !(((HashMap) (fswFormMap.get("account"))).get("accountNumber")).equals("")  && ((HashMap) (fswFormMap.get("account"))).get("accountNumber").toString().startsWith("ENT-") ) &&
					lienHolderDetails.get("lienholderPostalCode") != null && ! lienHolderDetails.get("lienholderPostalCode").equals(null)
					&& !lienHolderDetails.get("lienholderPostalCode").equals("") && ((String) lienHolderDetails.get("lienholderPostalCode")).length() > 5)
			{
			  lienHolderAddress.setPostalCode(((String) lienHolderDetails.get("lienholderPostalCode")).substring(0, 5));
			}else
			   lienHolderAddress.setPostalCode((String) lienHolderDetails.get("lienholderPostalCode"));*/


			lienHolderAddress.setCity((String) lienHolderDetails.get("lienholderCity"));
			lienHolderAddress.setStateCode((String) lienHolderDetails.get("lienholderState"));
			lienHolder.setAddress(lienHolderAddress);

			Set<Phone> lienHolderPhone = new HashSet();
			Phone lienHolderWorkphone = new Phone();
			lienHolderWorkphone.setType("W");
			lienHolderWorkphone.setNumber((String) lienHolderDetails.get("lienholderPhoneNumber"));
			lienHolderPhone.add(lienHolderWorkphone);

			lienHolder.setPhones(lienHolderPhone);
		}
		return lienHolder;
	}

	// Populate JSON Product Details in form Request
	private Set<ProductDetail> populateJSONProductDetails(Map fswFormMap) throws Exception {

		// Create an entity with ProductDetails
		Set<ProductDetail> productDetails = new HashSet<>();
		ProductDetail productDetail = null;
		Map productDetailMap = null;
		// Create entities with Product Details
		ArrayList products = (ArrayList) fswFormMap.get("product");

		// Retrieve single product
		if (products != null) {
			productDetail = new ProductDetail();
			productDetailMap = (Map) products.get(0);

			if (productDetailMap.get("formNumber") != null && !(productDetailMap.get("formNumber").equals(null))
					&& !(productDetailMap.get("formNumber").equals("")))
				productDetail.setFormNumber((String) productDetailMap.get("formNumber"));
			if (productDetailMap.get("productDetailType") != null
					&& !(productDetailMap.get("productDetailType").equals(null))
					&& !(productDetailMap.get("productDetailType").equals("")))
				productDetail.setProductDetailType((String) productDetailMap.get("productDetailType"));
			if (productDetailMap.get("reportedCustomerCost") == null
					|| productDetailMap.get("reportedCustomerCost").equals(null)
					|| productDetailMap.get("reportedCustomerCost").equals(""))
				productDetail.setReportedCustomerCost(0.0);
			else
				productDetail.setReportedCustomerCost(
						(Double.parseDouble(productDetailMap.get("reportedCustomerCost").toString())));

			if (productDetailMap.get("salesTax") == null || productDetailMap.get("salesTax").equals(null)
					|| productDetailMap.get("salesTax").equals(""))
				productDetail.setSalesTax(0.0);
			else
				productDetail.setSalesTax((Double.parseDouble(productDetailMap.get("salesTax").toString())));
			
			if (productDetailMap.get("GST") != null && !(productDetailMap.get("GST").equals(null))
					&& !(productDetailMap.get("GST").equals("")))
				productDetail.setGST(Double.parseDouble(productDetailMap.get("GST").toString()));
			else
				productDetail.setGST(0.0);
			
			if (productDetailMap.get("PST") != null && !(productDetailMap.get("PST").equals(null))
					&& !(productDetailMap.get("PST").equals("")))
				productDetail.setPST(Double.parseDouble(productDetailMap.get("PST").toString()));
			else
				productDetail.setPST(0.0);

			if (productDetailMap.get("deductible") == null || productDetailMap.get("deductible").equals(null)
					|| productDetailMap.get("deductible").equals(""))
				productDetail.setDeductibleAmount(0.00);
			else
				productDetail.setDeductibleAmount((Double.parseDouble(productDetailMap.get("deductible").toString())));

			if (productDetailMap.get("reducedDeductible") == null
					|| productDetailMap.get("reducedDeductible").equals(null)
					|| productDetailMap.get("reducedDeductible").equals(""))
				productDetail.setReducedDeductible(0.00);
			else
				productDetail.setReducedDeductible(
						(Double.parseDouble(productDetailMap.get("reducedDeductible").toString())));
			productDetail.setDisappearing(true);
			productDetail.setDisappearing((productDetail.getReducedDeductible() == null
					|| productDetail.getReducedDeductible().equals(productDetail.getDeductibleAmount())) ? false
							: true);
			/*
			 * if (productDetailMap.get("oilType") == null ||
			 * productDetailMap.get("oilType").equals(""))
			 * {productDetail.setCoverageName((String) productDetailMap.get("coverage"));}
			 *
			 * else { productDetail.setCoverageName((String)
			 * productDetailMap.get("coverage") + " " + (String)
			 * productDetailMap.get("oilType"));
			 *
			 * }
			 */
			productDetail.setCoverageName((String) productDetailMap.get("coverage"));
			productDetail.setOilType((String) productDetailMap.get("oilType"));

			if (productDetailMap.get("termMonths") == null || productDetailMap.get("termMonths").equals(null)
					|| productDetailMap.get("termMonths").equals(""))
				productDetail.setTermMonths(0);
			else
			{
				productDetail.setTermMonths((Integer.parseInt(productDetailMap.get("termMonths").toString())));
			}
			if (productDetailMap.get("termDistance") == null || productDetailMap.get("termDistance").equals(null)
					|| productDetailMap.get("termDistance").equals(""))
				productDetail.setTermDistance(0);
			else
				productDetail.setTermDistance((Integer.parseInt(productDetailMap.get("termDistance").toString())));

			productDetail.setAgreementStatus((String) productDetailMap.get("agreementStatus"));

			if (productDetailMap.get("ppmIntervalMiles") == null
					|| productDetailMap.get("ppmIntervalMiles").equals(null)
					|| productDetailMap.get("ppmIntervalMiles").equals(""))
				productDetail.setPpmIntervalMiles(0);
			else
				productDetail
						.setPpmIntervalMiles(Integer.parseInt(productDetailMap.get("ppmIntervalMiles").toString()));

			if (productDetailMap.get("serviceInterval") == null || productDetailMap.get("serviceInterval").equals(null)
					|| productDetailMap.get("serviceInterval").equals(""))
				productDetail.setServiceInterval(0);
			else
				productDetail.setServiceInterval(Integer.parseInt(productDetailMap.get("serviceInterval").toString()));

			if (productDetailMap.get("numberOfServices") == null
					|| productDetailMap.get("numberOfServices").equals(null)
					|| productDetailMap.get("numberOfServices").equals(""))
				productDetail.setNumberOfServices(0);
			else
				productDetail
						.setNumberOfServices(Integer.parseInt(productDetailMap.get("numberOfServices").toString()));

			if (productDetailMap.get("totalAllowableMilesContract") == null
					|| productDetailMap.get("totalAllowableMilesContract").equals(null)
					|| productDetailMap.get("totalAllowableMilesContract").equals(""))
				productDetail.setTotalAllowableMilesContract(0.0);
			else
				productDetail.setTotalAllowableMilesContract(
						(Double.parseDouble(productDetailMap.get("totalAllowableMilesContract").toString())));
			productDetail.setProductClass((String) productDetailMap.get("productClass"));
			productDetail.setProductDetailType((String) productDetailMap.get("productDetailType"));
			productDetail.setSystemPin((String) productDetailMap.get("systemPin"));

			if (productDetailMap.get("coverageCode") != null && !(productDetailMap.get("coverageCode").equals(null))
					&& !(productDetailMap.get("coverageCode").equals("")))
				productDetail.setCoverageCode((String) productDetailMap.get("coverageCode"));

			if (productDetailMap.get("optionalCoverages") != null && !(productDetailMap.get("optionalCoverages").equals(null))
					&& !(productDetailMap.get("optionalCoverages").equals("")))
				productDetail.setOptionalCoverages((ArrayList) productDetailMap.get("optionalCoverages"));

			if (productDetailMap.get("autoRenew") != null  && !(productDetailMap.get("autoRenew").equals(null))
					&& (!(productDetailMap.get("autoRenew").equals(""))))
				productDetail.setAutoRenew((String) productDetailMap.get("autoRenew"));

			if (productDetailMap.get("netPremium") == null  || (productDetailMap.get("netPremium").equals(null)))
				productDetail.setNetPremium(0.0);
			else 
				productDetail.setNetPremium(Double.parseDouble(productDetailMap.get("netPremium").toString()));

			productDetails.add(productDetail);
		}

		// For multiple products
		if ((((ArrayList) fswFormMap.get("product")).size() > 1)) {
			// System.out.println("size is "+((ArrayList)
			// fswFormMap.get("product")).size());
			// Retrieve Multiple products
			if (products != null) {
				productDetail = new ProductDetail();
				productDetailMap = (Map) products.get(1);

				if (productDetailMap.get("formNumber") != null && !(productDetailMap.get("formNumber").equals(null))
						&& !(productDetailMap.get("formNumber").equals("")))
					productDetail.setFormNumber((String) productDetailMap.get("formNumber"));
				if (productDetailMap.get("productDetailType") != null
						&& !(productDetailMap.get("productDetailType").equals(null))
						&& !(productDetailMap.get("productDetailType").equals("")))
					productDetail.setProductDetailType((String) productDetailMap.get("productDetailType"));
				if (productDetailMap.get("reportedCustomerCost") == null
						|| productDetailMap.get("reportedCustomerCost").equals(null)
						|| productDetailMap.get("reportedCustomerCost").equals(""))
					productDetail.setReportedCustomerCost(0.0);
				else
					productDetail.setReportedCustomerCost(
							(Double.parseDouble(productDetailMap.get("reportedCustomerCost").toString())));

				if (productDetailMap.get("salesTax") == null || productDetailMap.get("salesTax").equals(null)
						|| productDetailMap.get("salesTax").equals(""))
					productDetail.setSalesTax(0.0);
				else
					productDetail.setSalesTax((Double.parseDouble(productDetailMap.get("salesTax").toString())));

				if (productDetailMap.get("deductible") == null || productDetailMap.get("deductible").equals(null)
						|| productDetailMap.get("deductible").equals(""))
					productDetail.setDeductibleAmount(0.00);
				else
					productDetail
							.setDeductibleAmount((Double.parseDouble(productDetailMap.get("deductible").toString())));

				if (productDetailMap.get("reducedDeductible") == null
						|| productDetailMap.get("reducedDeductible").equals(null)
						|| productDetailMap.get("reducedDeductible").equals(""))
					productDetail.setReducedDeductible(0.00);
				else
					productDetail.setReducedDeductible(
							(Double.parseDouble(productDetailMap.get("reducedDeductible").toString())));
				productDetail.setDisappearing(true);
				productDetail.setDisappearing((productDetail.getReducedDeductible() == null
						|| productDetail.getReducedDeductible().equals(productDetail.getDeductibleAmount())) ? false
								: true);
				productDetail.setCoverageName((String) productDetailMap.get("coverage"));
				productDetail.setOilType((String) productDetailMap.get("oilType"));

				if (productDetailMap.get("termMonths") == null || productDetailMap.get("termMonths").equals(null)
						|| productDetailMap.get("termMonths").equals(""))
					productDetail.setTermMonths(0);
				else
					productDetail.setTermMonths((Integer.parseInt(productDetailMap.get("termMonths").toString())));

				if (productDetailMap.get("termDistance") == null || productDetailMap.get("termDistance").equals(null)
						|| productDetailMap.get("termDistance").equals(""))
					productDetail.setTermDistance(0);
				else
					productDetail.setTermDistance((Integer.parseInt(productDetailMap.get("termDistance").toString())));

				productDetail.setAgreementStatus((String) productDetailMap.get("agreementStatus"));

				if (productDetailMap.get("ppmIntervalMiles") == null
						|| productDetailMap.get("ppmIntervalMiles").equals(null)
						|| productDetailMap.get("ppmIntervalMiles").equals(""))
					productDetail.setPpmIntervalMiles(0);
				else
					productDetail
							.setPpmIntervalMiles(Integer.parseInt(productDetailMap.get("ppmIntervalMiles").toString()));

				if (productDetailMap.get("serviceInterval") == null
						|| productDetailMap.get("serviceInterval").equals(null)
						|| productDetailMap.get("serviceInterval").equals(""))
					productDetail.setServiceInterval(0);
				else
					productDetail
							.setServiceInterval(Integer.parseInt(productDetailMap.get("serviceInterval").toString()));

				if (productDetailMap.get("numberOfServices") == null
						|| productDetailMap.get("numberOfServices").equals(null)
						|| productDetailMap.get("numberOfServices").equals(""))
					productDetail.setNumberOfServices(0);
				else
					productDetail
							.setNumberOfServices(Integer.parseInt(productDetailMap.get("numberOfServices").toString()));

				if (productDetailMap.get("totalAllowableMilesContract") == null
						|| productDetailMap.get("totalAllowableMilesContract").equals(null)
						|| productDetailMap.get("totalAllowableMilesContract").equals(""))
					productDetail.setTotalAllowableMilesContract(0.0);
				else
					productDetail.setTotalAllowableMilesContract(
							(Double.parseDouble(productDetailMap.get("totalAllowableMilesContract").toString())));
				productDetail.setProductClass((String) productDetailMap.get("productClass"));
				productDetail.setProductDetailType((String) productDetailMap.get("productDetailType"));
				productDetail.setSystemPin((String) productDetailMap.get("systemPin"));

				if (productDetailMap.get("coverageCode") != null && !(productDetailMap.get("coverageCode").equals(null))
						&& !(productDetailMap.get("coverageCode").equals("")))
					productDetail.setCoverageCode((String) productDetailMap.get("coverageCode"));

				productDetails.add(productDetail);
			}
		}
		
		return productDetails;
	}

	// Populate JSON Broker Details in form Request
		private BrokerDetails populateJSONBrokerDetails(Map fswFormMap) throws Exception {
			BrokerDetails brokerDetails = new BrokerDetails();
			if (fswFormMap.get("brokerInfo") != null && !fswFormMap.get("brokerInfo").equals(null)
					&& !fswFormMap.get("brokerInfo").equals("")) {
				Map brokerDetailsMap = new HashMap();
				brokerDetailsMap = (Map) fswFormMap.get("brokerInfo");
				
				brokerDetails.setBrokerName((String) brokerDetailsMap.get("brokerName"));
				brokerDetails.setBrokerTaxId((String) brokerDetailsMap.get("brokerTaxId"));
				brokerDetails.setCobrokerName((String) brokerDetailsMap.get("coBrokerName"));
				brokerDetails.setCobrokerTaxId((String) brokerDetailsMap.get("coBrokerTaxId"));
				brokerDetails.setSubstipulatorName((String) brokerDetailsMap.get("substipulatorName"));
				brokerDetails.setSubstipulatorTaxId((String) brokerDetailsMap.get("substipulatorTaxId"));
				brokerDetails.setRepresentativeName((String) brokerDetailsMap.get("representativeName"));
				brokerDetails.setRepresentativeTaxId((String) brokerDetailsMap.get("representativeTaxId"));
				brokerDetails.setRepresentativePhone((String) brokerDetailsMap.get("representativePhone"));
				brokerDetails.setBrokerPhone((String) brokerDetailsMap.get("brokerPhone"));
				brokerDetails.setCobrokerPhone((String) brokerDetailsMap.get("coBrokerPhone"));
				brokerDetails.setSubstipulatorPhone((String) brokerDetailsMap.get("substipulatorPhone"));
				
				
				if (brokerDetailsMap.get("brokerPercentage") == null || brokerDetailsMap.get("brokerPercentage").equals(null)
						|| brokerDetailsMap.get("brokerPercentage").equals(""))
					brokerDetails.setBrokerPercentage(0.00);
				else
					brokerDetails.setBrokerPercentage((Double.parseDouble(brokerDetailsMap.get("brokerPercentage").toString())));
			
				if (brokerDetailsMap.get("coBrokerPercentage") == null || brokerDetailsMap.get("coBrokerPercentage").equals(null)
						|| brokerDetailsMap.get("coBrokerPercentage").equals(""))
					brokerDetails.setCobrokerPercentage(0.00);
				else
					brokerDetails.setCobrokerPercentage((Double.parseDouble(brokerDetailsMap.get("coBrokerPercentage").toString())));
			
				if (brokerDetailsMap.get("substipulatorPercentage") == null || brokerDetailsMap.get("substipulatorPercentage").equals(null)
						|| brokerDetailsMap.get("substipulatorPercentage").equals(""))
					brokerDetails.setSubstipulatorPercentage(0.00);
				else
					brokerDetails.setSubstipulatorPercentage((Double.parseDouble(brokerDetailsMap.get("substipulatorPercentage").toString())));
				
				if (brokerDetailsMap.get("representativePercentage") == null || brokerDetailsMap.get("representativePercentage").equals(null)
						|| brokerDetailsMap.get("representativePercentage").equals(""))
					brokerDetails.setRepresentativePercentage(0.00);
				else
					brokerDetails.setRepresentativePercentage((Double.parseDouble(brokerDetailsMap.get("representativePercentage").toString())));
			
			}
			return brokerDetails;
		}
		
}