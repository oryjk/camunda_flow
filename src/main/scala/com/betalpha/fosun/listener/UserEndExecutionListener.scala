package com.betalpha.fosun.listener

import org.camunda.bpm.engine.delegate.{DelegateExecution, ExecutionListener}
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by WangRui on 17/05/2018.
  *
  * 通用的ExecutionListener，在end事件触发
  */
class UserEndExecutionListener extends ExecutionListener {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  override def notify(execution: DelegateExecution): Unit = {
    logger.info("UserEndExecutionListener")
  }
}
