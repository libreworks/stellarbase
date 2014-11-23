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
package com.libreworks.stellarbase.security.acl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jonathan Hawk
 * @version $Id$
 */
public class NoOpAclCacheTest
{
	private NoOpAclCache object;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		object = new NoOpAclCache();
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.security.acl.NoOpAclCache#contains(org.springframework.security.acls.model.ObjectIdentity, java.util.Collection)}.
	 */
	@Test
	public void testContains()
	{
		assertFalse(object.contains(null, null));
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.security.acl.NoOpAclCache#get(org.springframework.security.acls.model.ObjectIdentity, java.util.Collection)}.
	 */
	@Test
	public void testGet()
	{
		assertNull(object.get(null, null));
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.security.acl.NoOpAclCache#put(org.springframework.security.acls.model.ObjectIdentity, java.util.Collection, org.springframework.security.acls.model.Acl)}.
	 */
	@Test
	public void testPut()
	{
		assertSame(object, object.put(null, null, null));
	}
}
