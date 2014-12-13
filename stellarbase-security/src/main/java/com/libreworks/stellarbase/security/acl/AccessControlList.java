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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.acls.model.Sid;

import com.google.common.collect.ImmutableList;
import com.libreworks.stellarbase.util.Arguments;

/**
 * Immutable, thread-safe implementation of the Spring Security ACL
 *   
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class AccessControlList extends AbstractAcl
{
	private static final long serialVersionUID = 1L;

	protected final List<AccessControlEntry> entries;

    /**
     * Creates a new AccessControlList
     * 
     * @param objectIdentity The object for which we have ACL rules
     * @param parentAcl The parent ACL (nullable)
     * @param loadedSids The list of Sids whose rules are in this ACL (nullable)
     * @param entries The ACL rule entries
     * @param permissionGrantingStrategy The strategy for permission logic
     * @param entriesInheriting Whether permissions should be found in the parent ACL
     */
	private AccessControlList(ObjectIdentity objectIdentity, Acl parentAcl, List<Sid> loadedSids, List<AccessControlEntryDto> entries, PermissionGrantingStrategy permissionGrantingStrategy, boolean entriesInheriting)
	{
		super(objectIdentity, parentAcl, loadedSids, permissionGrantingStrategy, entriesInheriting);
		ArrayList<AccessControlEntry> aces = new ArrayList<AccessControlEntry>(entries.size());
		for (AccessControlEntryDto input : entries) {
			aces.add(input.granting ?
				new GrantAccessControlEntry(this, input.sid, input.permission) :
				new DenyAccessControlEntry(this, input.sid, input.permission));
		}
		this.entries = ImmutableList.copyOf(aces);
	}

	public static Builder builder(ObjectIdentity objectIdentity, Acl parentAcl, List<Sid> loadedSids)
	{
		return new Builder(objectIdentity, parentAcl, loadedSids);
	}
	
	public List<AccessControlEntry> getEntries()
	{
		return entries;
	}

	/**
	 * Simple DTO for ACE values
	 */
	private static class AccessControlEntryDto
	{
		private final Sid sid;
		private final Permission permission;
		private final boolean granting;
		
		private AccessControlEntryDto(Sid sid, Permission permission, boolean granting)
		{
			this.sid = sid;
			this.permission = permission;
			this.granting = granting;
		}
	}
	
	/**
	 * Builds immutable AccessControlList instances
	 */
	public static class Builder
	{
		private final ObjectIdentity objectIdentity;
		private final Acl parentAcl;
		private final List<Sid> loadedSids;
		private final List<AccessControlEntryDto> entries = new LinkedList<AccessControlEntryDto>();
		private PermissionGrantingStrategy permissionGrantingStrategy;
	    private boolean entriesInheriting = true;
	    
		private Builder(ObjectIdentity objectIdentity, Acl parentAcl, List<Sid> loadedSids)
		{
			this.objectIdentity = Arguments.checkNull(objectIdentity);
			this.parentAcl = parentAcl;
			this.loadedSids = loadedSids == null ? null : ImmutableList.copyOf(loadedSids);
		}
		
		/**
		 * Adds several granting {@link AccessControlEntry}s to the Acl.
		 * 
		 * @param sid The granted Sid
		 * @param permissions The allowed permissions
		 * @return provides a fluent interface
		 */
		public Builder allow(Sid sid, Permission... permissions)
		{
			for (Permission p : permissions) {
				entries.add(new AccessControlEntryDto(sid, p, true));
			}
			return this;
		}
		
		/**
		 * Adds several denying {@link AccessControlEntry}s to the Acl.
		 * 
		 * @param sid The denied Sid
		 * @param permissions The denied permissions
		 * @return provides a fluent interface
		 */
		public Builder deny(Sid sid, Permission... permissions)
		{
			for (Permission p : permissions) {
				entries.add(new AccessControlEntryDto(sid, p, false));
			}
			return this;
		}
		
		/**
		 * Sets whether the resulting Acl will use the parent for missing rules
		 * 
		 * @param entriesInheriting 
		 * @return provides a fluent interface
		 */
		public Builder setEntriesInheriting(boolean entriesInheriting)
		{
			this.entriesInheriting = entriesInheriting;
			return this;
		}
		
		/**
		 * Sets the strategy for allow/deny.
		 * 
		 * <p>If not specified, the Acl is given a new {@link DefaultPermissionGrantingStrategy}. 
		 * 
		 * @param permissionGrantingStrategy
		 * @return provides a fluent interface
		 */
		public Builder setPermissionGrantingStrategy(PermissionGrantingStrategy permissionGrantingStrategy)
		{
			this.permissionGrantingStrategy = Arguments.checkNull(permissionGrantingStrategy);
			return this;
		}
		
		/**
		 * Creates the built {@link Acl}.
		 * 
		 * @return the built Acl.
		 */
		public AccessControlList build()
		{
			PermissionGrantingStrategy pgs = permissionGrantingStrategy == null ?
				new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger()) :
				permissionGrantingStrategy;	
			return new AccessControlList(objectIdentity, parentAcl, loadedSids,
				entries, pgs, entriesInheriting);
		}
	}
}
