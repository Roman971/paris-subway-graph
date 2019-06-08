package network;

public class Line {

    private String name;
    private String number;

    public Line(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return this.name;
    }

    public String getNumber() {
        return this.number;
    }

    @Override
    public String toString() {
        return "Line " + this.number + " (" + this.name + ")";
    }

}
