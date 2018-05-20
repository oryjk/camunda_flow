package com.betalpha.fosun;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by WangRui on 12/05/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BackendFosunApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@Ignore
public class InvestTaskIT {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    private String deploymentId;

    @Before
    public void initialize() {
        deploymentId = repositoryService.createDeployment().addString("invest.bpmn20.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:bpmndi=\"http://www.omg" +
                ".org/spec/BPMN/20100524/DI\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:dc=\"http://www.omg" +
                ".org/spec/DD/20100524/DC\" xmlns:camunda=\"http://camunda.org/schema/1.0/bpmn\" id=\"Definitions_1x8z08g\" " +
                "targetNamespace=\"http://bpmn.io/schema/bpmn\" exporter=\"Camunda Modeler\" exporterVersion=\"1.14.0\">\n" +
                "  <bpmn:process id=\"Process_1\" isExecutable=\"true\">\n" +
                "    <bpmn:startEvent id=\"StartEvent_1\">\n" +
                "      <bpmn:outgoing>SequenceFlow_1kawblg</bpmn:outgoing>\n" +
                "    </bpmn:startEvent>\n" +
                "    <bpmn:userTask id=\"Task_1yq22kw\">\n" +
                "      <bpmn:extensionElements>\n" +
                "        <camunda:executionListener class=\"com.betalpha.fosun.delegate.InvestTask\" event=\"start\" />\n" +
                "      </bpmn:extensionElements>\n" +
                "      <bpmn:incoming>SequenceFlow_1kawblg</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_1s19kna</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:endEvent id=\"EndEvent_1nyitqg\">\n" +
                "      <bpmn:incoming>SequenceFlow_1s19kna</bpmn:incoming>\n" +
                "    </bpmn:endEvent>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1kawblg\" sourceRef=\"StartEvent_1\" targetRef=\"Task_1yq22kw\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1s19kna\" sourceRef=\"Task_1yq22kw\" targetRef=\"EndEvent_1nyitqg\" />\n" +
                "  </bpmn:process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"Process_1\">\n" +
                "      <bpmndi:BPMNShape id=\"_BPMNShape_StartEvent_2\" bpmnElement=\"StartEvent_1\">\n" +
                "        <dc:Bounds x=\"222\" y=\"135\" width=\"36\" height=\"36\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"195\" y=\"171\" width=\"90\" height=\"20\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_183oyo0_di\" bpmnElement=\"Task_1yq22kw\">\n" +
                "        <dc:Bounds x=\"338\" y=\"113\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"EndEvent_1nyitqg_di\" bpmnElement=\"EndEvent_1nyitqg\">\n" +
                "        <dc:Bounds x=\"553\" y=\"135\" width=\"36\" height=\"36\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"571\" y=\"174\" width=\"0\" height=\"13\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1kawblg_di\" bpmnElement=\"SequenceFlow_1kawblg\">\n" +
                "        <di:waypoint x=\"258\" y=\"153\" />\n" +
                "        <di:waypoint x=\"338\" y=\"153\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"298\" y=\"131\" width=\"0\" height=\"13\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1s19kna_di\" bpmnElement=\"SequenceFlow_1s19kna\">\n" +
                "        <di:waypoint x=\"438\" y=\"153\" />\n" +
                "        <di:waypoint x=\"553\" y=\"153\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"495.5\" y=\"131.5\" width=\"0\" height=\"13\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</bpmn:definitions>\n").deploy().getId();
        log.info("deploymentId {}",deploymentId);
    }

    @Test
    public void investTask_test() {

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_1");
        TaskQuery taskQuery = taskService.createTaskQuery().processInstanceId(processInstance.getId());
        List<Task> tasks = taskQuery.list();
        Assert.assertEquals(1, tasks.size());
        log.info("Task count {}", tasks.size());
        tasks.stream().forEach(task -> {
            log.info("Task {}", task.toString());
            taskService.complete(task.getId());
        });
    }

    @After
    public void destroy() {
        repositoryService.deleteDeployment(deploymentId, true);
    }
}
