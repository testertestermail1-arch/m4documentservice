package com.twg.api.experience_forms_api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.Base64;
import com.aon.awg.qcert.businesslayer.VehiclePDFOptions;
import com.aon.awg.qcert.domain.Dealer;
import com.aon.awg.qcert.domain.DealerProduct;
import com.aon.awg.qcert.domain.Inventory;
import com.aon.awg.qcert.domain.Product;
import com.aon.awg.qcert.domain.VehIndicators;
import com.aon.awg.qcert.domain.vehicle.Make;
import com.aon.awg.qcert.domain.vehicle.Model;
import com.aon.awg.qcert.domain.vehicle.Vehicle;
import com.aon.awg.qcert.domain.vehicle.VehicleOption;
import com.aon.awg.qcert.domain.vehicle.VehicleVinAttributes;
import com.aon.awg.qcert.utils.QcertCorePdfConstants.VehicleDetails;
import com.aon.awg.qcert.utils.UtilityObjects;

public class QCertWrapper {
	
	public LinkedHashMap generateForm(ArrayList canonicalList) throws Exception {
		LinkedHashMap responseMap = new LinkedHashMap();

		//variables declaration
		ArrayList canonical_list= null;
		Map<String,Object> canonical = null;
		String formsName= null;
		String formName= null;
		byte[] finaloutput= null;
		List<byte[]> total=new ArrayList<byte[]>();
		byte[] bytearray_output=null;
		
		//need to change
		canonical_list = canonicalList;
				
		for(int i=0 ; i<canonical_list.size(); i++ )
		{	
		//System.out.println("list of  "+i+" is"+canonical_list.get(i));
		canonical = (Map)canonical_list.get(i);
	    formsName = (String)canonical.get("formName");
		
		String formNames_list[] = formsName.split(",");
		List<String> forms_list = ordertemplates(Arrays.asList(formNames_list));
		Inventory _inventory = getVehInventory_populateData(canonical);
		Dealer dealer = _inventory.getDealer();
		
		for(int j=0; j<forms_list.size(); j++)
		{
			 formName = (String)forms_list.get(j);
			 
		if(formName.equals("VehicleDetails"))
		{
			VehiclePDFOptions vpo = populateVehicleOptionsInteractiveMode(_inventory);
			VehicleDetailsPdf vdp = new VehicleDetailsPdf();
			bytearray_output=vdp.createVehicleDetailsPdf(vpo,_inventory,dealer);
			total.add(bytearray_output);
		}
		if(formName.equals("VehicleTitle"))
		{
		   QCertFromLegacySystem _legacy = new QCertFromLegacySystem();
		   VehIndicators vehIndicators = getVehIndicators_populateData(canonical);
		   bytearray_output=_legacy.createTitleCheckPdf(vehIndicators, _inventory);
		   total.add(bytearray_output);
		}
		if(formName.equals("BuyersGuide"))
		{
			QCertFromLegacySystemforBuyersGuide bg = new QCertFromLegacySystemforBuyersGuide();
			bytearray_output=bg.createBuyersGuidePdf(_inventory);
			total.add(bytearray_output);
		}
		if(formName.equals("BuyersGuideSpanish"))
		{
			QCertFromLegacySystemforBuyersGuide bg = new QCertFromLegacySystemforBuyersGuide();
			bytearray_output=bg.createBuyersGuidePdfSpanish(_inventory);
			total.add(bytearray_output);
		}
		if(formName.equals("VehicleBrochure"))
		{
		   VehiclePDFOptions vpo = populateVehicleOptionsInteractiveMode(_inventory);
		   VehicleDetailsPdf vdp = new VehicleDetailsPdf();
		   bytearray_output=vdp.createVehicleBrochurePdf(vpo,_inventory,dealer);
		   total.add(bytearray_output);
		}
		}
		}
		MergePDF mergePDF=new MergePDF();
		finaloutput =mergePDF.mergePDF(total);
		responseMap.put("finaloutput", finaloutput);
		return responseMap;
	}

	private VehiclePDFOptions populateVehicleOptionsInteractiveMode(Inventory _inventory) {
		final VehiclePDFOptions vehiclePdfOptions = new VehiclePDFOptions();
		vehiclePdfOptions.setIntFeaturesForPdf(
				vehicleOptionsEvaluator(getVehicleTypeCollection(_inventory, VehicleDetails.FEATURES_INTERIOR)));
		vehiclePdfOptions.setExtFeaturesForPdf(
				vehicleOptionsEvaluator(getVehicleTypeCollection(_inventory, VehicleDetails.FEATURES_EXTERIOR)));
		vehiclePdfOptions.setEngFeaturesForPdf(
				vehicleOptionsEvaluator(getVehicleTypeCollection(_inventory, VehicleDetails.FEATURES_ENGINEERING)));
		vehiclePdfOptions.setSafetyFeaturesForPdf(
				vehicleOptionsEvaluator(getVehicleTypeCollection(_inventory, VehicleDetails.FEATURES_SAFE)));
		vehiclePdfOptions.setAddedFeaturesForPdf(
				vehicleOptionsEvaluator(getVehicleTypeCollection(_inventory, VehicleDetails.FEATURES_ADDITIONAL)));
		return vehiclePdfOptions;
	}

	private String vehicleOptionsEvaluator(Collection<VehicleOption> _vehicleOptions) {
		final StringBuilder options = new StringBuilder();
		for (VehicleOption vehicleOption : _vehicleOptions) {
			options.append("* " + vehicleOption.getDescription().toUpperCase() + "\n");
		}
		return options.toString();
	}

	public Collection<VehicleOption> getVehicleTypeCollection(Inventory _inventory, VehicleDetails _vehicleDetails) {
		final Collection<?> rawCollection = _inventory.getOptions(_vehicleDetails.toValue(), true);
		@SuppressWarnings("unchecked")
		final Collection<VehicleOption> typedCollection = Collections
				.checkedCollection((Collection<VehicleOption>) rawCollection, VehicleOption.class);
		return typedCollection;
	}

	
	@SuppressWarnings("unchecked")
	public VehIndicators getVehIndicators_populateData(Map<String,Object> canonical) {

		VehIndicators vehIndicators = new VehIndicators(true);

		vehIndicators.setVin((String)UtilityObjects.checkforEmpty((String)(((Map<String,Object>)canonical.get("vinIndicators")).get("vin"))));
		vehIndicators.setCountry((String)UtilityObjects.checkforEmpty((String)(((Map<String,Object>)canonical.get("vinIndicators")).get("country"))));
		vehIndicators.setLastDate((String)UtilityObjects.checkforEmpty((String)(((Map<String,Object>)canonical.get("vinIndicators")).get("last_Date"))));
		vehIndicators.setLastState((String)UtilityObjects.checkforEmpty((String)(((Map<String,Object>)canonical.get("vinIndicators")).get("lastState"))));
		vehIndicators.setLastTitle((String)UtilityObjects.checkforEmpty((String)(((Map<String,Object>)canonical.get("vinIndicators")).get("lastTitle"))));
		vehIndicators.setDuplicateTitle((String)(((Map<String,Object>)canonical.get("vinIndicators")).get("duplicateTitle")));
		vehIndicators.setLastOdometer((String)UtilityObjects.checkforEmpty((String)(((Map<String,Object>)canonical.get("vinIndicators")).get("lastOdometer"))));
		vehIndicators.setNam((String)UtilityObjects.checkforValue((String)(((Map<String,Object>)canonical.get("vinIndicators")).get("nam"))));
		vehIndicators.setOtherOdometerBrand((String)UtilityObjects.checkforEmpty((String)(((Map<String,Object>)canonical.get("vinIndicators")).get("otherOdometerBrand"))));
		vehIndicators.setOdometerIndpdntSrc((String)(((Map<String,Object>)canonical.get("vinIndicators")).get("odometerIndependentSource")));
		vehIndicators.setSalvage((String)(((Map<String,Object>)canonical.get("vinIndicators")).get("salvage")));
		vehIndicators.setEml((String)(((Map<String,Object>)canonical.get("vinIndicators")).get("exceedsMechanicalLimits")));
		vehIndicators.setFailedEmission((String)(((Map<String,Object>)canonical.get("vinIndicators")).get("failedEmission")));
		vehIndicators.setDamage((String)(((Map<String,Object>)canonical.get("vinIndicators")).get("damage")));
		vehIndicators.setWaterDamage((String)(((Map<String,Object>)canonical.get("vinIndicators")).get("water_Damage")));
		vehIndicators.setRebuilt((String)(((Map<String,Object>)canonical.get("vinIndicators")).get("rebuilt")));
		vehIndicators.setTheft((String)(((Map<String,Object>)canonical.get("vinIndicators")).get("theft")));
		vehIndicators.setLemon((String)(((Map<String,Object>)canonical.get("vinIndicators")).get("lemon")));
		vehIndicators.setUsedForRentalFleet((String)(((Map<String,Object>)canonical.get("vinIndicators")).get("usedForRentalFleet")));

		return vehIndicators;
	}

	
	
	@SuppressWarnings("unchecked")
	public Inventory getVehInventory_populateData(Map<String,Object> canonical) {
		Inventory _inventory = new Inventory();

		Make _make = new Make();
		_make.setDescription((String)((Map<String,Object>)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("model")).get("make")).get("description"));

		Model model = new Model();
		model.setDescription((String)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("model")).get("description"));
		
		model.setMake(_make);

		VehicleVinAttributes vehicleVinAttributes = new VehicleVinAttributes();
		vehicleVinAttributes.setTrim((String)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("vinAttributes")).get("trim"));
		vehicleVinAttributes.setBodystyle((String)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("vinAttributes")).get("bodyStyle"));
		vehicleVinAttributes.setEsize((String)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("vinAttributes")).get("esize"));
		vehicleVinAttributes.setCarburetion((String)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("vinAttributes")).get("carburetion"));
		vehicleVinAttributes.setEheadconfig((String)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("vinAttributes")).get("eheadConfig"));
		vehicleVinAttributes.setEblocktype((String)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("vinAttributes")).get("eblockType"));
		vehicleVinAttributes.setCylinder((String)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("vinAttributes")).get("cylinder"));
		vehicleVinAttributes.setTrans((String)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("vinAttributes")).get("transmission"));
		vehicleVinAttributes.setWheelbase((String)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("vinAttributes")).get("wheelBase"));
		vehicleVinAttributes.setLength((String)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("vinAttributes")).get("length"));
		vehicleVinAttributes.setWidth((String)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("vinAttributes")).get("width"));
		vehicleVinAttributes.setHeight((String)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("vinAttributes")).get("height"));
		vehicleVinAttributes.setGas((String)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("vinAttributes")).get("gas"));
		vehicleVinAttributes.setSeats((String)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("vinAttributes")).get("seats"));
		vehicleVinAttributes.setEncyclopedia((String)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("vinAttributes")).get("encyclopedia"));
		vehicleVinAttributes.setPedigree((String)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("vinAttributes")).get("pedigree"));
		vehicleVinAttributes.setCity((String)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("vinAttributes")).get("city"));
		vehicleVinAttributes.setHwy((String)((Map<String,Object>)((Map<String,Object>)canonical.get("vehicle")).get("vinAttributes")).get("hwy"));
		
		
		Vehicle vehicle = new Vehicle();
		
	
		if(!(((String)((Map<String,Object>)canonical.get("vehicle")).get("buyersGuideTemplate")).equals("templateNotfound"))){
		byte[] decoded = Base64.getDecoder().decode(((String)((Map<String,Object>)canonical.get("vehicle")).get("buyersGuideTemplate")).getBytes());
		vehicle.setTemplate(decoded);
		}
		else{
			vehicle.setdefaultTemplate("noTemplateFoundInSalesForce");
		}
		
		
		if(!("").equals((String)((Map<String,Object>)canonical.get("vehicle")).get("year")) && null != (String)((Map<String,Object>)canonical.get("vehicle")).get("year")){
		vehicle.setYear(Integer.parseInt((String)((Map<String,Object>)canonical.get("vehicle")).get("year")));
		}
		vehicle.setModel(model);
		vehicle.setVinAttributes(vehicleVinAttributes);
		vehicle.setVin((String)((Map<String,Object>)canonical.get("vehicle")).get("vin"));
		vehicle.setColor((String)((Map<String,Object>) canonical.get("vehicle")).get("color"));
		
		ArrayList<Object> options=null;
		if(((Map<String,Object>)canonical.get("vehicle")).get("options") != null)
		{
		  options = ((ArrayList<Object>)(((Map<String,Object>)canonical.get("vehicle")).get("options")));
		  for(int i=0; i < options.size() ; i++)
		  {
			String type = (String) ((Map<String,Object>)options.get(i)).get("type");
			String description = (String) ((Map<String,Object>)options.get(i)).get("description");
			VehicleOption vo=new VehicleOption(type,description);
			vehicle.addOption(vo);
		  }
		}
		
		Dealer dealer = new Dealer();
		dealer.setName((String)((Map<String,Object>)canonical.get("dealer")).get("name"));
		dealer.setAddress(((Map<String,Object>)canonical.get("dealer")).get("displayAddress").equals("YES")?(String)((Map<String,Object>)canonical.get("dealer")).get("address"):null);
		dealer.setCity((String)((Map<String,Object>)canonical.get("dealer")).get("city"));
		dealer.setState((String)((Map<String,Object>)canonical.get("dealer")).get("State"));
		dealer.setZip((String)((Map<String,Object>)canonical.get("dealer")).get("zip"));
		dealer.setSalesPhone((String)((Map<String,Object>)canonical.get("dealer")).get("salesPhone"));
		dealer.setFinalPriceLabel((String)((Map<String,Object>)canonical.get("dealer")).get("priceLabel"));
		dealer.setWebsiteURL((String)((Map<String,Object>)canonical.get("dealer")).get("websiteURL"));
		dealer.setWindowStickerFreeFormData((String)((Map<String,Object>)canonical.get("dealer")).get("windowStickerFreeFormData"));
		
		DealerProduct dealerProduct = new DealerProduct();
		Product product=new Product();
		product.setAgrFormNo((String)(((Map<String,Object>)((Map<String,Object>)canonical.get("dealerProduct")).get("product")).get("agrFormNo")));
		product.setTermMiles(String.valueOf((((Map<String,Object>)((Map<String,Object>)canonical.get("dealerProduct")).get("product")).get("termMiles"))));
		product.setTermMonths(String.valueOf((((Map<String,Object>)((Map<String,Object>)canonical.get("dealerProduct")).get("product")).get("termMonths"))));
		product.setFormDeductible(String.valueOf((((Map<String,Object>)((Map<String,Object>)canonical.get("dealerProduct")).get("product")).get("formDeductible"))));
		product.setPlatinumMiles(String.valueOf((((Map<String,Object>)((Map<String,Object>)canonical.get("dealerProduct")).get("product")).get("platinumMiles"))));
		product.setPlatinumMonths(String.valueOf((((Map<String,Object>)((Map<String,Object>)canonical.get("dealerProduct")).get("product")).get("platinumMonths"))));
		product.setAddOnMile(Boolean.parseBoolean((String)(((Map<String,Object>)((Map<String,Object>)canonical.get("dealerProduct")).get("product")).get("addOnMile"))));
		dealerProduct.setProduct(product);
		
		_inventory.setVehicle(vehicle);
		_inventory.setStockNo((String)canonical.get("stockNo"));
		_inventory.setDealerProduct(dealerProduct);
		if(!("").equals((String)canonical.get("mileage")) && null != (String)canonical.get("mileage"))
		{
		_inventory.setMileage(Integer.parseInt((String)canonical.get("mileage")));
		}
		_inventory.setDealer(dealer);
		if(((Map<String,Object>)canonical.get("dealer")).get("displayListPrice").equals("YES")){
			_inventory.setPrintPrices(1);			
		}else{
			_inventory.setPrintPrices(0);
		}
		if((!("").equals((String)((Map<String,Object>)canonical.get("vehicle")).get("listPrice"))) && (((Map<String,Object>)canonical.get("vehicle")).get("listPrice") != null)){
		_inventory.setListPrice(new BigDecimal((String)((Map<String,Object>)canonical.get("vehicle")).get("listPrice")));
		}

		return _inventory;
	}
	
	
	/*For ordering the templates */
	public List<String> ordertemplates(List<String> forms)
	{
		List<String> orderred_templates = new ArrayList<String>(5);
		
		orderred_templates.add("");
		orderred_templates.add("");
		orderred_templates.add("");
		orderred_templates.add("");
		orderred_templates.add("");
		
		String formName= null;
		for(int j=0; j<forms.size(); j++)
		{
			 formName = (String)forms.get(j);
			 if(formName.equals("VehicleDetails"))
			 {
				 orderred_templates.set(0, formName);
			 }
			 else if(formName.equals("VehicleBrochure"))
			 {
				 orderred_templates.set(1, formName); 
			 } 
			 else if(formName.equals("BuyersGuide"))
			 {
				 orderred_templates.set(2, formName);
			 }
			 else if(formName.equals("VehicleTitle"))
			 {
				 orderred_templates.set(3, formName);
			 }
			 else if(formName.equals("BuyersGuideSpanish"))
			 {
				 orderred_templates.set(4, formName);
			 }
		}
		return orderred_templates;
	}
}