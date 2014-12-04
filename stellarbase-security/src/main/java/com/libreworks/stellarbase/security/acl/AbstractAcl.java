/**
 * Copyright 2014 LibreWorks contributors
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

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.UnloadedSidException;
import org.springframework.util.Assert;

import com.google.common.collect.ImmutableList;
import com.libreworks.stellarbase.text.Strings;

/**
 * Abstract super class for immutable {@link Acl} implementations.
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractAcl implements Acl
{
	protected final ObjectIdentity objectIdentity;
	protected final Acl parentAcl;
	protected final List<Sid> loadedSids;
	protected final transient PermissionGrantingStrategy permissionGrantingStrategy;
    protected final boolean entriesInheriting;

    /**
     * Default constructor
     * 
     * @param objectIdentity The object for which we have ACL rules
     * @param parentAcl The parent ACL (nullable)
     * @param loadedSids The list of Sids whose rules are in this ACL (nullable)
     * @param permissionGrantingStrategy The strategy for permission logic
     * @param entriesInheriting Whether permissions should be found in the parent ACL
     */
	protected AbstractAcl(ObjectIdentity objectIdentity, Acl parentAcl, List<Sid> loadedSids, PermissionGrantingStrategy permissionGrantingStrategy, boolean entriesInheriting)
	{
		this.objectIdentity = objectIdentity;
		this.parentAcl = parentAcl;
		this.loadedSids = loadedSids == null ? null : ImmutableList.copyOf(loadedSids);
		this.permissionGrantingStrategy = permissionGrantingStrategy;
		this.entriesInheriting = entriesInheriting;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof AbstractAcl))
			return false;
		AbstractAcl other = (AbstractAcl)obj;
		return new EqualsBuilder()
			.append(objectIdentity, other.objectIdentity)
			.append(parentAcl, other.parentAcl)
			.append(entriesInheriting, other.entriesInheriting)
			.append(permissionGrantingStrategy, other.permissionGrantingStrategy)
			.append(loadedSids, other.loadedSids)
			.append(getEntries(), other.getEntries())
			.isEquals();
	}
	
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
			.append(objectIdentity)
			.append(parentAcl)
			.append(entriesInheriting)
			.append(permissionGrantingStrategy)
			.append(loadedSids)
			.append(getEntries())
			.toHashCode();
	}

	public ObjectIdentity getObjectIdentity()
	{
		return objectIdentity;
	}

	public Sid getOwner()
	{
		return null;
	}

	public Acl getParentAcl()
	{
		return parentAcl;
	}

	public boolean isEntriesInheriting()
	{
		return entriesInheriting;
	}

	public boolean isGranted(List<Permission> permission, List<Sid> sids, boolean administrativeMode) throws NotFoundException, UnloadedSidException
	{
		Assert.notEmpty(permission);
        Assert.notEmpty(sids);

        if (!this.isSidLoaded(sids)) {
            throw new UnloadedSidException("ACL was not loaded for one or more SID");
        }

        return permissionGrantingStrategy.isGranted(this, permission, sids, administrativeMode);
	}

	public boolean isSidLoaded(List<Sid> sids)
	{
		if (loadedSids == null || sids == null || sids.isEmpty()) {
			return true;
		}
		
		return loadedSids.containsAll(sids);
	}
	
	public String toString()
	{
        StringBuilder sb = new StringBuilder()
        	.append(getClass().getSimpleName())
        	.append("[")
        	.append("objectIdentity: ").append(this.objectIdentity).append("; ");
        if (getEntries().isEmpty()) {
            sb.append("no ACEs; ");
        } else {
        	sb.append(Strings.NL);
        	for (AccessControlEntry ace : getEntries()) {
            	sb.append(ace).append(Strings.NL);
        	}
        }
        return sb.append("inheriting: ").append(this.entriesInheriting)
        	.append("; ")
        	.append("parent: ")
        	.append((this.parentAcl == null) ? Strings.NULL : this.parentAcl.getObjectIdentity().toString())
        	.append("; ")
        	.append("permissionGrantingStrategy: ")
        	.append(this.permissionGrantingStrategy)
        	.append("]")
        	.toString();
    }	
}
