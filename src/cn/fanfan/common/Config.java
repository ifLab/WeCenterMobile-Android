package cn.fanfan.common;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/*
 * 功能：将一些常用的键值对如：接口的url，写入配置文件中，便于以后修改。
 * 
 * edit at 2014/7/14 by huangchen
 * 
 * 在配置文件(config.properties)中写入信息，信息格式已经已经在配置文件中给出
 * 调用函数getValue(String key)
 * 如：String loginUrl = Config.getValue("loginUrl")
 */
public class Config {
	private static Properties props = new Properties();
	static String profilepath = "config.properties";
	static {
		try {
			props.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("config.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getValue(String key) {
		return props.getProperty(key);
	}

	public static void updateProperties(String keyname, String keyvalue) {
		try {
			OutputStream fos = new FileOutputStream(Thread.currentThread()
					.getContextClassLoader().getResource("config.properties")
					.getFile());
			props.setProperty(keyname, keyvalue);
			props.store(fos, "Update '" + keyname + "' value");
			fos.close();
		} catch (IOException e) {
		}
	}

}
