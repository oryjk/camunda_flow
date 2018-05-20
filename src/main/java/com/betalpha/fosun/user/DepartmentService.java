package com.betalpha.fosun.user;

import com.betalpha.fosun.model.FusonUser;
import com.betalpha.fosun.model.InvestmentCommittee;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class DepartmentService {

    public List<InvestmentCommittee> genInvestmentCommitteeList() {

        List<FusonUser> pUserList = MockUserUtils.getPUserMap().entrySet().stream().map(k -> new FusonUser(k.getKey(), k.getValue())).collect
                (toList());
        InvestmentCommittee pInvestmentCommittee = new InvestmentCommittee();
        pInvestmentCommittee.setId(CommitteeConstants.BOND_COMMITTEE_COMPANY_ID);
        pInvestmentCommittee.setName(CommitteeConstants.BOND_COMMITTEE);
        pInvestmentCommittee.setLevel(CommitteeConstants.COMPANY);
        pInvestmentCommittee.setUserGroup(pUserList);
        pInvestmentCommittee.setVetoPowerUser("p1");

        List<FusonUser> vUserList = MockUserUtils.getVUserMap().entrySet().stream().map(k -> new FusonUser(k.getKey(), k.getValue())).collect
                (toList());

        InvestmentCommittee vInvestmentCommittee = new InvestmentCommittee();
        vInvestmentCommittee.setId(CommitteeConstants.VOTE_COMMITTEE_COMPANY_ID);
        vInvestmentCommittee.setName(CommitteeConstants.VOTE_COMMITTEE);
        vInvestmentCommittee.setLevel(CommitteeConstants.COMPANY);
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
        iInvestmentCommittee.setVetoPowerUser("i1");
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

}
