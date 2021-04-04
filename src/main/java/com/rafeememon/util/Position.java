package com.rafeememon.util;

import java.math.BigDecimal;

import org.w3c.dom.Node;

public class Position {

    private final BigDecimal latitude;
    private final BigDecimal longitude;

    public Position(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double distanceTo(Position position) {
        double a = latitude.doubleValue() - position.latitude.doubleValue();
        double b = longitude.doubleValue() - position.longitude.doubleValue();
        return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2)) * 1000;
    }

    @Override
    public String toString() {
        return latitude + "," + longitude;
    }

    public static Position fromNode(Node node) {
        return new Position(Nodes.getBigDecimalNode(node, "LatitudeDegrees"),
                Nodes.getBigDecimalNode(node, "LongitudeDegrees"));
    }

}
