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

import java.util.List;

import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;

/**
 * A container for Acls a little more abstract than the one included with Spring.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public interface AclCache
{
	/**
	 * Whether the cache has an entry for the ObjectIdentity supplied.
	 * 
	 * @param objId The ObjectIdentity
	 * @param sids The SIDs
	 * @return Whether the ACL is available
	 */
	public boolean contains(ObjectIdentity objId, List<Sid> sids);
	
	/**
	 * Gets the ACL for an ObjectIdentity or null if one is not available.
	 * 
	 * @param objId The ObjectIdentity
	 * @param sids The Sids
	 * @return The ACL or null
	 */
	public Acl get(ObjectIdentity objId, List<Sid> sids);
	
	/**
	 * Puts an Acl in the cache.
	 * 
	 * @param objId The ObjectIdentity
	 * @param sids The Sids
	 * @param acl The ACL
	 * @return provides a fluent interface 
	 */
	public AclCache put(ObjectIdentity objId, List<Sid> sids, Acl acl);
}
