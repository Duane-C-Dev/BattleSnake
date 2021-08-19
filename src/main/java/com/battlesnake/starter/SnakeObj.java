package com.battlesnake.starter;

import java.util.Queue;

public class SnakeObj {
    Queue<Coords> body;
    int length;

    public SnakeObj(int startX, int startY, int length) {
        body.add(new Coords(startX, startY));
    }

    public void Move(int toX, int toY) {
        body.add(new Coords(toX, toY));

        if (body.size() > length) {
            body.remove();
        }
    }

    public void EatFood() {
        length++;
    }
}
