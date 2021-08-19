package com.battlesnake.starter;

import java.util.ArrayList;
import java.util.List;

public class Coords {
    int x;
    int y;

    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public List<Coords> getSurroundingNodes() {
        List<Coords> surrounding = new ArrayList<>();
        surrounding.add(new Coords(x + 1, y));
        surrounding.add(new Coords(x - 1, y));
        surrounding.add(new Coords(x, y + 1));
        surrounding.add(new Coords(x, y - 1));
        return surrounding;
    }

    public String getMove(Coords location) {
        if (location.x == x + 1 && location.y == y) {
            return "right";
        } else if(location.x == x - 1 && location.y == y) {
            return "left";
        } else if(location.x == x && location.y == y + 1) {
            return "up";
        } else if(location.x == x && location.y == y - 1) {
            return "down";
        }
        return null;
    }

    @Override
    public String toString() {
        return "Coords{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
