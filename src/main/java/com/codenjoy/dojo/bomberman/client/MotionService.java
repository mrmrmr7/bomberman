package com.codenjoy.dojo.bomberman.client;

import java.util.List;
import java.util.Random;

public class MotionService {
    public static String getRandomMoves(List<String> moves) {

        if (moves.contains(""));
        Random random = new Random();
        return moves.get(random.nextInt(moves.size()));
    }
}
