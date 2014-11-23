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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

/**
 * A wrapper for an ACL to allow boolean testing instead of exceptions
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class AccessDecider
{
	private Acl acl;
	private ArrayList<Sid> sids = new ArrayList<Sid>();
	
	/**
	 * Creates a new AccessDecider.
	 * 
	 * The currently authenticated user will be used to determine the Sids.
	 * 
	 * @param acl The ACL
	 */
	public AccessDecider(Acl acl)
	{
		this(acl, new SidRetrievalStrategyImpl().getSids(
				SecurityContextHolder.getContext().getAuthentication()));
	}
	
	/**
	 * Creates a new AccessDecider.
	 * 
	 * @param acl The ACL. Cannot be null.
	 * @param sids The Sids of the users. Cannot be null.
	 */
	public AccessDecider(Acl acl, List<Sid> sids)
	{
		Assert.notNull(acl);
		Assert.notNull(sids);
		this.acl = acl;
		this.sids.addAll(sids);
	}
	
	/**
	 * Whether the current authenticated user is granted all of the permissions.
	 * 
	 * @param permission The permissions to test
	 * @return Whether the authenticated user is allowed
	 */
	public boolean isAllowed(Permission... permission)
	{
		try {
			return acl.isGranted(Arrays.asList(permission), sids, false);
		} catch ( NotFoundException nfe ) {
			// just return false below
		}
		return false;
	}
	
	/**
	 * Creates an AccessDecider.
	 * 
	 * @param aclService The ACL Service from which the ACL will be retrieved
	 * @param entity The domain entity or an {@link ObjectIdentity}.
	 * @return The AccessDecider created
	 */
	public static AccessDecider factory(AclService aclService, Object entity)
	{
		return factory(aclService, entity, new SidRetrievalStrategyImpl());
	}
	
	/**
	 * Creates an AccessDecider.
	 * 
	 * @param aclService The ACL Service from which the ACL will be retrieved
	 * @param entity The domain entity or an {@link ObjectIdentity}.
	 * @param sidRetrievalStrategy The {@link Sid} retrieval strategy
	 * @return The AccessDecider created
	 */
	public static AccessDecider factory(AclService aclService, Object entity, SidRetrievalStrategy sidRetrievalStrategy)
	{
		List<Sid> sids = sidRetrievalStrategy.getSids(
			SecurityContextHolder.getContext().getAuthentication());
		return new AccessDecider(aclService.readAclById(
			entity instanceof ObjectIdentity ? (ObjectIdentity)entity :
				new ObjectIdentityImpl(entity), sids), sids);
	}
}
