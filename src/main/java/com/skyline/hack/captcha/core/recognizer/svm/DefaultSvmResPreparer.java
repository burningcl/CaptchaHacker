package com.skyline.hack.captcha.core.recognizer.svm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyline.hack.captcha.core.Constants;
import com.skyline.hack.captcha.core.exception.RecognizationException;
import com.skyline.hack.captcha.util.Utils;

/**
 * 
 * @author jairus
 *
 */
public class DefaultSvmResPreparer implements SvmResPreparer {
	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DefaultSvmResPreparer.class);

	@Override
	public void prepareTrain(String srcDirStr, String destFileStr) throws IOException, RecognizationException {
		this.prepareTrain(srcDirStr, destFileStr, null);
	}

	@Override
	public void prepareTrain(String srcDirStr, String destFileStr, IOFileFilter ioFileFilter)
			throws IOException, RecognizationException {
		Utils.checkArgNullable("srcDir", false, srcDirStr);
		Utils.checkArgNullable("destDir", false, destFileStr);
		File srcDir = new File(srcDirStr);
		if (!srcDir.exists()) {
			throw new RecognizationException("prepareTrain, srcDir: " + srcDir + ", not exist!");
		}
		if (srcDir.isFile()) {
			throw new RecognizationException(
					"prepareTrain, srcDir: " + srcDir + ", it is not a directory, it is a file!");
		}
		File[] srcSubDirs = srcDir.listFiles();
		File destFile = new File(destFileStr);
		if (!destFile.exists()) {
			if (!destFile.getParentFile().exists()) {
				destFile.getParentFile().mkdirs();
			}
		} else {
			destFile.delete();
		}
		destFile.createNewFile();
		FileWriter destWriter = null;
		try {
			destWriter = new FileWriter(destFile);
			for (File srcSubDir : srcSubDirs) {
				Iterator<File> imgFiles = Utils.listFiles(srcSubDir, ioFileFilter);
				String dirName = srcSubDir.getName().trim();
				Character c = Utils.string2Char(dirName);
				if (c == null) {
					throw new RecognizationException(
							"prepareTrain, fail, can not convert dirName, dirName: " + dirName);
				}
				while (imgFiles.hasNext()) {
					write(c, imgFiles.next(), destWriter);
				}
			}
		} finally {
			if (destWriter != null) {
				destWriter.flush();
				destWriter.close();
			}
		}
	}

	@Override
	public void prepareTest(String srcDir, String destFile) throws IOException, RecognizationException {
		this.prepareTest(srcDir, destFile, null);
	}

	@Override
	public void prepareTest(String srcDirStr, String destFileStr, IOFileFilter ioFileFilter)
			throws IOException, RecognizationException {
		Utils.checkArgNullable("srcDir", false, srcDirStr);
		Utils.checkArgNullable("destDir", false, destFileStr);
		File srcDir = new File(srcDirStr);
		if (!srcDir.exists()) {
			throw new RecognizationException("prepareTest, srcDir: " + srcDir + ", not exist!");
		}
		if (srcDir.isFile()) {
			throw new RecognizationException(
					"prepareTest, srcDir: " + srcDir + ", it is not a directory, it is a file!");
		}
		File destFile = new File(destFileStr);
		if (!destFile.exists()) {
			if (!destFile.getParentFile().exists()) {
				destFile.getParentFile().mkdirs();
			}
		} else {
			destFile.delete();
		}
		destFile.createNewFile();
		FileWriter destWriter = null;
		try {
			destWriter = new FileWriter(destFile);
			Iterator<File> imgFiles = Utils.listFiles(srcDirStr, ioFileFilter);
			while (imgFiles.hasNext()) {
				write(null, imgFiles.next(), destWriter);
			}
		} finally {
			if (destWriter != null) {
				destWriter.flush();
				destWriter.close();
			}
		}
	}

	protected void write(Character c, File imgFile, FileWriter destWriter) throws IOException {
		BufferedImage img = ImageIO.read(imgFile);
		int width = img.getWidth();
		int height = img.getHeight();
		StringBuilder sb = new StringBuilder();
		if (c != null)
			sb.append(Integer.valueOf(c)).append(" ");
		int index = 1;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int flag = 0;
				int color = img.getRGB(i, j);
				if (color == Constants.COLOR_CHAR) {
					flag = 1;
				}
				sb.append(index).append(": ").append(flag);
				if (i * width + j < width * height - 1) {
					sb.append(" ");
				}
				index++;
			}
		}
		destWriter.append(sb.toString());
		destWriter.append("\n");
	}

}
