package org.taskm.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

/**
 * Created by diancrud on 2/8/2017.
 */
@Controller
public class AppController {

    @RequestMapping("/header")
    public String header(Model model) {
        model.addAttribute("now", LocalDateTime.now());
        return "fragments/header";
    }

    @RequestMapping("/time")
    public String time(Model model) {
        model.addAttribute("now", LocalDateTime.now());
        return "fragments/time";
    }


    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping("/help")
    public String help() {
        return "fragments/help";
    }

}
