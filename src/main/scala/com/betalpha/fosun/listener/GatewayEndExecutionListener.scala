package com.betalpha.fosun.listener

import org.camunda.bpm.engine.delegate.{DelegateExecution, ExecutionListener}
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by WangRui on 21/05/2018.
  *
  * 网关在end事件时触发
  */
class GatewayEndExecutionListener extends ExecutionListener {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)
  logger.info("GatewayEndExecutionListener is working ")
  override def notify(execution: DelegateExecution) = {

  }
}
