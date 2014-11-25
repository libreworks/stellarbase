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
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collection;

import org.apache.commons.lang3.math.Fraction;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;

import com.libreworks.stellarbase.util.ValueUtils;

/**
 * Utility class for safe math operations.
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class SafeMath {
	/**
	 * Sums the values in a collection returning zero if empty or null.
	 * 
	 * If any entry is null or zero, it will be ignored.
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
							sum = sum.add(NumberUtils.convertNumberToTargetClass(v, BigDecimal.class));
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
	 * Multiplies the values in a collection returning zero if empty or null.
	 * 
	 * If any entry is null or zero, zero will be returned.
	 * 
	 * @param <T>
	 *            The type of number to return
	 * @param values
	 *            The values to multiply
	 * @param toClass
	 *            The class of Number to return
	 * @return The product of the values in the collection
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Number> T product(Collection<? extends Number> values, Class<T> toClass)
	{
		Assert.notNull(toClass);
		if (values == null || values.isEmpty()) {
			return ValueUtils.getZero(toClass);
		}
		boolean typeMatch = true;
		for (Number v : values){
			if(v != null && !toClass.isInstance(v)){
				typeMatch = false;
				break;
			}
		}
		if(typeMatch){
			if (Fraction.class.equals(toClass)) {
				Fraction product = Fraction.ONE;
				for (Number v : values) {
					Fraction multiplier = (Fraction) v;
					if (multiplier == null || multiplier.getNumerator() == 0){
						return (T) Fraction.ZERO;
					}
					product = product.multiplyBy(multiplier);
				}
				return (T) product;
			} else if (BigDecimal.class.isAssignableFrom(toClass)) {
				BigDecimal product = BigDecimal.ONE;
				for (Number v : values) {
					BigDecimal multiplier = (BigDecimal) v;
					if (multiplier == null || BigDecimal.ZERO.compareTo(multiplier) == 0) {
						return (T) BigDecimal.ZERO;
					}
					product = product.multiply(multiplier);
				}
				return (T) product;
			} else if (BigInteger.class.isAssignableFrom(toClass)) {
				BigInteger product = BigInteger.ONE;
				for (Number v : values) {
					BigInteger multiplier = (BigInteger) v;
					if (multiplier == null || BigInteger.ZERO.compareTo(multiplier) == 0) {
						return (T) BigInteger.ZERO;
					}
					product = product.multiply(multiplier);
				}
				return (T) product;
			} else if (Byte.class.equals(toClass) || Integer.class.equals(toClass)
					|| Long.class.equals(toClass) || Short.class.equals(toClass)) {
				long product = 1L;
				for (Number v : values) {
					if(v == null) {
						return ValueUtils.getZero(toClass);
					}
					long multiplier = v.longValue();
					if(multiplier == 0) {
						return ValueUtils.getZero(toClass);
					}
					product *= multiplier;
				}
				return NumberUtils.convertNumberToTargetClass(product, toClass);
			} else if (Float.class.equals(toClass) || Double.class.equals(toClass)) {
				double product = 1.0;
				for (Number v : values) {
					if (v == null) {
						return ValueUtils.getZero(toClass);
					}
					double multiplier = v.doubleValue();
					if (multiplier == 0.0) {
						return ValueUtils.getZero(toClass);
					}
					product *= multiplier;
				}
				return NumberUtils.convertNumberToTargetClass(product, toClass);
			}
		}
		// all other cases
		BigDecimal product = BigDecimal.ONE;
		for (Number v : values) {
			if(v == null){
				return ValueUtils.getZero(toClass);
			} else {
				BigDecimal multiplier = v instanceof BigDecimal ? (BigDecimal) v : ValueUtils.value(BigDecimal.class, v); 
				if (BigDecimal.ZERO.compareTo(multiplier) == 0) {
					return ValueUtils.getZero(toClass);
				}
				product = product.multiply(multiplier);
			}
		}
		return ValueUtils.value(toClass, product);
	}
	
	/**
	 * Adds two objects together as Numbers in a null-safe way.
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
		return doAdd(a, b, toClass, false);
	}

	/**
	 * Subtracts one object from another as Numbers in a null-safe way.
	 * 
	 * If one of the values is null, empty, or otherwise not a number, the
	 * number zero will be used instead.
	 * 
	 * @param <T>
	 *            The type of number expected
	 * @param a
	 *            The first number
	 * @param b
	 *            The number to subtract from {@code a}
	 * @param toClass
	 *            The type of the number to return
	 * @return The difference of {@code b} subtracted from {@code a}
	 */
	public static <T extends Number> T subtract(Object a, Object b, Class<T> toClass)
	{
		return doAdd(a, b, toClass, true);
	}	
	
	@SuppressWarnings("unchecked")
	protected static <T extends Number> T doAdd(Object a, Object b, Class<T> toClass, boolean subtract)
	{
		Assert.notNull(toClass);
		if (a == null && b == null) {
			return ValueUtils.getZero(toClass);
		}
		if (Fraction.class.equals(toClass)) {
			Fraction sum = ValueUtils.value(Fraction.class, a);
			return (T) (subtract ?
					sum.subtract(ValueUtils.value(Fraction.class, b)) :
					sum.add(ValueUtils.value(Fraction.class, b)));
		} else if (Byte.class.equals(toClass) || Integer.class.equals(toClass)
				|| Long.class.equals(toClass) || Short.class.equals(toClass)) {
			long a1 = ValueUtils.value(Long.class, a);
			long b1 = ValueUtils.value(Long.class, b);
			return NumberUtils.convertNumberToTargetClass(subtract ? a1 - b1 : a1 + b1, toClass);
		} else if(Float.class.equals(toClass) || Double.class.equals(toClass)) {
			double a1 = ValueUtils.value(Double.class, a);
			double b1 = ValueUtils.value(Double.class, b);
			return NumberUtils.convertNumberToTargetClass(subtract ? a1 - b1 : a1 + b1, toClass);
		} else {
			BigDecimal a1 = ValueUtils.value(BigDecimal.class, a);
			BigDecimal b1 = ValueUtils.value(BigDecimal.class, b);
			return NumberUtils.convertNumberToTargetClass(subtract ? a1.subtract(b1) : a1.add(b1), toClass);
		}
	}

	/**
	 * Multiplies two objects together as numbers in a null-safe way.
	 * 
	 * If one of the values is null, empty, or otherwise not a number, the
	 * method will return zero (you know, because anything times zero is zero).
	 * 
	 * @param <T>
	 *            The type of number expected
	 * @param a
	 *            The first number
	 * @param b
	 *            The second number
	 * @param toClass
	 *            The type of the number to return
	 * @return The product of the two numbers
	 */	
	@SuppressWarnings("unchecked")
	public static <T extends Number> T multiply(Object a, Object b, Class<T> toClass)
	{
		Assert.notNull(toClass);
		if(a == null || b == null) {
			return ValueUtils.getZero(toClass);
		} else if (Fraction.class.equals(toClass)) {
			// Fraction has a short-circuit to send ZERO if either numerator is zero
			return (T) ValueUtils.value(Fraction.class, a).multiplyBy(ValueUtils.value(Fraction.class, b));
		} else if (Byte.class.equals(toClass) || Integer.class.equals(toClass)
				|| Long.class.equals(toClass) || Short.class.equals(toClass)) {
			long multiplicand = ValueUtils.value(Long.class, a);
			long multiplier = ValueUtils.value(Long.class, b);
			if (multiplicand == 0L || multiplier == 0L) {
				return ValueUtils.getZero(toClass);
			} else {
				return NumberUtils.convertNumberToTargetClass(multiplicand * multiplier, toClass);
			}
		} else if (Float.class.equals(toClass) || Double.class.equals(toClass)) {
			double multiplicand = ValueUtils.value(Double.class, a);
			double multiplier = ValueUtils.value(Double.class, b);
			if (multiplicand == 0.0 || multiplier == 0.0) {
				return ValueUtils.getZero(toClass);
			} else {
				return NumberUtils.convertNumberToTargetClass(multiplicand * multiplier, toClass);
			}
		} else {
			BigDecimal multiplicand = ValueUtils.value(BigDecimal.class, a);
			BigDecimal multiplier = ValueUtils.value(BigDecimal.class, b);
			if (BigDecimal.ZERO.compareTo(multiplicand) == 0 || BigDecimal.ZERO.compareTo(multiplier) == 0) {
				return ValueUtils.getZero(toClass);
			} else {
				return NumberUtils.convertNumberToTargetClass(multiplicand.multiply(multiplier), toClass);
			}
		}
	}
	
	/**
	 * Divides one object from another as numbers in a null and zero-safe way.
	 * 
	 * If one of the values is null, empty, or otherwise not a number, the
	 * method will return zero.
	 * 
	 * @param <T>
	 *            The type of number expected
	 * @param a
	 *            The first number (numerator)
	 * @param b
	 *            The second number (denominator)
	 * @param toClass
	 *            The type of the number to return
	 * @return The quotient of {@code a} divided by {@code b}
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Number> T divide(Object a, Object b, Class<T> toClass)
	{
		Assert.notNull(toClass);
		if(ValueUtils.isZero(a) || ValueUtils.isZero(b)) {
			return ValueUtils.getZero(toClass);
		} else if (Fraction.class.equals(toClass)) {
			if (isInteger(a) && isInteger(b)) {
				return (T) Fraction.getFraction(ValueUtils.value(Integer.class, a), ValueUtils.value(Integer.class, b));
			} else {
				Fraction dividend = ValueUtils.value(Fraction.class, a);
				Fraction divisor = ValueUtils.value(Fraction.class, b);
				if (dividend.getNumerator() == 0 || divisor.getNumerator() == 0) {
					return (T) Fraction.ZERO;
				} else {
					return (T) dividend.divideBy(divisor);
				}
			}
		} else if (Byte.class.equals(toClass) || Integer.class.equals(toClass)
				|| Long.class.equals(toClass) || Short.class.equals(toClass)) {
			long dividend = ValueUtils.value(Long.class, a);
			long divisor = ValueUtils.value(Long.class, b);
			if (dividend == 0L || divisor == 0L) {
				return ValueUtils.getZero(toClass);
			} else {
				return NumberUtils.convertNumberToTargetClass(dividend / divisor, toClass);
			}
		} else if (Float.class.equals(toClass) || Double.class.equals(toClass)) {
			double dividend = ValueUtils.value(Double.class, a);
			double divisor = ValueUtils.value(Double.class, b);
			if (dividend == 0.0 || divisor == 0.0) {
				return ValueUtils.getZero(toClass);
			} else {
				return NumberUtils.convertNumberToTargetClass(dividend / divisor, toClass);
			}
		} else {
			BigDecimal multiplicand = ValueUtils.value(BigDecimal.class, a);
			BigDecimal multiplier = ValueUtils.value(BigDecimal.class, b);
			if (BigDecimal.ZERO.compareTo(multiplicand) == 0 || BigDecimal.ZERO.compareTo(multiplier) == 0) {
				return ValueUtils.getZero(toClass);
			} else {
				return NumberUtils.convertNumberToTargetClass(
						multiplicand.divide(multiplier, 10, RoundingMode.HALF_UP).stripTrailingZeros(), toClass);
			}
		}
	}
	
	protected static boolean isInteger(Object value) {
		if (value instanceof CharSequence) {
			String svalue = value.toString();
			if (org.apache.commons.lang3.math.NumberUtils.isNumber(svalue)) {
				char dot = ((DecimalFormat)DecimalFormat.getInstance()).getDecimalFormatSymbols().getDecimalSeparator();
				value = svalue.indexOf(dot) > -1 ?
						ValueUtils.value(BigDecimal.class, svalue) :
						ValueUtils.value(BigInteger.class, svalue);
			}
		}
		if (value instanceof Byte || value instanceof Short || value instanceof Integer) {
			return true;
		} else if (value instanceof Fraction) {
			return ((Fraction) value).reduce().getDenominator() == 1;
		} else if (value instanceof Long || value instanceof BigInteger) {
			return ((Number) value).longValue() < Integer.MAX_VALUE &&
					((Number) value).longValue() > Integer.MIN_VALUE;
		} else if (value instanceof Double || value instanceof Float) {
			double dvalue = ((Number) value).doubleValue();
			return dvalue == Math.floor(dvalue) && !Double.isInfinite(dvalue)
					&& dvalue < Integer.MAX_VALUE && dvalue > Integer.MIN_VALUE;
		} else if (value instanceof BigDecimal) {
			BigDecimal bd = (BigDecimal) value;
			return (bd.signum() == 0 || bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0) &&
					bd.longValue() < Integer.MAX_VALUE && bd.longValue() > Integer.MIN_VALUE;
		}
		return false;
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
	public static <T extends Number> T percentify(Object numerator, Object denominator, Class<T> toClass)
	{
		return multiply(divide(numerator, denominator, toClass), 100, toClass);
	}
}
