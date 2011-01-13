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
package org.eclipse.jubula.client.ui.handlers.switchto;

import org.eclipse.jubula.client.ui.constants.Constants;
import org.eclipse.jubula.client.ui.views.dataset.AbstractDataSetPage;
import org.eclipse.jubula.client.ui.views.dataset.GDDataSetView;
import org.eclipse.jubula.client.ui.views.dataset.AbstractDataSetPage.DSVTableCursor;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;


/**
 * @author BREDEX GmbH
 * @created Sep 3, 2010
 */
public class SwitchToDataSetViewHandler extends AbstractSwitchToHandler {
    /**
     * {@inheritDoc}
     */
    protected String getViewIDToSwitchTo() {
        return Constants.GUIDANCERDATASET_VIEW_ID;
    }
    
    /**
     * {@inheritDoc}
     */
    protected void executeSetFocus(IWorkbenchPart activePart) {
        if (activePart instanceof GDDataSetView) {
            GDDataSetView dataSetView = (GDDataSetView) activePart;
            IPage p = dataSetView.getCurrentPage();
            if (p instanceof AbstractDataSetPage) {
                AbstractDataSetPage dataSetPage = (AbstractDataSetPage) p;
                DSVTableCursor tableCursor = dataSetPage.getTableCursor();
                if (tableCursor != null) {
                    try {
                        tableCursor.setSelection(0, 0);
                    } catch (IllegalArgumentException e) {
                        // ignore
                    }
                }
            }
        }
    }
}