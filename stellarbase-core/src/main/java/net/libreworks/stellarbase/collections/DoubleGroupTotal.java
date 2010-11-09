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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.libreworks.stellarbase.util.MathUtils;

/**
 * A Map that stores a running total for two groups.
 * 
 * When values are added to the DoubleGroupTotal, they're stored in a map and
 * added with existing values.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class DoubleGroupTotal extends AbstractMapDelegate<String,SingleGroupTotal>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a DoubleGroupTotal backed by a HashMap.
	 */
	public DoubleGroupTotal()
	{
		this(new HashMap<String,SingleGroupTotal>());
	}
	
	/**
	 * Creates a DoubleGroupTotal with a custom map delegate
	 * 
	 * @param delegate The map used to store groups and values
	 */
	public DoubleGroupTotal(Map<String,SingleGroupTotal> delegate)
	{
		super(delegate);
	}

	/**
	 * Adds a value into the groups
	 * 
	 * @param key1 The first group
	 * @param key2 The second group
	 * @param value The value to add
	 */
	public void add(String key1, Comparable<?> key2, Double value)
	{
		if ( !containsKey(key1) ) {
			delegate.put(key1, new SingleGroupTotal());
		}
		get(key1).add(key2, value);
	}
	
	/**
	 * Adds the values into the groups
	 * 
	 * @param key1 The first group
	 * @param key2 The second group
	 * @param values The values to add
	 */
	public void addAll(String key1, Comparable<?> key2, Collection<Double> values)
	{
		add(key1, key2, MathUtils.sum(values, Double.class));
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public SingleGroupTotal put(String key, SingleGroupTotal value)
	{
		throw new UnsupportedOperationException("Use the 'add' method");
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map<? extends String,? extends SingleGroupTotal> m)
	{
		throw new UnsupportedOperationException("Use the 'addAll' method");
	}
}
