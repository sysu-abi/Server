package com.c72;

import java.util.List;
import java.sql.Timestamp;

import javax.sql.DataSource;

public interface TaskDAO {
	/** 
	    * This is the method to be used to initialize
	    * database resources ie. connection.
	    */
	   public void setDataSource(DataSource ds);
	   /** 
	    * This is the method to be used to Create User.
	    * 
	    */
	   
	   //USER
	   //----
	   public void creatUser(String name, String phone,  String email, String password);
	   /** 
	    * This is the method to be used to list down
	    * a record from the User table corresponding
	    * to a passed user id.
	    */
	   public User getUserbyUid(Integer uid);
	   public User getUserbyName(String name);
	   /**
	    * This is method to be used to update user info
	    * in the User table.
	    */
	   public void updateUser(int uid, String name, String phone, String email, String password, int money, int credit);
	   /**
	    * This is method to be used to delete user 
	    * from the User table.
	    * @return
	    */
	   public void deleteUser(Integer uid);
	   
	   //TASK
	   //----
	   /** 
	    * This is the method to be used to Create Task.
	    * 
	    */
	   public void creatTask(int uid,String title,String detail,int money,String type,int total_num,Timestamp end_time,String state);
	   /** 
	    * This is the method to be used to list down
	    * a record from the Task table corresponding
	    * to a passed task id.
	    */
	   public Task getTask(Integer tid);
	   /** 
	    * This is the method to be used to list down
	    * a record from the Task table corresponding
	    * to a passed uid.
	    */
	   public List<Task> getpublishTask(Integer uid);
	   /**
	    * This is method to be used to update task info
	    * in the Task table.
	    */
	   public void updateTask(int tid,String title,String detail,int money,String type,int total_num,int current_num,Timestamp start_time,Timestamp end_time,String state);
	   /**
	    * This is method to be used to delete task 
	    * from the Task table.
	    * @return
	    */
	   public void deleteTask(Integer tid);
	   /**
	    * This is method to list all tasks
	    * from the Task table.
	    */
	   public List<Task> listTasks(String extra);
	   public List<Task> listTasksbyDLL(String mode, String extra);
	   public List<Task> listTasksbyMoney(String mode, String extra);
	   public List<Task> listTasksbyStartTime(String mode, String extra);
	   
	   public boolean isValid(Task task);
	   public List<Task> removeInvalid(List<Task> tasks);
	   
	   //MESSAGE
	   //-------
	   /** 
	    * This is the method to be used to Create message.
	    * 
	    */
	   public void createMessage(int tid, int uid, int rank, String detail);
	   /** 
	    * This is the method to be used to search message.
	    * 
	    */
	   public Message getMessage(int tid, int rank);
	   /**
	    * This is method to be used to delete task 
	    * from the Task table.
	    * @return
	    */
	   public void deleteMessage(Integer tid, int rank);
	   /**
	    * This is method to list all tasks
	    * from the Task table.
	    */
	   public List<Message> listMessages(Integer tid);
	   
	   //USERJOINS
	   //---------
	   /** 
	    * This is the method to be used to Create userjoins.
	    * 
	    */
	   public void creatUserJoins(int tid, int uid, Timestamp time);
	   /** 
	    * This is the method to be used to list down
	    * a record from the UserJoins table corresponding
	    * to a passed task id and user id.
	    */
	   public UserJoins getUserJoins(Integer tid, int uid);
	   /**
	    * This is method to be used to delete Joins 
	    * from the UserJoins table by task id.
	    * @return
	    */
	   public void deleteUserJoinsbyTid(Integer tid);
	   /**
	    * This is method to be used to delete joins
	    * from the UserJoins table by user id.
	    * @return
	    */
	   public void deleteUserJoinsbyUid(int tid, int uid);
	   /**
	    * This is method to list all tasks
	    * from the Task table by task id.
	    */
	   public List<UserJoins> listUserJoinsbyTid(Integer tid);
	   /**
	    * This is method to list all tasks
	    * from the Task table by user id.
	    */
	   public List<UserJoins> listUserJoinsbyUid(int uid);
	   
	 //Survey
	   //-------
	   /** 
	    * This is the method to be used to Create Survey.
	    * It will response a sid.
	    */
	   public void createSurvey(int tid);
	   /** 
	    * This is the method to be used to search Survey according to the tid.
	    * It will response a list of sid.
	    */
	   public List<Survey> getSurveybyTid(int tid);
	   /** 
	    * This is the method to be used to search Survey according to the sid.
	    * It will return the Survey.
		* @return
	    */
	   public Survey getSurveybySid(int sid);
	   /**
	    * This is method to be used to delete survey and related questions
	    * from the Survey table and the Question table.
	    * @return
	    */
		public void deleteSurvey(int sid);
	   
	   //Question
	   //-------
	   /** 
	    * This is the method to be used to Create Question.
	    * 
	    */
	   public void createQuestion(int sid,int qid,String qtype,String qtitle,String answer_a,String answer_b,String answer_c,String answer_d);
	   /** 
	    * This is the method to be used to search a list of Questions according to the sid.
	    * @return
	    */
	   public List<Question> getQuestionbySid(int sid);
	   /** 
	    * This is the method to be used to search Question according to the sid and qid.
	    * @return
	    */
	   public Question getQuestionbyId(int sid,int qid);
	   /**
	    * This is method to be used to delete questions
	    */
		public void deleteQuestion(int sid);
		
		//AnswerStatistics
		/** 
	    * This is the method to be used to Create AnswerStatistics.
	    * 
	    */
		public void createAnswerStatistics(int sid,int qid);
		 /** 
	    * This is the method to be used to search a list of AnswerStatistics according to the sid.
	    * @return
	    */
		public List<AnswerStatistics> getStatisticsbySid(int sid);
		/** 
	    * This is the method to be used to search AnswerStatistics according to the sid and qid.
	    * @return
	    */
		public AnswerStatistics getAnswerStatisticsbyID(int sid,int qid);
		/**
	    * This is method to be used to update AnswerStatistics info
	    * in the AnswerStatistics table.
	    */
		public void updateAnswerStatistics(int sid,int qid,int a,int b,int c,int d);
		/**
	    * This is method to be used to delete questions
	    */
		public void deleteAnswerStatistics(int sid);
}
