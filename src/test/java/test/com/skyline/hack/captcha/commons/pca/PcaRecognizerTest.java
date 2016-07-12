package test.com.skyline.hack.captcha.commons.pca;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyline.hack.captcha.core.exception.RecognizationException;
import com.skyline.hack.captcha.core.recognizer.pca.DefaultPcaRecognizer;
import com.skyline.hack.captcha.core.recognizer.pca.PcaRecognizer;

import test.com.skyline.hack.captcha.BaseUnitTest;
import test.com.skyline.hack.captcha.commons.spliter.FloodingSpliterTest;

/**
 * 
 * @author jairus
 *
 */
public class PcaRecognizerTest extends BaseUnitTest {
	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FloodingSpliterTest.class);

	PcaRecognizer recgonizer;

	@Before
	public void before() throws RecognizationException {
		LOG.debug("before");
		recgonizer = new DefaultPcaRecognizer();

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

		long t2 = System.currentTimeMillis();
		LOG.debug("test, success,  cost: " + (t2 - t1));

		File testSrcDir = new File(baseDir, "src/svm/test");
		File testDestFile = new File(baseDir, "dest/train/svm/svm_test.src");
		recgonizer.recognize(testSrcDir.getAbsolutePath(), testDestFile.getAbsolutePath(), null);
		long t3 = System.currentTimeMillis();
		LOG.debug("test, success, train cost: " + (t2 - t1) + ", test cost: " + (t3 - t2));
	}
}
