package com.skyline.hack.captcha.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.skyline.hack.captcha.core.HandlerFileFilter;

/**
 * 默认工作类
 * 
 * @author jairus
 *
 */
public class Utils {

	/**
	 * 检查参数是否为空
	 * 
	 * 如果不能为空的参数，传入了空值，则会抛出IllegalArgumentException
	 * 
	 * @param name
	 * @param nullbale
	 * @param value
	 */
	public static void checkArgNullable(String name, boolean nullbale, Object value) {
		if (value == null && !nullbale) {
			throw new IllegalArgumentException(name + " should not be null");
		}
	}

	/**
	 * 计算两个点之间的距离
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}

	/**
	 * 获取dir下的文件列表
	 * 
	 * @param dir
	 * @param filter
	 * @return
	 */
	public static Iterator<File> listFiles(String dir, IOFileFilter filter) {
		return listFiles(new File(dir), filter);
	}

	/**
	 * 获取dir下的文件列表
	 * 
	 * @param dir
	 * @param filter
	 * @return
	 */
	public static Iterator<File> listFiles(File dir, IOFileFilter filter) {
		if (filter == null) {
			filter = HandlerFileFilter.imgFileFilter();
		}
		@SuppressWarnings("unchecked")
		Collection<File> collection = FileUtils.listFiles(dir, filter, null);
		return collection.iterator();
	}

	/**
	 * 获取dir下的文件列表
	 * 
	 * @param dir
	 * @param filter
	 * @return
	 */
	public static List<File> listDirs(String dir) {
		List<File> subDirs = new ArrayList<>();
		File[] files = new File(dir).listFiles();
		for (File file : files) {
			if (file.isDirectory())
				subDirs.add(file);
		}
		return subDirs;
	}

	/**
	 * 创建文件
	 * 
	 * 如果文件所在的目录不存在，则创建此目录； 如果文件不存在，创建此文件；
	 * 
	 * @param dir
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static File createFile(File dir, String filename) throws IOException {
		File destFile = new File(dir, filename);
		if (!destFile.exists()) {
			if (!destFile.getParentFile().exists()) {
				destFile.mkdirs();
			}
			destFile.createNewFile();
		}
		return destFile;
	}

	/**
	 * 创建文件
	 * 
	 * 如果文件所在的目录不存在，则创建此目录； 如果文件不存在，创建此文件；
	 * 
	 * @param dir
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static File createFile(String dir, String filename) throws IOException {
		return createFile(new File(dir), filename);
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public static String char2String(char c) {
		if (c >= 'a') {
			return String.valueOf(c);
		} else {
			return c + "_";
		}
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static Character string2Char(String str) {
		if (str == null || str.length() <= 0 || str.length() > 2) {
			return null;
		} else if (str.length() == 1) {
			return str.charAt(0);
		} else {
			if (str.charAt(1) != '_') {
				return null;
			} else {
				return str.charAt(0);
			}
		}
	}

}
