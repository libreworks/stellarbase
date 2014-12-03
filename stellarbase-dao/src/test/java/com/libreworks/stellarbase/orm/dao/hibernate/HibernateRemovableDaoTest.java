package com.libreworks.stellarbase.orm.dao.hibernate;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Date;

import com.google.common.collect.ImmutableMap;
import com.libreworks.stellarbase.orm.dao.Person;

import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

public class HibernateRemovableDaoTest extends AbstractHibernateTestSupport
{
	@Autowired
	private HibernatePersonDao object;
	
	@Test
	@Transactional(rollbackFor=Throwable.class)
	public void testGetAllNotRemoved()
	{
		Person p1 = getFixture(10);
		Person p2 = getFixture(11);
		Person p3 = getFixture(12);
		object.remove(p2, "foo");
		HibernateTemplate ht = getHibernateTemplate();
		ht.flush();
		ht.refresh(p2);
		Collection<Person> ps = object.getAllNotRemoved();
		assertTrue(ps.contains(p1));
		assertFalse(ps.contains(p2));
		assertTrue(ps.contains(p3));
	}

	@Test
	@Transactional(rollbackFor=Throwable.class)
	public void testGetByNaturalIdNotRemoved()
	{
		Person p = getFixture(2);
		Person entity = object.getByNaturalIdNotRemoved(Restrictions.naturalId().set("username", p.getUsername()));
		assertSame(p, entity);
		object.remove(entity, "me");
		HibernateTemplate ht = getHibernateTemplate();
		ht.flush();
		ht.refresh(entity);
		assertNull(object.getByNaturalIdNotRemoved(Restrictions.naturalId().set("username", entity.getUsername())));
	}

	@Test
	@Transactional(rollbackFor=Throwable.class)
	public void testRemove()
	{
		Person p = getFixture(1);
		assertFalse(p.isRemoved());
		assertNull(p.getRemovedOn());
		assertNull(p.getRemovedBy());
		object.remove(p, "foo");
		HibernateTemplate ht = getHibernateTemplate();
		ht.flush();
		ht.refresh(p);
		assertTrue(p.isRemoved());
		assertNotNull(p.getRemovedOn());
		assertEquals("foo", p.getRemovedBy());
	}
	
	@Test
	@Transactional(rollbackFor=Throwable.class)
	public void testUnremove()
	{
		Person p = getFixture(23);
		assertFalse(p.isRemoved());
		assertNull(p.getRemovedOn());
		assertNull(p.getRemovedBy());
		object.remove(p, "foo");
		HibernateTemplate ht = getHibernateTemplate();
		ht.flush();
		ht.refresh(p);
		assertTrue(p.isRemoved());
		assertNotNull(p.getRemovedOn());
		assertEquals("foo", p.getRemovedBy());
		object.unremove(p, "foo2");
		ht.flush();
		ht.refresh(p);
		assertFalse(p.isRemoved());
		assertNull(p.getRemovedOn());
		assertNull(p.getRemovedBy());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnremoveBad()
	{
		Person p = new Person();
		object.unremove(p, "foo");
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
