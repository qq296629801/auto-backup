package com.wandoufilm.dao.imp;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.wandoufilm.dao.BaseDao;
import com.wandoufilm.model.Blog;
import com.wandoufilm.model.Model;
import com.wandoufilm.model.Post;
import com.wandoufilm.util.Dbcon;

import cm.ymsys.service.BaseService;

public class BaseDaoImpl implements BaseDao {
	public Statement st;

	public static void main(String[] args) throws SQLException, IntrospectionException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
		Post post = new Post();
		post.setContent("324234");
		post.setCreateTime(sdf.format(new Date()));
		post.setImg("32423");
		post.setTitle("4324");

		Model model = new Model();
		model.setName("4234");
		model.setPid(1);
		model.setTime(sdf.format(new Date(0)));
		model.setUrl("3243");

		Blog blog = new Blog();
		blog.setCar_id(4324);
		blog.setUser_id(1);

		BaseService bs = BaseService.getInstance();
		bs.add(post);
		bs.add(model);
		bs.add(blog);
	}

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
				value = value == null ? "" : value;
				if (i == fields.length - 1) {
					tableName += key + ") ";
					values += "'" + value + "')";
				} else if (i != 0) {
					tableName += fields[i].getName() + ",";
					if (fields[i].getType().getSimpleName().equals("int")
							|| fields[i].getType().getSimpleName().equals("double")) {
						values += value + ",";
					} else {
						values += "'" + value + "',";
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int delete(Object clazz, int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Object clazz, int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
