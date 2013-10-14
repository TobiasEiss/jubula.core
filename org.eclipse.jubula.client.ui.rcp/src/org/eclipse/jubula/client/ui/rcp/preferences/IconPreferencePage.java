/**
 * 
 */
package org.eclipse.jubula.client.ui.rcp.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jubula.client.ui.rcp.i18n.Messages;
import org.eclipse.jubula.client.ui.rcp.icons.Icon;
import org.eclipse.jubula.client.ui.rcp.icons.IconModelProvider;
import org.eclipse.jubula.client.ui.rcp.icons.IconPathEditingSupport;
import org.eclipse.jubula.client.ui.rcp.icons.IconRefPathEditingSupport;
import org.eclipse.jubula.client.ui.rcp.icons.StartWithStrEditingSupport;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author keekx
 * 
 */
public class IconPreferencePage extends PreferencePage implements
        IWorkbenchPreferencePage {

    /** provider */
    private IconModelProvider m_provider;
    
    /** the tableViewer */
    private TableViewer m_viewer;

    /**
     * init
     * 
     * @param workbench
     *            the Workbench
     */
    public void init(IWorkbench workbench) {
        setDescription(Messages.IconPropPageDescription);
    }

    @Override
    protected Control createContents(Composite parent) {
        setMessage(Messages.IconPropPageMessage);
        createViewer(parent);
        createButtons(parent);
        m_provider = IconModelProvider.INSTANCE;
        m_viewer.setInput(m_provider.getIcons());
        return parent;
    }

    /**
     * Method declared on IPreferencePage. 
     * 
     * @return performOK
     */
    public boolean performOk() {
        m_provider.storeIcons();
        return super.performOk();
    }
    
    /**
     * @param parent Composite
     */
    private void createButtons(Composite parent) {
        Composite innerComp = new Composite(parent, SWT.NONE);
        FillLayout fillLayout = new FillLayout();
        innerComp.setLayout(fillLayout);
        Button addBtn = new Button(innerComp, SWT.PUSH);
        addBtn.setText("Add"); //$NON-NLS-1$
        addBtn.addSelectionListener(new SelectionListener() {
            
            public void widgetSelected(SelectionEvent e) {
                m_provider.getIcons().add(new Icon("New.gif", "New", "NewRef.gif"));  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
                m_viewer.refresh();
            }
            
            public void widgetDefaultSelected(SelectionEvent e) {
                //empty
            }
        });
        Button remBtn = new Button(innerComp, SWT.PUSH);
        remBtn.setText("Remove"); //$NON-NLS-1$
        remBtn.addSelectionListener(new SelectionListener() {
            
            public void widgetSelected(SelectionEvent e) {
                IStructuredSelection sel = (IStructuredSelection) m_viewer
                        .getSelection();
                Icon icon = (Icon) sel.getFirstElement();
                m_provider.getIcons().remove(icon);
                m_viewer.refresh();
            }
            
            public void widgetDefaultSelected(SelectionEvent e) {
                // empty
            }
        });
    }

    /**
     * @param composite
     *            as parent
     */
    private void createViewer(Composite composite) {
        m_viewer = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns(composite, m_viewer);
        final Table table = m_viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        m_viewer.setContentProvider(new ArrayContentProvider());
        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 1;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        m_viewer.getControl().setLayoutData(gridData);
    }

    /**
     * @param composite parent
     * @param viewer the viewer
     */
    private void createColumns(Composite composite,
            TableViewer viewer) {
        String[] titles = { "Regex", "Icon-name", "Ref-Icon-Name" };  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
        int[] bounds = { 100, 100, 100 };

        TableViewerColumn col = 
                createTableViewerColumn(titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Icon i = (Icon) element;
                return i.getRegex();
            }
        });
        col.setEditingSupport(new StartWithStrEditingSupport(viewer));
        
        col = createTableViewerColumn(titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Icon i = (Icon) element;
                return i.getIconPath();
            }
        });
        col.setEditingSupport(new IconPathEditingSupport(viewer));
        
        col = createTableViewerColumn(titles[2], bounds[2], 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Icon i = (Icon) element;
                return i.getRefIconPath();
            }
        });
        col.setEditingSupport(new IconRefPathEditingSupport(viewer));
    }

    /**
     * @param title from column
     * @param bound of column
     * @param col number
     * @return Column
     */
    private TableViewerColumn createTableViewerColumn(String title, int bound,
            final int col) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(m_viewer,
                SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;
    }

}
