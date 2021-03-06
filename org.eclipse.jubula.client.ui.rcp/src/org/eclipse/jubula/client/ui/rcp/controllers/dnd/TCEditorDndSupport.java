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
package org.eclipse.jubula.client.ui.rcp.controllers.dnd;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.jubula.client.core.businessprocess.db.TestCaseBP;
import org.eclipse.jubula.client.core.model.ICapPO;
import org.eclipse.jubula.client.core.model.ICategoryPO;
import org.eclipse.jubula.client.core.model.IExecTestCasePO;
import org.eclipse.jubula.client.core.model.INodePO;
import org.eclipse.jubula.client.core.model.IProjectPO;
import org.eclipse.jubula.client.core.model.ISpecTestCasePO;
import org.eclipse.jubula.client.core.model.ITestSuitePO;
import org.eclipse.jubula.client.core.persistence.EditSupport;
import org.eclipse.jubula.client.core.persistence.PMAlreadyLockedException;
import org.eclipse.jubula.client.core.persistence.PMDirtyVersionException;
import org.eclipse.jubula.client.core.persistence.PMException;
import org.eclipse.jubula.client.ui.rcp.controllers.MultipleTCBTracker;
import org.eclipse.jubula.client.ui.rcp.controllers.PMExceptionHandler;
import org.eclipse.jubula.client.ui.rcp.editors.AbstractTestCaseEditor;
import org.eclipse.jubula.client.ui.rcp.editors.JBEditorHelper;
import org.eclipse.jubula.client.ui.rcp.editors.NodeEditorInput;
import org.eclipse.jubula.client.ui.rcp.views.TestCaseBrowser;


/**
 * Utility class containing methods for use in drag and drop as well as 
 * cut and paste operations in the Test Case Editor.
 *
 * @author BREDEX GmbH
 * @created 25.03.2008
 */
public class TCEditorDndSupport extends AbstractEditorDndSupport {

    /**
     * Private constructor
     */
    private TCEditorDndSupport() {
        // Do nothing
    }

    /**
     * 
     * @param targetEditor The editor to which the item is to be dropped/pasted.
     * @param toDrop The items that were dragged/cut.
     * @param dropTarget The drop/paste target.
     * @param dropPosition One of the values defined in ViewerDropAdapter to 
     *                     indicate the drop position relative to the drop
     *                     target.
     * @return <code>true</code> if the drop/paste was successful. 
     *         Otherwise <code>false</code>.
     */
    public static boolean performDrop(AbstractTestCaseEditor targetEditor,
            IStructuredSelection toDrop, INodePO dropTarget, int dropPosition) {
        if (targetEditor.getEditorHelper().requestEditableState() 
                != JBEditorHelper.EditableState.OK) {
            return false;
        }
        List<Object> selectedElements = toDrop.toList();
        Collections.reverse(selectedElements);
        Iterator iter = selectedElements.iterator();
        while (iter.hasNext()) {
            INodePO droppedNode = null;
            Object obj = iter.next();
            if (!(obj instanceof INodePO)) {
                return false;
            }
            INodePO node = (INodePO)obj;
            if (node instanceof ICapPO || node instanceof IExecTestCasePO) {
                INodePO target = dropTarget;
                if (target != node
                    && (target instanceof ICapPO 
                        || target instanceof IExecTestCasePO)) {
                        
                    droppedNode = moveNode(node, target);
                }
            }
            if (node instanceof ISpecTestCasePO) {
                if (!performDrop(targetEditor, dropTarget, 
                        dropPosition, (ISpecTestCasePO)node)) {
                    return false;
                }
            }
            postDropAction(droppedNode, targetEditor);
        }
        return true;
    }

    /**
     * 
     * @param targetEditor The editor to which the item is to be dropped/pasted.
     * @param toDrop The item that was dragged/cut.
     * @param dropTarget The drop/paste target.
     * @param dropPosition One of the values defined in ViewerDropAdapter to 
     *                     indicate the drop position relative to the drop
     *                     target.
     * @return <code>true</code> if the drop/paste was successful. 
     *         Otherwise <code>false</code>.
     */
    private static boolean performDrop(AbstractTestCaseEditor targetEditor,
            INodePO dropTarget, int dropPosition, ISpecTestCasePO toDrop) {
        INodePO target = dropTarget;
        if (target != toDrop) {
            EditSupport editSupport = 
                targetEditor.getEditorHelper().getEditSupport();
            try {
                if (target instanceof ICapPO 
                    || target instanceof IExecTestCasePO) {
                    dropOnCAPorExecTc(editSupport, toDrop, 
                            target, dropPosition);
                } else if (target instanceof ISpecTestCasePO) {
                    dropOnSpecTc(editSupport, toDrop, target);
                } else if (target instanceof ITestSuitePO) {
                        
                    dropOnTestsuite(editSupport, (ITestSuitePO)target, toDrop);
                }
            } catch (PMException e) {
                NodeEditorInput inp = (NodeEditorInput)targetEditor.
                    getAdapter(NodeEditorInput.class);
                INodePO inpNode = inp.getNode();
                PMExceptionHandler.handlePMExceptionForMasterSession(e);
                // If an object was already locked, *and* the locked 
                // object is not the editor Test Case, *and* the editor 
                // is dirty, then we do *not* want to revert all 
                // editor changes.
                // The additional test as to whether the the editor is 
                // marked as dirty is important because, due to the 
                // requestEditableState() call earlier in this method, 
                // the editor TC is locked (even though the editor 
                // isn't dirty). Reopening the editor removes this lock.
                if (!(e instanceof PMAlreadyLockedException
                        && ((PMAlreadyLockedException)e)
                            .getLockedObject() != null
                        && !((PMAlreadyLockedException)e)
                            .getLockedObject().equals(inpNode))
                    || !targetEditor.isDirty()) {
                    
                    try {
                        targetEditor.reOpenEditor(inpNode);
                    } catch (PMException e1) {
                        PMExceptionHandler.handlePMExceptionForEditor(e,
                                targetEditor);
                    }
                }
                return false;
            }
        }
        
        return true;
    }

    /**
     * 
     * @param sourceViewer The viewer containing the dragged/cut item.
     * @param targetViewer The viewer to which the item is to be dropped/pasted.
     * @param toDrop The items that were dragged/cut.
     * @param dropTarget The drop/paste target.
     * @param allowFromBrowser Whether items from the Test Case Browser are 
     *                         allowed to be dropped/pasted.
     * @return <code>true</code> if the given information indicates that the
     *         drop/paste is valid. Otherwise <code>false</code>.
     */
    public static boolean validateDrop(Viewer sourceViewer, Viewer targetViewer,
            IStructuredSelection toDrop, INodePO dropTarget, 
            boolean allowFromBrowser) {
        if (toDrop == null || toDrop.isEmpty() || dropTarget == null) {
            return false;
        }

        if (sourceViewer != null && !sourceViewer.equals(targetViewer)) {
            boolean foundOne = false;
            for (TestCaseBrowser tcb : MultipleTCBTracker.getInstance()
                    .getOpenTCBs()) {
                if (sourceViewer.equals(tcb.getTreeViewer())) {
                    foundOne = true;
                }
            }
            if (!(allowFromBrowser && foundOne)) {
                return false;
            }
        }

        Iterator iter = toDrop.iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            if (!(obj instanceof INodePO)) {
                return false;
            } 
            INodePO transferGUI = (INodePO)obj;
            INodePO parentNode = transferGUI.getParentNode();
            if (!(parentNode instanceof ISpecTestCasePO)
                    && !(parentNode instanceof IProjectPO)
                    && !(parentNode instanceof ICategoryPO)) {
                return false;
            }
            if (!(transferGUI instanceof ISpecTestCasePO) 
                    && transferGUI.getParentNode() 
                        != dropTarget.getParentNode()) {
                return false;
            }
            ISpecTestCasePO specTcGUI;
            if (!(dropTarget instanceof ISpecTestCasePO)) {
                specTcGUI = (ISpecTestCasePO)dropTarget.getParentNode();
            } else {
                specTcGUI = (ISpecTestCasePO)dropTarget;
            }
            if (!(transferGUI instanceof ISpecTestCasePO)) {
                continue;
            }
            ISpecTestCasePO childGUI = (ISpecTestCasePO)transferGUI;
            if (childGUI.hasCircularDependences(specTcGUI)) {
                
                return false;
            }
        }
        return true;

    }

    /**
     * @param editSupport The EditSupport in which to perform the action.
     * @param node the node to be dropped.
     * @param target the target node.
     * @throws PMDirtyVersionException in case of version conflict (dirty read)
     * @throws PMAlreadyLockedException if the origSpecTc is already locked by another user
     * @throws PMException in case of unspecified db error
     */
    private static void dropOnSpecTc(EditSupport editSupport, 
            INodePO node, INodePO target)
        throws PMAlreadyLockedException,
            PMDirtyVersionException, PMException {
        TestCaseBP.addReferencedTestCase(editSupport, 
                target, (ISpecTestCasePO)node, 0);
    }
    
    /**
     * Drops the given TestCase on the given TestSuite.
     * The TestCase will be inserted at the end.
     * @param editSupport The EditSupport in which to perform the action.
     * @param testSuite the TestSuite to drop on
     * @param testcase the TestCAse to drop
     * @throws PMAlreadyLockedException in case of persistence error
     * @throws PMDirtyVersionException in case of persistence error
     * @throws PMException in case of persistence error
     */
    private static void dropOnTestsuite(EditSupport editSupport, 
            ITestSuitePO testSuite, ISpecTestCasePO testcase) 
        throws PMAlreadyLockedException, 
        PMDirtyVersionException, PMException {
        
        TestCaseBP.addReferencedTestCase(editSupport, testSuite, 
                testcase, 0);
    }

    /**
     * @param editSupport The EditSupport in which to perform the action.
     * @param node the node to be dropped
     * @param target the target node.
     * @param location One of the values defined in ViewerDropAdapter to 
     *                     indicate the drop position relative to the drop
     *                     target.
     * @throws PMDirtyVersionException in case of version conflict (dirty read)
     * @throws PMAlreadyLockedException if the origSpecTc is already locked by another user
     * @throws PMException in case of unspecified db error
     */
    private static void dropOnCAPorExecTc(EditSupport editSupport, 
            INodePO node, INodePO target,
            int location) throws PMAlreadyLockedException,
            PMDirtyVersionException, PMException {
        ISpecTestCasePO specTcGUItoDrop = (ISpecTestCasePO)node;
        INodePO parentGUI = target.getParentNode();
        int position = parentGUI.indexOf(target);
        if (location != ViewerDropAdapter.LOCATION_BEFORE) {
            position++;
        }
        TestCaseBP.addReferencedTestCase(editSupport, parentGUI, 
                specTcGUItoDrop, position);
    }
}
