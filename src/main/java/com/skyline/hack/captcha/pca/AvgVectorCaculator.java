package com.skyline.hack.captcha.pca;

/**
 * 第一步，计算平均脸
 * 
 * @author hzchenliang
 *
 */
public class AvgVectorCaculator {

	public static double[][] caculate(double[][][] imgs, int width, int height) {
		double[][] avg = new double[width][height];
		int m = imgs.length;

		for (int i = 0; i < m; i++) {
			double[][] img = imgs[i];
			for (int j = 0; j < width; j++) {
				for (int k = 0; k < height; k++) {
					avg[j][k] += img[j][k];
				}
			}
		}
		for (int j = 0; j < width; j++) {
			for (int k = 0; k < height; k++) {
				avg[j][k]/=m;
			}
		}
		return avg;
	}

}
