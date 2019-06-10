package network;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import graph.BFSShortestPaths;

public class Network extends AbstractNetwork<Path> {

    public Network() {
        super();
    }

    public Network(String jsonData) {
        super(jsonData);
    }

    public void addPath(Line line, Station origin, Station dest) {
        this.addEdge(new Path(line, origin, dest));
    }

    public List<Station> getDiameterPath() {
        BFSShortestPaths<Station, Path> SP = new BFSShortestPaths<>(this.order());

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

    public List<List<Path>> getAllShortestPaths() {
        BFSShortestPaths<Station, Path> SP = new BFSShortestPaths<>(this.order());

        List<List<Integer>> pathIdsList = new ArrayList<>();
        for (Station origin : this.nodes()) {
            SP.process(this, origin);
            for (Station dest : this.nodes()) {
                pathIdsList.add(SP.getPathIdsTo(this.nodeIndex(dest)));
            }
        }

        List<List<Station>> stationList = pathIdsList.stream()
                .map(l -> l.stream().map(id -> this.nodeFromIndex(id)).collect(Collectors.toList()))
                .collect(Collectors.toList());

        return stationList.stream().map(pathStations -> this.getPathListFromStationPath(pathStations))
                .collect(Collectors.toList());
    }

}
