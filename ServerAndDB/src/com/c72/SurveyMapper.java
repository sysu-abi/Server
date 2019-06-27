package com.c72;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
public class SurveyMapper implements RowMapper<Survey> {
   public Survey mapRow(ResultSet rs, int rowNum) throws SQLException {
      Survey survey=new Survey();
      survey.setTid(rs.getInt("tid"));
      survey.setSid(rs.getInt("sid"));
      return survey;
   }
}