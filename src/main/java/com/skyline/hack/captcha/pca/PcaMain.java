package com.skyline.hack.captcha.pca;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PCA运算的入口
 * 
 * @author jairus
 *
 */
public class PcaMain {

	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger(PcaMain.class);

	double[][] C;

	double[][][] projs;

	/**
	 * 训练
	 * 
	 * @param imgs
	 * @throws MatrixCacuException
	 */
	public void train(double[][][] imgs) throws MatrixCacuException {

		int m;
		int width;
		int height;
		int k;
		double[][] avg;

		m = imgs.length;
		width = imgs[0].length;
		height = imgs[0][0].length;

		LOG.debug("train, imgs num: " + m + ", width: " + width + ",  height: " + height);

		// 计算平均脸
		System.out.println("计算平均脸");
		avg = AvgVectorCaculator.caculate(imgs, width, height);

		// 计算协方差矩阵
		System.out.println("计算协方差矩阵");
		// m*width*height
		double[][][] A = CovMatrixCaculator.getA(avg, imgs, m, width, height);
		// height*height
		double[][] cov = CovMatrixCaculator.caculate(A, m, width, height);
		System.out.println("协方差矩阵:");

		// 计算特征脸
		System.out.println("计算特征脸");
		// height*k
		C = TzMatrixCaculator.caculateTzMatrix(cov, height);

		k = C[0].length;
		System.out.println("k: " + k);

		// 计算每一个脸在特征脸空间上的投影
		System.out.println("计算每一个脸在特征脸空间上的投影");
		projs = new double[m][][];
		for (int i = 0; i < m; i++) {
			projs[i] = CovPrjVectorCaculator.caculate(C, imgs[i]);
		}
	}

	/**
	 * 测试
	 * 
	 * @param testImg
	 * @return
	 * @throws MatrixCacuException
	 */
	public double test(double[][] testImg) throws MatrixCacuException {
		double[][] proj = CovPrjVectorCaculator.caculate(C, testImg);

		double result = Double.MAX_VALUE;
		for (int i = 0; i < projs.length; i++) {
			double d = DistanceCaculator.caculate(proj, projs[i]);
			if (d < result) {
				result = d;
			}
		}
		return result;
	}

}
