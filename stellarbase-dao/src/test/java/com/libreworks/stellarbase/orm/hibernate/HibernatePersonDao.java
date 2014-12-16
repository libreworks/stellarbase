package com.libreworks.stellarbase.orm.hibernate;

import org.hibernate.criterion.Restrictions;

import com.libreworks.messageboardsample.dao.Person;
import com.libreworks.messageboardsample.dao.PersonDao;
import com.libreworks.stellarbase.orm.hibernate.AbstractRemovableHibernateDao;

public class HibernatePersonDao extends AbstractRemovableHibernateDao<Person,Long> implements PersonDao 
{
	public Person getByUsername(String username)
	{
		return getByNaturalId(Restrictions.naturalId().set("username", username));
	}
}
