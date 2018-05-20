package com.wandoufilm.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ReflectMatch {

	// 封装属性
	private Map<String, Field> fieldMap = new HashMap<String, Field>();
	// 封装属性的set方法
	private Map<String, Method> methodMap = new HashMap<String, Method>();

	/*
	 * c:为赋值对象， values为给属性要赋的值，put('m1',1);put('m2',2),m1、m2等为属性
	 */
	public void setValue(Object c, Map values) {
		try {
			Field[] fields = c.getClass().getDeclaredFields();

			Method[] methods = c.getClass().getDeclaredMethods();

			for (Field field : fields) {
				String attri = field.getName();
				fieldMap.put(attri.toLowerCase(), field);
				for (Method method : methods) {
					String meth = method.getName();
					// 匹配set方法
					if (meth != null && "set".equals(meth.substring(0, 3)) && Modifier.isPublic(method.getModifiers())
							&& ("set" + Character.toUpperCase(attri.charAt(0)) + attri.substring(1)).equals(meth)) {
						methodMap.put(attri.toLowerCase(), method);
						break;
					}
				}
			}

			// 2、属性赋值
			for (Iterator it = values.keySet().iterator(); it.hasNext();) {
				String name = (String) it.next();
				Object value = values.get(name);

				if (value == null)
					continue;
				name = name.trim();

				Field field = fieldMap.get(name.toLowerCase());
				if (field == null)
					continue;
				Method method = methodMap.get(name.toLowerCase());
				if (method == null)
					continue;
				fill(c, field, method, value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将字符串值转换为合适的值填充到对象的指定域
	 * 
	 * @param bean
	 *            被填充的java bean
	 * @param field
	 *            需要填充的域
	 * @param value
	 *            字符串值
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void fill(Object bean, Field field, Method method, Object value)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (value == null)
			return;
		method.invoke(bean, value);
	}
}
