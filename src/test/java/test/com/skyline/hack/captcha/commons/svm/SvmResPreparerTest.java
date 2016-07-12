package test.com.skyline.hack.captcha.commons.svm;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyline.hack.captcha.core.recognizer.svm.DefaultSvmResPreparer;
import com.skyline.hack.captcha.core.recognizer.svm.SvmResPreparer;

import test.com.skyline.hack.captcha.BaseUnitTest;
import test.com.skyline.hack.captcha.commons.spliter.FloodingSpliterTest;

/**
 * 
 * @author jairus
 *
 */
public class SvmResPreparerTest extends BaseUnitTest {
	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FloodingSpliterTest.class);

	SvmResPreparer preparer;

	@Before
	public void before() {
		LOG.debug("before");
		preparer = new DefaultSvmResPreparer();
	}

	@Test
	public void test() throws Exception {
		LOG.debug("test");
		File trainSrcDir = new File(baseDir, "src/recognizer/train");
		File trainDestFile = new File(baseDir, "dest/train/recognizer/svm_train.src");
		preparer.prepareTrain(trainSrcDir.getAbsolutePath(), trainDestFile.getAbsolutePath());
		
		File testSrcDir = new File(baseDir, "src/recognizer/test");
		File testDestFile = new File(baseDir, "dest/train/recognizer/svm_test.src");
		preparer.prepareTest(testSrcDir.getAbsolutePath(), testDestFile.getAbsolutePath());
		LOG.debug("test, success");
	}
}
