package com.betalpha.fosun.listener

import com.betalpha.fosun.FlowConstants.{High_Yield_Bond, Hign_Rate_Bond, IS_GRADE, IS_MOCK}
import org.camunda.bpm.engine.delegate.{DelegateExecution, ExecutionListener}
import org.camunda.bpm.engine.impl.el.FixedValue
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by WangRui on 21/05/2018.
  *
  * 网关在start事件时触发
  */
class GatewayStartExecutionListener extends ExecutionListener {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)
  logger.info("GatewayStartExecutionListener is working ")
  var isHighYield: FixedValue = new FixedValue("0")

  override def notify(execution: DelegateExecution) = {
    val isGrade = execution.getVariable(IS_GRADE).asInstanceOf[String]
    val isMock = execution.getVariable(IS_MOCK).asInstanceOf[String]

    if ("1".equals(isHighYield.getExpressionText)) {

      if ("1".equals(isMock)) {
        execution.setVariable("result", 1)
      }
      if (High_Yield_Bond.eq(isGrade)) {
        execution.setVariable("result", 1)
      }
      if (Hign_Rate_Bond.eq(isGrade)) {
        execution.setVariable("result", 0)
      }
    }
  }
}
