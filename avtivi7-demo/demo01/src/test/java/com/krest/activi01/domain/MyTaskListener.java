package com.krest.activi01.domain;

import org.activiti.engine.delegate.DelegateTask;

public class MyTaskListener implements org.activiti.engine.delegate.TaskListener {
    public void notify(DelegateTask delegateTask) {
        if (delegateTask.getName().equals("出差单") && delegateTask.getEventName().equals("create")){
            delegateTask.setAssignee("监听-李老师");
        }
    }
}
