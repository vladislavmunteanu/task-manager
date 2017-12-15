package org.taskm.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;

/**
 * Created by diancrud on 2/8/2017.
 */
@Controller
public class AppController {

    @RequestMapping(value = "/header",method = RequestMethod.GET)
    public String header(Model model) {
        model.addAttribute("now", LocalDateTime.now());
        return "fragments/header";
    }

    @RequestMapping(value = "/time",method = RequestMethod.GET)
    public String time(Model model) {
        model.addAttribute("now", LocalDateTime.now());
        return "fragments/time";
    }


    @RequestMapping(value = "/help",method = RequestMethod.GET)
    public String help() {
        return "fragments/help";
    }

}
