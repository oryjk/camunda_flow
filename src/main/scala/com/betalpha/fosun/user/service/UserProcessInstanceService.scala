package com.betalpha.fosun.user.service

import com.betalpha.fosun.user.service.UserProcessInstanceService.ProcessInstanceApi

/**
  * Created by WangRui on 14/05/2018.
  */


trait
UserProcessInstanceService {

  def queryProcessInstance(userId: String): List[ProcessInstanceApi]
}

object UserProcessInstanceService {

  case class ProcessInstanceApi(processInstanceId: String, name: String) extends Serializable

}

