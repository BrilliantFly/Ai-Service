package com.know.knowboot.flowable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeanHelper {

    public static FlowableDefinitionService definitionService;
    public static FlowableInstanceService instanceService;
    public static FlowableTaskService taskService;

    @Autowired
    public void setService(FlowableDefinitionService flowableDefinitionService,FlowableInstanceService flowableInstanceService,FlowableTaskService flowableTaskService){
        definitionService = flowableDefinitionService;
        instanceService = flowableInstanceService;
        taskService = flowableTaskService;
    }

}
