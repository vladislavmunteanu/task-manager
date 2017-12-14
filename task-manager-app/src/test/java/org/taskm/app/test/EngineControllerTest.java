package org.taskm.app.test;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.taskm.app.controller.EngineController;
import org.taskm.core.task.Task;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;



@RunWith(SpringRunner.class)
@WebMvcTest(EngineController.class)
public class EngineControllerTest {

    private static Map<String,Object> modelResult;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EngineController controller;

    @Test
    public void getTaskByName() {

        try {
            modelResult = this.mockMvc.perform(get("/groups/Group_1/task_test_2")).andReturn().getModelAndView().getModel();
            Task task = (Task) modelResult.get("task");
            Assert.assertEquals("task_test_2",task.getMethodName());
            Assert.assertEquals("{String=Got the message}",modelResult.get("taskParameters").toString());

        } catch (Exception e) {
            Assert.fail(String.format("No error expected '%s'",e.getMessage()));
        }

    }

    @Test
    public void tasks() {
    }

    @Test
    public void taskDetails() {
    }

    @Test
    public void groupDetails() {
    }

    @Test
    public void getGroups() {
    }

    @Test
    public void index() {

        try {
            modelResult = this.mockMvc.perform(get("/")).andReturn().getModelAndView().getModel();
            Assert.assertEquals(2,modelResult.get("groups_size"));
            Assert.assertEquals(6,modelResult.get("tasks_size"));
            Assert.assertEquals("Executions : 0 Failures : 0",modelResult.get("systemHistory").toString());
        } catch (Exception e) {
            Assert.fail(String.format("No error expected '%s'",e.getMessage()));
        }

    }

}
