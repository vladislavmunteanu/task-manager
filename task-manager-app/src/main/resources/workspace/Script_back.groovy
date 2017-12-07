
import org.apache.log4j.Logger

class Script_back {

    private static final Logger Log = Logger.getLogger(Script.class)

    void task_1(){
        Log.info 'Groovy: task_1 successfully finished'
        sleep(80000)

    }

    void task_2(String message){
        Log.info "Groovy: The string is : ${message}"
        sleep(200)
        throw new RuntimeException("Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception")
    }

    void task_3(int number){
        Log.info "Groovy: The number is : ${number}"
        sleep(100)
    }

    void task_4(double number){
        sleep(300)
        Log.info "Groovy: The number is : ${number}"
    }

    void task_5(Date date){
        sleep(400)
        Log.info "Groovy: The date is : ${date}"
    }

    void task_6(boolean value){
        sleep(500)
        Log.info "Groovy: The boolean is : ${value}"
    }


}