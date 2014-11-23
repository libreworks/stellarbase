package com.libreworks.stellarbase.orm.dao;

public interface PersonDao extends RemovableDao<Person,Long>, WritableDao<Person,Long>
{
	public Person getByUsername(String username);
}
