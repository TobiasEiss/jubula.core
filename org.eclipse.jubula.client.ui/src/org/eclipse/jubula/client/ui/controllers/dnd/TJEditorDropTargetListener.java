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
package org.eclipse.jubula.client.ui.controllers.dnd;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.jubula.client.ui.Plugin;
import org.eclipse.jubula.client.ui.constants.Constants;
import org.eclipse.jubula.client.ui.editors.TestJobEditor;
import org.eclipse.jubula.client.ui.model.GuiNode;
import org.eclipse.swt.dnd.TransferData;


/**
 * @author BREDEX GmbH
 * @created Mar 17, 2010
 */
public class TJEditorDropTargetListener extends GuiNodeViewerDropAdapter {
    /** <code>m_editor</code> */
    private TestJobEditor m_editor;
    
    /**
     * @param editor the TestCaseEditor.
     */
    public TJEditorDropTargetListener(TestJobEditor editor) {
        super(editor.getTreeViewer());
        m_editor = editor;
        boolean scrollExpand = Plugin.getDefault().getPreferenceStore().
            getBoolean(Constants.TREEAUTOSCROLL_KEY);
        setScrollExpandEnabled(scrollExpand);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean performDrop(Object data) {
        LocalSelectionTransfer transfer = LocalSelectionTransfer.getInstance();
        IStructuredSelection selection = transfer.getSelection();
        Object target = getCurrentTarget();
        int location = getCurrentLocation();
        if (target == null) {
            target = getFallbackTarget(getViewer());
            location = ViewerDropAdapter.LOCATION_AFTER;
        }
        if (selection instanceof StructuredSelection 
                && target instanceof GuiNode) {
            return TJEditorDndSupport.performDrop(m_editor, 
                    selection, (GuiNode)target, 
                    location);
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean validateDrop(Object target, int operation,
            TransferData transferType) {
        LocalSelectionTransfer transfer = LocalSelectionTransfer.getInstance();
        IStructuredSelection selection = transfer.getSelection();
        if (selection instanceof StructuredSelection) {
            Object targetNode = target;
            if (targetNode == null) {
                targetNode = getFallbackTarget(getViewer());
            }
            if (targetNode instanceof GuiNode) {
                return TJEditorDndSupport.validateDrop(transfer.getSource(),
                        getViewer(), selection, (GuiNode)targetNode, true);
            }
        }

        return false;
    }
}