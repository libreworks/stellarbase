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
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * A wrapper for an ACL to allow boolean testing instead of exceptions
 * 
 * @author Jonathan Hawk
 */
public class AccessDecider
{
	private final PermissionEvaluator permissionEvaluator;
	private final Object domainObject;
	private final Authentication authentication;
	
	/**
	 * Creates a new AccessDecider.
	 * 
	 * The currently authenticated user will be used to determine the Sids.
	 * 
	 * @param permissionEvaluator The object to evaluate permissions
	 */
	public AccessDecider(PermissionEvaluator permissionEvaluator, Object domainObject)
	{
		this.permissionEvaluator = permissionEvaluator;
		this.domainObject = domainObject;
		this.authentication = SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * Creates a new AccessDecider.
	 * 
	 * The currently authenticated user will be used to determine the Sids.
	 * 
	 * @param permissionEvaluator The object to evaluate permissions
	 */
	public AccessDecider(PermissionEvaluator permissionEvaluator, Object domainObject, Authentication authentication)
	{
		this.permissionEvaluator = permissionEvaluator;
		this.domainObject = domainObject;
		this.authentication = authentication;
	}	
	
	/**
	 * Whether the current authenticated user is granted all of the permissions.
	 * 
	 * @param permission The permissions to test
	 * @return Whether the authenticated user is allowed
	 */
	public boolean isAllowed(Permission... permission)
	{
		return domainObject instanceof ObjectIdentity ?
			permissionEvaluator.hasPermission(authentication, ((ObjectIdentity)domainObject).getIdentifier(), ((ObjectIdentity)domainObject).getType(), permission) :
			permissionEvaluator.hasPermission(authentication, domainObject, permission);
	}
}
