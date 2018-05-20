package com.wandoufilm.dao;

import java.sql.SQLException;

public interface BaseDao {

	public int save(Object object) throws SQLException;

	public int delete(Object object, int id) throws SQLException;

	public int update(Object object, int id) throws SQLException;

	public Object findById(Object object, int id) throws SQLException;

}
