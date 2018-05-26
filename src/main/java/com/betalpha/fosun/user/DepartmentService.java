package com.betalpha.fosun.user;

import com.betalpha.fosun.model.FusonUser;
import com.betalpha.fosun.model.InvestmentCommittee;
import com.google.common.collect.Maps;
import jersey.repackaged.com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.betalpha.fosun.user.CommitteeConstants.COMPANY;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class DepartmentService {

    public List<InvestmentCommittee> genInvestmentCommitteeList() {

        List<FusonUser> pUserList = MockUserUtils.getPUserMap().entrySet().stream().map(k -> new FusonUser(k.getKey(), k.getValue())).collect
                (toList());
        InvestmentCommittee pInvestmentCommittee = new InvestmentCommittee();
        pInvestmentCommittee.setId(CommitteeConstants.BOND_COMMITTEE_COMPANY_ID);
        pInvestmentCommittee.setName(CommitteeConstants.BOND_COMMITTEE);
        pInvestmentCommittee.setLevel(COMPANY);
        pInvestmentCommittee.setUserGroup(pUserList);
        pInvestmentCommittee.setVotePowerUser("P_user_01");

        List<FusonUser> vUserList = MockUserUtils.getVUserMap().entrySet().stream().map(k -> new FusonUser(k.getKey(), k.getValue())).collect
                (toList());

        InvestmentCommittee vInvestmentCommittee = new InvestmentCommittee();
        vInvestmentCommittee.setId(CommitteeConstants.VOTE_COMMITTEE_COMPANY_ID);
        vInvestmentCommittee.setName(CommitteeConstants.VOTE_COMMITTEE);
        vInvestmentCommittee.setLevel(COMPANY);
        vInvestmentCommittee.setUserGroup(vUserList);


        List<FusonUser> aUserList = MockUserUtils.getAUserMap().entrySet().stream().map(k -> new FusonUser(k.getKey(), k.getValue())).collect
                (toList());
        InvestmentCommittee aInvestmentCommittee = new InvestmentCommittee();
        aInvestmentCommittee.setId(CommitteeConstants.A_COMMITTEE_DEPARTMENT_ID);
        aInvestmentCommittee.setName("A部门");
        aInvestmentCommittee.setLevel(CommitteeConstants.DEPARTMENT);
        aInvestmentCommittee.setUserGroup(aUserList);

        List<FusonUser> iUserList = MockUserUtils.getIUserMap().entrySet().stream().map(k -> new FusonUser(k.getKey(), k.getValue())).collect
                (toList());
        InvestmentCommittee iInvestmentCommittee = new InvestmentCommittee();
        iInvestmentCommittee.setId(CommitteeConstants.A_INTERNAL_AUDIT_DEPARTMENT_ID);
        iInvestmentCommittee.setName(CommitteeConstants.INTERNAL_AUDIT);
        iInvestmentCommittee.setLevel(CommitteeConstants.DEPARTMENT);
        iInvestmentCommittee.setParentId("3");
        iInvestmentCommittee.setVotePowerUser("I_user_01");
        iInvestmentCommittee.setUserGroup(iUserList);


        List<FusonUser> bUserList = MockUserUtils.getBUserMap().entrySet().stream().map(k -> new FusonUser(k.getKey(), k.getValue())).collect
                (toList());

        InvestmentCommittee bInvestmentCommittee = new InvestmentCommittee();
        bInvestmentCommittee.setId(CommitteeConstants.B_COMMITTEE_DEPARTMENT_ID);
        bInvestmentCommittee.setName("B部门");
        bInvestmentCommittee.setLevel(CommitteeConstants.DEPARTMENT);
        bInvestmentCommittee.setUserGroup(bUserList);
        return Lists.newArrayList(pInvestmentCommittee, vInvestmentCommittee, aInvestmentCommittee,
                iInvestmentCommittee, bInvestmentCommittee);
    }

    public List<String> getDepartmentIdByNameAndLevel(String userName, String name, String level) {
        List<InvestmentCommittee> investmentCommitteeList = genInvestmentCommitteeList();
        Map<String, List<String>> departmentUserNameMap = Maps.newHashMap();
        List<String> companyUserNameList = Lists.newArrayList();
        String[] belongParentId = {""};
        investmentCommitteeList.forEach(investmentCommittee -> {
            List<String> userIdList = Lists.newArrayList();
            List<String> userNameList = investmentCommittee.getUserGroup().stream().map(FusonUser::getName).collect(toList());
            if (investmentCommittee.getName().equals(name) && investmentCommittee.getLevel().equals(level)) {
                if (COMPANY.equals(level)) {
                    companyUserNameList.addAll(userNameList);
                }
                userIdList.addAll(userNameList);
            }
            departmentUserNameMap.put(
                    investmentCommittee.getParentId() == null ? investmentCommittee.getId() : investmentCommittee.getParentId(), userIdList);

            if (userNameList.contains(userName)) {
                belongParentId[0] = investmentCommittee.getId();
            }
        });
        if (CollectionUtils.isEmpty(departmentUserNameMap.get(belongParentId[0]))) {
            return companyUserNameList;
        } else {
            return departmentUserNameMap.get(belongParentId[0]);
        }
    }

    public String getUserDepartmentByUserName(String userName) {
        String[] departmentName = {""};
        List<InvestmentCommittee> investmentCommitteeList = genInvestmentCommitteeList();
        investmentCommitteeList.forEach(investmentCommittee -> {
            List<FusonUser> fusonUserList = investmentCommittee.getUserGroup();
            fusonUserList.forEach(fusonUser -> {
                if (fusonUser.getName().equals(userName)) {
                    departmentName[0] = investmentCommittee.getName();
                }
            });
        });
        return departmentName[0];
    }

}
