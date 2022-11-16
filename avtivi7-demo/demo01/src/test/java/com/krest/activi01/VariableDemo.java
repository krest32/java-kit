package com.krest.activi01;

import com.krest.activi01.domain.Holiday;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class VariableDemo {

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
                .addClasspathResource("bpmn/holiday-global.bpmn20.xml")
                .name("出差流程-变量")
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

        // 配置参数
        Map<String, Object> params = new HashMap<String, Object>();
        Holiday holiday = new Holiday();
        holiday.setNum(3);

        // 通过参数发送，确认审批人
        params.put("assignee0", "李老师");
        params.put("assignee1", "王老师");
        params.put("assignee2", "杜老师");
        params.put("holiday", holiday);
        // 4. 流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holiday-global", params);

        // 任务 id
        System.out.println(processInstance.getId());

        System.out.println(processInstance.getActivityId());
        // 部署实例 id
        System.out.println(processInstance.getProcessDefinitionId());

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
                .processDefinitionKey("holiday-global")
                .taskAssignee("杜老师")
                .singleResult();

        // 完成任务
        taskService.complete(task.getId());

        // 完成任务的同时设置参数
//        Map<String, Object> params = new HashMap<String, Object>();
//        Holiday holiday = new Holiday();
//        holiday.setNum(2);
//        taskService.complete(task.getId(), params);
    }

}
