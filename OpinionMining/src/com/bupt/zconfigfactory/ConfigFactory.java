package com.bupt.zconfigfactory;


import java.io.File;
import java.util.List;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 * ���ýӿڶ���
 * 
 * @author bupt
 * 
 * 
 */
public class ConfigFactory {

	private static final String CONFIG_FILE_DEFAULT_PATH = "/AnalysisConfig.xml";
	private static XMLConfiguration config = null;
	private static File path;
    //���ýӿڳ�ʼ��
	public static void init(String configFilePath) {
		// ���δ���û�ȡĬ��ֵ
		if (configFilePath == null) {
			configFilePath = CONFIG_FILE_DEFAULT_PATH;
		}
		try {
			File p = new File(configFilePath);
			config = new XMLConfiguration(configFilePath);
			//�Զ����������ļ�
			config.setReloadingStrategy(new FileChangedReloadingStrategy());
			path = p.getParentFile();
		} catch (ConfigurationException e) {
			System.out.println("Fatal:Create Config Object Error!!!");
			System.exit(1);
		}
}

	/**
	 * ��ȡ�����ļ�
	 * @return
	 */
	public static File getPath() {
		return path;
	}

	// �������ⲿʵ����
	private ConfigFactory() {
	}

	/**
	 * ��ȡ���õ��ַ���ֵ
	 * 
	 * @param configXPath
	 *            ������·��
	 * @return
	 */
	public static String getString(String configXPath) {
		return config.getString(configXPath, null);
	}

	/**
	 * ��ȡ���õ��ַ���ֵ
	 * 
	 * @param configXPath
	 *            ������·��
	 * @param defaultValue
	 *            �������Ĭ��ֵ
	 * @return
	 */
	public static String getString(String configXPath, String defaultValue) {
		return config.getString(configXPath, defaultValue);
	}

	/**
	 * ��ȡ���õ�����ֵ
	 * 
	 * @param configXPath
	 *            ������·��
	 * @return
	 */
	public static int getInt(String configXPath) {
		return config.getInt(configXPath);
	}

	/**
	 * ��ȡfloat�����Ͳ���
	 * 
	 * @param configXPath
	 * @param defaultValue
	 * @return
	 */
	public static float getFloat(String configXPath, float defaultValue) {
		return config.getFloat(configXPath);
	}

	/**
	 * ��ȡ���õ�����ֵ
	 * 
	 * @param configXPath
	 *            ������·��
	 * @param defaultValue
	 *            �������Ĭ��ֵ
	 * @return
	 */
	public static int getInt(String configXPath, int defaultValue) {
		return config.getInt(configXPath, defaultValue);
	}

	public static long getLong(String configXPath, long defaultValue) {
		return config.getLong(configXPath, defaultValue);
	}

	/**
	 * ��ȡ���õ�booleanֵ
	 * 
	 * @param configXPath
	 *            ������·��
	 * @return
	 */
	public static boolean getBoolean(String configXPath) {
		return config.getBoolean(configXPath);
	}

	/**
	 * ��ȡ���õ�booleanֵ
	 * 
	 * @param configXPath
	 *            ������·��
	 * @param defaultValue
	 *            �������Ĭ��ֵ
	 * @return
	 */
	public static boolean getBoolean(String configXPath, boolean defaultValue) {
		return config.getBoolean(configXPath, defaultValue);
	}

	/**
	 * ��ȡ���õ�Listֵ
	 * 
	 * @param configXPath
	 *            ������·��
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getList(String configXPath) {
		return config.getList(configXPath);
	}


	/**
	 * �������õ��ַ���ֵ
	 * 
	 * @param configXPath
	 *            ������·��
	 * @return
	 */
	public static void setString(String configXPath, String value) {
		config.setProperty(configXPath, value);
	}

	/**
	 * �������õ�����ֵ
	 * 
	 * @param configXPath
	 *            ������·��
	 * @return
	 */
	public static void setInt(String configXPath, int value) {
		config.setProperty(configXPath, value);
	}

	/**
	 * ����float�����Ͳ���
	 * 
	 * @param configXPath
	 * @param defaultValue
	 * @return
	 */
	public static void setFloat(String configXPath, float defaultValue) {
		config.setProperty(configXPath, defaultValue);
	}


	/**
	 * ����long�����Ͳ���
	 * 
	 * @param configXPath
	 * @param defaultValue
	 * @return
	 */
	public static void setLong(String configXPath, long defaultValue) {
		config.setProperty(configXPath, defaultValue);
	}

	/**
	 * �������õ�booleanֵ
	 * 
	 * @param configXPath
	 *            ������·��
	 * @param defaultValue
	 *            �������Ĭ��ֵ
	 * @return
	 */
	public static void setBoolean(String configXPath, boolean defaultValue) {
		config.setProperty(configXPath, defaultValue);
	}

	/**
	 * �������õ�Listֵ
	 * 
	 * @param configXPath
	 *            ������·��
	 * @return
	 */
	public static void setList(String configXPath, List<String> list) {
		config.setProperty(configXPath, list);
	}

	/**
	 * ɾ��ָ��������
	 * 
	 * @param key
	 */
	public static void remove(String key) {
		config.clearTree(key);
	}

	/**
	 * ����
	 */
	public static void save() {
		try {
			config.save();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

}
