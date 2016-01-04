package info.karlovskiy.filteria;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 1/3/16
 */
public class FilteriaParseTest {

    private Filteria filteria;

    @Before
    public void before() {
        filteria = new Filteria();
    }

    @Test
    public void testParseEquals() {
        String filter = "name::todd|city::denver|title::grand poobah";
        List<ParsedToken> actual = filteria.parse(filter).tokens();
        Assert.assertEquals(3, actual.size());
        assertTokenParsed(actual, "name", "todd", TokenType.EQUALS);
        assertTokenParsed(actual, "city", "denver", TokenType.EQUALS);
        assertTokenParsed(actual, "title", "grand poobah", TokenType.EQUALS);
    }

    @Test
    public void testParseEndsWith() {
        String filter = "name::*odd|city::*enver|title::*nd poobah";
        List<ParsedToken> actual = filteria.parse(filter).tokens();
        Assert.assertEquals(3, actual.size());
        assertTokenParsed(actual, "name", "odd", TokenType.ENDS_WITH);
        assertTokenParsed(actual, "city", "enver", TokenType.ENDS_WITH);
        assertTokenParsed(actual, "title", "nd poobah", TokenType.ENDS_WITH);
    }

    @Test
    public void testParseStartsWith() {
        String filter = "name::to*|city::denv*|title::grand po*";
        List<ParsedToken> actual = filteria.parse(filter).tokens();
        Assert.assertEquals(3, actual.size());
        assertTokenParsed(actual, "name", "to", TokenType.STARTS_WITH);
        assertTokenParsed(actual, "city", "denv", TokenType.STARTS_WITH);
        assertTokenParsed(actual, "title", "grand po", TokenType.STARTS_WITH);
    }

    @Test
    public void testParseContains() {
        String filter = "name::!todd|city::!denver|title::!grand poobah";
        List<ParsedToken> actual = filteria.parse(filter).tokens();
        Assert.assertEquals(3, actual.size());
        assertTokenParsed(actual, "name", "todd", TokenType.NOT_EQUALS);
        assertTokenParsed(actual, "city", "denver", TokenType.NOT_EQUALS);
        assertTokenParsed(actual, "title", "grand poobah", TokenType.NOT_EQUALS);
    }

    private void assertTokenParsed(List<ParsedToken> tokens, String name, String value, TokenType tokenType) {
        for (ParsedToken token : tokens) {
            if (token.getName().equals(name) && token.getValue().equals(value) &&
                    token.getTokenType() == tokenType) {
                return;
            }
        }
        Assert.fail("Token " + name + " with value " + value + " and type " + tokenType + " not found, tokens: " + tokens);
    }
}
