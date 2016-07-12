package com.skyline.hack.captcha.core.recognizer.svm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyline.hack.captcha.core.exception.RecognizationException;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

/**
 * 
 * @author jairus
 *
 */
public class DefaultSvmRecognizer implements SvmRecognizer {

	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DefaultSvmRecognizer.class);

	private SvmResPreparer resPreparer;

	public svm_model model;

	public DefaultSvmRecognizer(SvmResPreparer resPreparer) throws RecognizationException {
		this.resPreparer = resPreparer;
		if (resPreparer == null) {
			throw new RecognizationException("resPreparer must not be null");
		}
	}

	@Override
	public void train(String srcDir, String trainFile, String modelFile) throws IOException, RecognizationException {
		this.train(srcDir, trainFile, modelFile, null);
	}

	@Override
	public void train(String srcDir, String trainFile, String modelFile, IOFileFilter ioFileFilter)
			throws IOException, RecognizationException {
		resPreparer.prepareTrain(srcDir, trainFile, ioFileFilter);
		svm_parameter param = initSvmParam();
		svm_problem problem = read_problem(trainFile, param);
		String checkMsg = svm.svm_check_parameter(problem, param);
		if (!StringUtils.isBlank(checkMsg)) {
			LOG.warn("train, checkMsg: " + checkMsg);
		}
		do_cross_validation(problem, param);
		model = svm.svm_train(problem, param);
		svm.svm_save_model(modelFile, model);
	}

	private static double atof(String s) throws RecognizationException {
		double d = Double.valueOf(s).doubleValue();
		if (Double.isNaN(d) || Double.isInfinite(d)) {
			throw new RecognizationException("NaN or Infinity in input");
		}
		return (d);
	}

	private static int atoi(String s) {
		return Integer.parseInt(s);
	}

	private void do_cross_validation(svm_problem prob, svm_parameter param) {
		int i;
		int total_correct = 0;
		double total_error = 0;
		double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
		double[] target = new double[prob.l];

		svm.svm_cross_validation(prob, param, 5, target);
		if (param.svm_type == svm_parameter.EPSILON_SVR || param.svm_type == svm_parameter.NU_SVR) {
			for (i = 0; i < prob.l; i++) {
				double y = prob.y[i];
				double v = target[i];
				total_error += (v - y) * (v - y);
				sumv += v;
				sumy += y;
				sumvv += v * v;
				sumyy += y * y;
				sumvy += v * y;
			}
			System.out.print("Cross Validation Mean squared error = " + total_error / prob.l + "\n");
			System.out.print("Cross Validation Squared correlation coefficient = "
					+ ((prob.l * sumvy - sumv * sumy) * (prob.l * sumvy - sumv * sumy))
							/ ((prob.l * sumvv - sumv * sumv) * (prob.l * sumyy - sumy * sumy))
					+ "\n");
		} else {
			for (i = 0; i < prob.l; i++)
				if (target[i] == prob.y[i])
					++total_correct;
			System.out.print("Cross Validation Accuracy = " + 100.0 * total_correct / prob.l + "%\n");
		}
	}

	private svm_parameter initSvmParam() {
		svm_parameter param = new svm_parameter();
		// default values
		param.svm_type = svm_parameter.NU_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.degree = 3;
		param.gamma = 0; // 1/num_features
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];
		return param;
	}

	private svm_problem read_problem(String destFile, svm_parameter param) throws IOException, RecognizationException {
		BufferedReader fp = null;
		svm_problem prob = null;
		try {
			fp = new BufferedReader(new FileReader(destFile));
			Vector<Double> vy = new Vector<Double>();
			Vector<svm_node[]> vx = new Vector<svm_node[]>();
			int max_index = 0;

			while (true) {
				String line = fp.readLine();
				if (line == null)
					break;

				StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

				vy.addElement(atof(st.nextToken()));
				int m = st.countTokens() / 2;
				svm_node[] x = new svm_node[m];
				for (int j = 0; j < m; j++) {
					x[j] = new svm_node();
					x[j].index = atoi(st.nextToken());
					x[j].value = atof(st.nextToken());
					// System.out.print(x[j].index+":"+x[j].value+" ");
				}
				// System.out.println();
				if (m > 0)
					max_index = Math.max(max_index, x[m - 1].index);
				vx.addElement(x);
			}

			prob = new svm_problem();
			prob.l = vy.size();
			prob.x = new svm_node[prob.l][];
			for (int i = 0; i < prob.l; i++)
				prob.x[i] = vx.elementAt(i);
			prob.y = new double[prob.l];
			for (int i = 0; i < prob.l; i++)
				prob.y[i] = vy.elementAt(i);

			if (param.gamma == 0 && max_index > 0)
				param.gamma = 1.0 / max_index;

			if (param.kernel_type == svm_parameter.PRECOMPUTED)
				for (int i = 0; i < prob.l; i++) {
					if (prob.x[i][0].index != 0) {
						System.err.print("Wrong kernel matrix: first column must be 0:sample_serial_number\n");
						System.exit(1);
					}
					if ((int) prob.x[i][0].value <= 0 || (int) prob.x[i][0].value > max_index) {
						System.err.print("Wrong input format: sample_serial_number out of range\n");
						System.exit(1);
					}
				}
		} finally {
			if (fp != null)
				fp.close();
		}
		return prob;
	}

	@Override
	public void recognize(String srcDir, String destFile, String resultFile)
			throws IOException, RecognizationException {
		this.recognize(srcDir, destFile, resultFile, null);
	}

	@Override
	public void recognize(String srcDir, String destFile, String resultFile, IOFileFilter ioFileFilter)
			throws IOException, RecognizationException {
		if (this.model == null) {
			throw new RecognizationException("model must not be null. pls train one model, or load one!");
		}
		resPreparer.prepareTest(srcDir, destFile, ioFileFilter);

		@SuppressWarnings("unchecked")
		List<String> lines = FileUtils.readLines(new File(destFile), "UTF-8");
		for (String line : lines) {
			StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

			int m = st.countTokens() / 2;
			svm_node[] x = new svm_node[m];
			for (int j = 0; j < m; j++) {
				x[j] = new svm_node();
				x[j].index = atoi(st.nextToken());
				x[j].value = atof(st.nextToken());
			}
			double v = svm.svm_predict(model, x);
			LOG.debug("c: " + Character.valueOf((char) v) + ", " + v);
		}
	}

	@Override
	public void loadModel(String modelFile) throws IOException {
		this.model = svm.svm_load_model(modelFile);
	}

}
