package com.skyline.hack.captcha.core.spliter.flooding;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.skyline.hack.captcha.core.Constants;
import com.skyline.hack.captcha.util.Utils;

public class FloodingMerger {

	private Comparator<SplitInfo> cmpByPixdels = new Comparator<SplitInfo>() {
		@Override
		public int compare(SplitInfo o1, SplitInfo o2) {
			return o2.pixels - o1.pixels;
		}
	};

	public List<SplitInfo> merge(List<SplitInfo> sis, int cNum) {
		Collections.sort(sis, cmpByPixdels);
		List<SplitInfo> mSis = sis.subList(0, cNum);
		for (int i = cNum; i < sis.size(); i++) {
			SplitInfo fInfo = sis.get(i);
			SplitInfo tInfo = null;
			double distance = Double.MAX_VALUE;
			for (int j = 0; j < mSis.size(); j++) {
				SplitInfo cInfo = mSis.get(j);
				double cDistance = Utils.distance(fInfo.centerX(), fInfo.centerY(), cInfo.centerX(), cInfo.centerY());
				if (cDistance < distance) {
					distance = cDistance;
					tInfo = cInfo;
				}
				// System.out.println(fInfo.index + "," + cInfo.index + ", " +
				// cDistance);
			}

			SplitInfo mInfo = new SplitInfo();
			mInfo.index = Math.min(fInfo.index, tInfo.index);
			mInfo.x = Math.min(fInfo.x, tInfo.x);
			mInfo.y = Math.min(fInfo.y, tInfo.y);
			mInfo.width = Math.max(fInfo.x1(), tInfo.x1()) - mInfo.x;
			mInfo.height = Math.max(fInfo.y1(), tInfo.y1()) - mInfo.y;
			mInfo.pixels = fInfo.pixels + tInfo.pixels;

			BufferedImage mImg = new BufferedImage(mInfo.width, mInfo.height, BufferedImage.TYPE_INT_RGB);
			for (int ii = 0; ii < mInfo.width; ii++) {
				for (int jj = 0; jj < mInfo.height; jj++) {
					mImg.setRGB(ii, jj, Constants.COLOR_BG);
				}
			}
			for (int ii = 0; ii < fInfo.width; ii++) {
				for (int jj = 0; jj < fInfo.height; jj++) {
					if (fInfo.img.getRGB(ii, jj) == Constants.COLOR_CHAR)
						mImg.setRGB(ii + (fInfo.x - mInfo.x), jj + (fInfo.y - mInfo.y), Constants.COLOR_CHAR);
				}
			}

			for (int ii = 0; ii < tInfo.width; ii++) {
				for (int jj = 0; jj < tInfo.height; jj++) {
					if (tInfo.img.getRGB(ii, jj) == Constants.COLOR_CHAR)
						mImg.setRGB(ii + (tInfo.x - mInfo.x), jj + (tInfo.y - mInfo.y), tInfo.img.getRGB(ii, jj));
				}
			}
			mInfo.img = mImg;
			mSis.remove(tInfo);
			mSis.add(mInfo);
		}

		return mSis;
	}
}
