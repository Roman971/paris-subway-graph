package network;

public class Station {

    private String id;
    private String name;
    private double latitude;
    private double longitude;

    public Station(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    @Override
    public String toString() {
        return "Station '" + this.name + "' (lat=" + this.latitude + ", lon=" + this.longitude + ")";
    }

}
