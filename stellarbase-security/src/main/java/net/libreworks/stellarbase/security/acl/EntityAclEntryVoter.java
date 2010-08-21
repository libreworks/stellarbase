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
package net.libreworks.stellarbase.security.acl;

import org.springframework.security.acls.AclEntryVoter;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.Permission;

/**
 * An extension to AclEntryVoter which also supports direct use of an entity
 * class instead of just method invocations.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class EntityAclEntryVoter extends AclEntryVoter
{
	/**
	 * Creates a new ACL entry voter
	 * 
	 * @param aclService The ACL service
	 * @param processConfigAttribute The config attributes
	 * @param requirePermission The permissions required
	 */
	public EntityAclEntryVoter(AclService aclService, String processConfigAttribute, Permission[] requirePermission, Class<?> domainObjectClass)
	{
		super(aclService, processConfigAttribute, requirePermission);
		setProcessDomainObjectClass(domainObjectClass);
	}
	
	@Override
	protected Object getDomainObjectInstance(Object secureObject)
	{
        return getProcessDomainObjectClass().isInstance(secureObject) ? 
        	secureObject : super.getDomainObjectInstance(secureObject);
    }
	
	@Override
	public boolean supports(Class<?> cls)
	{
		return super.supports(cls) ||
			getProcessDomainObjectClass().isAssignableFrom(cls);
	}
}
