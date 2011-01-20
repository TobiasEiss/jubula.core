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
package org.eclipse.jubula.autagent.monitoring;

import org.eclipse.jubula.autagent.AutStarter;
import org.eclipse.jubula.communication.Communicator;
import org.eclipse.jubula.communication.ICommand;
import org.eclipse.jubula.communication.message.BuildMonitoringReportMessage;
import org.eclipse.jubula.communication.message.Message;
import org.eclipse.jubula.communication.message.SendMonitoringReportMessage;
import org.eclipse.jubula.tools.exception.CommunicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This command is calling the "buildMonitoringReport" method specified by the
 * IMonitoring interface.
 * @author BREDEX GmbH
 * @created 13.09.2010
 */
public class BuildMonitoringReportCommand implements ICommand {
    /** the logger */
    private static final Logger LOG = LoggerFactory
            .getLogger(BuildMonitoringReportCommand.class);  
   
    /** message */
    private BuildMonitoringReportMessage m_message;      
    /** 
     * {@inheritDoc}
     */
    public Message execute() {
        
        MonitoringDataStore cm = MonitoringDataStore.getInstance();        
        IMonitoring agent = cm.getMonitoringAgent(m_message.getAutId());    
        byte[] report = agent.buildMonitoringReport();
        Communicator m = AutStarter.getInstance().getCommunicator();
        SendMonitoringReportMessage message = new SendMonitoringReportMessage(
                report);
        try {
            m.send(message);
        } catch (CommunicationException e) {
            LOG.debug("Failed to send SendMonitoringReportMessage", e); //$NON-NLS-1$

        }       
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
        m_message = (BuildMonitoringReportMessage)message;

    }

    /**
     * {@inheritDoc}
     */
    public void timeout() {
        LOG.error(this.getClass().getName() + ".timeout() called"); //$NON-NLS-1$
    }

}
