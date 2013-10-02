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
package org.eclipse.jubula.client.ui.constants;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jubula.client.ui.Plugin;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author BREDEX GmbH
 * @created 31.07.2006
 */
public class IconConstants {
    /** error image */
    public static final Image ERROR_IMAGE = Plugin.getImage("error.gif"); //$NON-NLS-1$
    /** warning image */
    public static final Image WARNING_IMAGE = Plugin.getImage("warning.gif"); //$NON-NLS-1$
    /** warning small image */
    public static final ImageDescriptor WARNING_IMAGE_DESCRIPTOR = PlatformUI
            .getWorkbench().getSharedImages()
            .getImageDescriptor(ISharedImages.IMG_DEC_FIELD_WARNING);
    
    /** info image descriptor */
    public static final ImageDescriptor INFO_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("info.gif"); //$NON-NLS-1$
    /** info image */
    public static final Image INFO_IMAGE = INFO_IMAGE_DESCRIPTOR.createImage();
    /** testSuiteeditor image */
    public static final Image DISABLED_TS_EDITOR_IMAGE = Plugin.getImage("testSuiteEditor_disabled.gif"); //$NON-NLS-1$
    /** testCaseEditor image */
    public static final Image DISABLED_TC_EDITOR_IMAGE = Plugin.getImage("specTcEditor_disabled.gif"); //$NON-NLS-1$
    /** disabled test job editor image */
    public static final Image DISABLED_TJ_EDITOR_IMAGE = Plugin.getImage("tjEditor_disabled.gif"); //$NON-NLS-1$
    /** disabled central test data editor image */
    public static final Image DISABLED_CTD_EDITOR_IMAGE = Plugin.getImage("ctdEditor_disabled.gif"); //$NON-NLS-1$
    /** ObjectMappingEditor image */
    public static final Image DISABLED_OM_EDITOR_IMAGE = Plugin.getImage("omEditor_disabled.gif"); //$NON-NLS-1$
    /** delete image */
    public static final Image DELETE_IMAGE = Plugin.getImage("delete.gif"); //$NON-NLS-1$
    /** disabled delete image */
    public static final Image DELETE_IMAGE_DISABLED = 
        Plugin.getImage("delete_disabled.gif"); //$NON-NLS-1$
    /** new TestCase imageDescriptor */
    public static final ImageDescriptor NEW_TC_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("newTestCaseIcon.gif"); //$NON-NLS-1$
    /** new TestCase_disabled imageDescriptor */
    public static final ImageDescriptor NEW_TC_DISABLED_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("newTestCaseIcon_disabled.gif"); //$NON-NLS-1$
    /** refresh imageDescriptor */
    public static final ImageDescriptor REFRESH_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("refresh.gif"); //$NON-NLS-1$
    /** refresh_diabled imageDescriptor */
    public static final ImageDescriptor REFRESH_DISABLED_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("refresh_disabled.gif"); //$NON-NLS-1$
    /** add EventHandler image */
    public static final Image ADD_EH_IMAGE = Plugin.getImage("addEH.gif"); //$NON-NLS-1$
    /** add new referenced TestCase imageDescriptor */
    public static final ImageDescriptor NEW_REF_TC_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("testCaseRefNew.gif"); //$NON-NLS-1$
    /** add new referenced TestCase_diabled imageDescriptor */
    public static final ImageDescriptor NEW_REF_TC_DISABLED_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("testCaseRefNew_disabled.gif"); //$NON-NLS-1$
    /** delete project dialog-image image */
    public static final Image DELETE_PROJECT_DIALOG_IMAGE = Plugin.getImage("deleteProject_big.gif"); //$NON-NLS-1$
    /** name for import project image */
    public static final String IMPORT_PROJECT_STRING = "importProject.gif"; //$NON-NLS-1$
    /** name for new CAP dialog image */
    public static final String NEW_CAP_DIALOG_STRING = "newCAPDialog.gif"; //$NON-NLS-1$
    /** new test data cube dialog image */
    public static final Image NEW_TESTDATAMANAGER_DIALOG_IMAGE = Plugin.getImage("newTestDataCubeDialog.gif"); //$NON-NLS-1$
    /** rename test data cube dialog image */
    public static final Image RENAME_TESTDATAMANAGER_DIALOG_IMAGE = Plugin.getImage("renameTestDataCubeDialog.gif"); //$NON-NLS-1$
    /** name for new component dialog image */
    public static final String NEW_COMPONENT_DIALOG_STRING = "addLogicalNameDialog.gif"; //$NON-NLS-1$
    /** open project dialog-image image */
    public static final Image OPEN_PROJECT_DIALOG_IMAGE = Plugin.getImage("chooseProject.gif"); //$NON-NLS-1$
    /** name for observe TestCase dialog image */
    public static final String OBSERVE_TC_DIALOG_STRING = "recordTestCaseDialog.gif"; //$NON-NLS-1$
    /** name for big project image */
    public static final String BIG_PROJECT_STRING = "bigProject.gif"; //$NON-NLS-1$
    /** The Step Testing image */
    public static final ImageDescriptor STEP_TESTING_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("StepTesting.gif"); //$NON-NLS-1$
    /** The Step Testing image */
    public static final Image STEP_TESTING_IMAGE = STEP_TESTING_IMAGE_DESCRIPTOR
            .createImage();
    /** The StepNotOK  */
    public static final ImageDescriptor STEP_NOT_OK_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("StepNotOK.gif"); //$NON-NLS-1$
    /** The StepNotOK  */
    public static final Image STEP_NOT_OK_IMAGE = STEP_NOT_OK_IMAGE_DESCRIPTOR
            .createImage();
    /** The stepOK descriptor */
    public static final ImageDescriptor STEP_OK_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("StepOK.gif"); //$NON-NLS-1$
    /** The stepOK descriptor */
    public static final Image STEP_OK_IMAGE = STEP_OK_IMAGE_DESCRIPTOR
            .createImage();
    /** Step failed */
    public static final ImageDescriptor STEP_FAILED_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("StepFailed.gif"); //$NON-NLS-1$
    /** Step failed */
    public static final Image STEP_FAILED_IMAGE = STEP_FAILED_IMAGE_DESCRIPTOR
            .createImage();
    /** The retryingStep descriptor */
    public static final ImageDescriptor STEP_RETRY_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("StepRetry.png"); //$NON-NLS-1$
    /** The retryingStep image */
    public static final Image STEP_RETRY_IMAGE = STEP_RETRY_IMAGE_DESCRIPTOR
            .createImage();
    /** The retryStepOK descriptor */
    public static final ImageDescriptor STEP_RETRY_OK_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("StepRetryOK.png"); //$NON-NLS-1$
    /** The retryStepOK descriptor */
    public static final Image STEP_RETRY_OK_IMAGE = 
        STEP_RETRY_OK_IMAGE_DESCRIPTOR.createImage();
    /** reference value image */
    public static final Image REF_VALUE_IMAGE = Plugin.getImage("refValue.gif"); //$NON-NLS-1$
    /** deprecated action image */
    public static final Image DEPRECATED_IMAGE = Plugin.getImage("depricated.gif"); //$NON-NLS-1$
    /** read only image */
    public static final Image READ_ONLY_IMAGE = Plugin.getImage("readonly.gif"); //$NON-NLS-1$
    /** incomplete data image */
    public static final Image INCOMPLETE_DATA_IMAGE = Plugin.getImage("StepNotOK.gif"); //$NON-NLS-1$
    /** original data image */
    public static final Image ORIGINAL_DATA_IMAGE = Plugin.getImage("orginalData.gif"); //$NON-NLS-1$
    /** overwritten data image */
    public static final Image OVERWRITTEN_DATA_IMAGE = Plugin.getImage("overwrittenData.gif"); //$NON-NLS-1$
    /** complete data imageDescriptor */
    public static final ImageDescriptor ERROR_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("incomplData.gif"); //$NON-NLS-1$
    /** excel data imageDescriptor */
    public static final ImageDescriptor EXCEL_DATA_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("excelData.gif"); //$NON-NLS-1$
    /** greenDot imageDescriptor */
    public static final ImageDescriptor GREEN_DOT_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("greenDot.gif"); //$NON-NLS-1$
    /** redDot imageDescriptor */
    public static final ImageDescriptor RED_DOT_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("redDot.gif"); //$NON-NLS-1$
    /** yellowDot imageDescriptor */
    public static final ImageDescriptor YELLOW_DOT_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("yellowDot.gif"); //$NON-NLS-1$
    /** greenDot imageDescriptor */
    public static final Image TECH_NAME_OK_IMAGE = Plugin.getImageDescriptor("techNameOK.gif").createImage(); //$NON-NLS-1$
    /** redDot imageDescriptor */
    public static final Image TECH_NAME_ERROR_IMAGE = Plugin.getImageDescriptor("techNameERR.gif").createImage(); //$NON-NLS-1$
    /** yellowDot imageDescriptor */
    public static final Image TECH_NAME_WARNING_IMAGE = Plugin.getImageDescriptor("techNameWARN.gif").createImage(); //$NON-NLS-1$
    /** excel data image */
    public static final Image EXCEL_DATA_IMAGE = 
        EXCEL_DATA_IMAGE_DESCRIPTOR.createImage();
    /** new event handler dialog-image */
    public static final Image NEW_EH_DIALOG_IMAGE = Plugin.getImage("newEventHandlerDialog.gif"); //$NON-NLS-1$
    /** name for class path image */
    public static final String CLASS_PATH_STRING = "classpath.gif"; //$NON-NLS-1$
    /** up arrow image */
    public static final Image UP_ARROW_IMAGE = Plugin.getImage("upArrow.gif"); //$NON-NLS-1$
    /** down arrow image */
    public static final Image DOWN_ARROW_IMAGE = Plugin.getImage("downArrow.gif"); //$NON-NLS-1$
    /** up arrow disabled image */
    public static final Image UP_ARROW_DIS_IMAGE = Plugin.getImage("upArrow_disabled.gif"); //$NON-NLS-1$
    /** down arrow disabled image */
    public static final Image DOWN_ARROW_DIS_IMAGE = Plugin.getImage("downArrow_disabled.gif"); //$NON-NLS-1$
    /** right arrow image */
    public static final Image RIGHT_ARROW_IMAGE = Plugin.getImage("rightArrow.gif"); //$NON-NLS-1$
    /** left arrow image */
    public static final Image LEFT_ARROW_IMAGE = Plugin.getImage("leftArrow.gif"); //$NON-NLS-1$
    /** right arrow disabled image */
    public static final Image RIGHT_ARROW_DIS_IMAGE = Plugin.getImage("rightArrow_disabled.gif"); //$NON-NLS-1$
    /** left arrow disabled image */
    public static final Image LEFT_ARROW_DIS_IMAGE = Plugin.getImage("leftArrow_disabled.gif"); //$NON-NLS-1$
    /** double right arrow disabled image */
    public static final Image DOUBLE_RIGHT_ARROW_DIS_IMAGE = Plugin.getImage("allRightArrow_disabled.gif"); //$NON-NLS-1$
    /** double left arrow disabled image */
    public static final Image DOUBLE_LEFT_ARROW_DIS_IMAGE = Plugin.getImage("allLeftArrow_disabled.gif"); //$NON-NLS-1$
    /** swap arrow disabled image */
    public static final Image SWAP_ARROW_DIS_IMAGE = Plugin.getImage("swapArrow_disabled.gif"); //$NON-NLS-1$
    /** double right arrow image */
    public static final Image DOUBLE_RIGHT_ARROW_IMAGE = Plugin.getImage("allRightArrow.gif"); //$NON-NLS-1$
    /** double left arrow image */
    public static final Image DOUBLE_LEFT_ARROW_IMAGE = Plugin.getImage("allLeftArrow.gif"); //$NON-NLS-1$
    /** swap arrow image */
    public static final Image SWAP_ARROW_IMAGE = Plugin.getImage("swapArrow.gif"); //$NON-NLS-1$
    /** db login dialog-image */
    public static final Image DB_LOGIN_DIALOG_IMAGE = Plugin.getImage("dblogin.gif"); //$NON-NLS-1$
    /** import dialog-image */
    public static final Image IMPORT_DIALOG_IMAGE = Plugin.getImage("import_big.gif"); //$NON-NLS-1$
    /** import dialog-image descriptor */
    public static final ImageDescriptor IMPORT_DIALOG_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("import_big.gif"); //$NON-NLS-1$
    /** new cap dialog-image */
    public static final Image NEW_CAP_DIALOG_IMAGE = Plugin.getImage(
            NEW_CAP_DIALOG_STRING); 
    /** new component dialog-image */
    public static final Image NEW_COMPONENT_DIALOG_IMAGE = 
        Plugin.getImage(NEW_COMPONENT_DIALOG_STRING); 
    /** server port dialog-image */
    public static final Image SERVER_PORT_DIALOG_IMAGE = Plugin.getImage("port.gif"); //$NON-NLS-1$
    /** project dialog-image */
    public static final Image PROJECT_DIALOG_IMAGE = Plugin.getImage("projectAction.gif"); //$NON-NLS-1$
    /** add test case dialog-image */
    public static final Image ADD_TC_DIALOG_IMAGE = Plugin.getImage("addTC.gif"); //$NON-NLS-1$
    /** open test case dialog-image */
    public static final Image OPEN_TC_DIALOG_IMAGE = Plugin.getImage("openTC.gif"); //$NON-NLS-1$
    /** event handler CAP image */
    public static final Image EH_CAP_IMAGE = Plugin.getImage("EventHandlerCap.gif"); //$NON-NLS-1$
    /** clock image */
    public static final Image CLOCK_IMAGE = Plugin.getImage("longRunning.gif"); //$NON-NLS-1$
    /** missing project image */
    public static final Image MISSING_PROJECT_IMAGE = Plugin.getImage("missingReusedProject.gif"); //$NON-NLS-1$
    /** aut running image */
    public static final Image AUT_RUNNING_IMAGE = Plugin.getImage("AUTup.gif"); //$NON-NLS-1$
    /** propagate image */
    public static final Image PROPAGATE_IMAGE = Plugin.getImage("propagate.gif"); //$NON-NLS-1$
    /** global name image */
    public static final Image GLOBAL_NAME_IMAGE = Plugin.getImage("globalName.gif"); //$NON-NLS-1$
    /** global name_disabled image */
    public static final Image GLOBAL_NAME_DISABLED_IMAGE = Plugin.getImage("globalName_disabled.gif"); //$NON-NLS-1$
    /** language image */
    public static final Image LANGUAGE_IMAGE = Plugin.getImage("globe.gif"); //$NON-NLS-1$
    /** local name image */
    public static final Image LOCAL_NAME_IMAGE = Plugin.getImage("localName.gif"); //$NON-NLS-1$
    /** local name_disabled image */
    public static final Image LOCAL_NAME_DISABLED_IMAGE = Plugin.getImage("localName_disabled.gif"); //$NON-NLS-1$
    /** global name image */
    public static final Image AUT_COMP_NAME_IMAGE = Plugin.getImage("autCompName.gif"); //$NON-NLS-1$
    /** global name_disabled image */
    public static final Image AUT_COMP_NAME_DISABLED_IMAGE = Plugin.getImage("autCompName_disabled.gif"); //$NON-NLS-1$
    /** project wizard imageDescriptor */
    public static final ImageDescriptor PROJECT_WIZARD_IMAGE_DESCRIPTOR = Plugin.getImageDescriptor("ProjectWizard.gif"); //$NON-NLS-1$
    /** name for move test case dialog-image */
    public static final String MOVE_TC_DIALOG_STRING = "moveTestCaseDialog.gif"; //$NON-NLS-1$
    /** name for new test case dialog-image */
    public static final String NEW_TC_DIALOG_STRING = "newTestCaseDialog.gif"; //$NON-NLS-1$
    /** name for new test suite dialog-image */
    public static final String NEW_TS_DIALOG_STRING = "newTestSuiteDialog.gif"; //$NON-NLS-1$
    /** name for new test job dialog-image */
    public static final String NEW_TJ_DIALOG_STRING = "newTestJobDialog.gif"; //$NON-NLS-1$
    /** name for test job dialog-image */
    public static final String TJ_DIALOG_STRING = "testJobDialog.gif"; //$NON-NLS-1$
    /** name for new category dialog-image */
    public static final String NEW_CAT_DIALOG_STRING = "newCategoryDialog.gif"; //$NON-NLS-1$
    /** CAP image */
    public static final Image CAP_IMAGE = Plugin.getImage("cap.gif"); //$NON-NLS-1$
    /** category image */
    public static final Image CATEGORY_IMAGE = Plugin.getImage("category.gif"); //$NON-NLS-1$
    /** event handler image */
    public static final Image EH_IMAGE = Plugin.getImage("execEventHandler.gif"); //$NON-NLS-1$
    /** event handler image */
    public static final Image RESULT_EH_IMAGE = Plugin.getImage("EventHandler.gif"); //$NON-NLS-1$
    /** referenced testCase image */
    public static final Image TC_REF_IMAGE = Plugin.getImage("testcaseRef.png"); //$NON-NLS-1$
    /** referenced testSuite image */
    public static final Image TS_REF_IMAGE = Plugin.getImage("testSuiteRef.gif"); //$NON-NLS-1$
    /** logical name image */
    public static final Image LOGICAL_NAME_IMAGE = Plugin.getImage("OMLogName.gif"); //$NON-NLS-1$
    /** logical name image */
    public static final Image PROPAGATED_LOGICAL_NAME_IMAGE = Plugin.getImage("PropagatedOMLogName.gif"); //$NON-NLS-1$
    /** technical name image */
    public static final Image TECHNICAL_NAME_IMAGE = Plugin.getImage("OMTecName.gif"); //$NON-NLS-1$
    /** project image */
    public static final Image PROJECT_IMAGE = Plugin.getImage("project.gif"); //$NON-NLS-1$
    /** testSuite image */
    public static final Image TS_IMAGE = Plugin.getImage("testSuiteNode.gif"); //$NON-NLS-1$
    /** test data cube decorator image descriptor */
    public static final ImageDescriptor TDC_DECORATION_IMAGE_DESCRIPTOR = 
        Plugin.getImageDescriptor("testDataCubeDecoration.gif"); //$NON-NLS-1$
    /** test data cube image */
    public static final Image TDC_IMAGE = Plugin.getImage("testDataCube.gif"); //$NON-NLS-1$
    /** testJob image */
    public static final Image TJ_IMAGE = Plugin.getImage("testJobNode.gif"); //$NON-NLS-1$
    /** testSuite image */
    public static final Image TS_DISABLED_IMAGE = Plugin.getImage("testSuiteNode_disabled.gif"); //$NON-NLS-1$
    /** test case image */
    public static final Image TC_IMAGE = Plugin.getImage("testcase.png"); //$NON-NLS-1$
    /** testCase_disabled image */
    /** test case image */
    public static final Image ROOT_IMAGE = Plugin.getImage("root.gif"); //$NON-NLS-1$
    /** test case image */
    public static final Image PROBLEM_CAT_IMAGE = Plugin.getImage("problemCategory.gif"); //$NON-NLS-1$
    /** Anwendungsfall image */
    public static final Image TC_ANWENDUNGSFALL_IMAGE = Plugin.getImage("anwendungsfall.png"); //$NON-NLS-1$
    /** Testfall image */
    public static final Image TC_TESTFALL_IMAGE = Plugin.getImage("testfall.png"); //$NON-NLS-1$
    /** Fachkonzept image */
    public static final Image TC_FACHKONZEPT_IMAGE = Plugin.getImage("fachkonzept.png"); //$NON-NLS-1$
    /** public tc image */
    public static final Image TC_PUBLIC_IMAGE = Plugin.getImage("publicKontext.png"); //$NON-NLS-1$
    /** private tc image */
    public static final Image TC_PRIVATE_IMAGE = Plugin.getImage("privateKontext.png"); //$NON-NLS-1$
    /** Anwendungsfall image */
    public static final Image TC_ANWENDUNGSFALL_REF_IMAGE = Plugin.getImage("anwendungsfallRef.png"); //$NON-NLS-1$
    /** Testfall image */
    public static final Image TC_TESTFALL_REF_IMAGE = Plugin.getImage("testfallRef.png"); //$NON-NLS-1$
    /** Fachkonzept image */
    public static final Image TC_FACHKONZEPT_REF_IMAGE = Plugin.getImage("fachkonzeptRef.png"); //$NON-NLS-1$
    /** public tc image */
    public static final Image TC_PUBLIC_REF_IMAGE = Plugin.getImage("publicKontextRef.png"); //$NON-NLS-1$
    /** private tc image */
    public static final Image TC_PRIVATE_REF_IMAGE = Plugin.getImage("privateKontextRef.png"); //$NON-NLS-1$
    /** name for new test case dialog-image */
    public static final String RENAME_TC_DIALOG_STRING = "renameTC.gif"; //$NON-NLS-1$
    /** name for new test suite dialog-image */
    public static final String RENAME_TS_DIALOG_STRING = "renameTS.gif"; //$NON-NLS-1$
    /** name for new category dialog-image */
    public static final String RENAME_CAT_DIALOG_STRING = "category_big.gif"; //$NON-NLS-1$
    /** name for new category dialog-image */
    public static final String RENAME_CAP_DIALOG_STRING = "renameCAP.gif"; //$NON-NLS-1$
    /** name for rename logical name dialog image */
    public static final String RENAME_COMPONENT_DIALOG_STRING = "renameLogicalName.gif"; //$NON-NLS-1$
    /** name for new test case dialog-image */
    public static final String RENAME_EH_DIALOG_STRING = "renameEH.gif"; //$NON-NLS-1$
    /** name for new test suite dialog-image */
    public static final String RENAME_PROJECT_DIALOG_STRING = "renameProject.gif"; //$NON-NLS-1$
    /** Mail image */
    public static final ImageDescriptor MAIL = Plugin.getImageDescriptor("eMail.gif"); //$NON-NLS-1$
    /** merge component name dialog image */
    public static final Image MERGE_COMPONENT_NAME_DIALOG_IMAGE = Plugin.getImage("mergeLogicalNameDialog.gif"); //$NON-NLS-1$
    /** new component dialog-image */
    public static final Image RENAME_COMPONENT_DIALOG_IMAGE = 
        Plugin.getImage(RENAME_COMPONENT_DIALOG_STRING);

    /** the logger */
    private static final Logger LOG = 
        LoggerFactory.getLogger(IconConstants.class);

    /** maps images to their "generated" (green-tinted) counterparts */
    private static final Map<Image, Image> GEN_IMAGES = 
        new HashMap<Image, Image>();
    static {
        GEN_IMAGES.put(TC_IMAGE, Plugin.getImage("testCase_generated.gif")); //$NON-NLS-1$
        GEN_IMAGES.put(TC_REF_IMAGE, Plugin.getImage("testCaseRef_generated.gif")); //$NON-NLS-1$
        GEN_IMAGES.put(CATEGORY_IMAGE, Plugin.getImage("category_generated.gif")); //$NON-NLS-1$
    }

    /** to prevent instantiation */
    private IconConstants() {
        // do nothing
    }

    /**
     * 
     * @param original The original, or base, image.
     * @return the "generated" version of the image. Client should not 
     *         dispose this image.
     */
    public static Image getGeneratedImage(Image original) {
        Image genImage = GEN_IMAGES.get(original);
        if (genImage == null) {
            LOG.error("'Generated' image does not exist."); //$NON-NLS-1$
            genImage = original;
        }
        
        return genImage;
    }

}