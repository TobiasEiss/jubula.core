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
package org.eclipse.jubula.client.ui.rcp.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jubula.client.core.model.ICapPO;
import org.eclipse.jubula.client.core.model.ICategoryPO;
import org.eclipse.jubula.client.core.model.IComponentNamePO;
import org.eclipse.jubula.client.core.model.IEventExecTestCasePO;
import org.eclipse.jubula.client.core.model.IExecObjContPO;
import org.eclipse.jubula.client.core.model.IExecTestCasePO;
import org.eclipse.jubula.client.core.model.IObjectMappingAssoziationPO;
import org.eclipse.jubula.client.core.model.IProjectPO;
import org.eclipse.jubula.client.core.model.IRefTestSuitePO;
import org.eclipse.jubula.client.core.model.ISpecTestCasePO;
import org.eclipse.jubula.client.core.model.ITestJobPO;
import org.eclipse.jubula.client.core.model.ITestSuitePO;
import org.eclipse.jubula.client.core.persistence.GeneralStorage;
import org.eclipse.jubula.client.ui.rcp.controllers.propertysources.AutIdentifierPropertySource;
import org.eclipse.jubula.client.ui.rcp.controllers.propertysources.CapGUIPropertySource;
import org.eclipse.jubula.client.ui.rcp.controllers.propertysources.CategoryGUIPropertySource;
import org.eclipse.jubula.client.ui.rcp.controllers.propertysources.EventExecTestCaseGUIPropertySource;
import org.eclipse.jubula.client.ui.rcp.controllers.propertysources.ExecTestCaseGUIPropertySource;
import org.eclipse.jubula.client.ui.rcp.controllers.propertysources.OMLogicNameGUIPropertySource;
import org.eclipse.jubula.client.ui.rcp.controllers.propertysources.OMTechNameGUIPropertySource;
import org.eclipse.jubula.client.ui.rcp.controllers.propertysources.ProjectGUIPropertySource;
import org.eclipse.jubula.client.ui.rcp.controllers.propertysources.RefTestSuiteGUIPropertySource;
import org.eclipse.jubula.client.ui.rcp.controllers.propertysources.SpecTestCaseGUIPropertySource;
import org.eclipse.jubula.client.ui.rcp.controllers.propertysources.TestJobGUIPropertySource;
import org.eclipse.jubula.client.ui.rcp.controllers.propertysources.TestSuiteGUIPropertySource;
import org.eclipse.jubula.tools.registration.AutIdentifier;
import org.eclipse.ui.views.properties.IPropertySource;


/**
 * Provides property sources for Jubula model objects.
 *
 * @author BREDEX GmbH
 * @created Feb 20, 2009
 */
public class PropertySourceAdapterFactory implements IAdapterFactory {

    /** types for which adapters are available */
    private final Class[] m_types = {
        IComponentNamePO.class, IObjectMappingAssoziationPO.class,
        AutIdentifier.class, ITestSuitePO.class, ISpecTestCasePO.class,
        ICapPO.class, IEventExecTestCasePO.class, IExecTestCasePO.class,
        IExecObjContPO.class, IRefTestSuitePO.class, ITestJobPO.class,
        ICategoryPO.class
    };

    /**
     * {@inheritDoc}
     */
    public IPropertySource getAdapter(Object adaptableObject, 
            Class adapterType) {
        if (adapterType == IPropertySource.class) {
            if (adaptableObject instanceof IComponentNamePO) {
                return new OMLogicNameGUIPropertySource(
                        (IComponentNamePO)adaptableObject);
            } else if (adaptableObject instanceof IObjectMappingAssoziationPO) {
                return new OMTechNameGUIPropertySource(
                        (IObjectMappingAssoziationPO)adaptableObject);
            } else if (adaptableObject instanceof AutIdentifier) {
                return new AutIdentifierPropertySource(
                        (AutIdentifier)adaptableObject);
            } else if (adaptableObject instanceof ITestSuitePO) {
                return new TestSuiteGUIPropertySource(
                        (ITestSuitePO)adaptableObject);
            } else if (adaptableObject instanceof ISpecTestCasePO) {
                return new SpecTestCaseGUIPropertySource(
                        (ISpecTestCasePO)adaptableObject);
            } else if (adaptableObject instanceof ICapPO) {
                return new CapGUIPropertySource(
                        (ICapPO)adaptableObject);
            } else if (adaptableObject instanceof IEventExecTestCasePO) {
                return new EventExecTestCaseGUIPropertySource(
                        (IEventExecTestCasePO)adaptableObject);
            } else if (adaptableObject instanceof IExecTestCasePO) {
                return new ExecTestCaseGUIPropertySource(
                        (IExecTestCasePO)adaptableObject);
            } else if (adaptableObject instanceof IExecObjContPO) {
                IProjectPO project = GeneralStorage
                        .getInstance().getProject();
                return new ProjectGUIPropertySource(project);
            } else if (adaptableObject instanceof IRefTestSuitePO) {
                return new RefTestSuiteGUIPropertySource(
                        (IRefTestSuitePO)adaptableObject);
            } else if (adaptableObject instanceof ICategoryPO) {
                return new CategoryGUIPropertySource(
                        (ICategoryPO)adaptableObject);
            } else if (adaptableObject instanceof ITestJobPO) {
                return new TestJobGUIPropertySource(
                        (ITestJobPO)adaptableObject);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Class[] getAdapterList() {
        return m_types;
    }
}
