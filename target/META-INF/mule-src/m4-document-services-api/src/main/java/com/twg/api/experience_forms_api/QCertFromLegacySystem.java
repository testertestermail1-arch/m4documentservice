package com.twg.api.experience_forms_api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.aon.awg.qcert.domain.Inventory;
import com.aon.awg.qcert.domain.VehIndicators;
import com.aon.awg.qcert.domain.vehicle.Vehicle;
import com.aon.awg.qcert.domain.vehicle.VehicleVinAttributes;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import com.aon.awg.qcert.utils.QcertCorePdfConstants.LineConstants;

public class QCertFromLegacySystem {
	

	//create title check pdf
	public byte[] createTitleCheckPdf(VehIndicators vehIndicators, Inventory _inventory) throws Exception {
		
		byte[] bytearray;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			final StickeringCommon common = new StickeringCommon();
			final PdfReader pdfReader = new PdfReader(common.getPdfTemplateAbsoluteFileName("vehicleTitleRpt_template"));
			final PdfStamper pdfStamper = new PdfStamper(pdfReader,(OutputStream)baos);
			
			// Method calls to generate pdf content.
			stampVehicleDetails(pdfStamper, vehIndicators);
			stampVehicleTitle(pdfStamper, _inventory);
			stampDisclaimers(pdfStamper);
			pdfStamper.setFormFlattening(true);
			pdfStamper.close();
			bytearray= baos.toByteArray();
			return bytearray;
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw ioe;
		} catch (DocumentException de) {
			de.printStackTrace();
			throw de;
		}
	}

	private void stampVehicleDetails(PdfStamper _pdfStamper, VehIndicators _vehicleIndicators)
			throws DocumentException, IOException {
		final AcroFields form = _pdfStamper.getAcroFields();
		
		form.setField("vin", _vehicleIndicators.getVin());
		form.setField("assembled", _vehicleIndicators.getCountry());
		form.setField("lastDate", _vehicleIndicators.getLast_Date());
		form.setField("lastReporting", _vehicleIndicators.getLast_State());
		form.setField("lastTitleNumber", _vehicleIndicators.getLast_Title());
		form.setField("duplicateTitle", _vehicleIndicators.getDuplicateTitle());
		form.setField("lastOdometerReading", _vehicleIndicators.getLastOdometer());
		form.setField("notActualMiles", _vehicleIndicators.getNam());
		form.setField("otherOdometerBrand", _vehicleIndicators.getOtherOdometerBrand());
		form.setField("odometerIndSrc", _vehicleIndicators.getOdometerIndpdntSrc());
		form.setField("salvage", _vehicleIndicators.getSalvage());
		form.setField("exceedsMechLim", _vehicleIndicators.getEml());
		form.setField("failedEmission", _vehicleIndicators.getFailedEmission());
		form.setField("damage", _vehicleIndicators.getDamage());
		form.setField("floodDamage", _vehicleIndicators.getWaterDamage());
		form.setField("majorDmgInc", _vehicleIndicators.getRebuilt());
		form.setField("insuranceThfClm", _vehicleIndicators.getTheft());
		form.setField("lemon", _vehicleIndicators.getLemon());
		form.setField("usedForRentalFleet", _vehicleIndicators.getUsedForRentalFleet());
	}

	
	/*
	 * To set the vehicle title header in pdf template
	 */
	private void stampVehicleTitle(PdfStamper _pdfStamper, Inventory _inventory)
			throws DocumentException, IOException {
		final StringBuilder titleChkBuf = new StringBuilder("");
		final Vehicle vehicle = _inventory.getVehicle();
		String titleChk= null;

		if (vehicle != null) {
			
			titleChkBuf.append(null!=vehicle.getYear()?vehicle.getYear():"").append(LineConstants.SPACER_);
			if(null != vehicle.getModel().getMake().getDescription() ){ 
			if( "null" != vehicle.getModel().getMake().getDescription().toString() &&
					!("null").equalsIgnoreCase(vehicle.getModel().getMake().getDescription().toString()))
			{
				titleChkBuf.append(vehicle.getModel().getMake().getDescription()).append(LineConstants.SPACER_);
			}
			}
			
			final VehicleVinAttributes vehicleVinAttributes= vehicle.getVinAttributes();
			if (vehicleVinAttributes != null) {
				if(null != String.valueOf(vehicle.getModel())){ 
				if(	!("null").equalsIgnoreCase(vehicle.getModel().toString()) && !vehicle.getModel().toString().isEmpty())
				{
					titleChkBuf.append(vehicle.getModel()).append(LineConstants.SPACER_);
				}
				}
				
				final String vehicleTrim = vehicle.getTrim();
				if (vehicleTrim != null) {
					titleChkBuf.append(vehicleTrim).append(LineConstants.SPACER_);
				}
				if( null != vehicleVinAttributes.getBodystyle()){ 
				if(!("null").equalsIgnoreCase(vehicleVinAttributes.getBodystyle().toString()) && !vehicleVinAttributes.getBodystyle().toString().isEmpty()){
				titleChkBuf.append(vehicleVinAttributes.getBodystyle()).append(LineConstants.NEW_LINE);
				}
				}
			}
		}
		final AcroFields form = _pdfStamper.getAcroFields();
		titleChk = titleChkBuf.toString().trim();
		if("null".equalsIgnoreCase(titleChk))
		{
			titleChk = "";
		}else{
			titleChk = titleChkBuf.toString().trim();
		}
		form.setField("vehicleTitle", titleChk.toUpperCase());
	}

	private void stampDisclaimers(PdfStamper stamp) throws DocumentException, IOException {
		final AcroFields form = stamp.getAcroFields();
		final StringBuilder disclaimer1 = new StringBuilder("");
		disclaimer1.append("Experian's Reports are compiled from multiple sources. ");
		disclaimer1.append("It is not always possible for Experian to obtain complete discrepancy ");
		disclaimer1.append("information on all vehicles; therefore, there may be other title brands, ");
		disclaimer1.append("odometer readings or discrepancies that apply to a vehicle that are not ");
		disclaimer1.append("reflected on that vehicle's Report. Experian searches data from ");
		disclaimer1.append("additional sources where possible, but all discrepancies may not be ");
		disclaimer1.append("reflected on the Report.");

		final StringBuilder disclaimer2 = new StringBuilder("");
		disclaimer2.append("These Reports are based on information supplied to Experian ");
		disclaimer2.append("ASSUMED by external sources believed to be reliable, BUT NO ");
		disclaimer2.append("RESPONSIBILITY IS BY EXPERIAN OR ITS AGENTS FOR ERRORS, INACCURACIES OR ");
		disclaimer2.append("OMISSIONS. THE REPORTS ARE PROVIDED STRICTLY ON AN \"AS IS WHERE IS\" ");
		disclaimer2.append("BASIS, AND EXPERIAN FURTHER EXPRESSLY DISCLAIMS ALL WARRANTIES, EXPRESS ");
		disclaimer2.append("OR IMPLIED, INCLUDING ANY IMPLIED WARRANTIES OF MERCHANTABILITY OR ");
		disclaimer2.append("FITNESS FOR A PARTICULAR PURPOSE REGARDING THIS REPORT.YOU AGREE TO ");
		disclaimer2.append("INDEMNIFY EXPERIAN FOR ANY CLAIMS OR LOSSES, INCLUDING COSTS, EXPENSES ");
		disclaimer2.append("AND ATTORNEYS FEES, INCURRED BY EXPERIAN ARISING DIRECTLY OR INDIRECTLY ");
		disclaimer2.append("FROM YOUR IMPROPER OR UNAUTHORIZED USE OF AUTOCHECK VEHICLE HISTORY ");
		disclaimer2.append("REPORTS.");

		final StringBuilder disclaimer3 = new StringBuilder("");
		disclaimer3.append("Experian shall not be liable for any delay or failure to provide ");
		disclaimer3.append("an accurate Report if and to the extent which such delay or failure is ");
		disclaimer3.append("caused by events beyond the reasonable control of Experian, including, ");
		disclaimer3.append("without limitation, \"acts of God\", terrorism, or public enemies, labor ");
		disclaimer3.append("disputes, equipment malfunctions, material or component shortages, ");
		disclaimer3.append("supplier failures, embargoes, rationing, acts of local, state or ");
		disclaimer3.append("national governments, or public agencies, utility or communication ");
		disclaimer3.append("failures or delays, fire, earthquakes, flood, epidemics, riots and ");
		disclaimer3.append("strikes.");

		final StringBuilder disclaimer4 = new StringBuilder("");
		disclaimer4.append("These terms and the relationship between you and Experian shall be ");
		disclaimer4.append("governed by the laws of the State of Illinois (USA) without regard to ");
		disclaimer4.append("its conflict of law provisions. You and Experian agree to submit to the ");
		disclaimer4.append("personal and exclusive jurisdiction of the courts located within the ");
		disclaimer4.append("county of Cook, Illinois.");

		form.setField("disclaimer1", disclaimer1.toString());
		form.setField("disclaimer2", disclaimer2.toString());
		form.setField("disclaimer3", disclaimer3.toString());
		form.setField("disclaimer4", disclaimer4.toString());
	}
}