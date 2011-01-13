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
package org.eclipse.jubula.client.ui.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.jubula.client.archive.XmlStorage;
import org.eclipse.jubula.client.core.businessprocess.ComponentNamesDecorator;
import org.eclipse.jubula.client.core.businessprocess.INameMapper;
import org.eclipse.jubula.client.core.businessprocess.IWritableComponentNameCache;
import org.eclipse.jubula.client.core.businessprocess.IWritableComponentNameMapper;
import org.eclipse.jubula.client.core.businessprocess.ParamNameBP;
import org.eclipse.jubula.client.core.businessprocess.ParamNameBPDecorator;
import org.eclipse.jubula.client.core.businessprocess.ProjectComponentNameMapper;
import org.eclipse.jubula.client.core.businessprocess.ProjectNameBP;
import org.eclipse.jubula.client.core.events.DataEventDispatcher;
import org.eclipse.jubula.client.core.events.DataEventDispatcher.DataState;
import org.eclipse.jubula.client.core.events.DataEventDispatcher.UpdateState;
import org.eclipse.jubula.client.core.model.IProjectPO;
import org.eclipse.jubula.client.core.persistence.GeneralStorage;
import org.eclipse.jubula.client.core.persistence.NodePM;
import org.eclipse.jubula.client.core.persistence.PMException;
import org.eclipse.jubula.client.core.persistence.PMSaveException;
import org.eclipse.jubula.client.core.persistence.ProjectPM;
import org.eclipse.jubula.client.ui.Plugin;
import org.eclipse.jubula.client.ui.businessprocess.AbstractActionBP;
import org.eclipse.jubula.client.ui.businessprocess.SaveProjectAsBP;
import org.eclipse.jubula.client.ui.constants.ContextHelpIds;
import org.eclipse.jubula.client.ui.constants.IconConstants;
import org.eclipse.jubula.client.ui.controllers.PMExceptionHandler;
import org.eclipse.jubula.client.ui.dialogs.InputDialog;
import org.eclipse.jubula.client.ui.utils.DialogUtils;
import org.eclipse.jubula.client.ui.utils.Utils;
import org.eclipse.jubula.tools.exception.Assert;
import org.eclipse.jubula.tools.exception.ConverterException;
import org.eclipse.jubula.tools.exception.GDProjectDeletedException;
import org.eclipse.jubula.tools.exception.GDVersionException;
import org.eclipse.jubula.tools.i18n.I18n;
import org.eclipse.jubula.tools.jarutils.IGdVersion;
import org.eclipse.jubula.tools.messagehandling.MessageIDs;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.PlatformUI;


/**
 * @author BREDEX GmbH
 * @created 11.05.2005
 */
@SuppressWarnings("synthetic-access")
public class SaveProjectAsAction extends AbstractAction {
    
    /** standard logging */
    private static Log log = LogFactory.getLog(SaveProjectAsAction.class);
    
    /**
     * Worker operation for SaveProjectAs action.
     * 
     * @author BREDEX GmbH
     * @created Dec 4, 2007
     */
    private class SaveAsOperation implements IRunnableWithProgress {
        
        /** the total work for the operation */
        private static final int TOTAL_WORK = 100;
        
        /** the work for gathering project data from the database*/
        private static final int WORK_GET_PROJECT_FROM_DB = 5;
        
        /** the work for creating the domain objects for the project */
        private static final int WORK_PROJECT_CREATION = 10;
        
        /** the work for saving the project to the database */
        private static final int WORK_PROJECT_SAVE = 
            TOTAL_WORK - WORK_PROJECT_CREATION - WORK_GET_PROJECT_FROM_DB;

        /** The name for the new project */
        private String m_newProjectName;
        
        /**
         * Constructor
         * 
         * @param newProjectName name of new project
         */
        public SaveAsOperation(String newProjectName) {
            m_newProjectName = newProjectName;
        }
        
        /**
         * {@inheritDoc}
         */
        public void run(IProgressMonitor monitor) 
            throws InterruptedException, InvocationTargetException {
            String cProjectName = GeneralStorage.getInstance()
                .getProject().getName();
            SubMonitor subMonitor = SubMonitor.convert(monitor, I18n.getString(
                    "SaveProjectAsOperation.SavingProject", //$NON-NLS-1$
                    new Object[] {cProjectName,
                                  m_newProjectName}), TOTAL_WORK);
            final ParamNameBPDecorator paramNameMapper = 
                new ParamNameBPDecorator(ParamNameBP.getInstance());
            final IWritableComponentNameCache compNameCache = 
                new ComponentNamesDecorator(null);
            try {
                NodePM.getInstance().setUseCache(true);
                String content = getContentForNewProject(subMonitor.newChild(
                        WORK_GET_PROJECT_FROM_DB));
                if (monitor.isCanceled()) {
                    throw new InterruptedException();
                }
                if (content != null) {
                    final IProjectPO duplicatedProject = XmlStorage.load(
                        content, true, paramNameMapper, compNameCache,
                        subMonitor.newChild(WORK_PROJECT_CREATION));
                    IWritableComponentNameMapper compNameMapper =
                        new ProjectComponentNameMapper(
                            compNameCache, duplicatedProject);
                    try {
                        duplicatedProject.setClientMetaDataVersion(IGdVersion
                            .GD_CLIENT_METADATA_VERSION);
                        attachProjectWithProgress(subMonitor.newChild(
                                WORK_PROJECT_SAVE), paramNameMapper, 
                                compNameMapper, duplicatedProject);
                    } catch (PMSaveException e) {
                        Plugin.stopLongRunning();
                        PMExceptionHandler.handlePMExceptionForMasterSession(
                            new PMSaveException(e.getMessage(), MessageIDs
                                .E_SAVE_AS_PROJECT_FAILED));
                        throw new InvocationTargetException(e);
                    } catch (PMException e) {
                        Plugin.stopLongRunning();
                        PMExceptionHandler.handlePMExceptionForMasterSession(e);
                        throw new InvocationTargetException(e);
                    } catch (GDProjectDeletedException e) {
                        Plugin.stopLongRunning();
                        PMExceptionHandler.handleGDProjectDeletedException();
                        throw new InvocationTargetException(e);
                    }
                }
            } catch (PMSaveException e) {
                Plugin.stopLongRunning();
                PMExceptionHandler.handlePMExceptionForMasterSession(
                        new PMSaveException(e.getMessage(), 
                            MessageIDs.E_SAVE_AS_PROJECT_FAILED));
            } catch (PMException e) {
                Plugin.stopLongRunning();
                PMExceptionHandler.handlePMExceptionForMasterSession(e);
            } catch (GDProjectDeletedException e) {
                Plugin.stopLongRunning();
                PMExceptionHandler.handleGDProjectDeletedException();    
            } catch (GDVersionException e) {
                // should not be occur, that a used toolkit of current project
                // has a version conflict with installed Toolkit Plugin.
                log.error("Toolkit version conflict while save project as action."); //$NON-NLS-1$
            } catch (ConverterException e) { // should not occur
                final String msg = "Exception '" + e + "' should not occur!"; //$NON-NLS-1$//$NON-NLS-2$
                log.error(msg);
                Assert.notReached(msg);  
            } finally {
                NodePM.getInstance().setUseCache(false);
                Plugin.stopLongRunning();
                monitor.done();
            }
        }

        /**
         * Attaches the given project to the Master Session and database 
         * using the given parameter name mapper. 
         * Reports progress during the operation.
         * 
         * @param monitor The progress monitor for the operation.
         * @param paramNameMapper The parameter name mapper to use when adding the
         *               project to the database.
         * @param compNameMapper The component name mapper to use when adding the
         *               project to the database.
         * @param project The project to add to the database
         * @throws PMException
         *             in case of any db error
         * @throws GDProjectDeletedException if project is already deleted
         * @throws InterruptedException if the operation was canceled.
         */
        private void attachProjectWithProgress(IProgressMonitor monitor,
                final ParamNameBPDecorator paramNameMapper, 
                IWritableComponentNameMapper compNameMapper,
                final IProjectPO project) throws PMException,
                GDProjectDeletedException, InterruptedException {

            // We need to clear the current project data so 
            // we are in a known state if the operation is 
            // canceled.
            IProjectPO clearedProject = 
                GeneralStorage.getInstance().getProject();
            if (clearedProject != null) {
                Utils.clearGuidancer();
                GeneralStorage.getInstance().setProject(null);
                DataEventDispatcher.getInstance()
                    .fireDataChangedListener(clearedProject, DataState.Deleted,
                        UpdateState.all);
            }
            List<INameMapper> mapperList = new ArrayList<INameMapper>();
            List<IWritableComponentNameMapper> compNameCacheList = 
                new ArrayList<IWritableComponentNameMapper>();
            mapperList.add(paramNameMapper);
            compNameCacheList.add(compNameMapper);
            ProjectPM.attachProjectToROSession(project, m_newProjectName, 
                mapperList, compNameCacheList, monitor);

            Plugin.stopLongRunning();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void runWithEvent(IAction action, Event event) {
        if (action != null && !action.isEnabled()) {
            return;
        }
        Plugin.startLongRunning(I18n.getString("SaveProjectAsAction.waitWhileSaving")); //$NON-NLS-1$
        InputDialog dialog = openInputDialog();
        if (dialog.getReturnCode() == Window.OK) {
            final String newProjectName = dialog.getName();
            IRunnableWithProgress op = createOperation(newProjectName);
            try {
                PlatformUI.getWorkbench().getProgressService()
                    .busyCursorWhile(op);
                fireReady();
            } catch (InvocationTargetException ite) {
                // Exception occurred during operation
                log.error(ite.getCause());
            } catch (InterruptedException e) {
                // Operation was canceled.
                // We have to clear the GUI because all of 
                // the save work was done in the Master Session, which has been 
                // rolled back.
                Utils.clearGuidancer();
            }
        } else {
            Plugin.stopLongRunning();
        }
    }

    /**
     * @param newProjectName name of new project
     * @return a new operation for project import
     */
    private IRunnableWithProgress createOperation(final String newProjectName) {
        return new SaveAsOperation(newProjectName);
    }
    

    /**
     * Opens the dialog to change the project name
     * @return the dialog
     */
    private InputDialog openInputDialog() {
        InputDialog dialog = new InputDialog(
            Plugin.getShell(),
            I18n.getString("SaveProjectAsAction.title"),  //$NON-NLS-1$
            GeneralStorage.getInstance().getProject().getName(),
            I18n.getString("SaveProjectAsAction.message"), //$NON-NLS-1$
            I18n.getString("SaveProjectAsAction.label"), //$NON-NLS-1$
            I18n.getString("SaveProjectAsAction.invalidName"), //$NON-NLS-1$
            I18n.getString("SaveProjectAsAction.doubleOrInvalidName"), //$NON-NLS-1$
            IconConstants.BIG_PROJECT_STRING, 
            I18n.getString("SaveProjectAsAction.shellTitle"), //$NON-NLS-1$ 
            false) {

            /**
             * {@inheritDoc}
             */
            protected boolean isInputAllowed() {
                final String newProjectName = getInputFieldText();
                return ProjectNameBP.isValidProjectName(newProjectName, true)
                        && !ProjectPM.doesProjectNameExist(newProjectName);
            }

            /**
             * {@inheritDoc}
             */
            protected void okPressed() {
                if (ProjectPM.doesProjectNameExist(getInputFieldText())) {
                    Utils.createMessageDialog(
                        MessageIDs.E_PROJECTNAME_ALREADY_EXISTS, 
                        new Object[]{getInputFieldText()}, null);
                    return;
                }
                super.okPressed();
            }
        };
        dialog.setHelpAvailable(true);
        dialog.create();
        DialogUtils.setWidgetNameForModalDialog(dialog);
        Plugin.getHelpSystem().setHelp(dialog.getShell(), 
            ContextHelpIds.DIALOG_PROJECT_SAVEAS);
        dialog.open();
        return dialog;
    }

    /**
     * {@inheritDoc}
     */
    protected AbstractActionBP getActionBP() {
        return SaveProjectAsBP.getInstance();
    }

    /**
     * call this if the "save as" has ended to update the GUI.
     */
    private void fireReady() {
        DataEventDispatcher dispatcher = DataEventDispatcher.getInstance();
        dispatcher.fireProjectLoadedListener(new NullProgressMonitor());
        IProjectPO project = GeneralStorage.getInstance().getProject();
        Plugin.setProjectNameInTitlebar(project.getName(),
            project.getMajorProjectVersion(), project.getMinorProjectVersion());
        dispatcher.fireProjectOpenedListener();
    }

    /**
     * 
     * @param monitor The progress monitor for this potentially long-running 
     *                operation.
     * @return content for new project to create, or <code>null</code> if the 
     *         operation was cancelled.
     * @throws PMException if saving of project as xml file failed
     * @throws GDProjectDeletedException if current project is already deleted
     * @throws InterruptedException if the operation was canceled.
     */
    private String getContentForNewProject(IProgressMonitor monitor) 
        throws GDProjectDeletedException, InterruptedException, PMException {
        GeneralStorage.getInstance().validateProjectExists(
                GeneralStorage.getInstance().getProject());
        String serializedProject = XmlStorage.save(
            GeneralStorage.getInstance().getProject(), null, false, monitor);

        if (monitor.isCanceled()) {
            throw new InterruptedException();
        }
        if (serializedProject != null) {
            StringBuilder result = new StringBuilder(
                serializedProject.length());
            result.append(serializedProject);
            String content = new XmlStorage()
                .checkAndReduceXmlHeaderForSaveAs(result);
            return content;
        }
        
        return null;
    }
}