package com.libreworks.stellarbase.security.acl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Permission;

public class GrantAccessControlEntryTest
{
	/**
	 * Test method for {@link com.libreworks.stellarbase.security.acl.GrantAccessControlEntry#isGranting()}.
	 */
	@Test
	public void testIsGranting()
	{
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 1L);
		StubAcl acl = new StubAcl(oi);
		PrincipalSid sid = new PrincipalSid("foobar");
		Permission permission = BasePermission.CREATE;
		GrantAccessControlEntry object = new GrantAccessControlEntry(acl, sid, permission);
		assertTrue(object.isGranting());
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.security.acl.AbstractAccessControlEntry#hashCode()}.
	 */
	@Test
	public void testHashCode()
	{
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 1L);
		StubAcl acl = new StubAcl(oi);
		PrincipalSid sid = new PrincipalSid("foobar");
		Permission permission = BasePermission.CREATE;
		GrantAccessControlEntry object = new GrantAccessControlEntry(acl, sid, permission);		
		GrantAccessControlEntry object2 = new GrantAccessControlEntry(acl, sid, permission);		
		GrantAccessControlEntry object3 = new GrantAccessControlEntry(acl, sid, BasePermission.DELETE);		
		assertEquals(object2.hashCode(), object.hashCode());
		assertNotEquals(object3.hashCode(), object.hashCode());
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.security.acl.AbstractAccessControlEntry#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject()
	{
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 1L);
		StubAcl acl = new StubAcl(oi);
		PrincipalSid sid = new PrincipalSid("foobar");
		Permission permission = BasePermission.CREATE;
		GrantAccessControlEntry object = new GrantAccessControlEntry(acl, sid, permission);
		GrantAccessControlEntry object2 = new GrantAccessControlEntry(acl, sid, permission);
		GrantAccessControlEntry object3 = new GrantAccessControlEntry(acl, sid, BasePermission.DELETE);
		
		assertTrue(object.equals(object));
		assertTrue(object.equals(object2));
		assertFalse(object.equals(null));
		assertFalse(object.equals("foobar"));
		assertFalse(object.equals(object3));
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.security.acl.AbstractAccessControlEntry#getAcl()}.
	 */
	@Test
	public void testGetAcl()
	{
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 1L);
		StubAcl acl = new StubAcl(oi);
		PrincipalSid sid = new PrincipalSid("foobar");
		Permission permission = BasePermission.CREATE;
		GrantAccessControlEntry object = new GrantAccessControlEntry(acl, sid, permission);
		assertSame(acl, object.getAcl());
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.security.acl.AbstractAccessControlEntry#getPermission()}.
	 */
	@Test
	public void testGetPermission()
	{
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 1L);
		StubAcl acl = new StubAcl(oi);
		PrincipalSid sid = new PrincipalSid("foobar");
		Permission permission = BasePermission.CREATE;
		GrantAccessControlEntry object = new GrantAccessControlEntry(acl, sid, permission);
		assertSame(permission, object.getPermission());
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.security.acl.AbstractAccessControlEntry#getSid()}.
	 */
	@Test
	public void testGetSid()
	{
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 1L);
		StubAcl acl = new StubAcl(oi);
		PrincipalSid sid = new PrincipalSid("foobar");
		Permission permission = BasePermission.CREATE;
		GrantAccessControlEntry object = new GrantAccessControlEntry(acl, sid, permission);
		assertSame(sid, object.getSid());
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.security.acl.AbstractAccessControlEntry#toString()}.
	 */
	@Test
	public void testToString()
	{
		ObjectIdentityImpl oi = new ObjectIdentityImpl(String.class, 1L);
		StubAcl acl = new StubAcl(oi);
		PrincipalSid sid = new PrincipalSid("foobar");
		Permission permission = BasePermission.CREATE;
		GrantAccessControlEntry object = new GrantAccessControlEntry(acl, sid, permission);
		assertEquals(sid.toString() + " can " + permission.getPattern() + " on " + oi.toString(), object.toString());
	}
}
