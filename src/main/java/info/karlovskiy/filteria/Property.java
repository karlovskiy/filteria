package info.karlovskiy.filteria;

import java.util.Objects;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 2/8/16
 */
class Property {
    private final String name;
    private final String property;
    private final Class clazz;

    public Property(String name, String property, Class clazz) {
        this.name = name;
        this.property = property;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public String getProperty() {
        return property;
    }

    public Class getClazz() {
        return clazz;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Property{");
        sb.append("name='").append(name).append('\'');
        sb.append(", property='").append(property).append('\'');
        sb.append(", clazz=").append(clazz);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Property)) return false;
        Property property = (Property) o;
        return Objects.equals(getName(), property.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
