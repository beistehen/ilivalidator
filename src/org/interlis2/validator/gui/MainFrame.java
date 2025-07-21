package org.interlis2.validator.gui;

import java.awt.Desktop;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.view.*;
import ch.ehi.basics.settings.Settings;
import ch.ehi.basics.swing.SwingWorker;
import ch.ehi.basics.tools.StringUtility;

import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.interlis2.validator.Main;
import org.interlis2.validator.Validator;

/** GUI of ilivalidator.
 */
public class MainFrame extends JFrame {
	private static final String WINDOW_HEIGHT = "org.interlis2.validator.gui.MainFrame.windowHeight";
	private static final String WINDOW_WIDTH = "org.interlis2.validator.gui.MainFrame.windowWidth";
	private static final String WINDOW_X = "org.interlis2.validator.gui.MainFrame.windowX";
	private static final String WINDOW_Y = "org.interlis2.validator.gui.MainFrame.windowY";

	private java.util.ResourceBundle rsrc=java.util.ResourceBundle.getBundle("org.interlis2.validator.gui.IliValidatorTexts");
	private Settings settings=null;
	private javax.swing.JPanel jContentPane = null;

	private javax.swing.JLabel xtfFileLabel = null;
    private javax.swing.JLabel xtfRefFileLabel = null;
	// area to display one file per line in multiple input file validation
	private javax.swing.JTextArea xtfFileUi = null;
    private javax.swing.JTextArea xtfRefFileUi = null;
	private javax.swing.JCheckBox allObjectsAccessibleUi = null;
	private javax.swing.JButton doXtfFileSelBtn = null;
    private javax.swing.JButton doXtfRefFileSelBtn = null;
	
	private javax.swing.JLabel modelNamesLabel = null;
	private javax.swing.JTextField modelNamesUi = null;
	
	private javax.swing.JLabel configFileLabel = null;
	private javax.swing.JTextField configFileUi = null;
	private javax.swing.JButton doConfigFileSelBtn = null;
	
    private javax.swing.JLabel metaConfigFileLabel = null;
    private javax.swing.JTextField metaConfigFileUi = null;
    private javax.swing.JButton doMetaConfigFileSelBtn = null;
    
	private javax.swing.JLabel logFileLabel = null;
	private javax.swing.JTextField logFileUi = null;
	private javax.swing.JButton doLogFileSelBtn = null;
	
	private javax.swing.JLabel xtfLogFileLabel = null;
	private javax.swing.JTextField xtfLogFileUi = null;
	private javax.swing.JButton doXtfLogFileSelBtn = null;
	private javax.swing.JButton doNewConfigFileBtn = null;
	
	private javax.swing.JTextArea logUi = null;
	private javax.swing.JButton clearlogBtn = null;
	
	private JCheckBoxMenuItem optionsSkipPolygonBuildingItem = null;
	private JCheckBoxMenuItem optionsMultiplicityOffItem = null;
	private JCheckBoxMenuItem optionsAllowItfAreaHolesItem = null;
    private JCheckBoxMenuItem optionsSimpleBoundaryItem = null;
    private JCheckBoxMenuItem optionsLogTimeItem = null;
	private JCheckBoxMenuItem optionsTraceItem = null;
	private JCheckBoxMenuItem optionsDisableConstraintValidationItem = null;
	private JCheckBoxMenuItem optionsDisableAreaValidationItem = null;
	
	
	public MainFrame() {
		super();
		initialize();
	}
	private void initialize() {
		this.setSize(500, 361);
		this.setLocationByPlatform(true);
		this.setContentPane(getJContentPane());
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		this.setName(Main.APP_NAME);
		this.setTitle(rsrc.getString("MainFrame.Title"));
		
		//creates a border, which looks like jtextfield border
		xtfFileUi.setBorder(new JTextField().getBorder());
        xtfRefFileUi.setBorder(new JTextField().getBorder());
		
	    //Create the menu bar.
		JMenuBar menuBar = new JMenuBar();
	    setJMenuBar(menuBar);

	    JMenu menu = new JMenu(rsrc.getString("MainFrame.ToolsMenu"));
	    menu.setMnemonic(KeyEvent.VK_T);
	    menuBar.add(menu);
	    JMenuItem menuItem = new JMenuItem(rsrc.getString("MainFrame.ModelRepositoriesMenuItem"));
		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent e){
				ch.interlis.ili2c.gui.RepositoriesDialog dlg=new ch.interlis.ili2c.gui.RepositoriesDialog(MainFrame.this);
				dlg.setIlidirs(settings.getValue(Validator.SETTING_ILIDIRS));
				dlg.setHttpProxyHost(settings.getValue(ch.interlis.ili2c.gui.UserSettings.HTTP_PROXY_HOST));
				dlg.setHttpProxyPort(settings.getValue(ch.interlis.ili2c.gui.UserSettings.HTTP_PROXY_PORT));
				dlg.setLocationRelativeTo(getJContentPane());
				if(dlg.showDialog()==ch.interlis.ili2c.gui.RepositoriesDialog.OK_OPTION){
					String ilidirs=dlg.getIlidirs();
					if(ilidirs==null){
						ilidirs=Validator.SETTING_DEFAULT_ILIDIRS;
					}
					settings.setValue(Validator.SETTING_ILIDIRS,ilidirs);
					settings.setValue(ch.interlis.ili2c.gui.UserSettings.HTTP_PROXY_HOST,dlg.getHttpProxyHost());
					settings.setValue(ch.interlis.ili2c.gui.UserSettings.HTTP_PROXY_PORT,dlg.getHttpProxyPort());
					saveSettings(settings);
				}
			}

		});
		menu.add(menuItem);
		
		// Add Options Menu in the Menu Bar
        JMenu optionsMenu = new JMenu(rsrc.getString("MainFrame.OptionsMenu"));
        optionsMenu.setMnemonic(KeyEvent.VK_T);
        menuBar.add(optionsMenu);
        
        // Add Checkboxes to Options Menu
        optionsMultiplicityOffItem = new JCheckBoxMenuItem(rsrc.getString("MainFrame.OptionsMultiplicityOffItem"));
        optionsMenu.add(optionsMultiplicityOffItem);
        
        optionsSkipPolygonBuildingItem = new JCheckBoxMenuItem(rsrc.getString("MainFrame.OptionsSkipPolygonBuildingItem"));
        optionsMenu.add(optionsSkipPolygonBuildingItem); 
        
        optionsAllowItfAreaHolesItem = new JCheckBoxMenuItem(rsrc.getString("MainFrame.OptionsAllowItfAreaHolesItem"));
        optionsMenu.add(optionsAllowItfAreaHolesItem);

        optionsSimpleBoundaryItem = new JCheckBoxMenuItem(rsrc.getString("MainFrame.OptionsSimpleBoundaryItem"));
        optionsMenu.add(optionsSimpleBoundaryItem);

        optionsDisableConstraintValidationItem = new JCheckBoxMenuItem(rsrc.getString("MainFrame.OptionsDisableConstraintValidationItem"));
        optionsMenu.add(optionsDisableConstraintValidationItem);
        
        optionsDisableAreaValidationItem = new JCheckBoxMenuItem(rsrc.getString("MainFrame.OptionsDisableAreaValidationItem"));
        optionsMenu.add(optionsDisableAreaValidationItem);
        
        optionsLogTimeItem = new JCheckBoxMenuItem(rsrc.getString("MainFrame.OptionsLogTimeItem"));
        optionsMenu.add(optionsLogTimeItem);
        
        optionsTraceItem = new JCheckBoxMenuItem(rsrc.getString("MainFrame.OptionsTraceItem"));
        optionsMenu.add(optionsTraceItem);

        // Add Help Menu in the Menu Bar
		JMenu helpMenu = new JMenu(rsrc.getString("MainFrame.HelpMenu"));
		menuBar.add(helpMenu);

		JMenuItem onlineDocumentation = new JMenuItem(rsrc.getString("MainFrame.OnlineHelpMenuItem"));
		helpMenu.add(onlineDocumentation);
		onlineDocumentation.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					Desktop currentDesktop = Desktop.getDesktop();
					if (Desktop.isDesktopSupported() && currentDesktop.isSupported(Desktop.Action.BROWSE)) {
						URI docUri = URI.create(rsrc.getString("MainFrame.DocURL"));
						currentDesktop.browse(docUri);
					}
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		});

		final JDialog aboutDialog = new AboutDialog(this);
		JMenuItem aboutMenuItem = new JMenuItem(rsrc.getString("MainFrame.AboutMenuItem"));
		helpMenu.add(aboutMenuItem);
		aboutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aboutDialog.setLocationRelativeTo(getJContentPane());
				aboutDialog.setVisible(true);
			}
		});

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
              saveSettings(getSettings());
  	    	System.exit(0);
            }
          });
	    
	}
	private void saveSettings(Settings settings) {
        // save not all, but only some values 
		Settings toSave=new Settings();
		toSave.setValue(ch.interlis.ili2c.gui.UserSettings.WORKING_DIRECTORY,settings.getValue(ch.interlis.ili2c.gui.UserSettings.WORKING_DIRECTORY));
		toSave.setValue(Validator.SETTING_ILIDIRS,settings.getValue(Validator.SETTING_ILIDIRS));
		toSave.setValue(Validator.SETTING_MODELNAMES,settings.getValue(Validator.SETTING_MODELNAMES));
		toSave.setValue(Validator.SETTING_ALL_OBJECTS_ACCESSIBLE,settings.getValue(Validator.SETTING_ALL_OBJECTS_ACCESSIBLE));
        toSave.setValue(Validator.SETTING_LOGFILE_TIMESTAMP,settings.getValue(Validator.SETTING_LOGFILE_TIMESTAMP));
		toSave.setValue(ch.interlis.ili2c.gui.UserSettings.HTTP_PROXY_HOST,settings.getValue(ch.interlis.ili2c.gui.UserSettings.HTTP_PROXY_HOST));
		toSave.setValue(ch.interlis.ili2c.gui.UserSettings.HTTP_PROXY_PORT,settings.getValue(ch.interlis.ili2c.gui.UserSettings.HTTP_PROXY_PORT));
		toSave.setValue(WINDOW_WIDTH, settings.getValue(WINDOW_WIDTH));
		toSave.setValue(WINDOW_HEIGHT, settings.getValue(WINDOW_HEIGHT));
		toSave.setValue(WINDOW_X, settings.getValue(WINDOW_X));
		toSave.setValue(WINDOW_Y, settings.getValue(WINDOW_Y));

		Main.writeSettings(toSave);
	}
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			java.awt.GridBagConstraints xtfFileLabelConstraints = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints xtfFileUiConstraints = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints doXtfFileSelBtnConstraints = new java.awt.GridBagConstraints();

            java.awt.GridBagConstraints xtfRefFileLabelConstraints = new java.awt.GridBagConstraints();
            java.awt.GridBagConstraints xtfRefFileUiConstraints = new java.awt.GridBagConstraints();
            java.awt.GridBagConstraints doXtfRefFileSelBtnConstraints = new java.awt.GridBagConstraints();
			
			java.awt.GridBagConstraints allObjectsAccessibleConstraints = new java.awt.GridBagConstraints();

			java.awt.GridBagConstraints modelNamesLabelConstraints = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints modelNamesUiConstraints = new java.awt.GridBagConstraints();

			java.awt.GridBagConstraints logFileLabelConstraints = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints logFileUiConstraints = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints doLogFileSelBtnConstraints = new java.awt.GridBagConstraints();
			
			java.awt.GridBagConstraints xtfLogFileLabelConstraints = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints xtfLogFileUiConstraints = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints doXtfLogFileSelBtnConstraints = new java.awt.GridBagConstraints();
			
			java.awt.GridBagConstraints configFileLabelConstraints = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints configFileUiConstraints = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints doConfigFileSelBtnConstraints = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints doNewConfigFileBtnConstraints = new java.awt.GridBagConstraints();

            java.awt.GridBagConstraints metaConfigFileLabelConstraints = new java.awt.GridBagConstraints();
            java.awt.GridBagConstraints metaConfigFileUiConstraints = new java.awt.GridBagConstraints();
            java.awt.GridBagConstraints doMetaConfigFileSelBtnConstraints = new java.awt.GridBagConstraints();
			
			java.awt.GridBagConstraints clearlogBtnConstraints = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints logPaneConstraints = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints doValidateConstraints = new java.awt.GridBagConstraints();
			
			// row 0
			int row=0;
			xtfFileLabelConstraints.gridx = 0;
			xtfFileLabelConstraints.gridy = row;
			xtfFileLabelConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
			xtfFileUiConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			xtfFileUiConstraints.weightx = 1.0;
			xtfFileUiConstraints.gridx = 1;
			xtfFileUiConstraints.gridy = row;
			xtfFileUiConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
			doXtfFileSelBtnConstraints.gridx = 2;
			doXtfFileSelBtnConstraints.gridy = row;
			doXtfFileSelBtnConstraints.anchor = java.awt.GridBagConstraints.WEST;
			
            // row 1
            row++;
            xtfRefFileLabelConstraints.gridx = 0;
            xtfRefFileLabelConstraints.gridy = row;
            xtfRefFileLabelConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            xtfRefFileUiConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            xtfRefFileUiConstraints.weightx = 1.0;
            xtfRefFileUiConstraints.gridx = 1;
            xtfRefFileUiConstraints.gridy = row;
            xtfRefFileUiConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            doXtfRefFileSelBtnConstraints.gridx = 2;
            doXtfRefFileSelBtnConstraints.gridy = row;
            doXtfRefFileSelBtnConstraints.anchor = java.awt.GridBagConstraints.WEST;
            
			// row 1
			row++;
			allObjectsAccessibleConstraints.gridx = 1;
			allObjectsAccessibleConstraints.gridy = row;
			allObjectsAccessibleConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
			
			// row 2
			row++;
			modelNamesLabelConstraints.gridx = 0;
			modelNamesLabelConstraints.gridy = row;
			modelNamesLabelConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
			modelNamesUiConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			modelNamesUiConstraints.weightx = 1.0;
			modelNamesUiConstraints.gridx = 1;
			modelNamesUiConstraints.gridy = row;
			modelNamesUiConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
			
			// row 3
			row++;
			logFileLabelConstraints.gridx = 0;
			logFileLabelConstraints.gridy = row;
			logFileLabelConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
			logFileUiConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			logFileUiConstraints.weightx = 1.0;
			logFileUiConstraints.gridx = 1;
			logFileUiConstraints.gridy = row;
			logFileUiConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
			doLogFileSelBtnConstraints.gridx = 2;
			doLogFileSelBtnConstraints.gridy = row;
			doLogFileSelBtnConstraints.anchor = java.awt.GridBagConstraints.WEST;
			
			// row 4
			row++;
			xtfLogFileLabelConstraints.gridx = 0;
			xtfLogFileLabelConstraints.gridy = row;
			xtfLogFileLabelConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
			xtfLogFileUiConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			xtfLogFileUiConstraints.weightx = 1.0;
			xtfLogFileUiConstraints.gridx = 1;
			xtfLogFileUiConstraints.gridy = row;
			xtfLogFileUiConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
			doXtfLogFileSelBtnConstraints.gridx = 2;
			doXtfLogFileSelBtnConstraints.gridy = row;
			doXtfLogFileSelBtnConstraints.anchor = java.awt.GridBagConstraints.WEST;
			
			// row 5
			row++;
			configFileLabelConstraints.gridx = 0;
			configFileLabelConstraints.gridy = row;
			configFileLabelConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
			configFileUiConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			configFileUiConstraints.weightx = 1.0;
			configFileUiConstraints.gridx = 1;
			configFileUiConstraints.gridy = row;
			configFileUiConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
			doConfigFileSelBtnConstraints.gridx = 2;
			doConfigFileSelBtnConstraints.gridy = row;
			doConfigFileSelBtnConstraints.anchor = java.awt.GridBagConstraints.WEST;
            doNewConfigFileBtnConstraints.gridx = 3;
            doNewConfigFileBtnConstraints.gridy = row;

            // row 6
            row++;
            metaConfigFileLabelConstraints.gridx = 0;
            metaConfigFileLabelConstraints.gridy = row;
            metaConfigFileLabelConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            metaConfigFileUiConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            metaConfigFileUiConstraints.weightx = 1.0;
            metaConfigFileUiConstraints.gridx = 1;
            metaConfigFileUiConstraints.gridy = row;
            metaConfigFileUiConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            doMetaConfigFileSelBtnConstraints.gridx = 2;
            doMetaConfigFileSelBtnConstraints.gridy = row;
            doMetaConfigFileSelBtnConstraints.anchor = java.awt.GridBagConstraints.WEST;
            
			// row 7
            row++;
			logPaneConstraints.fill = java.awt.GridBagConstraints.BOTH;
			logPaneConstraints.weightx = 1.0;
			logPaneConstraints.weighty = 1.0;
			logPaneConstraints.gridx = 0;
			logPaneConstraints.gridy = row;
			logPaneConstraints.gridheight = 2;
			logPaneConstraints.gridwidth = 2;
			doValidateConstraints.gridy = row;
			doValidateConstraints.gridx = 2;
			doValidateConstraints.gridwidth = 2;
			doValidateConstraints.anchor = java.awt.GridBagConstraints.WEST;
			
			// row 8
			row++;
			clearlogBtnConstraints.gridx = 2;//2
			clearlogBtnConstraints.gridy = row;
			clearlogBtnConstraints.gridwidth = 2;
			clearlogBtnConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
			
			jContentPane.setLayout(new java.awt.GridBagLayout());
			
			jContentPane.add(getXtfFileLabel(), xtfFileLabelConstraints);
			jContentPane.add(getXtfFileUi(), xtfFileUiConstraints);
			jContentPane.add(getDoXtfFileSelBtn(), doXtfFileSelBtnConstraints);

            jContentPane.add(getXtfRefFileLabel(), xtfRefFileLabelConstraints);
            jContentPane.add(getXtfRefFileUi(), xtfRefFileUiConstraints);
            jContentPane.add(getDoXtfRefFileSelBtn(), doXtfRefFileSelBtnConstraints);
			
			jContentPane.add(getModelNamesLabel(), modelNamesLabelConstraints);
			jContentPane.add(getModelNamesUi(), modelNamesUiConstraints);
			
			jContentPane.add(getLogFileLabel(), logFileLabelConstraints);
			jContentPane.add(getLogFileUi(), logFileUiConstraints);
			jContentPane.add(getDoLogFileSelBtn(), doLogFileSelBtnConstraints);

			jContentPane.add(getXtfLogFileLabel(), xtfLogFileLabelConstraints);
			jContentPane.add(getXtfLogFileUi(), xtfLogFileUiConstraints);
			jContentPane.add(getDoXtfLogFileSelBtn(), doXtfLogFileSelBtnConstraints);
			
			jContentPane.add(getConfigFileLabel(), configFileLabelConstraints);
			jContentPane.add(getConfigFileUi(), configFileUiConstraints);
			jContentPane.add(getDoConfigFileSelBtn(), doConfigFileSelBtnConstraints);
			jContentPane.add(getNewConfigFileBtn(), doNewConfigFileBtnConstraints);

            jContentPane.add(getMetaConfigFileLabel(), metaConfigFileLabelConstraints);
            jContentPane.add(getMetaConfigFileUi(), metaConfigFileUiConstraints);
            jContentPane.add(getDoMetaConfigFileSelBtn(), doMetaConfigFileSelBtnConstraints);
			
			jContentPane.add(getJScrollPane(), logPaneConstraints);
			jContentPane.add(getClearlogBtn(), clearlogBtnConstraints);
			jContentPane.add(getAllObjectsAccessibleUi(), allObjectsAccessibleConstraints);
			jContentPane.add(getDoValidateBtn(), doValidateConstraints);
		}
		return jContentPane;
	}
	private javax.swing.JLabel getXtfFileLabel() {
		if(xtfFileLabel == null) {
			xtfFileLabel = new javax.swing.JLabel();
			xtfFileLabel.setText(rsrc.getString("MainFrame.xtfFileLabel"));
		}
		return xtfFileLabel;
	}
    private javax.swing.JLabel getXtfRefFileLabel() {
        if(xtfRefFileLabel == null) {
            xtfRefFileLabel = new javax.swing.JLabel();
            xtfRefFileLabel.setText(rsrc.getString("MainFrame.xtfRefFileLabel"));
        }
        return xtfRefFileLabel;
    }
	private javax.swing.JLabel getModelNamesLabel() {
		if(modelNamesLabel == null) {
			modelNamesLabel = new javax.swing.JLabel();
			modelNamesLabel.setText(rsrc.getString("MainFrame.modelNamesLabel"));
		}
		return modelNamesLabel;
	}
	private javax.swing.JLabel getConfigFileLabel() {
		if(configFileLabel == null) {
			configFileLabel = new javax.swing.JLabel();
			configFileLabel.setText(rsrc.getString("MainFrame.configFileLabel"));
		}
		return configFileLabel;
	}
    private javax.swing.JLabel getMetaConfigFileLabel() {
        if(metaConfigFileLabel == null) {
            metaConfigFileLabel = new javax.swing.JLabel();
            metaConfigFileLabel.setText(rsrc.getString("MainFrame.metaConfigFileLabel"));
        }
        return metaConfigFileLabel;
    }
	private javax.swing.JLabel getLogFileLabel() {
		if(logFileLabel == null) {
			logFileLabel = new javax.swing.JLabel();
			logFileLabel.setText(rsrc.getString("MainFrame.logFileLabel"));
		}
		return logFileLabel;
	}
	private javax.swing.JLabel getXtfLogFileLabel() {
		if(xtfLogFileLabel == null) {
			xtfLogFileLabel = new javax.swing.JLabel();
			xtfLogFileLabel.setText(rsrc.getString("MainFrame.xtfLogFileLabel"));
		}
		return xtfLogFileLabel;
	}
	private javax.swing.JTextArea getXtfFileUi() {
		if(xtfFileUi == null) {
			xtfFileUi = new javax.swing.JTextArea();
			new DropTarget(xtfFileUi, getXtfFileDragAndDropHandler());
		}
		return xtfFileUi;
	}
    private javax.swing.JTextArea getXtfRefFileUi() {
        if(xtfRefFileUi == null) {
            xtfRefFileUi = new javax.swing.JTextArea();
            new DropTarget(xtfRefFileUi, getXtfRefFileDragAndDropHandler());
        }
        return xtfRefFileUi;
    }
	private javax.swing.JTextField getModelNamesUi() {
		if(modelNamesUi == null) {
			modelNamesUi = new javax.swing.JTextField();
		}
		return modelNamesUi;
	}
	private javax.swing.JTextField getConfigFileUi() {
		if(configFileUi == null) {
			configFileUi = new javax.swing.JTextField();
		}
		return configFileUi;
	}
    private javax.swing.JTextField getMetaConfigFileUi() {
        if(metaConfigFileUi == null) {
            metaConfigFileUi = new javax.swing.JTextField();
        }
        return metaConfigFileUi;
    }
	private javax.swing.JTextField getLogFileUi() {
		if(logFileUi == null) {
			logFileUi = new javax.swing.JTextField();
		}
		return logFileUi;
	}
	private javax.swing.JTextField getXtfLogFileUi() {
		if(xtfLogFileUi == null) {
			xtfLogFileUi = new javax.swing.JTextField();
		}
		return xtfLogFileUi;
	}
	private javax.swing.JTextArea getLogUi() {
		if(logUi == null) {
			logUi = new javax.swing.JTextArea();
			logUi.setEditable(false);
		}
		return logUi;
	}
	private javax.swing.JButton getClearlogBtn() {
		if(clearlogBtn == null) {
			clearlogBtn = new javax.swing.JButton();
			clearlogBtn.setText(rsrc.getString("MainFrame.clearLogButton"));
			clearlogBtn.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					logClear();
				}
			});
		}
		return clearlogBtn;
	}
	private javax.swing.JCheckBox getAllObjectsAccessibleUi() {
		if(allObjectsAccessibleUi == null) {
			allObjectsAccessibleUi = new javax.swing.JCheckBox();
			allObjectsAccessibleUi.setText(rsrc.getString("MainFrame.allObjectsAccessible"));
		}
		return allObjectsAccessibleUi;
	}
	private DropTargetListener getXtfFileDragAndDropHandler() {
		return new DropTargetListener() {
			public void dragEnter(DropTargetDragEvent evt) {}
			public void dragOver(DropTargetDragEvent evt) {}
			public void dropActionChanged(DropTargetDragEvent evt) {}
			public void dragExit(DropTargetEvent dte) {}
			public void drop(DropTargetDropEvent evt) {
				try {
					Transferable tr = evt.getTransferable();
					for (DataFlavor flavor : tr.getTransferDataFlavors()) {
						if (flavor.isFlavorJavaFileListType()){
							evt.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
							List<File> files = (List<File>)tr.getTransferData(flavor);
							String[] absolutePaths = new String[files.size()];
							for(int i = 0; i < files.size(); i++){
								absolutePaths[i] = files.get(i).getAbsolutePath();
							}
							setXtfFile(absolutePaths);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (UnsupportedFlavorException e) {
					e.printStackTrace();
				}
			}
		};
	}
    private DropTargetListener getXtfRefFileDragAndDropHandler() {
        return new DropTargetListener() {
            public void dragEnter(DropTargetDragEvent evt) {}
            public void dragOver(DropTargetDragEvent evt) {}
            public void dropActionChanged(DropTargetDragEvent evt) {}
            public void dragExit(DropTargetEvent dte) {}
            public void drop(DropTargetDropEvent evt) {
                try {
                    Transferable tr = evt.getTransferable();
                    for (DataFlavor flavor : tr.getTransferDataFlavors()) {
                        if (flavor.isFlavorJavaFileListType()){
                            evt.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                            List<File> files = (List<File>)tr.getTransferData(flavor);
                            String[] absolutePaths = new String[files.size()];
                            for(int i = 0; i < files.size(); i++){
                                absolutePaths[i] = files.get(i).getAbsolutePath();
                            }
                            setXtfRefFile(absolutePaths);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                }
            }
        };
    }
	// selected files
	public String[] getXtfFile(){
		String fileTextInUi=getXtfFileUi().getText();
		String[] files=splitFilenames(fileTextInUi);
		return files;
	}
    public String[] getXtfRefFile(){
        String fileTextInUi=getXtfRefFileUi().getText();
        String[] files=splitFilenames(fileTextInUi);
        return files;
    }
	/** unterteilt den mehrzeiligen String in einzelne Zeilen. 
	 * Leerzeilen werden nicht ins Resultat uebernommen.
	 * Leerzeichen am Anfang und Ende der einzelnen Zeilen werden entfernt.
	 * Leerzeichen innerhlab der Zeile werden nicht veraendert.
	 * @param lines der mehrzeilige String
	 * @return das array der einzelnen Zeilen
	 */
	public static String[] splitFilenames(String lines) {
		String[] dataFileParts=lines.split("\n");
		List<String> trimmedFileParts=new ArrayList<String>();
		for(String fileText:dataFileParts) {
			String trimmedText=fileText.trim();
			String retText=trimmedText.replace("\n", "");
			if(retText!=null && !retText.isEmpty() && retText.toString().length()>0) {
				trimmedFileParts.add(retText);
			}
		}
		String[] partArr = trimmedFileParts.toArray(new String[0]);
		return partArr;
	}
	public void setXtfFile(String[] xtfFileList){
        StringBuilder stringBuilder = new StringBuilder();
	    if(xtfFileList!=null) {
	        String newLine="";
	        for(int i=0;i<xtfFileList.length;i++){
	            if(xtfFileList[i]!=null) {
	                stringBuilder.append(newLine);
	                stringBuilder.append(xtfFileList[i]);
	                newLine = "\n";
	            }
	        }
	    }
		getXtfFileUi().setText(stringBuilder.toString());
	}
    public void setXtfRefFile(String[] xtfFileList){
        StringBuilder stringBuilder = new StringBuilder();
        if(xtfFileList!=null) {
            String newLine="";
            for(int i=0;i<xtfFileList.length;i++){
                if(xtfFileList[i]!=null) {
                    stringBuilder.append(newLine);
                    stringBuilder.append(xtfFileList[i]);
                    newLine = "\n";
                }
            }
        }
        getXtfRefFileUi().setText(stringBuilder.toString());
    }
	// selected model names
	public String getModelNames() {
		return StringUtility.purge(getModelNamesUi().getText());
	}
	
	public void setModelNames(String modelNames){
		getModelNamesUi().setText(modelNames);
	}
	
	public String getConfigFile(){
		return StringUtility.purge(getConfigFileUi().getText());
	}
    public String getMetaConfigFile(){
        return StringUtility.purge(getMetaConfigFileUi().getText());
    }
	public boolean getObjectsAccessible(){
		boolean allObjectsAccessible=getAllObjectsAccessibleUi().isSelected();
		return allObjectsAccessible;
	}
	public void setObjectsAccessible(boolean allObjectsAccessible){
		getAllObjectsAccessibleUi().setSelected(allObjectsAccessible);
	}
	public void setConfigFile(String dbhost){
		getConfigFileUi().setText(dbhost);
	}
    public void setMetaConfigFile(String dbhost){
        getMetaConfigFileUi().setText(dbhost);
    }
	public String getLogFile(){
		return StringUtility.purge(getLogFileUi().getText());
	}
	public void setLogFile(String logfile){
		getLogFileUi().setText(logfile);
	}
	public String getXtfLogFile(){
		return StringUtility.purge(getXtfLogFileUi().getText());
	}
	public void setXtfLogFile(String xtflogfile){
		getXtfLogFileUi().setText(xtflogfile);
	}
	public Settings getSettings()
	{
		// get values from UI
        String xtfRefFiles=Main.makeFileList(getXtfRefFile());
        
		String logFile=getLogFile();
		String datalogFile=getXtfLogFile();
		String configFile=getConfigFile();
        String metaConfigFile=getMetaConfigFile();
		String modelNames=getModelNames();
		String objectsAccess=getObjectsAccessible()?Validator.TRUE:Validator.FALSE;
		
		// keep some values from current settings
		String workingDir=settings.getValue(ch.interlis.ili2c.gui.UserSettings.WORKING_DIRECTORY);
		String proxyHost=settings.getValue(ch.interlis.ili2c.gui.UserSettings.HTTP_PROXY_HOST);
		String proxyPort=settings.getValue(ch.interlis.ili2c.gui.UserSettings.HTTP_PROXY_PORT);
		String ilidirs=settings.getValue(Validator.SETTING_ILIDIRS);
        String appHome=settings.getValue(Validator.SETTING_APPHOME);

		// save window location and size
		Dimension dimension = getSize();
		String windowWidth = Integer.toString((int) dimension.getWidth());
		String windowHeight = Integer.toString((int) dimension.getHeight());
		Point origin = getLocation();
		String windowX = Integer.toString((int) origin.getX());
		String windowY = Integer.toString((int) origin.getY());

		
		Settings newSettings=new Settings();
		
        newSettings.setValue(Validator.SETTING_REF_DATA,xtfRefFiles);
		newSettings.setValue(ch.interlis.ili2c.gui.UserSettings.WORKING_DIRECTORY,workingDir);
		newSettings.setValue(Validator.SETTING_LOGFILE,logFile);
		if(datalogFile!=null && GenericFileFilter.createCsvFilter().getExtension().equals(GenericFileFilter.getFileExtension(datalogFile))){
	        newSettings.setValue(Validator.SETTING_CSVLOG,datalogFile);
		}else {
	        newSettings.setValue(Validator.SETTING_XTFLOG,datalogFile);
		}
		newSettings.setValue(Validator.SETTING_MODELNAMES,modelNames);
		newSettings.setValue(Validator.SETTING_CONFIGFILE,configFile);
        newSettings.setValue(Validator.SETTING_META_CONFIGFILE,metaConfigFile);
		newSettings.setValue(Validator.SETTING_ALL_OBJECTS_ACCESSIBLE,objectsAccess);
		newSettings.setValue(Validator.SETTING_ILIDIRS,ilidirs);
		newSettings.setValue(Validator.SETTING_APPHOME, appHome);
		newSettings.setValue(ch.interlis.ili2c.gui.UserSettings.HTTP_PROXY_HOST,proxyHost);
		newSettings.setValue(ch.interlis.ili2c.gui.UserSettings.HTTP_PROXY_PORT,proxyPort);
		newSettings.setValue(WINDOW_WIDTH, windowWidth);
		newSettings.setValue(WINDOW_HEIGHT, windowHeight);
		newSettings.setValue(WINDOW_X, windowX);
		newSettings.setValue(WINDOW_Y, windowY);
		
		if (optionsSkipPolygonBuildingItem.isSelected()) {
		    newSettings.setValue(ch.interlis.iox_j.validator.Validator.CONFIG_DO_ITF_LINETABLES, ch.interlis.iox_j.validator.Validator.CONFIG_DO_ITF_LINETABLES_DO);
		}
		if (optionsMultiplicityOffItem.isSelected()) {
		    newSettings.setValue(Validator.SETTING_MULTIPLICITY_VALIDATION,ch.interlis.iox_j.validator.ValidationConfig.OFF);
		}
		if (optionsAllowItfAreaHolesItem.isSelected()) {
		    newSettings.setValue(Validator.SETTING_ALLOW_ITF_AREA_HOLES,Validator.TRUE);
		}
        if (optionsSimpleBoundaryItem.isSelected()) {
            newSettings.setValue(Validator.SETTING_SIMPLE_BOUNDARY,Validator.TRUE);
        }
        if (optionsLogTimeItem.isSelected()) {
            newSettings.setValue(Validator.SETTING_LOGFILE_TIMESTAMP,Validator.TRUE);
        }
		if (optionsTraceItem.isSelected()) {
		    EhiLogger.getInstance().setTraceFilter(false);
		} else {
		    EhiLogger.getInstance().setTraceFilter(true);
		}
		if (optionsDisableConstraintValidationItem.isSelected()) {
		    newSettings.setValue(Validator.SETTING_DISABLE_CONSTRAINT_VALIDATION,Validator.TRUE);
		}
		if (optionsDisableAreaValidationItem.isSelected()) {
		    newSettings.setValue(Validator.SETTING_DISABLE_AREA_VALIDATION,Validator.TRUE);
		}
		
		return newSettings;
	}
	public void setSettings(Settings settings)
	{
		this.settings=settings;
	}
	private StringBuffer body=new StringBuffer();
	private javax.swing.JScrollPane jScrollPane = null;
	private javax.swing.JButton doValidateBtn = null;
	public void logAppend(String msg){
		body.append(msg);
		if(!msg.endsWith("\n")){
			body.append("\n");
		}
		getLogUi().setText(body.toString());
	}
	public void logClear(){
		body=new StringBuffer();
		getLogUi().setText(body.toString());
	}
	private javax.swing.JScrollPane getJScrollPane() {
		if(jScrollPane == null) {
			jScrollPane = new javax.swing.JScrollPane();
			jScrollPane.setViewportView(getLogUi());
		}
		return jScrollPane;
	}
	@Deprecated
    public static void main(String[] xtfFile,Settings settings) {
        main(xtfFile,null,settings);
    }
	public static void main(String[] xtfFile,String[] xtfRefFile,Settings settings) {
			MainFrame frame=new MainFrame();
			frame.setSettings(settings);
			String logFile=settings.getValue(Validator.SETTING_LOGFILE);
			frame.setLogFile(logFile);
			resetLogListener(frame,logFile);
			frame.setXtfFile(xtfFile);
			if(xtfRefFile!=null) {
	            frame.setXtfRefFile(xtfRefFile);
			}
			String modelList=settings.getValue(Validator.SETTING_MODELNAMES);
			frame.setModelNames(modelList);
			String xtflogFile=settings.getValue(Validator.SETTING_XTFLOG);
			frame.setXtfLogFile(xtflogFile);
			String configFile=settings.getValue(Validator.SETTING_CONFIGFILE);
			frame.setConfigFile(configFile);
            String metaConfigFile=settings.getValue(Validator.SETTING_META_CONFIGFILE);
            frame.setMetaConfigFile(metaConfigFile);
			frame.setObjectsAccessible(Validator.TRUE.equals(settings.getValue(Validator.SETTING_ALL_OBJECTS_ACCESSIBLE)));
			restoreWindowSizeAndLocation(frame, settings);
			frame.show();
	}
	private LogListener logListener=null;
	private static void resetLogListener(MainFrame frame,String logFile) {
	    if(frame.logListener!=null) {
	        EhiLogger.getInstance().removeListener(frame.logListener);
	    }
	    frame.logListener=new LogListener(frame,logFile);
        EhiLogger.getInstance().addListener(frame.logListener);
	}
	private static void restoreWindowSizeAndLocation(JFrame frame, Settings settings) {
		try {
			int width = Integer.parseInt(settings.getValue(WINDOW_WIDTH));
			int height = Integer.parseInt(settings.getValue(WINDOW_HEIGHT));
			int x = Integer.parseInt(settings.getValue(WINDOW_X));
			int y = Integer.parseInt(settings.getValue(WINDOW_Y));

			frame.setSize(width, height);
			if (isLocationOnScreen(x, y)) {
				frame.setLocation(x, y);
			}
		} catch (NumberFormatException ex) {
			// ignore settings, use the default size and location
		}
	}
	private static boolean isLocationOnScreen(int x, int y) {
		for (GraphicsDevice screen : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			Rectangle bounds = screen.getDefaultConfiguration().getBounds();
			if (bounds.contains(x, y)) {
				return true;
			}
		}
		return false;
	}
	private javax.swing.JButton getDoValidateBtn() {
		if(doValidateBtn == null) {
			doValidateBtn = new javax.swing.JButton();
			doValidateBtn.setText(rsrc.getString("MainFrame.doValidateButton"));
			doValidateBtn.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {
					SwingWorker worker = new SwingWorker() {
						public Object construct() {
							try {
								boolean ret=Validator.runValidation(getXtfFile(),getSettings());
								resetLogListener(MainFrame.this, getLogFile());
                                getLogUi().setCaretPosition(getLogUi().getDocument().getLength());
								Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(MainFrame.this, ret?Validator.MSG_VALIDATION_DONE:Validator.MSG_VALIDATION_FAILED);                                   
							} catch (Exception ex) {
								EhiLogger.logError(rsrc.getString("MainFrame.generalError"),ex);
							}
							return null;
						}
					};
					worker.start();
				}
			});
		}
		return doValidateBtn;
	}
    private javax.swing.JButton getDoXtfFileSelBtn() {
        if(doXtfFileSelBtn == null) {
            doXtfFileSelBtn = new javax.swing.JButton();
            doXtfFileSelBtn.setText("...");
            doXtfFileSelBtn.addActionListener(new java.awt.event.ActionListener() { 
                public void actionPerformed(java.awt.event.ActionEvent e) {    
                    FileChooser fileDialog =  new FileChooser();
                    fileDialog.setCurrentDirectory(new File(getWorkingDirectory()));
                    fileDialog.setDialogTitle(rsrc.getString("MainFrame.xtfFileChooserTitle"));
                    fileDialog.addChoosableFileFilter(new GenericFileFilter(rsrc.getString("MainFrame.itfFileFilter"),"itf"));
                    fileDialog.addChoosableFileFilter(GenericFileFilter.createXmlFilter());
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(rsrc.getString("MainFrame.xtfFileFilter"), "xtf", "xml");
                    fileDialog.setFileFilter(filter);
                    
                    fileDialog.setMultiSelectionEnabled(true);

                    if (fileDialog.showOpenDialog(MainFrame.this) == FileChooser.APPROVE_OPTION) {
                        setWorkingDirectory(fileDialog.getCurrentDirectory().getAbsolutePath());
                        File[] multipleFiles = fileDialog.getSelectedFiles();
                        String[] selectedFiles = new String[multipleFiles.length];
                        for(int i=0;i<multipleFiles.length;i++){
                            selectedFiles[i]=multipleFiles[i].getAbsolutePath();
                        }
                        setXtfFile(selectedFiles);
                    }               
                }
            });
        }
        return doXtfFileSelBtn;
    }
	private javax.swing.JButton getDoXtfRefFileSelBtn() {
		if(doXtfRefFileSelBtn == null) {
			doXtfRefFileSelBtn = new javax.swing.JButton();
			doXtfRefFileSelBtn.setText("...");
			doXtfRefFileSelBtn.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
                    FileChooser fileDialog =  new FileChooser();
                    fileDialog.setCurrentDirectory(new File(getWorkingDirectory()));
                    fileDialog.setDialogTitle(rsrc.getString("MainFrame.xtfFileChooserTitle"));
                    fileDialog.addChoosableFileFilter(new GenericFileFilter(rsrc.getString("MainFrame.itfFileFilter"),"itf"));
                    fileDialog.addChoosableFileFilter(GenericFileFilter.createXmlFilter());
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(rsrc.getString("MainFrame.xtfFileFilter"), "xtf", "xml");
                    fileDialog.setFileFilter(filter);
                    
                    fileDialog.setMultiSelectionEnabled(true);

					if (fileDialog.showOpenDialog(MainFrame.this) == FileChooser.APPROVE_OPTION) {
						setWorkingDirectory(fileDialog.getCurrentDirectory().getAbsolutePath());
						File[] multipleFiles = fileDialog.getSelectedFiles();
						String[] selectedFiles = new String[multipleFiles.length];
						for(int i=0;i<multipleFiles.length;i++){
							selectedFiles[i]=multipleFiles[i].getAbsolutePath();
						}
						setXtfRefFile(selectedFiles);
					}				
				}
			});
		}
		return doXtfRefFileSelBtn;
	}
	private javax.swing.JButton getDoLogFileSelBtn() {
		if(doLogFileSelBtn == null) {
			doLogFileSelBtn = new javax.swing.JButton();
			doLogFileSelBtn.setText("...");
			doLogFileSelBtn.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					String file=getLogFile();
					FileChooser fileDialog =  new FileChooser(file);
					fileDialog.setCurrentDirectory(new File(getWorkingDirectory()));
					fileDialog.setDialogTitle(rsrc.getString("MainFrame.logFileChooserTitle"));
					fileDialog.setFileFilter(new GenericFileFilter(rsrc.getString("MainFrame.logFileFilter"),"log"));
					fileDialog.addChoosableFileFilter(new GenericFileFilter(rsrc.getString("MainFrame.txtFileFilter"),"txt"));

					if (fileDialog.showSaveDialog(MainFrame.this) == FileChooser.APPROVE_OPTION) {
						setWorkingDirectory(fileDialog.getCurrentDirectory().getAbsolutePath());
						file=fileDialog.getSelectedFile().getAbsolutePath();
						setLogFile(file);
					}					
				}
			});
		}
		return doLogFileSelBtn;
	}
	private javax.swing.JButton getDoXtfLogFileSelBtn() {
		if(doXtfLogFileSelBtn == null) {
			doXtfLogFileSelBtn = new javax.swing.JButton();
			doXtfLogFileSelBtn.setText("...");
			doXtfLogFileSelBtn.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					String file=getLogFile();
					FileChooser fileDialog =  new FileChooser(file);
					fileDialog.setCurrentDirectory(new File(getWorkingDirectory()));
					fileDialog.setDialogTitle(rsrc.getString("MainFrame.xtflogFileChooserTitle"));
                    fileDialog.addChoosableFileFilter(new GenericFileFilter(rsrc.getString("MainFrame.xtfFileFilter"),"xtf"));
                    fileDialog.setFileFilter(GenericFileFilter.createCsvFilter());

					if (fileDialog.showSaveDialog(MainFrame.this) == FileChooser.APPROVE_OPTION) {
						setWorkingDirectory(fileDialog.getCurrentDirectory().getAbsolutePath());
						file=fileDialog.getSelectedFile().getAbsolutePath();
						setXtfLogFile(file);
					}					
				}
			});
		}
		return doXtfLogFileSelBtn;
	}
    private javax.swing.JButton getNewConfigFileBtn() {
        if(doNewConfigFileBtn == null) {
            doNewConfigFileBtn = new javax.swing.JButton();
            doNewConfigFileBtn.setText("new..");
            doNewConfigFileBtn.addActionListener(new java.awt.event.ActionListener() { 
                public void actionPerformed(java.awt.event.ActionEvent e) {    
                    String file=getLogFile();
                    FileChooser fileDialog =  new FileChooser(file);
                    fileDialog.setCurrentDirectory(new File(getWorkingDirectory()));
                    fileDialog.setDialogTitle(rsrc.getString("MainFrame.xtflogFileChooserTitle"));
                    fileDialog.setFileFilter(createConfigFileFilter());

                    if (fileDialog.showSaveDialog(MainFrame.this) == FileChooser.APPROVE_OPTION) {
                        setWorkingDirectory(fileDialog.getCurrentDirectory().getAbsolutePath());
                        file=fileDialog.getSelectedFile().getAbsolutePath();
                        setConfigFile(file);
                        writeNewConfigFile(file);
                    }                   
                }
                private void writeNewConfigFile(String file) {
                    BufferedWriter writer = null;
                    if (file != null) {
                        try {
                            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
                            // 1. Configuration
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine1"));
                            
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine2"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine3"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine4"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine5"));
                            
                            // 2. Configuration
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine6"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine7"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine8"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine9"));
                            
                            // 3. Configuration
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine10"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine11"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine12"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine13"));
                            
                            // 4. Configuration
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine14"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine15"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine16"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine17"));
                            
                            // 5. Configuration
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine18"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine19"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine20"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine21"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine22"));
                            
                            // 6. Configuration
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine23"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine24"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine25"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine26"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine27"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine28"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine29"));
                            
                            // 7. Configuration
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine30"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine31"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine32"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine33"));
                            
                            // 8. Configuration
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine34"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine35"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine36"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine37"));
                            writer.write(rsrc.getString("MainFrame.ConfigFileLine38"));
                        } catch (IOException e) {
                            EhiLogger.logError(e);
                        } finally {
                            if (writer != null) {
                                try {
                                    writer.close();
                                } catch (IOException e) {
                                    EhiLogger.logError(e);
                                }
                            }
                        }
                    }
                }
            });
        }
        return doNewConfigFileBtn;
    }
    
    private javax.swing.JButton getDoConfigFileSelBtn() {
		if(doConfigFileSelBtn == null) {
			doConfigFileSelBtn = new javax.swing.JButton();
			doConfigFileSelBtn.setText("...");
			doConfigFileSelBtn.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					String file=getConfigFile();
					FileChooser fileDialog =  new FileChooser(file);
					fileDialog.setCurrentDirectory(new File(getWorkingDirectory()));
					fileDialog.setDialogTitle(rsrc.getString("MainFrame.configFileChooserTitle"));
					fileDialog.setFileFilter(createConfigFileFilter());

					if (fileDialog.showOpenDialog(MainFrame.this) == FileChooser.APPROVE_OPTION) {
						setWorkingDirectory(fileDialog.getCurrentDirectory().getAbsolutePath());
						file=fileDialog.getSelectedFile().getAbsolutePath();
						setConfigFile(file);
					}					
				}

			});
		}
		return doConfigFileSelBtn;
	}
    private javax.swing.JButton getDoMetaConfigFileSelBtn() {
        if(doMetaConfigFileSelBtn == null) {
            doMetaConfigFileSelBtn = new javax.swing.JButton();
            doMetaConfigFileSelBtn.setText("...");
            doMetaConfigFileSelBtn.addActionListener(new java.awt.event.ActionListener() { 
                public void actionPerformed(java.awt.event.ActionEvent e) {    
                    String file=getMetaConfigFile();
                    FileChooser fileDialog =  new FileChooser(file);
                    fileDialog.setCurrentDirectory(new File(getWorkingDirectory()));
                    fileDialog.setDialogTitle(rsrc.getString("MainFrame.metaConfigFileChooserTitle"));
                    fileDialog.setFileFilter(createMetaConfigFileFilter());

                    if (fileDialog.showOpenDialog(MainFrame.this) == FileChooser.APPROVE_OPTION) {
                        setWorkingDirectory(fileDialog.getCurrentDirectory().getAbsolutePath());
                        file=fileDialog.getSelectedFile().getAbsolutePath();
                        setMetaConfigFile(file);
                    }                   
                }

            });
        }
        return doMetaConfigFileSelBtn;
    }
    private GenericFileFilter createConfigFileFilter() {
        return new GenericFileFilter(rsrc.getString("MainFrame.configFileFilter"),"ini");
    }
    private GenericFileFilter createMetaConfigFileFilter() {
        return new GenericFileFilter(rsrc.getString("MainFrame.metaConfigFileFilter"),"ini");
    }
	private java.lang.String getWorkingDirectory() {
		String wd=settings.getValue(ch.interlis.ili2c.gui.UserSettings.WORKING_DIRECTORY);
		if(wd==null){
			wd=new File(".").getAbsolutePath();
		}
		return wd;
	}
	private void setWorkingDirectory(java.lang.String workingDirectory) {
		settings.setValue(ch.interlis.ili2c.gui.UserSettings.WORKING_DIRECTORY, workingDirectory);
	}
}
