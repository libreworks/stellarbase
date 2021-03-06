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
package com.libreworks.stellarbase.orm.hibernate;

import java.io.Serializable;

import com.libreworks.stellarbase.dao.DeletableDao;
import com.libreworks.stellarbase.persistence.context.DeleteEvent;
import com.libreworks.stellarbase.persistence.model.Modifiable;

/**
 * Abstract Hibernate DAO for deletable entities 
 * @author Jonathan Hawk
 * @param <T>
 * @param <K>
 * @version $Id$
 */
public abstract class AbstractDeletableHibernateDao<T extends Modifiable<K>,K extends Serializable> extends AbstractWritableHibernateDao<T,K> implements DeletableDao<T,K>
{
    /*
     * (non-Javadoc)
     * @see com.libreworks.stellarbase.orm.dao.DeletableDao#delete(com.libreworks.stellarbase.orm.model.Identifiable, java.lang.String)
     */
	public void delete(T entity, String by)
    {
		eventPublisher.publishEvent(new DeleteEvent(entity, by));
		getHibernateTemplate().delete(entity);
    }
}
