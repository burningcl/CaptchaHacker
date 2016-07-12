package test.com.skyline.hack.captcha.commons.pretreater;

import java.io.File;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyline.hack.captcha.core.HandlerFileFilter;
import com.skyline.hack.captcha.core.pretreater.DefaultPretreater;
import com.skyline.hack.captcha.core.pretreater.Pretreater;

import test.com.skyline.hack.captcha.BaseUnitTest;

/**
 * 默认处理器的测试
 * 
 * @author jairus
 *
 */
public class DefaultPretreaterTest extends BaseUnitTest {

	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DefaultPretreaterTest.class);

	Pretreater pretreater;

	@Before
	public void before() {
		LOG.debug("before");
		pretreater = new DefaultPretreater();
	}

	@Test
	public void test() throws Exception {
		LOG.debug("test");
		File srcDir = new File(baseDir, "src/basic");
		File destDir = new File(baseDir, "dest/common/pretreat_test");
		String[] types = { "png" };
		IOFileFilter filter = new HandlerFileFilter(types);
		LOG.debug("test, srcDir: " + srcDir + ", destDir; " + destDir);
		pretreater.pretreat(srcDir.getAbsolutePath(), destDir.getAbsolutePath(), filter);
		LOG.debug("test, success");
	}
}
