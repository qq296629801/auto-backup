package com.baidu.pcs;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.baidu.pcs.exception.PcsParseException;
/**
 * 表示quota 请求返回的结果
 * 包含以下信息: <br/>
 * 	private long quota  总容量 <br/>
	private long used   已用容量 <br/>
 */
public class PcsQuotaInfo {
	private long quota;
	private long used ;
	public PcsQuotaInfo(String responseBody) throws PcsParseException{
		JSONObject obj = null;
		try {
			obj = (JSONObject) new JSONParser().parse(responseBody);
			this.quota = PcsFileEntry.getAsLong(obj, "quota");
			this.used = PcsFileEntry.getAsLong(obj, "used");
		} catch (Exception e) {
			e.printStackTrace();
			throw new PcsParseException("error on parse Quoat response " + responseBody);
		}
	}
	
	@Override
	public String toString() {
		return  "总容量="+quota/1024/1024/1024/1024+"TB" +"已使用大小="+ used/1024/1024+"MB" ;
	}

	public long getQuota() {
		return quota;
	}
	public void setQuota(long quota) {
		this.quota = quota;
	}
	public long getUsed() {
		return used;
	}
	public void setUsed(long used) {
		this.used = used;
	}
	
}
