package com.skyline.hack.captcha.core.spliter.flooding;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyline.hack.captcha.core.Constants;
import com.skyline.hack.captcha.core.spliter.Spliter;
import com.skyline.hack.captcha.util.ImageUtils;
import com.skyline.hack.captcha.util.Utils;

/**
 * 基于洪泛法算法的验证码分割器
 * 
 * @author jairus
 *
 */
public class FloodingSpliter implements Spliter {
	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FloodingSpliter.class);

	private int destWidth;

	private int destHeight;

	private int cNum;

	/**
	 * 是否在批处理时抛出异常
	 * 
	 * 如果为true，则在批处理时抛出异常， 中断批处理；
	 * 
	 * 如果为false，则在批处理时吞没异常，继续批处理；
	 */
	private boolean throwBashException = true;

	/**
	 * 
	 * @param destWidth
	 *            分割好的图片的宽度
	 * @param destHeight
	 *            分割好的图片的高度
	 * @param cNum
	 *            文字数量
	 */
	public FloodingSpliter(int destWidth, int destHeight, int cNum) {
		this.destWidth = destWidth;
		this.destHeight = destHeight;
		this.cNum = cNum;
	}

	/**
	 * 
	 * @param destWidth
	 *            分割好的图片的宽度
	 * @param destHeight
	 *            分割好的图片的高度
	 * @param cNum
	 *            文字数量
	 * @param throwBashException
	 */
	public FloodingSpliter(int destWidth, int destHeight, int cNum, boolean throwBashException) {
		this.destWidth = destWidth;
		this.destHeight = destHeight;
		this.cNum = cNum;
		this.throwBashException = throwBashException;
	}

	@Override
	public void split(String srcDir, String destDir) throws Exception {
		this.split(srcDir, destDir, null);
	}

	@Override
	public void split(String srcDir, String destDir, IOFileFilter filter) throws Exception {
		Utils.checkArgNullable("srcDir", false, srcDir);
		Utils.checkArgNullable("destDir", false, destDir);
		Iterator<File> it = Utils.listFiles(srcDir, filter);
		while (it.hasNext()) {
			File file = it.next();
			File destSubDir = new File(destDir, file.getName());
			if (destSubDir.exists()) {
				FileUtils.cleanDirectory(destSubDir);
			} else {
				destSubDir.mkdirs();
			}
			try {
				BufferedImage srcImg = ImageIO.read(file);
				List<BufferedImage> destImgs = split(srcImg);

				LOG.debug("split, srcImg: " + file + ", destDir: " + destSubDir + ", destImgs.size(): "
						+ destImgs.size());
				for (int i = 0; i < destImgs.size(); i++) {
					BufferedImage destImg = destImgs.get(i);
					File destFile = Utils.createFile(destSubDir, i + ".png");
					ImageIO.write(destImg, "png", destFile);
				}
			} catch (Exception e) {
				if (throwBashException)
					throw e;
				else
					LOG.warn("split, fail, srcFile: " + file, e);
			}
		}
	}

	@Override
	public List<BufferedImage> split(BufferedImage srcImg) throws Exception {
		Utils.checkArgNullable("srcImg", false, srcImg);
		int width = srcImg.getWidth();
		int height = srcImg.getHeight();
		int charPixelThreshold = width * height / 800;
		List<SplitInfo> destInfos = new ArrayList<SplitInfo>();
		boolean[][] visitMap = new boolean[width][height];
		FloodingVisitor visitor = new FloodingVisitor(visitMap, srcImg);
		int index = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (visitMap[i][j]) {
					continue;
				}
				int color = FloodingVisitor.getColor(i, j, srcImg);
				if (color == Constants.COLOR_CHAR) {
					SplitInfo destInfo = visitor.getCimg(i, j);
					// 字符像素点太少的图，直接扔掉了
					if (destInfo.pixels > charPixelThreshold) {
						destInfo.index += index;
						LOG.debug("split, i: " + i + ", j: " + j + ", destInfo: " + destInfo);
						destInfos.add(destInfo);
						index += 100;
					}
				}
				visitMap[i][j] = true;
			}
		}

		destInfos = discardPoints(destInfos);
		if (destInfos.size() < cNum) {
			FloodingAttCharSpliter attCharSpliter = new FloodingAttCharSpliter();
			destInfos = attCharSpliter.split(destInfos, avgPixels, cNum);
		}
		return convert(destInfos);
	}

	int avgPixels;

	private List<SplitInfo> discardPoints(List<SplitInfo> sis) {
		int totalPixels = 0;
		for (SplitInfo si : sis) {
			totalPixels += si.pixels;
		}
		avgPixels = totalPixels / cNum;
		int pointPointsMax = avgPixels / 6;
		List<SplitInfo> dsis = new ArrayList<>();
		for (SplitInfo si : sis) {
			if (si.pixels < pointPointsMax) {
				continue;
			}
			dsis.add(si);
		}
		return dsis;
	}

	private Comparator<SplitInfo> cmpByIndex = new Comparator<SplitInfo>() {
		@Override
		public int compare(SplitInfo o1, SplitInfo o2) {
			return o1.index - o2.index;
		}
	};

	private List<BufferedImage> convert(List<SplitInfo> sis) throws Exception {
		Collections.sort(sis, cmpByIndex);
		List<BufferedImage> list = new ArrayList<>();
		for (SplitInfo s : sis) {
			list.add(ImageUtils.resize(destWidth, destHeight, s.img));
		}
		return list;
	}

}
