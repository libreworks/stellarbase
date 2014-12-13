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

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.model.Permission;

import com.libreworks.stellarbase.util.Arguments;

/**
 * A service to generate or query {@link AccessDecider} objects.
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class AccessDeciderService
{
	protected final PermissionEvaluator permissionEvaluator;
	
	/**
	 * Creates a new AccessDeciderService.
	 * 
	 * @param permissionEvaluator The {@link PermissionEvaluator} passed to new {@link AccessDecider} instances
	 */
	public AccessDeciderService(PermissionEvaluator permissionEvaluator)
	{
		this.permissionEvaluator = Arguments.checkNull(permissionEvaluator, "permissionEvaluator must not be null");
	}
	
	/**
	 * Gets an AccessDecider for a domain object (also supports {@link org.springframework.security.acls.model.ObjectIdentity}).
	 * 
	 * @param domainObject The domain object (or {@code ObjectIdentity})
	 * @return the AccessDecider
	 */
	public AccessDecider get(Object domainObject)
	{
		return new AccessDecider(permissionEvaluator, domainObject);
	}
	
	/**
	 * Does a single permission check for a domain object (also supports {@link org.springframework.security.acls.model.ObjectIdentity}).
	 *  
	 * @param domainObject The domain object (or {@code ObjectIdentity})
	 * @param permission the permission to check
	 * @return whether the authenticated Principal is granted one of the supplied Permissions
	 */
	public boolean can(Object domainObject, Permission... permission)
	{
		return (new AccessDecider(permissionEvaluator, domainObject)).isAllowed(permission);
	}	
}
