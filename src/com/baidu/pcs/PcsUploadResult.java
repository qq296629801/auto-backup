package com.baidu.pcs;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.baidu.pcs.exception.PcsParseException;

/**
 * 表示 upload 请求返回的结果
 * 包含以下信息: <br/>
 *  private String path;  服务器端路径 <br/>
	private String md5;  服务器端计算的文件md5 <br/>
  	private long size   总容量 <br/>
	private long ctime   创建时间戳<br/>
	private long mtime   修改时间戳 <br/>
 */
public class PcsUploadResult {
	private String path;
	private String md5;
	private long size;
	private long ctime;
	private long mtime;

	public PcsUploadResult(String responseBody) throws PcsParseException {
		JSONObject obj = null;
		try {
			obj = (JSONObject) new JSONParser().parse(responseBody);

			this.path = (String) obj.get("path");
			this.md5 = (String) obj.get("md5");
			this.size = PcsFileEntry.getAsLong(obj, "size");
			this.ctime = PcsFileEntry.getAsLong(obj, "ctime");
			this.mtime = PcsFileEntry.getAsLong(obj, "mtime");
		} catch (Exception e) {
			e.printStackTrace();
			throw new PcsParseException("error on parse Quoat response " + responseBody);
		}
	}

	@Override
	public String toString() {
		return "PcsUploadResult [path=" + path + ", md5=" + md5 + ", size=" + size + ", ctime=" + ctime + ", mtime="
				+ mtime + "]";
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public long getMtime() {
		return mtime;
	}

	public void setMtime(long mtime) {
		this.mtime = mtime;
	}
}
