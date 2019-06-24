package com.example.gatewaysample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class GatewayContoller {

    @RequestMapping(value = "/token")
    public Mono<String> getToken(
            @RequestHeader(value = "key") String key) {
        return Mono.just("token string");
    }

    @GetMapping("/fallback")
    public Mono<String> fallback() {
        return Mono.just("fallback");
    }

}
