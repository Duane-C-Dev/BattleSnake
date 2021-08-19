package com.battlesnake.starter.Entities;

import com.fasterxml.jackson.annotation.*;
import java.util.List;

@lombok.Data
public class Board {
    @lombok.Getter(onMethod_ = {@JsonProperty("height")})
    @lombok.Setter(onMethod_ = {@JsonProperty("height")})
    private long height;
    @lombok.Getter(onMethod_ = {@JsonProperty("width")})
    @lombok.Setter(onMethod_ = {@JsonProperty("width")})
    private long width;
    @lombok.Getter(onMethod_ = {@JsonProperty("food")})
    @lombok.Setter(onMethod_ = {@JsonProperty("food")})
    private List<Head> food;
    @lombok.Getter(onMethod_ = {@JsonProperty("hazards")})
    @lombok.Setter(onMethod_ = {@JsonProperty("hazards")})
    private List<Head> hazards;
    @lombok.Getter(onMethod_ = {@JsonProperty("snakes")})
    @lombok.Setter(onMethod_ = {@JsonProperty("snakes")})
    private List<You> snakes;
}
