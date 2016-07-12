package com.skyline.hack.captcha.core.spliter;

import java.awt.image.BufferedImage;
import java.util.List;

import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * 验证码图片的分割器
 * 
 * 将验证码图片中的文字分割开，每一个文字存储于一个文件。 为了后续的训练与识别，请将图片转换为同样的分别率。
 * 
 * @author jairus
 *
 */
public interface Spliter {

	/**
	 * 分割验证码图片
	 * 
	 * @param srcDir
	 *            源目录
	 * @param destDir
	 *            目标目录
	 * @throws Exception
	 */
	public void split(String srcDir, String destDir) throws Exception;

	/**
	 * 分割验证码图片
	 * 
	 * @param srcDir
	 *            源目录
	 * @param destDir
	 *            目标目录
	 * @param filter
	 * 
	 * @throws Exception
	 */
	public void split(String srcDir, String destDir, IOFileFilter filter) throws Exception;

	/**
	 * 分割验证码图片
	 * 
	 * @param srcImg
	 *            源图片
	 * @return
	 * @throws Exception
	 */
	public List<BufferedImage> split(BufferedImage srcImg) throws Exception;

}
