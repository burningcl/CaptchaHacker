package test.com.skyline.hack.captcha.crawler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.com.skyline.hack.captcha.BaseUnitTest;

/**
 * 这是一个用于抓取验证码图片的爬虫
 * 
 * @author jairus
 *
 */
public class Crawler extends BaseUnitTest {

	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger(Crawler.class);

	/**
	 * 
	 */
	public static String COOKIE = null;
	public static String URL = null;
	public static String HOST = null;
	public static int CNT = 0;

	@Before
	public void before() {
		InputStream is = null;
		try {
			File file = new File(baseDir, "crawler.properties");
			LOG.debug("load config, file: " + file.getAbsolutePath());
			is = new FileInputStream(file);
			Properties props = new Properties();
			props.load(is);
			COOKIE = props.getProperty("crawler.cookie");
			URL = props.getProperty("crawler.captcha_url");
			HOST = props.getProperty("crawler.host");
			CNT = Integer.valueOf(props.getProperty("crawler.download_cnt", "0"));
			LOG.debug("load config, COOKIE: " + COOKIE);
			LOG.debug("load config, URL: " + URL);
			LOG.debug("load config, HOST: " + HOST);
			LOG.debug("load config, CNT: " + CNT);
		} catch (Exception e) {
			LOG.warn("load config, fail", e);
		}
	}

	@Test
	public void test() {
		for (int i = 0; i < CNT; i++) {
			try {
				File file = new File(baseDir, "dest/train/" + i + ".png");
				LOG.debug("download, file: " + file);
				download(file);
			} catch (Exception e) {
				LOG.warn("download, fail", e);
			}
		}
	}

	public static void download(File file) throws Exception {
		java.net.URL url = new java.net.URL(URL);
		HttpURLConnection connection = null;
		InputStream is = null;
		if (file.exists()) {
			file.delete();
		}
		if (!file.getParentFile().exists()) {
			file.mkdirs();
		}
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setReadTimeout(10 * 1000);
			connection.setConnectTimeout(5 * 1000);
			connection.setRequestProperty("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4");
			connection.setRequestProperty("Cache-Control", "max-age=0");
			connection.setRequestProperty("Connection", "keep-alive");
			connection.setRequestProperty("Cookie", COOKIE);
			connection.setRequestProperty("Host", HOST);
			connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
			connection.connect();
			is = connection.getInputStream();
			byte[] bytes = decompress(is);
			LOG.debug("bytes.length: " + bytes.length);
			LOG.debug("connection.getResponseCode(): " + connection.getResponseCode());
			LOG.debug("connection.getContentType(): " + connection.getContentType());
			LOG.debug("connection.getContent(): " + connection.getContent());
			LOG.debug("connection.getContentLength(): " + connection.getContentLength());
			FileUtils.writeByteArrayToFile(file, bytes);
		} finally {
			if (connection != null)
				connection.disconnect();
			if (is != null)
				is.close();
		}
	}

	public static byte[] decompress(InputStream is) throws Exception {

		GZIPInputStream gis = null;
		ByteArrayOutputStream os = null;

		try {
			os = new ByteArrayOutputStream();
			gis = new GZIPInputStream(is);
			int count;
			byte data[] = new byte[1024];
			while ((count = gis.read(data, 0, 1024)) != -1) {
				os.write(data, 0, count);
			}
			return os.toByteArray();
		} finally {
			if (gis != null) {
				gis.close();
			}
			if (os != null) {
				os.close();
			}
		}
	}

}
