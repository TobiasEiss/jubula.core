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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jubula.client.core.businessprocess.ComponentNamesBP;
import org.eclipse.jubula.client.core.businessprocess.IComponentNameMapper;
import org.eclipse.jubula.client.core.persistence.GeneralStorage;
import org.eclipse.jubula.client.core.utils.StringHelper;
import org.eclipse.jubula.client.ui.Plugin;
import org.eclipse.jubula.client.ui.constants.ContextHelpIds;
import org.eclipse.jubula.client.ui.constants.IconConstants;
import org.eclipse.jubula.client.ui.constants.Layout;
import org.eclipse.jubula.client.ui.model.GuiNode;
import org.eclipse.jubula.client.ui.model.SpecTestCaseGUI;
import org.eclipse.jubula.client.ui.widgets.CompNamePopUpTextField;
import org.eclipse.jubula.client.ui.widgets.DirectCombo;
import org.eclipse.jubula.client.ui.widgets.GDText;
import org.eclipse.jubula.toolkit.common.xml.businessprocess.ComponentBuilder;
import org.eclipse.jubula.tools.constants.StringConstants;
import org.eclipse.jubula.tools.exception.Assert;
import org.eclipse.jubula.tools.i18n.I18n;
import org.eclipse.jubula.tools.xml.businessmodell.Action;
import org.eclipse.jubula.tools.xml.businessmodell.CompSystem;
import org.eclipse.jubula.tools.xml.businessmodell.Component;
import org.eclipse.jubula.tools.xml.businessmodell.ConcreteComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


/**
 * @author BREDEX GmbH
 * @created 06.12.2004
 */
public class NewCAPDialog extends TitleAreaDialog {
    
    /** number of columns = 1 */
    private static final int NUM_COLUMNS_1 = 1;    
    /** number of columns = 4 */
    private static final int NUM_COLUMNS_4 = 4;    
    /** vertical spacing = 2 */
    private static final int VERTICAL_SPACING = 2;    
    /** margin width = 0 */
    private static final int MARGIN_WIDTH = 10;    
    /** margin height = 2 */
    private static final int MARGIN_HEIGHT = 10;
    /** width hint = 300 */
    private static final int WIDTH_HINT = 300;
    /** horizontal span = 3 */
    private static final int HORIZONTAL_SPAN = 3;
    /** the m_text field for the CapPO name */
    private GDText m_capNameField;
    /** the m_text field for the component name */
    private CompNamePopUpTextField m_componentNameField;
    /** the combo box for the components */
    private DirectCombo <String> m_componentCombo;
    /** the combo box for the actions */
    private DirectCombo <String> m_actionCombo;
    /** list of all default names */
    private Set <String>m_defaultNames = new HashSet<String>();
    /** The name of the cap */
    private String m_capName;
    /** The type of the component */
    private String m_componentType;
    /** The name of the component */
    private String m_componentName;
    /** The name of the action */
    private String m_actionName;
    /** The label of the actionCombo */
    private Label m_actionLabel;
    /** The label of the componentTextField */
    private Label m_compNameLabel;
    /** the SpecTestCaseGUI */
    private SpecTestCaseGUI m_specTCGui;
    /** the modifyListener */
    private final GuiDancerModifyListener m_modifyListener = 
        new GuiDancerModifyListener();

    /** the component mapper to use for finding and modifying components */
    private IComponentNameMapper m_compMapper;
    
    /**
     * The constructor.
     * @param parentShell the parent shell
     * @param specTCGui the SpecTestCaseGUI.
     * @param compMapper The Component Name mapper to use.
     */
    public NewCAPDialog(Shell parentShell, SpecTestCaseGUI specTCGui, 
            IComponentNameMapper compMapper) {
       
        super(parentShell);
        m_specTCGui = specTCGui;
        m_compMapper = compMapper;
    }

    /**
     * {@inheritDoc}
     */
    protected Control createDialogArea(Composite parent) {
        setTitle(I18n.getString("NewCapDialog.title")); //$NON-NLS-1$
        setMessage(I18n.getString("NewCapDialog.message")); //$NON-NLS-1$
        setTitleImage(IconConstants.NEW_CAP_DIALOG_IMAGE);
        getShell().setText(I18n.getString("NewCAPDialog.shellTitle")); //$NON-NLS-1$
//      new Composite as container
        final GridLayout gridLayoutParent = new GridLayout();
        gridLayoutParent.numColumns = NUM_COLUMNS_1;
        gridLayoutParent.verticalSpacing = VERTICAL_SPACING;
        gridLayoutParent.marginWidth = MARGIN_WIDTH;
        gridLayoutParent.marginHeight = MARGIN_HEIGHT;
        parent.setLayout(gridLayoutParent);
        Plugin.createSeparator(parent);
        Composite area = new Composite(parent, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = NUM_COLUMNS_4;
        area.setLayout(gridLayout);
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.widthHint = WIDTH_HINT;
        area.setLayoutData(gridData);
        createFields(area);
        Plugin.createSeparator(parent);
        String str = getNextChildrenName(m_specTCGui);
        m_capNameField.setText(str); 
        m_capNameField.selectAll();
        m_capNameField.addModifyListener(m_modifyListener);
        m_componentNameField.addModifyListener(m_modifyListener);
        Plugin.getHelpSystem().setHelp(parent, ContextHelpIds.CAP);
        setHelpAvailable(true);
        initDefaultNamesList();
        return area;
    }

    /**
     * initializes a list with all defaultNames
     */
    private void initDefaultNamesList() {
        m_defaultNames.clear();
        final List concreteComponents = ComponentBuilder.getInstance()
            .getCompSystem().getConcreteComponents();
        for (Object o : concreteComponents) {
            ConcreteComponent c = (ConcreteComponent)o;
            if (c.hasDefaultMapping()) {
                m_defaultNames.add(StringHelper.getInstance().get(
                    c.getDefaultMapping().getLogicalName(), true));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    protected Button createButton(Composite parent, int id, String label, 
        boolean defaultButton) {
        
        Button button = super.createButton(parent, id, label, defaultButton);
        getButton(IDialogConstants.OK_ID).setEnabled(false);
        return button;
    }

    /**
     *  This method is called, when the OK button or ENTER was pressed.
     */
    protected void okPressed() {
        m_capName = m_capNameField.getText();
        m_componentType = m_componentCombo.getSelectedObject();
        m_componentName = m_componentNameField.getText();
        m_actionName = m_actionCombo.getSelectedObject();
        setReturnCode(OK);
        close();
    }

    /**
     * {@inheritDoc}
     */
    public boolean close() {
        return super.close();
    }

    /**
     * @param area The composite.
     * creates the editor widgets
     */
    private void createFields(Composite area) {
        createCapNameField(area);
        createComponentCombo(area);
        createComponentName(area);
        createActionCombo(area);
    }
    
    /**
     * @param area The composite.
     * creates the m_text field to edit the CapPO name
     */
    private void createCapNameField(Composite area) {
        Label label = new Label(area, SWT.NONE);
        label.setText(I18n.getString("NewCAPDialog.capNameLabel")); //$NON-NLS-1$
        m_capNameField = new GDText(area, SWT.SINGLE | SWT.BORDER);
        GridData gridData = newGridData();
        Layout.addToolTipAndMaxWidth(gridData, m_capNameField);
        m_capNameField.setLayoutData(gridData);
        Layout.setMaxChar(m_capNameField);
    }
    
    /**
     * Creates a new GridData.
     * @return grid data
     */
    private GridData newGridData() {
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.horizontalSpan = HORIZONTAL_SPAN;
        return gridData;
    }

    /**
     * @param area The composite.
     * creates the combo box to choose the component
     */
    private void createComponentCombo(Composite area) {
        CompSystem compSystem = ComponentBuilder.getInstance().getCompSystem();
        Label label = new Label(area, SWT.NONE);
        label.setText(I18n.getString("NewCAPDialog.componentLabel")); //$NON-NLS-1$
        List <String> valueList = new ArrayList <String> ();
        List <String> displayList = new ArrayList <String> ();
        
        String[] toolkitComponents = compSystem.getComponentTypes(
            GeneralStorage.getInstance().getProject().getToolkit());
        for (String compType : toolkitComponents) {
            if (compSystem.findComponent(compType).isVisible()) {
                valueList.add(compType);
                displayList.add(StringHelper.getInstance().get(compType, true));
            }
        }        
        m_componentCombo = new DirectCombo<String>(area, 
                SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY, valueList, 
                displayList, true, true);
        GridData gridData = newGridData();
        Layout.addToolTipAndMaxWidth(gridData, m_componentCombo);
        m_componentCombo.setLayoutData(gridData);
        m_componentCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                componentSelection();
            }
        });
    }
    
    /**
     * @param area The composite.
     * creates the m_text field to edit the component name
     */
    private void createComponentName(Composite area) {
        m_compNameLabel = new Label(area, SWT.NONE);
        m_compNameLabel.setText(I18n.getString(
                "NewCAPDialog.componentNameLabel")); //$NON-NLS-1$
        m_compNameLabel.setEnabled(false);
        m_componentNameField = new CompNamePopUpTextField(
                m_compMapper, area,
                SWT.SINGLE | SWT.BORDER);
        m_componentNameField.setEnabled(false);
        GridData gridData = newGridData();
        Layout.addToolTipAndMaxWidth(gridData, m_componentNameField);
        m_componentNameField.setLayoutData(gridData);
        Layout.setMaxChar(m_componentNameField);
    }
    
    /**
     * @param area The composite.
     * creates the combo box to choose the action
     */
    private void createActionCombo(Composite area) {
        m_actionLabel = new Label(area, SWT.NONE);
        m_actionLabel.setText(I18n.getString("NewCAPDialog.actionLabel")); //$NON-NLS-1$
        m_actionLabel.setEnabled(false);
        m_actionCombo = new DirectCombo<String>(
            area, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY, 
            new ArrayList<String>(), new ArrayList<String>(), 
            false, false);
        m_actionCombo.setEnabled(false);
        GridData gridData = newGridData();
        Layout.addToolTipAndMaxWidth(gridData, m_actionCombo);
        m_actionCombo.setLayoutData(gridData);
    }
    
    /**
     * <p>fills in the actionCombo after selecting a component type</p>
     * <p>enables the actionCombo and the componentNameTextField</p>
     */
    private void componentSelection() {
        boolean defaultMappingComponent = false;
        String componentName = StringConstants.EMPTY;
        if (m_componentCombo.getSelectedObject() == null) {
            m_actionCombo.removeAll();
            return;
        }
        Map map = StringHelper.getInstance().getMap();
        Component component = ComponentBuilder.getInstance().getCompSystem()
            .findComponent(m_componentCombo.getSelectedObject());
        // If this is a concrete component with default object
        // mapping, the component name is fix and must not be
        // changed by the user. The component name is actually
        // the logical name of the default mapping.
        if (component.isConcrete()) {
            ConcreteComponent cc = (ConcreteComponent)component;
            if (cc.hasDefaultMapping()) {
                defaultMappingComponent = true;
                componentName = StringHelper.getInstance().get(
                    cc.getDefaultMapping().getLogicalName(), true);
            }
        }
        List<Action> actions = 
            new ArrayList<Action>(component.getActions().size());
        for (Object obj : component.getActions()) {
            Action action = (Action)obj;
            if (!action.isDeprecated()) {
                actions.add(action);
            }
        }
        
        final int actionSize = actions.size();
        String[] actionNamesSorted = new String[actionSize];
        Map <String, String> helpMap = new HashMap <String, String> ();
        for (int i = 0; i < actionSize; i++) {
            Action action = actions.get(i);
            if (!action.isDeprecated()) {
                String actionName = action.getName();
                actionNamesSorted[i] = map.get(actionName).toString();
                helpMap.put(actionNamesSorted[i], actionName);
            }
        }
        Arrays.sort(actionNamesSorted);
        List <String>actionComboObjList = new ArrayList<String>();
        for (String actionName : actionNamesSorted) {
            actionComboObjList.add(helpMap.get(actionName));
        }
        m_actionCombo.setItems(actionComboObjList, 
            Arrays.asList(actionNamesSorted));
        m_componentNameField.setText(componentName);
        m_compNameLabel.setEnabled(!defaultMappingComponent);
        m_componentNameField.setEnabled(!defaultMappingComponent);
        m_actionLabel.setEnabled(true);
        m_actionCombo.setEnabled(true);
        modifyComponentTypeAction();
        modifyComponentNameFieldAction();
    }
    
    /** 
     * the action of the cap name field
     * @param compNamesAreAlreadyCorrect true, if component names are already correct
     * @return false, if the cap name field contents an error:
     * the step name starts or end with a blank, or the field is empty
     */
    private boolean modifyCapNameFieldAction(
            boolean compNamesAreAlreadyCorrect) {
        
        boolean isCorrect = true;
        int capNameLength = m_capNameField.getText().length();
        if ((capNameLength == 0)
            || (m_capNameField.getText().startsWith(" "))  //$NON-NLS-1$
            || (m_capNameField.getText().charAt(capNameLength - 1) == ' ')) {
            
            isCorrect = false;
        }
        if (isCorrect) {
            if (!compNamesAreAlreadyCorrect) {
                modifyComponentTypeAction();
            } else {
                enableOKButton();
            }
            // ----------------------------
            // FIXME Andreas : Iteration over DB Names.
            // ----------------------------
            
        } else {
            getButton(IDialogConstants.OK_ID).setEnabled(false);
            if (capNameLength == 0) {
                setErrorMessage(I18n.getString("NewCAPDialog.emptyStep")); //$NON-NLS-1$); 
            } else {
                setErrorMessage(I18n.getString("NewCAPDialog.notValidStep")); //$NON-NLS-1$); 
            }
        }
        return isCorrect;
    }
    
    /** 
     * the action of the component type combobox
     * @return false, if the component type combobox is empty
     */
    private boolean modifyComponentTypeAction() {
        boolean isCorrect = true;
        if (m_componentCombo.getText().length() == 0) {
            isCorrect = false;
        }
        if (isCorrect) {
            m_componentNameField.setFilter(m_componentCombo
                .getSelectedObject());
            modifyComponentNameFieldAction();
        } else {
            if (getButton(IDialogConstants.OK_ID) != null) {
                getButton(IDialogConstants.OK_ID).setEnabled(false);
            }
            setErrorMessage(I18n.getString("NewCAPDialog.emptyCompType")); //$NON-NLS-1$); 
        }
        return isCorrect;
    }
    
    /** 
     * the action of the cap name field
     * @return false, if the cap name field contents an error:
     * the step name starts or end with a blank, or the field is empty
     */
    private boolean modifyComponentNameFieldAction() {        
        boolean isCorrect = true, defaultName = false;
        int componentNameLength = m_componentNameField.getText().length();
        if ((componentNameLength == 0)
            || (m_componentNameField.getText().startsWith(" ")) //$NON-NLS-1$ 
            || (m_componentNameField.getText().charAt(
                componentNameLength - 1) == ' ')) {
            
            isCorrect = false;
        }
        if (m_componentNameField.isEnabled() 
            && m_defaultNames.contains(m_componentNameField.getText())) {
            
            defaultName = true;
            isCorrect = false;
        }

        String compatibilityErrorMsg = 
            ComponentNamesBP.getInstance().isCompatible(
                m_componentCombo.getSelectedObject(),
                m_componentNameField.getText(), 
                m_compMapper, 
                GeneralStorage.getInstance().getProject().getId());
 
        isCorrect &= compatibilityErrorMsg == null;
        
        if (isCorrect) {
            modifyCapNameFieldAction(true);
            return isCorrect;
        }
        getButton(IDialogConstants.OK_ID).setEnabled(false);
        if (compatibilityErrorMsg != null) {
            setErrorMessage(compatibilityErrorMsg);
        } else if (componentNameLength == 0) {
            setErrorMessage(I18n.getString("NewCAPDialog.emptyCompName")); //$NON-NLS-1$)
        } else if (defaultName) {
            setErrorMessage(I18n.getString("NewCAPDialog.reservedCompName", //$NON-NLS-1$ 
                    new Object[] { m_componentNameField.getText() }));
        } else {
            setErrorMessage(I18n.getString("NewCAPDialog.notValidCompName")); //$NON-NLS-1$)  
        }

        if (isCorrect) {
            modifyCapNameFieldAction(true);
        }
        return isCorrect;
    }
    
    /**
     * enables the OK button and makes a non-error title message
     */
    private void enableOKButton() {
        if (getButton(IDialogConstants.OK_ID) != null) {
            getButton(IDialogConstants.OK_ID).setEnabled(true);
        }
        setErrorMessage(null);
    }
    
    /**
     * @return Returns the capNameFieldText.
     */
    public String getCapName() {
        return m_capName;
    }
    
    /**
     * @return Returns the componentComboText.
     */
    public String getComponentType() {
        return m_componentType;
    }
    
    /**
     * @return Returns the actionNameComboText.
     */
    public String getActionName() {
        return m_actionName;
    }
    
    /**
     * @return Returns the componentNameFieldText.
     */
    public String getComponentName() {
        return m_componentName;
    }
    
    /**
     * @param tc Parent TestCase
     * @return name for Next Cap
     */
    private String getNextChildrenName(SpecTestCaseGUI tc) {
        int index = tc.getChildren().size() + 1;
        boolean uniqueName = false;
        String capName = StringConstants.EMPTY;
        while (!uniqueName) {
            capName = I18n.getString("NewCAPDialog.newCap") + index; //$NON-NLS-1$
            uniqueName = true;
            for (GuiNode node : tc.getChildren()) {
                if (node.getName().equals(capName)) {
                    uniqueName = false;
                    index++;
                    break;
                }
            }
        }
        return capName;
    }  
    
    /**
     * This private inner class contains a new ModifyListener.
     * @author BREDEX GmbH
     * @created 15.07.2005
     */
    private class GuiDancerModifyListener implements ModifyListener {
        /**
         * {@inheritDoc}
         */
        public void modifyText(ModifyEvent e) {
            Object o = e.getSource();
            if (o.equals(m_capNameField)) {
                modifyCapNameFieldAction(false);
                return;
            } else if (o.equals(m_componentNameField)) {
                modifyComponentNameFieldAction();
                return;
            } 
            Assert.notReached("Event activated by unknown widget(" + o + ")."); //$NON-NLS-1$ //$NON-NLS-2$        
        }     
    }
}