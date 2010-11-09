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
package net.libreworks.stellarbase.collections;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class SingleGroupTotalTest
{
	private SingleGroupTotal object;
	
	@Before
	public void setUp() throws Exception
	{
		object = new SingleGroupTotal();
	}

	@Test
	public void testAdd()
	{
		object.add("foo", 3.0);
		object.add("bar", 4.0);
		object.add("foo", 5.0);
		assertEquals(8.0, object.get("foo").doubleValue(), 0);
		assertEquals(4.0, object.get("bar").doubleValue(), 0);
	}
	
	@Test
	public void testAddNumbers()
	{
	    object.add(1, 3.1);
	    object.add(2, 4.1);
	    object.add(2, 5.1);
	    assertEquals(3.1, object.get(1).doubleValue(), 0);
	    assertEquals(9.2, object.get(2).doubleValue(), 0);
	}

	@Test
	public void testAddAll()
	{
		object.addAll("foo", Arrays.asList(4.0, 3.0, 2.0));
		object.addAll("bar", Arrays.asList(1.0, 18.0, 0.5));
		assertEquals(9.0, object.get("foo").doubleValue(), 0);
		assertEquals(19.5, object.get("bar").doubleValue(), 0);
	}

	@Test
	public void testAddAllNumbers()
	{
	    object.addAll(4, Arrays.asList(4.0, 3.0, 2.0));
	    object.addAll(5, Arrays.asList(1.0, 18.0, 0.5));
	    assertEquals(9.0, object.get(4).doubleValue(), 0);
        assertEquals(19.5, object.get(5).doubleValue(), 0);
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testPut()
	{
		object.put("test", 0.0);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testPutAll()
	{
		object.putAll(new HashMap<String,Double>());
	}
}
