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
package com.libreworks.stellarbase.dao;

import java.io.Serializable;
import java.util.Map;

import org.springframework.validation.BindException;

import com.libreworks.stellarbase.persistence.model.Modifiable;

/**
 * Base interface for data access objects which allow creating and updating.
 * 
 * @author Jonathan Hawk
 * @param <T> The type of entity
 * @param <K> The type of identifier
 */
public interface WritableDao<T extends Modifiable<K>,K extends Serializable> extends ReadableDao<T,K>
{
	/**
	 * Creates a new entity with the values supplied.
	 * 
	 * @param values The values to use
	 * @param by The user who did the creating
	 * @return The entity created
	 * @throws BindException if validation of the entity fails
	 */
	public T create(Map<String,?> values, String by) throws BindException;
	
	/**
	 * Whether entities supported by this DAO can be updated.
	 * 
	 * If a DAO supports creating an entity but not updating it, this method
	 * should return false, and similarly the
	 * {@link #update(Modifiable, Map, String)} method should throw an exception
	 * if called.
	 * 
	 * @return Whether entities can be updated
	 */
	public boolean canUpdate();
	
	/**
	 * Updates a given entity with the values supplied.
	 * 
	 * @param entity The entity to update
	 * @param values The values to update
	 * @param by The user who did the updating
	 * @throws BindException if validation of the entity fails
	 * @throws UnsupportedOperationException if this DAO doesn't support updates
	 */
	public void update(T entity, Map<String,?> values, String by) throws BindException;
}
