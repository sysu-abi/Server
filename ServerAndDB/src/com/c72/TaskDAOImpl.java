package com.c72;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import Mybatis_sqlMap.Mybatis_UserMapper;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TaskDAOImpl implements TaskDAO {
	   private DataSource dataSource;
	   private JdbcTemplate jdbcTemplateObject; 
	   //hibernate的工厂
	   private SessionFactory factory; 
	   //mybatis的工厂
	   private SqlSessionFactory sqlSessionFactory;
	@Override
	@Autowired
	public void setDataSource(DataSource ds) {
		// TODO Auto-generated method stub
		dataSource=ds;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
		try{
			factory = new Configuration().configure().buildSessionFactory();
	    }catch (Throwable ex) { 
	    	System.err.println("Failed to create sessionFactory object." + ex);
	        throw new ExceptionInInitializerError(ex); 
	    }
		try {
			sqlSessionFactory=new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("sqlMapConfig.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		//String SQL = "select * from User where uid = ?";
		User user=null;
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         user = session.get(User.class, uid); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
		/*try {
		    user = jdbcTemplateObject.queryForObject(SQL, 
		                        new Object[]{uid}, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			user=null;
			// TODO: handle exception
		}*/
	      return user;
	}
	
	@Override
	public User getUserbyName(String name) {
		// TODO Auto-generated method stub
		//JDBCTemplate实现
		//--------------
		//String SQL = "select * from User where name = ?";
		/*try {
	    user = jdbcTemplateObject.queryForObject(SQL, 
	                        new Object[]{name}, new UserMapper());
	}catch (EmptyResultDataAccessException e) {
		e.printStackTrace();
		user=null;
		// TODO: handle exception
	}*/
		//Hibernate实现
		//-----------
		User user=null;
		/*Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         //charVar类型/String类型要用单引号括起，否则会出错
	         Query<User> q=session.createQuery("from User user where user.name='"+name+"'");
	         List<User> users=q.list();
	         if(users.size()>0) {
	        	 user=users.get(0);
	         }
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      return user;*/
	    //mybatis低级实现
	    //------------
		/*SqlSession sqlSession= sqlSessionFactory.openSession();
	    user=sqlSession.selectOne("test.findUserByName",name);//只有一行代码。。对比太过明显
	    sqlSession.close();
	    return user;*/
	    //mybatis高级实现
		//直接对接口进行操作，符合OOP习惯
		SqlSession sqlSession= sqlSessionFactory.openSession();
		Mybatis_UserMapper userMapper=sqlSession.getMapper(Mybatis_UserMapper.class);
		user=userMapper.findUserByName(name);
	    sqlSession.close();
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
		String SQL="select * from Task ";
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

}
