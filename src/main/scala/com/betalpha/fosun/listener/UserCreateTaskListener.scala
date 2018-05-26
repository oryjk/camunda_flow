package com.betalpha.fosun.listener

import com.betalpha.fosun.FlowConstants._
import com.betalpha.fosun.user.DepartmentService
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.delegate.{DelegateTask, TaskListener}
import org.camunda.bpm.engine.impl.el.FixedValue
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.stereotype.Service
import org.springframework.util.CollectionUtils

import scala.collection.JavaConverters._

/**
  * Created by WangRui on 17/05/2018.
  *
  * 用户任务在创建的时候触发
  */
@Service
class UserCreateTaskListener(departmentService: DepartmentService
                             , runtimeService: RuntimeService) extends TaskListener {


  private val logger: Logger = LoggerFactory.getLogger(this.getClass)
  var votable: FixedValue = new FixedValue("1")
  var processor: FixedValue = new FixedValue("")
  var department: FixedValue = new FixedValue("department")
  var oneVote: FixedValue = new FixedValue("0")
  var passThreshold: FixedValue = new FixedValue("1")
  var continueResearch: FixedValue = new FixedValue("1")
  var noneProcessorSkip: FixedValue = new FixedValue("1")
  var allPassed: FixedValue = new FixedValue("0")

  override def notify(delegateTask: DelegateTask): Unit = {
    logger.info("task id {}, delegateTask {}, user {},votable {}", delegateTask.getId, delegateTask, delegateTask.getVariable("group1"), votable.getExpressionText)
    val userNameList = departmentService.getDepartmentIdByNameAndLevel(
      runtimeService.getVariable(delegateTask.getProcessInstanceId, SUBMITTER).toString, processor.getExpressionText, department.getExpressionText)

    delegateTask.addCandidateUsers(userNameList)

    delegateTask.setVariables(Map("votable" -> votable.getExpressionText
      , "processor" -> processor.getExpressionText
      , "department" -> department.getExpressionText
      , "oneVote" -> oneVote.getExpressionText
      , "passThreshold" -> passThreshold.getExpressionText
      , "continueResearch" -> continueResearch.getExpressionText
      , "noneProcessorSkip" -> noneProcessorSkip.getExpressionText
      , "allPassed" -> allPassed.getExpressionText
    ).asJava)

    if (FALSE_String.equals(delegateTask.getVariable("votable")) ||
      (TRUE_String.equals(delegateTask.getVariable("noneProcessorSkip")) && CollectionUtils.isEmpty(userNameList))) {
      delegateTask.complete()
    }
  }

}
