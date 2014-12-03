package com.libreworks.stellarbase.orm.dao.hibernate;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.libreworks.stellarbase.orm.dao.Person;

import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

public class HibernateReadableDaoTest extends AbstractHibernateTestSupport
{
	@Autowired
	private HibernatePersonDao object;
	
	@Test
	@Transactional(rollbackFor=Throwable.class)
	public void testConvert()
	{
		Person p = getFixture(4);
		assertSame(p, object.convert(p.getId()));
	}

	@Test(expected=IllegalArgumentException.class)
	@Transactional(rollbackFor=Throwable.class)
	public void testConvertBad()
	{
		object.convert(50L);
	}
	
	@Test
	@Transactional(rollbackFor=Throwable.class)
	public void testFind()
	{
		Person p = getFixture(6);
		assertSame(p, object.find(ImmutableMap.of("username", p.getUsername())));
	}

	@Test
	@Transactional(rollbackFor=Throwable.class)
	public void testFindAll()
	{
		Date now = new Date();
		String by = "foobar1";
		Map<String,?> base = ImmutableMap.<String,Object>builder()
			.put("lastName", "Jones")
			.put("createdOn", now)
			.put("createdBy", by)
			.put("modifiedBy", by)
			.put("modifiedOn", now).build();
		Person p1 = create(Person.class, ImmutableMap.<String,Object>builder()
			.put("username", "indy")
			.put("firstName", "Indiana")
			.putAll(base).build());
		Person p2 = create(Person.class, ImmutableMap.<String,Object>builder()
			.put("username", "davy")
			.put("firstName", "Davy")
			.putAll(base).build());
		Collection<Person> all = object.findAll(ImmutableMap.of("lastName", "Jones"));
		assertTrue(all.contains(p1));
		assertTrue(all.contains(p2));
	}

	@Test
	@Transactional(rollbackFor=Throwable.class)
	public void testGetAll()
	{
		getFixture(10);
		getFixture(11);
		getFixture(12);
		assertEquals(3, object.getAll().size());
	}

	@Test
	@Transactional(rollbackFor=Throwable.class)
	public void testGetById()
	{
		Person p = getFixture(3);
		assertSame(p, object.getById(p.getId()));
		assertNull(object.getById(60L));
	}

	@Test
	@Transactional(rollbackFor=Throwable.class)
	public void testGetByIds()
	{
		Person p1 = getFixture(20);
		Person p2 = getFixture(21);
		Person p3 = getFixture(22);
		Collection<Person> ps = object.getByIds(Arrays.asList(p1.getId(), p2.getId(), p3.getId()));
		assertTrue(ps.contains(p1));
		assertTrue(ps.contains(p2));
		assertTrue(ps.contains(p3));
	}

	@Test
	@Transactional(rollbackFor=Throwable.class)
	public void testGetByNaturalId()
	{
		Person p = getFixture(5);
		assertSame(p, object.getByNaturalId(Restrictions.naturalId().set("username", p.getUsername())));
	}

	@Test
	public void testGetEntityClass()
	{
		assertEquals(Person.class, object.getEntityClass());
	}

	@Test
	public void testGetIdentifierClass()
	{
		assertEquals(Long.class, object.getIdentifierClass());
	}

	@Test
	public void testGetPropertyValues()
	{
		Calendar c = Calendar.getInstance();
		c.set(2010, 1, 19, 0, 0, 0);
		Map<String,?> vals = ImmutableMap.<String,Object>builder()
			.put("username", "foobar")
			.put("firstName", "Bruce")
			.put("mi", new Character('?'))
			.put("lastName", "Wayne")
			.put("bio", "The batman")
			.put("birthday", c.getTime())
			.put("admin", Boolean.TRUE)
			.build();
		Person p = new Person();
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(p);
		bw.setPropertyValues(vals);
		PropertyValues pv = object.getPropertyValues(p);
		assertNull(pv.getPropertyValue("fullName"));
		for(PropertyValue val : pv.getPropertyValues()) {
			assertEquals(val.getValue(), vals.get(val.getName()));
		}
	}

	@Test
	@Transactional(rollbackFor=Throwable.class)
	public void testLoadById()
	{
		Person p = getFixture(2);
		assertEquals(p, object.loadById(p.getId()));
	}
	
	@Test(expected=DataAccessException.class)
	@Transactional(rollbackFor=Throwable.class)
	public void testLoadByIdBad()
	{
		object.loadById(40L);
	}

	@Test
	public void testMapToCriterion()
	{
		Conjunction a = new Conjunction();
		a.add(Restrictions.isNull("foo"));
		assertEquals(a.toString(), object.mapToCriterion(Collections.singletonMap("foo", null)).toString());
		Conjunction b = new Conjunction();
		b.add(Restrictions.eq("bar", 7));
		assertEquals(b.toString(), object.mapToCriterion(ImmutableMap.of("bar", 7)).toString());
		String[] vals = new String[]{"A", "B", "C"};
		Conjunction c = new Conjunction();
		c.add(Restrictions.in("baz", vals));
		assertEquals(c.toString(), object.mapToCriterion(ImmutableMap.of("baz", vals)).toString());
		Conjunction d = new Conjunction();
		d.add(Restrictions.in("the", Arrays.asList(vals)));
		assertEquals(d.toString(), object.mapToCriterion(ImmutableMap.of("the", vals)).toString());
	}

	@Test
	@Transactional(rollbackFor=Throwable.class)
	public void testRefresh()
	{
		Person entity = getFixture(1);
		String name = "fred";
		entity.setFirstName(name);
		assertEquals(name, entity.getFirstName());
		object.refresh(entity);
		assertNull(entity.getFirstName());
	}
	
	public Person getFixture(int seed)
	{
		return create(Person.class, ImmutableMap.<String,Object>builder()
			.put("username", "foobar" + seed)
			.put("createdOn", new Date())
			.put("createdBy", "foobar")
			.put("modifiedOn", new Date())
			.put("modifiedBy", "foobar").build());
	}
}
