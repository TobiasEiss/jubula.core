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
package org.eclipse.jubula.client.ui.views;

import java.util.List;

import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jubula.client.core.events.DataEventDispatcher;
import org.eclipse.jubula.client.core.events.DataEventDispatcher.IDataChangedListener;
import org.eclipse.jubula.client.core.events.DataEventDispatcher.IProjectLoadedListener;
import org.eclipse.jubula.client.core.events.DataEventDispatcher.IResetFrameColourListener;
import org.eclipse.jubula.client.core.model.INodePO;
import org.eclipse.jubula.client.core.persistence.GeneralStorage;
import org.eclipse.jubula.client.ui.Plugin;
import org.eclipse.jubula.client.ui.constants.Constants;
import org.eclipse.jubula.client.ui.constants.IconConstants;
import org.eclipse.jubula.client.ui.constants.Layout;
import org.eclipse.jubula.client.ui.controllers.AbstractPartListener;
import org.eclipse.jubula.client.ui.controllers.TreeIterator;
import org.eclipse.jubula.client.ui.editors.AbstractTestCaseEditor;
import org.eclipse.jubula.client.ui.filter.GDBrowserPatternFilter;
import org.eclipse.jubula.client.ui.filter.GDFilteredTree;
import org.eclipse.jubula.client.ui.model.ExecTestCaseGUI;
import org.eclipse.jubula.client.ui.model.GuiNode;
import org.eclipse.jubula.client.ui.model.SpecTestCaseGUI;
import org.eclipse.jubula.client.ui.model.TestSuiteGUI;
import org.eclipse.jubula.client.ui.sorter.GuiNodeNameViewerSorter;
import org.eclipse.jubula.client.ui.utils.NodeSelection;
import org.eclipse.jubula.client.ui.utils.ResetColourAdapter;
import org.eclipse.jubula.client.ui.utils.Utils;
import org.eclipse.jubula.tools.i18n.I18n;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;


/**
 * @author BREDEX GmbH
 * @created 21.10.2005
 */
public abstract class AbstractGDTreeView extends ViewPart implements
    IProjectLoadedListener, IDataChangedListener {
    /** Default expansion for the tree */
    public static final int DEFAULT_EXPANSION = 2;
    /** number of columns = 1 */
    protected static final int NUM_COLUMNS_1 = 1;  
    /** vertical spacing = 2 */
    protected static final int VERTICAL_SPACING = 3;
    
    /**
     * <code>m_treeViewer</code>tree Viewer
     */
    private TreeViewer m_treeViewer;
    
    /**
     * <code>m_treeFilterText</code>tree Viewer
     */
    private Text m_treeFilterText;
    
    /**
     * <code>m_selElemList</code> list of selected elements. Used for restoring
     * selection state.
     */
    private List<NodeSelection> m_selElemList;

    /**
     * <code>m_expElemList</code> list of expanded elements. Used for restoring
     * expansion state.
     */
    private List<Object> m_expElemList;
    
    /** adapter to set the frame colour
     * please initialize the ColourAdapter in the creatPartControl()-method
     */
    private ResetColourAdapter m_colourAdapter;
    
    /** the parent composite */
    private Composite m_parentComposite;
    
    /** observation of events need a reset of frame colour */
    @SuppressWarnings("synthetic-access") 
    private IResetFrameColourListener m_resetFrameColourListener = 
        new IResetFrameColourListener() {
        
            public void eventOccured(List< ? extends Object> params) {
                m_colourAdapter.resetColouredFrame();
            }
            public void checkGenericListElementType(
                    List< ? extends Object> params) {
            // do nothing
            }
        };

    /** 
     * This part's reference to the clipboard.
     * Note that the part shares this clipboard with the entire operating 
     * system, and this instance is only for easier access to the clipboard. 
     * The clipboard does not exclusively belong to the part.
     */
    private Clipboard m_clipboard;
    
    /** flag wether the view is linked with the editor or not */
    private boolean m_isLinkedWithEditor = false;

    /** The partListener of this view */
    private PartListener m_partListener = new PartListener();

    /**
     * This listener updates the selection of the view based on the activated
     * part.
     * 
     * @author BREDEX GmbH
     * @created 20.09.2006
     */
    private final class PartListener extends AbstractPartListener {
        /**
         * {@inheritDoc}
         */
        public void partActivated(IWorkbenchPart part) {
            setSelectionToEditorNode(part);
            super.partActivated(part);
        }
    }
    

    /**
     * Toggles "Link with Editor" functionality for this view. When linked,
     * the selection of the view can be changed automatically based on the 
     * currently active editor.
     * 
     * @author BREDEX GmbH
     * @created Nov 8, 2006
     */
    private final class ToggleLinkingAction extends Action {
        
        /**
         * Constructor
         */
        public ToggleLinkingAction() {
            super(I18n.getString("TestCaseBrowser.LinkWithEditor"), IAction.AS_CHECK_BOX); //$NON-NLS-1$
            setImageDescriptor(IconConstants.LINK_WITH_EDITOR_IMAGE_DESCRIPTOR);
            m_isLinkedWithEditor = Plugin.getDefault().getPreferenceStore()
                .getBoolean(Constants.LINK_WITH_EDITOR_TCVIEW_KEY);
            setChecked(m_isLinkedWithEditor);
        }
        
        /**
         * {@inheritDoc}
         */
        public void run() {
            m_isLinkedWithEditor = isChecked();
            Plugin.getDefault().getPreferenceStore().setValue(
                    Constants.LINK_WITH_EDITOR_TCVIEW_KEY, 
                    m_isLinkedWithEditor);
            if (Plugin.getActiveEditor() instanceof AbstractTestCaseEditor
                && m_isLinkedWithEditor) {
                
                setSelectionToEditorNode(Plugin.getActiveEditor());
            }
        }
    }
    
    /**
     * Sets the selection to the node of the current active editor if the
     * linking is enabled
     * @param part the current activated IWorkbenchPart
     */
    private void setSelectionToEditorNode(IWorkbenchPart part) {
        if (part != null) {
            Object obj = part.getAdapter(AbstractTestCaseEditor.class);
            AbstractTestCaseEditor tce = (AbstractTestCaseEditor)obj;
            if (obj != null && m_isLinkedWithEditor && tce != null 
                    &&  tce.getEditorInputGuiNode() != null) {
                INodePO editorNode = tce.getEditorInputGuiNode().getContent();
                
                // check if node already selected
                StructuredViewer v = getTreeViewer();
                if (v != null) {
                    ISelection treeSelection = v.getSelection();
                    if (treeSelection instanceof IStructuredSelection) {
                        IStructuredSelection selection = 
                            (IStructuredSelection) treeSelection;
                        if (selection.size() == 1) {
                            Object firstElement = selection.getFirstElement();
                            if (firstElement instanceof GuiNode) {
                                GuiNode gn = (GuiNode) firstElement;
                                INodePO content = gn.getContent();
                                if (content != null
                                        && content.equals(editorNode)) {
                                    v.reveal(selection.getFirstElement());
                                    return;
                                }
                            }
                        }
                    }
                    // search for node in GDTreeViewer and select it
                    Object viewerInput = v.getInput();
                    if (viewerInput instanceof GuiNode) {
                        TreeIterator iter = new TreeIterator(
                                (GuiNode) viewerInput, new Class[] {
                                    SpecTestCaseGUI.class,
                                    TestSuiteGUI.class });
                        while (iter.hasNext()) {
                            GuiNode currentNode = iter.next();
                            if (currentNode != null) {
                                INodePO content = currentNode.getContent();
                                if (!(currentNode.getParentNode() 
                                        instanceof ExecTestCaseGUI)
                                        && content != null
                                        && content.equals(editorNode)) {
                                    v.refresh();
                                    v.setSelection(new StructuredSelection(
                                            currentNode));
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 
     */
    protected abstract void rebuildTree();

    /**
     * {@inheritDoc}
     */
    public Object getAdapter(Class adapter) {
        if (adapter.equals(AbstractGDTreeView.class)) {
            return this;
        } else if (adapter.equals(ResetColourAdapter.class)) {
            return m_colourAdapter;
        } else if (adapter.equals(IPropertySheetPage.class)) {
            return new GDPropertiesView(false, null);
        }
        return super.getAdapter(adapter);
    }

    /**
     * 
     * {@inheritDoc}
     */
    public final void handleProjectLoaded() {
        if (GeneralStorage.getInstance().getProject() == null) {
            // project-loaded fired for clearing the current project
            // do not rebuild the tree
            return;
        }
        Plugin.startLongRunning();
        
        try {
            Plugin.getDisplay().syncExec(new Runnable() {
                public void run() {
                    m_selElemList = Utils.getSelectedTreeItems(
                            getTreeViewer());
                    m_expElemList = 
                        Utils.getExpandedTreeItems(getTreeViewer());
                }
            });
            rebuildTree();
            Plugin.getDisplay().syncExec(new Runnable() {
                public void run() {
                    Utils.restoreTreeState(
                            getTreeViewer(), m_expElemList, m_selElemList);
                }
            });
        } catch (OperationCanceledException oce) {
            Plugin.getDisplay().syncExec(new Runnable() {
                public void run() {
                    getTreeViewer().setInput(null);
                }
            });
        } finally {
            Plugin.getDisplay().syncExec(new Runnable() {
                public void run() {
                    getTreeViewer().refresh();
                }
            });
            Plugin.stopLongRunning();
        }
    }

    /**
     * @return tree Viewer
     */
    public TreeViewer getTreeViewer() {
        return m_treeViewer;
    }

    /**
     * @return selection
     */
    public ISelection getSelection() {
        return m_treeViewer.getSelection();
    }

    /**
     * @param selection selection to set
     */
    public void setSelection(ISelection selection) {
        m_treeViewer.setSelection(selection, true);
    }

    /**
     * @param treeViewer The treeViewer to set.
     */
    public void setTreeViewer(TreeViewer treeViewer) {
        m_treeViewer = treeViewer;
    }
    
    /**
     * {@inheritDoc}
     */
    public void createPartControl(Composite parent) {
        m_parentComposite = parent;
        setColourAdapter(new ResetColourAdapter(parent)); 
        m_clipboard = new Clipboard(parent.getDisplay());
        
        GridLayout layout = new GridLayout();
        layout.numColumns = NUM_COLUMNS_1;
        layout.verticalSpacing = VERTICAL_SPACING;
        layout.marginWidth = Layout.MARGIN_WIDTH;
        layout.marginHeight = Layout.MARGIN_HEIGHT;
        parent.setLayout(layout);
        
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(layout);
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.FILL_BOTH);
        composite.setLayoutData(gridData);
        
        final FilteredTree ft = new GDFilteredTree(composite, SWT.MULTI
                | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER, 
                new GDBrowserPatternFilter(), true);
        setTreeViewer(ft.getViewer());
        setTreeFilterText(ft.getFilterControl());
        addTreeListener();
        getTreeViewer().setUseHashlookup(true);
        getTreeViewer().setSorter(new GuiNodeNameViewerSorter());
        
        GridData layoutData = new GridData();
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.horizontalAlignment = GridData.FILL;
        layoutData = new GridData();
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.grabExcessVerticalSpace = true;
        layoutData.horizontalAlignment = GridData.FILL;
        layoutData.verticalAlignment = GridData.FILL;
        getTreeViewer().getControl().setLayoutData(layoutData);
        
        getViewSite().setSelectionProvider(getTreeViewer());
        getTreeViewer().setAutoExpandLevel(DEFAULT_EXPANSION);
        
        final DataEventDispatcher dispatcher = 
            DataEventDispatcher.getInstance();
        dispatcher.addProjectLoadedListener(this, false);
        dispatcher.addDataChangedListener(this, true);
        
        getViewSite().getActionBars().getToolBarManager().add(
                new ToggleLinkingAction());
        getViewSite().getWorkbenchWindow().getPartService().addPartListener(
                m_partListener);
        
        setFocus();
    }
    
    /**
     * Adds DoubleClickListener to Treeview.
     */
    protected abstract void addTreeListener();
    
    /**
     * {@inheritDoc}
     */
    public void dispose() {
        try {
            m_clipboard.dispose();
            getViewSite().getWorkbenchWindow().getPartService()
                .removePartListener(m_partListener);
            final DataEventDispatcher dispatcher = 
                DataEventDispatcher.getInstance();
            // clear corresponding views
            dispatcher.firePartClosed(this);
            dispatcher.removeProjectLoadedListener(this);
        } finally {
            getTreeViewer().getTree().dispose();
            getSite().setSelectionProvider(null);
            super.dispose();
        }
    }
    
    /**
     * Sets the selection in the tree to the given node.
     * @param node the node to select.
     */
    public void setSelection(INodePO node) {
        GuiNode nodeGUI = TreeBuilder.getGuiNodeByContent(
            (GuiNode)m_treeViewer.getInput(), node);
        if (nodeGUI == null) {
            return;
        }
        ISelection selection = new StructuredSelection(nodeGUI);
        m_treeViewer.setSelection(selection, true);
    }
    
    /**
     * @return the root guiNode of the tree of the actual treeViewer
     */
    public GuiNode getRootGuiNode() {
        return (GuiNode)m_treeViewer.getInput();
    }
    
    /**
     * @param po the actual nodePO
     * @return the quantity of nodes, where the nodePO is used.
     */
    public int countNodePOsInTree(INodePO po) {
        return new TreeIterator(getRootGuiNode()).getGuiNodeOfNodePO(po).size();
    }

    /**
     * @param colourAdapter the colourAdapter to set
     */
    protected void setColourAdapter(ResetColourAdapter colourAdapter) {
        m_colourAdapter = colourAdapter;
    }

    /**
     * 
     * @return a reference to the clipboard.
     */
    public Clipboard getClipboard() {
        return m_clipboard;
    }
    
    /**
     * @return The parent composite of this workbench part.
     */
    public Composite getParentComposite() {
        return m_parentComposite;
    }

    /**
     * @param treeFilterText the treeFilterText to set
     */
    public void setTreeFilterText(Text treeFilterText) {
        m_treeFilterText = treeFilterText;
    }

    /**
     * @return the treeFilterText
     */
    public Text getTreeFilterText() {
        return m_treeFilterText;
    }
}