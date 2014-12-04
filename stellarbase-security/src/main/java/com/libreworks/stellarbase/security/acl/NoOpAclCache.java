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

import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;

/**
 * An AclCache which does nothing.
 * 
 * @author Jonathan Hawk
 */
public class NoOpAclCache implements AclCache
{
	public boolean contains(ObjectIdentity objId, List<Sid> sids)
	{
		return false;
	}

	public Acl get(ObjectIdentity objId, List<Sid> sids)
	{
		return null;
	}

	public AclCache put(ObjectIdentity objId, List<Sid> sids, Acl acl)
	{
		return this;
	}
}
