package org.taskm.app.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.taskm.app.Application;
import org.taskm.core.task.Task;
import org.taskm.core.task.TaskGroup;
import org.taskm.core.task.TaskMap;
import org.taskm.engine.utils.SystemHistory;

import java.util.List;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(Application.class)
public class ApplicationTest {

    private static Map<String,Object> modelResults;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void getTaskByName() {

        try {
            modelResults = this.mockMvc.perform(get("/groups/Group_1/task_test_2")).andReturn().getModelAndView().getModel();
            Task task = (Task) modelResults.get("task");
            Assert.assertEquals("task_test_2",task.getMethodName());
            Assert.assertEquals("{String=Got the message}", modelResults.get("taskParameters").toString());

        } catch (Exception e) {
            Assert.fail(String.format("No error expected '%s'",e.getMessage()));
        }

    }

    @Test
    @SuppressWarnings("unchecked")
    public void getTasks() {
        try {
            modelResults = this.mockMvc.perform(get("/tasks")).andReturn().getModelAndView().getModel();
            Map<String,TaskMap> tasksMap = (Map<String, TaskMap>) modelResults.get("tasksMap");

            for (int i = 0; i < tasksMap.size(); i++){

                int taskIndex = i+1;
                int groupIndex = 1;

                if(taskIndex > 3){
                    groupIndex = 2;
                }
                TaskMap taskMap = tasksMap.get("task_test_"+taskIndex+"-Group_"+groupIndex);

                Assert.assertEquals("task_test_"+taskIndex,taskMap.getTask().getMethodName());
                Assert.assertEquals("Group_"+groupIndex,taskMap.getTaskGroup().getName());
                Assert.assertEquals(3,taskMap.getTaskGroup().getTaskList().size());
            }

        } catch (Exception e) {
            Assert.fail(String.format("No error expected '%s'",e.getMessage()));
        }
    }

    @Test
    public void taskDetails() {

        try {
            modelResults = this.mockMvc.perform(get("/tasks/task_test_2-Group_1")).andReturn().getModelAndView().getModel();
            TaskMap taskMap = (TaskMap) modelResults.get("taskMap");

            Assert.assertEquals("task_test_2",taskMap.getTask().getMethodName());
            Assert.assertEquals("Group_1",taskMap.getTaskGroup().getName());
            Assert.assertEquals("{String=Got the message}", modelResults.get("taskParameters").toString());
        } catch (Exception e) {
            Assert.fail(String.format("No error expected '%s'",e.getMessage()));
        }
    }

    @Test
    public void groupDetails() {

        try {
            modelResults = this.mockMvc.perform(get("/groups/Group_2")).andReturn().getModelAndView().getModel();

            TaskGroup taskGroup = (TaskGroup) modelResults.get("selected_group");

            Assert.assertEquals("Group_2",modelResults.get("group_name"));
            Assert.assertEquals("Group_2",taskGroup.getName());
            Assert.assertEquals("task_test_4",taskGroup.getTaskList().get(0).getMethodName());
            Assert.assertEquals("task_test_5",taskGroup.getTaskList().get(1).getMethodName());
            Assert.assertEquals("task_test_6",taskGroup.getTaskList().get(2).getMethodName());

        } catch (Exception e) {
            Assert.fail(String.format("No error expected '%s'",e.getMessage()));
        }

    }

    @Test
    @SuppressWarnings("unchecked")
    public void getGroups() {

        try {
            modelResults = this.mockMvc.perform(get("/groups")).andReturn().getModelAndView().getModel();

            TaskGroup first_group = (TaskGroup) modelResults.get("first_group");
            List<TaskGroup> groupList = (List<TaskGroup>) modelResults.get("groups");

            Assert.assertEquals("Group_1",modelResults.get("first_group_name"));
            Assert.assertEquals("Group_1",first_group.getName());
            Assert.assertEquals("task_test_1",first_group.getTaskList().get(0).getMethodName());
            Assert.assertEquals(2, groupList.size());

        } catch (Exception e) {
            Assert.fail(String.format("No error expected '%s'",e.getMessage()));
        }
    }

    @Test
    public void index() {

        try {
            modelResults = this.mockMvc.perform(get("/")).andReturn().getModelAndView().getModel();
            Assert.assertEquals(2, modelResults.get("groups_size"));
            Assert.assertEquals(6, modelResults.get("tasks_size"));
            Assert.assertEquals(applicationContext.getBean(SystemHistory.class).toString(), modelResults.get("systemHistory").toString());
        } catch (Exception e) {
            Assert.fail(String.format("No error expected '%s'",e.getMessage()));
        }

    }

    @Test
    public void getFailures() {
        try {
            this.mockMvc.perform(get("/failures")).andExpect(content().string(Integer.toString(applicationContext.getBean(SystemHistory.class).getFailures())));
        } catch (Exception e) {
            Assert.fail(String.format("No error expected '%s'",e.getMessage()));
        }
    }

    @Test
    public void getExecutions() {
        try {
            this.mockMvc.perform(get("/executions")).andExpect(content().string(Integer.toString(applicationContext.getBean(SystemHistory.class).getExecutions())));
        } catch (Exception e) {
            Assert.fail(String.format("No error expected '%s'",e.getMessage()));
        }
    }

    @Test
    public void getLastExecutedGroup() {
        try {
            Thread.sleep(6000);
            this.mockMvc.perform(get("/last-executed-group")).andExpect(content().string(applicationContext.getBean(SystemHistory.class).getLastExecutedGroup()));
        } catch (Exception e) {
            Assert.fail(String.format("No error expected '%s'",e.getMessage()));
        }

    }

}
