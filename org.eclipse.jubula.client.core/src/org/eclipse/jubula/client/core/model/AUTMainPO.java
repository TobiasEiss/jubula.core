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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.eclipse.jubula.client.core.persistence.PersistenceUtil;
import org.eclipse.jubula.tools.constants.StringConstants;
import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

/**
 * @author BREDEX GmbH
 * @created 27.01.2005
 */
@Entity
@Table(name = "AUT")
class AUTMainPO implements IAUTMainPO {
    /** Persistence (JPA / EclipseLink) OID */
    private transient Long m_id = null;

    /** Globally Unique Identifier for recognizing nodes across databases */
    private transient String m_guid = null;

    /** AUT name */
    private String m_autName;
    
    /** information about the used Toolkit (Swing(=default), Swt, Web, ...) */
    private String m_toolkit = null;
    
    /** the set of aut configurations */
    private Set<IAUTConfigPO> m_autConfigSet = new HashSet<IAUTConfigPO>();
    
    /** the AUT IDs associated with this AUT */
    private List<String> m_autIDs = new ArrayList<String>();

    /** Persistence (JPA / EclipseLink) version id */
    private transient Integer m_version = null;
    
    /**
     * the object mapping for the test suite
     */
    private IObjectMappingPO m_objMap = null;

    /**
     * <code>m_autLanguageSet</code> set of AUT languages
     */
    private Set<String> m_autLanguageSet = new TreeSet<String>();

    /**
     * <code>m_langHelper</code> helper for language management
     */
    private LanguageHelper m_langHelper;
    
    /** The ID of the parent project */
    private Long m_parentProjectId = null;
    
    /** flag to indicate that for this AUT some names should be generated */
    private boolean m_generateNames;

    /**
     * <code>m_isModified</code> flag to signal modification of language list
     * by Persistence (JPA / EclipseLink)
     */
    private transient boolean m_isModified = true;
    
    /**
     * only for Persistence (JPA / EclipseLink)
     */
    AUTMainPO() {
        m_langHelper = new LanguageHelper(this);
    }

    /**
     * The contructor.
     * @param autName The name of this AUT.
     */
    AUTMainPO(String autName) {
        this(autName, PersistenceUtil.generateGuid());
    }

    /**
     * The contructor.
     * @param autName The name of this AUT.
     * @param guid The GUID of this AUT.
     */
    AUTMainPO(String autName, String guid) {
        m_autName = autName;
        m_objMap = PoMaker.createObjectMappingPO();
        m_objMap.setParentProjectId(getParentProjectId());
        m_langHelper = new LanguageHelper(this);
        m_guid = guid;
        m_generateNames = false;
    }

    /**
     * only for Persistence (JPA / EclipseLink)
     * 
     * @return Returns the id.
     */
    @Id
    @GeneratedValue
    public Long getId()  {
        return m_id;
    }
    /**
     * only for Persistence (JPA / EclipseLink)
     * 
     * @param id The id to set.
     */
    void setId(Long id) {
        m_id = id;
    }
    /**
     * 
     * @return Returns the autName.
     */
    @Basic
    @Column(name = "NAME", length = 4000)
    public String getName() {
        return m_autName;
    }

    /**
     * @param autName
     *            The autName to set.
     */
    public void setName(String autName) {
        m_autName = autName;
    }
    
    /**
     * 
     * @return Returns the autConfigSet.
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, 
               targetEntity = AUTConfigPO.class, orphanRemoval = true)
    public Set<IAUTConfigPO> getAutConfigSet() {
        return m_autConfigSet;
    }

    /**
     * only for Persistence (JPA / EclipseLink)
     * @param autConfigSet The autConfigSet to set.
     */
    void setAutConfigSet(Set<IAUTConfigPO> autConfigSet) {
        m_autConfigSet = autConfigSet;
    }
    
    /**
     * Adds a aut configuration to the set.
     * @param autConfig The aut configuration to add.
     */
    public void addAutConfigToSet(IAUTConfigPO autConfig) {
        getAutConfigSet().add(autConfig);
        autConfig.setParentProjectId(getParentProjectId());
    }
    
    /**
     * Removes a aut configuratio from the list.
     * @param autConfig The autConfig to remove.
     */
    public void removeAutConfig(IAUTConfigPO autConfig) {
        m_autConfigSet.remove(autConfig);
    }
    /**
     * only for Persistence (JPA / EclipseLink) !!!
     * 
     * @return Returns the languageList.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "AUT_LANGUAGES", 
                     joinColumns = @JoinColumn(name = "AUT"))
    @Column(name = "LANGUAGE")
    @OrderBy
    private Set<String> getAUTLanguageList() {
        return m_autLanguageSet;
    }
    
    /**
     * @deprecated
     * only to use by LanguageHelper
     * @return language set
     */
    @Transient
    public Set<String> getHbmLanguageList() {
        return getAUTLanguageList();
    }
    
    /**
     * only for Persistence (JPA / EclipseLink) !!!
     * set the languagelist
     * @param langList languageSet from database
     */
    @SuppressWarnings("unused")
    private void setAUTLanguageList(Set<String> langList) {
        m_autLanguageSet = langList;
        m_isModified = true;
    }


    /**
     * 
     * @return Returns the objMap.
     */
    @OneToOne(cascade = CascadeType.ALL, 
              fetch = FetchType.EAGER, 
              targetEntity = ObjectMappingPO.class)
    @BatchFetch(value = BatchFetchType.JOIN)
    private IObjectMappingPO getHbmObjMap() {
        return m_objMap;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Transient
    public IObjectMappingPO getObjMap() {
        return getHbmObjMap();
    }

    /**
     * 
     * @param objMap The objMap to set.
     */
    private void setHbmObjMap(IObjectMappingPO objMap) {
        m_objMap = objMap;
    }
    
    /**
     * 
     * @param objMap The objMap to set.
     */
    public void setObjMap(IObjectMappingPO objMap) {
        objMap.setParentProjectId(getParentProjectId());
        setHbmObjMap(objMap);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return super.toString() + StringConstants.SPACE 
            + StringConstants.LEFT_PARENTHESES + m_autName 
            + StringConstants.RIGHT_PARENTHESES;
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
     * @param version version
     */
    @SuppressWarnings("unused")
    private void setVersion(Integer version) {
        m_version = version;
    }
    
    /**
     * 
     * {@inheritDoc}
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AUTMainPO || obj instanceof IAUTMainPO)) {
            return false;
        }
        IAUTMainPO o = (IAUTMainPO)obj;

        return getGuid().equals(o.getGuid());
    }

    /**
     * 
     * {@inheritDoc}
     * @return
     */
    public int hashCode() {
        return getGuid().hashCode();
    }

    /**
     * @return Returns the langHelper.
     */
    @Transient
    public LanguageHelper getLangHelper() {
        return m_langHelper;
    }

    /**
     * @deprecated
     * only to use by LanguageHelper
     * {@inheritDoc}
     * @param lang
     */
    public void addLangToList(String lang) {
        getAUTLanguageList().add(lang);        
    }

    /**
     * @deprecated
     * only to use by LanguageHelper
     * {@inheritDoc}
     */
    public void clearLangList() {
        getAUTLanguageList().clear();
    }
    
    /**
     * @deprecated
     * only to use by LanguageHelper
     * {@inheritDoc}
     * @param lang
     */
    public void removeLang(String lang) {
        getAUTLanguageList().remove(lang);
    }

    /**
     * @deprecated
     * only to use by LanguageHelper
     * @return Returns the isModified.
     */
    @Transient
    public boolean isModified() {
        return m_isModified;
    }

    /**
     * @deprecated
     * only to use by LanguageHelper
     * @param isModified The isModified to set.
     */
    public void setModified(boolean isModified) {
        m_isModified = isModified;
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(Object o) {
        IAUTMainPO aut = (IAUTMainPO)o;
        return this.getName().compareTo(aut.getName());
    }

    /**
     * 
     * @return the GUID.
     */
    @Basic
    @Column(name = "GUID")
    public String getGuid() {
        return m_guid;
    }
    /**
     * only for Persistence (JPA / EclipseLink)
     * @param guid The guid to set.
     */
    void setGuid(String guid) {
        m_guid = guid;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Transient
    public String getToolkit() {
        return getHbmToolkit();
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
        for (IAUTConfigPO configPO : getAutConfigSet()) {
            configPO.setParentProjectId(projectId);
        }
        if (getObjMap() != null) {
            getObjMap().setParentProjectId(projectId);
        }
    }

    /**
     *    
     * {@inheritDoc}
     */
    @Basic
    @Column(name = "PARENT_PROJ")
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
     * only for Persistence (JPA / EclipseLink) !!!
     *    
     * @return the toolkit
     */
    @Basic
    @Column(name = "TOOLKIT")
    private String getHbmToolkit() {
        return m_toolkit;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public void setToolkit(String toolkit) {
        setHbmToolkit(toolkit);
    }
    
    /**
     * @param toolkit the toolkit
     */
    private void setHbmToolkit(String toolkit) {
        m_toolkit = toolkit;
    }

    /**
     * @param generateNames the generateNames to set
     */
    public void setGenerateNames(boolean generateNames) {
        m_generateNames = generateNames;
    }

    /**
     * 
     * @return the generateNames
     */
    @Basic
    @Column(nullable = false)
    public boolean isGenerateNames() {
        return m_generateNames;
    }

    /**
     * 
     * @return the AUT ID list.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "AUT_IDS", 
                     joinColumns = @JoinColumn(name = "FK_AUT"))
    @Column(name = "AUT_ID_STRING")
    public List<String> getAutIds() {
        return m_autIDs;
    }

    /**
     * For Persistence (JPA / EclipseLink).
     * 
     * @param autIds The AUT IDs to set.
     */
    @SuppressWarnings("unused")
    private void setAutIds(List<String> autIds) {
        m_autIDs = autIds;
    }
}
