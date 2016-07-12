package com.skyline.hack.captcha.core.spliter.flooding;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyline.hack.captcha.core.Constants;

/**
 * 粘连字符的分割器
 * 
 * @author jairus
 *
 */
public class FloodingAttCharSpliter {

	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FloodingAttCharSpliter.class);

	private Comparator<SplitInfo> cmpByPixdels = new Comparator<SplitInfo>() {
		@Override
		public int compare(SplitInfo o1, SplitInfo o2) {
			return o2.pixels - o1.pixels;
		}
	};

	public List<SplitInfo> split(List<SplitInfo> sis, int avgPixels, int cNum) {
		Collections.sort(sis, cmpByPixdels);
		List<SplitInfo> splited = new ArrayList<>();
		List<SplitInfo> toSplited = new ArrayList<>();
		List<Integer> toSplitedCnt = new ArrayList<>();
		int left = cNum;
		for (int i = 0; i < sis.size(); i++) {
			SplitInfo ci = sis.get(i);
			int cCnt = Math.round((float) ci.pixels / avgPixels);
			if (cCnt > 1) {
				toSplitedCnt.add(cCnt);
				toSplited.add(ci);
			} else {
				splited.add(ci);
				left--;
			}
		}

		if (left >= 1 && toSplited.size() <= 0) {
			int orgLeft=left;
			for (int i = 0; i < orgLeft; i++) {
				SplitInfo si = splited.remove(0);
				toSplitedCnt.add(2);
				toSplited.add(si);
				left++;
			}
		}

		for (int i = 0; i < toSplited.size(); i++) {
			SplitInfo ci = toSplited.get(i);
			// 当前节点可被切成几个
			int cCnt = toSplitedCnt.get(i);
			// 除当前节点之外，还有几个点还没有被切
			int toSplitedSize = toSplited.size() - i - 1;
			// 当前节点 最多还可以被切成几个
			int cCntMax = left - toSplitedSize;
			if (cCntMax < cCnt) {
				cCnt = cCntMax;
			}
			if (cCnt == 1) {
				splited.add(ci);
				left--;
			} else {
				SplitInfo[] subInfos = split(ci, cCnt);
				for (SplitInfo suInfo : subInfos) {
					splited.add(suInfo);
					left--;
				}
			}
		}

		return splited;
	}

	public SplitInfo[] split(SplitInfo ci, int cCnt) {
		LOG.debug("split, ci: " + ci + ", cCnt: " + cCnt);
		SplitInfo[] is = new SplitInfo[cCnt];
		BufferedImage src = ci.img;
		int itemWidth = ci.width / cCnt;
		for (int i = 0; i < cCnt; i++) {
			SplitInfo cInfo = new SplitInfo();
			BufferedImage cImg = new BufferedImage(itemWidth, ci.height, BufferedImage.TYPE_INT_RGB);
			cInfo.index = ci.index + i;
			cInfo.x = ci.x + itemWidth * i;
			cInfo.y = ci.y;
			cInfo.width = itemWidth;
			cInfo.height = ci.height;
			cInfo.pixels = split(src, cImg, itemWidth * i, itemWidth * (i + 1), ci.height);
			cInfo.img = cImg;
			is[i] = cInfo;
		}
		return is;
	}

	// private int getSplitX(BufferedImage src, int startX, int endX, int
	// height) {
	// int splitX = (endX + startX) / 2;
	// int splitPixels = Integer.MAX_VALUE;
	// for (int i = startX; i <= endX; i++) {
	// int csp = 0;
	// for (int j = 0; j < height; j++) {
	// int color = src.getRGB(i + startX, j);
	// if (color == Constants.COLOR_CHAR)
	// csp++;
	// }
	// csp += Math.abs((endX + startX) / 2 - i) / 2;
	// if (csp < splitPixels) {
	// splitPixels = csp;
	// splitX = i;
	// }
	// }
	// return splitX;
	// }

	public int split(BufferedImage src, BufferedImage dest, int startX, int endX, int height) {
		int pixels = 0;
		for (int i = 0; i < (endX - startX); i++) {
			for (int j = 0; j < height; j++) {
				int color = src.getRGB(i + startX, j);
				if (color == Constants.COLOR_CHAR)
					pixels++;
				dest.setRGB(i, j, color);
			}
		}
		return pixels;
	}
}
