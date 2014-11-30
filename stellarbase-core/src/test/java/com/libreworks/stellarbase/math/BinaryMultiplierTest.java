package com.libreworks.stellarbase.math;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class BinaryMultiplierTest
{
	@Test
	public void testFrom()
	{
		BigDecimal b1 = new BigDecimal("-1234.56");
		assertEquals("-1.21 Ki", BinaryMultiplier.from(b1).toString());
		BigDecimal b2 = new BigDecimal("9876.54");
		assertEquals("9.65 Ki", BinaryMultiplier.from(b2).toString());
		BigDecimal b3 = new BigDecimal("123456789");
		assertEquals("117.74 Mi", BinaryMultiplier.from(b3).toString());
		BigDecimal b4 = new BigDecimal("456789123456");
		assertEquals("425.42 Gi", BinaryMultiplier.from(b4).toString());
		BigDecimal b5 = new BigDecimal("-147258369147258");
		assertEquals("-133.93 Ti", BinaryMultiplier.from(b5).toString());
		BigDecimal b6 = new BigDecimal("963852741963852741");
		assertEquals("856.07 Pi", BinaryMultiplier.from(b6).toString());
		BigDecimal b7 = new BigDecimal("258369147258369147258");
		assertEquals("224.1 Ei", BinaryMultiplier.from(b7).toString());
		BigDecimal b8 = null;
		assertEquals("0", BinaryMultiplier.from(b8).toString());
	}
	
	@Test
	public void testEquals()
	{
		BinaryMultiplier b1 = BinaryMultiplier.from(new BigDecimal("-1234.56"));
		BinaryMultiplier b2 = BinaryMultiplier.from(new BigDecimal("9876.54"));
		assertTrue(b1.equals(b1));
		assertFalse(b1.equals(null));
		assertFalse(b1.equals("foo"));
		assertFalse(b2.equals(b1));
		assertTrue(b2.equals(BinaryMultiplier.from(new BigDecimal("9876.54"))));
	}
	
	@Test
	public void testHashCode()
	{
		BinaryMultiplier b1 = BinaryMultiplier.from(new BigDecimal("-1234.56"));
		BinaryMultiplier b2 = BinaryMultiplier.from(new BigDecimal("9876.54"));
		assertEquals(b1.hashCode(), b1.hashCode());
		assertNotEquals(b1.hashCode(), b2.hashCode());
	}
	
	@Test
	public void testCompareTo()
	{
		BinaryMultiplier b1 = BinaryMultiplier.from(new BigDecimal("-1234.56"));
		BinaryMultiplier b2 = BinaryMultiplier.from(new BigDecimal("9876.54"));
		BinaryMultiplier b3 = BinaryMultiplier.from(new BigDecimal("123456789"));
		assertEquals(0, b1.compareTo(b1));
		assertTrue(b2.compareTo(b1) > 0);
		assertTrue(b1.compareTo(b2) < 0);
		assertTrue(b3.compareTo(b2) > 0);
		assertTrue(b3.compareTo(b1) > 0);
		assertTrue(b2.compareTo(b3) < 0);
	}
	
	@Test
	public void testArithmetic()
	{
		BinaryMultiplier b1 = BinaryMultiplier.from(1.0);
		BinaryMultiplier b2 = BinaryMultiplier.from(3.0);
		BinaryMultiplier b3 = BinaryMultiplier.from(10.0);
		BinaryMultiplier b4 = BinaryMultiplier.from(5.5);
		
		assertEquals(BinaryMultiplier.from(2), b2.minus(b1));
		assertEquals(BinaryMultiplier.from(4), b2.plus(b1));
		assertEquals(BinaryMultiplier.from(100), b3.multiply(b3));
		assertEquals(BinaryMultiplier.from(SafeMath.divide(1.0, 3.0, BigDecimal.class)), b1.div(b2));
		assertEquals(BinaryMultiplier.from(4.5), b3.minus(b4));
	}
}
