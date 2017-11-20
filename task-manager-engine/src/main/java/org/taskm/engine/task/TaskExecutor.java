package org.taskm.engine.task;

import org.taskm.engine.EngineException;
import org.taskm.core.task.Task;
import org.taskm.core.task.TaskStatus;
import org.taskm.engine.utils.NotificationClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Created by Vlad Munteanu on 8/26/2016.
 */
class TaskExecutor implements Callable<Void> {

    private final Task task;

    TaskExecutor(Task task){
        this.task = task;
    }

    @Override
    public Void call() throws EngineException {

        Method method;

        if (task.getParameters() == null || task.getParameters().isEmpty()){
            try {
                method = task.getObject().getClass().getMethod(task.getMethodName());

                long startTime = System.currentTimeMillis();

                task.setStatus(TaskStatus.Running);
                task.setLastFireTime(new Date(startTime));
                task.setExecutionTime(null);
                task.setErrorMessage(null);

                method.invoke(task.getObject());

                long endTime = System.currentTimeMillis();

                task.setExecutionTime(extractExecutionTime(endTime - startTime));
                task.setStatus(TaskStatus.Executed);
            } catch (NoSuchMethodException e) {
                task.setErrorMessage(e.getCause().getMessage());
                task.setStatus(TaskStatus.Failed);
                throw new EngineException(String.format("Could not find '%s'.", task),e);
            } catch (InvocationTargetException | IllegalAccessException e) {
                task.setErrorMessage(e.getCause().getMessage());
                task.setStatus(TaskStatus.Failed);
                throw new EngineException(String.format("Could not execute '%s'.", task),e);
            }
        }else {
            method = getMethod(task);
            if (method != null) {
                try {

                    long startTime = System.currentTimeMillis();
                    task.setStatus(TaskStatus.Running);
                    task.setLastFireTime(new Date(startTime));
                    task.setExecutionTime(null);
                    task.setErrorMessage(null);

                    method.invoke(task.getObject(),task.getParameters().toArray());

                    long endTime = System.currentTimeMillis();
                    task.setExecutionTime(extractExecutionTime(endTime - startTime));
                    task.setStatus(TaskStatus.Executed);

                } catch (InvocationTargetException | IllegalAccessException e) {
                    task.setErrorMessage(e.getCause().getMessage());
                    task.setStatus(TaskStatus.Failed);
                    throw new EngineException(String.format("Could not execute '%s'.", task),e);
                }
            }else {
                task.setErrorMessage(String.format("Could not find '%s'.", task));
                task.setStatus(TaskStatus.Failed);
                throw new EngineException(String.format("Could not find '%s'.", task));
            }
        }
        return null;
    }

    private static Method getMethod(Task task) {

        Method[] methods = task.getObject().getClass().getMethods();

        for (Method method : methods) {
            if (Objects.equals(method.getName(), task.getMethodName()) && method.getParameters().length == task.getParameters().size()) {
                Parameter[] parameters = method.getParameters();
                Object[] objectParameters = task.getParameters().toArray();
                int check = 0;
                int i = 0;
                while (i < objectParameters.length) {
                    if (Objects.equals((getParameterType(parameters[i])), objectParameters[i].getClass().getTypeName())) {
                        check++;
                    }
                    i++;
                }

                if (check == objectParameters.length) {
                    return method;
                }
            }
        }
        return null;
    }

    private static String getParameterType(Parameter parameter){

        switch (parameter.getType().getName()){
            case "byte":
                return "java.lang.Byte";
            case "int":
                return "java.lang.Integer";
            case "short":
                return "java.lang.Short";
            case "char":
                return "java.lang.Character";
            case "double":
                return "java.lang.Double";
            case "long":
                return "java.lang.Long";
            case "float":
                return "java.lang.Float";
            case "boolean":
                return "java.lang.Boolean";
            default:
                return parameter.getType().getTypeName();
        }

    }

    private static String extractExecutionTime(long milliseconds){

        String format = String.format("%%0%dd", 2);
        long elapsedTime = milliseconds / 1000;
        String seconds = String.format(format, elapsedTime % 60);
        String minutes = String.format(format, (elapsedTime % 3600) / 60);
        String hours = String.format(format, elapsedTime / 3600);

        String stringRep = String.format("%.3f",(double) milliseconds / 1000);

        String milli = stringRep.substring(stringRep.lastIndexOf("."),stringRep.lastIndexOf(".")+4);

        return hours + ":" + minutes + ":" + seconds + milli;

    }

}
