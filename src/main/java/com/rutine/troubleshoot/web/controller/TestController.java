package com.rutine.troubleshoot.web.controller;

import com.rutine.troubleshoot.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author rutine
 * @date 2019/3/12 18:33
 */
@RestController
@RequestMapping("/test")
public class TestController {
    private static Logger logger = LoggerFactory.getLogger(TestController.class);


    @PostMapping
    public String test(@RequestBody Map map) {
        logger.info("request params={}", JsonUtils.toJson(map));

        return "test";
    }
}
