package org.taskm.engine.task;

import org.taskm.core.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Vlad Munteanu on 8/26/2016.
 */

public class TaskRunner {

    private int failedTasks;
    private int executedTasks;

    /**
     * @param tasks - methods to be executed
     */
    public void runParallelTasks(List<Task> tasks) throws TaskRunnerException {

        final ExecutorService executorService = Executors.newFixedThreadPool(tasks.size());
        List<Future> taskFutures = new ArrayList<>();

        for (Task task : tasks){
            taskFutures.add(executorService.submit(new TaskExecutor(task)));
        }
        runTasksFuture(taskFutures);
        executorService.shutdown();
    }

    /**
     * @param task - methods to be executed
     */
    public void runTask(Task task) throws TaskRunnerException {

        final ExecutorService executorService = Executors.newFixedThreadPool(1);
        List<Future> taskFutures = new ArrayList<>();

        taskFutures.add(executorService.submit(new TaskExecutor(task)));

        runTasksFuture(taskFutures);
        executorService.shutdown();
    }

    private void runTasksFuture(List<Future> taskFutures) throws TaskRunnerException {
        for (Future taskFuture : taskFutures) {
            try {
                taskFuture.get();
                executedTasks++;
            } catch (InterruptedException | ExecutionException e) {
                failedTasks++;
                executedTasks++;
                throw new TaskRunnerException("Failed to run task.", e);
            }
        }
    }

    public int getFailedTasks() {
        return failedTasks;
    }

    public void setFailedTasks(int failedTasks) {
        this.failedTasks = failedTasks;
    }

    public int getExecutedTasks() {
        return executedTasks;
    }

    public void setExecutedTasks(int executedTasks) {
        this.executedTasks = executedTasks;
    }
}



