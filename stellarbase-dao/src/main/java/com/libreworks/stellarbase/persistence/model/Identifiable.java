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
package com.libreworks.stellarbase.persistence.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Base interface for data entities.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 * @param <K> The type of identifier
 */
public interface Identifiable<K extends Serializable> extends Serializable
{
	/**
	 * Gets the username of the user who created this entity.
	 * 
	 * If the entity type does not support a created by field, this method can
	 * safely return null.
	 * 
	 * @return The created by username or null.
	 */
	public String getCreatedBy();
	
	/**
	 * Gets the created on date of the entity.
	 * 
	 * If the entity type does not support a created on date, this method can
	 * safely return null.
	 * 
	 * @return The created on date or null.
	 */
	public Date getCreatedOn();
	
	/**
	 * Gets the entity identifier.
	 * 
	 * @return The entity identifier
	 */
	public K getId();
	
	/**
	 * Sets the username of the user who created this entity.
	 * 
	 * If the entity type does not support a created by field, this method can
	 * safely do nothing.
	 * 
	 * @param createdBy The created by username
	 */
	public void setCreatedBy(String createdBy);
	
	/**
	 * Sets the created on date of the entity.
	 * 
	 * If the entity type does not support a created on date, this method can
	 * safely do nothing.
	 * 
	 * @param createdOn The created on date
	 */
	public void setCreatedOn(Date createdOn);
}
