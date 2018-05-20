package com.betalpha.fosun.user;

import com.betalpha.fosun.api.OrganizationStructureApi;
import com.betalpha.fosun.model.FusonUser;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
@Slf4j
public class UserService {
    @Autowired
    private IdentityService identityService;
    @Autowired
    private OrganizationStructureService organizationStructureService;

    private UserService() {
    }


    @PostConstruct
    public void initialUserIntoH2() {
        saveUserMap(MockUserUtils.getAUserMap());
        saveUserMap(MockUserUtils.getBUserMap());
        saveUserMap(MockUserUtils.getPUserMap());
        saveUserMap(MockUserUtils.getVUserMap());
        saveUserMap(MockUserUtils.getIUserMap());
        log.info("save all user finished");
        saveUserAndGroup();
        log.info("save all group and membership finished");
    }

    private void saveUserMap(Map<String, String> userMap) {
        userMap.forEach((k, v) -> {
            User user = identityService.newUser(k);
            user.setFirstName(v);
            identityService.saveUser(user);
        });
    }

    private void saveUserAndGroup() {
        List<OrganizationStructureApi> organizationStructureApiList = organizationStructureService.getOrganizationStructureApiList();
        organizationStructureApiList.forEach(organizationStructureApi -> {
            buildGroupAndMemberShip(organizationStructureApi);
            if (!CollectionUtils.isEmpty(organizationStructureApi.getChildren())) {
                List<OrganizationStructureApi> children = organizationStructureApi.getChildren();
                children.forEach(this::buildGroupAndMemberShip);
            }
        });
    }

    private void buildGroupAndMemberShip(OrganizationStructureApi organizationStructureApi) {
        Group group = identityService.newGroup(organizationStructureApi.getId());
        group.setName(organizationStructureApi.getName());
        identityService.saveGroup(group);
        if (!StringUtils.isEmpty(organizationStructureApi.getVetoPowerUser())) {
            String vetoUser = organizationStructureApi.getVetoPowerUser();
            identityService.setUserInfo(vetoUser, "oneVetoPower", "true");
        }
        List<FusonUser> userList = organizationStructureApi.getUserGroup();
        userList.forEach(user -> identityService.createMembership(user.getId(), group.getId()));
    }

    public Map<String, String> getUserMap() {
        return identityService.createUserQuery().list().stream().collect(toMap(User::getFirstName, User::getId));
    }
}
