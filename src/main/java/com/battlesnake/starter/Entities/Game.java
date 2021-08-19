package com.battlesnake.starter.Entities;

import com.fasterxml.jackson.annotation.*;

@lombok.Data
public class Game {
    @lombok.Getter(onMethod_ = {@JsonProperty("id")})
    @lombok.Setter(onMethod_ = {@JsonProperty("id")})
    private String id;
    @lombok.Getter(onMethod_ = {@JsonProperty("ruleset")})
    @lombok.Setter(onMethod_ = {@JsonProperty("ruleset")})
    private Ruleset ruleset;
    @lombok.Getter(onMethod_ = {@JsonProperty("timeout")})
    @lombok.Setter(onMethod_ = {@JsonProperty("timeout")})
    private long timeout;
}
