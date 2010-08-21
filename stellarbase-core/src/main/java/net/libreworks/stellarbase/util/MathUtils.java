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
package net.libreworks.stellarbase.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;

import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;

/**
 * Utility class for math operations.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class MathUtils
{
	/**
	 * Sums the values in a collection returning zero if empty or null.
	 * 
	 * @param <T>
	 *            The type of number to return
	 * @param values
	 *            The values to sum
	 * @param toClass
	 *            The class of Number to return
	 * @return The sum of the values in the collection
	 */
	public static <T extends Number> T sum(Collection<? extends Number> values, Class<T> toClass)
	{
		Assert.notNull(toClass);
		if (BigInteger.class.isAssignableFrom(toClass)
				|| BigDecimal.class.isAssignableFrom(toClass)) {
			BigDecimal sum = BigDecimal.ZERO;
			if (values != null && !values.isEmpty()) {
				for (Number v : values) {
					if (v != null) {
						if (v instanceof BigDecimal) {
							sum = sum.add((BigDecimal) v);
						} else if (v instanceof Short || v instanceof Long
								|| v instanceof Integer) {
							sum = sum.add(BigDecimal.valueOf(v.longValue()));
						} else {
							sum = sum.add(NumberUtils
									.convertNumberToTargetClass(v,
											BigDecimal.class));
						}
					}
				}
			}
			return NumberUtils.convertNumberToTargetClass(sum, toClass);
		} else {
			double sum = 0.0;
			if (values != null && !values.isEmpty()) {
				for (Number v : values) {
					if (v != null) {
						sum += v.doubleValue();
					}
				}
			}
			return NumberUtils.convertNumberToTargetClass(sum, toClass);
		}
	}

	/**
	 * Adds two objects together as Numbers.
	 * 
	 * If one of the values is null, empty, or otherwise not a number, the
	 * number zero will be used instead.
	 * 
	 * @param <T>
	 *            The type of number expected
	 * @param a
	 *            The first number
	 * @param b
	 *            The second number
	 * @param toClass
	 *            The type of the number to return
	 * @return The sum of the objects evaluated as numbers
	 */
	public static <T extends Number> T add(Object a, Object b, Class<T> toClass)
	{
		Assert.notNull(toClass);
		if (a == null && b == null) {
			return NumberUtils.convertNumberToTargetClass(0, toClass);
		}
		if (BigInteger.class.isAssignableFrom(toClass)
				|| BigDecimal.class.isAssignableFrom(toClass)) {
			BigDecimal sum = ValueUtils.value(BigDecimal.class, a);
			return NumberUtils.convertNumberToTargetClass(sum.add(ValueUtils
					.value(BigDecimal.class, b)), toClass);
		} else {
			return NumberUtils.convertNumberToTargetClass(ValueUtils.value(
					Double.class, a)
					+ ValueUtils.value(Double.class, b), toClass);
		}
	}

	/**
	 * Divides two numbers and multiplies by 100.
	 * 
	 * @param numerator
	 *            The numerator
	 * @param denominator
	 *            The denominator
	 * @return The result
	 */
	public static double percentify(Object numerator, Object denominator)
	{
		if (numerator == null || denominator == null
				|| ValueUtils.value(Double.class, denominator) == 0.0) {
			return 0;
		}
		return ValueUtils.value(Double.class, numerator).doubleValue()
				/ ValueUtils.value(Double.class, denominator).doubleValue()
				* 100;
	}
}
