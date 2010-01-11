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
package net.libreworks.stellarbase.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.libreworks.stellarbase.dao.ReadableDao;
import net.libreworks.stellarbase.model.Identifiable;

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
	 * @see net.libreworks.stellarbase.model.EntityRepository#find(java.util.Map)
	 */
    public T find(Map<String,?> fields)
    {
	    return getDao().find(fields);
    }

    /*
     * (non-Javadoc)
     * @see net.libreworks.stellarbase.model.EntityRepository#findAll(java.util.Map)
     */
    public List<T> findAll(Map<String,?> fields)
    {
	    return getDao().findAll(fields);
    }

    /*
     * (non-Javadoc)
     * @see net.libreworks.stellarbase.model.EntityRepository#getAll()
     */
    public List<T> getAll()
    {
	    return getDao().getAll();
    }

    /*
     * (non-Javadoc)
     * @see net.libreworks.stellarbase.model.EntityRepository#getById(java.io.Serializable)
     */
    public T getById(K id)
    {
	    return getDao().getById(id);
    }

    /*
     * (non-Javadoc)
     * @see net.libreworks.stellarbase.model.EntityRepository#getByIds(java.util.Collection)
     */
    public List<T> getByIds(Collection<K> ids)
    {
	    return getDao().getByIds(ids);
    }

    /*
     * (non-Javadoc)
     * @see net.libreworks.stellarbase.model.EntityRepository#getEntityClass()
     */
    public Class<T> getEntityClass()
    {
	    return getDao().getEntityClass();
    }

    /*
     * (non-Javadoc)
     * @see net.libreworks.stellarbase.model.EntityRepository#loadById(java.io.Serializable)
     */
    public T loadById(K id)
    {
	    return getDao().loadById(id);
    }

	/**
	 * Gets the DAO that can be used to load entities.
	 * 
	 * @return The DAO
	 */
	abstract protected ReadableDao<T,K> getDao();
}
