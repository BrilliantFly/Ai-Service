package com.know.knowboot.flowable;

import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class FlowableInstanceService {

    @Autowired
    private RuntimeService runtimeService;         //与正在执行的流程实例和执行对象相关的Service(执行管理，包括启动、推进、删除流程实例等操作)

    @Autowired
    private IdentityService identityService;

    @Autowired
    private HistoryService historyService;


    // --------------------------------------------------------- 启动工作流的几种方式 -----------------------------------------------------------------

    /**通过KEY启动流程实例(不带变量)
     * @param processInstanceKey //流程定义的KEY
     * @return 返回流程实例ID
     */
    public String startProcessInstanceByKey(String processInstanceKey){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processInstanceKey);            //用流程定义的KEY启动，会自动选择KEY相同的流程定义中最新版本的那个(KEY为模型中的流程唯一标识)

        // 输出相关的流程实例信息
        System.out.println("流程定义的ID：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例的ID：" + processInstance.getId());
        System.out.println("当前活动的ID：" + processInstance.getActivityId());

        return processInstance.getId();    //返回流程实例ID
    }

    /**通过KEY启动流程实例(带变量)
     * @param processInstanceKey 流程定义的KEY
     * @param map 存流程变量
     * @param USERNAME 流程发起人
     * @return 返回流程实例ID
     */
    public String startProcessInstanceByKeyHasVariables(String processInstanceKey, Map<String,Object> map, String USERNAME){
        Authentication.setAuthenticatedUserId(USERNAME);//设置流程发起人
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processInstanceKey, map);//map存储变量 用流程定义的KEY启动，会自动选择KEY相同的流程定义中最新版本的那个(KEY为模型中的流程唯一标识)
        Authentication.setAuthenticatedUserId(null);//这个方法最终使用一个ThreadLocal类型的变量进行存储，也就是与当前的线程绑定，所以流程实例启动完毕之后，需要设置为null，防止多线程的时候出问题。

        // 输出相关的流程实例信息
        System.out.println("流程定义的ID：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例的ID：" + processInstance.getId());
        System.out.println("当前活动的ID：" + processInstance.getActivityId());

        return processInstance.getId();    //返回流程实例ID
    }

    /**通过KEY启动流程实例(带变量)
     * @param processInstanceKey 流程定义的KEY
     * @param map 存流程变量
     * @param USERNAME 流程发起人
     * @return 返回流程实例ID
     */
    public String startProcessInstanceByKeyHasVariables2(String processInstanceKey, Map<String,Object> map, String USERNAME){
        identityService.setAuthenticatedUserId(USERNAME);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processInstanceKey,map);

        // 输出相关的流程实例信息
        System.out.println("流程定义的ID：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例的ID：" + processInstance.getId());
        System.out.println("当前活动的ID：" + processInstance.getActivityId());

        return processInstance.getId();    //返回流程实例ID
    }

    /**通过ID启动流程实例
     * @param processInstanceId //流程定义的ID
     * @return 返回流程实例ID
     */
    public String startProcessInstanceById(String processInstanceId){
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processInstanceId);            //用流程定义的ID启动

        // 输出相关的流程实例信息
        System.out.println("流程定义的ID：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例的ID：" + processInstance.getId());
        System.out.println("当前活动的ID：" + processInstance.getActivityId());

        return processInstance.getId();    //返回流程实例ID
    }

    /**通过ID启动流程实例
     * @param processInstanceId //流程定义的ID
     * @return 返回流程实例ID
     */
    public String startProcessInstanceById(String processInstanceId, Map<String,Object> map){
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processInstanceId,map);            //用流程定义的ID启动

        // 输出相关的流程实例信息
        System.out.println("流程定义的ID：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例的ID：" + processInstance.getId());
        System.out.println("当前活动的ID：" + processInstance.getActivityId());

        return processInstance.getId();    //返回流程实例ID
    }

    // --------------------------------------------------------- 工作流操作 -----------------------------------------------------------------

    /**
     * 激活实例
     * @param processInstanceId
     */
    public void activateInstanceById(String processInstanceId){
        runtimeService.activateProcessInstanceById(processInstanceId);
    }

    /**
     * 挂起实例
     * @param processInstanceId
     */
    public void suspendInstanceById(String processInstanceId){
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    /**
     * 删除实例
     * @param processInstanceId
     */
    public void deleteInstance(String processInstanceId, String deleteReason){
        // 查询历史数据
        HistoricProcessInstance historicProcessInstance = getHistoricProcessInstanceById(processInstanceId);
        if (historicProcessInstance.getEndTime() != null) {
            historyService.deleteHistoricProcessInstance(historicProcessInstance.getId());
            return;
        }
        // 删除流程实例
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
        // 删除历史流程实例
        historyService.deleteHistoricProcessInstance(processInstanceId);
    }

    /**
     * 获取实例
     */
    public void queryProcessInstance() {
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().list();

        processInstances.forEach(t -> {
            System.out.println("t.getName()=" + t.getName());
            System.out.println("t.getProcessInstanceId()=" + t.getProcessInstanceId());
            System.out.println("t.getDeploymentId()=" + t.getDeploymentId());
            System.out.println("t.getStartUserId()=" + t.getStartUserId());
            System.out.println("t.getTenantId()=" + t.getTenantId());
            System.out.println("t.getActivityId()=" + t.getActivityId());
            System.out.println("t.getProcessVariables()=" + t.getProcessVariables());
        });

    }

    /**
     * 获取实例状态
     * @param processInstanceId
     */
    public void queryProcessInstanceState(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                //.deploymentId()
                .singleResult();

        if (processInstance != null) {
            System.out.println("当前流程实例正在运行");
        } else {
            System.out.println("当前流程实例已经结束");
        }
    }

    /**
     * 获取实例状态
     * @param processInstanceId
     */
    public void queryProcessInstanceState2(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance =
                historyService.createHistoricProcessInstanceQuery()
                        .processInstanceId(processInstanceId).singleResult();
        if (historicProcessInstance.getEndTime() == null) {
            System.out.println("当前流程实例正在运行");
        } else {
            System.out.println("当前流程实例已经结束");
        }
    }

    /**
     * 获取历史实例
     * @param processInstanceId
     * @return
     */
    public HistoricProcessInstance getHistoricProcessInstanceById(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance =
                historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (Objects.isNull(historicProcessInstance)) {
            throw new FlowableObjectNotFoundException("流程实例不存在: " + processInstanceId);
        }
        return historicProcessInstance;
    }

    /**
     * 查询历史活动实例
     */
    public void createHistoricActivityInstanceQuery() {
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().list();
        for (HistoricActivityInstance hai : list) {
            System.out.println(hai.getId());
        }
    }

    /**
     * 查询历史任务实例
     */
    public void createHistoricTaskInstanceQuery() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().list();
    }

}
