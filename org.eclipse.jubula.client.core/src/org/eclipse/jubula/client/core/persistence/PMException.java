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
package org.eclipse.jubula.client.core.persistence;

import org.eclipse.jubula.tools.exception.JBException;


/** PMException as base class for all other PM exceptions */
public class PMException extends JBException {

    /**
     * {@inheritDoc}
     * @param message guess what...
     * @param id An ErrorMessage.ID.
     * {@inheritDoc}
     */
    public PMException(String message, Integer id) {
        super(message, id);
    }
}