package com.skyline.hack.captcha.core.spliter.flooding;

import java.awt.image.BufferedImage;

/**
 * 每一个文字的切割信息
 * 
 * @author jairus
 *
 */
public class SplitInfo {

	public int index;

	public int x;

	public int y;

	public int width;

	public int height;

	public int pixels;

	public BufferedImage img;

	public double centerX() {
		return x + width / 2;
	}

	public double centerY() {
		return y + height / 2;
	}

	public int x1() {
		return x + width;
	}

	public int y1() {
		return y + height;
	}

	@Override
	public String toString() {
		return "SplitInfo [index=" + index + ", x=" + x + ", y=" + y + ", width=" + width + ", height=" + height
				+ ", pixels=" + pixels + ", centerX()=" + centerX() + ", centerY()=" + centerY() + "]";
	}

}
