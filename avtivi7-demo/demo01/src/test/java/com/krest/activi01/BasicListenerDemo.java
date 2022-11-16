package com.krest.activi01;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class BasicListenerDemo {

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
                .addClasspathResource("bpmn/holiday-listener.bpmn20.xml")
                .name("出差流程-监听器")
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
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holiday-listener");

        // 任务 id
        System.out.println(processInstance.getId());

        System.out.println(processInstance.getActivityId());
        // 部署实例 id
        System.out.println(processInstance.getProcessDefinitionId());

    }

}
