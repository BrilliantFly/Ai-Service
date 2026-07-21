//package com.know.knowboot.activiti;
//
//import com.know.knowboot.other.common.RegularUtil;
//import org.activiti.bpmn.model.FlowElement;
//import org.activiti.bpmn.model.UserTask;
//import org.activiti.engine.RuntimeService;
//import org.activiti.engine.TaskService;
//import org.activiti.engine.task.Task;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.*;
//
///**
// * 流程任务管理类
// * 备注：所有的流程图定义审批通过"yes"、审批不通过"no"
// *
// */
//public class ActivitiTaskUtils {
//
//	public static final String TASK_ARRPOVE_KEY = "approve";
//	public static final String TASK_APPROVE_RESULT_YES     = "yes";
//	public static final String TASK_APPROVE_RESULT_NO        = "no";
//
//	/**
//	 * 根据ID查询Task
//	 * @return
//	 */
//	public static Task findTaskById(String taskId){
//		Task task = ActivitiConstant.getProcessEngine().getTaskService()
//				.createTaskQuery()
//				.taskId(taskId)
//				.singleResult();
//		return task;
//	}
//
//	/**
//	 * 根据任务委托人ID查询任务
//	 * @return
//	 */
//	public static List<Task> findTaskByAssignee(String assigneeID){
//		List<Task> taskList = ActivitiConstant.getProcessEngine().getTaskService()
//				.createTaskQuery()
//				.taskAssignee(assigneeID)
//				.list();
//		return taskList;
//	}
//
//
//	/**
//	 * 根据任务候选人ID查询任务
//	 * @return
//	 */
//	public static List<Task> findByCandidateGroup(String candidateGroup){
//		List<Task> taskList = ActivitiConstant.getProcessEngine().getTaskService()
//				.createTaskQuery()
//				.taskCandidateGroup(candidateGroup)
//				.list();
//		return taskList;
//	}
//
//	/**
//	 * 完成任务
//	 */
//	public static void completeTask(String taskId,String commentText){
//		Map<String, Object> params = new HashMap();
//		params.put(TASK_ARRPOVE_KEY, TASK_APPROVE_RESULT_YES);
//
//		TaskService taskService = ActivitiConstant.getProcessEngine().getTaskService();
//		taskService.addComment(taskId, null,commentText);
//		taskService.complete(taskId,params);
//	}
//
//
//	/**
//	 * 驳回任务
//	 */
//	public static void rejectTask(String taskId,String commentText){
//		Map<String, Object> params = new HashMap();
//		params.put(TASK_ARRPOVE_KEY, TASK_APPROVE_RESULT_NO);
//
//		TaskService taskService = ActivitiConstant.getProcessEngine().getTaskService();
//		taskService.addComment(taskId, null,commentText);
//		taskService.complete(taskId,params);
//	}
//
//	/**
//	 * 根据实例ID查询当前任务实例
//	 * @param procInstId
//	 * @return
//	 */
//	public static Task findTaskByProcInstId(String procInstId){
//		Task task = ActivitiConstant.getProcessEngine().getTaskService().createTaskQuery()
//				.processInstanceId(procInstId)
//				.singleResult();
//		return task;
//	}
//
//
//
//	/**
//	 * 设置流程变量
//	 */
//	public static void setVariables(String taskId,HashMap<String, Object> params){
//		TaskService taskService = ActivitiConstant.getProcessEngine().getTaskService();
//
//		for(Iterator<String> iter = params.keySet().iterator();iter.hasNext();){
//			String key = iter.next();
//			if(StringUtils.isNotEmpty(key)){
//				taskService.setVariableLocal(taskId,key,params.get(key));
//			}
//		}
//	}
//
//	/**
//	 * 获取流程变量
//	 */
//	public static Object getVariables(String taskId,String key){
//		TaskService taskService = ActivitiConstant.getProcessEngine().getTaskService();
//		Object obj = taskService.getVariable(taskId,key);
//		return obj;
//	}
//
//	/**
//	 * 根据流程定义ID获取UserTask
//	 * @return
//	 */
//	public static List<UserTask> getAllDefTaskByProcdefId(String procdefId){
//		List<UserTask> list = new ArrayList();
//		Collection<FlowElement> flowElements = ActivitiCommonService.getFlowElementByProcDefId(procdefId);
//		UserTask userTask = null;
//		for (FlowElement flowElement : flowElements) {
//			if(flowElement.getClass().equals(UserTask.class)){
//				userTask = (UserTask)flowElement;
//				userTask.setAssignee(RegularUtil.matcher(userTask.getAssignee(), "[0-9|a-z|A-Z]+"));
//				list.add(userTask);
//
//			}
//		}
//		return list;
//	}
//	/**
//	 * 流程实例设置变量
//	 */
//	public static void updateProcess(String processInstId,String key,String value){
//		RuntimeService runtimeService = ActivitiConstant.processEngine.getRuntimeService();
//		try {
//			runtimeService.setVariable(processInstId, key, value);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//}
