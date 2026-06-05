	package com.twg.api.experience_forms_api;


	import java.io.ByteArrayOutputStream;
	import java.io.File;
	import java.io.FileNotFoundException;
	import java.io.IOException;
	import java.io.InputStream;
	import java.io.OutputStream;
	import java.text.DateFormat;
	import java.text.DecimalFormat;
	import java.text.MessageFormat;
	import java.text.SimpleDateFormat;
	import java.util.Date;
	import java.util.HashSet;
	import java.util.Iterator;
	import java.util.Set;

	import org.apache.commons.lang.StringUtils;
	import org.apache.commons.lang.time.DateUtils;
	import org.apache.commons.logging.Log;
	import org.apache.commons.logging.LogFactory;
	import org.apache.cxf.helpers.FileUtils;

	import com.google.common.collect.Sets;
	import com.google.common.io.Files;
	import com.twg.api.experience_forms_api.document.Pdf;
	import com.twg.api.experience_forms_api.document.PdfBean;
	import com.twg.api.experience_forms_api.document.PdfUtil;
	import com.twg.api.experience_forms_api.request.Contact;
	import com.twg.api.experience_forms_api.request.FormRequest;
	import com.twg.api.experience_forms_api.request.FormType;
	import com.twg.api.experience_forms_api.request.Phone;
	import com.twg.api.experience_forms_api.request.ProductDetail;

	public class DocumentMapper 
	{
		private DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
		private DecimalFormat numberFormat = new DecimalFormat("#,##0");
		private DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
		private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
		public static final String FORMAT_TYPE_ALL_CAPS = "ALL_CAPS";
	
		public byte[] getForm(FormRequest request , InputStream template) throws Exception 
			{
			
			//Generate the Form based on Template and Form Request and build a stream
			//Stream is preferred than File to meet the limitations of File storage in CloudHub.
			byte[] ByteArrayInputStream = null;
			
			try 
			{
				Set<Pdf> pdfs = new HashSet<Pdf>();
				ByteArrayInputStream = generateAllPDFs(pdfs, request, template);
			} 
			catch (Exception exception) 
			{
				exception.printStackTrace();
			}
			return ByteArrayInputStream;
			}
	

		private byte[] generateAllPDFs(Set<Pdf> pdfs, FormRequest request, InputStream template) 
			{
			return generateBundledPDF(pdfs, request,template);
			}
	
		private byte[] generateBundledPDF(Set<Pdf> pdfs, FormRequest request, InputStream template) 
			{
				String formatType = "";
				byte[] ByteArrayInputStream=null;
				Date issueDate = new Date();
				try 
				{
					ByteArrayInputStream = writePdfToFile(generatePdfMultipeProductDetails(request, issueDate), formatType, template);
				}
				catch (FileNotFoundException fileException) 
				{
					fileException.printStackTrace();
				}
				catch (IOException ioException) 
				{
					System.out.println("IO Exception");
				}
				catch (Exception exception) 
				{
					System.out.println("Exception Error.");
				}
		
			return ByteArrayInputStream;
			}
	

		private PdfBean generatePdfMultipeProductDetails(FormRequest request, Date issueDate) 
		{
			PdfBean pdf = new PdfBean();
			pdf.addField(ContractFormFields.contratNumber, request.getContractNumber());
			pdf.addField(ContractFormFields.vin, request.getVehicle().getVin());
			pdf.addField(ContractFormFields.model, request.getVehicle().getModel());
			pdf.addField(ContractFormFields.odometer, numberFormat.format(request.getVehicle().getOdometer()));
			pdf.addField(ContractFormFields.year, request.getVehicle().getYear());
			pdf.addField(ContractFormFields.make, request.getVehicle().getMake());
			fillUpVehicleInformation(request, pdf);
			fillUpContactInformation(request, pdf);
			fillUpClientInformation(request, pdf);
	
			Contact lienholder = request.getLienHolder();
			if (lienholder != null && !lienholder.getFullName().equals("") && !lienholder.getFullName().equals(null)) 
			{
				pdf.addField(ContractFormFields.lienName, lienholder.getFullName());
				pdf.addField(ContractFormFields.lienAddress, lienholder.getAddress().getAddressString());
				pdf.addField(ContractFormFields.lienCity, lienholder.getAddress().getCity());
				pdf.addField(ContractFormFields.lienStateCode, lienholder.getAddress().getStateCode().toUpperCase());
				pdf.addField(ContractFormFields.lienPostalCode, lienholder.getAddress().getPostalCode());
				Set<Phone> lienholderPhones = new HashSet();
				lienholderPhones=lienholder.getPhones();
				 for (Phone lienholderPhone : lienholderPhones) 
				 {
					 pdf.addField(ContractFormFields.lienPhone, this.formatPhone(lienholderPhone));
				 }
			}
			ProductDetail productDetail = request.getProductDetail();
			fillUpComplimentaryInformation(request, productDetail, pdf, issueDate);
			return pdf;
		}
	

		private void fillUpComplimentaryInformation(FormRequest request, ProductDetail productDetail, PdfBean pdf, Date issueDate) 
		{
			boolean isWrapProduct = StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "wrap");
			// set a field prefix if the product is a Wrap
			String prefix = isWrapProduct ? "W_" : "";
	
			pdf.addField(prefix + ContractFormFields.months, productDetail.getTermMonths());
			pdf.addField(prefix + ContractFormFields.miles, numberFormat.format(productDetail.getTermDistance()));
			pdf.addField(prefix + ContractFormFields.price, this.formatCurrencyWithSymbol(productDetail.getReportedCustomerCost()));
	
			pdf.addField(prefix + ContractFormFields.isBase,StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "Base") ? "Yes" : "No");
	
			pdf.addField(prefix + ContractFormFields.isPowerTrain,StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "Power") ? "Yes" : "No");
			
			pdf.addField(prefix + ContractFormFields.isHighTech,!StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Wrap")? checkMultipleValuesForCheckBox(productDetail.getCoverageName(),new String[] { "High Tech", "High-Tech" }): "No");
	
			pdf.addField(prefix + ContractFormFields.isComprehensive,StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "Comprehensive")&& !StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Wrap") ? "Yes" : "No");
	
			pdf.addField(prefix + ContractFormFields.isHighTechWrap, checkMultipleValuesForCheckBox(productDetail.getCoverageName(),new String[] { "High Tech Wrap", "High-Tech Wrap" }));
	
			pdf.addField(prefix + ContractFormFields.isComprehensiveWrap,StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "Comprehensive Wrap") ? "Yes" : "No");
	
			pdf.addField(ContractFormFields.isPreferred, StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "preferred") ? "Yes" : "No");
			
			pdf.addField(ContractFormFields.isStandard, StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "standard") ? "Yes" : "No");
			
			if (!StringUtils.containsIgnoreCase(productDetail.getCoverageName(), "Wrap")&& (StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "Base")|| StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "High Tech")|| StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "High-Tech")|| StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "Comprehensive"))) 
			{
				pdf.addField(ContractFormFields.months, productDetail.getTermMonths());
				pdf.addField(ContractFormFields.miles, numberFormat.format(productDetail.getTermDistance()));
	
			}
			if (StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "High-Tech Wrap")|| StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "High Tech Wrap")|| StringUtils.startsWithIgnoreCase(productDetail.getCoverageName(), "Comprehensive Wrap")) 
			{
				pdf.addField(prefix + ContractFormFields.months, "");
				pdf.addField(prefix + ContractFormFields.miles, "");
				pdf.addField(ContractFormFields.monthsWrap, productDetail.getTermMonths());
				pdf.addField(ContractFormFields.milesWrap, numberFormat.format(productDetail.getTermDistance()));
			}
			pdf.addField(ContractFormFields.deductible,numberFormat.format( decimalFormat.format(productDetail.getDeductibleAmount())));
			pdf.addField(ContractFormFields.disappearingDeductible, productDetail.isDisappearing()? "Yes":"No");
			Date argDate = new Date();
			pdf.addField(ContractFormFields.purchaseDate, formatDate(argDate));
			Date expiryDate = DateUtils.addMonths(argDate,productDetail.getTermMonths());
			pdf.addField(ContractFormFields.ExpiryDate, formatDate(expiryDate));
			pdf.addField(ContractFormFields.inServiceDate, formatDate(request.getVehicle().getInServiceDate()));
			pdf.addField(ContractFormFields.price, decimalFormat.format(productDetail.getReportedCustomerCost()));
			if (!FormType.Final.name().equals(productDetail.getAgreementStatus())) {
				pdf.addField("WATERMARK", "SAMPLE INELIGIBLE");
				pdf.addField("SIGNHERE", "SAMPLE INELIGIBLE");
			}
		}
	

		private void fillUpVehicleInformation(FormRequest request, PdfBean pdf) 
		{
			pdf.addField(ContractFormFields.vin, request.getVehicle().getVin());
			pdf.addField(ContractFormFields.make, request.getVehicle().getMake());
			pdf.addField(ContractFormFields.model, request.getVehicle().getModel());
			pdf.addField(ContractFormFields.odometer, numberFormat.format(request.getVehicle().getOdometer()));
			pdf.addField(ContractFormFields.year, request.getVehicle().getYear());
			pdf.addField(ContractFormFields.isNew, request.getVehicle().isNewCar() ? "Yes":"No");
			pdf.addField(ContractFormFields.isused,request.getVehicle().isNewCar() ? "No":"Yes" );
			pdf.addField(ContractFormFields.is4wd, request.getVehicle().isFourByFour() ? "Yes" : "No");
			pdf.addField(ContractFormFields.isDieselEngine, request.getVehicle().isDiesel() ? "Yes" : "No");
			pdf.addField(ContractFormFields.isTurbo, request.getVehicle().isTurbo() ? "Yes" : "No");
		}
	
		private void fillUpContactInformation(FormRequest request, PdfBean pdf) 
		{
			Contact buyer = request.getBuyer();
			pdf.addField(ContractFormFields.buyerName, buyer.getFullName());
			pdf.addField(ContractFormFields.buyerAddress, buyer.getAddress().getAddressString());
			pdf.addField(ContractFormFields.buyerStateCode, buyer.getAddress().getStateCode());
			pdf.addField(ContractFormFields.buyerCity, buyer.getAddress().getCity());
			pdf.addField(ContractFormFields.buyerPostalCode, buyer.getAddress().getPostalCode());
			pdf.addField(ContractFormFields.buyercompleteAddress, buyer.getAddress().getCompleteAddressString());
			pdf.addField(ContractFormFields.buyerAccountno,buyer.getMemberNumber()); //used by Gap form
			pdf.addField(ContractFormFields.buyerMemberno, buyer.getMemberNumber());//used by service contract form
			Set<Phone> buyerPhones = new HashSet();
			buyerPhones=buyer.getPhones();
			 for (Phone buyerPhone : buyerPhones) 
			 {
				 if(buyerPhone.getType().equals("H"))
				 {
					 pdf.addField(ContractFormFields.buyerHomePhone, this.formatPhone(buyerPhone));
				 }
				 if(buyerPhone.getType().equals("M"))
				 {
					 pdf.addField(ContractFormFields.buyerMobile, this.formatPhone(buyerPhone));
				 }
			  }
	
			pdf.addField(ContractFormFields.buyerEmail, buyer.getEmail());
	
			Contact coBuyer = request.getCoBuyer();
			
			if (coBuyer != null && !coBuyer.getFullName().equals("") && !coBuyer.getFullName().equals(null)) 
			{
				pdf.addField(ContractFormFields.coBuyerName, coBuyer.getFullName());
				if (coBuyer.getAddress() != null) 
				{
					pdf.addField(ContractFormFields.coBuyerAddress, coBuyer.getAddress().getAddressString());
					pdf.addField(ContractFormFields.coBuyerStateCode, coBuyer.getAddress().getStateCode());
					pdf.addField(ContractFormFields.coBuyerCity, coBuyer.getAddress().getCity());
					pdf.addField(ContractFormFields.coBuyerPostalCode, coBuyer.getAddress().getPostalCode());
				}
				
				Set<Phone> coBuyerPhones = new HashSet();
				coBuyerPhones=coBuyer.getPhones();
				 for (Phone coBuyerPhone : coBuyerPhones) 
				 {
					 if(coBuyerPhone.getType().equals("H"))
					 {
						 pdf.addField(ContractFormFields.coBuyerHomePhone, this.formatPhone(coBuyerPhone));
					 }
					 if(coBuyerPhone.getType().equals("M"))
					 {
						 pdf.addField(ContractFormFields.coBuyerMobile, this.formatPhone(coBuyerPhone));
					 }
				 }
				
				pdf.addField(ContractFormFields.coBuyerEmail, coBuyer.getEmail());
				
			}
		}
	
	
		private String checkMultipleValuesForCheckBox(String value, String[] values) 
		{
			return checkForMultipleValues(value, values, "Yes", "No");
		}
	
		private String checkForMultipleValues(String value, String[] values, String trueValue, String falseValue) 
		{
			String newValue = falseValue;
	
			for (int i = 0; i < values.length; i++) 
			{
				String valueA = values[i];
				if (StringUtils.containsIgnoreCase(value, valueA)) 
				{
					newValue = trueValue;
					break;
				}
			}
			return newValue;
		}
	
	
		private void fillUpClientInformation(FormRequest request, PdfBean pdf) 
		{
			pdf.addField(ContractFormFields.dealerName, request.getAccount().getName());
			pdf.addField(ContractFormFields.dealerNumber, request.getAccount().getAccountNumber());
			pdf.addField(ContractFormFields.dealerAddress, request.getAccount().getAddress().getAddressString());
			pdf.addField(ContractFormFields.dealerCity, request.getAccount().getAddress().getCity());
			pdf.addField(ContractFormFields.dealerStateCode, request.getAccount().getAddress().getStateCode());
			pdf.addField(ContractFormFields.dealerPostalCode, request.getAccount().getAddress().getPostalCode());
			pdf.addField(ContractFormFields.dealerPhone, this.formatPhone(request.getAccount().getPhone()));
		}
	
		private byte[] writePdfToFile(PdfBean pdf, String formatType, InputStream template) throws Exception 
		{
			byte[] inputByteArray = PdfUtil.generatePdfFile(pdf, formatType,template);
			return inputByteArray;
		}
		

		private String formatCurrencyWithSymbol(Double value) 
		{
			return value != null ? currencyFormat.format(value) : null;
		}
	

		private String formatDate(Date date) 
		{
			return date != null ? this.dateFormat.format(date) : null;
		}
	

		private String formatPhone(Phone phone) 
		{
			if (phone == null || phone.getNumber() == null) 
			{
				return ""; // return empty string as returning null prints null word
							// on the pdf;
			}
			String phoneNumber = phone.getNumber().trim();
			if (phone.getAreaCode() != null) 
			{
				if (phoneNumber.matches("[0-9]+") && phoneNumber.length() == 7) 
				{
					return MessageFormat.format("({0}) {1}-{2}", phone.getAreaCode(), phoneNumber.substring(0, 3),
							phoneNumber.substring(3));
				}
				return MessageFormat.format("({0}) {1}", phone.getAreaCode(), phoneNumber);
			} 
			else if (phoneNumber.matches("[0-9]+") && phoneNumber.length() == 7) 
			{
				return MessageFormat.format("{0} - {1}", phoneNumber.substring(0, 3), phoneNumber.substring(3));
			}
	
			return phone.getNumber().trim();
		}
	}
