package com.betalpha.fosun;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Created by WangRui on 13/05/2018.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BackendFosunApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class UserTaskIT {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private FormService formService;
    private String deploymentId;


    @Before
    public void initial() {
        String deploymentId = repositoryService.createDeployment().addString("user.bpmn20.xml",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:bpmndi=\"http://www.omg" +
                        ".org/spec/BPMN/20100524/DI\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:dc=\"http://www.omg" +
                        ".org/spec/DD/20100524/DC\" xmlns:camunda=\"http://camunda.org/schema/1.0/bpmn\" xmlns:xsi=\"http://www" +
                        ".w3.org/2001/XMLSchema-instance\" id=\"Definitions_1x8z08g\" targetNamespace=\"http://bpmn.io/schema/bpmn\" " +
                        "exporter=\"Camunda Modeler\" exporterVersion=\"1.14.0\">\n" +
                        "  <bpmn:process id=\"Process_1\" isExecutable=\"true\">\n" +
                        "    <bpmn:startEvent id=\"StartEvent_1\">\n" +
                        "      <bpmn:outgoing>SequenceFlow_1kawblg</bpmn:outgoing>\n" +
                        "    </bpmn:startEvent>\n" +
                        "    <bpmn:userTask id=\"Task_1yq22kw\" camunda:formKey=\"invest_form_key\" camunda:candidateUsers=\"user1,user2,user3\">\n" +
                        "      <bpmn:extensionElements>\n" +
                        "        <camunda:executionListener class=\"com.betalpha.fosun.delegate.InvestTask\" event=\"start\" />\n" +
                        "        <camunda:taskListener class=\"com.betalpha.fosun.listener.InvestListener\" event=\"create\" />\n" +
                        "        <camunda:formData>\n" +
                        "          <camunda:formField id=\"FormField_27fg5n2\" label=\"同意吗\" type=\"boolean\" defaultValue=\"true\" />\n" +
                        "        </camunda:formData>\n" +
                        "      </bpmn:extensionElements>\n" +
                        "      <bpmn:incoming>SequenceFlow_1kawblg</bpmn:incoming>\n" +
                        "      <bpmn:outgoing>SequenceFlow_15fmqdv</bpmn:outgoing>\n" +
                        "      <bpmn:multiInstanceLoopCharacteristics>\n" +
                        "        <bpmn:loopCardinality xsi:type=\"bpmn:tFormalExpression\">10</bpmn:loopCardinality>\n" +
                        "        <bpmn:completionCondition xsi:type=\"bpmn:tFormalExpression\">${nrOfCompletedInstances/nrOfInstances &gt;= " +
                        "0.6}</bpmn:completionCondition>\n" +
                        "      </bpmn:multiInstanceLoopCharacteristics>\n" +
                        "    </bpmn:userTask>\n" +
                        "    <bpmn:endEvent id=\"EndEvent_1nyitqg\">\n" +
                        "      <bpmn:incoming>SequenceFlow_0zecdfu</bpmn:incoming>\n" +
                        "      <bpmn:incoming>SequenceFlow_11ltdqb</bpmn:incoming>\n" +
                        "    </bpmn:endEvent>\n" +
                        "    <bpmn:sequenceFlow id=\"SequenceFlow_1kawblg\" sourceRef=\"StartEvent_1\" targetRef=\"Task_1yq22kw\" />\n" +
                        "    <bpmn:serviceTask id=\"Task_0vrnpho\" camunda:class=\"com.betalpha.fosun.task.ServiceTask\">\n" +
                        "      <bpmn:incoming>SequenceFlow_1a1z6m5</bpmn:incoming>\n" +
                        "      <bpmn:outgoing>SequenceFlow_0t1t3f5</bpmn:outgoing>\n" +
                        "    </bpmn:serviceTask>\n" +
                        "    <bpmn:task id=\"Task_11u2pgw\" name=\"sssss\">\n" +
                        "      <bpmn:incoming>SequenceFlow_0t1t3f5</bpmn:incoming>\n" +
                        "      <bpmn:outgoing>SequenceFlow_0zecdfu</bpmn:outgoing>\n" +
                        "    </bpmn:task>\n" +
                        "    <bpmn:sequenceFlow id=\"SequenceFlow_0t1t3f5\" sourceRef=\"Task_0vrnpho\" targetRef=\"Task_11u2pgw\" />\n" +
                        "    <bpmn:sequenceFlow id=\"SequenceFlow_0zecdfu\" sourceRef=\"Task_11u2pgw\" targetRef=\"EndEvent_1nyitqg\" />\n" +
                        "    <bpmn:sequenceFlow id=\"SequenceFlow_1a1z6m5\" sourceRef=\"ExclusiveGateway_0p36ilx\" targetRef=\"Task_0vrnpho\">\n" +
                        "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${result==false}</bpmn:conditionExpression>\n" +
                        "    </bpmn:sequenceFlow>\n" +
                        "    <bpmn:sequenceFlow id=\"SequenceFlow_11ltdqb\" sourceRef=\"ExclusiveGateway_0p36ilx\" " +
                        "targetRef=\"EndEvent_1nyitqg\">\n" +
                        "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${result==true}</bpmn:conditionExpression>\n" +
                        "    </bpmn:sequenceFlow>\n" +
                        "    <bpmn:sequenceFlow id=\"SequenceFlow_15fmqdv\" sourceRef=\"Task_1yq22kw\" targetRef=\"ExclusiveGateway_0p36ilx\" />\n" +
                        "    <bpmn:exclusiveGateway id=\"ExclusiveGateway_0p36ilx\">\n" +
                        "      <bpmn:incoming>SequenceFlow_15fmqdv</bpmn:incoming>\n" +
                        "      <bpmn:outgoing>SequenceFlow_1a1z6m5</bpmn:outgoing>\n" +
                        "      <bpmn:outgoing>SequenceFlow_11ltdqb</bpmn:outgoing>\n" +
                        "    </bpmn:exclusiveGateway>\n" +
                        "  </bpmn:process>\n" +
                        "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n" +
                        "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"Process_1\">\n" +
                        "      <bpmndi:BPMNShape id=\"_BPMNShape_StartEvent_2\" bpmnElement=\"StartEvent_1\">\n" +
                        "        <dc:Bounds x=\"8\" y=\"150\" width=\"36\" height=\"36\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"-19\" y=\"186\" width=\"90\" height=\"20\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNShape>\n" +
                        "      <bpmndi:BPMNShape id=\"UserTask_183oyo0_di\" bpmnElement=\"Task_1yq22kw\">\n" +
                        "        <dc:Bounds x=\"173\" y=\"128\" width=\"100\" height=\"80\" />\n" +
                        "      </bpmndi:BPMNShape>\n" +
                        "      <bpmndi:BPMNShape id=\"EndEvent_1nyitqg_di\" bpmnElement=\"EndEvent_1nyitqg\">\n" +
                        "        <dc:Bounds x=\"555\" y=\"150\" width=\"36\" height=\"36\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"573\" y=\"189\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNShape>\n" +
                        "      <bpmndi:BPMNEdge id=\"SequenceFlow_1kawblg_di\" bpmnElement=\"SequenceFlow_1kawblg\">\n" +
                        "        <di:waypoint x=\"44\" y=\"168\" />\n" +
                        "        <di:waypoint x=\"173\" y=\"168\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"108.5\" y=\"146.5\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNEdge>\n" +
                        "      <bpmndi:BPMNShape id=\"ServiceTask_0lm9i9i_di\" bpmnElement=\"Task_0vrnpho\">\n" +
                        "        <dc:Bounds x=\"414\" y=\"26\" width=\"100\" height=\"80\" />\n" +
                        "      </bpmndi:BPMNShape>\n" +
                        "      <bpmndi:BPMNShape id=\"Task_11u2pgw_di\" bpmnElement=\"Task_11u2pgw\">\n" +
                        "        <dc:Bounds x=\"584.4970000000001\" y=\"26\" width=\"100\" height=\"80\" />\n" +
                        "      </bpmndi:BPMNShape>\n" +
                        "      <bpmndi:BPMNEdge id=\"SequenceFlow_0t1t3f5_di\" bpmnElement=\"SequenceFlow_0t1t3f5\">\n" +
                        "        <di:waypoint x=\"514\" y=\"66\" />\n" +
                        "        <di:waypoint x=\"584\" y=\"66\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"549\" y=\"44\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNEdge>\n" +
                        "      <bpmndi:BPMNEdge id=\"SequenceFlow_0zecdfu_di\" bpmnElement=\"SequenceFlow_0zecdfu\">\n" +
                        "        <di:waypoint x=\"634\" y=\"106\" />\n" +
                        "        <di:waypoint x=\"634\" y=\"128\" />\n" +
                        "        <di:waypoint x=\"573\" y=\"128\" />\n" +
                        "        <di:waypoint x=\"573\" y=\"150\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"603.5\" y=\"106\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNEdge>\n" +
                        "      <bpmndi:BPMNEdge id=\"SequenceFlow_1a1z6m5_di\" bpmnElement=\"SequenceFlow_1a1z6m5\">\n" +
                        "        <di:waypoint x=\"336\" y=\"144\" />\n" +
                        "        <di:waypoint x=\"336\" y=\"66\" />\n" +
                        "        <di:waypoint x=\"414\" y=\"66\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"351\" y=\"98.5\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNEdge>\n" +
                        "      <bpmndi:BPMNEdge id=\"SequenceFlow_11ltdqb_di\" bpmnElement=\"SequenceFlow_11ltdqb\">\n" +
                        "        <di:waypoint x=\"362\" y=\"168\" />\n" +
                        "        <di:waypoint x=\"555\" y=\"168\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"458.5\" y=\"146.5\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNEdge>\n" +
                        "      <bpmndi:BPMNEdge id=\"SequenceFlow_15fmqdv_di\" bpmnElement=\"SequenceFlow_15fmqdv\">\n" +
                        "        <di:waypoint x=\"273\" y=\"168\" />\n" +
                        "        <di:waypoint x=\"312\" y=\"168\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"292.5\" y=\"146.5\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNEdge>\n" +
                        "      <bpmndi:BPMNShape id=\"ExclusiveGateway_0t889i2_di\" bpmnElement=\"ExclusiveGateway_0p36ilx\" " +
                        "isMarkerVisible=\"true\">\n" +
                        "        <dc:Bounds x=\"312\" y=\"143\" width=\"50\" height=\"50\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"336.497\" y=\"196\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNShape>\n" +
                        "    </bpmndi:BPMNPlane>\n" +
                        "  </bpmndi:BPMNDiagram>\n" +
                        "</bpmn:definitions>\n").deploy().getId();
    }

    @Test
    public void read_condidate_test() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_1");
        log.info("processInstance.getId() {}", processInstance.getId());
        TaskQuery taskQuery = taskService.createTaskQuery().processInstanceId(processInstance.getId());
        List<Task> beforeTask = taskQuery.list();
        Assert.assertEquals(10, beforeTask.size());
        Map<String, Object> var = new HashMap<>();
        var.put("result", false);
        IntStream.range(0, 6).forEach(index -> {
            log.info("task id {}", beforeTask.get(index).getId());
            taskService.complete(beforeTask.get(index).getId(), var);
        });
        List<Task> tasks = taskQuery.list();
        log.info("tasks {}", tasks);

    }
}
