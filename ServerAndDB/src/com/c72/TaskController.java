package com.c72;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class TaskController{ 

   private ApplicationContext context = new ClassPathXmlApplicationContext("mySpring.xml");
   private TaskDAOImpl taskDAOImpl = 
   (TaskDAOImpl)context.getBean("TaskDAOImpl");   
   
   //用户相关
   @RequestMapping(value = "/userRegist", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> userRegist(@RequestParam("name") String name,
		   								@RequestParam("phone") String phone,
		   								@RequestParam("email") String email,
		   								@RequestParam("password") String password
		   ) {
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   //检查是不是已经存在同名用户
	   User user=taskDAOImpl.getUserbyName(name);
	   if(user!=null) {
		   modelMap.put("status", "fail");
		   modelMap.put("log","This name has been used.");
	   }else {
		   taskDAOImpl.creatUser(name, phone, email, password);
		   modelMap.put("status", "success");
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> userLogin(@RequestParam("name") String name,
		   							   @RequestParam("password") String password
		   ) {
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   User user=taskDAOImpl.getUserbyName(name);
	   //检查用户存在
	   if(user==null) {
		   modelMap.put("status", "fail");
		   modelMap.put("log","User not exist.");
	   }else {
		   //密码匹配
		   if(user.getPassword().equals(password)) {
			   modelMap.put("status", "success");
			   modelMap.put("user", user);
		   }else {
			   modelMap.put("status", "fail");
			   modelMap.put("log","Password mismatch.");
		   }
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/getUser", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> getUser(@RequestParam("name") String name
		   ) {
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   User user=taskDAOImpl.getUserbyName(name);
	   //检查用户存在
	   if(user==null) {
		   modelMap.put("status", "fail");
		   modelMap.put("log","User not exist.");
	   }else {
		   //密码匹配
		   modelMap.put("status", "success");
		   modelMap.put("user", user);
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> updateUser(@RequestParam("name") String name,
										@RequestParam("phone") String phone,
										@RequestParam("email") String email,
										@RequestParam("password") String password,
										@RequestParam("money") String money
										
		   ) {
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   User user=taskDAOImpl.getUserbyName(name);
	   //检查用户存在
	   if(user==null) {
		   modelMap.put("status", "fail");
		   modelMap.put("log","User not exist.");
	   }else {
		   taskDAOImpl.updateUser(user.getUid(), name, phone, email, password, Integer.valueOf(money), user.getCredit());
		   modelMap.put("status", "success");
	   }
	   return modelMap;
   }
   
   //查询任务相关
   @RequestMapping(value = "/listTasks", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> listTasks(@RequestParam("extra") String extra
		   ) {
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   List<Task> tasks = taskDAOImpl.listTasks(extra);
	   modelMap.put("taskList", tasks);
	   return modelMap;
   }
   
   //mode=ASC/DESC:升/降序
   @RequestMapping(value = "/listTasksbyDDL", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> listTasksbyDDL(@RequestParam("mode") String mode,
		   									@RequestParam("extra") String extra
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   List<Task> tasks = taskDAOImpl.listTasksbyDLL(mode,extra);
	   modelMap.put("taskList", tasks);
	   return modelMap;
   }
   
   @RequestMapping(value = "/listTasksbyMoney", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> listTasksbyMoney(@RequestParam("mode") String mode,
		   									  @RequestParam("extra") String extra
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   List<Task> tasks = taskDAOImpl.listTasksbyMoney(mode,extra);
	   modelMap.put("taskList", tasks);
	   return modelMap;
   }
   
   @RequestMapping(value = "/listTasksbyStartTime", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> listTasksbyStartTime(@RequestParam("mode") String mode,
		   										  @RequestParam("extra") String extra
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   List<Task> tasks = taskDAOImpl.listTasksbyStartTime(mode, extra);
	   modelMap.put("taskList", tasks);
	   return modelMap;
   }
   
   //发布、接取任务相关
   @RequestMapping(value = "/createTask", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> createTask(@RequestParam("uid") String uid,
		   								@RequestParam("title") String title,
		   								@RequestParam("detail") String detail,
		   								@RequestParam("money") String money,
		   								@RequestParam("type") String type,
		   								@RequestParam("total_num") String total_num,
		   								@RequestParam("end_time") String end_time
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   //检查余额
	   User user=taskDAOImpl.getUserbyUid(Integer.valueOf(uid));
	   if(user.getMoney()<Integer.valueOf(money)*Integer.valueOf(total_num)) {
		   modelMap.put("state", "fail");
		   modelMap.put("log", "No enough money.");
	   }else {//创建任务并且扣除钱
		   taskDAOImpl.creatTask(Integer.valueOf(uid), title, detail, Integer.valueOf(money), type, Integer.valueOf(total_num), Timestamp.valueOf(end_time), "unfinished");
		   taskDAOImpl.updateUser(user.getUid(), user.getName(), user.getPhone(), user.getEmail(), user.getPassword(), user.getMoney()-Integer.valueOf(money)*Integer.valueOf(total_num), user.getCredit());
		   modelMap.put("state", "success"); 
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/getTask", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> getTask(@RequestParam("tid") String tid
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   Task task=taskDAOImpl.getTask(Integer.valueOf(tid));
	   modelMap.put("task", task);
	   return modelMap;
   }
   
   @RequestMapping(value = "/updateTask", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> updateTask(@RequestParam("tid") String tid,
		   								@RequestParam("uid") String uid,
		   								@RequestParam("title") String title,
		   								@RequestParam("detail") String detail,
		   								@RequestParam("type") String type,
		   								@RequestParam("end_time") String end_time
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   Task task=taskDAOImpl.getTask(Integer.valueOf(tid));
	   if(task==null) {
		   modelMap.put("state", "fail");
		   modelMap.put("log", "Task not exist.");
	   }else {
		   taskDAOImpl.updateTask(Integer.valueOf(tid), title, detail, task.getMoney(), type, task.getTotal_num(), task.getCurrent_num(), task.getStart_time(), Timestamp.valueOf(end_time), task.getState());
		   modelMap.put("state", "success");
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/joinTask", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> joinTask(@RequestParam("tid") String tid,
		   							  @RequestParam("uid") String uid
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   //检查任务状态
	   Task task=taskDAOImpl.getTask(Integer.valueOf(tid));
	   if(task==null) {
		   modelMap.put("state", "fail");
		   modelMap.put("log", "Task not exist.");
	   }else {
		   if(task.getState().equals("finished")) {
			   modelMap.put("state", "fail");
			   modelMap.put("log", "Task has finished.");
		   }else {
			   if(task.getCurrent_num()==task.getTotal_num()) {
				   modelMap.put("state", "fail");
				   modelMap.put("log", "Task join is full.");
			   }else {
				   taskDAOImpl.creatUserJoins(Integer.valueOf(tid), Integer.valueOf(uid), new Timestamp(new java.util.Date().getTime()));
				   taskDAOImpl.updateTask(task.getTid(), task.getTitle(), task.getDetail(), task.getMoney(), task.getType(), task.getTotal_num(), task.getCurrent_num()+1, task.getStart_time(), task.getEnd_time(), task.getState());
				   modelMap.put("state", "success");
			   }
		   }
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/disjoinTask", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> disjoinTask(@RequestParam("tid") String tid,
		   								 @RequestParam("uid") String uid
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   //检查任务状态
	   Task task=taskDAOImpl.getTask(Integer.valueOf(tid));
	   if(task==null) {
		   modelMap.put("state", "fail");
		   modelMap.put("log", "Task not exist.");
	   }else {
		   if(task.getState().equals("finished")) {
			   modelMap.put("state", "fail");
			   modelMap.put("log", "Task has finished.");
		   }else {
			   taskDAOImpl.deleteUserJoinsbyUid(Integer.valueOf(tid), Integer.valueOf(uid));
			   taskDAOImpl.updateTask(task.getTid(), task.getTitle(), task.getDetail(), task.getMoney(), task.getType(), task.getTotal_num(), task.getCurrent_num()-1, task.getStart_time(), task.getEnd_time(), task.getState());
			   modelMap.put("state", "success");
		   }
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/endTask", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> endTask(@RequestParam("tid") String tid,
		   							 @RequestParam("uid") String uid
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   //检查任务状态
	   Task task=taskDAOImpl.getTask(Integer.valueOf(tid));
	   if(task==null) {
		   modelMap.put("state", "fail");
		   modelMap.put("log", "Task not exist.");
	   }else {
		   if(task.getUid()!=Integer.valueOf(uid)) {
			   modelMap.put("state", "fail");
			   modelMap.put("log", "User mismatch publisher.");
		   }else {
			   if(task.getState().equals("finished")) {
				   modelMap.put("state", "fail");
				   modelMap.put("log", "Task already finished.");
			   }else {
				   List<UserJoins> userJoins=taskDAOImpl.listUserJoinsbyTid(task.getTid());
				   for(int i=0;i<userJoins.size();i++) {
					   UserJoins join=userJoins.get(i);
					   User user=taskDAOImpl.getUserbyUid(join.getUid());
					   taskDAOImpl.updateUser(join.getUid(), user.getName(), user.getPhone(), user.getEmail(), user.getPassword(), user.getMoney()+task.getMoney(), user.getCredit()+1);
				   }
				   User publisher=taskDAOImpl.getUserbyUid(task.getUid());
				   taskDAOImpl.updateUser(publisher.getUid(), publisher.getName(), publisher.getPhone(), publisher.getEmail(), publisher.getPassword(), publisher.getMoney() + (task.getTotal_num() - task.getCurrent_num()) * task.getMoney(), publisher.getCredit());
				   taskDAOImpl.updateTask(task.getTid(), task.getTitle(), task.getDetail(), task.getMoney(), task.getType(), task.getTotal_num(), task.getCurrent_num(), task.getStart_time(), task.getEnd_time(), "finished");
				   modelMap.put("state", "success");
			   }
		   }
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/getJoinUsers", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> getJoinUsers(@RequestParam("tid") String tid
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   //检查任务状态
	   Task task=taskDAOImpl.getTask(Integer.valueOf(tid));
	   if(task==null) {
		   modelMap.put("state", "fail");
		   modelMap.put("log", "Task not exist.");
	   }else {
		   List<UserJoins> userJoins=taskDAOImpl.listUserJoinsbyTid(task.getTid());
		   List<User> users=new ArrayList<User>();
		   for(int i=0;i<userJoins.size();i++) {
			   UserJoins join=userJoins.get(i);
			   users.add(taskDAOImpl.getUserbyUid(join.getUid()));
		   }
		   modelMap.put("state", "success");
		   modelMap.put("userList", users);
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/getJoinTasks", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> getJoinTasks(@RequestParam("uid") String uid
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   //检查任务状态
	   List<UserJoins> userJoins=taskDAOImpl.listUserJoinsbyUid(Integer.valueOf(uid));
	   List<Task> tasks=new ArrayList<Task>();
	   for(int i=0;i<userJoins.size();i++) {
		   UserJoins join=userJoins.get(i);
		   tasks.add(taskDAOImpl.getTask(join.getTid()));
	   }
	   modelMap.put("taskList", tasks);
	   return modelMap;
   }
   
   @RequestMapping(value = "/createMessage", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> createMessage( @RequestParam("tid") String tid,
		   									@RequestParam("uid") String uid,
		   									@RequestParam("detail") String detail
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   Task task=taskDAOImpl.getTask(Integer.valueOf(tid));
	   List<Message> messages=taskDAOImpl.listMessages(task.getTid());
	   taskDAOImpl.createMessage(task.getTid(), Integer.valueOf(uid), messages==null?1:messages.size()+1, detail);
	   modelMap.put("detail", detail); 
	   return modelMap;
   }
   
   @RequestMapping(value = "/removeMessage", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> removeMessage( @RequestParam("tid") String tid,
		   									@RequestParam("rank") String rank
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();;
	   taskDAOImpl.deleteMessage(Integer.valueOf(tid), Integer.valueOf(rank));
	   return modelMap;
   }
   
   @RequestMapping(value = "/listMessages", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> listMessages( @RequestParam("tid") String tid
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   List<Message> messages=taskDAOImpl.listMessages(Integer.valueOf(tid));
	   modelMap.put("messages", messages);
	   return modelMap;
   }
   
   @RequestMapping(value = "/getMessage", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> getMessage( @RequestParam("tid") String tid,
										   @RequestParam("rank") String rank
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   Message message=taskDAOImpl.getMessage(Integer.valueOf(tid),Integer.valueOf(rank));
	   modelMap.put("message", message);
	   return modelMap;
   }
   
   @RequestMapping(value = "/hello.test", method = RequestMethod.GET)
   public String testLink(HttpServletRequest request, HttpServletResponse response, Model model, String v_uid, String v_password) { 
	   /*ModelAndView mv=new ModelAndView();
	   mv.setViewName("hello");
	   System.out.println("visit");
	   return mv;*/
	   /*try {
		   System.out.println("visit");
		request.getRequestDispatcher("newhello.test").forward(request, response);
	} catch (ServletException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	   //return "forward:newhello.test";
	   /*try {
		response.sendRedirect("newhello.test");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	   if(Integer.valueOf(v_uid)==-1) {
		   return "error";
	   }
	   return "redirect:newhello.test"; 
   }
   	
   @RequestMapping(value = "/newhello.test", method = RequestMethod.GET)
   public ModelAndView testLinkRedirect(HttpServletRequest request, HttpServletResponse response, Model model) { 
	   ModelAndView mv=new ModelAndView();
	   mv.setViewName("newhello");
	   System.out.println("visit1");
	   return mv;
   }
}