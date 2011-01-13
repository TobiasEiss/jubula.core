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
package org.eclipse.jubula.client.ui.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jubula.client.core.model.IEventExecTestCasePO;
import org.eclipse.jubula.client.core.model.ISpecTestCasePO;
import org.eclipse.jubula.client.ui.Plugin;
import org.eclipse.jubula.client.ui.constants.ContextHelpIds;
import org.eclipse.jubula.client.ui.constants.IconConstants;
import org.eclipse.jubula.client.ui.dialogs.TestCaseTreeDialog;
import org.eclipse.jubula.client.ui.editors.AbstractTestCaseEditor;
import org.eclipse.jubula.client.ui.editors.GDEditorHelper;
import org.eclipse.jubula.client.ui.model.GuiNode;
import org.eclipse.jubula.client.ui.model.SpecTestCaseGUI;
import org.eclipse.jubula.client.ui.utils.DialogUtils;
import org.eclipse.jubula.client.ui.utils.Utils;
import org.eclipse.jubula.toolkit.common.xml.businessprocess.ComponentBuilder;
import org.eclipse.jubula.tools.constants.StringConstants;
import org.eclipse.jubula.tools.exception.Assert;
import org.eclipse.jubula.tools.i18n.I18n;
import org.eclipse.jubula.tools.messagehandling.MessageIDs;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;


/**
 * @author BREDEX GmbH
 * @created 05.04.2005
 */
public class AddExistingEventHandlerHandler extends AbstractHandler {
    /**
     * @author BREDEX GmbH
     */
    private static class SelectionTransfer {
        /**
         * the selection to transfer
         */
        private ISelection m_selection = null;

        /**
         * @param selection the selection
         */
        public void setSelection(ISelection selection) {
            this.m_selection = selection;
        }

        /**
         * @return the selection
         */
        public ISelection getSelection() {
            return m_selection;
        }
    }
    
    /** {@inheritDoc} */
    public Object execute(ExecutionEvent event) {
        IEditorPart editor = Plugin.getActiveEditor();
        Assert.verify(editor instanceof AbstractTestCaseEditor, "Wrong editor type!"); //$NON-NLS-1$
        AbstractTestCaseEditor testCaseEditor = 
            (AbstractTestCaseEditor) editor;
        if (GDEditorHelper.EditableState.OK == testCaseEditor.getEditorHelper()
                .requestEditableState()) {
            openTestCasePopUp(testCaseEditor);
        }
        return null;
    }
        
    /**
     * Opens the PopUp with the TestCaseTree.
     * @param editor The test case editor.
     */
    private void openTestCasePopUp(final AbstractTestCaseEditor editor) {  
        final SpecTestCaseGUI parentNode = (SpecTestCaseGUI)editor
            .getTreeViewer().getTree().getItem(0).getData(); 
        if (hasTestCaseAllEventHandler((ISpecTestCasePO)parentNode
            .getContent())) {
            
            return;
        }
        String title = I18n.getString("AddEventHandlerAction.AddEventHandler"); //$NON-NLS-1$
        TestCaseTreeDialog dialog = new TestCaseTreeDialog(Plugin
            .getShell(), title, StringConstants.EMPTY, 
            (ISpecTestCasePO)parentNode.getContent(), title, SWT.SINGLE, 
            IconConstants.ADD_EH_IMAGE, TestCaseTreeDialog.EVENTHANDLER); 
        final SelectionTransfer selTransferObj = new SelectionTransfer();
        ISelectionListener selListener = new ISelectionListener() {
            public void selectionChanged(IWorkbenchPart part,
                    ISelection selection) {
                selTransferObj.setSelection(selection);
            }
        };
        dialog.addSelectionListener(selListener);
        dialog.setHelpAvailable(true);
        dialog.create();
        DialogUtils.setWidgetNameForModalDialog(dialog);
        Plugin.getHelpSystem().setHelp(dialog.getShell(), 
            ContextHelpIds.EVENT_HANDLER_ADD);
        int returnCode = dialog.open();
        if (returnCode == TestCaseTreeDialog.ADD) {
            addEventHandler(selTransferObj.getSelection(), parentNode, editor);
        }
        dialog.removeSelectionListener(selListener);
    }
    
    /**
     * Checks, if the actual test case has eventhandler with all available event types.
     * @param parentNode The actual test case.
     * @return True, if the actual test case has eventhandler with all available event types, false otherwise.
     */
    private boolean hasTestCaseAllEventHandler(ISpecTestCasePO parentNode) {
        Collection eventTcList = parentNode.getAllEventEventExecTC();
        // get a List of used event types in this TestCase.
        List < String > existentEventTypes = new ArrayList < String > ();
        for (Object object : eventTcList) {
            IEventExecTestCasePO eventTc = (IEventExecTestCasePO)object;
            existentEventTypes.add(eventTc.getEventType());
        }
        
        Set mapKeySet = ComponentBuilder.getInstance().getCompSystem()
            .getEventTypes().keySet(); 
        String[] eventTypes = new String[mapKeySet.size()];
        int i = 0;
        for (Object object : mapKeySet) {
            eventTypes[i] = object.toString();
            i++;
        }
        List < String > availableEventTypes = Arrays.asList(eventTypes);
        if (availableEventTypes.size() == existentEventTypes.size()) {
            Utils.createMessageDialog(MessageIDs.E_ENOUGH_EVENT_HANDLER, null, 
                    new String[]{I18n.getString("AddEventHandlerDialog.enoughEventHandler", //$NON-NLS-1$
                            new Object[]{parentNode.getName()})});
            return true;
        }
        return false;
    }
     
    /**
     * Adds the given Selection as Eventhandler.
     * @param selection the ISelection to add as EventHandler
     * @param nodeGUI The selected nodeGUI.
     * @param editor the editor
     */
    void addEventHandler(ISelection selection, GuiNode nodeGUI, 
        AbstractTestCaseEditor editor) {
        if (!(selection instanceof IStructuredSelection)) {
            return;
        }
        SpecTestCaseGUI eventHandler = (SpecTestCaseGUI)
            ((IStructuredSelection)selection).getFirstElement();
        editor.addEventHandler(eventHandler, (SpecTestCaseGUI)nodeGUI);
    }
}