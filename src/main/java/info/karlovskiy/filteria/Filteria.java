package info.karlovskiy.filteria;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
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

    private final SessionFactory sessionFactory;
    private final Class baseClass;
    private static final String TOKEN_DELIMITER = "\\|";
    private List<FilterToken> filterTokens = Collections.emptyList();
    private List<SorterToken> sorterTokens = Collections.emptyList();
    private Map<String, Property> propertyByName = new HashMap<>();

    private Filteria(SessionFactory sessionFactory, Class baseClass) {
        this.sessionFactory = sessionFactory;
        this.baseClass = baseClass;
    }

    /**
     * Filteria creation
     *
     * @param sessionFactory session factory
     * @return filteria
     */
    public static Filteria create(SessionFactory sessionFactory, Class baseClass) {
        return new Filteria(sessionFactory, baseClass);
    }

    /**
     * Adding parameter to property mapping
     *
     * @param name     parameter name
     * @param property object property path
     * @return self
     */
    public Filteria parameter(String name, String property) {
        Class clazz = propertyClass(property);
        Property prop = new Property(name, property, clazz);
        propertyByName.put(name, prop);
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
            Property prop = getProperty(name);
            String property = prop.getProperty();
            OperationType operationType = filterToken.getOperationType();
            String value = filterToken.getValue();
            Class valueClass = prop.getClazz();
            ValueType valueType = ValueType.valueOf(valueClass, operationType);
            Object convertedValue = valueType.convert(value, valueClass);
            switch (operationType) {
                case GREATER_OR_EQUALS:
                    criteria.add(Restrictions.ge(property, convertedValue));
                    break;
                case GREATER:
                    criteria.add(Restrictions.gt(property, convertedValue));
                    break;
                case LESS_OR_EQUALS:
                    criteria.add(Restrictions.le(property, convertedValue));
                    break;
                case LESS:
                    criteria.add(Restrictions.lt(property, convertedValue));
                    break;
                case CONTAINS:
                    criteria.add(Restrictions.ilike(property, value, MatchMode.ANYWHERE));
                    break;
                case ENDS_WITH:
                    criteria.add(Restrictions.ilike(property, value, MatchMode.END));
                    break;
                case STARTS_WITH:
                    criteria.add(Restrictions.ilike(property, value, MatchMode.START));
                    break;
                case IS_NOT_NULL:
                    criteria.add(Restrictions.isNotNull(property));
                    break;
                case NOT_EQUALS:
                    criteria.add(Restrictions.ne(property, convertedValue));
                    break;
                case IS_NULL:
                    criteria.add(Restrictions.isNull(property));
                    break;
                case EQUALS:
                    criteria.add(Restrictions.eq(property, convertedValue));
                    break;
                default:
                    throw new FilteriaException("Unknown token type " + operationType);
            }
            logger.info("Filteria {} operation for {} has been added to criteria", operationType, property);
        }
        // sorter
        for (SorterToken sorterToken : sorterTokens) {
            String name = sorterToken.getName();
            boolean desc = sorterToken.isDesc();
            Property prop = getProperty(name);
            String property = prop.getProperty();
            criteria.addOrder(desc ? Order.desc(property) : Order.asc(property));
            logger.info("Filteria sort by {} {} has been added to criteria", property, (desc ? "DESC" : "ASC"));
        }
        return this;
    }

    List<FilterToken> filterTokens() {
        return filterTokens;
    }

    List<SorterToken> sorterTokens() {
        return sorterTokens;
    }

    private Property getProperty(String name) {
        Property property = propertyByName.get(name);
        if (property == null) {
            throw new FilteriaException("Property for name " + name + " not found");
        }
        return property;
    }

    private Class propertyClass(String property) {
        String[] subPropertyNames = property.split("\\.");
        Class baseClass = this.baseClass;
        for (int i = 0; i < subPropertyNames.length; i++) {
            String subProperty = subPropertyNames[i];
            Type type = sessionFactory.getClassMetadata(baseClass).getPropertyType(subProperty);
            if (i == subPropertyNames.length - 1) {
                return type.getReturnedClass();
            }
            baseClass = type.getReturnedClass();
        }
        throw new FilteriaException("Property class " + property + " not found in the metadata");
    }

}
