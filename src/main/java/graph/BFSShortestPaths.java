package graph;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BFSShortestPaths<N, E extends Edge<N>> {

    private boolean[] marked;
    private int[] previous;
    private int[] distance;

    public BFSShortestPaths(int n) {
        this.marked = new boolean[n];
        this.previous = new int[n];
        this.distance = new int[n];
    }

    public void reset() {
        Arrays.fill(marked, false);
        Arrays.fill(previous, -1);
        Arrays.fill(distance, Integer.MAX_VALUE);
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
                N v = itr.next().to();
                if (!marked[graph.nodeIndex(v)]) {
                    distance[graph.nodeIndex(v)] = distance[graph.nodeIndex(u)] + 1;
                    previous[graph.nodeIndex(v)] = graph.nodeIndex(u);
                    marked[graph.nodeIndex(v)] = true;
                    queue.add(v);
                }
            }
        }
    }

    public boolean hasPathTo(int vId) {
        return marked[vId];
    }

    public int distTo(int vId) {
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
