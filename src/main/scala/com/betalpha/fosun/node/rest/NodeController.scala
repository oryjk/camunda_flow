package com.betalpha.fosun.node.rest

import com.betalpha.fosun.api.RestResponse
import com.betalpha.fosun.user.OrganizationStructureService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RestController}

/**
  * Created by WangRui on 18/05/2018.
  */
@RequestMapping(Array("/api/node"))
@RestController
class NodeController {

  @Autowired
  var organizationStructureService: OrganizationStructureService = _

  @GetMapping(Array("/processor"))
  def processorList(): ResponseEntity[RestResponse] = {
    new ResponseEntity[RestResponse](new RestResponse(organizationStructureService.getDealer), HttpStatus.OK)
  }

  @GetMapping(Array("/departments"))
  def departmentList(): ResponseEntity[RestResponse] = {
    new ResponseEntity[RestResponse](new RestResponse(organizationStructureService.getDepartment), HttpStatus.OK)
  }

}
