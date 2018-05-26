package com.betalpha.fosun.service

import java.util.UUID

import com.betalpha.fosun.api.process.{ProcessSource, StartParameter}
import com.betalpha.fosun.user.DepartmentService
import org.camunda.bpm.engine._
import org.mockito.Mockito.{mock, when}
import org.slf4j.{Logger, LoggerFactory}
import org.specs2.mutable.Specification
import org.springframework.util.CollectionUtils

/**
  * Created by WangRui on 14/05/2018.
  */


class ProcessInitiatorTest extends Specification {
  var logger: Logger = LoggerFactory.getLogger(this.getClass)
  val engine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault().buildProcessEngine()
  private val repositoryService: RepositoryService = engine.getRepositoryService
  private val runtimeService: RuntimeService = engine.getRuntimeService
  private val taskService: TaskService = engine.getTaskService
  private val identityService: IdentityService = engine.getIdentityService

  val departmentService = mock(classOf[DepartmentService])
  val ratingService = mock(classOf[RatingService])

  private val processService: ProcessService = new ProcessService(repositoryService, runtimeService, taskService, identityService, departmentService, ratingService)

  private val source =
    """<?xml version="1.0" encoding="UTF-8"?>
      |<bpmn:definitions xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:di="http://www.omg.org/spec/DD/20100524/DI" xmlns:dc="http://www.omg.org/spec/DD/20100524/DC" xmlns:camunda="http://camunda.org/schema/1.0/bpmn" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" id="Definitions_1x8z08g" targetNamespace="http://bpmn.io/schema/bpmn" exporter="Camunda Modeler" exporterVersion="1.14.0">
      |  <bpmn:process id="Process_1" name="复星决策流程" isExecutable="true">
      |    <bpmn:startEvent id="StartEvent_1" >
      |      <bpmn:outgoing>SequenceFlow_1kawblg</bpmn:outgoing>
      |    </bpmn:startEvent>
      |    <bpmn:userTask id="Task_1yq22kw" name="投决会投票" camunda:candidateUsers="user1,user2,user3">
      |      <bpmn:extensionElements>
      |        <camunda:taskListener class="com.betalpha.fosun.listener.InvestListener" event="complete" />
      |        <camunda:formData>
      |          <camunda:formField id="FormField_34i7as8" type="string">
      |            <camunda:properties>
      |              <camunda:property id="Property_1ijn2r4" />
      |            </camunda:properties>
      |          </camunda:formField>
      |        </camunda:formData>
      |        <camunda:inputOutput>
      |          <camunda:inputParameter name="rule">通过</camunda:inputParameter>
      |        </camunda:inputOutput>
      |      </bpmn:extensionElements>
      |      <bpmn:incoming>SequenceFlow_1kawblg</bpmn:incoming>
      |      <bpmn:outgoing>SequenceFlow_15fmqdv</bpmn:outgoing>
      |      <bpmn:multiInstanceLoopCharacteristics>
      |        <bpmn:loopCardinality xsi:type="bpmn:tFormalExpression">3</bpmn:loopCardinality>
      |      </bpmn:multiInstanceLoopCharacteristics>
      |    </bpmn:userTask>
      |    <bpmn:sequenceFlow id="SequenceFlow_1kawblg" sourceRef="StartEvent_1" targetRef="Task_1yq22kw" />
      |    <bpmn:sequenceFlow id="SequenceFlow_15fmqdv" sourceRef="Task_1yq22kw" targetRef="ExclusiveGateway_0p36ilx" />
      |    <bpmn:exclusiveGateway id="ExclusiveGateway_0p36ilx">
      |      <bpmn:incoming>SequenceFlow_15fmqdv</bpmn:incoming>
      |      <bpmn:outgoing>SequenceFlow_1j9ft3l</bpmn:outgoing>
      |      <bpmn:outgoing>SequenceFlow_0bbsi0o</bpmn:outgoing>
      |    </bpmn:exclusiveGateway>
      |    <bpmn:endEvent id="EndEvent_1a8c7yq">
      |      <bpmn:incoming>SequenceFlow_1j9ft3l</bpmn:incoming>
      |    </bpmn:endEvent>
      |    <bpmn:sequenceFlow id="SequenceFlow_1j9ft3l" name="否定了" sourceRef="ExclusiveGateway_0p36ilx" targetRef="EndEvent_1a8c7yq">
      |      <bpmn:conditionExpression xsi:type="bpmn:tFormalExpression">${x==2}</bpmn:conditionExpression>
      |    </bpmn:sequenceFlow>
      |    <bpmn:sequenceFlow id="SequenceFlow_0bbsi0o" name="通过" sourceRef="ExclusiveGateway_0p36ilx" targetRef="Task_0gs5ln7">
      |      <bpmn:conditionExpression xsi:type="bpmn:tFormalExpression">${x==1}</bpmn:conditionExpression>
      |    </bpmn:sequenceFlow>
      |    <bpmn:userTask id="Task_0gs5ln7" name="董事会投票">
      |      <bpmn:extensionElements>
      |        <camunda:executionListener class="com.betalpha.fosun.delegate.InvestTask" event="start" />
      |      </bpmn:extensionElements>
      |      <bpmn:incoming>SequenceFlow_0bbsi0o</bpmn:incoming>
      |      <bpmn:outgoing>SequenceFlow_1m2950i</bpmn:outgoing>
      |    </bpmn:userTask>
      |    <bpmn:exclusiveGateway id="ExclusiveGateway_0ccns8l">
      |      <bpmn:incoming>SequenceFlow_1m2950i</bpmn:incoming>
      |      <bpmn:outgoing>SequenceFlow_0a76doz</bpmn:outgoing>
      |      <bpmn:outgoing>SequenceFlow_0brr8vt</bpmn:outgoing>
      |      <bpmn:outgoing>SequenceFlow_1tply3e</bpmn:outgoing>
      |    </bpmn:exclusiveGateway>
      |    <bpmn:sequenceFlow id="SequenceFlow_1m2950i" sourceRef="Task_0gs5ln7" targetRef="ExclusiveGateway_0ccns8l" />
      |    <bpmn:sequenceFlow id="SequenceFlow_0a76doz" sourceRef="ExclusiveGateway_0ccns8l" targetRef="Task_0s70ujr">
      |      <bpmn:conditionExpression xsi:type="bpmn:tFormalExpression">${y==2}</bpmn:conditionExpression>
      |    </bpmn:sequenceFlow>
      |    <bpmn:sequenceFlow id="SequenceFlow_0brr8vt" sourceRef="ExclusiveGateway_0ccns8l" targetRef="Task_1k01bsq">
      |      <bpmn:conditionExpression xsi:type="bpmn:tFormalExpression">${y==3}</bpmn:conditionExpression>
      |    </bpmn:sequenceFlow>
      |    <bpmn:sequenceFlow id="SequenceFlow_1tply3e" sourceRef="ExclusiveGateway_0ccns8l" targetRef="Task_0auiq1i">
      |      <bpmn:conditionExpression xsi:type="bpmn:tFormalExpression">${y==1}</bpmn:conditionExpression>
      |    </bpmn:sequenceFlow>
      |    <bpmn:userTask id="Task_0auiq1i" name="经理投票">
      |      <bpmn:incoming>SequenceFlow_1tply3e</bpmn:incoming>
      |      <bpmn:outgoing>SequenceFlow_1ow1dwh</bpmn:outgoing>
      |    </bpmn:userTask>
      |    <bpmn:userTask id="Task_0s70ujr" name="副经理投票">
      |      <bpmn:incoming>SequenceFlow_0a76doz</bpmn:incoming>
      |      <bpmn:outgoing>SequenceFlow_036gszf</bpmn:outgoing>
      |    </bpmn:userTask>
      |    <bpmn:endEvent id="EndEvent_1rmaenx">
      |      <bpmn:extensionElements>
      |        <camunda:executionListener class="" event="start" />
      |      </bpmn:extensionElements>
      |      <bpmn:incoming>SequenceFlow_0y2rtuy</bpmn:incoming>
      |      <bpmn:incoming>SequenceFlow_180aijn</bpmn:incoming>
      |      <bpmn:incoming>SequenceFlow_036gszf</bpmn:incoming>
      |    </bpmn:endEvent>
      |    <bpmn:sequenceFlow id="SequenceFlow_0y2rtuy" sourceRef="Task_1k01bsq" targetRef="EndEvent_1rmaenx" />
      |    <bpmn:sequenceFlow id="SequenceFlow_1ow1dwh" sourceRef="Task_0auiq1i" targetRef="Task_0dujfcv" />
      |    <bpmn:serviceTask id="Task_0dujfcv" name="后勤部投票" camunda:class="com.betalpha.fosun.delegate.InvestTask">
      |      <bpmn:incoming>SequenceFlow_1ow1dwh</bpmn:incoming>
      |      <bpmn:outgoing>SequenceFlow_180aijn</bpmn:outgoing>
      |    </bpmn:serviceTask>
      |    <bpmn:sequenceFlow id="SequenceFlow_180aijn" sourceRef="Task_0dujfcv" targetRef="EndEvent_1rmaenx" />
      |    <bpmn:sequenceFlow id="SequenceFlow_036gszf" sourceRef="Task_0s70ujr" targetRef="EndEvent_1rmaenx" />
      |    <bpmn:userTask id="Task_1k01bsq" name="董事长投票">
      |      <bpmn:incoming>SequenceFlow_0brr8vt</bpmn:incoming>
      |      <bpmn:outgoing>SequenceFlow_0y2rtuy</bpmn:outgoing>
      |    </bpmn:userTask>
      |  </bpmn:process>
      |  <bpmndi:BPMNDiagram id="BPMNDiagram_1">
      |    <bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="Process_1">
      |      <bpmndi:BPMNShape id="_BPMNShape_StartEvent_2" bpmnElement="StartEvent_1">
      |        <dc:Bounds x="-133" y="150" width="36" height="36" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="-160" y="186" width="90" height="20" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNShape>
      |      <bpmndi:BPMNShape id="UserTask_183oyo0_di" bpmnElement="Task_1yq22kw">
      |        <dc:Bounds x="-52" y="128" width="100" height="80" />
      |      </bpmndi:BPMNShape>
      |      <bpmndi:BPMNEdge id="SequenceFlow_1kawblg_di" bpmnElement="SequenceFlow_1kawblg">
      |        <di:waypoint x="-97" y="168" />
      |        <di:waypoint x="-52" y="168" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="-74.5" y="146.5" width="0" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNEdge>
      |      <bpmndi:BPMNEdge id="SequenceFlow_15fmqdv_di" bpmnElement="SequenceFlow_15fmqdv">
      |        <di:waypoint x="48" y="168" />
      |        <di:waypoint x="111" y="168" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="79.5" y="146.5" width="0" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNEdge>
      |      <bpmndi:BPMNShape id="ExclusiveGateway_0t889i2_di" bpmnElement="ExclusiveGateway_0p36ilx" isMarkerVisible="true">
      |        <dc:Bounds x="111" y="143" width="50" height="50" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="136.497" y="119.5" width="0" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNShape>
      |      <bpmndi:BPMNShape id="EndEvent_1a8c7yq_di" bpmnElement="EndEvent_1a8c7yq">
      |        <dc:Bounds x="118" y="276" width="36" height="36" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="136" y="315" width="0" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNShape>
      |      <bpmndi:BPMNEdge id="SequenceFlow_1j9ft3l_di" bpmnElement="SequenceFlow_1j9ft3l">
      |        <di:waypoint x="136" y="193" />
      |        <di:waypoint x="136" y="276" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="135" y="228" width="33" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNEdge>
      |      <bpmndi:BPMNEdge id="SequenceFlow_0bbsi0o_di" bpmnElement="SequenceFlow_0bbsi0o">
      |        <di:waypoint x="161" y="168" />
      |        <di:waypoint x="227" y="168" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="183" y="147" width="22" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNEdge>
      |      <bpmndi:BPMNShape id="UserTask_00g4gxh_di" bpmnElement="Task_0gs5ln7">
      |        <dc:Bounds x="227" y="128" width="100" height="80" />
      |      </bpmndi:BPMNShape>
      |      <bpmndi:BPMNShape id="ExclusiveGateway_0ccns8l_di" bpmnElement="ExclusiveGateway_0ccns8l" isMarkerVisible="true">
      |        <dc:Bounds x="369.247" y="143" width="50" height="50" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="394.247" y="119.5" width="0" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNShape>
      |      <bpmndi:BPMNEdge id="SequenceFlow_1m2950i_di" bpmnElement="SequenceFlow_1m2950i">
      |        <di:waypoint x="327" y="168" />
      |        <di:waypoint x="369" y="168" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="348" y="146" width="0" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNEdge>
      |      <bpmndi:BPMNEdge id="SequenceFlow_0a76doz_di" bpmnElement="SequenceFlow_0a76doz">
      |        <di:waypoint x="394" y="193" />
      |        <di:waypoint x="394" y="254" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="409" y="217" width="0" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNEdge>
      |      <bpmndi:BPMNEdge id="SequenceFlow_0brr8vt_di" bpmnElement="SequenceFlow_0brr8vt">
      |        <di:waypoint x="419" y="168" />
      |        <di:waypoint x="495" y="168" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="457" y="146.5" width="0" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNEdge>
      |      <bpmndi:BPMNEdge id="SequenceFlow_1tply3e_di" bpmnElement="SequenceFlow_1tply3e">
      |        <di:waypoint x="394" y="143" />
      |        <di:waypoint x="394" y="88" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="409" y="109" width="0" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNEdge>
      |      <bpmndi:BPMNShape id="UserTask_0tgkexh_di" bpmnElement="Task_0auiq1i">
      |        <dc:Bounds x="345" y="8" width="100" height="80" />
      |      </bpmndi:BPMNShape>
      |      <bpmndi:BPMNShape id="UserTask_1bawnpk_di" bpmnElement="Task_0s70ujr">
      |        <dc:Bounds x="345" y="254" width="100" height="80" />
      |      </bpmndi:BPMNShape>
      |      <bpmndi:BPMNShape id="EndEvent_1rmaenx_di" bpmnElement="EndEvent_1rmaenx">
      |        <dc:Bounds x="718.7529999999999" y="150" width="36" height="36" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="764.5059999999999" y="161" width="0" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNShape>
      |      <bpmndi:BPMNEdge id="SequenceFlow_0y2rtuy_di" bpmnElement="SequenceFlow_0y2rtuy">
      |        <di:waypoint x="595" y="168" />
      |        <di:waypoint x="719" y="168" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="657" y="146.5" width="0" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNEdge>
      |      <bpmndi:BPMNEdge id="SequenceFlow_1ow1dwh_di" bpmnElement="SequenceFlow_1ow1dwh">
      |        <di:waypoint x="445" y="48" />
      |        <di:waypoint x="495" y="48" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="470" y="26.5" width="0" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNEdge>
      |      <bpmndi:BPMNShape id="ServiceTask_0wqgti1_di" bpmnElement="Task_0dujfcv">
      |        <dc:Bounds x="495" y="8" width="100" height="80" />
      |      </bpmndi:BPMNShape>
      |      <bpmndi:BPMNEdge id="SequenceFlow_180aijn_di" bpmnElement="SequenceFlow_180aijn">
      |        <di:waypoint x="595" y="48" />
      |        <di:waypoint x="737" y="48" />
      |        <di:waypoint x="737" y="150" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="666" y="26.5" width="0" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNEdge>
      |      <bpmndi:BPMNEdge id="SequenceFlow_036gszf_di" bpmnElement="SequenceFlow_036gszf">
      |        <di:waypoint x="445" y="294" />
      |        <di:waypoint x="737" y="294" />
      |        <di:waypoint x="737" y="186" />
      |        <bpmndi:BPMNLabel>
      |          <dc:Bounds x="591" y="272.5" width="0" height="13" />
      |        </bpmndi:BPMNLabel>
      |      </bpmndi:BPMNEdge>
      |      <bpmndi:BPMNShape id="UserTask_042xhfz_di" bpmnElement="Task_1k01bsq">
      |        <dc:Bounds x="495" y="128" width="100" height="80" />
      |      </bpmndi:BPMNShape>
      |    </bpmndi:BPMNPlane>
      |  </bpmndi:BPMNDiagram>
      |</bpmn:definitions>
      |""".stripMargin


  "ProcessService" should {
    val deployment = processService.createProcess(new ProcessSource("Process_1", "复星流程demo", source))
    val definitions = processService.queryProcessDefinition()
    CollectionUtils.isEmpty(definitions) must be equalTo false

    "create process ok" in {
      logger.info("deployment {}", deployment)
      deployment should not be null
      deployment.getId shouldNotEqual null
      deployment.getName shouldNotEqual null

    }
    "start process ok" in {
      when(departmentService.getUserDepartmentByUserName("uses1")).thenReturn("department")
      when(ratingService.getSubmitter("1")).thenReturn("A")
      when(ratingService.getRating("1")).thenReturn("A")
      val instance = processService.startProcess(new StartParameter(definitions.get(0).getKey(), UUID.randomUUID().toString, "user1", "1"))
      instance should not be null
      instance.getId shouldNotEqual empty
    }

  }


}
