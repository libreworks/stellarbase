/**
 * Copyright 2010 LibreWorks contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Jonathan Hawk
 */
package com.libreworks.stellarbase.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.google.common.base.Joiner;
import com.libreworks.stellarbase.text.Characters;
import com.libreworks.stellarbase.util.Arguments;

/**
 * A rule for required fields.
 * 
 * This rule verifies that each of the specified fields is not null, not empty,
 * and not consisting of only whitespace.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class RequiredRule extends AbstractRule
{
	protected String[] fields;
	protected String errorCode;
	
	protected static final String LABEL = "Required";
	
	/**
	 * Creates a new RequiredRule using "field.empty" as the error code.
	 * 
	 * @param fields The fields that cannot be empty or only whitespace
	 */
	public RequiredRule(String... fields)
	{
		this(fields, FIELD_EMPTY);
	}
	
	/**
	 * Creates a new RequiredRule using a custom error code.
	 * 
	 * @param fields The fields that cannot be empty or only whitespace
	 * @param errorCode The custom error code
	 */
	public RequiredRule(String[] fields, String errorCode)
	{
		this.fields = Arguments.checkEmpty(fields, "Must specify at least one field");
		this.errorCode = errorCode;
	}
	
	public String getConstraints()
	{
		return Joiner.on(Characters.COMMA).join(fields);
	}
	
	public String getLabel()
    {
	    return LABEL;
    }

	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.validation.Rule#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors)
	{
		for(String field : fields){
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, field, errorCode);
		}
	}
}
