package com.twg.api.service_forms.constants;

import java.util.EnumSet;

public enum FinanceType {
	Finance,
	Lease,
	Cash,
	Loan,
	OtherLoan,
	Balloon;
	
	  public static boolean contains(String s)
	  {
	      for(FinanceType field:values())
	           if (field.name().equals(s)) 
	              return true;
	      return false;
	  } 
	  
	  public static boolean contains(EnumSet<FinanceType> financeTypes, String s)
	  {
	      for(FinanceType field:financeTypes)
	           if (field.name().equals(s)) 
	              return true;
	      return false;
	  } 
	  
	  public static EnumSet<FinanceType> lenderFinanceTypes(){
		  EnumSet<FinanceType> financeTypes = EnumSet.of( Finance , Lease );
		  return financeTypes;
	  }
}
