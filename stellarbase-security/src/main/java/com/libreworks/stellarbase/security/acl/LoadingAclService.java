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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.util.Assert;

/**
 * An AclService which delegates loading of AccessControlEntry objects to loaders.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class LoadingAclService extends AbstractAclService
{
	protected Collection<AccessControlEntryLoader> loaders = new ArrayList<AccessControlEntryLoader>();
	protected AclCache aclCache = new NoOpAclCache();
	protected ParentObjectIdentityResolver parentResolver = new NoOpParentObjectIdentityResolver();
	
	public List<ObjectIdentity> findChildren(ObjectIdentity arg0)
	{
		// if you want to override this, go right ahead
		return Collections.emptyList();
	}

	/**
	 * Gets an Acl based on AccessControlEntry objects from the loaders.
	 * 
	 * @param oid The ObjectIdentity
	 * @param sids The Sids (can be null)
	 * @return The Acl constructed
	 */
	protected Acl loadAcl(ObjectIdentity oid, List<Sid> sids)
	{
		Acl acl = aclCache.get(oid, sids);
		if ( acl == null ) {
			ObjectIdentity parentOid = parentResolver.getParent(oid);
			Acl parentAcl = parentOid != null ? loadAcl(parentOid, sids) : null;
			acl = new AclImpl(oid, parentAcl, sids);
			ArrayList<AccessControlEntry> aces = new ArrayList<AccessControlEntry>();
			for(AccessControlEntryLoader loader : loaders) {
				if ( loader.supports(oid) ) {
					aces.addAll(loader.getEntries(acl, oid, sids));
				}
			}
			((AclImpl)acl).setEntries(aces);
			aclCache.put(oid, sids, acl);
		}
		return acl;
	}
	
	public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids) throws NotFoundException
	{
		if ( sids == null ) {
			sids = Collections.emptyList();
		}
		HashMap<ObjectIdentity,Acl> map = new HashMap<ObjectIdentity, Acl>();
		try {
			for(ObjectIdentity oid : objects) {
				map.put(oid, loadAcl(oid, sids));
			}
		} catch ( DataRetrievalFailureException e ) {
			throw new NotFoundException("Could not load entity", e);
		}
		return map;
	}

	/**
	 * @param aclCache the aclCache to set
	 */
	public void setAclCache(AclCache aclCache)
	{
		Assert.notNull(aclCache);
		this.aclCache = aclCache;
	}

	/**
	 * Sets the AccessControlEntryLoader objects that will be used by this service.
	 * 
	 * @param loaders The ACE loaders
	 */
	public void setLoaders(Collection<? extends AccessControlEntryLoader> loaders)
	{
		Assert.notNull(loaders);
		this.loaders.addAll(loaders);
	}

	/**
     * @param parentResolver the parentResolver to set
     */
    public void setParentResolver(ParentObjectIdentityResolver parentResolver)
    {
    	Assert.notNull(parentResolver);
    	this.parentResolver = parentResolver;
    }
}
