package com.baidu.pcs.exception;

/**
 * 表示在与pcs server交互过程中的网络错误
 *
 */
public class PcsHttpException extends PcsException {
	public PcsHttpException(String detailMessage) {
		super(detailMessage);
	}
}