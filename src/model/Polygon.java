package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a polygon defined by a list of vertices.
 * Tracks whether the polygon has been completed.
 */
public class Polygon {
    private final List<Point> points;
    private boolean isFinished;

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public Polygon() {
        this(new ArrayList<>());
    }

    public Polygon(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }
}
