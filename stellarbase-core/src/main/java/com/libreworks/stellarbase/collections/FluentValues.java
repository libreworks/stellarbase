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

import java.util.HashMap;
import java.util.Map;

/**
 * A map that allows for chaining when adding values.
 * 
 * Java's default Map implementation lacks a way to chain method calls when
 * establishing a set of known values. We can shorten this:
 * <pre>
 * HashMap&lt;String,?&gt; map = new HashMap&lt;String,?&gt;();
 * map.put("key1", "value1");
 * map.put("key2", new Integer(7));
 * map.put("key3", Boolean.FALSE);
 * </pre>
 * To this:
 * <pre>
 * new FluentValues().set("key1", "value1")
 *     .set("key2", new Integer(7)).set("key3", Boolean.FALSE);
 * </pre>
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class FluentValues extends AbstractMapDelegate<String,Object>
{
    private static final long serialVersionUID = 1L;

	/**
	 * Creates a new FluentValues
	 */
	public FluentValues()
	{
		this(new HashMap<String,Object>());
	}
	
	/**
	 * Creates a new FluentValues with the supplied backing map
	 * 
	 * @param delegate The map to use
	 */
	public FluentValues(Map<String,Object> delegate)
    {
	    super(delegate);
    }

	/**
	 * Delegates to {@link #put(String, Object)} but returns this.
	 * 
	 * @param key The entry key
	 * @param value The entry value
	 * @return provides a fluent interface
	 */
	public FluentValues set(String key, Object value)
	{
		delegate.put(key, value);
		return this;
	}
	
	/**
	 * Delegates to {@link #putAll(Map)} but returns this;
	 * 
	 * @param m The map whose values will be copied
	 * @return provides a fluent interface
	 */
	public FluentValues setAll(Map<? extends String,? extends Object> m)
	{
		delegate.putAll(m);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(String key, Object value)
	{
		return delegate.put(key, value);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map<? extends String,? extends Object> m)
    {
		delegate.putAll(m);
    }
}
