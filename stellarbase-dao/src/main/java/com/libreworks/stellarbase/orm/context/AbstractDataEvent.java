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

import com.libreworks.stellarbase.orm.model.Identifiable;

import org.springframework.context.ApplicationEvent;

/**
 * Abstract base for all data access events (create, update, delete, etc.).
 * 
 * @author Jonathan Hawk
 * @version $Id: AbstractDataEvent.java 5 2010-01-04 23:30:16Z jonathanhawk $
 */
public abstract class AbstractDataEvent extends ApplicationEvent
{
	private static final long serialVersionUID = 1L;
	
	private final String by;
	
	/**
	 * Creates a new data event.
	 * 
	 * @param source The object on which the event initially occurred
	 * @param by The name of the user who was authenticated at the time
	 */
	public AbstractDataEvent(Identifiable<?> source, String by)
	{
		super(source);
		this.by = by;
	}

	/**
	 * Gets the entity.
	 * 
	 * This is just a shortcut that casts the source back into an Identifiable.
	 * 
	 * @return The entity
	 */
	public Identifiable<?> getEntity()
	{
		return (Identifiable<?>)source;
	}
	
	/**
	 * Gets the authenticated user during the event
	 * 
	 * @return The authenticated user
	 */
	public String getBy()
	{
		return by;
	}
}
