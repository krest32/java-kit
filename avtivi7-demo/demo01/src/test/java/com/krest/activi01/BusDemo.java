package com.krest.activi01;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.util.List;

public class BusDemo {
    /**
     * 关联 busid 开始流程
     */
    @Test
    public void startWithBisID() {
        // 1. 创建流程引擎资配置文件
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        // 2. 创建引擎
        ProcessEngine engine = configuration.buildProcessEngine();

        // 开始流程：传入 prodessDefineKey 和 busid
        ProcessInstance process = engine.getRuntimeService().startProcessInstanceByKey("holiday", "busid-01");
        System.out.println(process.getId() + " " + process.getProcessDefinitionId()
                + " " + process.getProcessDefinitionKey() + " " + process.getBusinessKey());
    }

    /**
     * todo 查询业务流程，先不写了
     */


    /**
     * 全部流程挂起：所有流程全部先暂停，先不进行流程审批
     */
    @Test
    public void suspendAllProcessInstance() {
        // 1. 创建流程引擎资配置文件
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        // 2. 创建引擎
        ProcessEngine engine = configuration.buildProcessEngine();

        RepositoryService repositoryService = engine.getRepositoryService();

        List<ProcessDefinition> before = repositoryService.createProcessDefinitionQuery().processDefinitionKey("holiday").list();
        for (ProcessDefinition holiday : before) {
            System.out.println(holiday.getId() + " " + holiday.isSuspended());
            if (holiday.isSuspended()) {
                // 挂起的激活
                repositoryService.activateProcessDefinitionById(holiday.getId(), true, null);
            } else {
                // 活动的停止
                repositoryService.suspendProcessDefinitionById(holiday.getId(), false, null);
            }
        }
        List<ProcessDefinition> after = repositoryService.createProcessDefinitionQuery().processDefinitionKey("holiday").list();
        for (ProcessDefinition holiday : after) {
            System.out.println(holiday.getId() + " " + holiday.isSuspended());
        }
    }

    /**
     * 单个程挂起
     */
    @Test
    public void suspendProcessInstanceById() {
        // 1. 创建流程引擎资配置文件
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        // 2. 创建引擎
        ProcessEngine engine = configuration.buildProcessEngine();

        RuntimeService runtimeService = engine.getRuntimeService();

        List<ProcessInstance> before = runtimeService.createProcessInstanceQuery().processDefinitionKey("holiday").list();
        for (ProcessInstance processInstance : before) {
            System.out.println("---" + processInstance.getId() + " " + processInstance.isSuspended() + " " + processInstance.getProcessDefinitionKey() + " " + processInstance.getBusinessKey());
            if (processInstance.getId().equals("12501") && !processInstance.isSuspended()) {
                runtimeService.suspendProcessInstanceById(processInstance.getId());
            }
        }

        List<ProcessInstance> after = runtimeService.createProcessInstanceQuery().processDefinitionKey("holiday").list();
        for (ProcessInstance processInstance : after) {
            System.out.println("+++" + processInstance.getId() + " " + processInstance.isSuspended() + " " + processInstance.getProcessDefinitionKey() + " " + processInstance.getBusinessKey());
        }
    }
}
