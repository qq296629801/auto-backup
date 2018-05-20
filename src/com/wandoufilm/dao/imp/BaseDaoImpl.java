package com.wandoufilm.dao.imp;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wandoufilm.dao.BaseDao;
import com.wandoufilm.util.Dbcon;
import com.wandoufilm.util.ReflectMatch;

public class BaseDaoImpl implements BaseDao {
	public Statement st;

	@Override
	public int save(Object object) throws SQLException {
		try {
			int rs = 0;
			Class clazz = object.getClass();
			st = Dbcon.getConnection();
			String table = clazz.getSimpleName();
			String sql = "INSERT INTO " + table.toLowerCase();
			String tableName = " (";
			String values = "VALUES (";
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				String key = fields[i].getName();
				PropertyDescriptor descriptor = new PropertyDescriptor(key, clazz);
				Method method = descriptor.getReadMethod();
				Object value = method.invoke(object);
				String type = fields[i].getType().getName();

				if (i == fields.length - 1) {
					tableName += key + ") ";
					if ("java.lang.String".equals(type) || "java.util.Date".equals(type)
							|| "java.sql.Timestamp".equals(type)) {
						if (value == null) {
							values += value + ")";
						} else {
							values += "'" + value + "')";
						}
					} else {
						values += value + ")";
					}
				} else if (i != 0 && value != null) {
					tableName += fields[i].getName() + ",";
					if ("java.lang.String".equals(type) || "java.util.Date".equals(type)
							|| "java.sql.Timestamp".equals(type)) {
						values += "'" + value + "',";
					} else {
						values += value + ",";
					}

				}
			}
			sql += tableName + values;
			System.out.println(sql);
			rs = st.executeUpdate(sql);
			st.close();
			Dbcon.close();
			return rs;
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int delete(Object object, int id) throws SQLException {
		Class clazz = object.getClass();
		st = Dbcon.getConnection();
		String table = clazz.getSimpleName();
		String sql = "DELETE FROM " + table.toLowerCase() + " WHERE id=" + id;
		int rs = st.executeUpdate(sql);
		return rs;
	}

	@Override
	public int update(Object object, int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object findById(Object object, int id) throws SQLException {
		st = Dbcon.getConnection();
		Class clazz = object.getClass();
		String sql = "select * from " + clazz.getSimpleName().toLowerCase() + " where id=" + id;
		ResultSet rs;
		rs = st.executeQuery(sql);
		if (rs.next()) {
			ReflectMatch reflectMatch = new ReflectMatch();
			Map map = new HashMap();
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				String key = fields[i].getName();
				map.put(key, rs.getObject(key));
				System.out.println(key + " " + rs.getObject(key));
			}
			reflectMatch.setValue(object, map);
		}
		return object;
	}

	@Override
	public List<Object> findAll(Object object) throws SQLException {
		List<Object> list = new ArrayList<Object>();
		st = Dbcon.getConnection();
		Class clazz = object.getClass();
		String sql = "select * from " + clazz.getSimpleName().toLowerCase() + "";
		ResultSet rs;
		rs = st.executeQuery(sql);
		while (rs.next()) {
			ReflectMatch reflectMatch = new ReflectMatch();
			Map map = new HashMap();
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				String key = fields[i].getName();
				map.put(key, rs.getObject(key));
				System.out.println(key + " " + rs.getObject(key));
			}
			reflectMatch.setValue(object, map);
			list.add(object);
		}
		return list;
	}

}
