package com.betalpha.fosun.listener

import org.camunda.bpm.engine.delegate.{DelegateExecution, ExecutionListener}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

/**
  * Created by WangRui on 17/05/2018.
  *
  * 通用的ExecutionListener，在start事件触发
  */
class UserStartExecutionListener extends ExecutionListener {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  override def notify(execution: DelegateExecution): Unit = {
    logger.info("UserStartExecutionListener")
    execution.setVariableLocal("group1", "group 1")
    execution.setVariableLocal("count", "10")
    execution.setVariableLocal("assigneeList", List(1, 2, 3, 4).asJava)


  }
}
