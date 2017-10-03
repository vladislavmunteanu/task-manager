
import org.apache.log4j.Logger

class Script {

    private static final Logger Log = Logger.getLogger(Script.class)

    void task_1(){
        Log.info 'Groovy: task_1 successfully finished'
        sleep(80000)

    }

    void task_2(String message){
        Log.info "Groovy: The string is : ${message}"
        throw new RuntimeException("Failed to start task group 'Group_1': org.taskm.engine.EngineException: Could not execute 'task_2 : [Got the message]'.")
    }

    void task_3(int number){
        Log.info "Groovy: The number is : ${number}"
        sleep(100)
    }

    void task_4(double number){
        Log.info "Groovy: The number is : ${number}"
    }

    void task_5(Date date){
        Log.info "Groovy: The date is : ${date}"
    }

    void task_6(boolean value){
        Log.info "Groovy: The boolean is : ${value}"
    }


}