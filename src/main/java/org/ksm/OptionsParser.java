package org.ksm;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;


@Slf4j
public class OptionsParser {
    public static final String CONFIG = "config";
    public static final String PROPERTIES = "properties";
    public static final String SPARK_CONF = "sparkConf";
    public static final String SYSTEM = "system.";

    public static Options buildOptions() {
        Options options = new Options();
        Option configOption = new Option("config", true, "config file for the project");
        configOption.setRequired(false);
        options.addOption(configOption);

        Option propOption = new Option("properties", true, "property file for the project");
        propOption.setRequired(true);
        options.addOption(propOption);

        Option sparkOption = new Option("sparkConf", true, "sparkConf file for the project");
        sparkOption.setRequired(false);
        options.addOption(sparkOption);

        Option hiveOption = new Option("hiveSite", true, "hive-site.xml file for the project");
        hiveOption.setRequired(false);
        options.addOption(hiveOption);
        return options;

    }

    public static Properties retrieveAllProperties(CommandLine cmd, Map<String, String> extraConf)
            throws Exception {

        String configFilePath = cmd.getOptionValue(OptionsParser.CONFIG);
        String envFilePath = cmd.getOptionValue(OptionsParser.PROPERTIES);
        String sparkConfPath = cmd.getOptionValue(OptionsParser.SPARK_CONF);

        Properties properties = getProperties(configFilePath);
        properties.putAll(getProperties(envFilePath));
        properties.putAll(getProperties(sparkConfPath));
        getSystemProperties(properties);
        return properties;
    }

    public static Properties getProperties(String filePath) throws Exception {
        Properties properties = new Properties();
        if (StringUtils.isNoneBlank(filePath)) {
            try {
                properties.load(new FileReader(filePath));
            } catch (IOException e) {
                log.error(e.toString());
                throw e;
            }
        }
        return properties;
    }

    public static void getSystemProperties(Properties properties) {
        final Map<String, String> getenv = System.getenv();
        for (Map.Entry kv : getenv.entrySet()) {
            properties.put(SYSTEM + kv.getKey(), kv.getValue());
        }
    }
}
