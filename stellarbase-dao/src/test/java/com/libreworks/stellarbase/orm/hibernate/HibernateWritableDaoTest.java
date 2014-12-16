package com.libreworks.stellarbase.orm.hibernate;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.libreworks.messageboardsample.dao.Person;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

public class HibernateWritableDaoTest extends AbstractHibernateTestSupport
{
	@Autowired
	private HibernatePersonDao object;
	
	@Test
	public void testCanUpdate()
	{
		assertTrue(object.canUpdate());
	}

	@Test
	@Transactional(rollbackFor=Throwable.class)
	public void testCreate() throws Exception
	{
		Calendar bd = Calendar.getInstance();
		bd.set(2010, 3, 19, 0, 0, 0);
		Map<String,?> values = ImmutableMap.<String,Object>builder()
			.put("username", "doublecompile")
			.put("firstName", "Jonathan")
			.put("mi", new Character('D'))
			.put("lastName", "Hawk")
			.put("birthday", bd.getTime())
			.put("bio", "I am pretty cool")
			.put("admin", Boolean.TRUE)
			.build();
		HibernateTemplate ht = getHibernateTemplate();
		Session s = sessionFactory.getCurrentSession();
		assertNull(s.createCriteria(Person.class)
			.add(Restrictions.eq("username", "doublecompile"))
			.uniqueResult());
		Person entity = object.create(values, "foo");
		assertNotNull(entity);
		assertNotNull(entity.getId());
		assertTrue(ht.contains(entity));
		assertEquals("doublecompile", entity.getUsername());
		assertEquals("Jonathan", entity.getFirstName());
		assertEquals(new Character('D'), entity.getMi());
		assertEquals("Hawk", entity.getLastName());
		assertEquals("I am pretty cool", entity.getBio());
		assertTrue(entity.getAdmin());
		assertEquals(bd.getTimeInMillis(), entity.getBirthday().getTime());
		assertNotNull(entity.getCreatedOn());
		assertNotNull(entity.getModifiedOn());
		assertEquals("foo", entity.getCreatedBy());
		assertEquals("foo", entity.getModifiedBy());
	}
	
	@Test(expected=DataAccessException.class)
	@Transactional(rollbackFor=Throwable.class)
	public void testDuplicateNaturalKey() throws Exception
	{
		Map<String,?> values = ImmutableMap.of("username", "doublecompile1");
		create(Person.class, values);
		object.create(values, "foo1");
	}

	@Test
	public void testCanEnforceNaturalKey()
	{
		assertTrue(object.canEnforceNaturalKey());
	}

	@Test
	@Transactional(rollbackFor=Throwable.class)
	public void testUpdateDisallowedField() throws Exception
	{
		Calendar bd = Calendar.getInstance();
		bd.set(2010, 3, 19, 0, 0, 0);
		Map<String,?> values = ImmutableMap.<String,Object>builder()
			.put("username", "doublecompile")
			.put("firstName", "Jonathan")
			.put("mi", new Character('D'))
			.put("lastName", "Hawk")
			.put("birthday", bd.getTime())
			.put("bio", "I am pretty cool")
			.put("admin", Boolean.TRUE)
			.put("createdOn", new Date())
			.put("modifiedOn", new Date())
			.put("modifiedBy", "foo")
			.put("createdBy", "foo").build();
		Person entity = create(Person.class, values);
		HibernateTemplate ht = getHibernateTemplate();
		ht.refresh(entity);
		Date then = entity.getModifiedOn();
		Integer version = entity.getVersion();
		Map<String,?> values2 = ImmutableMap.of("username", "ANewUsername");
		object.update(entity, values2, "foo1");
		ht.flush();
		ht.refresh(entity);
		assertEquals("doublecompile", entity.getUsername());
		assertEquals("foo1", entity.getModifiedBy());
		assertFalse(entity.getModifiedBy().equals(then));
		assertTrue(entity.getVersion() > version);
	}
	
	@Test
	@Transactional(rollbackFor=Throwable.class)
	public void testUpdate() throws Exception
	{
		Calendar bd = Calendar.getInstance();
		bd.set(2010, 3, 19, 0, 0, 0);
		Map<String,?> values = ImmutableMap.<String,Object>builder()
			.put("username", "doublecompile")
			.put("firstName", "Jonathan")
			.put("mi", new Character('D'))
			.put("lastName", "Hawk")
			.put("birthday", bd.getTime())
			.put("bio", "I am pretty cool")
			.put("admin", Boolean.TRUE)
			.put("createdOn", new Date())
			.put("modifiedOn", new Date())
			.put("modifiedBy", "foo")
			.put("createdBy", "foo").build();
		Person entity = create(Person.class, values);
		HibernateTemplate ht = getHibernateTemplate();
		ht.refresh(entity);
		Date then = entity.getModifiedOn();
		Integer version = entity.getVersion();
		Calendar newBd = Calendar.getInstance();
		newBd.set(2010, 2, 1, 0, 0, 0);
		newBd.set(Calendar.MILLISECOND, 0);
		Map<String,?> values2 = ImmutableMap.<String,Object>builder()
			.put("firstName", "George")
			.put("mi", new Character('O'))
			.put("lastName", "Jungle")
			.put("birthday", newBd.getTime())
			.put("bio", "Hangs around with monkies")
			.put("admin", Boolean.FALSE)
			.build();
		object.update(entity, values2, "foo1");
		ht.flush();
		ht.refresh(entity);
		assertEquals("George", entity.getFirstName());
		assertEquals(new Character('O'), entity.getMi());
		assertEquals("Jungle", entity.getLastName());
		assertEquals(newBd.getTimeInMillis(), entity.getBirthday().getTime());
		assertEquals("Hangs around with monkies", entity.getBio());
		assertEquals(Boolean.FALSE, entity.getAdmin());
		assertEquals("foo1", entity.getModifiedBy());
		assertFalse(entity.getModifiedBy().equals(then));
		assertTrue(entity.getVersion() > version);
	}
}
