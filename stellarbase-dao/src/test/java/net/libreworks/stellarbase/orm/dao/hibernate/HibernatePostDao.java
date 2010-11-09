package net.libreworks.stellarbase.orm.dao.hibernate;

import java.util.Map;

import org.springframework.validation.BindException;

import net.libreworks.stellarbase.orm.dao.Post;
import net.libreworks.stellarbase.orm.dao.PostDao;

public class HibernatePostDao extends AbstractDeletableHibernateDao<Post,Long> implements PostDao
{
	private final static String[] ACCEPTED_CREATE = new String[]{"thread", "author", "title", "body"};
	private final static String[] ACCEPTED_UPDATE = new String[]{"title", "body"};
	
	/* (non-Javadoc)
	 * @see net.libreworks.stellarbase.orm.dao.hibernate.AbstractWritableHibernateDao#create(java.util.Map, java.lang.String)
	 */
	@Override
	public Post create(Map<String,?> values, String by) throws BindException
	{
		Post entity = super.create(values, by);
		net.libreworks.stellarbase.orm.dao.Thread t = entity.getThread();
		t.setLastPostOn(entity.getCreatedOn());
		t.setPostsSum(entity.getThread().getPostsSum() + 1);
		return entity;
	}

	/* (non-Javadoc)
	 * @see net.libreworks.stellarbase.orm.dao.hibernate.AbstractWritableHibernateDao#getAllowedCreateFields()
	 */
	@Override
	protected String[] getAllowedCreateFields()
	{
		return ACCEPTED_CREATE;
	}
	
	/* (non-Javadoc)
	 * @see net.libreworks.stellarbase.orm.dao.hibernate.AbstractWritableHibernateDao#getAllowedUpdateFields()
	 */
	@Override
	protected String[] getAllowedUpdateFields()
	{
		return ACCEPTED_UPDATE;
	}
}
