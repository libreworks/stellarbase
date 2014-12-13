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
package com.libreworks.stellarbase.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.math.Fraction;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

import com.google.common.collect.ImmutableList;


public class SafeMathTest {
	@Test
	public void testSum()
	{
		assertEquals(Short.valueOf((short)0), SafeMath.sum(null, Short.class));
		assertEquals(Short.valueOf((short)0), SafeMath.sum(Collections.<Number>emptyList(), Short.class));
		
		assertEquals(Integer.valueOf(20), SafeMath.sum(Arrays.asList(3, 3, 4,
				3, 7), Integer.class));
		assertEquals(Double.valueOf(2.5), SafeMath.sum(Arrays.asList(0.5, 0.0,
				1.0, 1.0, 0.0), Double.class));
	}

	@Test
	public void testProduct()
	{
		assertEquals(Short.valueOf((short)0), SafeMath.product(null, Short.class));
		assertEquals(Short.valueOf((short)0), SafeMath.product(Collections.<Number>emptyList(), Short.class));
		
		assertEquals(Integer.valueOf(30), SafeMath.product(Arrays.asList(1, 3, 10), Integer.class));
		assertEquals(Integer.valueOf(30), SafeMath.product(Arrays.asList(1.0d, 3, 10), Integer.class));
		assertEquals(Integer.valueOf(0), SafeMath.product(Arrays.asList(1, null, 10), Integer.class));
		assertEquals(Integer.valueOf(0), SafeMath.product(Arrays.asList(1, 3, 0), Integer.class));
		
		assertEquals(Double.valueOf(30), SafeMath.product(Arrays.asList(1.0, 3.0, 10.0), Double.class));
		assertEquals(Double.valueOf(30), SafeMath.product(Arrays.asList(1.0, 3, 10), Double.class));
		assertEquals(Double.valueOf(0), SafeMath.product(Arrays.asList(1.0, null, 10), Double.class));
		assertEquals(Double.valueOf(0), SafeMath.product(Arrays.asList(1.0, 3.0, 0.0), Double.class));
		
		assertEquals(BigInteger.valueOf(30), SafeMath.product(Arrays.asList(BigDecimal.ONE, BigDecimal.valueOf(3), BigInteger.TEN), BigInteger.class));
		assertEquals(BigDecimal.valueOf(30), SafeMath.product(Arrays.asList(BigDecimal.ONE, BigDecimal.valueOf(3), BigDecimal.TEN), BigDecimal.class));
		assertEquals(BigDecimal.valueOf(30), SafeMath.product(Arrays.asList(BigDecimal.ONE, 3, 10), BigDecimal.class));
		assertEquals(BigDecimal.valueOf(0), SafeMath.product(Arrays.asList(BigDecimal.ONE, null, 10), BigDecimal.class));
		assertEquals(BigDecimal.valueOf(0), SafeMath.product(Arrays.asList(BigDecimal.ONE, 0.0, 10), BigDecimal.class));
		assertEquals(BigDecimal.valueOf(0), SafeMath.product(Arrays.asList(BigDecimal.ONE, BigDecimal.valueOf(3), BigDecimal.ZERO), BigDecimal.class));
		
		assertEquals(Fraction.ONE_QUARTER, SafeMath.product(Arrays.asList(Fraction.ONE_HALF, Fraction.ONE, Fraction.ONE_HALF), Fraction.class));
		assertEquals(Fraction.ZERO, SafeMath.product(Arrays.asList(Fraction.ONE_HALF, null, Fraction.ONE_HALF), Fraction.class));
		assertEquals(Fraction.ZERO, SafeMath.product(Arrays.asList(Fraction.ONE_HALF, Fraction.ONE, Fraction.ZERO), Fraction.class));
		assertEquals(Fraction.ONE_QUARTER, SafeMath.product(Arrays.asList(Fraction.ONE_HALF, Fraction.ONE, 0.5), Fraction.class));
	}
	
	@Test
	public void testAdd()
	{
		assertEquals(BigDecimal.ZERO, SafeMath.add(null, null, BigDecimal.class));
		assertEquals(Integer.valueOf(3), SafeMath.add(3, null, Integer.class));
		assertEquals(Byte.valueOf((byte)3), SafeMath.add(null, 3, Byte.class));
		assertEquals(Double.valueOf(10), SafeMath.add("4", BigDecimal.valueOf(6), Double.class));
		assertEquals(BigInteger.valueOf(20), SafeMath.add(12L, 8f, BigInteger.class));
		assertEquals(Long.valueOf(123456L), SafeMath.add("123450", "0x06", Long.class));
		assertEquals(Fraction.ONE, SafeMath.add(Fraction.THREE_QUARTERS, Fraction.ONE_QUARTER, Fraction.class));
		assertEquals(Fraction.ONE, SafeMath.add(0.75, 0.25, Fraction.class));
	}
	
	@Test
	public void testSubtract()
	{
		assertEquals(BigDecimal.ZERO, SafeMath.subtract(null, null, BigDecimal.class));
		assertEquals(Integer.valueOf(3), SafeMath.subtract(3, null, Integer.class));
		assertEquals(Double.valueOf(4.0), SafeMath.subtract("10", BigDecimal.valueOf(6), Double.class));
		assertEquals(BigInteger.valueOf(12L), SafeMath.subtract(20, 8.0f, BigInteger.class));
		assertEquals(Long.valueOf(123450L), SafeMath.subtract("123456", "0x06", Long.class));
		assertEquals(Fraction.ZERO, SafeMath.subtract(Fraction.ONE_QUARTER, Fraction.ONE_QUARTER, Fraction.class));
		assertEquals(Fraction.THREE_QUARTERS, SafeMath.subtract(Fraction.ONE, Fraction.ONE_QUARTER, Fraction.class));
		assertEquals(Fraction.ONE_QUARTER, SafeMath.subtract(1.00, 0.75, Fraction.class));
	}	
	
	@Test
	public void testMultiply()
	{
		assertEquals(BigDecimal.ZERO, SafeMath.multiply(1, null, BigDecimal.class));
		assertEquals(BigDecimal.ZERO, SafeMath.multiply(null, 1, BigDecimal.class));
		assertEquals(Fraction.ONE, SafeMath.multiply(Fraction.ONE_THIRD, Fraction.getFraction(3, 1), Fraction.class));
		assertEquals(Fraction.ZERO, SafeMath.multiply(Fraction.ONE_THIRD, Fraction.ZERO, Fraction.class));
		assertEquals(Integer.valueOf(100), SafeMath.multiply("20", "0x05", Integer.class));
		assertEquals(Long.valueOf(0), SafeMath.multiply(5.0, 0, Long.class));
		assertEquals(Short.valueOf((short)0), SafeMath.multiply(0, 100000, Short.class));
		assertEquals(BigInteger.valueOf(86400L), SafeMath.multiply(3600.0f, (short)24, BigInteger.class));
	}
	
	@Test
	public void testDivide()
	{
		assertEquals(BigDecimal.ZERO, SafeMath.divide(1, null, BigDecimal.class));
		assertEquals(BigDecimal.ZERO, SafeMath.divide(null, 1, BigDecimal.class));
		assertEquals(new BigDecimal("0.3333333333"), SafeMath.divide(1, 3, BigDecimal.class));
		assertEquals(Double.valueOf(1.0 / 3.0), SafeMath.divide(1, 3, Double.class));
		assertEquals(Fraction.ONE_HALF, SafeMath.divide(Fraction.ONE_QUARTER, Fraction.ONE_HALF, Fraction.class));
		assertEquals(Fraction.getFraction(1, 3), SafeMath.divide(1, 3, Fraction.class));
		assertEquals(Fraction.getFraction(22, 7), SafeMath.divide(22.0, 7L, Fraction.class));
		assertEquals(Fraction.getFraction(22, 7), SafeMath.divide("22.0", Fraction.getFraction(7, 1), Fraction.class));
		assertEquals(Integer.valueOf(0), SafeMath.divide(100, 0, Integer.class));
		assertEquals(Integer.valueOf(0), SafeMath.divide(0, 100, Integer.class));
		assertEquals(Float.valueOf(20f), SafeMath.divide(100, 5, Float.class));
		assertEquals(Double.valueOf(0.0), SafeMath.divide(0.0, 1.0, Double.class));
		assertEquals(Double.valueOf(0.0), SafeMath.divide(1.0, 0.0, Double.class));
		assertEquals(Double.valueOf(1.0), SafeMath.divide(6.25, 6.25, Double.class));
		assertEquals(Integer.valueOf(1), SafeMath.divide(6.25, 6.25, Integer.class));
		assertEquals(Double.valueOf(4.0 / 19.0), SafeMath.divide((short)4, "0x13", Double.class));
	}	
	
	@Test
	public void testIsInteger()
	{
		List<?> wholes = Arrays.asList(1, 1.0, new BigDecimal("1.000"), "123456",
				"0x001", "0777",
				BigDecimal.ZERO, BigDecimal.TEN, 1.0f, (short)1, (byte)2, Fraction.getFraction(7, 1), Fraction.getFraction(4, 2));
		for (Object v : wholes) {
			assertTrue("Object " + v + " should be a whole number, but it isn't", SafeMath.isInteger(v));
		}
		List<?> decimals = Arrays.asList(0.1, new BigDecimal("0.000000001"),
				Integer.valueOf(Integer.MAX_VALUE).toString() + "1",
				"123123123123123123123123", Fraction.getFraction(5, 3),
				"123.456", "aoeu", 0.00001, Long.MAX_VALUE, new BigDecimal(Long.MAX_VALUE), Fraction.ONE_FIFTH);
		for (Object v : decimals) {
			assertFalse("Object " + v + " shouldn't be a whole number, but it is", SafeMath.isInteger(v));
		}
	}
	
	@Test
	public void testPercentify()
	{
		assertEquals(50, SafeMath.percentify(5, 10, Double.class), 0);
		assertEquals(0, SafeMath.percentify(1, 0, Double.class), 0);
		assertEquals(0, SafeMath.percentify(0, 1, Double.class), 0);
		assertEquals(25, SafeMath.percentify("2", 8f, Double.class), 0);
	}
	

	/**
	 * Test method for {@link com.libreworks.stellarbase.util.ValueUtils#toNumber(Object, Class)}
	 */
	@Test
	public void testToNumber()
	{
		BigInteger in1 = new BigInteger("12345");
		assertEquals(in1, SafeMath.value(BigInteger.class, in1));
		Long out1 = SafeMath.value(Long.class, in1);
		assertEquals(in1.longValue(), out1.longValue());
		Long out2 = SafeMath.value(Long.class, "12345");
		assertEquals(in1.longValue(), out2.longValue());
		
		BigDecimal in2 = new BigDecimal("9876.54321");
		assertEquals(in2, SafeMath.value(BigDecimal.class, in2));
		assertEquals(Double.valueOf(9876.54321), SafeMath.value(Double.class, in2));
		assertEquals(in2, SafeMath.value(BigDecimal.class, "9876.54321"));
		
		Double in3 = Double.valueOf(0.4);
		assertEquals(in3, SafeMath.value(Double.class, in3));
		assertEquals(in3, SafeMath.value(Double.class, "0.4"));
		assertEquals(Float.valueOf(0.4f), SafeMath.value(Float.class, in3));
		assertEquals(Double.valueOf(24.0), SafeMath.value(Double.class, "24"));
		assertEquals(Double.valueOf(24.0), SafeMath.value(Double.class, "24.0"));
		assertEquals(Double.valueOf(24.0), SafeMath.value(Double.class, "0x18"));
		
		Integer in4 = Integer.valueOf(22);
		assertEquals(in4, SafeMath.value(Integer.class, in4));
		assertEquals(in4, SafeMath.value(Integer.class, "22"));
		assertEquals(in4, SafeMath.value(Integer.class, "22.0"));
		assertEquals(in4, SafeMath.value(Integer.class, in4));
		assertEquals(in4, SafeMath.value(Integer.class, "0x16"));
		
		// Check zero
		assertEquals(Fraction.ZERO, SafeMath.value(Fraction.class, 0.0));
		assertEquals(Fraction.ZERO, SafeMath.value(Fraction.class, ""));
		assertEquals(Fraction.ZERO, SafeMath.value(Fraction.class, "0.0"));
		assertEquals(Fraction.ZERO, SafeMath.value(Fraction.class, Fraction.ZERO));
		assertEquals(BigDecimal.ZERO, SafeMath.value(BigDecimal.class, ""));
		assertEquals(BigInteger.ZERO, SafeMath.value(BigInteger.class, 0L));
		assertEquals(NumberUtils.DOUBLE_ZERO, SafeMath.value(Double.class, null));
		assertEquals(NumberUtils.INTEGER_ZERO, SafeMath.value(Integer.class, 0.0));
		assertEquals(NumberUtils.SHORT_ZERO, SafeMath.value(Short.class, "   "));
		assertEquals(NumberUtils.BYTE_ZERO, SafeMath.value(Byte.class, BigDecimal.ZERO));		
		assertEquals(NumberUtils.LONG_ZERO, SafeMath.value(Long.class, 0));
		assertEquals(NumberUtils.FLOAT_ZERO, SafeMath.value(Float.class, "foo"));
	}
	
	@Test
	public void testIsZero()
	{
		List<?> zeroes = Arrays.asList(null, "", "  ", "foobar", "0", "0x0",
			"000", "0.0", "0000.000000", new ArrayList<Object>(), Boolean.FALSE,
			0.0, 0, BigDecimal.ZERO, new BigDecimal("000.000"), BigInteger.ZERO, Fraction.ZERO);
		for (Object v : zeroes) {
			assertTrue("Object " + v + " is not zero and should be", SafeMath.isZero(v));
		}
		List<?> nons = ImmutableList.of("123", 123, "0x01", "0777", 1234L,
				BigDecimal.ONE, Fraction.ONE_HALF, 123.5, 0.2f, BigInteger.ONE);
		for (Object v : nons) {
			assertFalse("Object " + v + " is zero and shouldn't be", SafeMath.isZero(v));
		}
	}	
}
