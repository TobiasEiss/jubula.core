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
package org.eclipse.jubula.client.ui.rcp.handlers;

import java.util.Set;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jubula.client.core.model.IComponentNamePO;
import org.eclipse.jubula.client.ui.rcp.editors.IJBEditor;
import org.eclipse.jubula.client.ui.rcp.editors.JBEditorHelper.EditableState;
import org.eclipse.jubula.client.ui.utils.ErrorHandlingUtil;
import org.eclipse.jubula.tools.messagehandling.MessageIDs;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;


/**
 * @author BREDEX GmbH
 * @created 12.02.2009
 */
public class MergeLogicalNameHandler extends AbstractMergeComponentNameHandler {

    /**
     * {@inheritDoc}
     */
    public Object execute(ExecutionEvent event) {
        
        ISelection sel = HandlerUtil.getCurrentSelection(event);
        IWorkbenchPart activePart = HandlerUtil.getActivePart(event);
        if (sel instanceof IStructuredSelection
                && activePart instanceof IJBEditor) {
            IStructuredSelection structuredSel = (IStructuredSelection)sel;
            IJBEditor editor = 
                (IJBEditor)activePart;
            if (editor.isDirty()) {
                ErrorHandlingUtil.createMessageDialog(MessageIDs.I_SAVE_EDITOR);
                return null;
            }
   
            // Get model objects from selection
            Set<IComponentNamePO> compNames = getComponentNames(structuredSel);

            // Dialog
            IComponentNamePO selectedCompNamePo = openDialog(compNames);
            
            if (selectedCompNamePo == null) {
                // cancel operation
                return null;
            }

            if (editor.getEditorHelper().requestEditableState() 
                    != EditableState.OK) {

                return null;
            }

            performOperation(editor, compNames, selectedCompNamePo);
            
        }
        
        return null;
    }

    /**
     * Perform the actual merge operation.
     * 
     * @param editor The editor in which the merge takes place.
     * @param compNames The Component Names to merge.
     * @param selectedCompNamePo The Component Name that will be used in place
     *                           of the merged Component Names.
     */
    private void performOperation(IJBEditor editor,
            Set<IComponentNamePO> compNames,
            IComponentNamePO selectedCompNamePo) {

        performOperation(compNames, selectedCompNamePo);

        editor.getEditorHelper().setDirty(true);
        IProgressMonitor saveProgressMonitor = new NullProgressMonitor();
        editor.doSave(saveProgressMonitor);

        if (!saveProgressMonitor.isCanceled()) {
            fireChangeEvents(compNames);
        }
    }

}