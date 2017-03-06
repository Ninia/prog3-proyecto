package ud.main.common;

public class Pair<k,v> {

    private final k left;
    private final v right;

    public Pair(k left, v right) {
        this.left = left;
        this.right = right;
    }

    public k getKey() { return left; }
    public v getValue() { return right; }

    @Override
    public int hashCode() { return left.hashCode() ^ right.hashCode(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) return false;
        Pair pair = (Pair) o;
        return this.left.equals(pair.getKey()) &&
                this.right.equals(pair.getValue());
    }

}