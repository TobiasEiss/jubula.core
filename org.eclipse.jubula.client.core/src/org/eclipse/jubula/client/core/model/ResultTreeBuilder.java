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
package org.eclipse.jubula.client.core.model;

import org.apache.commons.lang.Validate;
import org.eclipse.jubula.client.core.i18n.Messages;
import org.eclipse.jubula.client.core.utils.Traverser;
import org.eclipse.jubula.tools.i18n.CompSystemI18n;


/**
 * 
 * class for creation of result tree for display of test results
 * @author BREDEX GmbH
 * @created 14.04.2005
 *
 */
public class ResultTreeBuilder implements IExecStackModificationListener {
    
    /**
     * <code>m_endNode</code> last result node in resultTree, which is associated 
     * with last execTestCase or with the testsuite
     */
    private TestResultNode m_endNode;
    
    /**
     * <code>m_rootNode</code>root resultNode of resultTree
     */
    private TestResultNode m_rootNode;
    
    /**
     * <code>m_lastCap</code> resultNode to actual executed cap
     */
    private TestResultNode m_lastCap;

    /**
     * @param trav traverser for associated testexecution tree
     */
    public ResultTreeBuilder(Traverser trav) {
        Validate.notNull(trav, Messages.NoTraverserInstance);
        m_rootNode = new TestResultNode(trav.getRoot(), null);
        m_endNode = m_rootNode;
    }
    
    /** 
     * {@inheritDoc}
     */
    public void stackIncremented(INodePO node) {
        m_endNode = new TestResultNode(node, m_endNode);     
    }

    /** 
     * {@inheritDoc}
     */
    public void stackDecremented() {
        m_endNode = m_endNode.getParent();
    }

    /** 
     * {@inheritDoc}
     */
    public void nextDataSetIteration() {
        m_endNode = new TestResultNode(m_endNode.getNode(), 
            m_endNode.getParent());
    }

    /** 
     * {@inheritDoc}
     */
    public void nextCap(ICapPO cap) {
        m_lastCap = new TestResultNode(cap, m_endNode);
        m_lastCap.setActionName(CompSystemI18n.getString(cap.getActionName()));
        m_lastCap.setComponentType(
                CompSystemI18n.getString(cap.getComponentType()));
    }
    /**
     * @return Returns the rootNode.
     */
    public TestResultNode getRootNode() {
        return m_rootNode;
    }

    /**
     * {@inheritDoc}
     */
    public void retryCap(ICapPO cap) {
        nextCap(cap);
    }
}
