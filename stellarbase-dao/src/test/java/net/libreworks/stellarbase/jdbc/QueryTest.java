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
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import net.libreworks.stellarbase.jdbc.symbols.Criterion;
import net.libreworks.stellarbase.jdbc.symbols.Expression;
import net.libreworks.stellarbase.jdbc.symbols.Field;
import net.libreworks.stellarbase.jdbc.symbols.Sort;

import org.hsqldb.Types;
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
				object.getTypes(new Object[]{Integer.valueOf(4), "Hey", Boolean.FALSE}));
	}

	@Test
	public void testGetSqlType()
	{
		assertEquals(Types.INTEGER, object.getSqlType(new Integer(3)));
		assertEquals(Types.SMALLINT, object.getSqlType(new Short((short)2)));
		assertEquals(Types.BOOLEAN, object.getSqlType(true));
		assertEquals(Types.NULL, object.getSqlType(null));
		assertEquals(Types.DATE, object.getSqlType(new java.sql.Date(System.currentTimeMillis())));
		assertEquals(Types.TIME, object.getSqlType(new java.sql.Time(System.currentTimeMillis())));
		assertEquals(Types.TIMESTAMP, object.getSqlType(new java.sql.Timestamp(System.currentTimeMillis())));
		assertEquals(Types.TIMESTAMP, object.getSqlType(new Date()));
		assertEquals(Types.DECIMAL, object.getSqlType(new BigDecimal(123.45)));
		assertEquals(Types.BIGINT, object.getSqlType(1234L));
		assertEquals(Types.BIGINT, object.getSqlType(new BigInteger("12345")));
		assertEquals(Types.REAL, object.getSqlType(new Float(12345.0f)));
		assertEquals(Types.DOUBLE, object.getSqlType(12345.678));
		assertEquals(Types.VARBINARY, object.getSqlType(new byte[0]));
		assertEquals(Types.VARCHAR, object.getSqlType("thing"));
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
			};
		}
	};
}