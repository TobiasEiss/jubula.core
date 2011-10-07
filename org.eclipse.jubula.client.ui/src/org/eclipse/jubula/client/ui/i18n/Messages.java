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
package org.eclipse.jubula.client.ui.i18n;

import org.eclipse.osgi.util.NLS;

/**
 * @author BREDEX GmbH
 * @created 19.01.2011
 */
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.jubula.client.ui.i18n.messages"; //$NON-NLS-1$

    public static String AnErrorHasOccurred;
    public static String CantLoadMetadataFromDatabase;
    public static String DatabaseConnectionDialogDefaultName;
    public static String DatabaseConnectionDialogTitle;
    public static String DatabaseConnectionPreferencePageAddButtonLabel;
    public static String DatabaseConnectionPreferencePageEditButtonLabel;
    public static String DatabaseConnectionPreferencePageRemoveButtonLabel;
    public static String DatabaseConnectionPrefPageSelecObjIsOfIncorrectType;
    public static String DBLoginDialogConnectionLabel;
    public static String DBLoginDialogEmptyUser;
    public static String DBLoginDialogMessage;
    public static String DBLoginDialogNoSchemaAvailable;
    public static String DBLoginDialogNoSchemaSelected;
    public static String DBLoginDialogPwdLabel;
    public static String DBLoginDialogShell;
    public static String DBLoginDialogTitle;
    public static String DBLoginDialogUserLabel;
    public static String DBLoginDialogWrongPwd;
    public static String DBLoginDialogWrongUser;
    public static String EditorInitCreateError;
    public static String EditorsOpenEditorOperationCanceled;
    public static String EnterCommentDialogDetailLabel;
    public static String EnterCommentDialogMessage;
    public static String EnterCommentDialogTitle;
    public static String EnterCommentDialogTitleLabel;
    public static String ErrorFetchingTestResultInformation;
    public static String ErrorOccurredWhileExecutingCommand;
    public static String Export;
    public static String InputElementHasInvalidTypeReturningEmptyArray;
    public static String JobFilterSummaryView;
    public static String NoEditorInputCouldBeCreated;
    public static String ParentElementHasInvalidTypeReturningEmptyArray;
    public static String PluginConnectProgress;
    public static String SelectDatabaseConnectFailed;
    public static String SelectDatabaseConnectSuccessful;
    public static String SelectedElementIsNotTestResultSummary;
    public static String SetItemsNotValidForThisSubclass;
    public static String StoringOfMetadataFailed;
    public static String TestresultSummaryAutAgentHostname;
    public static String TestresultSummaryAutConf;
    public static String TestresultSummaryAutHostname;
    public static String TestresultSummaryAutId;
    public static String TestresultSummaryAutName;
    public static String TestresultSummaryAutOS;
    public static String TestresultSummaryCmdParam;
    public static String TestresultSummaryCommentTitle;
    public static String TestresultSummaryConnectToDb;
    public static String TestresultSummaryDate;
    public static String TestresultSummaryDeleteTestrun;
    public static String TestresultSummaryDeleteTestrunDialogMessage;
    public static String TestresultSummaryDeleteTestrunDialogTitle;
    public static String TestresultSummaryDetailsAvailable;
    public static String TestresultSummaryDuration;
    public static String TestresultSummaryEndTime;
    public static String TestresultSummaryExecCaps;
    public static String TestresultSummaryExpecCaps;
    public static String TestresultSummaryFilterLabel;
    public static String TestresultSummaryForLabel;
    public static String TestresultSummaryHandlerCaps;
    public static String TestresultSummaryLanguage;
    public static String TestresultSummaryMonitoringDetails;
    public static String TestresultSummaryMonitoringDetailsAvailable;
    public static String TestresultSummaryMonitoringDetailsNotAvailable;
    public static String TestresultSummaryMonitoringId;
    public static String TestresultSummaryMonitoringIdNonSelected;
    public static String TestresultSummaryMonitoringValue;
    public static String TestresultSummaryMonitoringValueNotAvailable;
    public static String TestresultSummaryNoData;
    public static String TestresultSummaryNumberOfFailedCaps;
    public static String TestresultSummaryProjectName;
    public static String TestresultSummaryRefreshButton;
    public static String TestresultSummaryStartTime;
    public static String TestresultSummaryTestJob;
    public static String TestresultSummaryTestJobStartTime;
    public static String TestresultSummaryTestrunID;
    public static String TestresultSummaryTestrunRelevant;
    public static String TestresultSummaryTestrunState;
    public static String TestresultSummaryTestsuite;
    public static String TestresultSummaryTestsuiteStatus;
    public static String TestresultSummaryToolkit;
    public static String UIJobDeletingTestResultFromDB;
    public static String UnknownTypeOfElementInTreeOfType;
    public static String UtilsError;
    public static String UtilsErrorOccurred;
    public static String UtilsInfo1;
    public static String UtilsInfo2;
    public static String UtilsRequest1;
    public static String UtilsRequest2;
    public static String UtilsWarning1;
    public static String UtilsWarning2;
    public static String ValidationPortErrorInvalidPortNumber;
    
    
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    /**
     * Constructor
     */
    private Messages() {
        // hide
    }
}
