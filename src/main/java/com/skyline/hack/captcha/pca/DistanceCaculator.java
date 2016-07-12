package com.skyline.hack.captcha.pca;

/**
 * 计算两个矩阵之间的距离
 * 
 * @author jairus
 *
 */
public class DistanceCaculator {

	/**
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double caculate(double[][] d1, double[][] d2) {
		double d = 0;
		for (int i = 0; i < d1.length; i++) {
			for (int j = 0; j < d1[0].length; j++) {
				d += (d1[i][j] - d2[i][j]) * (d1[i][j] - d2[i][j]);
			}
		}
		return Math.sqrt(d);
	}

}
