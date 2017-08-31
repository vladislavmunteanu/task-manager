package org.taskm.engine.task;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.taskm.core.task.Task;
import org.taskm.core.task.TaskCoreException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 1/27/2017.
 */
public class TaskRunnerTest{

    private static final Logger LOG = Logger.getLogger(TaskRunnerTest.class);

    private static List<Task> taskList;

    private static Task testTwoParameters;

    private static TaskRunner runner;

    private static Task testInt;

    private static Task doError;

    private static Task testByte;

    private static Task testShort;

    private static TaskTemplate taskTemplate;



    @BeforeClass
    public static void setUp() throws TaskRunnerException {

        taskTemplate = new TaskTemplate();
        runner = new TaskRunner();

        List<Object> testIntParameters = new ArrayList<>();
        int testIntParameter = 3;
        testIntParameters.add(testIntParameter);
        testInt = new Task(taskTemplate,"testInt");
        testInt.setParameters(testIntParameters);

        List<Object> testByteParameters = new ArrayList<>();
        byte testByteParameter = 3;
        testByteParameters.add(testByteParameter);
        testByte = new Task(taskTemplate,"testByte");
        testByte.setParameters(testByteParameters);


        List<Object> testShortParameters = new ArrayList<>();
        short testShortParameter = 3;
        testShortParameters.add(testShortParameter);
        testShort = new Task(taskTemplate,"testShort");
        testShort.setParameters(testShortParameters);
        doError = new Task(taskTemplate,"doError");

    }



    @Test
    public void test_All_Data_Types() throws TaskRunnerException, InterruptedException {


        taskList = new ArrayList<>();
        taskList.add(testInt);
        taskList.add(testByte);
        taskList.add(testShort);

        long startTime = System.currentTimeMillis();

        runner.runParallelTasks(taskList);

        long endTime = System.currentTimeMillis();
        double duration = (endTime - startTime) / 1000;
        LOG.info("Done in " + duration + " seconds");

        long startTime1 = System.currentTimeMillis();

        taskTemplate.testInt(3);
        taskTemplate.testByte((byte) 3);
        taskTemplate.testShort((short) 3);

        long endTime1 = System.currentTimeMillis();
        double duration1 = (endTime1 - startTime1) / 1000;

        LOG.info("Done in " + duration1 + " seconds");


        assert duration < duration1;

    }

    @Test
    public void test_Method_No_Parameters() throws TaskRunnerException {

        taskList = new ArrayList<>();
        taskList.add(new Task(taskTemplate, "testNoParameter"));
        runner.runParallelTasks(taskList);

    }

    @Test
    public void test_Method_One_Parameter() throws TaskRunnerException, TaskCoreException {

        List<Object> parameters = new ArrayList<>();
        parameters.add("TESTING");
        taskList = new ArrayList<>();
        taskList.add(new Task(taskTemplate, "testOneParameter", parameters));
        runner.runParallelTasks(taskList);

    }

    @Test
    public void test_Method_Mixed_Parameters() throws TaskRunnerException, InterruptedException, TaskCoreException {

        List<Object> parameters = new ArrayList<>();
        parameters.add("TESTING");
        taskList = new ArrayList<>();
        taskList.add(new Task(taskTemplate, "testOneParameter", parameters));
        taskList.add(new Task(taskTemplate, "testNoParameter"));

        runner.runParallelTasks(taskList);

    }

    @Test
    public void test_TheSame_Method_Different_Parameters() throws TaskRunnerException, TaskCoreException {

        String parameterString = "Passed";
        int parameterInt = 1;
        List<Object> parametersString = new ArrayList<>();
        parametersString.add(parameterString);
        List<Object> parametersInt = new ArrayList<>();
        parametersInt.add(parameterInt);

        taskList = new ArrayList<>();
        taskList.add(new Task(taskTemplate, "theSame", parametersString));

        taskList.add(new Task(taskTemplate, "theSame", parametersInt));

        runner.runParallelTasks(taskList);
    }

    @Test
    public void test_TwoParametersProvided() throws TaskRunnerException, TaskCoreException {


        testTwoParameters = new Task(taskTemplate,"testTwoParameters");
        testTwoParameters.addParameter("message 0");
        testTwoParameters.addParameter("message 1");
        taskList = new ArrayList<>();
        taskList.add(testTwoParameters);
        runner.runParallelTasks(taskList);


    }

    @Test
    public void test_TwoParametersOneProvided(){

        try {
            testTwoParameters = new Task(taskTemplate,"testTwoParameters");
            testTwoParameters.addParameter("message 0");
            testTwoParameters.addParameter(null);
            taskList = new ArrayList<>();
            taskList.add(testTwoParameters);
            runner.runParallelTasks(taskList);
            Assert.fail("Exception expected");
        } catch (TaskRunnerException | TaskCoreException e) {
            LOG.info(e.getMessage()+" is expected");
        }
    }

    @Test
    public void test_DoError(){

        try {
            runner.runTask(doError);
            Assert.fail("Exception expected");
        } catch (TaskRunnerException taskManagerExceptions) {
            LOG.info(taskManagerExceptions.getMessage()+" is expected");
        }

    }


}
