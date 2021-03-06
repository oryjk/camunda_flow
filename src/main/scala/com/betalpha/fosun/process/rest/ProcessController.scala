package com.betalpha.fosun.process.rest

import com.betalpha.fosun.api.RestResponse
import com.betalpha.fosun.api.process._
import com.betalpha.fosun.service.{AssetPoolService, ProcessService}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

/**
  * Created by WangRui on 15/05/2018.
  */
@RequestMapping(Array("/api/process"))
@RestController
class ProcessController {

  @Autowired
  var processService: ProcessService = _
  @Autowired
  var assetPoolService: AssetPoolService = _


  var logger: Logger = LoggerFactory.getLogger(this.getClass)


  @PostMapping(Array("/start"))
  def startProcess(@RequestBody startParameter: StartParameter): ResponseEntity[RestResponse] = {
    logger.info("start process {}", startParameter)
    val dataBaseIsIn = assetPoolService.checkExistIsIn(startParameter.getIsinId)
    if (dataBaseIsIn.isPresent) {
      new ResponseEntity[RestResponse](new RestResponse(startParameter.getIsinId + " 已入池"), HttpStatus.OK)
    } else {
      val instance = processService.startProcess(startParameter)
      logger.info("new instance id {}", instance.getId)
      new ResponseEntity[RestResponse](new RestResponse(new StartSuccess(instance.getId)), HttpStatus.CREATED)
    }
  }

  @PostMapping(Array("/create"))
  def createProcess(@RequestBody processSource: ProcessSource): ResponseEntity[RestResponse] = {
    logger.info("create process {}", processSource)
    val deployment = processService.createProcess(processSource)
    new ResponseEntity[RestResponse](new RestResponse(processService.queryDeployment(deployment)), HttpStatus.CREATED)
  }

  @GetMapping(Array("/list"))
  def processList(): ResponseEntity[RestResponse] = {
    new ResponseEntity[RestResponse](new RestResponse(processService.queryDeployments()), HttpStatus.OK)
  }

}
