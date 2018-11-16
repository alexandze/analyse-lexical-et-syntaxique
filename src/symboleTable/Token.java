package symboleTable;

public class Token {
    private Tag tag;
    private final String word;
    private Type type;

    public Token(Tag tag, String word, Type type) {
        this.tag = tag;
        this.word = word;
        this.type = type;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getWord() {
        return word;
    }
}
