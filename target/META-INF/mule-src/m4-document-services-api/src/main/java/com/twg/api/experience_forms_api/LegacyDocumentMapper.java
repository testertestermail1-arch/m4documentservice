package com.twg.api.experience_forms_api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.helpers.FileUtils;

import com.google.common.collect.Sets;
import com.twg.api.experience_forms_api.document.*;

import com.twg.api.experience_forms_api.request.Contact;
import com.twg.api.experience_forms_api.request.FinancingType;
import com.twg.api.experience_forms_api.request.FormRequest;
import com.twg.api.experience_forms_api.request.FormType;
import com.twg.api.experience_forms_api.request.KeyValuePair;
import com.twg.api.experience_forms_api.request.Phone;
import com.twg.api.experience_forms_api.request.PhoneType;
import com.twg.api.experience_forms_api.request.ProductDetail;
import com.twg.api.experience_forms_api.request.ProductType;
import com.twg.api.experience_forms_api.request.Vehicle;
import com.twg.api.experience_forms_api.request.VehicleType;
import com.twg.api.experience_forms_api.response.Error;
import com.twg.api.experience_forms_api.response.DocumentResponse;

public class LegacyDocumentMapper 
{
	static final String COMPFORMID = "COMPFORMID";
	static final String BUNFORMID = "BUNFORMID";
	private static final Log log = LogFactory.getLog(LegacyDocumentMapper.class);
	private static final String WS_DEALER = "sfdcservices";
	private static final String SERVICE = "services";
	//private static String INPUT_PATH = System.getProperty("forms.input.path");
	//private static String OUTPUT_PATH = System.getProperty("forms.output.path");
	private DecimalFormat decimalFormat = new DecimalFormat("#0.00");
	private DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
	private DecimalFormat currencyFormatWithoutSymbol = new DecimalFormat("#,##0.00");
	private DecimalFormat numberFormat = new DecimalFormat("#,##0");
	private DecimalFormat decimalFormatInt = new DecimalFormat("#");
	private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private DateFormat dateFormatStoreProcedure = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat dateFormatForBrazil = new SimpleDateFormat("dd-MM-yyyy");
		
	
		public byte[] getForm(FormRequest request,InputStream template) throws Exception 
		{
		byte[] ByteArrayInputStream = null;
		//request.validate(errors);
//		if (errors.size() > 0) {
//			return new FormResponse(errors);
//		}
//		if (!validateSecurity(request)) {
//			errors.add(new Error("request.callingApp.securityService.invalid", "The security service credentials are not valid."));
//			return new FormResponse(errors);
//		}
		try {
			Set<Pdf> pdfs = new HashSet<Pdf>();
			if(isCountryMX(request)) {
				dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			}else {
				dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			}
			ByteArrayInputStream = generateAllPDFs(pdfs, request, template);
//			if (hasToValidateContrctNumber(request)){
//				errors = this.validateContractNumber(request.getContractNumber(), request.getProductDetails());
//				if (errors.size() > 0) {
//					return new FormResponse(errors);
//				}
//			}
		} 
		catch (Exception exception) 
		{
			exception.printStackTrace();
			LegacyDocumentMapper.log.error("Error while trying to get the form.");
		}
		return ByteArrayInputStream;
	}
	
	private boolean hasToValidateContrctNumber(FormRequest request)
	{
		boolean result = false;
		if (!StringUtils.isBlank(request.getContractNumber())) 
		{
			result = true;
			if (request.getFillers()!=null)
			{
				for (Iterator<KeyValuePair> iterator = request.getFillers().iterator(); iterator.hasNext();) 
				{
					KeyValuePair filler = iterator.next();
					if (("DEALERTYPE".equals(filler.getKey().toUpperCase())) && ("REMIT".equals(filler.getValue().toUpperCase())))
					{
						result = false;
					}				
				}
			}
		}
		return result;
	}
	
	/**
	 * Return True if the web service call is made by SalesForce Application
	 * @param request This is the request
	 * @return returns the data
	 */
	public boolean isSalesForceCall(FormRequest request)
	{
		return ("WLSC".equals(request.getCallingApp().getAppId()));
	}
	
	/**
	 * Return True if the web service call is made by SalesForce Application
	 * @param request javadoc comments
	 * @return javadoc comments
	 */
	public boolean isDealerTrackCall(FormRequest request)
	{
		return ("DLRTRK".equals(request.getCallingApp().getAppId()));
	}
	
	/**
	 * Generate all PDF Contracts
	 * @param pdfs Set to insert each PDF generated
	 * @param request Web Service request
	 * @return FormResponse to Web Service
	 * @throws ParseException 
	 */
	private  byte[] generateAllPDFs(Set<Pdf> pdfs,FormRequest request, InputStream template) throws ParseException
	{
		if ((isBundled(request) && isSalesForceCall(request)) || isComplimentary(request))
		{		
			
			return generateBundledPDF(pdfs, request,template);
		}
		else
		{	
			
			return generatePDFs(pdfs,request,template);
		}			 
	}


	
	/**
	 * Generate PDF Contracts for Complimentary or Bundled
	 * @param request
	 * @throws ParseException 
	 */
	private byte[] generateBundledPDF(Set<Pdf> pdfs,FormRequest request, InputStream template) throws ParseException
	{	
		byte[] ByteArrayInputStream=null;
		String pdfFileName;
		String formatType = "";
		ProductDetail productDetail = null;
		
	
        Date issueDate = new SimpleDateFormat("dd/MM/yyyy").parse(this.getDateInCST());
     
      
        
		if(request.getFinanceDetails() != null && request.getFinanceDetails().getStartDate() != null && isClassificationCodeAN(request))
		{
			issueDate = request.getFinanceDetails().getStartDate();
		}
		try 
		{	
			if(request.getAccount() !=null && request.getAccount().getClassificationCode() != null && request.getAccount().getClassificationCode().equals("AN"))
			{
				formatType = DocumentMapper.FORMAT_TYPE_ALL_CAPS;
			}
			productDetail = (ProductDetail)request.getProductDetails().toArray()[0];
			ByteArrayInputStream = writePdfToFile(generatePdfMultipeProductDetails(request, issueDate), formatType, template);
//			if (pdfFileName != null) {
//				pdfFileName = pdfFileName.replaceAll(".pdf$", "");
//				String userName = request.getCallingApp().getServiceSecurity() != null
//						&& request.getCallingApp().getServiceSecurity().getUsername() != null ? request.getCallingApp().getServiceSecurity().getUsername()
//						: "anonymous";
//				File file = new File(MessageFormat.format("{0}{1}.pdf", OUTPUT_PATH, pdfFileName));
//				byte[] fileByteArray = Files.toByteArray(file);
//				Long id = formsDAO.storeForm(request.getQuoteId(), request.getContractNumber(), issueDate, productDetail.getType(), request.getCallingApp()
//						.getSourceSystem(), pdfFileName, productDetail.getForm().getType(), fileByteArray, userName);
//				String url = MessageFormat.format("{0}/FormsService/pdf/{1,number,#}", System.getProperty("forms.host.url"), id);
				//if (request.isUrlOnly()) {
				//	pdfs.add(new Pdf(productDetail.getProductDetailType(), "", productDetail.getFormNumber(), new Date()));
				//} else {
					pdfs.add(new Pdf(productDetail.getProductDetailType(), "", ByteArrayInputStream, productDetail.getFormNumber(), issueDate));
				//}
				//FileUtils.delete(file);
				
//			}
		} 
		catch (FileNotFoundException fileException) 
		{
			fileException.printStackTrace();
			LegacyDocumentMapper.log.error("Error while trying to get the form.");
		} 
		catch (IOException ioException)
		{
			LegacyDocumentMapper.log.error("IO Error.");
		}
		catch (Exception exception)
		{
			LegacyDocumentMapper.log.error("Exception Error.");
		}			
		return ByteArrayInputStream;
	}
	
	/**
	 * Generate One PDF Contract from Multiple Product Details. This is used fro Bundled and Complimentary products
	 * @param request
	 * @param issueDate
	 * @return
	 */
	private PdfBean generatePdfMultipeProductDetails(FormRequest request,Date issueDate) 
	{
		LegacyDocumentMapper.log.info("Generating pdf.");
		Set<ProductDetail> productDetails = request.getProductDetails();				
		ProductDetail firstProductDetail = request.getProductDetails().iterator().next();
		LegacyDocumentMapper.log.debug(MessageFormat.format("generatePdf[productDetail={0}, issueDate={1}]", firstProductDetail, this.formatDate(issueDate)));
		PdfBean pdf = new PdfBean();
		pdf.addField("AGREEMENTNO", request.getContractNumber());
		pdf.addField("VIN",request.getVehicle().getVin());
		pdf.addField("VEHICLECLASS", request.getVehicle().getVehicleClass());
		pdf.addField("MODEL", request.getVehicle().getModel());
		pdf.addField("ODOMETER",numberFormat.format(request.getVehicle().getOdometer()));
		//Added the Wrap Odometer as per JIRA TWGQA-11743
		pdf.addField("W_ODOMETER",numberFormat.format(request.getVehicle().getOdometer()));
		pdf.addField("YEAR", request.getVehicle().getYear());
		pdf.addField("MAKE", request.getVehicle().getMake());
		
		fillUpVehicleInformation(request, pdf);
		fillUpContactInformation(request,pdf);
		fillUpClientInformation(request,pdf);
		fillUpBundledInformation(request,pdf,issueDate);
		
		if (!request.getFsmName().isEmpty()) {
			pdf.addField("REPRESENTATIVE_NAME", request.getFsmName());
		} else {
			pdf.addField("REPRESENTATIVE_NAME", request.getAccount().getFieldRep());
		}
		if (isBundled(request))
		{
			if(request.getVehicle().isNewCar()){
				pdf.addField("MONTHS", firstProductDetail.getTermMonths());
				pdf.addField("MILES", numberFormat.format(firstProductDetail.getTermDistance()));
			}			
			if(isCountryMX(request)){
				pdf.addField("COST",this.formatCurrencyWithSymbol(firstProductDetail.getReportedCustomerCost()));
			}else {
				pdf.addField("COST",this.formatCurrencyWithoutSymbol(firstProductDetail.getReportedCustomerCost()));
			}
		}
		
		
		if(request.getVehicle().getExpirationDate() != null && (!request.getVehicle().getExpirationDate().equals("")))
		{
			pdf.addField("EXPIRATIONDATE",this.formatDate(request.getVehicle().getExpirationDate()));
			pdf.addField("PP_EXPIRATIONDATE",this.formatDate(request.getVehicle().getExpirationDate()));
			pdf.addField("maturity_date",this.formatDate(request.getVehicle().getExpirationDate()));
		}
		else if(request.getExpirationType() != "" && request.getExpirationType() != null)
		{
			//EXPIRATIONDATE
			if(request.getExpirationType().equals("AOTAOM") || request.getExpirationType().equals("AOT") || request.getExpirationType().equals("AOTCM"))
			{		
				if(request.getFinanceDetails() !=null && request.getFinanceDetails().getStartDate()== null)
				{
					pdf.addField("EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, request.getProductDetail().getTermMonths())));
				}
				else
				{
				pdf.addField("EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getFinanceDetails().getStartDate(), request.getProductDetail().getTermMonths())));
				}
				
			}
			else if (request.getExpirationType().equals("ISDTCM") || request.getExpirationType().equals("ISDT"))
			{
				pdf.addField("EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getVehicle().getInServiceDate(), request.getProductDetail().getTermMonths())));
			}
		}
		if(request.getVehicle().getExpirationMileage() != null && (!request.getVehicle().getExpirationMileage().equals("")))
		{
			pdf.addField("EXPIRATIONMILEAGE",request.getVehicle().getExpirationMileage());
		}
		else if(request.getExpirationType() != "" && request.getExpirationType() != null)
		{ 
			if(request.getExpirationType().equals("AOTAOM"))
			{		
				pdf.addField("EXPIRATIONMILEAGE",numberFormat.format(((request.getVehicle().getOdometer())+(request.getProductDetail().getTermDistance()))));
			}
			else if (request.getExpirationType().equals("ISDTCM") || request.getExpirationType().equals("AOTCM") || request.getExpirationType().equals("CEDTCM") || request.getExpirationType().equals("AOT") || request.getExpirationType().equals("ISDT"))
			{	
				pdf.addField("EXPIRATIONMILEAGE",numberFormat.format(request.getProductDetail().getTermDistance()));
			}
			
			
		}
		
		
		/*populated for all the Product type except KEY,TWP,PDR,WNS*/
		if (!ProductType.WNS.toString().equals(firstProductDetail.getProductDetailType()) && !ProductType.PDR.toString().equals(firstProductDetail.getProductDetailType())
				&& !ProductType.KEY.toString().equals(firstProductDetail.getProductDetailType()) && !ProductType.TWP.toString().equals(firstProductDetail.getProductDetailType())) {
			if (request.getFinanceDetails() != null && request.getFinanceDetails().getStartDate() == null) {
				pdf.addField(getCompWrapPrefix(firstProductDetail) + "ACTIVATIONDATE", this.formatDate(issueDate));

			} else {
				pdf.addField(getCompWrapPrefix(firstProductDetail)+ "ACTIVATIONDATE", this.formatDate(request.getFinanceDetails().getStartDate()));

			}
		}
		Contact contact = request.getLienHolder();
		if (contact != null) 
		{
			pdf.addField("LIEN_NAME", contact.getFullName());			
			pdf.addField("LIEN_ADDRESS", contact.getAddress().getAddressString());			
			pdf.addField("LIEN_CITY", contact.getAddress().getCity());
			pdf.addField("LIEN_STATE", contact.getAddress().getStateCode().toUpperCase());
			pdf.addField("LIEN_ZIP", contact.getAddress().getPostalCode());
			pdf.addField("LIEN_PHONE", this.formatPhone(contact.getMainPhone()));
		}
		
		for (Iterator<ProductDetail> iterator = productDetails.iterator(); iterator.hasNext();) 
		{
			ProductDetail productDetail = iterator.next();
			fillUpComplimentaryInformation(request, productDetail,pdf, issueDate);
			
		}
		
		//if it is not a ThreeForOne
		if (!isThreeForOne(request) )
		{
			pdf.addField("TotalPrice",this.formatCurrencyWithoutSymbol(request.getTotalPrice()));
		}
		
		if (isCountryMX(request)) {
			fillDataForMexico(request, pdf);
		}
		
		if (!FormType.Final.name().equals(firstProductDetail.getAgreementStatus())) 
		{
			pdf.addField("WATERMARK", "SAMPLE INELIGIBLE");
			pdf.addField("SIGNHERE", "SAMPLE INELIGIBLE");
		}					
							
		return pdf;
	}
	
	/**
	 * Set Radio button for Complimentary Forms
	 * @param productDetail
	 * @param pdf
	 */
	private void fillUpComplimentaryInformation(FormRequest request, ProductDetail productDetail, PdfBean pdf, Date issueDate) 
	{
		boolean isWrapProduct = isCompWrap(productDetail);
		
			
		// set a field prefix if the product is a Wrap
		String prefix = isWrapProduct ? "W_" : "";

		
		// Radio Field's name that are in the form, radioFiedls.length == radioText.length
		
		String[] radioFields = {"Plus_Radio","Diesel_Oil_Radio","Conventional_Radio","Conventional_Plus_Radio","Blend_Radio", "Synthetic_Radio","Synthetic_Radio","Synthetic_Radio","Synthetic_Radio"};
		String[] radioText =   {"Preferred+","Diesel","Convntnl Oil(Up to 5 Qt)","Convntnl Oil(Over 5 Qt)","Synthetic Blend Oil","Full Synthetic Oil","Synthetic Asian and Domestic","Synthetic European and Cadillac","Synthetic Highline and Exotic"};
		String[] radioTextMR = {"Preferred+","Diesel","ConvOilUpto","ConvOilOver","Synthetic Blend","Full Synthetic","Synthetic Asian and Domestic","Synthetic European and Cadillac","Synthetic Highline and Exotic"};

		String[] oilTypeRadioFields = {"Diesel_Oil_Radio","Conventional_Radio","Conventional_Plus_Radio","Blend_Radio", "Synthetic_Radio","Synthetic_Radio","Synthetic_Radio","Synthetic_Radio"};
		String[] oilTypeRadioText   = {"Diesel","Convntnl Oil(Up to 5 Qt)","Convntnl Oil(Over 5 Qt)","Synthetic Blend Oil","Full Synthetic Oil","Synthetic Asian and Domestic","Synthetic European and Cadillac","Synthetic Highline and Exotic"};		
		String[] oilTypeRadioTextMR = {"Diesel","ConvOilUpto","ConvOilOver","Synthetic Blend","Full Synthetic","Synthetic Asian and Domestic","Synthetic European and Cadillac","Synthetic Highline and Exotic"};		

		pdf.addField(prefix + "Plus_Radio",StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Preferred Plus")   ? "Yes" : "No");
		pdf.addField(prefix + "Preferred_Radio",StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Preferred") && !StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "+") && !StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Plus") && !StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "hiTech") ? "Yes" : "No");
		if(isWrapProduct){
			pdf.addField(prefix + "MONTHS", productDetail.getTermMonths());
			pdf.addField(prefix + "MILES", numberFormat.format(productDetail.getTermDistance()));
		}
		if(isCountryMX(request)){
			pdf.addField(prefix + "COST",this.formatCurrencyWithSymbol(productDetail.getReportedCustomerCost()));
		}else {
			pdf.addField(prefix + "COST",this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
		}
		
		/*populated for all the Product type except KEY,TWP,PDR,WNS*/
		if (!ProductType.WNS.toString().equals(productDetail.getProductDetailType()) && !ProductType.PDR.toString().equals(productDetail.getProductDetailType())
				&& !ProductType.KEY.toString().equals(productDetail.getProductDetailType()) && !ProductType.TWP.toString().equals(productDetail.getProductDetailType())) {
			if (request.getFinanceDetails() != null && request.getFinanceDetails().getStartDate() == null) {
				pdf.addField(prefix + "ACTIVATIONDATE", this.formatDate(issueDate));

			} else {
				pdf.addField(prefix + "ACTIVATIONDATE", this.formatDate(request.getFinanceDetails().getStartDate()));
			}
		}
		
		
		if(productDetail.getCoverageName() != null)
		{
			pdf.addField(prefix + "Class1_Radio", (StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Class 1 Conventional Oil") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Class1") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Class 1"))? "Yes" : "No");
			pdf.addField(prefix + "Class2_Radio", (StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Class 2 Synthetic Oil") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Class2") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Class 2"))? "Yes" : "No");
			pdf.addField(prefix + "Class3_Radio", (StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Class 3 Diesel/Premium") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Class3") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Class 3"))? "Yes" : "No");
		}
		
		pdf.addField(prefix + "ISPREF",	StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Preferred") && !productDetail.getCoverageName().contains("+") && !productDetail.getCoverageName().contains("Plus") && !productDetail.getCoverageName().contains("Hi-Tech")? "X" : "");
		pdf.addField(prefix + "ISPREFPLUS", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Preferred+") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Preferred Plus") ? "X" : "");
		pdf.addField(prefix + "ISSYNTHETICOIL", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Synthetic") || StringUtils.containsIgnoreCase(productDetail.getOilType(), "Synthetic")? "X" : "");
	    pdf.addField(prefix + "Synthetic_Radio", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Full Synthetic") || StringUtils.containsIgnoreCase(productDetail.getOilType(), "Full Synthetic") ? "X" : "");
		pdf.addField(prefix + "ISDIESEL", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Diesel")  || request.getVehicle().isDiesel() || StringUtils.containsIgnoreCase(productDetail.getOilType(), "Diesel") ? "X" : "");
	   
 	    pdf.addField(prefix + "ADITIONALOIL", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Additional") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Addition") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Adition") || StringUtils.containsIgnoreCase(productDetail.getOilType(), "Additional") ? "X" : "");
		pdf.addField(prefix + "ADDITIONALOIL", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Additional") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Addition") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Adition") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "ConvOilOver") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Convntnl Oil(Over 5 Qt)")  || StringUtils.containsIgnoreCase(productDetail.getOilType(), "Additional") ? "X" : "");
		pdf.addField(prefix + "ISCONVENTIONAL", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "VCP CONVENTIONAL") ? "X" : "");
		pdf.addField(prefix + "ISSYNTHETIC", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "VCP Full Synthetic") ? "X" : "");
		pdf.addField(prefix + "ISBLEND", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "VCP Synthetic Blend") ? "X" : "");
		pdf.addField(prefix + "SERVICES", productDetail.getNumberOfServices());
		if(productDetail.getCoverageCode() != null && !(productDetail.getCoverageCode().equals("")))
		{
		pdf.addField(prefix + "dollars_3500", StringUtils.containsIgnoreCase(productDetail.getCoverageCode(), "3500 Benefit") ? "X" : "");
		pdf.addField(prefix + "dollars_6000", StringUtils.containsIgnoreCase(productDetail.getCoverageCode(), "6000 Benefit") ? "X" : "");
		pdf.addField(prefix + "dollars_10000", StringUtils.containsIgnoreCase(productDetail.getCoverageCode(), "10000 Benefit") ? "X" : "");
		}
		else
		{
		pdf.addField(prefix + "dollars_3500", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "3500 Benefit") ? "X" : "");
		pdf.addField(prefix + "dollars_6000", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "6000 Benefit") ? "X" : "");
		pdf.addField(prefix + "dollars_10000", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "10000 Benefit") ? "X" : "");
		}		
		pdf.addField(prefix + "SERVICEINTERVAL", productDetail.getServiceInterval());
	
		ProductDetail productDetailPPM = null;
		if (ProductType.PPM.toString().equals(productDetail.getProductDetailType())) 
		{
			productDetailPPM = productDetail;
		}
		else
		{
			productDetailPPM = request.getProductDetailsByType(ProductType.PPM);
		}
		
		if(productDetailPPM != null && productDetailPPM.getDeductibleAmount() != null && FormRequest.getIsRequestTypeJSON() != true)
		{
			pdf.addField(prefix + "miles_3000",	productDetailPPM.getDeductibleAmount() == 3000 ? "X" : "");
			pdf.addField(prefix + "miles_3750",	productDetailPPM.getDeductibleAmount() == 3750 ? "X" : "");
			pdf.addField(prefix + "miles_5000", productDetailPPM.getDeductibleAmount() == 5000 ? "X" : "");
			pdf.addField(prefix + "miles_6000", productDetailPPM.getDeductibleAmount() == 6000 ? "X" : "");
			pdf.addField(prefix + "miles_7500", productDetailPPM.getDeductibleAmount() == 7500 ? "X" : "");
			pdf.addField(prefix + "miles_10000", productDetailPPM.getDeductibleAmount() == 10000 ? "X" : "");
			
			pdf.addField(prefix + "3K_Miles_Radio", productDetailPPM.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 3000 ? "Yes" : "No");
			pdf.addField(prefix + "3.75K_Miles_Radio", productDetailPPM.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 3750 ? "Yes" : "No");
			pdf.addField(prefix + "5K_Miles_Radio", productDetailPPM.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 5000 ? "Yes" : "No");
			pdf.addField(prefix + "6K_Miles_Radio", productDetailPPM.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 6000 ? "Yes" : "No");
			pdf.addField(prefix + "7.5K_Miles_Radio", productDetailPPM.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 7500 ? "Yes" : "No");
			pdf.addField(prefix + "10K_Miles_Radio", productDetailPPM.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 10000 ? "Yes" : "No");
		}
		
		if(productDetailPPM != null && productDetailPPM.getServiceInterval() != 0)
		{
			pdf.addField(prefix + "miles_3000", productDetailPPM.getServiceInterval() == 3000 ? "X" : "");
			pdf.addField(prefix + "miles_3750", productDetailPPM.getServiceInterval() == 3750 ? "X" : "");
			pdf.addField(prefix + "miles_5000", productDetailPPM.getServiceInterval() == 5000 ? "X" : "");
			pdf.addField(prefix + "miles_6000", productDetailPPM.getServiceInterval() == 6000 ? "X" : "");
			pdf.addField(prefix + "miles_7500", productDetailPPM.getServiceInterval() == 7500 ? "X" : "");
			pdf.addField(prefix + "miles_10000", productDetailPPM.getServiceInterval() == 10000 ? "X" : "");
			
			pdf.addField(prefix + "3K_Miles_Radio", productDetail.getServiceInterval() == 3000 ? "Yes" : "No");
			pdf.addField(prefix + "3.75K_Miles_Radio", productDetail.getServiceInterval() == 3750 ? "Yes" : "No");
			pdf.addField(prefix + "5K_Miles_Radio", productDetail.getServiceInterval() == 5000 ? "Yes" : "No");
			pdf.addField(prefix + "6K_Miles_Radio", productDetail.getServiceInterval() == 6000 ? "Yes" : "No");
			pdf.addField(prefix + "7.5K_Miles_Radio", productDetail.getServiceInterval() == 7500 ? "Yes" : "No");
			pdf.addField(prefix + "10K_Miles_Radio", productDetail.getServiceInterval() == 10000 ? "Yes" : "No");
		}
		
		if(productDetailPPM != null && !StringUtils.equalsIgnoreCase(productDetailPPM.getOilType(), null) && !StringUtils.equalsIgnoreCase(productDetailPPM.getOilType(), ""))
		{
			pdf.addField("COVERAGEPLAN",productDetail.getCoverageName().concat("/").concat(productDetail.getOilType()));
			//Added the Wrap Coverage plan and oil type as per JIRA TWGQA-11743
			pdf.addField(prefix + "COVERAGEPLAN",productDetail.getCoverageName().concat("/").concat(productDetail.getOilType()));
			pdf.addField(prefix + "ENGINE_OIL_TYPE",productDetail.getOilType());
		}
		
		for (int i = 0; i < radioFields.length; i++) 
		{
			String field = prefix + radioFields[i];
			String text = radioText[i];
			String textMR = radioTextMR[i];
			if(StringUtils.containsIgnoreCase(productDetail.getCoverageName(), text) || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), textMR) || StringUtils.containsIgnoreCase(productDetail.getOilType(), textMR) || StringUtils.containsIgnoreCase(productDetail.getOilType(), text)) {
				pdf.addField(field,"Yes");
				break;
			}
			else {
				pdf.addField(field,"No");
			}
	}
		
		if(productDetailPPM != null && productDetailPPM.getOilType() != null && FormRequest.getIsRequestTypeJSON() != false)
		{
			for (int i = 0; i < oilTypeRadioFields.length; i++) 
			{
				String field = prefix + oilTypeRadioFields[i];
				String text = oilTypeRadioText[i];
				String textMR = oilTypeRadioTextMR[i];
				pdf.addField(field, (StringUtils.equalsIgnoreCase(productDetail.getOilType(), text) || StringUtils.equalsIgnoreCase(productDetail.getOilType(), textMR)) ? "Yes" : "No");
	 			if(productDetailPPM != null && StringUtils.equalsIgnoreCase((productDetail.getOilType()).trim(), "All"))
					{
						pdf.addField(field, "Yes");
					}
			}
			pdf.addField(prefix + "Synthetic_Radio", (StringUtils.equalsIgnoreCase(productDetail.getOilType(), "Synthetic") || StringUtils.equalsIgnoreCase(productDetail.getOilType(), "Full Synthetic Oil") || StringUtils.equalsIgnoreCase(productDetail.getOilType(), "Synthetic Asian and Domestic") || StringUtils.equalsIgnoreCase(productDetail.getOilType(), "Synthetic European and Cadillac") || StringUtils.equalsIgnoreCase(productDetail.getOilType(), "Synthetic Highline and Exotic") || StringUtils.equalsIgnoreCase(productDetail.getOilType(),"Full Synthetic")) ? "Yes" : "No");
		}
		//Added synthetic oil in the list as per JIRA TWGQA-11740
		
		pdf.addField(prefix + "Plus_Radio",StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Preferred Plus") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Preferred+") ? "Yes" : "No");
		pdf.addField(prefix + "Base_Radio", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "Base") ? "Yes" : "No");
		pdf.addField(prefix + "High-Tech_Radio", !StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Wrap") ? checkMultipleValuesForCheckBox(productDetail.getCoverageName(), new String[] {"High Tech","High-Tech"}) : "No");
		pdf.addField(prefix + "Comprehensive_Radio", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "Comprehensive") && !StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Wrap") ? "Yes" : "No");
		pdf.addField(prefix + "High-Tech_Wrap_Radio", checkMultipleValuesForCheckBox(productDetail.getCoverageName(), new String[] {"High Tech Wrap","High-Tech Wrap"}));
		
		pdf.addField(prefix + "Comprehensive_Wrap_Radio", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "Comprehensive Wrap") ? "Yes" : "No");
		
		if(StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "incl")) {
	        pdf.addField("AudioVisual_Radio", StringUtils.containsIgnoreCase(productDetail.getCoverageName().substring(productDetail.getCoverageName().lastIndexOf("incl") + 4), "AUDIO_VISUAL") ? "Yes" : "No");
	        pdf.addField("Ultra_Radio", StringUtils.containsIgnoreCase(productDetail.getCoverageName().substring(productDetail.getCoverageName().lastIndexOf("incl") + 4), "ULTRA_ELECTRONIC") ? "Yes" : "No");
	        pdf.addField("Wheelchair_Radio", StringUtils.containsIgnoreCase(productDetail.getCoverageName().substring(productDetail.getCoverageName().lastIndexOf("incl") + 4), "WHEELCHAIR_LIFT") ? "Yes" : "No");
	        pdf.addField("Seals_Radio", StringUtils.containsIgnoreCase(productDetail.getCoverageName().substring(productDetail.getCoverageName().lastIndexOf("incl") + 4), "SEALS_AND_GASKETS") ? "Yes" : "No");
	        pdf.addField("Appliance_Radio", StringUtils.containsIgnoreCase(productDetail.getCoverageName().substring(productDetail.getCoverageName().lastIndexOf("incl") + 4), "DELUXE_APPLIANCE") ? "Yes" : "No");
	        pdf.addField("HydraulicJacks_Radio", StringUtils.containsIgnoreCase(productDetail.getCoverageName().substring(productDetail.getCoverageName().lastIndexOf("incl") + 4), "LEVELING_JACKS") ? "Yes" : "No");
	        pdf.addField("HydraulicRooms_Radio", StringUtils.containsIgnoreCase(productDetail.getCoverageName().substring(productDetail.getCoverageName().lastIndexOf("incl") + 4), "SLIDE_OUT_ROOMS") ? "Yes" : "No");
	        }
		
		       
		
		
		if(!StringUtils.containsIgnoreCase(productDetail.getCoverageName(),"Wrap") && !isWrapProduct ){
			
			if(!StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "COACHONLY")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "COACH ONLY")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "COACH")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "TRAVELTRAILER")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "TRAVEL TRAILER")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "FIFTHWHEEL")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "FIFTH WHEEL")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "FOLDINGCAMPERS")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "FOLDING CAMPERS")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "SLIDEIN")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "SLIDE IN")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "SLIDE-IN"))
			{
				
			if(!request.getVehicle().isNewCar() && (!ProductType.WNS.toString().equals(productDetail.getProductDetailType()) && !ProductType.PDR.toString().equals(productDetail.getProductDetailType())
					&& !ProductType.KEY.toString().equals(productDetail.getProductDetailType()) && !ProductType.TWP.toString().equals(productDetail.getProductDetailType())))
			{
				pdf.addField("MONTHS_USED", productDetail.getTermMonths());
				pdf.addField("MILES_USED", productDetail.getTermDistance());
				pdf.addField("MONTHS_MOTORHOME_USED", productDetail.getTermMonths());
				pdf.addField("MILES_MOTORHOME_USED", productDetail.getTermDistance());
			}
			else
			{
				pdf.addField("MONTHS", productDetail.getTermMonths());
				pdf.addField("MILES", numberFormat.format(productDetail.getTermDistance()));
				pdf.addField("MONTHS_MOTORHOME", productDetail.getTermMonths());
				pdf.addField("MILES_MOTORHOME", productDetail.getTermDistance());
			}	
			}
			else{
				
				if(!request.getVehicle().isNewCar())
				{
					pdf.addField("MONTHS_USED_TOWABLE", productDetail.getTermMonths());
					
				}
				else
				{
					pdf.addField("MONTHS_TOWABLE", productDetail.getTermMonths());
				}
				
			}
		}
		if(StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "High-Tech Wrap") || StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "High Tech Wrap") || StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "Comprehensive Wrap"))
		{
			pdf.addField(prefix + "MONTHS", "");
			pdf.addField(prefix + "MILES", "");
			pdf.addField("MONTHS_WRAP", productDetail.getTermMonths());
			pdf.addField("MILES_WRAP", numberFormat.format(productDetail.getTermDistance()));
		}
		
	}
	
	
	
	/**
	 * Return true or false if the request have id 21
	 * @param request javadoc comments
	 * @return javadoc comments
	 */
	public boolean isThreeForOne(FormRequest request)
	{
		for (Iterator<ProductDetail> iterator = request.getProductDetails().iterator(); iterator.hasNext();) 
		{							
			ProductDetail productDetail = iterator.next();
			String productType = productDetail.getProductDetailType();
			if ("BUN".equals(productType))
			{
				return true;
			}
		}
		return false;
	}


	/**
	 * Generate all PDF Contracts
	 * @param pdfs
	 * @param request
	 * @return
	 * @throws ParseException 
	 */
	private byte[] generatePDFs(Set<Pdf> pdfs,FormRequest request, InputStream template) throws ParseException
	{
		String formatType = "";
		byte[] ByteArrayInputStream=null;
		if(request.getAccount() !=null && request.getAccount().getClassificationCode() != null && request.getAccount().getClassificationCode().equals("AN"))
		{
			formatType = DocumentMapper.FORMAT_TYPE_ALL_CAPS;
		}
		Date issueDate =new SimpleDateFormat("dd/MM/yyyy").parse(this.getDateInCST());
		if(request.getFinanceDetails() != null && request.getFinanceDetails().getStartDate() != null && isClassificationCodeAN(request))
		{
			issueDate = request.getFinanceDetails().getStartDate();
		}
		if (request.getProductDetails().iterator().hasNext())  
		{
			try {				
				PdfBean pdfBean = this.generatePdf(request, request.getProductDetails(), issueDate);
				if (pdfBean != null) 
				{
					ByteArrayInputStream = writePdfToFile(pdfBean, formatType, template);
					//pdfFileName = pdfFileName.replaceAll(".pdf$", "");
//					String userName = request.getCallingApp().getServiceSecurity() != null
//							&& request.getCallingApp().getServiceSecurity().getUsername() != null ? request.getCallingApp().getServiceSecurity().getUsername()
//							: "anonymous";
					//File file = new File(MessageFormat.format("{0}{1}.pdf", OUTPUT_PATH, pdfFileName));
					//byte[] fileByteArray = Files.toByteArray(file);
//					Long id = formsDAO.storeForm(request.getQuoteId(), request.getContractNumber(), issueDate, productDetail.getType(), request.getCallingApp()
//							.getSourceSystem(), pdfFileName, productDetail.getForm().getType(), fileByteArray, userName);
//					String url = MessageFormat.format("{0}/FormsService/pdf/{1,number,#}", System.getProperty("forms.host.url"), id);
//					if (request.isUrlOnly()) {
						//pdfs.add(new Pdf(productDetail.getProductDetailType(), "", productDetail.getFormNumber(), new Date()));
//					} else {
					pdfs.add(new Pdf(request.getProductDetails().iterator().next().getProductDetailType(), "", ByteArrayInputStream, request.getProductDetails().iterator().next().getFormNumber(), issueDate));
//					}
					//FileUtils.delete(file);
					
				}
			}
			catch (FileNotFoundException fileException) 
			{
				fileException.printStackTrace();
				LegacyDocumentMapper.log.error("Error while trying to get the form.");
				Set errors = new HashSet();
				errors.add(new Error("error", 
						MessageFormat.format("The {0} form template could not be found.", request.getProductDetails().iterator().next().getFormNumber())));
			}
			catch (IOException ioException)
			{
				LegacyDocumentMapper.log.error("IO Error.");
				Set errors = new HashSet();
				errors.add(new Error("error", MessageFormat.format("The {0} IO Error.", request.getProductDetails().iterator().next()
						.getFormNumber())));
			} 
			catch (Exception exception)
			{
				LegacyDocumentMapper.log.error("Exception Error.");
				Set errors = new HashSet();
				errors.add(new Error("error", MessageFormat.format("The {0} exception error.", request.getProductDetails().iterator().next().getFormNumber())));
			}			
		}
		return ByteArrayInputStream;
	}
	
	/**
	 * Return from Id from Fillers
	 * @return Form ID used to generate the PDF
	 * @param request javadoc comments
	 */
	public String getFormId(FormRequest request) 
	{
		if (request.getFillers() != null)
		{
			for (Iterator<KeyValuePair> iter = request.getFillers().iterator();iter.hasNext();) 
			{
				KeyValuePair value = iter.next();
				// if text to compare is NULL we take the first value form fillers
				if ((BUNFORMID.equals(value.getKey())) || (COMPFORMID.equals(value.getKey())))
				{				 
					return MessageFormat.format("{0}.pdf",value.getValue());
				}
			}		
		}
		
		return null;
	}

	
	/**
	 * It check in Filler has key BUNFORMID
	 * @param request
	 * @return
	 */
	private boolean isBundled(FormRequest request)
	{
		
		return (request.getFormId(BUNFORMID) != null);
	}
	
	/**
	 * It check in Filler has key COMPFORMID
	 * @param request
	 * @return
	 */
	private boolean isComplimentary(FormRequest request)
	{
		//System.out.println("isComplimentary");
		
		return (request.getFormId(COMPFORMID) != null);
	}
	
//	private boolean validateSecurity(FormRequest request) {
//		ServiceSecurity serviceSecurity = request.getCallingApp().getServiceSecurity();
//		if (serviceSecurity != null && (!StringUtils.isBlank(serviceSecurity.getToken()) || !StringUtils.isBlank(serviceSecurity.getUsername()))) {
//			return securityManager.canAccess(WS_DEALER, SERVICE, serviceSecurity.getUsername(), serviceSecurity.getToken());
//		} else {
//			return true;
//		}
//	}
	
	private Set<Error> validateContractNumber(String contractNumber, Set<ProductDetail> productDetails) 
	{
		Set<Error> errors = new HashSet<Error>();
		if (!StringUtils.isBlank(contractNumber)) 
		{
		 for (ProductDetail productDetail : productDetails) 
		 {
			if (!productDetail.isCombo())
			{
//				if (formsDAO.hasContractNumberWithProductType(contractNumber, productDetail.getType())) {
//					errors.add(new Error("formRequest.contractNumber.duplicate",
//							"The provided combination of contract number and product type was already used. [contractNumber={0},productType={1}]",
//							contractNumber, productDetail.getType()));
//				}
			}
		 }
		}
		return errors;
	}
	
	/**
	 * Fills up the pdf's vehicle information.
	 * @param request the request.
	 * @param pdf the pdf.
	 */
	private void fillUpVehicleInformation(FormRequest request, PdfBean pdf) 
	{
		LegacyDocumentMapper.log.debug("Filling up vehicle information.");
		
		pdf.addField("VIN", request.getVehicle().getVin());
		pdf.addField("VEH_PRICE", this.formatCurrencyWithoutSymbol(request.getVehicle().getVehiclePurchasePrice())); 
		pdf.addField("VEHICLE_RETAIL", this.formatCurrencyWithoutSymbol(request.getVehicle().getVehiclePurchasePrice()));
		
		pdf.addField("New_Radio", request.getVehicle().isNewCar() ? "Yes" : "No");
		pdf.addField("Used_Radio", !request.getVehicle().isNewCar() ? "Yes" : "No");
		
		if (request.getAccount().getAccountNumber().startsWith("ENT-"))
		{
			/*pdf.addField("new", request.getVehicle().isNewCar() ? "X" : "");
			pdf.addField("pre_owned", request.getVehicle().isNewCar() ? "" : "X"); */
			
			pdf.addField("New_Radio", request.getVehicle().getVehicleNewOrUsed().equalsIgnoreCase("N") ? "X" : "");
		    pdf.addField("Used_Radio", request.getVehicle().getVehicleNewOrUsed().equalsIgnoreCase("U") ? "X" : "");
		}
		
		pdf.addField("MAKE", request.getVehicle().getMake());
		pdf.addField("MODEL", request.getVehicle().getModel());
		
		if (request.getVehicle().getChassisWarrantyTermMonth() != null && request.getVehicle().getChassisWarrantyTermMonth() != "") 
		{
			pdf.addField("CHASSIS WARR TERM", request.getVehicle().getChassisWarrantyTermMonth());
		}
		if (request.getVehicle().getManufacturerWarrantyTermMonth() != null && request.getVehicle().getManufacturerWarrantyTermMonth() != "") 
		{
	        pdf.addField("MFR WARR TERM", request.getVehicle().getManufacturerWarrantyTermMonth());
		}
		
		if(isCountryBrazil(request)) {
			pdf.addField("ODOMETER", setFormatWithDotSeparator(request.getVehicle().getOdometer()));
		}
		else {
			pdf.addField("ODOMETER",numberFormat.format(request.getVehicle().getOdometer()));
		}
		
		//Added the Wrap Odometer as per JIRA TWGQA-11743
		pdf.addField("W_ODOMETER",numberFormat.format(request.getVehicle().getOdometer()));
		pdf.addField("YEAR", request.getVehicle().getYear());
		
		pdf.addField("ISDIESEL_NO", request.getVehicle().isDiesel() ? "" : "X");
		pdf.addField("ISDIESEL", request.getVehicle().isDiesel() ? "X" : "");
		pdf.addField("ISDIESEL_RADIO", request.getVehicle().isDiesel() ? "Yes" : "No");
		
		pdf.addField("IS4X4", request.getVehicle().isFourByFour() ? "X" : "");
		pdf.addField("IS4X4_NO", request.getVehicle().isFourByFour() ? "" : "X");
		pdf.addField("IS4X4_RADIO", request.getVehicle().isFourByFour() ? "Yes" : "No");
		pdf.addField("4x4_Radio", request.getVehicle().isFourByFour() ? "Yes" : "No");
				
		pdf.addField("ISTURBO_NO", request.getVehicle().isTurbo() ? "" : "X");
		pdf.addField("Diesel_Engine_Radio", request.getVehicle().isDiesel() ? "Yes" : "No");
		pdf.addField("ISTURBO", request.getVehicle().isTurbo() ? "X" : "");
		pdf.addField("Turbo_Radio", request.getVehicle().isTurbo() ? "Yes" : "No");
		pdf.addField("ISTURBO_RADIO", request.getVehicle().isTurbo() ? "Yes" : "No");
				
		pdf.addField("ISCAR", request.getVehicle().isCar() ? "X" : "");
		pdf.addField("ISTRUCK", request.getVehicle().isTruck() ? "X" : "");
		
		pdf.addField("ISCOMMERCIAL_RADIO",request.getVehicle().isCommercialUsage() ? "Yes" : "No");
		if(request.getAccount().getAccountNumber().startsWith("PR-")) {
			
			pdf.addField("ISCOMMERCIAL", request.getVehicle().isCommercialUsage() ? "Yes" : "No");
		} else
		{
			pdf.addField("ISCOMMERCIAL", request.getVehicle().isCommercialUsage() ? "X" : "");
		}
		pdf.addField("Comercial_Radio", request.getVehicle().isCommercialUsage() ? "Yes" : "No");
		if(request.getAccount().getAccountNumber().startsWith("PR-")) {
			pdf.addField("ISPERSONAL", !(request.getVehicle().isCommercialUsage()) ? "Yes" : "No");
		}
		else 
		{
			pdf.addField("ISPERSONAL", !(request.getVehicle().isCommercialUsage()) ? "X" : "");		
		}
		
		pdf.addField("Personal_Radio", !(request.getVehicle().isCommercialUsage()) ? "Yes" : "No");
		
		pdf.addField("ISHYBRID_RADIO", request.getVehicle().isHybrid() ? "Yes" : "No");
		pdf.addField("HYBRID_YES", request.getVehicle().isHybrid() ? "Yes" : "No");
		pdf.addField("HYBRID_NO", !request.getVehicle().isHybrid() ? "Yes" : "No");
		
		//TWGQA-11961
		pdf.addField("High_Mileage_Radio", request.getVehicle().getOdometer() > 75001 ? "Yes" : "No");
		
		pdf.addField("TRIMLEVEL", request.getVehicle().getTrim());

		
	}
	
	/**
	 * Fills up the pdf's insurance information.
	 * @param request the request.
	 * @param pdf the pdf.
	 */
	private void fillUpInsuranceInformation(FormRequest request, PdfBean pdf) 
	{
		LegacyDocumentMapper.log.debug("Filling up Insurance information.");
		if(request.getInsurance() !=null) {
		
		pdf.addField("INSUR_NAME", request.getInsurance().getInsuranceCarrierName());
		if(request.getInsurance().getInsuranceCarrierAddress1() != null && request.getInsurance().getInsuranceCarrierAddress1() != "" 
				&& request.getInsurance().getInsuranceCarrierAddress2() != null && request.getInsurance().getInsuranceCarrierAddress2() != "" )
		{		
		pdf.addField("INSUR_ADDRESS", (request.getInsurance().getInsuranceCarrierAddress1()) + " " +  request.getInsurance().getInsuranceCarrierAddress2()  );
		}
		else if (request.getInsurance().getInsuranceCarrierAddress1() != null && request.getInsurance().getInsuranceCarrierAddress1() != "" ) {
			pdf.addField("INSUR_ADDRESS", (request.getInsurance().getInsuranceCarrierAddress1()));	
		}
		else {
			pdf.addField("INSUR_ADDRESS", (request.getInsurance().getInsuranceCarrierAddress2()));
		}
		pdf.addField("INSUR_CITY", request.getInsurance().getInsuranceCarrierCity());
		pdf.addField("INSUR_STATE", request.getInsurance().getInsuranceCarrierState());
		pdf.addField("INSUR_ZIP", request.getInsurance().getInsuranceCarrierPostalCode());
		}
	}
	
	/**
	 * Fills up the pdf's Plan information.
	 * @param request the request.
	 * @param pdf the pdf.
	 */
	
	private void fillUpPlanInformation(FormRequest request, PdfBean pdf)
	{
		LegacyDocumentMapper.log.debug("Filling up Plan information.");
		
		if (request.getFinanceDetails().getFinanceType() != "" && request.getFinanceDetails().getFinanceType() != null) {
			pdf.addField("loan",(request.getFinanceDetails().getFinanceType().equalsIgnoreCase("L") || request.getFinanceDetails().getFinanceType().equalsIgnoreCase("F"))? "Yes" : "No");
			pdf.addField("cash", request.getFinanceDetails().getFinanceType().equalsIgnoreCase("C") ? "Yes" : "No");
			pdf.addField("loan_other", (request.getFinanceDetails().getFinanceType().equalsIgnoreCase("LO") || request.getFinanceDetails().getFinanceType().equalsIgnoreCase("LE")) ? "Yes" : "No");
			
			pdf.addField("cash", request.getFinanceDetails().getFinanceType().equalsIgnoreCase("C") ? "Yes" : "No");
		}
		pdf.addField("ISPREFERRED",StringUtils.startsWithIgnoreCase(request.getProductDetail().getCoverageName(),"Preferred") || StringUtils.startsWithIgnoreCase(request.getProductDetail().getCoverageName(),"PR Preferred") ? "Yes" : "No");
		pdf.addField("ISMAXIMUM",StringUtils.startsWithIgnoreCase(request.getProductDetail().getCoverageName(),"Maximum") || StringUtils.startsWithIgnoreCase(request.getProductDetail().getCoverageName(),"PR Maximum") ? "Yes" : "No");
		pdf.addField("ISMAXIMUMWRAP",StringUtils.startsWithIgnoreCase(request.getProductDetail().getCoverageName(),"WRAP") || StringUtils.startsWithIgnoreCase(request.getProductDetail().getCoverageName(),"PR WRAP") ? "Yes" : "No");
		
	}
	
	 /* Fills up the pdf's optional coverages information for PRVSC-1639*/
	 
		private void fillUpOptionalCoveragesInformation(FormRequest request, PdfBean pdf) {
			ArrayList optionalCoverage = (ArrayList) request.getProductDetail().getOptionalCoverages();
			Map optCoverage = null;

			for (int i = 0; i < optionalCoverage.size(); i++) {
				optCoverage = (Map) optionalCoverage.get(i);
				if(optCoverage.get("coverageType") != null)
				{
				if (optCoverage.get("coverageType").equals("LUXURY") && request.getProductDetail().getCoverageName().contains("Lux")) {
					pdf.addField("ISLUXURY", "Yes");
					pdf.addField("LUXURY_SURCHARGE", "250");
					pdf.addField("ISEXOTIC", "Yes");
					
				}
			     if (optCoverage.get("coverageType").equals("LUXURY") && (!(request.getProductDetail().getCoverageName().contains("Lux")))) {
					pdf.addField("ISLUXURY", "Yes");
					pdf.addField("LUXURY_SURCHARGE", optCoverage.get("coverageAmount"));
					pdf.addField("ISEXOTIC", "Yes");
					
				}
				if (optCoverage.get("coverageType").equals("COMMERCIAL")) {
					pdf.addField("COMMERCIAL_SURCHARGE", optCoverage.get("coverageAmount"));
				}
				if (optCoverage.get("coverageType").equals("HYBRID")) {
					pdf.addField("HYBRID_YES", "Yes");
					pdf.addField("HYBRID_SURCHARGE", optCoverage.get("coverageAmount"));
				}
				
				if (optCoverage.get("coverageType").equals("ELECTRIC")) {
					pdf.addField("HYBRID_YES", "Yes");
					pdf.addField("HYBRID_SURCHARGE", optCoverage.get("coverageAmount"));
				}
				
				if (optCoverage.get("coverageType").equals("SUR_HYBR")) {
					pdf.addField("HYBRID_YES", "Yes");
					pdf.addField("HYBRID_SURCHARGE", optCoverage.get("coverageAmount"));
				}
				}
			}
			

		}
	
	/**
	 * Fills up the pdf's customer information.
	 * @param request the request.
	 * @param pdf the pdf.
	 */
	private void fillUpContactInformation(FormRequest request, PdfBean pdf) 
	{
		LegacyDocumentMapper.log.debug("Filling up customer information.");
		Contact contact = request.getBuyer();
		
		pdf.addField("CUS_FIRSTNAME", contact.getFirstName());
		pdf.addField("CUS_LASTNAME", contact.getLastName());
		pdf.addField("CUS_INITIAL", contact.getMiddleInitialName());
		if(isCountryMX(request) || isCountryLATAM(request)) {
			pdf.addField("CUS_NAME", contact.getFullNameMX());
		}else {
			pdf.addField("CUS_NAME", contact.getFullName());	
		}		
		pdf.addField("CUS_MI", contact.getMiddleInitialName());	
		pdf.addField("CUS_ADDRESS", contact.getAddress().getAddressString());
		pdf.addField("CUS_COMPLETE_ADDRESS", contact.getAddress().getCompleteAddressString());
		pdf.addField("CUS_STATE", contact.getAddress().getStateCode());
		pdf.addField("CUS_CITY", contact.getAddress().getCity());
		pdf.addField("CUS_ZIP", contact.getAddress().getPostalCode());
		
		
		if(isCountryMX(request)) {
			pdf.addField("CUS_HOMEPHONE", this.formatPhoneMX(contact.getPhoneByType(PhoneType.Home)));
			pdf.addField("CUS_CELLPHONE",this.formatPhoneMX(contact.getPhoneByType(PhoneType.Mobile)));
			pdf.addField("CUS_WORKPHONE",this.formatPhoneMX(contact.getPhoneByType(PhoneType.Work)));
		}else {
			pdf.addField("CUS_HOMEPHONE", this.formatPhone(contact.getPhoneByType(PhoneType.Home)));
			pdf.addField("CUS_CELLPHONE",this.formatPhone(contact.getPhoneByType(PhoneType.Mobile)));
			pdf.addField("CUS_WORKPHONE",this.formatPhone(contact.getPhoneByType(PhoneType.Work)));	
		}
		pdf.addField("CUS_EMAIL_ADDRESS", contact.getEmail());
		pdf.addField("CUS_EMAIL", contact.getEmail());
		
		pdf.addField("ACCOUNTNO", contact.getMemberNumber()); //used by Gap form
		pdf.addField("MEMBERNO", contact.getMemberNumber());//used by service contract form
		contact = request.getCoBuyer();
		if (contact != null) 
		{
			pdf.addField("COMEMBER_FIRSTNAME", contact.getFirstName());
			pdf.addField("COMEMBER_LASTNAME", contact.getLastName());
			pdf.addField("COMEMBER_CUS_NAME", contact.getFullName());
			if (contact.getAddress() != null) 
			{
				if (contact.getAddress().getAddressString() != null && !(contact.getAddress().getAddressString()).equals("")) 
				{
				pdf.addField("COMEMBER_CUS_ADDRESS", contact.getAddress().getAddressString());
				}
				if (contact.getAddress().getStateCode() != null) 
				{
				pdf.addField("COMEMBER_CUS_STATE", contact.getAddress().getStateCode());
				}
				if (contact.getAddress().getCity() != null) 
				{
				pdf.addField("COMEMBER_CUS_CITY", contact.getAddress().getCity());
				}
				if (contact.getAddress().getPostalCode() != null) 
				{
				pdf.addField("COMEMBER_CUS_ZIP", contact.getAddress().getPostalCode());
				}
			}
			pdf.addField("COMEMBER_CUS_HOMEPHONE", this.formatPhone(contact.getPhoneByType(PhoneType.Home)));
			pdf.addField("COMEMBER_CUS_WORKPHONE", this.formatPhone(contact.getPhoneByType(PhoneType.Work)));
			pdf.addField("COMEMBER_CUS_CELLPHONE", this.formatPhone(contact.getPhoneByType(PhoneType.Mobile)));
			pdf.addField("COMEMBER_CUS_EMAIL_ADDRESS", contact.getEmail());
		}
	}
	
	/**
	 * Fills up the pdf's term months information.
	 * @param request the request.
	 * @param pdf the pdf.
	 */
	private void fillUpTermMonthsInformation(FormRequest request, ProductDetail productDetail, PdfBean pdf) 
	{
		LegacyDocumentMapper.log.debug("Filling up term months information.");

		boolean is2Years = productDetail.getTermMonths() == 24;
		boolean is3Years = productDetail.getTermMonths() == 36;
		boolean is4Years = productDetail.getTermMonths() == 48;
		boolean is5Years = productDetail.getTermMonths() == 60;
		boolean is6Years = productDetail.getTermMonths() == 72;
		boolean is7Years = productDetail.getTermMonths() == 84;
		boolean is3Months = productDetail.getTermMonths() == 3;
		boolean is6Months = productDetail.getTermMonths() == 6;
		boolean is12Months = productDetail.getTermMonths() == 12;
		boolean is24Months = productDetail.getTermMonths() == 24;
		boolean is36Months = productDetail.getTermMonths() == 36;
		boolean is48Months = productDetail.getTermMonths() == 48;
		boolean is72Months = productDetail.getTermMonths() == 72;
		boolean is84Months = productDetail.getTermMonths() == 84;
		boolean is120Months = productDetail.getTermMonths() == 120;
		boolean is66Months = productDetail.getTermMonths() == 66;
		boolean is75Months = productDetail.getTermMonths() == 75;
		

		pdf.addTermField("2YEARS", is2Years ? "X" : "");
		pdf.addTermField("3YEARS", is3Years ? "X" : "");
		pdf.addTermField("4YEARS", is4Years ? "X" : "");
		pdf.addTermField("5YEARS", is5Years ? "X" : "");
		pdf.addTermField("6YEARS", is6Years ? "X" : "");
		pdf.addTermField("7YEARS", is7Years ? "X" : "");
		
		//MONTHS 
		pdf.addTermField("3MONTHS", is3Months ? "X" : "");
		pdf.addTermField("6MONTHS", is6Months ? "X" : "");
		pdf.addTermField("12MONTHS", is12Months ? "X" : "");
		pdf.addTermField("24MONTHS", is24Months ? "X" : "");
		pdf.addTermField("36MONTHS", is36Months ? "X" : "");
		pdf.addTermField("48MONTHS", is48Months ? "X" : "");
		pdf.addTermField("72MONTHS", is72Months ? "X" : "");
		pdf.addTermField("60MONTHS", is5Years ? "X" : "");
		pdf.addTermField("84MONTHS", is84Months ? "X" : "");
		pdf.addTermField("120MONTHS", is120Months ? "X" : "");
		pdf.addTermField("66MONTHS", is66Months ? "X" : "");
		pdf.addTermField("75MONTHS", is75Months ? "X" : "");

		pdf.addOtherTermField("OTHER_YEARS", "X");
		pdf.addOtherTermField("OTHER_YEARS_AMT", productDetail.getTermMonths() + " Months");
		pdf.addOtherTermField("OTHER_YEARS_2", "X");
		pdf.addOtherTermField("OTHER_YEARS_AMT_2", productDetail.getTermMonths() + " Months");
		pdf.addOtherTermField("OTHER_MONTHS_1", "X");
		pdf.addOtherTermField("OTHER_MONTH_AMT_1", productDetail.getTermMonths());
		
		
		if( !is12Months && !is24Months && !is36Months && !is48Months && !is5Years)
		{
			   pdf.addField("OTHER_MONTHS", "X");  
			   pdf.addField("OTHER_MONTHS_AMT", productDetail.getTermMonths());
		}

		String productClass = null;
		if(productDetail.getProductClass() != null && productDetail.getProductClass().indexOf('|') > 0)
		{
			productClass = productDetail.getProductClass().split("\\|")[0];
		}
		else
		{
			productClass = productDetail.getProductClass();
		}
		
		if(productClass != null)
		{
			String [] splitProductClass = productClass.split(" ");
			boolean found = false;
			if(splitProductClass.length >= 2){
				for(int i = 0; i < splitProductClass.length; i++)
				{
					if(splitProductClass[i] != null && splitProductClass[i].matches("[0-9]+/[0-9]+"))
					{
						pdf.addField("Years_Miles", splitProductClass[i]);
						found = true;
						break;
					}
				}
			}
			if(!found)
			{
				pdf.addField("Years_Miles", productClass);
			}
		}
		else
		{
			pdf.addField("Years_Miles", productDetail.getProductClass());
		}
		

		String[] multipleProductClassFor12Months = new String[] { "1/100 Q Certified Wrap", "1/100 Q-Certified Wrap", "12/12 Q-Certified Wrap", "12/12 Q Certified Wrap" }; 
		String[] multipleProductClassFor24Months = new String[] { "24/100 Qcertified Wrap", "24/100 Q-Certified Wrap", "2/100 Q Certified Wrap", "2/100 Q-Certified Wrap", "2/100 Q Cert Wrap" };
		String[] multipleProductClassFor60Months = new String[] { "5/100 Q Certified Wrap", "5/100 Q-Certified Wrap" };
		String[] multipleProductClassFor84Months = new String[] { "84/100 Qcertified Wrap", "84/100 Q-Certified Wrap", "7/100 Q Certified Wrap", "7/100 Q-Certified Wrap" };
		String[] multipleProductClassFor120Months = new String[] { "120/100 Q Certified Wrap", "120/100 Q-Certified Wrap", "10/100 Q-Certified Wrap", "10/100 Q Certified Wrap" };
		String[] multipleValues = new String[] { "12/100 Qcertified Wrap", "12/100 Q-Certified Wrap" }; 
		
		/* Removed the populating the field based on Terms selected as per the JIRA TWGQA-9140*/
		if(checkMultipleValuesForCheckBox(productDetail.getProductClass(),multipleProductClassFor12Months) == "Yes"){
			pdf.addTermField("12MONTHS",checkMultipleValuesForCheckBoxModified(productDetail.getProductClass(),multipleProductClassFor12Months) );			
		}else if(checkMultipleValuesForCheckBox(productDetail.getProductClass(),multipleProductClassFor24Months)=="Yes"){
			pdf.addTermField("24MONTHS", checkMultipleValuesForCheckBoxModified(productDetail.getProductClass(),multipleProductClassFor24Months));
			pdf.addTermField("24100_Radio",checkMultipleValuesForCheckBoxModified(productDetail.getProductClass(),multipleProductClassFor24Months) );			
		}else if(checkMultipleValuesForCheckBox(productDetail.getProductClass(),multipleProductClassFor60Months) == "Yes"){
			pdf.addTermField("60MONTHS",checkMultipleValuesForCheckBoxModified(productDetail.getProductClass(),multipleProductClassFor60Months)  );
		}else if(checkMultipleValuesForCheckBox(productDetail.getProductClass(),multipleProductClassFor84Months) == "Yes"){
			pdf.addTermField("84MONTHS", checkMultipleValuesForCheckBoxModified(productDetail.getProductClass(),multipleProductClassFor84Months) );
			pdf.addTermField("84100_Radio", checkMultipleValuesForCheckBoxModified(productDetail.getProductClass(),multipleProductClassFor84Months)  );
		}else if(checkMultipleValuesForCheckBox(productDetail.getProductClass(),multipleProductClassFor120Months) == "Yes"){
			pdf.addTermField("120MONTHS",checkMultipleValuesForCheckBoxModified(productDetail.getProductClass(),multipleProductClassFor120Months) );
			pdf.addTermField("120100_Radio", checkMultipleValuesForCheckBoxModified(productDetail.getProductClass(),multipleProductClassFor120Months) );
		}else if(checkMultipleValuesForCheckBox(productDetail.getProductClass(),multipleValues) == "Yes"){
			pdf.addTermField("12100_Radio",checkMultipleValuesForCheckBoxModified(productDetail.getProductClass(),multipleValues) );
		}
	
		// As of now we dont have this product, so commenting out. 
		// Also if later sometime this product is added, make sure that its product class name is different than which we have for "60MONTHS".
		//String[] multipleProductClassFor60MonthsSL = new String[] { "4/100 Q Certified Wrap", "4/100 Q-Certified Wrap" }; 
		//pdf.addTermField("60MONTHSSL",checkMultipleValuesForCheckBox(productDetail.getProductClass(),multipleProductClassFor60MonthsSL) );		
		
	}
	
	private String checkMultipleValuesForCheckBox(String value, String[] values)
	{
		return checkForMultipleValues(value, values, "Yes","No");
	}
	
	private String checkMultipleValuesForCheckBoxModified(String value, String[] values)
	{
		return checkForMultipleValues(value, values, "X","");
	}
	
	private String checkForMultipleValues(String value, String[] values,String trueValue,String falseValue)
	{
		String newValue = falseValue;
		
		for (int i = 0; i < values.length; i++) 
		{
			String valueA = values[i];
			if (StringUtils.containsIgnoreCase(value,valueA))
			{
				newValue = trueValue;
				break;
			}		
		}
		return newValue;
	}
	
	/**
	 * Fills up the pdf's deductible information.
	 * @param request the request.
	 * @param pdf the pdf.
	 */
	private void fillUpDeductibleInformation(FormRequest request, ProductDetail productDetail, PdfBean pdf) 
	{
		LegacyDocumentMapper.log.debug("Filling up deductible information.");
		pdf.addField("ISDEDUCTIBLE0", productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 0 ? "X" : "");
		pdf.addField("ISDEDUCTIBLE25", productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 25 ? "X" : "");
		if (request.getAccount().getAccountNumber().startsWith("PR-"))
		{
			pdf.addField("ISDEDUCTIBLE50", productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 50 ? "Yes" : "No");
			pdf.addField("ISDEDUCTIBLE100", productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 100 ? "Yes" : "No");
			pdf.addField("ISDEDUCTIBLE200", productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 200 ? "Yes" : "No");
		}
		else {
			pdf.addField("ISDEDUCTIBLE50", productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 50 ? "X" : "");
			pdf.addField("ISDEDUCTIBLE100", productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 100 ? "X" : "");
			pdf.addField("ISDEDUCTIBLE200", productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 200 ? "X" : "");
		}
		
		pdf.addField("ISDEDUCTIBLE500", productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 500 ? "X" : "");
		
		// PS-14996 changes
		if (productDetail.isDisappearing() || productDetail.isReducing()) {
			pdf.addField("REDUCINGAMOUNT", this.formatCurrencyWithSymbol(productDetail.getReducedDeductible()));
			pdf.addField("DEDUCTIBLE_WAIVED_AMOUNT",
					this.formatCurrencyWithSymbol(productDetail.getReducedDeductible()));
		} else {
			pdf.addField("REDUCINGAMOUNT", this.formatCurrencyWithSymbol(productDetail.getDeductibleAmount()));
			pdf.addField("DEDUCTIBLE_WAIVED_AMOUNT",
					this.formatCurrencyWithSymbol(productDetail.getDeductibleAmount()));
		}
		
		if(isCountryMX(request)) {
			pdf.addField("DEDUCTIBLE", this.formatCurrencyWithSymbol(productDetail.getDeductibleAmount()));
		}
		else
		{
			pdf.addField("DEDUCTIBLE", this.formatCurrencyWithoutSymbol(productDetail.getDeductibleAmount()));
		}
		
		if (productDetail.isDisappearing())
		{
			pdf.addField("ISDEDUCTIBLEU100", productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 100 ? "X" : "");
			pdf.addField("ISDEDUCTIBLEU250", productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 250 ? "X" : "");			
		}
		else
		{
			if (request.getAccount().getAccountNumber().startsWith("PR-"))
			{	
			pdf.addField("ISDEDUCTIBLE100", productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 100 ? "Yes" : "");
			}
			else{
			pdf.addField("ISDEDUCTIBLE100", productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 100 ? "X" : "");
			}
			pdf.addField("ISDEDUCTIBLE150", productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 150 ? "X" : "");
			pdf.addField("ISDEDUCTIBLE250", productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 250 ? "X" : "");
		}
	

		boolean isDeductibleRadio0 = productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 0 && !productDetail.isDisappearing();
		boolean isDeductibleRadio25 = productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 25 && !productDetail.isDisappearing();
		boolean isDeductibleRadio50 = productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 50 && !productDetail.isDisappearing();
		boolean isDeductibleRadio100 = productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 100 && !productDetail.isDisappearing();
		boolean isDisapearingadio100;
		if (FormRequest.getIsRequestTypeJSON()){
			isDisapearingadio100 = ((productDetail.getDeductibleAmount().intValue() == 100) && productDetail.getDeductibleAmount() != productDetail.getReducedDeductible().doubleValue());
		} else {
			isDisapearingadio100 = productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 100 && productDetail.isDisappearing(); 
		}
		boolean isDeductibleRadio200 = productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 200 && !productDetail.isDisappearing();
		boolean isDeductibleRadio250 = productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 250 && !productDetail.isDisappearing();		
		boolean isDeductibleRadio300 = productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 300 && !productDetail.isDisappearing();
		boolean isDeductibleRadio500 = productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 500 && !productDetail.isDisappearing();
		boolean isDeductibleRadio1000 = productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 1000 && !productDetail.isDisappearing();
		
		if((request.getAccount().getAccountNumber().startsWith("RT66-")) || (request.getAccount().getAccountNumber().startsWith("CPW-"))) {
            isDeductibleRadio100 = productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 100;
            isDeductibleRadio200 = productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 200;
            isDeductibleRadio250 = productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 250;                    
            isDeductibleRadio500 = productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 500;
            pdf.addField("DEDUCTIBLE", "$" +formatCurrencyWithoutSymbolInt(productDetail.getDeductibleAmount()));
		}
		
		pdf.addField("ISDISAPPEARINGRADIO100", isDisapearingadio100 ? "Yes" : "No");
		pdf.addField("ISDISAPPEARINGRADIO", isDisapearingadio100 ? "Yes" : "No");
		pdf.addField("ISDEDUCTIBLERADIO0", isDeductibleRadio0 ? "Yes" : "No");
		pdf.addField("ISDEDUCTIBLERADIO25", isDeductibleRadio25 ? "Yes" : "No");
		pdf.addField("ISDEDUCTIBLERADIO50", isDeductibleRadio50 ? "Yes" : "No");
		pdf.addField("ISDEDUCTIBLERADIO100", isDeductibleRadio100 ? "Yes" : "No");
		pdf.addField("ISDEDUCTIBLERADIO200", isDeductibleRadio200 ? "Yes" : "No");
		pdf.addField("ISDEDUCTIBLERADIO250", isDeductibleRadio250 ? "Yes" : "No");
		pdf.addField("ISDEDUCTIBLERADIO300", isDeductibleRadio300 ? "Yes" : "No");
		pdf.addField("ISDEDUCTIBLERADIO500", isDeductibleRadio500 ? "Yes" : "No");
		pdf.addField("ISDEDUCTIBLERADIO1000", isDeductibleRadio1000 ? "Yes" : "No");
		
		if (!productDetail.isDisappearing() && !isDeductibleRadio25 && !isDeductibleRadio50 && !isDeductibleRadio100) 
		{
			pdf.addField("ISDEDUCTIBLEOTHERRADIO", "Yes");
			pdf.addField("ISDEDUCTIBLEOTHERDESC", "$" +formatCurrencyWithoutSymbolInt(productDetail.getDeductibleAmount()));
		}
		else if (productDetail.isDisappearing() && !isDeductibleRadio25 && !isDeductibleRadio50 && !isDeductibleRadio100) 
		{
			pdf.addField("ISDEDUCTIBLEOTHERRADIO", productDetail.isDisappearing() ? "Yes" : "No");
			pdf.addField("ISDEDUCTIBLEOTHERDESC",productDetail.isDisappearing() ? MessageFormat.format("{0} Disappearing", productDetail.getDeductibleAmount() != null ? "$" + formatCurrencyWithoutSymbolInt(productDetail.getDeductibleAmount()) : 0) : "");
		}
	}
	
	private void fillUpEngineOilTypeInformation(FormRequest request, ProductDetail productDetail, PdfBean pdf)
	{
		LegacyDocumentMapper.log.debug("Filling up Engile Oil information.");
		if(productDetail.getCoverageName() != null)
		{
			pdf.addField("Class1_Radio", (StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Class 1 Conventional Oil") 
					|| StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Class 1")
					|| StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Class1"))? "Yes" : "No");
			pdf.addField("Class2_Radio", (StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Class 2 Synthetic Oil") 
					|| StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Class 2")
					|| StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Class2"))? "Yes" : "No");
			pdf.addField("Class3_Radio", (StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Class 3 Diesel/Premium") 
					|| StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Class 3")
					|| StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Class3"))? "Yes" : "No");
		}
	}
	/**
	 * Fills up the pdf's term miles information.
	 * @param request the request.
	 * @param pdf the pdf.
	 */
	private void fillUpTermMilesInformation(FormRequest request, ProductDetail productDetail, PdfBean pdf) 
	{
		LegacyDocumentMapper.log.debug("Filling up term miles information.");
		
		boolean is3KMiles = productDetail.getTermDistance() == 3000;
		boolean is6KMiles = productDetail.getTermDistance() == 6000;
		boolean is12KMiles = productDetail.getTermDistance() == 12000;
		boolean is24KMiles = productDetail.getTermDistance() == 24000;
		boolean is36KMiles = productDetail.getTermDistance() == 36000;
		boolean is40KMiles = productDetail.getTermDistance() == 40000;
		boolean is45KMiles = productDetail.getTermDistance() == 45000;
		boolean is48KMiles = productDetail.getTermDistance() == 48000;
		boolean is50KMiles = productDetail.getTermDistance() == 50000;
		boolean is60KMiles = productDetail.getTermDistance() == 60000;
		boolean is70KMiles = productDetail.getTermDistance() == 70000;
		boolean is72KMiles = productDetail.getTermDistance() == 72000;
		boolean is75KMiles = productDetail.getTermDistance() == 75000;
		boolean is80KMiles = productDetail.getTermDistance() == 80000;
		boolean is85KMiles = productDetail.getTermDistance() == 85000;
		boolean is100KMiles = productDetail.getTermDistance() == 100000;
		
		pdf.addMilesField("3K_MILES", is3KMiles ? "X" : "");
		pdf.addMilesField("6K_MILES", is6KMiles ? "X" : "");
		pdf.addMilesField("12K_MILES", is12KMiles ? "X" : "");
		pdf.addMilesField("24K_MILES", is24KMiles ? "X" : "");
		pdf.addMilesField("36K_MILES", is36KMiles ? "X" : "");
		pdf.addMilesField("40K_MILES", is40KMiles ? "X" : "");
		pdf.addMilesField("45K_MILES", is45KMiles ? "X" : "");
		pdf.addMilesField("48K_MILES", is48KMiles ? "X" : "");
		pdf.addMilesField("50K_MILES", is50KMiles ? "X" : "");
		pdf.addMilesField("60K_MILES", is60KMiles ? "X" : "");
		pdf.addMilesField("70K_MILES", is70KMiles ? "X" : "");
		pdf.addMilesField("72K_MILES", is72KMiles ? "X" : "");
		pdf.addMilesField("75K_MILES", is75KMiles ? "X" : "");
		pdf.addMilesField("80K_MILES", is80KMiles ? "X" : "");
		pdf.addMilesField("85K_MILES", is85KMiles ? "X" : "");
		pdf.addMilesField("100K_MILES", is100KMiles ? "X" : "");
		
		pdf.addOtherMilesField("OTHER_MILES", "X");
		pdf.addOtherMilesField("OTHER_MILES_AMT", numberFormat.format(productDetail.getTermDistance()));
		pdf.addOtherMilesField("OTHER_MILES_1", "X");
		pdf.addOtherMilesField("OTHER_MILES_AMT_1", numberFormat.format(productDetail.getTermDistance()));
		pdf.addOtherMilesField("OTHER_MILES_2", "X");
		pdf.addOtherMilesField("OTHER_MILES_AMT_2", numberFormat.format(productDetail.getTermDistance()));
	}
	
	
	private PdfBean generatePdf(FormRequest request, Set<ProductDetail> productDetails, Date issueDate) throws Exception 
	{
		LegacyDocumentMapper.log.info("Generating pdf.");
		LegacyDocumentMapper.log.debug(MessageFormat.format("generatePdf[productDetail={0}, issueDate={1}]", productDetails.iterator().hasNext(), this.formatDate(issueDate)));
		PdfBean pdf = new PdfBean();
		
		
		
		for (ProductDetail  productDetail: productDetails) 
		{
		
			
			
		if (ProductType.PPM.equals(productDetail.getProductDetailType()) && productDetail.isCombo()) 
		{
			return null;
		}
		
		// Retrieve system : "MR"
		
		if (FormType.Blank.equals(productDetail.getTermDistance())) 
		{			
			return pdf;
		}
				
		//For LUX product
		if (null != productDetail.getProductDetailType() && productDetail.getProductDetailType().equalsIgnoreCase("LUX"))
				{
		
		if (productDetail.getCoverageName().equalsIgnoreCase("Basic Protection"))
		{
			pdf.addField("BPFNP_Radio", "Yes");
			pdf.addField("BPFNP_COST", this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
			pdf.addField("BPFNP_ACTDATE",(request.getFinanceDetails() != null && request.getFinanceDetails().getStartDate() == null)? this.formatDate(issueDate) : this.formatDate(request.getFinanceDetails().getStartDate()));
		}
		
		if (productDetail.getCoverageName().equalsIgnoreCase("Total Protection"))
		{
			pdf.addField("TPP_Radio", "Yes");
			pdf.addField("TPP_COST", this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
			pdf.addField("TPP_ACTDATE",(request.getFinanceDetails() != null && request.getFinanceDetails().getStartDate() == null)? this.formatDate(issueDate) : this.formatDate(request.getFinanceDetails().getStartDate()));
		}
		
		if (productDetail.getCoverageName().equalsIgnoreCase("Leather Vinyl Protection"))
		{
			pdf.addField("LPFNP_Radio", "Yes");
			pdf.addField("LPFNP_COST", this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
			pdf.addField("LPFNP_ACTDATE",(request.getFinanceDetails() != null && request.getFinanceDetails().getStartDate() == null)? this.formatDate(issueDate) : this.formatDate(request.getFinanceDetails().getStartDate()));
			pdf.addField("TIP_COST", this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
		}
		
		if (productDetail.getCoverageName().equalsIgnoreCase("Paint Protection"))
		{
			pdf.addField("PPFNP_Radio", "Yes");
			pdf.addField("PPFNP_COST", this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
			pdf.addField("PPFNP_ACTDATE",(request.getFinanceDetails() != null && request.getFinanceDetails().getStartDate() == null)? this.formatDate(issueDate) : this.formatDate(request.getFinanceDetails().getStartDate()));
			pdf.addField("TIP_COST", this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
		}
		
		if (productDetail.getCoverageName().equalsIgnoreCase("Fabric Protection"))
		{
			pdf.addField("FPFNP_Radio", "Yes");
			pdf.addField("FPFNP_COST", this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
			pdf.addField("FPFNP_ACTDATE",(request.getFinanceDetails() != null && request.getFinanceDetails().getStartDate() == null)? this.formatDate(issueDate) : this.formatDate(request.getFinanceDetails().getStartDate()));
			pdf.addField("TIP_COST", this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
		}
		
		if (productDetail.getCoverageName().equalsIgnoreCase("Rust Protection"))
		{
			pdf.addField("RPFNP_Radio", "Yes");
			pdf.addField("RPFNP_COST", this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
			pdf.addField("RPFNP_ACTDATE",(request.getFinanceDetails() != null && request.getFinanceDetails().getStartDate() == null)? this.formatDate(issueDate) : this.formatDate(request.getFinanceDetails().getStartDate()));
			pdf.addField("TIP_COST", this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
		}
		
		if (productDetail.getCoverageName().equalsIgnoreCase("Undercoat/Sound Protection"))
		{
			pdf.addField("UPFNP_Radio", "Yes");
			pdf.addField("UPFNP_COST", this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
			pdf.addField("UPFNP_ACTDATE",(request.getFinanceDetails() != null && request.getFinanceDetails().getStartDate() == null)? this.formatDate(issueDate) : this.formatDate(request.getFinanceDetails().getStartDate()));
			pdf.addField("TIP_COST", this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
		}
				}
		
		setFieldsForAllProductType(request, productDetail, issueDate, pdf);
			if (isCountryMX(request)) {
				fillDataForMexico(request, pdf);
			}
			if(isCountryLATAM(request)) {
				
				fillDataForLATAM(request, pdf);
				
			}
			
			if(isCountryBrazil(request)) {
				fillDataForBrazil(request, pdf);
			}
		
		//fillUpComplimentaryInformation(request, productDetail,pdf);
		if (ProductType.WAR.name().equals(productDetail.getProductDetailType())) 
		{	
			setFieldsForProductTypeWAR(request, productDetail, pdf, issueDate);
			if (productDetail.isCombo()) 
			{
				setFieldForComboProducts(request, productDetail, issueDate, pdf);
			} 
		} 
		else if (ProductType.PPM.name().equals(productDetail.getProductDetailType()) && !productDetail.isCombo()) 
		{
			
			setFieldsForProductTypePPM(request, productDetail, pdf, issueDate);
		} 
		else if (ProductType.GAP.name().equals(productDetail.getProductDetailType())) 
		{
			setFieldsForProductTypeGAP(request, productDetail, pdf);
		} 
		else if (ProductType.DPP.name().equals(productDetail.getProductDetailType())) 
		{
			setFieldsForProductTypeDPP(request, productDetail, pdf);
		}
		else if (ProductType.MOTCL.name().equals(productDetail.getProductDetailType())) 
		{
			pdf.addField("car", request.getVehicle().isCar() ? "X" : "");
			pdf.addField("truck", request.getVehicle().isTruck() ? "X" : "");
		} 
		else if (ProductType.LW.name().equals(productDetail.getProductDetailType()) || ProductType.LWW.name().equals(productDetail.getProductDetailType()) || ProductType.LWS.name().equals(productDetail.getProductDetailType()) || ProductType.WT.name().equals(productDetail.getProductDetailType()) || ProductType.LWT.name().equals(productDetail.getProductDetailType())) 
		{
			setFieldsForLWT(request, productDetail, pdf);
		} 
	   }
		return pdf;
	}

	private void setFieldsForLWT(FormRequest request, ProductDetail productDetail, PdfBean pdf)
	{
		pdf.addField("ISSTAND", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Standard") ? "Yes" : "");
		pdf.addField("Standard_Radio", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Standard") ? "Yes" : "No");
		pdf.addField("ISOPEN_TERM", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Open") ? "Yes" : "");
		pdf.addField("DEDUCTIBLE_WAIVED", productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 0 ? "Yes" : "");
		pdf.addField("ISDEDUCTIBLE100", productDetail.getDeductibleAmount() != null && productDetail.getDeductibleAmount() == 100 ? "Yes" : "");
		pdf.addField("TOTAL_ALLOWABLE_MILES", numberFormat.format(request.getFinanceDetails().getTotalAllowableMilesContract()));
	}

	private void setFieldsForAllProductType(FormRequest request, ProductDetail productDetail, Date issueDate, PdfBean pdf)
	{
		boolean is84Months = productDetail.getTermMonths() == 84;
		boolean is24Months = productDetail.getTermMonths() == 24;
		pdf.addField("AGREEMENTNO", FormType.Final.name().equals(productDetail.getAgreementStatus()) ? request.getContractNumber() : "Sample Ineligible");
		if (!FormType.Final.name().equals(productDetail.getAgreementStatus())) 
		{
			pdf.addField("WATERMARK", "SAMPLE INELIGIBLE");
			pdf.addField("SIGNHERE", "SAMPLE INELIGIBLE");
		}
		
		if(((request.getVehicle().getVehicleNewOrUsed()) != null) && (request.getVehicle().getVehicleNewOrUsed().equalsIgnoreCase("N") || request.getVehicle().getVehicleNewOrUsed().equalsIgnoreCase("U"))){
			pdf.addField("new", request.getVehicle().getVehicleNewOrUsed().equalsIgnoreCase("N") ? "Yes" : "No");
			pdf.addField("pre_owned", request.getVehicle().getVehicleNewOrUsed().equalsIgnoreCase("U") ? "Yes" : "No");
		}
		else if ((productDetail.getCoverageName() != null) &&  ( StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "New") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Used")  ) ) {
			pdf.addField("new", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "New") ? "X" : "");
			pdf.addField("pre_owned", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Used") ? "X" : "");
		} 

		  else { pdf.addField("new", request.getVehicle().isNewCar() ? "X" : "");
		  pdf.addField("pre_owned", request.getVehicle().isNewCar() ? "" : "X"); }
		 
		 
		
		if (request.getVehicle().getVehiclePurchaseDate() != null) {
			pdf.addField(ContractFormFields.vechilePurchaseDate,
					formatDate(request.getVehicle().getVehiclePurchaseDate()));
		}
		
		if(request.getFinanceDetails().getFinancingTypeEnum() != null) {
			pdf.addField("cash", FinancingType.Cash.equals(request.getFinanceDetails().getFinancingTypeEnum()) ? "X" : "");
			pdf.addField("lease", FinancingType.Lease.equals(request.getFinanceDetails().getFinancingTypeEnum()) ? "X" : "");
			pdf.addField("balloon_loan", FinancingType.Balloon.equals(request.getFinanceDetails().getFinancingTypeEnum()) ? "X" : "");
			pdf.addField("loan", (FinancingType.Loan.equals(request.getFinanceDetails().getFinancingTypeEnum()) || FinancingType.Finance.equals(request.getFinanceDetails().getFinancingTypeEnum())) ? "X" : "");
			pdf.addField("loan_other",!FinancingType.Loan.equals(request.getFinanceDetails().getFinancingTypeEnum()) && !FinancingType.Lease.equals(request.getFinanceDetails().getFinancingTypeEnum()) && !FinancingType.Balloon.equals(request.getFinanceDetails().getFinancingTypeEnum()) ? "X" : "");
		}
				
		fillUpContactInformation(request, pdf);
		fillUpVehicleInformation(request, pdf);
		fillUpInsuranceInformation(request, pdf);
		
		if (request.getAccount().getAccountNumber().startsWith("ENT-"))
		{
			  
		   if (request.getProductDetail().getOptionalCoverages() != null && !request.getProductDetail().getOptionalCoverages().equals(null)
				 && !(request.getProductDetail().getOptionalCoverages().equals("")))
		       {
			     fillUpOptionalCoveragesInformation(request, pdf);
		       } 
		}
		
		if (request.getAccount().getAccountNumber().startsWith("PR-"))
		{
		   fillUpPlanInformation(request,pdf);
		   if (request.getProductDetail().getOptionalCoverages() != null && !request.getProductDetail().getOptionalCoverages().equals(null)
				 && !(request.getProductDetail().getOptionalCoverages().equals("")))
	       fillUpOptionalCoveragesInformation(request, pdf);
		}
		fillUpClientInformation(request, pdf);
		fillUpBundledInformation(request,pdf,issueDate);
		
		pdf.addField(getCompWrapPrefix(productDetail) + "TERM", productDetail.getTermMonths());
		if(isCompWrap(productDetail)){
			pdf.addField(getCompWrapPrefix(productDetail) + "MONTHS", productDetail.getTermMonths());
			
			}
		pdf.addField(getCompWrapPrefix(productDetail) + "YEARS", productDetail.getTermMonths() / 12);
		pdf.addField(getCompWrapPrefix(productDetail) + "MILES",numberFormat.format(productDetail.getTermDistance()));
				
		this.fillUpTermMonthsInformation(request, productDetail, pdf);
		this.fillUpTermMilesInformation(request, productDetail, pdf);
		this.fillUpEngineOilTypeInformation(request, productDetail, pdf);
		this.fillUpDeductibleInformation(request, productDetail, pdf);
		
		setExpirationDates(productDetail,request,issueDate,pdf);
		setJsonExpirationDates(productDetail,request,issueDate,pdf);
		setXmlExpirationDates(productDetail,request,issueDate,pdf);
		
		if(StringUtils.contains(productDetail.getProductClass(), "84/100")){
			pdf.addField("84MONTHS",is84Months ? "X" :"");			
		}else if(StringUtils.contains(productDetail.getProductClass(), "Q-CERT")){
			pdf.addField("24MONTHS",is24Months ? "X" : "");
		}
		
		pdf.addField("INSERVICEDATE", this.formatDate(request.getVehicle().getInServiceDate()));
		pdf.addField(getCompWrapPrefix(productDetail) + "INSERVICEDATE", this.formatDate(request.getVehicle().getInServiceDate()));
		//pdf.addField("ISPREF", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Preferred") && !productDetail.getCoverageName().contains("Hi-Tech") ? "X" : "");
		pdf.addField("ISPREM", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Premium") ? "X" : "");
		
		pdf.addField("ISSTAND", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Standard") ? "X" : "");
		pdf.addField("Standard_Radio", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Standard") ? "Yes" : "No");
		pdf.addField("ISOPEN_TERM", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Open Term") ? "Yes" : "No");
				
//		pdf.addField("EXT", (StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Exterior") && !StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Interior")) ? "X" : "");
//		pdf.addField("INT", (StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Interior") && !StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Exterior")) ? "X" : "");
//		pdf.addField("EXTINT", (StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Exterior & Interior") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Exterior and Interior") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Ext and Int") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Ext & Int")) ? "X" : "");
		
		//Added the logic as part of INT-2009
		String protectionProductPrefix = ""; 
		Boolean calTipCost = false; 
		if(StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Exterior") &&!StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Interior"))
		{ 	
			protectionProductPrefix = "EXT";
			calTipCost = true; 
			pdf.addField("PriceEXT_RQ",this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
		}
		else if(StringUtils.containsIgnoreCase (productDetail.getCoverageName(), "Interior") &&
				!StringUtils.containsIgnoreCase (productDetail.getCoverageName(), "Exterior"))
		{ 
			protectionProductPrefix = "INT";
			calTipCost = true; 
			pdf.addField("PriceINT_RI",this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
		}else if((StringUtils.containsIgnoreCase (productDetail.getCoverageName(), "Vehicle Body")
				||StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Veh Body")) && !StringUtils.containsIgnoreCase (productDetail.getCoverageName(), "Exterior")
				&& !StringUtils.containsIgnoreCase (productDetail.getCoverageName(), "Interior"))
		{ 
			protectionProductPrefix = "VBP";
			calTipCost = true; 
		}else if(StringUtils.containsIgnoreCase (productDetail.getCoverageName(), "Exterior & Interior")||StringUtils.containsIgnoreCase (productDetail.getCoverageName(), "Exterior and Interior") || 
				StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Ext and Int")||StringUtils.containsIgnoreCase (productDetail.getCoverageName(), "Ext & Int"))
		{ 
			protectionProductPrefix = "EXTINT"; 
			pdf.addField("PriceEXTINT_RS",this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
		}
		
		else if(StringUtils.containsIgnoreCase (productDetail.getCoverageName(), "Total Protection"))
		{
			protectionProductPrefix = "TPP";
		}
		if(!StringUtils.isBlank(protectionProductPrefix)) {
				pdf.addField(protectionProductPrefix, "X"); 
				pdf.addField(protectionProductPrefix + "_RADIO", "Yes"); 
				pdf.addField(protectionProductPrefix + "_Radio", "Yes"); 
				pdf.addField(protectionProductPrefix + "_COST", this. formatCurrencyWithoutSymbol (productDetail.getReportedCustomerCost())); 
				pdf.addField(protectionProductPrefix + "_MONTHS", productDetail.getTermMonths()); 
				if(calTipCost) {
					String tempTipCost = pdf.getFields().get("TEMP_TIP_COST"); 
					Double cost = 0.00; 
					if(tempTipCost != null && tempTipCost.trim().length() > 0){
						cost = Double.valueOf(tempTipCost);
					}
				pdf.addField("TIP_COST", this. formatCurrencyWithoutSymbol(cost + productDetail.getReportedCustomerCost())); 
				pdf.addField("TEMP_TIP_COST", String.valueOf(cost + productDetail.getReportedCustomerCost()));
				}
				if (FormType.Final.name().equals(productDetail.getAgreementStatus())) {
				pdf.addField(protectionProductPrefix + "_ACTDATE", (request.getFinanceDetails() != null && request.getFinanceDetails().getStartDate() == null)? this.formatDate(issueDate) : this.formatDate(request.getFinanceDetails().getStartDate()));
				}
				pdf.addField(protectionProductPrefix + "_EXPDATE", pdf.getFields().get("EXPIRATIONDATE"));
		}
		
		pdf.addField("residual_value", this.formatCurrencyWithoutSymbol(request.getFinanceDetails().getBaloonResidualValue()));
	
		
		if( request.getAccount().getAccountNumber().startsWith("PR") &&((request.getFinanceDetails().getMsrp()!= null && request.getFinanceDetails().getMsrp() > 0))){
			pdf.addField("MSRP", this.formatCurrencyWithoutSymbol(request.getFinanceDetails().getMsrp()));
		}
		else if(request.getAccount().getAccountNumber().startsWith("PR") && (request.getFinanceDetails().getNada()!= null && request.getFinanceDetails().getNada() > 0)) {
			pdf.addField("MSRP", this.formatCurrencyWithoutSymbol(request.getFinanceDetails().getNada()));
		}
		else if(request.getFinanceDetails().getMsrp()!= null && request.getFinanceDetails().getMsrp() > 0 && (request.getFinanceDetails().getNada() == null || request.getFinanceDetails().getNada()<= 0) && request.getVehicle().isNewCar() == true) {
			pdf.addField("MSRP", this.formatCurrencyWithoutSymbol(request.getFinanceDetails().getMsrp()));
		} else if(request.getFinanceDetails().getMsrp()!= null && request.getFinanceDetails().getMsrp() > 0 && (request.getFinanceDetails().getNada() == null || request.getFinanceDetails().getNada()<= 0) && request.getVehicle().isNewCar() == false) {
			pdf.addField("NADA", this.formatCurrencyWithoutSymbol(request.getFinanceDetails().getMsrp()));
		} else if(request.getFinanceDetails().getNada()!= null && request.getFinanceDetails().getNada() > 0 && (request.getFinanceDetails().getMsrp() == null || request.getFinanceDetails().getMsrp()<= 0)) {
			pdf.addField("NADA", this.formatCurrencyWithoutSymbol(request.getFinanceDetails().getNada()));
		} else if(request.getFinanceDetails().getNada()!= null && request.getFinanceDetails().getNada() > 0 && request.getFinanceDetails().getMsrp()!= null && request.getFinanceDetails().getMsrp() > 0) {
		    if(request.getVehicle().isNewCar() == true){
		    	pdf.addField("MSRP", this.formatCurrencyWithoutSymbol(request.getFinanceDetails().getMsrp()));
		    }else {
		    	pdf.addField("NADA", this.formatCurrencyWithoutSymbol(request.getFinanceDetails().getNada()));
		    }
		} else if((request.getFinanceDetails().getMsrp()== null || request.getFinanceDetails().getMsrp() <= 0) && (request.getFinanceDetails().getNada()== null || request.getFinanceDetails().getNada()<= 0)) {
			pdf.addField("NADA",this.formatCurrencyWithoutSymbol(0.0));
			pdf.addField("MSRP",this.formatCurrencyWithoutSymbol(0.0));
			
		}
		
		
		if (request.getFinanceDetails().getIsPaymentPlan() != null && request.getFinanceDetails().getIsPaymentPlan() != "" && request.getFinanceDetails().getIsPaymentPlan().equals("Y"))  
		{
			pdf.addField("Pmt Plan Yes_Radio", "Yes" );

		}
		else 
		{
			pdf.addField("Pmt Plan No_Radio", "Yes");
		}
		
	
		
		pdf.addField(getCompWrapPrefix(productDetail) + "term", productDetail.getTermMonths());
		if ((request.getFinanceDetails().getApr() != null) && (request.getFinanceDetails().getApr() > 0)) 
		{
			pdf.addField("apr", decimalFormat.format(request.getFinanceDetails().getApr()));
		}
		else
		{
			pdf.addField("apr", decimalFormat.format(0));
		}
		pdf.addField("MONTHLY_PAYMENT",this.formatCurrencyWithoutSymbol(request.getFinanceDetails().getMonthlyPaymentAmount()));
		
		//if financed amount is > 0 we print it in amount_financed field
		if ((request.getFinanceDetails().getFinancedAmount() != null) && (request.getFinanceDetails().getFinancedAmount() > 0)) 
		{
			pdf.addField("amount_financed",this.formatCurrencyWithoutSymbol(request.getFinanceDetails().getFinancedAmount()));
		}
		if (request.getFinanceDetails().getGrossCapCost() != null  && (request.getFinanceDetails().getGrossCapCost() > 0))
		{
			pdf.addField("amount_financed",this.formatCurrencyWithoutSymbol(request.getFinanceDetails().getGrossCapCost()));
		}
		
		if (request.getFinanceDetails().getGrossCapCost() != null  && (request.getFinanceDetails().getGrossCapCost() > 0))
		{
			pdf.addField("capitalized_amount",this.formatCurrencyWithoutSymbol(request.getFinanceDetails().getGrossCapCost()));
		}
		
		if(request.getFinanceDetails() !=null && request.getFinanceDetails().getStartDate()== null)
		{
			
			pdf.addField("contract_date",this.formatDate(issueDate));
		}
		else
		{
			pdf.addField("contract_date", this.formatDate(request.getFinanceDetails().getStartDate()));
		}
		pdf.addField("first_payment", this.formatDate(request.getFinanceDetails().getFirstPaymentDate()));
		
		
		// TODO: We are not using this so far.
		// pdf.addField("ISWARRANTY_YES", productDetail.isComponentCoverage() ? "X" : "");
		// pdf.addField("ISWARRANTY_NO", !productDetail.isComponentCoverage() ? "X" : "");			
		if(isCountryMX(request) || isCountryBrazil(request)){
			pdf.addField("COST",this.formatCurrencyWithSymbol(productDetail.getReportedCustomerCost()));
		}else {
			pdf.addField("COST",this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
		}		
		pdf.addField("SALESTAX",productDetail.getSalesTax() != null && productDetail.getSalesTax() > 0 ? this.formatCurrencyWithoutSymbol(productDetail.getSalesTax()) : "");
		pdf.addField("TOTALCOST",this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost() + (productDetail.getSalesTax() != null ? productDetail.getSalesTax() : 0)));
		String ppmPrefix = ProductType.PPM.equals(productDetail.getProductDetailType()) ? "_OP" : "";
		pdf.addField("GST",productDetail.getGST() != null && productDetail.getGST() > 0 ? this.formatCurrencyWithoutSymbol(productDetail.getGST()) : "");
		pdf.addField("PST",productDetail.getPST() != null && productDetail.getPST() > 0 ? this.formatCurrencyWithoutSymbol(productDetail.getPST()) : "");
		pdf.addField("TotalPriceCanada",this.formatCurrencyWithoutSymbol((productDetail.getReportedCustomerCost() != null ? productDetail.getReportedCustomerCost() : 0) + (productDetail.getGST() != null ? productDetail.getGST() : 0) + (productDetail.getPST() != null ? productDetail.getPST() : 0)));
		
		/*populated for all the Product type except KEY,TWP,PDR,WNS*/
		if (!ProductType.WNS.toString().equals(productDetail.getProductDetailType()) && !ProductType.PDR.toString().equals(productDetail.getProductDetailType())
				&& !ProductType.KEY.toString().equals(productDetail.getProductDetailType()) && !ProductType.TWP.toString().equals(productDetail.getProductDetailType()) ){
			if (request.getFinanceDetails() != null && request.getFinanceDetails().getStartDate() == null) {
				pdf.addField("ACTIVATIONDATE" + ppmPrefix, this.formatDate(issueDate));

			} else {
				pdf.addField("ACTIVATIONDATE" + ppmPrefix, this.formatDate(request.getFinanceDetails().getStartDate()));

			}
		}
		
		if (ProductType.WNS.toString().equals(productDetail.getProductDetailType()) || ProductType.PDR.toString().equals(productDetail.getProductDetailType())
				|| ProductType.KEY.toString().equals(productDetail.getProductDetailType()) || ProductType.TWP.toString().equals(productDetail.getProductDetailType()) || ProductType.MC.toString().equals(productDetail.getProductDetailType())) {
		if(request.getFinanceDetails() !=null && request.getFinanceDetails().getStartDate()== null )
		{
			
			pdf.addField("PP_ACTIVATIONDATE", this.formatDate(issueDate));
		}
		else
		{
			
			pdf.addField("PP_ACTIVATIONDATE", this.formatDate(request.getFinanceDetails().getStartDate()));
		}
		}
		
		if (!request.getFsmName().isEmpty()) {
			pdf.addField("REPRESENTATIVE_NAME", request.getFsmName());
		} else {
			pdf.addField("REPRESENTATIVE_NAME", request.getAccount().getFieldRep());
		}
		pdf.addField("REPRESENTATIVE_ID", "");
		pdf.addField("gap_charge", this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
		
		pdf.addField("PT", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "PLAN P") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(),"POWERTRAIN") ? "Yes" : "No");
		pdf.addField("PC", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "PLAN A") ? "Yes" : "No");
		pdf.addField("PM", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "PLAN B") ? "Yes" : "No");
		pdf.addField("CA", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "PLAN CA") ? "Yes" : "No");
		pdf.addField("CB", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "PLAN CB") ? "Yes" : "No");
		pdf.addField("PP", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "PLAN D") ? "Yes" : "No");
		pdf.addField("PU", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "PLAN E") ? "Yes" : "No");
		pdf.addField("PH", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "PLAN F") ? "Yes" : "No");
		
		pdf.addField("COVERAGEPLAN",productDetail.getCoverageName());
		pdf.addField(getCompWrapPrefix(productDetail) + "COVERAGEPLAN", productDetail.getCoverageName());
		pdf.addField("FACTORY_NAV_RADIO", request.getVehicle().getHasNavigation() ? "Yes" : "No");
		
		pdf.addField("CUS_PGM_PHONE", this.formatPhone(request.getBuyer().getMainPhone()));
		
		Contact contact = request.getLienHolder();
		if (contact != null) 
		{
			pdf.addField("LIEN_NAME", contact.getFullName());
			pdf.addField("lholder_name", contact.getFullName());
			if (contact.getAddress().getAddressString() != null && !(contact.getAddress().getAddressString()).equals("")) 
			{
			pdf.addField("LIEN_ADDRESS", contact.getAddress().getAddressString());
			}
			if (contact.getAddress().getAddressString() != null && !(contact.getAddress().getAddressString()).equals("")) 
			{
			pdf.addField("lholder_address", contact.getAddress().getAddressString());
			}
			pdf.addField("LIEN_CITY", contact.getAddress().getCity());
			if (contact.getAddress().getStateCode() != null && !(contact.getAddress().getStateCode()).equals("")) 
			{
			pdf.addField("LIEN_STATE", contact.getAddress().getStateCode().toUpperCase());
			}
			pdf.addField("LIEN_ZIP", contact.getAddress().getPostalCode());
			pdf.addField("LIEN_PHONE", this.formatPhone(contact.getMainPhone()));
		}
		pdf.addField("LIEN_TERM", request.getFinanceDetails().getNumberOfPayments());
		if(request.getFinanceDetails().getFinancingTypeEnum() != null) {
			pdf.addField("LIEN_TYPE", request.getFinanceDetails().getFinancingTypeEnum().toString());
		}
		if (!request.getFsmName().isEmpty()) {
			pdf.addField("CREDITOR_SIGN", request.getFsmName());
		} else {
			pdf.addField("CREDITOR_SIGN", request.getAccount().getFieldRep());
		}
		if (!request.getFsmName().isEmpty()) {
			pdf.addField("AGENT_SIGN", request.getFsmName());
		} else {
			pdf.addField("AGENT_SIGN", request.getAccount().getFieldRep());
		}
		
		/*pdf.addField("New_Radio", request.getVehicle().isNewCar() ? "Yes" : "No");
		pdf.addField("Used_Radio", !request.getVehicle().isNewCar() ? "Yes" : "No");*/
		
		ProductDetail firstProductDetail =(ProductDetail) request.getProductDetails().toArray()[0]; 
		pdf.addField("PIN",firstProductDetail.getSystemPin());
		
		pdf.addField("VEHICLECLASS", request.getVehicle().getVehicleClass());
		
		pdf.addField("Base_Radio", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "Base") ? "Yes" : "No");
		pdf.addField("High-Tech_Radio", !StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Wrap") ?checkMultipleValuesForCheckBox(productDetail.getCoverageName(), new String[] {"High Tech","High-Tech"}) : "No");
		pdf.addField("Comprehensive_Radio", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "Comprehensive") && !StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Wrap") ? "Yes" : "No");
		pdf.addField("Stated_Radio", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "Stated") ? "Yes" : "No");
		pdf.addField("High-Tech_Wrap_Radio", checkMultipleValuesForCheckBox(productDetail.getCoverageName(), new String[] {"High Tech Wrap","High-Tech Wrap"}));
		
		pdf.addField("Comprehensive_Wrap_Radio", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "Comprehensive Wrap") ? "Yes" : "No");
		
		pdf.addField("New Unit Plan_Radio", productDetail.getCoverageName().contains("New") ? "Yes" : "No");
		pdf.addField("PreOwned Unit_Radio", (productDetail.getCoverageName().contains("Pre-owned") || productDetail.getCoverageName().contains("Used")) ? "Yes" : "No");
		pdf.addField("ChassisCoach_Radio", (productDetail.getCoverageName().contains("New Motor Home") || productDetail.getCoverageName().contains("Used Motor Home") || productDetail.getCoverageName().contains("New Motorhome") || productDetail.getCoverageName().contains("Used Motorhome")) ? "Yes" : "No");
		pdf.addField("CoachOnly_Radio", (productDetail.getCoverageName().contains("New Coach Only") || productDetail.getCoverageName().contains("Used Coach Only")) ? "Yes" : "No");
		pdf.addField("PTWrap_Radio", productDetail.getCoverageName().contains("Wrap") ? "Yes" : "No");
		pdf.addField("ISDIMND", productDetail.getCoverageName().contains("Diamond") ? "Yes" : "No");
		pdf.addField("Scooter", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Scooters") ? "X" : "");
		pdf.addField("Motorcycle", (StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "On-Road Motorcycle") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Harley Buell") ) ? "X" : "");
		pdf.addField("FactoryTrike", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Trike") ? "X" : "");
		pdf.addField("OffRoadMotorcycle", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Off-Road Motorcycle") ? "X" : "");
		pdf.addField("ATV", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), " ATV ") ? "X" : "");
		pdf.addField("SidebySide", (StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Side by Side") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), " UTV ") ) ? "X" : "");
		pdf.addField("Personal Watercraft", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Personal Watercraft") ? "X" : "");
		pdf.addField("Snowmobile", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Snowmobile") ? "X" : "");
		pdf.addField("Ultra", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Ultra") ? "X" : "");
		pdf.addField("Primary", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Primary") ? "X" : "");
		
		
		
		//populating unit plan information
		pdf.addField("MotorHome_Radio", StringUtils.containsIgnoreCase(';'+request.getVehicle().getModel()+';', ";MOTORHOME;") || StringUtils.containsIgnoreCase(';'+request.getVehicle().getModel()+';', ";MOTOR HOME;") || request.getVehicle().getVehicleType().contains("RM") ? "Yes" : "No");
        pdf.addField("Coach_Radio", StringUtils.containsIgnoreCase(';'+request.getVehicle().getModel()+';', ";COACHONLY;") || StringUtils.containsIgnoreCase(';'+request.getVehicle().getModel()+';', ";COACH;") || StringUtils.containsIgnoreCase(';'+request.getVehicle().getModel()+';', ";COACH ONLY;") ? "Yes" : "No");
        pdf.addField("TravelTrailer_Radio", StringUtils.containsIgnoreCase(';'+request.getVehicle().getModel()+';', ";TRAVELTRAILER;") || StringUtils.containsIgnoreCase(';'+request.getVehicle().getModel()+';', ";TRAVEL TRAILER;") || request.getVehicle().getVehicleType().contains("RT") ? "Yes" : "No");
        pdf.addField("FifthWheel_Radio", StringUtils.containsIgnoreCase(';'+request.getVehicle().getModel()+';', ";FIFTHWHEEL;") || StringUtils.containsIgnoreCase(';'+request.getVehicle().getModel()+';', ";FIFTH WHEEL;") ? "Yes" : "No");
        pdf.addField("FoldingCampers_Radio", StringUtils.containsIgnoreCase(';'+request.getVehicle().getModel()+';', ";FOLDINGCAMPERS;") || StringUtils.containsIgnoreCase(';'+request.getVehicle().getModel()+';', ";FOLDING CAMPERS;") ? "Yes" : "No");
        pdf.addField("Slide-In_Radio", StringUtils.containsIgnoreCase(';'+request.getVehicle().getModel()+';', ";SLIDEIN;") || StringUtils.containsIgnoreCase(';'+request.getVehicle().getModel()+';', ";SLIDE-IN;") || StringUtils.containsIgnoreCase(';'+request.getVehicle().getModel()+';', ";SLIDE IN;") || request.getVehicle().getVehicleType().contains("RP") || request.getVehicle().getVehicleType().contains("RS") ? "Yes" : "No");
        pdf.addField("ToyHauler_Radio", request.getVehicle().getVehicleType().contains("RH") ? "Yes" : "No");
        
        
        
        if(productDetail.getCoverageCode() != null && !(productDetail.getCoverageCode().equals("")))
        {
        	pdf.addField("dollars_3500", StringUtils.containsIgnoreCase(productDetail.getCoverageCode(), "3500 Benefit") ? "X" : "");
    		pdf.addField("dollars_6000", StringUtils.containsIgnoreCase(productDetail.getCoverageCode(), "6000 Benefit") ? "X" : "");
    		pdf.addField("dollars_10000", StringUtils.containsIgnoreCase(productDetail.getCoverageCode(), "10000 Benefit") ? "X" : "");	
        }
        else
        {
        pdf.addField("dollars_3500", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "3500 Benefit") ? "X" : "");
		pdf.addField("dollars_6000", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "6000 Benefit") ? "X" : "");
		pdf.addField("dollars_10000", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "10000 Benefit") ? "X" : "");
        }
		if (productDetail.getOptionalCoverages() != null && !productDetail.getOptionalCoverages().equals(null)
				 && !(productDetail.getOptionalCoverages().equals("")))
		{
		ArrayList optionalCoverage = (ArrayList) request.getProductDetail().getOptionalCoverages();
		Map optCoverage = null;

		for (int i = 0; i < optionalCoverage.size(); i++) {
			optCoverage = (Map) optionalCoverage.get(i);
			if(optCoverage.get("coverageType") != null)
			{
			if(optCoverage.get("coverageType").toString().equalsIgnoreCase("SUR_ELIG"))
			{
				pdf.addField("Ext Eligibility_Radio", "Yes");
			}
			if(optCoverage.get("coverageType").toString().equalsIgnoreCase("SUR_AGE"))
			{
				pdf.addField("Unit Age_Radio", "Yes");
			}
			if(optCoverage.get("coverageType").toString().equalsIgnoreCase("CNSQ_LOSS"))
			{
				pdf.addField("Consequential Loss_Radio", "Yes");
			}
			if(optCoverage.get("coverageType").toString().equalsIgnoreCase("NAVIGATION"))
			{
				pdf.addField("Nav Package_Radio", "Yes");
			}
			if(optCoverage.get("coverageType").toString().equalsIgnoreCase("POWERSURGE"))
			{
				pdf.addField("Brown Out_Radio", "Yes");
			}
			if(optCoverage.get("coverageType").toString().equalsIgnoreCase("TIRE"))    
			{
				pdf.addField("OPT_TIREPROT", "Yes");
			}
			if((optCoverage.get("coverageType").toString().equalsIgnoreCase("7_RENTAL_DAYS")))    
			{
				pdf.addField("7 Day Rental Truck", "Yes");
			}
			if((optCoverage.get("coverageType").toString().equalsIgnoreCase("14_RENTAL_DAYS")))    
			{
				pdf.addField("14 Day Rental Truck", "Yes");
			}
			
			
			  if(StringUtils.containsIgnoreCase(optCoverage.get("coverageType").toString(), "COMMERCIAL"))
			  { pdf.addField("CommSurcharge", "X"); }
			  
			  if(StringUtils.containsIgnoreCase(optCoverage.get("coverageType").toString(), "TOURING_PACKAGE")) 
			  { pdf.addField("TouringSurcharge", "X"); }
			  
			  if(StringUtils.containsIgnoreCase(optCoverage.get("coverageType").toString(), "TRAILER"))
			  { pdf.addField("TrailerSurcharge", "X"); }
			 
			
			}
			

		}
		}
		
		pdf.addField("ELECT_AUTO RENEW", (productDetail.getAutoRenew() != "" && productDetail.getAutoRenew() != null && productDetail.getAutoRenew().equals("Y")) ? "Yes" : "No");
		
		if(!StringUtils.containsIgnoreCase(productDetail.getCoverageName(),"Wrap") && !isCompWrap(productDetail) && (!ProductType.WNS.toString().equals(productDetail.getProductDetailType()) && !ProductType.PDR.toString().equals(productDetail.getProductDetailType())
				&& !ProductType.KEY.toString().equals(productDetail.getProductDetailType()) && !ProductType.TWP.toString().equals(productDetail.getProductDetailType()))){
			
			if(!StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "COACHONLY")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "COACH ONLY")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "COACH")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "TRAVELTRAILER")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "TRAVEL TRAILER")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "FIFTHWHEEL")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "FIFTH WHEEL")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "FOLDINGCAMPERS")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "FOLDING CAMPERS")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "SLIDEIN")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "SLIDE IN")
                    && !StringUtils.containsIgnoreCase(request.getVehicle().getModel(), "SLIDE-IN"))
			{
				
			if(!request.getVehicle().isNewCar())
			{
				pdf.addField("MONTHS_USED", productDetail.getTermMonths());
				pdf.addField("MILES_USED", productDetail.getTermDistance());
				pdf.addField("MONTHS_MOTORHOME_USED", productDetail.getTermMonths());
				pdf.addField("MILES_MOTORHOME_USED", productDetail.getTermDistance());
			}
			else
			{
				pdf.addField("MONTHS", productDetail.getTermMonths());
				pdf.addField("MILES",numberFormat.format(productDetail.getTermDistance()));
				pdf.addField("MONTHS_MOTORHOME", productDetail.getTermMonths());
				pdf.addField("MILES_MOTORHOME", productDetail.getTermDistance());
				
			}	
			}
			
		}
		pdf.addField("MONTHS/MILES", productDetail.getTermMonths() + "/" + productDetail.getTermDistance());	
		
		if(StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "High-Tech Wrap") || StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "High Tech Wrap") || StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "Comprehensive Wrap"))
		{
			pdf.addField(getCompWrapPrefix(productDetail)  + "MONTHS", "");
			pdf.addField(getCompWrapPrefix(productDetail)  + "MILES", "");
			pdf.addField("MONTHS_WRAP", productDetail.getTermMonths());
			pdf.addField("MILES_WRAP", numberFormat.format(productDetail.getTermDistance()));
		}
		
		pdf.addField(getCompWrapPrefix(productDetail)+ "SERVICES", productDetail.getNumberOfServices());
		pdf.addField("SERVICEINTERVAL", productDetail.getServiceInterval());
		pdf.addField("DAY_OF_PAYMENT", request.getFinanceDetails().getPaymentDueDay());
		pdf.addField("INITIAL_PAYMENT", this.formatCurrencyWithoutSymbol(request.getFinanceDetails().getInstallmentDownPayment()));
		pdf.addField("INSTALLMENT_AMOUNT", this.formatCurrencyWithoutSymbol(request.getFinanceDetails().getInstallmentAmount()));
		pdf.addField("installment_Payment_Frequency", request.getFinanceDetails().getInstallmentPaymentFrequency());
		pdf.addField("installment_Payment_Term", request.getFinanceDetails().getInstallmentPaymentTerm());
	}

	private void setFieldsForProductTypeGAP(FormRequest request,ProductDetail productDetail, PdfBean pdf) 
	{
		pdf.addField("CUS_PHONE", this.formatPhone(request.getBuyer().getMainPhone()));
		
		pdf.addField("skippedpayments", request.getFinanceDetails().getNumberOfSkipPayments());
		pdf.addField("total_payment", this.formatCurrencyWithoutSymbol(request.getFinanceDetails().getPaymentAmount()));
		pdf.addField("numeric_amount", this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
		pdf.addField("GAPID", request.getFinanceDetails().getGapIdNumber());
		pdf.addField("LOANNO", request.getFinanceDetails().getLoanNumber());
		
		
		// null?formatDate(vpl.getMaturityDate(),(Locale)sessionService.retrieve(sessionId,"WLSC_USER_LOCALE")):"");
		pdf.addField("new", request.getVehicle().isNewCar() ? "X" : "");
		pdf.addField("pre_owned", request.getVehicle().isNewCar() ? "" : "X");
		
		
		pdf.addField("car", request.getVehicle().isCar() ? "X" : "");
		pdf.addField("truck", request.getVehicle().isTruck() ? "X" : "");
		pdf.addField("van", VehicleType.Van.equals(request.getVehicle().getVehicleType()) ? "X" : "");
		pdf.addField("motorcycle", VehicleType.Motorcycle.equals(request.getVehicle().getVehicleType()) ? "X" : "");
		pdf.addField("atv", VehicleType.AllTerrain.equals(request.getVehicle().getVehicleType()) ? "X" : "");
		pdf.addField("snowmobile", VehicleType.Snowmobile.equals(request.getVehicle().getVehicleType()) ? "X" : "");
		pdf.addField("personalwatercraft", VehicleType.PersonalWatercraft.equals(request.getVehicle().getVehicleType()) ? "X" : "");
		pdf.addField("rv", VehicleType.Recreational.equals(request.getVehicle().getVehicleType()) ? "X" : "");
		pdf.addField("watercraft", VehicleType.Watercraft.equals(request.getVehicle().getVehicleType()) ? "X" : "");
		if (request.getFinanceDetails().getFinanceType() != "" && request.getFinanceDetails().getFinanceType() != null) {
			pdf.addField("loan_f", !FinancingType.Lease.equals(request.getFinanceDetails().getFinanceType()) ? "X" : "");
		}
		
		//if Financing Type IS Lease we print the LeaseCapAmount in capitalized_amount
		if (request.getFinanceDetails().getLeaseCapAmount() > 0) 
		{	
			if(request.getFinanceDetails().getFinancingTypeEnum() != null) {
				if (FinancingType.Lease.equals(request.getFinanceDetails().getFinancingTypeEnum()))
				{
					pdf.addField("capitalized_amount",this.formatCurrencyWithoutSymbol(request.getFinanceDetails().getLeaseCapAmount()));
					// and we clear the amount_financed,both values are mutual exclusive
					pdf.addField("amount_financed", "");					
				}
			}
		}			
		
		if(request.getFinanceDetails().getFinancingTypeEnum() != null) {
			pdf.addField("lease_f", FinancingType.Lease.equals(request.getFinanceDetails().getFinancingTypeEnum()) ? "X" : "");
		}
		pdf.addField("gap", !StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "GAP+") ? "X" : "");
		pdf.addField("gap_plus", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "GAP+") ? "X" : "");
		pdf.addField("ISCOMMERCIAL", request.getVehicle().isCommercialUsage() ? "X" : "");
	}
	
	private void setFieldsForProductTypeDPP(FormRequest request,ProductDetail productDetail, PdfBean pdf) 
	{
		//if Financing Type IS Lease we print the LeaseCapAmount in capitalized_amount
		if (request.getFinanceDetails().getLeaseCapAmount() > 0) 
		{	
			if(request.getFinanceDetails().getFinancingTypeEnum() != null) {
				if (FinancingType.Lease.equals(request.getFinanceDetails().getFinancingTypeEnum()))
				{
					pdf.addField("capitalized_amount",this.formatCurrencyWithoutSymbol(request.getFinanceDetails().getLeaseCapAmount()));
					// and we clear the amount_financed,both values are mutual exclusive
					pdf.addField("amount_financed", "");					
				}
			}
		}			
		
		if(request.getFinanceDetails().getFinancingTypeEnum() != null) {
			pdf.addField("lease_f", FinancingType.Lease.equals(request.getFinanceDetails().getFinancingTypeEnum()) ? "X" : "");
		}
	}

	private void setFieldsForProductTypePPM(FormRequest request, ProductDetail productDetail, PdfBean pdf, Date issueDate) 
	{
		pdf.addField("plan_a", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Plan A") ? "X" : "");
		pdf.addField("plan_b", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Plan B") ? "X" : "");
		pdf.addField("VEHICLECLASS", request.getVehicle().getVehicleClass());
		pdf.addField("MONTHS_OP", productDetail.getTermMonths());
		pdf.addField("MILES_OP", numberFormat.format(productDetail.getTermDistance()));
		
		fillUpComplimentaryInformation(request, productDetail,pdf, issueDate);
		
		//pdf.addField("SERVICE_COST", this.formatCurrencyWithoutSymbol(productDetail.getCost()));
		pdf.addField("AGREEMENT_COST", this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
	}

	private void setFieldsForProductTypeWAR(FormRequest request,ProductDetail productDetail, PdfBean pdf, Date issueDate) 
	{
		pdf.addField("ISDEDUCTIBLEOPTIONAL", productDetail.getDeductibleAmount());
		pdf.addField("ISSILVER",StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Silver") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Silver + SP") ? "X" : "");
		pdf.addField("Silver_Radio",StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Silver") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Silver + SP") ? "Yes" : "No");
		pdf.addField(	"ISGOLD",StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Gold") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Gold + SP") ? "X" : "");
		pdf.addField("Gold_Radio",	StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Gold") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Gold + SP") ? "Yes" : "No");
		pdf.addField("ISPLATINUM",StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Platinum") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Platinum + SP") ? "X" : "");
		pdf.addField("Platinum_Radio",StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Platinum") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Platinum + SP") ? "Yes" : "No");
		pdf.addField("ISSNOWPLOW", (StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Platinum + SP") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Gold + SP") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Silver + SP") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Powertrain + SP") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Platinum +SP") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Gold +SP") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Silver +SP") || StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Powertrain +SP") || (StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Snow") && StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Plow"))) ? "X" : "");
		String[] multipleValues = new String[] { "Lifetime Q Certified Wrap", "Lifetime Q-Certified Wrap" }; 
		pdf.addTermField("Lifetime_Radio",checkMultipleValuesForCheckBox(productDetail.getProductClass(),multipleValues) );
		
		multipleValues = new String[] { "12/12 Qcertified Wrap", "12/12 Q-Certified Wrap" }; 
		pdf.addTermField("1212_Radio",checkMultipleValuesForCheckBox(productDetail.getProductClass(),multipleValues) );
		
		multipleValues = new String[] { "24/100 Qcertified Wrap", "24/100 Q-Certified Wrap", "2/100 Q Certified Wrap", "2/100 Q-Certified Wrap" }; 
		pdf.addTermField("24100_Radio",checkMultipleValuesForCheckBox(productDetail.getProductClass(),multipleValues) );
		pdf.addField("24100_Radio",checkMultipleValuesForCheckBox(productDetail.getProductClass(),multipleValues));
		
		multipleValues = new String[] { "84/100 Qcertified Wrap", "84/100 Q-Certified Wrap", "7/100 Q Certified Wrap", "7/100 Q-Certified Wrap" }; 
		pdf.addTermField("84100_Radio",checkMultipleValuesForCheckBox(productDetail.getProductClass(),multipleValues) );
		pdf.addField("84100_Radio",checkMultipleValuesForCheckBox(productDetail.getProductClass(),multipleValues));
		
		multipleValues = new String[] { "120/100 Qcertified Wrap", "120/100 Q-Certified Wrap", "10/100 Q-Certified Wrap", "10/100 Q Certified Wrap" }; 
		pdf.addTermField("120100_Radio",checkMultipleValuesForCheckBox(productDetail.getProductClass(),multipleValues) );
		pdf.addField("120100_Radio",checkMultipleValuesForCheckBox(productDetail.getProductClass(),multipleValues));
		
		multipleValues = new String[] { "12/100 Qcertified Wrap", "12/100 Q-Certified Wrap" }; 
		pdf.addTermField("12100_Radio",checkMultipleValuesForCheckBox(productDetail.getProductClass(),multipleValues) );
		pdf.addField("12100_Radio",checkMultipleValuesForCheckBox(productDetail.getProductClass(),multipleValues));
		
		pdf.addField("ISBRONZE", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Bronze") ? "X" : "");
		if(request.getAccount().getAccountNumber().startsWith("PR-"))
		{
				pdf.addField("ISPOWERTRAIN",StringUtils.startsWithIgnoreCase(request.getProductDetail().getCoverageName(),"Powertrain") || StringUtils.startsWithIgnoreCase(request.getProductDetail().getCoverageName(),"PR Powertrain") ? "Yes" : "No");	
	    }
		else {
			pdf.addField("ISPOWERTRAIN",StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(),"Powertrain") || StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(),"Powertrain + SP") ? "X" : "");
		}
		
		pdf.addField("IS_POWERTRAIN",StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(),"Powertrain") || StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(),"Powertrain + SP") ? "X" : "");
		
		pdf.addField("Power_Train_Plus_Radio",StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(),"Power Train Plus (P+)") || StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(),"Powertrain Plus")? "Yes" : "No");		
		pdf.addField("Power_Train_Radio",StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(),"Powertrain") || StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(),"Powertrain + SP") ? "Yes" : "");
		
		pdf.addField("ISPTPLUS", productDetail.getCoverageName().startsWith("Powertrain Plus")? "X" : "");
		pdf.addField("IS_BA", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(),"Base") && !StringUtils.containsIgnoreCase(productDetail.getCoverageName(),"Wrap") ? "X" : "");
		pdf.addField("IS_HT", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "Hi-Tech") || StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(),"High-Tech") && !StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Wrap")? checkMultipleValuesForCheckBoxModified(productDetail.getCoverageName(), new String[] {"Hi-Tech","High-Tech"}): "");
		pdf.addField("IS_CC", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "Comprehensive")&& ! StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Wrap")? "X" : "");
				
		pdf.addField("IS_BAW", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(),"Base Wrap") ? "X" : "");
		pdf.addField("IS_HTW", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(),"Hi-Tech Wrap") || StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "High-Tech Wrap")? checkMultipleValuesForCheckBoxModified(productDetail.getCoverageName(), new String[] {"Hi-Tech Wrap","High-Tech Wrap"}) : "");
		pdf.addField("IS_CCW", StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(),"Comprehensive Wrap") ? "X" : "");
		
		pdf.addField("ISPREFHITECH", productDetail.getCoverageName().startsWith("Preferred Hi-Tech") ? "X" : "");
		pdf.addField("Preferred_Hi-Tech_Radio",StringUtils.startsWithIgnoreCase("Preferred hiTech (PH)",productDetail.getCoverageName()) ? "Yes" : "No");		
		pdf.addField("Standard_Radio", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Standard") ? "Yes" : "No");
		
		pdf.addField("ISBASIC", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Basic") && !productDetail.getCoverageName().contains("+") && !StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Plus") ? "X" : "");
		pdf.addField(	"ISBASICPLUS",StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Basic+")
						|| StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Basic Plus") ? "X" : "");
		pdf.addField("SURCHARGE", request.getVehicle().isFourByFour() || request.getVehicle().isDiesel() || request.getVehicle().isTurbo() ? "X" : "");
		if(isCountryMX(request)) {
			pdf.addField("DEDUCTIBLE", this.formatCurrencyWithSymbol(productDetail.getDeductibleAmount()));
		}
		else
		{
			pdf.addField("DEDUCTIBLE", this.formatCurrencyWithoutSymbol(productDetail.getDeductibleAmount()));
		}
		
	    // PS-14996 changes
		
		if (productDetail.isDisappearing() || productDetail.isReducing()) {
			pdf.addField("REDUCINGAMOUNT", this.formatCurrencyWithSymbol(productDetail.getReducedDeductible()));
			pdf.addField("DEDUCTIBLE_WAIVED_AMOUNT",
					this.formatCurrencyWithSymbol(productDetail.getReducedDeductible()));
		} else {
			pdf.addField("REDUCINGAMOUNT", this.formatCurrencyWithSymbol(productDetail.getDeductibleAmount()));
			pdf.addField("DEDUCTIBLE_WAIVED_AMOUNT",
					this.formatCurrencyWithSymbol(productDetail.getDeductibleAmount()));
		}
		
		pdf.addField("PLAN_SUPREME", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "SUPREME") ? "X" : "");
		pdf.addField("PLAN_WRAP", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "WRAP") ? "X" : "");
		pdf.addField("PLAN_DELUXE", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "DELUXE") ? "X" : "");
		
		pdf.addField("PLAN_POWERTRAIN",StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(),"POWERTRAIN") ? "X" : "");
		
		pdf.addField("SALEDATE", this.formatDate(request.getVehicle().getInServiceDate()));
		//pdf.addField("VEHICLE_RETAIL", this.formatCurrencyWithoutSymbol(request.getVehicle().getVehiclePurchasePrice()));
		pdf.addField("DEDUCTIBLE_WAIVED", productDetail.isDisappearing() ? "X" : "");
		pdf.addField("SERVICE_COST", this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));

		fillUpComplimentaryInformation(request, productDetail,pdf, issueDate);
		
		pdf.addField("Elite_Radio", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Elite") && !StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Plus") && !StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "New") ? "Yes" : "No");
		pdf.addField("ElitePlus_Radio", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Elite Plus") ? "Yes" : "No");
		pdf.addField("ElitePlusGPS_Radio", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Elite New Nav/GPS") ? "Yes" : "No");
		pdf.addField("ElitePlusAirRide_Radio", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Elite New Air Ride") ? "Yes" : "No");
		pdf.addField("ElitePlusGPSAirRide_Radio", StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Elite New AirRide/Nav") ? "Yes"
				: "No");
		pdf.addField("QCERT12M", StringUtils.equals(productDetail.getProductClass(), "12/100") ? "X" : "");
		pdf.addField("QCERT24M", StringUtils.equals(productDetail.getProductClass(), "24/100") ? "X" : "");
		pdf.addField("QCERT60MPT", StringUtils.equals(productDetail.getProductClass(), "60/100 PT") ? "X" : "");
		pdf.addField("QCERT60MSL", StringUtils.equals(productDetail.getProductClass(), "60/100 SL") ? "X" : "");
		pdf.addField("QCERT84M", StringUtils.equals(productDetail.getProductClass(), "84/100") ? "X" : "");
		pdf.addField("QCERT120M", StringUtils.equals(productDetail.getProductClass(), "120/100") ? "X" : "");
	}

	private void setFieldForComboProducts(FormRequest request, ProductDetail productDetail, Date issueDate, PdfBean pdf) 
	{
		// Retrieve system : "MR"
		ProductDetail productDetailPPM = request.getProductDetailsByType(ProductType.PPM);
		if(productDetailPPM != null)
		{
			pdf.addField("plan_a", StringUtils.containsIgnoreCase(productDetailPPM.getCoverageName(), "Plan A") ? "X" : "");
			pdf.addField("plan_b", StringUtils.containsIgnoreCase(productDetailPPM.getCoverageName(), "Plan B") ? "X" : "");
			pdf.addField("MONTHS_OP", productDetailPPM.getTermMonths());
			pdf.addField("MILES_OP", productDetailPPM.getTermDistance());
			if(request.getFinanceDetails() !=null && request.getFinanceDetails().getStartDate()== null)
			{
				pdf.addField("ACTIVATIONDATE_OP", this.formatDate(issueDate));
			}
			else
			{
				pdf.addField("ACTIVATIONDATE_OP", this.formatDate(request.getFinanceDetails().getStartDate()));
			}
			pdf.addField("AGREEMENT_COST", this.formatCurrencyWithoutSymbol(productDetailPPM.getReportedCustomerCost()));
			double amount = productDetail.getReportedCustomerCost() + productDetailPPM.getReportedCustomerCost();
			pdf.addField("COST", this.formatCurrencyWithSymbol(amount));
			
			pdf.addField("ISPREF",	StringUtils.containsIgnoreCase(productDetailPPM.getCoverageName(), "Preferred") && !productDetailPPM.getCoverageName().contains("+") && !productDetailPPM.getCoverageName().contains("Plus") && !productDetailPPM.getCoverageName().contains("Hi-Tech")? "X" : "");
			pdf.addField("ISPREFPLUS", StringUtils.containsIgnoreCase(productDetailPPM.getCoverageName(), "Preferred+") || StringUtils.containsIgnoreCase(productDetailPPM.getCoverageName(), "Preferred Plus") ? "X" : "");
			pdf.addField("ISSYNTHETICOIL", StringUtils.containsIgnoreCase(productDetailPPM.getCoverageName(), "Synthetic") || StringUtils.containsIgnoreCase(productDetailPPM.getOilType(), "Synthetic")? "X" : "");
			pdf.addField("ISDIESEL", StringUtils.containsIgnoreCase(productDetailPPM.getCoverageName(), "Diesel")  || request.getVehicle().isDiesel() || StringUtils.containsIgnoreCase(productDetail.getOilType(), "Diesel") ? "X" : "");
			pdf.addField("ADDITIONALOIL", StringUtils.containsIgnoreCase(productDetailPPM.getCoverageName(), "Additional") || StringUtils.containsIgnoreCase(productDetailPPM.getCoverageName(), "Addition") || StringUtils.containsIgnoreCase(productDetailPPM.getCoverageName(), "Convntnl Oil(Over 5 Qt)") || StringUtils.containsIgnoreCase(productDetailPPM.getCoverageName(), "ConvOilOver") || StringUtils.containsIgnoreCase(productDetailPPM.getCoverageName(), "Adition") || StringUtils.containsIgnoreCase(productDetail.getOilType(), "Additional") ? "X" : "");
			pdf.addField("ADITIONALOIL", StringUtils.containsIgnoreCase(productDetailPPM.getCoverageName(), "Additional") || StringUtils.containsIgnoreCase(productDetailPPM.getCoverageName(), "Addition") || StringUtils.containsIgnoreCase(productDetailPPM.getCoverageName(), "Adition") || StringUtils.containsIgnoreCase(productDetail.getOilType(), "Additional") ? "X" : "");
			pdf.addField("ISCONVENTIONAL", StringUtils.containsIgnoreCase(productDetailPPM.getCoverageName(), "VCP CONVENTIONAL") || StringUtils.containsIgnoreCase(productDetailPPM.getOilType(), "VCP CONVENTIONAL") ? "X" : "");
			pdf.addField("ISSYNTHETIC", StringUtils.containsIgnoreCase(productDetailPPM.getCoverageName(), "VCP Full Synthetic") || StringUtils.containsIgnoreCase(productDetailPPM.getOilType(), "VCP Full Synthetic") ? "X" : "");
			pdf.addField("ISBLEND", StringUtils.containsIgnoreCase(productDetailPPM.getCoverageName(), "VCP Synthetic Blend") || StringUtils.containsIgnoreCase(productDetailPPM.getOilType(), "VCP Synthetic Blend")? "X" : "");
			pdf.addField("SERVICES", productDetailPPM.getNumberOfServices());
			pdf.addField("SERVICEINTERVAL", productDetailPPM.getServiceInterval());
			
			if(productDetailPPM.getDeductibleAmount() != null)
			{
				pdf.addField("miles_3000",	productDetailPPM.getDeductibleAmount() == 3000 ? "X" : "");
				pdf.addField("miles_3750",	productDetailPPM.getDeductibleAmount() == 3750 ? "X" : "");
				pdf.addField("miles_5000", productDetailPPM.getDeductibleAmount() == 5000 ? "X" : "");
				pdf.addField("miles_6000", productDetailPPM.getDeductibleAmount() == 6000 ? "X" : "");
				pdf.addField("miles_7500", productDetailPPM.getDeductibleAmount() == 7500 ? "X" : "");
				pdf.addField("miles_10000", productDetailPPM.getDeductibleAmount() == 10000 ? "X" : "");
				
				pdf.addField("3K_Miles_Radio", productDetailPPM.getDeductibleAmount() == 3000 ? "Yes" : "No");
				pdf.addField("3.75K_Miles_Radio", productDetailPPM.getDeductibleAmount() == 3750 ? "Yes" : "No");
				pdf.addField("5K_Miles_Radio", productDetailPPM.getDeductibleAmount() == 5000 ? "Yes" : "No");
				pdf.addField("6K_Miles_Radio", productDetailPPM.getDeductibleAmount() == 6000 ? "Yes" : "No");
				pdf.addField("7.5K_Miles_Radio", productDetailPPM.getDeductibleAmount() == 7500 ? "Yes" : "No");
				pdf.addField("10K_Miles_Radio", productDetailPPM.getDeductibleAmount() == 10000 ? "Yes" : "No");
			}
		}
		
	}

	/*private void setOutPutFileName(FormRequest request,
			ProductDetail productDetail, PdfBean pdf) {
		if (FormType.Final.equals(productDetail.getAgreementStatus())) {
			pdf.setOutputFileName(MessageFormat.format("wlsconnect{0}{1}{2}.pdf", request.getQuoteId(), productDetail.getProductDetailType(), request.getContractNumber()));
		} else {
			pdf.setOutputFileName(MessageFormat.format("wlsconnect{0}{1}.pdf", request.getQuoteId(), productDetail.getProductDetailType()));
		}
	}

	private void setInputFileName(ProductDetail productDetail, PdfBean pdf) {
		if (ProductType.WAR.equals(productDetail.getProductDetailType())) {
			pdf.setInputFileName(productDetail.getFormNumber() == null ? "AWS281CTR1207.pdf" : MessageFormat.format("{0}.pdf", productDetail.getFormNumber()));
		} else if (ProductType.PPM.equals(productDetail.getProductDetailType())) {
			pdf.setInputFileName(productDetail.getFormNumber() == null ? "MPVC011R0507.pdf" : MessageFormat.format("{0}.pdf", productDetail.getFormNumber()));
		} else if (ProductType.GAP.equals(productDetail.getProductDetailType())) {
			pdf.setInputFileName(productDetail.getFormNumber() == null ? "GAP88R0708.pdf" : MessageFormat.format("{0}.pdf", productDetail.getFormNumber()));
		} else if (ProductType.MOTCL.equals(productDetail.getProductDetailType())) {
			pdf.setInputFileName(productDetail.getFormNumber() == null ? "AWS266R0607.pdf" : MessageFormat.format("{0}.pdf", productDetail.getFormNumber()));
		} else {
			pdf.setInputFileName(MessageFormat.format("{0}.pdf", productDetail.getFormNumber()));
		}
	}*/

	/**
	 * Run Store Procedure that return expiration dates and mileage	
	 * @param pdf
	 */
	private void setExpirationDates(ProductDetail product,FormRequest request, Date issueDate, PdfBean pdf) 
	{
		boolean expDateSet = false;
		boolean isWrapProduct = isCompWrap(product);
		// set a field prefix if the product is a Wrap
		String prefix = isWrapProduct ? "W_" : "";
		if(request.getVehicle().getExpirationDate() != null && (!request.getVehicle().getExpirationDate().equals("")))
		{
			pdf.addField("maturity_date",this.formatDate(request.getVehicle().getExpirationDate()));
		}
		else
		{
		pdf.addField("maturity_date",this.formatDate(request.getVehicle().getMaturityDate()));
		}
		if(request.getVehicle().getExpirationDate() != null && (!request.getVehicle().getExpirationDate().equals("")))
		{
			pdf.addField("EXPIRATIONDATE",this.formatDate(request.getVehicle().getExpirationDate()));
			pdf.addField("PP_EXPIRATIONDATE",this.formatDate(request.getVehicle().getExpirationDate()));
		}
		else if(request.getExpirationType() != "" && request.getExpirationType() != null)
		{
			//EXPIRATIONDATE
			if(request.getExpirationType().equals("AOTAOM") || request.getExpirationType().equals("AOT") || request.getExpirationType().equals("AOTCM") || request.getExpirationType().equals("AOTAOMDAY"))
			{		
				if(request.getFinanceDetails() !=null && request.getFinanceDetails().getStartDate()== null)
				{
					
					pdf.addField(prefix +"EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, product.getTermMonths())));
				}
				else
				{
					
					pdf.addField(prefix + "EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getFinanceDetails().getStartDate(), product.getTermMonths())));
				}
				expDateSet = true;
			}
			else if (request.getExpirationType().equals("ISDTCM") || request.getExpirationType().equals("ISDT"))
			{
				pdf.addField("EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getVehicle().getInServiceDate(), request.getProductDetail().getTermMonths())));
				expDateSet = true;
			}
		}
		if(request.getVehicle().getExpirationMileage() != null && (!request.getVehicle().getExpirationMileage().equals("")))
		{
			pdf.addField("EXPIRATIONMILEAGE",request.getVehicle().getExpirationMileage());
		}
		else if(request.getExpirationType() != "" && request.getExpirationType() != null)
		{
			
			if(request.getExpirationType().equals("AOTAOM") || request.getExpirationType().equals("AOTAOMDAY"))
			{		
				pdf.addField(prefix +"EXPIRATIONMILEAGE",numberFormat.format(((request.getVehicle().getOdometer())+(product.getTermDistance()))));
			}
			else if (request.getExpirationType().equals("ISDTCM") || request.getExpirationType().equals("AOTCM") || request.getExpirationType().equals("CEDTCM") || request.getExpirationType().equals("AOT") || request.getExpirationType().equals("ISDT"))
			{	
				pdf.addField(prefix +"EXPIRATIONMILEAGE",numberFormat.format(product.getTermDistance()));
			}
			expDateSet = true;
			
		}
		
		if(request.getVehicle().getExpirationDate() != null && (!request.getVehicle().getExpirationDate().equals("")))
		{
			pdf.addField("maturity_date",this.formatDate(request.getVehicle().getExpirationDate()));
		}
		else if (request.getFinanceDetails().getScheduledTerminationDate() != null)
		{
			pdf.addField("maturity_date",this.formatDate(request.getFinanceDetails().getScheduledTerminationDate()));
		}
		else if(isDealerTrackCall(request) && (ProductType.LWS.name().equals(product.getProductDetailType()) || ProductType.LWW.name().equals(product.getProductDetailType()) || ProductType.LUX.name().equals(product.getProductDetailType())))
		{
			if(request.getFinanceDetails() !=null && request.getFinanceDetails().getStartDate()== null)
			{
				pdf.addField("maturity_date",this.formatDate(DateUtils.addMonths(issueDate, product.getTermMonths())));
			}
			else
			{
				pdf.addField("maturity_date",this.formatDate(DateUtils.addMonths(request.getFinanceDetails().getStartDate(), product.getTermMonths())));
			}
		}
		
		if(isDealerTrackCall(request) && (ProductType.LWS.equals(product.getProductDetailType()) || ProductType.LWW.equals(product.getProductDetailType()) || ProductType.LUX.equals(product.getProductDetailType())))
		{
			if(request.getVehicle().getExpirationDate() != null && (!request.getVehicle().getExpirationDate().equals("")))
			{
				pdf.addField("EXPIRATIONDATE",this.formatDate(request.getVehicle().getExpirationDate()));
			}
			else if(request.getFinanceDetails() !=null && request.getFinanceDetails().getStartDate()== null)
			{
				pdf.addField("EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, product.getTermMonths())));
			}
			else
			{
				pdf.addField("EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getFinanceDetails().getStartDate(), product.getTermMonths())));
			}
			expDateSet = true;
		}
		else if(ProductType.LWS.name().equals(product.getProductDetailType()) || ProductType.LWW.name().equals(product.getProductDetailType()))
		{
			if(request.getVehicle().getExpirationDate() != null && (!request.getVehicle().getExpirationDate().equals("")))
			{
				pdf.addField("EXPIRATIONDATE",this.formatDate(request.getVehicle().getExpirationDate()));
			}
			else if(request.getFinanceDetails().getScheduledTerminationDate() != null)
			{
				pdf.addField("EXPIRATIONDATE",this.formatDate(request.getFinanceDetails().getScheduledTerminationDate()));
				expDateSet = true;
			}
		}
		else if(ProductType.LUX.name().equals(product.getProductDetailType()))
		{
			if(request.getVehicle().getExpirationDate() != null && (!request.getVehicle().getExpirationDate().equals("")))
			{
				pdf.addField("EXPIRATIONDATE",this.formatDate(request.getVehicle().getExpirationDate()));
			}
			else if(request.getFinanceDetails() !=null && request.getFinanceDetails().getStartDate()== null){
				pdf.addField("EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, product.getTermMonths())));
			}
			else
			{
				pdf.addField("EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getFinanceDetails().getStartDate(), product.getTermMonths())));
			}
			expDateSet = true;
		} 
		else if(ProductType.WNS.name().equals(product.getProductDetailType()))
		{
			if(request.getVehicle().getExpirationDate() != null && (!request.getVehicle().getExpirationDate().equals("")))
			{
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(request.getVehicle().getExpirationDate()));
			}
			else if(request.getFinanceDetails() !=null && request.getFinanceDetails().getStartDate()== null){
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, product.getTermMonths())));
			}
			else
			{
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getFinanceDetails().getStartDate(), product.getTermMonths())));
			}
			expDateSet = true;
		}
		else if(ProductType.PDR.name().equals(product.getProductDetailType()))
		{
			if(request.getVehicle().getExpirationDate() != null && (!request.getVehicle().getExpirationDate().equals("")))
			{
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(request.getVehicle().getExpirationDate()));
			}
			else if(request.getFinanceDetails() !=null && request.getFinanceDetails().getStartDate()== null){
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, product.getTermMonths())));
			}
			else
			{
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getFinanceDetails().getStartDate(), product.getTermMonths())));
			}
			expDateSet = true;
		}
		else if(ProductType.TWP.name().equals(product.getProductDetailType()))
		{
			if(request.getVehicle().getExpirationDate() != null && (!request.getVehicle().getExpirationDate().equals("")))
			{
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(request.getVehicle().getExpirationDate()));
			}
			else if(request.getFinanceDetails() !=null && request.getFinanceDetails().getStartDate()== null){
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, product.getTermMonths())));
			}
			else
			{
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getFinanceDetails().getStartDate(), product.getTermMonths())));
			}
			expDateSet = true;
		}
		else if(ProductType.TWB.name().equals(product.getProductDetailType()))
		{
			if(request.getVehicle().getExpirationDate() != null && (!request.getVehicle().getExpirationDate().equals("")))
			{
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(request.getVehicle().getExpirationDate()));
			}
			else if(request.getFinanceDetails() !=null && request.getFinanceDetails().getStartDate()== null){
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, product.getTermMonths())));
			}
			else
			{
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getFinanceDetails().getStartDate(), product.getTermMonths())));
			}
			expDateSet = true;
		}
		else if(ProductType.KEY.name().equals(product.getProductDetailType()))
		{
			if(request.getVehicle().getExpirationDate() != null && (!request.getVehicle().getExpirationDate().equals("")))
			{
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(request.getVehicle().getExpirationDate()));
				
			}
			else if(request.getFinanceDetails() !=null && request.getFinanceDetails().getStartDate()== null){
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, product.getTermMonths())));
			}
			else
			{
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getFinanceDetails().getStartDate(), product.getTermMonths())));
			}
			expDateSet = true;
		}
		else if(ProductType.BUN.name().equals(product.getProductDetailType()))
		{
			if(request.getVehicle().getExpirationDate() != null && (!request.getVehicle().getExpirationDate().equals("")))
			{
				pdf.addField("EXPIRATIONDATE",this.formatDate(request.getVehicle().getExpirationDate()));
			}
			else if(request.getFinanceDetails() !=null && request.getFinanceDetails().getStartDate()== null){
				pdf.addField("EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, product.getTermMonths())));
			}
			else
			{
				pdf.addField("EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getFinanceDetails().getStartDate(), product.getTermMonths())));
			}
			expDateSet = true;
		}
		
		
			
		
	}
	
	private void setJsonExpirationDates(ProductDetail product,FormRequest request, Date issueDate, PdfBean pdf) 
	{
		boolean isWrapProduct = isCompWrap(product);
		// set a field prefix if the product is a Wrap
		String prefix = isWrapProduct ? "W_" : "";
		if(request.getVehicle().getExpirationDate() != null && (!request.getVehicle().getExpirationDate().equals("")))
		{
			pdf.addField("EXPIRATIONDATE",this.formatDate(request.getVehicle().getExpirationDate()));
			pdf.addField("PP_EXPIRATIONDATE",this.formatDate(request.getVehicle().getExpirationDate()));
			pdf.addField("maturity_date",this.formatDate(request.getVehicle().getExpirationDate()));
		}
		else if(request.getExpirationType() != "" && request.getExpirationType() != null)
		{
			//EXPIRATIONDATE
			if(request.getExpirationType().equals("AOTAOM") || request.getExpirationType().equals("AOT") || request.getExpirationType().equals("AOTCM") || request.getExpirationType().equals("AOTAOMDAY"))
			{		
				if(request.getFinanceDetails() !=null && request.getFinanceDetails().getStartDate()== null)
				{
					pdf.addField(prefix +"EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, product.getTermMonths())));
					pdf.addField("PP_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, product.getTermMonths())));
				}
				else
				{
				pdf.addField(prefix + "EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getFinanceDetails().getStartDate(), product.getTermMonths())));
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getFinanceDetails().getStartDate(), product.getTermMonths())));
				}
			}
			else if (request.getExpirationType().equals("CEDTCM"))
			{		
				if(request.getFinanceDetails() !=null && request.getFinanceDetails().getStartDate()== null)
				{
					pdf.addField(prefix +"EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, product.getTermMonths())));
					pdf.addField("PP_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, product.getTermMonths())));
				}
				else
				{
				pdf.addField(prefix + "EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getVehicle().getInServiceDate(), product.getTermMonths())));
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getVehicle().getInServiceDate(), product.getTermMonths())));
				}
			}
			else if (request.getExpirationType().equals("ISDTCM") || request.getExpirationType().equals("ISDT"))
			{
				pdf.addField(prefix + "EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getVehicle().getInServiceDate(), product.getTermMonths())));
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getVehicle().getInServiceDate(), product.getTermMonths())));
			}
			
			
			//MATURITYDATE
			if(request.getExpirationType().equals("AOTAOM") || request.getExpirationType().equals("AOT") || request.getExpirationType().equals("AOTCM"))
			{	
				if(request.getFinanceDetails() !=null && request.getFinanceDetails().getStartDate()== null)
				{
					pdf.addField(prefix + "maturity_date",this.formatDate(DateUtils.addMonths(issueDate, product.getTermMonths())));
				}
				else
				{
				pdf.addField(prefix + "maturity_date",this.formatDate(DateUtils.addMonths(request.getFinanceDetails().getStartDate(), product.getTermMonths())));
				}
			}
			else if (request.getExpirationType().equals("ISDTCM") || request.getExpirationType().equals("ISDT") || request.getExpirationType().equals("CEDTCM"))
			{	
				pdf.addField(prefix + "maturity_date",this.formatDate(DateUtils.addMonths(request.getVehicle().getInServiceDate(), product.getTermMonths())));
			}
		}
		if(request.getVehicle().getExpirationMileage() != null && (!request.getVehicle().getExpirationMileage().equals("")))
		{
			pdf.addField("EXPIRATIONMILEAGE",request.getVehicle().getExpirationMileage());
		}
		else if(request.getExpirationType() != "" && request.getExpirationType() != null)
		{
			
			//EXPIRATIONMILEAGE
			if(request.getExpirationType().equals("AOTAOM") || request.getExpirationType().equals("AOTAOMDAY"))
			{		
				pdf.addField(prefix + "EXPIRATIONMILEAGE",numberFormat.format(((request.getVehicle().getOdometer())+(product.getTermDistance()))));
			}
			else if (request.getExpirationType().equals("ISDTCM") || request.getExpirationType().equals("AOTCM") || request.getExpirationType().equals("CEDTCM") || request.getExpirationType().equals("AOT") || request.getExpirationType().equals("ISDT"))
			{	
				pdf.addField(prefix + "EXPIRATIONMILEAGE",numberFormat.format(product.getTermDistance()));
			}
			
			
		}
		
	}
	
	
	private void setXmlExpirationDates(ProductDetail product,FormRequest request, Date issueDate, PdfBean pdf) 
	{
		
		boolean isWrapProduct = isCompWrap(product);
		// set a field prefix if the product is a Wrap
	
		String prefix = isWrapProduct ? "W_" : "";
		if(request.getVehicle().getExpirationDate() != null && (!request.getVehicle().getExpirationDate().equals("")))
		{
			pdf.addField("EXPIRATIONDATE",this.formatDate(request.getVehicle().getExpirationDate()));
			pdf.addField("PP_EXPIRATIONDATE",this.formatDate(request.getVehicle().getExpirationDate()));
			pdf.addField("maturity_date",this.formatDate(request.getVehicle().getExpirationDate()));
		}
		else if(request.getExpirationType() != "" && request.getExpirationType() != null)
		{
			//EXPIRATIONDATE
			if(request.getExpirationType().equals("AOTAOM") || request.getExpirationType().equals("AOT") || request.getExpirationType().equals("AOTCM") || request.getExpirationType().equals("AOTAOMDAY"))
			{		
				if(request.getFinanceDetails() !=null && request.getFinanceDetails().getStartDate()== null)
				{
					pdf.addField(prefix +"EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, product.getTermMonths())));
					pdf.addField("PP_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, product.getTermMonths())));
				}
				else
				{
				pdf.addField(prefix + "EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getFinanceDetails().getStartDate(), product.getTermMonths())));
				/* Implemented as part of TWGQA-9504 */
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getFinanceDetails().getStartDate(), product.getTermMonths())));
				}
			}
			else if (request.getExpirationType().equals("ISDTCM") || request.getExpirationType().equals("ISDT") || request.getExpirationType().equals("CEDTCM"))
			{		
				pdf.addField(prefix + "EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getVehicle().getInServiceDate(), product.getTermMonths())));
				/* Implemented as part of TWGQA-9504 */
				pdf.addField("PP_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(request.getVehicle().getInServiceDate(), product.getTermMonths())));
			}
			
			//MATURITYDATE
			if(request.getExpirationType().equals("AOTAOM") || request.getExpirationType().equals("AOT") || request.getExpirationType().equals("AOTCM"))
			{		
				if(request.getFinanceDetails() !=null && request.getFinanceDetails().getStartDate()== null)
				{
					pdf.addField(prefix + "maturity_date",this.formatDate(DateUtils.addMonths(issueDate, product.getTermMonths())));
				}
				else {
				pdf.addField(prefix + "maturity_date",this.formatDate(DateUtils.addMonths(request.getFinanceDetails().getStartDate(), product.getTermMonths())));
				}
			}
			else if (request.getExpirationType().equals("ISDTCM") || request.getExpirationType().equals("ISDT") || request.getExpirationType().equals("CEDTCM"))
			{	
				pdf.addField(prefix + "maturity_date",this.formatDate(DateUtils.addMonths(request.getVehicle().getInServiceDate(), product.getTermMonths())));
			}
		
		}
		if(request.getVehicle().getExpirationMileage() != null && !request.getVehicle().getExpirationMileage().equals(""))
		{
			pdf.addField("EXPIRATIONMILEAGE",request.getVehicle().getExpirationMileage());
		}
		else if(request.getExpirationType() != "" && request.getExpirationType() != null)
		{
		
			//EXPIRATIONMILEAGE
            if(request.getExpirationType().equals("AOTAOM") || request.getExpirationType().equals("AOTAOMDAY"))
			{		
				
				pdf.addField(prefix + "EXPIRATIONMILEAGE",numberFormat.format(((request.getVehicle().getOdometer())+(product.getTermDistance()))));
			}
            else if (request.getExpirationType().equals("ISDTCM") || request.getExpirationType().equals("AOTCM") || request.getExpirationType().equals("CEDTCM") || request.getExpirationType().equals("AOT") || request.getExpirationType().equals("ISDT"))
			{	
				
				pdf.addField(prefix + "EXPIRATIONMILEAGE",numberFormat.format(product.getTermDistance()));
			}
			

		}
		
	}
	
	
	
	
		
	
	private boolean isAlphaNumeric(String s)
	{
		if(s == null) return false;
		String pattern= "^[a-zA-Z0-9]+$";
		if(s.matches(pattern))
		{
			return true;
		}
		return false;   
	}
	
	private boolean isNumeric(String s)
	{
		if(s == null) return false;
		String pattern= "^[0-9]+$";
		if(s.matches(pattern))
		{
			return true;
		}
		return false;   
	}

	private void fillUpBundledInformation(FormRequest request, PdfBean pdf, Date issueDate) 
	{
		double wrapTotalAmount = 0.00;
		double totalAmount = 0.00;
		boolean isBunProductFound = false;
		for (ProductDetail productDetail : request.getProductDetails()) 
		{
			//ProductDetail productDetail = request.getProductDetails().iterator().next();
			String productType = productDetail.getProductDetailType();
			
			boolean isWrapProduct = isCompWrap(productDetail);
			// set a field prefix if the product is a Wrap
			String prefix = isWrapProduct ? "W_" : "";
			if ("BUN".equals(productType))
			
			{
				
				
				if(request.getAccount().getAccountNumber().startsWith("PR-")) {
										
						if( StringUtils.endsWithIgnoreCase(productDetail.getCoverageName(), "Tire and Wheel_Paintless Dent Repair_Windshield")){
						pdf.addField(prefix + "47_Radio", "Yes");
						pdf.addField(prefix + "price47",this.formatCurrencyWithoutSymbol(productDetail.getPrice()));
						pdf.addField("47_MONTHS", productDetail.getTermMonths());
						pdf.addField(prefix +"47_ACTIVATEDATE", this.formatDate(issueDate));
						pdf.addField(prefix +"47_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, productDetail.getTermMonths())));
						
					}
					else if( StringUtils.endsWithIgnoreCase(productDetail.getCoverageName(), "Tire and Wheel_Paintless Dent Repair_Windshield_Key")){
						pdf.addField(prefix + "21_Radio", "Yes");
						pdf.addField(prefix + "price21",this.formatCurrencyWithoutSymbol(productDetail.getPrice()));
						pdf.addField("21_MONTHS", productDetail.getTermMonths());
						pdf.addField(prefix +"21_ACTIVATEDATE", this.formatDate(issueDate));
						pdf.addField(prefix +"21_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, productDetail.getTermMonths())));
		
					}
					else if( StringUtils.endsWithIgnoreCase(productDetail.getCoverageName(), "Paintless Dent Repair_Key Replacement")){
						pdf.addField(prefix + "49_Radio", "Yes");
						pdf.addField(prefix + "price49",this.formatCurrencyWithoutSymbol(productDetail.getPrice()));
						pdf.addField("49_MONTHS", productDetail.getTermMonths());
						pdf.addField(prefix +"49_ACTIVATEDATE", this.formatDate(issueDate));
						pdf.addField(prefix +"49_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, productDetail.getTermMonths())));
		
					}
					else if( StringUtils.endsWithIgnoreCase(productDetail.getCoverageName(), "Key Replacement")){
						pdf.addField(prefix + "46_Radio", "Yes");
						pdf.addField(prefix + "price46",this.formatCurrencyWithoutSymbol(productDetail.getPrice()));
						pdf.addField("46_MONTHS", productDetail.getTermMonths());
						pdf.addField(prefix +"46_ACTIVATEDATE", this.formatDate(issueDate));
						pdf.addField(prefix +"46_EXPIRATIONDATE",this.formatDate(DateUtils.addMonths(issueDate, productDetail.getTermMonths())));
		
					}
					
				}  
				else {
					
				//If the product type is Bundle, we set field 21 and price21
				pdf.addField(prefix + "21","X");
				pdf.addField(prefix + "21_Radio","Yes");
				pdf.addField(prefix + "price21",this.formatCurrencyWithoutSymbol(productDetail.getPrice()));
				
				}
				
				//Commented the Total Price population based on the Bug 
				//pdf.addField(prefix + "TotalPrice",this.formatCurrencyWithoutSymbol(productDetail.getPrice()));
				pdf.addField(prefix + "Bundled_TotalPrice", this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
				pdf.addField(prefix + "Bundled_Radio", "Yes");
				pdf.addField("Bundled",  "X");
				if(isWrapProduct)
				{
					wrapTotalAmount += productDetail.getReportedCustomerCost();
				}
				else
				{
					totalAmount += productDetail.getReportedCustomerCost();
				}
				if(isWrapProduct){
					pdf.addField(prefix + "MONTHS", productDetail.getTermMonths());
					pdf.addField(prefix + "MILES", numberFormat.format(productDetail.getTermDistance()));
				}
				isBunProductFound = true;
			} 
			else
			{
				String fieldId ="";
				double amount = 0.00;
				if ("PDR".equals(productType))
				{
					fieldId = "49";
					pdf.addField(prefix + fieldId,"X");
					amount = productDetail.getPrice();
				}
				else if ("TWB".equals(productType) || "TWP".equals(productType))
				{
					fieldId = "47";
					pdf.addField(prefix + fieldId,"X");
					amount = productDetail.getPrice();
				}
				else if ("WNS".equals(productType))
				{
					fieldId = "48";
					pdf.addField(prefix + fieldId,"X");
					amount = productDetail.getPrice();
				}
				/* Implemented as part of TWGQA-9504 */
				else if ("KEY".equals(productType))
				{
					fieldId = "46";
					pdf.addField(prefix + fieldId,"X");
					amount = productDetail.getPrice();
				}
				if(isWrapProduct)
				{
					wrapTotalAmount += amount;
				}
				else
				{
					totalAmount += amount;
				}
				pdf.addField(prefix + fieldId +"_Radio","Yes");
				pdf.addField(prefix + "price"+ fieldId, this.formatCurrencyWithoutSymbol(amount));
				/* Implemented as part of TWGQA-9504 */
				pdf.addField("PP_MONTHS", productDetail.getTermMonths());
				pdf.addField(prefix + "MONTHS", productDetail.getTermMonths());
				if (request.getFinanceDetails() != null && request.getFinanceDetails().getStartDate() == null) {
					pdf.addField(prefix +"ACTIVATIONDATE", this.formatDate(issueDate));

				} else {
					pdf.addField(prefix +"ACTIVATIONDATE", this.formatDate(request.getFinanceDetails().getStartDate()));

				}
				
			}
		}
		if(!isBunProductFound)
		{
			pdf.addField("W_TotalPrice",this.formatCurrencyWithoutSymbol(wrapTotalAmount));
			pdf.addField("TotalPrice",this.formatCurrencyWithoutSymbol(totalAmount));
			pdf.addField("PP_TotalPrice",this.formatCurrencyWithoutSymbol(totalAmount));
		}
	}

	private void fillUpClientInformation(FormRequest request, PdfBean pdf) 
	{
		pdf.addField("CLI_NAME", request.getAccount().getName());
		pdf.addField("CLI_NUMBER", request.getAccount().getAccountNumber());
		pdf.addField("CLI_ADDRESS", request.getAccount().getAddress().getAddressString());
		pdf.addField("CLI_CITY", request.getAccount().getAddress().getCity());
		pdf.addField("CLI_STATE", request.getAccount().getAddress().getStateCode());
		pdf.addField("CLI_ZIP", request.getAccount().getAddress().getPostalCode());
		pdf.addField("CLI_PHONE", this.formatPhone(request.getAccount().getPhone()));
	}
	
	private void fillDataForBrazil(FormRequest request, PdfBean pdf) {
		if (request.getAccount().getRfc() != null) {
			pdf.addField(ContractFormFields.rfc, request.getAccount().getRfc());
		}
		
		if (request.getAccount().getInvoiceNumber() != null) {
			pdf.addField(ContractFormFields.invoiceNumber, request.getAccount().getInvoiceNumber());
		}	
		
		if(request.getAccount().getSalesManager() != null) {
			pdf.addField(ContractFormFields.salesManager, request.getAccount().getSalesManager());
		}
		
		if(request.getVehicle().getVehicleRegistrationNumber() != null) {
			pdf.addField(ContractFormFields.vehicleRegistrationNumber, request.getVehicle().getVehicleRegistrationNumber());
		}
		
		if(request.getProductDetail().getNetPremium() != null) {
			pdf.addField(ContractFormFields.netPremium, request.getProductDetail().getNetPremium());
		}
		
		if(request.getFinanceDetails().getStartDate() != null) {
			pdf.addField("W_ACTIVATIONDATE", this.dateFormatForBrazil.format(request.getFinanceDetails().getStartDate()));
		}
		
		// EXPIRATIONDATE
		if (request.getFinanceDetails().getScheduledTerminationDate() != null) {
			pdf.addField(ContractFormFields.ExpiryDate, this.dateFormatForBrazil.format(request.getFinanceDetails().getScheduledTerminationDate()));
		}
		
		//SELLERDATE
		if (request.getVehicle().getVehiclePurchaseDate() != null) {
			pdf.addField(ContractFormFields.vechilePurchaseDate, this.dateFormatForBrazil.format(request.getVehicle().getVehiclePurchaseDate()));
		}
		
		//Broker data
		if(request.getBrokerDetails().getBrokerName() != null) {
			pdf.addField(ContractFormFields.brokerName, request.getBrokerDetails().getBrokerName());
		}
		
		if(request.getBrokerDetails().getBrokerTaxId() != null) {
			pdf.addField(ContractFormFields.brokerTaxId, request.getBrokerDetails().getBrokerTaxId());
		}
		
		if(request.getBrokerDetails().getBrokerPercentage() != null) {
			pdf.addField(ContractFormFields.brokerPercentage, request.getBrokerDetails().getBrokerPercentage());
		}
		
		if(request.getBrokerDetails().getBrokerPhone() != null) {
			pdf.addField(ContractFormFields.brokerPhone, request.getBrokerDetails().getBrokerPhone());
		}
		
		//co broker data
		if(request.getBrokerDetails().getCobrokerName() != null) {
			pdf.addField(ContractFormFields.cobrokerName, request.getBrokerDetails().getCobrokerName());
		}
		
		if(request.getBrokerDetails().getCobrokerPercentage() != null) {
			pdf.addField(ContractFormFields.cobrokerPercentage, request.getBrokerDetails().getCobrokerPercentage());
		}
		
		if(request.getBrokerDetails().getCobrokerTaxId() != null) {
			pdf.addField(ContractFormFields.cobrokerTaxId, request.getBrokerDetails().getCobrokerTaxId());
		}
		
		if(request.getBrokerDetails().getCobrokerPhone() != null) {
			pdf.addField(ContractFormFields.cobrokerPhone, request.getBrokerDetails().getCobrokerPhone());
		}
		
		//substipulator
		if(request.getBrokerDetails().getSubstipulatorName() != null) {
			pdf.addField(ContractFormFields.substipulatorName, request.getBrokerDetails().getSubstipulatorName());
		}
		
		if(request.getBrokerDetails().getSubstipulatorPercentage() != null) {
			pdf.addField(ContractFormFields.substipulatorPercentage, request.getBrokerDetails().getSubstipulatorPercentage());
		}
		
		if(request.getBrokerDetails().getSubstipulatorTaxId() != null) {
			pdf.addField(ContractFormFields.substipulatorTaxId, request.getBrokerDetails().getSubstipulatorTaxId());
		}
		
		if(request.getBrokerDetails().getSubstipulatorPhone() != null) {
			pdf.addField(ContractFormFields.substipulatorPhone, request.getBrokerDetails().getSubstipulatorPhone());
		}
		
		//representative
		if(request.getBrokerDetails().getRepresentativeName() != null) {
			pdf.addField(ContractFormFields.representativeName, request.getBrokerDetails().getRepresentativeName());
		}
		
		if(request.getBrokerDetails().getRepresentativePercentage() != null) {
			pdf.addField(ContractFormFields.representativePercentage, request.getBrokerDetails().getRepresentativePercentage());
		}
		
		if(request.getBrokerDetails().getRepresentativeTaxId() != null) {
			pdf.addField(ContractFormFields.representativeTaxId, request.getBrokerDetails().getRepresentativeTaxId());
		}
		
		if(request.getBrokerDetails().getRepresentativePhone() != null) {
			pdf.addField(ContractFormFields.representativePhone, request.getBrokerDetails().getRepresentativePhone());
		}
	}
	
	
	private void fillDataForMexico(FormRequest request, PdfBean pdf) {

		if (request.getAccount().getSalesman() != null) {
			pdf.addField(ContractFormFields.salesman, request.getAccount().getSalesman());
		}

		if (request.getAccount().getRfc() != null) {
			pdf.addField(ContractFormFields.rfc, request.getAccount().getRfc());
		}

		if (request.getAccount().getPaymentType() != null) {
			pdf.addField(ContractFormFields.paymentType, request.getAccount().getPaymentType());
		}

		if (request.getAccount().getInvoiceNumber() != null) {
			pdf.addField(ContractFormFields.invoiceNumber, request.getAccount().getInvoiceNumber());
		}

		if (request.getVehicle().getVehiclePurchaseDate() != null) {
			
			
		 
		 int[] ans = request.getVehicle().getInsuranceStartAndEndDate();
	       
	        pdf.addField(ContractFormFields.insuranceStartDD,ans[0]);
	        pdf.addField(ContractFormFields.insuranceStartMM,ans[1]);
	        pdf.addField(ContractFormFields.insuranceStartYYYY,ans[2]);
	        pdf.addField(ContractFormFields.insuranceEndDD,ans[3]);
	        pdf.addField(ContractFormFields.insuranceEndMM,ans[4]);
	        pdf.addField(ContractFormFields.insuranceEndYYYY,ans[5]);
	        
		}

		if (request.getVehicle().getVehicleNewOrUsed() != null) {
			pdf.addField(ContractFormFields.vehicleNewOrUsed, request.getVehicle().getVehicleNewOrUsed());
		}

		if (request.getVehicle().getRegistrationDateEMDCS() != null) {
			pdf.addField(ContractFormFields.registrationDateEMDCS,
					formatDate(request.getVehicle().getRegistrationDateEMDCS()));
		}

		if (request.getVehicle().getCoverageStartMileage() != null) {
			pdf.addField(ContractFormFields.odometerAtPurchase, numberFormat.format(request.getVehicle().getCoverageStartMileage()));
		}

		if (request.getVehicle().getTransmission() != null) {
			pdf.addField(ContractFormFields.transmission, request.getVehicle().getTransmission());
		}

		// EXPIRATIONDATE
		if (request.getFinanceDetails().getScheduledTerminationDate() != null) {

			pdf.addField(ContractFormFields.ExpiryDate,
					this.formatDate(request.getFinanceDetails().getScheduledTerminationDate()));
		}

		// EXPIRATIONMILEAGE
		if (request.getFinanceDetails().getTotalAllowableMilesContract() != null) {
			pdf.addField(ContractFormFields.expirationMilage,numberFormat.format(
					request.getFinanceDetails().getTotalAllowableMilesContract()));
			pdf.addField(ContractFormFields.totalAllowableMiles,numberFormat.format(
					request.getFinanceDetails().getTotalAllowableMilesContract()));
		}
		
		if(request.getVehicle().getManufacturerWarrantyEndDate() != null) {
			pdf.addField("END_DATE_MW",formatDate(request.getVehicle().getManufacturerWarrantyEndDate()));
		}
		
		if(request.getVehicle().getManufacturerWarrantyEndMileage() != null) {
			pdf.addField("END_ODOMETER_MW", numberFormat.format(request.getVehicle().getManufacturerWarrantyEndMileage()));
		}
		
	}
	
	private byte[] writePdfToFile(PdfBean pdf, String formatType, InputStream template) throws Exception
	{
		return PdfUtil.generatePdfFile(pdf, formatType, template);
		//return pdf.getOutputFileName();
	}
	
	/**
	 * Formats the value as a currency
	 * @param value the value to format.
	 * @return the formatted value.
	 */
	private String formatCurrencyWithoutSymbol(Double value) 
	{
		return value != null ? currencyFormatWithoutSymbol.format(value) : null;
	}
	
	/**
	 * Formats the value as a currency
	 * @param value the value to format.
	 * @return the formatted value.
	 */
	private String formatCurrencyWithSymbol(Double value) 
	{
		return value != null ? currencyFormat.format(value) : null;
	}
	
	/**
	 * Formats the value as a currency
	 * @param value the value to format.
	 * @return the formatted value.
	 */
	private String formatCurrencyWithoutSymbolInt(Double value) 
	{
		return value != null ? decimalFormatInt.format(value) : null;
	}
	
	/**
	 * Formats the date
	 * @param date the date to format.
	 * @return the formatted date.
	 */
	private String formatDate(Date date) 
	{
		return date != null ? this.dateFormat.format(date) : null;
	}
	
	/**
	 * Formats the phone number as "(areaCode) number".
	 * @param phone the phone to format.
	 * @return the formatted phone.
	 */
	private String formatPhone(Phone phone) 
	{
		if (phone == null || phone.getNumber() == null) 
		{
			return ""; //return empty string as returning null prints null word  on the pdf;
		}
		String phoneNumber = phone.getNumber().trim();
		if (phone.getAreaCode() != null) 
		{
			if(phoneNumber.matches("[0-9]+") && phoneNumber.length() == 7)
			{
				return MessageFormat.format("({0}) {1}-{2}", phone.getAreaCode(), phoneNumber.substring(0,3), phoneNumber.substring(3));
			}
			return MessageFormat.format("({0}) {1}", phone.getAreaCode(), phoneNumber);
		}
		else if(phoneNumber.matches("[0-9]+") && phoneNumber.length() == 7)
		{
			return MessageFormat.format("{0} - {1}", phoneNumber.substring(0,3), phoneNumber.substring(3));
			
		}else if(phoneNumber.matches("[0-9]+") && phoneNumber.length() == 10) {
			
			return MessageFormat.format("({0}) {1}-{2}", phoneNumber.substring(0,3), phoneNumber.substring(3,6), phoneNumber.substring(6));
		}
		
		return phone.getNumber().trim();
	}
	
	private String formatPhoneMX(Phone phone) 
	{
		if (phone == null || phone.getNumber() == null) 
		{
			return ""; //return empty string as returning null prints null word  on the pdf;
		}
		return phone.getNumber().trim();
	}
	
	private boolean isCompWrap(ProductDetail productDetail)
	{
		boolean isWrapProduct = false;
		String [] productClassSplit = productDetail.getProductClass().split("\\|");		
				
		if(productClassSplit.length > 1)
		{
			String productClassCode = productClassSplit[1];
			String[] startWithValueList = {"true", "39", "169", "194", "196", "197"};
			String[] containValueList = {"Complimentary Wrap", "Comp Wrap"};
			for(String compCodeOrName : containValueList)
			{
				if(StringUtils.containsIgnoreCase(productClassCode, compCodeOrName) )
				{
					 isWrapProduct = true;
					 break;
				 }
			}
			for(String compCodeOrName : startWithValueList)
			 {
				if(StringUtils.startsWithIgnoreCase(productClassCode, compCodeOrName) )
				 {
					isWrapProduct = true;
					break;
				 }
			 }
		}
			
		return isWrapProduct;	
	}
	
	private String getCompWrapPrefix(ProductDetail productDetail)
	{
		return isCompWrap(productDetail) ? "W_" : "";
	}
	
	private String getDateInCST()
	{
		Date todayDate = new Date();
		DateFormat todayDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    TimeZone timeZone = TimeZone.getTimeZone("US/Central");
	    todayDateFormat.setTimeZone(timeZone);
	    String todayDateStr = todayDateFormat.format(todayDate);
	    return todayDateStr;
	}
	
	
	private boolean isClassificationCodeAN(FormRequest request)
	{
		return (request.getAccount() !=null && request.getAccount().getClassificationCode() != null	&& request.getAccount().getClassificationCode().equals("AN"));
	}

	
	private boolean isCountryMX(FormRequest request)
	{
		return (request.getAccount() != null && request.getAccount().getAddress() != null
				&& request.getAccount().getAddress().getCountryCode() != null
				&& request.getAccount().getAddress().getCountryCode().equals("MEX"));
	}
	
	private boolean isCountryBrazil(FormRequest request) {
		return (request.getAccount() != null && request.getAccount().getAddress() != null
				&& request.getAccount().getAddress().getCountryCode() != null
				&& request.getAccount().getAddress().getCountryCode().equals("BRA"));
	}
	
	private boolean isCountryLATAM(FormRequest request)
	{
		return (request.getAccount() != null && request.getAccount().getAddress() != null
				&& request.getAccount().getAddress().getCountryCode() != null
				&& (request.getAccount().getAddress().getCountryCode().equals("BRB") || request.getAccount().getAddress().getCountryCode().equals("COL") 
				|| request.getAccount().getAddress().getCountryCode().equals("TTO") || request.getAccount().getAddress().getCountryCode().equals("PER") 
				|| request.getAccount().getAddress().getCountryCode().equals("CHL") || request.getAccount().getAddress().getCountryCode().equals("ARG")));
	}
	
private void fillDataForLATAM(FormRequest request, PdfBean pdf) {
		
		if (request.getAccount().getSalesman() != null) {
			pdf.addField(ContractFormFields.salesman, request.getAccount().getSalesman());
		}
		
		if(request.getAccount().getAddress().getCountryCode().equals("COL") || request.getAccount().getAddress().getCountryCode().equals("PER") || request.getAccount().getAddress().getCountryCode().equals("ARG") ) {
			
			DateFormat dateFormat=new SimpleDateFormat("dd/MM/yy");
	
			if (request.getVehicle().getVehiclePurchaseDate() != null) {
				pdf.addField(ContractFormFields.vechilePurchaseDate,
						dateFormat.format(request.getVehicle().getVehiclePurchaseDate()));
			}

			if (request.getVehicle().getRegistrationDateEMDCS() != null) {
				pdf.addField(ContractFormFields.registrationDateEMDCS,
						dateFormat.format(request.getVehicle().getRegistrationDateEMDCS()));
			}
			
			if(request.getVehicle().getCoverageStartDate() !=null) {
			pdf.addField("ACTIVATIONDATE_PR",dateFormat.format( request.getVehicle().getCoverageStartDate()));
			}
			
			if(request.getVehicle().getExpirationDate() != null) {
			pdf.addField("expirationDate", dateFormat.format(request.getVehicle().getExpirationDate()));
			}
			
			if(request.getFinanceDetails().getStartDate() != null) {
			pdf.addField("ACTIVATIONDATE_OP", dateFormat.format(request.getFinanceDetails().getStartDate()));
			}
			
			if(request.getVehicle().getContractStartDate() !=null) {
			pdf.addField("contractStartDate", dateFormat.format( request.getVehicle().getContractStartDate()));
			
			}
			if(request.getVehicle().getCreatedDate() != null) {
			pdf.addField("createdDate",dateFormat.format(request.getVehicle().getCreatedDate()) );		
			}			
		}
		else {
			
			if (request.getVehicle().getRegistrationDateEMDCS() != null) {
				pdf.addField(ContractFormFields.registrationDateEMDCS,
						formatDate(request.getVehicle().getRegistrationDateEMDCS()));
			}
			
			pdf.addField("contractStartDate", formatDate( request.getVehicle().getContractStartDate()));
			pdf.addField("createdDate",formatDate(request.getVehicle().getCreatedDate()) );	
			pdf.addField("ACTIVATIONDATE_PR",formatDate( request.getVehicle().getCoverageStartDate()));
			pdf.addField("expirationDate", formatDate(request.getVehicle().getExpirationDate()));
		}

		if (request.getVehicle().getTransmission() != null) {
			pdf.addField(ContractFormFields.transmission, request.getVehicle().getTransmission());
		}
		
		ProductDetail productDetail = request.getProductDetail();
		if(productDetail != null) {
		pdf.addField("MONTHS_OP", productDetail.getTermMonths());
		pdf.addField("MILES_OP", productDetail.getTermDistance());
		pdf.addField("AGREEMENT_COST",this.formatCurrencyWithoutSymbol(productDetail.getReportedCustomerCost()));
		}
		
		
		Contact contact = request.getLienHolder();
		if (contact != null) 
		{
			pdf.addField("LIEN_NAME", contact.getFullName());			
			pdf.addField("LIEN_ADDRESS", contact.getAddress().getAddressString());	
		}
		
		
		pdf.addField("EXPIRATIONMILEAGE_PR",request.getVehicle().getExpirationMileage()); //EXPIRATIONMILEAGE_PR
		
		
	//	ARG AND CHL
		pdf.addField("customerCost", request.getVehicle().getCustomerCost());
		pdf.addField("totalRetailCost", request.getVehicle().getTotalRetailCost());
		pdf.addField("totalBilledCost", request.getVehicle().getTotalBilledCost());
		pdf.addField("retailPrice",request.getVehicle().getRetailPrice());	
		pdf.addField("engineNumber",request.getVehicle().getEngineNumber());
		pdf.addField("customerNumber", request.getVehicle().getCustomerNumber());
		pdf.addField("purchasePricesales", request.getVehicle().getPurchasePricesales());
		
		if (request.getAccount().getRfc() != null) {
			pdf.addField(ContractFormFields.rfc, request.getAccount().getRfc());
		}
		
		if(request.getVehicle().getGapProductTenure() !=null && request.getVehicle().getGapProductTenure() !="") {
			pdf.addField("GAPPRODUCTNAME",request.getVehicle().getGapProductTenure());
		}
	}


private static String setFormatWithDotSeparator(int number) {
	DecimalFormatSymbols symbols = new DecimalFormatSymbols();
	symbols.setGroupingSeparator('.');
	DecimalFormat formatter = new DecimalFormat("#,##0", symbols);
	return formatter.format(number);
}

}

