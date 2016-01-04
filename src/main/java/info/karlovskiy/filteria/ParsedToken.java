package info.karlovskiy.filteria;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 1/3/16
 */
public class ParsedToken {
    private final String name;
    private final String value;
    private final TokenType tokenType;

    public ParsedToken(String name, String value, TokenType tokenType) {
        this.name = name;
        this.value = value;
        this.tokenType = tokenType;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ParsedToken{");
        sb.append("name='").append(name).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append(", operator=").append(tokenType);
        sb.append('}');
        return sb.toString();
    }
}
