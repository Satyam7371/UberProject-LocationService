package org.example.uberprojectlocationservice.controllers;

import org.example.uberprojectlocationservice.services.RedisTestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisTestController {

    private final RedisTestService redisTestService;

    public RedisTestController(RedisTestService redisTestService) {
        this.redisTestService = redisTestService;
    }

    @GetMapping("/redis/set")
    public String setValue(@RequestParam String key, @RequestParam String value) {
        redisTestService.saveValue(key, value);
        return "Saved " + key + " = " + value;
    }

    @GetMapping("/redis/get")
    public String getValue(@RequestParam String key) {
        String value = redisTestService.getValue(key);
        return value == null ? "No value found" : value;
    }
}
