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
package org.eclipse.jubula.client.ui.businessprocess;

import javax.persistence.EntityManager;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jubula.client.core.events.InteractionEventDispatcher;
import org.eclipse.jubula.client.core.model.IExecTestCasePO;
import org.eclipse.jubula.client.core.model.INodePO;
import org.eclipse.jubula.client.core.model.IPersistentObject;
import org.eclipse.jubula.client.core.model.ISpecTestCasePO;
import org.eclipse.jubula.client.core.model.ITestSuitePO;
import org.eclipse.jubula.client.core.model.NodeMaker;
import org.eclipse.jubula.client.core.model.TestResultNode;
import org.eclipse.jubula.client.core.persistence.Hibernator;


/**
 * BP utility class for GUINodes.
 *
 * @author BREDEX GmbH
 * @created 03.03.2006
 */
public class UINodeBP {

    /**
     * Default utility constructor.
     */
    private UINodeBP() {
        // do nothing
    }
    
    /**
     * Gets the TestSuite of the given node
     * @param node the node
     * @return the TestSuite of the given node or null if no TestSuite found
     */
    public static ITestSuitePO getTestSuiteOfNode(INodePO node) {
        INodePO parent = node;
        while (!(parent instanceof ITestSuitePO) && parent != null) {
            parent = parent.getParentNode();
        }
        return (ITestSuitePO)parent;
    }

    /**
     * sets the selection in the given TreeViewer to the given node and also
     * gives the given tree viewer focus
     * 
     * @param node
     *            the node to select
     * @param tv
     *            the TreeViewer
     */
    public static void setSelectionAndFocusToNode(INodePO node, TreeViewer tv) {
        tv.getTree().setFocus();
        tv.setSelection(new StructuredSelection(node), true);
    }
    

    /**
     * @param structuredSel
     *            find the referenced specification test case for the given
     *            structured selection
     * @return a valid ISpecTestCasePO or <code>null</code> if no reference
     *         could be found
     */
    public static ISpecTestCasePO getSpecTC(
        IStructuredSelection structuredSel) {
        IExecTestCasePO execTc = null;
        if (structuredSel.getFirstElement() instanceof IExecTestCasePO) {
            execTc = (IExecTestCasePO)structuredSel.getFirstElement();
        } else if (structuredSel.getFirstElement() instanceof TestResultNode) {
            TestResultNode trNode = (TestResultNode)structuredSel
                    .getFirstElement();
            INodePO nodePO = trNode.getNode();
            while (!(Hibernator.isPoSubclass(nodePO, IExecTestCasePO.class))) {
                trNode = trNode.getParent();
                if (trNode == null) {
                    return null;
                }
                nodePO = trNode.getNode();
            }
            execTc = (IExecTestCasePO)nodePO;
        }
        if (execTc != null) {
            return execTc.getSpecTestCase();
        }
        return null;
    }

    /**
     * Tries to select a node with the given ID in the given TreeViewer.
     * 
     * @param id
     *            The id of the node to select
     * @param tv
     *            the TreeViewer
     * @param em
     *            the entity manager to use for retrieving the node with the
     *            given id
     * @return the node that has been selected or null if not found
     */
    public static INodePO selectNodeInTree(Long id, TreeViewer tv,
            EntityManager em) {
        INodePO nodeToSelect = em.find(NodeMaker.getNodePOClass(), id);
        selectNodeInTree(nodeToSelect, tv);
        return nodeToSelect;
    }
    
    /**
     * Tries to select the given node in the given TreeViewer.
     * 
     * @param po
     *            The po to select
     * @param tv
     *            the TreeViewer
     */
    public static void selectNodeInTree(IPersistentObject po, TreeViewer tv) {
        ISelection oldSelection = tv.getSelection();
        if (po != null) {
            tv.refresh();
            tv.expandToLevel(po, 0);
            tv.reveal(po);
            StructuredSelection newSelection = 
                new StructuredSelection(po);
            tv.setSelection(newSelection);
            InteractionEventDispatcher.getDefault()
                .fireProgammableSelectionEvent(newSelection);
        } else {
            tv.setSelection(oldSelection);
        }
    }
}