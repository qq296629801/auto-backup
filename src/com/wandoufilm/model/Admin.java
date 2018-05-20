package com.wandoufilm.model;
import java.io.Serializable;
import java.sql.Timestamp;
/**
 * @category Generated 2018-05-20 15:40:19 by GeneratedTool
 * @author mjy
 */
public class Admin implements Serializable {
private int id;
public int getId() {
 return id;
}
public void setId(int id) {
 this.id = id;
}
private String user_name;
// 
public String getUser_name(){
 return user_name;
}

public void setUser_name(String user_name){
 this.user_name=user_name;
}

private String pass_word;
// 
public String getPass_word(){
 return pass_word;
}

public void setPass_word(String pass_word){
 this.pass_word=pass_word;
}

private String nick_name;
// 
public String getNick_name(){
 return nick_name;
}

public void setNick_name(String nick_name){
 this.nick_name=nick_name;
}

private Timestamp create_time;
// 
public Timestamp getCreate_time(){
 return create_time;
}

public void setCreate_time(Timestamp create_time){
 this.create_time=create_time;
}

private String remark;
// 
public String getRemark(){
 return remark;
}

public void setRemark(String remark){
 this.remark=remark;
}

private int role_id;
// 
public int getRole_id(){
 return role_id;
}

public void setRole_id(int role_id){
 this.role_id=role_id;
}

private int login_times;
// 
public int getLogin_times(){
 return login_times;
}

public void setLogin_times(int login_times){
 this.login_times=login_times;
}

private String ip;
// 
public String getIp(){
 return ip;
}

public void setIp(String ip){
 this.ip=ip;
}

private Timestamp last_time;
// 
public Timestamp getLast_time(){
 return last_time;
}

public void setLast_time(Timestamp last_time){
 this.last_time=last_time;
}

}