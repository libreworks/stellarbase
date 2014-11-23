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

import java.sql.Timestamp;
import java.util.Date;
import org.apache.commons.lang3.ObjectUtils;
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
	 * @param <T> The number class
	 * @param value The value to convert into a number
	 * @param toClass The Number class to which the object will be converted
	 * @return The number
	 * @throws IllegalArgumentException if the value could not be converted
	 */
	public static <T extends Number> T value(Class<T> toClass, Object value)
	{
		Assert.notNull(toClass);
		if ( value == null ) {
			return NumberUtils.convertNumberToTargetClass(0, toClass);
		} else if ( toClass.isInstance(value) ) {
			return toClass.cast(value);
	    } else if ( value instanceof Number ) {
	    	return NumberUtils.convertNumberToTargetClass((Number)value, toClass);
	    } else {
	    	try {
				return NumberUtils.parseNumber(ObjectUtils.toString(value), toClass);
			} catch ( Exception e ) {
				return NumberUtils.convertNumberToTargetClass(0, toClass);
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
