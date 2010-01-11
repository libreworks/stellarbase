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
package net.libreworks.stellarbase.security.acl;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.UnloadedSidException;

/**
 * @author Jonathan Hawk
 * @version $Id$
 */
public class AclImplTest
{
	/**
	 * Tests basic constructor
	 */
	@Test
	public void testBasic()
	{
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 2);
		AclImpl parent = new AclImpl(oi);
		ArrayList<Sid> sids = new ArrayList<Sid>();
		AclImpl acl = new AclImpl(oi, parent, sids);
		assertEquals(parent, acl.getParentAcl());
		assertEquals(oi, acl.getObjectIdentity());
	}
	
	/**
	 * Tests getEntries and setEntries
	 */
	@Test
	public void testGetEntries()
	{
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 2);
		AclImpl acl = new AclImpl(oi);
		List<AccessControlEntry> aces = new ArrayList<AccessControlEntry>();
		PrincipalSid sid = new PrincipalSid("foobar1");
		aces.add(new AccessControlEntryImpl(1, acl, sid, BasePermission.WRITE, true, false, false));
		acl.setEntries(aces);
		assertEquals(aces, acl.getEntries());
		List<Sid> sids = new ArrayList<Sid>();
		sids.add(sid);
		assertTrue(acl.isSidLoaded(sids));
	}

	/**
	 * Tests isEntriesInheriting
	 */
	@Test
	public void testIsEntriesInheriting()
	{
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 2);
		AclImpl parent = new AclImpl(oi);
		AclImpl acl = new AclImpl(oi, parent);
		assertTrue(acl.isEntriesInheriting());
		AclImpl acl2 = new AclImpl(oi);
		assertFalse(acl2.isEntriesInheriting());
	}

	/**
	 * Tests isGranted
	 */
	@Test
	public void testIsGranted()
	{
		ArrayList<Sid> sids = new ArrayList<Sid>();
		PrincipalSid sid = new PrincipalSid("foobar1");
		sids.add(sid);
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 2);
		AclImpl acl = new AclImpl(oi, null, sids);
		List<AccessControlEntry> aces = new ArrayList<AccessControlEntry>();
		aces.add(new AccessControlEntryImpl(1, acl, sid, BasePermission.CREATE, true, false, false));
		aces.add(new AccessControlEntryImpl(2, acl, sid, BasePermission.DELETE, false, false, false));
		acl.setEntries(aces);
		List<Permission> permission = new ArrayList<Permission>();
		permission.add(BasePermission.CREATE);
		assertTrue(acl.isGranted(permission, sids, false));
		List<Permission> permission2 = new ArrayList<Permission>();
		permission2.add(BasePermission.DELETE);
		assertFalse(acl.isGranted(permission2, sids, false));
	}
	
	/**
	 * Tests isGranted
	 */
	@Test(expected=UnloadedSidException.class)
	public void testIsGrantedNotLoaded()
	{
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 3);
		AclImpl acl = new AclImpl(oi);
		ArrayList<Sid> sids = new ArrayList<Sid>();
		PrincipalSid sid = new PrincipalSid("foobar1");
		sids.add(sid);
		acl.isGranted(null, sids, false);
	}

	/**
	 * Tests isSidsLoaded
	 */
	@Test
	public void testIsSidLoadedEmpty()
	{
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 2);
		AclImpl acl = new AclImpl(oi);
		ArrayList<Sid> sids = new ArrayList<Sid>();
		PrincipalSid sid = new PrincipalSid("foobar1");
		sids.add(sid);
		assertFalse(acl.isSidLoaded(sids));
		
		AclImpl acl2 = new AclImpl(oi, null, sids);
		assertTrue(acl2.isSidLoaded(null));
		List<Sid> sids2 = Collections.emptyList();
		assertTrue(acl2.isSidLoaded(sids2));
	}
	
	/**
	 * Tests isSidsLoaded
	 */
	@Test
	public void testIsSidLoadedTrue()
	{
		ArrayList<Sid> sids = new ArrayList<Sid>();
		PrincipalSid sid = new PrincipalSid("foobar1");
		sids.add(sid);
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 2);
		AclImpl acl = new AclImpl(oi, null, sids);
		assertTrue(acl.isSidLoaded(sids));
		List<Sid> sids2 = Arrays.asList((Sid)new PrincipalSid("foobar2"));
		assertFalse(acl.isSidLoaded(sids2));
	}
	
	/**
	 * Tests getOwner
	 */
	@Test
	public void testGetOwner()
	{
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 2);
		AclImpl acl = new AclImpl(oi);
		assertNull(acl.getOwner());
	}
}
