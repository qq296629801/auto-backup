package com.wandoufilm.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @category 代码生成器工具类
 * @author mjy
 * @version 1.0
 */
public class GeneratorTool {
	public static final String url = "jdbc:mysql://qdm19334814.my3w.com/information_schema";
	public static final String name = "com.mysql.jdbc.Driver";
	public static final String user = "qdm19334814";
	public static final String password = "296629801";
	public static final String tableName = "blog";
	public static final String dataBase = "qdm19334814_db";
	public static final String filePath = "";
	public static boolean dataFlag = true;
	public static boolean timeStampflag = true;
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args) throws IOException {
		new GeneratorTool().start();
	}

	/**
	 * @category 开始生成
	 * @throws IOException
	 */
	public void start() throws IOException {
		List<Table> list = new TableDao().findByDataBase(dataBase);
		generatorBean(list);
	}

	/**
	 * @throws IOException
	 * @category 创建实体类
	 */
	private void generatorBean(List<Table> list) throws IOException {
		StringBuilder code = new StringBuilder();
		/*********************** 导入包 **********************/
		code.append("package com.wandoufilm.model;\n");
		code.append("import java.io.Serializable;\n");
		for (Table table : list) {
			if (table.getName().equals(tableName)) {
				if (table.getType().equals("date")) {
					if (dataFlag) {
						code.append("import java.util.Date;\n");
						dataFlag = false;
					}
				}
				if (table.getType().equals("datetime")) {
					if (timeStampflag) {
						code.append("import java.sql.Timestamp;\n");
						timeStampflag = false;
					}

				}
			}
		}
		code.append("/**\n");
		code.append(" * @category Generated " + sdf.format(new Date()) + " by GeneratedTool\n");
		code.append(" * @author mjy\n");
		code.append(" */\n");
		code.append("public class " + firstWordCapital(tableName) + " implements Serializable {\n");
		code.append("private int id;\n");
		code.append("public int getId() {\n");
		code.append(" return id;\n");
		code.append("}\n");
		code.append("public void setId(int id) {\n");
		code.append(" this.id = id;\n");
		code.append("}\n");
		for (Table table : list) {
			if (table.getName().equals(tableName) && !table.getKey().equals("PRI")) {
				/*********************** 定义字段 **********************/
				code.append("private ");
				code.append(getSwithType(table));
				// String[] arrs = table.getColumn().split("_");
				// if (arrs.length > 1) {
				// code.append(getFirstWordLower(table.getColumn()));
				// } else {
				// code.append(table.getColumn());
				// }
				code.append(table.getColumn());

				code.append(";\n");
				code.append("// " + table.getComment() + "\n");
				/*********************** get方法 **********************/
				code.append("public ");
				code.append(getSwithType(table));
				code.append("get" + captureName(table.getColumn()) + "(){\n");
				code.append(" return " + lowerName(table.getColumn()) + ";\n");
				code.append("}\n");
				code.append("\n");
				/*********************** set方法 **********************/
				code.append("public void ");
				code.append("set" + captureName(table.getColumn()) + "(");
				code.append(getSwithType(table));
				code.append(lowerName(table.getColumn()));
				code.append("){\n");
				code.append(" this." + lowerName(table.getColumn()) + "=" + lowerName(table.getColumn()) + ";\n");
				code.append("}\n");
				code.append("\n");
			}
		}
		// 最后大括号结尾
		code.append("}");
		/******************* 生成文件 **************************/
		createFile(code.toString());
		System.out.println("创建Bean层文件成功！");
	}

	/**
	 * @category 得到第一个单词小写的字符串
	 * @param str
	 * @return
	 */
	private String firstWordLower(String str) {
		String[] arrTableNames = str.split("_");
		str = "";
		for (int i = 0; i < arrTableNames.length; i++) {
			char first = arrTableNames[i].charAt(0);
			if (i != 0) {
				first -= 32;
			}
			str += first + arrTableNames[i].substring(1);
		}
		return str;
	}

	/**
	 * @category 得到第一个单词大写的字符串
	 * @param str
	 * @return
	 */
	private String firstWordCapital(String str) {
		String[] arrTableNames = str.split("_");
		str = "";
		for (int i = 0; i < arrTableNames.length; i++) {
			char first = arrTableNames[i].charAt(0);
			first -= 32;
			str += first + arrTableNames[i].substring(1);
		}
		return str;
	}

	public static String captureName(String name) {
		char[] cs = name.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);
	}

	public static String lowerName(String name) {
		char[] cs = name.toCharArray();
		return String.valueOf(cs);
	}

	/**
	 * @category 新建文件
	 * @param file
	 * @param str
	 * @throws IOException
	 */
	private void createFile(String str) throws IOException {
		File file = new File(getRootPath() + "\\src\\com\\wandoufilm\\model\\" + firstWordCapital(tableName) + ".java");
		System.out.println(file.getAbsolutePath());
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter writer = new FileWriter(file);
		writer.write(str);
		writer.flush();
		writer.close();
	}

	@SuppressWarnings("unused")
	private void findFiles(String path) {
		File fileA = new File(path);
		File[] files = fileA.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				System.err.println(file.getName());
			} else {
				if (file.length() != 0) {
					System.err.println(file.getName());
				}
			}

		}
	}

	/**
	 * @category 根目录
	 * @return
	 */
	private String getRootPath() {
		File rootPth = new File("");
		return rootPth.getAbsolutePath();
	}

	/**
	 * @category 返回类型
	 * @param table
	 * @return
	 */
	public StringBuilder getSwithType(Table table) {
		StringBuilder code = new StringBuilder();
		if (table.getType().equals("varchar") || table.getType().equals("text")) {
			code.append("String ");
		} else if (table.getType().equals("datetime")) {
			code.append("Timestamp ");
		} else if (table.getType().equals("int")) {
			code.append("int ");
		} else if (table.getType().equals("date")) {
			code.append("Date ");
		} else if (table.getType().equals("double")) {
			code.append("double ");
		}
		return code;
	}

	public String getSwithTypeFormat(Table table) {
		StringBuilder code = new StringBuilder();
		if (table.getType().equals("varchar") || table.getType().equals("text")) {
			code.append("java.lang.String");
		} else if (table.getType().equals("datetime")) {
			code.append("java.sql.Timestamp");
		} else if (table.getType().equals("int")) {
			code.append("int");
		} else if (table.getType().equals("date")) {
			code.append("java.util.Date");
		} else if (table.getType().equals("double")) {
			code.append("double");
		}
		return code.toString();
	}

	class TableDao {
		public String sql = null;
		public DBHelper db1 = null;
		public ResultSet ret = null;

		public List<Table> findByDataBase(String database) {
			List<Table> list = new ArrayList<Table>();
			sql = "select table_name,column_name,data_type,column_key,column_comment from information_schema.columns where TABLE_SCHEMA ='"
					+ database + "'";// SQL语句
			db1 = new DBHelper(sql);// 创建DBHelper对象
			try {
				ret = db1.pst.executeQuery();// 执行语句，得到结果集
				while (ret.next()) {
					Table table = new Table();
					table.setName(ret.getString(1));
					table.setColumn(ret.getString(2));
					table.setType(ret.getString(3));
					table.setKey(ret.getString(4));
					table.setComment(ret.getString(5));
					list.add(table);
				}
				db1.close();// 关闭连接
				ret.close();
				// 显示数据
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return list;
		}

	}

	class Table {
		private int id;
		private String name;
		private String column;
		private String type;
		private String comment;
		private String key;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getColumn() {
			return column;
		}

		public void setColumn(String column) {
			this.column = column;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

	}

	class DBHelper {
		public Connection conn = null;
		public PreparedStatement pst = null;

		public DBHelper(String sql) {
			try {
				Class.forName(name);// 指定连接类型
				conn = DriverManager.getConnection(url, user, password);// 获取连接
				pst = conn.prepareStatement(sql);// 准备执行语句
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void close() {
			try {
				this.conn.close();
				this.pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
