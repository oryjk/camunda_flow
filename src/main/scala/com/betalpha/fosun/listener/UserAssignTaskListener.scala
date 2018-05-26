package com.betalpha.fosun.listener

import org.camunda.bpm.engine.delegate.{DelegateTask, TaskListener}
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by WangRui on 17/05/2018.
  *
  * 用户任务在分配给某个人的时候触发
  */
class UserAssignTaskListener extends TaskListener{
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)
  override def notify(delegateTask: DelegateTask): Unit = {
    logger.info("UserAssignTaskListener")
  }
}
