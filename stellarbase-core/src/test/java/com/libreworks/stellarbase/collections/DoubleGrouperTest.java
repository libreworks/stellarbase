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
package com.libreworks.stellarbase.collections;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jonathan Hawk
 * @version $Id$
 */
public class DoubleGrouperTest
{
	private DoubleGrouper<String,Integer,Double> object; 
	
	/**
	 * Sets up the test
	 */
	@Before
	public void setUp()
	{
		object = new DoubleGrouper<String,Integer,Double>();
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.collections.DoubleGrouper#add(java.lang.Object, java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testAdd()
	{
		for(int i=0; i<10; i++){
			object.add("foo", new Integer((int)Math.ceil(i/2)), (double)i/10);
		}
		assertTrue(object.containsKey("foo"));
		Map<Integer,Collection<Double>> foo = object.get("foo");
		assertFalse(foo.isEmpty());
		assertEquals(5, foo.size());
		assertTrue(foo.containsKey(new Integer(1)));
		assertEquals(Arrays.asList(new Double(0.2), new Double(0.3)), foo.get(1));
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.collections.DoubleGrouper#addAll(java.lang.Object, java.lang.Object, java.util.Collection)}.
	 */
	@Test
	public void testAddAllABCollectionOfV()
	{
		Collection<Double> data = Arrays.asList(new Double(0.2),
		    new Double(0.3), new Double(0.4));
		object.addAll("2-4", 0, data);
		object.addAll("2-4", 1, Arrays.asList((Double)null));
		assertTrue(object.containsKey("2-4"));
		assertTrue(object.get("2-4").containsKey(0));
		assertEquals(data, object.get("2-4").get(0));
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.collections.DoubleGrouper#addAll(java.lang.Object, java.util.Map)}.
	 */
	@Test
	public void testAddAllAMapOfBCollectionOfV()
	{
		HashMap<Integer,Collection<Double>> map = new HashMap<Integer,Collection<Double>>();
		Collection<Double> data = Arrays.asList(new Double(0.2),
		    new Double(0.4), new Double(0.6));
		map.put(10, data);
		map.put(12, Arrays.asList((Double)null));
		object.addAll("bar", map);
		assertTrue(object.containsKey("bar"));
		assertTrue(object.get("bar").containsKey(10));
		assertTrue(object.get("bar").get(10).containsAll(data));
	}

	@Test
	public void testAddAllGenerics()
	{
	    DoubleGrouper<String,String,Number> object = new DoubleGrouper<String,String,Number>();
	    List<Double> nums = Arrays.asList(1.0, 2.0, 3.0);
	    HashMap<String,Collection<Number>> nums2 = new HashMap<String,Collection<Number>>();
	    nums2.put("b", new ArrayList<Number>(nums));
	    object.addAll("a", "b", nums);
	    object.addAll("a", nums2);
	}
	
	/**
	 * Test method for {@link com.libreworks.stellarbase.collections.DoubleGrouper#put(java.lang.Object, java.util.Map)}.
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void testPut()
	{
		object.put("thing", null);
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.collections.DoubleGrouper#putAll(java.util.Map)}.
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void testPutAll()
	{
		object.putAll(null);
	}
}
