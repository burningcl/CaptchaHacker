package com.skyline.hack.captcha.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * 图片工具类
 * 
 * @author jairus
 *
 */
public class ImageUtils {

	/**
	 * 图片缩放
	 * 
	 * @param width
	 *            int 新宽度
	 * @param height
	 *            int 新高度
	 * @return BufferedImage 缩放完成的图片
	 */
	public static BufferedImage resize(int width, int height, BufferedImage srcImg) throws Exception {

		Graphics2D g = null;
		BufferedImage destImg = null;
		try {
			Image tmpImg = srcImg.getScaledInstance(width, height, Image.SCALE_DEFAULT);
			destImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			g = destImg.createGraphics();
			g.drawImage(tmpImg, 0, 0, null);
		} finally {
			if (g != null)
				g.dispose();
		}
		return destImg;
	}

	/**
	 * 将RGB色值转换为灰度值
	 * @param r
	 * @param g
	 * @param b
	 */
	public static double rgb2Gray(double r, double g, double b){
		return (r * 299 + g * 587 + b * 114) / 1000;
	}
}
