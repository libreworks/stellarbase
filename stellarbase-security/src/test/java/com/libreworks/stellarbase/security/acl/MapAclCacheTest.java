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
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.Sid;

/**
 * @author Jonathan Hawk
 * @version $Id$
 */
public class MapAclCacheTest
{
	private MapAclCache object;
	
	/**
	 * Sets up the test
	 */
	@Before
	public void setUp()
	{
		object = new MapAclCache();
	}

	/**
	 * Tests basic operation of the class
	 */
	@Test
	public void testBasic()
	{
		ObjectIdentityImpl objId = new ObjectIdentityImpl(String.class, 1);
		ArrayList<Sid> sids = new ArrayList<Sid>();
		assertFalse(object.contains(objId, sids));
		assertNull(object.get(objId, sids));
		EmptyAccessControlList acl = new EmptyAccessControlList(objId, null, sids);
		object.put(objId, sids, acl);
		assertTrue(object.contains(objId, sids));
		assertEquals(acl, object.get(objId, sids));
	}
}
