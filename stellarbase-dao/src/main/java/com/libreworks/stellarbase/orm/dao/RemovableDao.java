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
package com.libreworks.stellarbase.orm.dao;

import java.io.Serializable;
import java.util.List;

import com.libreworks.stellarbase.orm.model.Removable;

/**
 * Base interface for data access objects which allow for removal of entities (as opposed to deleting them).
 * 
 * @author Jonathan Hawk
 * @version $Id$
 * @param <T> The type of entity
 * @param <K> The type of identifier
 */
public interface RemovableDao<T extends Removable<K>,K extends Serializable> extends ReadableDao<T, K>
{
	/**
	 * Gets all entities in the repository that aren't removed.
	 * 
	 * This method could be very inefficient for sources which have a great
	 * number of entities (thousands). Ordering is left up to the implementing
	 * class.
	 * 
	 * @return All entities that are not removed
	 */
	public List<T> getAllNotRemoved();
	
	/**
	 * Removes an entity, clearing it from the persistence context.
	 * 
	 * @param entity The entity to remove
	 * @param by The user who did the removing
	 */
	public void remove(T entity, String by);
	
	/**
	 * Clears the removed information for entity.
	 * 
	 * @param entity The entity to un-remove (must be removed already).
	 * @param by The user who did the un-removing.
	 * @throws IllegalArgumentException if the entity isn't removed
	 */
	public void unremove(T entity, String by);
}
