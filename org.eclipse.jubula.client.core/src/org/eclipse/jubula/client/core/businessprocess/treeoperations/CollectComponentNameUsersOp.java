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
package org.eclipse.jubula.client.core.businessprocess.treeoperations;

import org.eclipse.jubula.client.core.businessprocess.ComponentNamesBP;
import org.eclipse.jubula.client.core.datastructure.CompNameUsageMap;
import org.eclipse.jubula.client.core.model.ICapPO;
import org.eclipse.jubula.client.core.model.ICompNamesPairPO;
import org.eclipse.jubula.client.core.model.IComponentNamePO;
import org.eclipse.jubula.client.core.model.IExecTestCasePO;
import org.eclipse.jubula.client.core.model.INodePO;
import org.eclipse.jubula.client.core.utils.ITreeNodeOperation;
import org.eclipse.jubula.client.core.utils.ITreeTraverserContext;
import org.eclipse.jubula.tools.exception.JBException;


/**
 * Collects used Component Names and their users.
 *
 * @author BREDEX GmbH
 * @created Mar 30, 2009
 */
public class CollectComponentNameUsersOp 
        implements ITreeNodeOperation<INodePO> {

    /** mapping from used Component Names to their users */
    private CompNameUsageMap m_usageMap = new CompNameUsageMap();
    
    /** the GUID of the currently opened Project */
    private String m_projectGuid;
    
    /** the ID of the currently opened Project */
    private Long m_projectId;

    /** 
     * the exception that occurred during traversal, or <code>null</code>
     * if no such exception occurred.
     */
    private JBException m_exception = null;
    
    /**
     * Constructor
     * 
     * @param projectGuid The GUID of the currently opened Project.
     * @param projectId The ID of the currently opened Project.
     */
    public CollectComponentNameUsersOp(String projectGuid, Long projectId) {
        m_projectGuid = projectGuid;
        m_projectId = projectId;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean operate(ITreeTraverserContext<INodePO> ctx, 
            INodePO parent, INodePO node, boolean alreadyVisited) {

        try {
            if (node instanceof ICapPO) {
                final ICapPO cap = (ICapPO)node;
                final String guid = cap.getComponentName();
                IComponentNamePO compNamePo;
                compNamePo = ComponentNamesBP.getInstance()
                    .getCompNamePo(guid, m_projectGuid);
                m_usageMap.addSecondNameUser(compNamePo, cap);
            } else if (node instanceof IExecTestCasePO) {
                final IExecTestCasePO execTc = (IExecTestCasePO)node;
                for (ICompNamesPairPO pair : execTc.getCompNamesPairs()) {
                    String guid = pair.getFirstName();
                    IComponentNamePO compNamePo = ComponentNamesBP
                        .getInstance().getCompNamePo(guid, m_projectGuid);
                    if (compNamePo != null 
                            && compNamePo.getParentProjectId().equals(
                                    m_projectId)) {
                        m_usageMap.addFirstNameUser(compNamePo, execTc);
                    }

                    guid = pair.getSecondName();
                    compNamePo = ComponentNamesBP
                    .getInstance().getCompNamePo(guid, m_projectGuid);
                    if (compNamePo != null 
                            && compNamePo.getParentProjectId().equals(
                                    m_projectId)) {

                        m_usageMap.addSecondNameUser(compNamePo, execTc);
                    }

                }
            }
        } catch (JBException e) {
            m_exception = e;
            ctx.setContinued(false); 
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void postOperate(ITreeTraverserContext<INodePO> ctx, INodePO parent,
            INodePO node, boolean alreadyVisited) {
        // no op
    }

    /**
     * 
     * @return the Component Name usage map.
     * @throws JBException if a <code>JBException</code> occurred during 
     *                     traversal.
     */
    public CompNameUsageMap getUsageMap() throws JBException {
        if (m_exception != null) {
            throw m_exception;
        }
        return m_usageMap;
    }
}
