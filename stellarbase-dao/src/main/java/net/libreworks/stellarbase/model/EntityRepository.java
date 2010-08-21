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
package net.libreworks.stellarbase.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Base interface for objects serving as repositories, such as services or DAOs.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 * @param <T> The type of entity
 * @param <K> The type of identifier
 */
public interface EntityRepository<T extends Identifiable<K>, K extends Serializable>
{
	/**
	 * Gets an entity by the fields specified.
	 * 
	 * @param fields The values to search
	 * @return The entity found or null
	 */
	public T find(Map<String,?> fields);
	
	/**
	 * Searches for all entities matching the fields specified.
	 * 
	 * @param fields The values to search
	 * @return The entities found
	 */
	public List<T> findAll(Map<String,?> fields);
	
	/**
	 * Gets all entities in the repository.
	 * 
	 * This method could be very inefficient for sources which have a great
	 * number of entities (thousands). Ordering is left up to the implementing
	 * class.
	 * 
	 * @return All entities
	 */
	public List<T> getAll();
	
	/**
	 * Gets an entity by its identifier.
	 * 
	 * If the entity is not found in the repository, null is returned.
	 * 
	 * @param id The identifier
	 * @return The entity or null
	 */
	public T getById(K id);
	
	/**
	 * Gets several entities by their identifiers.
	 * 
	 * @param ids The identifiers
	 * @return The entities found
	 */
	public List<T> getByIds(Collection<K> ids);
	
	/**
	 * Gets the class supported by this repository.
	 * 
	 * @return The entity class
	 */
	public Class<T> getEntityClass();
	
	/**
	 * Loads an entity by its identifier.
	 * 
	 * If the entity is not found in the repository, an exception will be
	 * thrown.
	 * 
	 * @param id The identifier
	 * @return The entity
	 */
	public T loadById(K id);
	
	/**
	 * Updates the entity with the most recent values from the data store.
	 * 
	 * @param entity The entity to refresh
	 */
	public void refresh(T entity);
}
