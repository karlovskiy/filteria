package info.karlovskiy.filteria;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 1/3/16
 */
class FilterToken {
    private final String name;
    private final String value;
    private final OperationType operationType;

    FilterToken(String name, String value, OperationType operationType) {
        this.name = name;
        this.value = value;
        this.operationType = operationType;
    }

    String getName() {
        return name;
    }

    String getValue() {
        return value;
    }

    OperationType getOperationType() {
        return operationType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ParsedToken{");
        sb.append("name='").append(name).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append(", operation=").append(operationType);
        sb.append('}');
        return sb.toString();
    }
}
