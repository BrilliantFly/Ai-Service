//package com.know.knowboot.activiti;
//import org.activiti.engine.RepositoryService;
//import org.activiti.engine.RuntimeService;
//import org.activiti.engine.repository.Deployment;
//import org.activiti.engine.repository.ProcessDefinition;
//import org.activiti.engine.repository.ProcessDefinitionQuery;
//import org.activiti.engine.runtime.ProcessInstance;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * 流程实例管理类
// *
// */
//public class ActivitiProcessUtils {
//
//
//	/**
//	 * 部署流程实例
//	 */
//	public static void publishProcess(String name,String bpmnFile,String pngFile){
//		Deployment deployment = ActivitiConstant.processEngine.getRepositoryService()
//						.createDeployment()
//						.name(name)
//						.addClasspathResource(bpmnFile)
//						.addClasspathResource(pngFile).deploy();
//	}
//
//	/**
//	 * KEY启动流程实例
//	 */
//	public static ProcessInstance startPrococessInstByKey(String processKey){
//		RuntimeService runtimeService = ActivitiConstant.processEngine.getRuntimeService();
//		ProcessInstance ps = runtimeService.startProcessInstanceByKey(processKey);
//		return ps;
//	}
//
//	/**
//	 * ID启动流程实例
//	 */
//	public static ProcessInstance startProcessInstById(String id,Map<String, Object> parms){
//		RuntimeService runtimeService = ActivitiConstant.processEngine.getRuntimeService();
//
//		ProcessInstance ps = null;
//		if(parms == null){
//			ps = runtimeService.startProcessInstanceById(id);
//		}else{
//			ps = runtimeService.startProcessInstanceById(id, parms);
//		}
//
//		return ps;
//	}
//
//	/**
//	 * 根据ID得到流程实例
//	 */
//	public static ProcessInstance findProcessInstanceById(String id){
//		RuntimeService runtimeService = ActivitiConstant.processEngine.getRuntimeService();
//		ProcessInstance ps = runtimeService.createProcessInstanceQuery().processInstanceId(id).singleResult();
//		return ps;
//	}
//
//
//	/**
//	 * 查看流程定义
//	 */
//	public static List<ProcessDefinition> findProcessDefintion(String deploymentId,String processDefintionId,String processDefintionKey,String processDefintionKeyLike){
//		RepositoryService repositoryService = ActivitiConstant.getProcessEngine().getRepositoryService();
//		ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery()
//				.orderByProcessDefinitionVersion();
//
//		if(StringUtils.isEmpty(deploymentId)){
//			definitionQuery.deploymentId(deploymentId);
//		}
//		if(StringUtils.isEmpty(processDefintionId)){
//			definitionQuery.processDefinitionId(processDefintionId);
//		}
//		if(StringUtils.isEmpty(processDefintionKey)){
//			definitionQuery.processDefinitionKey(processDefintionKey);
//		}
//		if(StringUtils.isEmpty(processDefintionKeyLike)){
//			definitionQuery.processDefinitionKeyLike(processDefintionKeyLike);
//		}
//
//		return definitionQuery.asc().list();
//
//	}
//
//	/**
//	 * 根据ID得到流程定义
//	 * @return
//	 */
//	public static ProcessDefinition findProcDefById(String procdefId){
//		ProcessDefinition procDef = ActivitiConstant.getProcessEngine().getRepositoryService()
//				.createProcessDefinitionQuery().processDefinitionId(procdefId)
//				.orderByProcessDefinitionVersion()
//				.asc().singleResult();
//		return procDef;
//	}
//
//
//	/**
//	 * 删除流程定义
//	 */
//	public static void deleteProcess(String deploymentId){
//		RepositoryService repositoryService = ActivitiConstant.getProcessEngine().getRepositoryService();
//		try {
//			repositoryService.deleteDeployment(deploymentId);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 流程是否已经结束
//	 * @param processId
//	 * @return
//	 */
//	public static boolean processIsFinish(String processId){
//		ProcessInstance ps = findProcessInstanceById(processId);
//		if(ps == null || ps.isEnded()){
//			return true;
//		}
//		return false;
//	}
//
//
//	/**
//	 * 挂起一个流程
//	 */
//	public static void hangupProcess(String processKey){
//		RepositoryService repository = ActivitiConstant.processEngine.getRepositoryService();
//		repository.suspendProcessDefinitionByKey(processKey);
//	}
//
//
//	/**
//	 * 激活一个流程
//	 */
//	public static void activateProcessInstance(String processInstanceId){
//		RuntimeService runtimeService = ActivitiConstant.processEngine.getRuntimeService();
//        runtimeService.activateProcessInstanceById(processInstanceId);
//    }
//
////	/**
////	 * 发起流程
////	 */
////	@SuppressWarnings("unchecked")
////	public static void launchProcess(String appr_no,String entityid,String path,String tableName){
////		ProcSettingService procSettingService = SpringContextHolder.getBean(ProcSettingService.class);
////		TaskSettingService taskSettingService = SpringContextHolder.getBean(TaskSettingService.class);
////
////		ProcSetting procSetting = procSettingService.findUniqueByProperty("appr_no",appr_no);
////		TaskSetting taskSetting = taskSettingService.findUniqueByProperty("procsetting_id", procSetting.getId());
////		Map<String,Object> params = (Map<String, Object>) Json.fromJson(taskSetting.getAssignee());
////		if(procSetting != null && StringUtils.isNotBlank(procSetting.getId())){
////			ProcessInstance procInst = ActivitiProcessUtils.startProcessInstById(procSetting.getProcdefId(),params);
////			ActivitiTaskUtils.updateProcess(procInst.getId(),"form_id",entityid);
////			ActivitiTaskUtils.updateProcess(procInst.getId(),"path_id",path);
////			ActivitiTaskUtils.updateProcess(procInst.getId(),"table_name",tableName);
////			Task task = ActivitiTaskUtils.findTaskByProcInstId(procInst.getId());
////			ActivitiTaskUtils.completeTask(task.getId(),"流程发起");
////		}
////	}
////	/**
////	 * 获取模块流程所对应哪个的角色
////	 */
////	@SuppressWarnings("unchecked")
////	public static Map<String,Object> findTaskSetting(String appr_no){
////		ProcSettingService procSettingService = SpringContextHolder.getBean(ProcSettingService.class);
////		TaskSettingService taskSettingService = SpringContextHolder.getBean(TaskSettingService.class);
////		ProcSetting procSetting = procSettingService.findUniqueByProperty("appr_no",appr_no);
////		TaskSetting taskSetting = taskSettingService.findUniqueByProperty("procsetting_id", procSetting.getId());
////		Map<String,Object> params = (Map<String, Object>) Json.fromJson(taskSetting.getAssignee());
////		return params;
////
////	}
////	public static List<UserTask> findUserTaskList(String appr_no){
////		ProcSettingService procSettingService = SpringContextHolder.getBean(ProcSettingService.class);
////		ProcSetting procSetting = procSettingService.findUniqueByProperty("appr_no",appr_no);
////		List<UserTask> allDefTaskByProcdefId = ActivitiTaskUtils.getAllDefTaskByProcdefId(procSetting.getProcdefId());
////		return allDefTaskByProcdefId;
////
////	}
//
//}
