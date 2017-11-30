package org.springframework.contrib.gae.objectify.support;

import org.springframework.contrib.gae.objectify.ObjectifyProxy;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.data.repository.core.EntityInformation;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Encapsulates information about an Objectify entity.
 *
 * @param <E> Entity type.
 * @param <I> Entity id type.
 */
public class ObjectifyEntityInformation<E, I extends Serializable> implements EntityInformation<E, I> {
    private ObjectifyProxy objectify;

    private Class<E> entityType;

    private Field idField;
    private Class<I> idType;


    /**
     * @param objectify  Objectify proxy.
     * @param entityType The entity to return information about.
     */
    public ObjectifyEntityInformation(ObjectifyProxy objectify, Class<E> entityType) {
        this.objectify = objectify;
        this.entityType = entityType;

        this.idField = findIdField();
        this.idType = findIdType();
    }

    @Override
    public boolean isNew(Object entity) {
        return getId(entity) != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public I getId(Object entity) {
        try {
            return (I) getIdField().get(entity);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("Cannot get id for entity type %s - %s", entityType.getSimpleName(), entity), e);
        }
    }

    public Field getIdField() {
        return idField;
    }

    @Override
    public Class<I> getIdType() {
        return idType;
    }

    @Override
    public Class<E> getJavaType() {
        return entityType;
    }

    private Field findIdField() {
        String idFieldName = objectify.ofy().factory().getMetadata(entityType).getKeyMetadata().getIdFieldName();
        return FieldUtils.getField(entityType, idFieldName, true);
    }

    @SuppressWarnings("unchecked")
    private Class<I> findIdType() {
        return (Class<I>) this.idField.getType();
    }
}
