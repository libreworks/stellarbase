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
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.UnsignedInteger;
import com.google.common.util.concurrent.AtomicDouble;


public class SafeMathTest
{
    @Test
    public void testIsNumber() {
    	// borrowed these test examples from Apache Commons Lang
        testIsNumber("12345", true);
        testIsNumber("1234.5", true);
        testIsNumber(".12345", true);
        testIsNumber("1234E5", true);
        testIsNumber("1234E+5", true);
        testIsNumber("1234E-5", true);
        testIsNumber("123.4E5", true);
        testIsNumber("-1234", true);
        testIsNumber("-1234.5", true);
        testIsNumber("-.12345", true);
        testIsNumber("-1234E5", true);
        testIsNumber("0", true);
        testIsNumber("-0", true);
        testIsNumber("01234", true);
        testIsNumber("-01234", true);
        testIsNumber("-0xABC123", true);
        testIsNumber("-0x0", true);
        testIsNumber(null, false);
        testIsNumber("", false);
        testIsNumber(" ", false);
        testIsNumber("\r\n\t", false);
        testIsNumber("--2.3", false);
        testIsNumber(".12.3", false);
        testIsNumber("-123E", false);
        testIsNumber("-123E+-212", false);
        testIsNumber("-123E2.12", false);
        testIsNumber("0xGF", false);
        testIsNumber("0xFAE-1", false);
        testIsNumber(".", false);
        testIsNumber("-0ABC123", false);
        testIsNumber("123.4E-D", false);
        testIsNumber("123.4ED", false);
        testIsNumber("1234E5l", false);
        testIsNumber("11a", false);
        testIsNumber("1a", false);
        testIsNumber("a", false);
        testIsNumber("11g", false);
        testIsNumber("11z", false);
        testIsNumber("11def", false);
        testIsNumber("11d11", false);
        testIsNumber("11 11", false);
        testIsNumber(" 1111", false);
        testIsNumber("1111 ", false);
        testIsNumber("2.", true);
        testIsNumber("00", true);
        testIsNumber("0.0", true);
        testIsNumber("0.4790", true);
        testIsNumber("0xABCD", true);
        testIsNumber("0XABCD", true);
    }

    private void testIsNumber(final String val, final boolean expected) {
        final boolean isValid = SafeMath.isNumber(val);
        if (isValid == expected) {
            return;
        }
        fail("Expecting "+ expected + " for isNumber using \"" + val + "\" but got " + isValid);
    }
	
	@Test
	public void testIsNumber2()
	{
		System.out.println(Double.valueOf("0x1.0p0"));
		List<String> good = Arrays.asList("1", "1.0", "01.0", "-1.0", "2E10", "0x1");
		for (String v : good) {
			assertTrue("Object " + v + " is not a number and should be", SafeMath.isNumber(v));
		}
		List<String> bad = ImmutableList.of("", "hello world");
		for (String v : bad) {
			assertFalse("Object " + v + " is a number and shouldn't be", SafeMath.isNumber(v));
		}
	}
	
	@Test
	public void testGetZero()
	{
		assertEquals(UnsignedInteger.ZERO, SafeMath.getZero(UnsignedInteger.class));
	}
	
	@Test
	public void testSum() throws IllegalArgumentException, IllegalAccessException
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
	}
	
	@Test
	public void testSubtract()
	{
		assertEquals(BigDecimal.ZERO, SafeMath.subtract(null, null, BigDecimal.class));
		assertEquals(Integer.valueOf(3), SafeMath.subtract(3, null, Integer.class));
		assertEquals(Double.valueOf(4.0), SafeMath.subtract("10", BigDecimal.valueOf(6), Double.class));
		assertEquals(BigInteger.valueOf(12L), SafeMath.subtract(20, 8.0f, BigInteger.class));
		assertEquals(Long.valueOf(123450L), SafeMath.subtract("123456", "0x06", Long.class));
	}	
	
	@Test
	public void testMultiply()
	{
		assertEquals(BigDecimal.ZERO, SafeMath.multiply(1, null, BigDecimal.class));
		assertEquals(BigDecimal.ZERO, SafeMath.multiply(null, 1, BigDecimal.class));
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
		List<?> wholes = Arrays.asList(
			1.0f, 2.0d, new BigDecimal("123.0000"), 456L, "123.00",
			1, 1.0, new BigDecimal("1.000"), "123456",
				"0x001", "0777",
				BigDecimal.ZERO, BigDecimal.TEN, 1.0f, (short)1, (byte)2,
				new AtomicInteger(7));
		for (Object v : wholes) {
			assertTrue("Object " + v + " should be a whole number, but it isn't", SafeMath.isInteger(v));
		}
		List<?> decimals = Arrays.asList(
			1.2f, 2.123d, new BigDecimal("123.456"), "1231123123123123123123123123", "hello",			
				0.1, new BigDecimal("0.000000001"),
				Integer.valueOf(Integer.MAX_VALUE).toString() + "1",
				"123123123123123123123123", new AtomicDouble(123.45),
				"123.456", "aoeu", 0.00001, Long.MAX_VALUE, new BigDecimal(Long.MAX_VALUE));
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
		assertEquals(BigDecimal.ZERO, SafeMath.value(BigDecimal.class, ""));
		assertEquals(BigInteger.ZERO, SafeMath.value(BigInteger.class, 0L));
		assertEquals(Double.valueOf(0.0), SafeMath.value(Double.class, null));
		assertEquals(Integer.valueOf(0), SafeMath.value(Integer.class, 0.0));
		assertEquals(Short.valueOf((short) 0), SafeMath.value(Short.class, "   "));
		assertEquals(Byte.valueOf((byte) 0), SafeMath.value(Byte.class, BigDecimal.ZERO));
		assertEquals(Long.valueOf(0L), SafeMath.value(Long.class, 0));
		assertEquals(Float.valueOf(0f), SafeMath.value(Float.class, "foo"));
	}
	
	@Test
	public void testIsZero()
	{
		List<?> zeroes = Arrays.asList(null, "", "  ", "foobar", "0", "0x0",
			"000", "0.0", "0000.000000", new ArrayList<Object>(), Boolean.FALSE,
			0.0, 0, BigDecimal.ZERO, new BigDecimal("000.000"), BigInteger.ZERO,
			new AtomicInteger(0));
		for (Object v : zeroes) {
			assertTrue("Object " + v + " is not zero and should be", SafeMath.isZero(v));
		}
		List<?> nons = ImmutableList.of("123", 123, "0x01", "0777", 1234L,
				BigDecimal.ONE, new AtomicDouble(123.45), 123.5, 0.2f, BigInteger.ONE);
		for (Object v : nons) {
			assertFalse("Object " + v + " is zero and shouldn't be", SafeMath.isZero(v));
		}
	}	
}
