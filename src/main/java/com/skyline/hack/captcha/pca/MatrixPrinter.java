package com.skyline.hack.captcha.pca;

public class MatrixPrinter {

	public static void print(double[][] m) {
//		for (int i = 0; i < m.length; i++) {
//			for (int j = 0; j < m[0].length; j++) {
//				System.out.print(m[i][j] + "\t");
//			}
//			System.out.println();
//		}
	}

	public static void print(double[] m) {
//		for (int i = 0; i < m.length; i++) {
//			System.out.print(m[i] + "\t");
//		}
//		System.out.println();
	}

	public static void main(String... strings) {
		double[][] m = { { 1, 2, 3 }, { 2, 3, 4 }, { 5, 6, 7 } };
		print(m);
	}

}
