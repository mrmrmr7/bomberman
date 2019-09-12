package com.codenjoy.dojo.bomberman.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.bomberman.model.Elements;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;

import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private Dice dice;
    private Board board;
    private static int BOMB_RANGE = 3;

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;

        if (board.isMyBombermanDead()) return "";

        //--------------------------------------
        Point me = board.getBomberman();

        List<Point> availablePointlist = getAvaliableDirections(me);
        List<Point> safePointList = new ArrayList<>();

        availablePointlist.stream().forEach(each -> {
                    if (isSaveMove(each)) {
                        safePointList.add(each);
                    }
                }
            );

        List<Point> secondLevelSafePointList = new ArrayList<>();
        safePointList.forEach(e->{
            List<Point> secondLevelAvailablePointlist = getAvaliableDirections(e);
            secondLevelAvailablePointlist.forEach(each-> {
                if (isSaveMove(each)) {
                    secondLevelSafePointList.add(each);
                }
            });
        });

        int randomNumber = (int) Math.round(Math.random() * (safePointList.size() - 1));

        Point randomPoint = safePointList.get(randomNumber);
        //--------------------------------------

        return actAndMove(getDirectionByAroundPoint(me, randomPoint));
    }

    private String actAndMove(Direction direction) {
        return "(ACT" + "," + Direction.valueOf(direction.value()) + ")";
    }

    private List<Point> getAvaliableDirections(Point current) {
        List<Point> res = new ArrayList<>();

        if (!board.isBarrierAt(getRightPoint(current))) {
            res.add(getRightPoint(current));
        }

        if (!board.isBarrierAt(getLeftPoint(current))) {
            res.add(getLeftPoint(current));
        }

        if (!board.isBarrierAt(getUpPoint(current))) {
            res.add(getUpPoint(current));
        }

        if (!board.isBarrierAt(getDownPoint(current))) {
            res.add(getDownPoint(current));
        }

        return res;
    }

    private boolean isSaveMove(Point point) {
        boolean flag = true;
        Collection<Point> bombs = board.get(Elements.BOMB_TIMER_2);
        bombs.addAll(board.get(Elements.BOMB_TIMER_1));

        for (Point bomb : bombs) {
            if (bomb.getX() == point.getX()) {
                if (point.getY() < bomb.getY() - BOMB_RANGE
                        && point.getY() > bomb.getY() + BOMB_RANGE) {
                    flag = false;
                }
            }

            if (bomb.getY() == point.getY()) {
                if (point.getX() < bomb.getX() - BOMB_RANGE
                        && point.getX() > bomb.getX() + BOMB_RANGE) {
                    flag = false;
                }
            }
        }

        if (bombs.contains(point)) {
            flag = false;
        }

        return flag;
    }



    private Point getRightPoint(Point point) {
        Point nextPoint = new PointImpl();
        nextPoint.setX(point.getX() + 1);
        nextPoint.setY(point.getY());
        return nextPoint;
    }

    private Point getLeftPoint(Point point) {
        Point nextPoint = new PointImpl();
        nextPoint.setX(point.getX() - 1);
        nextPoint.setY(point.getY());
        return nextPoint;
    }

    private Point getUpPoint(Point point) {
        Point nextPoint = new PointImpl();
        nextPoint.setX(point.getX());
        nextPoint.setY(point.getY() + 1);
        return nextPoint;
    }

    private Point getDownPoint(Point point) {
        Point nextPoint = new PointImpl();
        nextPoint.setX(point.getX());
        nextPoint.setY(point.getY() - 1);
        return nextPoint;
    }

    private boolean isGoodPlaceForBomb(Point point) {
        boolean flag = false;
        Collection<Point> destroyableWalls = board.getDestroyableWalls();
        for (Point wall : destroyableWalls) {
            if (wall.getX() == point.getX()) {
                if (wall.getY() < point.getY() + BOMB_RANGE
                        && wall.getY() > point.getY() - BOMB_RANGE) {
                    System.out.println("good wall = " + wall);
                    System.out.println("my bomber = " + point);
                    flag = true;
                }
            }
            if (wall.getY() == point.getY()) {
                if (wall.getX() < point.getX() + BOMB_RANGE
                        && wall.getX() > point.getX() - BOMB_RANGE) {
                    System.out.println("good wall = " + wall);
                    System.out.println("my bomber = " + point);
                    flag = true;
                }
            }
        }

        Collection<Point> otherBombermans = board.getOtherBombermans();
        for (Point enemy : otherBombermans) {
            if (enemy.getX() == point.getX()) {
                if (enemy.getY() < point.getY() + BOMB_RANGE
                        && enemy.getY() > point.getY() - BOMB_RANGE) {
                    System.out.println("enemy = " + enemy);
                    System.out.println("my bomber = " + point);
                    flag = true;
                }
            }
            if (enemy.getY() == point.getY()) {
                if (enemy.getX() < point.getX() + BOMB_RANGE
                        && enemy.getX() > point.getX() - BOMB_RANGE) {
                    System.out.println("enemy = " + enemy);
                    System.out.println("my bomber = " + point);
                    flag = true;
                }
            }
        }

        Collection<Point> meatChoppers = board.getMeatChoppers();
        for (Point enemy : meatChoppers) {
            if (enemy.getX() == point.getX()) {
                if (enemy.getY() < point.getY() + BOMB_RANGE
                        && enemy.getY() > point.getY() - BOMB_RANGE) {
                    System.out.println("choper = " + enemy);
                    System.out.println("my bomber = " + point);
                    flag = true;
                }
            }
            if (enemy.getY() == point.getY()) {
                if (enemy.getX() < point.getX() + BOMB_RANGE
                        && enemy.getX() > point.getX() - BOMB_RANGE) {
                    System.out.println("choper = " + enemy);
                    System.out.println("my bomber = " + point);
                    flag = true;
                }
            }
        }
        return flag;
    }

    private Direction getDirectionByAroundPoint(Point my, Point point) {
        if (my.getX() != point.getX()) {
            if (my.getX() < point.getX()) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else {
            if (my.getY() < point.getY()) {
                return Direction.UP;
            } else {
                return Direction.DOWN;
            }
        }
    }

    public static void main(String[] args) {
        YourSolver me = new YourSolver(new RandomDice());
        Board board = new Board();

        WebSocketRunner.runClient(
                // paste here board page url from browser after registration
                "http://codenjoy.com:80/codenjoy-contest/board/player/b279qymn26ff08v8vhkj?code=2785640024324989813",
                me,
                board);
    }
}

