package com.betalpha.fosun.user;

import com.betalpha.fosun.api.OrganizationStructureApi;
import com.betalpha.fosun.model.FusonUser;
import com.betalpha.fosun.model.InvestmentCommittee;
import jersey.repackaged.com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrganizationStructureService {
    @Autowired
    private DepartmentService departmentService;

    public List<OrganizationStructureApi> getOrganizationStructureApiList() {
        List<InvestmentCommittee> investmentCommitteeList = departmentService.genInvestmentCommitteeList();
        log.info("investmentCommitteeList size:{}", investmentCommitteeList.size());
        Map<String, OrganizationStructureApi> organizationStructureApiMap = new HashMap<>();
        Map<String, OrganizationStructureApi> childrenMap = new HashMap<>();
        investmentCommitteeList.forEach(investmentCommittee -> {
            OrganizationStructureApi organizationStructureApi = new OrganizationStructureApi();
            BeanUtils.copyProperties(investmentCommittee, organizationStructureApi);
            if (!StringUtils.isEmpty(investmentCommittee.getParentId())) {
                childrenMap.put(investmentCommittee.getParentId(), organizationStructureApi);
            } else {
                organizationStructureApiMap.put(organizationStructureApi.getId(), organizationStructureApi);
            }
        });
        log.info("organizationStructureApiMap ,size:{}", organizationStructureApiMap.size());
        log.info("childrenMap ,size:{}", childrenMap.size());
        if (!CollectionUtils.isEmpty(childrenMap)) {
            childrenMap.forEach((key, value) -> organizationStructureApiMap.get(key).getChildren().add(value));
        }
        return Lists.newArrayList(organizationStructureApiMap.values());
    }


    public List<FusonUser> getAllUser() {
        return departmentService.genInvestmentCommitteeList()
                .stream()
                .flatMap(investmentCommittee ->
                        investmentCommittee.getUserGroup().stream())
                .collect(Collectors.toList());
    }


    public List<FusonUser> getUsersById(String committeeId) {

        return departmentService.genInvestmentCommitteeList()
                .stream()
                .filter(investmentCommittee ->
                        investmentCommittee.getId().equals(committeeId) || investmentCommittee.getParentId().equals(committeeId))
                .flatMap(investmentCommittee ->
                        investmentCommittee.getUserGroup().stream())
                .collect(Collectors.toList());
    }

    public List<String> getDealer() {

        return Lists.newArrayList(
                CommitteeConstants.BOND_COMMITTEE,
                CommitteeConstants.VOTE_COMMITTEE,
                CommitteeConstants.INTERNAL_AUDIT

        );
    }

    public List<String> getDepartment() {
        return Lists.newArrayList(
                CommitteeConstants.DEPARTMENT,
                CommitteeConstants.COMPANY
        );
    }

}
