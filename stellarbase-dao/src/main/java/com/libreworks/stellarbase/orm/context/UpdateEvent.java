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
package com.libreworks.stellarbase.orm.context;

import org.springframework.beans.PropertyValues;

import com.libreworks.stellarbase.orm.model.Identifiable;

/**
 * Occurs when an entity is updated.
 * 
 * @author Jonathan Hawk
 * @version $Id: UpdateEvent.java 7 2010-01-07 06:48:34Z jonathanhawk $
 */
public class UpdateEvent extends AbstractDataEvent
{
    private static final long serialVersionUID = 1L;
    
	protected final PropertyValues oldValues;
	
	/**
	 * Creates a new Update event.
	 * 
	 * @param source The entity that is involved in the insert
	 * @param old The property values before the update occurred
	 * @param by The username of the user who was authenticated
	 */
	public UpdateEvent(Identifiable<?> source, PropertyValues old, String by)
    {
	    super(source, by);
	    this.oldValues = old;
    }

	/**
     * @return the old
     */
    public PropertyValues getOldValues()
    {
    	return oldValues;
    }
}
