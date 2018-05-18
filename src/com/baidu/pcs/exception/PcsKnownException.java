package com.baidu.pcs.exception;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.baidu.pcs.http.HttpHelper.RestHttpException;
/**
 * 表示服务器端返回的错误. <br/>
 * 是解析以下json的结果: <br/> 
 * {"error_code":31066,"error_msg":"file does not exist","request_id":1528151662}
 */

public class PcsKnownException extends PcsException {
	public long errorCode;
	public String errorMsg;

	public PcsKnownException(String responseBody) {
		super(responseBody);
		JSONObject obj;
		try {
			obj = (JSONObject)new JSONParser().parse(responseBody);
			errorCode = (Long) obj.get("error_code");
			errorMsg = (String) obj.get("error_msg");
		} catch (Exception e) {
			errorMsg = "parse response Json error " + responseBody;
			e.printStackTrace();
		}
	}
	
	public PcsKnownException(long errorCode, String errorMsg) {
		super("" + errorCode + ": " + errorMsg);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	@Override
	public String toString() {
		return "PcsKnownException [errorCode=" + errorCode + ", errorMsg=" + errorMsg + "]";
	}
}