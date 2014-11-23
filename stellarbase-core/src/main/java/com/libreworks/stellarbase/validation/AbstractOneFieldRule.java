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

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

/**
 * An abstract rule to allow processing of one field.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 * @param <T> The type of property to validate
 */
public abstract class AbstractOneFieldRule<T> extends AbstractRule
{
	protected String field;

	/**
	 * @param field The field to validate
	 */
	public AbstractOneFieldRule(String field)
	{
		Assert.notNull(field);
		this.field = field;
	}
	
	@Override
	public String getConstraints()
	{
		return field + ":" + getConstraintLabel();
	}
	
	/**
	 * Performs the real validation.
	 * 
	 * @param value The value retrieved from the bean
	 * @param errors The errors
	 */
	protected abstract void validateField(T value, Errors errors);
	
	/**
	 * Gets a label about the Rule's constraints.
	 * 
	 * For example, a regular expression's constraint is its pattern. A greater
	 * than constraint would be the number the value must be greater than.
	 * 
	 * @return The constraint label
	 */
	public abstract String getConstraintLabel();
	
	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.validation.Rule#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@SuppressWarnings("unchecked")
    public final void validate(Object target, Errors errors)
	{
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(target);
		validateField((T)bw.getPropertyValue(field), errors);
	}
}
