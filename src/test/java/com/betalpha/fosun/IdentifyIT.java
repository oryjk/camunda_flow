package com.betalpha.fosun;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by WangRui on 13/05/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BackendFosunApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class IdentifyIT {
    @Autowired
    private IdentityService identityService;

    @Test
    public void create_user_test() {
        User user = identityService.newUser("001");
        identityService.saveUser(user);
        List<User> list = identityService.createUserQuery().userId(user.getId()).list();
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void query_user_test() {
        User user = identityService.newUser("001");
        identityService.saveUser(user);
        List<User> list = identityService.createUserQuery().list();
        Assert.assertFalse(CollectionUtils.isEmpty(list));
    }

    @Test
    public void query_user_group() {
        Group groupCompany = identityService.newGroup("company");
        User user = identityService.newUser("007");
        User user2 = identityService.newUser("008");

        identityService.saveUser(user);
        identityService.saveUser(user2);
        identityService.saveGroup(groupCompany);
        identityService.createMembership("007", "company");
        identityService.createMembership("008", "company");
        identityService.setUserInfo("007", "oneVotePower", "true");
        identityService.setUserInfo("007", "admin", "true");

        identityService.setUserInfo("008", "oneVotePower", "false");

        List<User> list = identityService.createUserQuery().list();
        List<Group> Group = identityService.createGroupQuery().list();
        String oneVotePower = identityService.getUserInfo("008", "oneVotePower");
        String oneVotePowerTrue = identityService.getUserInfo("007", "oneVotePower");
        String admin = identityService.getUserInfo("007", "admin");

        Assert.assertFalse(Boolean.parseBoolean(oneVotePower));
        Assert.assertTrue(Boolean.parseBoolean(oneVotePowerTrue));
        Assert.assertTrue(Boolean.parseBoolean(admin));
        Assert.assertFalse(CollectionUtils.isEmpty(list));
        Assert.assertFalse(CollectionUtils.isEmpty(Group));

    }

}
