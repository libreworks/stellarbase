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
import net.libreworks.stellarbase.util.ValueUtils;

/**
 * A Map that stores a running total for a group name.
 * 
 * When values are added to the SingleGroupTotal, they're stored in a map and
 * added with existing values.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class SingleGroupTotal extends AbstractMapDelegate<String,Double>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new SingleGroupTotal backed by a HashMap
	 */
	public SingleGroupTotal()
	{
		this(new HashMap<String,Double>());
	}

	/**
	 * Creates a new SingleGroupTotal with a custom delegate
	 * 
	 * @param delegate
	 *            The map to use to store the groups and values
	 */
	public SingleGroupTotal(Map<String,Double> delegate)
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
	public void add(String key, Double value)
	{
		if (!containsKey(key)) {
			delegate.put(key, 0.0);
		}
		delegate.put(key, MathUtils.add(get(key), ValueUtils.value(
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
	public void addAll(String key, Collection<Double> values)
	{
		add(key, MathUtils.sum(values, Double.class));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Double put(String arg0, Double arg1)
	{
		throw new UnsupportedOperationException("Use the 'add' method");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map<? extends String,? extends Double> arg0)
	{
		throw new UnsupportedOperationException("Use the 'addAll' method");
	}
}
