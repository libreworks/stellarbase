package net.libreworks.stellarbase.orm.dao.hibernate;

import net.libreworks.stellarbase.orm.dao.ThreadDao;

public class HibernateThreadDao extends AbstractDeletableHibernateDao<net.libreworks.stellarbase.orm.dao.Thread,Long> implements ThreadDao
{
	private final static String[] ALLOWED = new String[]{"title", "sticky", "announcement", "closed", "lastPostOn", "postsSum"};

	/* (non-Javadoc)
	 * @see net.libreworks.stellarbase.orm.dao.hibernate.AbstractWritableHibernateDao#getAllowedCreateFields()
	 */
	@Override
	protected String[] getAllowedCreateFields()
	{
		return ALLOWED;
	}

	/* (non-Javadoc)
	 * @see net.libreworks.stellarbase.orm.dao.hibernate.AbstractWritableHibernateDao#getAllowedUpdateFields()
	 */
	@Override
	protected String[] getAllowedUpdateFields()
	{
		return ALLOWED;
	}
}
