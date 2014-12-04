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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;

import com.google.common.collect.ImmutableList;

/**
 * @author Jonathan Hawk
 * @version $Id$
 */
public class LoadingAclServiceTest
{
	private LoadingAclService object;
	
	/**
	 * Sets up the class
	 */
	@Before
	public void setUp()
	{
		object = new LoadingAclService();
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.security.acl.LoadingAclService#findChildren(org.springframework.security.acls.model.ObjectIdentity)}.
	 */
	@Test
	public void testFindChildren()
	{
		assertEquals(ImmutableList.of(), object.findChildren(null));
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.security.acl.LoadingAclService#loadAcl(org.springframework.security.acls.model.ObjectIdentity, java.util.List)}.
	 */
	@Test
	public void testLoadAcl()
	{
		ObjectIdentityImpl oid = new ObjectIdentityImpl(String.class, new Integer(1));
		ArrayList<Sid> sids = new ArrayList<Sid>();
		sids.add(new PrincipalSid("foobar1"));
		object.setLoaders(Arrays.asList(new StubAceLoader()));
		Acl result = object.loadAcl(oid, sids);
		assertEquals(oid, result.getObjectIdentity());
		assertTrue(result.isSidLoaded(sids));
		assertEquals(1, result.getEntries().size());
	}
	
	/**
	 * Tests cached loading
	 */
	@Test
	public void testLoadAclCached()
	{
		MapAclCache aclCache = new MapAclCache();
		object.setAclCache(aclCache);
		ObjectIdentityImpl oid = new ObjectIdentityImpl(String.class, new Integer(1));
		ArrayList<Sid> sids = new ArrayList<Sid>();
		sids.add(new PrincipalSid("foobar1"));		
		EmptyAccessControlList acl = new EmptyAccessControlList(oid, null, sids);
		aclCache.put(oid, sids, acl);
		Acl result = object.loadAcl(oid, sids);
		assertSame(result, acl);
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.security.acl.LoadingAclService#readAclsById(java.util.List, java.util.List)}.
	 */
	@Test
	public void testReadAclsByIdListOfObjectIdentityListOfSid()
	{
		List<ObjectIdentity> oids = Collections.emptyList();
		List<Sid> sids = Collections.emptyList();
		Map<ObjectIdentity,Acl> map = object.readAclsById(oids, sids);
		assertTrue(map.isEmpty());
		
		oids = new ArrayList<ObjectIdentity>();
		ObjectIdentityImpl oid = new ObjectIdentityImpl(String.class, new Integer(1));		
		oids.add(oid);
		Map<ObjectIdentity,Acl> map2 = object.readAclsById(oids, sids);
		assertEquals(1, map2.size());
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.security.acl.LoadingAclService#setAclCache(com.libreworks.stellarbase.security.acl.AclCache)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSetAclCache()
	{
		object.setAclCache(null);
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.security.acl.LoadingAclService#setLoaders(java.util.Collection)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSetLoaders()
	{
		object.setLoaders(null);
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.security.acl.LoadingAclService#setParentResolver(com.libreworks.stellarbase.security.acl.ParentObjectIdentityResolver)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSetParentResolver()
	{
		object.setParentResolver(null);
	}
	
	protected class StubAceLoader implements AccessControlEntryLoader
	{
		public List<AccessControlEntry> getEntries(Acl acl, ObjectIdentity oid, Collection<Sid> sids)
        {
			ArrayList<AccessControlEntry> aces = new ArrayList<AccessControlEntry>();
			aces.add(new AccessControlEntryImpl("test1", acl, sids.iterator()
			    .next(), BasePermission.READ, true, false, false));
			return aces;
        }

		public boolean supports(ObjectIdentity oid)
        {
	        return true;
        }
	}
}
