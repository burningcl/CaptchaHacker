package com.skyline.hack.captcha.core.exception;

/**
 * 
 * @author jairus
 *
 */
public class RecognizationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1364131261611954448L;

	public RecognizationException() {
		super();
	}

	public RecognizationException(String message) {
		super(message);
	}

	public RecognizationException(String message, Throwable cause) {
		super(message, cause);
	}
}
