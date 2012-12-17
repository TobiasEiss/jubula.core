/*******************************************************************************
 * Copyright (c) 2012 BREDEX GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BREDEX GmbH - initial API and implementation 
 *******************************************************************************/
package org.eclipse.jubula.rc.common.caps;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jubula.rc.common.exception.StepExecutionException;
import org.eclipse.jubula.rc.common.implclasses.MatchUtil;
import org.eclipse.jubula.rc.common.implclasses.Verifier;
import org.eclipse.jubula.rc.common.uiadapter.interfaces.ITextComponentAdapter;
import org.eclipse.jubula.tools.objects.event.EventFactory;
import org.eclipse.jubula.tools.objects.event.TestErrorEvent;
import org.eclipse.jubula.tools.utils.TimeUtil;
/**
 * Implementation of the CAPs from all Text like components.
 * @author BREDEX GmbH
 *
 */
public class AbstractTextComponent extends AbstractTextInputSupport {

    /**
     * Gets the specific adapter
     * @return the specific adapter
     */
    private ITextComponentAdapter getTextCompAdapter() {
        return (ITextComponentAdapter) getComponent();
    }
    
    /**
     * Sets the caret at the position <code>index</code>.
     * @param index The caret position
     */
    private void setCaretPosition(final int index) {
        if (index < 0) {
            throw new StepExecutionException("Invalid position: " + index, //$NON-NLS-1$
                EventFactory.createActionError(TestErrorEvent.INPUT_FAILED));
        }
        int index2 = 0;
        String text = getTextCompAdapter().getText();
        if (text != null) {
            index2 = index > text.length() ? text.length() : index;
        }
        getTextCompAdapter().setSelection(index2);
    }
    
    /**
     * @param text The text to insert at the current caret position
     */
    protected void insertText(final String text) {
        // Scroll it to visible first to ensure that the typing
        // performs correctly.
        getRobot().scrollToVisible(getComponent().getRealComponent(), null);
        getRobot().type(getComponent().getRealComponent(), text);
    }
    
    /**
     * Types <code>text</code> into the component. This replaces the shown
     * content.
     *
     * @param text the text to type in
     */
    public void gdReplaceText(String text) {
        gdSelect();
        if (StringUtils.EMPTY.equals(text)) {
            getRobot().keyStroke("DELETE"); //$NON-NLS-1$
        }
        insertText(text);
    }

    /**
     * Types <code>text</code> into the component.
     *
     * @param text the text to type in
     */
    public void gdInputText(String text) {
        if (!getTextCompAdapter().hasFocus()) {
            TimeUtil.delay(100);
            gdClick(1, 1);
        }
        insertText(text);
    }
    
    /**
     * Inserts <code>text</code> at the position <code>index</code>.
     *
     * @param text The text to insert
     * @param index The position for insertion
     */
    public void gdInsertText(String text, int index) {
        gdClick(1, 1);
        setCaretPosition(index);
        insertText(text);
    }
    
    /**
     * Inserts <code>text</code> before or after the first appearance of
     * <code>pattern</code>.
     * @param text The text to insert
     * @param pattern The pattern to find the position for insertion
     * @param operator Operator to select Matching Algorithm
     * @param after If <code>true</code>, the text will be inserted after the
     *            pattern, otherwise before the pattern.
     * @throws StepExecutionException If the pattern is invalid or cannot be found
     */
    public void gdInsertText(String text, String pattern, String operator, 
            boolean after)
        throws StepExecutionException {
        
        if (text == null) {
            throw new StepExecutionException(
                "The text to be inserted must not be null", EventFactory //$NON-NLS-1$
                    .createActionError());
        }
        final MatchUtil.FindResult matchedText = MatchUtil.getInstance().
            find(getTextCompAdapter().getText(), pattern, operator);
        
        if ((matchedText == null) || (matchedText.getStr() == null)) {
            throw new StepExecutionException("The pattern '" + pattern //$NON-NLS-1$
                + "' could not be found", //$NON-NLS-1$
                EventFactory.createActionError(TestErrorEvent.NOT_FOUND));
        }

        int index = matchedText.getPos();
        
        int insertPos = after ? index + matchedText.getStr().length() : index;
        gdInsertText(text, insertPos);
    }
    

        
    /**
     * select the whole text of the textfield by calling "selectAll()".
     */
    public void gdSelect() {
        gdClick(1, 1);
        // Wait a while. Without this, we got no selectAll sometimes!
        TimeUtil.delay(100);
        getTextCompAdapter().selectAll();
    }
    
    /**
     * Verifies the editable property.
     * @param editable The editable property to verify.
     */
    public void gdVerifyEditable(boolean editable) {
        Verifier.equals(editable, getTextCompAdapter().isEditable());
    }
        
    /**
     * Selects the first (not)appearance of <code>pattern</code> in the text
     * component's content.
     *
     * @param pattern The pattern to select
     * @param operator operator
     * @throws StepExecutionException
     *             If the pattern is invalid or cannot be found
     */
    public void gdSelect(final String pattern, String operator)
        throws StepExecutionException {
        gdClick(1, 1);
        final MatchUtil.FindResult matchedText = MatchUtil.getInstance().
            find(getTextCompAdapter().getText(), pattern, operator);
        if ((matchedText == null) || (matchedText.getStr().length() == 0)) {
            throw new StepExecutionException("Invalid pattern for insertion", //$NON-NLS-1$
                EventFactory.createActionError());
        }
        final int index = matchedText.getPos();
        if (operator.startsWith("not")) { //$NON-NLS-1$
            if (pattern.equals(getTextCompAdapter().getText())) {
                String msg = "The pattern '" + pattern //$NON-NLS-1$
                    + "' is equal to current text"; //$NON-NLS-1$
                throw new StepExecutionException(msg, EventFactory
                    .createActionError(TestErrorEvent
                        .EXECUTION_ERROR, new String[] {msg}));
            } else if (index > 0) {
                // select part before pattern
                getTextCompAdapter().setSelection(0, index);
            } else {
                // select part after pattern
                getTextCompAdapter().setSelection(
                        matchedText.getStr().length(),
                        getTextCompAdapter().getText().length());
            }
        } else {
            getTextCompAdapter().setSelection(index,
                    index + matchedText.getStr().length());
        }
    }
        
    /**
     * {@inheritDoc}
     */
    public String[] getTextArrayFromComponent() {
        return null;
    }


}