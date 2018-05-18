package com.wandoufilm.model;

import java.io.Serializable;

/**
 * @category Generated 2018-05-19 01:22:48 by GeneratedTool
 * @author mjy
 */
public class Post implements Serializable {
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

	private String content;

	//
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	private String createTime;

	//
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	private String img;

	//
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}