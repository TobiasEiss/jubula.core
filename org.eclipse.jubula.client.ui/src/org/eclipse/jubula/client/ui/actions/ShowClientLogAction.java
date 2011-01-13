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
package org.eclipse.jubula.client.ui.actions;

import java.io.File;

import org.eclipse.jface.action.IAction;
import org.eclipse.jubula.client.ui.businessprocess.AbstractActionBP;
import org.eclipse.jubula.client.ui.businessprocess.ShowClientLogBP;
import org.eclipse.jubula.client.ui.editors.ClientLogInput;
import org.eclipse.jubula.client.ui.utils.Utils;
import org.eclipse.jubula.tools.messagehandling.MessageIDs;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.Workbench;


/**
 * @author BREDEX GmbH
 * @created Feb 7, 2007
 */
public class ShowClientLogAction extends AbstractAction {

    /** single instance of the ClientLogInput */
    private IEditorInput m_clientLogInput = null;

    /**
     * {@inheritDoc}
     */
    public void runWithEvent(IAction action, Event event) {
        if (action != null && !action.isEnabled()) {
            return;
        }
        
        File clientLogFile = ShowClientLogBP.getInstance().getClientLogFile();
        
        if (clientLogFile != null) {
            IWorkbenchPage currentPage = 
                Workbench.getInstance().getActiveWorkbenchWindow()
                .getActivePage();
            
            if (currentPage != null) {
                if (m_clientLogInput != null 
                    && currentPage.findEditor(m_clientLogInput) != null) {
                    currentPage.closeEditor(
                        currentPage.findEditor(m_clientLogInput), false);
                }
                
                m_clientLogInput = new ClientLogInput(clientLogFile);

                try {
                    currentPage.openEditor(m_clientLogInput, 
                        "org.eclipse.jubula.client.ui.editors.LogViewer"); //$NON-NLS-1$
                } catch (PartInitException e) {
                    Utils.createMessageDialog(MessageIDs.E_CANNOT_OPEN_EDITOR);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    protected AbstractActionBP getActionBP() {
        return ShowClientLogBP.getInstance();
    }

}