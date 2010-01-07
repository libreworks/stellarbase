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

import org.apache.commons.lang.ObjectUtils;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;
import org.springframework.validation.Errors;

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
 * @version $Id$
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
		Assert.isTrue(min != null || max != null, "You must specify at least one range limit");
		this.min = min;
		this.max = max;
	}
	
	@Override
	public String getConstraintLabel()
	{
		StringBuilder sb = new StringBuilder()
			.append(ObjectUtils.toString(min));
		if (min != null && max != null) {
			sb.append('\u2013'); // unicode for the en-dash
		}
		return sb.append(ObjectUtils.toString(max)).toString();
	}

	@Override
	public String getLabel()
	{
		return LABEL;
	}

	@Override
    protected void validateField(Number value, Errors errors)
    {
		Double cvalue = NumberUtils.convertNumberToTargetClass(value, Double.class);
	    if ( value != null ) {
	    	if ( min != null && min.compareTo(cvalue) > 0 ) {
	    		errors.rejectValue(field, FIELD_INVALID);
	    	} else if ( max != null && max.compareTo(cvalue) < 0 ) {
	    		errors.rejectValue(field, FIELD_INVALID);
	    	}
	    }
    }
}
