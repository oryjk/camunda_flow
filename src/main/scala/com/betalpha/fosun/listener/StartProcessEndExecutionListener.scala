package com.betalpha.fosun.listener

import org.camunda.bpm.engine.delegate.{DelegateExecution, ExecutionListener}

/**
  * Created by WangRui on 21/05/2018.
  *
  *
  * 开始节点在end事件时触发
  */
class StartProcessEndExecutionListener extends ExecutionListener {
  override def notify(execution: DelegateExecution) = {

  }
}
