package com.codenjoy.dojo.bomberman.client;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

public class PointService {
    public static String actAndMoveString(Direction direction) {
        return "(ACT," + Direction.valueOf(direction.value()) + ")";
    }

    public static Point getRightPoint(Point point) {
        Point nextPoint = new PointImpl();
        nextPoint.setX(point.getX() + 1);
        nextPoint.setY(point.getY());
        return nextPoint;
    }

    public static Point getLeftPoint(Point point) {
        Point nextPoint = new PointImpl();
        nextPoint.setX(point.getX() - 1);
        nextPoint.setY(point.getY());
        return nextPoint;
    }

    public static Point getUpPoint(Point point) {
        Point nextPoint = new PointImpl();
        nextPoint.setX(point.getX());
        nextPoint.setY(point.getY() + 1);
        return nextPoint;
    }

    public static Point getDownPoint(Point point) {
        Point nextPoint = new PointImpl();
        nextPoint.setX(point.getX());
        nextPoint.setY(point.getY() - 1);
        return nextPoint;
    }
}
