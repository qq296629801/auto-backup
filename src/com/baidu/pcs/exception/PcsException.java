package com.baidu.pcs.exception;

/**
 * 所有pcs 异常的父类
 * @author xiaomi
 *
 */
public class PcsException extends Exception {
	public PcsException(String detailMessage) {
		super(detailMessage);
	}
}
