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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hsqldb.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import net.libreworks.stellarbase.jdbc.symbols.Criterion;
import net.libreworks.stellarbase.jdbc.symbols.Field;
import net.libreworks.stellarbase.jdbc.symbols.GroupField;
import net.libreworks.stellarbase.jdbc.symbols.Junction;
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
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
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
     * Gets the SQL query and parameters
     * 
     * @param template The JDBCTemplate
     * @return The SQL fragment
     * @throws SQLException 
     */
    protected Fragment assemble(JdbcTemplate template) throws SQLException
    {
    	StringBuilder sql = new StringBuilder("SELECT ");
    	Translator translator = new Translator(template.getDataSource().getConnection().getMetaData());
    	ArrayList<Object> params = new ArrayList<Object>();
    	boolean quote = true;
    	// get columns
    	ArrayList<String> columns = new ArrayList<String>();
    	for(Field field : select) {
    		StringBuilder fieldSql = new StringBuilder()
    			.append(translator.translateField(field, quote).getSql())
    			.append(" AS ")
    			.append(translator.idQuote).append(field.getAlias())
    			.append(translator.idQuote);
    		columns.add(fieldSql.toString());
    	}
    	sql.append(StringUtils.join(columns, ", "));
    	// add from
    	sql.append(" FROM ").append(from);
    	// add WHERE
    	if ( !where.isEmpty() ) {
    		Fragment whereSql = translator.translateCriterion(Junction.and(where), quote);
    		params.addAll(whereSql.getParameters());
    		sql.append(" WHERE ")
    			.append(whereSql.getSql());
    	}
    	// add group
    	SymbolClause<GroupField> groups = new SymbolClause<GroupField>();
    	for(Field field : select){
    		if ( field instanceof GroupField ) {
    			groups.add((GroupField)field);
    		}
    	}
    	if ( !groups.isEmpty() ) {
    		sql.append(" GROUP BY ")
    			.append(translator.translateClause(groups, quote).getSql());
    	}
    	// add having
    	if ( !groups.isEmpty() && !having.isEmpty() ) {
    		Fragment havingSql = translator.translateCriterion(Junction.and(having), quote);
    		params.addAll(havingSql.getParameters());
    		sql.append(" HAVING ")
    			.append(havingSql.getSql());
    	}
    	// add order by
    	if ( !order.isEmpty() ) {
    		sql.append(" ORDER BY ")
    			.append(translator.translateClause(order, quote).getSql());
    	}
    	return new Fragment(sql.toString(), params);
    }
    
    protected int[] getTypes(Object[] params)
    {
    	int[] sqlTypes = new int[params.length];
    	for(int i=0; i<params.length; i++){
    		sqlTypes[i] = getSqlType(params[i]);
    	}
    	return sqlTypes;
    }
    
    protected int getSqlType(Object value)
    {
    	if ( value == null ) {
    		return Types.NULL;
    	} else if ( value instanceof java.sql.Date ) {
    		return Types.DATE;
    	} else if ( value instanceof java.sql.Time ) {
    		return Types.TIME;
    	} else if ( value instanceof java.util.Date || value instanceof java.sql.Timestamp ) {
    		return Types.TIMESTAMP;
    	} else if ( value instanceof Boolean ) {
    		return Types.BOOLEAN;
    	} else if ( value instanceof byte[] ) {
    		return Types.VARBINARY;
    	} else if ( value instanceof Short ) {
    		return Types.SMALLINT;
    	} else if ( value instanceof Integer ) {
    		return Types.INTEGER;
    	} else if ( value instanceof Long || value instanceof BigInteger ) {
    		return Types.BIGINT;
    	} else if ( value instanceof Double ) {
    		return Types.DOUBLE;
    	} else if ( value instanceof BigDecimal ) {
    		return Types.DECIMAL;
    	} else if ( value instanceof Float ) {
    		return Types.REAL;
    	} else {
    		return Types.VARCHAR;
    	}
    }
    
    /**
     * Executes the query.
     * 
     * @param template The JDBC Template
     * @return The results as a List of Maps
     * @throws SQLException 
     */
    public List<Map<String,Object>> execute(JdbcTemplate template) throws SQLException
    {
    	Fragment sql = assemble(template);
    	if ( logger.isDebugEnabled() ) {
    		logger.debug("Generated SQL: " + sql.getSql());
    	}
    	return ( sql.getParameters().isEmpty() ) ?
    		template.queryForList(sql.getSql()) :
   			template.queryForList(sql.getSql(), sql.getParameters().toArray(),
   				getTypes(sql.getParameters().toArray()));
    }
}
