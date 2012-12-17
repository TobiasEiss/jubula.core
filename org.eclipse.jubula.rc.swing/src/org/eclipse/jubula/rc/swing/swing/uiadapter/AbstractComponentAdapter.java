package org.eclipse.jubula.rc.swing.swing.uiadapter;

import org.eclipse.jubula.rc.common.driver.IRobotFactory;
import org.eclipse.jubula.rc.common.uiadapter.interfaces.IComponentAdapter;
import org.eclipse.jubula.rc.swing.driver.RobotFactoryConfig;
/**
 * 
 * @author BREDEX GmbH
 *
 */
public abstract class AbstractComponentAdapter implements IComponentAdapter {

    /** the RobotFactory from the AUT */
    private IRobotFactory m_robotFactory;
    
    /**
     * Gets the Robot factory. The factory is created once per instance.
     *
     * @return The Robot factory.
     */
    public IRobotFactory getRobotFactory() {
        if (m_robotFactory == null) {
            m_robotFactory = new RobotFactoryConfig().getRobotFactory();
        }
        return m_robotFactory;
    }

}