package com.skyline.hack.captcha.core.pretreater;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyline.hack.captcha.core.Constants;
import com.skyline.hack.captcha.util.ImageUtils;
import com.skyline.hack.captcha.util.Utils;

/**
 * 默认的预处理器
 * 
 * 该预览处理器，进行了二值化处理
 * 
 * @author jairus
 *
 */
public class DefaultPretreater implements Pretreater {

	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DefaultPretreater.class);

	/**
	 * 是否在批处理时抛出异常
	 * 
	 * 如果为true，则在批处理时抛出异常， 中断批处理；
	 * 
	 * 如果为false，则在批处理时吞没异常，继续批处理；
	 */
	private boolean throwBashException = true;

	public DefaultPretreater() {

	}

	public DefaultPretreater(boolean throwBashException) {
		this.throwBashException = throwBashException;
	}

	@Override
	public void pretreat(String srcDir, String destDir) throws Exception {
		this.pretreat(srcDir, destDir, null);
	}

	@Override
	public void pretreat(String srcDir, String destDir, IOFileFilter filter) throws Exception {
		Utils.checkArgNullable("srcDir", false, srcDir);
		Utils.checkArgNullable("destDir", false, destDir);
		LOG.debug("pretreat, srcDir: " + srcDir + ", destDir: " + destDir);

		Iterator<File> it = Utils.listFiles(srcDir, filter);
		while (it.hasNext()) {
			File file = it.next();
			try {
				BufferedImage srcImg = ImageIO.read(file);
				BufferedImage destImg = pretreat(srcImg);
				File destFile = Utils.createFile(destDir, file.getName());
				LOG.debug("pretreat, srcFile: " + file + ", destFile: " + destFile);
				ImageIO.write(destImg, "png", destFile);
			} catch (Exception e) {
				if (throwBashException)
					throw e;
				else
					LOG.warn("pretreat, fail, srcFile: " + file, e);
			}
		}
	}

	@Override
	public BufferedImage pretreat(BufferedImage srcImg) throws Exception {
		Utils.checkArgNullable("srcImg", false, srcImg);
		int width = srcImg.getWidth();
		int height = srcImg.getHeight();
		BufferedImage destImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Object data = srcImg.getRaster().getDataElements(i, j, null);
				int r = srcImg.getColorModel().getRed(data);
				int g = srcImg.getColorModel().getGreen(data);
				int b = srcImg.getColorModel().getBlue(data);
				int color = (int) ImageUtils.rgb2Gray(r, g, b);
				if (color > max) {
					max = color;
				}
				if (color < min) {
					min = color;
				}
			}
		}
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Object data = srcImg.getRaster().getDataElements(i, j, null);
				int r = srcImg.getColorModel().getRed(data);
				int g = srcImg.getColorModel().getGreen(data);
				int b = srcImg.getColorModel().getBlue(data);
				int color = (r * 299 + g * 587 + b * 114) / 1000;
				if (max - color > color - min) {
					destImg.setRGB(i, j, Constants.COLOR_CHAR);
				} else {
					destImg.setRGB(i, j, Constants.COLOR_BG);
				}
			}
		}
		return destImg;
	}

}
