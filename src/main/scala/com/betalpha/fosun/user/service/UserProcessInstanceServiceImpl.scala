package com.betalpha.fosun.user.service

import com.betalpha.fosun.user.service.UserProcessInstanceService.ProcessInstanceApi
import org.camunda.bpm.engine.{IdentityService, RepositoryService, RuntimeService, TaskService}
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._

/**
  * Created by WangRui on 14/05/2018.
  */
@Service
class UserProcessInstanceServiceImpl(runtimeService: RuntimeService,
                                     identityService: IdentityService,
                                     taskService: TaskService,
                                     repositoryService: RepositoryService) extends UserProcessInstanceService {


  override def queryProcessInstance(userId: String): List[ProcessInstanceApi] = {

    taskService.createTaskQuery().taskCandidateUser(userId).list().asScala
      .map(task => {
        val definition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId).singleResult()
        val deployment = repositoryService.createDeploymentQuery().deploymentId(definition.getDeploymentId).singleResult()
        ProcessInstanceApi(task.getProcessInstanceId, deployment.getName)
      }).toList
  }
}
