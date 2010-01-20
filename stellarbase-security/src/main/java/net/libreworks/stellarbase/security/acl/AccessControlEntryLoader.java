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

import java.util.Collection;
import java.util.List;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;

/**
 * Provides the mechanism to retrieve the Access Control Entries for an entity.
 * 
 * A typical implementation of this class will load rules from a database,
 * model, or some other configuration source.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public interface AccessControlEntryLoader
{
	/**
	 * Whether this loader supports loading rules for the ObjectIdentity supplied.
	 * 
	 * @param oid The ObjectIdentity
	 * @return Whether this loader supports rules for the ObjectIdentity
	 */
	public boolean supports(ObjectIdentity oid);
	
	/**
	 * Loads the AccessControlEntries for the ACL and entity provided.
	 * 
	 * The AccessControlEntry objects will have their ACL set to be the one
	 * provided. It is up to the caller of this method to actually add these
	 * entries into the ACL.
	 * 
	 * The {@code sids} parameter should be able to be null, so that when a list
	 * of sids aren't provided, the loader will retrieve all ACL rules for the
	 * entity.
	 * 
	 * Avoid any kind of caching strategy as the AclService itself should be the
	 * one doing that kind of behavior.
	 * 
	 * @param acl The ACL
	 * @param oid The ObjectIdentity whose authorization rules will be loaded
	 * @param sids The specific sids to which the rules will apply
	 * @return The entries loaded
	 */
	public List<AccessControlEntry> getEntries(Acl acl, ObjectIdentity oid, Collection<Sid> sids);
}
