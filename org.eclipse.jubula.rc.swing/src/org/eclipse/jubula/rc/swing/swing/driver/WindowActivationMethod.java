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
package org.eclipse.jubula.rc.swing.swing.driver;

import java.awt.Point;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.InputEvent;

import org.eclipse.jubula.rc.common.CompSystemConstants;
import org.eclipse.jubula.rc.common.driver.IEventThreadQueuer;
import org.eclipse.jubula.rc.common.driver.IRunnable;
import org.eclipse.jubula.rc.common.driver.RobotConfiguration;
import org.eclipse.jubula.rc.common.driver.RobotTiming;
import org.eclipse.jubula.rc.common.exception.StepExecutionException;


/**
 * @author BREDEX GmbH
 * @created 26.01.2006
 */
public abstract class WindowActivationMethod {
    
    /**
     * no operation 
     */
    private static class NoneMethod extends WindowActivationMethod {
        /**
         * constructor
         * @param robot robot
         * @param queuer queuer
         */
        public NoneMethod(Robot robot, IEventThreadQueuer queuer) {
            super(robot, queuer);
        }
        /**
         * {@inheritDoc}
         */
        protected Point getClickPoint(Window window) {
            return null;
        }
        /**
         * {@inheritDoc}
         */
        public void activate(Window window) {
            // do nothing
        }
    }
    /**
     * clicks into the title
     */
    private static class TitleMethod extends WindowActivationMethod {
        /**
         * constructor
         * @param robot robot
         * @param queuer queuer
         */
        public TitleMethod(Robot robot, IEventThreadQueuer queuer) {
            super(robot, queuer);
        }
        /**
         * {@inheritDoc}
         */
        protected Point getClickPoint(Window window) {
            return new Point(window.getWidth() / 2, 3);
        }
    }
    /**
     * clicks into the upper left corner
     */
    private static class NorthWestMethod extends WindowActivationMethod {
        /**
         * constructor
         * @param robot robot
         * @param queuer queuer
         */
        public NorthWestMethod(Robot robot, IEventThreadQueuer queuer) {
            super(robot, queuer);
        }
        /**
         * {@inheritDoc}
         */
        protected Point getClickPoint(Window window) {
            return new Point(0, 0);
        }
    }
    /**
     * clicks into the upper right corner
     */
    private static class NorthEastMethod extends WindowActivationMethod {
        /**
         * constructor
         * @param robot robot
         * @param queuer queuer
         */
        public NorthEastMethod(Robot robot, IEventThreadQueuer queuer) {
            super(robot, queuer);
        }
        /**
         * {@inheritDoc}
         */
        protected Point getClickPoint(Window window) {
            return new Point(window.getWidth() - 1, 0);
        }
    }
    /**
     * clicks into the bottom left corner 
     */
    private static class SouthWestMethod extends WindowActivationMethod {
        /**
         * constructor
         * @param robot robot
         * @param queuer queuer
         */
        public SouthWestMethod(Robot robot, IEventThreadQueuer queuer) {
            super(robot, queuer);
        }
        /**
         * {@inheritDoc}
         */
        protected Point getClickPoint(Window window) {
            return new Point(0, window.getHeight() - 1);
        }
    }
    /**
     * clicks into the center
     */
    private static class CenterMethod extends WindowActivationMethod {
        /**
         * constructor
         * @param robot robot
         * @param queuer queuer
         */
        public CenterMethod(Robot robot, IEventThreadQueuer queuer) {
            super(robot, queuer);
        }
        /**
         * {@inheritDoc}
         */
        protected Point getClickPoint(Window window) {
            return new Point(window.getWidth() / 2, window.getHeight() / 2);
        }
    }
    /**
     * clicks into the bottom right corner 
     */
    private static class SouthEastMethod extends WindowActivationMethod {
        /**
         * constructor
         * @param robot robot
         * @param queuer queuer
         */
        public SouthEastMethod(Robot robot, IEventThreadQueuer queuer) {
            super(robot, queuer);
        }
        /**
         * {@inheritDoc}
         */
        protected Point getClickPoint(Window window) {
            return new Point(window.getWidth() - 1, window.getHeight() - 1);
        }
    }

    /**
     * button used to activate the window
     */
    private static final int ACTIVATE_BTN = InputEvent.BUTTON1_MASK;
    
    /** robot */
    private final Robot m_robot;
    /** queuer */
    private final IEventThreadQueuer m_queuer;
    
    /**
     * constructor
     * @param robot robot
     * @param queuer queuer
     */
    protected WindowActivationMethod(Robot robot, IEventThreadQueuer queuer) {
        m_robot = robot;
        m_queuer = queuer;
    }
    
    /**
     * The point that is clicked to activate the window
     * @param window the window
     * @return the point
     */
    protected abstract Point getClickPoint(Window window);
    
    /**
     * activates the window
     * @param window window
     */
    public void activate(final Window window) {
        m_queuer.invokeAndWait("window activate", new IRunnable() { //$NON-NLS-1$
            public Object run() throws StepExecutionException {
                Point pos = window.getLocationOnScreen();
                Point cp = getClickPoint(window);
                m_robot.mouseMove(pos.x + cp.x, pos.y + cp.y);
                m_robot.mousePress(ACTIVATE_BTN);
                m_robot.mouseRelease(ACTIVATE_BTN);
                RobotTiming.sleepPostWindowActivationDelay();
                return null;
            }
        });
    }
    
    /**
     * creates an activation method
     * @param method a string representation of the method
     * @param robot the robot
     * @param queuer the queuer
     * @return the method
     */
    public static WindowActivationMethod createWindowActivationMethod(
        String method, Robot robot, IEventThreadQueuer queuer) {
        
        if (CompSystemConstants.AAM_AUT_DEFAULT.equals(method)) {
            return createWindowActivationMethod(RobotConfiguration
                    .getInstance().getDefaultActivationMethod(), robot, queuer);
        } else if (CompSystemConstants.AAM_NONE.equals(method)) {
            return new NoneMethod(robot, queuer);
        } else if (CompSystemConstants.AAM_TITLE.equals(method)) {
            return new TitleMethod(robot, queuer);
        } else if (CompSystemConstants.AAM_NORTHWEST.equals(method)) {
            return new NorthWestMethod(robot, queuer);
        } else if (CompSystemConstants.AAM_NORTHEAST.equals(method)) {
            return new NorthEastMethod(robot, queuer);
        } else if (CompSystemConstants.AAM_SOUTHWEST.equals(method)) {
            return new SouthWestMethod(robot, queuer);
        } else if (CompSystemConstants.AAM_SOUTHEAST.equals(method)) {
            return new SouthEastMethod(robot, queuer);
        } else if (CompSystemConstants.AAM_CENTER.equals(method)) {
            return new CenterMethod(robot, queuer);
        } else {
            return new NoneMethod(robot, queuer);
        }
    }

}
