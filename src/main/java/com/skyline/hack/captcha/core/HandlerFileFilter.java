package com.skyline.hack.captcha.core;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 文件类型的过滤器
 * 
 * @author jairus
 *
 */
public class HandlerFileFilter implements IOFileFilter {

	/**
	 * 文件类型的Set
	 */
	protected final Set<String> filterSet = new HashSet<>();

	/**
	 * 
	 * @param types
	 *            只有在该数组中的文件类型才能被接受
	 */
	public HandlerFileFilter(String[] types) {
		if (!ArrayUtils.isEmpty(types)) {
			for (String type : types) {
				if (StringUtils.isBlank(type)) {
					continue;
				}
				filterSet.add(type.toLowerCase());
			}
		}
	}

	@Override
	public boolean accept(File file) {
		if (file == null || !file.exists()) {
			return false;
		}
		String type = FilenameUtils.getExtension(file.getName());
		if (type != null) {
			type = type.toLowerCase();
			return filterSet.contains(type);
		} else {
			if (filterSet.size() <= 0) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public boolean accept(File dir, String name) {
		if (dir == null || !dir.exists()) {
			return false;
		}
		File file = new File(dir, name);
		return accept(file);
	}

	/**
	 * 图片文件过滤器
	 */
	protected static HandlerFileFilter IMG_FILE_FILTER = null;

	public static String[] IMG_TYPES = { "png", "jpg", "jpeg" };

	/**
	 * 获取一个 图片文件过滤器。
	 * 
	 * @return
	 */
	public static HandlerFileFilter imgFileFilter() {
		if (IMG_FILE_FILTER != null) {
			return IMG_FILE_FILTER;
		}
		synchronized (HandlerFileFilter.class) {
			if (IMG_FILE_FILTER != null) {
				return IMG_FILE_FILTER;
			}
			IMG_FILE_FILTER = new HandlerFileFilter(IMG_TYPES);
		}
		return IMG_FILE_FILTER;
	}
}
