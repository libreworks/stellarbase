package com.libreworks.messageboardsample.dao;

import com.libreworks.stellarbase.dao.DeletableDao;
import com.libreworks.stellarbase.dao.WritableDao;

public interface ThreadDao extends DeletableDao<Thread,Long>, WritableDao<Thread,Long>
{

}
