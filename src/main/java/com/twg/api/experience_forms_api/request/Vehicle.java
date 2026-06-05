package com.twg.api.experience_forms_api.request;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Holds the vehicle information.
 * @author Juan Berrueta
 */

public class Vehicle {
	private String model;
	private String make;
	private Date inServiceDate;
	private String vin;
	private String year;
	private int odometer;
	private int odometerAtPurchase;
	private boolean diesel;
	private boolean fourByFour;
	private boolean turbo;
	private boolean newCar;
	private String vehicleClass;
	private Double vehiclePurchasePrice;
	private boolean commercialUsage;
	private boolean hasNavigation;
	private String vehicleType;
	private Date maturityDate;
	private String vehicleCode;
	private String expirationType;
	private String vehicleNewOrUsed;
	private Date vehiclePurchaseDate;	
	private Date registrationDateEMDCS;
	private Double coverageStartMileage;
	private String transmission;
	private Date manufacturerWarrantyEndDate;
	private Double manufacturerWarrantyEndMileage; 
	private Double expirationMileage;
	private Date coverageStartDate;
	private Double totalRetailCost;
	private Double totalBilledCost;
	private Date contractStartDate;
	private Double retailPrice;
	private Date expirationDate;
	private Double purchasePricesales;
	private String customerNumber;
	private Date createdDate;
	private String engineNumber;
	private Double customerCost;
	private String gapProductTenure;
	private String chassisWarrantyTermMonth;
	private String manufacturerWarrantyTermMonth;
	private String trim;
	private String vehicleRegistrationNumber;
	
	
	public String getVehicleRegistrationNumber() {
		return vehicleRegistrationNumber;
	}

	public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
		this.vehicleRegistrationNumber = vehicleRegistrationNumber;
	}
	
	public String getTrim() {
		return trim;
	}

	public void setTrim(String trim) {
		this.trim = trim;
	}

	public String getChassisWarrantyTermMonth() {
		return chassisWarrantyTermMonth;
	}

	public void setChassisWarrantyTermMonth(String chassisWarrantyTermMonth) {
		this.chassisWarrantyTermMonth = chassisWarrantyTermMonth;
	}

	public String getManufacturerWarrantyTermMonth() {
		return manufacturerWarrantyTermMonth;
	}

	public void setManufacturerWarrantyTermMonth(String manufacturerWarrantyTermMonth) {
		this.manufacturerWarrantyTermMonth = manufacturerWarrantyTermMonth;
	}

	public String getExpirationType() {
		return expirationType;
	}

	public void setExpirationType(String expirationType) {
		this.expirationType = expirationType;
	}

	public void setHasNavigation(boolean hasNavigation) {
		this.hasNavigation = hasNavigation;
	}

	public String getVehicleCode() {
		return vehicleCode;
	}

	public void setVehicleCode(String vehicleCode) {
		this.vehicleCode = vehicleCode;
	}

	public Date getMaturityDate() 
	{
		return maturityDate;
	}

	public void setMaturityDate(Date maturityDate) 
	{
		this.maturityDate = maturityDate;
	}

	public String getVehicleType() 
	{
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) 
	{
		this.vehicleType = vehicleType;
	}

	public boolean getHasNavigation() 
	{
		return hasNavigation;
	}

	public void setNavigation(boolean hasNavigation) 
	{
		this.hasNavigation = hasNavigation;
	}

	public boolean isCommercialUsage() 
	{
		return commercialUsage;
	}

	public void setCommercialUsage(boolean commercialUsage) 
	{
		this.commercialUsage = commercialUsage;
	}

	private boolean hybrid;
	private boolean car;
	private boolean truck;
	
	public boolean isTruck() 
	{
		return truck;
	}

	public void setTruck(boolean truck) 
	{
		this.truck = truck;
	}

	public boolean isCar() 
	{
		return car;
	}

	public void setCar(boolean car) 
	{
		this.car = car;
	}

	public boolean isHybrid() 
	{
		return hybrid;
	}

	public void setHybrid(boolean hybrid) 
	{
		this.hybrid = hybrid;
	}

	public void setInServiceDate(Date inServiceDate) 
	{
		this.inServiceDate = inServiceDate;
	}

	/**
	 * Creates a vehicle.<br>
	 * This method may be required by the services framework.
	 */
	public Vehicle() 
	{}
	
	
	public String getModel() 
	{
		return this.model;
	}

	public String getMake() 
	{
		return this.make;
	}
	
	public Date getInServiceDate() 
	{
		return this.inServiceDate;
	}
	
	public String getVin() 
	{
		return this.vin;
	}
	
	public String getYear() 
	{
		return this.year;
	}
	
	public int getOdometer() 
	{
		return this.odometer;
	}
	
	public int getOdometerAtPurchase() 
	{
		return this.odometerAtPurchase;
	}
	
	/**
	 * Validates the vehicle.<br>
	 * The vehicle is invalid if any of the following occur:
	 * <ul>
	 * <li>the type is null or invalid</li>
	 * <li>the model is null or invalid</li>
	 * <li>the inServiceDate is null</li>
	 * <li>the odometerAtPurchase is less than zero</li>
	 * <li>the vin is null, blank or contains whitespaces only</li>
	 * <li>the vehicleClass is null, blank or contains whitespaces only</li>
	 * <li>the year is null, blank or contains whitespaces only</li>
	 * <li>the price is less than zero</li>
	 * <li>the odometer is less than zero</li>
	 * </ul>
	 * @param contentType
	 * @return vehicleErrors
	 */
	public Map validate(String contentType) 
	{
		Map<String,String> vehicleErrors = new HashMap<String,String>();
		try {
			if(this.vehicleType == null || (this.vehicleType).equals("") || !(this.vehicleType).equals((VehicleType.find(this.vehicleType).toString())))
		{
			vehicleErrors.put("vehicle type", "The vehicle type should be one of the following values: "+ArrayUtils.toString(VehicleType.values())+".");
		}
			} 
		catch (Exception exception) 
		{
			vehicleErrors.put("vehicle type", "The vehicle type should be one of the following values: "+ArrayUtils.toString(VehicleType.values())+".");
		}
		if (this.model == null || (this.model).equals("")) 
		{
			vehicleErrors.put("vehicle model", "The vehicle model cannot be null.");
		} 
		if (this.make == null || (this.make).equals("")) 
		{
			vehicleErrors.put("vehicle make", "The make name cannot be null, blank nor contain whitespaces only.");
		}
		if (this.inServiceDate == null) 
		{
			vehicleErrors.put("vehicle inServiceDate", "The vehicle in-service date cannot be null.");
		}
		if (this.vehicleClass == null || (this.vehicleClass).equals("") || StringUtils.isBlank(this.vehicleClass)) 
		{
			vehicleErrors.put("vehicle class", "The vehicle class cannot be null, blank nor contain whitespaces only.");
		}
		if (this.vin == null || (this.vin).equals("") || StringUtils.isBlank(this.vin)) 
		{
			vehicleErrors.put("vehicle vin", "The vehicle vin cannot be null, blank nor contain whitespaces only.");
		}
		if (this.year == null || (this.year).equals("") || (this.year).equals("null") || StringUtils.isBlank(this.year) || (this.year).equals(null)) 
		{
			vehicleErrors.put("vehicle year", "The vehicle year cannot be null, blank nor contain whitespaces only.");
		}
		if (this.odometer < 0) 
		{
			vehicleErrors.put("vehicle odometer", "The vehicle odometer cannot be less than zero.");
		}
		if (this.odometerAtPurchase < 0) {

			vehicleErrors.put("vehicle odometer At Purchase", "The vehicle odometer at purchase cannot be less than zero.");

		}
		return vehicleErrors;
	}
	
	@Override
	public String toString() 
	{
		return new ToStringBuilder(this).append("model", this.model).append("make", this.make).append("inService", this.inServiceDate)
				.append("vin", this.vin).append("year", this.year).append("odometer", this.odometer).append("isDiesel", this.diesel)
				.append("odometerAtPurchase", this.odometerAtPurchase).append("isFourByFour", this.fourByFour).append("isTurbo", this.turbo).append("isCar", this.car).append("isTruck", this.truck)
				.append("isCommercial", this.commercialUsage).append("isHybrid", this.hybrid).append("hasNavigation", this.hasNavigation)
				.append("isNew", this.newCar).append("vehicleNewOrUsed", this.vehicleNewOrUsed).append("coverageStartMileage", this.coverageStartMileage)
				.append("vehiclePurchaseDate", this.vehiclePurchaseDate).append("registrationDateEMDCS", this.registrationDateEMDCS).append("trim", this.trim).append("vehicleRegistrationNumber", this.vehicleRegistrationNumber).toString();
	}

	public void setModel(String model) 
	{
		// TODO Auto-generated method stub
		this.model=model;
	}

	public void setMake(String make) 
	{
		// TODO Auto-generated method stub
		this.make=make;
	}

	public void setVin(String vin) 
	{
		// TODO Auto-generated method stub
		this.vin=vin;
	}

	public void setYear(String year) 
	{
		// TODO Auto-generated method stub
		this.year=year;
	}

	public boolean isDiesel() 
	{
		return diesel;
	}

	public void setDiesel(boolean diesel) 
	{
		this.diesel = diesel;
	}

	public boolean isFourByFour() 
	{
		return fourByFour;
	}

	public void setFourByFour(boolean fourByFour) 
	{
		this.fourByFour = fourByFour;
	}

	public boolean isTurbo() 
	{
		return turbo;
	}

	public void setTurbo(boolean turbo) 
	{
		this.turbo = turbo;
	}

	public boolean isNewCar() 
	{
		return newCar;
	}

	public void setNewCar(boolean newCar) 
	{
		this.newCar = newCar;
	}

	public void setOdometer(int odometer) 
	{
		this.odometer = odometer;
	}
	
	public void setOdometerAtPurchase(int odometerAtPurchase) 
	{
		this.odometerAtPurchase = odometerAtPurchase;
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
	
	public String getVehicleNewOrUsed() {
		return vehicleNewOrUsed;
	}

	public void setVehicleNewOrUsed(String vehicleNewOrUsed) {
		this.vehicleNewOrUsed = vehicleNewOrUsed;
	}

	public Date getVehiclePurchaseDate() {
		return vehiclePurchaseDate;
	}

	public void setVehiclePurchaseDate(Date vehiclePurchaseDate) {
		this.vehiclePurchaseDate = vehiclePurchaseDate;
	}
	
	
	public int[] getInsuranceStartAndEndDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.vehiclePurchaseDate);
		int startDay = calendar.get(Calendar.DATE);
		//+1 Is Important Because if the month is January then coming 0 so Add +1
		int startMonth = calendar.get(Calendar.MONTH)+1;
		int startYear = calendar.get(Calendar.YEAR);
		calendar.add(Calendar.YEAR, 1);
		int endDay = calendar.get(Calendar.DATE);
		//+1 Is Important Because if the month is January then coming 0 so Add +1
		int endMonth = calendar.get(Calendar.MONTH)+1;
		int endYear = calendar.get(Calendar.YEAR);
		int[] ans = new int[6]; 
        ans[0] = startDay; 
        ans[1] = startMonth;
        ans[2] = startYear; 
        ans[3] = endDay; 
        ans[4] = endMonth; 
        ans[5] = endYear;  
        // returning array of elements 
        return ans; 
	}

	public Date getRegistrationDateEMDCS() {
		return registrationDateEMDCS;
	}

	public void setRegistrationDateEMDCS(Date registrationDateEMDCS) {
		this.registrationDateEMDCS = registrationDateEMDCS;
	}

	public Double getCoverageStartMileage() {
		return coverageStartMileage;
	}

	public void setCoverageStartMileage(Double coverageStartMileage) {
		this.coverageStartMileage = coverageStartMileage;
	}

	public String getTransmission() {
		return transmission;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	public Date getManufacturerWarrantyEndDate() {
		return manufacturerWarrantyEndDate;
	}

	public void setManufacturerWarrantyEndDate(Date manufacturerWarrantyEndDate) {
		this.manufacturerWarrantyEndDate = manufacturerWarrantyEndDate;
	}

	public Double getManufacturerWarrantyEndMileage() {
		return manufacturerWarrantyEndMileage;
	}

	public void setManufacturerWarrantyEndMileage(Double manufacturerWarrantyEndMileage) {
		this.manufacturerWarrantyEndMileage = manufacturerWarrantyEndMileage;
	}

	public Double getExpirationMileage() {
		return expirationMileage;
	}

	public void setExpirationMileage(Double expirationMileage) {
		this.expirationMileage = expirationMileage;
	}

	public Date getCoverageStartDate() {
		return coverageStartDate;
	}

	public void setCoverageStartDate(Date coverageStartDate) {
		this.coverageStartDate = coverageStartDate;
	}

	public Double getTotalRetailCost() {
		return totalRetailCost;
	}

	public void setTotalRetailCost(Double totalRetailCost) {
		this.totalRetailCost = totalRetailCost;
	}

	public Double getTotalBilledCost() {
		return totalBilledCost;
	}

	public void setTotalBilledCost(Double totalBilledCost) {
		this.totalBilledCost = totalBilledCost;
	}

	public Date getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public Double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Double getPurchasePricesales() {
		return purchasePricesales;
	}

	public void setPurchasePricesales(Double purchasePricesales) {
		this.purchasePricesales = purchasePricesales;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getEngineNumber() {
		return engineNumber;
	}

	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	} 

	public Double getCustomerCost() {
		return customerCost;
	}

	public void setCustomerCost(Double customerCost) {
		this.customerCost = customerCost;
	}
	
	public String getGapProductTenure() {
		return gapProductTenure;
	}


	public void setGapProductTenure(String gapProductTenure) {
		this.gapProductTenure = gapProductTenure;
	}
	
}
