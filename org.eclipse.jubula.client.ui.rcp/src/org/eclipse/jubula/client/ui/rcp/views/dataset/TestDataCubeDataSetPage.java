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
package org.eclipse.jubula.client.ui.rcp.views.dataset;

import java.util.Locale;

import org.eclipse.jubula.client.core.businessprocess.ParameterInterfaceBP;
import org.eclipse.jubula.client.core.model.IParameterInterfacePO;


/**
 * @author BREDEX GmbH
 * @created Jul 13, 2010
 */
public class TestDataCubeDataSetPage extends AbstractDataSetPage {
    /**
     * Constructor
     */
    public TestDataCubeDataSetPage() {
        super(new ParameterInterfaceBP());
    }

    /** {@inheritDoc} */
    protected boolean isEditorOpen(IParameterInterfacePO paramNode) {
        // Test Data Cubes can only be seen in the editor --> implies editor open
        return true;
    }

    /** {@inheritDoc} */
    protected boolean isNodeValid(IParameterInterfacePO cParamInterfaceObj) {
        return true;
    }

    /** {@inheritDoc} */
    protected void setIsEntrySetComplete(IParameterInterfacePO paramNode,
            Locale locale) {
        // currently not used
    }
}
