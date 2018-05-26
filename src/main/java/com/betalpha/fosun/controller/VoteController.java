package com.betalpha.fosun.controller;

import com.betalpha.fosun.api.VoteApi;
import com.betalpha.fosun.model.Ballot;
import com.betalpha.fosun.store.VoteCountStore;
import com.betalpha.fosun.user.DepartmentService;
import com.betalpha.fosun.user.OrganizationStructureService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@Slf4j
@RequestMapping("/api/task")
public class VoteController {

    private static final String RESULT = "result";
    @Autowired
    private TaskService taskService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private OrganizationStructureService organizationStructureService;


    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity vote(@RequestBody VoteApi voteApi) {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateUser(voteApi.getUserId()).list().stream().filter(task -> voteApi.getTaskIds()
                .contains(task.getProcessInstanceId())).collect(toList());
        tasks.stream().filter(task -> VoteCountStore.voteResult.get(task.getProcessInstanceId()) == null).forEach(task -> VoteCountStore.voteResult
                .put(task.getProcessInstanceId(), new Ballot()));
        if (!CollectionUtils.isEmpty(tasks.stream().filter(task -> getAllUserByTask(task.getProcessInstanceId()).contains(voteApi.getUserId()))
                .collect(Collectors.toList()))) {
            throw new IllegalArgumentException("已经投票的不能再投票");
        }

        switch (voteApi.getType()) {
            case "APPROVE":
                tasks.forEach(task -> VoteCountStore.voteResult.get(task.getProcessInstanceId()).getApprove().add(voteApi.getUserId()));
                break;
            case "REJECT":
                tasks.forEach(task -> VoteCountStore.voteResult.get(task.getProcessInstanceId()).getReject().add(voteApi.getUserId()));
                break;
            case "CONTINUE":
                tasks.forEach(task -> VoteCountStore.voteResult.get(task.getProcessInstanceId()).getContinues().add(voteApi.getUserId()));
                break;
            default:
                log.error("vote type is not matching ，type {}", voteApi.getType());
                throw new IllegalArgumentException("投票类型不匹配");
        }

        tasks.forEach(task -> {
                    Ballot ballot = VoteCountStore.voteResult.get(task.getProcessInstanceId());
                    Integer personSize = taskService.getIdentityLinksForTask(task.getId()).size();
                    if (personSize != ballot.getApprove().size() + ballot.getContinues().size() + ballot.getReject().size()) {
                        return;
                    }

                    //继续研究
                    if (!CollectionUtils.isEmpty(ballot.getContinues())
                            && "1".equals(taskService.getVariable(task.getId(), "continueResearch"))) {
                        clearVoteResult(task.getProcessInstanceId());
                        taskService.setVariable(task.getId(), RESULT, 2);
                        taskService.complete(task.getId());
                        return;
                    }

                    //一票否决
                    if ("1".equals(taskService.getVariable(task.getId(), "oneVote"))
                            && groupOneVotePower(ballot.getReject())) {
                        clearVoteResult(task.getProcessInstanceId());
                        taskService.setVariable(task.getId(), RESULT, 0);
                        taskService.complete(task.getId());
                        return;
                    }


                    //其他情况
                    Double passThreshold = Double.valueOf(taskService.getVariable(task.getId(), "passThreshold").toString());
                    Double voteResult = new BigDecimal(ballot.getApprove().size()).divide(new BigDecimal(personSize), 2, BigDecimal
                            .ROUND_HALF_DOWN).doubleValue();

                    //否决
                    if (voteResult.compareTo(passThreshold) < 0) {
                        taskService.setVariable(task.getId(), RESULT, 0);
                        taskService.complete(task.getId());
                        return;
                    }


                    //没有勾选全票通过
                    if (taskService.getVariable(task.getId(), "allPassed").equals("0")) {
                        taskService.setVariable(task.getId(), RESULT, 1);
                        taskService.complete(task.getId());
                        return;
                    }
                    //勾选全票通过
                    taskService.setVariable(task.getId(), RESULT, 3);
                    taskService.complete(task.getId());
                    log.info("task id: {} finished !", task.getId());
                }
        );
        return ResponseEntity.ok().build();
    }

    private boolean groupOneVotePower(Set<String> rejects) {
        String[] power = {""};
        rejects.forEach(reject -> power[0] = identityService.getUserInfo(reject, "oneVotePower"));
        return "true".equals(power[0]);
    }

    private void clearVoteResult(String processInstanceId) {
        VoteCountStore.voteResult.get(processInstanceId).getReject().clear();
        VoteCountStore.voteResult.get(processInstanceId).getContinues().clear();
        VoteCountStore.voteResult.get(processInstanceId).getApprove().clear();
    }

    private List<String> getAllUserByTask(String taskId) {
        List<String> list = Lists.newArrayList();
        list.addAll(VoteCountStore.voteResult.get(taskId).getApprove());
        list.addAll(VoteCountStore.voteResult.get(taskId).getContinues());
        list.addAll(VoteCountStore.voteResult.get(taskId).getReject());
        return list;
    }

}
