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

import net.libreworks.stellarbase.model.Identifiable;

import org.springframework.security.acls.AclEntryVoter;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.Permission;

/**
 * An extension to AclEntryVoter which also supports direct use of Identifiable
 * instead of just method invocations.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class IdentifiableAclEntryVoter extends AclEntryVoter
{
	/**
	 * Creates a new ACL entry voter
	 * 
	 * @param aclService The ACL service
	 * @param processConfigAttribute The config attributes
	 * @param requirePermission The permissions required
	 */
	public IdentifiableAclEntryVoter(AclService aclService, String processConfigAttribute, Permission[] requirePermission)
	{
		super(aclService, processConfigAttribute, requirePermission);
		setProcessDomainObjectClass(Identifiable.class);
	}
	
	@Override
	public boolean supports(Class<?> cls)
	{
		return super.supports(cls) ||
			getProcessDomainObjectClass().isAssignableFrom(cls);
	}
}
