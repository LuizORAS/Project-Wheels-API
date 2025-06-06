package org.wheels.model;

public class Bike {
    private int id;
    private BikeType type;
    private boolean available;

    public Bike() {}
    public Bike(int id, BikeType type, boolean available) {
        this.id = id;
        this.type = type;
        this.available = available;
    }

    public int getId() { return id; }
    public BikeType getType() { return type; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
