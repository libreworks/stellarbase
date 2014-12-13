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

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Date;
import org.junit.Test;

/**
 * @author Jonathan Hawk
 * @version $Id$
 */
public class ValueUtilsTest
{
	/**
	 * Test method for {@link com.libreworks.stellarbase.util.ValueUtils#equivalent(java.lang.Object, java.lang.Object)}.
	 * @throws URISyntaxException 
	 */
	@Test
	public void testEquivalent() throws URISyntaxException
	{
		assertTrue(ValueUtils.equivalent(null, null));
		String test1 = "foobar";
		assertTrue(ValueUtils.equivalent(test1, test1));
		String foobar1 = "foobar1";
		String foobar1again = new String(foobar1);
		assertTrue(ValueUtils.equivalent(foobar1, foobar1again));
		assertFalse(ValueUtils.equivalent("foobar1", "foobar2"));
		assertFalse(ValueUtils.equivalent("foobar1", null));
		assertTrue(ValueUtils.equivalent(2, "2"));
		assertTrue(ValueUtils.equivalent("C", new Character('C')));
		assertTrue(ValueUtils.equivalent("file:///", new URI("file:///")));
		long now = System.currentTimeMillis();
		assertTrue(ValueUtils.equivalent(new Timestamp(now), new Date(now)));
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.util.ValueUtils#equivalentNumbers(java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testEquivalentNumbers()
	{
		assertTrue(ValueUtils.equivalentNumbers("987.654", "987.654"));
		assertTrue(ValueUtils.equivalentNumbers(987.654, new BigDecimal(987.654)));
		assertTrue(ValueUtils.equivalentNumbers("123456", 123456));
		assertTrue(ValueUtils.equivalentNumbers(new Short("123"), new Integer(123)));
		assertFalse(ValueUtils.equivalentNumbers(null, 1234));
		assertFalse(ValueUtils.equivalentNumbers(new BigDecimal(12345.678), new Float(12345.6789)));
		assertTrue(ValueUtils.equivalentNumbers("Adam", "Eve")); // turns into NaN
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.util.ValueUtils#equivalentDates(java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testEquivalentDates()
	{
		long now = System.currentTimeMillis();
		assertTrue(ValueUtils.equivalentDates(new Date(now), new Timestamp(now)));
		assertTrue(ValueUtils.equivalentDates(new Timestamp(now), new Date(now)));
		assertFalse(ValueUtils.equivalentDates(new Date(now), new Date(now-100)));
		assertFalse(ValueUtils.equivalentDates(new Date(now), null));
		assertTrue(ValueUtils.equivalentDates(new Date(now), new Date(now)));
		assertTrue(ValueUtils.equivalentDates(new Timestamp(now), new Timestamp(now)));
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.util.ValueUtils#toDate(java.lang.Object)}.
	 */
	@Test
	public void testToDate()
	{
		long now = System.currentTimeMillis();
		Timestamp nowt = new Timestamp(now);
		Date nowd = new Date(now);
		assertSame(nowd, ValueUtils.toDate(nowd));
		Date tots = ValueUtils.toDate(nowt);
		assertFalse((tots instanceof Timestamp));
		assertEquals(tots, nowd);
		assertNull(ValueUtils.toDate("not a date, clearly"));
	}
	
	/**
	 * Test method for {@link com.libreworks.stellarbase.util.ValueUtils#toNumberOrNan(java.lang.Object, java.lang.Class)}.
	 */
	@Test
	public void testToNumberOrNan()
	{
		BigInteger in1 = new BigInteger("12345");
		Long out1 = ValueUtils.toNumberOrNan(in1, Long.class);
		assertEquals(out1.longValue(), in1.longValue(), 0);
		
		BigDecimal in2 = new BigDecimal("987.654");
		Double out2 = ValueUtils.toNumberOrNan(in2, Double.class);
		assertEquals(out2.doubleValue(), in2.doubleValue(), 0);
	}
}
