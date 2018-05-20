package com.betalpha.fosun;


import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BackendFosunApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class CamundaIT {


    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;


    @Test
    public void startUpTest() {
        log.info("runtimeService {}", runtimeService.toString());
        log.info("taskService {}", taskService.toString());
        log.info("repositoryService {}", repositoryService.toString());
    }

    @Test
    public void deploy_test() {
        Deployment deploy = repositoryService.createDeployment().addString("deployment.bpmn", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<bpmn2:definitions xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmn2=\"http://www.omg" +
                ".org/spec/BPMN/20100524/MODEL\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg" +
                ".org/spec/DD/20100524/DC\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:camunda=\"http://camunda" +
                ".org/schema/1.0/bpmn\" id=\"sample-diagram\" targetNamespace=\"http://bpmn.io/schema/bpmn\" xsi:schemaLocation=\"http://www.omg" +
                ".org/spec/BPMN/20100524/MODEL BPMN20.xsd\">\n" +
                "  <bpmn2:process id=\"Process_1\" isExecutable=\"true\">\n" +
                "    <bpmn2:startEvent id=\"StartEvent_1\">\n" +
                "      <bpmn2:outgoing>SequenceFlow_09rmodf</bpmn2:outgoing>\n" +
                "    </bpmn2:startEvent>\n" +
                "    <bpmn2:userTask id=\"Task_1t0m418\" camunda:formKey=\"form1\" camunda:candidateUsers=\"张三,李四\">\n" +
                "      <bpmn2:extensionElements>\n" +
                "        <camunda:formData>\n" +
                "          <camunda:formField id=\"agree\" label=\"是否同意\" type=\"boolean\" defaultValue=\"true\" />\n" +
                "        </camunda:formData>\n" +
                "      </bpmn2:extensionElements>\n" +
                "      <bpmn2:incoming>SequenceFlow_09rmodf</bpmn2:incoming>\n" +
                "      <bpmn2:outgoing>SequenceFlow_1h15269</bpmn2:outgoing>\n" +
                "    </bpmn2:userTask>\n" +
                "    <bpmn2:sequenceFlow id=\"SequenceFlow_09rmodf\" sourceRef=\"StartEvent_1\" targetRef=\"Task_1t0m418\" />\n" +
                "    <bpmn2:sequenceFlow id=\"SequenceFlow_1h15269\" sourceRef=\"Task_1t0m418\" targetRef=\"Task_1j1uwcj\" />\n" +
                "    <bpmn2:userTask id=\"Task_1j1uwcj\" camunda:formKey=\"form2\" camunda:candidateUsers=\"王五,麻子\">\n" +
                "      <bpmn2:extensionElements>\n" +
                "        <camunda:formData>\n" +
                "          <camunda:formField id=\"agree\" label=\"是否同意\" type=\"boolean\" defaultValue=\"true\" />\n" +
                "        </camunda:formData>\n" +
                "      </bpmn2:extensionElements>\n" +
                "      <bpmn2:incoming>SequenceFlow_1h15269</bpmn2:incoming>\n" +
                "      <bpmn2:outgoing>SequenceFlow_1kvumkm</bpmn2:outgoing>\n" +
                "    </bpmn2:userTask>\n" +
                "    <bpmn2:endEvent id=\"EndEvent_1yv88bo\">\n" +
                "      <bpmn2:incoming>SequenceFlow_1kvumkm</bpmn2:incoming>\n" +
                "    </bpmn2:endEvent>\n" +
                "    <bpmn2:sequenceFlow id=\"SequenceFlow_1kvumkm\" sourceRef=\"Task_1j1uwcj\" targetRef=\"EndEvent_1yv88bo\" />\n" +
                "  </bpmn2:process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"Process_1\">\n" +
                "      <bpmndi:BPMNShape id=\"_BPMNShape_StartEvent_2\" bpmnElement=\"StartEvent_1\">\n" +
                "        <dc:Bounds x=\"412\" y=\"240\" width=\"36\" height=\"36\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_1i2mcbu_di\" bpmnElement=\"Task_1t0m418\">\n" +
                "        <dc:Bounds x=\"514\" y=\"218\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_09rmodf_di\" bpmnElement=\"SequenceFlow_09rmodf\">\n" +
                "        <di:waypoint x=\"448\" y=\"258\" />\n" +
                "        <di:waypoint x=\"514\" y=\"258\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"481\" y=\"236\" width=\"0\" height=\"13\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1h15269_di\" bpmnElement=\"SequenceFlow_1h15269\">\n" +
                "        <di:waypoint x=\"614\" y=\"258\" />\n" +
                "        <di:waypoint x=\"691\" y=\"258\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"652.5\" y=\"236.5\" width=\"0\" height=\"13\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_0cuw1i4_di\" bpmnElement=\"Task_1j1uwcj\">\n" +
                "        <dc:Bounds x=\"691\" y=\"218\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"EndEvent_1yv88bo_di\" bpmnElement=\"EndEvent_1yv88bo\">\n" +
                "        <dc:Bounds x=\"878\" y=\"240\" width=\"36\" height=\"36\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"896\" y=\"279\" width=\"0\" height=\"13\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1kvumkm_di\" bpmnElement=\"SequenceFlow_1kvumkm\">\n" +
                "        <di:waypoint x=\"791\" y=\"258\" />\n" +
                "        <di:waypoint x=\"878\" y=\"258\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"834.5\" y=\"236\" width=\"0\" height=\"13\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</bpmn2:definitions>").name("test_name").deployWithResult();
        Assert.assertNotNull(deploy);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        runtimeService.startProcessInstanceById(processDefinition.getId());
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
        Assert.assertEquals(1, list.size());
    }
}
