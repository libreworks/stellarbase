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
package net.libreworks.stellarbase.dao.hibernate;

import java.io.Serializable;
import java.util.Date;
import net.libreworks.stellarbase.dao.DeletableDao;
import net.libreworks.stellarbase.model.Modifiable;
import net.libreworks.stellarbase.model.Removable;

/**
 * Abstract Hibernate DAO for deletable entities 
 * @author Jonathan Hawk
 * @param <T>
 * @param <K>
 */
public abstract class AbstractDeletableHibernateDao<T extends Modifiable<K>,K extends Serializable> extends AbstractWritableHibernateDao<T,K> implements DeletableDao<T,K>
{
	/*
	 * (non-Javadoc)
	 * @see net.libreworks.stellarbase.dao.DeletableDao#delete(net.libreworks.stellarbase.model.Identifiable, java.lang.String)
	 */
	public void delete(T entity, String by)
    {
		if ( entity instanceof Removable<?> ) {
			Removable<?> rentity = (Removable<?>)entity;
			rentity.setRemovedOn(new Date());
			rentity.setRemovedBy(by);
			getHibernateTemplate().evict(rentity);
		} else {
			getHibernateTemplate().delete(entity);
		}
    }
}
