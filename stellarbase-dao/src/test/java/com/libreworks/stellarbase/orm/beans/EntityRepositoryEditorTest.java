/**
 * Copyright 2010 LibreWorks contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Jonathan Hawk
 */
package com.libreworks.stellarbase.orm.beans;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.libreworks.stellarbase.orm.model.EntityRepository;
import com.libreworks.stellarbase.orm.model.Identifiable;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.DataBinder;

public class EntityRepositoryEditorTest
{
	private EntityRepositoryEditor<TestEntity,Long> object;
	
	@Before
	public void setUp() throws Exception
	{
		object = EntityRepositoryEditor.factory(new TestRepository());
	}

	@Test
	public void testRegister()
	{
		TestEntity entity = new TestEntity();
		DataBinder binder = new DataBinder(entity, "target");
		assertNull(binder.findCustomEditor(TestEntity.class, null));
		EntityRepositoryEditor.register(binder, new TestRepository());
		assertNotNull(binder.findCustomEditor(TestEntity.class, null));
	}
	
	@Test
	public void testGetAsText()
	{
		TestEntity entity = new TestEntity();
		entity.setId(12345L);
		object.setValue(entity);
		assertEquals("12345", object.getAsText());
	}

	@Test
	public void testSetAsTextString()
	{
		String text = "654321";
		object.setAsText(text);
		assertEquals(text, object.getAsText());
	}

	@Test
	public void testSetValueObject()
	{
		TestEntity entity = new TestEntity();
		object.setValue(entity);
		assertSame(entity, object.getValue());
	}
	
	@Test
	public void testSetValueNull()
	{
		object.setValue(null);
		assertNull(object.getValue());
	}
	
	@Test
	public void testSetValueString()
	{
		TestEntity entity = new TestEntity();
		entity.setId(999888L);
		object.setValue(entity.getId());
		assertEquals(entity, object.getValue());
	}

	private class TestEntity implements Identifiable<Long>
	{
		private static final long serialVersionUID = 1L;
		private Long id;
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TestEntity other = (TestEntity) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}

		public String getCreatedBy()
		{
			return null;
		}

		public Date getCreatedOn()
		{
			return null;
		}

		public void setCreatedBy(String createdBy)
		{
		}

		public void setCreatedOn(Date createdOn)
		{
		}

		public Long getId()
		{
			return id;
		}

		public void setId(Long id)
		{
			this.id = id;
		}

		private EntityRepositoryEditorTest getOuterType()
		{
			return EntityRepositoryEditorTest.this;
		}
	}
	
	private class TestRepository implements EntityRepository<TestEntity,Long>
	{
		public TestEntity convert(Long source)
		{
			return getById(source);
		}

		public TestEntity find(Map<String,?> fields)
		{
			return null;
		}

		public List<TestEntity> findAll(Map<String,?> fields)
		{
			return null;
		}

		public List<TestEntity> getAll()
		{
			return null;
		}

		public TestEntity getById(Long id)
		{
			TestEntity entity = new TestEntity();
			entity.setId(id);
			return entity;
		}

		public List<TestEntity> getByIds(Collection<Long> ids)
		{
			return null;
		}

		public Class<TestEntity> getEntityClass()
		{
			return TestEntity.class;
		}

		public Class<Long> getIdentifierClass()
		{
			return Long.class;
		}

		public TestEntity loadById(Long id)
		{
			return null;
		}

		public void refresh(TestEntity entity)
		{
		}
	}
}
