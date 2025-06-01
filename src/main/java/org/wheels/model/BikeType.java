package org.wheels.model;

public enum BikeType {
    BASIC(0.40),
    COMFORT(0.80),
    ELECTRIC(1.20);

    private final double finePer3Minutes;

    BikeType(double finePer3Minutes) {
        this.finePer3Minutes = finePer3Minutes;
    }

    public double calculateFine(int exceededMinutes) {
        int blocksOf3Minutes = exceededMinutes / 3;
        return blocksOf3Minutes * finePer3Minutes;
    }
}