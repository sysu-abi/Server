package com.c72;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class TaskController{ 

   private ApplicationContext context = new ClassPathXmlApplicationContext("mySpring.xml");
   private TaskDAOImpl taskDAOImpl = 
   (TaskDAOImpl)context.getBean("TaskDAOImpl");   
   
   //闁烩偓鍔嶉崺娑㈡儎缁嬪灝褰�
   @RequestMapping(value = "/userRegist", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> userRegist(@RequestParam("name") String name,
		   								@RequestParam("phone") String phone,
		   								@RequestParam("email") String email,
		   								@RequestParam("password") String password
		   ) {
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   //婵☆偓鎷烽柡灞诲劜濡插憡绋夊鍡樞︾�规瓕灏欑划锛勶拷娑櫭﹢顏堝触鐏炶姤鍊抽柣鈧妽閸╋拷
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
	   //婵☆偓鎷烽柡灞诲劤閺併倝骞嬪畡鎵憼闁革讣鎷�
	   if(user==null) {
		   modelMap.put("status", "fail");
		   modelMap.put("log","User not exist.");
	   }else {
		   //閻庨潧妫涢悥婊堝礌瑜版帒甯�
		   if(user.getPassword().equals(password)) {
			   taskDAOImpl.deleteSession(user.getUid());
			   taskDAOImpl.createSession(user.getUid());
			   Session session = taskDAOImpl.getSessionbyUid(user.getUid());
			   int cookie = taskDAOImpl.Encode(session.getCookie());	   
			   modelMap.put("status", "success");
			   modelMap.put("user", user);
			   modelMap.put("cookie", cookie);
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
	   //婵☆偓鎷烽柡灞诲劤閺併倝骞嬪畡鎵憼闁革讣鎷�
	   if(user==null) {
		   modelMap.put("status", "fail");
		   modelMap.put("log","User not exist.");
	   }else {
		   //閻庨潧妫涢悥婊堝礌瑜版帒甯�
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
										@RequestParam("money") String money,
										@RequestParam("cookie") String cookie
										
		   ) {
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   User user=taskDAOImpl.getUserbyName(name);
	   //婵☆偓鎷烽柡灞诲劤閺併倝骞嬪畡鎵憼闁革讣鎷�
	   if(user==null) {
		   modelMap.put("status", "fail");
		   modelMap.put("log","User not exist.");
	   }
	   else if(!taskDAOImpl.CheckCookie(user.getUid(), cookie)){
		   modelMap.put("status", "fail");
		   modelMap.put("log","User no match.");
	   }else {		   	
		   taskDAOImpl.updateUser(user.getUid(), name, phone, email, password, Integer.valueOf(money), user.getCredit());
		   modelMap.put("status", "success");
	   }
	   return modelMap;
   }
   
   //闁哄被鍎撮妤佺鐠囨彃顫ら柣鈺冾焾閸欙拷
   @RequestMapping(value = "/listTasks", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> listTasks(@RequestParam("extra") String extra
		   ) {
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   List<Task> tasks = taskDAOImpl.listTasks(extra);
	   modelMap.put("taskList", tasks);
	   return modelMap;
   }
   
   //mode=ASC/DESC:闁告鎷�/闂傚嫬绉寸花锟�
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
   
   //闁告瑦鍨电粩鐑藉Υ娴ｇ懓澶嶉柛娆愮墧閹广垽宕濋敍鍕ゲ闁稿骏鎷�
   @RequestMapping(value = "/createTask", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> createTask(@RequestParam("cookie") String cookie,
		   								@RequestParam("uid") String uid,
		   								@RequestParam("title") String title,
		   								@RequestParam("detail") String detail,
		   								@RequestParam("money") String money,
		   								@RequestParam("type") String type,
		   								@RequestParam("total_num") String total_num,
		   								@RequestParam("end_time") String end_time
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   //婵☆偓鎷烽柡灞诲劙缂嶆垶锛愰敓锟�
	   User user=taskDAOImpl.getUserbyUid(Integer.valueOf(uid));
	   if(uid == null) {
		   modelMap.put("state", "fail");
		   modelMap.put("log", "User not exist.");
	   }	   
	   else if(!taskDAOImpl.CheckCookie(Integer.valueOf(uid), cookie)) {
		   modelMap.put("state", "fail");
		   modelMap.put("log", "No enough money.");
	   }
	   else if(user.getMoney()<Integer.valueOf(money)*Integer.valueOf(total_num)) {
		   modelMap.put("state", "fail");
		   modelMap.put("log", "No enough money.");
	   }else {//闁告帗绋戠紓鎾寸鐠囨彃顫ゆ鐐存构缁楁牠骞嶉敐澶嬬彑闂佹枻鎷�
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
		   								@RequestParam("end_time") String end_time,
		   								@RequestParam("cookie") String cookie
		   								
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   Task task=taskDAOImpl.getTask(Integer.valueOf(tid));
	   if(task==null) {
		   modelMap.put("state", "fail");
		   modelMap.put("log", "Task not exist.");
	   }else if(!taskDAOImpl.CheckCookie(Integer.valueOf(uid), cookie)) {
		   modelMap.put("state", "fail");
		   modelMap.put("log", "No enough money.");
	   }else {
		   taskDAOImpl.updateTask(Integer.valueOf(tid), title, detail, task.getMoney(), type, task.getTotal_num(), task.getCurrent_num(), task.getStart_time(), Timestamp.valueOf(end_time), task.getState());
		   modelMap.put("state", "success");
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/joinTask", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> joinTask(@RequestParam("tid") String tid,
		   							  @RequestParam("uid") String uid,
		   							  @RequestParam("cookie") String cookie
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   //婵☆偓鎷烽柡灞诲劙閹广垽宕濋敍鍕﹂柟顒婃嫹
	   Task task=taskDAOImpl.getTask(Integer.valueOf(tid));
	   if(task==null) {
		   modelMap.put("state", "fail");
		   modelMap.put("log", "Task not exist.");
	   }else if(!taskDAOImpl.CheckCookie(Integer.valueOf(uid), cookie)) {
		   modelMap.put("state", "fail");
		   modelMap.put("log", "No enough money.");
	   }else {
		   if(task.getState().equals("finished")) {
			   modelMap.put("state", "fail");
			   modelMap.put("log", "Task has finished.");
		   }else {
			   if(task.getCurrent_num()==task.getTotal_num()) {
				   modelMap.put("state", "fail");
				   modelMap.put("log", "Task join is full.");
			   }else {
				   UserJoins uj = taskDAOImpl.getUserJoins(Integer.valueOf(tid), Integer.valueOf(uid)); 
				   if(uj == null) {
					   taskDAOImpl.creatUserJoins(Integer.valueOf(tid), Integer.valueOf(uid), new Timestamp(new java.util.Date().getTime()));
					   taskDAOImpl.updateTask(task.getTid(), task.getTitle(), task.getDetail(), task.getMoney(), task.getType(), task.getTotal_num(), task.getCurrent_num()+1, task.getStart_time(), task.getEnd_time(), task.getState());
					   modelMap.put("state", "success");
				   }
				   else {
					   modelMap.put("state", "fail");
					   modelMap.put("log", "The User has already joint the Task.");
				   }   
			   }
		   }
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/disjoinTask", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> disjoinTask(@RequestParam("tid") String tid,
		   								 @RequestParam("uid") String uid,
		   								 @RequestParam("cookie") String cookie
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   //婵☆偓鎷烽柡灞诲劙閹广垽宕濋敍鍕﹂柟顒婃嫹
	   Task task=taskDAOImpl.getTask(Integer.valueOf(tid));
	   if(task==null) {
		   modelMap.put("state", "fail");
		   modelMap.put("log", "Task not exist.");
	   }else if(!taskDAOImpl.CheckCookie(Integer.valueOf(uid), cookie)){
		   modelMap.put("status", "fail");
		   modelMap.put("log","User no match.");
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
		   							 @RequestParam("uid") String uid,
		   							@RequestParam("cookie") String cookie
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   //婵☆偓鎷烽柡灞诲劙閹广垽宕濋敍鍕﹂柟顒婃嫹
	   Task task=taskDAOImpl.getTask(Integer.valueOf(tid));
	   if(task==null) {
		   modelMap.put("state", "fail");
		   modelMap.put("log", "Task not exist.");
	   }else if(!taskDAOImpl.CheckCookie(Integer.valueOf(uid), cookie)){
		   modelMap.put("status", "fail");
		   modelMap.put("log","User no match.");
	   }else {
		   if(task.getUid()!=Integer.valueOf(uid)) {
			   modelMap.put("state", "fail");
			   modelMap.put("log", "User mismatch publisher.");
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
	   return modelMap;
   }
   
   @RequestMapping(value = "/getJoinUsers", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> getJoinUsers(@RequestParam("tid") String tid
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   //婵☆偓鎷烽柡灞诲劙閹广垽宕濋敍鍕﹂柟顒婃嫹
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
   public Map<String,Object> getJoinTasks(@RequestParam("uid") String uid,
		   								  @RequestParam("cookie") String cookie
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   if(!taskDAOImpl.CheckCookie(Integer.valueOf(uid), cookie)){
		   modelMap.put("status", "fail");
		   modelMap.put("log","User no match.");
	   }
	   else {
		   //婵☆偓鎷烽柡灞诲劙閹广垽宕濋敍鍕﹂柟顒婃嫹
		   List<UserJoins> userJoins=taskDAOImpl.listUserJoinsbyUid(Integer.valueOf(uid));
		   List<Task> tasks=new ArrayList<Task>();
		   for(int i=0;i<userJoins.size();i++) {
			   UserJoins join=userJoins.get(i);
			   tasks.add(taskDAOImpl.getTask(join.getTid()));
		   }
		   modelMap.put("taskList", tasks);
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/getpublishTasks", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> getpublishTasks(@RequestParam("uid") String uid,
		   									 @RequestParam("cookie") String cookie
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   //婵☆偓鎷烽柡灞诲劙閹广垽宕濋敍鍕﹂柟顒婃嫹
	   User user = taskDAOImpl.getUserbyUid(Integer.valueOf(uid));
	   if(user == null) {
		   modelMap.put("state", "fail");
		   modelMap.put("log", "User not exist.");
	   }
	   else if(!taskDAOImpl.CheckCookie(Integer.valueOf(uid), cookie)){
		   modelMap.put("status", "fail");
		   modelMap.put("log","User no match.");
	   }
	   else {
		   List<Task> taskList = taskDAOImpl.getpublishTask(Integer.valueOf(uid));
		   modelMap.put("state", "success");
		   modelMap.put("taskList", taskList);
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/createMessage", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> createMessage( @RequestParam("tid") String tid,
		   									@RequestParam("uid") String uid,
		   									@RequestParam("detail") String detail,
		   									@RequestParam("cookie") String cookie
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   Task task=taskDAOImpl.getTask(Integer.valueOf(tid));
	   if(task == null){
		   modelMap.put("status", "fail");
		   modelMap.put("log","This Task do not exist.");
	   }
	   else if(!taskDAOImpl.CheckCookie(Integer.valueOf(uid), cookie)){
		   modelMap.put("status", "fail");
		   modelMap.put("log","User no match.");
	   }
	   else {
		   List<Message> messages=taskDAOImpl.listMessages(task.getTid());
		   taskDAOImpl.createMessage(task.getTid(), Integer.valueOf(uid), messages==null?1:messages.size()+1, detail);
		   modelMap.put("status", "success");
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/removeMessage", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> removeMessage( @RequestParam("tid") String tid,
		   									@RequestParam("rank") String rank
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   Task task=taskDAOImpl.getTask(Integer.valueOf(tid));
	   if(task == null){
		   modelMap.put("status", "fail");
		   modelMap.put("log","This Task do not exist.");
	   }
	   else {
		   taskDAOImpl.deleteMessage(Integer.valueOf(tid), Integer.valueOf(rank));
		   modelMap.put("status", "success");
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/listMessages", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> listMessages( @RequestParam("tid") String tid
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   Task task=taskDAOImpl.getTask(Integer.valueOf(tid));
	   if(task == null){
		   modelMap.put("status", "fail");
		   modelMap.put("log","This Task do not exist.");
	   }
	   else {
		   List<Message> messages=taskDAOImpl.listMessages(Integer.valueOf(tid));
		   modelMap.put("status", "success");
		   modelMap.put("messages", messages);
	   }
	   
	   return modelMap;
   }
   
   @RequestMapping(value = "/getMessage", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> getMessage( @RequestParam("tid") String tid,
										   @RequestParam("rank") String rank
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   Task task=taskDAOImpl.getTask(Integer.valueOf(tid));
	   if(task == null){
		   modelMap.put("status", "fail");
		   modelMap.put("log","This Task do not exist.");
	   }
	   else {
		   Message message=taskDAOImpl.getMessage(Integer.valueOf(tid),Integer.valueOf(rank));
		   if(message == null) {
			   modelMap.put("status", "fail");
			   modelMap.put("log","This Message do not exist.");
		   }
		   else {
			   int id = message.getUid();
			   User user = taskDAOImpl.getUserbyUid(id);
			   String name = user.getName();
			   modelMap.put("status", "success");
			   modelMap.put("message", message);
			   modelMap.put("name", name);
		   }
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/createSurvey", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> createSurvey( @RequestParam("tid") String tid
		   ) { 
		
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   Task task=taskDAOImpl.getTask(Integer.valueOf(tid));
	   if(task == null){
		   modelMap.put("status", "fail");
		   modelMap.put("log","This Task do not exist.");
	   }
	   else{
		   taskDAOImpl.createSurvey(Integer.valueOf(tid));
		   modelMap.put("status", "success");
	   }
	   return modelMap;
   }

   @RequestMapping(value = "/getSurveyList", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> getSurveyList( @RequestParam("tid") String tid
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();  
	   Task task=taskDAOImpl.getTask(Integer.valueOf(tid));
	   if(task == null){
		   modelMap.put("status", "fail");
		   modelMap.put("log","This Task do not exist.");
	   }
	   else{
		   List<Survey> surveyList=taskDAOImpl.getSurveybyTid(Integer.valueOf(tid));
		   modelMap.put("status", "success");
		   modelMap.put("survey", surveyList);		   
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/getSurvey", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> getSurvey( @RequestParam("sid") String sid
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   Survey survey=taskDAOImpl.getSurveybySid(Integer.valueOf(sid));
	   if(survey == null){
		   modelMap.put("status", "fail");
		   modelMap.put("log","This Survey do not exist.");
	   }
	   else{
		   List<Question> questionList= taskDAOImpl.getQuestionbySid(Integer.valueOf(sid));
		   modelMap.put("status", "success");
		   modelMap.put("survey", survey);
		   modelMap.put("questionList", questionList);	   
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/deleteSurvey", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> deleteSurvey( @RequestParam("sid") String sid
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   Survey survey=taskDAOImpl.getSurveybySid(Integer.valueOf(sid));
	   if(survey == null){
		   modelMap.put("status", "fail");
		   modelMap.put("log","This Survey do not exist.");
	   }
	   else{
		   taskDAOImpl.deleteAnswerStatistics(Integer.valueOf(sid));
		   taskDAOImpl.deleteQuestion(Integer.valueOf(sid));
		   taskDAOImpl.deleteSurvey(Integer.valueOf(sid));
		   modelMap.put("status", "success");
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/addQuestion", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> addQuestion( @RequestParam("sid") String sid,
		   									 @RequestParam("qid") String qid,
		   									 @RequestParam("qtype") String qtype,
		   									 @RequestParam("qtitle") String qtitle,
		   									 @RequestParam("answer_a") String answer_a,
		   								 	 @RequestParam("answer_b") String answer_b,
		   									 @RequestParam("answer_c") String answer_c,
		   								 	 @RequestParam("answer_d") String answer_d
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   Survey survey=taskDAOImpl.getSurveybySid(Integer.valueOf(sid));
	   if(survey == null){
		   modelMap.put("status", "fail");
		   modelMap.put("log","This Survey do not exist.");
	   }
	   else{
		   Question q = taskDAOImpl.getQuestionbyId(Integer.valueOf(sid), Integer.valueOf(qid));
		   if(q == null) {
			   taskDAOImpl.createQuestion(Integer.valueOf(sid), Integer.valueOf(qid), qtype, qtitle, answer_a, answer_b, answer_c, answer_d);
			   taskDAOImpl.createAnswerStatistics(Integer.valueOf(sid), Integer.valueOf(qid));
			   modelMap.put("status", "success");
		   }
		   else {
			   modelMap.put("status", "fail");
			   modelMap.put("log","This Qid is already exist.");
		   }	   
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/getAnswers", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> getAnswers( @RequestParam("sid") String sid
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   Survey survey=taskDAOImpl.getSurveybySid(Integer.valueOf(sid));
	   if(survey == null){
		   modelMap.put("status", "fail");
		   modelMap.put("log","This Survey do not exist.");
	   }
	   else{
		   List<AnswerStatistics> statistics = taskDAOImpl.getStatisticsbySid(Integer.valueOf(sid));
		   modelMap.put("status", "succes");
		   modelMap.put("statistics", statistics);	   
	   }
	   return modelMap;
   }
   
   @RequestMapping(value = "/updateAnswers", method = RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> updateAnswers( @RequestParam("sid") String sid,
													 @RequestParam("qid") String qid,
													 @RequestParam("answer") String answer
		   ) { 
	   Map<String, Object> modelMap=new HashMap<String, Object>();
	   Survey survey=taskDAOImpl.getSurveybySid(Integer.valueOf(sid));
	   if(survey == null){
		   modelMap.put("status", "fail");
		   modelMap.put("log","This Survey do not exist.");
	   }
	   else if(!(answer.equals("A")||answer.equals("B")||answer.equals("C")||answer.equals("D"))) {
		   modelMap.put("status", "fail");
		   modelMap.put("log","The Answer should be A,B,C or D");
	   }
	   else{
		   Question question = taskDAOImpl.getQuestionbyId(Integer.valueOf(sid),Integer.valueOf(qid));
		   if(question == null){
			   modelMap.put("status", "fail");
			   modelMap.put("log","This Question do not exist.");
		   }
		   else{
			   AnswerStatistics statistics = taskDAOImpl.getAnswerStatisticsbyID(Integer.valueOf(sid),Integer.valueOf(qid));
			   int a = statistics.getCountA();
			   int b = statistics.getCountB();
			   int c = statistics.getCountC();
			   int d = statistics.getCountD();
			   if(answer.equals("A")) a = a + 1;
			   if(answer.equals("B")) b = b + 1;
			   if(answer.equals("C")) c = c + 1;
			   if(answer.equals("D")) d = d + 1;
			   taskDAOImpl.updateAnswerStatistics(Integer.valueOf(sid),Integer.valueOf(qid),a,b,c,d);
			   modelMap.put("status", "success");
		   }  
	   }
	   return modelMap;
   }
}