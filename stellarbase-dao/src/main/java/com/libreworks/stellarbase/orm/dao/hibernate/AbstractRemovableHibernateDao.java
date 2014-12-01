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
package com.libreworks.stellarbase.orm.dao.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.libreworks.stellarbase.orm.context.DeleteEvent;
import com.libreworks.stellarbase.orm.context.UndeleteEvent;
import com.libreworks.stellarbase.orm.dao.RemovableDao;
import com.libreworks.stellarbase.orm.model.Removable;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.NaturalIdentifier;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;

/**
 * Abstract Hibernate DAO for entities which are removable.
 * 
 * @author Jonathan Hawk
 * @param <T> The type of entity
 * @param <K> The type of identifier
 */
public abstract class AbstractRemovableHibernateDao<T extends Removable<K>,K extends Serializable> extends AbstractWritableHibernateDao<T, K> implements RemovableDao<T, K>
{
    /*
     * (non-Javadoc)
     * @see com.libreworks.stellarbase.orm.dao.RemovableDao#getAllNotRemoved()
     */
	public List<T> getAllNotRemoved()
	{
		return getHibernateTemplate().execute(new HibernateCallback<List<T>>()
		{
			@SuppressWarnings("unchecked")
			public List<T> doInHibernate(Session session) throws HibernateException
			{
				return session.createCriteria(entityClass)
					.add(Restrictions.isNull("removedOn"))
					.list();
			}
		});
	}
	
	/**
	 * Gets a non-removed entity by Natural Identifier.
	 * 
	 * @param naturalId The identifier
	 * @return The entity found or null
	 */
	public T getByNaturalIdNotRemoved(final NaturalIdentifier naturalId)
	{
		return getHibernateTemplate().execute(new HibernateCallback<T>()
		{
			@SuppressWarnings("unchecked")
			public T doInHibernate(Session session) throws HibernateException
			{
				return (T) session.createCriteria(entityClass)
					.add(naturalId).add(Restrictions.isNull("removedOn"))
					.setCacheable(true).uniqueResult();
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.orm.dao.RemovableDao#remove(com.libreworks.stellarbase.orm.model.Removable, java.lang.String)
	 */
	public void remove(T entity, String by)
	{
		eventPublisher.publishEvent(new DeleteEvent(entity, by));
		entity.setRemovedBy(by);
		entity.setRemovedOn(new Date());
		HibernateTemplate ht = getHibernateTemplate();
		if ( !ht.contains(entity) ) {
			ht.saveOrUpdate(entity);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.orm.dao.RemovableDao#unremove(com.libreworks.stellarbase.orm.model.Removable, java.lang.String)
	 */
	public void unremove(T entity, String by)
	{
		if ( entity == null || !entity.isRemoved() ) {
			throw new IllegalArgumentException("Entity must be removed");
		}
		entity.setRemovedOn(null);
		entity.setRemovedBy(null);
		HibernateTemplate ht = getHibernateTemplate();
		if ( !ht.contains(entity) ) {
			ht.saveOrUpdate(entity);
		}
		eventPublisher.publishEvent(new UndeleteEvent(entity, by));
	}
}
