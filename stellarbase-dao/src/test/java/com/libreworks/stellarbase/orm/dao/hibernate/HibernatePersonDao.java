package com.libreworks.stellarbase.orm.dao.hibernate;

import org.hibernate.criterion.Restrictions;

import com.libreworks.stellarbase.orm.dao.Person;
import com.libreworks.stellarbase.orm.dao.PersonDao;

public class HibernatePersonDao extends AbstractRemovableHibernateDao<Person,Long> implements PersonDao 
{
	public Person getByUsername(String username)
	{
		return getByNaturalId(Restrictions.naturalId().set("username", username));
	}
}
