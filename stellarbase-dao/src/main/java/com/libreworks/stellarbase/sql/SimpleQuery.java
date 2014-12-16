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
package com.libreworks.stellarbase.sql;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.libreworks.stellarbase.persistence.criteria.Expression;
import com.libreworks.stellarbase.persistence.criteria.Field;
import com.libreworks.stellarbase.persistence.criteria.FieldImpl;
import com.libreworks.stellarbase.persistence.criteria.Junction;
import com.libreworks.stellarbase.persistence.criteria.Order;
import com.libreworks.stellarbase.persistence.criteria.Predicate;
import com.libreworks.stellarbase.persistence.criteria.Projection;
import com.libreworks.stellarbase.persistence.criteria.Query;
import com.libreworks.stellarbase.persistence.criteria.QueryBuilder;
import com.libreworks.stellarbase.persistence.criteria.ValueExpression;
import com.libreworks.stellarbase.text.Characters;

import static com.libreworks.stellarbase.util.Arguments.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * Parameters for a SQL query.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class SimpleQuery implements Query
{
	protected final String from;
	protected final boolean distinct;
	protected final List<Projection<?>> select;
	protected final Predicate where;
	protected final List<Expression<?>> group;
	protected final Predicate having;
	protected final List<Order> order;
	protected final int max;
	protected final int offset;
	
	protected static final Joiner COMMA_JOIN = Joiner.on(", ");
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected SimpleQuery(Builder builder)
	{
		this.from = builder.from;
		this.distinct = builder.distinct;
		this.select = ImmutableList.copyOf(builder.select);
		this.where = builder.where.build();
		
		ImmutableList.Builder<Expression<?>> gb = ImmutableList.builder();
		for (Projection<?> p : builder.select) {
			if (p.isGrouped()) {
				gb.add(p.getExpression());
			}
		}
		this.group = gb.build();
		
		this.having = builder.having.build();
		this.order = ImmutableList.copyOf(builder.orderBy);
		this.max = builder.max;
		this.offset = builder.offset;
	}
	

	@Override
	public int getFirstResult()
	{
		return offset;
	}

	@Override
	public List<Expression<?>> getGroupBy()
	{
		return group;
	}

	@Override
	public Predicate getHaving()
	{
		return having;
	}

	@Override
	public int getMaxResults()
	{
		return max;
	}

	@Override
	public List<Order> getOrderBy()
	{
		return order;
	}

	@Override
	public List<Projection<?>> getSelect()
	{
		return select;
	}

	@Override
	public Predicate getWhere()
	{
		return where;
	}

	@Override
	public boolean isDistinct()
	{
		return distinct;
	}	
	
    /**
     * Gets the SQL query and parameters
     * 
     * @param template The JDBCTemplate
     * @return The SQL fragment
     * @throws SQLException 
     */
    protected Fragment assemble(JdbcTemplate template)
    {
    	StringBuilder sql = new StringBuilder("SELECT ");
    	DatabaseMetaData dbmd = null;
    	java.sql.Connection conn = DataSourceUtils.getConnection(template.getDataSource());
		try {
			dbmd = conn.getMetaData();
		} catch (SQLException e) {
			throw new CannotGetJdbcConnectionException("Could not get database metadata", e);
		}
    	Translator translator = new Translator(dbmd);
    	ArrayList<Object> params = new ArrayList<Object>();
    	boolean quote = true;
    	// get columns
    	if ( select.isEmpty() ) {
    		sql.append(Characters.STAR);
    	} else {
        	ArrayList<String> columns = new ArrayList<String>();
	    	for(Projection<?> projection : select) {
	    		Fragment pf = translator.translateProjection(projection, quote);
	    		params.addAll(pf.getParameters());
	    		StringBuilder fieldSql = new StringBuilder()
	    			.append(pf.getSql());
	    		columns.add(fieldSql.toString());
	    	}
	    	sql.append(COMMA_JOIN.join(columns));
    	}
    	// add from
    	sql.append(" FROM ").append(from);
    	// add WHERE
    	if (where != null) {
    		Fragment whereSql = translator.translateCriterion(where, quote);
    		params.addAll(whereSql.getParameters());
    		sql.append(" WHERE ")
    			.append(whereSql.getSql());
    	}
    	// add group
    	if (!group.isEmpty() ) {
    		sql.append(" GROUP BY ");
    		ArrayList<String> groupSql = new ArrayList<String>(group.size());
    		for (Expression<?> g : group) {
    			Fragment gf = translator.translateExpression(g, quote);
    			params.addAll(gf.getParameters());
    			groupSql.add(gf.getSql());
    		}
    		sql.append(COMMA_JOIN.join(groupSql));
    	}
    	// add having
    	if (!group.isEmpty() && having != null) {
    		Fragment havingSql = translator.translateCriterion(having, quote);
    		params.addAll(havingSql.getParameters());
    		sql.append(" HAVING ")
    			.append(havingSql.getSql());
    	}
    	// add order by
    	if ( !order.isEmpty() ) {
    		sql.append(" ORDER BY ");
    		ArrayList<String> orderSql = new ArrayList<String>(group.size());
    		for (Order o : order) {
    			Fragment of = translator.translateSort(o, quote);
    			params.addAll(of.getParameters());
    			orderSql.add(of.getSql());
    		}
    		sql.append(COMMA_JOIN.join(orderSql));
    	}
    	return new Fragment(sql.toString(), params);
    }
    
    /**
     * Tries to get the JDBC SQL Types based on the objects passed.
     * 
     * @param params The objects to determine their JDBC SQL Type
     * @return The java.sql.Types constants
     */
    public static int[] getSqlTypes(Object[] params)
    {
    	int[] sqlTypes = new int[params.length];
    	for(int i=0; i<params.length; i++){
    		sqlTypes[i] = getSqlType(params[i]);
    	}
    	return sqlTypes;
    }
    
    /**
     * Tries to get the JDBC SQL Type based on an object passed.
     * 
     * @param value The object to determine its JDBC SQL Type
     * @return The java.sql.Types constant
     */
    public static int getSqlType(Object value)
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
     * @throws DataAccessException if something goes wrong with the database 
     */
    public List<Map<String,Object>> execute(JdbcTemplate template)
    {
    	Fragment sql = assemble(template);
    	if ( logger.isDebugEnabled() ) {
    		logger.debug("Generated SQL: " + sql.getSql());
    	}
    	return ( sql.getParameters().isEmpty() ) ?
    		template.queryForList(sql.getSql()) :
   			template.queryForList(sql.getSql(), sql.getParameters().toArray(),
   				getSqlTypes(sql.getParameters().toArray()));
    }

    /**
     * Creates a new builder to create a SimpleQuery.
     *  
     * @return A new builder
     */
    public static Builder builder()
    {
    	return new Builder();
    }
    
    /**
     * Creates a new builder to create a SimpleQuery.
     *  
     * @param from The table from which results are selected
     * @return A new builder
     */
    public static Builder builder(String from)
    {
    	return new Builder(from);
    }
    
    public static class Builder implements QueryBuilder<SimpleQuery.Builder>
    {
    	protected boolean distinct;
    	protected int max = Integer.MAX_VALUE;
    	protected int offset = 0;
    	protected String from;
    	protected final ArrayList<Projection<?>> select = new ArrayList<Projection<?>>();
    	protected final Junction.Builder where = Junction.builder(true);
    	protected final Junction.Builder having = Junction.builder(true);
    	protected final ArrayList<Order> orderBy = new ArrayList<Order>();
    	
    	protected Builder()
    	{
    	}
    	
    	protected Builder(String table)
    	{
    		this.from = checkBlank(table);
    	}
    	
    	protected Predicate toPredicate(String name, Object value)
    	{
    	    if ( value == null ) {
                return toField(name).isNull();
            } else if ( value instanceof Object[] ) {
                return toField(name).in(toExpressions((Object[])value));
            } else if ( value instanceof Collection<?> ) {
                return toField(name).in(toExpressions((Collection<?>)value));
            } else {
                return toField(name).eq(toExpression(value));
            }
    	}
    	
    	protected List<Expression<?>> toExpressions(Collection<?> value)
    	{
    		ArrayList<Expression<?>> expressions = new ArrayList<Expression<?>>(value.size());
    		for (Object o : value) {
    			expressions.add(toExpression(o));
    		}
    		return expressions;
    	}
    	
    	protected List<Expression<?>> toExpressions(Object... value)
    	{
    		ArrayList<Expression<?>> expressions = new ArrayList<Expression<?>>(value.length);
    		for (Object o : value) {
    			expressions.add(toExpression(o));
    		}
    		return expressions;
    	}
    	
    	protected Expression<?> toExpression(Object value)
    	{
    		if (value instanceof Expression) {
    			return (Expression<?>) value;
    		} else {
    			return new ValueExpression<Object>(value);
    		}
    	}
    	
    	protected Field<?> toField(String value)
    	{
   			return new FieldImpl<Object>(checkBlank(value, "Field name cannot be blank"), Object.class);
    	}

    	/**
    	 * Sets the origin table
    	 * 
    	 * @param from The table
    	 * @return provides a fluent interface
    	 */
    	public Builder from(String from)
    	{
    	    this.from = checkBlank(from);
    	    return this;
    	}
    	
    	@Override
		public Builder distinct(boolean distinct)
		{
			this.distinct = distinct;
			return this;
		}

		@Override
		public Builder having(Collection<Expression<Boolean>> predicates)
		{
			having.addAll(checkContainsNull(predicates));
			return this;
		}

		@Override
		public Builder having(Expression<Boolean> predicate)
		{
			having.add(checkNull(predicate));
			return this;
		}
		/**
	     * Adds Criterion to the having clause (field must be an aggregate function)
	     * 
	     * If the value is null, it's the equivalent of {@link Expression#isNull(String)}.
	     * If the value is a Collection or an Object array, it's the equivalent of
	     * {@link Expression#in(String, Collection)}. Otherwise, it's the equivalent
	     * of {@link Expression#eq(String, Object)}.
	     * 
	     * @param name The criteria
	     * @param value The value
	     * @return provides a fluent interface
	     */
	    public Builder having(String name, Object value)
	    {
	        having.add(toPredicate(name, value));
	        return this;
	    }

	    /**
	     * Adds Criterion to the having clause (field must be an aggregate function)
	     *
	     * Each key is the field name, and the value is the Criterion value. See
	     * {@link #having(String, Object)} for more information.
	     * 
	     * @param having
	     * @return provides a fluent interface
	     */
	    public Builder having(Map<String,?> having)
	    {
	        for(Map.Entry<String,?> entry : checkNull(having).entrySet()){
	            this.having.add(toPredicate(entry.getKey(), entry.getValue()));
	        }
	        return this;
	    }
	    
		@Override
		public Builder orderBy(Collection<Order> order)
		{
			orderBy.addAll(checkContainsNull(order));
			return this;
		}

		@Override
		public Builder orderBy(Order... order)
		{
			for (Order o : checkContainsNull(order)) {
				orderBy.add(o);
			}
			return this;
		}

	    /**
	     * Adds an order by clause with ascending fields
	     * 
	     * @param fields The sort clause fields
	     * @return provides a fluent interface
	     */
	    public Builder orderAsc(String... fields)
	    {
	        for(String field : checkNull(fields)){
	            orderBy.add(toField(field).asc());
	        }
	        return this;
	    }
	    
	    /**
	     * Adds an order by clause with descending fields
	     * 
	     * @param fields The sort clause fields
	     * @return provides a fluent interface
	     */
	    public Builder orderDesc(String... fields)
	    {
	        for(String field : checkNull(fields)){
	            orderBy.add(toField(field).desc());
	        }
	        return this;
	    }
		
		@Override
		public Builder select(Collection<Projection<?>> projections)
		{
			select.addAll(checkContainsNull(projections));
			return this;
		}

		@Override
		public Builder select(Projection<?>... projections)
		{
			for (Projection<?> p : checkContainsNull(projections)) {
				select.add(p);
			}
			return this;
		}

		/**
		 * Sets the fields used in the select.
		 * 
		 * @param fields The fields
		 * @return provides a fluent interface
		 */
		public Builder select(String... fields)
		{
		    for(String field : checkNull(fields)){
		        select.add(toField(field).as(null));
		    }
		    return this;
		}
		
		/**
		 * Sets the fields used in the select.
		 * 
		 * @param fields A map whose keys are the alias and values are the names.
		 * @return provides a fluent interface
		 */
		public Builder select(Map<String,String> fields)
		{
		    for(Map.Entry<String,String> entry : checkNull(fields).entrySet()){
		        select.add(toField(entry.getValue()).as(entry.getKey()));
		    }
		    return this;
		}
		
		@Override
		public Builder setFirstResult(int first)
		{
			this.offset = first;
			return this;
		}

		@Override
		public Builder setMaxResults(int max)
		{
			this.max = max;
			return this;
		}

		@Override
		public Builder where(Collection<Expression<Boolean>> predicates)
		{
			where.addAll(checkContainsNull(predicates));
			return this;
		}

		@Override
		public Builder where(Expression<Boolean> predicate)
		{
			where.add(checkNull(predicate));
			return this;
		}

		/**
		 * Adds a criteria to the where clause.
		 * 
		 * If the value is null, it's the equivalent of {@link Expression#isNull(String)}.
		 * If the value is a Collection or an Object array, it's the equivalent of
		 * {@link Expression#in(String, Collection)}. Otherwise, it's the equivalent
		 * of {@link Expression#eq(String, Object)}.
		 * 
		 * @param name The field name
		 * @param value The value
		 * @return provides a fluent interface
		 */
		public Builder where(String name, Object value)
		{
		    where.add(toPredicate(name, value));
	        return this;
		}
		
		/**
		 * Adds criteria to the where clause.
		 * 
		 * Each key is the field name, and the value is the Criterion value. See
		 * {@link #where(String, Object)} for more information. 
		 * 
		 * @param criteria The criteria
		 * @return provides a fluent interface
		 */
		public Builder where(Map<String,?> criteria)
		{
		    for(Map.Entry<String,?> entry : checkNull(criteria).entrySet()) {
		        where.add(toPredicate(entry.getKey(), entry.getValue()));
		    }
		    return this;
		}
		
		@Override
		public Query build()
		{
			if (StringUtils.isBlank(from)) {
				throw new IllegalStateException("You must specify a table");
			}
			return new SimpleQuery(this);
		}
    }
}
