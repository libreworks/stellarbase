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

import com.google.common.collect.ImmutableList;

/**
 * An {@link org.springframework.security.acls.model.Acl} with no entries.
 * 
 * <p>Calls to 
 * {@link EmptyAccessControlList#isGranted(List, List, boolean)} will delegate
 * to the {@code parentAcl} if one is provided and {@code entriesInheriting} is
 * true. Otherwise, a {@link NotFoundException} will be thrown.   
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class EmptyAccessControlList extends AbstractAcl
{
	private static final long serialVersionUID = 1L;
	private static final EmptyPermissionGrantingStrategy STRATEGY = new EmptyPermissionGrantingStrategy();
	
	/**
	 * Creates a new EmptyAccessControlList with {@code entriesInheriting} as true.
	 * 
     * @param objectIdentity The object for which we have ACL rules
     * @param parentAcl The parent ACL (nullable)
     * @param loadedSids The list of Sids whose rules are in this ACL (nullable)
	 */
	public EmptyAccessControlList(ObjectIdentity objectIdentity, Acl parentAcl, List<Sid> loadedSids)
	{
		this(objectIdentity, parentAcl, loadedSids, true);
	}
	
	/**
	 * Creates a new EmptyAccessControlList.
	 * 
     * @param objectIdentity The object for which we have ACL rules
     * @param parentAcl The parent ACL (nullable)
     * @param loadedSids The list of Sids whose rules are in this ACL (nullable)
     * @param entriesInheriting Whether permissions should be found in the parent ACL
	 */
	public EmptyAccessControlList(ObjectIdentity objectIdentity, Acl parentAcl, List<Sid> loadedSids, boolean entriesInheriting)
	{
		super(objectIdentity, parentAcl, loadedSids, STRATEGY, entriesInheriting);
	}

	@Override
	public List<AccessControlEntry> getEntries()
	{
		return ImmutableList.of();
	}
	
	static class EmptyPermissionGrantingStrategy implements PermissionGrantingStrategy
	{
		@Override
		public boolean isGranted(Acl acl, List<Permission> permission, List<Sid> sids, boolean administrativeMode) throws NotFoundException
		{
			if (acl.isEntriesInheriting() && acl.getParentAcl() != null) {
				return acl.getParentAcl().isGranted(permission, sids, administrativeMode);
			}
			throw new NotFoundException("Unable to locate a matching ACE for passed permissions and SIDs");
		}
	}
}
