package net.libreworks.stellarbase.orm.dao.hibernate;

import org.hibernate.criterion.Restrictions;

import net.libreworks.stellarbase.orm.dao.Person;
import net.libreworks.stellarbase.orm.dao.PersonDao;

public class HibernatePersonDao extends AbstractRemovableHibernateDao<Person,Long> implements PersonDao 
{
	private final static String[] ALLOWED_CREATE = new String[]{"username", "firstName", "mi", "lastName", "suffix", "birthday", "bio", "admin"};
	private final static String[] ALLOWED_UPDATE = new String[]{"firstName", "mi", "lastName", "suffix", "birthday", "bio", "admin"};
	
	/* (non-Javadoc)
	 * @see net.libreworks.stellarbase.orm.dao.hibernate.AbstractWritableHibernateDao#getAllowedCreateFields()
	 */
	@Override
	protected String[] getAllowedCreateFields()
	{
		return ALLOWED_CREATE;
	}

	/* (non-Javadoc)
	 * @see net.libreworks.stellarbase.orm.dao.hibernate.AbstractWritableHibernateDao#getAllowedUpdateFields()
	 */
	@Override
	protected String[] getAllowedUpdateFields()
	{
		return ALLOWED_UPDATE;
	}

	public Person getByUsername(String username)
	{
		return getByNaturalId(Restrictions.naturalId().set("username", username));
	}
}
