package com.paulofranklins;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {
    record PingPong(String result) {
    }

    @GetMapping()
    public PingPong getPingPong() {
        return new PingPong("Pong #1.");
    }
}
