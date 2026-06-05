package com.twg.api.experience_forms_api.request;


import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.twg.api.experience_forms_api.DocumentError;

/**
 * Holds the form information.
 * @author Juan Berrueta
 */
public class Form 
{
	private String type;
	private FormType typeEnum;
	private String number;
	private boolean isCombo;
	
	/**
	 * Creates the form.<br>
	 * This method may be required by the services framework.
	 */
	protected Form() 
	{}
	
	public String getType() 
	{
		return this.type;
	}
	
	public FormType getTypeEnum() 
	{
		return this.typeEnum;
	}
	
	public String getNumber() 
	{
		return this.number;
	}
	
	public boolean isCombo() 
	{
		return this.isCombo;
	}
	
	/**
	 * Validates the form.<br>
	 * The form is invalid if any of the following occur:
	 * <ul>
	 * <li>the form type is null or invalid</li>
	 * <li>the number is blank or contains whitespaces only</li>
	 * <li>the number is null and the form type is not Blank</li>
	 * </ul>
	 * @param errors the errors list to fulfill.
	 */
	public void validate(Set<DocumentError> errors) 
	{
		try 
		{
			this.typeEnum = FormType.find(this.type);
		} 
		catch (Exception exception) 
		{
			errors.add(new DocumentError("form.type.invalid", "The form type should be on of the following values: {0}.", ArrayUtils.toString(FormType.values())));
		}
		if (StringUtils.isWhitespace(this.number) || (!FormType.Blank.equals(this.typeEnum) && StringUtils.isBlank(this.number))) 
		{
			errors.add(new DocumentError("form.number.invalid",
					"The form number cannot be blank nor contain whitespaces only, and it can also not be null if the form type is not blank."));
		}
	}
	
	@Override
	public String toString() 
	{
		return new ToStringBuilder(this).append("type", this.type).append("number", this.number).append("isCombo", this.isCombo).toString();
	}
	
}
