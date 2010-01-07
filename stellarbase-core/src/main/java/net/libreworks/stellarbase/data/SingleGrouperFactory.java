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
package net.libreworks.stellarbase.data;

import java.util.Collection;
import java.util.Map;

/**
 * @author Jonathan Hawk
 * @version $Id$
 * @param <K> The key type
 * @param <V> The value type
 */
public interface SingleGrouperFactory<K,V>
{
	/**
	 * Gets the container for group items.
	 * 
	 * @return The container for group items
	 */
	public Collection<V> getContainer();
	
	/**
	 * Gets the container for groups and their items.
	 * 
	 * @return The container for group items
	 */
	public Map<K,Collection<V>> getGroupContainer();
}
