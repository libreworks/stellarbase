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

import java.util.regex.Pattern;
import org.springframework.validation.Errors;

/**
 * Rule for matching a regular expression against a field value.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class RegexRule extends AbstractOneFieldRule<String>
{
	protected Pattern pattern;
	
	protected static final String LABEL = "Regex";
	
	/**
	 * @param field The field to validate
	 * @param pattern The pattern to match (will be compiled using default settings)
	 */
	public RegexRule(String field, String pattern)
	{
		this(field, Pattern.compile(pattern));
	}
	
	/**
	 * @param field The field to validate
	 * @param pattern The pattern to match
	 */
	public RegexRule(String field, Pattern pattern)
	{
		super(field);
		this.pattern = pattern;
	}

	@Override
	protected void validateField(String value, Errors errors)
	{
		if ( value != null && !pattern.matcher(value).matches() ) {
			errors.rejectValue(field, FIELD_INVALID);
		}
	}

	@Override
    public String getConstraintLabel()
    {
	    return pattern.toString();
    }

	public String getLabel()
    {
	    return LABEL;
    }
}
