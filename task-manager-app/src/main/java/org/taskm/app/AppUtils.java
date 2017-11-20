package org.taskm.app;

import org.taskm.core.task.Task;

import java.util.HashMap;
import java.util.Map;

public class AppUtils {

    public static Map<String,String> extractTaskParameters(Task task){

        Map<String,String> map = new HashMap<>();

        for (Object parameter : task.getParameters()){
            map.put(parameter.getClass().getName().substring(10),parameter.toString());

        }

        return map;
    }

}
