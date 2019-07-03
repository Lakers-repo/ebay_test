package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
    private static String properiesName = "";

    public PropertiesUtils() {

    }

    public PropertiesUtils(String fileName) {
        this.properiesName = fileName;
    }

    /**
     * 按key获取值
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        String value = "";
        InputStream is = null;
        try {
            is = PropertiesUtils.class.getClassLoader().getResourceAsStream(properiesName);
            Properties p = new Properties();
            p.load(is);
            value = p.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }
}
