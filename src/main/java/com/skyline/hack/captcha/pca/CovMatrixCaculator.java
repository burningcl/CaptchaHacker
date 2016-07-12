package com.skyline.hack.captcha.pca;

/**
 * 第二、第三半步，计算协方差矩阵
 * 
 * @author hzchenliang
 *
 */
public class CovMatrixCaculator {

	public static double[][] caculate(double[][][] A, int m, int width, int height) throws MatrixCacuException {
		double[][] cov = new double[height][height];
		for (int i = 0; i < m; i++) {
			double[][] a = A[i];
			double[][] at = MatrixCaculator.transpose(a);
			double[][] mutiply = MatrixCaculator.multiply(at, a);
			MatrixCaculator.addToSrc(cov, mutiply);
		}

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < height; j++) {
				cov[i][j] = cov[i][j] / m;
			}
		}

		return cov;
	}

	public static double[][][] getA(double[][] avg, double[][][] imgs, int m, int width, int height) {
		double[][][] A = new double[m][width][height];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < width; j++) {
				for (int k = 0; k < height; k++) {
					A[i][j][k] = imgs[i][j][k] - avg[j][k];
				}
			}
		}
		return A;
	}

}
