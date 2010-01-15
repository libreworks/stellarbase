/**
 * Copyright 2010 LibreWorks contributors
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
package net.libreworks.stellarbase.dao.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.PropertyValues;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import net.libreworks.stellarbase.context.InsertEvent;
import net.libreworks.stellarbase.context.UpdateEvent;
import net.libreworks.stellarbase.dao.WritableDao;
import net.libreworks.stellarbase.model.Modifiable;

/**
 * Abstract Hibernate DAO for mutable entities.
 * 
 * @author Jonathan Hawk
 * @param <T> The type of entity
 * @param <K> The type of identifier
 * @version $Id$
 */
public abstract class AbstractWritableHibernateDao<T extends Modifiable<K>,K extends Serializable> extends AbstractHibernateDao<T,K> implements WritableDao<T,K>
{
	protected Validator validator;
	
	/*
	 * (non-Javadoc)
	 * @see net.libreworks.stellarbase.dao.WritableDao#canUpdate()
	 */
	public boolean canUpdate()
	{
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.libreworks.stellarbase.dao.WritableDao#create(java.util.Map, java.lang.String)
	 */
	public T create(Map<String,?> values, String by) throws BindException
	{
		T entity = BeanUtils.instantiate(entityClass);
        Date now = new Date();
        entity.setCreatedBy(by);
        entity.setCreatedOn(now);
        entity.setModifiedBy(by);
        entity.setModifiedOn(now);
        doSave(entity, values, by);
        return entity;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.libreworks.stellarbase.dao.WritableDao#update(net.libreworks.stellarbase.model.Modifiable, java.util.Map, java.lang.String)
	 */
	public void update(T entity, Map<String,?> values, String by) throws BindException
    {
		if ( !canUpdate() ) {
			throw new UnsupportedOperationException("This DAO does not support updating");
		}
		doUpdate(entity, values, by);
    }	
	
	protected Serializable doSave(T entity, Map<String,?> values, String by) throws BindException
	{
		doBind(entity, values);
		eventMulticaster.multicastEvent(new InsertEvent(entity, by));
		return getHibernateTemplate().save(entity);
	}
	
	protected void doUpdate(T entity, Map<String,?> values, String by) throws BindException
	{
		PropertyValues old = getPropertyValues(entity);
		entity.setModifiedOn(new Date());
		entity.setModifiedBy(by);
		doBind(entity, values);
		eventMulticaster.multicastEvent(new UpdateEvent(entity, old, by));
	}
	
	/**
	 * Binds the values to the entity provided and invokes the DAO's validator.
	 * 
	 * @param entity The entity to bind
	 * @param values The values to bind
	 * @throws BindException if validation of the entity fails
	 */
	protected void doBind(T entity, Map<String,?> values) throws BindException
	{
		DataBinder binder = new DataBinder(entity);
		registerCustomEditors(binder);
		binder.setValidator(validator);
		MutablePropertyValues mpv = new MutablePropertyValues(values);
		binder.bind(mpv);
		binder.validate();
		BindingResult results = binder.getBindingResult();
		if ( results.hasErrors() ) {
			throw new BindException(results);
		}
	}

	/**
	 * Registers custom property editors with the data binder
	 * 
	 * @param registry The registry to which editors will be added
	 */
	public void registerCustomEditors(PropertyEditorRegistry registry)
	{
	}
	
	/**
	 * Sets the validator this DAO will use when data is bound to an entity.
	 * 
	 * @param validator the validator
	 */
	public void setValidator(Validator validator)
    {
		if (validator == null || !validator.supports(entityClass)) {
			throw new IllegalArgumentException("Validator must support " + entityClass);
		}
    	this.validator = validator;
    }	
}
