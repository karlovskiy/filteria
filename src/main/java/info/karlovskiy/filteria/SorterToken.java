package info.karlovskiy.filteria;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 1/8/16
 */
class SorterToken {
    private final String name;
    private final boolean desc;

    SorterToken(String name, boolean desc) {
        this.name = name;
        this.desc = desc;
    }

    String getName() {
        return name;
    }

    boolean isDesc() {
        return desc;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SorterToken{");
        sb.append("name='").append(name).append('\'');
        sb.append(", desc=").append(desc);
        sb.append('}');
        return sb.toString();
    }
}
