package com.betalpha.fosun;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * Created by WangRui on 12/05/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BackendFosunApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class VoteTaskIT {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    private String deploymentId;

    @Before
    public void initialize() {
        deploymentId = repositoryService.createDeployment().addString("vote.bpmn20.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:camunda=\"http://camunda.org/schema/1.0/bpmn\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" id=\"Definitions_04sj2nb\" targetNamespace=\"http://bpmn.io/schema/bpmn\" exporter=\"Camunda Modeler\" exporterVersion=\"1.14.0\">\n" +
                "  <bpmn:process id=\"Process_1\" isExecutable=\"true\">\n" +
                "    <bpmn:startEvent id=\"StartEvent_1\">\n" +
                "      <bpmn:outgoing>SequenceFlow_0vktym9</bpmn:outgoing>\n" +
                "    </bpmn:startEvent>\n" +
                "    <bpmn:endEvent id=\"EndEvent_0ohvo8c\">\n" +
                "      <bpmn:extensionElements>\n" +
                "        <camunda:executionListener class=\"com.betalpha.fosun.listener.UserEndExecutionListener\" event=\"start\" />\n" +
                "      </bpmn:extensionElements>\n" +
                "      <bpmn:incoming>SequenceFlow_079wse1</bpmn:incoming>\n" +
                "      <bpmn:incoming>SequenceFlow_0u4p0fk</bpmn:incoming>\n" +
                "      <bpmn:incoming>SequenceFlow_0vgu1n5</bpmn:incoming>\n" +
                "    </bpmn:endEvent>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0vktym9\" sourceRef=\"StartEvent_1\" targetRef=\"Task_11stjz1\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_16owqsy\" sourceRef=\"Task_11stjz1\" targetRef=\"Task_1yheu4x\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_079wse1\" sourceRef=\"Task_1yheu4x\" targetRef=\"EndEvent_0ohvo8c\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${result==0}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:task id=\"Task_0iu24hu\">\n" +
                "      <bpmn:incoming>SequenceFlow_109r9lo</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_0u4p0fk</bpmn:outgoing>\n" +
                "    </bpmn:task>\n" +
                "    <bpmn:userTask id=\"Task_1yheu4x\" camunda:candidateUsers=\"user2\">\n" +
                "      <bpmn:extensionElements>\n" +
                "        <camunda:inputOutput>\n" +
                "          <camunda:inputParameter name=\"Input_3fp32a8\">result</camunda:inputParameter>\n" +
                "          <camunda:outputParameter name=\"Output_3gspph0\">result</camunda:outputParameter>\n" +
                "        </camunda:inputOutput>\n" +
                "      </bpmn:extensionElements>\n" +
                "      <bpmn:incoming>SequenceFlow_16owqsy</bpmn:incoming>\n" +
                "      <bpmn:incoming>SequenceFlow_1hzxu2k</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_079wse1</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>SequenceFlow_109r9lo</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>SequenceFlow_1fbrl2u</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>SequenceFlow_1je1g21</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:userTask id=\"Task_0hf0ic1\" camunda:candidateUsers=\"user1\">\n" +
                "      <bpmn:incoming>SequenceFlow_1fbrl2u</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_0vgu1n5</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_109r9lo\" sourceRef=\"Task_1yheu4x\" targetRef=\"Task_0iu24hu\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${result==1}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0u4p0fk\" sourceRef=\"Task_0iu24hu\" targetRef=\"EndEvent_0ohvo8c\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0vgu1n5\" sourceRef=\"Task_0hf0ic1\" targetRef=\"EndEvent_0ohvo8c\" />\n" +
                "    <bpmn:userTask id=\"Task_11stjz1\" camunda:candidateUsers=\"user4\">\n" +
                "      <bpmn:incoming>SequenceFlow_0vktym9</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_16owqsy</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1fbrl2u\" sourceRef=\"Task_1yheu4x\" targetRef=\"Task_0hf0ic1\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${result==2}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:exclusiveGateway id=\"ExclusiveGateway_1qddikt\">\n" +
                "      <bpmn:extensionElements>\n" +
                "        <camunda:executionListener class=\"com.betalpha.fosun.listener.UserEndExecutionListener\" event=\"start\" />\n" +
                "      </bpmn:extensionElements>\n" +
                "      <bpmn:incoming>SequenceFlow_1je1g21</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_1hzxu2k</bpmn:outgoing>\n" +
                "    </bpmn:exclusiveGateway>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1je1g21\" sourceRef=\"Task_1yheu4x\" targetRef=\"ExclusiveGateway_1qddikt\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${result==5}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1hzxu2k\" sourceRef=\"ExclusiveGateway_1qddikt\" targetRef=\"Task_1yheu4x\" />\n" +
                "  </bpmn:process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"Process_1\">\n" +
                "      <bpmndi:BPMNShape id=\"_BPMNShape_StartEvent_2\" bpmnElement=\"StartEvent_1\">\n" +
                "        <dc:Bounds x=\"171\" y=\"86\" width=\"36\" height=\"36\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"144\" y=\"122\" width=\"90\" height=\"20\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"EndEvent_0ohvo8c_di\" bpmnElement=\"EndEvent_0ohvo8c\">\n" +
                "        <dc:Bounds x=\"842\" y=\"102\" width=\"36\" height=\"36\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"860\" y=\"141\" width=\"0\" height=\"13\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_0vktym9_di\" bpmnElement=\"SequenceFlow_0vktym9\">\n" +
                "        <di:waypoint x=\"207\" y=\"104\" />\n" +
                "        <di:waypoint x=\"280\" y=\"104\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"198.5\" y=\"82.5\" width=\"90\" height=\"13\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_16owqsy_di\" bpmnElement=\"SequenceFlow_16owqsy\">\n" +
                "        <di:waypoint x=\"380\" y=\"100\" />\n" +
                "        <di:waypoint x=\"400\" y=\"99\" />\n" +
                "        <di:waypoint x=\"469\" y=\"99\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"345\" y=\"78\" width=\"90\" height=\"13\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_079wse1_di\" bpmnElement=\"SequenceFlow_079wse1\">\n" +
                "        <di:waypoint x=\"575\" y=\"120\" />\n" +
                "        <di:waypoint x=\"842\" y=\"120\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"708.5\" y=\"98.5\" width=\"0\" height=\"13\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape id=\"Task_0iu24hu_di\" bpmnElement=\"Task_0iu24hu\">\n" +
                "        <dc:Bounds x=\"647\" y=\"201\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_0y6xlg6_di\" bpmnElement=\"Task_1yheu4x\">\n" +
                "        <dc:Bounds x=\"475\" y=\"80\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_0riwedv_di\" bpmnElement=\"Task_0hf0ic1\">\n" +
                "        <dc:Bounds x=\"647\" y=\"-36\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_109r9lo_di\" bpmnElement=\"SequenceFlow_109r9lo\">\n" +
                "        <di:waypoint x=\"525\" y=\"160\" />\n" +
                "        <di:waypoint x=\"525\" y=\"241\" />\n" +
                "        <di:waypoint x=\"647\" y=\"241\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"540\" y=\"194\" width=\"0\" height=\"13\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_0u4p0fk_di\" bpmnElement=\"SequenceFlow_0u4p0fk\">\n" +
                "        <di:waypoint x=\"747\" y=\"241\" />\n" +
                "        <di:waypoint x=\"783\" y=\"241\" />\n" +
                "        <di:waypoint x=\"783\" y=\"120\" />\n" +
                "        <di:waypoint x=\"842\" y=\"120\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"798\" y=\"174\" width=\"0\" height=\"13\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_0vgu1n5_di\" bpmnElement=\"SequenceFlow_0vgu1n5\">\n" +
                "        <di:waypoint x=\"747\" y=\"4\" />\n" +
                "        <di:waypoint x=\"783\" y=\"4\" />\n" +
                "        <di:waypoint x=\"783\" y=\"120\" />\n" +
                "        <di:waypoint x=\"842\" y=\"120\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"798\" y=\"55.5\" width=\"0\" height=\"13\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_15nih7t_di\" bpmnElement=\"Task_11stjz1\">\n" +
                "        <dc:Bounds x=\"280\" y=\"64\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1fbrl2u_di\" bpmnElement=\"SequenceFlow_1fbrl2u\">\n" +
                "        <di:waypoint x=\"525\" y=\"80\" />\n" +
                "        <di:waypoint x=\"525\" y=\"4\" />\n" +
                "        <di:waypoint x=\"647\" y=\"4\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"540\" y=\"35.5\" width=\"0\" height=\"13\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape id=\"ExclusiveGateway_1qddikt_di\" bpmnElement=\"ExclusiveGateway_1qddikt\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds x=\"395.75\" y=\"237.25\" width=\"50\" height=\"50\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"420.75\" y=\"290.25\" width=\"0\" height=\"13\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1je1g21_di\" bpmnElement=\"SequenceFlow_1je1g21\">\n" +
                "        <di:waypoint x=\"507\" y=\"160\" />\n" +
                "        <di:waypoint x=\"507\" y=\"202\" />\n" +
                "        <di:waypoint x=\"507\" y=\"262\" />\n" +
                "        <di:waypoint x=\"445\" y=\"262\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"522\" y=\"225.5\" width=\"0\" height=\"13\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1hzxu2k_di\" bpmnElement=\"SequenceFlow_1hzxu2k\">\n" +
                "        <di:waypoint x=\"421\" y=\"237\" />\n" +
                "        <di:waypoint x=\"421\" y=\"120\" />\n" +
                "        <di:waypoint x=\"475\" y=\"120\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"436\" y=\"171.5\" width=\"0\" height=\"13\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</bpmn:definitions>\n").deploy().getId();
        log.info("deploymentId {}", deploymentId);


    }

    @Test
    public void case_line1_test() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_1");
        log.info("processInstance.getId() {}", processInstance.getId());
        Task task = taskService.createTaskQuery().taskCandidateUser("user4").singleResult();
        taskService.complete(task.getId(), Variables.putValue("result",0));
        Task task2 = taskService.createTaskQuery().taskCandidateUser("user2").singleResult();
        taskService.complete(task2.getId());

    }

    @Test
    public void case_line2_test(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_1");
        log.info("processInstance.getId() {}", processInstance.getId());
        Task task = taskService.createTaskQuery().taskCandidateUser("user4").singleResult();
        taskService.complete(task.getId(), Variables.putValue("result",2));
        Task task2 = taskService.createTaskQuery().taskCandidateUser("user2").singleResult();
        taskService.complete(task2.getId());
        Task task1 = taskService.createTaskQuery().taskCandidateUser("user1").singleResult();
        taskService.complete(task1.getId());
    }

    @Test
    public void case_line3_test(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_1");
        log.info("processInstance.getId() {}", processInstance.getId());
        Task task = taskService.createTaskQuery().taskCandidateUser("user4").singleResult();
        taskService.complete(task.getId(), Variables.putValue("result",1));
        Task task2 = taskService.createTaskQuery().taskCandidateUser("user2").singleResult();
        taskService.complete(task2.getId());
    }

    @Test
    public void case_gateway_test(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_1");
        log.info("processInstance.getId() {}", processInstance.getId());
        Task task = taskService.createTaskQuery().taskCandidateUser("user4").singleResult();
        taskService.complete(task.getId(), Variables.putValue("result",1));
        Task task2 = taskService.createTaskQuery().taskCandidateUser("user2").singleResult();
        System.out.println(task2.getId());
        taskService.complete(task2.getId(), Variables.putValue("result",5));
        task2 = taskService.createTaskQuery().taskCandidateUser("user2").singleResult();
        System.out.println(task2.getId());
        taskService.complete(task2.getId(), Variables.putValue("result",0));
    }

    @After
    public void destroy() {
        repositoryService.deleteDeployment(deploymentId, true);
    }
}
