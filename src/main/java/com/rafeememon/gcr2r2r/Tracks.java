package com.rafeememon.gcr2r2r;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.w3c.dom.Node;

import com.rafeememon.util.Nodes;
import com.rafeememon.util.Position;

public class Tracks {

    private static final double THRESHOLD_DISTANCE_M = 10;
    private static final double THRESHOLD_POSITION = 1.7;

    // Returns the new distance
    public static BigDecimal fixDistances(Node track) {
        assertTrack(track);

        List<Node> trackpoints = Nodes.getChildNodes(track, "Trackpoint");

        BigDecimal cumulativeCorrection = BigDecimal.ZERO;
        int count = 0;

        for (int k = 1; k < trackpoints.size(); k++) {
            Node previous = trackpoints.get(k - 1);
            Node current = trackpoints.get(k);

            BigDecimal previousDistance = getDistance(previous);
            BigDecimal currentDistance = getDistance(current);
            BigDecimal distanceDifference = currentDistance.subtract(previousDistance);

            if (distanceDifference.doubleValue() > THRESHOLD_DISTANCE_M) {
                System.out.println("Time: " + Nodes.getTextNode(current, "Time"));
                System.out.println("Position: " + getPosition(current));
                System.out.println("Difference: " + distanceDifference);
                System.out.println("Speed: " + getSpeed(current));
                System.out.println("-----");

                removePosition(current);

                BigDecimal speed = getSpeed(current);
                BigDecimal newDistance = previousDistance.add(speed);
                BigDecimal correction = currentDistance.subtract(newDistance);

                for (int j = k; j < trackpoints.size(); j++) {
                    Node iter = trackpoints.get(j);
                    setDistance(iter, getDistance(iter).subtract(correction));
                }

                cumulativeCorrection = cumulativeCorrection.add(correction);
                count++;
            }
        }

        System.out.println("Total points fixed: " + count);
        System.out.println("Total correction: " + cumulativeCorrection.toPlainString());

        return getDistance(trackpoints.get(trackpoints.size() - 1));
    }

    // Returns the number of positions removed
    public static int fixPositions(Node track) {
        assertTrack(track);

        int totalRemoved = 0;
        int removed = 0;
        do {
            removed = fixPositionsOnce(track);
            totalRemoved += removed;
        } while (removed > 0);

        System.out.println("Total positions removed: " + totalRemoved);

        return totalRemoved;
    }

    // Returns the number of positions removed
    private static int fixPositionsOnce(Node track) {
        assertTrack(track);

        List<Node> trackpoints = Nodes.getChildNodes(track, "Trackpoint");

        Optional<Position> lastPositionOptional = Optional.empty();
        int count = 0;

        for (int k = 0; k < trackpoints.size(); k++) {
            Node current = trackpoints.get(k);

            Optional<Position> positionOptional = getPosition(current);
            if (!positionOptional.isPresent()) {
                continue;
            }

            if (!lastPositionOptional.isPresent()) {
                lastPositionOptional = positionOptional;
                continue;
            }

            Position position = positionOptional.get();
            Position lastPosition = lastPositionOptional.get();
            double distance = position.distanceTo(lastPosition);
            // System.out.println(distance);

            if (distance > THRESHOLD_POSITION) {
                System.out.println("Removing position: " + position);
                removePosition(current);
                count++;
            }

            lastPositionOptional = positionOptional;
        }

        return count;
    }

    private static BigDecimal getDistance(Node trackpoint) {
        return Nodes.getBigDecimalNode(trackpoint, "DistanceMeters");
    }

    private static void setDistance(Node trackpoint, BigDecimal distance) {
        Nodes.getChildNode(trackpoint, "DistanceMeters").setTextContent(distance.toPlainString());
    }

    private static Optional<Position> getPosition(Node trackpoint) {
        return Nodes.getOptionalChildNode(trackpoint, "Position").map(Position::fromNode);
    }

    private static void removePosition(Node trackpoint) {
        Nodes.getOptionalChildNode(trackpoint, "Position").ifPresent(Nodes::removeNode);
    }

    private static BigDecimal getSpeed(Node trackpoint) {
        return Nodes.getBigDecimalNode(trackpoint, "Extensions", "ns3:TPX", "ns3:Speed");
    }

    private static void assertTrack(Node track) {
        if (!track.getNodeName().equals("Track")) {
            throw new RuntimeException("Expected type track");
        }
    }

    private Tracks() {
        // utility class
    }

}
