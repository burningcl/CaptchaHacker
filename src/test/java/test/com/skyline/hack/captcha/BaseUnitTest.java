package test.com.skyline.hack.captcha;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;

import ch.qos.logback.core.joran.spi.JoranException;
import test.com.skyline.hack.captcha.commons.log.LogBackConfigLoader;

public class BaseUnitTest {

	protected static String baseDir;

	@BeforeClass
	public static void beforeClass() {
		baseDir = BaseUnitTest.class.getResource("/").getFile().toString();
		System.out.println("beforeClass");
		try {
			LogBackConfigLoader.load(new File(baseDir, "logback-log.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JoranException e) {
			e.printStackTrace();
		}
	}

}
