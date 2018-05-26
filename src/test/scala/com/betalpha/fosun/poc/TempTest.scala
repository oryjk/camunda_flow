package com.betalpha.fosun.poc

import java.util.UUID

import org.camunda.bpm.engine._
import org.camunda.bpm.engine.impl.persistence.entity.ResourceEntity
import org.slf4j.{Logger, LoggerFactory}
import org.specs2.mutable.Specification

import scala.collection.JavaConverters._

/**
  * Created by WangRui on 15/05/2018.
  */
class TempTest extends Specification {

  var logger: Logger = LoggerFactory.getLogger(this.getClass)
  val engine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault().buildProcessEngine()
  private val repositoryService: RepositoryService = engine.getRepositoryService
  private val runtimeService: RuntimeService = engine.getRuntimeService
  private val taskService: TaskService = engine.getTaskService
  private val source =
    """<?xml version="1.0" encoding="UTF-8"?>
<bpmn2:definitions xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:bpmn2="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:dc="http://www.omg.org/spec/DD/20100524/DC" xmlns:di="http://www.omg.org/spec/DD/20100524/DI" xmlns:camunda="http://camunda.org/schema/1.0/bpmn" id="sample-diagram" targetNamespace="http://bpmn.io/schema/bpmn" xsi:schemaLocation="http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd">
  <bpmn2:process id="Process_1" isExecutable="true">
    <bpmn2:startEvent id="StartEvent_1gmkzah" name="开始">
      <bpmn2:extensionElements>
        <camunda:executionListener class="com.betalpha.fosun.listener.StartProcessEndExecutionListener" event="end" />
      </bpmn2:extensionElements>
      <bpmn2:outgoing>SequenceFlow_0cx9pmi</bpmn2:outgoing>
    </bpmn2:startEvent>
    <bpmn2:userTask id="UserTask_15s5k13" name="内审">
      <bpmn2:extensionElements>
        <camunda:taskListener class="com.betalpha.fosun.listener.UserCreateTaskListener" event="create">
          <camunda:field name="votable">
            <camunda:string>1</camunda:string>
          </camunda:field>
          <camunda:field name="processor">
            <camunda:string>提交人直属内审会</camunda:string>
          </camunda:field>
          <camunda:field name="department">
            <camunda:string>部门</camunda:string>
          </camunda:field>
          <camunda:field name="oneVote">
            <camunda:string>1</camunda:string>
          </camunda:field>
          <camunda:field name="continueResearch">
            <camunda:string>1</camunda:string>
          </camunda:field>
          <camunda:field name="passThreshold">
            <camunda:string>0.5</camunda:string>
          </camunda:field>
          <camunda:field name="noneProcessorSkip">
            <camunda:string>1</camunda:string>
          </camunda:field>
        </camunda:taskListener>
        <camunda:taskListener class="com.betalpha.fosun.listener.UserAssignTaskListener" event="assignment" />
        <camunda:taskListener class="com.betalpha.fosun.listener.UserCompleteTaskListener" event="complete" />
        <camunda:taskListener class="com.betalpha.fosun.listener.UserDeleteTaskListener" event="delete" />
        <camunda:executionListener class="com.betalpha.fosun.listener.UserStartExecutionListener" event="start" />
        <camunda:executionListener class="com.betalpha.fosun.listener.UserEndExecutionListener" event="end" />
      </bpmn2:extensionElements>
      <bpmn2:incoming>SequenceFlow_0cx9pmi</bpmn2:incoming>
      <bpmn2:outgoing>SequenceFlow_1twhlsk</bpmn2:outgoing>
      <bpmn2:outgoing>SequenceFlow_00upkrk</bpmn2:outgoing>
    </bpmn2:userTask>
    <bpmn2:sequenceFlow id="SequenceFlow_0cx9pmi" sourceRef="StartEvent_1gmkzah" targetRef="UserTask_15s5k13" />
    <bpmn2:endEvent id="EndEvent_0nuo9l1" name="结束">
      <bpmn2:extensionElements>
        <camunda:executionListener class="com.betalpha.fosun.listener.EndProcessEndExecutionListener" event="end">
          <camunda:field name="stored">
            <camunda:string>0</camunda:string>
          </camunda:field>
        </camunda:executionListener>
      </bpmn2:extensionElements>
      <bpmn2:incoming>SequenceFlow_1twhlsk</bpmn2:incoming>
    </bpmn2:endEvent>
    <bpmn2:sequenceFlow id="SequenceFlow_1twhlsk" name="不通过" sourceRef="UserTask_15s5k13" targetRef="EndEvent_0nuo9l1">
      <bpmn2:conditionExpression xsi:type="bpmn2:tFormalExpression">${result==0}</bpmn2:conditionExpression>
    </bpmn2:sequenceFlow>
    <bpmn2:userTask id="UserTask_1u68czq" name="预审">
      <bpmn2:extensionElements>
        <camunda:taskListener class="com.betalpha.fosun.listener.UserCreateTaskListener" event="create">
          <camunda:field name="votable">
            <camunda:string>1</camunda:string>
          </camunda:field>
          <camunda:field name="processor">
            <camunda:string>提交人直属债券委员会</camunda:string>
          </camunda:field>
          <camunda:field name="department">
            <camunda:string>公司</camunda:string>
          </camunda:field>
          <camunda:field name="oneVote">
            <camunda:string>0</camunda:string>
          </camunda:field>
          <camunda:field name="continueResearch">
            <camunda:string>0</camunda:string>
          </camunda:field>
          <camunda:field name="passThreshold">
            <camunda:string>0.5</camunda:string>
          </camunda:field>
          <camunda:field name="noneProcessorSkip">
            <camunda:string>1</camunda:string>
          </camunda:field>
        </camunda:taskListener>
        <camunda:taskListener class="com.betalpha.fosun.listener.UserAssignTaskListener" event="assignment" />
        <camunda:taskListener class="com.betalpha.fosun.listener.UserCompleteTaskListener" event="complete" />
        <camunda:taskListener class="com.betalpha.fosun.listener.UserDeleteTaskListener" event="delete" />
        <camunda:executionListener class="com.betalpha.fosun.listener.UserStartExecutionListener" event="start" />
        <camunda:executionListener class="com.betalpha.fosun.listener.UserEndExecutionListener" event="end" />
      </bpmn2:extensionElements>
      <bpmn2:incoming>SequenceFlow_00upkrk</bpmn2:incoming>
      <bpmn2:outgoing>SequenceFlow_0l1hkk3</bpmn2:outgoing>
      <bpmn2:outgoing>SequenceFlow_03u7iwk</bpmn2:outgoing>
      <bpmn2:outgoing>SequenceFlow_1pv8p0c</bpmn2:outgoing>
    </bpmn2:userTask>
    <bpmn2:sequenceFlow id="SequenceFlow_00upkrk" name="通过" sourceRef="UserTask_15s5k13" targetRef="UserTask_1u68czq">
      <bpmn2:conditionExpression xsi:type="bpmn2:tFormalExpression">${result==1}</bpmn2:conditionExpression>
    </bpmn2:sequenceFlow>
    <bpmn2:endEvent id="EndEvent_1ymw1dc" name="结束">
      <bpmn2:extensionElements>
        <camunda:executionListener class="com.betalpha.fosun.listener.EndProcessEndExecutionListener" event="end">
          <camunda:field name="stored">
            <camunda:string>0</camunda:string>
          </camunda:field>
        </camunda:executionListener>
      </bpmn2:extensionElements>
      <bpmn2:incoming>SequenceFlow_0l1hkk3</bpmn2:incoming>
    </bpmn2:endEvent>
    <bpmn2:sequenceFlow id="SequenceFlow_0l1hkk3" name="不通过" sourceRef="UserTask_1u68czq" targetRef="EndEvent_1ymw1dc">
      <bpmn2:conditionExpression xsi:type="bpmn2:tFormalExpression">${result==0}</bpmn2:conditionExpression>
    </bpmn2:sequenceFlow>
    <bpmn2:endEvent id="EndEvent_03w6rbv" name="入池">
      <bpmn2:extensionElements>
        <camunda:executionListener class="com.betalpha.fosun.listener.EndProcessEndExecutionListener" event="end">
          <camunda:field name="stored">
            <camunda:string>1</camunda:string>
          </camunda:field>
        </camunda:executionListener>
      </bpmn2:extensionElements>
      <bpmn2:incoming>SequenceFlow_03u7iwk</bpmn2:incoming>
      <bpmn2:incoming>SequenceFlow_1sn72gn</bpmn2:incoming>
    </bpmn2:endEvent>
    <bpmn2:sequenceFlow id="SequenceFlow_03u7iwk" sourceRef="UserTask_1u68czq" targetRef="EndEvent_03w6rbv" />
    <bpmn2:userTask id="UserTask_0dm0x46" name="投决">
      <bpmn2:extensionElements>
        <camunda:taskListener class="com.betalpha.fosun.listener.UserCreateTaskListener" event="create">
          <camunda:field name="votable">
            <camunda:string>1</camunda:string>
          </camunda:field>
          <camunda:field name="processor">
            <camunda:string>提交人直属投决会</camunda:string>
          </camunda:field>
          <camunda:field name="department">
            <camunda:string>部门</camunda:string>
          </camunda:field>
          <camunda:field name="oneVote">
            <camunda:string>1</camunda:string>
          </camunda:field>
          <camunda:field name="continueResearch">
            <camunda:string>1</camunda:string>
          </camunda:field>
          <camunda:field name="passThreshold">
            <camunda:string>0.5</camunda:string>
          </camunda:field>
          <camunda:field name="noneProcessorSkip">
            <camunda:string>1</camunda:string>
          </camunda:field>
        </camunda:taskListener>
        <camunda:taskListener class="com.betalpha.fosun.listener.UserAssignTaskListener" event="assignment" />
        <camunda:taskListener class="com.betalpha.fosun.listener.UserCompleteTaskListener" event="complete" />
        <camunda:taskListener class="com.betalpha.fosun.listener.UserDeleteTaskListener" event="delete" />
        <camunda:executionListener class="com.betalpha.fosun.listener.UserStartExecutionListener" event="start" />
        <camunda:executionListener class="com.betalpha.fosun.listener.UserEndExecutionListener" event="end" />
      </bpmn2:extensionElements>
      <bpmn2:incoming>SequenceFlow_1pv8p0c</bpmn2:incoming>
      <bpmn2:outgoing>SequenceFlow_1sn72gn</bpmn2:outgoing>
      <bpmn2:outgoing>SequenceFlow_0psj5cd</bpmn2:outgoing>
    </bpmn2:userTask>
    <bpmn2:sequenceFlow id="SequenceFlow_1pv8p0c" name="部分同意" sourceRef="UserTask_1u68czq" targetRef="UserTask_0dm0x46">
      <bpmn2:conditionExpression xsi:type="bpmn2:tFormalExpression">${result==5}</bpmn2:conditionExpression>
    </bpmn2:sequenceFlow>
    <bpmn2:sequenceFlow id="SequenceFlow_1sn72gn" sourceRef="UserTask_0dm0x46" targetRef="EndEvent_03w6rbv" />
    <bpmn2:endEvent id="EndEvent_072431o" name="结束">
      <bpmn2:extensionElements>
        <camunda:executionListener class="com.betalpha.fosun.listener.EndProcessEndExecutionListener" event="end">
          <camunda:field name="stored">
            <camunda:string>0</camunda:string>
          </camunda:field>
        </camunda:executionListener>
      </bpmn2:extensionElements>
      <bpmn2:incoming>SequenceFlow_0psj5cd</bpmn2:incoming>
    </bpmn2:endEvent>
    <bpmn2:sequenceFlow id="SequenceFlow_0psj5cd" sourceRef="UserTask_0dm0x46" targetRef="EndEvent_072431o" />
  </bpmn2:process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_1">
    <bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="Process_1">
      <bpmndi:BPMNShape id="StartEvent_1gmkzah_di" bpmnElement="StartEvent_1gmkzah">
        <dc:Bounds x="123" y="149" width="36" height="36" />
        <bpmndi:BPMNLabel>
          <dc:Bounds x="130" y="188" width="22" height="13" />
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape id="UserTask_15s5k13_di" bpmnElement="UserTask_15s5k13">
        <dc:Bounds x="225" y="127" width="100" height="80" />
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge id="SequenceFlow_0cx9pmi_di" bpmnElement="SequenceFlow_0cx9pmi">
        <di:waypoint x="159" y="167" />
        <di:waypoint x="225" y="167" />
        <bpmndi:BPMNLabel>
          <dc:Bounds x="192" y="145" width="0" height="13" />
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNShape id="EndEvent_0nuo9l1_di" bpmnElement="EndEvent_0nuo9l1">
        <dc:Bounds x="257" y="271" width="36" height="36" />
        <bpmndi:BPMNLabel>
          <dc:Bounds x="264" y="310" width="22" height="13" />
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge id="SequenceFlow_1twhlsk_di" bpmnElement="SequenceFlow_1twhlsk">
        <di:waypoint x="275" y="207" />
        <di:waypoint x="275" y="271" />
        <bpmndi:BPMNLabel>
          <dc:Bounds x="274" y="232" width="33" height="13" />
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNShape id="UserTask_1u68czq_di" bpmnElement="UserTask_1u68czq">
        <dc:Bounds x="393" y="127" width="100" height="80" />
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge id="SequenceFlow_00upkrk_di" bpmnElement="SequenceFlow_00upkrk">
        <di:waypoint x="325" y="167" />
        <di:waypoint x="393" y="167" />
        <bpmndi:BPMNLabel>
          <dc:Bounds x="348" y="145" width="22" height="13" />
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNShape id="EndEvent_1ymw1dc_di" bpmnElement="EndEvent_1ymw1dc">
        <dc:Bounds x="425" y="271" width="36" height="36" />
        <bpmndi:BPMNLabel>
          <dc:Bounds x="432" y="310" width="22" height="13" />
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge id="SequenceFlow_0l1hkk3_di" bpmnElement="SequenceFlow_0l1hkk3">
        <di:waypoint x="443" y="207" />
        <di:waypoint x="443" y="271" />
        <bpmndi:BPMNLabel>
          <dc:Bounds x="442" y="232" width="33" height="13" />
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNShape id="EndEvent_03w6rbv_di" bpmnElement="EndEvent_03w6rbv">
        <dc:Bounds x="738" y="149" width="36" height="36" />
        <bpmndi:BPMNLabel>
          <dc:Bounds x="746" y="188" width="22" height="13" />
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge id="SequenceFlow_03u7iwk_di" bpmnElement="SequenceFlow_03u7iwk">
        <di:waypoint x="493" y="167" />
        <di:waypoint x="738" y="167" />
        <bpmndi:BPMNLabel>
          <dc:Bounds x="615.5" y="145.5" width="0" height="13" />
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNShape id="UserTask_0dm0x46_di" bpmnElement="UserTask_0dm0x46">
        <dc:Bounds x="393" y="-28" width="100" height="80" />
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge id="SequenceFlow_1pv8p0c_di" bpmnElement="SequenceFlow_1pv8p0c">
        <di:waypoint x="443" y="127" />
        <di:waypoint x="443" y="52" />
        <bpmndi:BPMNLabel>
          <dc:Bounds x="436" y="82" width="44" height="13" />
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge id="SequenceFlow_1sn72gn_di" bpmnElement="SequenceFlow_1sn72gn">
        <di:waypoint x="493" y="12" />
        <di:waypoint x="616" y="12" />
        <di:waypoint x="616" y="167" />
        <di:waypoint x="738" y="167" />
        <bpmndi:BPMNLabel>
          <dc:Bounds x="631" y="82.5" width="0" height="13" />
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNShape id="EndEvent_072431o_di" bpmnElement="EndEvent_072431o">
        <dc:Bounds x="425" y="-114" width="36" height="36" />
        <bpmndi:BPMNLabel>
          <dc:Bounds x="432" y="-137.5" width="22" height="13" />
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge id="SequenceFlow_0psj5cd_di" bpmnElement="SequenceFlow_0psj5cd">
        <di:waypoint x="443" y="-28" />
        <di:waypoint x="443" y="-78" />
        <bpmndi:BPMNLabel>
          <dc:Bounds x="458" y="-60" width="0" height="13" />
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</bpmn2:definitions>
""".stripMargin


  "ListenerTest" should {
    logger.info("start deploy deployment")
    val resourceName = s"${UUID.randomUUID().toString}.bpmn20.xml"
    val deployment = repositoryService.createDeployment().addString(resourceName, source).name("xxx").deploy()

    "parse success" in {
      deployment should not be null
      val stream = repositoryService.getDeploymentResources(deployment.getId).asInstanceOf[java.util.List[ResourceEntity]]
      val deploymenaat = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId).singleResult()
      val instance = runtimeService.startProcessInstanceByKey(deploymenaat.getKey)
      import java.nio.charset.StandardCharsets
      val sss = new String(stream.get(0).getBytes, StandardCharsets.UTF_8)
      deployment.getSource shouldEqual null
    }

  }


}
