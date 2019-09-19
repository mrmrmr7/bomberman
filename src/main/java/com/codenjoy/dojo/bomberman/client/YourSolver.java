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
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.*;

import java.util.*;

/**
 * User: your name
 */

public class YourSolver implements Solver<Board> {


    private Dice dice;
    private Board board;
    private String lastDirection = "404";

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // paste here board page url from browser after registration
                "http://10.6.219.126/codenjoy-contest/board/player/id0l607a9f404irn5f3z?code=3482662921311447627",
                new YourSolver(new RandomDice()),
                new Board());

    }

    @Override
    public String get(Board board) {
        this.board = board;

        if (board.isMyBombermanDead()) return "";
        Point myBomberman = board.getBomberman();

        List<String> moves = MotionValidator.getSaveMoves(board, myBomberman);

//        if (moves.size() > 1) {
//            for (String each : moves){
//                if (each.contains(Direction.valueOf(lastDirection).inverted().toString())){
//                    moves.remove(each);
//                }
//            }
//        }

       String finalDirection = "";
        if (moves.isEmpty()) {
            moves = MotionValidator.isCurrentPlaceSave(board,myBomberman);
            if (!moves.isEmpty()){
                finalDirection = moves.get(0);
            } else {
                moves = MotionValidator.getAvailableMoves(board, myBomberman);

                if (!moves.isEmpty()) {
                    finalDirection = MotionService.getRandomMoves(moves);
                } else {
                    finalDirection = Direction.ACT();
                }
            }
        } else {
            finalDirection = MotionService.getRandomMoves(moves);
        }

//        String testLastMove;
//        if (finalDirection.length() > 5) {
//            testLastMove = finalDirection.substring(5, finalDirection.length() - 1) + "!!!!!!!";
//            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!" + finalDirection.substring(5, finalDirection.length() - 1) + "!!!!!!!");
//            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!! INVERTED: " + Direction.valueOf(finalDirection.substring(5, finalDirection.length() - 1)).inverted().toString() + "!!!!!!!!" );
//        } else {
//            testLastMove = Direction.valueOf(finalDirection).inverted().toString();
//        }
//
//        System.out.println(testLastMove);

//        if (finalDirection.startsWith("(")) {
//            lastDirection = finalDirection.substring(5, finalDirection.length() - 1);
//            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + lastDirection);
//        } else {
//            lastDirection = finalDirection;
//        }

        return finalDirection;
    }

}
