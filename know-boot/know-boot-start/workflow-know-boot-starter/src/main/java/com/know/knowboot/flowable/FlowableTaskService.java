package com.know.knowboot.flowable;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class FlowableTaskService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private ProcessEngineConfigurationImpl processEngineConfiguration;

    // --------------------------------------------------------- 任务操作 -----------------------------------------------------------------

    /**
     * 分页查询任务(针对用户)
     * @param userName
     * @return
     */
    public List<Task> findTaskByUserPage(int pageIndex, int pageSize,String userName) {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .includeProcessVariables()
                .taskCandidateOrAssigned(userName)
                .orderByTaskCreateTime().desc();
        List<Task> taskList = taskQuery.listPage(pageIndex - 1, pageSize);

        //根据以上条件查询出来的结果计算总条数
        long total = taskQuery.count();
        System.out.println("分页查询任务,总条数=" + total);

        return taskList;
    }

    /**
     * 分页查询任务(针对用户组)
     * @param roles
     * @return
     */
    public List<Task> findTaskByGroupPage(int pageIndex, int pageSize,List<String> roles) {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .includeProcessVariables()
                .taskCandidateGroupIn(roles)
                .orderByTaskCreateTime().desc();
        List<Task> taskList = taskQuery.listPage(pageIndex - 1, pageSize);

        //根据以上条件查询出来的结果计算总条数
        long total = taskQuery.count();
        System.out.println("分页查询任务,总条数=" + total);

        return taskList;
    }

    /**
     * 查询我的任务
     * @param userName
     * @return
     */
    public List<Task> findMyPersonalTask(String userName) {
        return taskService.createTaskQuery() 	// 创建查询对象
                .taskAssignee(userName) 		// 指定办理人
                .list(); 						// 读出列表
    }

    /**
     * 候选人待签收任务列表
     * @param candidateUserName
     * @return
     */
    public List<Task> findCandidateTask(String candidateUserName) {
        return taskService.createTaskQuery() 	// 创建查询对象
                .taskCandidateUser(candidateUserName) 		// 指定办理人
                .list(); 						// 读出列表
    }

    /**
     * 查询发起人待办列表(针对用户)
     * @param userName
     * @return
     */
    public List<Task> findMyPersonalTask1(String userName) {
        return taskService.createTaskQuery() 	// 创建查询对象
                .taskCandidateOrAssigned(userName) 		// 指定办理人
                .list(); 						// 读出列表
    }

    /**
     * 查询发起人待办列表(针对用户组)
     * @param roleCodes
     * @return
     */
    public List<Task> findMyPersonalTask2(List<String> roleCodes) {
        return taskService.createTaskQuery().taskCandidateGroupIn(roleCodes).orderByTaskCreateTime().desc().list();
    }

    /**
     * 候选组待签收任务列表
     * @param candidateGroupUserName
     * @return
     */
    public List<Task> findCandidateGroupTask(String candidateGroupUserName) {
        return taskService.createTaskQuery() 	// 创建查询对象
                .taskCandidateGroup(candidateGroupUserName)// 指定办理人
                .list(); 						// 读出列表
    }

    /**
     * 校验任务是否被签收
     * @param taskId
     */
    public void checkClaimTask(String taskId) {
        //获取当前任务信息
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //判断该任务是否被签领
        String assignee = task.getAssignee();
        if (StringUtils.isBlank(assignee)){
            System.out.println("任务尚未被签收");
        }else {
            System.out.println("任务已被签收");
        }
    }

    /**
     * 任务签收
     * @param taskId
     * @param userId
     */
    public void claimTask(String taskId,String userId) {
        taskService.claim(taskId,userId);
    }

    /**
     * 任务取消签收
     * @param taskId
     */
    public void unClaimTask(String taskId) {
        taskService.unclaim(taskId);
    }

    /**
     * 转办任务
     * @param taskId
     * @param curUserId
     * @param acceptUserId
     */
    public void transferTask(String taskId,String curUserId,String acceptUserId) {
        taskService.setOwner(taskId, curUserId);
        taskService.setAssignee(taskId,acceptUserId );
    }

    /**
     * 委派任务
     * 是将任务节点分给其他人处理，等其他人处理好之后，委派任务会自动回到委派人的任务中
     * @param taskId
     * @param curUserId
     * @param acceptUserId
     */
    public void delegateTask(String taskId,String curUserId,String acceptUserId) {
        taskService.setOwner(taskId, curUserId);
        taskService.delegateTask(taskId,acceptUserId);
    }

    /**
     *  被委派任务的办理
     *  办理完成后，委派任务会自动回到委派人的任务中
     * @param taskId
     * @param variables
     */
    public void delegateBackTask(String taskId, Map<String,Object> variables) {
        taskService.resolveTask(taskId, variables);
    }

    /**
     * 完成任务
     * @param taskId
     */
    public void completeMyPersonalTask(String taskId) {
        taskService.complete(taskId);
    }

    /**
     * 完成任务
     * @param taskId
     */
    public void completeMyPersonalTask(String taskId, Map<String,Object> variables) {
        taskService.complete(taskId,variables);
    }

    /**
     * 作废流程
     * @param processId
     * @param reason
     * @throws Exception
     */
    public void deleteProcessInstance(String processId, String reason) throws Exception {
        runtimeService.deleteProcessInstance(processId, reason);
    }

    /**
     * 删除历史流程
     * @param PROC_INST_ID_
     * @throws Exception
     */
    public void deleteHiProcessInstance(String PROC_INST_ID_) throws Exception {
        historyService.deleteHistoricProcessInstance(PROC_INST_ID_);
    }

    /**
     * 将节点移动到任意节点上
     * @param taskId
     */
    public void withdrawProcess(String taskId) {
        //获取当前任务，让其移动到审核节点位置
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task != null ) {
            //将节点移动到某个节点（ACT_RU_ACTINST的ACT_ID_）
            runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId()).moveActivityIdTo(task.getTaskDefinitionKey(), "Activity_1ir92dg").changeState();
        }else{
            System.out.println("移动到任意节点失败");
        }
    }

    /**
     * 将节点移动到上一个节点
     * @param taskId
     */
    public void withdrawProcess1(String taskId) {
        //获取当前任务，让其移动到审核节点位置
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task != null ) {
            //获取历史任务节点(ACT_HI_TASKINST)
            List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId()).orderByHistoricTaskInstanceStartTime().asc().list();
            Iterator<HistoricTaskInstance> it = historicTaskInstanceList.iterator();
            //循环节点，获取当前节点的上一节点的key
            String tkey = "";
            while(it.hasNext())    {
                HistoricTaskInstance his = it.next();
                if (!task.getTaskDefinitionKey().equals(his.getTaskDefinitionKey())){
                    tkey = his.getTaskDefinitionKey();
                }
            }
            runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId()).moveActivityIdTo(task.getTaskDefinitionKey(), tkey).changeState();
        }else{
            System.out.println("移动到上一个节点失败");
        }
    }

    /**
     * 获取流程变量
     * @param taskId
     */
    public void getVariables(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Map<String, Object> variables = null;
        if(task != null){
            variables  = runtimeService.getVariables(task.getExecutionId());
            System.out.println("获取流程变量=" + variables);
        }
    }


}
