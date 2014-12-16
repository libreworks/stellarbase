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
package com.libreworks.stellarbase.persistence.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.libreworks.stellarbase.dao.ReadableDao;
import com.libreworks.stellarbase.persistence.model.Identifiable;

/**
 * Base entity service.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 * @param <T> The type of entity
 * @param <K> The type of identifier
 */
public abstract class AbstractEntityService<T extends Identifiable<K>,K extends Serializable> implements EntityService<T,K>
{
    /*
     * (non-Javadoc)
     * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
     */
    public T convert(K source)
    {
        return getDao().convert(source);
    }

    /*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.orm.model.EntityRepository#find(java.util.Map)
	 */
    public T find(Map<String,?> fields)
    {
	    return getDao().find(fields);
    }

    /*
     * (non-Javadoc)
     * @see com.libreworks.stellarbase.orm.model.EntityRepository#findAll(java.util.Map)
     */
    public List<T> findAll(Map<String,?> fields)
    {
	    return getDao().findAll(fields);
    }

    /*
     * (non-Javadoc)
     * @see com.libreworks.stellarbase.orm.model.EntityRepository#getAll()
     */
    public List<T> getAll()
    {
	    return getDao().getAll();
    }

    /*
     * (non-Javadoc)
     * @see com.libreworks.stellarbase.orm.model.EntityRepository#getById(java.io.Serializable)
     */
    public T getById(K id)
    {
	    return getDao().getById(id);
    }

    /*
     * (non-Javadoc)
     * @see com.libreworks.stellarbase.orm.model.EntityRepository#getByIds(java.util.Collection)
     */
    public List<T> getByIds(Collection<K> ids)
    {
	    return getDao().getByIds(ids);
    }

    /**
	 * Gets the DAO that can be used to load entities.
	 * 
	 * @return The DAO
	 */
	abstract protected ReadableDao<T,K> getDao();

    /*
     * (non-Javadoc)
     * @see com.libreworks.stellarbase.orm.model.EntityRepository#getEntityClass()
     */
    public Class<T> getEntityClass()
    {
	    return getDao().getEntityClass();
    }

    /*
     * (non-Javadoc)
     * @see com.libreworks.stellarbase.orm.model.EntityRepository#getIdentifierClass()
     */
	public Class<K> getIdentifierClass()
    {
        return getDao().getIdentifierClass();
    }

    /*
     * (non-Javadoc)
     * @see com.libreworks.stellarbase.orm.model.EntityRepository#loadById(java.io.Serializable)
     */
    public T loadById(K id)
    {
	    return getDao().loadById(id);
    }
    
	/*
     * (non-Javadoc)
     * @see com.libreworks.stellarbase.orm.model.EntityRepository#refresh(com.libreworks.stellarbase.orm.model.Identifiable)
     */
    public void refresh(T entity)
    {
    	getDao().refresh(entity);
    }
}
