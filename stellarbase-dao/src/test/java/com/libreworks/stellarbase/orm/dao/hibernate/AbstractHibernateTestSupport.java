package com.libreworks.stellarbase.orm.dao.hibernate;

import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/libreworks/stellarbase/orm/dao/hibernate/hibernate-context.xml")
public abstract class AbstractHibernateTestSupport
{
	@Autowired
	protected LocalSessionFactoryBean localSessionFactoryBean;
	@Autowired
	protected SessionFactory sessionFactory;
	private HibernateTemplate hibernateTemplate;
	
	/**
	 * Creates the database schema
	 */
	@BeforeTransaction
	public void setUpTransaction()
	{
		SchemaExport export = new SchemaExport(localSessionFactoryBean.getConfiguration());
	    export.create(false, true);
	}

	/**
	 * Drops the database schema
	 */
	@AfterTransaction
	public void tearDownTransaction()
	{
		SchemaExport export = new SchemaExport(localSessionFactoryBean.getConfiguration());
	    export.drop(false, true);
	}

	/**
	 * Convenience method to persist an instance of the class with the values.
	 * 
	 * @param <K>
	 *            The type of the entity identifier
	 * @param entityClass
	 *            The entity class
	 * @param values
	 *            The values
	 * @return The ID of the persisted entity
	 */
	protected <T> T create(Class<T> entityClass, Map<String,?> values)
	{
		HibernateTemplate ht = getHibernateTemplate();
		T entity = BeanUtils.instantiate(entityClass);
		PropertyAccessorFactory.forBeanPropertyAccess(entity)
				.setPropertyValues(values);
		ht.save(entity);
		ht.flush();
		return entity;
	}

	/**
	 * Gets a HibernateTemplate for the current sessionFactory.
	 * 
	 * @return The HibernateTemplate
	 */
	public HibernateTemplate getHibernateTemplate()
	{
		if (hibernateTemplate == null) {
			hibernateTemplate = new HibernateTemplate(sessionFactory);
		}
		return hibernateTemplate;
	}
}
