package com.skyline.hack.captcha.core.mapper;

import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * 分类器
 * 
 * 将切好的图片，按文字进行分类
 * 
 * @author jairus
 *
 */
public interface Mapper {
	/**
	 * 
	 * @param srcDir
	 *            源目录
	 * @param destDir
	 *            目标目录
	 * @param textFile
	 */
	public void classifier(String srcDir, String destDir, String textFile) throws Exception;

	/**
	 * 
	 * @param srcDir
	 *            源目录
	 * @param destDir
	 *            目标目录
	 * @param textFile
	 * @param filter
	 * 
	 */
	public void classifier(String srcDir, String destDir, String textFile, IOFileFilter filter) throws Exception;

}
