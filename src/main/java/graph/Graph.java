package graph;

import java.util.ArrayList;
import java.util.List;

public class Graph<N, E extends Edge<N>> {

    private int n;
    private int m;
    private List<N> nodes;
    private final List<List<E>> adjList;

    public Graph(int n, int m) {
        this.n = n;
        this.m = m;
        this.nodes = new ArrayList<>(n);
        this.adjList = new ArrayList<>();
    }

    public Graph(int n) {
        this(n, 0);
    }

    public Graph() {
        this(0, 0);
    }

    public void addNode(N v) {
        this.nodes.add(v);
        this.adjList.add(new ArrayList<>());
        this.n++;
    }

    public void addEdge(E e) {
        this.neighbors(e.from()).add(e);
        this.m++;
    }

    public List<N> nodes() {
        return this.nodes;
    }

    public List<E> neighbors(N v) {
        return this.adjList.get(this.nodeIndex(v));
    }

    public int nodeIndex(N v) {
        try {
            return this.nodes().indexOf(v);
        } catch (ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }

    public N nodeFromIndex(int i) {
        return this.nodes().get(i);
    }

    public int order() {
        return this.n;
    }

    public int size() {
        return this.m;
    }

    @Override
    public String toString() {
        String output = String.format("Graph (Size: %d, Order: %d)", this.size(), this.order());
        for (N v : this.nodes()) {
            output += "\n" + v + " : " + this.neighbors(v);
        }
        return output;
    }

}
