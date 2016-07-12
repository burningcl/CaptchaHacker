package test.com.skyline.hack.captcha.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyline.hack.captcha.util.ImageUtils;
import com.skyline.hack.captcha.util.Utils;

import test.com.skyline.hack.captcha.BaseUnitTest;

/**
 * ImageUtil的测试
 * 
 * @author jairus
 *
 */
public class ImageUtilTest extends BaseUnitTest {
	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ImageUtilTest.class);

	@Test
	public void test() throws Exception {
		LOG.debug("test");
		File srcDir = new File(baseDir, "src/train");
		File destDir = new File(baseDir, "dest/util/image_util_test");
		Iterator<File> files = Utils.listFiles(srcDir, null);
		while (files.hasNext()) {
			File file = files.next();
			LOG.debug("resize, file: " + file.getAbsolutePath());
			BufferedImage srcImg = ImageIO.read(file);
			BufferedImage destImg = ImageUtils.resize(64, 64, srcImg);
			File destFile = new File(destDir, file.getName());
			if (destFile.exists()) {
				destFile.deleteOnExit();
			}
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			LOG.debug("resize, destImg: " + destImg);
			ImageIO.write(destImg, "png", destFile);
		}
		LOG.debug("test, success");
	}
}
