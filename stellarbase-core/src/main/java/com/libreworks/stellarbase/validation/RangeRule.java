/**
 * Copyright 2014 LibreWorks contributors
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

import com.libreworks.stellarbase.text.Characters;

/**
 * A rule for an inclusive range of numbers, a minimum, a maximum, or both.
 * 
 * Inclusive, of course, means that the minimum and maximum numbers themselves
 * are inside the range of allowable numbers.
 * 
 * With a min of 4 and a max of 10, the following numbers would be allowed:
 * 4, 4.5, 6, 8, 9.5, and 10.
 * 
 * @author Jonathan Hawk
 */
public class RangeRule extends AbstractOneFieldRule<Number>
{
	protected Double min;
	protected Double max;
	
	protected static final String LABEL = "Range";
	
	/**
	 * Creates a new RangeRule with just a minimum.
	 * 
	 * @param field The field to validate
	 * @param min The minimum number (inclusive, cannot be null)
	 */
	public RangeRule(String field, Double min)
	{
		this(field, min, null);
	}
	
	/**
	 * Creates a new RangeRule.
	 * 
	 * The {@code min} argument can be null if {@code max} is specified, and
	 * vice-versa, but both cannot be null simultaneously. 
	 * 
	 * @param field The field to validate
	 * @param min The minimum number (inclusive, can be null)
	 * @param max The maximum number (inclusive, can be null)
	 */
	public RangeRule(String field, Double min, Double max)
	{
		super(field);
		if (min == null || max == null) {
			throw new IllegalArgumentException("You must specify at least one range limit");
		}
		this.min = min;
		this.max = max;
	}
	
	@Override
	public String getConstraintLabel()
	{
		StringBuilder sb = new StringBuilder()
			.append(min == null ? "" : min);
		if (min != null && max != null) {
			sb.append(Characters.ENDASH);
		}
		return sb.append(max == null ? "" : max).toString();
	}

	@Override
	public String getLabel()
	{
		return LABEL;
	}

	@Override
    protected void validateField(Number value, Errors errors)
    {
	    if ( value != null ) {
			Double cvalue = new Double(value.doubleValue());
	    	if ( min != null && min.compareTo(cvalue) > 0 ) {
	    		errors.rejectValue(field, FIELD_INVALID);
	    	} else if ( max != null && max.compareTo(cvalue) < 0 ) {
	    		errors.rejectValue(field, FIELD_INVALID);
	    	}
	    }
    }
}
