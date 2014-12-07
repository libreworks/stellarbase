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
import java.text.NumberFormat;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.libreworks.stellarbase.text.Characters;

/**
 * Abstract superclass for multiples of a base.
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public abstract class AbstractMultiplier<T extends Multiplier<T>> implements Multiplier<T>
{
	private static final long serialVersionUID = 1L;
	
	private final BigDecimal value;
	private final BigDecimal reduced;
	private final String multiplier;
	private final String unit;
	private final String prefix;
	
	/**
	 * Creates a new AbstractMultiplier.
	 *  
	 * @param value The underlying value
	 * @param reduced The reduced value
	 * @param multiplier The multiplier symbol
	 * @param unit The unit (may be null)
	 * @param prefix The prefix, for example a currency symbol (may be null)
	 */
	protected AbstractMultiplier(BigDecimal value, BigDecimal reduced, String multiplier, String unit, String prefix)
	{
		this.value = value;
		this.reduced = reduced;
		this.multiplier = multiplier;
		this.unit = unit;
		this.prefix = prefix;
	}

	@Override
	public int compareTo(T o)
	{
		if (o == null) {
			return 1;
		}
		return new CompareToBuilder()
			.append(value, o.getValue())
			.append(unit, o.getUnit())
			.append(prefix, o.getPrefix())
			.toComparison();
	}
	
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(1, 31)
			.append(value)
			.append(unit)
			.append(prefix)
			.toHashCode();
	}
	
	public BigDecimal getValue()
	{
		return value;
	}
	
	/**
	 * The value after being divided by the multiplier (e.g. 1.2 K for 1,234.56)
	 * 
	 * @return The reduced value
	 */
	public BigDecimal getReduced()
	{
		return reduced;
	}
	
	/**
	 * Gets the multiplier symbol (e.g. M for million, B for billion)
	 * 
	 * @return The multiplier symbol
	 */
	public String getMultiplier()
	{
		return unit == null ? multiplier : (multiplier == null ? unit : multiplier.concat(unit));
	}
	
	public String getUnit()
	{
		return unit;
	}

	public String getPrefix()
	{
		return prefix;
	}

	public String format(NumberFormat format)
	{
		StringBuilder sb = new StringBuilder();
		if (prefix != null) {
			sb.append(prefix);
		}
		sb.append(format.format(reduced.doubleValue()));
		String multi = getMultiplier();
		if (multi != null) {
			sb.append(Characters.SPACE).append(multi);
		}
		return sb.toString();
	}
	
	@Override
	public String toString()
	{
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(2);
		return format(format);
	}	
}
