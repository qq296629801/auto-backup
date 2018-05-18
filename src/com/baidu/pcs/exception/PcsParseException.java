package com.baidu.pcs.exception;

/**
 * 
 * 表示解析服务器端返回结果的过程中出错. 
 *
 */
public class PcsParseException extends PcsException {
	public PcsParseException(String detailMessage) {
		super(detailMessage);
	}
}