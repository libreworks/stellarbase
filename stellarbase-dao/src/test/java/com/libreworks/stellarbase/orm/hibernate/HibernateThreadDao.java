package com.libreworks.stellarbase.orm.hibernate;

import com.libreworks.messageboardsample.dao.ThreadDao;
import com.libreworks.stellarbase.orm.hibernate.AbstractDeletableHibernateDao;

public class HibernateThreadDao extends AbstractDeletableHibernateDao<com.libreworks.messageboardsample.dao.Thread,Long> implements ThreadDao
{
}
