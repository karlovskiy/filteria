package info.karlovskiy.filteria;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Arrays;
import java.util.EnumSet;

import static info.karlovskiy.filteria.OperationType.CONTAINS;
import static info.karlovskiy.filteria.OperationType.ENDS_WITH;
import static info.karlovskiy.filteria.OperationType.EQUALS;
import static info.karlovskiy.filteria.OperationType.GREATER;
import static info.karlovskiy.filteria.OperationType.GREATER_OR_EQUALS;
import static info.karlovskiy.filteria.OperationType.IS_NOT_NULL;
import static info.karlovskiy.filteria.OperationType.IS_NULL;
import static info.karlovskiy.filteria.OperationType.LESS;
import static info.karlovskiy.filteria.OperationType.LESS_OR_EQUALS;
import static info.karlovskiy.filteria.OperationType.NOT_EQUALS;
import static info.karlovskiy.filteria.OperationType.STARTS_WITH;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 1/19/16
 */
enum ValueType {

    STRING(EQUALS, NOT_EQUALS, CONTAINS, STARTS_WITH, ENDS_WITH, IS_NOT_NULL, IS_NULL),
    NUMBER(EQUALS, NOT_EQUALS, GREATER, GREATER_OR_EQUALS, LESS, LESS_OR_EQUALS, IS_NOT_NULL, IS_NULL) {
        @Override
        Object convert(String value, Class clazz) {
            if ("^null".equals(value)) {
                return value;
            }
            try {
                if (clazz == Integer.class) {
                    Integer integer = Integer.valueOf(value);
                    return integer;
                } else if (clazz == BigDecimal.class) {
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                    //symbols.setGroupingSeparator(',');
                    symbols.setDecimalSeparator('.');
                    String pattern = "#,##0.##";
                    DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
                    decimalFormat.setParseBigDecimal(true);
                    return decimalFormat.parse(value);
                }
            } catch (NumberFormatException | ParseException e) {
                throw new FilteriaException("Error parsing " + value + " to " +
                        name().toLowerCase() + " for class " + clazz.getSimpleName(), e);
            }
            throw new IllegalArgumentException("Unsupported numeric type class " + clazz.getName());
        }
    };

    private final EnumSet<OperationType> operations;

    ValueType(OperationType... operations) {
        this.operations = EnumSet.copyOf(Arrays.asList(operations));
    }

    Object convert(String value, Class clazz) {
        return value;
    }

    static ValueType valueOf(Class clazz, OperationType operation) {
        ValueType valueType;
        if (clazz == String.class) {
            valueType = STRING;
        } else if (Number.class.isAssignableFrom(clazz)) {
            valueType = NUMBER;
        } else {
            throw new IllegalArgumentException("Unsupported value class " + clazz.getName());
        }
        if (!valueType.operations.contains(operation)) {
            throw new FilteriaException("Unsupported operation " + operation +
                    " for type " + valueType.name().toLowerCase());
        }
        return valueType;
    }
}
