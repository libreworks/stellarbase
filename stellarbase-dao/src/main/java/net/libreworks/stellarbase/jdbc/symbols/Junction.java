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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * A Junction is an infix expression of {@link Xyster_Data_Criterion} objects.
 * 
 * A typical example of a junction is a SQL where clause: 
 * {@code ( <var>expression</var> AND <var>expression</var> )}
 * 
 * Ported from the PHP framework Xyster by Jonathan Hawk, the original author.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class Junction extends Criterion implements Clause<Criterion>
{
	private static final long serialVersionUID = 1L;

	protected ArrayList<Criterion> criteria = new ArrayList<Criterion>();
	protected boolean conjunction;
	
	protected static final String OR = " OR ";
	protected static final String AND = " AND ";

	/**
	 * Creates a new Junction using an "AND" operator (conjunction).
	 * 
	 * @param a The left Criterion
	 * @param b The right Criterion
	 * @return The new Junction
	 */
	public static Junction and(Criterion a, Criterion b)
	{
		return new Junction(a, b, true);
	}
	
	/**
	 * Creates a new Junction using an "OR" operator (disjunction).
	 * 
	 * @param a The left Criterion
	 * @param b The right Criterion
	 * @return The new Junction
	 */
	public static Junction or(Criterion a, Criterion b)
	{
		return new Junction(a, b, false);
	}
	
	protected Junction(Criterion a, Criterion b, boolean isConjunction)
	{
		this.conjunction = isConjunction;
		Assert.notNull(a);
		Assert.notNull(b);
		if ( a instanceof Junction && conjunction == ((Junction) a).isConjunction() ) {
			criteria.addAll(((Junction)a).getSymbols());
		} else {
			criteria.add(a);
		}
		if ( b instanceof Junction && conjunction == ((Junction) b).isConjunction() ) {
			criteria.addAll(((Junction)b).getSymbols());
		} else {
			criteria.add(b);
		}
	}
	
	public Junction add(Criterion c)
	{
		Assert.notNull(c);
		if ( c instanceof Junction && conjunction == ((Junction) c).isConjunction() ) {
			criteria.addAll(((Junction)c).getSymbols());
		} else {
			criteria.add(c);
		}
		return this;
	}
	
	@Override
	public boolean evaluate(Object value)
	{
		boolean ok = true;
		if ( !conjunction ) {
			ok = false;
			for(Criterion c : criteria){
				if ( c.evaluate(value) ) {
					ok = true;
					break;
				}
			}
		} else {
			for(Criterion c : criteria){
				if ( !c.evaluate(value) ) {
					ok = false;
					break;
				}
			}
		}
		return ok;
	}

	@Override
    public Collection<Field> getAllFields()
    {
	    ArrayList<Field> fields = new ArrayList<Field>();
        for(Criterion c : criteria) {
            fields.addAll(c.getAllFields());
        }
        return fields;
    }

    public Collection<Criterion> getSymbols()
	{
		return Collections.unmodifiableCollection(criteria);
	}
	
	/**
	 * @return the conjunction
	 */
	public boolean isConjunction()
	{
		return conjunction;
	}

	public boolean isEmpty()
	{
		return criteria.isEmpty();
	}

	public Iterator<Criterion> iterator()
	{
		return criteria.iterator();
	}

	public boolean remove(Criterion e)
	{
		return criteria.remove(e);
	}

	public int size()
	{
		return criteria.size();
	}

	@Override
	public String toString()
	{
		ArrayList<String> strings = new ArrayList<String>();
		for(Criterion c : criteria){
			strings.add(c.toString());
		}
		return "( " + StringUtils.join(strings.toArray(), conjunction ? AND : OR) + " )";
	}
}
