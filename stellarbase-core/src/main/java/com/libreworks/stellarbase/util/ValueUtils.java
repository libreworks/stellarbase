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
package com.libreworks.stellarbase.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.Fraction;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;

/**
 * Utility class for value conversion and comparison.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class ValueUtils
{
	protected static final Pattern ZERO = Pattern.compile("^(0)+(\\.0+)?$");
	protected static final Pattern HEX = Pattern.compile("^0[xX][0-9A-Fa-f]+$");
	protected static final Pattern OCT = Pattern.compile("^0[0-7]+$");
	
	/**
	 * Method for comparing value equivalence between objects.
	 *  
	 * The following may give you a better idea of what this means:
	 * <pre>
	 * ValueUtils.equivalent("C", new Character('C')); // returns true
	 * ValueUtils.equivalent("", null); // returns false
	 * ValueUtils.equivalent(new Double(1.0), new Integer(1)); // returns true
	 * </pre>
	 *  
	 * @param a The first value
	 * @param b The second value
	 * @return Whether the values are considered equivalent
	 */
	public static boolean equivalent(Object a, Object b)
	{
		if ( a == b ) {
			return true;
		} else if ( a == null || b == null ) {
			return false;
		}
		if ( a.equals(b) ) {
			return true;
		} else if ( a.getClass().equals(b.getClass())) {
			// if they're not equal and they have the exact same class,
			// skip remaining checks.
			return false;
		} else if ( a instanceof Date || b instanceof Date ) {
			return equivalentDates(a, b);
		} else if ( a instanceof Number || b instanceof Number ) {
			return equivalentNumbers(a, b);
		}
		
		return ObjectUtils.toString(a).equals(ObjectUtils.toString(b));
	}
	
	/**
	 * Compares two objects as if they were numbers.
	 * 
	 * If either object is not a Number, it will be parsed if it is recognized
	 * as some kind of number format, otherwise, it will be defaulted to
	 * {@link java.lang.Double#NaN}.
	 * 
	 * @param a The first value
	 * @param b The second value
	 * @return Whether the numbers are considered equivalent
	 */
	public static boolean equivalentNumbers(Object a, Object b)
	{
		if ( a == null || b == null ) {
			return false;
		}
		return (a.equals(b) || toNumberOrNan(a, Double.class).equals(toNumberOrNan(b, Double.class)));
	}
	
	/**
	 * Compares two objects that should be cast to dates.
	 * 
	 * If either object is not a Date, it will act as null. This method's chief
	 * raison d'etre is that java.sql.Timestamp doesn't consider java.util.Date
	 * equal even if they are at the same instant in time.
	 * 
	 * @param a The first value
	 * @param b The second value
	 * @return Whether the dates are considered equivalent
	 */
	public static boolean equivalentDates(Object a, Object b)
	{
		if ( a == null || b == null ) {
			return false;
		}
		return ObjectUtils.equals(toDate(a), toDate(b));
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
		Assert.notNull(toClass);
		if ( value == null || Boolean.FALSE.equals(value) ) {
			return getZero(toClass);
		} else if ( toClass.isInstance(value) ) {
			return toClass.cast(value);
		} else if ( Fraction.class.equals(toClass) ) {
		// Since the Spring NumberUtils class doesn't support Fraction
			if(value instanceof Number){
				return isZero(value) ? getZero(toClass) :
					(T) Fraction.getFraction(NumberUtils.convertNumberToTargetClass((Number)value, Double.class));
			} else {
				String toString = value.toString();
				return StringUtils.isBlank(toString) ? 
					getZero(toClass) : (T) Fraction.getFraction(toString);
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
				return getZero(nval.getClass()).equals(nval);
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
	
	protected static Date toDate(Object value)
	{
		if ( value instanceof Timestamp ) {
			// java.sql.Timestamp doesn't like to play nice with equals,
			// but the getTime methods should return equal values
			return new Date(((Timestamp)value).getTime());
		} else if ( value instanceof Date ) {
			return (Date)value;
		}
		return null;
	}
	
	/**
	 * Turns an object into a Number, or {@link Double#NaN} if there's a problem
	 * 
	 * @param <T> The number class
	 * @param value The value to convert
	 * @param toClass The target class
	 * @return The number
	 */
	protected static <T extends Number> T toNumberOrNan(Object value, Class<T> toClass)
	{
		Number nvalue = value instanceof Number ? (Number)value : Double.NaN;
		if (!(value instanceof Number)){
			String svalue = ObjectUtils.toString(value);
			if(org.apache.commons.lang3.math.NumberUtils.isNumber(svalue)) {
				nvalue = NumberUtils.parseNumber(svalue, toClass);
			}
		}
		return NumberUtils.convertNumberToTargetClass(nvalue, toClass);
	}
}
