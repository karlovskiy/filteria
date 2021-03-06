package info.karlovskiy.filteria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 1/3/16
 */
enum OperationType {

    LESS_OR_EQUALS("(.*)::<=(.*)"),
    LESS("(.*)::<(.*)"),
    GREATER_OR_EQUALS("(.*)::>=(.*)"),
    GREATER("(.*)::>(.*)"),
    IS_NOT_NULL("(.*)::!(\\^null)"),
    NOT_EQUALS("(.*)::!(.*)"),
    CONTAINS("(.*)::\\*(.*)\\*"),
    STARTS_WITH("(.*)::(.*)\\*"),
    ENDS_WITH("(.*)::\\*(.*)"),
    IS_NULL("(.*)::(\\^null)"),
    EQUALS("(.*)::(.*)");// must always be last one

    private final List<Pattern> patterns;

    OperationType(String... patterns) {
        List<Pattern> list = new ArrayList<>();
        for (String pattern : patterns) {
            list.add(Pattern.compile(pattern));
        }
        this.patterns = Collections.unmodifiableList(list);
    }

    List<Pattern> getPatterns() {
        return patterns;
    }
}
