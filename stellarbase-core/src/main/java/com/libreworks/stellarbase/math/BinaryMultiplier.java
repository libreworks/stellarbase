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
 * Stores a number and presents it abbreviated as a power of two (e.g 1024 = 1 Ki).
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class BinaryMultiplier extends AbstractMultiplier<BinaryMultiplier>
{
	private static final long serialVersionUID = 1L;
	
	protected static final String K = "Ki";
	protected static final String M = "Mi";
	protected static final String G = "Gi";
	protected static final String T = "Ti";
	protected static final String P = "Pi";
	protected static final String E = "Ei";

	protected static final BigDecimal TWO = new BigDecimal(2);
	protected static final BigDecimal KIBI = TWO.pow(10);
	protected static final BigDecimal MEBI = TWO.pow(20);
	protected static final BigDecimal GIBI = TWO.pow(30);
	protected static final BigDecimal TEBI = TWO.pow(40);
	protected static final BigDecimal PEBI = TWO.pow(50);
	protected static final BigDecimal EXBI = TWO.pow(60);
	
	private BinaryMultiplier(BigDecimal value, BigDecimal reduced, String multiplier, String unit, String prefix)
	{
		super(value, reduced, multiplier, unit, prefix);
	}
	
	/**
	 * Creates a new BinaryMultiplier
	 * 
	 * @param number The value, null will be treated as ZERO
	 */	
	public static BinaryMultiplier from(Number number)
	{
		return create(number, null, null);
	}
	
	static BinaryMultiplier create(Number number, String unit, String prefix)
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
		if (KIBI.compareTo(value) > 0) {
			reduced = value.multiply(adjust);
			multiplier = null;
		} else if (MEBI.compareTo(value) > 0) {
			reduced = value.divide(KIBI).multiply(adjust);
			multiplier = K;
		} else if (GIBI.compareTo(value) > 0) {
			reduced = value.divide(MEBI).multiply(adjust);
			multiplier = M;
		} else if (TEBI.compareTo(value) > 0) {
			reduced = value.divide(GIBI).multiply(adjust);
			multiplier = G;
		} else if (PEBI.compareTo(value) > 0) {
			reduced = value.divide(TEBI).multiply(adjust);
			multiplier = T;
		} else if (EXBI.compareTo(value) > 0) {
			reduced = value.divide(PEBI).multiply(adjust);
			multiplier = P;
		} else {
			reduced = value.divide(EXBI).multiply(adjust);
			multiplier = E;
		}
		return new BinaryMultiplier(value.multiply(adjust).stripTrailingZeros(), reduced, multiplier, unit, prefix);		
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		} else if (obj == null || !(obj instanceof BinaryMultiplier)) {
			return false;
		}
		BinaryMultiplier other = (BinaryMultiplier) obj;
		return new EqualsBuilder()
			.append(getValue(), other.getValue())
			.append(getUnit(), other.getUnit())
			.append(getPrefix(), other.getPrefix())
			.isEquals();
	}

	/**
	 * Returns a new BinaryMultiplier with the two values added together.
	 *  
	 * @param other The augend
	 * @return The sum of the two BinaryMultiplier values
	 */
	public BinaryMultiplier plus(BinaryMultiplier other)
	{
		return from(getValue().add(other.getValue()));
	}
	
	/**
	 * Returns a new BinaryMultiplier with {@code other} subtracted from this.
	 *  
	 * @param other The subtrahend 
	 * @return The difference of the two BinaryMultiplier values
	 */
	public BinaryMultiplier minus(BinaryMultiplier other)
	{
		return from(getValue().subtract(other.getValue()));
	}
	
	/**
	 * Returns a new BinaryMultiplier with the two values multiplied.
	 *  
	 * @param other The multiplicand
	 * @return The product of the two BinaryMultiplier values
	 */
	public BinaryMultiplier multiply(BinaryMultiplier other)
	{
		return from(getValue().multiply(other.getValue()));
	}
	
	/**
	 * Returns a new BinaryMultiplier with this divided by {@code other}.
	 * 
	 * To prevent an {@link ArithmeticException}, we enforce a precision of 10
	 * and {@link RonudingMode#HALF_EVEN}, then stripping any trailing zeros.
	 *  
	 * @param other The divisor
	 * @return The sum of the two BinaryMultiplier values
	 */	
	public BinaryMultiplier div(BinaryMultiplier other)
	{
		return from(getValue().divide(other.getValue(), 10, RoundingMode.HALF_EVEN).stripTrailingZeros());
	}
}
