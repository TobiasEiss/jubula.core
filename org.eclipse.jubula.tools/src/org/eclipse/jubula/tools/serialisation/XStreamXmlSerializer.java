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
package org.eclipse.jubula.tools.serialisation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.eclipse.jubula.tools.constants.StringConstants;
import org.eclipse.jubula.tools.exception.SerialisationException;
import org.eclipse.jubula.tools.jarutils.IVersion;
import org.eclipse.jubula.tools.messagehandling.MessageIDs;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.core.BaseException;
import com.thoughtworks.xstream.core.util.CompositeClassLoader;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * The XStream implementation of the XML serializer.
 * 
 * @author BREDEX GmbH
 * @created 28.07.2005
 */
public class XStreamXmlSerializer implements IXmlSerializer {
    /** static xml header */
    private static final String XML_HEADER =
        "<?xml version=\"1.0\" minor=\""  //$NON-NLS-1$
        + IVersion.JB_XML_IMPORT_MINOR_VERSION 
        + "\" major=\"" //$NON-NLS-1$
        + IVersion.JB_XML_IMPORT_MAJOR_VERSION
        + "\"?>"; //$NON-NLS-1$
    /** The XStream instance. This is the facade for all XStream operations. */
    private final XStream m_stream;
    /** A list of all class loaders which have been added to the XStream <code>CompositeClassLoader</code>. */
    private final List m_addedClassLoaders;

    /**
     * The default constructor.
     */
    public XStreamXmlSerializer() {
        m_stream = new XStream(new PureJavaReflectionProvider(), 
            new DomDriver());
        m_addedClassLoaders = new ArrayList();
    }

    /**
     * {@inheritDoc}
     *      java.lang.Class)
     */
    public Object deserialize(String text, Class clazz)
        throws SerialisationException {

        Validate.notNull(text);
        Validate.notNull(clazz);
        try {
            ClassLoader streamClassLoader = m_stream.getClassLoader();
            // If this is a CompositeClassLoader, the class loader that
            // has loaded the passed clazz instance, will be added to
            // the CompositeClassLoader. So we ensure that XStream
            // loads the class correctly inside an Eclipse Plugin.
            // If XStream doesn't return the CompositeClassLoader, there
            // is no way to give XStream any class loader support.
            if (streamClassLoader instanceof CompositeClassLoader) {
                synchronized (this) {
                    ClassLoader classLoader = clazz.getClassLoader();
                    if (!m_addedClassLoaders.contains(classLoader)) {
                        ((CompositeClassLoader)streamClassLoader)
                            .add(classLoader);
                        m_addedClassLoaders.add(classLoader);
                    }
                }
            }
            // split into Header and Body
            if (text.startsWith("<?")) { //$NON-NLS-1$
                String xmlHeader = text.substring(0, text.indexOf(">") + 1); //$NON-NLS-1$
                String xmlBody = StringUtils.substringAfter(text, ">"); //$NON-NLS-1$
                checkVersion(xmlHeader);
                return m_stream.fromXML(xmlBody);
            } 
            return m_stream.fromXML(text);
            
        } catch (BaseException e) {
            throw new SerialisationException(e.getMessage(), 
                MessageIDs.E_SERILIZATION_FAILED);
        }
    }

    /**
     * checks if xml file has supported Version
     * @param header XML Header
     */
    private void checkVersion(String header) {
        if (header.indexOf("minor") == -1 //$NON-NLS-1$
            || header.indexOf("major") == -1) { //$NON-NLS-1$
            return;
        }
        String minor = header.substring(header.indexOf("minor"), //$NON-NLS-1$
            header.indexOf("major")); //$NON-NLS-1$ 
        String major = header.substring(header.indexOf("major")); //$NON-NLS-1$
        StringUtils.substringBetween(minor, StringConstants.QUOTE);
        StringUtils.substringBetween(major, StringConstants.QUOTE);
    }
    
    /**
     * {@inheritDoc}
     */
    public String serialize(Object object, boolean writeXmlHeader)
        throws SerialisationException {

        Validate.notNull(object);
        try {
            StringBuffer buffer = new StringBuffer();
            if (writeXmlHeader) {
                buffer.append(XML_HEADER);
            }
            buffer.append(m_stream.toXML(object));
            return buffer.toString();
        } catch (BaseException e) {
            throw new SerialisationException(e.getMessage(), 
                MessageIDs.E_SERILIZATION_FAILED);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Object getImplementation() {
        return m_stream;
    }
}