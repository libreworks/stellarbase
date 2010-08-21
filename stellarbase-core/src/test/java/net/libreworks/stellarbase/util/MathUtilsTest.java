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
package net.libreworks.stellarbase.util;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;

public class MathUtilsTest
{
	@Test
	public void testSum()
	{
		assertEquals(Integer.valueOf(20), MathUtils.sum(Arrays.asList(3, 3, 4,
				3, 7), Integer.class));
		assertEquals(Double.valueOf(2.5), MathUtils.sum(Arrays.asList(0.5, 0.0,
				1.0, 1.0, 0.0), Double.class));
	}

	@Test
	public void testAdd()
	{
		assertEquals(Integer.valueOf(3), MathUtils.add(3, null, Integer.class));
		assertEquals(Double.valueOf(10), MathUtils.add("4", BigDecimal.valueOf(6), Double.class));
		assertEquals(BigInteger.valueOf(20), MathUtils.add(12L, 8f, BigInteger.class));
	}

	@Test
	public void testPercentify()
	{
		assertEquals(50, MathUtils.percentify(5, 10), 0);
		assertEquals(0, MathUtils.percentify(1, 0), 0);
		assertEquals(0, MathUtils.percentify(0, 1), 0);
		assertEquals(25, MathUtils.percentify("2", 8f), 0);
	}
}
