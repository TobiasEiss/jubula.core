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
import org.eclipse.jubula.client.core.i18n.Messages;
import org.eclipse.jubula.communication.ICommand;
import org.eclipse.jubula.communication.message.Message;
import org.eclipse.jubula.communication.message.SendDirectoryResponseMessage;
import org.eclipse.jubula.tools.constants.StringConstants;


/**
 * @author BREDEX GmbH
 * @created May 19, 2009
 */
public class ProcessDirCommand implements ICommand {

    /** the logger */
    private static Log log = LogFactory.getLog(ProcessDirCommand.class);

    /** the message */
    private SendDirectoryResponseMessage m_message;

    /**
     * {@inheritDoc}
     */
    public Message execute() {
        log.debug(Messages.ExecutingDirectoryListCommand 
            + StringConstants.RIGHT_PARENTHESES + Messages.Response
            + StringConstants.LEFT_PARENTHESES);
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void timeout() {
        
        
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
        m_message = (SendDirectoryResponseMessage)message;
    }


}
