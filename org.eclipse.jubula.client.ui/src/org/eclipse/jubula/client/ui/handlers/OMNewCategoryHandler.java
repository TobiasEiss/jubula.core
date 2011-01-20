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


import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jubula.client.core.events.DataEventDispatcher;
import org.eclipse.jubula.client.core.events.DataEventDispatcher.DataState;
import org.eclipse.jubula.client.core.events.DataEventDispatcher.UpdateState;
import org.eclipse.jubula.client.core.model.IComponentNamePO;
import org.eclipse.jubula.client.core.model.IObjectMappingAssoziationPO;
import org.eclipse.jubula.client.core.model.IObjectMappingCategoryPO;
import org.eclipse.jubula.client.core.model.PoMaker;
import org.eclipse.jubula.client.ui.Plugin;
import org.eclipse.jubula.client.ui.constants.ContextHelpIds;
import org.eclipse.jubula.client.ui.constants.IconConstants;
import org.eclipse.jubula.client.ui.dialogs.InputDialog;
import org.eclipse.jubula.client.ui.editors.ObjectMappingMultiPageEditor;
import org.eclipse.jubula.client.ui.editors.JBEditorHelper.EditableState;
import org.eclipse.jubula.client.ui.utils.DialogUtils;
import org.eclipse.jubula.tools.i18n.I18n;
import org.eclipse.ui.handlers.HandlerUtil;


/**
 * @author BREDEX GmbH
 * @created 27.04.2005
 */
public class OMNewCategoryHandler extends AbstractHandler {
    /**
     * {@inheritDoc}
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {
        final ObjectMappingMultiPageEditor editor = 
            ((ObjectMappingMultiPageEditor)
                    HandlerUtil.getActivePartChecked(event));
        if (!(HandlerUtil.getCurrentSelection(event) 
                instanceof IStructuredSelection)) {
            return null;
        }
        IStructuredSelection selection = 
            (IStructuredSelection)HandlerUtil.getCurrentSelection(event);
        if (selection.size() == 1) { 
            ISelectionProvider selectionProvider = 
                HandlerUtil.getActiveSiteChecked(event)
                    .getSelectionProvider();
            createNewCategory(selection.getFirstElement(), editor, 
                    selectionProvider);
        } else {
            throw new ExecutionException("No valid selection count"); //$NON-NLS-1$
        }
        return null;
    }

    /**
     * 
     * @param selElement The selected element.
     * @param editor The active editor.
     * @param selectionProvider The active selection provider. This will be used
     *                          to set the new selection.
     */
    private void createNewCategory(Object selElement, 
            final ObjectMappingMultiPageEditor editor, 
            ISelectionProvider selectionProvider) {
        
        IObjectMappingCategoryPO category = null;
        if (selElement instanceof IObjectMappingCategoryPO) {
            category = (IObjectMappingCategoryPO)selElement;
        } else if (selElement instanceof IObjectMappingAssoziationPO) {
            category = 
                ((IObjectMappingAssoziationPO)selElement).getCategory();
        } else if (selElement instanceof IComponentNamePO) {
            category = editor.getOmEditorBP().getCategory(
                    (IComponentNamePO)selElement);
        }
        final IObjectMappingCategoryPO node = category;
        InputDialog dialog = 
            new InputDialog(Plugin.getShell(), 
                I18n.getString("OMNewCategoryAction.Title"), //$NON-NLS-1$
                I18n.getString("OMNewCategoryAction.Name"), //$NON-NLS-1$
                I18n.getString("OMNewCategoryAction.Message"),  //$NON-NLS-1$
                I18n.getString("OMNewCategoryAction.Label"), //$NON-NLS-1$
                I18n.getString("OMNewCategoryAction.Error1"), //$NON-NLS-1$
                I18n.getString("OMNewCategoryAction.doubleCatName"), //$NON-NLS-1$
                IconConstants.NEW_CAT_DIALOG_STRING,
                I18n.getString("OMNewCategoryAction.Shell"), //$NON-NLS-1$
                false) {

                /**
                 * @return False, if the input name already exists.
                 */
                protected boolean isInputAllowed() {
                    return !editor.getOmEditorBP().existCategory(
                            node, getInputFieldText());
                }
            };
        if (node != null) {
            dialog.setHelpAvailable(true);
            dialog.create();
            DialogUtils.setWidgetNameForModalDialog(dialog);
            Plugin.getHelpSystem().setHelp(dialog.getShell(), 
                ContextHelpIds.DIALOG_OM_CAT_NEW);
            dialog.open();
            if (dialog.getReturnCode() == Window.OK) {
                if (editor.getEditorHelper().requestEditableState() 
                        != EditableState.OK) {
                    return;
                }

                IObjectMappingCategoryPO newCategory = 
                    PoMaker.createObjectMappingCategoryPO(dialog.getName());
                node.addCategory(newCategory);
                editor.getEditorHelper().setDirty(true);

                DataEventDispatcher.getInstance().fireDataChangedListener(
                        node, DataState.StructureModified, 
                        UpdateState.onlyInEditor);
                DataEventDispatcher.getInstance().fireDataChangedListener(
                        newCategory, DataState.Added, 
                        UpdateState.onlyInEditor);
                StructuredSelection newSel = 
                    new StructuredSelection(newCategory);
                if (selectionProvider != null) {
                    selectionProvider.setSelection(newSel);
                }
            }
        }
    }
}