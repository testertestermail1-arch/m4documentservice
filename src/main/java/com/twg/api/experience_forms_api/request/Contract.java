package com.twg.api.experience_forms_api.request;


import java.util.Date;

/**
 * Holds the contact information.
 * @author Juan Berrueta
 */

public class Contract 
{
	boolean coveragePlanPowerTrain;
	boolean coveragePlanBase;
	boolean coveragePlanHighTech;
	boolean coveragePlanComprehensive;
	boolean coveragePlanHighTechWrap;
	boolean coveragePlanComprehensiveWrap;
	int contractTermMonths;
	int contractTermDistance;
	int deductible;
	boolean disappearingDeductible;
	int inServiceDateTermMonths;
	int inServiceDateTermDistance;
	Date contractPurchaseDate;
	Date inServiceDate;
	int serviceContractPrice;

	public boolean isCoveragePlanPowerTrain() 
	{
		return coveragePlanPowerTrain;
	}

	public void setCoveragePlanPowerTrain(boolean coveragePlanPowerTrain) 
	{
		this.coveragePlanPowerTrain = coveragePlanPowerTrain;
	}

	public boolean isCoveragePlanBase() 
	{
		return coveragePlanBase;
	}

	public void setCoveragePlanBase(boolean coveragePlanBase) 
	{
		this.coveragePlanBase = coveragePlanBase;
	}

	public boolean isCoveragePlanHighTech() 
	{
		return coveragePlanHighTech;
	}

	public void setCoveragePlanHighTech(boolean coveragePlanHighTech) 
	{
		this.coveragePlanHighTech = coveragePlanHighTech;
	}

	public boolean isCoveragePlanComprehensive() 
	{
		return coveragePlanComprehensive;
	}

	public void setCoveragePlanComprehensive(boolean coveragePlanComprehensive) 
	{
		this.coveragePlanComprehensive = coveragePlanComprehensive;
	}

	public boolean isCoveragePlanHighTechWrap() 
	{
		return coveragePlanHighTechWrap;
	}

	public void setCoveragePlanHighTechWrap(boolean coveragePlanHighTechWrap) 
	{
		this.coveragePlanHighTechWrap = coveragePlanHighTechWrap;
	}

	public boolean isCoveragePlanComprehensiveWrap() 
	{
		return coveragePlanComprehensiveWrap;
	}

	public void setCoveragePlanComprehensiveWrap(boolean coveragePlanComprehensiveWrap) 
	{
		this.coveragePlanComprehensiveWrap = coveragePlanComprehensiveWrap;
	}

	public int getContractTermMonths() 
	{
		return contractTermMonths;
	}

	public void setContractTermMonths(int contractTermMonths) 
	{
		this.contractTermMonths = contractTermMonths;
	}

	public int getContractTermDistance() 
	{
		return contractTermDistance;
	}

	public void setContractTermDistance(int contractTermDistance) 
	{
		this.contractTermDistance = contractTermDistance;
	}

	public int getDeductible() 
	{
		return deductible;
	}

	public void setDeductible(int deductible) 
	{
		this.deductible = deductible;
	}

	public boolean isDisappearingDeductible() 
	{
		return disappearingDeductible;
	}

	public void setDisappearingDeductible(boolean disappearingDeductible) 
	{
		this.disappearingDeductible = disappearingDeductible;
	}

	public int getInServiceDateTermMonths() 
	{
		return inServiceDateTermMonths;
	}

	public void setInServiceDateTermMonths(int inServiceDateTermMonths) 
	{
		this.inServiceDateTermMonths = inServiceDateTermMonths;
	}

	public int getInServiceDateTermDistance() 
	{
		return inServiceDateTermDistance;
	}

	public void setInServiceDateTermDistance(int inServiceDateTermDistance) 
	{
		this.inServiceDateTermDistance = inServiceDateTermDistance;
	}

	public Date getContractPurchaseDate() 
	{
		return contractPurchaseDate;
	}

	public void setContractPurchaseDate(Date contractPurchaseDate) 
	{
		this.contractPurchaseDate = contractPurchaseDate;
	}

	public Date getInServiceDate() 
	{
		return inServiceDate;
	}

	public void setInServiceDate(Date inServiceDate) 
	{
		this.inServiceDate = inServiceDate;
	}

	public int getServiceContractPrice() 
	{
		return serviceContractPrice;
	}

	public void setServiceContractPrice(int serviceContractPrice) 
	{
		this.serviceContractPrice = serviceContractPrice;
	}
}
