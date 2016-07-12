package test.com.skyline.hack.captcha.util;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyline.hack.captcha.util.ImageUtils;

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
		File[] files = srcDir.listFiles();
		for (File file : files) {
			LOG.debug("resize, file: " + file.getAbsolutePath());
			BufferedImage srcImg = ImageIO.read(file);
			BufferedImage destImg = ImageUtils.resize(64, 64, srcImg);
			File destFile = new File(destDir, file.getName());
			if (destFile.exists()) {
				destFile.deleteOnExit();
			}
			if (!destDir.exists()) {
				destFile.mkdirs();
			}
			LOG.debug("resize, destImg: " + destImg);
			ImageIO.write(destImg, "png", destFile);
		}
		LOG.debug("test, success");
	}
}
