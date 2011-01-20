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
package org.eclipse.jubula.autagent.commands;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jubula.autagent.AutStarter;
import org.eclipse.jubula.tools.constants.AutConfigConstants;
import org.eclipse.jubula.tools.constants.CommandConstants;
import org.eclipse.jubula.tools.constants.StringConstants;


/**
 * @author BREDEX GmbH
 * @created Jul 6, 2007
 * 
 */
public class StartSwingAutServerCommand extends AbstractStartJavaAut {

    /** the logger */
    private static final  Log LOG = 
        LogFactory.getLog(StartSwingAutServerCommand.class);
    
    /** separates the environment variables */
    private static final String ENV_SEPARATOR = "\n"; //$NON-NLS-1$
    
    /** the classpath of the Aut Server */
    private String m_autServerClasspath = "AutServerClasspath"; //$NON-NLS-1$
    
    /**
     * {@inheritDoc}
     */
    protected String[] createEnvArray(Map parameters, boolean isAgentSet) {
        
        if (isRunningFromExecutable(parameters) 
                || isRunnigWithMonitoring(parameters)) {
            setEnv(parameters);
            boolean agentActive = true;
            return super.createEnvArray(parameters, agentActive);
        }       
          
        return super.createEnvArray(parameters, isAgentSet);
        
    }
    
    /**
     * Sets -javaagent, JRE arguments and the arguments for 
     * the AutServer as environment variables.
     * @param parameters The parameters for starting the AUT
     */
    private void setEnv(Map parameters) {
        String env = (String)parameters.get(AutConfigConstants.ENVIRONMENT);
        if (env == null) {
            env = StringConstants.EMPTY;
        } else {
            env += ENV_SEPARATOR;
        }
        env += setJavaOptions(parameters);
        if (isRunningFromExecutable(parameters)) {
        // agent arguments
            String serverPort = "null"; //$NON-NLS-1$
            if (AutStarter.getInstance().getAutCommunicator() != null) {
                serverPort = String.valueOf(AutStarter.getInstance()
                    .getAutCommunicator().getLocalPort());
            }
        
            env += ENV_SEPARATOR + "AUT_SERVER_PORT=" + serverPort; ////$NON-NLS-1$
            env += ENV_SEPARATOR + "AUT_SERVER_CLASSPATH=" + m_autServerClasspath; //$NON-NLS-1$
            env += ENV_SEPARATOR + "AUT_SERVER_NAME=" + getServerClassName(); //$NON-NLS-1$
        
            // Aut Agent variables
            env += ENV_SEPARATOR + AutConfigConstants.AUT_AGENT_HOST + "=" + parameters.get(AutConfigConstants.AUT_AGENT_HOST); //$NON-NLS-1$
            env += ENV_SEPARATOR + AutConfigConstants.AUT_AGENT_PORT + "=" + parameters.get(AutConfigConstants.AUT_AGENT_PORT); //$NON-NLS-1$
            env += ENV_SEPARATOR + AutConfigConstants.AUT_NAME + "=" + parameters.get(AutConfigConstants.AUT_NAME); //$NON-NLS-1$
        }
        // create environment
        parameters.put(AutConfigConstants.ENVIRONMENT, env);
    }
    
    /**
     * Sets -javaagent and JRE arguments as SUN environment variable.
     * @param parameters The parameters for starting the AUT
     * @return the _JAVA_OPTIONS environment variable including -javaagent
     * and jre arguments
     */
    protected String setJavaOptions(Map parameters) {
        StringBuffer sb = new StringBuffer();
        if (isRunningFromExecutable(parameters)) {
            Locale locale = (Locale)parameters.get(IStartAut.LOCALE);
            // set agent and locals
            
            sb.append("_JAVA_OPTIONS=\"-javaagent:"); //$NON-NLS-1$
            sb.append(getAbsoluteAgentJarPath()).append(StringConstants.QUOTE);
            if (isRunnigWithMonitoring(parameters)) {
                sb.append(" "); //$NON-NLS-1$ 
                sb.append(this.getMonitoringAgent(parameters));
            }         
            if (locale != null) {
                sb.append(" -Duser.country=").append(locale.getCountry()); //$NON-NLS-1$
                sb.append(" -Duser.language=").append(locale.getLanguage()); //$NON-NLS-1$
            }
        }
       
        if (isRunnigWithMonitoring(parameters) 
                && !isRunningFromExecutable(parameters)) {            
            sb.append("_JAVA_OPTIONS="); //$NON-NLS-1$
            sb.append(this.getMonitoringAgent(parameters));
        }       
        
        return sb.toString();
    }
    
    /**
     * {@inheritDoc}
     */
    protected String[] createCmdArray(String baseCmd, Map parameters) {
        List cmds = new Vector();
        cmds.add(baseCmd);
        
        String classpath = System.getProperty("java.class.path"); //$NON-NLS-1$
        if (LOG.isDebugEnabled()) {
            LOG.debug("classPath:" + classpath); //$NON-NLS-1$
        }
        StringBuffer autServerClasspath = new StringBuffer();
        createServerClasspath(classpath, autServerClasspath);
        
        List autAgentArgs = new ArrayList();
        autAgentArgs.add(String.valueOf(
                parameters.get(AutConfigConstants.AUT_AGENT_HOST)));
        autAgentArgs.add(String.valueOf(
                parameters.get(AutConfigConstants.AUT_AGENT_PORT)));
        autAgentArgs.add(String.valueOf(
                parameters.get(AutConfigConstants.AUT_NAME)));
        
        if (!isRunningFromExecutable(parameters)) {
            createAutServerLauncherClasspath(
                    cmds, autServerClasspath, parameters);
            createAutServerClasspath(autServerClasspath, cmds, parameters);
            cmds.addAll(autAgentArgs);
            // information for aut server that agent is not used
            cmds.add("false"); //$NON-NLS-1$
        } else { 
            String serverBasePath = createServerBasePath(parameters); 
            autServerClasspath.append(PATH_SEPARATOR).append(serverBasePath);
            createServerDirs(autServerClasspath);
            m_autServerClasspath = autServerClasspath.toString();
                       
        }
        cmds.addAll(createAutArguments(parameters));
        return (String[])cmds.toArray(new String[cmds.size()]);
    }
    
    /**     * Creates the AUT settings.
     * @param cmds the commands list
     * @param parameters The parameters for starting the AUT.
     */
    private void addBaseSettings(List cmds, Map parameters) {
        // add locale
        addLocale(cmds, (Locale)parameters.get(IStartAut.LOCALE)); 
                    
        // add jre params
        final String jreParams = (String)parameters.get(
                AutConfigConstants.JRE_PARAMETER);
        if (jreParams != null && jreParams.length() > 0) {
            StringTokenizer tok = new StringTokenizer(jreParams, 
                WHITESPACE_DELIMITER);
            while (tok.hasMoreTokens()) {
                cmds.add(tok.nextToken());
            }
        }
                
        // add debug options (if neccessary)
        addDebugParams(cmds);
        // add -Duser.dir and workingDir here
    }
    /**
     * Creates the Server classpath.
     * @param classpath the classpath
     * @param serverClasspath the server classpath
     */
    private void createServerClasspath(String classpath, 
        StringBuffer serverClasspath) {
        
        StringTokenizer classpathElems = new StringTokenizer(classpath,
                PATH_SEPARATOR);
        while (classpathElems.hasMoreTokens()) {
            String pathElem = classpathElems.nextToken();
            File filePathElem = new File(pathElem);
            serverClasspath.append(filePathElem.getAbsolutePath());
            if (classpathElems.hasMoreTokens()) {
                serverClasspath.append(PATH_SEPARATOR);                
            }
        }
        serverClasspath.append(PATH_SEPARATOR);
        serverClasspath.append(getAbsExtImplClassesPath());       
        if (LOG.isDebugEnabled()) {
            LOG.debug("classPath:" + classpath); //$NON-NLS-1$
            LOG.debug("serverClasspath" + serverClasspath); //$NON-NLS-1$
        }
    }
    
    /**
     * @param cmds the commands list
     * @param autServerClasspath the autServerClassPath to change
     * @param parameters The parameters for starting the AUT.
     */
    private void createAutServerLauncherClasspath(List cmds, 
            StringBuffer autServerClasspath, Map parameters) {
        
        addBaseSettings(cmds, parameters);
        cmds.add("-classpath"); //$NON-NLS-1$
        StringBuffer autClassPath = createAutClasspath(parameters);
        String serverBasePath = createServerBasePath(parameters); 
        cmds.add(autClassPath.append(serverBasePath).toString());
        // add classname of autLauncher
        cmds.add(CommandConstants.AUT_SERVER_LAUNCHER);
        // add autServerBase dirs to autServerClassPath
        autServerClasspath.append(PATH_SEPARATOR).append(serverBasePath);
    }
    
    /**
     * Creates the AUT classpath. 
     * @param parameters The parameters for starting the AUT.
     * @return The classpath of the AUT.
     */
    private StringBuffer createAutClasspath(Map parameters) {
        // Add AUT classpath
        String autClassPathStr = (String)parameters.get(
                AutConfigConstants.CLASSPATH);
        if (autClassPathStr == null) {
            autClassPathStr = StringConstants.EMPTY;
        }
        StringBuffer autClassPath = new StringBuffer(
                convertClientSeparator(autClassPathStr)); 
        if (autClassPath.length() > 0) {
            autClassPath.append(PATH_SEPARATOR);
        }
        String jarFile = (String)parameters.get(AutConfigConstants.JAR_FILE);
        if (jarFile == null) {
            jarFile = StringConstants.EMPTY;
        }
        String manifestClassPath = getClassPathFromManifest(parameters);
        if (manifestClassPath.length() > 0) {
            autClassPath.append(manifestClassPath).append(PATH_SEPARATOR);
        }
        autClassPath.append(jarFile);
        if (jarFile != null && jarFile.length() > 0) {
            autClassPath.append(PATH_SEPARATOR);
        }
        return autClassPath;
    }
    
    /**
     * @param parameters The parameters for starting the AUT.
     * @return the server base path including resources path
     */
    private String createServerBasePath(Map parameters) {
        // path for IDE version
        File filePathElem = new File(CommandConstants.AUT_SERVER_BASE_BIN);
        // path for the BuildVersion
        String serverBasePath = filePathElem.getAbsolutePath();
        if (!isRunningFromExecutable(parameters)) {
            filePathElem = new File(CommandConstants.AUT_LAUNCHER_JAR);
            final String autLauncherJarDir = filePathElem.getAbsolutePath();
            serverBasePath += PATH_SEPARATOR + autLauncherJarDir; 
        }
        return serverBasePath;
    }
    
    /**
     * Adds elements to the given cmds List.
     * @param autServerClasspath the server classpath
     * @param cmds the 1st part of the cmd array
     * @param parameters The parameters for starting the AUT.
     */
    private void createAutServerClasspath(StringBuffer autServerClasspath, 
        List cmds, Map parameters) {
        
        if (AutStarter.getInstance().getAutCommunicator() != null) {
            cmds.add(String.valueOf(
                    AutStarter.getInstance().getAutCommunicator()
                        .getLocalPort()));
        } else {
            cmds.add("null"); //$NON-NLS-1$
        }
        
        String autMain = getAUTMainClass(parameters);
        if (autMain == null) {
            return;
        }
        cmds.add(autMain);
        createServerDirs(autServerClasspath);
        cmds.add(autServerClasspath.toString());
        cmds.add(getServerClassName());
    }
    
    /**
     * Adds AUT server bin dir and jar dir to autServerClasspath.
     * @param autServerClasspath the server classpath
     */ 
    private void createServerDirs(StringBuffer autServerClasspath) {
        String autServerClasses = getServerClasses();
        String autServerJar = getServerJar();

        // path to /bin
        File filePathElem = new File(autServerClasses);
        String autServerBinDir = filePathElem.getAbsolutePath();
        // for the BuildVersion
        filePathElem = new File(autServerJar); 
        String autServerJarDir = filePathElem.getAbsolutePath();
        
        autServerClasspath.append(PATH_SEPARATOR).append(autServerBinDir)
            .append(PATH_SEPARATOR).append(autServerJarDir);
    }
    
    /**
     * @param parameters The parameters for starting the AUT.
     * @return The arguments for the AUT that were found in the 
     *         given parameters.
     */
    private List createAutArguments(Map parameters) {
        List argsList = new Vector();
        if (parameters.get(AutConfigConstants.GD_RUN_AUT_ARGUMENTS)
                instanceof String[]) {
            String[] autArgs = (String[])parameters
                    .get(AutConfigConstants.GD_RUN_AUT_ARGUMENTS);
            return Arrays.asList(autArgs);
        }
        String autArguments = 
            (String)parameters.get(AutConfigConstants.AUT_ARGUMENTS);
        
        if (autArguments == null) {
            autArguments = StringConstants.EMPTY;
        }
       
        StringTokenizer args = new StringTokenizer(autArguments, 
            WHITESPACE_DELIMITER);
        while (args.hasMoreTokens()) {
            String arg = args.nextToken();
            argsList.add(arg);
        }
        
        return argsList;
    }
    
    /**
     * Adds the parameters for remote debugging to the given command List.
     * @param cmds the command List
     */
    private void addDebugParams(List cmds) {
        if (BXDEBUG != null) {
            cmds.add("-Xdebug"); //$NON-NLS-1$
            cmds.add("-Xnoagent"); //$NON-NLS-1$
            cmds.add("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=" + BXDEBUG); //$NON-NLS-1$
            cmds.add("-Djava.compiler=NONE"); //$NON-NLS-1$
        }
    }
    
    /**
     * Gets the absolute path of the location of the external ImplClasses.
     * @return the absolute path
     */
    private String getAbsExtImplClassesPath() {
        
        final File implDir = new File(CommandConstants.EXT_IMPLCLASSES_PATH);
        final StringBuffer paths = new StringBuffer(implDir.getAbsolutePath());
        final File[] jars = implDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar"); //$NON-NLS-1$
            }
        });
        
        if (jars != null) {
            final int maxIdx = jars.length;
            for (int i = 0; i < maxIdx; i++) {
                File f = jars[i];
                paths.append(PATH_SEPARATOR);
                paths.append(f.getAbsolutePath());
            }
        }
        return paths.toString();
    }
    /**
     * Gets/loads external jars from the ext directory
     * @return the absolute path  
     */
    private String getExtJarPath() {
        
        final File extDir = new File(CommandConstants.EXT_JARS_PATH);        
        final StringBuffer paths = new StringBuffer(extDir.getAbsolutePath());
        final File[] extJars = extDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar"); //$NON-NLS-1$
            }
        });
        
        if (extJars != null) {           
            for (int i = 0; i < extJars.length; i++) {
                File f = extJars[i];
                paths.append(PATH_SEPARATOR);
                paths.append(f.getAbsolutePath());
            }
        }
        return paths.toString();
        
    }
    
    
    
    /**
     * Gets the absolute path of the GDAgent.jar file.
     * @return the absolute path
     */
    protected String getAbsoluteAgentJarPath() {
        final File agentJarDir = new File(CommandConstants.GDAGENT_JAR);
        final StringBuffer paths = 
            new StringBuffer(agentJarDir.getAbsolutePath());
        String absPath = paths.toString();
        return absPath.replace('\\', '/');
    }
    
    /**
     * {@inheritDoc}
     */
    protected String getServerClassName() {
        return CommandConstants.AUT_SWING_SERVER;
    }
    
    /**
     * {@inheritDoc}
     */
    protected String getServerClasses() {
        return CommandConstants.AUT_SERVER_BIN;
    }

    /**
     * {@inheritDoc}
     */
    protected String getServerJar() {
        return CommandConstants.AUT_SERVER_JAR;
    }
    
}
