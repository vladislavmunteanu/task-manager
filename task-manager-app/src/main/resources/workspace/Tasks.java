
import org.apache.log4j.Logger;
import java.util.Date;


public class Tasks{

    private static final Logger Log = Logger.getLogger(Tasks.class);

    public void task_1(){
        Log.info("task_1 successfully finished");
    }

    public void task_2(String message){
        Log.info(String.format("The string is : %s",message));
    }

    public void task_3(int number){
        Log.info(String.format("The number is : %d",number));
    }

    public void task_4(double number){
        Log.info(String.format("The number is : %f",number));
    }

    public void task_5(Date date){
        Log.info(String.format("The date is : %s",date));
    }

    public void task_6(boolean value){
        Log.info(String.format("The boolean is : %s",value));
    }

}
