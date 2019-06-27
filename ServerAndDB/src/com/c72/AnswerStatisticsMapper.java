package com.c72;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
public class AnswerStatisticsMapper implements RowMapper<AnswerStatistics> {
   public AnswerStatistics mapRow(ResultSet rs, int rowNum) throws SQLException {
      AnswerStatistics statistics=new AnswerStatistics();   
      statistics.setSid(rs.getInt("sid"));
	  statistics.setQid(rs.getInt("qid"));
	  statistics.setCountA(rs.getInt("count_a"));
	  statistics.setCountB(rs.getInt("count_b"));
	  statistics.setCountC(rs.getInt("count_c"));
	  statistics.setCountD(rs.getInt("count_d"));
      return statistics;
   }
}