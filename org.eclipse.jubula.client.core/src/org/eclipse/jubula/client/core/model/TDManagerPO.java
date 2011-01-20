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
package org.eclipse.jubula.client.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.lang.Validate;
import org.eclipse.jubula.client.core.businessprocess.TestDataBP;
import org.eclipse.jubula.client.core.businessprocess.progress.ElementLoadedProgressListener;
import org.eclipse.jubula.client.core.i18n.Messages;
import org.eclipse.jubula.tools.constants.StringConstants;


/**
 * class to link parameter description to values or references <br>
 * provides methods for handling of testdata
 * 
 * @author BREDEX GmbH
 * @created 08.12.2004
 * 
 */
@Entity
@Table(name = "TD_MANAGER")
@EntityListeners(value = { ElementLoadedProgressListener.class })
class TDManagerPO implements ITDManagerPO {
    /** hibernate OID */
    private transient Long m_id = null;

    /**
     * <code>m_dataTable</code> list with ListWrapperPO objects inside <br>
     * <li>index of list corresponds to dataset number</li>
     * <li>listWrapper object contains the reference to a list with testdata
     * objects
     * <li>constraint: the order of entries in this referenced list must
     * correspond to the order of parameters in parameterList of paramNode</li>
     * <br>
     * 
     */
    private List<IListWrapperPO> m_dataTable = new ArrayList<IListWrapperPO>();
    
    /** 
     * unique id of each parameter to get the assignement between parameter and its testdata
     */
    private List<String> m_uniqueIds = new ArrayList<String>();
    
    /** The ID of the parent project */
    private Long m_parentProjectId = null;

    /** hibernate version id */
    private transient Integer m_version = null;

    /**
     * @param node
     *            corresponding node to TDManagerPO
     */
    TDManagerPO(IParameterInterfacePO node) {
        Validate.notNull(node);
        createUniqueIds(node);
    }

    /**
     * @param node corresponding node to TDManagerPO
     * @param uniqueIds the uniqueIds
     */
    TDManagerPO(IParameterInterfacePO node, List<String> uniqueIds) {
        Validate.notNull(node);
        setUniqueIds(uniqueIds);
    }
    
    /**
     * private constructor only for hibernate
     */
    TDManagerPO() {
        // nothing so far
    }
    
    /**
     * create the list with ids of all parameters
     * 
     * @param node The Parameter Interface from which Parameters are determined.
     */
    private void createUniqueIds(IParameterInterfacePO node) {
        List<IParamDescriptionPO> params = 
            node.getParameterList();
        for (IParamDescriptionPO param : params) {
            getUniqueIds().add(param.getUniqueId());
        }
    }
    
    /**
     * 
     * @return Id
     */
    @Id
    @GeneratedValue
    public Long getId() {
        return m_id;
    }
    
    /**
     * @param id The id to set.
     */
    void setId(Long id) {
        m_id = id;
    }

    /**
     *    
     * {@inheritDoc}
     */
    @Transient
    public Long getParentProjectId() {
        return getHbmParentProjectId();
    }

    /**
     * 
     * {@inheritDoc}
     */
    public void setParentProjectId(Long projectId) {
        setHbmParentProjectId(projectId);
        for (IListWrapperPO lWrapperPO : getDataTable()) {
            lWrapperPO.setParentProjectId(projectId);
        }
    }

    /**
     *    
     * {@inheritDoc}
     */
    @Basic
    @Column(name = "PARENT_PROJ")
//    @Index(name = "PI_TD_MANAGER_PARENT_PROJ")
    Long getHbmParentProjectId() {
        return m_parentProjectId;
    }

    /**
     * 
     * {@inheritDoc}
     */
    void setHbmParentProjectId(Long projectId) {
        m_parentProjectId = projectId;
    }

    /**
     * Only use this method for internal purposes!</b>
     * 
     * @return Returns the dataTable.
     */
    @OneToMany(cascade = CascadeType.ALL, 
               orphanRemoval = true, 
               targetEntity = ListWrapperPO.class,
               fetch = FetchType.EAGER)
    @OrderColumn(name = "IDX")
    public List<IListWrapperPO> getDataTable() {
        return m_dataTable;
    }

    /**
     * only for hibernate
     * @param dataTable The dataTable to set.
     */
    void setDataTable(List<IListWrapperPO> dataTable) {
        m_dataTable = dataTable;
    }

    /**
     * deletes the dataset with specified number from datatable shifts all
     * following datasets to the next lower number
     * 
     * @param number
     *            number of dataset to delete
     */
    public void removeDataSet(int number) {
        getDataTable().remove(number);
    }
    
    /**
     * Deletes the values for the parameter with the given id
     * in all rows. If the data set rows are empty after this operation, they
     * will be deleted too.
     * 
     * @param uniqueId
     *            The unique id of the parameter the data to delete
     */
    public void removeColumn(String uniqueId) {
        int index = findColumnForParam(uniqueId);
        if (index >= 0) {
            for (IListWrapperPO dataSet : getDataSets()) {
                dataSet.removeColumn(index);
            }
            if (getColumnCount() == 0) {
                getDataTable().clear();
            }
        }
    }

    /**
     * Creates new rows with empty test data.
     * @param row The new row count
     */
    private void expandRows(int row) {
        int colCount = getColumnCount();
        while (row >= getDataTable().size()) {
            List <ITestDataPO> columns =
                new ArrayList <ITestDataPO> (colCount);
            for (int i = 0; i < colCount; i++) {
                columns.add(TestDataBP.instance().createEmptyTestData());
            }
            IListWrapperPO listW = PoMaker.createListWrapperPO(columns);
            getDataTable().add(listW);
            listW.setParentProjectId(getParentProjectId());
        }
    }
    
    /**
     * Inserts a new empty row (data set) at the given position.
     * @param position the position to insert.
     */
    public void insertDataSet(int position) {
        int colCount = getColumnCount();
        if (position < getDataTable().size()) {
            List <ITestDataPO> columns =
                new ArrayList <ITestDataPO> (colCount);
            for (int i = 0; i < colCount; i++) {
                columns.add(TestDataBP.instance().createEmptyTestData());
            }
            IListWrapperPO listW = PoMaker.createListWrapperPO(columns);
            getDataTable().add(position, listW);
            listW.setParentProjectId(getParentProjectId());
        } else {
            expandRows(position);
        }
    }
    
    
    /**
     * Creates new columns in all rows with empty test data.
     * 
     * @param column
     *            The new column count
     */
    private void expandColumns(int column) {
        while (column >= getColumnCount()) {
            for (IListWrapperPO dataSet : getDataSets()) {
                dataSet.addColumn(TestDataBP.instance().createEmptyTestData());
            }
        }
    }
    /**
     * reads a single testdata object with value or reference for a specified
     * dataset row and the parameter name.
     * 
     * @param dataSetRow
     *            dataSetRow of dataset
     * @param uniqueId
     *            unique id of parameter, which is wanted the value/reference for
     * @return testdata object for parameter in specified dataset or null, if no
     *         testdata object is available
     * @throws IllegalArgumentException
     *             If the parameter with the userdefined name
     *             <code>paramName</code> doesn't exist
     */
    public ITestDataPO getCell(int dataSetRow, String uniqueId)
        throws IllegalArgumentException {
        int index = getUniqueIds().indexOf(uniqueId);
        if (index == -1) {
            throw new IndexOutOfBoundsException(Messages.ParameterWithUniqueId 
                    + StringConstants.SPACE + uniqueId + StringConstants.SPACE 
                    + Messages.IsNotAvailable + StringConstants.DOT);
        }
        return getCell(dataSetRow, index);
    }
    
    /**
     * Gets a test data entry at the specified row and column indices.
     * 
     * @param row
     *            The row
     * @param column
     *            The column
     * @return The test data
     */
    public ITestDataPO getCell(int row, int column) {
        return getDataSet(row).getColumn(column);
    }
    
    /**
     * {@inheritDoc}
     */
    public ITestDataPO getCell(int dataSetRow, IParamDescriptionPO parameter)
        throws IllegalArgumentException {

        return getCell(dataSetRow, parameter.getUniqueId());
    }
    
    /**
     * reads a single dataset with specified dataSetRow <br>
     * <p>
     * <b>usage: </b> <br>
     * for restore of a single dataset
     * 
     * @param dataSetRow
     *            dataSetRow of wanted dataset 
     * @return the list with testdata objects for specified dataset or null
     */
    public IListWrapperPO getDataSet(int dataSetRow) {
        return getDataTable().get(dataSetRow);
    }

    /**
     * Reads all datasets of a node.
     * 
     * @return The list of data sets or an empty list if the manager is empty.
     */
    @Transient
    public List<IListWrapperPO> getDataSets() {
        return Collections.unmodifiableList(getDataTable());
    }
    
    /**
     * Updates the test data at the specified row and column. The data in the
     * passed test data instance are copied into the test data in the specified
     * cell. If the row and/or column are greater than the existing row/column
     * count, new rows/columns are created automatically.
     * 
     * @param testData
     *            The test data to update
     * @param row
     *            The row
     * @param column
     *            The column
     */
    public void updateCell(ITestDataPO testData, int row, int column) {
        expandRows(row);
        expandColumns(column);
        ITestDataPO td = getCell(row, column);
        if (testData != null) {
            td.setValue(testData.getValue());
        } else {
            td.setValue(null);
        }
    }
    /**
     * Updates the test data at the specified row and parameter name. The data
     * in the passed test data instance are copied into the test data in the
     * specified cell. If the row and/or column are greater than the existing
     * row/column count, new rows/columns are created automatically.
     * 
     * @param testData
     *            The test data to update
     * @param row
     *            The row
     * @param uniqueId
     *            uniqueId of the parameter
     */
    public void updateCell(ITestDataPO testData, int row, String uniqueId) {
        int index = getUniqueIds().indexOf(uniqueId);
        if (index > -1) {
            updateCell(testData, row, index);
        }
    }
    /**
     * @return number of datasets 
     */
    @Transient
    public int getDataSetCount() {
        return getDataTable().size();
    }
    /**
     * @return The number of columns
     */
    @Transient
    public int getColumnCount() {
        int columns = 0;
        try {
            List<IListWrapperPO> dataTable = getDataTable();
            if (dataTable.size() > 0) {
                IListWrapperPO listW = dataTable.get(0);
                columns = listW.getList().size();
            }
        
        } catch (IndexOutOfBoundsException e) { // NOPMD by al on 3/19/07 1:37 PM
            // Nothing to be done
        }
        return columns;
    }

    /**
     * Copies the data of this TDManager to the given TDManager
     * @param tdMan the TDManager to copy the data to
     * @return the given TDManager with the new data.
     */
    public ITDManagerPO deepCopy(ITDManagerPO tdMan) {
        for (String uniqueId : getUniqueIds()) {
            tdMan.addUniqueId(uniqueId);
        }
        tdMan.getDataTable().clear();
        for (IListWrapperPO dataSet : getDataSets()) {
            List<ITestDataPO> newRow = new ArrayList<ITestDataPO> (
                    dataSet.getColumnCount());
            for (ITestDataPO testData : dataSet.getList()) {
                newRow.add(testData.deepCopy());
            }
            tdMan.getDataTable().add(PoMaker.createListWrapperPO(newRow));
        }
        return tdMan;
    }
    
    /**
     * Clears this TDManager. Removes all TestData!
     */
    public void clear() {
        getDataTable().clear();
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Version
    public Integer getVersion() {        
        return m_version;
    }

    /** 
     * {@inheritDoc}
     */
    void setVersion(Integer version) {
        m_version = version;
    }
    
    
    /**
     *          
     * {@inheritDoc}
     */
    @Transient
    public String getName() {
        return toString();
    }

    /**
     * 
     * @return the uniqueIds
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TD_MANAGER_PARAM_ID")
    @Column(name = "UNIQUE_ID")
    @OrderColumn(name = "IDX")
    @JoinColumn(name = "FK_TD_MANAGER")
    public List<String> getUniqueIds() {
        return m_uniqueIds;
    }
    
    /**
     * @param uniqueId unique id of parameter to find the column in datatable
     * @return the column contains values for given parameter or -1, if param is not contained in datatable
     */
    public int findColumnForParam(String uniqueId) {
        return getUniqueIds().indexOf(uniqueId);
    }

    /**
     * @param uniqueIds the uniqueIds to set
     */
    private void setUniqueIds(List<String> uniqueIds) {
        m_uniqueIds = uniqueIds;
    }
    
    /**
     * @param uniqueId uniqueId of a new parameter (independent of display order)
     */
    public void addUniqueId(String uniqueId) {
        getUniqueIds().add(uniqueId);
    }
    
    /**
     * clears the unique ids list
     */
    public void clearUniqueIds() {
        getUniqueIds().clear();
    }
    
    /**
     * removes the parameter id and its testdata
     * @param uniqueId id of parameter to remove
     */
    public void removeUniqueId(String uniqueId) {
        if (getUniqueIds().contains(uniqueId)) {
            removeColumn(uniqueId);
            getUniqueIds().remove(uniqueId);
        }
    }

}