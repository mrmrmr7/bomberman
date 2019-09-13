package com.codenjoy.dojo.bomberman.client;

import com.codenjoy.dojo.bomberman.model.Elements;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.*;

public class MotionValidator {
    private static final int BOMB_RANGE = 3;

    public static boolean isSaveMove(Board board, Point point) {
        boolean flag = true;
        Collection<Point> meatChoppers = board.getMeatChoppers();
        if (!isSafePlaceFromBombs(board, point)) {
            flag = false;
        }
        if (!isSafePlaceFromFutureBlasts(board, point)) {
            flag = false;
        }
//        if (!isSafePlaceFromChoppers(point)){
//            flag = false;
//        }
        return flag;
    }


    public static boolean isSafePlaceFromBombs(Board board, Point point) {
        boolean flag = true;
        Collection<Point> walls = board.getWalls();

        Collection<Point> bombs = board.get(Elements.BOMB_TIMER_1,
                Elements.BOMB_TIMER_2,
                Elements.BOMB_BOMBERMAN,
                Elements.OTHER_BOMB_BOMBERMAN);
        for (Point bomb : bombs) {
            if (bomb.getX() == point.getX()) {
                if (point.getY() <= bomb.getY() + BOMB_RANGE
                        && point.getY() >= bomb.getY() - BOMB_RANGE) {
                    flag = false;
                }
            }
            if (bomb.getY() == point.getY()) {
                if (point.getX() <= bomb.getX() + BOMB_RANGE
                        && point.getX() >= bomb.getX() - BOMB_RANGE) {
                    flag = false;
                }
            }
        }
        if (bombs.contains(point)) {
            flag = false;
        }
        return flag;
    }


    public static boolean isGoodPlaceForBomb(Board board, Point point) {

        Map<Point,Integer> ms = new HashMap<>();



        boolean flag = false;
        Collection<Point> destroyableWalls = board.getDestroyableWalls();
        Collection<Point> walls = board.getWalls();

        for (Point destroybleWall : destroyableWalls) {
            if (destroybleWall.getX() == point.getX()) {
                if (destroybleWall.getY() <= point.getY() + BOMB_RANGE
                        && destroybleWall.getY() >= point.getY() - BOMB_RANGE
                        && !(board.isAt(destroybleWall.getX(), destroybleWall.getY() + 1, Elements.WALL)
                        || board.isAt(destroybleWall.getX(), destroybleWall.getY() - 1, Elements.WALL))) {
                    flag = true;
                }
            }
            if (destroybleWall.getY() == point.getY()) {
                if (destroybleWall.getX() <= point.getX() + BOMB_RANGE
                        && destroybleWall.getX() >= point.getX() - BOMB_RANGE
                        && !(board.isAt(destroybleWall.getX() - 1, destroybleWall.getY(), Elements.WALL)
                        || board.isAt(destroybleWall.getX() + 1, destroybleWall.getY(), Elements.WALL))) {
                    flag = true;
                }
            }
        }

        Collection<Point> otherBombermans = board.getOtherBombermans();
        for (Point enemy : otherBombermans) {
            if (enemy.getX() == point.getX()) {
                if (enemy.getY() <= point.getY() + BOMB_RANGE
                        && enemy.getY() >= point.getY() - BOMB_RANGE
                        && !(board.isAt(enemy.getX(), enemy.getY() + 1, Elements.WALL)
                        || board.isAt(enemy.getX(), enemy.getY() - 1, Elements.WALL))) {
                    flag = true;
                }
            }
            if (enemy.getY() == point.getY()) {
                if (enemy.getX() <= point.getX() + BOMB_RANGE
                        && enemy.getX() >= point.getX() - BOMB_RANGE
                        && !(board.isAt(enemy.getX() - 1, enemy.getY(), Elements.WALL)
                        || board.isAt(enemy.getX() + 1, enemy.getY(), Elements.WALL))) {
                    flag = true;
                }
            }
        }

        Collection<Point> meatChoppers = board.getMeatChoppers();
        for (Point enemy : meatChoppers) {
            if (enemy.getX() == point.getX()) {
                if (enemy.getY() <= point.getY() + BOMB_RANGE
                        && enemy.getY() >= point.getY() - BOMB_RANGE
                        && !(board.isAt(enemy.getX(), enemy.getY() + 1, Elements.WALL)
                        || board.isAt(enemy.getX(), enemy.getY() - 1, Elements.WALL))) {
                    flag = true;
                }
            }
            if (enemy.getY() == point.getY()) {
                if (enemy.getX() <= point.getX() + BOMB_RANGE
                        && enemy.getX() >= point.getX() - BOMB_RANGE
                        && !(board.isAt(enemy.getX() + 1, enemy.getY(), Elements.WALL)
                        || board.isAt(enemy.getX() - 1, enemy.getY(), Elements.WALL))) {
                    flag = true;
                }
            }
        }
        return flag;
    }


    public static boolean isSafePlaceFromFutureBlasts(Board board, Point point) {
        boolean flag = true;
        Collection<Point> futureBlasts = board.getFutureBlasts();
        for (Point futureBlast : futureBlasts) {
            if (futureBlast.getX() == point.getX()) {
                if (point.getY() == futureBlast.getY()
                        && point.getY() == futureBlast.getY()) {
                    flag = false;
                }
            }
            if (futureBlast.getY() == point.getY()) {
                if (point.getX() == futureBlast.getX()
                        && point.getX() == futureBlast.getX()) {
                    flag = false;
                }
            }
        }
        if (futureBlasts.contains(point)) {
            flag = false;
        }
        return flag;
    }



    public static List<String> getSaveMoves(Board board, Point myBomberman) {
        List<String> moves = new ArrayList<>();

        Point leftPoint = PointService.getLeftPoint(myBomberman);
        if (!board.isBarrierAt(leftPoint)
                && MotionValidator.isSaveMove(board, leftPoint)) {
            if (MotionValidator.isGoodPlaceForBomb(board, myBomberman)) {
                moves.add(PointService.actAndMoveString(Direction.LEFT));
            } else {
                moves.add(Direction.LEFT.toString());
            }
        }

        Point downPoint = PointService.getDownPoint(myBomberman);
        if (!board.isBarrierAt(downPoint)
                && MotionValidator.isSaveMove(board, downPoint)) {
            if (MotionValidator.isGoodPlaceForBomb(board, myBomberman)) {
                moves.add(PointService.actAndMoveString(Direction.DOWN));
            } else {
                moves.add(Direction.DOWN.toString());
            }
        }

        Point rightPoint = PointService.getRightPoint(myBomberman);
        if (!board.isBarrierAt(rightPoint)
                && MotionValidator.isSaveMove(board, rightPoint)) {
            if (MotionValidator.isGoodPlaceForBomb(board, myBomberman)) {
                moves.add(PointService.actAndMoveString(Direction.RIGHT));
            } else {
                moves.add(Direction.RIGHT.toString());
            }
        }

        Point upPoint = PointService.getUpPoint(myBomberman);
        if (!board.isBarrierAt(upPoint)
                && MotionValidator.isSaveMove(board, upPoint)) {
            if (MotionValidator.isGoodPlaceForBomb(board, myBomberman)) {
                moves.add(PointService.actAndMoveString(Direction.UP));
            } else {
                moves.add(Direction.UP.toString());
            }
        }



        return moves;
    }

    public static List<String> isCurrentPlaceSave(Board board, Point point){
        List<String> moves = new ArrayList<>();
        if (MotionValidator.isSaveMove(board, point)){
            if (MotionValidator.isGoodPlaceForBomb(board, point)){
                moves.add(Direction.ACT.toString());
            } else {
                moves.add("");
            }
        }
        return moves;
    }

    public static List<String> getAvailableMoves(Board board, Point point) {
        List<String> moves = new ArrayList<>();
        moves.add("");

        if (!board.isBarrierAt(PointService.getLeftPoint(point))) {
            if (MotionValidator.isGoodPlaceForBomb(board, point)) {
                moves.add(PointService.actAndMoveString(Direction.LEFT));
            } else {
                moves.add(Direction.LEFT.toString());
            }
        }

        if (!board.isBarrierAt(PointService.getUpPoint(point))) {
            if (MotionValidator.isGoodPlaceForBomb(board, point)) {
                moves.add(PointService.actAndMoveString(Direction.UP));
            } else {
                moves.add(Direction.UP.toString());
            }
        }

        if (!board.isBarrierAt(PointService.getRightPoint(point))) {
            if (MotionValidator.isGoodPlaceForBomb(board, point)) {
                moves.add(PointService.actAndMoveString(Direction.RIGHT));
            } else {
                moves.add(Direction.RIGHT.toString());
            }
        }

        if (!board.isBarrierAt(PointService.getDownPoint(point))) {
            if (MotionValidator.isGoodPlaceForBomb(board, point)) {
                moves.add(PointService.actAndMoveString(Direction.DOWN));
            } else {
                moves.add(Direction.DOWN.toString());
            }
        }
        return moves;
    }

}
