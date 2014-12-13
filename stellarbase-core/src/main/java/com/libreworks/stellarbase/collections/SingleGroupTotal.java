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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.libreworks.stellarbase.math.SafeMath;

/**
 * A Map that stores a running total for a group name.
 * 
 * When values are added to the SingleGroupTotal, they're stored in a map and
 * added with existing values.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class SingleGroupTotal extends AbstractMapDelegate<Comparable<?>,Double>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new SingleGroupTotal backed by a HashMap
	 */
	public SingleGroupTotal()
	{
		this(new HashMap<Comparable<?>,Double>());
	}

	/**
	 * Creates a new SingleGroupTotal with a custom delegate
	 * 
	 * @param delegate
	 *            The map to use to store the groups and values
	 */
	public SingleGroupTotal(Map<Comparable<?>,Double> delegate)
	{
		super(delegate);
	}

	/**
	 * Adds a value to a group
	 * 
	 * @param key
	 *            The group into which the value will be added
	 * @param value
	 *            The value to be added
	 */
	public void add(Comparable<?> key, Double value)
	{
		if (!containsKey(key)) {
			delegate.put(key, 0.0);
		}
		delegate.put(key, SafeMath.add(get(key), SafeMath.value(
				Double.class, value), Double.class));
	}

	/**
	 * Adds all the values to a group
	 * 
	 * @param key
	 *            The group into which the values will be added
	 * @param values
	 *            The values to be added
	 */
	public void addAll(Comparable<?> key, Collection<Double> values)
	{
		add(key, SafeMath.sum(values, Double.class));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Double put(Comparable<?> arg0, Double arg1)
	{
		throw new UnsupportedOperationException("Use the 'add' method");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map<? extends Comparable<?>,? extends Double> arg0)
	{
		throw new UnsupportedOperationException("Use the 'addAll' method");
	}
}
