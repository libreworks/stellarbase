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
package com.libreworks.stellarbase.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.util.NumberUtils;

/**
 * Stores a number and presents it as a reduced multiplier (e.g 1,234,567 as 1.2 M).
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class DecimalMultiplier extends AbstractMultiplier<DecimalMultiplier>
{
	private static final long serialVersionUID = 1L;
	
	protected static final String K = "K";
	protected static final String M = "M";
	protected static final String B = "B";	
	protected static final String T = "T";
	
	protected static final BigDecimal THOUSAND = BigDecimal.TEN.pow(3);
	protected static final BigDecimal MILLION = BigDecimal.TEN.pow(6);
	protected static final BigDecimal BILLION = BigDecimal.TEN.pow(9);
	protected static final BigDecimal TRILLION = BigDecimal.TEN.pow(12);
	
	private DecimalMultiplier(BigDecimal value, BigDecimal reduced, String multiplier, String unit, String prefix)
	{
		super(value, reduced, multiplier, unit, prefix);
	}

	/**
	 * Creates a new DecimalMultiplier
	 * 
	 * @param number The value, null will be treated as ZERO
	 * @return The multiplier
	 */	
	public static DecimalMultiplier from(Number number)
	{
		return create(number, null, null);
	}
	
	static DecimalMultiplier create(Number number, String unit, String prefix)
	{
		BigDecimal value = null;
		if (number == null) {
			value = BigDecimal.ZERO;
		} else if (number instanceof BigDecimal) {
			value = (BigDecimal) number;
		} else {
			value = NumberUtils.convertNumberToTargetClass(number, BigDecimal.class);
		}
		BigDecimal adjust = BigDecimal.ONE;
		if (BigDecimal.ZERO.compareTo(value) > 0) {
			value = value.abs();
			adjust = BigDecimal.ZERO.subtract(BigDecimal.ONE);
		}
		BigDecimal reduced = BigDecimal.ZERO;
		String multiplier = null;
		if (THOUSAND.compareTo(value) > 0) {
			reduced = value.multiply(adjust);
			multiplier = null;
		} else if (MILLION.compareTo(value) > 0) {
			reduced = value.divide(THOUSAND).multiply(adjust);
			multiplier = K;
		} else if (BILLION.compareTo(value) > 0) {
			reduced = value.divide(MILLION).multiply(adjust);
			multiplier = M;
		} else if (TRILLION.compareTo(value) > 0) {
			reduced = value.divide(BILLION).multiply(adjust);
			multiplier = B;
		} else {
			reduced = value.divide(TRILLION).multiply(adjust);
			multiplier = T;
		}
		return new DecimalMultiplier(value.multiply(adjust).stripTrailingZeros(), reduced, multiplier, unit, prefix);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		} else if (obj == null || !(obj instanceof DecimalMultiplier)) {
			return false;
		}
		DecimalMultiplier other = (DecimalMultiplier) obj;
		return new EqualsBuilder()
			.append(getValue(), other.getValue())
			.append(getUnit(), other.getUnit())
			.append(getPrefix(), other.getPrefix())
			.isEquals();
	}
		
	/**
	 * Returns a new DecimalMultiplier with the two values added together.
	 *  
	 * @param other The augend
	 * @return The sum of the two DecimalMultiplier values
	 */
	public DecimalMultiplier plus(DecimalMultiplier other)
	{
		return DecimalMultiplier.from(getValue().add(other.getValue()));
	}
	
	/**
	 * Returns a new DecimalMultiplier with {@code other} subtracted from this.
	 *  
	 * @param other The subtrahend 
	 * @return The difference of the two DecimalMultiplier values
	 */
	public DecimalMultiplier minus(DecimalMultiplier other)
	{
		return DecimalMultiplier.from(getValue().subtract(other.getValue()));
	}
	
	/**
	 * Returns a new DecimalMultiplier with the two values multiplied.
	 *  
	 * @param other The multiplicand
	 * @return The product of the two DecimalMultiplier values
	 */
	public DecimalMultiplier multiply(DecimalMultiplier other)
	{
		return DecimalMultiplier.from(getValue().multiply(other.getValue()));
	}
	
	/**
	 * Returns a new DecimalMultiplier with this divided by {@code other}.
	 * 
	 * To prevent an {@link ArithmeticException}, we enforce a precision of 10
	 * and {@link RonudingMode#HALF_EVEN}, then stripping any trailing zeros.
	 *  
	 * @param other The divisor
	 * @return The sum of the two DecimalMultiplier values
	 */	
	public DecimalMultiplier div(DecimalMultiplier other)
	{
		return DecimalMultiplier.from(getValue().divide(other.getValue(), 10, RoundingMode.HALF_EVEN).stripTrailingZeros());
	}
}
