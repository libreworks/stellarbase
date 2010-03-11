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

import org.springframework.security.acls.model.ObjectIdentity;

/**
 * Resolves the parent ObjectIdentity of a child. 
 * 
 * This class is primarily used by the {@link LoadingAclService} to load parent
 * ACLs. 
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public interface ParentObjectIdentityResolver
{
	/**
	 * Gets the parent ObjectIdentity for the one provided.
	 * 
	 * @param child The child ObjectIdentity
	 * @return The ObjectIdentity representing the parent
	 */
	public ObjectIdentity getParent(ObjectIdentity child);
}
