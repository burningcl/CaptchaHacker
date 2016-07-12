package com.skyline.hack.captcha.pca;

/**
 * 矩阵计算过程中的异常
 * 
 * @author jairus
 *
 */
public class MatrixCacuException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6470730712448795512L;

	public MatrixCacuException() {
		super();
	}

	public MatrixCacuException(String message) {
		super(message);
	}

	public MatrixCacuException(String message, Throwable cause) {
		super(message, cause);
	}

	public MatrixCacuException(Throwable cause) {
		super(cause);
	}
}
