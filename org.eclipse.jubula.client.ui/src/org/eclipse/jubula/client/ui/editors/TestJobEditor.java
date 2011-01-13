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
package org.eclipse.jubula.client.ui.editors;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jubula.client.core.businessprocess.db.TimestampBP;
import org.eclipse.jubula.client.core.events.DataEventDispatcher;
import org.eclipse.jubula.client.core.events.DataEventDispatcher.DataState;
import org.eclipse.jubula.client.core.events.DataEventDispatcher.UpdateState;
import org.eclipse.jubula.client.core.model.INodePO;
import org.eclipse.jubula.client.core.model.IPersistentObject;
import org.eclipse.jubula.client.core.model.IProjectPO;
import org.eclipse.jubula.client.core.model.IRefTestSuitePO;
import org.eclipse.jubula.client.core.model.ITestJobPO;
import org.eclipse.jubula.client.core.model.ITestSuitePO;
import org.eclipse.jubula.client.core.model.ITimestampPO;
import org.eclipse.jubula.client.core.persistence.EditSupport;
import org.eclipse.jubula.client.core.persistence.IncompatibleTypeException;
import org.eclipse.jubula.client.core.persistence.NodePM;
import org.eclipse.jubula.client.core.persistence.PMException;
import org.eclipse.jubula.client.ui.Plugin;
import org.eclipse.jubula.client.ui.actions.SearchTreeAction;
import org.eclipse.jubula.client.ui.businessprocess.GuiNodeBP;
import org.eclipse.jubula.client.ui.constants.CommandIDs;
import org.eclipse.jubula.client.ui.constants.ContextHelpIds;
import org.eclipse.jubula.client.ui.constants.IconConstants;
import org.eclipse.jubula.client.ui.controllers.PMExceptionHandler;
import org.eclipse.jubula.client.ui.controllers.dnd.LocalSelectionTransfer;
import org.eclipse.jubula.client.ui.controllers.dnd.TJEditorDropTargetListener;
import org.eclipse.jubula.client.ui.controllers.dnd.TreeViewerContainerDragSourceListener;
import org.eclipse.jubula.client.ui.events.GuiEventDispatcher;
import org.eclipse.jubula.client.ui.model.GuiNode;
import org.eclipse.jubula.client.ui.model.TestJobGUI;
import org.eclipse.jubula.client.ui.provider.contentprovider.TestJobEditorContentProvider;
import org.eclipse.jubula.client.ui.utils.CommandHelper;
import org.eclipse.jubula.client.ui.utils.Utils;
import org.eclipse.jubula.client.ui.views.TreeBuilder;
import org.eclipse.jubula.tools.exception.Assert;
import org.eclipse.jubula.tools.exception.GDProjectDeletedException;
import org.eclipse.jubula.tools.i18n.I18n;
import org.eclipse.jubula.tools.messagehandling.MessageIDs;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;


/**
 * The Test Job Editor
 * 
 * @author BREDEX GmbH
 * @created Mar 17, 2010
 */
public class TestJobEditor extends AbstractGDEditor {
    /**
     * <code>TEST_JOB_EDITOR_ROOT_NAME</code>
     */
    public static final String TEST_JOB_EDITOR_ROOT_NAME = "TestJobEditor_root"; //$NON-NLS-1$

    /**
     * {@inheritDoc}
     */
    public String getEditorPrefix() {
        return I18n.getString("Plugin.TJ"); //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    public void createPartControlImpl(Composite parent) {
        createMainPart(parent);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        getMainTreeViewer().getControl().setLayoutData(gridData);
        setControl(getMainTreeViewer().getControl());
        getMainTreeViewer().setContentProvider(
                new TestJobEditorContentProvider());
        addDragAndDropSupport();
        getEditorHelper().addListeners();
        setActionHandlers();
        setInitialInput();
        createContextMenu();
        DataEventDispatcher ded = DataEventDispatcher.getInstance();
        ded.addPropertyChangedListener(this, true);
        GuiEventDispatcher.getInstance().addEditorDirtyStateListener(
                this, true);
    }

    /**
     * 
     * {@inheritDoc}
     */
    public void setInitialInput() {
        GuiNode rootTop = new TestJobGUI(TEST_JOB_EDITOR_ROOT_NAME);
        INodePO rootPOTop = (INodePO)getEditorHelper().getEditSupport()
                .getWorkVersion();
        TreeBuilder.buildTestJobEditorTree((ITestJobPO)rootPOTop, rootTop);
        try {
            getTreeViewer().getTree().getParent().setRedraw(false);
            getTreeViewer().setInput(rootTop);
            getTreeViewer().expandAll();

        } finally {
            getTreeViewer().getTree().getParent().setRedraw(true);
        }
    }

    /**
     * adds Drag and Drop support for the trees.
     */
    protected void addDragAndDropSupport() {
        int ops = DND.DROP_MOVE;
        Transfer[] transfers = new Transfer[] { LocalSelectionTransfer
                .getInstance() };
        getMainTreeViewer().addDragSupport(ops, transfers,
                new TreeViewerContainerDragSourceListener(getTreeViewer()));
        getMainTreeViewer().addDropSupport(ops, transfers,
                new TJEditorDropTargetListener(this));
    }

    /**
     * {@inheritDoc}
     */
    protected void fillContextMenu(IMenuManager mgr) {
        CommandHelper.createContributionPushItem(mgr,
                CommandIDs.REVERT_CHANGES_COMMAND_ID);
        CommandHelper.createContributionPushItem(mgr,
                CommandIDs.SHOW_SPECIFICATION_COMMAND_ID);
        CommandHelper.createContributionPushItem(mgr,
                CommandIDs.OPEN_SPECIFICATION_COMMAND_ID);
        mgr.add(new Separator());
        CommandHelper.createContributionPushItem(mgr,
                CommandIDs.DELETE_COMMAND_ID);
        mgr.add(SearchTreeAction.getAction());
        CommandHelper.createContributionPushItem(mgr,
                CommandIDs.EXPAND_TREE_ITEM_COMMAND_ID);
    }

    /**
     * {@inheritDoc}
     */
    protected void setHelp(Composite parent) {
        Plugin.getHelpSystem().setHelp(parent, 
                ContextHelpIds.TEST_JOB_EDITOR);   
    }

    /**
     * {@inheritDoc}
     */
    public void doSave(IProgressMonitor monitor) {
        monitor.beginTask(I18n.getString(
                "Editors.saveEditors"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
        try {
            EditSupport editSupport = getEditorHelper().getEditSupport();
            final IPersistentObject perObj = editSupport.getWorkVersion();
            TimestampBP.refreshTimestamp((ITimestampPO)perObj);
            editSupport.saveWorkVersion();

            getEditorHelper().resetEditableState();
            getEditorHelper().setDirty(false);
        } catch (IncompatibleTypeException pmce) {
            handlePMCompNameException(pmce);
        } catch (PMException e) {
            PMExceptionHandler.handlePMExceptionForMasterSession(e);
            try {
                reOpenEditor(((NodeEditorInput)getEditorInput()).getNode());
            } catch (PMException e1) {
                PMExceptionHandler.handlePMExceptionForEditor(e, this);
            }
        } catch (GDProjectDeletedException e) {
            PMExceptionHandler.handleGDProjectDeletedException();
        } finally {
            monitor.done();
        }
    }

    /**
     * {@inheritDoc}
     */
    public Image getDisabledTitleImage() {
        return IconConstants.DISABLED_TJ_EDITOR_IMAGE;
    }

    /**
     * {@inheritDoc}
     */
    public void handleDataChanged(IPersistentObject po, DataState dataState,
            UpdateState updateState) {
        if (po instanceof INodePO) {
            GuiNode root = (GuiNode)getTreeViewer().getInput();
            TreeViewer tv = getTreeViewer();
            switch (dataState) {
                case Added:
                    INodePO addedNode = (INodePO)po;
                    INodePO editorNode = (INodePO)getEditorHelper()
                            .getEditSupport().getWorkVersion();
                    if (editorNode.indexOf(addedNode) > -1) {
                        root = root.getChildren().get(0);
                        GuiNodeBP.rebuildEditorGuiNode(root, editorNode);
                        getTreeViewer().refresh();
                        getTreeViewer().expandAll();
                        GuiNodeBP.setSelectionAndFocusToNode(addedNode, tv);
                    }
                    break;
                case Deleted:
                    if (!(po instanceof IProjectPO)) {
                        boolean deleted = GuiNodeBP.deleteGuiNode(root,
                                (INodePO)po);
                        if (deleted) {
                            GuiNode guiNode = (GuiNode)getTreeViewer()
                                    .getInput();
                            guiNode = guiNode.getChildren().get(0);
                            getTreeViewer().refresh();
                            setFocus();
                            getTreeViewer().setSelection(
                                    new StructuredSelection(guiNode));
                        }
                    }
                    break;
                case Renamed:
                    renameGUINode(po);
                    break;
                case StructureModified:
                    if (!handleStructureModified(po)) {
                        return;
                    }
                    break;
                case ReuseChanged:
                    // nothing yet!
                    break;
                default:
                    Assert.notReached();
            }
            getEditorHelper().handleDataChanged(po, dataState, updateState);
        }
    }

    /**
     * Handles a PO that has been modified.
     * 
     * @param po
     *            The modified object.
     * @return <code>false</code> if an error occurs during handling. Otherwise,
     *         <code>true</code>.
     */
    private boolean handleStructureModified(IPersistentObject po) {
        if (po instanceof ITestSuitePO) {
            final ITestSuitePO testsuitePO = (ITestSuitePO)po;
            final INodePO workVersion = (INodePO)getEditorHelper()
                    .getEditSupport().getWorkVersion();
            final List<IRefTestSuitePO> refTestSuites = NodePM
                    .getInternalRefTestSuites(testsuitePO.getGuid(),
                            testsuitePO.getParentProjectId());
            if (!refTestSuites.isEmpty()
                    && containsWorkVersionReuses(workVersion, testsuitePO)) {
                if (Plugin.getActiveEditor() != this && isDirty()) {
                    Utils.createMessageDialog(
                            MessageIDs.I_SAVE_AND_REOPEN_EDITOR, new Object[] {
                                    getTitle(), testsuitePO.getName() },
                            null);
                    return false;
                }
                try {
                    reOpenEditor(getEditorHelper().getEditSupport()
                            .getOriginal());
                } catch (PMException e) {
                    Utils.createMessageDialog(MessageIDs.E_REFRESH_FAILED,
                            null, new String[] { I18n
                                    .getString("ErrorMessage.EDITOR_CLOSE") }); //$NON-NLS-1$
                    getSite().getPage().closeEditor(this, false);
                }
                return false;
            }
        }

        return true;
    }
    
    /**
     * @param root
     *            node, where starts the validation
     * @param ts
     *            changed test suite
     * @return if editor contains an reusing testcase for given specTestCase
     */
    @SuppressWarnings("unchecked")
    private static boolean containsWorkVersionReuses(INodePO root,
            ITestSuitePO ts) {
        final Iterator it = root.getNodeListIterator();
        final List<INodePO> childList = IteratorUtils.toList(it);
        for (INodePO child : childList) {
            if (child instanceof IRefTestSuitePO) {
                final IRefTestSuitePO refTs = (IRefTestSuitePO)child;
                if (ts.getGuid().equals(refTs.getTestSuiteGuid())) {
                    return true;
                }
                if (containsWorkVersionReuses(refTs, ts)) {
                    return true;
                }
            }
        }
        return false;
    }
}