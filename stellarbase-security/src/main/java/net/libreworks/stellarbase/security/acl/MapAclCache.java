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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;

/**
 * A non-thread safe HashMap-backed Acl Cache.
 * 
 * This class is intended to be used as a request-scoped proxy through the
 * Spring applicationContext. It is <em>not</em> thread safe and has no
 * behavior for expiration.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class MapAclCache implements AclCache, Serializable
{
	private static final long serialVersionUID = 1L;
	
	final private Map<ObjectIdentity,Map<Collection<Sid>,Acl>> acls =
		new HashMap<ObjectIdentity,Map<Collection<Sid>,Acl>>();

	public boolean contains(ObjectIdentity objId, Collection<Sid> sids)
	{
		return acls.containsKey(objId) && acls.get(objId).containsKey(sids);
	}

	public Acl get(ObjectIdentity objId, Collection<Sid> sids)
	{
		return acls.containsKey(objId) ? acls.get(objId).get(sids) : null;
	}

	public AclCache put(ObjectIdentity objId, Collection<Sid> sids, Acl acl)
	{
		if ( !acls.containsKey(objId) ) {
			acls.put(objId, new HashMap<Collection<Sid>, Acl>());
		}
		acls.get(objId).put(sids, acl);
		return this;
	}
}
