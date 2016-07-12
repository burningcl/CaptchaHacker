package test.com.skyline.hack.captcha.commons.spliter;

import java.io.File;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyline.hack.captcha.core.HandlerFileFilter;
import com.skyline.hack.captcha.core.spliter.flooding.FloodingSpliter;

import test.com.skyline.hack.captcha.commons.pretreater.DefaultPretreaterTest;

/**
 * 洪泛法分割器测试
 * 
 * @author jairus
 *
 */
public class FloodingSpliterTest extends DefaultPretreaterTest {
	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FloodingSpliterTest.class);

	FloodingSpliter spliter;

	@Before
	public void before() {
		LOG.debug("before");
		super.before();
		int destWidth = 64;
		int destHeight = 64;
		spliter = new FloodingSpliter(destWidth, destHeight, 5);
	}

	@Test
	public void test() throws Exception {
		LOG.debug("test");
		super.test();
		File srcDir = new File(baseDir, "dest/common/pretreat_test");
		File destDir = new File(baseDir, "dest/common/split_test");
		String[] types = { "png" };
		IOFileFilter filter = new HandlerFileFilter(types);
		spliter.split(srcDir.getAbsolutePath(), destDir.getAbsolutePath(), filter);
		LOG.debug("test, success");
	}
}
