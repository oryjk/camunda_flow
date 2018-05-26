package com.betalpha.fosun.user.service

import java.util

import com.betalpha.fosun.api.process.Task
import com.betalpha.fosun.repostory.CompleteProcessRepository
import org.camunda.bpm.engine.{HistoryService, RuntimeService, TaskService}
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._

/**
  * Created by WangRui on 17/05/2018.
  */
trait BarTaskService {

  def queryUserTask(userId: String): java.util.List[Task]

  def querySubmitterTask(userId: String): java.util.List[Task]

  def queryHistoryTask(userId: String): java.util.List[Task]

}


@Service
class BarTaskServiceImp(taskService: TaskService,
                        runtimeService: RuntimeService,
                        historyService: HistoryService,
                        completeProcessRepository: CompleteProcessRepository) extends BarTaskService {

  import com.betalpha.fosun.FlowConstants._

  override def queryUserTask(userId: String): util.List[Task] = {
    taskService.createTaskQuery().active().taskCandidateUser(userId).list().asScala
      .map(task => {
        val isIn = runtimeService.getVariable(task.getExecutionId, ISIN).asInstanceOf[String]
        val isGrade = runtimeService.getVariable(task.getExecutionId, IS_GRADE).asInstanceOf[String]
        val submissionDepartment = runtimeService.getVariable(task.getExecutionId, SUBMISSION_DEPARTMENT).asInstanceOf[String]
        val submitter = runtimeService.getVariable(task.getExecutionId, SUBMITTER).asInstanceOf[String]
        val issuer = runtimeService.getVariable(task.getExecutionId, ISSUER).asInstanceOf[String]
        new Task(task.getProcessInstanceId, task.getName, task.getCreateTime.toString, isIn, isGrade, submissionDepartment, submitter, issuer)
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
        val issuer = runtimeService.getVariable(task.getExecutionId, ISSUER).asInstanceOf[String]
        new Task(task.getProcessInstanceId, task.getName, task.getCreateTime.toString, isIn, isGrade, submissionDepartment, userId, issuer)
      }).asJava
  }

  override def queryHistoryTask(userId: String): util.List[Task] = {
    historyService.createHistoricProcessInstanceQuery().startedBy(userId).list().asScala
      .filter(ins => {
        val completeProcess = completeProcessRepository.findOne(ins.getId)
        completeProcess != null
      }).map(ins => {
      val completeProcess = completeProcessRepository.findOne(ins.getId)
      new Task(
        ins.getId,
        completeProcess.getName,
        completeProcess.getCreateTime,
        completeProcess.getBondCode,
        completeProcess.getGrade,
        completeProcess.getSubmissionDepartment,
        completeProcess.getSubmitter,
        completeProcess.getIssuer)
    }).asJava
  }
}
