/**
 * Copyright 2011 LibreWorks contributors
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

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.google.common.collect.ImmutableMap;
import com.libreworks.stellarbase.persistence.criteria.Expression;
import com.libreworks.stellarbase.persistence.criteria.FieldImpl;
import com.libreworks.stellarbase.persistence.criteria.Junction;
import com.libreworks.stellarbase.persistence.criteria.Order;
import com.libreworks.stellarbase.persistence.criteria.Projection;
import com.libreworks.stellarbase.persistence.criteria.Query;
import com.libreworks.stellarbase.persistence.criteria.ValueExpression;
import com.libreworks.stellarbase.sql.Fragment;
import com.libreworks.stellarbase.sql.SimpleQuery;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class SimpleQueryTest
{
	private Query object;

	@Test
	public void testFrom()
	{
		String table = "foo";
		object = SimpleQuery.builder(table).build();
		assertEquals(table, ((SimpleQuery)object).from);
	}

	@Test
	public void testFrom2()
	{
		String table = "foo";
		object = SimpleQuery.builder().from(table).build();
		assertEquals(table, ((SimpleQuery)object).from);
	}

	@Test(expected=IllegalStateException.class)
	public void testFrom3()
	{
		object = SimpleQuery.builder().build();
	}
	
	@Test
	public void testDistinct()
	{
		object = SimpleQuery.builder("foo").distinct(true).build();
		assertTrue(object.isDistinct());
		object = SimpleQuery.builder("bar").distinct(false).build();
		assertFalse(object.isDistinct());
	}

	@Test
	public void testSelectFieldArray()
	{
		FieldImpl<?> foo = new FieldImpl<Object>("foo", Object.class);
		FieldImpl<?> bar = new FieldImpl<Object>("bar1", Object.class);

		object = SimpleQuery.builder("test").select(foo.as(null), bar.as(null)).build();
		assertTrue(object.getSelect().contains(foo.as(null)));
		assertTrue(object.getSelect().contains(bar.as(null)));
	}

	@Test
	public void testSelectCollectionOfField()
	{
		FieldImpl<?> foo = new FieldImpl<Object>("foo", Object.class);
		FieldImpl<?> bar = new FieldImpl<Object>("bar", Object.class);
		
		ArrayList<Projection<?>> fields = new ArrayList<Projection<?>>();
		fields.add(foo.as(null));
		fields.add(bar.as(null));
		
		object = SimpleQuery.builder("test").select(fields).build();
		assertTrue(object.getSelect().containsAll(fields));
	}

	@Test
	public void testSelectStringVarArgs()
	{
		FieldImpl<?> foo = new FieldImpl<Object>("foo", Object.class);
		FieldImpl<?> bar = new FieldImpl<Object>("bar", Object.class);
		FieldImpl<?> baz = new FieldImpl<Object>("baz", Object.class);
		
		object = SimpleQuery.builder("test").select("foo", "bar", "baz").build();
		
	    assertTrue(object.getSelect().contains(foo.as(null)));
	    assertTrue(object.getSelect().contains(bar.as(null)));
	    assertTrue(object.getSelect().contains(baz.as(null)));
	}
	
	@Test
	public void testSelectMapStringObject()
	{
		FieldImpl<?> foo = new FieldImpl<Object>("foo", Object.class);
		FieldImpl<?> bar = new FieldImpl<Object>("bar", Object.class);
		
		Map<String,String> fields = ImmutableMap.of("f", "foo", "b", "bar");
	    
		object = SimpleQuery.builder("test").select(fields).build();
		
	    assertTrue(object.getSelect().contains(foo.as("f")));
	    assertTrue(object.getSelect().contains(bar.as("b")));
	}
	
	@Test
	public void testHavingCriterionArray()
	{
		FieldImpl<?> foo = new FieldImpl<Object>("foo", Object.class);
		FieldImpl<?> bar = new FieldImpl<Object>("bar", Object.class);
		
		ArrayList<Expression<Boolean>> having = new ArrayList<Expression<Boolean>>();
		having.add(foo.max().gt(ValueExpression.of(50)));
		having.add(bar.min().lt(ValueExpression.of(50)));
		
		object = SimpleQuery.builder("test").having(Junction.and(having.get(0), having.get(1))).build();
		assertTrue(((Junction)object.getHaving()).getSymbols().containsAll(having));
	}

	@Test
	public void testHavingCollectionOfCriterion()
	{
		FieldImpl<?> foo = new FieldImpl<Object>("foo", Object.class);
		FieldImpl<?> bar = new FieldImpl<Object>("bar", Object.class);
		
		ArrayList<Expression<Boolean>> having = new ArrayList<Expression<Boolean>>();
		having.add(foo.max().gt(ValueExpression.of(50)));
		having.add(bar.min().lt(ValueExpression.of(50)));

		object = SimpleQuery.builder("test").having(having).build();
		
		assertTrue(((Junction)object.getHaving()).getSymbols().containsAll(having));
	}

	@Test
	public void testWhereCriterionArray()
	{
		FieldImpl<?> d = new FieldImpl<Object>("d", Object.class);
		FieldImpl<?> e = new FieldImpl<Object>("e", Object.class);
		
		ArrayList<Expression<Boolean>> criteria = new ArrayList<Expression<Boolean>>();
		criteria.add(d.eq(ValueExpression.of("foo")));
		criteria.add(e.notLike(ValueExpression.of("foo%")));
		
		object = SimpleQuery.builder("test").where(Junction.and(criteria.get(0), criteria.get(1))).build();
		
		assertTrue(((Junction)object.getWhere()).getSymbols().containsAll(criteria));
	}

	@Test
	public void testWhereCollectionOfCriterion()
	{
		FieldImpl<?> d = new FieldImpl<Object>("d", Object.class);
		FieldImpl<?> e = new FieldImpl<Object>("e", Object.class);
		
		ArrayList<Expression<Boolean>> criteria = new ArrayList<Expression<Boolean>>();
		criteria.add(d.eq(ValueExpression.of("foo")));
		criteria.add(e.notLike(ValueExpression.of("foo%")));
		
		object = SimpleQuery.builder("test").where(criteria).build();
		
		assertTrue(((Junction)object.getWhere()).getSymbols().containsAll(criteria));
	}

	@Test
	public void testOrderBySortArray()
	{
		FieldImpl<?> d = new FieldImpl<Object>("d", Object.class);
		FieldImpl<?> e = new FieldImpl<Object>("e", Object.class);
		
		ArrayList<Order> sorts = new ArrayList<Order>();
		sorts.add(d.asc());
		sorts.add(e.desc());
		
		object = SimpleQuery.builder("test").orderBy(sorts.toArray(new Order[0])).build();
		assertTrue(object.getOrderBy().containsAll(sorts));
	}

	@Test
	public void testOrderByCollectionOfSort()
	{
		FieldImpl<?> d = new FieldImpl<Object>("d", Object.class);
		FieldImpl<?> e = new FieldImpl<Object>("e", Object.class);
		
		ArrayList<Order> sorts = new ArrayList<Order>();
		sorts.add(d.asc());
		sorts.add(e.desc());
		
		object = SimpleQuery.builder("test").orderBy(sorts).build();
		
		assertTrue(object.getOrderBy().containsAll(sorts));
	}

	@Test
	public void testOrderAsc()
	{
		FieldImpl<?> foo = new FieldImpl<Object>("foo", Object.class);
		FieldImpl<?> bar = new FieldImpl<Object>("bar", Object.class);
		
		object = SimpleQuery.builder("test").orderAsc("foo", "bar").build();
		
        assertTrue(object.getOrderBy().contains(foo.asc()));
        assertTrue(object.getOrderBy().contains(bar.asc()));
	}
	
	@Test
	public void testOrderDesc()
	{
		FieldImpl<?> foo = new FieldImpl<Object>("foo", Object.class);
		FieldImpl<?> bar = new FieldImpl<Object>("bar", Object.class);
		
		object = SimpleQuery.builder("test").orderDesc("foo", "bar").build();
		
        assertTrue(object.getOrderBy().contains(foo.desc()));
        assertTrue(object.getOrderBy().contains(bar.desc()));
	}
	
	@Test
	public void testToExpression()
	{
		FieldImpl<?> foo = new FieldImpl<Object>("foo", Object.class);
		FieldImpl<?> bar = new FieldImpl<Object>("bar", Object.class);
		FieldImpl<?> baz = new FieldImpl<Object>("baz", Object.class);
		FieldImpl<?> foobar = new FieldImpl<Object>("foobar", Object.class);
		
		SimpleQuery.Builder obj = SimpleQuery.builder(); 
		
	    assertEquals(foo.isNull(), obj.toPredicate("foo", null));
	    assertEquals(bar.in(new Expression<?>[]{ValueExpression.of("hi")}), obj.toPredicate("bar", new Object[]{"hi"}));
	    assertEquals(baz.in(Collections.<Expression<?>>singletonList(ValueExpression.of(1))), obj.toPredicate("baz", Collections.singleton(1)));
	    assertEquals(foobar.eq(ValueExpression.of("abc")), obj.toPredicate("foobar", "abc"));
	}

	@Test
	public void testAssemble2() throws SQLException
	{
		FieldImpl<?> foo = new FieldImpl<Object>("foo", Object.class);
		FieldImpl<?> baz = new FieldImpl<Object>("baz", Object.class);
		
		object = SimpleQuery.builder("place")
			.where(baz.eq(ValueExpression.of(6)))
			.orderBy(foo.asc())
			.build();
		Fragment sql = ((SimpleQuery)object).assemble(new JdbcTemplate(){
			@Override
			public DataSource getDataSource()
			{
				return new StubDataSource();
			}
		});
		assertEquals("SELECT * FROM place WHERE (\"baz\" = ?) ORDER BY \"foo\" ASC", sql.getSql());
		assertArrayEquals(new Object[]{6}, sql.getParameters().toArray());
	}
	
	@Test
	public void testAssemble() throws SQLException
	{
		FieldImpl<?> fuzz = new FieldImpl<Object>("fuzz", Object.class);
		FieldImpl<?> foo = new FieldImpl<Object>("foo", Object.class);		
		FieldImpl<?> bar = new FieldImpl<Object>("bar", Object.class);
		FieldImpl<?> test = new FieldImpl<Object>("test", Object.class);
		
		object = SimpleQuery.builder()
			.select(fuzz.grouped("grp"), foo.as("f"), bar.as(null))
			.from("place")
			.where("baz", 5)
			.having(test.max().ge(ValueExpression.of("lorem ipsum")))
			.orderBy(foo.asc())
			.build();
		Fragment sql = ((SimpleQuery)object).assemble(new JdbcTemplate(){
			@Override
			public DataSource getDataSource()
			{
				return new StubDataSource();
			}
		});
		assertEquals("SELECT \"fuzz\" AS \"grp\", \"foo\" AS \"f\", " +
				"\"bar\" FROM place WHERE (\"baz\" = ?) GROUP BY " +
				"\"fuzz\" HAVING (MAX(\"test\") >= ?) ORDER BY \"foo\" ASC", sql.getSql());
		assertArrayEquals(new Object[]{5, "lorem ipsum"}, sql.getParameters().toArray());
	}

	@Test
	public void testGetTypes()
	{
		assertArrayEquals(new int[]{Types.INTEGER, Types.VARCHAR, Types.BOOLEAN},
			SimpleQuery.getSqlTypes(new Object[]{Integer.valueOf(4), "Hey", Boolean.FALSE}));
	}

	@Test
	public void testGetSqlType()
	{
		assertEquals(Types.INTEGER, SimpleQuery.getSqlType(new Integer(3)));
		assertEquals(Types.SMALLINT, SimpleQuery.getSqlType(new Short((short)2)));
		assertEquals(Types.BOOLEAN, SimpleQuery.getSqlType(true));
		assertEquals(Types.NULL, SimpleQuery.getSqlType(null));
		assertEquals(Types.DATE, SimpleQuery.getSqlType(new java.sql.Date(System.currentTimeMillis())));
		assertEquals(Types.TIME, SimpleQuery.getSqlType(new java.sql.Time(System.currentTimeMillis())));
		assertEquals(Types.TIMESTAMP, SimpleQuery.getSqlType(new java.sql.Timestamp(System.currentTimeMillis())));
		assertEquals(Types.TIMESTAMP, SimpleQuery.getSqlType(new Date()));
		assertEquals(Types.DECIMAL, SimpleQuery.getSqlType(new BigDecimal(123.45)));
		assertEquals(Types.BIGINT, SimpleQuery.getSqlType(1234L));
		assertEquals(Types.BIGINT, SimpleQuery.getSqlType(new BigInteger("12345")));
		assertEquals(Types.REAL, SimpleQuery.getSqlType(new Float(12345.0f)));
		assertEquals(Types.DOUBLE, SimpleQuery.getSqlType(12345.678));
		assertEquals(Types.VARBINARY, SimpleQuery.getSqlType(new byte[0]));
		assertEquals(Types.VARCHAR, SimpleQuery.getSqlType("thing"));
	}
	
	private class StubDataSource implements DataSource
	{
		public <T> T unwrap(Class<T> iface) throws SQLException
		{
			return null;
		}
		
		public boolean isWrapperFor(Class<?> iface) throws SQLException
		{
			return false;
		}
		
		public void setLoginTimeout(int seconds) throws SQLException
		{
		}
		
		public void setLogWriter(PrintWriter out) throws SQLException
		{
		}
		
		public int getLoginTimeout() throws SQLException
		{
			return 0;
		}
		
		public PrintWriter getLogWriter() throws SQLException
		{
			return null;
		}
		
		public Connection getConnection(String username, String password) throws SQLException
		{
			return null;
		}
		
		public Connection getConnection() throws SQLException
		{
			return new Connection()
			{
				
				public <T> T unwrap(Class<T> iface) throws SQLException
				{
					return null;
				}
				
				public boolean isWrapperFor(Class<?> iface) throws SQLException
				{
					return false;
				}
				
				public void setTypeMap(Map<String,Class<?>> map) throws SQLException
				{
				}
				
				public void setTransactionIsolation(int level) throws SQLException
				{
				}
				
				public Savepoint setSavepoint(String name) throws SQLException
				{
					return null;
				}
				
				public Savepoint setSavepoint() throws SQLException
				{
					return null;
				}
				
				public void setReadOnly(boolean readOnly) throws SQLException
				{
				}
				
				public void setHoldability(int holdability) throws SQLException
				{
				}
				
				public void setClientInfo(String name, String value) throws SQLClientInfoException
				{
				}
				
				public void setClientInfo(Properties properties) throws SQLClientInfoException
				{
				}
				
				public void setCatalog(String catalog) throws SQLException
				{
				}
				
				public void setAutoCommit(boolean autoCommit) throws SQLException
				{
				}
				
				public void rollback(Savepoint savepoint) throws SQLException
				{
				}
				
				public void rollback() throws SQLException
				{
				}
				
				public void releaseSavepoint(Savepoint savepoint) throws SQLException
				{
				}
				
				public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
				{
					return null;
				}
				
				public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
				{
					return null;
				}
				
				public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException
				{
					return null;
				}
				
				public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException
				{
					return null;
				}
				
				public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException
				{
					return null;
				}
				
				public PreparedStatement prepareStatement(String sql) throws SQLException
				{
					return null;
				}
				
				public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
				{
					return null;
				}
				
				public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
				{
					return null;
				}
				
				public CallableStatement prepareCall(String sql) throws SQLException
				{
					return null;
				}
				
				public String nativeSQL(String sql) throws SQLException
				{
					return null;
				}
				
				public boolean isValid(int timeout) throws SQLException
				{
					return false;
				}
				
				public boolean isReadOnly() throws SQLException
				{
					return false;
				}
				
				public boolean isClosed() throws SQLException
				{
					return false;
				}
				
				public SQLWarning getWarnings() throws SQLException
				{
					return null;
				}
				
				public Map<String,Class<?>> getTypeMap() throws SQLException
				{
					return null;
				}
				
				public int getTransactionIsolation() throws SQLException
				{
					return 0;
				}
				
				public DatabaseMetaData getMetaData() throws SQLException
				{
					return new StubDatabaseMetadata();
				}
				
				public int getHoldability() throws SQLException
				{
					return 0;
				}
				
				public String getClientInfo(String name) throws SQLException
				{
					return null;
				}
				
				public Properties getClientInfo() throws SQLException
				{
					return null;
				}
				
				public String getCatalog() throws SQLException
				{
					return null;
				}
				
				public boolean getAutoCommit() throws SQLException
				{
					return false;
				}
				
				public Struct createStruct(String typeName, Object[] attributes) throws SQLException
				{
					return null;
				}
				
				public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
				{
					return null;
				}
				
				public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException
				{
					return null;
				}
				
				public Statement createStatement() throws SQLException
				{
					return null;
				}
				
				public SQLXML createSQLXML() throws SQLException
				{
					return null;
				}
				
				public NClob createNClob() throws SQLException
				{
					return null;
				}
				
				public Clob createClob() throws SQLException
				{
					return null;
				}
				
				public Blob createBlob() throws SQLException
				{
					return null;
				}
				
				public Array createArrayOf(String typeName, Object[] elements) throws SQLException
				{
					return null;
				}
				
				public void commit() throws SQLException
				{
				}
				
				public void close() throws SQLException
				{
				}
				
				public void clearWarnings() throws SQLException
				{
				}

				public void setSchema(String schema) throws SQLException {
				}

				public String getSchema() throws SQLException {
					return null;
				}

				public void abort(Executor executor) throws SQLException {
				}

				public void setNetworkTimeout(Executor executor,
						int milliseconds) throws SQLException {
				}

				public int getNetworkTimeout() throws SQLException {
					return 0;
				}
			};
		}

		public Logger getParentLogger() throws SQLFeatureNotSupportedException {
			return null;
		}
	};
}
