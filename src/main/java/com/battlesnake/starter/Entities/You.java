package com.battlesnake.starter.Entities;

import com.fasterxml.jackson.annotation.*;
import java.util.List;

@lombok.Data
public class You {
    @lombok.Getter(onMethod_ = {@JsonProperty("id")})
    @lombok.Setter(onMethod_ = {@JsonProperty("id")})
    private String id;
    @lombok.Getter(onMethod_ = {@JsonProperty("name")})
    @lombok.Setter(onMethod_ = {@JsonProperty("name")})
    private String name;
    @lombok.Getter(onMethod_ = {@JsonProperty("health")})
    @lombok.Setter(onMethod_ = {@JsonProperty("health")})
    private long health;
    @lombok.Getter(onMethod_ = {@JsonProperty("body")})
    @lombok.Setter(onMethod_ = {@JsonProperty("body")})
    private List<Head> body;
    @lombok.Getter(onMethod_ = {@JsonProperty("latency")})
    @lombok.Setter(onMethod_ = {@JsonProperty("latency")})
    private String latency;
    @lombok.Getter(onMethod_ = {@JsonProperty("head")})
    @lombok.Setter(onMethod_ = {@JsonProperty("head")})
    private Head head;
    @lombok.Getter(onMethod_ = {@JsonProperty("length")})
    @lombok.Setter(onMethod_ = {@JsonProperty("length")})
    private long length;
    @lombok.Getter(onMethod_ = {@JsonProperty("shout")})
    @lombok.Setter(onMethod_ = {@JsonProperty("shout")})
    private String shout;
    @lombok.Getter(onMethod_ = {@JsonProperty("squad")})
    @lombok.Setter(onMethod_ = {@JsonProperty("squad")})
    private String squad;
}
