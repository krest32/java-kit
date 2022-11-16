package com.krest.activi01;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.util.List;

public class BasicDemo {

    /**
     * 生成 activiti的数据库表
     */
    @Test
    public void testCreateDbTable() {
        // 默认方式
        //使用classpath下的activiti.cfg.xml中的配置创建processEngine
        // ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // System.out.println(processEngine);

        // 一般方式创建
        //先构建ProcessEngineConfiguration
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        // 通过ProcessEngineConfiguration创建ProcessEngine，此时会创建数据库
        ProcessEngine processEngine = configuration.buildProcessEngine();

    }


    /**
     * 部署流程（可以单个部署，也可以批量部署 使用 zip 进行部署）
     */
    @Test
    public void testDeploy() {
        // 1. 创建流程引擎资配置文件
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        // 2. 创建引擎
        ProcessEngine engine = configuration.buildProcessEngine();
        // 3. 拿到资源服务
        RepositoryService repositoryService = engine.getRepositoryService();
        // 4. 加载 bpmn 资源
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/holiday.bpmn20.xml")
                .name("出差流程")
                .deploy();
        // 查看部署Id
        System.out.println(deployment.getId());
        // 查看部署name
        System.out.println(deployment.getName());
    }

    /**
     * 开始流程实例
     */
    @Test
    public void startProcess() {
        // 1. 创建流程引擎资配置文件
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        // 2. 创建引擎
        ProcessEngine engine = configuration.buildProcessEngine();
        // 3. 开始流程
        RuntimeService runtimeService = engine.getRuntimeService();
        // 4. 流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holiday");

        // 任务 id
        System.out.println(processInstance.getId());

        System.out.println(processInstance.getActivityId());
        // 部署实例 id
        System.out.println(processInstance.getProcessDefinitionId());

    }

    /**
     * 任务查询
     */
    @Test
    public void findProcessInstance() {
        // 1. 创建流程引擎资配置文件
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        // 2. 创建引擎
        ProcessEngine engine = configuration.buildProcessEngine();
        // 3. 获取task服务
        TaskService taskService = engine.getTaskService();
        // 4. 创建查询 : holiday 实例，且当前分配给 王老师的任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("holiday")
                .taskAssignee("李老师")
                .list();
        for (Task task : list) {
            // 流程定义 id
            System.out.println(task.getProcessDefinitionId());
            // 获取任务 id
            System.out.println(task.getId());

            System.out.println(task.getAssignee());
            System.out.println(task.getName());
        }
    }


    /**
     * 完成个人任务
     */
    @Test
    public void completeTask() {
        // 1. 创建流程引擎资配置文件
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        // 2. 创建引擎
        ProcessEngine engine = configuration.buildProcessEngine();
        // 3. 获取task服务
        TaskService taskService = engine.getTaskService();

        // 查寻当前任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("holiday")
                .taskAssignee("杜老师")
                .singleResult();

        // 完成任务
        taskService.complete(task.getId());
    }

    /**
     * 查询任务部署信息
     */
    @Test
    public void queryProcess() {
        // 1. 创建流程引擎资配置文件
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        // 2. 创建引擎
        ProcessEngine engine = configuration.buildProcessEngine();
        // 3. 拿到资源服务
        RepositoryService repositoryService = engine.getRepositoryService();

        List<ProcessDefinition> holidays = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("holiday")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        for (ProcessDefinition holiday : holidays) {
            System.out.println(holiday.getId());
            System.out.println(holiday.getName());
            System.out.println(holiday.getVersion());
        }
    }

    /**
     * 删除流程
     */
    @Test
    public void delProcess() {
        // 1. 创建流程引擎资配置文件
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        // 2. 创建引擎
        ProcessEngine engine = configuration.buildProcessEngine();
        // 3. 拿到资源服务
        RepositoryService repositoryService = engine.getRepositoryService();
        // 根据 deploy 删除流程实例（如果有任务正在运行，无法删除）
        repositoryService.deleteDeployment("1");
        // 强制删除实例与正在运行的任务
        repositoryService.deleteDeployment("1", true);
    }

    /**
     * 资源下载：流程图资源下载
     */
    @Test
    public void downLoadBpmn() throws IOException {
        // 1. 创建流程引擎资配置文件
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        // 2. 创建引擎
        ProcessEngine engine = configuration.buildProcessEngine();

        // 3. 拿到资源服务
        RepositoryService repositoryService = engine.getRepositoryService();

        // 4. 获取相应的资源
        ProcessDefinition holiday = repositoryService.createProcessDefinitionQuery().processDefinitionKey("holiday").singleResult();
        String deploymentId = holiday.getDeploymentId();
        String resourceName = holiday.getResourceName();
        String diagramResourceName = holiday.getDiagramResourceName();
        System.out.println(deploymentId + " " + resourceName);

        // 获取输入流
        InputStream xmlFileInputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
        // InputStream pngInputStream = repositoryService.getResourceAsStream(deploymentId, diagramResourceName);

        // 输出文件
        FileOutputStream xmlFileOutPutStream = new FileOutputStream(new File("C:/Krest WorkSpace/holiday.xml"));
        IOUtils.copy(xmlFileInputStream, xmlFileOutPutStream);

        // 关闭流
        xmlFileInputStream.close();
        xmlFileOutPutStream.close();
    }

    /**
     * 任务历史信息
     */
    @Test
    public void queryHis() {
        // 1. 创建流程引擎资配置文件
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        // 2. 创建引擎
        ProcessEngine engine = configuration.buildProcessEngine();

        // 3. 得到 历史 服务
        List<HistoricActivityInstance> list = engine.getHistoryService()
                .createHistoricActivityInstanceQuery()
                .processDefinitionId("holiday:1:3")
                .orderByHistoricActivityInstanceStartTime()
                .asc().list();
        for (HistoricActivityInstance history : list) {
            System.out.println(history.getId() + " " + history.getActivityName() + " " + history.getStartTime() + " " + history.getEndTime());
        }
    }


}
