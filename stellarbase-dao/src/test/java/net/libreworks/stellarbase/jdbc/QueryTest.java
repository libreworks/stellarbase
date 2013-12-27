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
package net.libreworks.stellarbase.jdbc;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

import javax.sql.DataSource;

import net.libreworks.stellarbase.jdbc.symbols.Criterion;
import net.libreworks.stellarbase.jdbc.symbols.Expression;
import net.libreworks.stellarbase.jdbc.symbols.Field;
import net.libreworks.stellarbase.jdbc.symbols.Sort;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class QueryTest
{
	private Query object;
	
	@Before
	public void setUp() throws Exception
	{
		object = new Query();
	}

	@Test
	public void testFrom()
	{
		assertSame(object, object.from("foo"));
		assertEquals("foo", object.from);
	}

	public void testFrom2()
	{
	    Query foo = new Query("bar");
	    assertEquals("bar", foo.from);
	}
	
	@Test
	public void testDistinct()
	{
		assertFalse(object.distinct);
		assertSame(object, object.distinct(true));
		assertTrue(object.distinct);
		assertSame(object, object.distinct(false));
		assertFalse(object.distinct);
	}

	@Test
	public void testSelectFieldArray()
	{
		ArrayList<Field> fields = new ArrayList<Field>();
		fields.add(Field.named("foo"));
		fields.add(Field.named("bar"));
		assertSame(object, object.select(fields.toArray(new Field[0])));
		assertTrue(object.select.getSymbols().containsAll(fields));
	}

	@Test
	public void testSelectCollectionOfField()
	{
		ArrayList<Field> fields = new ArrayList<Field>();
		fields.add(Field.named("foo1"));
		fields.add(Field.named("bar1"));
		assertSame(object, object.select(fields));
		assertTrue(object.select.getSymbols().containsAll(fields));
	}

	@Test
	public void testSelectStringVarArgs()
	{
	    assertSame(object, object.select("foo", "bar", "baz"));
	    assertTrue(object.select.getSymbols().contains(Field.named("foo")));
	    assertTrue(object.select.getSymbols().contains(Field.named("bar")));
	    assertTrue(object.select.getSymbols().contains(Field.named("baz")));
	}
	
	@Test
	public void testSelectMapStringObject()
	{
	    HashMap<String,String> fields = new HashMap<String,String>();
	    fields.put("foo", "f");
	    fields.put("bar", "b");
	    assertSame(object, object.select(fields));
	    assertTrue(object.select.getSymbols().contains(Field.named("f", "foo")));
	    assertTrue(object.select.getSymbols().contains(Field.named("b", "bar")));
	}
	
	@Test
	public void testHavingCriterionArray()
	{
		ArrayList<Criterion> having = new ArrayList<Criterion>();
		having.add(Field.max("foo", "f").gt(50));
		having.add(Field.min("bar", "b").lt(50));
		assertSame(object, object.having(having.toArray(new Criterion[0])));
		assertTrue(object.having.getSymbols().containsAll(having));
	}

	@Test
	public void testHavingCollectionOfCriterion()
	{
		ArrayList<Criterion> having = new ArrayList<Criterion>();
		having.add(Field.max("foo", "f").gt(50));
		having.add(Field.min("bar", "b").lt(50));
		assertSame(object, object.having(having));
		assertTrue(object.having.getSymbols().containsAll(having));
	}

	@Test
	public void testWhereCriterionArray()
	{
		ArrayList<Criterion> criteria = new ArrayList<Criterion>();
		criteria.add(Field.named("a").eq("foo"));
		criteria.add(Field.named("b").notLike("foo%"));
		assertSame(object, object.where(criteria.toArray(new Criterion[0])));
		assertTrue(object.where.getSymbols().containsAll(criteria));
	}

	@Test
	public void testWhereCollectionOfCriterion()
	{
		ArrayList<Criterion> criteria = new ArrayList<Criterion>();
		criteria.add(Field.named("a").eq("foo"));
		criteria.add(Field.named("b").notLike("foo%"));
		assertSame(object, object.where(criteria));
		assertTrue(object.where.getSymbols().containsAll(criteria));
	}

	@Test
	public void testOrderBySortArray()
	{
		ArrayList<Sort> sorts = new ArrayList<Sort>();
		sorts.add(Sort.asc("d"));
		sorts.add(Sort.desc("e"));
		assertSame(object, object.orderBy(sorts.toArray(new Sort[0])));
		assertTrue(object.order.getSymbols().containsAll(sorts));
	}

	@Test
	public void testOrderByCollectionOfSort()
	{
		ArrayList<Sort> sorts = new ArrayList<Sort>();
		sorts.add(Sort.asc("d"));
		sorts.add(Sort.desc("e"));
		assertSame(object, object.orderBy(sorts));
		assertTrue(object.order.getSymbols().containsAll(sorts));
	}

	@Test
	public void testOrderAsc()
	{
	    assertSame(object, object.orderAsc("foo", "bar"));
	    assertTrue(object.order.getSymbols().contains(Sort.asc("foo")));
	    assertTrue(object.order.getSymbols().contains(Sort.asc("bar")));
	}
	
	@Test
	public void testOrderDesc()
	{
	    assertSame(object, object.orderDesc("foo", "bar"));
        assertTrue(object.order.getSymbols().contains(Sort.desc("foo")));
        assertTrue(object.order.getSymbols().contains(Sort.desc("bar")));
	}
	
	@Test
	public void testToExpression()
	{
	    assertEquals(Expression.isNull("foo"), object.toExpression("foo", null));
	    assertEquals(Expression.in("bar", new Object[0]), object.toExpression("bar", new Object[0]));
	    assertEquals(Expression.in("baz", Collections.singleton(null)), object.toExpression("baz", Collections.singleton(null)));
	    assertEquals(Expression.eq("foobar", "abc"), object.toExpression("foobar", "abc"));
	}

	@Test
	public void testAssemble2() throws SQLException
	{
		object.from("place")
			.where(Expression.eq("baz", 6))
			.orderBy(Sort.asc("foo"));
		Fragment sql = object.assemble(new JdbcTemplate(){
			@Override
			public DataSource getDataSource()
			{
				return new StubDataSource();
			}
		});
		assertEquals("SELECT * FROM place WHERE \"baz\" = ? ORDER BY \"foo\" ASC", sql.getSql());
		assertArrayEquals(new Object[]{6}, sql.getParameters().toArray());
	}
	
	@Test
	public void testAssemble() throws SQLException
	{
		object.select(Field.group("fuzz", "grp"), Field.named("foo", "f"), Field.named("bar"))
			.from("place")
			.where(Expression.eq("baz", 5))
			.having(Expression.ge(Field.max("test", "maxTest"), "lorem ipsum"))
			.orderBy(Sort.asc("foo"));
		Fragment sql = object.assemble(new JdbcTemplate(){
			@Override
			public DataSource getDataSource()
			{
				return new StubDataSource();
			}
		});
		assertEquals("SELECT \"fuzz\" AS \"grp\", \"foo\" AS \"f\", " +
				"\"bar\" AS \"bar\" FROM place WHERE \"baz\" = ? GROUP BY " +
				"\"fuzz\" HAVING MAX(\"test\") >= ? ORDER BY \"foo\" ASC", sql.getSql());
		assertArrayEquals(new Object[]{5, "lorem ipsum"}, sql.getParameters().toArray());
	}

	@Test
	public void testGetTypes()
	{
		assertArrayEquals(new int[]{Types.INTEGER, Types.VARCHAR, Types.BOOLEAN},
			Query.getSqlTypes(new Object[]{Integer.valueOf(4), "Hey", Boolean.FALSE}));
	}

	@Test
	public void testGetSqlType()
	{
		assertEquals(Types.INTEGER, Query.getSqlType(new Integer(3)));
		assertEquals(Types.SMALLINT, Query.getSqlType(new Short((short)2)));
		assertEquals(Types.BOOLEAN, Query.getSqlType(true));
		assertEquals(Types.NULL, Query.getSqlType(null));
		assertEquals(Types.DATE, Query.getSqlType(new java.sql.Date(System.currentTimeMillis())));
		assertEquals(Types.TIME, Query.getSqlType(new java.sql.Time(System.currentTimeMillis())));
		assertEquals(Types.TIMESTAMP, Query.getSqlType(new java.sql.Timestamp(System.currentTimeMillis())));
		assertEquals(Types.TIMESTAMP, Query.getSqlType(new Date()));
		assertEquals(Types.DECIMAL, Query.getSqlType(new BigDecimal(123.45)));
		assertEquals(Types.BIGINT, Query.getSqlType(1234L));
		assertEquals(Types.BIGINT, Query.getSqlType(new BigInteger("12345")));
		assertEquals(Types.REAL, Query.getSqlType(new Float(12345.0f)));
		assertEquals(Types.DOUBLE, Query.getSqlType(12345.678));
		assertEquals(Types.VARBINARY, Query.getSqlType(new byte[0]));
		assertEquals(Types.VARCHAR, Query.getSqlType("thing"));
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
