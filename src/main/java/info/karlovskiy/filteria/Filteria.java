package info.karlovskiy.filteria;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
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
    private List<FilterToken> filterTokens = Collections.emptyList();
    private List<SorterToken> sorterTokens = Collections.emptyList();
    private Map<String, String> propertyByName = new HashMap<>();

    private Filteria() {
    }

    /**
     * Filteria creation
     *
     * @return filteria
     */
    public static Filteria create() {
        return new Filteria();
    }

    /**
     * Adding parameter to property mapping
     *
     * @param name     parameter name
     * @param property object property path
     * @return self
     */
    public Filteria parameter(String name, String property) {
        propertyByName.put(name, property);
        return this;
    }

    /**
     * Parsing filter
     *
     * @param filter filter
     * @return self
     */
    public Filteria filter(String filter) {
        if (StringUtils.isEmpty(filter)) {
            return this;
        }
        String[] tokens = filter.split(TOKEN_DELIMITER);
        List<FilterToken> filterTokens = new ArrayList<>();
        for (String token : tokens) {
            FilterToken parsed = null;
            tokenTypes:
            for (OperationType operationType : OperationType.values()) {
                List<Pattern> patterns = operationType.getPatterns();
                for (Pattern pattern : patterns) {
                    Matcher matcher = pattern.matcher(token);
                    if (matcher.find()) {
                        String name = matcher.group(1);
                        String value = matcher.group(2);
                        if (StringUtils.isNoneEmpty(name, value)) {
                            parsed = new FilterToken(name, value, operationType);
                            logger.debug("Parsed filter token {}", parsed);
                            filterTokens.add(parsed);
                            break tokenTypes;
                        }
                    }
                }
            }
            if (parsed == null) {
                throw new FilteriaException("Problem parsing token " + token);
            }

        }
        this.filterTokens = filterTokens;
        return this;
    }

    /**
     * Parsing sorter
     *
     * @param sorter sorter
     * @return self
     */
    public Filteria sorter(String sorter) {
        if (StringUtils.isEmpty(sorter)) {
            // sorter will be API requirement :) rather than filter
            throw new FilteriaException("Empty sorter string");
        }
        String[] tokens = sorter.split(TOKEN_DELIMITER);
        List<SorterToken> sorterTokens = new ArrayList<>();
        for (String token : tokens) {
            boolean desc = token.startsWith("-");
            String name = desc ? token.substring(1) : token;
            SorterToken parsed = new SorterToken(name, desc);
            logger.debug("Parsed sorter token {}", parsed);
            sorterTokens.add(parsed);
        }
        this.sorterTokens = sorterTokens;
        return this;
    }

    /**
     * Appending parsed filter and sorter to criteria
     *
     * @param criteria criteria
     * @return self
     */
    public Filteria appendTo(Criteria criteria) {
        // filter
        for (FilterToken filterToken : filterTokens) {
            String name = filterToken.getName();
            String property = getProperty(name);
            OperationType operationType = filterToken.getOperationType();
            String value = filterToken.getValue();
            switch (operationType) {
                case CONTAINS:
                    criteria.add(Restrictions.ilike(property, value, MatchMode.ANYWHERE));
                    break;
                case ENDS_WITH:
                    criteria.add(Restrictions.ilike(property, value, MatchMode.END));
                    break;
                case STARTS_WITH:
                    criteria.add(Restrictions.ilike(property, value, MatchMode.START));
                    break;
                case NOT_EQUALS:
                    criteria.add(Restrictions.ne(property, value));
                    break;
                case EQUALS:
                    criteria.add(Restrictions.eq(property, value));
                    break;
                default:
                    throw new FilteriaException("Unknown token type " + operationType);
            }
        }
        // sorter
        for (SorterToken sorterToken : sorterTokens) {
            String name = sorterToken.getName();
            boolean desc = sorterToken.isDesc();
            String property = getProperty(name);
            criteria.addOrder(desc ? Order.desc(property) : Order.asc(property));
        }
        return this;
    }

    List<FilterToken> filterTokens() {
        return filterTokens;
    }

    List<SorterToken> sorterTokens() {
        return sorterTokens;
    }

    private String getProperty(String name) {
        String property = propertyByName.get(name);
        if (property == null) {
            throw new FilteriaException("Property for name " + name + " not found");
        }
        return property;
    }

}
