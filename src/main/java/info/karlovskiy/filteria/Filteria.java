package info.karlovskiy.filteria;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 1/3/16
 */
public class Filteria {

    private static final Logger logger = LoggerFactory.getLogger(Filteria.class);

    private static final String TOKEN_DELIMITER = "\\|";
    private List<ParsedToken> parsedTokens = Collections.emptyList();

    public Filteria parse(String filter) {
        if (StringUtils.isEmpty(filter)) {
            throw new FilteriaException("Empty filter string");
        }
        String[] tokens = filter.split(TOKEN_DELIMITER);
        List<ParsedToken> parsedTokens = new ArrayList<>();
        for (String token : tokens) {
            ParsedToken parsed = null;
            tokenTypes:
            for (TokenType tokenType : TokenType.values()) {
                List<Pattern> patterns = tokenType.getPatterns();
                for (Pattern pattern : patterns) {
                    Matcher matcher = pattern.matcher(token);
                    if (matcher.find()) {
                        String name = matcher.group(1);
                        String value = matcher.group(2);
                        if (StringUtils.isNoneEmpty(name, value)) {
                            parsed = new ParsedToken(name, value, tokenType);
                            logger.debug("Parsed token {}", parsed);
                            parsedTokens.add(parsed);
                            break tokenTypes;
                        }
                    }
                }
            }
            if (parsed == null) {
                throw new FilteriaException("Problem parsing token " + token);
            }

        }
        this.parsedTokens = parsedTokens;
        return this;
    }

    public Filteria appendTo(Criteria criteria) {
        return this;
    }

    public List<ParsedToken> tokens() {
        return parsedTokens;
    }

}
