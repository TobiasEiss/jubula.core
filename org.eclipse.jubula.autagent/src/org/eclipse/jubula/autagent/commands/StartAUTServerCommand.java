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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jubula.autagent.AutStarter;
import org.eclipse.jubula.communication.ICommand;
import org.eclipse.jubula.communication.message.Message;
import org.eclipse.jubula.communication.message.StartAUTServerMessage;
import org.eclipse.jubula.communication.message.StartAUTServerStateMessage;
import org.eclipse.jubula.tools.constants.AutConfigConstants;

/**
 * The class <code>StartAutServerCommand</code> and the associated
 * <code>StartAutServerMessage</code> are used as examples for the intended use
 * of the communications layer in the application. Since changes are expected,
 * this documentation is inlined in the source code. Please reevaluate the Java
 * doc frequently for changes in this templates.
 * 
 * The command object for starting the AUTServer. The method execute() returns a
 * StartAUTServerStateMessage which contains a state. In case of not OK, the
 * message always contains a short description.
 * 
 * @author BREDEX GmbH
 * @created 04.08.2004
 * 
 */
public class StartAUTServerCommand implements ICommand {
    
    /** the logger */
    private static Log log = LogFactory.getLog(StartAUTServerCommand.class);

    /** the data */
    private StartAUTServerMessage m_message;
    
    /**
     * empty default constructor
     */
    public StartAUTServerCommand() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public Message getMessage() {
        return m_message;
    }

    /**
     * {@inheritDoc}
     */
    public void setMessage(Message message) {
        try {
            m_message = (StartAUTServerMessage)message;
        } catch (ClassCastException cce) {
            if (log.isErrorEnabled()) {
                log.error("Cannot convert from " //$NON-NLS-1$
                        + message.getClass().toString() + " to " //$NON-NLS-1$
                        + m_message.getClass().toString(), cce);
            }
            throw cce;
        }
    }
    
    /**
     * The method builds the parameters necessary to start the AUT. It then starts the
     * AUT server using <code>Runtime.exec()</code> and begins monitoring this process.
     * 
     * There are several possibilities for this methods to fail:
     * 
     * <li> there is already an AUT running
     * <li> there are problems finding the main method to call as the AUT
     * <li> monitoring of the started AUT fails
     * <li> internal errors from the execution environment
     * 
     * @return a <code>StartAutServerStateMessage</code> which either describes an error
     * condition or just tells the originator that the AUT was started correctly.
     */
    public Message execute() {
        log.debug("execute() called"); //$NON-NLS-1$
        try {
            // Prepare additional parameters
            // For example, add locale to map
            m_message.getAutConfiguration().put(
                IStartAut.LOCALE, m_message.getLocale());
            m_message.getAutConfiguration()
                .put(AutConfigConstants.NAME_TECHNICAL_COMPONENTS, 
                        Boolean.valueOf(m_message.isGenerateNames()));
            
            AutStarter.getInstance().getAgent().setStartAutMessage(m_message);
            
            // Load class
            // FIXME RCP
            String autToolkit = m_message.getAutToolKit();
            String toolkitName = autToolkit.substring(
                autToolkit.lastIndexOf('.') + 1, 
                autToolkit.lastIndexOf("ToolkitPlugin")); //$NON-NLS-1$
            String className = "org.eclipse.jubula.autagent.commands.Start" //$NON-NLS-1$
                + toolkitName + "AutServerCommand"; //$NON-NLS-1$
            Class autServerClass = Class.forName(className);
            IStartAut autStarter = (IStartAut)autServerClass.newInstance();
            return autStarter.startAut(m_message.getAutConfiguration());
            
        } catch (IllegalArgumentException iae) {
            log.error(iae);
            return new StartAUTServerStateMessage(
                StartAUTServerStateMessage.EXECUTION,
                iae.getMessage());
        } catch (NullPointerException npe) {
            log.error(npe);
            return new StartAUTServerStateMessage(
                StartAUTServerStateMessage.DATA,
                npe.getMessage());
        } catch (SecurityException se) {
            log.error("security violation", se); //$NON-NLS-1$
            return new StartAUTServerStateMessage(
                StartAUTServerStateMessage.SECURITY,
                "security violation:" + se.getMessage()); //$NON-NLS-1$
        } catch (IOException ioe) {
            log.error("could not start AUTServer", ioe); //$NON-NLS-1$
            return new StartAUTServerStateMessage(
                StartAUTServerStateMessage.IO,
                ioe.getMessage());
        } catch (ClassNotFoundException cnfe) {
            log.error("could not find class for AUTServer", cnfe); //$NON-NLS-1$
            return new StartAUTServerStateMessage(
                StartAUTServerStateMessage.NO_SERVER_CLASS,
                cnfe.getMessage());
        } catch (InstantiationException ie) {
            log.error("could not instantiate class for AUTServer", ie); //$NON-NLS-1$
            return new StartAUTServerStateMessage(
                StartAUTServerStateMessage.NO_SERVER_CLASS,
                ie.getMessage());
        } catch (IllegalAccessException iae) {
            log.error("could not instantiate class for AUTServer", iae); //$NON-NLS-1$
            return new StartAUTServerStateMessage(
                StartAUTServerStateMessage.NO_SERVER_CLASS,
                iae.getMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    public void timeout() {
        log.error(this.getClass().getName() + "timeout() called when it shouldn't (no response)"); //$NON-NLS-1$
    }
}