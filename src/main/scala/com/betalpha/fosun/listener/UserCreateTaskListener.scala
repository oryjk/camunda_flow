package com.betalpha.fosun.listener

import org.camunda.bpm.engine.delegate.{DelegateTask, TaskListener}
import org.camunda.bpm.engine.impl.el.FixedValue
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

/**
  * Created by WangRui on 17/05/2018.
  */
class UserCreateTaskListener extends TaskListener {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)
  var votable: FixedValue = new FixedValue("1")
  var processor: FixedValue = new FixedValue("")
  var department: FixedValue = new FixedValue("department")
  var oneVote: FixedValue = new FixedValue("0")
  var passThreshold: FixedValue = new FixedValue("1")
  var continueResearch: FixedValue = new FixedValue("1")
  var noneProcessorSkip: FixedValue = new FixedValue("1")

  override def notify(delegateTask: DelegateTask): Unit = {
    logger.info("task id {}, delegateTask {}, user {},votable {}", delegateTask.getId, delegateTask, delegateTask.getVariable("group1"), votable.getExpressionText)
    delegateTask.addCandidateUser(department.getExpressionText)
    delegateTask.setVariables(Map("votable" -> votable.getExpressionText
      , "processor" -> processor.getExpressionText
      , "department" -> department.getExpressionText
      , "oneVote" -> oneVote.getExpressionText
      , "passThreshold" -> passThreshold.getExpressionText
      , "continueResearch" -> continueResearch.getExpressionText
      , "noneProcessorSkip" -> noneProcessorSkip.getExpressionText
    ).asJava)
  }
}
