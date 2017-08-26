package org.taskm.app.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.taskm.core.task.TaskGroup;
import org.taskm.engine.Engine;
import org.taskm.engine.EngineException;

import java.util.List;
import java.util.Objects;

/**
 * Created on 1/27/2017.
 */
@Controller
public class EngineController {

    private final Engine engine;
    private static final Logger Log = Logger.getLogger(EngineController.class);
    private List<TaskGroup> groups;

    @Autowired
    public EngineController(Engine engine) {
        this.engine = engine;
        try {
            this.engine.startEngine();
        } catch (EngineException e) {
            Log.error(e);
        }
        this.groups = this.engine.getTaskGroups();

    }

    @RequestMapping("/tasks")
    public String tasks(Model model) {
        model.addAttribute("groups",groups);
        return "fragments/tasks";

    }

    @RequestMapping(value = "/groups/{name}", method = RequestMethod.GET)
    public String getGroup(Model model, @PathVariable("name") String name) {

        model.addAttribute("groups",groups);

        if (Objects.equals(name, "_main")){
            model.addAttribute("group_name", groups.get(0).getName());
            model.addAttribute("selected_group",groups.get(0));
        }
        else {
            model.addAttribute("group_name", groups.get(engine.getEngineConf().getTaskGroupIndex(name)).getName());
            model.addAttribute("selected_group",groups.get(engine.getEngineConf().getTaskGroupIndex(name)));
        }

        return "fragments/groups";
    }




}


