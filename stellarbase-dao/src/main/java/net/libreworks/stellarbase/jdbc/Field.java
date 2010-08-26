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
package net.libreworks.stellarbase.jdbc;

import org.apache.commons.lang.StringUtils;

/**
 * A data field or column.
 * 
 * Ported from the PHP framework Xyster by Jonathan Hawk, the original author.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class Field implements Symbol
{
	protected String name;
	protected String alias;
	
	/**
	 * Creates a new field
	 * 
	 * @param name The field name
	 */
	public Field(String name)
	{
		this(name, name);
	}

	/**
	 * Creates a new field
	 * 
	 * @param name The field name
	 * @param alias The alias name
	 */
	public Field(String name, String alias)
	{
		if ( StringUtils.isBlank(name) ) {
			throw new IllegalArgumentException("Field name cannot be blank");
		}
		if ( StringUtils.isBlank(alias) ) {
			throw new IllegalArgumentException("Field alias cannot be blank");
		}
		this.name = name.trim();
		this.alias = alias.trim();
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the alias
	 */
	public String getAlias()
	{
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias)
	{
		this.alias = alias;
	}
}
