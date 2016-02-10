package info.karlovskiy.filteria;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.StringType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 1/3/16
 */
@RunWith(MockitoJUnitRunner.class)
public class StringOperationsTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private ClassMetadata classMetadata;

    private Filteria filteria;

    @Before
    public void before() {
        when(classMetadata.getPropertyType(anyString())).thenReturn(StringType.INSTANCE);
        when(sessionFactory.getClassMetadata(String.class)).thenReturn(classMetadata);
        filteria = Filteria.create(sessionFactory, String.class);
    }

    @Test
    public void testParseEquals() {
        String filter = "name::todd|city::denver|title::grand poobah";
        List<FilterToken> actual = filteria.filter(filter).filterTokens();
        Assert.assertEquals(3, actual.size());
        assertTokenParsed(actual, "name", "todd", OperationType.EQUALS);
        assertTokenParsed(actual, "city", "denver", OperationType.EQUALS);
        assertTokenParsed(actual, "title", "grand poobah", OperationType.EQUALS);
    }

    @Test
    public void testParseEndsWith() {
        String filter = "name::*odd|city::*enver|title::*nd poobah";
        List<FilterToken> actual = filteria.filter(filter).filterTokens();
        Assert.assertEquals(3, actual.size());
        assertTokenParsed(actual, "name", "odd", OperationType.ENDS_WITH);
        assertTokenParsed(actual, "city", "enver", OperationType.ENDS_WITH);
        assertTokenParsed(actual, "title", "nd poobah", OperationType.ENDS_WITH);
    }

    @Test
    public void testParseStartsWith() {
        String filter = "name::to*|city::denv*|title::grand po*";
        List<FilterToken> actual = filteria.filter(filter).filterTokens();
        Assert.assertEquals(3, actual.size());
        assertTokenParsed(actual, "name", "to", OperationType.STARTS_WITH);
        assertTokenParsed(actual, "city", "denv", OperationType.STARTS_WITH);
        assertTokenParsed(actual, "title", "grand po", OperationType.STARTS_WITH);
    }

    @Test
    public void testParseContains() {
        String filter = "name::!todd|city::!denver|title::!grand poobah";
        List<FilterToken> actual = filteria.filter(filter).filterTokens();
        Assert.assertEquals(3, actual.size());
        assertTokenParsed(actual, "name", "todd", OperationType.NOT_EQUALS);
        assertTokenParsed(actual, "city", "denver", OperationType.NOT_EQUALS);
        assertTokenParsed(actual, "title", "grand poobah", OperationType.NOT_EQUALS);
    }

    @Test
    public void testNull() {
        String filter = "name::^null|city::!^null|title::^null";
        List<FilterToken> actual = filteria.filter(filter).filterTokens();
        Assert.assertEquals(3, actual.size());
        assertTokenParsed(actual, "name", "^null", OperationType.IS_NULL);
        assertTokenParsed(actual, "city", "^null", OperationType.IS_NOT_NULL);
        assertTokenParsed(actual, "title", "^null", OperationType.IS_NULL);
    }

    private void assertTokenParsed(List<FilterToken> tokens, String name, String value, OperationType operationType) {
        for (FilterToken token : tokens) {
            if (token.getName().equals(name) && token.getValue().equals(value) &&
                    token.getOperationType() == operationType) {
                return;
            }
        }
        Assert.fail("Token " + name + " with value " + value + " and type " + operationType + " not found, filterTokens: " + tokens);
    }
}
