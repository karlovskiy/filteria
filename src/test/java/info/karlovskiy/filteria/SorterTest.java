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
 * @since 1.0, 1/8/16
 */
@RunWith(MockitoJUnitRunner.class)
public class SorterTest {

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
    public void testSingleAsc() {
        String sorter = "city";
        List<SorterToken> actual = filteria.sorter(sorter).sorterTokens();
        Assert.assertEquals(1, actual.size());
        assertTokenParsed(actual, "city", false);
    }

    @Test
    public void testSingleDesc() {
        String sorter = "-city";
        List<SorterToken> actual = filteria.sorter(sorter).sorterTokens();
        Assert.assertEquals(1, actual.size());
        assertTokenParsed(actual, "city", true);
    }

    @Test
    public void testMultiple() {
        String sorter = "city|name|-age";
        List<SorterToken> actual = filteria.sorter(sorter).sorterTokens();
        Assert.assertEquals(3, actual.size());
        assertTokenParsed(actual, "city", false);
        assertTokenParsed(actual, "name", false);
        assertTokenParsed(actual, "age", true);
    }

    private void assertTokenParsed(List<SorterToken> tokens, String name, boolean desc) {
        for (SorterToken token : tokens) {
            if (token.getName().equals(name) && token.isDesc() == desc) {
                return;
            }
        }
        Assert.fail("Token " + name + " with " + (desc ? "DESC" : "ASC") + " sort not found, sorterTokens: " + tokens);
    }
}
