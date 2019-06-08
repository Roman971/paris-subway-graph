package graph;

public class WEdge<N> extends Edge<N> {

    private final double weight;

    public WEdge(N v, N w, double weight) {
        super(v, w);
        this.weight = weight;
    }

    public double getWeight() {
        return this.weight;
    }

    @Override
    public String toString() {
        return super.toString() + " (w=" + this.weight + ")";
    }

}
