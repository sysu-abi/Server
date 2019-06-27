package com.c72;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
public class QuestionMapper implements RowMapper<Question> {
   public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
      Question questoin=new Question();
      questoin.setSid(rs.getInt("sid"));
	  questoin.setQid(rs.getInt("qid"));
	  questoin.setQtype(rs.getString("qtype"));
      questoin.setQtitle(rs.getString("qtitle"));
	  questoin.setAnswerA(rs.getString("Answer_a"));
	  questoin.setAnswerB(rs.getString("Answer_b"));
	  questoin.setAnswerC(rs.getString("Answer_c"));
	  questoin.setAnswerD(rs.getString("Answer_d"));
      return questoin;
   }
}