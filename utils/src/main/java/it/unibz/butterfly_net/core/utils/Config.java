package it.unibz.butterfly_net.core.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final String CONFIG_FILE_PATH = "utils/src/main/resources/application.properties";
    private static Config instance = null;
    private Properties props;

    public static Config getInstance() throws IOException {
        if (Config.instance == null)
            Config.instance = new Config();

        return Config.instance;
    }

    private Config() throws IOException {
        FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH);
        props = new Properties();
        props.load(fis);
    }

    public String property(String key) {
        return props.getProperty(key);
    }
}
