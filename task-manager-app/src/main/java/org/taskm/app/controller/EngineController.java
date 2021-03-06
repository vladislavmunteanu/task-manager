package org.taskm.app.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.taskm.app.AppUtils;
import org.taskm.core.task.Task;
import org.taskm.core.task.TaskGroup;
import org.taskm.core.task.TaskMap;
import org.taskm.engine.Engine;
import org.taskm.engine.EngineException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created on 1/27/2017.
 */
@Controller
public class EngineController {

    private final Engine engine;
    private static final Logger Log = Logger.getLogger(EngineController.class);
    private List<TaskGroup> groups;
    private Map<String,TaskMap> tasksMap;

    @Autowired
    public EngineController(Engine engine) {
        this.engine = engine;
        try {
            this.engine.startEngine();
        } catch (EngineException e) {
            Log.error("Failed to start Engine.", e);
            throw new RuntimeException("Failed to start Engine.", e);
        }
        this.groups = this.engine.getEngineConf().getTaskGroupList();
        this.tasksMap = this.engine.getEngineConf().getTasksMap();
    }

    @RequestMapping(value = "/groups/{group_name}/{task_name}", method = RequestMethod.GET)
    public String getTaskByName(Model model, @PathVariable("group_name") String group_name, @PathVariable("task_name") String task_name) {


        Task task = groups.get(engine.getEngineConf().getTaskGroupIndex(group_name)).getTask(task_name);

        model.addAttribute("task", task);

        Map<String, String> taskParameters = AppUtils.extractTaskParameters(task);

        model.addAttribute("taskParameters", taskParameters);

        return "fragments/group-task-details";
    }

    @RequestMapping(value = "/tasks",method = RequestMethod.GET)
    public String getTasks(Model model) {

        model.addAttribute("tasksMap", tasksMap);
        model.addAttribute("first_map",tasksMap.get(tasksMap.keySet().toArray()[0].toString()));
        model.addAttribute("first_map_name", tasksMap.get(tasksMap.keySet().toArray()[0].toString()).getTask().getMethodName()+"-"
                +tasksMap.get(tasksMap.keySet().toArray()[0].toString()).getTaskGroup().getName());

        Map<String, String> taskParameters = AppUtils.extractTaskParameters(tasksMap.get(tasksMap.keySet().toArray()[0].toString()).getTask());

        model.addAttribute("taskParameters", taskParameters);

        return "fragments/tasks";
    }

    @RequestMapping(value ="/tasks/{task_map}",method = RequestMethod.GET)
    public String taskDetails(Model model, @PathVariable("task_map") String task_map) {

        model.addAttribute("taskMap", tasksMap.get(task_map));

        Map<String, String> taskParameters = AppUtils.extractTaskParameters(tasksMap.get(task_map).getTask());

        model.addAttribute("taskParameters", taskParameters);

        return "fragments/task-details";
    }

    @RequestMapping(value ="/groups/{group_name}",method = RequestMethod.GET)
    public String groupDetails(Model model, @PathVariable("group_name") String group_name) {

        model.addAttribute("group_name", groups.get(engine.getEngineConf().getTaskGroupIndex(group_name)).getName());
        model.addAttribute("selected_group", groups.get(engine.getEngineConf().getTaskGroupIndex(group_name)));

        return "fragments/group-details";
    }

    @RequestMapping(value ="/groups",method = RequestMethod.GET)
    public String getGroups(Model model) {

        model.addAttribute("groups", groups);
        model.addAttribute("first_group",groups.get(0));
        model.addAttribute("first_group_name",groups.get(0).getName());

        return "fragments/groups";
    }

    @RequestMapping(value ="/",method = RequestMethod.GET)
    public String index(Model model) {

        model.addAttribute("groups_size",groups.size());
        model.addAttribute("tasks_size",tasksMap.size());
        model.addAttribute("systemHistory",this.engine.getSystemHistory());
        return "index";
    }

    //***** System History *****//

    @RequestMapping(value ="/failures",method = RequestMethod.GET)
    @ResponseBody
    public String getFailures(){
        return Integer.toString(this.engine.getSystemHistory().getFailures());
    }

    @RequestMapping(value ="/executions",method = RequestMethod.GET)
    @ResponseBody
    public String getExecutions(){
        return Integer.toString(this.engine.getSystemHistory().getExecutions());
    }

    @RequestMapping(value ="/last-executed-group",method = RequestMethod.GET)
    @ResponseBody
    public String getLastExecutedGroup(){
        return this.engine.getSystemHistory().getLastExecutedGroup();
    }

    @RequestMapping(value ="/last-executed-task",method = RequestMethod.GET)
    @ResponseBody
    public String getLastExecutedTask(){
        return this.engine.getSystemHistory().getLastExecutedTask();
    }

    @RequestMapping(value ="/last-execution-time",method = RequestMethod.GET)
    @ResponseBody
    public String getLastExecutionTime(){
        return this.engine.getSystemHistory().getLastExecutionTime();
    }

    @RequestMapping(value ="/last-failure-time",method = RequestMethod.GET)
    @ResponseBody
    public String getLastFailureTime(){
        return this.engine.getSystemHistory().getLastExecutionTime();
    }
}


