package com.wandoufilm.service;

import java.sql.SQLException;
import java.util.List;

import com.wandoufilm.dao.imp.BaseDaoImpl;

public class BaseService {
	private static BaseService instance = new BaseService();

	public static BaseService getInstance() {
		return instance;
	}

	private BaseDaoImpl dao = new BaseDaoImpl();

	public void add(Object object) throws SQLException {
		dao.save(object);
	}

	public Object findById(Object object, int id) throws SQLException {
		return dao.findById(object, id);
	}

	public List<Object> findAll(Object object) throws SQLException {
		return dao.findAll(object);
	}

}
