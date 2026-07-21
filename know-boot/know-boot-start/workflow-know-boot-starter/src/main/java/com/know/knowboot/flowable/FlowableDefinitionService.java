package com.know.knowboot.flowable;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipInputStream;

@Component
public class FlowableDefinitionService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;

    // --------------------------------------------------------- 部署工作流的几种方式 -----------------------------------------------------------------

    /**
     * 使用文件流stream，部署工作流
     * @param multipartFile 上传的文件必须是XXXbpmn20.xml格式的。resourceName的后缀格式必须是XXXbpmn20.xml
     * @throws IOException
     */
    public void streamDeploy(MultipartFile multipartFile) throws IOException {

        String resourceName = multipartFile.getOriginalFilename();
        Deployment deployStream = repositoryService.createDeployment()
                .addInputStream(resourceName, multipartFile.getInputStream())
                .deploy();

    }

    /**
     * 使用classpath，部署工作流
     */
    public void classPathDeploy(String resourceName,String category,String tenantId,String classPath){
        Deployment deployment = repositoryService.createDeployment()// 创建Deployment对象
                .name(resourceName) // 设置部署流程的名称
                .category(category) //分类
                .tenantId(tenantId) //租户ID
                .addClasspathResource(classPath) // 添加流程部署文件
                .deploy(); // 执行部署操作
        System.out.println("deployment.getId() = " + deployment.getId());
        System.out.println("deployment.getName() = " + deployment.getName());
    }

    /**
     * 使用压缩文件格式（zip）ZipStream，部署工作流
     * @param multipartFile
     * @throws IOException
     */
    public void zipStreamDeploy(MultipartFile multipartFile) throws IOException {

        String resourceName = multipartFile.getOriginalFilename();
        Deployment deployZipStream = repositoryService.createDeployment()
                .name(resourceName)
                .addZipInputStream(new ZipInputStream(multipartFile.getInputStream()))
                .deploy();

    }

    /**
     * 使用纯文本格式text，部署工作流
     */
    public void textDeploy(String resourceName,String flowableText){

        Deployment deployText = repositoryService.createDeployment()
                .addString(resourceName, flowableText)
                .deploy();

    }

    /**
     * 使用字节数组，部署工作流
     * @param multipartFile
     * @throws IOException
     */
    public void bytesStreamDeploy(MultipartFile multipartFile) throws IOException {

        String resourceName = multipartFile.getOriginalFilename();
        byte[] bytes = multipartFile.getBytes();
        Deployment deployByte = repositoryService.createDeployment()
                .addBytes(resourceName, bytes)
                .deploy();

    }


    // --------------------------------------------------------- 部署工作流查询 -----------------------------------------------------------------
    /**
     * 查询部署
     */
    public void searchDeployment() {

        RepositoryService repositoryService = processEngine.getRepositoryService();
        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        for (Deployment deployment : list) {
            System.out.println("部署ID===》" + deployment.getId());
            System.out.println("部署key===》" + deployment.getKey());
            System.out.println("部署名称===》" + deployment.getName());
            System.out.println("部署时间===》" + deployment.getDeploymentTime());
        }

    }

    /**
     * 激活工作流
     */
    public void activateDeployment(String deployId) {

        ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
        repositoryService.activateProcessDefinitionById(procDef.getId(), true, null);

    }

    /**
     * 挂起工作流
     */
    public void suspendDeployment(String deployId) {

        ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
        repositoryService.suspendProcessDefinitionById(procDef.getId(), true, null);

    }

    /**
     * 删除工作流
     */
    public void deleteDeployment(String deployId) {

        // true 允许级联删除 ,不设置会导致数据库外键关联异常
        repositoryService.deleteDeployment(deployId, true);

    }


}
