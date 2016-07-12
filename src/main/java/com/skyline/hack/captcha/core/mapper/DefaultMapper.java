package com.skyline.hack.captcha.core.mapper;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyline.hack.captcha.util.Utils;

/**
 * 默认的分类器
 * 
 * @author jairus
 *
 */
public class DefaultMapper implements Mapper {

	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DefaultMapper.class);

	@Override
	public void classifier(String srcDir, String destDir, String textFile) throws Exception {
		classifier(srcDir, destDir, textFile, null);
	}

	@Override
	public void classifier(String srcDirStr, String destDirStr, String textFile, IOFileFilter filter) throws Exception {
		Utils.checkArgNullable("srcDir", false, srcDirStr);
		Utils.checkArgNullable("destDir", false, destDirStr);
		File destDir = new File(destDirStr);
		if (destDir.exists() && destDir.isDirectory())
			FileUtils.cleanDirectory(destDir);
		else
			destDir.mkdir();

		List<File> srcSubDirs = Utils.listDirs(srcDirStr);
		@SuppressWarnings("unchecked")
		List<String> texts = FileUtils.readLines(new File(textFile), "UTF-8");
		Map<String, String> textMap = new HashMap<>();
		for (String text : texts) {
			String[] tx = text.split(" ");
			if (tx == null || tx.length < 2) {
				continue;
			}
			textMap.put(tx[0], tx[1]);
		}
		for (int i = 0; i < srcSubDirs.size(); i++) {
			File srcSubDir = srcSubDirs.get(i);
			String text = textMap.get(srcSubDir.getName());
			if (text == null) {
				LOG.debug("classifier, fail, can not found text, srcSubDir: " + srcSubDir);
			}
			LOG.debug("classifier, srcSubDir: " + srcSubDir + ", text: " + text);
			Iterator<File> it = Utils.listFiles(srcSubDir.getAbsolutePath(), filter);
			int index = 0;
			while (it.hasNext()) {
				File file = it.next();
				File destSubDir = new File(destDir, Utils.char2String(text.charAt(index)));
				if (!destSubDir.exists())
					destSubDir.mkdir();
				File destFile = new File(destSubDir, file.getParentFile().getName() + "_" + file.getName());
				FileUtils.copyFile(file, destFile);
				index++;
			}
		}
	}

}
