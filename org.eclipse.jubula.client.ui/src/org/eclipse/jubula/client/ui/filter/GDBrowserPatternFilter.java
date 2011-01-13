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
package org.eclipse.jubula.client.ui.filter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jubula.client.ui.model.CapGUI;
import org.eclipse.jubula.client.ui.model.ExecTestCaseGUI;


/**
 * Patter Filter for Test Case Browser; skips CAPs and Exec's due to performance
 * issues
 * 
 * @author BREDEX GmbH
 * @created 04.03.2009
 */
public class GDBrowserPatternFilter extends GDPatternFilter {

    /**
     * {@inheritDoc}
     */
    public boolean isElementVisible(Viewer viewer, Object element) {
        if (element instanceof CapGUI || element instanceof ExecTestCaseGUI) {
            return false;
        }
        return super.isElementVisible(viewer, element);
    }
}
