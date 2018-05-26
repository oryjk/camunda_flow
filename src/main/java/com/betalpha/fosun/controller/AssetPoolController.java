package com.betalpha.fosun.controller;


import com.betalpha.fosun.service.AssetPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/api/asset-pool", produces = MediaType.APPLICATION_JSON_VALUE)
public class AssetPoolController {

    @Autowired
    private AssetPoolService assetPoolService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getAssetPool() {
        return ResponseEntity.ok(assetPoolService.getAssetPool());
    }
}