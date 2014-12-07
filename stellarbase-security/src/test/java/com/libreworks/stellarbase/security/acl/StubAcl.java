package com.libreworks.stellarbase.security.acl;

import java.util.List;

import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.UnloadedSidException;

import com.google.common.collect.ImmutableList;

@SuppressWarnings("serial")
class StubAcl implements Acl
{
	private final ObjectIdentity objectIdentity;
	
	public StubAcl(ObjectIdentity objectIdentity)
	{
		this.objectIdentity = objectIdentity;
	}
	
	public boolean isSidLoaded(List<Sid> sids)
	{
		return false;
	}
	
	public boolean isGranted(List<Permission> permission, List<Sid> sids,
			boolean administrativeMode) throws NotFoundException,
			UnloadedSidException
	{
		return false;
	}
	
	public boolean isEntriesInheriting()
	{
		return false;
	}
	
	public Acl getParentAcl()
	{
		return null;
	}
	
	public Sid getOwner()
	{
		return null;
	}
	
	public ObjectIdentity getObjectIdentity()
	{
		return objectIdentity;
	}
	
	public List<AccessControlEntry> getEntries()
	{
		return ImmutableList.of();
	}
}