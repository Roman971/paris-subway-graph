package network;

import graph.Edge;

public class Path extends Edge<Station> {

    private Line line;

    public Path(Line line, Station origin, Station destination) {
        super(origin, destination);
        this.line = line;
    }

    public Line getLine() {
        return this.line;
    }

    @Override
    public String toString() {
        return this.from().getName() + " => " + this.to().getName();
    }

}
