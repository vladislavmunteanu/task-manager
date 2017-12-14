

import org.apache.log4j.Logger

class ScriptTest {

    private static final Logger Log = Logger.getLogger(Script.class)

    void task_test_1(){
        Log.info 'Groovy: task_1 successfully finished'
        sleep(80000)

    }

    void task_test_2(String message){
        Log.info "Groovy: The string is : ${message}"
        sleep(200)
        throw new RuntimeException("Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception")
    }

    void task_test_3(int number){
        Log.info "Groovy: The number is : ${number}"
        sleep(100)
    }

    void task_test_4(double number){
        sleep(300)
        Log.info "Groovy: The number is : ${number}"
    }

    void task_test_5(Date date){
        sleep(400)
        Log.info "Groovy: The date is : ${date}"
    }

    void task_test_6(boolean value){
        sleep(500)
        Log.info "Groovy: The boolean is : ${value}"
    }


}