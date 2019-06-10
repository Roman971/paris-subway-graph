package network;

import graph.WEdge;

public class DistancedPath extends WEdge<Station> {

    private Line line;

    public DistancedPath(Line line, Station origin, Station destination) {
        super(origin, destination, computeDistance(origin, destination));
        this.line = line;
    }

    public Line getLine() {
        return this.line;
    }

    public String getDistanceStr() {
        return Math.round(this.getWeight()) + "m";
    }

    @Override
    public String toString() {
        return this.from().getName() + " -> " + this.to().getName() + "(d=" + this.getDistanceStr() + ")";
    }

    // https://jonisalonen.com/2014/computing-distance-between-coordinates-can-be-simple-and-fast/
    public static double computeDistance(Station origin, Station destination) {
        int deglen = 110250;
        double deltaX = origin.getLatitude() - destination.getLatitude();
        double deltaY = (origin.getLongitude() - destination.getLongitude()) * Math.cos(destination.getLatitude());
        return deglen * Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

}
