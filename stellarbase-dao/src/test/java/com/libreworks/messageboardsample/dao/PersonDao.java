package com.libreworks.messageboardsample.dao;

import com.libreworks.stellarbase.dao.RemovableDao;
import com.libreworks.stellarbase.dao.WritableDao;

public interface PersonDao extends RemovableDao<Person,Long>, WritableDao<Person,Long>
{
	public Person getByUsername(String username);
}
