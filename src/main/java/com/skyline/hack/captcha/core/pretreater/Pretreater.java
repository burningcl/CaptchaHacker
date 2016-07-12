package com.skyline.hack.captcha.core.pretreater;

import java.awt.image.BufferedImage;

import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * 图片预览处理器
 * 
 * 请在这里处理类类似以下的操作： <br>
 * 1、去除干扰线；<br>
 * 2、去除噪点； <br>
 * 3、均值化或者二分化；<br>
 * ...
 * 
 * @author jairus
 *
 */
public interface Pretreater {

	/**
	 * 预览处理图片
	 * 
	 * @param srcDir
	 *            源目录
	 * @param destDir
	 *            目标目录
	 */
	public void pretreat(String srcDir, String destDir) throws Exception;

	/**
	 * 预览处理图片
	 * 
	 * @param srcDir
	 *            源目录
	 * @param destDir
	 *            目标目录
	 * @param filter
	 * 
	 */
	public void pretreat(String srcDir, String destDir, IOFileFilter filter) throws Exception;

	/**
	 * 预处理图片
	 * 
	 * @param srcImg
	 * 
	 * @return
	 */
	public BufferedImage pretreat(BufferedImage srcImg) throws Exception;
}
