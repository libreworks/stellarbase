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
 * Base interface for entities which have modified dates.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 * @param <K> The type of identifier
 */
public interface Modifiable<K extends Serializable> extends Identifiable<K>
{
	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy();
	
	/**
	 * @return the modifiedOn
	 */
	public Date getModifiedOn();
	
	/**
	 * Gets the version of the entity (usually a Long, Integer, or Short).
	 * 
	 * If the entity doesn't have a version number, this can safely return false.
	 * 
	 * @return The version
	 */
	public Serializable getVersion();
	
	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy);
	
	/**
	 * @param modifiedOn the modifiedOn to set
	 */
	public void setModifiedOn(Date modifiedOn);
	
	/**
	 * Sets the version of the entity (usually a Long, Integer, or Short).
	 * 
	 * If the entity doesn't have a version number, this can safely do nothing.
	 * 
	 * @param version the version 
	 */
	public void setVersion(Serializable version);
}
