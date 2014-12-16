package com.libreworks.stellarbase.orm.hibernate;

import static org.junit.Assert.*;

import java.util.Date;

import com.libreworks.messageboardsample.dao.Thread;
import com.libreworks.messageboardsample.dao.ThreadDao;
import com.libreworks.stellarbase.collections.Maps;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

public class HibernateDeletableDaoTest extends AbstractHibernateTestSupport
{
	@Autowired
	private ThreadDao object;
	
	@Test
	@Transactional(rollbackFor=Throwable.class)
	public void testDelete()
	{
		Thread entity = create(Thread.class, Maps.<String,Object>newLinked()
			.add("title", "An example title")
			.add("createdOn", new Date())
			.add("createdBy", "foo")
			.add("modifiedOn", new Date())
			.add("modifiedBy", "foo").toMap());
		assertNotNull(entity.getId());
		HibernateTemplate ht = getHibernateTemplate();
		assertTrue(ht.contains(entity));
		object.delete(entity, "foo1");
		ht.flush();
		assertFalse(ht.contains(entity));
		assertNull(ht.get(Thread.class, entity.getId()));
	}
}
