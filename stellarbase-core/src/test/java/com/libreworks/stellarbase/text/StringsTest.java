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
package com.libreworks.stellarbase.text;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class StringsTest
{
	@Test
	public void testIsTruthy()
	{
		List<String> truthy = Arrays.asList("TRUE", "TruE", "true", "Y", "y", "1");
		for (String v : truthy) {
			assertTrue("String " + v + " is falsey and shouldn't be", Strings.isTruthy(v));
		}
		List<String> falsey = Arrays.asList("false", "WAT no way", "0", "", null, "N", "D");
		for (String v : falsey) {
			assertFalse("String " + v + " is truthy and shouldn't be", Strings.isTruthy(v));
		}
	}
	
	@Test
	public void testCut()
	{
		assertNull(Strings.cut(null, 123));
		assertEquals("123…", Strings.cut("123456", 3));
		assertEquals("123456", Strings.cut("123456", 10));
		assertEquals("123456", Strings.cut("123456            ", 10));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCutIllegal()
	{
		Strings.cut("aoeu", -1);
	}
	
	@Test
	public void testGetExtension()
	{
		assertEquals("gz", Strings.getExtension("thing.tar.gz"));
		assertEquals("txt", Strings.getExtension("what.txt"));
		assertNull(Strings.getExtension(null));
		assertEquals("everything", Strings.getExtension("everything"));
	}	
}
