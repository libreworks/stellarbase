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

import java.util.Arrays;
import java.util.Collection;
import org.springframework.validation.Errors;

/**
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class OneOfRule extends AbstractOneFieldRule<Object>
{
	private Collection<Object> values;
	
	private static final String LABEL = "OneOf";
	
	/**
	 * Creates a new one-of rule
	 * 
	 * @param field The field to validate
	 * @param values The acceptable values
	 */
	public OneOfRule(String field, Object... values)
    {
	    super(field);
	    this.values = Arrays.asList(values);
    }

	@Override
    public String getConstraintLabel()
    {
	    return values.toString();
    }

	@Override
    protected void validateField(Object value, Errors errors)
    {
		if ( !values.contains(value) ) {
			errors.rejectValue(field, FIELD_INVALID);
		}
    }

	@Override
    public String getLabel()
    {
	    return LABEL;
    }
}
