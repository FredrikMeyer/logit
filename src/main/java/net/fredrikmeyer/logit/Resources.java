package net.fredrikmeyer.logit;

import com.fasterxml.jackson.core.util.VersionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Resources {

    static Logger logger = LoggerFactory.getLogger(Resources.class);

    /**
     * Reads the version from the version.properties file for the given package.
     *
     * @param packageName the name of the package to read the version for
     * @return the version string for the given package, or null if an error occurred
     */
    public static String readVersion(String packageName) {
        try (InputStream input = VersionUtil.class.getClassLoader()
                .getResourceAsStream("version.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(packageName + ".version");
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error("error getting version", ex);
            return null;
        }
    }
}
