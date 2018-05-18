package com.baidu.pcs;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.baidu.pcs.exception.PcsParseException;

/**
 * 表示list或search接口返回的 文件元信息, 包含以下信息: <br/>
 * long fsId: 目录在个人云存储的临时唯一标识id<br/>
 * String path: 该目录的绝对路径<br/>
 * String serverFilename: 文件名(解析path得到)<br/>
 * String md5: 文件md5<br/>
 * long size: 文件大小(bytes)<br/>
 * boolean isDir: 0为文件, 1为目录<br/>
 * long mtime: 文件服务器修改时间<br/>
 * long ctime: 文件服务器创建时间<br/>
 */
public class PcsFileEntry {
	private long fsId;
	private String path;
	private String serverFilename;
	private String md5;

	private long size;

	private long mtime;
	private long ctime;
	private boolean isDir;

	public PcsFileEntry() {
	}

	public static ArrayList<PcsFileEntry> parseArrayFromJsonString(String responseBody) throws PcsParseException {
		JSONObject rst;
		try {
			rst = (JSONObject) new JSONParser().parse(responseBody);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new PcsParseException("ParseException on parse FileList " + responseBody);
		}
		JSONArray rawList = (JSONArray) rst.get("list");

		ArrayList<PcsFileEntry> list = new ArrayList<PcsFileEntry>();
		for (Object item : rawList) {
			PcsFileEntry entry = PcsFileEntry.fromJsonObject((JSONObject) item);
			list.add(entry);
		}
		return list;
	}

	private static String basename(String path) {
		final Pattern BASENAME = Pattern.compile(".*?([^/]*)$");
		Matcher matcher = BASENAME.matcher(path);
		if (matcher.matches()) {
			return matcher.group(1);
		} else {
			throw new IllegalArgumentException("Can't parse " + path);
		}
	}

	public static PcsFileEntry fromJsonObject(JSONObject obj) throws PcsParseException {
		try {
			PcsFileEntry entry = new PcsFileEntry();
			entry.path = (String) obj.get("path");
			entry.serverFilename = basename(entry.path);
			entry.md5 = (String) obj.get("md5");
			entry.fsId = getAsLong(obj, "fs_id");
			entry.isDir = (getAsLong(obj, "isdir") == 1);

			entry.size = getAsLong(obj, "size");
			entry.mtime = getAsLong(obj, "mtime");
			entry.ctime = getAsLong(obj, "ctime");
			return entry;
		} catch (Exception e) {
			e.printStackTrace();
			throw new PcsParseException("ParseException on " + obj.toString());
		}
	}

	@Override
	public String toString() {
		return "PcsFileEntry [fsId=" + fsId + ", path=" + path + ", serverFilename=" + serverFilename + ", md5=" + md5
				+ ", size=" + size + ", mtime=" + mtime + ", ctime=" + ctime + ", isDir=" + isDir + "]";
	}

	protected static long getAsLong(JSONObject obj, String name) {
		Object val = obj.get(name);
		long ret = 0;
		if (val != null) {
			if (val instanceof Number) {
				ret = ((Number) val).longValue();
			} else if (val instanceof String) {
				// To parse cases where JSON can't represent a Long, so
				// it's stored as a string
				ret = Long.parseLong((String) val, 16);
			}
		}
		return ret;
	}

	public long getFsId() {
		return fsId;
	}

	public void setFsId(long fsId) {
		this.fsId = fsId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getServerFilename() {
		return serverFilename;
	}

	public void setServerFilename(String serverFilename) {
		this.serverFilename = serverFilename;
	}

	public boolean isDir() {
		return isDir;
	}

	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}

	public long getMtime() {
		return mtime;
	}

	public void setMtime(long mtime) {
		this.mtime = mtime;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

}
