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

import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.UnloadedSidException;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.libreworks.stellarbase.text.Strings;
import com.libreworks.stellarbase.util.Arguments;

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
	protected transient PermissionGrantingStrategy permissionGrantingStrategy;
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
		if (this == obj) {
			return true;
		} else if (obj instanceof AbstractAcl) {
			AbstractAcl other = (AbstractAcl)obj;
			return Objects.equal(objectIdentity, other.objectIdentity) &&
				Objects.equal(parentAcl, other.parentAcl) &&
				entriesInheriting == other.entriesInheriting &&
				Objects.equal(permissionGrantingStrategy, other.permissionGrantingStrategy) &&
				Objects.equal(loadedSids, other.loadedSids) &&
				Objects.equal(getEntries(), other.getEntries());
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(objectIdentity, parentAcl,
			Boolean.valueOf(entriesInheriting), permissionGrantingStrategy,
			loadedSids, getEntries());
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
		Arguments.checkEmpty(permission);
        Arguments.checkEmpty(sids);

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
