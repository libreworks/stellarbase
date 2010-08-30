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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import net.libreworks.stellarbase.jdbc.symbols.Criterion;
import net.libreworks.stellarbase.jdbc.symbols.Field;
import net.libreworks.stellarbase.jdbc.symbols.Sort;
import net.libreworks.stellarbase.jdbc.symbols.SymbolClause;

/**
 * Parameters for a SQL query.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class Query
{
	protected String from;
	protected boolean distinct;
	protected SymbolClause<Field> select = new SymbolClause<Field>();
	protected SymbolClause<Criterion> where = new SymbolClause<Criterion>();
	protected SymbolClause<Criterion> having = new SymbolClause<Criterion>();
	protected SymbolClause<Sort> order = new SymbolClause<Sort>();
	
	/**
	 * Creates a new Query
	 */
	public Query()
	{
	}
	
	/**
	 * Creates a new Query
	 * 
	 * @param from The table from which the query pulls
	 */
	public Query(String from)
	{
	    this.from = from;
	}
	
	/**
	 * Sets the origin table
	 * 
	 * @param from The table
	 * @return provides a fluent interface
	 */
	public Query from(String from)
	{
	    this.from = from;
	    return this;
	}
	
	/**
	 * Sets whether the query is distinct or not
	 * 
	 * @param distinct Whether the query is distinct
	 * @return provides a fluent interface
	 */
	public Query distinct(boolean distinct)
	{
	    this.distinct = distinct;
	    return this;
	}
	
	/**
	 * Sets the fields used in the select.
	 * 
	 * @param fields The fields
	 * @return provides a fluent interface
	 */
	public Query select(Field... fields)
	{
	    for(Field field : fields){
	        select.add(field);
	    }
	    return this;
	}
	
	/**
     * Sets the fields used in the select.
     * 
     * @param fields The fields
     * @return provides a fluent interface
     */
	public Query select(Collection<Field> fields)
	{
	    for(Field field : fields){
            select.add(field);
        }
        return this;
	}
    
    /**
     * Adds a having clause.
     * 
     * @param having The criteria
     * @return provides a fluent interface
     */
    public Query having(Criterion... having)
    {
        for(Criterion h : having) {
            this.having.add(h);
        }
        return this;
    }

    /**
     * Adds a having clause.
     * 
     * @param having The criteria
     * @return provides a fluent interface
     */
    public Query having(Collection<Criterion> having)
    {
        for(Criterion h : having) {
            this.having.add(h);
        }
        return this;
    }
	
	/**
	 * Adds a where clause.
	 * 
	 * @param criteria The criteria
	 * @return provides a fluent interface
	 */
	public Query where(Criterion... criteria)
	{
	    for(Criterion c : criteria) {
	        where.add(c);
	    }
	    return this;
	}
	
	/**
     * Adds a where clause.
     * 
     * @param criteria The criteria
     * @return provides a fluent interface
     */
	public Query where(Collection<Criterion> criteria)
	{
	    for(Criterion c : criteria) {
            where.add(c);
        }
	    return this;
	}
	
	/**
	 * Adds an order by clause
	 * 
	 * @param sorts The sort clause
	 * @return provides a fluent interface
	 */
	public Query orderBy(Sort... sorts)
	{
	    for(Sort sort : sorts){
	        order.add(sort);
	    }
	    return this;
	}
	
	/**
     * Adds an order by clause
     * 
     * @param sorts The sort clause
     * @return provides a fluent interface
     */
    public Query orderBy(Collection<Sort> sorts)
    {
        for(Sort sort : sorts){
            order.add(sort);
        }
        return this;
    }
    
    /**
     * Executes the query.
     * 
     * @param template The JDBC Template
     * @return The results as a List of Maps
     */
    public List<Map<String,Object>> execute(JdbcTemplate template)
    {
        return null;
    }
}
