package com.skyline.hack.captcha.pca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 * �����°롢���岽������������
 * 
 * @author hzchenliang
 *
 */
public class TzMatrixCaculator {

	private static class EVal {
		int index;
		double val;

		public EVal(int index, double val) {
			this.index = index;
			this.val = val;
		}
	}

	public static double[][] caculateTzMatrix(double[][] cov, int m) {
		Matrix matrix = new Matrix(cov);
		EigenvalueDecomposition dec = new EigenvalueDecomposition(matrix);
		Matrix v = dec.getV();
		System.out.println("d:");
		double[] es = dec.getRealEigenvalues();
		List<EVal> list = new ArrayList<EVal>();
		double eValSum = 0;
		for (int i = 0; i < es.length; i++) {
			list.add(new EVal(i, es[i]));
			eValSum += es[i];
		}
		Collections.sort(list, new Comparator<EVal>() {

			@Override
			public int compare(EVal arg0, EVal arg1) {
				if (arg0.val < arg1.val) {
					return 1;
				} else if (arg0.val > arg1.val) {
					return -1;
				}
				return 0;
			}
		});
		double eValSubSum = 0;
		int p = 0;
		for (; p < list.size(); p++) {
			EVal e = list.get(p);
			System.out.print(e.val + "," + e.index + "\t");
			eValSubSum += e.val;
			if (eValSubSum / eValSum >= 0.99) {
				break;
			}
		}
		System.out.println();
		p = p + 1;

		System.out.println("p: " + p);

		double[][] vm = v.getArray();
		MatrixPrinter.print(vm);

		double[][] vmk = new double[p][m];
		for (int i = 0; i < p; i++) {
			for (int j = 0; j < m; j++) {
				vmk[i][j] = vm[j][list.get(i).index];
			}
		}

		return vmk;
	}

	public static double[][] caculate(double[][] cov, int m) {

		double[][] vmk = caculateTzMatrix(cov, m);
		int p = vmk.length;

		for (int i = 0; i < p; i++) {
			double sum = 0;
			for (int j = 0; j < m; j++) {
				sum += vmk[i][j] * vmk[i][j];
			}
			sum = Math.sqrt(sum);
			System.out.println("sum: " + sum);
			for (int j = 0; j < m; j++) {
				vmk[i][j] /= sum;
			}
		}

		// System.out.println();
		// MatrixPrinter.print(tz);

		return vmk;
	}

	public static void main(String... strings) {
		// int n = 4;
		// int m = 3;
		// double[][] vm = { { 1, 0, -1 }, { 0, 1, 0 }, { 1, 0, 1 } };
		// double[][] A = { { 1, 2, 3, 4 }, { 2, 3, 4, 5 }, { 3, 4, 5, 6 } };
		// double[][] tz = new double[n][m];
		// for (int i = 0; i < n; i++) {
		// for (int j = 0; j < m; j++) {
		// for (int k = 0; k < m; k++) {
		// tz[i][j] += A[k][i] * vm[j][k];
		// }
		// }
		// }
		// MatrixPrinter.print(tz);
		double[][] a = { { -2, 0, -4 }, { 1, 2, 1 }, { 1, 0, 3 } };
		Matrix matrix = new Matrix(a);
		EigenvalueDecomposition dec = new EigenvalueDecomposition(matrix);
		Matrix v = dec.getV();
		// Matrix d = dec.getD();
		MatrixPrinter.print(v.getArray());
		// System.out.println();
		// MatrixPrinter.print(d.getArray());
		// System.out.println();
		double[] ies = dec.getRealEigenvalues();
		MatrixPrinter.print(ies);
		// System.out.println();
		// double[] res=dec.getRealEigenvalues();
		// MatrixPrinter.print(res);
	}

}
