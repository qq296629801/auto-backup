package com.wandoufilm.model;

import java.io.Serializable;

/**
 * @category Generated 2018-05-19 01:51:34 by GeneratedTool
 * @author mjy
 */
public class Blog implements Serializable {
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String title;

	//
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private String time;

	//
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	private int user_id;

	//
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	private String context;

	//
	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	private int zan;

	//
	public int getZan() {
		return zan;
	}

	public void setZan(int zan) {
		this.zan = zan;
	}

	private String post;

	//
	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	private String url;

	//
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private double car_id;

	//
	public double getCar_id() {
		return car_id;
	}

	public void setCar_id(double car_id) {
		this.car_id = car_id;
	}

}