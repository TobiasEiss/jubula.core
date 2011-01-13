/*******************************************************************************
 * Copyright (c) 2004, 2010 BREDEX GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BREDEX GmbH - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.jubula.client.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jubula.tools.exception.GDFatalException;
import org.eclipse.jubula.tools.messagehandling.MessageIDs;
import org.osgi.framework.Bundle;

/**
 * Utility class for bundle issues
 * 
 * @author BREDEX GmbH
 * @created Nov 29, 2010
 */
public class BundleUtils {
    /** standard logging */
    private static final Log LOG = LogFactory.getLog(BundleUtils.class);

    /**
     * Constructor
     */
    private BundleUtils() {
    // hide
    }

    /**
     * @param bundle
     *            the bundle to resolve the file URL from
     * @param resouceName
     *            the relative resource name e.g. resouces/plugin.properties
     * @return the URL for the resource
     */
    public static URL getFileURL(Bundle bundle, String resouceName) {
        try {
            URL url = bundle.getEntry(resouceName);
            if (url != null) {
                return FileLocator.toFileURL(url);
            }
            LOG.error("Resource: " + resouceName + " not found."); //$NON-NLS-1$//$NON-NLS-2$
        } catch (MalformedURLException e) {
            LOG.error(e);
        } catch (IOException e) {
            LOG.error(e);
        }
        return null;
    }
    
    /**
     * @param bundle the bundle to load the properties from
     * @param propertyFileName the name of the properties file
     * @return the loaded properties
     * @throws GDFatalException in case of error during properties loading
     */
    public static Properties loadProperties(Bundle bundle,
            String propertyFileName) throws GDFatalException {
        Properties prop = new Properties();
        InputStream propStream = null;
        try {
            propStream = getFileURL(bundle, propertyFileName).openStream();
            prop.load(propStream);
        } catch (IOException e) {
            String msg = "Can't load: " + propertyFileName; //$NON-NLS-1$
            LOG.fatal(msg, e);
            throw new GDFatalException(msg,
                    MessageIDs.E_PROPERTIES_FILE_NOT_FOUND);
        } finally {
            try {
                if (propStream != null) {
                    propStream.close();
                }
            } catch (IOException e) {
                // ignore
            }
        }
        return prop;
    }
}