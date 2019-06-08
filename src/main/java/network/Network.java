package network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import graph.BFSShortestPaths;
import graph.Graph;

public class Network extends Graph<Station, Path> {

    private List<Line> lines;

    public Network() {
        super();
        this.lines = new ArrayList<>();
    }

    public Network(String jsonData) {
        this();
        this.fromJsonData(jsonData);
    }

    public void fromJsonData(String jsonData) {
        JsonObject root = new JsonParser().parse(jsonData).getAsJsonObject();

        JsonObject linesObject = root.getAsJsonObject("lignes");
        Set<String> lineKeys = linesObject.keySet();
        for (String lineKey : lineKeys) {
            JsonObject lineObject = linesObject.getAsJsonObject(lineKey);
            if (lineObject.get("type").getAsString().equals("metro")) {
                String name = lineObject.get("name").getAsString();
                String number = lineObject.get("num").getAsString();
                lines.add(new Line(name, number));
            }
        }

        JsonObject stationsObject = root.getAsJsonObject("stations");
        Set<String> stationKeys = stationsObject.keySet();
        for (String stationKey : stationKeys) {
            JsonObject stationObject = stationsObject.getAsJsonObject(stationKey);
            Set<String> lineTypes = stationObject.getAsJsonObject("lignes").keySet();
            if (lineTypes.stream().anyMatch(str -> str.trim().equals("metro"))) {
                String id = stationObject.get("num").getAsString();
                String name = stationObject.get("nom").getAsString();
                double lat = stationObject.get("lat").getAsDouble();
                double lon = stationObject.get("lng").getAsDouble();
                this.addNode(new Station(id, name, lat, lon));
            }
        }

        JsonArray routesArray = root.getAsJsonArray("routes");
        for (JsonElement routeElement : routesArray) {
            JsonObject routeObject = routeElement.getAsJsonObject();
            if (routeObject.get("type").getAsString().equals("metro")) {
                String lineNumber = routeObject.get("ligne").getAsString();
                Line line = getLineByNumber(lineNumber);
                Iterator<JsonElement> stops = routeObject.getAsJsonArray("arrets").iterator();
                String originId = stops.next().getAsString();
                while (stops.hasNext()) {
                    Station origin = this.getStationById(originId);
                    String destId = stops.next().getAsString();
                    if (!this.neighbors(origin).stream().anyMatch(path -> path.to().getId().equals(destId))) {
                        Station dest = this.getStationById(destId);
                        Path path = new Path(line, origin, dest);
                        this.addEdge(path);
                    }
                    originId = destId;
                }
            }
        }
    }

    public Line getLineByNumber(String number) {
        for (Line line : this.lines) {
            if (line.getNumber().equals(number)) {
                return line;
            }
        }
        return null;
    }

    public Station getStationById(String id) {
        for (Station station : this.nodes()) {
            if (station.getId().equals(id)) {
                return station;
            }
        }
        return null;
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

    @Override
    public String toString() {
        String output = String.format("Paris Network (%d Lines, %d Stations)", this.lines.size(), this.order());

        output += "\n\nLines:";
        for (Line line : this.lines) {
            output += "\n" + line;
        }

        output += "\n\nStations:";
        for (Station station : this.nodes()) {
            Stream<Object> adjacentPathNames = neighbors(station).stream().map(path -> path.to().getName());
            output += "\n" + station + " : " + String.join(", ", adjacentPathNames.toArray(String[]::new));
        }

        return output;
    }
}
