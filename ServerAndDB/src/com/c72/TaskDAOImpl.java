package com.c72;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class TaskDAOImpl implements TaskDAO {
	   private DataSource dataSource;
	   private JdbcTemplate jdbcTemplateObject; 
	@Override
	@Autowired
	public void setDataSource(DataSource ds) {
		// TODO Auto-generated method stub
		dataSource=ds;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	@Override
	public void creatUser(String name, String phone, String email, String password) {
		// TODO Auto-generated method stub
		String SQL = "insert into User (name, phone, email, password) values (?, ?, ?, ?)";     
	      jdbcTemplateObject.update( SQL, name, phone, email, password);
	}

	@Override
	public User getUserbyUid(Integer uid) {
		// TODO Auto-generated method stub
		String SQL = "select * from User where uid = ?";
		User user;
		try {
		    user = jdbcTemplateObject.queryForObject(SQL, 
		                        new Object[]{uid}, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			user=null;
			// TODO: handle exception
		}
	      return user;
	}
	
	@Override
	public User getUserbyName(String name) {
		// TODO Auto-generated method stub
		String SQL = "select * from User where name = ?";
		User user;
		try {
		    user = jdbcTemplateObject.queryForObject(SQL, 
		                        new Object[]{name}, new UserMapper());
		}catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			user=null;
			// TODO: handle exception
		}
	      return user;
	}

	@Override
	public void updateUser(int uid, String name, String phone, String email, String password, int money, int credit) {
		// TODO Auto-generated method stub
		String SQL = "update User set name = ?, phone = ?, email = ?, password = ?, money = ?, credit = ? where uid = ?";
	      jdbcTemplateObject.update(SQL, name, phone, email, password, money, credit, uid);
	}

	@Override
	public void deleteUser(Integer uid) {
		// TODO Auto-generated method stub
		String SQL = "delete from User where uid = ?";
	      jdbcTemplateObject.update(SQL, uid);
	      return;
	}

	@Override
	public void creatTask(int uid, String title, String detail, int money, String type, int total_num,
			Timestamp end_time, String state) {
		// TODO Auto-generated method stub
		String SQL = "insert into Task (uid, title, detail, money, type, total_num, start_time, end_time, state) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";     
	      jdbcTemplateObject.update( SQL, uid, title, detail, money, type, total_num, new Timestamp(new java.util.Date().getTime()), end_time, state);
	}

	@Override
	public Task getTask(Integer tid) {
		// TODO Auto-generated method stub
		String SQL = "select * from Task where tid = ?";
		Task task;
		try {
		    task = jdbcTemplateObject.queryForObject(SQL, 
                    new Object[]{tid}, new TaskMapper());
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			task=null;
			// TODO: handle exception
		}
	      return task;
	}

	@Override
	public List<Task> getpublishTask(Integer uid) {
		// TODO Auto-generated method stub
		String SQL = "select * from Task where uid = ?";
		List<Task> taskList;
		try {
		    taskList = jdbcTemplateObject.query(SQL, new Object[]{uid}, new TaskMapper());
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			taskList=null;
			// TODO: handle exception
		}
	      return taskList;
	}
	
	@Override
	public void updateTask(int tid, String title, String detail, int money, String type, int total_num, int current_num,
			Timestamp start_time, Timestamp end_time, String state) {
		// TODO Auto-generated method stub
		String SQL = "update Task set title = ?, detail = ?, money = ?, type = ?, total_num = ?, "
				+ "current_num = ?, start_time = ?, end_time = ?, state = ? where tid = ?";
	      jdbcTemplateObject.update(SQL, title, detail, money, type, total_num, current_num, start_time, end_time, state, tid);
	}

	@Override
	public void deleteTask(Integer tid) {
		// TODO Auto-generated method stub
		String SQL = "delete from Task where tid = ?";
		jdbcTemplateObject.update(SQL,tid);
	}
	
	@Override
	public boolean isValid(Task task) {
		// TODO Auto-generated method stub
		return !(task.getState().equals("finished") || task.getEnd_time().getTime() < new java.util.Date().getTime());
	}
	
	@Override
	public List<Task> removeInvalid(List<Task> tasks) {
		// TODO Auto-generated method stub
		for(int i=0;i<tasks.size();i++) {
			//除去已完成的task
			if(!isValid(tasks.get(i))) {
				tasks.remove(i);
				i--;
			}
		}
		return tasks;
	}

	@Override
	public List<Task> listTasks(String extra) {
		// TODO Auto-generated method stub
		String SQL="select * from Task";
		if(extra.equals("true")) {
			SQL+="where state = 'extra' ";
		}
		List<Task> tasks=jdbcTemplateObject.query(SQL, new TaskMapper());
		return removeInvalid(tasks);
	}
	
	@Override
	public List<Task> listTasksbyDLL(String mode, String extra) {
		// TODO Auto-generated method stub
		String SQL="select * from Task ";
		if(extra.equals("true")) {
			SQL+="where state = 'extra' ";
		}
		SQL+="order by end_time " + mode;
		List<Task> tasks=jdbcTemplateObject.query(SQL, new TaskMapper());
		return removeInvalid(tasks);
	}
	
	@Override
	public List<Task> listTasksbyMoney(String mode, String extra) {
		// TODO Auto-generated method stub
		String SQL="select * from Task ";
		if(extra.equals("true")) {
			SQL+="where state = 'extra' ";
		}
		SQL+="order by money " + mode;
		List<Task> tasks=jdbcTemplateObject.query(SQL, new TaskMapper());
		return removeInvalid(tasks);
	}
	
	@Override
	public List<Task> listTasksbyStartTime(String mode, String extra) {
		// TODO Auto-generated method stub
		String SQL="select * from Task ";
		if(extra.equals("true")) {
			SQL+="where state = 'extra' ";
		}
		SQL+="order by start_time " + mode;
		List<Task> tasks=jdbcTemplateObject.query(SQL, new TaskMapper());
		return removeInvalid(tasks);
	}

	@Override
	public void createMessage(int tid, int uid, int rank, String detail) {
		// TODO Auto-generated method stub
		String SQL = "insert into Message (tid, uid, time, floor, detail) values(?, ?, ?, ?, ?)";
		jdbcTemplateObject.update(SQL, tid, uid, new Timestamp(new Date().getTime()), rank, detail);
	}
	
	@Override
	public Message getMessage(int tid, int rank) {
		// TODO Auto-generated method stub
		String SQL = "select * from Message where tid = ? and floor = ?";
		Message message;
		try {
			message=jdbcTemplateObject.queryForObject(SQL, new Object[] {tid, rank},new MessageMapper());
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			message=null;
			// TODO: handle exception
		}
		return message;
	}
	
	@Override
	public void deleteMessage(Integer tid, int rank) {
		// TODO Auto-generated method stub
		String SQL = "delete from Message where tid = ? and floor = ?";
		jdbcTemplateObject.update(SQL, tid, rank);
	}

	@Override
	public List<Message> listMessages(Integer tid) {
		// TODO Auto-generated method stub
		String SQL="select * from Message where tid = ?";
		List<Message> messages=jdbcTemplateObject.query(SQL, new Object[] {tid}, new MessageMapper());
		return messages;
	}

	@Override
	public void creatUserJoins(int tid, int uid, Timestamp time) {
		// TODO Auto-generated method stub
		String SQL="insert into UserJoins (tid, uid, time) values(?, ?, ?)";
		jdbcTemplateObject.update(SQL, tid, uid, time);
	}

	@Override
	public UserJoins getUserJoins(Integer tid, int uid) {
		// TODO Auto-generated method stub
		String SQL="select * from UserJoins where tid = ? and uid = ?";
		UserJoins userJoins;
		try {
			userJoins=jdbcTemplateObject.queryForObject(SQL, new Object[] {tid,uid}, new UserJoinsMapper());
		} catch (Exception e) {
			e.printStackTrace();
			userJoins=null;
			// TODO: handle exception
		}
		return userJoins;
	}

	@Override
	public void deleteUserJoinsbyTid(Integer tid) {
		// TODO Auto-generated method stub
		String SQL="delete from UserJoins where tid = ?";
		jdbcTemplateObject.update(SQL, tid);
	}

	@Override
	public void deleteUserJoinsbyUid(int tid, int uid) {
		// TODO Auto-generated method stub
		String SQL="delete from UserJoins where tid = ? and uid = ?";
		jdbcTemplateObject.update(SQL, tid, uid);
	}

	@Override
	public List<UserJoins> listUserJoinsbyTid(Integer tid) {
		// TODO Auto-generated method stub
		String SQL="select * from UserJoins where tid = ?";
		List<UserJoins> userJoins=jdbcTemplateObject.query(SQL,new Object[] {tid},new UserJoinsMapper());
		return userJoins;
	}

	@Override
	public List<UserJoins> listUserJoinsbyUid(int uid) {
		// TODO Auto-generated method stub
		String SQL="select * from UserJoins where uid = ?";
		List<UserJoins> userJoins=jdbcTemplateObject.query(SQL,new Object[] {uid},new UserJoinsMapper());
		return userJoins;
	}
	
	//Survey
	@Override
	public void createSurvey(int tid) {
		// TODO Auto-generated method stub
		String SQL = "insert into Survey (tid) values (?)";     
	    jdbcTemplateObject.update( SQL, tid);
	}
	
	@Override
	public List<Survey> getSurveybyTid(int tid) {
		// TODO Auto-generated method stub
		String SQL="select * from Survey where tid = ?";    
		List<Survey> surveyList;
		try {
			surveyList=jdbcTemplateObject.query(SQL,new Object[] {tid},new SurveyMapper());
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			surveyList=null;
			// TODO: handle exception
		}	
	    return surveyList;
	}
	
	@Override
	public Survey getSurveybySid(int sid) {
		// TODO Auto-generated method stub
		String SQL="select * from Survey where sid = ?";    
		Survey survey;
		try {
			survey=jdbcTemplateObject.queryForObject(SQL,new Object[] {sid},new SurveyMapper());
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			survey=null;
			// TODO: handle exception
		}
		
	    return survey;
	}
	
	@Override
	public void deleteSurvey(int sid) {
		// TODO Auto-generated method stub
		String SQL="delete from Survey where sid = ?";
		jdbcTemplateObject.update(SQL, sid);
	}

	//Question
	@Override
	public void createQuestion(int sid,int qid,String qtype,String qtitle,String answer_a,String answer_b,String answer_c,String answer_d) {
		// TODO Auto-generated method stub
		String SQL = "insert into Question (sid, qid, qtype, qtitle, answer_a, answer_b, answer_c, answer_d) values (?,?,?,?,?,?,?,?)";     
	    jdbcTemplateObject.update( SQL, sid, qid, qtype, qtitle, answer_a, answer_b, answer_c, answer_d);
	}
	
	@Override
	public List<Question> getQuestionbySid(int sid) {
		// TODO Auto-generated method stub
		String SQL="select * from Question where sid = ?";    
		List<Question> questionList;
		try {
			questionList=jdbcTemplateObject.query(SQL,new Object[] {sid},new QuestionMapper());
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			questionList=null;
			// TODO: handle exception
		}	
	    return questionList;
	}
	
	@Override
	public Question getQuestionbyId(int sid,int qid) {
		// TODO Auto-generated method stub
		String SQL="select * from Question where sid = ? and qid = ?";    
		Question question;
		try {
			question=jdbcTemplateObject.queryForObject(SQL,new Object[] {sid,qid},new QuestionMapper());
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			question=null;
			// TODO: handle exception
		}
	    return question;
	}
	
	@Override
	public void deleteQuestion(int sid) {
		// TODO Auto-generated method stub
		String SQL="delete from Question where sid = ?";
		jdbcTemplateObject.update(SQL, sid);
	}
	
	//AnswerStatistics
	@Override
	public void createAnswerStatistics(int sid,int qid) {
		// TODO Auto-generated method stub
		String SQL = "insert into AnswerStatistics (sid, qid, count_a, count_b, count_c, count_d) values (?,?,?,?,?,?)";     
	    jdbcTemplateObject.update( SQL, sid, qid, 0, 0, 0, 0);
	}
	
	@Override
	public List<AnswerStatistics> getStatisticsbySid(int sid) {
		// TODO Auto-generated method stub
		String SQL = "select * from AnswerStatistics where sid = ?";        
		List<AnswerStatistics> statistics;
		try {
			statistics=jdbcTemplateObject.query(SQL,new Object[] {sid},new AnswerStatisticsMapper());
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			statistics=null;
			// TODO: handle exception
		}	
	    return statistics;
	}
	
	@Override
	public AnswerStatistics getAnswerStatisticsbyID(int sid,int qid) {
		// TODO Auto-generated method stub
		String SQL="select * from AnswerStatistics where sid = ? and qid = ?";    
		AnswerStatistics statistics;
		try {
			statistics=jdbcTemplateObject.queryForObject(SQL,new Object[] {sid,qid},new AnswerStatisticsMapper());
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			statistics=null;
			// TODO: handle exception
		}
	    return statistics;
	}
	
	@Override
	public void updateAnswerStatistics(int sid,int qid,int count_a,int count_b,int count_c,int count_d){
		// TODO Auto-generated method stub
		String SQL = "update AnswerStatistics set count_a = ?, count_b = ?, count_c = ?, count_d = ? where sid = ? and qid = ?";
	    jdbcTemplateObject.update(SQL, count_a, count_b, count_c, count_d, sid, qid);
	}
	
	@Override
	public void deleteAnswerStatistics(int sid) {
		// TODO Auto-generated method stub
		String SQL="delete from AnswerStatistics where sid = ?";
		jdbcTemplateObject.update(SQL, sid);
	}

}
