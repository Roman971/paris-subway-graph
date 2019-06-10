package app;

import java.util.List;
import java.util.stream.Stream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

import network.Network;
import network.DistancedNetwork;
import network.DistancedPath;
import network.Station;

public class Main {
    public static void main(String args[]) {
        try {
            PrintStream out = new PrintStream(System.out, true, "UTF-8");
            out.println("======= Unweighted Graph (Network without distances) =======");
            unweightedGraph(out);

            out.println();
            out.println("======= Weighted Graph (Network without distances) =======");
            weightedGraph(out);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unweightedGraph(PrintStream out) throws UnsupportedEncodingException, IOException {
        String jsonNetworkData = new String(Files.readAllBytes(Paths.get("data/network.json")), "UTF-8");
        Network subwayNetwork = new Network(jsonNetworkData);

        out.println(subwayNetwork);

        List<Station> diameterPath = subwayNetwork.getDiameterPath();
        Stream<Object> diameterPathNames = diameterPath.stream().map(station -> station.getName());

        out.println();
        out.println("Diameter Path: " + String.join(" => ", diameterPathNames.toArray(String[]::new)));

        int totalLength = diameterPath.size() - 1;
        out.println("Total Length: " + totalLength);
    }

    public static void weightedGraph(PrintStream out) throws UnsupportedEncodingException, IOException {
        String jsonNetworkData = new String(Files.readAllBytes(Paths.get("data/network.json")), "UTF-8");
        DistancedNetwork subwayNetwork = new DistancedNetwork(jsonNetworkData);

        out.println(subwayNetwork);

        List<Station> diameterPath = subwayNetwork.getDiameterPath();
        Stream<Object> diameterPathNames = diameterPath.stream().map(station -> station.getName());

        out.println();
        out.println("Diameter Path: " + String.join(" => ", diameterPathNames.toArray(String[]::new)));

        List<DistancedPath> paths = subwayNetwork.getPathListFromStationPath(diameterPath);
        Stream<Object> pathStringStream = paths.stream().map(
                path -> path.from().getName() + "-" + path.to().getName() + ": " + Math.round(path.getWeight()) + "m");
        out.println("Length of sub-paths: " + String.join("; ", pathStringStream.toArray(String[]::new)));

        double totalLength = 0;
        for (DistancedPath path : paths) {
            totalLength += path.getWeight();
        }
        out.println("Total Length: " + Math.round(totalLength) + "m");
    }
}
