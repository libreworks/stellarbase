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

import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;

/**
 * Very similar to {@link LookupStrategy}, but allows for recursive parent lookups.
 *  
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public interface RecursiveLookupStrategy
{
    /**
     * Perform an optimized ACL lookup.
     * 
     * <p>Implementing classes must not throw
     * {@link org.springframework.security.acls.model.NotFoundException}, nor
     * any other kind of exception if an ObjectIdentity cannot be retrieved.
     *
     * @param objects the identities to lookup (required)
     * @param sids the SIDs for which identities are required (may be {@code null}, implementations may elect not to provide SID optimizations)
     * 
     * @return a Map where keys represent the {@link ObjectIdentity} of the located {@link Acl} and values
     *         are the located {@link Acl} (never {@code null} although some entries may be missing)
     */
    Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids, AclService aclService);
}
