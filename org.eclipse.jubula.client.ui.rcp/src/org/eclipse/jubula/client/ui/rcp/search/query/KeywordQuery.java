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
package org.eclipse.jubula.client.ui.rcp.search.query;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jubula.client.core.model.INodePO;
import org.eclipse.jubula.client.ui.rcp.search.data.SearchOptions;


/**
 * Keyword search query for names of nodes in Test Suite Browser or
 * Test Case Browsers.
 * @author BREDEX GmbH
 * @created Aug 9, 2010
 */
public class KeywordQuery extends AbstractTraverserQuery {

    /**
     * @param searchData The search data to use for this query.
     */
    public KeywordQuery(SearchOptions searchData) {
        super(searchData, null);
    }

    /**
     * Search in the whole project or in selected nodes for keywords using the
     * {@link TextFinder} depending on the {@link SearchOptions} given
     * to the constructor.
     * {@inheritDoc}
     */
    public IStatus run(IProgressMonitor monitor) {
        setMonitor(monitor);
        traverse();
        if (monitor.isCanceled()) {
            return Status.CANCEL_STATUS;
        }
        finished();
        return Status.OK_STATUS;
    }

    /**
     * Add the given node to the result, if it has the matching type and
     * the name contains the search string.
     * {@inheritDoc}
     */
    protected boolean operate(INodePO node) {
        if (matchSearchString(node.getName()) && matchingSearchType(node)) {
            // found node with keyword and correct type
            add(node);
        }
        return true;
    }

}
