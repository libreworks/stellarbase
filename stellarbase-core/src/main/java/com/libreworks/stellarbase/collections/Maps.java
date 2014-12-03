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
package com.libreworks.stellarbase.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.util.Assert;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;

/**
 * Provides builders that create {@link Map}s using a fluent interface.
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class Maps
{
	/**
	 * Creates a builder to produce an {@link EnumMap}.
	 * 
	 * <p>
	 * <b>Note:</b> if mutability is not required, and the Map will not contain
	 * null values or null keys, use {@link ImmutableMap#of()} instead.
	 *
	 * @param keyType The Enum to be used as Map keys
	 * @return An EnumMap builder
	 */	
	public static <K extends Enum<K>, V> EnumBuilder<K, V> newEnum(Class<K> keyType)
	{
		return new EnumBuilder<K, V>(new EnumMap<K,V>(keyType));
	}
	
	/**
	 * Creates a builder to produce an {@link EnumMap}.
	 * 
	 * <p>
	 * <b>Note:</b> if mutability is not required, and the Map will not contain
	 * null values or null keys, use {@link ImmutableMap#of()} instead.
	 *
	 * @param keyType The Enum to be used as Map keys
	 * @return An EnumMap builder
	 */
	public static <K extends Enum<K>, V> EnumBuilder<K, V> newEnum(K key, V value)
	{
		return new EnumBuilder<K, V>(new EnumMap<K,V>(key.getDeclaringClass())).add(key, value);
	}

	/**
	 * Creates a builder to produce an {@link EnumMap}.
	 * 
	 * <p>
	 * <b>Note:</b> if mutability is not required, and the Map will not contain
	 * null values or null keys, use {@link ImmutableMap#copyOf(Map)} instead.
	 *
	 * @param map the map whose mappings are to be placed in the new map
	 * @return An EnumMap builder
	 */
	public static <K extends Enum<K>, V> EnumBuilder<K, V> newEnum(Map<K, ? extends V> map)
	{
		return new EnumBuilder<K, V>(new EnumMap<K, V>(map));
	}	
	
	/**
	 * Creates a builder to produce a {@link LinkedHashMap}.
	 * 
	 * <p>
	 * <b>Note:</b> if mutability is not required, and the Map will not contain
	 * null values or null keys, use {@link ImmutableMap#of()} instead.
	 *
	 * @return A Map builder
	 */
	public static <K, V> MapBuilder<K, V> newLinked()
	{
		return new MapBuilder<K, V>(new LinkedHashMap<K, V>());
	}
	
	/**
	 * Creates a builder to produce a {@link LinkedHashMap}.
	 * 
	 * <p>
	 * <b>Note:</b> if mutability is not required, and the Map will not contain
	 * null values or null keys, use {@link ImmutableMap#of(Object, Object))} instead.
	 *
	 * @param The first key
	 * @param The first value
	 * @return A Map builder
	 */	
	public static <K, V> MapBuilder<K, V> newLinked(K key, V value)
	{
		return new MapBuilder<K, V>(new LinkedHashMap<K, V>()).add(key, value);
	}
	
	/**
	 * Creates a builder to produce a {@link LinkedHashMap}.
	 * 
	 * <p>
	 * <b>Note:</b> if mutability is not required, and the Map will not contain
	 * null values or null keys, use {@link ImmutableMap#copyOf(Map)} instead.
	 *
	 * @param map the map whose mappings are to be placed in the new map
	 * @return A Map builder
	 */
	public static <K, V> MapBuilder<K, V> newLinked(Map<K, ? extends V> map)
	{
		return new MapBuilder<K, V>(new LinkedHashMap<K, V>(map));
	}
	
	/**
	 * Creates a builder to produce a {@link SortedMap}.
	 * 
	 * <p>
	 * <b>Note:</b> if mutability is not required, and the Map will not contain
	 * null values or null keys, use {@link ImmutableSortedMap#of()} instead.
	 *
	 * @return A SortedMap builder
	 */
	public static <K extends Comparable, V> SortedBuilder<K, V> newSorted()
	{
		return new SortedBuilder<K,V>(new TreeMap<K,V>());
	}

	/**
	 * Creates a builder to produce a {@link LinkedHashMap}.
	 * 
	 * <p>
	 * <b>Note:</b> if mutability is not required, and the Map will not contain
	 * null values or null keys, use {@link ImmutableMap#of(Object, Object))} instead.
	 *
	 * @param The first key
	 * @param The first value
	 * @return A Map builder
	 */
	public static <K extends Comparable, V> SortedBuilder<K, V> newSorted(K key, V value)
	{
		return new SortedBuilder<K, V>(new TreeMap<K,V>()).add(key, value);
	}
	
	/**
	 * Creates builder to produce a {@link SortedMap} 
	 *
	 * <p>
	 * <b>Note:</b> if mutability is not required, and the Map will not contain
	 * null values or null keys, use
	 * {@link ImmutableSortedMap#copyOfSorted(SortedMap)} instead.
	 *
	 * @param map the sorted map whose mappings are to be placed in the new map and whose comparator is to be used to sort the new map
	 * @return A SortedMap builder
	 */
	public static <K, V> SortedBuilder<K, V> newSorted(SortedMap<K, ? extends V> map)
	{
		return new SortedBuilder<K,V>(new TreeMap<K, V>(map));
	}

	/**
	 * Creates a builder to produce a {@link SortedMap}.
	 *
	 * <p>
	 * <b>Note:</b> if mutability is not required, and the Map will not contain
	 * null values or null keys, use
	 * {@code ImmutableSortedMap.orderedBy(comparator).build()} instead.
	 *
	 * @param comparator the comparator to sort the keys with
	 * @return A SortedMap builder
	 */
	public static <C, K extends C, V> SortedBuilder<K, V> newSorted(Comparator<C> comparator)
	{
		return new SortedBuilder<K,V>(new TreeMap<K, V>(comparator));
	}
	
	/**
	 * The fluent Map builder.
	 * 
	 * @param <K> The intended key type
	 * @param <V> The intended key type
	 */
	@SuppressWarnings("unchecked")	
	public static abstract class Builder<K,V,T extends Builder<K,V,T>>
	{
		protected final Map<K,V> delegate;
		
		protected Builder(Map<K,V> map)
		{
			this.delegate = map;
		}
		
		/**
		 * Adds an entry to the Map, returning {@code this}.
		 * 
		 * @param key The key
		 * @param value The value
		 * @return provides a fluent interface
		 */
		public T add(K key, V value)
		{
			delegate.put(key, value);
			return (T) this;
		}
		
		/**
		 * Adds several entries to the Map, returning {@code this}.
		 * 
		 * @return provides a fluent interface
		 */
		public T add(K k1, V v1, K k2, V v2)
		{
			delegate.put(k1, v1);
			delegate.put(k2, v2);
			return (T) this;
		}

		/**
		 * Adds several entries to the Map, returning {@code this}.
		 * 
		 * @return provides a fluent interface
		 */
		public T add(K k1, V v1, K k2, V v2, K k3, V v3)
		{
			delegate.put(k1, v1);
			delegate.put(k2, v2);
			delegate.put(k3, v3);
			return (T) this;
		}
		
		/**
		 * Adds several entries to the Map, returning {@code this}.
		 * 
		 * @return provides a fluent interface
		 */
		public T add(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4)
		{
			delegate.put(k1, v1);
			delegate.put(k2, v2);
			delegate.put(k3, v3);
			delegate.put(k4, v4);
			return (T) this;
		}

		/**
		 * Adds several entries to the Map, returning {@code this}.
		 * 
		 * @return provides a fluent interface
		 */
		public T add(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5)
		{
			delegate.put(k1, v1);
			delegate.put(k2, v2);
			delegate.put(k3, v3);
			delegate.put(k4, v4);
			delegate.put(k5, v5);
			return (T) this;
		}
		
		/**
		 * Adds several entries to the Map, returning {@code this}.
		 * 
		 * @param value The map whose values will be copied
		 * @return provides a fluent interface
		 */
		public T addAll(Map<? extends K,? extends V> value)
		{
			delegate.putAll(value);
			return (T) this;
		}
		
		/**
		 * Combines two arrays of equal length into key-value pairs.
		 *  
		 * @param keys The keys
		 * @param values The values
		 * @return provides a fluent interface
		 * @throws IllegalArgumentException if the collections aren't of equal length 
		 */
		public T combine(K[] keys, V[] values)
		{
			Assert.notNull(keys);
			Assert.notNull(values);
			if (keys.length != values.length) {
				throw new IllegalArgumentException("The provided arrays must be the same length");
			}
			for (int i=0; i<keys.length; ++i) {
				delegate.put(keys[i], values[i]);
			}
			return (T) this;
		}
		
		/**
		 * Combines two collections of equal length into key-value pairs.
		 * 
		 * @param keys The keys
		 * @param values The values
		 * @return provides a fluent interface
		 * @throws IllegalArgumentException if the collections aren't of equal length 
		 */
		public T combine(Collection<K> keys, Collection<V> values)
		{
			Assert.notNull(keys);
			Assert.notNull(values);
			if (keys.size() != values.size()) {
				throw new IllegalArgumentException("The provided collections must be the same length");
			}
			Iterator<K> kit = keys.iterator();
			Iterator<V> vit = values.iterator();
			while (kit.hasNext()) {
				delegate.put(kit.next(), vit.next());
			}
			return (T) this;
		}
		
		/**
		 * Returns a {@link Map} containing all the entries added.
		 * 
		 * @return the built map
		 */
		public abstract Map<K,V> toMap();
		
		/**
		 * Returns an immutable {@link Map} containing all the entries added.
		 * 
		 * <p>If you know you're building an immutable map with no null keys nor
		 * values, it's probably just easier to use the Google Guava API. See
		 * {@link ImmutableMap#builder()}. 
		 * 
		 * <p>Anyway, if the Map contains null values or keys, this uses
		 * {@link Collections#unmodifiableMap(Map)}, otherwise it uses
		 * {@link ImmutableMap#copyOf(Map)} which is way better.
		 * 
		 * @return the built map
		 */
		public abstract Map<K,V> toImmutableMap();
	}
	
	/**
	 * The fluent Map builder.
	 * 
	 * @param <K> The intended key type
	 * @param <V> The intended key type
	 */
	public static class MapBuilder<K,V> extends Builder<K,V,MapBuilder<K,V>>
	{
		protected MapBuilder(Map<K,V> map)
		{
			super(map);
		}
		
		/**
		 * Returns a {@link LinkedHashMap} containing all the entries added.
		 * 
		 * @return the built map
		 */
		public Map<K,V> toMap()
		{
			return new LinkedHashMap<K,V>(delegate);
		}
		
		/**
		 * Returns an immutable {@link Map} containing all the entries added.
		 * 
		 * <p>If you know you're building an immutable map with no null keys nor
		 * values, it's probably just easier to use the Google Guava API. See
		 * {@link ImmutableMap#builder()}. 
		 * 
		 * <p>Anyway, if the Map contains null values or keys, this uses
		 * {@link Collections#unmodifiableMap(Map)}, otherwise it uses
		 * {@link ImmutableMap#copyOf(Map)} which is way better.
		 * 
		 * @return the built map
		 */
		public Map<K,V> toImmutableMap()
		{
			try {
				return ImmutableMap.copyOf(delegate);
			} catch (NullPointerException e) {
				return Collections.unmodifiableMap(toMap());
			}
		}
	}

	/**
	 * The fluent Map builder for SortedMap instances
	 * 
	 * @param <K> The intended key type
	 * @param <V> The intended key type
	 */
	public static class SortedBuilder<K,V> extends Builder<K,V,SortedBuilder<K,V>>
	{
		protected SortedBuilder(SortedMap<K,V> delegate)
		{
			super(delegate);
		}
		
		/**
		 * Returns a {@link TreeMap} containing all the entries added.
		 * 
		 * @return the built map
		 */
		public SortedMap<K,V> toMap()
		{
			return new TreeMap<K,V>(delegate);
		}
		
		public SortedMap<K,V> toImmutableMap()
		{
			try {
				return ImmutableSortedMap.copyOfSorted((SortedMap<K,V>) delegate);
			} catch (NullPointerException e) {
				return Collections.unmodifiableSortedMap(toMap());
			}
		}
	}

	/**
	 * The fluent Map builder for EnumMap instances
	 * 
	 * @param <K> The intended key type
	 * @param <V> The intended key type
	 */	
	public static class EnumBuilder<K extends Enum<K>,V> extends Builder<K,V,EnumBuilder<K,V>>
	{
		protected EnumBuilder(EnumMap<K,V> delegate)
		{
			super(delegate);
		}
		
		/**
		 * Returns a {@link EnumMap} containing all the entries added.
		 * 
		 * @return the built map
		 */
		public EnumMap<K,V> toMap()
		{
			return new EnumMap<K,V>(delegate);
		}
		
		public Map<K,V> toImmutableMap()
		{
			try {
				return ImmutableMap.copyOf(delegate);
			} catch(NullPointerException e ) {
				return Collections.unmodifiableMap(toMap());				
			}
		}
	}
}
