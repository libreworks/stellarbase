package com.libreworks.stellarbase.math;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import org.junit.Test;

public class DecimalMultiplierTest
{
	@Test
	public void testFrom()
	{
		BigDecimal b1 = new BigDecimal("-1234.56");
		assertEquals("-1.23 K", DecimalMultiplier.from(b1).toString());
		BigDecimal b2 = new BigDecimal("9876.54");
		assertEquals("9.88 K", DecimalMultiplier.from(b2).toString());
		BigDecimal b3 = new BigDecimal("123456789");
		assertEquals("123.46 M", DecimalMultiplier.from(b3).toString());
		BigDecimal b4 = new BigDecimal("456789123456");
		assertEquals("456.79 B", DecimalMultiplier.from(b4).toString());
		BigDecimal b5 = new BigDecimal("-147258369147258");
		assertEquals("-147.26 T", DecimalMultiplier.from(b5).toString());
		BigDecimal b6 = null;
		assertEquals("0", DecimalMultiplier.from(b6).toString());
	}
	
	@Test
	public void testCurrency()
	{
		DecimalMultiplier b1 = PrettyNumbers.currency(1234.56, Locale.US, Currency.getInstance("USD"));
		assertEquals("$1.23 K", b1.toString());
		DecimalMultiplier b2 = PrettyNumbers.currency(1234.56);
		Locale l = Locale.getDefault();
		Currency c = Currency.getInstance(l);
		NumberFormat nf = NumberFormat.getInstance(l);
		nf.setMaximumFractionDigits(2);
		assertEquals(c.getSymbol(l) + nf.format(b2.getReduced()) + " " +
			b2.getMultiplier(), b2.toString());
	}
	
	@Test
	public void testEquals()
	{
		DecimalMultiplier b1 = DecimalMultiplier.from(new BigDecimal("-1234.56"));
		DecimalMultiplier b2 = DecimalMultiplier.from(new BigDecimal("9876.54"));
		assertTrue(b1.equals(b1));
		assertFalse(b1.equals(null));
		assertFalse(b1.equals("foo"));
		assertFalse(b2.equals(b1));
		assertTrue(b2.equals(DecimalMultiplier.from(new BigDecimal("9876.54"))));
	}
	
	@Test
	public void testHashCode()
	{
		DecimalMultiplier b1 = DecimalMultiplier.from(new BigDecimal("-1234.56"));
		DecimalMultiplier b2 = DecimalMultiplier.from(new BigDecimal("9876.54"));
		assertEquals(b1.hashCode(), b1.hashCode());
		assertNotEquals(b1.hashCode(), b2.hashCode());
	}
	
	@Test
	public void testCompareTo()
	{
		DecimalMultiplier b1 = DecimalMultiplier.from(new BigDecimal("-1234.56"));
		DecimalMultiplier b2 = DecimalMultiplier.from(new BigDecimal("9876.54"));
		DecimalMultiplier b3 = DecimalMultiplier.from(new BigDecimal("123456789"));
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
		DecimalMultiplier b1 = DecimalMultiplier.from(1.0);
		DecimalMultiplier b2 = DecimalMultiplier.from(3.0);
		DecimalMultiplier b3 = DecimalMultiplier.from(10.0);
		DecimalMultiplier b4 = DecimalMultiplier.from(5.5);
		
		assertEquals(DecimalMultiplier.from(2), b2.minus(b1));
		assertEquals(DecimalMultiplier.from(4), b2.plus(b1));
		assertEquals(DecimalMultiplier.from(100), b3.multiply(b3));
		assertEquals(DecimalMultiplier.from(SafeMath.divide(1.0, 3.0, BigDecimal.class)), b1.div(b2));
		assertEquals(DecimalMultiplier.from(4.5), b3.minus(b4));
	}
}
