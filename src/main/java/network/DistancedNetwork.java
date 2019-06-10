package network;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import graph.DijkstraSP;

public class DistancedNetwork extends AbstractNetwork<DistancedPath> {

    public DistancedNetwork() {
        super();
    }

    public DistancedNetwork(String jsonData) {
        super(jsonData);
    }

    public void addPath(Line line, Station origin, Station dest) {
        this.addEdge(new DistancedPath(line, origin, dest));
    }

    public List<Station> getDiameterPath() {
        DijkstraSP<Station, DistancedPath> SP = new DijkstraSP<>(this.order());

        List<Integer> maxPathIds = new ArrayList<>();
        for (Station origin : this.nodes()) {
            SP.process(this, origin);
            for (Station dest : this.nodes()) {
                List<Integer> pathIds = SP.getPathIdsTo(this.nodeIndex(dest));
                if (maxPathIds.size() < pathIds.size()) {
                    maxPathIds = pathIds;
                }
            }
        }

        return maxPathIds.stream().map(id -> this.nodeFromIndex(id)).collect(Collectors.toList());
    }

}
