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
package com.libreworks.stellarbase.web.json;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;

/**
 * Provides an API to easily create JSON feeds compatible with {@code dojo.data.ItemFileReadStore}.
 * 
 * <p>Since the Dojo Toolkit has deprecated this format in favor of
 * {@code dojo.store}, this class will fall into disuse and will later be marked
 * deprecated.
 * 
 * @author Jonathan Hawk
 */
public class DojoData implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected String identifier;
	protected String label;
	protected final LinkedHashMap<Serializable,Map<String,?>> items = new LinkedHashMap<Serializable,Map<String,?>>();
	
	public DojoData()
	{
		this(null, null, null);
	}
	
	public DojoData(String identifier)
	{
		this(identifier, null, null);
	}
	
	public DojoData(String identifier, Iterable<Map<String,?>> items)
	{
		this(identifier, items, null);
	}
	
	public DojoData(String identifier, Iterable<Map<String,?>> items, String label)
	{
		this.identifier = identifier;
		this.label = label;
		if ( items != null ) {
			addAll(items);
		}
	}

	/**
	 * Adds a single item.
	 * 
	 * @param item The item to add
	 * @return provides a fluent interface
	 * @throws IllegalStateException if the {@code identifier} property hasn't been set
	 * @throws IllegalArgumentException if {@code item} doesn't have an identifier key
	 */
	public DojoData add(Map<String,?> item)
	{
		if ( StringUtils.isBlank(identifier) ) {
			throw new IllegalStateException("You must set an identifier prior to adding items");
		} else if ( !item.containsKey(identifier) ) {
			throw new IllegalArgumentException("The item must have an entry containing the identifier");
		} else if ( items.containsKey(item.get(identifier)) ) {
			throw new IllegalArgumentException("Overwriting items using addItem is not allowed");
		}
		items.put((Serializable)item.get(identifier), item);
		return this;
	}
	
	/**
	 * Adds multiple items.
	 * 
	 * @param items The items to add
	 * @return provides a fluent interface
	 * @throws IllegalStateException if the {@code identifier} property hasn't been set
	 * @throws IllegalArgumentException if an item doesn't have an identifier key
	 */
	public DojoData addAll(Iterable<Map<String,?>> items)
	{
		for(Map<String,?> item : items) {
			add(item);
		}
		return this;
	}
	
	/**
	 * Removes all items
	 * @return
	 */
	public DojoData clear()
	{
		items.clear();
		return this;
	}
	
	/**
	 * Whether the added items contains the identifier provided.
	 * 
	 * @param id the identifier to find
	 * @return whether the identifier was found
	 */
	public boolean contains(Serializable id)
	{
		return items.containsKey(id);
	}

	/**
	 * Exports the data container into a Map that can be converted to JSON.
	 * 
	 * You can use Spring 3.0's new MVC ability to turn {@link Map}s or other
	 * objects into their JSON representation. One way of achieving this is to
	 * return the {@code MappingJacksonJsonView} from your controller methods.
	 * 
	 * @return The data container
	 * @throws IllegalStateException if the {@code identifier} property hasn't been set
	 */
	public Map<String,?> export()
	{
		String identifier = getIdentifier();
		if ( StringUtils.isBlank(identifier) ) {
			throw new IllegalStateException("You must specify an identifier before you export");
		}
		LinkedHashMap<String,Object> map = new LinkedHashMap<String,Object>(3);
		map.put("identifier", identifier);
		String label = getLabel();
		if ( !StringUtils.isBlank(label) ) {
			map.put("label", label);
		}
		map.put("items", items.values());
		return map;
	}
	
	/**
	 * @return the identifier
	 */
	public String getIdentifier()
	{
		return identifier;
	}
	
	/**
	 * @return the items
	 */
	public Map<Serializable,Map<String,?>> getItems()
	{
		return ImmutableMap.copyOf(items);
	}

	/**
	 * @return the label
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * Removes an item by identifier
	 * 
	 * @param id the identifier to remove
	 * @return provides a fluent interface
	 */
	public DojoData remove(Serializable id)
	{
		if ( contains(id) ) {
			items.remove(id);
		}
		return this;
	}

	/**
	 * Sets a single item, potentially overwriting an existing one.
	 * 
	 * @param item The item to set
	 * @return provides a fluent interface
	 * @throws IllegalStateException if the {@code identifier} property hasn't been set
	 * @throws IllegalArgumentException if {@code item} doesn't have an identifier key
	 */
	public DojoData set(Map<String,?> item)
	{
		if ( StringUtils.isBlank(identifier) ) {
			throw new IllegalStateException("You must set an identifier prior to adding items");
		} else if ( !item.containsKey(identifier) ) {
			throw new IllegalArgumentException("The item must have an entry containing the identifier");
		}
		items.put((Serializable)item.get(identifier), item);
		return this;
	}

	/**
	 * @param items the items to set
	 * @return provides a fluent interface
	 * @throws IllegalStateException if the {@code identifier} property hasn't been set
	 * @throws IllegalArgumentException if an item doesn't have an identifier key
	 */
	public DojoData setAll(Iterable<Map<String,?>> items)
	{
		clear();
		addAll(items);
		return this;
	}
	
	/**
	 * @param identifier the identifier to set
	 * @return provides a fluent interface
	 */
	public DojoData setIdentifier(String identifier)
	{
		this.identifier = identifier;
		return this;
	}
	
	/**
	 * @param label the label to set
	 * @return provides a fluent interface
	 */
	public DojoData setLabel(String label)
	{
		this.label = label;
		return this;
	}
}
