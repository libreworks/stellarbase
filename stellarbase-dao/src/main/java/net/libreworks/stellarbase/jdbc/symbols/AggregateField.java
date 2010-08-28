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
import java.util.regex.Pattern;

/**
 * A data field or column with an applied aggregate function
 * 
 * Ported from the PHP framework Xyster by Jonathan Hawk, the original author.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class AggregateField extends Field
{
	private static final long serialVersionUID = 1L;
	
	protected Aggregate function;
    protected static Pattern AGGREGATES = Pattern.compile(
            "^(AVG|MAX|MIN|COUNT|SUM)\\(([\\w\\W]*)\\)$",
            Pattern.CASE_INSENSITIVE);
	
	/**
	 * Creates a new AggregateField
	 * 
	 * @param function The aggregate function
	 * @param name The name
	 */
	public AggregateField(Aggregate function, String name)
	{
		this(function, name, name);
	}
	
	/**
	 * Creates a new AggregateField
	 * 
	 * @param function The aggregate function
	 * @param name The name
	 * @param alias The alias
	 */
	public AggregateField(Aggregate function, String name, String alias)
	{
		super(name, alias);
		this.function = function;
	}

	/**
	 * @return the function
	 */
	public Aggregate getFunction()
	{
		return function;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return function.toString() + "(" + super.toString() + ")";
	}
	
	/**
	 * Returns a Matcher for matching a field against an aggregate function.
	 * 
	 * The first group is the function name, the second group is the field name.
	 * 
	 * @param haystack The string to search
	 * @return The Matcher
	 */
	public static Matcher match(String haystack)
	{
	    return AGGREGATES.matcher(haystack);
	}
}
