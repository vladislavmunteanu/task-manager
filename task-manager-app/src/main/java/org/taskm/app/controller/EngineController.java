package org.taskm.app.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
            Log.error(e);
        }
        this.groups = this.engine.getEngineConf().getTaskGroupList();
        this.tasksMap = this.engine.getEngineConf().getTasksMap();
    }

    @RequestMapping(value = "/groups/{name}", method = RequestMethod.GET)
    public String getGroup(Model model, @PathVariable("name") String name) {

        model.addAttribute("groups", groups);

        if (Objects.equals(name, "_main")) {
            model.addAttribute("group_name", groups.get(0).getName());
            model.addAttribute("selected_group", groups.get(0));
        } else {
            model.addAttribute("group_name", groups.get(engine.getEngineConf().getTaskGroupIndex(name)).getName());
            model.addAttribute("selected_group", groups.get(engine.getEngineConf().getTaskGroupIndex(name)));
        }

        return "fragments/groups";
    }

    @RequestMapping(value = "/groups/{group_name}/{task_name}", method = RequestMethod.GET)
    public String getGroup(Model model, @PathVariable("group_name") String group_name, @PathVariable("task_name") String task_name) {


        Task task = groups.get(engine.getEngineConf().getTaskGroupIndex(group_name)).getTask(task_name);

        model.addAttribute("task", task);


        Map<String, String> taskParameters = AppUtils.extractTaskParameters(task);

        model.addAttribute("taskParameters", taskParameters);

        return "fragments/group-task-details";
    }

    @RequestMapping("/tasks")
    public String talt(Model model) {

        model.addAttribute("tasksMap", tasksMap);
        model.addAttribute("first_map",tasksMap.get(tasksMap.keySet().toArray()[0]));
        model.addAttribute("first_map_name", tasksMap.get(tasksMap.keySet().toArray()[0]).getTask().getMethodName()+"-"
                +tasksMap.get(tasksMap.keySet().toArray()[0]).getTaskGroup().getName());

        return "fragments/tasks";
    }

    @RequestMapping("/tasks/{task_map}")
    public String taltDetails(Model model, @PathVariable("task_map") String task_map) {

        model.addAttribute("taskMap", tasksMap.get(task_map));

        Map<String, String> taskParameters = AppUtils.extractTaskParameters(tasksMap.get(task_map).getTask());

        model.addAttribute("taskParameters", taskParameters);

        return "fragments/task-details";
    }


}


