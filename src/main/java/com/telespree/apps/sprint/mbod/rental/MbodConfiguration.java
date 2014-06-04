package com.telespree.apps.sprint.mbod.rental;

import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import telespree.apps.fwk.DefaultApplicationConfiguration;
import telespree.common.configuration.DBConfigLocator;
import telespree.common.configuration.InvalidConfigurationException;

/**
 * MbodConfiguration
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2012/03/21 16:12:57 $
 * @version $Revision: #1 $
 * 
 */
public class MbodConfiguration extends DefaultApplicationConfiguration
        implements MbodConstants {

    private static final Logger logger = LoggerFactory
            .getLogger(MbodConfiguration.class);

    private static Hashtable<String, MbodConfiguration> instances;

    /**
     * 
     * @param applicationId
     */
    private MbodConfiguration(long applicationId) {
        super(new DBConfigLocator(), applicationId);
    }

    /**
     * 
     * @param appId
     * @return
     */
    public static synchronized MbodConfiguration getInstance(long appId) {
        if (instances == null) {
            instances = new Hashtable<String, MbodConfiguration>();
        }
        MbodConfiguration config = (MbodConfiguration) instances.get(Long
                .toString(appId));
        if (config == null) {
            config = new MbodConfiguration(appId);
            instances.put(Long.toString(appId), config);
        }
        return config;
    }

    /*
     * (non-Javadoc)
     * 
     * @see telespree.common.configuration.PropertyBaseConfig#getFileName()
     */
    public String getFileName() {
        return "app.properties";
    }

    /*
     * (non-Javadoc)
     * 
     * @see telespree.apps.fwk.DefaultApplicationConfiguration#doValidate()
     */
    public void doValidate() throws InvalidConfigurationException {
        logger.debug("validating MbodConfiguration...");
    }

    /**
     * 
     */
    public static synchronized void clearInstances() {
        if (instances != null) {
            instances.clear();
            instances = null;
        }
    }

}
