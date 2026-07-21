//package com.know.knowboot.activiti;
//
//import org.activiti.bpmn.model.BpmnModel;
//import org.activiti.bpmn.model.FlowElement;
//import org.activiti.engine.ProcessEngineConfiguration;
//import org.activiti.engine.impl.RepositoryServiceImpl;
//import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
//import org.activiti.engine.impl.pvm.process.ActivityImpl;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//
///**
// * 操纵Acitivi功能操作通用类
// *
// */
//public class ActivitiCommonService {
//
//	/**
//	 * 创建流程表
//	 */
//	public void createTable(){
//		ActivitiConstant.processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml")
//				.buildProcessEngine();
//	}
//
//	/**
//	 * 根据流程定义ID取得所有节点
//	 * @return
//	 */
//	public static List<ActivityImpl> getActivitiImplListByProcDefId(String defId){
//		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl)ActivitiConstant.getProcessEngine().getRepositoryService()).getDeployedProcessDefinition(defId);
//		List<ActivityImpl> list = new ArrayList<ActivityImpl>();
//		if(def != null){
//			list = def.getActivities();
//		}
//		return list;
//	}
//
//
//	/**
//	 * 根据流程定义ID取得所有节点
//	 * @return
//	 */
//	public static Collection<FlowElement> getFlowElementByProcDefId(String defId){
//		BpmnModel model = ActivitiConstant.getProcessEngine().getRepositoryService().getBpmnModel(defId);
//		Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
//		return flowElements;
//	}
//
//
//}
