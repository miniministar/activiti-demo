package com.exercise;

import com.exercise.pojo.Evection;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:activiti-spring.xml")
public class ActivitiGatewayParallel {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @Test
    public void testLine() {
        //1.部署流程
//        testDeployment();
        //2.启动流程
        startProcess();
        //3.1 完成 第一个节点， tom
        String key = "parallel";
        completeTase("tom", key);
        //3.2 完成 第二个节点， jack
        completeTase("jack", key);
        //3.3 完成 第三个节点， jerry
        completeTase("jerry", key);
        //3.4 完成 第四个节点， rose
        completeTase("rose", key);
    }

    /**
     * 部署流程定义
     */
    @Test
    public void testDeployment(){
//        3、使用RepositoryService进行部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/evection-parallel.bpmn") // 添加bpmn资源
//                .addClasspathResource("bpmn/evection.png")  // 添加png资源
                .name("出差申请流程-并行网关")
                .deploy();
//        4、输出部署信息
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
    }


    /**
     * 启动流程实例,设置流程变量的值
     */
    @Test
    public void startProcess(){
//        流程定义key
        String key = "parallel";
//       创建变量集合
        Map<String, Object> map = new HashMap<>();
//        创建出差pojo对象
        Evection evection = new Evection();
//        设置出差天数
        evection.setNum(4d);
//      定义流程变量，把出差pojo对象放入map
        map.put("evection",evection);
//        启动流程实例，并设置流程变量的值（把map传入）
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(key, map);
//      输出
        System.out.println("流程实例名称="+processInstance.getName());
        System.out.println("流程定义id=="+processInstance.getProcessDefinitionId());
    }

    @Test
    public void completTask(){
//        流程定义的Key
        String key = "parallel";
//        任务负责人
//        String assingee = "tom";
//        String assingee = "jack";
//        String assingee = "jerry";
        String assingee = "rose";
//        查询任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(assingee)
                .singleResult();
        if(task != null){
            //     根据任务id来   完成任务
            taskService.complete(task.getId());
        }
    }

    private void completeTase(String assingee, String processDefinitionKey) {
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskAssignee(assingee)
                .singleResult();
        if(task != null){
            //     根据任务id来   完成任务
            taskService.complete(task.getId());
            System.out.println(task.getId()+"----任务已完成,处理人：" + assingee);
        }
    }
}
