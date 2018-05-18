// package com.wandoufilm.dao;
//
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.sql.Statement;
// import java.text.SimpleDateFormat;
// import java.util.ArrayList;
// import java.util.Date;
// import java.util.List;
//
// import com.wandoufilm.model.Post;
// import com.wandoufilm.model.Wp_posts;
// import com.wandoufilm.util.Dbcon;
//
// public class PostDao implements BaseDao {
// Statement st;
//
// @Override
// public List<Wp_posts> find(int ID) {
// // TODO Auto-generated method stub
// st = Dbcon.getConnection();
// String sql = "select * from wp_posts where id= " + ID;
// ResultSet rs;
// List<Wp_posts> list = new ArrayList<>();
//
// try {
// rs = st.executeQuery(sql);
// while (rs.next()) {
// Wp_posts post = new Wp_posts();
// post.setID(rs.getInt("ID"));
// post.setPost_author(rs.getInt("post_author"));
// post.setPost_title(rs.getString("post_title"));
// post.setPost_content(rs.getString("post_content"));
// list.add(post);
// }
// return list;
// } catch (SQLException e) {
// // TODO Auto-generated catch block
// Dbcon.close();
// e.printStackTrace();
// }
// return null;
// }
//
// @Override
// public List<Wp_posts> find() {
// // TODO Auto-generated method stub
// st = Dbcon.getConnection();
// String sql = "select * from wp_posts";
// ResultSet rs;
// List<Wp_posts> list = new ArrayList<>();
//
// try {
// rs = st.executeQuery(sql);
// while (rs.next()) {
// Wp_posts post = new Wp_posts();
// post.setID(rs.getInt("ID"));
// post.setPost_author(rs.getInt("post_author"));
// post.setPost_title(rs.getString("post_title"));
// post.setPost_content(rs.getString("post_content"));
// list.add(post);
// }
// return list;
// } catch (SQLException e) {
// // TODO Auto-generated catch block
// Dbcon.close();
// e.printStackTrace();
// }
// return null;
// }
//
// @Override
// public List<Post> findPost(int ID) {
// // TODO Auto-generated method stub
//
// return null;
// }
//
// @Override
// public int save(Post post) {
// // TODO Auto-generated method stub
// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
// Date date = new Date();
// st = Dbcon.getConnection();
// String sql = "INSERT INTO `post` (`title`, `content`, `creatTime`, `cid`,
// `img`) VALUES ('" + post.getTitle()
// + "', '" + post.getContent() + "', '" + sdf.format(date) + "', " +
// post.getCid() + ", '" + post.getImg()
// + "')";
// int rs = 0;
// try {
// rs = st.executeUpdate(sql);
// } catch (SQLException e) {
// // TODO Auto-generated catch block
// Dbcon.close();
// e.printStackTrace();
// }
// return rs;
// }
//
// @Override
// public List<Post> findPost() {
// // TODO Auto-generated method stub
// st = Dbcon.getConnection();
// String sql = "select * from post";
// ResultSet rs;
// List<Post> list = new ArrayList<>();
//
// try {
// rs = st.executeQuery(sql);
// while (rs.next()) {
// Post post = new Post();
// post.setId(rs.getInt("id"));
// post.setTitle(rs.getString("title"));
// post.setContent(rs.getString("content"));
// post.setCid(rs.getInt("cid"));
// post.setCreatTime(rs.getTime("creatTime"));
// post.setImg(rs.getString("img"));
// list.add(post);
// }
// return list;
// } catch (SQLException e) {
// // TODO Auto-generated catch block
// Dbcon.close();
// e.printStackTrace();
// }
// return null;
// }
//
// }
