package com.skyline.hack.captcha.pca;

public class CovPrjVectorCaculator {

	/**
	 * 
	 * @param C
	 *            n*k
	 * @param d
	 * @return
	 * @throws MatrixCacuException 
	 */
	public static double[][] caculate(double[][] C, double[][] d) throws MatrixCacuException {
		double[][] ret = MatrixCaculator.multiply( d, MatrixCaculator.transpose(C));
		return ret;
	}

	public static void main(String... strings) {

	}
}
