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
package org.eclipse.jubula.client.core.commands;

import org.eclipse.jubula.client.core.ClientTestFactory;
import org.eclipse.jubula.communication.ICommand;
import org.eclipse.jubula.communication.message.GetAutConfigMapResponseMessage;
import org.eclipse.jubula.communication.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This command is handling the incoming autConfigMap from the Agent.
 * @author BREDEX GmbH
 * @created 05.08.2010
 * */
public class GetAutConfigMapResponseCommand implements ICommand {

    /** the logger */
    private static final Logger LOG = LoggerFactory
            .getLogger(GetAutConfigMapResponseCommand.class);    
    
    /** the message*/
    private GetAutConfigMapResponseMessage m_message;
        
    /**
     * {@inheritDoc}
     */
    public Message execute() {
       
        ClientTestFactory.getClientTest().
                    setLastConnectedAutConfigMap(m_message.getAutConfigMap());
        
        return null;
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
        m_message = (GetAutConfigMapResponseMessage)message;

    }
    /**
     * {@inheritDoc}
     */
    public void timeout() {
                
        LOG.error(this.getClass().getName() + ".timeout() called"); //$NON-NLS-1$

    }
}