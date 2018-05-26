package com.betalpha.fosun.controller;

import com.betalpha.fosun.api.process.Task;
import com.betalpha.fosun.model.Ballot;
import com.betalpha.fosun.store.VoteCountStore;
import com.betalpha.fosun.user.service.BarTaskService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
        List<Task> tasks = barTaskService.queryUserTask(userId);
        List<Task> taskList = new ArrayList<>();
        //去掉已投票的
        for (Task task : tasks) {
            Ballot ballot = VoteCountStore.voteResult.get(task.getProcessInstanceId());
            if (ballot != null){
                if (!ballot.getContinues().contains(userId) && !ballot.getApprove().contains(userId) && !ballot.getReject().contains(userId)){
                    taskList.add(task);
                }
            }else {
                taskList.add(task);
            }
        }
        return ResponseEntity.ok(taskList);
    }

    @RequestMapping(value = "/task/started/{userId}", method = RequestMethod.GET)
    public ResponseEntity getUserStartedTask(@PathVariable("userId") String userId) {
        List<Task> result = Lists.newArrayList();
        List<Task> activeTaskList = barTaskService.querySubmitterTask(userId);
        List<Task> hisTaskList = barTaskService.queryHistoryTask(userId);
        result.addAll(activeTaskList);
        result.addAll(hisTaskList);
        return ResponseEntity.ok(result);
    }
}
