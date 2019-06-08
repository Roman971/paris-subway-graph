package graph;

public class Edge<N> {

    private final N v;
    private final N w;

    public Edge(N v, N w) {
        this.v = v;
        this.w = w;
    }

    public N from() {
        return v;
    }

    public N to() {
        return w;
    }

    @Override
    public String toString() {
        return this.v + " -> " + this.w;
    }

}
