package com.skyline.hack.captcha.core.recognizer.svm;

import java.io.IOException;

import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * SVM 训练素材准备
 * 
 * @author jairus
 *
 */
public interface SvmResPreparer {

	/**
	 * 准备训练素材
	 * 
	 * @param srcDir
	 *            训练素材所在的目录，例如本项目中的src/test/resources/src/svm/train目录
	 * @param destFile
	 *            训练文件所生成的文件
	 */
	public void prepareTrain(String srcDir, String destFile) throws IOException;

	/**
	 * 准备训练素材
	 * 
	 * @param srcDir
	 *            训练素材所在的目录，例如本项目中的src/test/resources/src/svm/train目录
	 * @param destFile
	 *            训练文件所生成的文件
	 * @param ioFileFilter
	 */
	public void prepareTrain(String srcDir, String destFile, IOFileFilter ioFileFilter) throws IOException;

	/**
	 * 准备测试素材
	 * 
	 * @param srcDir
	 *            测试素材所在的目录，例如本项目中的src/test/resources/src/svm/test目录
	 * @param destFile
	 *            测试素材所生成的文件
	 */
	public void prepareTest(String srcDir, String destFile) throws IOException;

	/**
	 * 准备训练素材
	 * 
	 * @param srcDir
	 *            训练素材所在的目录，例如本项目中的src/test/resources/src/svm/test目录
	 * @param destFile
	 *            测试素材所生成的文件
	 * @param ioFileFilter
	 */
	public void prepareTest(String srcDir, String destFile, IOFileFilter ioFileFilter) throws IOException;

}
