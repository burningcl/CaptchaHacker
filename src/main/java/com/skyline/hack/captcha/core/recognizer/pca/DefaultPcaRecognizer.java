package com.skyline.hack.captcha.core.recognizer.pca;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyline.hack.captcha.core.Constants;
import com.skyline.hack.captcha.core.exception.RecognizationException;
import com.skyline.hack.captcha.pca.PcaMain;
import com.skyline.hack.captcha.pca.MatrixCacuException;
import com.skyline.hack.captcha.util.Utils;

/**
 * 
 * @author jairus
 *
 */
public class DefaultPcaRecognizer implements PcaRecognizer {

	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DefaultPcaRecognizer.class);

	public Map<Character, PcaMain> map = new HashMap<>();

	@Override
	public void train(String srcDir, String trainFile, String modelFile)
			throws IOException, RecognizationException, MatrixCacuException {
		this.train(srcDir, trainFile, modelFile, null);
	}

	@Override
	public void train(String srcDirPath, String trainFile, String modelFile, IOFileFilter ioFileFilter)
			throws IOException, RecognizationException, MatrixCacuException {
		File srcDir = new File(srcDirPath);
		File[] srcSubDirs = srcDir.listFiles();
		try {
			for (File srcSubDir : srcSubDirs) {
				Iterator<File> imgFiles = Utils.listFiles(srcSubDir, ioFileFilter);
				String dirName = srcSubDir.getName().trim();
				Character c = Utils.string2Char(dirName);
				if (c == null) {
					LOG.warn("prepareTrain, fail, can not convert dirName, dirName: " + dirName);
					break;
				}
				PcaMain main = map.get(c);
				if (main == null) {
					main = new PcaMain();
					map.put(c, main);
				}
				List<double[][]> imgs = new ArrayList<>();
				while (imgFiles.hasNext()) {
					BufferedImage img = ImageIO.read(imgFiles.next());
					int width = img.getWidth();
					int height = img.getHeight();
					double[][] ci = new double[width][height];
					for (int i = 0; i < width; i++) {
						for (int j = 0; j < height; j++) {
							ci[i][j] = img.getRGB(i, j) == Constants.COLOR_CHAR ? 1 : 0;
						}
					}
					imgs.add(ci);
				}
				main.train(imgs.toArray(new double[0][0][0]));
			}
		} finally {
		}
	}

	@Override
	public void recognize(String srcDir, String destFile, String resultFile)
			throws IOException, RecognizationException, MatrixCacuException {
		this.recognize(srcDir, destFile, resultFile, null);
	}

	@Override
	public void recognize(String srcDirPath, String destFile, String resultFile, IOFileFilter ioFileFilter)
			throws IOException, RecognizationException, MatrixCacuException {
		Set<Character> chars = map.keySet();
		Iterator<File> imgFiles = Utils.listFiles(srcDirPath, ioFileFilter);
		while (imgFiles.hasNext()) {
			BufferedImage img = ImageIO.read(imgFiles.next());
			int width = img.getWidth();
			int height = img.getHeight();
			double[][] ci = new double[width][height];
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					ci[i][j] = img.getRGB(i, j) == Constants.COLOR_CHAR ? 1 : 0;
				}
			}
			char minC = 0;
			double minD = Double.MAX_VALUE;
			for (Character c : chars) {
				PcaMain main = map.get(c);
				double d = main.test(ci);
				if (d < minD) {
					minD = d;
					minC = c;
				}
			}
			LOG.debug("minC: " + minC);
		}
	}
}
