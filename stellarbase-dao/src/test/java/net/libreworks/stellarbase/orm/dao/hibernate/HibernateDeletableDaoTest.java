package net.libreworks.stellarbase.orm.dao.hibernate;

import static org.junit.Assert.*;

import java.util.Date;

import net.libreworks.stellarbase.collections.FluentValues;
import net.libreworks.stellarbase.orm.dao.Thread;
import net.libreworks.stellarbase.orm.dao.ThreadDao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

public class HibernateDeletableDaoTest extends AbstractHibernateTestSupport
{
	@Autowired
	private ThreadDao object;
	
	@Test
	@Transactional(rollbackFor=Throwable.class)
	public void testDelete()
	{
		Thread entity = create(Thread.class, new FluentValues()
			.set("title", "An example title")
			.set("createdOn", new Date())
			.set("createdBy", "foo")
			.set("modifiedOn", new Date())
			.set("modifiedBy", "foo"));
		assertNotNull(entity.getId());
		HibernateTemplate ht = getHibernateTemplate();
		assertTrue(ht.contains(entity));
		object.delete(entity, "foo1");
		ht.flush();
		assertFalse(ht.contains(entity));
		assertNull(ht.get(Thread.class, entity.getId()));
	}
}
