package com.battlesnake.starter.Entities;

import com.fasterxml.jackson.annotation.*;

@lombok.Data
public class Head {
    @lombok.Getter(onMethod_ = {@JsonProperty("x")})
    @lombok.Setter(onMethod_ = {@JsonProperty("x")})
    private long x;
    @lombok.Getter(onMethod_ = {@JsonProperty("y")})
    @lombok.Setter(onMethod_ = {@JsonProperty("y")})
    private long y;
}
