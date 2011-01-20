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
package org.eclipse.jubula.client.ui.dialogs;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jubula.client.core.businessprocess.ComponentNamesBP;
import org.eclipse.jubula.client.core.model.IComponentNamePO;
import org.eclipse.jubula.client.core.persistence.GeneralStorage;
import org.eclipse.jubula.client.ui.Plugin;
import org.eclipse.jubula.client.ui.utils.Utils;
import org.eclipse.jubula.client.ui.widgets.JBText;
import org.eclipse.jubula.toolkit.common.xml.businessprocess.ComponentBuilder;
import org.eclipse.jubula.tools.constants.StringConstants;
import org.eclipse.jubula.tools.exception.JBException;
import org.eclipse.jubula.tools.i18n.CompSystemI18n;
import org.eclipse.jubula.tools.i18n.I18n;
import org.eclipse.jubula.tools.xml.businessmodell.CompSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


/**
 * @author BREDEX GmbH
 * @created Jun 5, 2008
 */
@SuppressWarnings("synthetic-access")
public class CompNameExistsDialog extends TitleAreaDialog {

    
    /** The IComponentNamePO to persist */
    private IComponentNamePO m_persCompName;
    
    /** The existing IComponentNamePO */
    private IComponentNamePO m_existingCompName;
    
    /** The new name of the IComponentNamePO to persist */
    private String m_newName = StringConstants.EMPTY;
    
    /** The Type Label */
    private Label m_typeLabel;
    
    /** The name Text-Field */
    private JBText m_nameField;

    /**
     * @param parentShell The parent shell.
     * @param persCompName The IComponentNamePO to persist.
     * @param existingCompName The existing IComponentNamePO.
     */
    public CompNameExistsDialog(Shell parentShell, 
            IComponentNamePO persCompName, 
            IComponentNamePO existingCompName) {
        
        super(parentShell);
        m_persCompName = persCompName;
        m_existingCompName = existingCompName;
        m_newName = persCompName.getName();
    }
    
    /**
     * {@inheritDoc}
     */
    protected Control createDialogArea(Composite parent) {
        super.createDialogArea(parent);
        
        final String dialogTitle = I18n.getString("CompNameExistsDialog.DialogTitle"); //$NON-NLS-1$
        setTitle(dialogTitle);
        setMessage(I18n.getString("CompNameExistsDialog.DefaultDlgMessage")); //$NON-NLS-1$
        
        getShell().setText(dialogTitle);
        
        final GridLayout gridLayoutParent = new GridLayout();
        gridLayoutParent.numColumns = 1;
        parent.setLayout(gridLayoutParent);
        Plugin.createSeparator(parent);
        final Composite area = new Composite(parent, SWT.NONE);
        final GridLayout areaLayout = new GridLayout();
        area.setLayout(areaLayout);
        final GridData areaGridData = new GridData();
        areaGridData.grabExcessVerticalSpace = true;
        areaGridData.horizontalAlignment = GridData.FILL;
        areaGridData.verticalAlignment = GridData.FILL;
        area.setLayoutData(areaGridData);
        
        try {
            createInput(area);
        } catch (JBException e) {
            Utils.createMessageDialog(e, null, null);
            cancelPressed();
        }
        
        return area;
    }
    
    
    /**
     * @param parent the parent
     */
    private void createInput(final Composite parent) throws JBException {
        final Composite nameFieldArea = new Composite(parent, SWT.NONE);
        final GridLayout areaLayout = new GridLayout(1, false);
        nameFieldArea.setLayout(areaLayout);
        final GridData tableAreaGridData = new GridData();
        tableAreaGridData.horizontalAlignment = GridData.FILL;
        nameFieldArea.setLayoutData(tableAreaGridData);
        final Label messageLabel = new Label(nameFieldArea, SWT.NONE);
        final String componentType = CompSystemI18n.getString(
                m_existingCompName.getComponentType());
        messageLabel.setText(I18n.getString("CompNameExistsDialog.CompAlreadyExistsInProj")); //$NON-NLS-1$
        
        m_typeLabel = new Label(nameFieldArea, SWT.NONE);
        m_typeLabel.setText(I18n.getString("CompNameExistsDialog.ComponentType") //$NON-NLS-1$
                + componentType); 
        
        final Label messageLabel2 = new Label(nameFieldArea, SWT.NONE);
        messageLabel2.setText(I18n.getString("CompNameExistsDialog.PressOkOrEnter")); //$NON-NLS-1$
        
        m_nameField = new JBText(nameFieldArea, SWT.SINGLE | SWT.BORDER);
        final GridData nameFieldGridData = new GridData();
        nameFieldGridData.grabExcessHorizontalSpace = true;
        nameFieldGridData.horizontalAlignment = GridData.FILL;
        m_nameField.setLayoutData(nameFieldGridData);
        m_nameField.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                m_newName = m_nameField.getText();
            }
        });
        m_nameField.setText(m_persCompName.getName());
        isNameOK();
        m_nameField.selectAll();
    }
    
    /**
     * {@inheritDoc}
     */
    protected Point getInitialSize() {
        return super.getInitialSize();
    }

    /**
     * @return the new entered name.
     */
    public String getNewName() {
        return m_newName;
    }
    
    /**
     * {@inheritDoc}
     */
    protected void okPressed() {
        try {
            if (isNameOK()) {
                super.okPressed();
            }
        } catch (JBException e) {
            Utils.createMessageDialog(e, null, null);
            cancelPressed();
        }
        return;
    }
    
    /**
     * @return Checks if the new entered name is OK.
     */
    private boolean isNameOK() throws JBException {
        if (StringConstants.EMPTY.equals(m_newName)) {
            setErrorMessage(I18n.getString("CompNameExistsDialog.TypeComponentName")); //$NON-NLS-1$
            return false;
        }
        final ComponentNamesBP compNameBP = ComponentNamesBP.getInstance();
        final String newNameGuid = compNameBP.getGuidForName(m_newName);
        if (newNameGuid == null) {
            return true;
        }
        final String projGuid = GeneralStorage.getInstance().getProject()
            .getGuid();
        final IComponentNamePO newCompNamePo = compNameBP.getCompNamePo(
                newNameGuid, projGuid);
        if (newCompNamePo == null) {
            return true;
        }
        final String persType = m_persCompName.getComponentType();
        final String newType = newCompNamePo.getComponentType();
        final CompSystem compSystem = ComponentBuilder.getInstance()
            .getCompSystem();

        if (compSystem.isRealizing(persType, newType)) {
            return true;
        }
        final String newTypeI18n = CompSystemI18n.getString(newType);

        setErrorMessage(I18n.getString("CompNameExistsDialog.CompNameNotCompatible")); //$NON-NLS-1$
        m_typeLabel.setText(I18n.getString("CompNameExistsDialog.ComponentType") //$NON-NLS-1$
                + newTypeI18n); 
        m_nameField.selectAll();
        return false;
    }
    
}
