package com.exercise.activiti;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

public class ActivitiDemo {
   /**
    * 部署流程定义
    */
   @Test
   public void testDeployment(){
//       1、创建ProcessEngine
       ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//       2、得到RepositoryService实例
       RepositoryService repositoryService = processEngine.getRepositoryService();
//       3、使用RepositoryService进行部署
       Deployment deployment = repositoryService.createDeployment()
              .addClasspathResource("bpmn/evection.bpmn") // 添加bpmn资源
              .addClasspathResource("bpmn/evection.png")  // 添加png资源
              .name("出差申请流程")
              .deploy();
//       4、输出部署信息
       System.out.println("流程部署id：" + deployment.getId());
       System.out.println("流程部署名称：" + deployment.getName());
  }


    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcess(){
//       1、创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//       2、获取RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
//       3、根据流程定义Id启动流程
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("myEvection");
//       输出内容
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
        System.out.println("当前活动Id：" + processInstance.getActivityId());
    }

    /**
     * 查询当前个人待执行的任务
     */
    @Test
    public void testFindPersonalTaskList() {
//       任务负责人
        String assignee = "zhangsan";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//       创建TaskService
        TaskService taskService = processEngine.getTaskService();
//       根据流程key 和 任务负责人 查询任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("myEvection") //流程Key
                .taskAssignee(assignee)//只查询该任务负责人的任务
                .list();
        for (Task task : list) {
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    // 完成任务
    @Test
    public void completTask(){
//       获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//       获取taskService
        TaskService taskService = processEngine.getTaskService();
//       根据流程key 和 任务的负责人 查询任务
//       返回一个任务对象
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("myEvection") //流程Key
                .taskAssignee("zhangsan")  //要查询的负责人
                .singleResult();
//       完成任务,参数：任务id
        taskService.complete(task.getId());
    }

    /**
     * 查询流程定义
     */
    @Test
    public void queryProcessDefinition(){
        //       获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//       repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
//       得到ProcessDefinitionQuery 对象
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
//         查询出当前所有的流程定义
//         条件：processDefinitionKey =evection
//         orderByProcessDefinitionVersion 按照版本排序
//       desc倒叙
//       list 返回集合
        List<ProcessDefinition> definitionList = processDefinitionQuery.processDefinitionKey("myEvection")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
//     输出流程定义信息
        for (ProcessDefinition processDefinition : definitionList) {
            System.out.println("流程定义 id="+processDefinition.getId());
            System.out.println("流程定义 name="+processDefinition.getName());
            System.out.println("流程定义 key="+processDefinition.getKey());
            System.out.println("流程定义 Version="+processDefinition.getVersion());
            System.out.println("流程部署ID ="+processDefinition.getDeploymentId());
        }
    }


    /**
     * 查看历史信息
     */
    @Test
    public void findHistoryInfo(){
//     获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//       获取HistoryService
        HistoryService historyService = processEngine.getHistoryService();
//       获取 actinst表的查询对象
        HistoricActivityInstanceQuery instanceQuery = historyService.createHistoricActivityInstanceQuery();
//       查询 actinst表，条件：根据 InstanceId 查询
//       instanceQuery.processInstanceId("2501");
//       查询 actinst表，条件：根据 DefinitionId 查询
        instanceQuery.processDefinitionId("myEvection:1:7504");
//       增加排序操作,orderByHistoricActivityInstanceStartTime 根据开始时间排序 asc 升序
        instanceQuery.orderByHistoricActivityInstanceStartTime().asc();
//       查询所有内容
        List<HistoricActivityInstance> activityInstanceList = instanceQuery.list();
//       输出
        for (HistoricActivityInstance hi : activityInstanceList) {
            System.out.println(hi.getActivityId());
            System.out.println(hi.getActivityName());
            System.out.println(hi.getProcessDefinitionId());
            System.out.println(hi.getProcessInstanceId());
            System.out.println("<==========================>");
        }
    }
}