/*******************************************************************************
 * Copyright (c) 2013 BREDEX GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BREDEX GmbH - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.jubula.client.ui.rcp.propertytester;

import org.eclipse.jubula.client.core.model.TestResultNode;
import org.eclipse.jubula.client.core.propertytester.AbstractBooleanPropertyTester;

/**
 * PropertyTester for TestResultSummary.
 *
 * @author BREDEX GmbH
 * @created Sep 13, 2013
 */
public class ImageViewPropertyTester
    extends AbstractBooleanPropertyTester {
    /** the id of the "hasMonitoringData" property */
    private static final String HAS_IMAGE = "hasImage"; //$NON-NLS-1$

    /**
     * <code>PROPERTIES</code>
     */
    private static final String[] PROPERTIES = new String[] {
        HAS_IMAGE };

    /** {@inheritDoc} */
    public boolean testImpl(Object receiver, String property, Object[] args) {
        if (property.equals(HAS_IMAGE)) {
            TestResultNode node = (TestResultNode) receiver;
            if (node.getScreenshot() != null) {
                return true;
            }
        }
        return false;
    }

    /** {@inheritDoc} */
    public Class<? extends Object> getType() {
        return TestResultNode.class;
    }

    /** {@inheritDoc} */
    public String[] getProperties() {
        return PROPERTIES;
    }
}
