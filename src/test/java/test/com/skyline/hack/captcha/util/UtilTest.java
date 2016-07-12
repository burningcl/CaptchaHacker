package test.com.skyline.hack.captcha.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyline.hack.captcha.util.Utils;

import test.com.skyline.hack.captcha.BaseUnitTest;

/**
 * Util的测试
 * 
 * @author jairus
 *
 */
public class UtilTest extends BaseUnitTest {
	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger(UtilTest.class);

	@Test
	public void testDistance() throws Exception {
		Utils.distance(-1, -1, 1, 1);
	}
}
