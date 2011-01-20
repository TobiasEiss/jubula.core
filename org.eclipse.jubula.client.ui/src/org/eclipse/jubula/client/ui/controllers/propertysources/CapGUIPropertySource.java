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
package org.eclipse.jubula.client.ui.controllers.propertysources;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jubula.client.core.businessprocess.CapBP;
import org.eclipse.jubula.client.core.businessprocess.ComponentNamesBP;
import org.eclipse.jubula.client.core.businessprocess.IComponentNameMapper;
import org.eclipse.jubula.client.core.businessprocess.IParamNameMapper;
import org.eclipse.jubula.client.core.businessprocess.IWritableComponentNameMapper;
import org.eclipse.jubula.client.core.businessprocess.ParamNameBP;
import org.eclipse.jubula.client.core.businessprocess.ComponentNamesBP.CompNameCreationContext;
import org.eclipse.jubula.client.core.events.DataEventDispatcher;
import org.eclipse.jubula.client.core.model.ICapPO;
import org.eclipse.jubula.client.core.model.IParamDescriptionPO;
import org.eclipse.jubula.client.core.model.IParamNodePO;
import org.eclipse.jubula.client.core.model.ISpecTestCasePO;
import org.eclipse.jubula.client.core.persistence.GeneralStorage;
import org.eclipse.jubula.client.core.persistence.IncompatibleTypeException;
import org.eclipse.jubula.client.core.persistence.NodePM;
import org.eclipse.jubula.client.core.persistence.PMException;
import org.eclipse.jubula.client.core.utils.GuiParamValueConverter;
import org.eclipse.jubula.client.core.utils.NullValidator;
import org.eclipse.jubula.client.core.utils.StringHelper;
import org.eclipse.jubula.client.ui.Plugin;
import org.eclipse.jubula.client.ui.businessprocess.WorkingLanguageBP;
import org.eclipse.jubula.client.ui.controllers.PMExceptionHandler;
import org.eclipse.jubula.client.ui.controllers.propertydescriptors.PopupCompNameTextPropertyDescriptor;
import org.eclipse.jubula.client.ui.controllers.propertydescriptors.JBPropertyDescriptor;
import org.eclipse.jubula.client.ui.editors.IJBEditor;
import org.eclipse.jubula.client.ui.factory.TestDataControlFactory;
import org.eclipse.jubula.client.ui.model.CapGUI;
import org.eclipse.jubula.client.ui.provider.labelprovider.DisabledLabelProvider;
import org.eclipse.jubula.client.ui.provider.labelprovider.ParameterValueLabelProvider;
import org.eclipse.jubula.client.ui.utils.Utils;
import org.eclipse.jubula.toolkit.common.xml.businessprocess.ComponentBuilder;
import org.eclipse.jubula.tools.constants.StringConstants;
import org.eclipse.jubula.tools.exception.Assert;
import org.eclipse.jubula.tools.i18n.I18n;
import org.eclipse.jubula.tools.xml.businessmodell.Action;
import org.eclipse.jubula.tools.xml.businessmodell.CompSystem;
import org.eclipse.jubula.tools.xml.businessmodell.Component;
import org.eclipse.jubula.tools.xml.businessmodell.ConcreteComponent;
import org.eclipse.jubula.tools.xml.businessmodell.Param;
import org.eclipse.jubula.tools.xml.businessmodell.ValueSetElement;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;


/**
 * This class is the PropertySource of a CAP.
 * Its used to display and edit the properties in the Properties View.
 * 
 *
 * @author BREDEX GmbH
 * @created 01.12.2004
 * {@inheritDoc}
 */
@SuppressWarnings("synthetic-access")
public class CapGUIPropertySource extends AbstractGuiNodePropertySource  {
    /** Property m_text on display */
    public static final String P_ELEMENT_DISPLAY_COMPNAME = 
        I18n.getString("CapGUIPropertySource.ComponentName"); //$NON-NLS-1$

    /** Property m_text on display */
    public static final String P_ELEMENT_DISPLAY_COMPTYPE = 
                                I18n.getString("CapGUIPropertySource.CompType");  //$NON-NLS-1$

    /** Property m_text on display */
    public static final String P_ELEMENT_DISPLAY_STEPNAME = 
                                I18n.getString("CapGUIPropertySource.StepName");  //$NON-NLS-1$
    
    /** Property ID for Parameters*/ 
    public static final String P_ELEMENT_PARAMS = "Parameters"; //$NON-NLS-1$
    
    /** Property m_text on display */
    public static final String P_ELEMENT_DISPLAY_PARAMS = 
                                I18n.getString("CapGUIPropertySource.Params");  //$NON-NLS-1$

    /** Property m_text on display */
    public static final String P_ELEMENT_DISPLAY_ACTIONTYPE = 
                            I18n.getString("CapGUIPropertySource.ActionType");  //$NON-NLS-1$
  
    /** Property m_text on display */
    public static final String P_ELEMENT_DISPLAY_PARAMETERNAME = 
                            I18n.getString("CapGUIPropertySource.ParamName");  //$NON-NLS-1$
    
    /** Property m_text on display */
    public static final String P_ELEMENT_DISPLAY_PARAMETERTYPE = 
                I18n.getString("CapGUIPropertySource.ParamType");  //$NON-NLS-1$
    
    /** Property m_text on display */
    public static final String P_ELEMENT_DISPLAY_PARAMETERVALUE = 
                            I18n.getString("CapGUIPropertySource.ParamValue");  //$NON-NLS-1$
      
    /** Constant for Category Component */
    public static final String P_COMPONENT_CAT = 
            I18n.getString("CapGUIPropertySource.Component");  //$NON-NLS-1$
    
    /** Constant for Category Action */
    public static final String P_ACTION_CAT = 
                I18n.getString("CapGUIPropertySource.Action");  //$NON-NLS-1$
    
    /** Constant for Category Parameter */
    public static final String P_PARAMETER_CAT = 
                I18n.getString("CapGUIPropertySource.Parameter");  //$NON-NLS-1$

    /** for LOG messages */
    static final Log LOG = 
        LogFactory.getLog(CapGUIPropertySource.class);
    
    /** List of component types (short names)*/
    private static String[] componentTypes;
    
    /** List of component types (long names)*/
    private static String[] componentTypesSubList;
    
    /** List of parameter names depending on the choosed action */
    private String[] m_parameterNames;
    
    /** List of parameter types depending on the choosed action (short names) */
    private String[] m_parameterTypes;
    
    /** List of actions depending on choosed component (short name)*/
    private String[] m_actionTypes;
    /** List of actions depending on choosed component (long name)*/
    private String[] m_actionTypesSubList;
    
    /** Controller for action type */
    private final ActionTypeController m_actionTypeController = 
        new ActionTypeController();
    
    /** List of parameter name controller */
    private List <ParameterNameController> m_paramNameControllerList = 
        new ArrayList <ParameterNameController> ();
    
    /** List of parameter type controller (short names)*/
    private List <ParameterTypeController> m_paramTypeControllerList = 
        new ArrayList <ParameterTypeController> ();
    
    /** List of parameter type controller (short names)*/
    private List <ParameterValueController> m_paramValueControllerList = 
        new ArrayList <ParameterValueController> ();

    /** cached property descriptor for name */
    private IPropertyDescriptor m_namePropDesc = null;

    /** cached property descriptor for component type */
    private IPropertyDescriptor m_compTypePropDesc = null;

    /** cached property descriptor for component name */
    private IPropertyDescriptor m_compNamePropDesc = null;

    /** cached property descriptor for action type */
    private IPropertyDescriptor m_actionPropDesc = null;

    /** cached property descriptors for parameters */
    private List<IPropertyDescriptor> m_initializedParamDescriptors = 
        new ArrayList<IPropertyDescriptor>();

    /**
     * Constructor 
     * @param cap a cap-reference
     */
    public CapGUIPropertySource(CapGUI cap) {
        
        super(cap);
        initCompTypes();
        initActionTypes();
        initParameters();
        initPropDescriptor();
    }
    
    /**
     * Inits the List of parameters.
     */
    private void initParameters() {
        Map<String, String> map = StringHelper.getInstance().getMap();
        List <String> paramNameList = new ArrayList <String> ();
        List <String> paramTypeList = new ArrayList <String> ();
        List <String> paramTypeSubList = new ArrayList <String> ();
        
        final ICapPO cap = (ICapPO) getPoNode();
        final Action action = CapBP.getAction(cap);
        for (String paramName : action.getParamNames()) {
            paramNameList.add(map.get(paramName));
            final Param parameter = action.findParam(paramName);
            final String paramType = parameter.getType();
            paramTypeList.add(map.get(paramType));
            paramTypeSubList.add(paramType);
        }     
        m_parameterNames = paramNameList.toArray(
            new String[paramNameList.size()]);
        m_parameterTypes = paramTypeList.toArray(
            new String[paramTypeList.size()]);
    }
    
    /**
     * Inits the List of component types.
     */
    private void initCompTypes() {
        List < String > tmpList = new ArrayList < String > ();
        List < String > tmpSubList = new ArrayList < String > ();
        Map<String, String> map = StringHelper.getInstance().getMap();
        Map<String, String> helpMap = new HashMap <String, String> ();
        
        final CompSystem compSystem = ComponentBuilder.getInstance()
            .getCompSystem();
        final String[] toolkitComponents = compSystem.getComponentTypes(
            GeneralStorage.getInstance().getProject().getToolkit());
        for (String currComponent : toolkitComponents) {
            final String compType = map.get(currComponent);
            if (compSystem.findComponent(currComponent).isVisible()) {
                tmpList.add(compType);
                helpMap.put(compType, currComponent);
            }
        }        
        componentTypes = tmpList.toArray(new String[tmpList.size()]);
        Arrays.sort(componentTypes);
        for (String compType : componentTypes) {
            tmpSubList.add(helpMap.get(compType));        
        }
        componentTypesSubList = tmpSubList.toArray(
            new String[tmpSubList.size()]);
    }
    
    /**
     * Inits the List of action types depending on the selected component type.
     */
    @SuppressWarnings("unchecked")
    private void initActionTypes() {
        List <String> tmpList = new ArrayList <String> ();
        List <String> tmpSubList = new ArrayList <String> ();
        Map map = StringHelper.getInstance().getMap();
        Map <String, String> helpMap = new HashMap <String, String> ();
        CompSystem compSystem = ComponentBuilder.getInstance().getCompSystem();
        String compType = ((ICapPO)getPoNode()).getComponentType();
        final Component component = compSystem.findComponent(compType);
        final List<Action> actions = component.getActions();
        for (Action action : actions) {
            // insert deprecated actions, too
            // because maybe you can find deprecated actions in older projects
            String actionName = String.valueOf(map.get(action.getName()));
            tmpList.add(actionName);
            helpMap.put(actionName, action.getName());
        }
        m_actionTypes = tmpList.toArray(new String[tmpList.size()]);
        Arrays.sort(m_actionTypes);
        for (String action : m_actionTypes) {
            tmpSubList.add(helpMap.get(action));            
        }
        m_actionTypesSubList = tmpSubList.toArray(
            new String[tmpSubList.size()]);
    }
    
    /**
     * @return the action property descriptor.
     */
    private IPropertyDescriptor getActionPropDesc() {
        if (m_actionPropDesc == null) {
            ComboBoxPropertyDescriptor cbpd = new ComboBoxPropertyDescriptor(
                    m_actionTypeController,
                    P_ELEMENT_DISPLAY_ACTIONTYPE, m_actionTypes);
            cbpd.setLabelProvider(new LabelProvider() {
                public String getText(Object element) {
                    if (element instanceof Integer) {
                        return m_actionTypes[((Integer)element).intValue()];
                    }
                    Assert.notReached("Wrong element type."); //$NON-NLS-1$
                    return String.valueOf(element);
                }
            });
            cbpd.setCategory(P_ACTION_CAT);
            m_actionPropDesc = cbpd;
        }
        return m_actionPropDesc;
    }
    
    /**
     * @return the component name property descriptor. The m_text field is
     * not editable if the component has a default mapping.
     */
    private IPropertyDescriptor getCompNamePropDesc() {
        if (m_compNamePropDesc == null) {
            PropertyDescriptor pd = null;
            final ICapPO capPO = (ICapPO)getPoNode();
            final Component comp = 
                ComponentBuilder.getInstance().getCompSystem()
                .findComponent(capPO.getComponentType());
            if (comp.isConcrete()) {
                final ConcreteComponent concrete = (ConcreteComponent)comp;
                if (concrete.hasDefaultMapping()) {
                    ComponentNameController cnc = new ComponentNameController();
                    final String logicalName = StringHelper.getInstance().get(
                        concrete.getDefaultMapping().getLogicalName(), true);

                    final IEditorPart activeEditor = Plugin.getActiveEditor();
                    // FIXME zeb This looks like a workaround for something
                    // unnecessary. We should see if we can handle this default
                    // mapping situation in a more elegant way.
                    if (activeEditor instanceof IJBEditor
                            && ((IJBEditor)activeEditor).getEditorHelper()
                                .getEditSupport().getWorkVersion() 
                                    == capPO.getParentNode()) {
                        IWritableComponentNameMapper mapper = 
                            ((IJBEditor)activeEditor).getEditorHelper()
                                .getEditSupport().getCompMapper();
                        try {
                            ComponentNamesBP.getInstance().setCompName(capPO, 
                                    logicalName, CompNameCreationContext.STEP, 
                                    mapper);
                            cnc.setProperty(logicalName);
                            pd = new JBPropertyDescriptor(
                                    cnc, P_ELEMENT_DISPLAY_COMPNAME);
                        } catch (IncompatibleTypeException e) {
                            Utils.createMessageDialog(
                                    e, e.getErrorMessageParams(), null);
                        } catch (PMException pme) {
                            PMExceptionHandler.handlePMExceptionForEditor(
                                    pme, (IJBEditor)activeEditor);
                        }
                    }
                } 
            }
            if (pd == null) {
                pd = new PopupCompNameTextPropertyDescriptor(
                    new ComponentNameController(), P_ELEMENT_DISPLAY_COMPNAME, 
                    capPO.getComponentType());
            }
            pd.setCategory(P_COMPONENT_CAT);
            m_compNamePropDesc = pd;
        }
        
        return m_compNamePropDesc;
    }
    
    /**
     * @return the component type property descriptor.
     */
    private IPropertyDescriptor getCompTypePropDesc() {
        if (m_compTypePropDesc == null) {
            ComboBoxPropertyDescriptor cbpd = new ComboBoxPropertyDescriptor(
                    new ComponentTypeController(), P_ELEMENT_DISPLAY_COMPTYPE,
                        componentTypes);
            cbpd.setLabelProvider(new LabelProvider() {
                    public String getText(Object element) {
                        if (element instanceof Integer) {
                            final Integer index = (Integer)element;
                            return index < componentTypes.length 
                                ? componentTypes[index] : StringConstants.EMPTY;
                        }
                        Assert.notReached("Wrong element type."); //$NON-NLS-1$
                        return String.valueOf(element);
                    }
                });
            cbpd.setCategory(P_COMPONENT_CAT);
            m_compTypePropDesc = cbpd;
        }

        return m_compTypePropDesc;
    }
    
    /**
     * @return the test step name property descriptor.
     */
    private IPropertyDescriptor getStepNamePropDesc() {
        if (m_namePropDesc == null) {
            m_namePropDesc = 
                new TextPropertyDescriptor(new ElementNameController(),
                    P_ELEMENT_DISPLAY_STEPNAME);
        }
        return m_namePropDesc;
    }
    
    /**
     * Inits the PropertyDescriptors
     */
    protected void initPropDescriptor() {
        Map<String, String> map = StringHelper.getInstance().getMap();
        clearPropertyDescriptors();
        // Step Name
        addPropertyDescriptor(getStepNamePropDesc());
        // comment
        super.initPropDescriptor();
        // Component Type
        addPropertyDescriptor(getCompTypePropDesc());
        // Component Name
        addPropertyDescriptor(getCompNamePropDesc());
        // Action Type
        addPropertyDescriptor(getActionPropDesc());
        // Parameters
        if (m_initializedParamDescriptors.isEmpty()) {
            clearControllerLists();
            IParamNameMapper activeParamNameMapper = getActiveParamNameMapper();
            IParamNodePO paramNode = getParamNode();
            final int parameterListSize = paramNode.getParameterList().size();
            for (int i = 0; i < parameterListSize; i++) {
                IParamDescriptionPO desc = paramNode.getParameterList().get(i);
                // Parameter Name
                ParameterNameController pnc = new ParameterNameController(i, 
                        desc.getUniqueId());
                m_paramNameControllerList.add(pnc);
                JBPropertyDescriptor propDes = new JBPropertyDescriptor(
                    pnc, P_ELEMENT_DISPLAY_PARAMETERNAME);
                propDes.setCategory(P_PARAMETER_CAT);
                propDes.setLabelProvider(new DisabledLabelProvider());
                m_initializedParamDescriptors.add(propDes);
                
                // Parameter Type
                ParameterTypeController ptc = new ParameterTypeController(i,
                        desc.getUniqueId());
                m_paramTypeControllerList.add(ptc);
                propDes = new JBPropertyDescriptor(
                    ptc, P_ELEMENT_DISPLAY_PARAMETERTYPE);
                propDes.setCategory(P_PARAMETER_CAT);
                propDes.setLabelProvider(new DisabledLabelProvider());
                m_initializedParamDescriptors.add(propDes);
                
                // Parameter Value
                ParameterValueController paramCtrl = 
                    new ParameterValueController(this, desc, 
                            activeParamNameMapper);
                m_paramValueControllerList.add(paramCtrl);
                Action action = CapBP.getAction((ICapPO)getPoNode());
                final List<String> values = new ArrayList<String>();
                Param param = action.findParam(desc.getUniqueId());
                for (Iterator iter = param.valueSetIterator(); 
                        iter.hasNext();) {
                    values.add(map.get(((ValueSetElement)iter.next())
                            .getValue()));
                }
                final String[] valArray = 
                    values.toArray(new String[values.size()]);
                PropertyDescriptor descr = 
                    TestDataControlFactory.createValuePropertyDescriptor(
                            paramCtrl, P_ELEMENT_DISPLAY_PARAMETERVALUE, 
                            valArray, param.getValueSet().isCombinable());
                descr.setLabelProvider(
                        new ParameterValueLabelProvider(INCOMPL_DATA_IMAGE));
                descr.setCategory(P_PARAMETER_CAT);                
                m_initializedParamDescriptors.add(descr);
                
                // empty line
                propDes = new JBPropertyDescriptor(new DummyController(),
                    StringConstants.EMPTY);
                propDes.setCategory(P_PARAMETER_CAT);
                m_initializedParamDescriptors.add(propDes);
            }
        }
        addPropertyDescriptor(m_initializedParamDescriptors);
    
    }
    
    /**
     * clears the controller lists. 
     */
    private void clearControllerLists() {
        m_paramNameControllerList.clear();
        m_paramTypeControllerList.clear();
    }
    
    /**
     * Updates the Model and the ComboBoxItems.
     */
    void updateModel() {
        m_compNamePropDesc = null;
        m_actionPropDesc = null;
        initActionTypes();
        m_actionTypeController.setProperty(Integer.valueOf(0));
        updateParameters();
    }
    
    /**
     * Updates the Parameters depending on the choosed action
     */
    void updateParameters() {
        m_initializedParamDescriptors.clear();
        initParameters();
        final int paramNameControllerListSize = 
            m_paramNameControllerList.size();
        for (int i = 0; i < paramNameControllerListSize; i++) {
            final ParameterNameController paramNameController = 
                m_paramNameControllerList.get(i);
            if (m_parameterNames == null || m_parameterNames.length == 0) {
                paramNameController.setProperty(StringConstants.EMPTY);
            } else {
                paramNameController.setProperty(m_parameterNames[0]);
            }
            final ParameterTypeController paramTypeController = 
                m_paramTypeControllerList.get(i);
            if (m_parameterTypes == null || m_parameterTypes.length == 0) {
                paramTypeController.setProperty(StringConstants.EMPTY);
            } else {
                paramTypeController.setProperty(m_parameterTypes[0]);
            }
        }
        if (getParamNode().getDataManager().getDataSets().size() > 1) {
            getParamNode().getDataManager().removeDataSet(0);
        }
    }
    
   

    /**
     * Class to control component type.
     * @author BREDEX GmbH
     * @created 06.01.2005
     */
    public class ComponentTypeController extends AbstractPropertyController {
        
        /**
         * {@inheritDoc}
         */
        public boolean setProperty(Object value) {
            if (value == null) {
                return false;
            }
            final int index = (Integer)value;
            if (index >= componentTypesSubList.length) {
                return false;
            }
            final ICapPO capNode = (ICapPO)getPoNode();
            capNode.clearTestData();
            final String typeName = componentTypesSubList[index];
            capNode.setComponentType(typeName);
            capNode.setComponentName(StringConstants.EMPTY);
            updateModel();
            return true;
        }
        
        /**
         * {@inheritDoc}
         */
        public Object getProperty() {
            return getIndexOfType(); 
        }       
        
        /**
         * {@inheritDoc}
         */
        public Image getImage() {
            final ICapPO capPO = (ICapPO)getPoNode();
            if (capPO.getMetaComponentType().isDeprecated()) {
                return DEPRECATED_IMAGE;
            }
            return DEFAULT_IMAGE;
        }
        
        
        /**
         * Returns the index of the type from the String-Array for the 
         * ComboBoxPropertyDescriptor.
         * @return an <code>Integer</code> value. The index.
         */
        private Integer getIndexOfType() {
            Map<String, String> map = StringHelper.getInstance().getMap();
            final ICapPO cap = (ICapPO) getPoNode();
            if (map.containsKey(cap.getComponentType())) {
                final String type = map.get(cap.getComponentType());
                final int componentTypesLength = componentTypes.length;
                for (int i = 0; i < componentTypesLength; i++) {
                    if (componentTypes[i].equals(type)) {
                        return Integer.valueOf(i);
                    }
                }
            }
            return Integer.valueOf(0);
        }
    }
    
    /**
     * Class to control component name.
     * @author BREDEX GmbH
     * @created 07.01.2005
     */
    public class ComponentNameController extends AbstractPropertyController {
        /**
         * {@inheritDoc}
         */
        public boolean setProperty(Object value) {
            
            boolean isCompNameChangedAndSpecTCReused = false;
            final IWritableComponentNameMapper compMapper = 
                getActiveComponentNameMapper();
            Validate.notNull(compMapper);
            final ICapPO cap = (ICapPO) getPoNode();
            try {
                ComponentNamesBP.getInstance().setCompName(cap, 
                        String.valueOf(value), 
                        CompNameCreationContext.STEP, compMapper);
                ISpecTestCasePO specTc = 
                    (ISpecTestCasePO)getPoNode().getParentNode();
                if (!NodePM.getInternalExecTestCases(specTc.getGuid(), 
                        specTc.getParentProjectId()).isEmpty()) {
                    
                    isCompNameChangedAndSpecTCReused = true;
                }
                DataEventDispatcher.getInstance().firePropertyChanged(
                        isCompNameChangedAndSpecTCReused);
                return true;
            } catch (IncompatibleTypeException ite) {
                Utils.createMessageDialog(ite, ite.getErrorMessageParams(),
                        null);
            } catch (PMException e) {
                IEditorPart activeEditor = Plugin.getActiveEditor();
                if (activeEditor instanceof IJBEditor) {
                    PMExceptionHandler.handlePMExceptionForEditor(
                            e, (IJBEditor)activeEditor);
                } else {
                    PMExceptionHandler.handlePMExceptionForMasterSession(e);
                }
            }
            
            return false;
        }
        
        /**
         * {@inheritDoc}
         */
        public Object getProperty() {
            final ICapPO cap = (ICapPO) getPoNode();
            IComponentNameMapper compMapper = 
                Plugin.getActiveCompMapper();
            final String componentNameGuid = cap.getComponentName();
            if (componentNameGuid != null) {
                if (compMapper != null) {
                    return compMapper.getCompNameCache().getName(
                            componentNameGuid);
                }
                return ComponentNamesBP.getInstance().getName(
                        componentNameGuid);
            }
            return StringConstants.EMPTY;
        }  
    }
    
    /**
     * Class to control action type.
     * @author BREDEX GmbH
     * @created 07.01.2005
     */
    public class ActionTypeController extends AbstractPropertyController {
        
        /**
         * {@inheritDoc}
         */
        public boolean setProperty(Object value) {
            final ICapPO cap = (ICapPO) getPoNode();
            cap.clearTestData();
            cap.setActionName(m_actionTypesSubList[(
                    (Integer)value).intValue()]);
            setDefaultValues(cap);
            DataEventDispatcher.getInstance().firePropertyChanged(false);
            updateParameters();
            return true;
        }
        
        /**
         * {@inheritDoc}
         */
        public Object getProperty() {
            Map<String, String> map = StringHelper.getInstance().getMap();
            ICapPO cap = (ICapPO) getPoNode();
            String action = String.valueOf(map.get(cap.getActionName()));
            final int actionTypesLength = m_actionTypes.length;
            for (int i = 0; i < actionTypesLength; i++) {
                if (m_actionTypes[i].equals(action)) {
                    return Integer.valueOf(i);
                }
            }
            return Integer.valueOf(0);
        }
        
        /**
         * {@inheritDoc}
         */
        public Image getImage() {
            ICapPO capPO = (ICapPO)getPoNode();
            if (capPO.getMetaAction().isDeprecated()) {
                return DEPRECATED_IMAGE;
            }
            return DEFAULT_IMAGE;
        }
        
        /**
         * Sets the default values to the parameters.
         * @param cap The actual param node
         */
        private void setDefaultValues(ICapPO cap) {           
            for (Object o : cap.getMetaAction().getParams()) {
                final Param param = (Param)o;
                if (param.getDefaultValue() != null) {
                    final IParamDescriptionPO desc = 
                        cap.getParameterForUniqueId(param.getName());
                    final Locale locale = WorkingLanguageBP.getInstance().
                        getWorkingLanguage();
                    GuiParamValueConverter conv = new GuiParamValueConverter(
                            param.getDefaultValue(), cap, locale, desc, 
                            new NullValidator());
                    // default values have no influence to parameter of parent node
                    getTestCaseParamBP().startParameterUpdate(conv, locale, 
                        0, ParamNameBP.getInstance());
                }
            }
        }

      
    }
     
    /**
     * Class to control parameter name.
     * @author BREDEX GmbH
     * @created 07.01.2005
     */
    public class ParameterNameController extends AbstractPropertyController {
        
        /** the id of this controller*/
        private final int m_id;
        /** the paramName of the compSystem */
        private String m_name;
        
        /**
         * @param id the id of this controller
         * @param name paramName of the compSystem (paramDescription)
         */
        public ParameterNameController(int id, String name) {
            m_id = id;
            m_name = name;
        }

        /**
         * {@inheritDoc}
         */
        public boolean setProperty(Object value) {
            // parameter names cannot be set manually
            return true;
        }
        
        /**
         * {@inheritDoc}
         */
        public Object getProperty() {
            List<IParamDescriptionPO>  paramList = 
                getParamNode().getParameterList();
            if (paramList.size() > m_id) {
                return paramList.get(m_id).getName();
            }
            return StringConstants.EMPTY;
        } 
        
        /**
         * {@inheritDoc}
         */
        public Image getImage() {
            return DEFAULT_IMAGE;
        }

        /**
         * @return the paramName
         */
        public String getName() {
            return m_name;
        }
    }
    
    /**
     * Class to control parameter value
     * @author BREDEX GmbH
     * @created 07.01.2005
     */
    public class ParameterValueController extends
        AbstractParamValueController {
        
        /**
         * Constructor.
         * @param desc The parameter description
         * @param s AbstractGuiNodePropertySource
         * @param paramNameMapper the param name mapper
         */
        public ParameterValueController(AbstractGuiNodePropertySource s,
            IParamDescriptionPO desc, IParamNameMapper paramNameMapper) {
            super(s, desc, paramNameMapper);
        }
        
    }
        
    /**
     * Class to control parameter type.
     * @author BREDEX GmbH
     * @created 07.01.2005
     */
    public class ParameterTypeController extends AbstractPropertyController {
        
        /** the id of this controller*/
        private final int m_id;
        /** the paramName of the compSystem */
        private String m_name;

        /**
         * @param id the id for this controller.
         * @param name paramName of the compSystem (paramDescription)
         */
        public ParameterTypeController(int id, String name) {
            m_id = id;
            m_name = name;
        }
        
        /**
         * @return the paramName
         */
        public String getName() {
            return m_name;
        }

        /**
         * {@inheritDoc}
         */
        public boolean setProperty(Object value) {
            // do nothing
            return true;
        }

        /**
         * {@inheritDoc}
         */
        public Object getProperty() {
            Map<String, String> map = StringHelper.getInstance().getMap();
            List<IParamDescriptionPO> paramList = getParamNode()
                .getParameterList();
            if (paramList != null && !(paramList.isEmpty())
                && !StringConstants.EMPTY.equals((paramList.get(m_id))
                        .getType())) {
                
                return map.get((paramList.get(m_id)).getType());
            }
            return StringConstants.EMPTY;
        }
        
        
        /**
         * {@inheritDoc}
         */
        public Image getImage() {
            return DEFAULT_IMAGE;
        }
    }
    
    /**
     * @return the paramNode
     */
    IParamNodePO getParamNode() {
        return (IParamNodePO)getPoNode();
    }

    /**
     * @return Returns the actionTypeController.
     */
    public ActionTypeController getActionTypeController() {
        return m_actionTypeController;
    }
}