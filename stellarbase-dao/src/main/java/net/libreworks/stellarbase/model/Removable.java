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
import java.util.Date;

/**
 * Base interface for data entities which can be removed.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 * @param <K> The type of identifier
 */
public interface Removable<K extends Serializable> extends Modifiable<K>
{
	/**
	 * @return the removedBy
	 */
	public String getRemovedBy();
	
	/**
	 * @return the removedOn
	 */
	public Date getRemovedOn();
	
	/**
	 * @param removedBy the removedBy to set
	 */
	public void setRemovedBy(String removedBy);
	
	/**
	 * @param removedOn the removedOn to set
	 */
	public void setRemovedOn(Date removedOn);
}
