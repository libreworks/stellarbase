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
package net.libreworks.stellarbase.jdbc.symbols;

import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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
	private static final long serialVersionUID = 1L;
	
	protected String name;
	protected String alias;
	
	/**
	 * Creates a new field
	 * 
	 * @param name The field name
	 */
	protected Field(String name)
	{
		this(name, name);
	}

	/**
	 * Creates a new field
	 * 
	 * @param name The field name
	 * @param alias The alias name
	 */
	protected Field(String name, String alias)
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof Field))
			return false;
		Field other = (Field) obj;
		return new EqualsBuilder()
			.append(name, other.name)
			.append(alias, other.alias)
			.isEquals();
	}

	/**
	 * @return the alias
	 */
	public String getAlias()
	{
		return alias;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
			.append(name)
			.append(alias)
			.toHashCode();
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getName();
	}
	
	/**
	 * Creates a new Field
	 * 
	 * @param name The name of the field
	 * @return The field created
	 */
	public static Field named(String name)
	{
		return named(name, name);
	}
	
	/**
	 * Creates a new Field with an alias
	 * 
	 * @param name The name of the field
	 * @param alias The alias of the field
	 * @return The field created
	 */
	public static Field named(String name, String alias)
	{
	    Matcher m = AggregateField.match(name);
	    if ( m.matches() ) {
	        return aggregate(Enum.valueOf(Aggregate.class, m.group(1)), name, alias);
	    } else {
	        return new Field(name, alias);
	    }
	}
	
	/**
	 * Creates a new Field that defines a group
	 * 
	 * @param name The name of the field
	 * @return The field created
	 */
	public static GroupField group(String name)
	{
		return new GroupField(name);
	}
	
	/**
	 * Creates a new Field that defines a group
	 * 
	 * @param name The name of the field
	 * @return The field created
	 */
	public static GroupField group(String name, String alias)
	{
		return new GroupField(name, alias);
	}

	/**
	 * Creates a new AggregateField using a pre-existing function.
	 * 
	 * @param function The function
	 * @param name The field name
	 * @param alias The field alias
	 * @return The field created
	 */
	public static AggregateField aggregate(Aggregate function, String name, String alias)
	{
	    return new AggregateField(function, name, alias);
	}
	
	/**
	 * Creates a new AggregateField to count the values in a tuple
	 * 
	 * @param name The name of the field
	 * @param alias The alias of the field
	 * @return The AggregateField created
	 */
	public static AggregateField count(String name, String alias)
	{
		return aggregate(Aggregate.COUNT, name, alias);
	}
	
	/**
	 * Creates a new AggregateField to sum the values in a field
	 * 
	 * @param name The name of the field
	 * @param alias The alias of the field
	 * @return The AggregateField created
	 */
	public static AggregateField sum(String name, String alias)
	{
		return aggregate(Aggregate.SUM, name, alias);
	}
	
	/**
	 * Creates a new AggregateField to average the values in a field
	 * 
	 * @param name The name of the field
	 * @param alias The alias of the field
	 * @return The AggregateField created
	 */
	public static AggregateField avg(String name, String alias)
	{
		return aggregate(Aggregate.AVG, name, alias);
	}
	
	/**
	 * Creates a new AggregateField to find the maximum value in a field
	 * 
	 * @param name The name of the field
	 * @param alias The alias of the field
	 * @return The AggregateField created
	 */
	public static AggregateField max(String name, String alias)
	{
		return aggregate(Aggregate.MAX, name, alias);
	}

	/**
	 * Creates a new AggregateField to find the minimum value in a field
	 * 
	 * @param name The name of the field
	 * @param alias The alias of the field
	 * @return The AggregateField created
	 */
	public static AggregateField min(String name, String alias)
	{
		return aggregate(Aggregate.MIN, name, alias);
	}
}
