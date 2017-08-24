Task Manager Version 1.0

Description
-----------
The tool is used to manage and run tasks which are written in Java and Groovy.

NOTE
-----------
There are two ways to run this tool:

    1.Using non-compiled class which should be located in task-manager/workspace/${Task_Class}.${language}
    2.Using compiled class which should be located in task-manager/lib/${Class_Jar}.jar

STEPS(1)
-----------
1.Configure properties in properties/task-manager.properties

    Set:
        task.manager.type = ${language}
        task.manager.xml = ${TaskManager}.xml
        task.manager.compiled = false
        task.manager.class = ${Task_Class}.${language}

    E.g. (Groovy):
        task.manager.type = groovy
        task.manager.xml = SampleTaskManager.xml
        task.manager.compiled = false
        task.manager.class = SampleScript.groovy

    E.g. (Java):
        task.manager.type = java
        task.manager.xml = SampleTaskManager.xml
        task.manager.compiled = false
        task.manager.class = SampleTasks.java

2.Configure Task Manager XML file in task-manager/${TaskManager}.xml

    E.g.:
        Check SampleTaskManager.xml in task-manager/SampleTaskManager.xml
        Update log4j.properties set line log4j.logger.${Task_Class}=INFO, TASK

    E.g.:
        log4j.logger.SampleTasks=INFO, TASK

3.Run task-manager.bat

STEPS(2)
-----------
1.Configure properties in properties/task-manager.properties

    Set:
        task.manager.xml = ${TaskManager}.xml
        task.manager.compiled = true
        task.manager.class = ${class_package}.${Task_Class}

    E.g.:
        task.manager.xml = SampleTaskManager.xml
        task.manager.compiled = false
        task.manager.class = com.my.tasks.MyTasks

2.Configure Task Manager XML file in task-manager/${TaskManager}.xml

    E.g.:
       Check SampleTaskManager.xml in task-manager/SampleTaskManager.xml
       Copy class jar to task-manager/lib/

3.Update log4j.properties by uncommenting line {#log4j.logger.class_package=INFO, TASK} and
comment line {log4j.logger.SampleTasks=INFO, TASK}

    E.g.:
       log4j.logger.com.my.tasks=INFO, TASK

4.Run task-manager.bat

(C) Vlad Munteanu : munteanu.vladislav89@gmail.com
