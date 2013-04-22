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
package org.eclipse.jubula.client.core.i18n;

import org.eclipse.osgi.util.NLS;

/**
 * @author BREDEX GmbH
 * @created 10.12.2010
 */
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.jubula.client.core.i18n.messages"; //$NON-NLS-1$
    
    public static String AcceptingFailed;
    public static String AcceptingFailedCalledAlthoughThisIsClient;
    public static String ActualExecutedCap;
    public static String ActualPeekObjectOnStack;
    public static String AddingIAUTEventListener;
    public static String AfterBackslashIn;
    public static String AlreadyLockedCurrentlyLockAttemptFailed;
    public static String AndDatasetNumberZero;
    public static String AndGUID;
    public static String AndLocalPort;
    public static String AndName;
    public static String ApplicationID;
    public static String AttemptToAddAnEventhandlerTwice;
    public static String AUTClassFormatNotSupportedByUsedJRE;
    public static String AUTConnectionFails;
    public static String AUTCouldNotStarted;
    public static String AUTIsRunning;
    public static String AUTServerCouldNotStart;
    public static String AUTServerIsReady;
    public static String AUTServerIsStarting;
    public static String AUTServerModeChangedTo;
    public static String CalledAlthoughThisIsServer;
    public static String CalledForPersistantObject;
    public static String CannotEstablishNewConnectionToAUT;
    public static String CantAttachProject;
    public static String CantContinueTSIsStopped;
    public static String CantLoad;
    public static String CantReadProjectFromDatabase;
    public static String CantSaveProject;
    public static String CantSetupPersistence;
    public static String ClearingOfMasterSessionFailed;
    public static String ClientBuildingReport;
    public static String ClientCalculating;
    public static String ClientCollectingInformation;
    public static String ClientWritingReport;
    public static String ClientWritingReportError;
    public static String ClientWritingReportToDB;
    public static String CloseSessionFailed;
    public static String ClosingConnectionToTheAUTAgent;
    public static String ClosingConnectionToTheAUTServer;
    public static String ClosingConnectionToTheAutStarter;
    public static String ClosingTheConnectionsFailed;
    public static String CommunicationErrorGetResourceBundle;
    public static String CommunicationErrorSetResourceBundle;
    public static String CommunicationErrorWhileSettingResourceBundle;
    public static String CommunicationWithAUTFails;
    public static String CommunicatorMustNotBeNull;
    public static String CompNameIncompatibleTypeDetail;
    public static String CompNameUnknownTypeDetail;
    public static String Component;
    public static String ComponentId;
    public static String ComponentIdentifiersDoes;
    public static String ComponentIsNotSupported;
    public static String ComponentNotFound;
    public static String ComponentTypeIsNull;
    public static String ConnectedTo;
    public static String ConnectedToTheServer;
    public static String ConnectingFailed;
    public static String ConnectingTheAUTAgentFailed;
    public static String ConnectingToAUT;
    public static String ConnectingToDatabase;
    public static String ConnectingToDatabaseJob;
    public static String ConnectionEstablishedOnPort;
    public static String ConnectionToAUTAgentClosed;
    public static String ConnectionToAUTCouldNotBeEstablished;
    public static String ConnectionToAUTEstablished;
    public static String ConnectionToAUTServerClosed;
    public static String ContinueTestExecution;
    public static String CouldNotBeLoadedAnUnavailableToolkitPlugin;
    public static String CouldNotConnectToAUT;
    public static String CouldNotDeleteAllParameters;
    public static String CouldNotDeleteProjectName;
    public static String CouldNotFindComponentNameWithGUID;
    public static String CouldNotFindParentForTestCase;
    public static String CouldNotGetALockOnTableCOMPONENT_NAMES;
    public static String CouldNotReadComponentNamesFromDBOfProjectWithID;
    public static String CouldNotReadComponentNameWithParentProjID;
    public static String CouldNotReadParameterNamesFromDB;
    public static String CouldNotReadParamNamesFromDB;
    public static String CouldNotRequestComponentsFromAUT;
    public static String CouldNotRequestComsFromAUT;
    public static String CouldNotSaveParameter;
    public static String CouldNotShutDownDatabaseConnectionPool;
    public static String CouldNotShutDownLockManager;
    public static String CouldntDropSsession;
    public static String CreatingAnMessageSharedInstanceFor;
    public static String CreatingMessageSharedInstanceFailed;
    public static String CurrenActionNotAvailabelForSelectedToolkit;
    public static String DatabaseConnectionInvalidPreferenceString;
    public static String DatabaseConnectionEstablished;
    public static String DatabaseProfileDoesNotExist;
    public static String DataEventDispatcherReopenProject;
    public static String DataSource;
    public static String DBEntryMissingAmbiguous;
    public static String DBVersion;
    public static String DBVersionDoesNotHaveAParentProject;
    public static String DBVersionProblem;
    public static String DefaultEventHandlerDefEH;
    public static String DefaultEventHandlerUnknown;
    public static String DefinedByComponent;
    public static String DeleteAllTestrunSummariesFailed;
    public static String DeleteTestresultElementFailed;
    public static String DeleteTestrunFailed;
    public static String DeleteTestrunMetadataFailed;
    public static String DeletingTestresultsFailed;
    public static String DeletionOfParamNamePOsFailed;
    public static String DeletionOfToolkitsFailed;
    public static String DisposeOfPersistorFailed;
    public static String DoesNotHaveAParentProject;
    public static String DoesNotTrackItsParentProject;
    public static String DoubleEventTestCaseForTheSameEvent;
    public static String EmptyComponentName;
    public static String EmptyParameterName;
    public static String ErrorDetailDATASOURCE_CONTAIN_EMPTY_DATA;
    public static String ErrorDetailNO_AUT_ID_FOR_REF_TS_FOUND;
    public static String ErrorExecutingCommand;
    public static String ErrorFileWriting;
    public static String ErrorInitializingComponentNamesLocking;
    public static String ErrorInTestExecutionTree;
    public static String TooltipWarningInChildren;
    public static String TooltipErrorInChildren;
    public static String ErrorMessageAUT_TOOLKIT_NOT_AVAILABLE;
    public static String ErrorMessageDATASOURCE_LOCALE_NOTSUPPORTED;
    public static String ErrorMessageDATASOURCE_MISSING_PARAMETER;
    public static String ErrorMessageDATASOURCE_MISSING_VALUES;
    public static String ErrorMessageDATASOURCE_READ_ERROR;
    public static String ErrorMessageNOT_SUPP_DATASOURCE;
    public static String ErrorMessagePresenter;
    public static String ErrorOccurredActivatingObjectMapping;
    public static String ErrorOccurredEstablishingConnectionToAUT;
    public static String ErrorOccurredInitializingDatabaseVersion;
    public static String ErrorOccurredResolvingDatabaseVersionConflict;
    public static String ErrorOccurredWhileGettingComponentsFromAUT;
    public static String ErrorOccurredWhileObservingTestStep;
    public static String ErrorOccurredWhileTryingToStopAUT;
    public static String ErrorParsingTimeoutParameter;
    public static String ErrorReadingFile;
    public static String ErrorSavingChangedComponentName;
    public static String ErrorSavingChangedComponentNameType;
    public static String ErrorWhileGettingListOfRegisteredAUTs;
    public static String ErrorWhileNotifyingListeners;
    public static String ErrorWritingComponentNamesToDBOfProjectID;
    public static String EstablishingConnectionToAUT;
    public static String EventExecTestCasePOBREAK;
    public static String EventExecTestCasePOCONTINUE;
    public static String EventExecTestCasePOEXIT;
    public static String EventExecTestCasePOGOTO;
    public static String EventExecTestCasePOREPEAT;
    public static String EventExecTestCasePORETRY;
    public static String EventExecTestCasePORETURN;
    public static String EventExecTestCasePOSTOP;
    public static String Exception;
    public static String ExceptionShouldNotHappen;
    public static String ExecTestCasePOMissingReference;
    public static String ExecTestCasePOMissingReferenceWithProjectName;
    public static String ExecutedFailed;
    public static String ExecutingClientAction;
    public static String ExecutingDirectoryListCommand;
    public static String ExecutionLog;
    public static String Failed;
    public static String FailedToUpdateApplicationTimestamp;
    public static String Failure;
    public static String FiringAUTStateChanged;
    public static String FlushFailed;
    public static String ForGuid;
    public static String FromTheDatabase;
    public static String FunctionNotDefined;
    public static String FunctionRegistry_WrongEvaluatorType;
    public static String FunctionRegistry_EvaluatorCreationError;
    public static String GeneralDatabaseErrorFor;
    public static String GeneralFailure;
    public static String GettingAllComponentsFromAUT;
    public static String PersistenceLoadFailed;
    public static String ImplementingMethodHasThrownAnException;
    public static String IncompatibleType;
    public static String IncompleteTestdata;
    public static String InDataSource;
    public static String InHierachy;
    public static String InitialisationOfAUTConnectionFailed;
    public static String InNode;
    public static String InReferenceNoAppropriateParameter;
    public static String InvalidCharacter;
    public static String InvalidGuid;
    public static String InvalidInstanceForInvokingOfThisMethod;
    public static String InvalidProjectVersionNumber;
    public static String InvocationOfListenerForReloadingSessionFailed;
    public static String IsBeingReplacedWith;
    public static String IsNotAssignableTo;
    public static String IsNotAvailable;
    public static String IsNotResolvable;
    public static String ItsNotAllowedToRemoveParametersFromCapPO;
    public static String JobConfigurationValidateAnyAut;
    public static String JobConfigurationValidateAutConf;
    public static String JobConfigurationValidateProjectExist;
    public static String JobConfigurationValidateTestSuiteExist;
    public static String JoiningTransaction;
    public static String KeepAliveAlreadyActive;
    public static String ListenerIsNull;
    public static String LocaleNotSupported;
    public static String LockFailedDueToDbOutOfSync;
    public static String LockFailedDueToDeletedDOT;
    public static String LockFailedDueToDeletedPO;
    public static String LockingWontStart;
    public static String LoggingConfigurationError;
    public static String MajorVersionInvalid;
    public static String MappedObject;
    public static String MessageCap;
    public static String Method;
    public static String MethodNotAccesible;
    public static String MinorVersionInvalid;
    public static String MissingActionForComponent;
    public static String MissingComponentNameForComponent;
    public static String MissingDefaultEventHandlerForEventType;
    public static String MissingImplementationClass;
    public static String MissingNameForCapPO;
    public static String MissingNameForParameterInTestcase;
    public static String MissingParameterType;
    public static String MissingParameterTypeForTestcase;
    public static String MissingPermission;
    public static String NeitherValueNorReferenceForNode;
    public static String NoAUTActivationMessageClassFoundForToolkit;
    public static String Node;
    public static String NoDefaultObjectMappingsCouldBeFoundForTheAUT;
    public static String NodeForGivenParameterValueMustNotBeNull;
    public static String NodeMismatch;
    public static String NodeWithReferenceIsNotChildOfParamNode;
    public static String NoEntryFor;
    public static String NoJavaFound;
    public static String NoLanguageConfiguredInChoosenAUT;
    public static String NoLogicalNameForDefaultMapping;
    public static String NonRecoverableError;
    public static String NoNullValueAllowed;
    public static String NoOrWrongUsernameOrPassword;
    public static String NotAllowedToAddNodeToCapPO;
    public static String NotAllowedToAddReferenceToNodeASpecTestCase;
    public static String NotAllowedToSaveAnUnlockedWorkversion;
    public static String NotAllowedToSetSingleBackslashIn;
    public static String NoTestdataAvailableForCAP;
    public static String NotFound;
    public static String NoTraverserInstance;
    public static String NotResolvable;
    public static String NotSupported;
    public static String NoValueAvailableForParameter;
    public static String Null;
    public static String OfType;
    public static String OK;
    public static String OrginalTestcaseLocked;
    public static String OriginalObjectForCreatingOfWorkversionIsNull;
    public static String Param;
    public static String Parameter;
    public static String ParameterParsingErrorOccurred;
    public static String parametersAreNotValid;
    public static String ParameterWithUniqueId;
    public static String ParamProposal_ParsingError;
    public static String ParentChildInconsistency;
    public static String ParentProjectDoesNotExistWithID;
    public static String ParentProjectId;
    public static String PersistenceErrorCreateEntityManagerFailed;
    public static String PMExceptionWhileWritingUsedToolkitsInDB;
    public static String ProblemIncompleteObjectMappingMarkerText;
    public static String ProblemIncompleteObjectMappingTooltip;
    public static String ProblemIncompleteTestDataMarkerText;
    public static String ProblemIncompleteTestDataTooltip;
    public static String ProblemInstallingDBScheme;
    public static String ProblemMissingReferencedTestCaseMarkerText;
    public static String ProblemMissingReferencedTestCaseTooltip;
    public static String ProblemWithDatabaseSchemeConf;
    public static String ProblemWithInstallingDBScheme;
    public static String ProfessionalName;
    public static String Project;
    public static String ProjectNotInDB;
    public static String ProjectWasDeleted;
    public static String ProjectWizardCreatingProject;
    public static String ReadingComponentNamesFailed;
    public static String ReadingOfProjectNameOrParamNamesFailed;
    public static String Reference;
    public static String RefreshOfOriginalVersionFailed;
    public static String ReinitOfSessionFailed;
    public static String RemovingIAUTEventListener;
    public static String RequestingAUTAgentToCloseAUTConnection;
    public static String ResetFailed;
    public static String Resource;
    public static String Response;
    public static String ReturningNull;
    public static String ReturnType;
    public static String ReturnValue;
    public static String RollbackFailed;
    public static String RunCalledToAnAlreadyConnectedConnection;
    public static String SavingProjectFailed;
    public static String SecurityViolationGettingHostNameFromIP;
    public static String SendingMessageFailed;
    public static String ServerConnectionIsNotInitialized;
    public static String SessionAndTransactionDontMatch;
    public static String SetNameNotSupportedOnProjectPO;
    public static String ShowWhereUsedSearching;
    public static String SpecifiedLanguageNotSupported;
    public static String StartAUTServerMessageSend;
    public static String StartingObjectMapping;
    public static String StartingRecordModus;
    public static String StartingTransaction;
    public static String ExecutingTestSuite;
    public static String PreparingTestSuiteExecution;
    public static String StartingTestSuite;
    public static String StartingTestSuite_resolvingTestStepsToExecute;
    public static String StartingTestSuite_resolvingPredefinedVariables;
    public static String StartingTestSuite_resettingMonitoringData;
    public static String StartingTestSuite_activatingAUT;
    public static String StoppingTest;
    public static String StoringOfMetadataFailed;
    public static String StoringOfTestResultsFailed;
    public static String StrayPersistenceException;
    public static String Success;
    public static String SyntaxErrorInReference;
    public static String TechnicalName;
    public static String TestDataNotAvailable;
    public static String TestErrorEventAction;
    public static String TestErrorEventCompNotFound;
    public static String TestErrorEventConfig;
    public static String TestErrorEventVerifyFailed;
    public static String TestexecutionHasResumed;
    public static String TestResultNodeAbort;
    public static String TestResultNodeErrorInChildren;
    public static String TestResultNodeGUINoNode;
    public static String TestResultNodeNotYetTested;
    public static String TestResultNodeRetrying;
    public static String TestResultNodeStepfailed;
    public static String TestResultNodeSuccessfullyTested;
    public static String TestResultNodeSuccessRetry;
    public static String TestResultNodeTesting;
    public static String TestResultNodeUnknown;
    public static String TestResultViewPreferencePageStyleErrorsOnly;
    public static String TestStep;
    public static String TestStepResult;
    public static String TestsuiteFinished;
    public static String TestsuiteIsPaused;
    public static String TestsuiteIsStopped;
    public static String TestsuiteMustNotBeNull;
    public static String TheAUTCouldNotFound;
    public static String TheDBAllreadyUseAnotherProcess;
    public static String TheFirstNameMustNotBeNull;
    public static String TheLogicComponentIsNotManaged;
    public static String TheMainMethodCouldNotLoaded;
    public static String TheSecondNameMustNotBeNull;
    public static String TheUniqueIdMustNotBeNull;
    public static String ThisConnectionIsAlreadyConnected;
    public static String TimeoutCalled;
    public static String TimeoutExpired;
    public static String TimeoutOccuredGettingCompAUT;
    public static String Timestamp;
    public static String TriedFindProjectsForNonExistantProject;
    public static String UnableToAppendAUTAgent;
    public static String UnableToConnectToAUT;
    public static String UnableToFind;
    public static String UnexpectedPersistenceErrorIgnored;
    public static String UnexpectedProblemWithStringReplacement;
    public static String UnhandledExceptionWhileCallingListeners;
    public static String UnhandledExceptionWhileCallListeners;
    public static String uniqueId;
    public static String UnknownErrorGettingAllCompsAUT;
    public static String UnknownObject;
    public static String UnknownSourceType;
    public static String UnknownState;
    public static String UnsupportedINodePOSubclass;
    public static String UnsupportedReentryProperty;
    public static String UpdateOfTimestampFailed;
    public static String User;
    public static String UsingDefaultValue;
    public static String VariableWithName;
    public static String WaitingForAutConfigMapFromAgent;
    public static String WasDeletedByAnotherTransaction;
    public static String WasModifiedInDBDirtyVersion;
    public static String WhileTryingToIdentifyItsType;
    public static String WithID;
    public static String WithReferenceIsNotChildOfParamNode;
    public static String WrongLoggingImplementation;
    public static String WrongNumFunctionArgs;
    public static String WrongTypeForAdditionOfEventhandler;
    public static String WrongTypeForRemovalOfEventhandler;
    public static String WrongTypeOfReentryProperty;

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
