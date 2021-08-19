package com.battlesnake.starter;

import java.util.Queue;

public class SnakeObj {
    String id;
    String name;
    int health;
    Queue<Coords> body;
    int latency;
    Coords head;
    int length;


    public SnakeObj(String id, String name, int health, Queue<Coords> body, int latency, Coords head, int length) {
        this.id = id;
        this.name = name;
        this.health = health;
        this.body = body;
        this.latency = latency;
        this.head = head;
        this.length = length;
    }


}
