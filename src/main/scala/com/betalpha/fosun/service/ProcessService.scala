package com.betalpha.fosun.service

import com.betalpha.fosun.api.process.{ProcessSource, StartParameter}
import org.camunda.bpm.engine.repository.{Deployment, ProcessDefinition}
import org.camunda.bpm.engine.runtime.ProcessInstance
import org.camunda.bpm.engine.{RepositoryService, RuntimeService, TaskService}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._

/**
  * Created by WangRui on 15/05/2018.
  */

@Service
class ProcessService(repositoryService: RepositoryService,
                     runtimeService: RuntimeService,
                     taskService: TaskService) {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  def createProcess(processSource: ProcessSource): Deployment = {
    repositoryService.createDeployment.addString(s"${processSource.getName}.bpmn20.xml", processSource.getSource).name(processSource.getName).deploy()
  }

  def queryProcessDefinition(): java.util.List[ProcessDefinition] = {
    repositoryService.createProcessDefinitionQuery().active().list()
  }

  def queryDeployments(): java.util.List[ProcessSource] = repositoryService.createProcessDefinitionQuery().list().asScala
    .map(processDefinition => {
      val deployment = repositoryService.createDeploymentQuery().deploymentId(processDefinition.getDeploymentId).singleResult()
      logger.debug("deployment {}", deployment)
      new ProcessSource(processDefinition.getId, processDefinition.getName, deployment.getSource)
    }).asJava


  def startProcess(startParameter: StartParameter): ProcessInstance = {
    runtimeService.startProcessInstanceById(startParameter.getProcessKey, Map[String, Object]("isinId" -> startParameter.getIsinId).asJava)
  }

  def queryVariableByDefinitionId(processDefinitionId: String, variableName: String): Object = {
    runtimeService.getVariable(processDefinitionId, variableName)
  }
}
