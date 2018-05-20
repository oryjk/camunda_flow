package com.betalpha.fosun.controller;

import com.betalpha.fosun.user.OrganizationStructureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/organization-structure")
public class OrganizationStructureController {
    @Autowired
    private OrganizationStructureService organizationStructureService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getOrganizationStructure() {
        return ResponseEntity.ok(organizationStructureService.getOrganizationStructureApiList());
    }
}
