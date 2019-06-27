package com.c72;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
public class SessionMapper implements RowMapper<Session> {
   public Session mapRow(ResultSet rs, int rowNum) throws SQLException {
	   Session session=new Session();
      session.setCookie(rs.getInt("cookie"));
      session.setUid(rs.getInt("uid"));
      return session;
   }
}