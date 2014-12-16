package com.libreworks.messageboardsample.dao;

import com.libreworks.stellarbase.dao.DeletableDao;
import com.libreworks.stellarbase.dao.WritableDao;

public interface PostDao extends DeletableDao<Post,Long>, WritableDao<Post,Long>
{
	
}
