package com.wandoufilm.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Dbcon {

	public static String url = "jdbc:mysql://qdm19334814.my3w.com:3306/qdm19334814_db?user=qdm19334814&password=296629801&useUnicode=true&characterEncoding=UTF8";
	public static Connection conn;
	public static Statement stmt;

	public static Statement getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			return stmt;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void close() {
		if (conn != null || stmt != null) {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
