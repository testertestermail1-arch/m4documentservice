package com.twg.api.experience_forms_api.request;


import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * Holds the contact information.
 *
 * @author Juan Berrueta
 */

public class Contact
{
	private String type;
	private ContactType typeEnum;
	private String salutation;
	private String firstName;
	private String lastName;
	private String middleInitialName;
	private String email;
	private Address address;
	private Set<Phone> phones;
	public String memberNumber;
	private String middleName;
	public String getMemberNumber()
	{
		return memberNumber;
	}

	public void setMemberNumber(String memberNumber)
	{
		this.memberNumber = memberNumber;
	}

	/**
	 * Creates the contact.<br>
	 * This method may be required by the services framework.
	 */
	public Contact()
	{}

	public String getType()
	{
		return this.type;
	}

	public ContactType getTypeEnum()
	{
		return this.typeEnum;
	}

	public String getSalutation()
	{
		return this.salutation;
	}

	public String getFirstName()
	{
		return this.firstName;
	}

	public String getLastName()
	{
		return this.lastName;
	}

	public String getMiddleInitialName()
	{
		return this.middleInitialName;
	}

	public String getEmail()
	{
		return this.email;
	}

	public Address getAddress()
	{
		return this.address;
	}

	public Set<Phone> getPhones()
	{
		return this.phones;
	}

	public String getFullName()
	{
		StringBuffer fullName = new StringBuffer();
		fullName.append(this.salutation != null ? this.salutation : "").append(" ");
		fullName.append(this.firstName != null ? this.firstName : "").append(" ");
		fullName.append(this.middleInitialName != null ? this.middleInitialName : "").append(" ");
		fullName.append(this.lastName != null ? this.lastName : "");
		return fullName.toString().trim();
	}

	public String getFullNameMX()
	{
		StringBuffer fullName = new StringBuffer();
		fullName.append(this.salutation != null ? this.salutation : "").append(" ");
		fullName.append(this.firstName != null ? this.firstName : "").append(" ");
		fullName.append(this.middleName != null ? this.middleName : "").append(" ");
		fullName.append(this.lastName != null ? this.lastName : "");
		return fullName.toString().trim();
	}
	/**
	 * Returns the first phone in the set that has the given type.
	 *
	 * @param type
	 *            the phone type.
	 * @return the first phone found or null if there wasn't any.
	 * @throws IllegalArgumentException
	 *             if the provided type is null.
	 */

	/**
	 * Returns the main phone in the set.
	 *
	 * @return the main phone found or null if there wasn't any.
	 * @throws IllegalArgumentException
	 *             if the provided type is null.
	 */

	/**
	 * Validates the contact.<br>
	 * The contact is invalid if any of the following occur:
	 * <ul>
	 * <li>the contact type is null or invalid</li>
	 * <li>the memberNumber is blank or contains whitespaces only</li>
	 * <li>the salutation is blank or contains whitespaces only</li>
	 * <li>the firstName is null, blank or contains whitespaces only</li>
	 * <li>the lastName is null, blank or contains whitespaces only for
	 * non-lienholders</li>
	 * <li>the middleInitialName is blank or contains whitespaces only</li>
	 * <li>the gender is invalid</li>
	 * <li>the email is invalid</li>
	 * <li>the address is null or invalid</li>
	 * <li>any of the phones are invalid</li>
	 * </ul>
	 *
	 * @param contentType
	 * @return contactErrors
	 */
	public Map validate(String contentType)
	{
		Map<String, String> contactErrors = new HashMap<>();

		boolean skipFullNameCheck = false;
		if (StringUtils.isBlank(this.firstName) && !StringUtils.isBlank(this.lastName))
		{
			//firstName = lastName;
			skipFullNameCheck = true;
		}
		else if (!StringUtils.isBlank(this.firstName) && StringUtils.isBlank(this.lastName))
		{
			skipFullNameCheck = true;
		}

		if (!skipFullNameCheck)
		{
			if (StringUtils.isBlank(this.firstName))
			{
				contactErrors.put("firstName",
						"The contact first name cannot be null, blank nor contain whitespaces only.");
			}
			if (StringUtils.isBlank(this.lastName) && !ContactType.Lienholder.equals(this.type))
			{
				contactErrors.put("lastName",
						"The contact last name cannot be null, blank or contain whitespaces only for non-lienholders.");
			}
		}

		if (this.email != null && !StringUtils.isBlank(this.email.trim())
				&& !EmailValidator.getInstance().isValid(email))
		{
			contactErrors.put("email", "The contact email should be a valid email address.");
		}

		if ((this.address == null) && (!ContactType.CoBuyer.equals(this.type)))
		{
			contactErrors.put("type", "The contact address cannot be null.");
		}

		if (this.address != null && this.type != "CB")
		{
			contactErrors.putAll(this.address.validate(contentType));
		}
		if (this.address.getCountryCode() != null
				&& (this.address.getCountryCode().equals("USA") || this.address.getCountryCode().equals("US"))
				&& (this.address.getStateCode() != null && !this.address.getStateCode().equals("PR")))
		{
			for (Phone phone : this.getPhones())
			{
				if (StringUtils.equals(contentType, "XML") && phone != null)
				{
					if ( phone.getAreaCode() == null && !StringUtils.isBlank(phone.getNumber()))
					{
					if ( !(phone.getNumber().matches("\\d{10}")
							|| phone.getNumber().matches("\\d{3}[-]\\d{3}[-\\s]\\d{4}")
							|| phone.getNumber().matches("\\d{3}[\\s-]\\d{3}[-]\\d{4}")
							|| phone.getNumber().matches("\\(\\d{3}\\)[\\s]?\\d{3}-\\d{4}")
							|| phone.getNumber().matches("\\d{6}[-]\\d{4}")
							|| phone.getNumber().matches("\\d{7}")
							|| phone.getNumber().matches("\\d{3}[-\\s]\\d{4}")))
						{
						contactErrors.put("phone", "The contact phone is invalid.");
						}
					}
				else if (phone.getAreaCode() != null && StringUtils.isBlank(phone.getNumber()))
				{
					contactErrors.put("phone", "The contact phone is invalid.");
				}
					else if ((phone.getAreaCode() != null && !StringUtils.isBlank(phone.getNumber()))
							&& (!phone.getAreaCode().toString().matches("\\d{3}")
									|| !(phone.getNumber().matches("\\d{7}") || phone.getNumber().matches("\\d{3}[-\\s]\\d{4}"))))
					{
						contactErrors.put("phone", "The contact phone is invalid.");
					}
				}
				if (StringUtils.equals(contentType, "JSON") && phone != null)
				{
					if (!StringUtils.isBlank(phone.getNumber()) && !(phone.getNumber().matches("\\d{10}")
							|| phone.getNumber().matches("\\d{3}[-]\\d{3}[-\\s]\\d{4}")
							|| phone.getNumber().matches("\\d{3}[\\s-]\\d{3}[-]\\d{4}")
							|| phone.getNumber().matches("\\(\\d{3}\\)[\\s]?\\d{3}-\\d{4}")
							|| phone.getNumber().matches("\\d{6}[-]\\d{4}")
							|| phone.getNumber().matches("\\d{7}")
							|| phone.getNumber().matches("\\d{3}[-\\s]\\d{4}")))
					{
						contactErrors.put("phone", "The contact phone is invalid.");
					}
				}
			}
			if (!StringUtils.isBlank(this.address.getPostalCode()) && this.address.getPostalCode().matches("\\d{9}"))
			{
				String formattedZip = MessageFormat.format("{0}-{1}", this.address.getPostalCode().substring(0, 5),
						this.address.getPostalCode().substring(5));
				this.address.setPostalCode(formattedZip);
			}
			if (!StringUtils.isBlank(this.address.getPostalCode())
					&& !(this.address.getPostalCode().matches("\\d{5}[-\\s]\\d{4}")
							|| this.address.getPostalCode().matches("\\d{5}")))
			{
				contactErrors.put("zip/postal code", "The contact Zip/Postal Code is invalid.");
			}
		}
		return contactErrors;
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("type", this.type).append("salutation", this.salutation)
				.append("firstName", this.firstName).append("lastName", this.lastName)
				.append("middleInitialName", this.middleInitialName).append("email", this.email)
				.append("address", this.address).append("phones", ArrayUtils.toString(this.phones)).toString();
	}

	public void setFirstName(String firstName)
	{
		// TODO Auto-generated method stub
		this.firstName = firstName;

	}

	public void setType(String type)
	{
		// TODO Auto-generated method stub
		this.type = type;
	}

	public void setSalutation(String salutation)
	{
		// TODO Auto-generated method stub
		this.salutation = salutation;
	}

	public void setLastName(String lastName)
	{
		// TODO Auto-generated method stub
		this.lastName = lastName;
	}

	public void setMiddleInitialName(String middleInitialName)
	{
		String middleName = middleInitialName;


		if(middleName != null && middleName.length() != 0 && !(middleName.isEmpty()) && middleName.length() >= 1){
			middleName=middleName.trim();
			middleName=middleName.replaceAll("\\s+","");
			middleName = middleName.substring(0,1);
			middleName=middleName.toUpperCase();
		this.middleInitialName = middleName;
		}
		else
		{
			this.middleInitialName = "";
		}
	}

	public void setEmail(String email)
	{
		// TODO Auto-generated method stub
		this.email = email;
	}

	public void setAddress(Address address)
	{
		// TODO Auto-generated method stub
		this.address = address;
	}

	public void setPhones(Set<Phone> phones)
	{
		// TODO Auto-generated method stub
		this.phones = phones;
	}

	public Phone getMainPhone() throws IllegalArgumentException
	{
		Phone phone = this.getPhoneByType(PhoneType.Home);
		if (phone != null)
		{
			return phone;
		}
		phone = this.getPhoneByType(PhoneType.Mobile);
		if (phone != null)
		{
			return phone;
		}
		return this.getPhoneByType(PhoneType.Work);
	}

	public Phone getPhoneByType(PhoneType type) throws IllegalArgumentException
	{
		Validate.notNull(type);
		if (this.phones == null)
		{
			return null;
		}
		for (Phone phone : this.phones)
		{
			if (type.equals(phone.getTypeEnum()))
			{
				return phone;
			}
		}
		return null;
	}
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
}
