package info.karlovskiy.filteria;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 1/8/16
 */
public class SorterParseTest {

    private Filteria filteria;

    @Before
    public void before() {
        filteria = Filteria.create();
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
