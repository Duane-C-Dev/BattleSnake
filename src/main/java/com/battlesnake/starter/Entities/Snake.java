package com.battlesnake.starter.Entities;

import com.fasterxml.jackson.annotation.*;

@lombok.Data
public class Snake {
    @lombok.Getter(onMethod_ = {@JsonProperty("game")})
    @lombok.Setter(onMethod_ = {@JsonProperty("game")})
    private Game game;
    @lombok.Getter(onMethod_ = {@JsonProperty("turn")})
    @lombok.Setter(onMethod_ = {@JsonProperty("turn")})
    private long turn;
    @lombok.Getter(onMethod_ = {@JsonProperty("board")})
    @lombok.Setter(onMethod_ = {@JsonProperty("board")})
    private Board board;
    @lombok.Getter(onMethod_ = {@JsonProperty("you")})
    @lombok.Setter(onMethod_ = {@JsonProperty("you")})
    private You you;
}
