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
package org.eclipse.jubula.client.alm.rc;

import org.eclipse.jubula.client.alm.core.utils.ALMAccess;
import org.eclipse.jubula.tools.exception.JBException;
import org.eclipse.jubula.tools.objects.event.EventFactory;
import org.eclipse.jubula.tools.objects.event.TestErrorEvent;

/** @author BREDEX GmbH */
public class AddCommentToTask extends AbstractALMExecutionCommand {
    /** {@inheritDoc} */
    public TestErrorEvent execute() throws JBException {
        TestErrorEvent event = null;
        String repoName = getValueForParam(ALM_REPOSITORY_NAME_KEY);
        String taskID = getValueForParam(ALM_TASK_ID_KEY);
        String comment = getValueForParam(ALM_COMMENT_KEY);

        boolean success = ALMAccess.createComment(repoName, taskID, comment);
        if (!success) {
            event = EventFactory.createActionError();
        }
        return event;
    }
}
