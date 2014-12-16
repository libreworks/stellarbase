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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.libreworks.stellarbase.persistence.criteria.AggregateExpression;
import com.libreworks.stellarbase.persistence.criteria.ComparisonPredicate;
import com.libreworks.stellarbase.persistence.criteria.CountExpression;
import com.libreworks.stellarbase.persistence.criteria.Expression;
import com.libreworks.stellarbase.persistence.criteria.Field;
import com.libreworks.stellarbase.persistence.criteria.InPredicate;
import com.libreworks.stellarbase.persistence.criteria.Junction;
import com.libreworks.stellarbase.persistence.criteria.LikePredicate;
import com.libreworks.stellarbase.persistence.criteria.NullPredicate;
import com.libreworks.stellarbase.persistence.criteria.Order;
import com.libreworks.stellarbase.persistence.criteria.OrderImpl;
import com.libreworks.stellarbase.persistence.criteria.Predicate;
import com.libreworks.stellarbase.persistence.criteria.PredicateClause;
import com.libreworks.stellarbase.persistence.criteria.Projection;
import com.libreworks.stellarbase.persistence.criteria.Symbol;
import com.libreworks.stellarbase.persistence.criteria.ValueExpression;
import com.libreworks.stellarbase.text.Characters;
import com.libreworks.stellarbase.text.Strings;
import com.libreworks.stellarbase.util.Arguments;

/**
 * Turns symbols into a SQL query
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class Translator
{
	protected String table;
	protected String idQuote;
	public static final char LP = '(';
	public static final char RP = ')';
	public static final String NULL = Strings.NULL.toUpperCase();
	protected static final Map<ComparisonPredicate.Operator,String> OPERATORS = ImmutableMap.<ComparisonPredicate.Operator,String>builder()
		.put(ComparisonPredicate.Operator.EQ, " = ")
		.put(ComparisonPredicate.Operator.GE, " >= ")
		.put(ComparisonPredicate.Operator.GT, " > ")
		.put(ComparisonPredicate.Operator.LE, " <= ")
		.put(ComparisonPredicate.Operator.LT, " < ")
		.put(ComparisonPredicate.Operator.NE, " <> ")
		.build();

	/**
	 * Creates a new Translator
	 * 
	 * @param dbMeta
	 *            The database metadata
	 * @throws DataAccessException
	 *             if a problem occurs using the database metadata
	 */
	public Translator(DatabaseMetaData dbMeta) throws DataAccessException
	{
		try {
			this.idQuote = dbMeta.getIdentifierQuoteString();
		} catch (SQLException e) {
			throw new UncategorizedSQLException("getIdentifierQuoteString", "", e);
		}
	}

	/**
	 * Sets the table name to be used during translation
	 * 
	 * @param name
	 *            The table name
	 * @return provides a fluent interface
	 */
	public Translator setTable(String name)
	{
		this.table = Arguments.checkBlank(name);
		return this;
	}

	/**
	 * Translates a Symbol into SQL
	 * 
	 * @param symbol
	 *            The Symbol
	 * @param quote
	 *            Whether to quote identifiers
	 * @return The translated SQL fragment
	 */
	public Fragment translate(Symbol symbol, boolean quote)
	{
		if (symbol instanceof Expression<?>) {
			return translateExpression((Expression<?>) symbol, quote);
		} else if (symbol instanceof Order) {
			return translateSort((Order) symbol, quote);
		} else if (symbol instanceof Projection<?>) {
			return translateProjection((Projection<?>) symbol, quote);
		}
		return null;
	}

	/**
	 * Translates a Field into SQL
	 * 
	 * @param field
	 *            The field
	 * @param quote
	 *            Whether to quote identifiers
	 * @return The translated SQL fragment
	 */
	public Fragment translateField(Field<?> field, boolean quote)
	{
		String fieldName = quote ? idQuote + field.getName() + idQuote : field.getName();
		if (table != null) {
			fieldName = table + Strings.DOT + fieldName;
		}
		return new Fragment(fieldName);
	}
	
	protected Fragment translateValue(ValueExpression<?> expression)
	{
		return new Fragment(Strings.QUESTION, ImmutableList.of(((ValueExpression<?>)expression).get()));
	}
	
	/**
	 * Translates a Sort into SQL
	 * 
	 * @param sort
	 *            The Sort
	 * @param quote
	 *            Whether to quote identifiers
	 * @return The translated SQL fragment
	 */
	public Fragment translateSort(Order sort, boolean quote)
	{
		Fragment expf = translateExpression(sort.getExpression(), quote);
		return new Fragment(expf.getSql()
		    + (sort.isAscending() ? OrderImpl.ASC : OrderImpl.DESC),
		    expf.getParameters());
	}

	/**
	 * Translates a Criterion into SQL
	 * 
	 * @param criterion
	 *            The Criterion
	 * @param quote
	 *            Whether to quote identifiers
	 * @return The translated SQL fragment
	 */
	public Fragment translateCriterion(Expression<Boolean> criterion, boolean quote)
	{
		Fragment fragment = null;
		if (criterion instanceof PredicateClause) {
			fragment = translateJunction((PredicateClause) criterion, quote);
		} else if (criterion instanceof Predicate){
			fragment = translatePredicate((Predicate) criterion, quote);
		} else if (criterion instanceof ValueExpression<?>) {
			return translateValue((ValueExpression<Boolean>) criterion);
		}
		return fragment;
	}

	/**
	 * Translates an Expression into SQL
	 * 
	 * @param expression
	 *            The Expression
	 * @param quote
	 *            Whether to quote identifiers
	 * @return The translated SQL fragment
	 */
	public Fragment translateExpression(Expression<?> expression, boolean quote)
	{
		if (expression instanceof Predicate) {
			return translatePredicate((Predicate) expression, quote);
		} else if (expression instanceof Field<?>) {
			return translateField((Field<?>) expression, quote);
		} else if (expression instanceof ValueExpression<?>) {
			return translateValue((ValueExpression<?>) expression);
		} else if (expression instanceof AggregateExpression<?>) {
			AggregateExpression<?> agg = (AggregateExpression<?>) expression;
			Fragment f = translateExpression(agg.getArgument(), quote);
			StringBuilder sql = new StringBuilder(agg.getFunction().name())
				.append(LP).append(f.getSql()).append(RP);
			return new Fragment(sql.toString(), f.getParameters());
		} else if (expression instanceof CountExpression) {
			CountExpression count = (CountExpression) expression;
			StringBuilder sql = new StringBuilder(CountExpression.COUNT).append(LP);
			Fragment f = null;
			if (count.getArgument() != null) {
				if (count.isDistinct()) {
					sql.append(CountExpression.DISTINCT);
				}
				f = translateExpression(count.getArgument(), quote);
				sql.append(f.getSql()).append(RP);
			} else {
				sql.append(Characters.STAR).append(RP);
			}
			return f == null ? new Fragment(sql.toString()) :
				new Fragment(sql.toString(), f.getParameters());
		}
		return null;
	}

	/**
	 * Translates a Predicate into SQL
	 * 
	 * @param predicate
	 *            The Predicate
	 * @param quote
	 *            Whether to quote identifiers
	 * @return The translated SQL fragment
	 */
	public Fragment translatePredicate(Predicate predicate, boolean quote)
	{
		if (predicate instanceof PredicateClause) {
			return translateJunction((PredicateClause) predicate, quote);
		}
		ArrayList<Object> binds = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		if (predicate instanceof ComparisonPredicate) {
			ComparisonPredicate cp = (ComparisonPredicate) predicate;
			Fragment fa = translateExpression(cp.getA(), quote);
			Fragment fb = translateExpression(cp.getB(), quote);
			binds.addAll(fa.getParameters());
			binds.addAll(fb.getParameters());
			if (ComparisonPredicate.Operator.BETWEEN == cp.getOperator()) {
				sql.append(fa.getSql());
				if (cp.isNegated()) {
					sql.append(" NOT");
				}
				Fragment fc = translateExpression(cp.getC(), quote);
				binds.addAll(fc.getParameters());
				sql.append(" BETWEEN ").append(fb.getSql()).append(" AND ")
					.append(fc.getSql());
			} else {
				if (cp.isNegated()) {
					sql.append("NOT(");
				}
				sql.append(fa.getSql()).append(OPERATORS.get(cp.getOperator()))
					.append(fb.getSql());
				if (cp.isNegated()) {
					sql.append(RP);
				}			
			}
		} else if (predicate instanceof NullPredicate) {
			NullPredicate np = (NullPredicate) predicate;
			Fragment f = translateExpression(np.getInner(), quote);
			sql.append(f.getSql()).append(" IS ");
			if (np.isNegated()) {
				sql.append("NOT ");
			}
			sql.append(NULL);
			binds.addAll(f.getParameters());
		} else if (predicate instanceof InPredicate) {
			InPredicate ip = (InPredicate) predicate;
			Fragment f = translateExpression(ip.getInner(), quote);
			binds.addAll(f.getParameters());
			sql.append(f.getSql());
			if (ip.isNegated()) {
				sql.append(" NOT");
			}
			sql.append(" IN (");
			LinkedList<String> values = new LinkedList<String>();
			for (Expression<?> ex : ip.getValues()) {
				Fragment exf = translateExpression(ex, quote);
				values.add(exf.getSql());
				binds.addAll(exf.getParameters());
			}
			sql.append(Joiner.on(", ").join(values)).append(RP);
		} else if (predicate instanceof LikePredicate) {
			LikePredicate lp = (LikePredicate) predicate;
			Fragment lhs = translateExpression(lp.getInner(), quote);
			binds.addAll(lhs.getParameters());
			sql.append(lhs.getSql());
			if (lp.isNegated()) {
				sql.append(" NOT");
			}
			sql.append(" LIKE ");
			Fragment rhs = translateExpression(lp.getPattern(), quote);
			binds.addAll(rhs.getParameters());
			sql.append(rhs.getSql());
		}
		return new Fragment(sql.toString(), binds);
	}

	/**
	 * Translates a Projection into SQL.
	 * 
	 * @param projection
	 *            The Projection
	 * @param quote
	 *            Whether to quote identifiers
	 * @return The translated SQL fragment
	 */
	public Fragment translateProjection(Projection<?> projection, boolean quote)
	{
		StringBuilder sql = new StringBuilder();
		Fragment f = translateExpression(projection.getExpression(), quote);
		sql.append(f.getSql());
		if (projection.getAlias() != null) {
			sql.append(" AS ").append(idQuote).append(projection.getAlias())
				.append(idQuote);
		}
		return new Fragment(sql.toString(), f.getParameters());
	}
	
	/**
	 * Translates a Junction into SQL
	 * 
	 * @param junction
	 *            The Junction
	 * @param quote
	 *            Whether to quote identifiers
	 * @return The translated SQL fragment
	 */
	public Fragment translateJunction(PredicateClause junction, boolean quote)
	{
		LinkedHashMap<String,Fragment> criteria = new LinkedHashMap<String,Fragment>();
		int params = 0;
		for (Expression<Boolean> c : junction.getSymbols()) {
			Fragment loopToken = translateCriterion(c, quote);
			params += loopToken.getParameters().size();
			criteria.put(loopToken.getSql(), loopToken);
		}
		ArrayList<Object> allParams = new ArrayList<Object>(params);
		for (Fragment f : criteria.values()) {
			allParams.addAll(f.getParameters());
		}
		return new Fragment(new StringBuilder().append(LP)
		    .append(Joiner.on(junction.isConjunction() ? Junction.AND : Junction.OR)
		    	.join(criteria.keySet()))
			.append(RP).toString(), allParams);
	}
}
