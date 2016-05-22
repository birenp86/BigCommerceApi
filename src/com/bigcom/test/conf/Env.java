package com.bigcom.test.conf;

import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Env {

	private static final String SRC_RESOURCE = "src/com/bigcom/test/conf/";
	static Logger log = (Logger) LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
	public final static String LOCAL = "localhost";

	private static Env conf = null;
	Properties props = null;

	public static Env instance() {
		if (conf == null) {
			conf = new Env();
			try {
				String testProp = "bigCommerce.properties";
				testProp = SRC_RESOURCE + testProp;
				log.info("-DtestProperties=" + testProp);
				String testSuite = "testng";
				log.info("-DtestSuite=" + testSuite);
				conf.props = loadProps(testProp);
				brands_url = getEnv("BRANDS_URL");
				count_url = getEnv("COUNT_URL");
				user = getEnv("USER");
				userkey = getEnv("USERKEY");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return conf;
	}

	public static String user = null;
	public static String userkey = null;
	public static String brands_url = null;
	public static String count_url = null;
	public static String HTTPS = "https://";

	public static String getEnv(String key) {
		String val = conf.props.getProperty(key);
		if (val == null || val.length() == 0) {
			return val;
		}
		val = val.trim();
		return val;
	}

	public static String getEnv(String key, String defValue) {
		String val = conf.props.getProperty(key, defValue);
		val.trim();
		if (val == null || val.length() == 0) {
			val = defValue;
		}
		return val;
	}

	public static void printProps(Properties props) {
		Enumeration<Object> keys = props.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = (String) props.get(key);
			log.info(key + "==" + value);
		}
	}

	public static Properties loadProps(String file) throws Exception {
		log.info("loadProps = " + file);
		Properties p = new Properties();
		if (file != null && !file.equals("")) {
			p.load(new FileInputStream(file));
		}
		return p;
	}

	Properties getProps() {
		return conf.props;
	}
}

