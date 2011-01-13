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
package org.eclipse.jubula.client.ui.perspective;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jubula.client.ui.constants.Constants;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;


/**
 * The Execution Perspective.
 * @author BREDEX GmbH
 * @created 08.08.2005
 */
public class ExecutionPerspective implements IPerspectiveFactory {

    /** The logger */
    static final Log LOG = LogFactory.getLog(ExecutionPerspective.class);
    /** Ration 0.27f */
    private static final float RATIO_0_27 = 0.27f;
    /** Ration 0.5f */
    private static final float RATIO_0_5 = 0.5f;
    /** Ration 0.6f */
    private static final float RATIO_0_6 = 0.6f;
    
    /**
     * construct ExecutionPerspective
     */
    public ExecutionPerspective() { 
        super();
    }
    /**
     * Creates the initial layout for a page.
     * @param layout IPageLayout
     */
    public void createInitialLayout(IPageLayout layout) { 
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);
        IFolderLayout topLeft = layout.createFolder("topLeft", //$NON-NLS-1$
                IPageLayout.LEFT, RATIO_0_27, editorArea);
        IFolderLayout topRight = layout.createFolder("topRight", //$NON-NLS-1$
                IPageLayout.RIGHT, RATIO_0_6, editorArea);
        IFolderLayout bottomRight = layout.createFolder("bottomRight", //$NON-NLS-1$
                IPageLayout.BOTTOM, RATIO_0_5, "topRight"); //$NON-NLS-1$
        IFolderLayout middle = layout.createFolder("middle", //$NON-NLS-1$
                IPageLayout.LEFT, RATIO_0_27, editorArea);
        topLeft.addView(Constants.TS_BROWSER_ID);
        middle.addView(Constants.TESTRE_ID);
        topRight.addView(Constants.PROPVIEW_ID);
        bottomRight.addView(Constants.IMAGEVIEW_ID);

        // mark test result tree view as not closeable
        layout.getViewLayout(Constants.TESTRE_ID).setCloseable(false);
    }
}