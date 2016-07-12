package com.skyline.hack.captcha.pca;

/**
 * 矩阵计算
 * 
 * @author jairus
 *
 */
public class MatrixCaculator {

	/**
	 * 转置计算
	 * 
	 * @param src
	 * @return
	 * @throws MatrixCacuException
	 */
	public static double[][] transpose(double[][] src) throws MatrixCacuException {
		if (src == null) {
			throw new MatrixCacuException("the matrix src can not be null!");
		}
		int width = src[0].length;
		int height = src.length;
		double[][] desc = new double[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				desc[i][j] = src[j][i];
			}
		}
		return desc;
	}

	public static double[][] multiply(double[][] src1, double[][] src2) throws MatrixCacuException {
		if (src1 == null || src2 == null) {
			throw new MatrixCacuException("the matrix src1 or src2 can not be null!");
		}
		int width = src1.length;
		int height = src1[0].length;
		int width2 = src2.length;
		int height2 = src2[0].length;
		if (height != width2) {
			return null;
		}
		double[][] ret = new double[width][height2];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height2; j++) {
				for (int k = 0; k < height; k++) {
					ret[i][j] += src1[i][k] * src2[k][j];
				}
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param src
	 * @param src1
	 * @return
	 * @throws MatrixCacuException
	 */
	public static double[][] addToSrc(double[][] src, double[][] src1) throws MatrixCacuException {
		if (src == null || src1 == null) {
			throw new MatrixCacuException("the matrix src1 or src2 can not be null!");
		}
		int width = src.length;
		int height = src[0].length;
		int width2 = src1.length;
		int height2 = src1[0].length;
		if (!(width == width2 && height == height2)) {
			throw new MatrixCacuException("the matrix src and src1 must have the same width and height!");
		}
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				src[i][j] += src1[i][j];
			}
		}
		return src;
	}

	public static void main(String... strings) throws MatrixCacuException {
		double[][] src = { { 1, 2 } };
		System.out.println("src:");

		double[][] transpose = transpose(src);
		System.out.println("transpose:");
		MatrixPrinter.print(transpose);

		double[][] multiply = multiply(src, transpose);
		System.out.println("src X transpose");
		MatrixPrinter.print(multiply);

		double[][] multiply1 = multiply(transpose, src);
		System.out.println("transpose X src");
		MatrixPrinter.print(multiply1);

		double[][] src1 = { { 1, 2 }, { 2, 3 } };
		double[][] src2 = { { 1 }, { 2 } };
		double[][] multiply2 = multiply(src1, src2);
		System.out.println("src1 X src2");
		MatrixPrinter.print(multiply2);

	}
}
