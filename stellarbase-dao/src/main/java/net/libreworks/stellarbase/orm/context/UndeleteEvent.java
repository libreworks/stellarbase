/**
 * Copyright 2011 LibreWorks contributors
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
package net.libreworks.stellarbase.orm.context;

import net.libreworks.stellarbase.orm.model.Identifiable;

/**
 * Occurs when an entity is Un-removed.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class UndeleteEvent extends AbstractDataEvent
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new Undelete event
	 * 
	 * @param source The entity that is involved in the insert
	 * @param by The username of the user who was authenticated
	 */
	public UndeleteEvent(Identifiable<?> source, String by)
	{
		super(source, by);
	}
}
