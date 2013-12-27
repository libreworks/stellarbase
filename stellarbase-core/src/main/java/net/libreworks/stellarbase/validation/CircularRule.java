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
package net.libreworks.stellarbase.validation;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

/**
 * A rule that validates an object doesn't reference itself in a given field.
 * 
 * This rule is useful for objects with parentage, to ensure an object can't
 * list itself as a parent.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class CircularRule extends AbstractRule
{
	private static final String LABEL = "Circular";
	
	private String field;
	
	/**
	 * Creates a new CircularRule
	 * 
	 * @param field The field name
	 */
	public CircularRule(String field)
	{
		Assert.notNull(field);
		this.field = field;
	}

	@Override
	public String getConstraints()
	{
		return field;
	}

	@Override
	public String getLabel()
	{
		return LABEL;
	}

	public void validate(Object target, Errors errors)
	{
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(target);
		if ( ObjectUtils.equals(target, bw.getPropertyValue(field)) ) {
			errors.rejectValue(field, FIELD_INVALID);
		}
	}
}
