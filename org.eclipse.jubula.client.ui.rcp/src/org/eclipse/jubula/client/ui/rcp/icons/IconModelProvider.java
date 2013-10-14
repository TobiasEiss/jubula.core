/**
 * 
 */
package org.eclipse.jubula.client.ui.rcp.icons;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jubula.client.ui.constants.Constants;
import org.eclipse.jubula.client.ui.rcp.Plugin;
import org.eclipse.jubula.tools.constants.StringConstants;
import org.eclipse.jubula.tools.exception.JBException;

/**
 * @author keekx
 *
 */
public enum IconModelProvider {

    /**
     * INSTANCE
     */
    INSTANCE;
    
    /**
     * Icons
     */
    private List<Icon> m_icons;
    
    /**
     * private Constructor
     */
    private IconModelProvider() {
        m_icons = new ArrayList<Icon>();
        readFromPrefStore();
    }
    
    /**
     * store Icons
     */
    public void storeIcons() {
        StringBuilder storage = new StringBuilder();
        for (Icon icon : m_icons) {
            // startwith;iconpath;
            byte[] startWithArray = icon.getRegex().getBytes();
            String iconEncoded = new String(
                    Base64.encodeBase64(startWithArray));
            storage.append(iconEncoded).append(";"); //$NON-NLS-1$
            storage.append(new String(Base64.encodeBase64(
                    icon.getIconPath().toString().getBytes())));
            storage.append(";"); //$NON-NLS-1$
            storage.append(new String(Base64.encodeBase64(
                    icon.getRefIconPath().toString().getBytes())));
            storage.append(";"); //$NON-NLS-1$
        }
        IPreferenceStore preferenceStore = 
                Plugin.getDefault().getPreferenceStore();
        preferenceStore.setValue(
                Constants.ICONS_SETTINGS_KEY, storage.toString());
    }
    
    /**
     * 
     * Reads the pref storage to build the IconList.
     */
    private void readFromPrefStore() {

        IPreferenceStore prefStore = Plugin.getDefault().getPreferenceStore();
        String iconValue = 
                prefStore.getString(Constants.ICONS_SETTINGS_KEY);
        
        try {
            decodeIconPrefs(iconValue);
        } catch (JBException jbe) {
            prefStore.setToDefault(
                    Constants.ICONS_SETTINGS_KEY);
            try {
                decodeIconPrefs(iconValue);
            } catch (JBException e) {
                e.printStackTrace();
            }
        }
    
    }
    
    /**
     * load the icon list from the preference store into m_icons
     * @param store string read from preference store
     * @throws JBException in case of problem with preference store
     */
    private void decodeIconPrefs(String store) throws JBException {
        m_icons.clear();
        String[] iconStrings = StringUtils.split(store, ';');
        if (iconStrings.length % 3 == 0) {
            for (int i = 0; i < iconStrings.length; i += 3) {
                String startWithStr = decodeString(iconStrings[i]);

                String[] encodedPaths = 
                        StringUtils.split(iconStrings[i + 1], ',');
                String iconPath = ""; //$NON-NLS-1$
                for (String encodedPath : encodedPaths) {
                    iconPath = decodeString(encodedPath);
                }
                String[] encodedRefPaths = 
                        StringUtils.split(iconStrings[i + 2], ',');
                String iconRefPath = ""; //$NON-NLS-1$
                for (String encodedRefPath : encodedRefPaths) {
                    iconRefPath = decodeString(encodedRefPath);
                }
                m_icons.add(
                        new Icon(iconPath, startWithStr, iconRefPath));
            }
            
        } else {
            throw new JBException("Number of entries in server list must be even.", Integer.valueOf(0)); //$NON-NLS-1$
        }

    }
    
    /**
     * @param encodedString A base64 encoded string.
     * @return the decoded string.
     * @throws JBException in case of not base64 encoded string
     */
    String decodeString(String encodedString) throws JBException {
        if (!Base64.isArrayByteBase64(encodedString.getBytes())) {
            throw new JBException(StringConstants.EMPTY, new Integer(0));
        }
        return new String(Base64.decodeBase64(encodedString.getBytes()));
    }
    
    /**
     * @return icons
     */
    public List<Icon> getIcons() {
        return m_icons;
    }
    
}
