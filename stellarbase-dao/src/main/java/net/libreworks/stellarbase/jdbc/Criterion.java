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

import java.util.ArrayList;
import java.util.Collection;

/**
 * The base class for Criteria.
 * 
 * Ported from the PHP framework Xyster by Jonathan Hawk, the original author.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public abstract class Criterion implements Symbol
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Evaluates the criterion for a given object
	 * 
	 * @param value The value to evaluate
	 * @return The result of the criterion applied to the object
	 */
	public abstract boolean evaluate(Object value);
	
	/**
	 * Gets all Field objects within a Criterion.
	 * 
	 * @param criteria The criterion containing the fields (can be null)
	 * @return A collection containing the fields within
	 */
	public static Collection<Field> getFields(Criterion criteria)
	{
	    ArrayList<Field> fields = new ArrayList<Field>();
	    if ( criteria instanceof Junction ) {
	        for(Criterion c : ((Junction) criteria).getSymbols()) {
	            fields.addAll(getFields(c));
	        }
	    } else if ( criteria instanceof Expression ) {
	        Expression e = (Expression)criteria;
	        fields.add(e.getLeft());
	        if ( e.getRight() instanceof Field ) {
	            fields.add((Field) e.getRight());
	        }
	    }
		return fields;
	}
}
