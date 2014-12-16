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
package com.libreworks.stellarbase.persistence.model;

import java.io.Serializable;

/**
 * Base interface for entities which can <em>become</em> read-only or finalized.
 * 
 * <p>The general contract here is that so long as an entity is finalized, it
 * should prevent writes entirely. It may vary from entity to entity whether
 * they could become writable again (e.g. a submit and un-submit process).
 *
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public interface Finalizable<K extends Serializable> extends Identifiable<K>
{
	/**
	 * @return whether this entity has been finalized
	 */
	boolean isFinalized();
}
