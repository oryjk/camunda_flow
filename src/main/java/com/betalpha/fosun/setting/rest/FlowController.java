package com.betalpha.fosun.setting.rest;

import com.betalpha.fosun.api.Flow;
import com.betalpha.fosun.api.RestResponse;
import com.betalpha.fosun.setting.dao.FlowStore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/setting/flow")
@Data
public class FlowController {

    private RepositoryService repositoryService;


    private FlowStore flowStore;

    public FlowController(RepositoryService repositoryService, FlowStore flowStore) {
        this.repositoryService = repositoryService;
        this.flowStore = flowStore;
    }

    @RequestMapping(method = RequestMethod.GET)
    public RestResponse findFlowId() {
        return new RestResponse(repositoryService.createProcessDefinitionQuery().list()
                .stream()
                .filter(processDefinition -> processDefinition.getKey().equals(flowStore.getFlowId()))
                .map(processDefinition ->
                        new Flow(processDefinition.getKey(), processDefinition.getName(), processDefinition.getKey().equals(flowStore.getFlowId()))
                ).findFirst().orElseGet(Flow::new));
    }

    @RequestMapping(method = RequestMethod.POST)
    public synchronized RestResponse updateFlowId(@RequestBody Flow pFlow) {
        flowStore.setFlowId(pFlow.getId());
        Flow flow = repositoryService.createProcessDefinitionQuery().list()
                .stream().filter(processDefinition -> processDefinition.getKey().equals(pFlow.getId()))
                .map(processDefinition ->
                        new Flow(processDefinition.getKey(), processDefinition.getName(), processDefinition.getKey().equals(flowStore.getFlowId()))
                ).findFirst().orElseThrow(IllegalStateException::new);

        return new RestResponse(flow);
    }

}


