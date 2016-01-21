package info.karlovskiy.filteria.metadata;

import org.hibernate.HibernateException;
import org.hibernate.type.IntegerType;
import org.hibernate.type.Type;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 1/21/16
 */
public class IntegerClassMetadata extends AbstractClassMetadata {

    @Override
    public Type getPropertyType(String propertyName) throws HibernateException {
        return IntegerType.INSTANCE;
    }
}
