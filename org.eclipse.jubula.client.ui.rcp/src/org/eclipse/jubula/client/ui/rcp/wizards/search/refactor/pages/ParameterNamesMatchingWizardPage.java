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
package org.eclipse.jubula.client.ui.rcp.wizards.search.refactor.pages;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jubula.client.core.model.IParamDescriptionPO;
import org.eclipse.jubula.client.core.model.ISpecTestCasePO;
import org.eclipse.jubula.client.ui.constants.ContextHelpIds;
import org.eclipse.jubula.client.ui.rcp.Plugin;
import org.eclipse.jubula.client.ui.rcp.i18n.Messages;
import org.eclipse.jubula.client.ui.rcp.provider.ControlDecorator;
import org.eclipse.jubula.client.ui.rcp.provider.labelprovider.GeneralLabelProvider;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Wizard page for matching the parameter names.
 *
 * @author BREDEX GmbH
 */
public class ParameterNamesMatchingWizardPage
        extends AbstractMatchSelectionPage
        implements SelectionListener {

    /** The data for replacing execution Test Cases. */
    private final ReplaceExecTestCaseData m_replaceExecTestCasesData;

    /** The last selected new specification Test Case. */
    private ISpecTestCasePO m_lastNewSpecTestCase;

    /** An array of combo boxes for the old names. */
    private List<Combo> m_oldNameCombos = new ArrayList<Combo>();

    /**
     * @param pageName
     *            The name of the page.
     * @param replaceExecTestCasesData The data for replacing execution Test Cases.
     */
    public ParameterNamesMatchingWizardPage(String pageName,
            ReplaceExecTestCaseData replaceExecTestCasesData) {
        super(pageName, Messages.ReplaceTCRWizard_matchParameterNames_title,
                null);
        m_replaceExecTestCasesData = replaceExecTestCasesData;
        setDescription(Messages
                .ReplaceTCRWizard_matchParameterNames_multi_description);
        setPageComplete(true);
    }

    /**
     * Create the table of parameters showing the new parameters at the left
     * column and the combo boxes at the right column.
     * @param parent The parent composite with a grid layout of 2 columns.
     */
    @Override
    protected void createSelectionTable(Composite parent) {
        if (m_lastNewSpecTestCase == m_replaceExecTestCasesData
                .getNewSpecTestCase()) {
            return; // no new specification Test Case has been selected
        }
        m_lastNewSpecTestCase = m_replaceExecTestCasesData
                .getNewSpecTestCase();
        // remove the previously shown parameter names
        for (Control child: parent.getChildren()) {
            child.dispose();
        }
        // create head row
        createHeadLabel(parent, Messages
                .ReplaceTCRWizard_matchParameterNames_newParameter);
        createHeadLabel(parent, Messages
                .ReplaceTCRWizard_matchParameterNames_oldParameter);
        // fill the rows with the new parameter names
        List<IParamDescriptionPO> paramDescList = m_replaceExecTestCasesData
                .getNewSpecTestCase()
                .getParameterList();
        m_oldNameCombos.clear();
        for (IParamDescriptionPO paramDesc: paramDescList) {
            createLabel(parent,
                    GeneralLabelProvider.getTextWithBrackets(paramDesc));
            List<String> oldNames = m_replaceExecTestCasesData
                    .getOldParameterNamesByType(paramDesc);
            if (oldNames.size() == 0) {
                Label label = createLabel(parent, Messages
                    .ReplaceTCRWizard_matchParameterNames_warningNoSameType);
                ControlDecorator.addWarningDecorator(
                    label,
                    Messages
                    .ReplaceTCRWizard_matchParameterNames_warningNoSameTypeDesc
                );
                m_oldNameCombos.add(null); // remember no matching with null
            } else {
                String message = NLS.bind(Messages
                    .ReplaceTCRWizard_matchParameterNames_warningUnmatchedParams
                    , m_replaceExecTestCasesData.getOldSpecTestCase().getName()
                );
                Combo combo = DecoratedCombo.create(
                    parent, oldNames, message);
                combo.addSelectionListener(this);
                m_oldNameCombos.add(combo);
            }
        }
        updateAdditionalInformation();
    }

    /**
     * Set the additional information.
     */
    private void updateAdditionalInformation() {
        boolean hasUnmatchedTypes = m_oldNameCombos.contains(null);
        List<String> messages = new ArrayList<String>();
        if (hasUnmatchedTypes) {
            messages.add(Messages
                .ReplaceTCRWizard_matchParameterNames_warningNoSameTypeHint);
        }
        if (!m_replaceExecTestCasesData.hasMatchableParameters()) {
            messages.add(Messages
                .ReplaceTCRWizard_matchParameterNames_warningUnusedOldParams);
        }
        setAdditionalInformation(messages);
    }

    /**
     * @param text The label text to set.
     * @param parent The composite.
     * @return A new label with the given text added to the given parent.
     */
    private static Label createLabel(Composite parent, String text) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(text);
        return label;
    }

    /**
     * Show help contend attached to wizard after selecting the ? icon,
     * or pressing F1 on Windows / Shift+F1 on Linux / Help on MAC.
     * {@inheritDoc}
     */
    public void performHelp() {
        Plugin.getHelpSystem().displayHelp(ContextHelpIds
                .SEARCH_REFACTOR_REPLACE_EXECUTION_TEST_CASE_WIZARD);
    }

    /**
     * Sets the new data, when the selection of the combo boxes have been changed.
     * {@inheritDoc}
     */
    public void widgetSelected(SelectionEvent e) {
        if (m_oldNameCombos.get(0).getSelectionIndex() == 0) {
            List<String> messages = new ArrayList<String>();
            messages.add("Ohh"); //$NON-NLS-1$
            setAdditionalInformation(messages);
        } else {
            setAdditionalInformation(null);
        }
        m_replaceExecTestCasesData.setNewOldParameterNames(m_oldNameCombos);
    }

    /**
     * {@inheritDoc}
     */
    public void widgetDefaultSelected(SelectionEvent e) {
        // do nothing
    }

}
