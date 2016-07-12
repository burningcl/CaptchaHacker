package test.com.skyline.hack.captcha.commons.mapper;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyline.hack.captcha.core.mapper.Mapper;
import com.skyline.hack.captcha.core.mapper.DefaultMapper;
import com.skyline.hack.captcha.core.pretreater.DefaultPretreater;
import com.skyline.hack.captcha.core.pretreater.Pretreater;
import com.skyline.hack.captcha.core.spliter.flooding.FloodingSpliter;

import test.com.skyline.hack.captcha.BaseUnitTest;

/**
 * 分类器测试
 * 
 * @author jairus
 *
 */
public class MapperTest extends BaseUnitTest {

	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MapperTest.class);

	Pretreater pretreater;

	FloodingSpliter spliter;

	Mapper classifier;

	@Before
	public void before() {
		LOG.debug("before");

		pretreater = new DefaultPretreater(false);

		int destWidth = 64;
		int destHeight = 64;
		spliter = new FloodingSpliter(destWidth, destHeight, 5, false);

		classifier = new DefaultMapper();
	}

	@Test
	public void test() throws Exception {

		File srcDir = new File(baseDir, "src/train");
		File pretreatDir = new File(baseDir, "dest/train/pretreat");
		File splitDir = new File(baseDir, "dest/train/split");
		File classifyDir = new File(baseDir, "dest/train/map");
		pretreater.pretreat(srcDir.getAbsolutePath(), pretreatDir.getAbsolutePath());
		spliter.split(pretreatDir.getAbsolutePath(), splitDir.getAbsolutePath());
		File srcTxtFile = new File(srcDir, "train.txt");
		classifier.classifier(splitDir.getAbsolutePath(), classifyDir.getAbsolutePath(), srcTxtFile.getAbsolutePath());

	}
}
