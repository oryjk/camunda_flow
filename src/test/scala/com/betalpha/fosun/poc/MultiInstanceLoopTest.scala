package com.betalpha.fosun.poc

import org.camunda.bpm.engine._
import org.slf4j.{Logger, LoggerFactory}
import org.specs2.mutable.Specification

import scala.collection.JavaConverters._

/**
  * Created by WangRui on 15/05/2018.
  */
class MultiInstanceLoopTest extends Specification {

  var logger: Logger = LoggerFactory.getLogger(this.getClass)
  val engine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault().buildProcessEngine()
  private val repositoryService: RepositoryService = engine.getRepositoryService
  private val runtimeService: RuntimeService = engine.getRuntimeService
  private val taskService: TaskService = engine.getTaskService
  private val source =
    """<?xml version="1.0" encoding="UTF-8"?>
      |<bpmn:definitions xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:di="http://www.omg.org/spec/DD/20100524/DI" xmlns:dc="http://www.omg.org/spec/DD/20100524/DC" xmlns:camunda="http://camunda.org/schema/1.0/bpmn" id="Definitions_1xwnns3" targetNamespace="http://bpmn.io/schema/bpmn" exporter="Camunda Modeler" exporterVersion="1.14.0">
      |  <bpmn:process id="Process_1" isExecutable="true">
      |    <bpmn:extensionElements>
      |      <camunda:executionListener class="com.betalpha.fosun.listener.UserStartExecutionListener" event="start" />
      |    </bpmn:extensionElements>
      |    <bpmn:startEvent id="StartEvent_1">
      |      <bpmn:outgoing>SequenceFlow_1sip09p</bpmn:outgoing>
      |    </bpmn:startEvent>
      |    <bpmn:userTask id="Task_1xem4je" camunda:assignee="wang,zhou">
      |      <bpmn:extensionElements>
      |        <camunda:executionListener class="com.betalpha.fosun.listener.UserStartExecutionListener" event="start" />
      |        <camunda:executionListener class="com.betalpha.fosun.listener.UserEndExecutionListener" event="end" />
      |        <camunda:taskListener class="com.betalpha.fosun.listener.UserCreateTaskListener" event="create">
      |          <camunda:field name="votable">
      |            <camunda:string>0</camunda:string>
      |          </camunda:field>
      |          <camunda:field name="department">
      |            <camunda:string>department</camunda:string>
      |          </camunda:field>
      |        </camunda:taskListener>
      |        <camunda:taskListener class="com.betalpha.fosun.listener.UserAssignTaskListener" event="assignment" />
      |        <camunda:taskListener class="com.betalpha.fosun.listener.UserCompleteTaskListener" event="complete" />
      |        <camunda:taskListener class="com.betalpha.fosun.listener.UserDeleteTaskListener" event="delete" />
      |      </bpmn:extensionElements>
      |      <bpmn:incoming>SequenceFlow_1sip09p</bpmn:incoming>
      |      <bpmn:outgoing>SequenceFlow_1ky3oa5</bpmn:outgoing>
      |      <bpmn:multiInstanceLoopCharacteristics camunda:collection="assigneeList" />
      |    </bpmn:userTask>
      |    <bpmn:sequenceFlow id="SequenceFlow_1sip09p" sourceRef="StartEvent_1" targetRef="Task_1xem4je" />
      |    <bpmn:endEvent id="EndEvent_09y6khv">
      |      <bpmn:incoming>SequenceFlow_1812y4d</bpmn:incoming>
      |    </bpmn:endEvent>
      |    <bpmn:userTask id="Task_03gf2j6">
      |      <bpmn:extensionElements>
      |        <camunda:taskListener class="com.betalpha.fosun.listener.UserCreateTaskListener" event="create">
      |          <camunda:field name="department">
      |            <camunda:string>company</camunda:string>
      |          </camunda:field>
      |        </camunda:taskListener>
      |      </bpmn:extensionElements>
      |      <bpmn:incoming>SequenceFlow_1ky3oa5</bpmn:incoming>
      |      <bpmn:outgoing>SequenceFlow_1812y4d</bpmn:outgoing>
      |    </bpmn:userTask>
      |    <bpmn:sequenceFlow id="SequenceFlow_1812y4d" sourceRef="Task_03gf2j6" targetRef="EndEvent_09y6khv" />
      |    <bpmn:sequenceFlow id="SequenceFlow_1ky3oa5" sourceRef="Task_1xem4je" targetRef="Task_03gf2j6" />
      |  </bpmn:process>
      |  <bpmndi:BPMNDiagram id="BPMNDiagram_1">
      |    <bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="Process_1">
      |      <bpmndi:BPMNShape id="_BPMNShape_StartEvent_2" bpmnElement="StartEvent_1">
      |        <dc:Bounds x="171" y="167" width="36" height="36" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="144" y="203" width="90" height="20" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNShape>
      |      <bpmndi:BPMNShape id="UserTask_1xgf6a0_di" bpmnElement="Task_1xem4je">
      |        <dc:Bounds x="287" y="145" width="100" height="80" />
      |      </bpmndi:BPMNShape>
      |      <bpmndi:BPMNEdge id="SequenceFlow_1sip09p_di" bpmnElement="SequenceFlow_1sip09p">
      |        <di:waypoint x="207" y="185" />
      |        <di:waypoint x="287" y="185" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="247" y="163.5" width="0" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNEdge>
      |      <bpmndi:BPMNShape id="EndEvent_09y6khv_di" bpmnElement="EndEvent_09y6khv">
      |        <dc:Bounds x="658" y="167" width="36" height="36" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="676" y="206" width="0" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNShape>
      |      <bpmndi:BPMNShape id="UserTask_0pg3l7k_di" bpmnElement="Task_03gf2j6">
      |        <dc:Bounds x="463" y="145" width="100" height="80" />
      |      </bpmndi:BPMNShape>
      |      <bpmndi:BPMNEdge id="SequenceFlow_1812y4d_di" bpmnElement="SequenceFlow_1812y4d">
      |        <di:waypoint x="563" y="185" />
      |        <di:waypoint x="658" y="185" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="610.5" y="163" width="0" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNEdge>
      |      <bpmndi:BPMNEdge id="SequenceFlow_1ky3oa5_di" bpmnElement="SequenceFlow_1ky3oa5">
      |        <di:waypoint x="387" y="185" />
      |        <di:waypoint x="463" y="185" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="425" y="163" width="0" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNEdge>
      |    </bpmndi:BPMNPlane>
      |  </bpmndi:BPMNDiagram>
      |</bpmn:definitions>
      |""".stripMargin


  "ListenerTest" should {
    logger.info("start deploy deployment")
    val deployment = repositoryService.createDeployment().addString("test.bpmn20.xml", source).deploy()

    logger.info("start instance")
    val instance = runtimeService.startProcessInstanceByKey("Process_1")

    "task process" in {
      instance should not be null
      deployment should not be null
      val task = taskService.createTaskQuery().taskCandidateUser("department").singleResult()
      task shouldEqual (null)
      var tasks = taskService.createTaskQuery().list()
      tasks.size() shouldEqual (4)
      tasks.asScala
        .foreach(task => {
          val assignee = task.getAssignee
          logger.info("assignee {}", assignee)
        })
      tasks = taskService.createTaskQuery().list()
      tasks shouldEqual(null)
    }

  }


}
