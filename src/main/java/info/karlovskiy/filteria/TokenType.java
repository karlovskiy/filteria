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
public enum TokenType {

    NOT_EQUALS("(.*)::!(.*)"),
    CONTAINS("(.*)::\\*(.*)\\*"),
    STARTS_WITH("(.*)::(.*)\\*"),
    ENDS_WITH("(.*)::\\*(.*)"),
    EQUALS("(.*)::(.*)");// must always be last one

    private final List<Pattern> patterns;

    TokenType(String... patterns) {
        List<Pattern> list = new ArrayList<>();
        for (String pattern : patterns) {
            list.add(Pattern.compile(pattern));
        }
        this.patterns = Collections.unmodifiableList(list);
    }

    public List<Pattern> getPatterns() {
        return patterns;
    }
}
