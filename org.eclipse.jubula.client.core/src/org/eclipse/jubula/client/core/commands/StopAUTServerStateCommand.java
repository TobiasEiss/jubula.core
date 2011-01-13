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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jubula.communication.ICommand;
import org.eclipse.jubula.communication.message.Message;
import org.eclipse.jubula.communication.message.StopAUTServerStateMessage;


/**
 * @author BREDEX GmbH
 * @created 18.12.2007
 * 
 */
public class StopAUTServerStateCommand implements ICommand {
    /** the logger */
    private static Log log = LogFactory
        .getLog(StopAUTServerStateCommand.class);

    /** the message */
    private StopAUTServerStateMessage m_message;

    /** whether a timeout has occurred */
    private boolean m_isTimeout = false;

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
        m_message = (StopAUTServerStateMessage)message;
    }

    /**
     * {@inheritDoc}
     */
    public Message execute() {
        setMessage(new StopAUTServerStateMessage());
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void timeout() {
        m_isTimeout = true;
        log.error(this.getClass().getName() + ".timeout() called"); //$NON-NLS-1$
    }

    /**
     * @return <code>true</code> if this command has timed out. Otherwise 
     *         <code>false</code>.
     */
    public boolean isTimeout() {
        return m_isTimeout;
    }
}