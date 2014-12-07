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
import java.util.Map;

import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.util.Assert;

/**
 * An extremely simple implementation of {@link AclService}.
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class SimpleAclService extends AbstractAclService
{
	protected final RecursiveLookupStrategy lookupStrategy;

	/**
	 * @param lookupStrategy the lookupStrategy to use
	 */
	public SimpleAclService(RecursiveLookupStrategy lookupStrategy)
	{
		Assert.notNull(lookupStrategy);
		this.lookupStrategy = lookupStrategy;
	}
	
	public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids) throws NotFoundException
	{
		Map<ObjectIdentity,Acl> result = lookupStrategy.readAclsById(objects, sids, this);
		
		// Check every requested object identity was found (throw NotFoundException if needed)
		for (ObjectIdentity oid : objects) {
			if (!result.containsKey(oid)) {
				throw new NotFoundException("Unable to find ACL information for object identity '" + oid + "'");
			}
		}
		
		return result;
	}
}
