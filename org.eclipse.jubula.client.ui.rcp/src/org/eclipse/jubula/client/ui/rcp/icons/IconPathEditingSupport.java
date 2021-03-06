/**
 * 
 */
package org.eclipse.jubula.client.ui.rcp.icons;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

/**
 * @author keekx
 *
 */
public class IconPathEditingSupport extends EditingSupport {

    /** the viewer */
    private final TableViewer m_viewer;
    
    /**
     * @param viewer TableViewer
     */
    public IconPathEditingSupport(TableViewer viewer) {
        super(viewer);
        m_viewer = viewer;
    }
    
    @Override
    protected CellEditor getCellEditor(Object element) {
        return new TextCellEditor(m_viewer.getTable());
    }

    @Override
    protected boolean canEdit(Object element) {
        return true;
    }

    @Override
    protected Object getValue(Object element) {
        return ((Icon) element).getIconPath();
    }

    @Override
    protected void setValue(Object element, Object value) {
        ((Icon) element).setIconPath(String.valueOf(value));
        m_viewer.update(element, null);
    }

}
