package com.betalpha.fosun.listener

import com.betalpha.fosun.FlowConstants._
import com.betalpha.fosun.model.CompleteProcess
import com.betalpha.fosun.repostory.CompleteProcessRepository
import com.betalpha.fosun.service.{AssetPoolService, RatingService}
import org.camunda.bpm.engine.delegate.{DelegateExecution, ExecutionListener}
import org.camunda.bpm.engine.impl.el.FixedValue
import org.springframework.stereotype.Service

/**
  * Created by WangRui on 21/05/2018.
  *
  * 结束节点在end事件触发
  */
@Service
class EndProcessEndExecutionListener(assetPoolService: AssetPoolService,
                                     completeProcessRepository: CompleteProcessRepository,
                                     ratingService: RatingService) extends ExecutionListener {
  var stored: FixedValue = new FixedValue(FALSE_String)

  override def notify(execution: DelegateExecution) = {

    val processInstanceId = execution.getProcessInstanceId
    val isInCode = execution.getVariable(ISIN).asInstanceOf[String]
    val isGrade = execution.getVariable(IS_GRADE).asInstanceOf[String]
    val isMock = execution.getVariable(IS_MOCK).asInstanceOf[String]
    val submissionDepartment = execution.getVariable(SUBMISSION_DEPARTMENT).asInstanceOf[String]
    val submitter = execution.getVariable(SUBMITTER).asInstanceOf[String]
    val name = execution.getCurrentActivityName
    val createTime = execution.getVariable(CREATE_TIME).asInstanceOf[String]
    val issuer = execution.getVariable(ISSUER).asInstanceOf[String]

    if (processInstanceId != null) {
      if (stored.getExpressionText.equals(TRUE_String) && FALSE_String.equals(isMock)) assetPoolService.saveIsInEndToPool(isInCode, isGrade, issuer)
      completeProcessRepository.save(new CompleteProcess(processInstanceId, createTime, isInCode, isGrade, submissionDepartment, submitter, name, issuer))
    }
  }
}
