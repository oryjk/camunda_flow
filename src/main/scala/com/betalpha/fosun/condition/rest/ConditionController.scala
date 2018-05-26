package com.betalpha.fosun.condition.rest

import com.betalpha.fosun.api.RestResponse
import com.betalpha.fosun.condition.ConditionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RestController}

/**
  * Created by WangRui on 17/05/2018.
  */

@RequestMapping(Array("/api/conditions"))
@RestController
class ConditionController {

  @Autowired
  var conditionService: ConditionService = _


  @GetMapping(Array("/list"))
  def processList(): ResponseEntity[RestResponse] = {
    new ResponseEntity[RestResponse](new RestResponse(conditionService.getConditions()), HttpStatus.OK)
  }
}
