package graph;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DijkstraSP<N, E extends WEdge<N>> {

    private boolean[] marked;
    private int[] previous;
    private double[] distance;

    public DijkstraSP(int n) {
        this.marked = new boolean[n];
        this.previous = new int[n];
        this.distance = new double[n];
    }

    public void reset() {
        Arrays.fill(marked, false);
        Arrays.fill(previous, -1);
        Arrays.fill(distance, Double.MAX_VALUE);
    }

    public void process(Graph<N, E> graph, N s) {
        LinkedList<N> queue = new LinkedList<N>();

        reset();
        marked[graph.nodeIndex(s)] = true;
        distance[graph.nodeIndex(s)] = 0;
        previous[graph.nodeIndex(s)] = 0;
        queue.add(s);

        while (!queue.isEmpty()) {
            N u = queue.poll();
            Iterator<E> itr = graph.neighbors(u).iterator();
            while (itr.hasNext()) {
                E e = itr.next();
                if (!marked[graph.nodeIndex(e.to())]) {
                    double newDistance = distance[graph.nodeIndex(u)] + e.getWeight();
                    if (newDistance < distance[graph.nodeIndex(e.to())]) {
                        distance[graph.nodeIndex(e.to())] = distance[graph.nodeIndex(u)] + e.getWeight();
                        previous[graph.nodeIndex(e.to())] = graph.nodeIndex(u);
                        queue.add(e.to());
                    }
                }
            }
            marked[graph.nodeIndex(u)] = true;
        }
    }

    public boolean hasPathTo(int vId) {
        return marked[vId];
    }

    public double distTo(int vId) {
        return distance[vId];
    }

    public List<Integer> getPathIdsTo(int vId) {
        if (previous[vId] < 0) {
            return null;
        }

        LinkedList<Integer> path = new LinkedList<Integer>();
        int sId = vId;
        path.add(sId);
        while ((sId = previous[sId]) > 0) {
            path.add(sId);
        }

        Collections.reverse(path);
        return path;
    }
}
