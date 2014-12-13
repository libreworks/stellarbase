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
package com.libreworks.stellarbase.orm.beans;

import java.beans.PropertyEditorSupport;
import java.io.Serializable;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import com.libreworks.stellarbase.orm.model.EntityRepository;
import com.libreworks.stellarbase.orm.model.Identifiable;
import com.libreworks.stellarbase.util.Arguments;

/**
 * A PropertyEditor that converts identifiers into entities using an {@link EntityRepository}.
 * 
 * Use the {@link #factory(EntityRepository)} method to create a new instance. 
 * 
 * @author Jonathan Hawk
 * @version $Id$
 * @param <T> The type of entity
 * @param <K> The type of identifier
 */
public class EntityRepositoryEditor<T extends Identifiable<K>, K extends Serializable> extends PropertyEditorSupport
{
    protected EntityRepository<T,K> repository;
    
    protected ConversionService conversionService = new DefaultConversionService();

    /**
     * Creates a new PropertyEditor for entities using their identifiers.
     * 
     * @param repository The backing repository in which to find the entities
     */
    protected EntityRepositoryEditor(EntityRepository<T,K> repository)
    {
        this.repository = Arguments.checkNull(repository);
    }

    /**
     * A convenience method to register multiple property editors.
     * 
     * This method will go through each repository provided and create an
     * {@link EntityRepositoryEditor} using the {@link #factory(EntityRepository)}
     * method, and register it with the {@code propertyEditor}.
     * 
     * @param propertyEditor The editor to which the editors will be registered
     * @param repos The repositories
     */
    public static void register(PropertyEditorRegistry propertyEditor, EntityRepository<?,?>... repos)
    {
    	Arguments.checkNull(propertyEditor, "propertyEditor must not be null");
    	for(EntityRepository<?,?> repo : repos) {
    		propertyEditor.registerCustomEditor(repo.getEntityClass(),
    				factory(repo));
    	}
    }
    
    /**
     * Creates a new PropertyEditor for entities using their identifiers.
     * 
     * @param repository The backing repository in which to find the entities
     */
    public static <A extends Identifiable<B>, B extends Serializable> EntityRepositoryEditor<A,B> factory(EntityRepository<A,B> repository)
    {
    	return new EntityRepositoryEditor<A,B>(repository);
    }
    
    @Override
    public String getAsText()
    {
    	Identifiable<?> entity = (Identifiable<?>) getValue();
		return entity != null ? ObjectUtils.toString(entity.getId())
				: StringUtils.EMPTY;
    }
    
    @Override
    public void setAsText(String text)
    {
    	setValue(StringUtils.isBlank(text) ? null :
    		repository.convert(conversionService.convert(text,
					repository.getIdentifierClass())));
    }

    @Override
    public void setValue(Object value)
    {
		if (value != null
				&& !repository.getEntityClass().isAssignableFrom(
						value.getClass())) {
    		setAsText(value.toString());
    	} else {
    		super.setValue(value);
    	}
    }
}
