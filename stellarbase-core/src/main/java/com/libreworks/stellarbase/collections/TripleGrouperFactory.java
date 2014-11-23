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
import java.util.Map;

/**
 * Factory interface for collections and maps used within {@link TripleGrouper}.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 * @param <A> The far outer group type
 * @param <B> The outer group type
 * @param <C> The inner group type
 * @param <V> The element type
 */
public interface TripleGrouperFactory<A,B,C,V> extends DoubleGrouperFactory<B,C,V>
{
	/**
	 * Gets the group container for nested groups.
	 * 
	 * @return The container for group items
	 */
	public Map<A,Map<B,Map<C,Collection<V>>>> getFarOuterGroupContainer();
}
