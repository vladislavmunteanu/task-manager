package org.taskm.core.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 1/26/2017.
 */
public class Task {

    private final Object object;
    private final String methodName;
    private List<Object> parameters;
    private TaskStatus status;

    public Task(Object object, String methodName){
        this.object = object;
        this.methodName = methodName;
        this.parameters = new ArrayList<>();
        this.status = TaskStatus.PENDING;
    }

    public Task(Object object, String methodName, List<Object> parameters) throws TaskCoreException {
        this.object = object;
        this.methodName = methodName;
        this.status = TaskStatus.PENDING;
        if(!(parameters == null)){
            this.parameters = new ArrayList<>();
            for (Object parameter : parameters){
                if (!(parameter == null)){
                    this.parameters.add(parameter);
                }else {
                    throw new TaskCoreException("Failed to build task.", new Throwable(String.format("Method %s has null parameter provided",methodName)));
                }
            }
        }else {
            throw new TaskCoreException("Failed to build task.", new Throwable(String.format("Method %s has null parameter provided",methodName)));
        }
    }

    public Object getObject() {
        return object;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters){
        this.parameters = parameters;
    }

    public void addParameter(Object parameter) throws TaskCoreException {

        if (!(parameter == null)){
            this.getParameters().add(parameter);
        }else {
            throw new TaskCoreException("Failed to add parameter.", new Throwable(String.format("Method %s has null parameter provided",methodName)));
        }

    }

    public String toString(){

        return String.format("%s : %s",this.getMethodName(),this.getStatus());

    }

    private TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int hashCode(){
        String hashCode = this.getMethodName()+this.getParameters();
        return hashCode.hashCode();
    }


}
