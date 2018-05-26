package com.betalpha.fosun.service;

import com.betalpha.fosun.BackendFosunApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BackendFosunApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class AssetPoolServiceIT {
    @Autowired
    private AssetPoolService assetPoolService;

    @Test
    public void save_end_to_pool_process() {

        assetPoolService.saveIsInEndToPool("22", "high", "A");
        Optional submitter = assetPoolService.checkExistIsIn("22");
        Assert.assertTrue(submitter.isPresent());
        Optional submitter2 = assetPoolService.checkExistIsIn("11");
        Assert.assertFalse(submitter2.isPresent());

    }
}