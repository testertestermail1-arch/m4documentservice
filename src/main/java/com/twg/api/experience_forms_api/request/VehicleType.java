package com.twg.api.experience_forms_api.request;


import java.text.MessageFormat;

public enum VehicleType 
{
	Watercraft("W"), Powersport("P"), Car("C"), Truck("T"), Snowmobile("S"), Motorcycle("M"), Recreational("R"), AllTerrain("A"), CoachOnly("CO"), MotorHome("MH"), PersonalWatercraft("PW"),
	Van("V"),TravelTrailer("TT"),FifthWheel("FW"),FoldingCampers("FC"),SlideIn("SI"),RecreationalHauler("RH"),RecreationalMotorhome("RM"),RecreationalPopUp("RP"),RecreationalSlideIn("RS"), RecreationalTravelTrailer("RT"), HeavyTruck("HT");
	private String code;
	private VehicleType(String code) 
	{
		this.code = code;
	}
	
	@Override
	public String toString() 
	{
		return this.code;
	}
	
	public static VehicleType find(String code) 
	{
		for (VehicleType type : VehicleType.values()) 
		{
			if (type.code.equals(code)) 
			{
				return type;
			}
		}
		throw new IllegalArgumentException(MessageFormat.format("No vehicle type found with the provided code. [code={0}]", code));
	}
	
}
