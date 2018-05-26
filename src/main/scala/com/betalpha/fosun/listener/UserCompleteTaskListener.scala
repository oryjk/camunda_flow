package com.betalpha.fosun.listener

import com.betalpha.fosun.FlowConstants._
import com.betalpha.fosun.user.DepartmentService
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.delegate.{DelegateTask, TaskListener}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.stereotype.Service
import org.springframework.util.CollectionUtils

/**
  * Created by WangRui on 17/05/2018.
  *
  * 用户任务在完成的时候触发
  */
@Service
class UserCompleteTaskListener(departmentService: DepartmentService, runtimeService: RuntimeService) extends TaskListener {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)
  logger.info("UserCompleteTaskListener")

  override def notify(delegateTask: DelegateTask): Unit = {
    val processor = delegateTask.getVariable("processor").toString
    val department = delegateTask.getVariable("department").toString
    val userNameList = departmentService.getDepartmentIdByNameAndLevel(
      runtimeService.getVariable(delegateTask.getProcessInstanceId, SUBMITTER).toString, processor, department)
    delegateTask.addCandidateUsers(userNameList)

    val votable = delegateTask.getVariable("votable")
    val noneProcessorSkip = delegateTask.getVariable("")
    if (FALSE_String.equals(votable)) {
      delegateTask.setVariable("result", 1)
    }
    if (TRUE_String.equals(delegateTask.getVariable("noneProcessorSkip")) && CollectionUtils.isEmpty(userNameList)) {
      delegateTask.setVariable("result", 1)
    }
  }
}
