package com.betalpha.fosun;

import com.betalpha.fosun.api.OrganizationStructureApi;
import com.betalpha.fosun.user.OrganizationStructureService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = BackendFosunApplication.class)
public class OrganizationStructureServiceIT {
    @Autowired
    private OrganizationStructureService organizationStructureService;

    @Test
    public void getOrganizationStructureApiList() {
        List<OrganizationStructureApi> organizationStructureApiList = organizationStructureService.getOrganizationStructureApiList();
        Assert.assertFalse(CollectionUtils.isEmpty(organizationStructureApiList));
    }
}