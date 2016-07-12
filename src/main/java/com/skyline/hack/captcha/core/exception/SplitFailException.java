package com.skyline.hack.captcha.core.exception;

import org.apache.commons.lang.StringUtils;

/**
 * 图片切割失败
 * 
 * @author jairus
 *
 */
public class SplitFailException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -293382983671811478L;

	private int i;

	private int j;

	public SplitFailException(String msg) {
		this(-1, -1, msg, null);
	}

	public SplitFailException(int i, int j) {
		this(i, j, null, null);
	}

	public SplitFailException(int i, int j, String msg) {
		this(i, j, msg, null);
	}

	public SplitFailException(int i, int j, String msg, Throwable t) {
		super(msg, t);
		this.i = 0;
		this.j = j;
	}

	@Override
	public String getMessage() {
		String msg = super.getMessage();
		if (StringUtils.isBlank(msg)) {
			msg = "fail to split img";
		}
		if (i >= 0 && j >= 0)
			return msg + "; i: " + i + ", j: " + j;
		else
			return msg;
	}

	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}

}
