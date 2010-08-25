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
package net.libreworks.stellarbase.orm.dao.hibernate;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.libreworks.stellarbase.orm.dao.ReadableDao;
import net.libreworks.stellarbase.orm.model.Identifiable;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.NaturalIdentifier;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.Type;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.PropertyValues;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Abstract base data access object for Hibernate.
 * 
 * @author Jonathan Hawk
 * @param <T> The entity type
 * @param <K> The identifier type
 * @version $Id$
 */
public abstract class AbstractHibernateDao<T extends Identifiable<K>,K extends Serializable> extends HibernateDaoSupport implements ReadableDao<T,K>
{
	protected ApplicationEventMulticaster eventMulticaster;
	protected Class<T> entityClass;
	protected Class<T> identifierClass;
	protected List<String> propertyNames = new ArrayList<String>();
	protected List<String> naturalIdProperties = new ArrayList<String>();
	protected boolean hasNaturalId = false;
	protected List<Type> propertyTypes = new ArrayList<Type>();
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	public T convert(K source)
    {
        T entity = getById(source);
        if ( entity == null ) {
            throw new IllegalArgumentException("Entity "
                    + entityClass.getName() + " not found with identifier: "
                    + source);
        }
        return entity;
    }

    /*
	 * (non-Javadoc)
	 * @see net.libreworks.stellarbase.orm.model.EntityRepository#find(java.util.Map)
	 */
	public T find(final Map<String,?> fields)
	{
		return getHibernateTemplate().execute(new HibernateCallback<T>()
		{
			@SuppressWarnings("unchecked")
			public T doInHibernate(Session session) throws HibernateException, SQLException
			{
				return (T) session.createCriteria(entityClass).add(
				    mapToCriterion(fields)).uniqueResult();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see net.libreworks.stellarbase.orm.model.EntityRepository#findAll(java.util.Map)
	 */
	public List<T> findAll(final Map<String,?> fields)
	{
		return getHibernateTemplate().execute(new HibernateCallback<List<T>>()
		{
			@SuppressWarnings("unchecked")
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException
			{
				return session.createCriteria(entityClass).add(
				    mapToCriterion(fields)).list();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see net.libreworks.stellarbase.orm.model.EntityRepository#getAll()
	 */
	public List<T> getAll()
	{
		return getHibernateTemplate().loadAll(entityClass);
	}

	/*
	 * (non-Javadoc)
	 * @see net.libreworks.stellarbase.orm.model.EntityRepository#getById(java.io.Serializable)
	 */
	public T getById(K id)
	{
		return getHibernateTemplate().get(entityClass, id);
	}

	/*
	 * (non-Javadoc)
	 * @see net.libreworks.stellarbase.orm.model.EntityRepository#getByIds(java.util.Collection)
	 */
	public List<T> getByIds(final Collection<K> ids)
	{
		if ( ids == null || ids.isEmpty() ) {
			return Collections.emptyList();
		}
		return getHibernateTemplate().execute(new HibernateCallback<List<T>>()
		{
			@SuppressWarnings("unchecked")
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException
			{
				return session.createCriteria(entityClass).add(
				    Restrictions.in("id", ids)).setCacheable(true).list();
			}
		});
	}

	/**
	 * Gets an entity by Natural Identifier.
	 * 
	 * @param naturalId
	 * @return The entity found or null
	 */
	public T getByNaturalId(final NaturalIdentifier naturalId)
	{
		return getHibernateTemplate().execute(new HibernateCallback<T>()
		{
			@SuppressWarnings("unchecked")
			public T doInHibernate(Session session) throws HibernateException, SQLException
			{
				return (T) session.createCriteria(entityClass).add(naturalId)
					.setCacheable(true).uniqueResult();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see net.libreworks.stellarbase.orm.model.EntityRepository#getEntityClass()
	 */
	public Class<T> getEntityClass()
	{
		return entityClass;
	}

	/*
	 * (non-Javadoc)
	 * @see net.libreworks.stellarbase.orm.model.EntityRepository#getIdentifierClass()
	 */
	public Class<T> getIdentifierClass()
    {
        return identifierClass;
    }

	/**
	 * Turns a Hibernate entity into PropertyValues
	 * 
	 * @param entity The entity
	 * @return The PropertyValues for the entity
	 */
	protected PropertyValues getPropertyValues(Identifiable<?> entity)
	{
		if ( entity instanceof HibernateProxy ) {
			getHibernateTemplate().initialize(entity);
		}
		MutablePropertyValues mpv = new MutablePropertyValues();
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(entity);
		for(PropertyDescriptor pd : bw.getPropertyDescriptors()) {
			if ( !Collection.class.isAssignableFrom(pd.getPropertyType()) &&
				!Map.class.isAssignableFrom(pd.getPropertyType())) {
				String name = pd.getName();
				mpv.add(name, bw.getPropertyValue(name));
			}
		}
		return mpv;
	}

    @SuppressWarnings("unchecked")
	@Override
	protected void initDao() throws Exception
	{
		entityClass = (Class<T>) ((ParameterizedType) getClass()
		    .getGenericSuperclass()).getActualTypeArguments()[0];
		ClassMetadata meta = getHibernateTemplate().getSessionFactory().getClassMetadata(entityClass);
		identifierClass = meta.getIdentifierType().getReturnedClass();
		propertyNames.addAll(Arrays.asList(meta.getPropertyNames()));
		propertyTypes.addAll(Arrays.asList(meta.getPropertyTypes()));
		hasNaturalId = meta.hasNaturalIdentifier();
		if ( hasNaturalId ) {
			for(int prop : meta.getNaturalIdentifierProperties()) {
				naturalIdProperties.add(propertyNames.get(prop));
			}
		}
	}

    /*
     * (non-Javadoc)
     * @see net.libreworks.stellarbase.orm.model.EntityRepository#loadById(java.io.Serializable)
     */
	public T loadById(K id)
	{
		HibernateTemplate ht = getHibernateTemplate();
		T entity = ht.load(entityClass, id);
		ht.initialize(entity);
		return entity;
	}
	
	/**
	 * Turns a map of key value pairs into a DetachedCriteria. Null values will
	 * be turned into "IS NULL", Collections and arrays will be turned into
	 * "IN", and everything else will be an "=".
	 * 
	 * @param values The values to convert into
	 * @return The criteria constructed.
	 */
	protected Criterion mapToCriterion(Map<String,?> values)
	{
		Conjunction crits = new Conjunction();
		for (Map.Entry<String,?> entry : values.entrySet()) {
			if (entry.getValue() instanceof Collection<?>) {
				crits.add(Restrictions.in(entry.getKey(), (Collection<?>) entry
				    .getValue()));
			} else if (entry.getValue() instanceof Object[]) {
				crits.add(Restrictions.in(entry.getKey(), (Object[]) entry
				    .getValue()));
			} else if (entry.getValue() == null) {
				crits.add(Restrictions.isNull(entry.getKey()));
			} else {
				crits.add(Restrictions.eq(entry.getKey(), entry.getValue()));
			}
		}
		return crits;
	}

	/*
	 * (non-Javadoc)
	 * @see net.libreworks.stellarbase.orm.model.EntityRepository#refresh(net.libreworks.stellarbase.orm.model.Identifiable)
	 */
	public void refresh(T entity)
	{
		getHibernateTemplate().refresh(entity);
	}
	
	/**
	 * @param eventMulticaster the eventMulticaster to set
	 */
	public void setEventMulticaster(ApplicationEventMulticaster eventMulticaster)
	{
		this.eventMulticaster = eventMulticaster;
	}
}
