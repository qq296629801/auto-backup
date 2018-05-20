package com.wandoufilm.dao.imp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.wandoufilm.model.Admin;
import com.wandoufilm.util.Dbcon;

public class AdminDaoImpl extends BaseDaoImpl {
	public Statement st;

	public Admin find(String name, String passwd) throws SQLException {
		Admin admin = new Admin();
		st = Dbcon.getConnection();
		String sql = "select * from admin where user_name='" + name + "' and pass_word='" + passwd + "'";
		ResultSet rs = st.executeQuery(sql);
		if (rs.next()) {
			admin.setId(rs.getInt("id"));
			admin.setNick_name(rs.getString("nick_name"));
			admin.setPass_word(rs.getString("pass_word"));
			admin.setUser_name(rs.getString("user_name"));
			admin.setRemark(rs.getString("remark"));
		}
		st.close();
		Dbcon.close();
		return admin;
	}

	public boolean findUserName(String name) throws SQLException {
		st = Dbcon.getConnection();
		String sql = "select * from admin where user_name='" + name + "'";
		ResultSet rs = st.executeQuery(sql);
		if (rs.next()) {
			return true;
		}
		st.close();
		Dbcon.close();
		return false;
	}
}
