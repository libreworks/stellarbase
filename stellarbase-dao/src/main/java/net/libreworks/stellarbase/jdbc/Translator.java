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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.apache.commons.lang.StringUtils;

import net.libreworks.stellarbase.jdbc.symbols.AggregateField;
import net.libreworks.stellarbase.jdbc.symbols.Clause;
import net.libreworks.stellarbase.jdbc.symbols.Criterion;
import net.libreworks.stellarbase.jdbc.symbols.Expression;
import net.libreworks.stellarbase.jdbc.symbols.Field;
import net.libreworks.stellarbase.jdbc.symbols.Junction;
import net.libreworks.stellarbase.jdbc.symbols.Operator;
import net.libreworks.stellarbase.jdbc.symbols.Sort;
import net.libreworks.stellarbase.jdbc.symbols.Symbol;

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
	public static final String QMARK = "?";
	public static final char COMMA = ',';

	/**
	 * Creates a new Translator
	 * 
	 * @param dbMeta
	 *            The database metadata
	 * @throws SQLException
	 *             if a problem occurs using the database metadata
	 */
	public Translator(DatabaseMetaData dbMeta) throws SQLException
	{
		this.idQuote = dbMeta.getIdentifierQuoteString();
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
		this.table = name;
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
		if (symbol instanceof Field) {
			return translateField((Field) symbol, quote);
		} else if (symbol instanceof Criterion) {
			return translateCriterion((Criterion) symbol, quote);
		} else if (symbol instanceof Sort) {
			return translateSort((Sort) symbol, quote);
		} else if (symbol instanceof Clause<?>) {
			return translateClause((Clause<?>) symbol, quote);
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
	public Fragment translateField(Field field, boolean quote)
	{
		String fieldName = quote ? idQuote + field.getName() + idQuote : field
				.getName();
		if (!StringUtils.isBlank(table)) {
			fieldName = table + "." + fieldName;
		}
		return new Fragment(
				field instanceof AggregateField ? ((AggregateField) field)
						.getFunction() + "(" + fieldName + ")" : fieldName);
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
	public Fragment translateSort(Sort sort, boolean quote)
	{
		return new Fragment(translateField(sort.getField(), quote).getSql()
				+ (sort.isAscending() ? " ASC" : " DESC"));
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
	public Fragment translateCriterion(Criterion criterion, boolean quote)
	{
		Fragment fragment = null;
		if (criterion instanceof Expression) {
			fragment = translateExpression((Expression) criterion, quote);
		} else if (criterion instanceof Junction) {
			fragment = translateJunction((Junction) criterion, quote);
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
	public Fragment translateExpression(Expression expression, boolean quote)
	{
		ArrayList<Object> binds = new ArrayList<Object>();
		Object val = expression.getRight();
		Operator o = expression.getOperator();
		StringBuilder sql = new StringBuilder()
				.append(translateField(expression.getLeft(), quote).getSql())
				.append(' ').append(o.getSql()).append(' ');
		if ("NULL".equals(val) || val == null) {
			sql.append("NULL");
		} else if (val instanceof Field) {
			sql.append(translateField((Field) val, quote).getSql());
		} else {
			if (val instanceof Object[]) {
				Object[] vals = (Object[]) val;
				if (Operator.between.equals(o) || Operator.notBetween.equals(o)) {
					sql.append(QMARK).append(" AND ").append(QMARK);
					binds.add(vals[0]);
					binds.add(vals[1]);
				} else if (Operator.in.equals(o) || Operator.notIn.equals(o)) {
					binds.addAll(Arrays.asList(vals));
					String[] qs = new String[vals.length];
					Arrays.fill(qs, QMARK);
					sql.append('(').append(StringUtils.join(qs, COMMA))
							.append(')');
				}
			} else {
				sql.append(QMARK);
				binds.add(val);
			}
		}
		return new Fragment(sql.toString(), binds);
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
	public Fragment translateJunction(Junction junction, boolean quote)
	{
		LinkedHashMap<String,Fragment> criteria = new LinkedHashMap<String,Fragment>();
		for (Criterion c : junction.getSymbols()) {
			Fragment loopToken = translateCriterion(c, quote);
			criteria.put(loopToken.getSql(), loopToken);
		}
		Fragment token = new Fragment("( "
				+ StringUtils.join(criteria.keySet(),
						(junction.isConjunction() ? " AND " : " OR ")) + " )");
		for (Fragment f : criteria.values()) {
			token.addParameters(f);
		}
		return token;
	}

	/**
	 * Translates a clause
	 * 
	 * @param clause
	 *            The clause
	 * @param quote
	 *            Whether to quote identifiers
	 * @return The translated SQL fragment
	 */
	public <T extends Symbol> Fragment translateClause(Clause<T> clause, boolean quote)
	{
		Fragment fragment = null;
		if (clause instanceof Junction) {
			fragment = translateJunction((Junction) clause, quote);
		} else {
			ArrayList<String> sql = new ArrayList<String>();
			ArrayList<Object> binds = new ArrayList<Object>();
			for (T s : clause.getSymbols()) {
				Fragment f = translate(s, quote);
				sql.add(f.getSql());
				binds.addAll(f.getParameters());
			}
			fragment = new Fragment(StringUtils.join(sql, ", "), binds);
		}
		return fragment;
	}
}
