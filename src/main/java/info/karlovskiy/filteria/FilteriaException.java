package info.karlovskiy.filteria;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 1/3/16
 */
public class FilteriaException extends RuntimeException {

    public FilteriaException(String message) {
        super(message);
    }

    public FilteriaException(String message, Throwable cause) {
        super(message, cause);
    }
}
