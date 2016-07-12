package com.skyline.hack.captcha.core.spliter.flooding;

import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyline.hack.captcha.core.Constants;
import com.skyline.hack.captcha.core.exception.SplitFailException;

/**
 * 
 * @author jairus
 *
 */
public class FloodingVisitor {

	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FloodingVisitor.class);

	boolean[][] visitMap;

	BufferedImage srcImg;

	BufferedImage destTmpImg;

	int srcWidth;

	int srcHeight;

	int left;

	int top;

	int right;

	int buttom;

	int pixels;

	public FloodingVisitor(boolean[][] visitMap, BufferedImage srcImg) {
		this.visitMap = visitMap;
		this.srcImg = srcImg;
	}

	public SplitInfo getCimg(int i, int j) throws Exception {
		destTmpImg = new BufferedImage(srcImg.getWidth(), srcImg.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int ii = 0; ii < srcImg.getWidth(); ii++) {
			for (int jj = 0; jj < srcImg.getHeight(); jj++) {
				destTmpImg.setRGB(ii, jj, Constants.COLOR_BG);
			}
		}
		this.srcWidth = this.left = srcImg.getWidth();
		this.srcHeight = this.top = srcImg.getHeight();
		this.right = 0;
		this.buttom = 0;
		this.pixels = 0;

		flooding(i, j);

		if (left > right || top > buttom) {
			throw new SplitFailException(i, j,
					"fail to split img; left: " + left + ", right: " + right + ", top: " + top + ", buttom: " + buttom);
		}
		LOG.debug("getCimg, i: " + i + ", j: " + j + ", [" + left + ", " + top + ", " + right + ", " + buttom + "]");

		int width = right - left + 1;
		int height = buttom - top + 1;
		SplitInfo info = new SplitInfo();
		BufferedImage destImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int ii = 0; ii < width; ii++) {
			for (int jj = 0; jj < height; jj++) {
				int color = getColor(ii + left, jj + top, destTmpImg);
				destImg.setRGB(ii, jj, color);
			}
		}
		info.x = left;
		info.y = top;
		info.width = right - left;
		info.height = buttom - top;
		info.pixels = pixels;
		info.img = destImg;
		return info;
	}

	private void flooding(int i, int j) {
		if (i >= srcWidth || j >= srcHeight || i < 0 || j < 0 || visitMap[i][j]) {
			return;
		}
		int color = visit(visitMap, i, j, srcImg);
		if (color == Constants.COLOR_CHAR) {
			flooding(i - 1, j);
			flooding(i, j - 1);
			flooding(i + 1, j);
			flooding(i, j + 1);
			if (left > i) {
				left = i;
			}
			if (right < i) {
				right = i;
			}
			if (top > j) {
				top = j;
			}
			if (buttom < j) {
				buttom = j;
			}
			destTmpImg.setRGB(i, j, Constants.COLOR_CHAR);
			pixels++;
		}
	}

	/**
	 * 获取srcImg中[i,j]位置的色值并访问该点
	 * 
	 * @param visitMap
	 * @param i
	 * @param j
	 * @param srcImg
	 * @return
	 */
	public static int visit(boolean[][] visitMap, int i, int j, BufferedImage srcImg) {
		visitMap[i][j] = true;
		return getColor(i, j, srcImg);
	}

	/**
	 * 获取srcImg中[i,j]位置的色值
	 * 
	 * @param i
	 * @param j
	 * @param srcImg
	 * @return
	 */
	public static int getColor(int i, int j, BufferedImage srcImg) {
		Object data = srcImg.getRaster().getDataElements(i, j, null);
		return srcImg.getColorModel().getRGB(data);
	}
}
