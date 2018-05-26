package com.betalpha.fosun.service

import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

import com.betalpha.fosun.FlowConstants._
import com.betalpha.fosun.api.process.{ProcessSource, StartParameter}
import com.betalpha.fosun.user.DepartmentService
import org.camunda.bpm.engine.impl.persistence.entity.ResourceEntity
import org.camunda.bpm.engine.repository.{Deployment, ProcessDefinition}
import org.camunda.bpm.engine.runtime.ProcessInstance
import org.camunda.bpm.engine.{IdentityService, RepositoryService, RuntimeService, TaskService}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._

/**
  * Created by WangRui on 15/05/2018.
  */

@Service
class ProcessService(repositoryService: RepositoryService,
                     runtimeService: RuntimeService,
                     taskService: TaskService,
                     identityService: IdentityService,
                     departmentService: DepartmentService,
                     ratingService: RatingService) {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  def createProcess(processSource: ProcessSource): Deployment = {
    repositoryService.createDeployment.addString(s"${processSource.getName}.bpmn20.xml", processSource.getSource).name(processSource.getName).deploy()
  }

  def queryProcessDefinition(): java.util.List[ProcessDefinition] = {
    repositoryService.createProcessDefinitionQuery().active().list()
  }

  def queryProcessDefinition(deploymentId: String): ProcessDefinition = {
    repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult()
  }

  def queryDeployments(): java.util.List[ProcessSource] = repositoryService.createProcessDefinitionQuery().list().asScala
    .map(processDefinition => {
      val deployment = repositoryService.createDeploymentQuery().deploymentId(processDefinition.getDeploymentId).singleResult()
      logger.debug("deployment {}", deployment)

      new ProcessSource(processDefinition.getKey,
        deployment.getName,
        new String(repositoryService.getDeploymentResources(deployment.getId).asInstanceOf[java.util.List[ResourceEntity]].get(0).getBytes, StandardCharsets.UTF_8))
    }).asJava


  def queryDeployment(deployment: Deployment): ProcessSource = {

    val processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId).singleResult()
    new ProcessSource(processDefinition.getKey,
      deployment.getName,
      new String(repositoryService.getDeploymentResources(deployment.getId).asInstanceOf[java.util.List[ResourceEntity]].get(0).getBytes, StandardCharsets.UTF_8))

  }


  def startProcess(startParameter: StartParameter): ProcessInstance = {
    identityService.setAuthenticatedUserId(startParameter.getUserId)
    val isMock = startParameter.getIsMock
    val submitter = startParameter.getUserId
    val isIn = startParameter.getIsinId

    val issuer = if (TRUE_String == isMock) submitter else ratingService.getSubmitter(isIn)
    val isGrade = if (TRUE_String == isMock) ratingService.getMockRating(isIn) else ratingService.getRating(isIn)

    runtimeService.startProcessInstanceByKey(startParameter.getProcessKey, Map[String, Object](
      ISIN -> startParameter.getIsinId,
      SUBMITTER -> submitter,
      IS_MOCK -> isMock,
      CREATE_TIME -> LocalDateTime.now().toString,
      SUBMISSION_DEPARTMENT -> departmentService.getUserDepartmentByUserName(startParameter.getUserId),
      ISSUER -> issuer,
      IS_GRADE -> isGrade
    ).asJava)
  }

  def queryVariableByDefinitionId(processDefinitionId: String, variableName: String): Object = {
    runtimeService.getVariable(processDefinitionId, variableName)
  }
}
