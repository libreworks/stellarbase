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

import java.io.Serializable;
import java.util.List;

import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;

/**
 * A non-thread safe HashMap-backed Acl Cache.
 * 
 * This class is intended to be used as a request-scoped proxy through the
 * Spring applicationContext. It is <em>not</em> thread safe and has no
 * behavior for expiration.
 * 
 * @author Jonathan Hawk
 */
public class MapAclCache implements AclCache, Serializable
{
	private static final long serialVersionUID = 1L;
	
	final private Table<ObjectIdentity, List<Sid>, Acl> acls = HashBasedTable.create();

	public boolean contains(ObjectIdentity objId, List<Sid> sids)
	{
		return acls.contains(objId, sids);
	}

	public Acl get(ObjectIdentity objId, List<Sid> sids)
	{
		return acls.get(objId, sids);
	}

	public AclCache put(ObjectIdentity objId, List<Sid> sids, Acl acl)
	{
		acls.put(objId, sids == null ? null : ImmutableList.copyOf(sids), acl);
		return this;
	}
}
