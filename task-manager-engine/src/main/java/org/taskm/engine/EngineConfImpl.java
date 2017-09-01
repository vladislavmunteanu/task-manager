package org.taskm.engine;

import groovy.lang.GroovyClassLoader;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.taskm.core.task.Task;
import org.taskm.core.task.TaskCoreException;
import org.taskm.core.task.TaskGroup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created on 1/26/2017.
 */
@Component
public class EngineConfImpl implements EngineConf {

    private String type;
    private String taskXml;
    private Boolean compiled;
    private String taskClass;
    private Object classInstance;
    private List<TaskGroup> taskGroupList; //TODO find a way to not keep objects in memory "org.mapdb"
    private static HashMap<String,Integer> tgIndexMap;
    private Properties properties;


    private static final Logger Log = Logger.getLogger(EngineConfImpl.class);

    public EngineConfImpl() throws EngineException {

        this.properties = loadProperties();
        this.compiled = Boolean.valueOf(this.getProperties().getProperty("task.manager.compiled"));
        if (!this.isCompiled()) {
            this.type = this.getProperties().getProperty("task.manager.type");
        }
        this.taskXml = this.getProperties().getProperty("task.manager.xml");

        validateAgainstXSD(Thread.currentThread().getContextClassLoader().getResourceAsStream(this.getTaskXml()),
                Thread.currentThread().getContextClassLoader().getResourceAsStream("TaskManager.xsd"));

        Log.info(String.format("Found valid Task Manager XML '%s'",this.getTaskXml()));
        this.taskClass = this.getProperties().getProperty("task.manager.class");
        this.classInstance = buildClassInstance(this.isCompiled(),this.getTaskClass(),this.getType());
        this.taskGroupList = buildTaskGroupList(this.getTaskXml(),this.getClassInstance());
    }

    @Override
    public List<TaskGroup> getTaskGroupList() {
        return taskGroupList;
    }

    @Override
    public Object getClassInstance() {
        return classInstance;
    }

    @Override
    public String getTaskXml() {
        return taskXml;
    }

    @Override
    public String getTaskClass() {
        return taskClass;
    }

    public String getType(){
        return type;
    }

    @Override
    public Integer getTaskGroupIndex(String tgName) {
        return tgIndexMap.get(tgName);
    }

    @Override
    public Properties getProperties(){
        return properties;
    }

    private static Properties loadProperties() throws EngineException {

        Properties properties = new Properties();
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("task-manager.properties");
            if (inputStream == null){
                throw new FileNotFoundException("Failed to load 'task-manager.properties'");
            }else {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            throw new EngineException("Failed to load properties" ,e);
        }
        return properties;
    }



    private Boolean isCompiled() {
        return compiled;
    }

    private static void validateAgainstXSD(InputStream xml, InputStream xsd) throws EngineException {

        Log.info("Verifying Task Manager XML");
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = factory.newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));
        } catch (SAXException | IOException e) {
            throw new EngineException("Failed to validate Task Manager XML file", e);
        }
    }

    private static List<TaskGroup> buildTaskGroupList(String taskXML,Object classInstance) throws EngineException{

        Log.info("Starting to build Task Groups");
        List<TaskGroup> taskGroupList = new ArrayList<>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream(taskXML));
            doc.getDocumentElement().normalize();
            NodeList groupList = doc.getElementsByTagName("TaskGroup");
            Set<String> taskGroupChecker = new HashSet<>();
            tgIndexMap= new HashMap<>();

            for (int i = 0; i < groupList.getLength();i++ ) {
                Element groupElement = (Element) groupList.item(i);
                String taskGroupName = groupElement.getAttribute("name");
                if (taskGroupChecker.contains(taskGroupName)) {
                    throw new EngineException(String.format("Task Group name '%s' is not unique.", taskGroupName));
                } else {
                    taskGroupChecker.add(taskGroupName);
                    tgIndexMap.put(taskGroupName,i);
                    boolean parallel = Boolean.parseBoolean(groupElement.getAttribute("parallel"));
                    String scheduler = groupElement.getAttribute("scheduler");
                    TaskGroup taskGroup = new TaskGroup(taskGroupName, parallel,scheduler);
                    NodeList taskList = groupElement.getElementsByTagName("Task");

                    for (int j = 0; j < taskList.getLength(); j++) {

                        Element taskElement = (Element) taskList.item(j);
                        String taskName = taskElement.getElementsByTagName("Name").item(0).getFirstChild().getNodeValue();

                        Task task = new Task(classInstance, taskName);
                        NodeList parameters = taskElement.getElementsByTagName("Parameter");

                        if (parameters.getLength() > 0) {
                            for (int k = 0; k < parameters.getLength(); k++) {
                                Element parameterElement = (Element) parameters.item(k);

                                if (Objects.equals(parameterElement.getAttribute("type"), "String")) {
                                    task.addParameter(parameterElement.getFirstChild().getNodeValue());
                                }
                                else if (Objects.equals(parameterElement.getAttribute("type"), "Number")) {
                                    task.addParameter(Integer.parseInt(parameterElement.getFirstChild().getNodeValue()));
                                }
                                else if (Objects.equals(parameterElement.getAttribute("type"), "Double")) {
                                    task.addParameter(Double.parseDouble(parameterElement.getFirstChild().getNodeValue()));
                                }
                                else if (Objects.equals(parameterElement.getAttribute("type"), "Boolean")) {
                                    task.addParameter(Boolean.valueOf(parameterElement.getFirstChild().getNodeValue()));
                                }
                                else if (Objects.equals(parameterElement.getAttribute("type"), "Date")) {
                                    task.addParameter(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(parameterElement.getFirstChild().getNodeValue()));
                                }else {
                                    throw new EngineException(String.format("Unknown parameter type '%s'",parameterElement.getAttribute("type")));
                                }

                            }
                            taskGroup.addTask(task);
                        } else {
                            taskGroup.addTask(task);
                        }
                    }
                    taskGroupList.add(taskGroup);
                    Log.info(String.format("Built %s", taskGroup));
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException | ParseException | TaskCoreException e) {
            throw new EngineException("Failed to build Task Group.",e);
        }

        Log.info(String.format("Successfully finished to build Task Groups, found %d groups",taskGroupList.size()));
        return taskGroupList;
    }

    private static Object buildClassInstance(boolean compiled,String className,String type) throws EngineException {
        File root;
        Class<?> clazz;
        Object instance;

        if (!compiled){
            Log.info(String.format("Starting to compile '%s'",className));
            URL url = Thread.currentThread().getContextClassLoader().getResource("workspace");
            if (url != null){
                root = new File(url.getPath());
            }else {
                throw new EngineException("'workspace' folder doesn't exists");
            }

            if (Objects.equals(type, "java")) {
                compileJava(root, className);
                Log.info(String.format("Create instance of class '%s'",className));
                instance = createJavaInstance(root,className.substring(0,className.length()-5));
                Log.info(String.format("Successfully instantiated class '%s'",className));
            }else if (Objects.equals(type, "groovy")){
                Log.info(String.format("Create instance of class '%s'",className));
                instance = compileGroovy(root,className);
                Log.info(String.format("Successfully instantiated class '%s'",className));
            }else {
                throw new EngineException(String.format("Unknown type '%s'",type));
            }
        }else {
            try {
                Log.info(String.format("Create instance of class '%s'",className));
                clazz = Class.forName(className);
                instance = clazz.newInstance();
                Log.info(String.format("Successfully instantiated class '%s'",className));
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new EngineException(String.format("Failed to instantiate class '%s'.",className),e);
            }

        }

        return instance;
    }

    private static Object compileGroovy(File root,String className) throws EngineException {

        File tasks = new File(root,className);
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        try {
            return groovyClassLoader.parseClass(tasks).newInstance();
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            throw new EngineException(String.format("Failed to compile groovy '%s' class. '%s'",className,e));
        }

    }

    private static void compileJava(File root,String className) throws EngineException {

        File compiledClass = new File(root.getPath()+"/"+className.substring(0,className.length()-5)+".class");

        if (compiledClass.exists()){
            if (compiledClass.delete()){
                Log.info(String.format("Removed already existing compiled class '%s'",compiledClass.getName()));
            }
        }

        File tasks = new File(root,className);
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, tasks.getAbsolutePath());

        if (!compiledClass.exists()){
            throw new EngineException(String.format("Failed to compile '%s' class",className),new Throwable("Compiled file was not created"));
        }
        Log.info(String.format("'%s' successfully compiled",className));

    }

    private static Object createJavaInstance(File root, String className) throws EngineException {
        try {
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
            Class<?> clazz = classLoader.loadClass(className);
            return clazz.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | MalformedURLException e) {
            throw new EngineException(String.format("Failed to create instance of '%s' class",className));
        }
    }

    public static String getProperty(String propertyName) throws EngineException {
        return loadProperties().getProperty(propertyName);
    }
}
