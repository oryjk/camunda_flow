package com.betalpha.fosun.user.service

import java.util

import com.betalpha.fosun.api.process.Task
import org.camunda.bpm.engine.{RuntimeService, TaskService}
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._

/**
  * Created by WangRui on 17/05/2018.
  */
trait BarTaskService {

  def queryUserTask(userId: String): java.util.List[Task]

  def querySubmitterTask(userId: String): java.util.List[Task]

}


@Service
class BarTaskServiceImp(taskService: TaskService, runtimeService: RuntimeService) extends BarTaskService {

  import com.betalpha.fosun.FlowConstants._

  override def queryUserTask(userId: String): util.List[Task] = {
    taskService.createTaskQuery().active().taskCandidateUser(userId).list().asScala
      .map(task => {
        val isIn = runtimeService.getVariable(task.getExecutionId, ISIN).asInstanceOf[String]
        val isGrade = runtimeService.getVariable(task.getExecutionId, IS_GRADE).asInstanceOf[String]
        val submissionDepartment = runtimeService.getVariable(task.getExecutionId, SUBMISSION_DEPARTMENT).asInstanceOf[String]
        val submitter = runtimeService.getVariable(task.getExecutionId, SUBMITTER).asInstanceOf[String]
        new Task(task.getProcessInstanceId, task.getName, task.getCreateTime.toString, isIn, isGrade, submissionDepartment, submitter)
      }).asJava
  }

  override def querySubmitterTask(userId: String): util.List[Task] = {
    runtimeService.createExecutionQuery().list().asScala
      .filter(execution => {
        val submitter = runtimeService.getVariable(execution.getId, SUBMITTER).asInstanceOf[String]
        submitter.equals(userId)
      })
      .map(execution => {
        val task = taskService.createTaskQuery().executionId(execution.getId).singleResult()
        val isIn = runtimeService.getVariable(task.getExecutionId, ISIN).asInstanceOf[String]
        val isGrade = runtimeService.getVariable(task.getExecutionId, IS_GRADE).asInstanceOf[String]
        val submissionDepartment = runtimeService.getVariable(task.getExecutionId, SUBMISSION_DEPARTMENT).asInstanceOf[String]
        new Task(task.getProcessInstanceId, task.getName, task.getCreateTime.toString, isIn, isGrade, submissionDepartment, userId)
      }).asJava
  }
}
