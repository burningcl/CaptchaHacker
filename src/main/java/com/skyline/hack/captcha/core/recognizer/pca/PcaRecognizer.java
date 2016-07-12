package com.skyline.hack.captcha.core.recognizer.pca;

import java.io.IOException;

import org.apache.commons.io.filefilter.IOFileFilter;

import com.skyline.hack.captcha.core.exception.RecognizationException;
import com.skyline.hack.captcha.pca.MatrixCacuException;

/**
 * 
 * @author jairus
 *
 */
public interface PcaRecognizer {


	/**
	 * 准备训练素材
	 * 
	 * @param srcDir
	 *            训练素材所在的目录，例如本项目中的src/test/resources/src/svm/train目录
	 * @param trainFile
	 *            所生成的训练文件
	 * @param modelFile
	 *            所生成的训练模型文件
	 */
	public void train(String srcDir, String trainFile, String modelFile) throws IOException, RecognizationException, MatrixCacuException;

	/**
	 * 准备训练素材
	 * 
	 * @param srcDir
	 *            训练素材所在的目录，例如本项目中的src/test/resources/src/svm/train目录
	 * @param trainFile
	 *            所生成的训练文件
	 * @param modelFile
	 *            所生成的训练模型文件
	 * @param ioFileFilter
	 */
	public void train(String srcDir, String trainFile, String modelFile, IOFileFilter ioFileFilter)
			throws IOException, RecognizationException, MatrixCacuException;

	/**
	 * 准备测试素材
	 * 
	 * @param srcDir
	 *            测试素材所在的目录，例如本项目中的src/test/resources/src/svm/test目录
	 * @param destFile
	 *            测试素材所生成的文件
	 * @param resultFile
	 *            识别结果文件
	 */
	public void recognize(String srcDir, String destFile, String resultFile) throws IOException, RecognizationException,MatrixCacuException;

	/**
	 * 准备训练素材
	 * 
	 * @param srcDir
	 *            训练素材所在的目录，例如本项目中的src/test/resources/src/svm/test目录
	 * @param destFile
	 *            测试素材所生成的文件
	 * @param resultFile
	 *            识别结果文件
	 * @param ioFileFilter
	 */
	public void recognize(String srcDir, String destFile, String resultFile, IOFileFilter ioFileFilter)
			throws IOException, RecognizationException, MatrixCacuException;

}
