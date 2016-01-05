package info.karlovskiy.filteria;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Map<String, String> propertyByName = new HashMap<>();

    private Filteria() {
    }

    public static Filteria create() {
        return new Filteria();
    }

    public Filteria parameter(String name, String propety) {
        propertyByName.put(name, propety);
        return this;
    }

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
        for (ParsedToken parsedToken : parsedTokens) {
            String name = parsedToken.getName();
            String path = propertyByName.get(name);
            if (path == null) {
                throw new FilteriaException("Property for name " + name + " not found");
            }
            TokenType tokenType = parsedToken.getTokenType();
            String value = parsedToken.getValue();
            switch (tokenType) {
                case CONTAINS:
                    criteria.add(Restrictions.ilike(path, value, MatchMode.ANYWHERE));
                    break;
                case ENDS_WITH:
                    criteria.add(Restrictions.ilike(path, value, MatchMode.END));
                    break;
                case STARTS_WITH:
                    criteria.add(Restrictions.ilike(path, value, MatchMode.START));
                    break;
                case NOT_EQUALS:
                    criteria.add(Restrictions.ne(path, value));
                    break;
                case EQUALS:
                    criteria.add(Restrictions.eq(path, value));
                    break;
                default:
                    throw new FilteriaException("Unknown token type " + tokenType);
            }
        }
        return this;
    }

    public List<ParsedToken> tokens() {
        return parsedTokens;
    }

}
