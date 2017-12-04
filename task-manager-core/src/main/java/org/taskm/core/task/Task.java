package org.taskm.core.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on 1/26/2017.
 */
public class Task {

    private final Object object;
    private final String methodName;
    private List<Object> parameters;
    private String status;
    private String executionTime;
    private Date lastFireTime;
    private String errorMessage;
    private TaskGroup parent;

    public Task(Object object, String methodName) {
        this.object = object;
        this.methodName = methodName;
        this.parameters = new ArrayList<>();
        this.status = String.valueOf(TaskStatus.New);
    }


    public Task(Object object, String methodName,TaskGroup parent) {
        this.object = object;
        this.methodName = methodName;
        this.parameters = new ArrayList<>();
        this.status = String.valueOf(TaskStatus.New);
        this.parent = parent;
    }

    public Task(Object object, String methodName, List<Object> parameters) throws TaskCoreException {
        this.object = object;
        this.methodName = methodName;
        this.status = String.valueOf(TaskStatus.New);
        if (!(parameters == null)) {
            this.parameters = new ArrayList<>();
            for (Object parameter : parameters) {
                if (!(parameter == null)) {
                    this.parameters.add(parameter);
                } else {
                    throw new TaskCoreException("Failed to build task.", new Throwable(String.format("Method %s has null parameter provided", methodName)));
                }
            }
        } else {
            throw new TaskCoreException("Failed to build task.", new Throwable(String.format("Method %s has null parameter provided", methodName)));
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

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(Object parameter) throws TaskCoreException {

        if (!(parameter == null)) {
            this.getParameters().add(parameter);
        } else {
            throw new TaskCoreException("Failed to add parameter.", new Throwable(String.format("Method %s has null parameter provided", methodName)));
        }

    }

    public String toString() {

        return String.format("%s : %s", this.getMethodName(), this.getParameters());

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {

        this.status = String.valueOf(status);
    }

    public int hashCode() {
        String hashCode = this.getMethodName() + this.getParameters();
        return hashCode.hashCode();
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public String getFireTime() {
        if (this.getLastFireTime() == null){
            return "--:--:--";
        }
        else {
            return new SimpleDateFormat("hh:mm:ss a").format(this.getLastFireTime());
        }
    }

    public int getDays() {

        Date d1 = this.getLastFireTime();
        Date d2 = new Date();
        if (d1 == null) {
            return 0;
        } else {
            return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
        }
    }

    public Date getLastFireTime() {
        return lastFireTime;
    }

    public void setLastFireTime(Date lastFireTime) {
        this.lastFireTime = lastFireTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public TaskGroup getParent() {
        return parent;
    }

    public void setParent(TaskGroup parent) {
        this.parent = parent;
    }
}
