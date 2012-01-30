/*******************************************************************************
 * Copyright (c) 2004, 2012 BREDEX GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BREDEX GmbH - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.jubula.client.core.tests.parameter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.jubula.client.core.gen.parser.parameter.lexer.LexerException;
import org.eclipse.jubula.client.core.gen.parser.parameter.node.Start;
import org.eclipse.jubula.client.core.gen.parser.parameter.parser.Parser;
import org.eclipse.jubula.client.core.gen.parser.parameter.parser.ParserException;
import org.eclipse.jubula.client.core.parser.parameter.JubulaParameterLexer;
import org.eclipse.jubula.client.core.utils.ParsedParameter;
import org.eclipse.jubula.client.core.utils.SemanticParsingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestParameterInput {

    private String m_inputString;
    
    private boolean m_inputIsValid;
    
    public TestParameterInput(String inputString, boolean inputIsValid) {
        m_inputString = inputString;
        m_inputIsValid = inputIsValid;
    }
    
    @SuppressWarnings("nls")
    @Test
    public void testParameterInput() throws IOException {
        Parser parser = new Parser(new JubulaParameterLexer(new PushbackReader(
                new StringReader(m_inputString))));
        try {
            Start ast = parser.parse();
            ParsedParameter param = 
                    new ParsedParameter(true, null, null);
            ast.apply(param);
            assertTrue(m_inputString + " succeeded unexpectedly.", m_inputIsValid);
        } catch (ParserException e) {
            assertFalse(m_inputString + " failed unexpectedly.", m_inputIsValid);
        } catch (LexerException e) {
            assertFalse(m_inputString + " failed unexpectedly.", m_inputIsValid);
        } catch (SemanticParsingException spe) {
            assertFalse(m_inputString + " failed unexpectedly.", m_inputIsValid);
        }
    }

    /**
     * 
     * @return the parameters for the tests, in the format:
     *         <table border="1"><tr><td>String: input</td>
     *         <td>boolean: inputIsValid</td></tr></table>
     *          
     */
    @SuppressWarnings("nls")
    @Parameters
    public static Collection<Object[]> createInputStrings() {
        return Arrays.asList(new Object[][] {
                {"", true},
                {"abc123", true},
                {"=PARAMETER", true},
                {"={PARAMETER}", true},
                {"=1", true},
                {"={1}", true},
                {"?abc()", true},
                {"?abc(arg)", true},
                {"?abc(arg1, arg2)", true},
                {"?abc(?abc(arg1, arg2), ={PARAM_ARG}, \\=prefix${VARIABLE_ARG}\\$\\?suffix)", true},
                
                {"=", false},       // missing parameter name
                {"={}", false},       // missing parameter name
                {"$", false},       // missing variable name
                {"${}", false},       // missing variable name
                {"'", false},       // missing closing single quote
                {"?", false},       // missing function body
                {"?(", false},       // missing closing parentheses
                {"?)", false},       // missing opening parentheses
                {"?()", false},     // missing function name
                {"?a", false},       // missing function argument list
                {"?=", false},       // invalid (parameter symbol) after function symbol
                {"??", false},       // invalid (function symbol) after function symbol
                {"?$", false},       // invalid (variable symbol) after function symbol
                {"?''", false},       // invalid (single quote) after function symbol
                {"?abc(?abc(arg1, arg2))", true},
                {"?abc(?abc(arg1, arg2), ={PARAM_ARG}, prefix${VARIABLE_ARG}suffix)", true},
                });
    }
}
