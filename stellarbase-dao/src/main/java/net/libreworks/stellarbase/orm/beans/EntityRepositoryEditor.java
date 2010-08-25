package net.libreworks.stellarbase.orm.beans;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.util.Assert;

import net.libreworks.stellarbase.orm.model.EntityRepository;
import net.libreworks.stellarbase.orm.model.Identifiable;

/**
 * A PropertyEditor that converts identifiers into entities using an {@link EntityRepository}.
 * 
 * @author Jonathan Hawk
 * @param <T> The type of entity
 */
public class EntityRepositoryEditor<T extends Identifiable<?>> extends PropertyEditorSupport
{
    protected EntityRepository<T,?> repository;
    protected ConversionService conversionService = ConversionServiceFactory.createDefaultConversionService();
    
    /**
     * Creates a new PropertyEditor for entities using their identifiers.
     * 
     * @param repository The backing repository in which to find the entities
     */
    public EntityRepositoryEditor(EntityRepository<T,?> repository)
    {
        Assert.notNull(repository);
        this.repository = repository;
    }

    @SuppressWarnings("unchecked")
    public String getAsText()
    {
        return ObjectUtils.toString(((T)getValue()).getId());
    }
}
