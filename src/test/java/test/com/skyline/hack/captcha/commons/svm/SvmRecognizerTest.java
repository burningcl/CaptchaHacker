package test.com.skyline.hack.captcha.commons.svm;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyline.hack.captcha.core.exception.RecognizationException;
import com.skyline.hack.captcha.core.recognizer.svm.DefaultSvmRecognizer;
import com.skyline.hack.captcha.core.recognizer.svm.DefaultSvmResPreparer;
import com.skyline.hack.captcha.core.recognizer.svm.SvmRecognizer;
import com.skyline.hack.captcha.core.recognizer.svm.SvmResPreparer;

import test.com.skyline.hack.captcha.BaseUnitTest;
import test.com.skyline.hack.captcha.commons.spliter.FloodingSpliterTest;

/**
 * 
 * @author jairus
 *
 */
public class SvmRecognizerTest extends BaseUnitTest {
	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FloodingSpliterTest.class);

	SvmResPreparer preparer;
	SvmRecognizer recgonizer;

	@Before
	public void before() throws RecognizationException {
		LOG.debug("before");
		preparer = new DefaultSvmResPreparer();
		recgonizer = new DefaultSvmRecognizer(preparer);

	}

	@Test
	public void test() throws Exception {
		LOG.debug("test");
		long t1 = System.currentTimeMillis();
		File trainSrcDir = new File(baseDir, "src/svm/train");
		File trainDestFile = new File(baseDir, "dest/train/svm/svm_train.src");
		File trainModelFile = new File(baseDir, "dest/train/svm/svm_train.model");
		recgonizer.train(trainSrcDir.getAbsolutePath(), trainDestFile.getAbsolutePath(),
				trainModelFile.getAbsolutePath());

		// recgonizer.loadModel(trainModelFile.getAbsolutePath());

		File testSrcDir = new File(baseDir, "src/svm/test");
		File testDestFile = new File(baseDir, "dest/train/svm/svm_test.src");
		recgonizer.recognize(testSrcDir.getAbsolutePath(), testDestFile.getAbsolutePath(), null);
		long t2 = System.currentTimeMillis();
		LOG.debug("test, success,  cost: " + (t2 - t1));
	}
}
