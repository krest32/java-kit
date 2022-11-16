package com.krest.activiti;


import com.krest.activiti.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;

import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest(classes = BootApp.class)
@RunWith(SpringRunner.class)
public class BootAppTest {

    @Autowired
    public ProcessRuntime processRuntime;

    @Autowired
    public TaskRuntime taskRuntime;

    @Autowired
    public SecurityUtil securityUtil;

    @Test
    public void findProcess() {
        //执行测试方法的时候要定义下谁在执行这个方法,,给权限,不然报错
        //An Authentication object was not found in the SecurityContext
        securityUtil.logInAs("zhangsan");
        //查询所有流程定义信息
        Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
        System.out.println("当前流程定义的数量：" + ((Page) processDefinitionPage).getTotalItems());
        //获取流程信息
        for (ProcessDefinition processDefinition : processDefinitionPage.getContent()) {
            System.out.println("---------------------------------");
            System.out.println("流程定义信息" + processDefinition);
        }

    }


    /* 测试流程启动 */
    @Test
    public void startProcess() {
        //执行测试方法的时候要定义下谁在执行这个方法,,给权限,不然报错
        //An Authentication object was not found in the SecurityContext
        securityUtil.logInAs("zhangsan");
        ProcessInstance instance = processRuntime.start(
                ProcessPayloadBuilder.start()
                        .withProcessDefinitionKey("holiday")
                        .build()
        );
        log.info("流程实例启动了:" + instance);
    }


    /**
     * 查询流程定义
     */
    @Test
    public void getProcess() {
         securityUtil.logInAs("zhangsan");
        //查询所有流程定义信息
        Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
        System.out.println("当前流程定义的数量：" + processDefinitionPage.getTotalItems());
        //获取流程信息
        for (ProcessDefinition processDefinition : processDefinitionPage.getContent()) {
            System.out.println("流程定义信息" + processDefinition);
        }
    }

    /**
     * 完成流程任务
     * 拾取任务
     */
    @Test
    public void getTask() {
        //执行测试方法的时候要定义下谁在执行这个方法,,给权限,不然报错
        //An Authentication object was not found in the SecurityContext
        securityUtil.logInAs("zhangsan");

        //查询任务
        Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 10));

        System.out.println("这里是产村出来tasks的个数" + tasks.getTotalItems());
        for (Task task : tasks.getContent()) {
            //拾取任务
            //使用的TaskPayloadBuilder来构建一个TaskPayload对象
            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
            System.out.println("拾取任 务:" + task.getName());
            //执行任务
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
            log.info("完成任务:" + task.getName());
        }
    }
}
