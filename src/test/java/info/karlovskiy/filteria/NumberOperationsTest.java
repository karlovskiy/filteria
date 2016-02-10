package info.karlovskiy.filteria;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.IntegerType;
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
 * @since 1.0, 1/21/16
 */
@RunWith(MockitoJUnitRunner.class)
public class NumberOperationsTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private ClassMetadata classMetadata;

    private Filteria filteria;

    @Before
    public void before() {
        when(classMetadata.getPropertyType(anyString())).thenReturn(IntegerType.INSTANCE);
        when(sessionFactory.getClassMetadata(Integer.class)).thenReturn(classMetadata);
        filteria = Filteria.create(sessionFactory, Integer.class);
    }

    @Test
    public void testEquals() {
        String filter = "name::100|city::0|title::-1";
        List<FilterToken> actual = filteria.filter(filter).filterTokens();
        Assert.assertEquals(3, actual.size());
        assertTokenParsed(actual, "name", "100", OperationType.EQUALS);
        assertTokenParsed(actual, "city", "0", OperationType.EQUALS);
        assertTokenParsed(actual, "title", "-1", OperationType.EQUALS);
    }

    @Test
    public void testNotEquals() {
        String filter = "name::!100|city::!0|title::!-1";
        List<FilterToken> actual = filteria.filter(filter).filterTokens();
        Assert.assertEquals(3, actual.size());
        assertTokenParsed(actual, "name", "100", OperationType.NOT_EQUALS);
        assertTokenParsed(actual, "city", "0", OperationType.NOT_EQUALS);
        assertTokenParsed(actual, "title", "-1", OperationType.NOT_EQUALS);
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

    @Test
    public void testLess() {
        String filter = "name::<0|city::<=-10|title::<100";
        List<FilterToken> actual = filteria.filter(filter).filterTokens();
        Assert.assertEquals(3, actual.size());
        assertTokenParsed(actual, "name", "0", OperationType.LESS);
        assertTokenParsed(actual, "city", "-10", OperationType.LESS_OR_EQUALS);
        assertTokenParsed(actual, "title", "100", OperationType.LESS);
    }

    @Test
    public void testGreater() {
        String filter = "name::>0|city::>=-10|title::>100";
        List<FilterToken> actual = filteria.filter(filter).filterTokens();
        Assert.assertEquals(3, actual.size());
        assertTokenParsed(actual, "name", "0", OperationType.GREATER);
        assertTokenParsed(actual, "city", "-10", OperationType.GREATER_OR_EQUALS);
        assertTokenParsed(actual, "title", "100", OperationType.GREATER);
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
