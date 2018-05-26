package com.betalpha.fosun.user;

import com.betalpha.fosun.BackendFosunApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BackendFosunApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class DepartmentServiceIT {

    @Autowired
    private DepartmentService departmentService;

    @Test
    public void get_departmentId_by_name_and_level_case1() {
        List<String> names = departmentService.getDepartmentIdByNameAndLevel("A_user", "投决会", "公司");
        Assert.assertFalse(CollectionUtils.isEmpty(names));
    }

    @Test
    public void get_departmentId_by_name_and_level_case2() {
        List<String> names = departmentService.getDepartmentIdByNameAndLevel("A_user", "债券委员会", "公司");
        Assert.assertFalse(CollectionUtils.isEmpty(names));
    }

    @Test
    public void get_departmentId_by_name_and_level_case3() {
        List<String> names = departmentService.getDepartmentIdByNameAndLevel("A_user", "内审会", "部门");
        Assert.assertFalse(CollectionUtils.isEmpty(names));
    }

    @Test
    public void get_departmentId_by_name_and_level_case4() {
        List<String> names = departmentService.getDepartmentIdByNameAndLevel("A_user", "投决会", "部门");
        Assert.assertTrue(CollectionUtils.isEmpty(names));
    }

    @Test
    public void get_departmentId_by_name_and_level_case5() {
        List<String> names = departmentService.getDepartmentIdByNameAndLevel("A_user", "债券委员会", "部门");
        Assert.assertTrue(CollectionUtils.isEmpty(names));
    }

    @Test
    public void get_departmentId_by_name_and_level_case6() {
        List<String> names = departmentService.getDepartmentIdByNameAndLevel("A_user", "内审会", "公司");
        Assert.assertTrue(CollectionUtils.isEmpty(names));
    }

    @Test
    public void get_departmentId_by_name_and_level_case7() {
        List<String> names = departmentService.getDepartmentIdByNameAndLevel("B_user", "内审会", "部门");
        Assert.assertTrue(CollectionUtils.isEmpty(names));
    }

    @Test
    public void get_departmentName_by_userMame() {
        String departmentName = departmentService.getUserDepartmentByUserName("A_user");
        Assert.assertNotEquals("", departmentName);

    }
}