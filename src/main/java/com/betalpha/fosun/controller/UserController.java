package com.betalpha.fosun.controller;

import com.betalpha.fosun.user.service.BarTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * created on 2018/5/16
 *
 * @author huzongpeng
 */
@RestController
@Slf4j
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {


    @Autowired
    private BarTaskService barTaskService;

    @RequestMapping(value = "/task/todo/{userId}", method = RequestMethod.GET)
    public ResponseEntity getUserTask(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(barTaskService.queryUserTask(userId));
    }

    @RequestMapping(value = "/task/started/{userId}", method = RequestMethod.GET)
    public ResponseEntity getUserStartedTask(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(barTaskService.querySubmitterTask(userId));
    }
}
