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
import java.util.HashMap;
import java.util.Map;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.PropertyValues;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.validation.BeanPropertyBindingResult;
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
	protected boolean hasNaturalId = false;
	
	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	
	@Override
	protected void initDao() throws Exception
	{
		super.initDao();
		// this must be done after we figure out the entity type this dao supports
		if ( validator != null && !validator.supports(entityClass)) {
			throw new IllegalArgumentException("Validator must support " + entityClass);
		}
		hasNaturalId = getHibernateTemplate().getSessionFactory()
			.getClassMetadata(entityClass).hasNaturalIdentifier();
	}
	
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
		enforceNaturalKey(values, null);
		T entity = BeanUtils.instantiate(entityClass);
        Date now = new Date();
        entity.setCreatedBy(by);
        entity.setCreatedOn(now);
        entity.setModifiedBy(by);
        entity.setModifiedOn(now);
        doSave(entity, values, by);
        return entity;
	}
	
	/**
	 * Whether the DAO should preemptively check entity natural id constraints.
	 * 
	 * By default, this is true. The methods create and update will first check
	 * natural id constraints. Note that unique constraints that are not part of
	 * the natural id are <em>not</em> checked by this behavior.
	 * 
	 * @return Whether unique constraints should be checked
	 */
	protected boolean canEnforceNaturalKey()
	{
		return true;
	}
	
	/**
	 * Gets the list of fields allowed for the bind during entity creation.
	 * 
	 * By default, this is all fields. Override this method to provide your own
	 * list of allowed fields.
	 * 
	 * @return The list of allowed create bind fields
	 */
	protected String[] getAllowedCreateFields()
	{
		return EMPTY_STRING_ARRAY;
	}
	
	/**
	 * Gets the list of fields allowed for the bind during entity updating.
	 * 
	 * By default, this is all fields. Override this method to provide your own
	 * list of allowed fields.
	 * 
	 * @return The list of allowed update bind fields
	 */
	protected String[] getAllowedUpdateFields()
	{
		return EMPTY_STRING_ARRAY;
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
		doBind(entity, values, getAllowedCreateFields());
		eventMulticaster.multicastEvent(new InsertEvent(entity, by));
		return getHibernateTemplate().save(entity);
	}
	
	protected void doUpdate(T entity, Map<String,?> values, String by) throws BindException
	{
		enforceNaturalKey(values, entity);
		PropertyValues old = getPropertyValues(entity);
		entity.setModifiedOn(new Date());
		entity.setModifiedBy(by);
		doBind(entity, values, getAllowedUpdateFields());
		HibernateTemplate ht = getHibernateTemplate();
		if ( !ht.contains(entity) ) {
			ht.saveOrUpdate(entity);
		}
		eventMulticaster.multicastEvent(new UpdateEvent(entity, old, by));
	}
	
	/**
	 * Binds the values to the entity provided and invokes the DAO's validator.
	 * 
	 * @param entity The entity to bind
	 * @param values The values to bind
	 * @throws BindException if validation of the entity fails
	 */
	protected void doBind(T entity, Map<String,?> values, String[] allowed) throws BindException
	{
		DataBinder binder = new DataBinder(entity);
		registerCustomEditors(binder);
		binder.setValidator(validator);
		binder.setAllowedFields(allowed); // it's ok to pass null or empty
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
    	this.validator = validator;
    }
	
	/**
	 * Enforces the natural key constraint of an entity.
	 * 
	 * This method searches Hibernate's config settings for the entity's natural
	 * key(s) and passes the matching key names and values to
	 * {@link #enforceUnique(Map, Modifiable)}.
	 * 
	 * @param values
	 * @param expected
	 * @throws BindException
	 */
	protected void enforceNaturalKey(Map<String,?> values, T expected) throws BindException
	{
		if ( canEnforceNaturalKey() && hasNaturalId && values != null ) {
			ClassMetadata meta = getHibernateTemplate().getSessionFactory().getClassMetadata(entityClass);
			String[] names = meta.getPropertyNames();
			HashMap<String,Object> fields = new HashMap<String,Object>();
			for(int prop : meta.getNaturalIdentifierProperties()) {
				fields.put(names[prop], values.get(names[prop]));
			}
			enforceUnique(fields, expected);
		}
	}

	/**
	 * Does a {@link #find(Map)} using the keyValues and errors if it doesn't match the expected. 
	 * 
	 * @param keyValues The keyvalues (to pass to find)
	 * @param expected The expected result (can be null or an entity)
	 * @throws BindException If the find method returned an unexpected result
	 */
	protected void enforceUnique(Map<String,?> keyValues, T expected) throws BindException
	{
		T other = find(keyValues);
		if ( other != null && (expected == null || !expected.equals(other)) ) {
			T target = expected != null ? expected : BeanUtils.instantiate(entityClass);
			BindingResult result = new BeanPropertyBindingResult(target, "target");
			String fieldName = keyValues.size() > 1 ?
					null : keyValues.keySet().iterator().next();
			result.rejectValue(fieldName, "record.exists",
					new Object[]{keyValues.toString()},
					"A record already exists with the values {0}");
			throw new BindException(result);
		}
	}
}
