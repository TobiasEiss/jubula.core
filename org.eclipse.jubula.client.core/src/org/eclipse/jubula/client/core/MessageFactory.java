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
package org.eclipse.jubula.client.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jubula.client.core.businessprocess.TestExecution;
import org.eclipse.jubula.client.core.communication.AUTConnection;
import org.eclipse.jubula.client.core.communication.ConnectionException;
import org.eclipse.jubula.client.core.model.IAUTMainPO;
import org.eclipse.jubula.communication.message.ActivateApplicationMessage;
import org.eclipse.jubula.communication.message.CAPTestMessage;
import org.eclipse.jubula.communication.message.MessageCap;
import org.eclipse.jubula.communication.message.SendAUTListOfSupportedComponentsMessage;
import org.eclipse.jubula.tools.constants.CommandConstants;
import org.eclipse.jubula.tools.constants.StringConstants;
import org.eclipse.jubula.tools.messagehandling.MessageIDs;


/**
 * @author BREDEX GmbH
 * @created 09.05.2006
 */
public class MessageFactory {

    /** The logger */
    private static final Log LOG = LogFactory.getLog(MessageFactory.class);

    /** 
     * mapping from toolkit name (short form) to corresponding Activate AUT 
     * Message class name (FQN) 
     */
    private static Map<String, String> toolkitToActivationMessageClassName = 
        new HashMap<String, String>();
    
    static {
        toolkitToActivationMessageClassName.put(CommandConstants.SWT_TOOLKIT, 
            "org.eclipse.jubula.communication.message.swt.ActivateSwtApplicationMessage"); //$NON-NLS-1$
        toolkitToActivationMessageClassName.put(CommandConstants.RCP_TOOLKIT, 
            toolkitToActivationMessageClassName.get(
                    CommandConstants.SWT_TOOLKIT));
        toolkitToActivationMessageClassName.put(CommandConstants.SWING_TOOLKIT, 
            "org.eclipse.jubula.communication.message.swing.ActivateSwingApplicationMessage"); //$NON-NLS-1$
    }
    
    /** 
     * mapping from toolkit name (short form) to corresponding CAP Test  
     * Message class name (FQN) 
     */
    private static Map<String, String> toolkitToTestMessageClassName =
        new HashMap<String, String>();

    static {
        toolkitToTestMessageClassName.put(CommandConstants.SWT_TOOLKIT, 
            "org.eclipse.jubula.communication.message.swt.CAPSwtTestMessage"); //$NON-NLS-1$
        toolkitToTestMessageClassName.put(CommandConstants.RCP_TOOLKIT, 
            toolkitToTestMessageClassName.get(CommandConstants.SWT_TOOLKIT));
        toolkitToTestMessageClassName.put(CommandConstants.SWING_TOOLKIT, 
            "org.eclipse.jubula.communication.message.swing.CAPSwingTestMessage"); //$NON-NLS-1$
        toolkitToTestMessageClassName.put(CommandConstants.HTML_TOOLKIT, 
            "org.eclipse.jubula.communication.message.html.CAPHtmlTestMessage"); //$NON-NLS-1$
    }
    
    /**
     * default utility constructor.
     */
    private MessageFactory() {
        // do nothing
    }

    /**
     * @throws UnknownMessageException the exception thrown if the instantiation of message failed.
     * @return the created Message
     */
    public static ActivateApplicationMessage getActivateApplicationMessage() 
        throws UnknownMessageException {
        
        final String autToolKit = getAutToolkit();
        String messageClassName = StringConstants.EMPTY;
        try {
            messageClassName = 
                toolkitToActivationMessageClassName.get(autToolKit);
            if (messageClassName != null) {
                Class messageClass = Class.forName(messageClassName, false, 
                        ActivateApplicationMessage.class.getClassLoader());
                if (!ActivateApplicationMessage.class.isAssignableFrom(
                        messageClass)) {
                    
                    throw new UnknownMessageException(messageClass.getName()
                            + "is not assignable to " //$NON-NLS-1$
                            + ActivateApplicationMessage.class.getName(),
                            MessageIDs.E_MESSAGE_NOT_ASSIGNABLE);
                }
                
                // create a sharedInstance and set the message
                ActivateApplicationMessage result = 
                    (ActivateApplicationMessage)messageClass.newInstance();
                return result;
            }
            
            throw new UnknownMessageException(
                    "creating an Message sharedInstance for " //$NON-NLS-1$ 
                            + messageClassName + "failed: " + //$NON-NLS-1$);
                            "No AUT Activation Message class found for toolkit " + autToolKit,  //$NON-NLS-1$
                            MessageIDs.E_MESSAGE_NOT_CREATED);
            
        } catch (ExceptionInInitializerError eiie) {
            LOG.error(eiie);
            throw new UnknownMessageException(
                    "creating an Message sharedInstance for " //$NON-NLS-1$ 
                            + messageClassName + "failed: " + //$NON-NLS-1$);
                            eiie.getMessage(), 
                            MessageIDs.E_MESSAGE_NOT_CREATED);
        } catch (LinkageError le) {
            LOG.error(le);
            throw new UnknownMessageException(
                    "creating an Message sharedInstance for " //$NON-NLS-1$ 
                            + messageClassName + "failed: " + //$NON-NLS-1$);
                            le.getMessage(), MessageIDs.E_MESSAGE_NOT_CREATED);
        } catch (ClassNotFoundException cnfe) {
            LOG.error(cnfe);
            throw new UnknownMessageException(
                    "creating an Message sharedInstance for " //$NON-NLS-1$ 
                            + messageClassName + "failed: " + //$NON-NLS-1$);
                            cnfe.getMessage(), 
                            MessageIDs.E_MESSAGE_NOT_CREATED);
        } catch (InstantiationException ie) {
            LOG.error(ie);
            throw new UnknownMessageException(
                    "creating an Message sharedInstance for " //$NON-NLS-1$ 
                            + messageClassName + "failed: " + //$NON-NLS-1$);
                            ie.getMessage(), MessageIDs.E_MESSAGE_NOT_CREATED);
        } catch (IllegalAccessException iae) {
            LOG.error(iae);
            throw new UnknownMessageException(
                    "creating an Message sharedInstance for " //$NON-NLS-1$ 
                            + messageClassName + "failed: " + //$NON-NLS-1$);
                            iae.getMessage(), MessageIDs.E_MESSAGE_NOT_CREATED);
        }
    }

    /**
     * @return the toolkit of the AUT
     */
    private static String getAutToolkit() {
        String autToolKit = StringConstants.EMPTY;
        final IAUTMainPO connectedAut = 
            TestExecution.getInstance().getConnectedAut();
        if (connectedAut != null) {
            autToolKit = connectedAut.getToolkit();            
        }
        return autToolKit;
    }
    
    /**
     * @param messageCap the messageCap to set.
     * @throws UnknownMessageException the exception thrown if the instantiation of message failed.
     * @return the created Message
     */
    public static CAPTestMessage getCAPTestMessage(MessageCap messageCap) 
        throws UnknownMessageException {
        final String autToolKit = getAutToolkit();
        try {
            if (StringConstants.EMPTY.equals(autToolKit) 
                    && !AUTConnection.getInstance().isConnected()) {
                throw new UnknownMessageException(
                        "creating a Message sharedInstance failed: No connection to AUT.", //$NON-NLS-1$);
                        MessageIDs.E_MESSAGE_NOT_CREATED);
            }
        } catch (ConnectionException e) {
            throw new UnknownMessageException(
                    "creating a Message sharedInstance failed: No connection to AUT.", //$NON-NLS-1$);
                    MessageIDs.E_MESSAGE_NOT_CREATED);
        }
        String messageClassName = "null"; //$NON-NLS-1$
        try {
            messageClassName = toolkitToTestMessageClassName.get(autToolKit);
            if (messageClassName != null) {
                Class messageClass = Class.forName(messageClassName, false, 
                        CAPTestMessage.class.getClassLoader());
                if (!CAPTestMessage.class.isAssignableFrom(
                        messageClass)) {
                    
                    throw new UnknownMessageException(messageClass.getName()
                            + "is not assignable to " //$NON-NLS-1$
                            + CAPTestMessage.class.getName(),
                            MessageIDs.E_MESSAGE_NOT_ASSIGNABLE);
                }
                
                // create a sharedInstance and set the message
                CAPTestMessage result = 
                    (CAPTestMessage)messageClass.newInstance();
                result.setMessageCap(messageCap);
                return result;
            }
            throw new UnknownMessageException(
                    "creating an Message sharedInstance for " //$NON-NLS-1$ 
                            + messageClassName + "failed: " + //$NON-NLS-1$);
                            "No Test Message class found for toolkit " + autToolKit,  //$NON-NLS-1$
                            MessageIDs.E_MESSAGE_NOT_CREATED);

        } catch (ExceptionInInitializerError eiie) {
            LOG.error(eiie);
            throw new UnknownMessageException(
                    "creating a Message sharedInstance for " //$NON-NLS-1$ 
                            + messageClassName + "failed: " + //$NON-NLS-1$);
                            eiie.getMessage(), 
                            MessageIDs.E_MESSAGE_NOT_CREATED);
        } catch (LinkageError le) {
            LOG.error(le);
            throw new UnknownMessageException(
                    "creating a Message sharedInstance for " //$NON-NLS-1$ 
                            + messageClassName + "failed: " + //$NON-NLS-1$);
                            le.getMessage(), MessageIDs.E_MESSAGE_NOT_CREATED);
        } catch (ClassNotFoundException cnfe) {
            LOG.error(cnfe);
            throw new UnknownMessageException(
                    "creating a Message sharedInstance for " //$NON-NLS-1$ 
                            + messageClassName + "failed: " + //$NON-NLS-1$);
                            cnfe.getMessage(), 
                            MessageIDs.E_MESSAGE_NOT_CREATED);
        } catch (InstantiationException ie) {
            LOG.error(ie);
            throw new UnknownMessageException(
                    "creating a Message sharedInstance for " //$NON-NLS-1$ 
                            + messageClassName + "failed: " + //$NON-NLS-1$);
                            ie.getMessage(), MessageIDs.E_MESSAGE_NOT_CREATED);
        } catch (IllegalAccessException iae) {
            LOG.error(iae);
            throw new UnknownMessageException(
                    "creating a Message sharedInstance for " //$NON-NLS-1$ 
                            + messageClassName + "failed: " + //$NON-NLS-1$);
                            iae.getMessage(), MessageIDs.E_MESSAGE_NOT_CREATED);
        }
    }
    
    /**
     * @throws UnknownMessageException the exception thrown if the instantiation of message failed.
     * @return the created Message
     */
    public static SendAUTListOfSupportedComponentsMessage 
    getSendAUTListOfSupportedComponentsMessage() 
        throws UnknownMessageException {
        
        final String autToolKit = getAutToolkit();
        String messageClassName = "null"; //$NON-NLS-1$
        try {
            if (CommandConstants.SWT_TOOLKIT.equals(autToolKit)
                || CommandConstants.RCP_TOOLKIT.equals(autToolKit)) {
                
                messageClassName = "org.eclipse.jubula.communication.message.swt.SendSwtAUTListOfSupportedComponentsMessage"; //$NON-NLS-1$                
            } else if (CommandConstants.SWING_TOOLKIT.equals(autToolKit)) {
                messageClassName = "org.eclipse.jubula.communication.message.swing.SendSwingAUTListOfSupportedComponentsMessage"; //$NON-NLS-1$                
            } else if (CommandConstants.WEB_TOOLKIT.equals(autToolKit)) {
                messageClassName = "org.eclipse.jubula.communication.message.web.SendWebAUTListOfSupportedComponentsMessage"; //$NON-NLS-1$
            } else if (CommandConstants.HTML_TOOLKIT.equals(autToolKit)) {
                messageClassName = "org.eclipse.jubula.communication.message.html.SendHtmlAUTListOfSupportedComponentsMessage"; //$NON-NLS-1$
            }
            Class messageClass = Class.forName(messageClassName, false, 
                    SendAUTListOfSupportedComponentsMessage.class
                    .getClassLoader());
            if (!SendAUTListOfSupportedComponentsMessage.class.isAssignableFrom(
                    messageClass)) {
                
                throw new UnknownMessageException(messageClass.getName()
                        + "is not assignable to " //$NON-NLS-1$
                        + SendAUTListOfSupportedComponentsMessage.class
                        .getName(), MessageIDs.E_MESSAGE_NOT_ASSIGNABLE);
            }

            // create a sharedInstance and set the message
            SendAUTListOfSupportedComponentsMessage result = 
                (SendAUTListOfSupportedComponentsMessage)messageClass
                    .newInstance();
            return result;
        } catch (ExceptionInInitializerError eiie) {
            LOG.error(eiie);
            throw new UnknownMessageException(
                    "creating an Message sharedInstance for " //$NON-NLS-1$ 
                            + String.valueOf(messageClassName) + "failed:" + //$NON-NLS-1$);
                            eiie.getMessage(), 
                            MessageIDs.E_MESSAGE_NOT_CREATED);
        } catch (LinkageError le) {
            LOG.error(le);
            throw new UnknownMessageException(
                    "creating an Message sharedInstance for " //$NON-NLS-1$ 
                            + String.valueOf(messageClassName) + "failed:" + //$NON-NLS-1$);
                            le.getMessage(), MessageIDs.E_MESSAGE_NOT_CREATED);
        } catch (ClassNotFoundException cnfe) {
            LOG.error(cnfe);
            throw new UnknownMessageException(
                    "creating an Message sharedInstance for " //$NON-NLS-1$ 
                            + String.valueOf(messageClassName) + "failed:" + //$NON-NLS-1$);
                            cnfe.getMessage(), 
                            MessageIDs.E_MESSAGE_NOT_CREATED);
        } catch (InstantiationException ie) {
            LOG.error(ie);
            throw new UnknownMessageException(
                    "creating an Message sharedInstance for " //$NON-NLS-1$ 
                            + String.valueOf(messageClassName) + "failed:" + //$NON-NLS-1$);
                            ie.getMessage(), MessageIDs.E_MESSAGE_NOT_CREATED);
        } catch (IllegalAccessException iae) {
            LOG.error(iae);
            throw new UnknownMessageException(
                    "creating an Message sharedInstance for " //$NON-NLS-1$ 
                            + String.valueOf(messageClassName) + "failed:" + //$NON-NLS-1$);
                            iae.getMessage(), MessageIDs.E_MESSAGE_NOT_CREATED);
        }
    }
}