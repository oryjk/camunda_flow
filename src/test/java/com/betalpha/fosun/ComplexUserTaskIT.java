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

/**
 * Created by WangRui on 13/05/2018.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BackendFosunApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class ComplexUserTaskIT {

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

    @Autowired
    private HistoryService historyService;

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
                        "    <bpmn:userTask id=\"Task_1yq22kw\" name=\"投决会投票\" camunda:candidateUsers=\"user1,user2,user3\">\n" +
                        "      <bpmn:extensionElements>\n" +
                        "        <camunda:taskListener class=\"com.betalpha.fosun.listener.InvestListener\" event=\"complete\" />\n" +
                        "      </bpmn:extensionElements>\n" +
                        "      <bpmn:incoming>SequenceFlow_1kawblg</bpmn:incoming>\n" +
                        "      <bpmn:outgoing>SequenceFlow_15fmqdv</bpmn:outgoing>\n" +
                        "      <bpmn:multiInstanceLoopCharacteristics>\n" +
                        "        <bpmn:loopCardinality xsi:type=\"bpmn:tFormalExpression\">3</bpmn:loopCardinality>\n" +
                        "      </bpmn:multiInstanceLoopCharacteristics>\n" +
                        "    </bpmn:userTask>\n" +
                        "    <bpmn:sequenceFlow id=\"SequenceFlow_1kawblg\" sourceRef=\"StartEvent_1\" targetRef=\"Task_1yq22kw\" />\n" +
                        "    <bpmn:sequenceFlow id=\"SequenceFlow_15fmqdv\" sourceRef=\"Task_1yq22kw\" targetRef=\"ExclusiveGateway_0p36ilx\" />\n" +
                        "    <bpmn:exclusiveGateway id=\"ExclusiveGateway_0p36ilx\">\n" +
                        "      <bpmn:incoming>SequenceFlow_15fmqdv</bpmn:incoming>\n" +
                        "      <bpmn:outgoing>SequenceFlow_1j9ft3l</bpmn:outgoing>\n" +
                        "      <bpmn:outgoing>SequenceFlow_0bbsi0o</bpmn:outgoing>\n" +
                        "    </bpmn:exclusiveGateway>\n" +
                        "    <bpmn:endEvent id=\"EndEvent_1a8c7yq\">\n" +
                        "      <bpmn:incoming>SequenceFlow_1j9ft3l</bpmn:incoming>\n" +
                        "    </bpmn:endEvent>\n" +
                        "    <bpmn:sequenceFlow id=\"SequenceFlow_1j9ft3l\" name=\"否定了\" sourceRef=\"ExclusiveGateway_0p36ilx\" " +
                        "targetRef=\"EndEvent_1a8c7yq\">\n" +
                        "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${x==2}</bpmn:conditionExpression>\n" +
                        "    </bpmn:sequenceFlow>\n" +
                        "    <bpmn:sequenceFlow id=\"SequenceFlow_0bbsi0o\" name=\"通过\" sourceRef=\"ExclusiveGateway_0p36ilx\" " +
                        "targetRef=\"Task_0gs5ln7\">\n" +
                        "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${x==1}</bpmn:conditionExpression>\n" +
                        "    </bpmn:sequenceFlow>\n" +
                        "    <bpmn:userTask id=\"Task_0gs5ln7\" name=\"董事会投票\">\n" +
                        "      <bpmn:extensionElements>\n" +
                        "        <camunda:executionListener class=\"com.betalpha.fosun.delegate.InvestTask\" event=\"start\" />\n" +
                        "      </bpmn:extensionElements>\n" +
                        "      <bpmn:incoming>SequenceFlow_0bbsi0o</bpmn:incoming>\n" +
                        "      <bpmn:outgoing>SequenceFlow_1m2950i</bpmn:outgoing>\n" +
                        "    </bpmn:userTask>\n" +
                        "    <bpmn:exclusiveGateway id=\"ExclusiveGateway_0ccns8l\">\n" +
                        "      <bpmn:incoming>SequenceFlow_1m2950i</bpmn:incoming>\n" +
                        "      <bpmn:outgoing>SequenceFlow_0a76doz</bpmn:outgoing>\n" +
                        "      <bpmn:outgoing>SequenceFlow_0brr8vt</bpmn:outgoing>\n" +
                        "      <bpmn:outgoing>SequenceFlow_1tply3e</bpmn:outgoing>\n" +
                        "    </bpmn:exclusiveGateway>\n" +
                        "    <bpmn:sequenceFlow id=\"SequenceFlow_1m2950i\" sourceRef=\"Task_0gs5ln7\" targetRef=\"ExclusiveGateway_0ccns8l\" />\n" +
                        "    <bpmn:sequenceFlow id=\"SequenceFlow_0a76doz\" sourceRef=\"ExclusiveGateway_0ccns8l\" targetRef=\"Task_0s70ujr\">\n" +
                        "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${y==2}</bpmn:conditionExpression>\n" +
                        "    </bpmn:sequenceFlow>\n" +
                        "    <bpmn:sequenceFlow id=\"SequenceFlow_0brr8vt\" sourceRef=\"ExclusiveGateway_0ccns8l\" targetRef=\"Task_1k01bsq\">\n" +
                        "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${y==3}</bpmn:conditionExpression>\n" +
                        "    </bpmn:sequenceFlow>\n" +
                        "    <bpmn:sequenceFlow id=\"SequenceFlow_1tply3e\" sourceRef=\"ExclusiveGateway_0ccns8l\" targetRef=\"Task_0auiq1i\">\n" +
                        "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${y==1}</bpmn:conditionExpression>\n" +
                        "    </bpmn:sequenceFlow>\n" +
                        "    <bpmn:userTask id=\"Task_0auiq1i\" name=\"经理投票\">\n" +
                        "      <bpmn:incoming>SequenceFlow_1tply3e</bpmn:incoming>\n" +
                        "      <bpmn:outgoing>SequenceFlow_1ow1dwh</bpmn:outgoing>\n" +
                        "    </bpmn:userTask>\n" +
                        "    <bpmn:userTask id=\"Task_1k01bsq\" name=\"董事长投票\">\n" +
                        "      <bpmn:incoming>SequenceFlow_0brr8vt</bpmn:incoming>\n" +
                        "      <bpmn:outgoing>SequenceFlow_0y2rtuy</bpmn:outgoing>\n" +
                        "    </bpmn:userTask>\n" +
                        "    <bpmn:userTask id=\"Task_0s70ujr\" name=\"副经理投票\">\n" +
                        "      <bpmn:incoming>SequenceFlow_0a76doz</bpmn:incoming>\n" +
                        "      <bpmn:outgoing>SequenceFlow_036gszf</bpmn:outgoing>\n" +
                        "    </bpmn:userTask>\n" +
                        "    <bpmn:endEvent id=\"EndEvent_1rmaenx\">\n" +
                        "      <bpmn:extensionElements>\n" +
                        "        <camunda:executionListener class=\"\" event=\"start\" />\n" +
                        "      </bpmn:extensionElements>\n" +
                        "      <bpmn:incoming>SequenceFlow_0y2rtuy</bpmn:incoming>\n" +
                        "      <bpmn:incoming>SequenceFlow_180aijn</bpmn:incoming>\n" +
                        "      <bpmn:incoming>SequenceFlow_036gszf</bpmn:incoming>\n" +
                        "    </bpmn:endEvent>\n" +
                        "    <bpmn:sequenceFlow id=\"SequenceFlow_0y2rtuy\" sourceRef=\"Task_1k01bsq\" targetRef=\"EndEvent_1rmaenx\" />\n" +
                        "    <bpmn:sequenceFlow id=\"SequenceFlow_1ow1dwh\" sourceRef=\"Task_0auiq1i\" targetRef=\"Task_0dujfcv\" />\n" +
                        "    <bpmn:serviceTask id=\"Task_0dujfcv\" name=\"后勤部投票\" camunda:class=\"com.betalpha.fosun.delegate.InvestTask\">\n" +
                        "      <bpmn:incoming>SequenceFlow_1ow1dwh</bpmn:incoming>\n" +
                        "      <bpmn:outgoing>SequenceFlow_180aijn</bpmn:outgoing>\n" +
                        "    </bpmn:serviceTask>\n" +
                        "    <bpmn:sequenceFlow id=\"SequenceFlow_180aijn\" sourceRef=\"Task_0dujfcv\" targetRef=\"EndEvent_1rmaenx\" />\n" +
                        "    <bpmn:sequenceFlow id=\"SequenceFlow_036gszf\" sourceRef=\"Task_0s70ujr\" targetRef=\"EndEvent_1rmaenx\" />\n" +
                        "  </bpmn:process>\n" +
                        "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n" +
                        "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"Process_1\">\n" +
                        "      <bpmndi:BPMNShape id=\"_BPMNShape_StartEvent_2\" bpmnElement=\"StartEvent_1\">\n" +
                        "        <dc:Bounds x=\"-133\" y=\"150\" width=\"36\" height=\"36\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"-160\" y=\"186\" width=\"90\" height=\"20\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNShape>\n" +
                        "      <bpmndi:BPMNShape id=\"UserTask_183oyo0_di\" bpmnElement=\"Task_1yq22kw\">\n" +
                        "        <dc:Bounds x=\"-52\" y=\"128\" width=\"100\" height=\"80\" />\n" +
                        "      </bpmndi:BPMNShape>\n" +
                        "      <bpmndi:BPMNEdge id=\"SequenceFlow_1kawblg_di\" bpmnElement=\"SequenceFlow_1kawblg\">\n" +
                        "        <di:waypoint x=\"-97\" y=\"168\" />\n" +
                        "        <di:waypoint x=\"-52\" y=\"168\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"-74.5\" y=\"146.5\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNEdge>\n" +
                        "      <bpmndi:BPMNEdge id=\"SequenceFlow_15fmqdv_di\" bpmnElement=\"SequenceFlow_15fmqdv\">\n" +
                        "        <di:waypoint x=\"48\" y=\"168\" />\n" +
                        "        <di:waypoint x=\"111\" y=\"168\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"79.5\" y=\"146.5\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNEdge>\n" +
                        "      <bpmndi:BPMNShape id=\"ExclusiveGateway_0t889i2_di\" bpmnElement=\"ExclusiveGateway_0p36ilx\" " +
                        "isMarkerVisible=\"true\">\n" +
                        "        <dc:Bounds x=\"111\" y=\"143\" width=\"50\" height=\"50\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"136.497\" y=\"119.5\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNShape>\n" +
                        "      <bpmndi:BPMNShape id=\"EndEvent_1a8c7yq_di\" bpmnElement=\"EndEvent_1a8c7yq\">\n" +
                        "        <dc:Bounds x=\"118\" y=\"276\" width=\"36\" height=\"36\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"136\" y=\"315\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNShape>\n" +
                        "      <bpmndi:BPMNEdge id=\"SequenceFlow_1j9ft3l_di\" bpmnElement=\"SequenceFlow_1j9ft3l\">\n" +
                        "        <di:waypoint x=\"136\" y=\"193\" />\n" +
                        "        <di:waypoint x=\"136\" y=\"276\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"135\" y=\"228\" width=\"33\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNEdge>\n" +
                        "      <bpmndi:BPMNEdge id=\"SequenceFlow_0bbsi0o_di\" bpmnElement=\"SequenceFlow_0bbsi0o\">\n" +
                        "        <di:waypoint x=\"161\" y=\"168\" />\n" +
                        "        <di:waypoint x=\"227\" y=\"168\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"183\" y=\"147\" width=\"22\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNEdge>\n" +
                        "      <bpmndi:BPMNShape id=\"UserTask_00g4gxh_di\" bpmnElement=\"Task_0gs5ln7\">\n" +
                        "        <dc:Bounds x=\"227\" y=\"128\" width=\"100\" height=\"80\" />\n" +
                        "      </bpmndi:BPMNShape>\n" +
                        "      <bpmndi:BPMNShape id=\"ExclusiveGateway_0ccns8l_di\" bpmnElement=\"ExclusiveGateway_0ccns8l\" " +
                        "isMarkerVisible=\"true\">\n" +
                        "        <dc:Bounds x=\"369.247\" y=\"143\" width=\"50\" height=\"50\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"394.247\" y=\"119.5\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNShape>\n" +
                        "      <bpmndi:BPMNEdge id=\"SequenceFlow_1m2950i_di\" bpmnElement=\"SequenceFlow_1m2950i\">\n" +
                        "        <di:waypoint x=\"327\" y=\"168\" />\n" +
                        "        <di:waypoint x=\"369\" y=\"168\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"348\" y=\"146\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNEdge>\n" +
                        "      <bpmndi:BPMNEdge id=\"SequenceFlow_0a76doz_di\" bpmnElement=\"SequenceFlow_0a76doz\">\n" +
                        "        <di:waypoint x=\"394\" y=\"193\" />\n" +
                        "        <di:waypoint x=\"394\" y=\"254\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"409\" y=\"217\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNEdge>\n" +
                        "      <bpmndi:BPMNEdge id=\"SequenceFlow_0brr8vt_di\" bpmnElement=\"SequenceFlow_0brr8vt\">\n" +
                        "        <di:waypoint x=\"419\" y=\"168\" />\n" +
                        "        <di:waypoint x=\"495\" y=\"168\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"457\" y=\"146.5\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNEdge>\n" +
                        "      <bpmndi:BPMNEdge id=\"SequenceFlow_1tply3e_di\" bpmnElement=\"SequenceFlow_1tply3e\">\n" +
                        "        <di:waypoint x=\"394\" y=\"143\" />\n" +
                        "        <di:waypoint x=\"394\" y=\"88\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"409\" y=\"109\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNEdge>\n" +
                        "      <bpmndi:BPMNShape id=\"UserTask_0tgkexh_di\" bpmnElement=\"Task_0auiq1i\">\n" +
                        "        <dc:Bounds x=\"345\" y=\"8\" width=\"100\" height=\"80\" />\n" +
                        "      </bpmndi:BPMNShape>\n" +
                        "      <bpmndi:BPMNShape id=\"UserTask_1cbemjd_di\" bpmnElement=\"Task_1k01bsq\">\n" +
                        "        <dc:Bounds x=\"495\" y=\"128\" width=\"100\" height=\"80\" />\n" +
                        "      </bpmndi:BPMNShape>\n" +
                        "      <bpmndi:BPMNShape id=\"UserTask_1bawnpk_di\" bpmnElement=\"Task_0s70ujr\">\n" +
                        "        <dc:Bounds x=\"345\" y=\"254\" width=\"100\" height=\"80\" />\n" +
                        "      </bpmndi:BPMNShape>\n" +
                        "      <bpmndi:BPMNShape id=\"EndEvent_1rmaenx_di\" bpmnElement=\"EndEvent_1rmaenx\">\n" +
                        "        <dc:Bounds x=\"718.7529999999999\" y=\"150\" width=\"36\" height=\"36\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"764.5059999999999\" y=\"161\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNShape>\n" +
                        "      <bpmndi:BPMNEdge id=\"SequenceFlow_0y2rtuy_di\" bpmnElement=\"SequenceFlow_0y2rtuy\">\n" +
                        "        <di:waypoint x=\"595\" y=\"168\" />\n" +
                        "        <di:waypoint x=\"719\" y=\"168\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"657\" y=\"146.5\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNEdge>\n" +
                        "      <bpmndi:BPMNEdge id=\"SequenceFlow_1ow1dwh_di\" bpmnElement=\"SequenceFlow_1ow1dwh\">\n" +
                        "        <di:waypoint x=\"445\" y=\"48\" />\n" +
                        "        <di:waypoint x=\"495\" y=\"48\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"470\" y=\"26.5\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNEdge>\n" +
                        "      <bpmndi:BPMNShape id=\"ServiceTask_0wqgti1_di\" bpmnElement=\"Task_0dujfcv\">\n" +
                        "        <dc:Bounds x=\"495\" y=\"8\" width=\"100\" height=\"80\" />\n" +
                        "      </bpmndi:BPMNShape>\n" +
                        "      <bpmndi:BPMNEdge id=\"SequenceFlow_180aijn_di\" bpmnElement=\"SequenceFlow_180aijn\">\n" +
                        "        <di:waypoint x=\"595\" y=\"48\" />\n" +
                        "        <di:waypoint x=\"737\" y=\"48\" />\n" +
                        "        <di:waypoint x=\"737\" y=\"150\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"666\" y=\"26.5\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNEdge>\n" +
                        "      <bpmndi:BPMNEdge id=\"SequenceFlow_036gszf_di\" bpmnElement=\"SequenceFlow_036gszf\">\n" +
                        "        <di:waypoint x=\"445\" y=\"294\" />\n" +
                        "        <di:waypoint x=\"737\" y=\"294\" />\n" +
                        "        <di:waypoint x=\"737\" y=\"186\" />\n" +
                        "        <bpmndi:BPMNLabel>\n" +
                        "          <dc:Bounds x=\"591\" y=\"272.5\" width=\"0\" height=\"13\" />\n" +
                        "        </bpmndi:BPMNLabel>\n" +
                        "      </bpmndi:BPMNEdge>\n" +
                        "    </bpmndi:BPMNPlane>\n" +
                        "  </bpmndi:BPMNDiagram>\n" +
                        "</bpmn:definitions>\n").deploy().getId();
    }

    @Test
    public void pass_to_next_test() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("agree", 0);
        variables.put("disagree", 0);


        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_1", variables);
        log.info("processInstance.getId() {}", processInstance.getId());
        TaskQuery taskQuery = taskService.createTaskQuery().processInstanceId(processInstance.getId());
        List<Task> beforeTask = taskQuery.list();
        Assert.assertEquals(3, beforeTask.size());

        variables.put("agree", (Integer) variables.get("agree") + 1);
        taskService.complete(beforeTask.get(0).getId(), variables);

        variables.put("agree", (Integer) variables.get("agree") + 1);
        taskService.complete(beforeTask.get(1).getId(), variables);

        variables.put("disagree", (Integer) variables.get("disagree") + 1);
        taskService.complete(beforeTask.get(2).getId(), variables);

        List<Task> tasks = taskQuery.list();
        Assert.assertEquals(1, tasks.size());

    }

    @Test
    public void pass_to_end_test() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("agree", 0);
        variables.put("disagree", 0);


        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_1", variables);
        log.info("processInstance.getId() {}", processInstance.getId());
        TaskQuery taskQuery = taskService.createTaskQuery().processInstanceId(processInstance.getId());
        List<Task> beforeTask = taskQuery.list();
        Assert.assertEquals(3, beforeTask.size());
        List<Task> userTasks = taskQuery.taskCandidateUser("user1").list();
        Assert.assertEquals(3, userTasks.size());

        variables.put("agree", (Integer) variables.get("agree") + 1);
        taskService.complete(beforeTask.get(0).getId(), variables);

        userTasks = taskQuery.taskCandidateUser("user1").list();
        Assert.assertEquals(2, userTasks.size());
        userTasks.stream().forEach(task -> {
            taskService.deleteCandidateUser(task.getId(), "user1");
        });
        userTasks = taskQuery.taskCandidateUser("user1").list();
        Assert.assertEquals(0, userTasks.size());
        variables.put("agree", (Integer) variables.get("disagree") + 1);
        taskService.complete(beforeTask.get(1).getId(), variables);

        variables.put("disagree", (Integer) variables.get("disagree") + 1);
        taskService.complete(beforeTask.get(2).getId(), variables);

        List<Task> tasks = taskQuery.list();
        Assert.assertEquals(0, tasks.size());

    }

}
