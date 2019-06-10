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
            String networkData = new String(Files.readAllBytes(Paths.get("data/network.json")), "UTF-8");

            out.println("============== Paris Subway Network Overview ===============\n");
            networkOverview(out, networkData);
            out.println();

            out.println("======= Unweighted Graph (Network without distances) =======\n");
            unweightedGraph(out, networkData);
            out.println();

            out.println("========= Weighted Graph (Network with distances) ==========\n");
            weightedGraph(out, networkData);
            out.println();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void networkOverview(PrintStream out, String networkData) {
        Network subwayNetwork = new Network(networkData);
        out.println(subwayNetwork);
    }

    public static void unweightedGraph(PrintStream out, String networkData) {
        Network subwayNetwork = new Network(networkData);

        List<Station> diameterPath = subwayNetwork.getDiameterPath();
        Stream<Object> diameterPathNames = diameterPath.stream().map(station -> station.getName());

        out.println("Diameter Path: " + String.join(" => ", diameterPathNames.toArray(String[]::new)));

        int totalLength = diameterPath.size() - 1;
        out.println("\nTotal Length: " + totalLength);
    }

    public static void weightedGraph(PrintStream out, String networkData) {
        DistancedNetwork subwayNetwork = new DistancedNetwork(networkData);

        List<Station> diameterPath = subwayNetwork.getDiameterPath();
        Stream<Object> diameterPathNames = diameterPath.stream().map(station -> station.getName());

        out.println("Diameter Path: " + String.join(" => ", diameterPathNames.toArray(String[]::new)));

        List<DistancedPath> paths = subwayNetwork.getPathListFromStationPath(diameterPath);
        Stream<Object> pathStringStream = paths.stream().map(
                path -> path.from().getName() + "-" + path.to().getName() + ": " + Math.round(path.getWeight()) + "m");
        out.println("\nLength of sub-paths: " + String.join("; ", pathStringStream.toArray(String[]::new)));

        double totalLength = 0;
        for (DistancedPath path : paths) {
            totalLength += path.getWeight();
        }
        out.println("\nTotal Length: " + Math.round(totalLength) + "m");
    }
}
