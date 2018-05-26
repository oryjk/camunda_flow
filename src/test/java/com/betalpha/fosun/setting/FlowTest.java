package com.betalpha.fosun.setting;

import com.betalpha.fosun.BackendFosunApplication;
import com.betalpha.fosun.api.Flow;
import com.betalpha.fosun.setting.rest.FlowController;
import com.google.common.collect.Lists;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BackendFosunApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Ignore
public class FlowTest {

    private MockMvc mockMvc;


    @Autowired
    private FlowController flowController;


    @Mock
    private RepositoryService repositoryService = mock(RepositoryService.class);
    private ProcessDefinitionQuery processDefinitionQuery = mock(ProcessDefinitionQuery.class);
    ProcessDefinitionEntity processDefinitionEntity = new ProcessDefinitionEntity();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);


        processDefinitionEntity.setId("xxx_id");
        processDefinitionEntity.setName("xxx_name");
        processDefinitionEntity.setKey("xxx_key");

        Mockito.when(processDefinitionQuery.list()).thenReturn(Lists.newArrayList(processDefinitionEntity));
        Mockito.when(repositoryService.createProcessDefinitionQuery()).thenReturn(processDefinitionQuery);
        flowController.setRepositoryService(repositoryService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(flowController).build();
    }

    @Test
    public void set_test() throws Exception {
        flowController.updateFlowId(new Flow("123456", null, true));


        mockMvc.perform(post("/api/setting/flow")
                .contentType(MediaType.APPLICATION_JSON)
                .content("123456")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    public void get_test() throws Exception {

        mockMvc.perform(get("/api/setting/flow"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());
    }
}
