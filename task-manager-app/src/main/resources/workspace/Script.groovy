
import org.apache.log4j.Logger

class Script {

    private static final Logger Log = Logger.getLogger(Script.class)

    void clear_Logs(){
        Log.info 'Groovy: task_1 successfully finished'
        sleep(80000)

    }

    void clear_Folders(String message){
        Log.info "Groovy: The string is : ${message}"
        sleep(200)
        throw new RuntimeException("Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception: Fake exception")
    }

    void clean_DataBase(int number){
        Log.info "Groovy: The number is : ${number}"
        sleep(100)
    }

    void copy_Package(boolean bool, double number){
        sleep(300)
        Log.info "Groovy: The bool is : ${number}"
        Log.info "Groovy: The number is : ${number}"
    }

    void install_DataBase(Date date){
        sleep(400)
        Log.info "Groovy: The date is : ${date}"
    }

    void deploy_Library(boolean value){
        sleep(500)
        Log.info "Groovy: The boolean is : ${value}"
    }


}