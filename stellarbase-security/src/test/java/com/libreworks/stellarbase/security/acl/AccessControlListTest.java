package com.libreworks.stellarbase.security.acl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.UnloadedSidException;

import com.google.common.collect.ImmutableList;

public class AccessControlListTest
{
	/**
	 * Tests basic constructor
	 */
	@Test
	public void testBasic()
	{
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 2);
		Acl parent = new StubAcl(oi);
		ArrayList<Sid> sids = new ArrayList<Sid>();
		AccessControlList acl = AccessControlList.builder(oi, parent, sids).build();
		assertEquals(parent, acl.getParentAcl());
		assertEquals(oi, acl.getObjectIdentity());
		assertTrue(acl.isEntriesInheriting());
		assertTrue(acl.equals(acl));
	}
	
	/**
	 * Tests getEntries and setEntries
	 */
	@Test
	public void testGetEntries()
	{
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 2);
		PrincipalSid sid = new PrincipalSid("foobar1");
		AccessControlList acl = AccessControlList.builder(oi, null, null)
			.allow(sid, BasePermission.WRITE)
			.deny(sid, BasePermission.DELETE)
			.build();
		AccessControlEntry ace = acl.getEntries().get(0);
		assertEquals(BasePermission.WRITE, ace.getPermission());
		assertEquals(sid, ace.getSid());
		assertTrue(ace.isGranting());
		AccessControlEntry ace2 = acl.getEntries().get(1);
		assertEquals(BasePermission.DELETE, ace2.getPermission());
		assertEquals(sid, ace2.getSid());
		assertFalse(ace2.isGranting());
	}

	/**
	 * Tests isEntriesInheriting
	 */
	@Test
	public void testIsEntriesInheriting()
	{
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 2);
		Acl parent = new StubAcl(oi);
		AccessControlList acl = AccessControlList.builder(oi, parent, null).setEntriesInheriting(true).build();
		assertTrue(acl.isEntriesInheriting());
		AccessControlList acl2 = AccessControlList.builder(oi, parent, null).setEntriesInheriting(false).build();
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
		
		StubPgs pgs = new StubPgs();
		pgs.setAllow(true);
		
		AccessControlList acl = AccessControlList.builder(oi, null, sids).setPermissionGrantingStrategy(pgs).build();
		List<Permission> permission = new ArrayList<Permission>();
		permission.add(BasePermission.CREATE);
		assertTrue(acl.isGranted(permission, sids, false));
		
		pgs.setAllow(false);
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
		ArrayList<Sid> sids = new ArrayList<Sid>();
		AccessControlList acl = AccessControlList.builder(oi, null, sids).build();
		PrincipalSid sid = new PrincipalSid("foobar1");
		sids.add(sid);
		acl.isGranted(ImmutableList.of(BasePermission.READ), sids, false);
	}

	/**
	 * Tests isSidsLoaded
	 */
	@Test
	public void testIsSidLoadedEmpty()
	{
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 2);
		ArrayList<Sid> sids = new ArrayList<Sid>();
		AccessControlList acl = AccessControlList.builder(oi, null, sids).build();
		PrincipalSid sid = new PrincipalSid("foobar1");
		assertFalse(acl.isSidLoaded(ImmutableList.<Sid>of(sid)));
		assertTrue(acl.isSidLoaded(null));
		assertTrue(acl.isSidLoaded(ImmutableList.<Sid>of()));
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
		AccessControlList acl = AccessControlList.builder(oi, null, sids).build();
		assertTrue(acl.isSidLoaded(sids));
		List<Sid> sids2 = Arrays.asList((Sid)new PrincipalSid("foobar2"));
		assertFalse(acl.isSidLoaded(sids2));
		assertTrue(acl.isSidLoaded(null));
		assertTrue(acl.isSidLoaded(ImmutableList.<Sid>of()));
	}
	
	/**
	 * Tests getOwner
	 */
	@Test
	public void testGetOwner()
	{
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 2);
		AccessControlList acl = AccessControlList.builder(oi, null, null).build();
		assertNull(acl.getOwner());
	}
	
	class StubPgs implements PermissionGrantingStrategy
	{
		private boolean allow;
		
		public boolean isGranted(Acl acl, List<Permission> permission, List<Sid> sids, boolean administrativeMode)
		{
			return allow;
		}
		
		public void setAllow(boolean allow)
		{
			this.allow = allow;
		}
	}
}
