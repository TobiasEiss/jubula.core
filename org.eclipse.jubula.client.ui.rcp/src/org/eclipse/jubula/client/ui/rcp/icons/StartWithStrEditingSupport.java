/**
 * 
 */
package org.eclipse.jubula.client.ui.rcp.icons;

import org.eclipse.jface.viewers.TableViewer;

/**
 * @author keekx
 *
 */
public class StartWithStrEditingSupport extends IconPathEditingSupport {

    /**
     * the viewer
     */
    private final TableViewer m_tableViewer;
    
    /**
     * @param viewer viewer
     */
    public StartWithStrEditingSupport(TableViewer viewer) {
        super(viewer);
        m_tableViewer = viewer;
    }
    
    @Override
    protected Object getValue(Object element) {
        return ((Icon) element).getRegex();
    }
    
    @Override
    protected void setValue(Object element, Object value) {
        ((Icon) element).setRegex(String.valueOf(value));
        m_tableViewer.update(element, null);
    }

}
