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

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Represents a number as a multiple of some base (e.g. 1,000,000 = 1 M, 1,024 = 1 Ki).
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public interface Multiplier<T extends Multiplier<T>> extends Serializable, Comparable<T>
{
	/**
	 * Returns a new T with this divided by {@code other}.
	 *  
	 * @param other The divisor
	 * @return The sum of the two T values
	 */	
	public T div(T other);
	
	/**
	 * Formats the value using the formatter supplied, appending a space and the multiplier symbol.
	 * 
	 * @param format The format to use
	 * @return The formatted value
	 */
	public String format(NumberFormat format);
	
	/**
	 * Gets the multiplier symbol
	 * 
	 * @return The multiplier symbol
	 */
	public String getMultiplier();

	/**
	 * Gets the prefix, for instance, a currency symbol
	 * 
	 * @return The prefix
	 */
	public String getPrefix();
	
	/**
	 * The value after being divided by the multiplier
	 * 
	 * @return The reduced value
	 */
	public BigDecimal getReduced();
	
	/**
	 * Gets the unit suffix for the multiplier
	 * 
	 * @return The unit suffix
	 */
	public String getUnit();
	
	/**
	 * The raw value
	 * 
	 * @return The value
	 */
	public BigDecimal getValue();
	
	/**
	 * Returns a new T with {@code other} subtracted from this.
	 *  
	 * @param other The subtrahend 
	 * @return The difference of the two T values
	 */
	public T minus(T other);
	
	/**
	 * Returns a new T with the two values multiplied.
	 *  
	 * @param other The multiplicand
	 * @return The product of the two T values
	 */
	public T multiply(T other);

	/**
	 * Returns a new T with the two values added together.
	 *  
	 * @param other The augend
	 * @return The sum of the two T values
	 */
	public T plus(T other);
}
