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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.math.Fraction;
import org.junit.Test;


public class SafeMathTest {
	@Test
	public void testSum()
	{
		assertEquals(Integer.valueOf(20), SafeMath.sum(Arrays.asList(3, 3, 4,
				3, 7), Integer.class));
		assertEquals(Double.valueOf(2.5), SafeMath.sum(Arrays.asList(0.5, 0.0,
				1.0, 1.0, 0.0), Double.class));
	}

	@Test
	public void testProduct()
	{
		assertEquals(Integer.valueOf(30), SafeMath.product(Arrays.asList(1, 3, 10), Integer.class));
		assertEquals(Integer.valueOf(30), SafeMath.product(Arrays.asList(1.0d, 3, 10), Integer.class));
		assertEquals(Integer.valueOf(0), SafeMath.product(Arrays.asList(1, null, 10), Integer.class));
		assertEquals(Integer.valueOf(0), SafeMath.product(Arrays.asList(1, 3, 0), Integer.class));
		
		assertEquals(Double.valueOf(30), SafeMath.product(Arrays.asList(1.0, 3.0, 10.0), Double.class));
		assertEquals(Double.valueOf(30), SafeMath.product(Arrays.asList(1.0, 3, 10), Double.class));
		assertEquals(Double.valueOf(0), SafeMath.product(Arrays.asList(1.0, null, 10), Double.class));
		assertEquals(Double.valueOf(0), SafeMath.product(Arrays.asList(1.0, 3.0, 0.0), Double.class));
		
		assertEquals(BigDecimal.valueOf(30), SafeMath.product(Arrays.asList(BigDecimal.ONE, BigDecimal.valueOf(3), BigDecimal.TEN), BigDecimal.class));
		assertEquals(BigDecimal.valueOf(30), SafeMath.product(Arrays.asList(BigDecimal.ONE, 3, 10), BigDecimal.class));
		assertEquals(BigDecimal.valueOf(0), SafeMath.product(Arrays.asList(BigDecimal.ONE, null, 10), BigDecimal.class));
		assertEquals(BigDecimal.valueOf(0), SafeMath.product(Arrays.asList(BigDecimal.ONE, BigDecimal.valueOf(3), BigDecimal.ZERO), BigDecimal.class));
		
		assertEquals(Fraction.ONE_QUARTER, SafeMath.product(Arrays.asList(Fraction.ONE_HALF, Fraction.ONE, Fraction.ONE_HALF), Fraction.class));
		assertEquals(Fraction.ZERO, SafeMath.product(Arrays.asList(Fraction.ONE_HALF, null, Fraction.ONE_HALF), Fraction.class));
		assertEquals(Fraction.ZERO, SafeMath.product(Arrays.asList(Fraction.ONE_HALF, Fraction.ONE, Fraction.ZERO), Fraction.class));
		assertEquals(Fraction.ONE_QUARTER, SafeMath.product(Arrays.asList(Fraction.ONE_HALF, Fraction.ONE, 0.5), Fraction.class));
	}
	
	@Test
	public void testAdd()
	{
		assertEquals(Integer.valueOf(3), SafeMath.add(3, null, Integer.class));
		assertEquals(Double.valueOf(10), SafeMath.add("4", BigDecimal.valueOf(6), Double.class));
		assertEquals(BigInteger.valueOf(20), SafeMath.add(12L, 8f, BigInteger.class));
	}
	
	@Test
	public void testDivide()
	{
		assertEquals(new BigDecimal("0.3333333333"), SafeMath.divide(1, 3, BigDecimal.class));
		assertEquals(Double.valueOf(1.0 / 3.0), SafeMath.divide(1, 3, Double.class));
		assertEquals(Fraction.getFraction(1, 3), SafeMath.divide(1, 3, Fraction.class));
		assertEquals(Fraction.getFraction(22, 7), SafeMath.divide(22.0, 7L, Fraction.class));
		assertEquals(Fraction.getFraction(22, 7), SafeMath.divide("22.0", 7L, Fraction.class));
	}	
	
	@Test
	public void testIsInteger()
	{
		List<?> wholes = Arrays.asList(1, 1.0, new BigDecimal("1.000"), "123456",
				"0x001", "0777",
				BigDecimal.ZERO, BigDecimal.TEN, 1.0f, (short)1, (byte)2, Fraction.getFraction(7, 1));
		for (Object v : wholes) {
			assertTrue("Object " + v + " should be a whole number, but it isn't", SafeMath.isInteger(v));
		}
		List<?> decimals = Arrays.asList(0.1, new BigDecimal("0.000000001"),
				Integer.valueOf(Integer.MAX_VALUE).toString() + "1",
				"123123123123123123123123",
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
}
