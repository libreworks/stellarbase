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
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.Fraction;
import org.springframework.util.NumberUtils;

import com.libreworks.stellarbase.util.Arguments;

/**
 * Utility class for safe math operations.
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class SafeMath
{
	protected static final Pattern ZERO = Pattern.compile("^(0)+(\\.0+)?$");
	protected static final Pattern HEX = Pattern.compile("^0[xX][0-9A-Fa-f]+$");
	protected static final Pattern OCT = Pattern.compile("^0[0-7]+$");
	
	/**
	 * Gets a constant for zero if one exists.
	 * 
	 * @param toClass The destination number class
	 * @return zero in that class (using a constant if available).
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Number> T getZero(Class<T> toClass)
	{
		if(BigDecimal.class.isAssignableFrom(toClass)){
			return (T) BigDecimal.ZERO;
		} else if(BigInteger.class.isAssignableFrom(toClass)){
			return (T) BigInteger.ZERO;
		} else if(Byte.class.equals(toClass)){
			return (T) org.apache.commons.lang3.math.NumberUtils.BYTE_ZERO;
		} else if(Double.class.equals(toClass)){
			return (T) org.apache.commons.lang3.math.NumberUtils.DOUBLE_ZERO;
		} else if(Float.class.equals(toClass)){
			return (T) org.apache.commons.lang3.math.NumberUtils.FLOAT_ZERO;
		} else if(Fraction.class.equals(toClass)){
			return (T) Fraction.ZERO;
		} else if(Integer.class.equals(toClass)){
			return (T) org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
		} else if(Long.class.equals(toClass)){
			return (T) org.apache.commons.lang3.math.NumberUtils.LONG_ZERO;
		} else if(Short.class.equals(toClass)){
			return (T) org.apache.commons.lang3.math.NumberUtils.SHORT_ZERO;
		} else {
			return NumberUtils.convertNumberToTargetClass(0, toClass);
		}
	}

	/**
	 * Determines whether the value supplied is zero-like.
	 * 
	 * <ul>
	 * <li>Null</li>
	 * <li>Empty and blank Strings</li>
	 * <li>Strings with just zeros (e.g. 000) or zeros with decimal zeros (e.g. 000.0000)</li>
	 * <li>Numbers that equal zero</li>
	 * <li>false</li>
	 * <li>Strings and other objects that don't evaluate as numbers</li>
	 * </ul>
	 * 
	 * @param value Any value
	 * @return Whether the object is zero-like
	 */
	public static boolean isZero(Object value) {
		if (value == null || Boolean.FALSE.equals(value)) {
			return true;
		}
		if (value instanceof CharSequence) {
			if (StringUtils.isBlank((CharSequence) value)) {
				return true;
			} else if (ZERO.matcher((CharSequence) value).matches()) {
				return true;
			} else if (!org.apache.commons.lang3.math.NumberUtils.isNumber(value.toString())) {
				return true;
			}
		}
		if (value instanceof Number) {
			if (value instanceof Fraction) {
				return ((Fraction)value).getNumerator() == 0;
			} else if (value instanceof BigDecimal) {
				return BigDecimal.ZERO.compareTo((BigDecimal)value) == 0;
			} else if (value instanceof BigInteger) {
				return BigInteger.ZERO.compareTo((BigInteger)value) == 0;
			} else {
				Number nval = (Number)value;
				return SafeMath.getZero(nval.getClass()).equals(nval);
			}
		} else {
			String svalue = value.toString();
			if (HEX.matcher(svalue).matches() || OCT.matcher(svalue).matches()) {
				return BigInteger.ZERO.compareTo(value(BigInteger.class, svalue)) == 0;
			} else {
				return BigDecimal.ZERO.compareTo(value(BigDecimal.class, svalue)) == 0;
			}
		}
	}	
	
	/**
	 * Tries to convert any object into a Number.
	 * 
	 * If the value is a Number, it will be converted or cast appropriately,
	 * otherwise it will be turned into a String and parsed. If the value cannot
	 * be turned into the number for whatever reason, zero will be returned.
	 * 
	 * Unlike the Spring {@link NumberUtils} class, this method does indeed
	 * support the Apache Commons {@link Fraction}.
	 * 
	 * @param <T> The number class
	 * @param value The value to convert into a number
	 * @param toClass The Number class to which the object will be converted
	 * @return The number
	 * @throws IllegalArgumentException if the value could not be converted
	 */
	public static <T extends Number> T value(Class<T> toClass, Object value)
	{
		Arguments.checkNull(toClass);
		if ( value == null || Boolean.FALSE.equals(value) ) {
			return SafeMath.getZero(toClass);
		} else if ( toClass.isInstance(value) ) {
			return toClass.cast(value);
		} else if ( Fraction.class.equals(toClass) ) {
		// Since the Spring NumberUtils class doesn't support Fraction
			if(value instanceof Number){
				return isZero(value) ? SafeMath.getZero(toClass) :
					(T) Fraction.getFraction(NumberUtils.convertNumberToTargetClass((Number)value, Double.class));
			} else {
				String toString = value.toString();
				return StringUtils.isBlank(toString) ? 
					SafeMath.getZero(toClass) : (T) Fraction.getFraction(toString);
			}
		} else if ( value instanceof Fraction ) {
			return NumberUtils.convertNumberToTargetClass(((Fraction)value).doubleValue(), toClass);
	    } else if ( value instanceof Number ) {
	    	return NumberUtils.convertNumberToTargetClass((Number)value, toClass);
	    } else {
    		try {
    			String svalue = value.toString();
    			char dot = ((DecimalFormat)DecimalFormat.getInstance()).getDecimalFormatSymbols().getDecimalSeparator();
    			if (svalue.indexOf(dot) > -1) {
       			// if the number is a decimal, integer decoding will barf
       			// decimals should be parsed into BigDecimal, then converted
    				return NumberUtils.convertNumberToTargetClass(NumberUtils.parseNumber(svalue, BigDecimal.class), toClass);
    			} else if (HEX.matcher(svalue).matches() || OCT.matcher(svalue).matches()) {
    			// if the number is hexadecimal, decimal decoding will barf
    			// hex numbers should be parsed into BigInteger, then converted
    				return NumberUtils.convertNumberToTargetClass(NumberUtils.parseNumber(svalue, BigInteger.class), toClass);
    			} else {
    				return NumberUtils.parseNumber(svalue, toClass);
    			}
			} catch ( Exception e ) {
				return NumberUtils.convertNumberToTargetClass(0, toClass);
			}
	    }
	}
	
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
		Arguments.checkNull(toClass);
		if (values == null || values.isEmpty()) {
			return getZero(toClass);
		} else if (BigInteger.class.isAssignableFrom(toClass)
				|| BigDecimal.class.isAssignableFrom(toClass)) {
			BigDecimal sum = BigDecimal.ZERO;
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
			return NumberUtils.convertNumberToTargetClass(sum, toClass);
		} else {
			double sum = 0.0;
			for (Number v : values) {
				if (v != null) {
					sum += v.doubleValue();
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
		Arguments.checkNull(toClass);
		if (values == null || values.isEmpty()) {
			return getZero(toClass);
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
						return getZero(toClass);
					}
					long multiplier = v.longValue();
					if(multiplier == 0) {
						return getZero(toClass);
					}
					product *= multiplier;
				}
				return NumberUtils.convertNumberToTargetClass(product, toClass);
			} else if (Float.class.equals(toClass) || Double.class.equals(toClass)) {
				double product = 1.0;
				for (Number v : values) {
					if (v == null) {
						return getZero(toClass);
					}
					double multiplier = v.doubleValue();
					if (multiplier == 0.0) {
						return getZero(toClass);
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
				return getZero(toClass);
			} else {
				BigDecimal multiplier = v instanceof BigDecimal ? (BigDecimal) v : value(BigDecimal.class, v); 
				if (BigDecimal.ZERO.compareTo(multiplier) == 0) {
					return getZero(toClass);
				}
				product = product.multiply(multiplier);
			}
		}
		return value(toClass, product);
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
		Arguments.checkNull(toClass);
		if (a == null && b == null) {
			return getZero(toClass);
		}
		if (Fraction.class.equals(toClass)) {
			Fraction sum = value(Fraction.class, a);
			return (T) (subtract ?
					sum.subtract(value(Fraction.class, b)) :
					sum.add(value(Fraction.class, b)));
		} else if (Byte.class.equals(toClass) || Integer.class.equals(toClass)
				|| Long.class.equals(toClass) || Short.class.equals(toClass)) {
			long a1 = value(Long.class, a);
			long b1 = value(Long.class, b);
			return NumberUtils.convertNumberToTargetClass(subtract ? a1 - b1 : a1 + b1, toClass);
		} else if(Float.class.equals(toClass) || Double.class.equals(toClass)) {
			double a1 = value(Double.class, a);
			double b1 = value(Double.class, b);
			return NumberUtils.convertNumberToTargetClass(subtract ? a1 - b1 : a1 + b1, toClass);
		} else {
			BigDecimal a1 = value(BigDecimal.class, a);
			BigDecimal b1 = value(BigDecimal.class, b);
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
		Arguments.checkNull(toClass);
		if(a == null || b == null) {
			return getZero(toClass);
		} else if (Fraction.class.equals(toClass)) {
			// Fraction has a short-circuit to send ZERO if either numerator is zero
			return (T) value(Fraction.class, a).multiplyBy(value(Fraction.class, b));
		} else if (Byte.class.equals(toClass) || Integer.class.equals(toClass)
				|| Long.class.equals(toClass) || Short.class.equals(toClass)) {
			long multiplicand = value(Long.class, a);
			long multiplier = value(Long.class, b);
			if (multiplicand == 0L || multiplier == 0L) {
				return getZero(toClass);
			} else {
				return NumberUtils.convertNumberToTargetClass(multiplicand * multiplier, toClass);
			}
		} else if (Float.class.equals(toClass) || Double.class.equals(toClass)) {
			double multiplicand = value(Double.class, a);
			double multiplier = value(Double.class, b);
			if (multiplicand == 0.0 || multiplier == 0.0) {
				return getZero(toClass);
			} else {
				return NumberUtils.convertNumberToTargetClass(multiplicand * multiplier, toClass);
			}
		} else {
			BigDecimal multiplicand = value(BigDecimal.class, a);
			BigDecimal multiplier = value(BigDecimal.class, b);
			if (BigDecimal.ZERO.compareTo(multiplicand) == 0 || BigDecimal.ZERO.compareTo(multiplier) == 0) {
				return getZero(toClass);
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
		Arguments.checkNull(toClass);
		if(isZero(a) || isZero(b)) {
			return getZero(toClass);
		} else if (Fraction.class.equals(toClass)) {
			if (isInteger(a) && isInteger(b)) {
				return (T) Fraction.getFraction(value(Integer.class, a), value(Integer.class, b));
			} else {
				Fraction dividend = value(Fraction.class, a);
				Fraction divisor = value(Fraction.class, b);
				if (dividend.getNumerator() == 0 || divisor.getNumerator() == 0) {
					return (T) Fraction.ZERO;
				} else {
					return (T) dividend.divideBy(divisor);
				}
			}
		} else if (Byte.class.equals(toClass) || Integer.class.equals(toClass)
				|| Long.class.equals(toClass) || Short.class.equals(toClass)) {
			long dividend = value(Long.class, a);
			long divisor = value(Long.class, b);
			if (dividend == 0L || divisor == 0L) {
				return getZero(toClass);
			} else {
				return NumberUtils.convertNumberToTargetClass(dividend / divisor, toClass);
			}
		} else if (Float.class.equals(toClass) || Double.class.equals(toClass)) {
			double dividend = value(Double.class, a);
			double divisor = value(Double.class, b);
			if (dividend == 0.0 || divisor == 0.0) {
				return getZero(toClass);
			} else {
				return NumberUtils.convertNumberToTargetClass(dividend / divisor, toClass);
			}
		} else {
			BigDecimal multiplicand = value(BigDecimal.class, a);
			BigDecimal multiplier = value(BigDecimal.class, b);
			if (BigDecimal.ZERO.compareTo(multiplicand) == 0 || BigDecimal.ZERO.compareTo(multiplier) == 0) {
				return getZero(toClass);
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
						value(BigDecimal.class, svalue) :
						value(BigInteger.class, svalue);
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
