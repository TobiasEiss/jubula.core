/**
 * 
 */
package org.eclipse.jubula.client.ui.rcp.icons;

/**
 * @author keekx
 *
 */
public class Icon {

    /**
     * path to the icon
     */
    private String m_iconpath;
    
    /**
     * ref icon Path
     */
    private String m_refIconpath;
    
    /**
     * the String as regex
     */
    private String m_regex;
    
    /**
     * @param iconpath path to icon
     * @param regex start with string
     * @param refIconPath ref icon
     */
    public Icon(String iconpath, String regex, String refIconPath) {
        super();
        m_iconpath = iconpath;
        m_regex = regex;
        m_refIconpath = refIconPath;
    }
    
    /**
     * @return iconpath
     */
    public String getIconPath() {
        return m_iconpath;
    }
    
    /**
     * @param iconPath new Path to icon
     */
    public void setIconPath(String iconPath) {
        m_iconpath = iconPath;
    }
    
    /**
     * @return iconpath
     */
    public String getRefIconPath() {
        return m_refIconpath;
    }
    
    /**
     * @param reficonPath new Path to icon
     */
    public void setRefIconPath(String reficonPath) {
        m_refIconpath = reficonPath;
    }
    
    /**
     * @return start with string
     */
    public String getRegex() {
        return m_regex;
    }
    
    /**
     * @param regex new regex
     */
    public void setRegex(String regex) {
        m_regex = regex;
    }
    
}
