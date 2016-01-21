package info.karlovskiy.filteria.metadata;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Map;

/**
 * Here will be javadoc
 * todo: maybe mocking will be better ?
 *
 * @author karlovskiy
 * @since 1.0, 1/21/16
 */
abstract class AbstractClassMetadata implements ClassMetadata {
    @Override
    public String getEntityName() {
        return null;
    }

    @Override
    public String getIdentifierPropertyName() {
        return null;
    }

    @Override
    public String[] getPropertyNames() {
        return new String[0];
    }

    @Override
    public Type getIdentifierType() {
        return null;
    }

    @Override
    public Type[] getPropertyTypes() {
        return new Type[0];
    }

    @Override
    public Type getPropertyType(String propertyName) throws HibernateException {
        return null;
    }

    @Override
    public boolean hasProxy() {
        return false;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public boolean isVersioned() {
        return false;
    }

    @Override
    public int getVersionProperty() {
        return 0;
    }

    @Override
    public boolean[] getPropertyNullability() {
        return new boolean[0];
    }

    @Override
    public boolean[] getPropertyLaziness() {
        return new boolean[0];
    }

    @Override
    public boolean hasIdentifierProperty() {
        return false;
    }

    @Override
    public boolean hasNaturalIdentifier() {
        return false;
    }

    @Override
    public int[] getNaturalIdentifierProperties() {
        return new int[0];
    }

    @Override
    public boolean hasSubclasses() {
        return false;
    }

    @Override
    public boolean isInherited() {
        return false;
    }

    @Override
    public Object[] getPropertyValuesToInsert(Object entity, Map mergeMap, SessionImplementor session) throws HibernateException {
        return new Object[0];
    }

    @Override
    public Class getMappedClass() {
        return null;
    }

    @Override
    public Object instantiate(Serializable id, SessionImplementor session) {
        return null;
    }

    @Override
    public Object getPropertyValue(Object object, String propertyName) throws HibernateException {
        return null;
    }

    @Override
    public Object[] getPropertyValues(Object entity) throws HibernateException {
        return new Object[0];
    }

    @Override
    public void setPropertyValue(Object object, String propertyName, Object value) throws HibernateException {

    }

    @Override
    public void setPropertyValues(Object object, Object[] values) throws HibernateException {

    }

    @Override
    public Serializable getIdentifier(Object object) throws HibernateException {
        return null;
    }

    @Override
    public Serializable getIdentifier(Object entity, SessionImplementor session) {
        return null;
    }

    @Override
    public void setIdentifier(Object entity, Serializable id, SessionImplementor session) {

    }

    @Override
    public boolean implementsLifecycle() {
        return false;
    }

    @Override
    public Object getVersion(Object object) throws HibernateException {
        return null;
    }
}
