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
package org.eclipse.jubula.client.core.test;

import org.eclipse.jubula.communication.message.Message;

/**
 * @author BREDEX GmbH
 * @created 26.07.2004
 */
public class ServerConnectionTestMessage extends Message {
    /**
     * {@inheritDoc}
     */
    public String getCommandClass() {
        return this.getClass().getName() + "ICommand"; //$NON-NLS-1$
    }
}
