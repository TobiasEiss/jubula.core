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
package org.eclipse.jubula.autagent.commands;

import java.io.IOException;
import java.util.Map;

import org.eclipse.jubula.communication.message.Message;
import org.eclipse.jubula.communication.message.StartAUTServerStateMessage;


/**
 * Implementors of this interface start an AUT.
 *
 * @author BREDEX GmbH
 * @created Jul 6, 2007
 * 
 */
public interface IStartAut {

    /** The locale of the AUT */
    public static final String LOCALE = "LOCALE"; //$NON-NLS-1$
    
    /** Default error message when the AUT cannot be started */
    public static final Message ERROR_MESSAGE = new StartAUTServerStateMessage(
        StartAUTServerStateMessage.ERROR, "Unexpected error, no detail available."); //$NON-NLS-1$
    
    /** <code>RC_DEBUG</code> */
    public static final String RC_DEBUG = System.getProperty("RC_DEBUG"); //$NON-NLS-1$

    /** <code>PATH_SEPARATOR</code> */
    public static final String PATH_SEPARATOR = System.getProperty("path.separator"); //$NON-NLS-1$
    
    /** <code>FILE_SEPARATOR</code> */
    public static final String FILE_SEPARATOR = System.getProperty("file.separator"); //$NON-NLS-1$
    
    /** Delimiter for key and value of properties (key=value) */
    public static final String PROPERTY_DELIMITER = "=";  //$NON-NLS-1$
    
    /** Whitespace delimiter */
    public static final String WHITESPACE_DELIMITER = " "; //$NON-NLS-1$
    
    /** The separator used when composing the Classpath in the AUT Configuration */
    public static final String CLIENT_PATH_SEPARATOR = ";"; //$NON-NLS-1$

    /**
     * Starts the AUT with the given parameters.
     * @param parameters The parameters for starting the AUT.
     * @return a <code>StartAutServerStateMessage</code> which either describes an error
     * condition or just tells the originator that the AUT was started correctly.
     * @throws IOException if an I/O error occurs.
     */
    public StartAUTServerStateMessage startAut(Map parameters) 
        throws IOException;
    
}
