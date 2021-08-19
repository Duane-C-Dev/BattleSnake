package com.battlesnake.starter.Entities;

import com.fasterxml.jackson.annotation.*;

@lombok.Data
public class Ruleset {
    @lombok.Getter(onMethod_ = {@JsonProperty("name")})
    @lombok.Setter(onMethod_ = {@JsonProperty("name")})
    private String name;
    @lombok.Getter(onMethod_ = {@JsonProperty("version")})
    @lombok.Setter(onMethod_ = {@JsonProperty("version")})
    private String version;
}
