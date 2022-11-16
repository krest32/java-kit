package com.krest.activi01;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

public class gateWayDemo {

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
                .addClasspathResource("bpmn/holiday-gateway.bpmn20.xml")
                .name("出差流程")
                .deploy();
        // 查看部署Id
        System.out.println(deployment.getId());
        // 查看部署name
        System.out.println(deployment.getName());
    }
}
