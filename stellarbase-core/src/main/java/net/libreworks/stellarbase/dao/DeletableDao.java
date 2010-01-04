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
package net.libreworks.stellarbase.dao;

import java.io.Serializable;
import net.libreworks.stellarbase.model.Identifiable;

/**
 * Base interface for data access objects which allow deleting or removing of entities.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 * @param <T>
 * @param <K>
 */
public interface DeletableDao<T extends Identifiable<K>,K extends Serializable> extends ReadableDao<T,K>
{
	/**
	 * Deletes the entity.
	 * 
	 * If an entity implements the removable interface, calling this method
	 * should only set the removed fields and not actually delete the entity.
	 * 
	 * @param entity The entity to delete
	 * @param by The user who did the deleting
	 */
	public void delete(T entity, String by);
}
