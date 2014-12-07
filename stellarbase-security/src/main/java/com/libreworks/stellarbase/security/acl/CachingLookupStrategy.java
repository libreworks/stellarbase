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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.util.Assert;

import com.google.common.collect.ImmutableMap;

/**
 * A decorator which caches Acls returned from a delegate LookupStrategy.
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class CachingLookupStrategy implements RecursiveLookupStrategy
{
	private final RecursiveLookupStrategy delegate;
	private final AclCache aclCache;

	/**
	 * Creates a new CachingLookupStrategy.
	 * 
	 * @param delegate The delegate that returns actual ACLs
	 * @param aclCache The cache in which to store ACLs
	 */
	public CachingLookupStrategy(RecursiveLookupStrategy delegate, AclCache aclCache)
	{
		Assert.notNull(delegate);
		Assert.notNull(aclCache);
		this.delegate = delegate;
		this.aclCache = aclCache;
	}
	
	public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids, AclService aclService)
	{
		LinkedList<ObjectIdentity> oids = new LinkedList<ObjectIdentity>(objects);
		ImmutableMap.Builder<ObjectIdentity,Acl> b = ImmutableMap.builder();
		for (ObjectIdentity objectIdentity : objects) {
			if (aclCache.contains(objectIdentity, sids)) {
				b.put(objectIdentity, aclCache.get(objectIdentity, sids));
				oids.remove(objectIdentity);
			}
		}
		Map<ObjectIdentity,Acl> results = delegate.readAclsById(oids, sids, aclService);
		for (Map.Entry<ObjectIdentity,Acl> entry : results.entrySet()) {
			aclCache.put(entry.getKey(), sids, entry.getValue());
		}
		return b.putAll(results).build();
	}
}
