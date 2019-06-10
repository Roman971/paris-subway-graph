package network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import graph.Edge;
import graph.Graph;

public abstract class AbstractNetwork<P extends Edge<Station>> extends Graph<Station, P> {

    private List<Line> lines;

    public AbstractNetwork() {
        super();
        this.lines = new ArrayList<>();
    }

    public AbstractNetwork(String jsonData) {
        this();
        this.fromJsonData(jsonData);
    }

    public abstract void addPath(Line line, Station origin, Station destination);

    public abstract List<Station> getDiameterPath();

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
                        this.addPath(line, origin, dest);
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

    public Set<P> getAllPaths() {
        return this.nodes().stream().map(station -> this.neighbors(station)).flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public List<P> getPathListFromStationPath(List<Station> stationPath) {
        List<P> pathList = new ArrayList<>();
        Iterator<Station> stationsItr = stationPath.iterator();
        Station origin = stationsItr.next();
        while (stationsItr.hasNext()) {
            Station dest = stationsItr.next();
            pathList.add(this.getPathFromStations(origin, dest));
            origin = dest;
        }
        return pathList;
    }

    public P getPathFromStations(Station origin, Station destination) {
        for (P path : this.neighbors(origin)) {
            if (path.to() == destination) {
                return path;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String output = String.format("Paris Network (%d Lines, %d Stations)", this.lines.size(), this.order());

        output += "\n\nList of Lines:";
        for (Line line : this.lines) {
            output += "\n" + line;
        }

        output += "\n\nList of Stations:";
        for (Station station : this.nodes()) {
            Stream<Object> adjacentPathNames = neighbors(station).stream().map(path -> path.to().getName());
            output += "\n" + station + " : " + String.join(", ", adjacentPathNames.toArray(String[]::new));
        }

        return output;
    }
}
