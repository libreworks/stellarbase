package com.libreworks.stellarbase.orm.hibernate;

import java.util.Map;

import org.springframework.validation.BindException;

import com.libreworks.messageboardsample.dao.Post;
import com.libreworks.messageboardsample.dao.PostDao;
import com.libreworks.stellarbase.orm.hibernate.AbstractDeletableHibernateDao;

public class HibernatePostDao extends AbstractDeletableHibernateDao<Post,Long> implements PostDao
{
	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.orm.dao.hibernate.AbstractWritableHibernateDao#create(java.util.Map, java.lang.String)
	 */
	@Override
	public Post create(Map<String,?> values, String by) throws BindException
	{
		Post entity = super.create(values, by);
		com.libreworks.messageboardsample.dao.Thread t = entity.getThread();
		t.setLastPostOn(entity.getCreatedOn());
		t.setPostsSum(entity.getThread().getPostsSum() + 1);
		return entity;
	}
}
