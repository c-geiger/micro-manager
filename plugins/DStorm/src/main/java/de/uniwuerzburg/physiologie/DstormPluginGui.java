package de.uniwuerzburg.physiologie;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.nio.file.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.micromanager.Studio;
import org.micromanager.acquisition.SequenceSettings;
import org.micromanager.internal.MMStudio;
import mmcorej.CMMCore;
import mmcorej.StrVector;

import javax.swing.border.EtchedBorder;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.InputMethodListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFileChooser.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTabbedPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.JSlider;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JProgressBar;

public class DstormPluginGui extends JFrame {
	private JLabel labFilesstem;
	private JLabel labPathstem;
	private JLabel labImageSize;
	private JLabel labStorPathScan;
	private JLabel labExposure;
	private JLabel labFramesPScanS;
	private JLabel labScanSpeedS;
	private JLabel labEmGainS;
	private JButton btnStartDistS;
	private JLabel labStartPosS;
	private JLabel valStartPosS;
	private JLabel labEndPosS;
	private JLabel valEndPosS;
	private JLabel labScanDepthS;
	private JLabel labNoScansS ;
	private JLabel labFramesPMicroS;
	private JLabel labStorPathBeadsBef;
	private JLabel labStorPathBeadsAf;
	private JLabel labExpBeads;
	private JLabel labFramesBeads;
	private JLabel labEmGainBeads ;
	private JLabel valStartPosBefore;
	private JLabel valStartAfter;
	private JLabel labStorPathEpi;
	private JLabel labExpEpi;
	private JLabel labEmGainEpi;
	private JLabel labExpCal ;
	private JLabel labFramesPScanCal;
	private JLabel labScanspeedCal;
	private JLabel labEmGainCal ;
	private JLabel valFocusPosCal ;
	private JLabel labScanDepthCal;
	private JLabel labStartPosCal;
	private JLabel valStartPosCal;
	private JLabel labEndPosCal;
	private JLabel valEndPosCal;
	private JLabel labFramesPMicroCal;
	private StrVector cameras;
	
	
	private String pathstate;
	
	private JRadioButton rdbtnChannel_1;
	private JRadioButton rdbtnChannel_2;
	
	private JPanel contentPane;
	private JTextField tfFilestem;
	private JTextField tfPathstem;
	private JTextField tfScanDepthS;
	private JTextField tfFramesPScanS;
	private JTextField tfNoScansS;
	private JTextField tfEmGainS;
	private JTextField tfStartDistS;
	private JTextField tfExpScan;
	private JTextField tfExpBeads;
	private JTextField tfFramesPScanBeads;
	private JTextField tfEmGainBeads;
	private JTextField tfExpCal;
	private JTextField tfFramesPScanCal;
	private JTextField tfEmGainCal;
	private JTextField tfScanDepthCal;
	private JTextField tfScanspeedS;
	//private JLabel tfStartPosS;
	private JLabel tfEndPosS;
	private JTextField tfFramesPMicroS;
	private JTextField tfExpEpi;
	private JTextField tfEmGainEpi;
	private JTextField tfScanspeedCal; 
	private JTextField tfFramesPMicroCal;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField tfImageSize;
	private String path2;
	
	private String prefix ;
	private String root ;
	private String channel = "channel1";
	private String parRoot;

	private String valStartPosSString;
	private String valEndPosSString;
	private String valStartPosCalString; 
	private String valEndPosCalString ;
	private boolean channel2Selected = false ;
	
	
	private double tempStartPosCal;
	private double tempEndPosCal;
	private double vartfScanDepthCal;
	private double vartfFramesPScanCal;
	private double vartfFramesPMicroCal;
	private double vartfScanspeedCal;
	private boolean camsettings;
	
	private volatile boolean exit = false;
	
	
	AccessorySequenceSettings accSettings = new AccessorySequenceSettings();
	
	
	private AccessorySequenceSettings accSettingsChannel1;
	private CameraSelectionbox defaultcambox;
	FolderName folderName = new FolderName(accSettings);
	
	
	
	
 
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					DstormPluginGui frame = new DstormPluginGui();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//	
 
//declare necessary variables
	

	 
	 



private String calibrationBeadsPos = "222";
private double doubleCalibrationBeadsPos = Double.parseDouble(calibrationBeadsPos);
private double doubleBeadsPos;
private String stringBeadsPos;
private String beadsPosString = String.valueOf(doubleBeadsPos);

private PluginEngine pluginEngine;
private SequenceSettings settings ;
private JCheckBox chckbxSetCamsettingsIn;
private Studio studio;
private CMMCore mmc;
private MMStudio MMStudio;
private String tempPoszS;
private double tempPosz;
private double distPosEpi;
private double distPosScan;
private String newPoszS;
private double newPosz;
private double tempstepsize;
private String channel1AssPath;
private double epistartPosz;
private String startDist;
private double beadsstartPosz;

//private AccessorySequenceSettings AccessorySequenceSettings;

private Component verticalStrut_8;
private JButton btnPiezoReset;
private JButton btnStop;
private JButton btnsetFocusPosEpi;
private JTextField tfDistToCovEpi;
private JLabel labDistToCovEpi;
private JLabel valRecordingPositionEpi;
private JLabel labEpirecordingposition;
private JPanel panel_1;
private JPanel piezoGui;
private JPanel panel_2;
private JButton btnRefresh;
private Component verticalStrut_2;
private JLabel lsbeEnterZPos;
private JLabel labZPos;
private Component verticalStrut_7;
private JLabel label_2;
private JLabel labBigStep;
private JPanel panel_3;
private JButton btnUpBig;
private JButton btnUpSmall;
private JTextField tfEnterZPos;
private JLabel valZPos;
private JButton btnDownSmall;
private JButton btnDownBig;
private Component verticalStrut_11;
private JTextField tfSmallStep;
private JTextField tfBigStep2;
private JSlider slider;
private Component verticalStrut_12;
private Component verticalStrut_13;
private Component verticalStrut_15;
private JToggleButton tglbtnNewToggleButton;
private Component horizontalStrut;
private boolean sliderChangeListenerActive = true;
private Piezo piezo;
private JButton btnNewButton;
private Component verticalStrut_16;
private Component verticalStrut_17;
private Component verticalStrut_21;
private Component verticalStrut_22;
private JButton btnStopLive;
private JButton btnStartLive;
private JLabel labLiveExposure;
private JTextField tfLiveExposure;
private Component verticalStrut_18;
private JLabel lblSelectCamera;
private CameraSelectionbox cameraSelectionboxLiveCam;
private CameraSelectionbox cameraSelectionbox3;
private JLabel lblNewLabel_1;
private CameraSelectionbox cameraSelectionbox2;
private JLabel lblBeads;
private CameraSelectionbox cameraSelectionbox1;
private JLabel lblNewLabel_2;
private CameraSelectionbox cameraSelectionbox4;
private JLabel lblNewLabel;
private JButton btnstoppLive;
private Component verticalStrut_14;
private Component verticalStrut_19;
private JCheckBox chckbxNewCheckBox;
private Component verticalStrut_20;
private JButton btnSetRoi;
private JButton btnStopCal;
private JLabel labStorPathCal;
private Component verticalStrut_23;
private JLabel lblScanRunning;
public void setLblScanRunning(String scanRunning) {
	if (scanRunning.equals("running")){
	lblScanRunning.setText("Scan running :");
	lblScanRunning.setForeground(Color.RED);
	labScannumber.setForeground(Color.RED);
	}
	if (scanRunning.equals("finished")){
	lblScanRunning.setText("recording completed!");
	lblScanRunning.setForeground(Color.BLACK);
	labScannumber.setForeground(Color.BLACK);
	}
	if (scanRunning.equals("reset")){
		lblScanRunning.setText("");
		labScannumber.setText("");
		}
}
private String piezoPort;

private JLabel labScannumber;
private JProgressBar progressBar=new JProgressBar();
private JTextField tfPiezoCom;
public JProgressBar getProgressBar() {
	return progressBar;
}
public void setLabScannumber(String scanNo) {
	labScannumber.setText(scanNo);
	
}
	/**
	 * Create the frame.
	 * @param pluginEngine 
	 */


	public DstormPluginGui( Studio app_,String piezoPort) {
		setTitle("dStorm Volume Recording V 1.0 2017");
		this.studio = app_;
		this.piezoPort = piezoPort;
		mmc =studio.getCMMCore();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				org.micromanager.internal.MMStudio.USE_CUSTOM_PATH=false;
			}
		});
		
		this.studio= app_;
		PluginUtils pluginUtils= new PluginUtils(this);
		this.piezo =new Piezo(accSettings, app_, this, pluginUtils, piezoPort);
		this.pluginEngine = new PluginEngine(app_, accSettings, folderName, piezo, this, pluginUtils);
		accSettings.channel= "channel1";
		//PiezoRun2 piezorun2 = new PiezoRun2 (accSettings, app_, pluginengine);	
		
		//settings=app_.acquisitions().getAcquisitionSettings();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1250, 831);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JButton btnSaveSettings = new JButton("save settings");
		btnSaveSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setaccSettings();
				try {
					JFileChooser fileChooser = new JFileChooser();
					int returnVal = fileChooser.showSaveDialog(null);
		            if (returnVal == JFileChooser.APPROVE_OPTION) {
		                File file = fileChooser.getSelectedFile();
		                String path = file.getPath();
		                if(!path.endsWith(".ass")){
		                path= path + ".ass";
		                }
		                
		                accSettings.save(path);
		                System.out.println("Saving: " + file.getName());
		            } else {
		            	System.out.println("Save command cancelled by user." );
		            }
		            
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		menuBar.add(btnSaveSettings);
		
		JButton btnLoadSettings = new JButton("load settings ");
		btnLoadSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				FileFilter filter = new FileNameExtensionFilter("fileextension","ass");	
	        	JFileChooser fileChooser = new JFileChooser();
	        	fileChooser.addChoosableFileFilter(filter);
	    		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
	    		
	    		
	    		//fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    		int result = fileChooser.showOpenDialog(null);
	    		if (result == JFileChooser.APPROVE_OPTION) {
	    		    File selectedFile = fileChooser.getSelectedFile();
	    		    String path = selectedFile.getPath();
	    		    
	    		    if (!path.endsWith(".ass")){
		    			System.out.println("not a valid settings file" );
		    			java.awt.Toolkit.getDefaultToolkit().beep();
	    		    }
	    		    else{
	        	
					
					accSettings = AccessorySequenceSettings.load(path);
					
					
					accSettings.scanFilename = null;
					accSettings.scanPathname = null;
					accSettings.epiFilename =null;
					accSettings.epiPathname = null;
					accSettings.calFilename = null;
					accSettings.calPathname = null;
					accSettings.beadsBeforeFilename = null;
					accSettings.beadsAfterFilename = null;
					accSettings.beadsPathname = null;
					accSettings.accSettingsFilename = null;
					accSettings.metadataFilename = null;
					accSettings.metadataPath = null;
					tfImageSize.setText(String.valueOf(accSettings.imageSizeS));
					tfExpScan.setText(String.valueOf(accSettings.expS));
					tfFramesPScanS.setText(String.valueOf(accSettings.framesPScanS));
					tfScanspeedS.setText(String.valueOf(accSettings.scanSpeedS));
					tfEmGainS.setText(String.valueOf(accSettings.emGainS));
					tfScanDepthS.setText(String.valueOf(accSettings.scanDepthS));
					tfNoScansS.setText(String.valueOf(accSettings.noScansS));
					tfFramesPMicroS.setText(String.valueOf(accSettings.framesPMicroS));
					tfExpBeads.setText(String.valueOf(accSettings.expBeads));
					tfFramesPScanBeads.setText(String.valueOf(accSettings.framesPScanBeads));
					tfEmGainBeads.setText(String.valueOf(accSettings.emGainBeads));
					tfExpEpi.setText(String.valueOf(accSettings.expEpi));
					tfEmGainEpi.setText(String.valueOf(accSettings.emGainEpi));
					tfExpCal.setText(String.valueOf(accSettings.expCal));
					tfFramesPScanCal.setText(String.valueOf(accSettings.framesPScanCal));
					tfScanspeedCal.setText(String.valueOf(accSettings.scanSpeedCal));
					tfScanDepthCal.setText(String.valueOf(accSettings.scanDepthCal));
					tfEmGainCal.setText(String.valueOf(accSettings.emGainCal));
					accSettings.startPositionScan=  0;
					accSettings.endPositionScan=  0;
					accSettings.positionBeadsBefore=  0;
					accSettings.positionBeadsAfter= 0;
					accSettings.positionEpi=  0;
					accSettings.startPositionCalibration=  0;
					accSettings.endPositionCalibration=  0;
					tfFramesPMicroCal.setText(String.valueOf(accSettings.framesPmicroCal));
					tfStartDistS.setText(String.valueOf(accSettings.distTocoverslipS));
					tfDistToCovEpi.setText(String.valueOf(accSettings.distanceToCoverslipEpi));
				}
	    		}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		menuBar.add(btnLoadSettings);
		
		horizontalStrut = Box.createHorizontalStrut(20);
		menuBar.add(horizontalStrut);
		
		tglbtnNewToggleButton = new JToggleButton("Filter Off");
		tglbtnNewToggleButton.setIcon(new ImageIcon(DstormPluginGui.class.getResource("/org/micromanager/icons/rgb_red_blank.png")));
		tglbtnNewToggleButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tglbtnNewToggleButton.isSelected()){
					tglbtnNewToggleButton.setText("Filter On");
					tglbtnNewToggleButton.setIcon(new ImageIcon(DstormPluginGui.class.getResource("/org/micromanager/icons/rgb_green_blank.png")));

					try {
						mmc.setState("ThorlabsMFF", 1);
					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}
				else{
					tglbtnNewToggleButton.setText("Filter Off");
					tglbtnNewToggleButton.setIcon(new ImageIcon(DstormPluginGui.class.getResource("/org/micromanager/icons/rgb_red_blank.png")));
					try {
						mmc.setState("ThorlabsMFF", 0);
					} catch (Exception e2) {
						System.out.println("Error: Can't set state for ThorlabsMFF device.\n");
						e2.printStackTrace();
					}
				}
			}
	
		}); 
		menuBar.add(tglbtnNewToggleButton);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		panel_1 = new JPanel();
		contentPane.add(panel_1);
		
		JPanel panel = new JPanel();
		panel.setToolTipText("");
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED, null, null), "FileInput", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setLayout(new GridLayout(0, 3, 0, 0));
		
		JPanel FileInput = new JPanel();
		panel.add(FileInput);
		FileInput.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Recording path and filename", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		FileInput.setLayout(new GridLayout(9, 2, 0, 0));
		
		labFilesstem = new JLabel("Enter filestem");
		FileInput.add(labFilesstem);
		
		tfFilestem = new JTextField();
		tfFilestem.setText("test");
		tfFilestem.setBackground(Color.WHITE);
		FileInput.add(tfFilestem);
		tfFilestem.setColumns(30);
		tfFilestem.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                
            	try {
                	prefix=tfFilestem.getText();
            		accSettings.prefix = prefix;
            		System.out.println("prefix__"+accSettings.prefix );
                	
                } catch (Exception e1) {
                	
                	e1.printStackTrace();
                }
            }
        });
		
		labPathstem = new JLabel("Enter pathstem");
		FileInput.add(labPathstem);
		
		tfPathstem = new JTextField();
		tfPathstem.setText("F:\\debug");
		tfPathstem.setBackground(Color.WHITE);
		FileInput.add(tfPathstem);
		tfPathstem.setColumns(30);
		
		tfPathstem.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                
            	try {
                	root=tfPathstem.getText();
            		accSettings.root = root;
            		System.out.println("root__"+accSettings.root);
            		
                	
                } catch (Exception e1) {
                	e1.printStackTrace();
                }
            }
        });
		
		Component verticalStrut_9 = Box.createVerticalStrut(20);
		FileInput.add(verticalStrut_9);
		
		JButton btnBrowse = new JButton("Browse");
		
		FileInput.add(btnBrowse);
		
		btnBrowse.addActionListener(new ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		        try { 	
		        	
		        	if (!isChannel2Selected()){
		        		JFileChooser fileChooser = new JFileChooser();
		    		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		    		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    		int result = fileChooser.showOpenDialog(null);
		    		if (result == JFileChooser.APPROVE_OPTION) {
		    		    File selectedFile = fileChooser.getSelectedFile();
		    		    Path path = Paths.get(selectedFile.getPath());
		    		    root = path.toString();
		    		    Path filename = path.getFileName();
		    		    accSettings.prefix =filename.toString();
		    		    File directory = fileChooser.getCurrentDirectory();
		    		    accSettings.root =root;
		    		    tfPathstem.setText(root);
		    		    File pardirectory = fileChooser.getCurrentDirectory();
		    		    accSettings.parRoot=(pardirectory.toString());}
		    		
		        	}
		        	
		        	else{
		        		
		        		JFileChooser fileChooser = new JFileChooser();
			    		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			    		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    		int result = fileChooser.showOpenDialog(null);
			    		if (result == JFileChooser.APPROVE_OPTION) {
			    		    File selectedFile = fileChooser.getSelectedFile();
			    		    Path path = Paths.get(selectedFile.getPath());
			    		    root = path.toString();
			    		    Path filename = path.getFileName();
			    		    String filenameS = filename.toString();
			    		    
			    		    try {
								//channel1AssPath  = root + File.separator + filenameS + File.separator + filenameS + "_channel1" + File.separator + "Scan_" + filenameS+ File.separator + filenameS + ".ass";
								channel1AssPath  = root + File.separator + filenameS + "_channel1" + File.separator + filenameS + "ch1.ass";
								AccessorySequenceSettings accSettingsChannel1 = AccessorySequenceSettings.load(channel1AssPath);
								System.out.println("settings file loaded = "+ channel1AssPath );
							
			    		    
			    		    
			    		    
			    		    // get values from Channel 1 settings file
			    		    System.out.println("settings file loaded imagesizeS= "+ accSettingsChannel1.imageSizeS );
			    		    accSettings.imageSizeS=accSettingsChannel1.imageSizeS;
							tfImageSize.setText(String.valueOf(accSettings.imageSizeS));
							accSettings.scanDepthS=accSettingsChannel1.scanDepthS;
							tfScanDepthS.setText(String.valueOf(accSettings.scanDepthS));
							accSettings.distTocoverslipS=accSettingsChannel1.distTocoverslipS;
							tfStartDistS.setText(String.valueOf(accSettings.distTocoverslipS));
							accSettings.distanceToCoverslipEpi=accSettingsChannel1.distanceToCoverslipEpi;
							tfDistToCovEpi.setText(String.valueOf(accSettings.distanceToCoverslipEpi));
							
							
			    		    
			    		    
			    		    
			    		    accSettings.channel1AssPath=channel1AssPath;
			    		    accSettings.prefix =filename.toString();
			    		    tfFilestem.setText(filename.toString());
			    		    File directory = fileChooser.getCurrentDirectory();
			    		    accSettings.root =(directory.toString());
			    		    tfPathstem.setText(directory.toString());
			    		    
			    		    
			    		    fileChooser.changeToParentDirectory();
			    		    File pardirectory = fileChooser.getCurrentDirectory();
			    		    accSettings.parRoot=(pardirectory.toString());
			    		    } catch (Exception e1) {
								System.out.println("could not load settings file");
								java.awt.Toolkit.getDefaultToolkit().beep();
								e1.printStackTrace();
							}
			    		    
			    		  
			    		}
		        		
		        	}
		    		
		        } catch (Exception e1) {
		        	e1.printStackTrace();
		        }
		    }
		});
		
		

		
		Component verticalStrut_6 = Box.createVerticalStrut(20);
		FileInput.add(verticalStrut_6);
		
		JButton btnStartNewImage = new JButton("Create New Image Path");
		btnStartNewImage.setBackground(Color.GREEN);
		FileInput.add(btnStartNewImage);
		
		verticalStrut_15 = Box.createVerticalStrut(20);
		FileInput.add(verticalStrut_15);
		
		verticalStrut_16 = Box.createVerticalStrut(20);
		FileInput.add(verticalStrut_16);
		
		verticalStrut_17 = Box.createVerticalStrut(20);
		FileInput.add(verticalStrut_17);
		
		verticalStrut_14 = Box.createVerticalStrut(20);
		FileInput.add(verticalStrut_14);
		
		verticalStrut_19 = Box.createVerticalStrut(20);
		FileInput.add(verticalStrut_19);
		
		verticalStrut_21 = Box.createVerticalStrut(20);
		FileInput.add(verticalStrut_21);
		
//		labLiveExposure = new JLabel("exposure Live [ms]");
//		labLiveExposure.setHorizontalAlignment(SwingConstants.CENTER);
//		FileInput.add(labLiveExposure);
//		
//		tfLiveExposure = new JTextField();
//		tfLiveExposure.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				try {
//					mmc.setExposure(Double.parseDouble(tfLiveExposure.getText()));
//				} catch (NumberFormatException e) {
//					pluginUtils.errorDialog("could not set exposure");
//					e.printStackTrace();
//				} catch (Exception e) {
//					pluginUtils.errorDialog("could not set exposure");
//					e.printStackTrace();
//				}
//				
//			}
//		});
//		tfLiveExposure.setBackground(Color.WHITE);
//		FileInput.add(tfLiveExposure);
//		tfLiveExposure.setColumns(10);
		
		btnNewButton = new JButton("Close all images");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				app_.displays().closeAllDisplayWindows(false);
			}
		});
		
		setBtnStartLive(new JButton("Start Live"));
		getBtnStartLive().setIcon(new ImageIcon(DstormPluginGui.class.getResource("/de/uniwuerzburg/physiologie/resources/camera_go.png")));
		getBtnStartLive().addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 app_.live().setLiveMode(true);
	            
	         }
	      });

		
		
		FileInput.add(getBtnStartLive());
		
		btnstoppLive = new JButton("Stop Live");
		btnstoppLive.setIcon(new ImageIcon(DstormPluginGui.class.getResource("/de/uniwuerzburg/physiologie/resources/stopklein.gif")));
		FileInput.add(btnstoppLive);
		btnstoppLive.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	            app_.live().setLiveMode(false);
	         }
	      });
		
		
		
		
		
		btnNewButton.setIcon(new ImageIcon(DstormPluginGui.class.getResource("/org/micromanager/icons/close_windows.png")));
		FileInput.add(btnNewButton);
		
		btnStartNewImage.addActionListener(new ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		        try { 	
		        	setLblScanRunning("reset");
		        	accSettings.clearAccSettings();
		        	root=tfPathstem.getText();
		        	prefix=tfFilestem.getText();
		        	accSettings.prefix= prefix;
		        	accSettings.root= root;
		        	channel = accSettings.channel;
		        	valEndPosCal.setText("empty");
		        	valStartPosCal.setText("empty");
		        	valRecordingPositionEpi.setText("empty");
		        	valStartPosS.setText("empty");
		        	valEndPosS.setText("empty");
		        	folderName.createImgDirectory (root, prefix, channel);
		        	
		        	setLabelsPreliminary();
        			
		        } catch (Exception e1) {
		        	e1.printStackTrace();
		        }
		    }
		});
		
				
				JPanel CannelSelection = new JPanel();
				panel.add(CannelSelection);
				CannelSelection.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Channel and image size", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
				CannelSelection.setLayout(new GridLayout(9, 2, 0, 0));
				
				rdbtnChannel_1 = new JRadioButton("Channel 1");
				buttonGroup.add(rdbtnChannel_1);
				CannelSelection.add(rdbtnChannel_1);
				
				rdbtnChannel_1.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                
            	try {
                	
            		
            		if (rdbtnChannel_1.isSelected()){
            			accSettings.channel = "channel1";
            			tfStartDistS.setEditable(true);
            			tfScanDepthS.setEditable(true);
            			tfPathstem.setEditable(true);
            			tfFilestem.setEditable(true);
            			
            			
            			setLabelsNull();
            		}
                	
                } catch (Exception e1) {
                	e1.printStackTrace();
                }
            }
        });
				
				
				Component verticalStrut_3 = Box.createVerticalStrut(20);
				CannelSelection.add(verticalStrut_3);
				
				rdbtnChannel_2 = new JRadioButton("Channel 2");
				buttonGroup.add(rdbtnChannel_2);
				CannelSelection.add(rdbtnChannel_2);
				rdbtnChannel_1.setSelected(true);
				
				rdbtnChannel_2.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                
            	try {
                	
            		
            		if (rdbtnChannel_2.isSelected()){
            			accSettings.channel = "channel2";
            			
            			
            			
            			tfStartDistS.setEditable(false);
            			tfScanDepthS.setEditable(false);
            			tfPathstem.setEditable(false);
            			tfFilestem.setEditable(false);
            			
            			setLabelsNull();
            			
            		}
                } catch (Exception e1) {
                	e1.printStackTrace();
                }
            }
        });	
				
				
				Component verticalStrut_4 = Box.createVerticalStrut(20);
				CannelSelection.add(verticalStrut_4);
				
				labImageSize = new JLabel("Image Size [px]");
				CannelSelection.add(labImageSize);
				
				tfImageSize = new JTextField();
				tfImageSize.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						cameras=app_.getCMMCore().getLoadedDevicesOfType(mmcorej.DeviceType.CameraDevice);
						for(String camera : cameras){
							
							accSettings.imageSizeS=Integer.parseInt(tfImageSize.getText());
							int roi = accSettings.imageSizeS;
							int roiBorderx = 256 -(roi/2);
							int roibordery = 256 -(roi/2);
							try {
								app_.getCMMCore().setROI(camera,roiBorderx,roibordery,roi,roi);
							} catch (Exception e1) {
								System.out.println("could not set Roi");
								e1.printStackTrace();
							}
						}
						

					}
				});
				tfImageSize.setBackground(Color.WHITE);
				tfImageSize.setText("512");
				tfImageSize.setColumns(10);
				CannelSelection.add(tfImageSize);
				
				btnSetRoi = new JButton("set Roi");
				btnSetRoi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						cameras=app_.getCMMCore().getLoadedDevicesOfType(mmcorej.DeviceType.CameraDevice);
						for(String camera : cameras){
							
							accSettings.imageSizeS=Integer.parseInt(tfImageSize.getText());
							int roi = accSettings.imageSizeS;
							int roiBorderx = 256 -(roi/2);
							int roibordery = 256 -(roi/2);
							try {
								app_.getCMMCore().setROI(camera,roiBorderx,roibordery,roi,roi);
							} catch (Exception e1) {
								System.out.println("could not set Roi");
								e1.printStackTrace();
							}
						}
						

					}
				});
				CannelSelection.add(btnSetRoi);
				
//				chckbxSetCamsettingsIn = new JCheckBox("set camsettings in \u00B5M-Main module");
//				chckbxSetCamsettingsIn.setSelected(false);
//				chckbxSetCamsettingsIn.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent e) {
//					
//					try {
//						if(chckbxSetCamsettingsIn.isSelected()){
//							accSettings.manuell = true;
//					}
//					else {
//							accSettings.manuell = false;
//						}
//					} catch (Exception e1) {
//						
//					}
//					}
//			});
				
				
				

					
						
				//CannelSelection.add(chckbxSetCamsettingsIn);
				
				Component verticalStrut_5 = Box.createVerticalStrut(20);
				CannelSelection.add(verticalStrut_5);
				
				verticalStrut_12 = Box.createVerticalStrut(20);
				CannelSelection.add(verticalStrut_12);
				
				verticalStrut_18 = Box.createVerticalStrut(20);
				CannelSelection.add(verticalStrut_18);
				
				verticalStrut_13 = Box.createVerticalStrut(20);
				CannelSelection.add(verticalStrut_13);
				
				verticalStrut_23 = Box.createVerticalStrut(20);
				CannelSelection.add(verticalStrut_23);
				
				lblScanRunning = new JLabel("");
				CannelSelection.add(lblScanRunning);
				
				labScannumber = new JLabel("");
				CannelSelection.add(labScannumber);
				
				CannelSelection.add(progressBar);
				//set(handles.progressBar,'visible','off')
				progressBar.setVisible(false);
				
//				lblSelectCamera = new JLabel("select camera");
//				lblSelectCamera.setHorizontalAlignment(SwingConstants.CENTER);
//				CannelSelection.add(lblSelectCamera);
//				
//				cameraSelectionboxLiveCam = new CameraSelectionbox(mmc, pluginUtils, true);
//				CannelSelection.add(cameraSelectionboxLiveCam);
				
				JPanel panelComments = new JPanel();
				panel.add(panelComments);
				panelComments.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Comments", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
				panelComments.setLayout(new BoxLayout(panelComments, BoxLayout.X_AXIS));
				
				JTextPane tpComments = new JTextPane();
				tpComments.setBackground(Color.WHITE);
				panelComments.add(tpComments);
				
				
				
				
		//Piezosteuerung
		piezoGui = new JPanel();
		piezoGui.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED, null, null), "Piezo", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 965, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(piezoGui, GroupLayout.PREFERRED_SIZE, 249, GroupLayout.PREFERRED_SIZE)
					.addGap(4))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
					.addComponent(piezoGui, GroupLayout.PREFERRED_SIZE, 304, Short.MAX_VALUE))
		);
		
		panel_2 = new JPanel();
		panel_2.setLayout(new GridLayout(9, 0, 0, 0));
		
		btnRefresh = new JButton("Refresh ");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshGuiElements(piezo.retrieveZPos(), btnRefresh);
			}
		});
		panel_2.add(btnRefresh);
		
		verticalStrut_2 = Box.createVerticalStrut(20);
		panel_2.add(verticalStrut_2);
		
		lsbeEnterZPos = new JLabel("Enter z-position");
		lsbeEnterZPos.setHorizontalAlignment(SwingConstants.TRAILING);
		panel_2.add(lsbeEnterZPos);
		
		labZPos = new JLabel("z-position");
		labZPos.setHorizontalAlignment(SwingConstants.TRAILING);
		panel_2.add(labZPos);
		
		verticalStrut_7 = Box.createVerticalStrut(20);
		panel_2.add(verticalStrut_7);
		
//		tfPiezoCom = new JTextField();
//		tfPiezoCom.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				piezo.InitializePiezoDevice(tfPiezoCom.getText());
//			}
//		});
//		tfPiezoCom.setHorizontalAlignment(SwingConstants.CENTER);
//		tfPiezoCom.setText("COM12");
//		panel_2.add(tfPiezoCom);
//		tfPiezoCom.setColumns(10);
		
		btnPiezoReset = new JButton("RESET");
		panel_2.add(btnPiezoReset);
		btnPiezoReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					piezo.resetPiezo();
					System.out.println("piezo reseted");
				} catch (Exception e1) {
					pluginUtils.errorDialog("error in piezo Reset");
					e1.printStackTrace();
				}
				//pluginengine.piezorun2();
				
				
			}
		});
		
		label_2 = new JLabel("");
		label_2.setIcon(new ImageIcon(DstormPluginGui.class.getResource("/org/micromanager/icons/stagecontrol/arrowhead-sr.png")));
		label_2.setHorizontalAlignment(SwingConstants.TRAILING);
		panel_2.add(label_2);
		
		labBigStep = new JLabel("");
		labBigStep.setIcon(new ImageIcon(DstormPluginGui.class.getResource("/org/micromanager/icons/stagecontrol/arrowhead-dr.png")));
		labBigStep.setHorizontalAlignment(SwingConstants.TRAILING);
		panel_2.add(labBigStep);
		
		panel_3 = new JPanel();
		panel_3.setLayout(new GridLayout(9, 0, 0, 0));

		btnUpBig = new JButton("");
		btnUpBig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					tempPosz = piezo.retrieveZPos();
					tempstepsize = Double.parseDouble(tfBigStep2.getText());
					newPosz = tempPosz + tempstepsize;
					tempPosz = piezo.setZPos(newPosz);
				} catch (Exception e1) {
					System.out.println("can't obtain zPosition");
					e1.printStackTrace();
				}
				refreshGuiElements(tempPosz, btnUpBig);
			}

		});
		
		btnUpBig.setIcon(new ImageIcon(DstormPluginGui.class.getResource("/org/micromanager/icons/stagecontrol/arrowhead-du.png")));
		btnUpBig.setSelectedIcon(new ImageIcon(DstormPluginGui.class.getResource("/org/micromanager/icons/stagecontrol/arrowhead-dup.png")));
		panel_3.add(btnUpBig);
		
		btnUpSmall = new JButton("");
		btnUpSmall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			try {
				tempPosz = piezo.retrieveZPos();
				tempstepsize= Double.parseDouble(tfSmallStep.getText());
				newPosz = tempPosz + tempstepsize;
				tempPosz = piezo.setZPos(newPosz);
			} catch (Exception e1) {
				System.out.println("can't obtain zPosition");
				e1.printStackTrace();
			}	 
			refreshGuiElements(tempPosz, btnUpSmall);
			}
		});
		
		btnUpSmall.setSelectedIcon(new ImageIcon(DstormPluginGui.class.getResource("/org/micromanager/icons/stagecontrol/arrowhead-sup.png")));
		btnUpSmall.setIcon(new ImageIcon(DstormPluginGui.class.getResource("/org/micromanager/icons/stagecontrol/arrowhead-su.png")));
		panel_3.add(btnUpSmall);
		
		tfEnterZPos = new JTextField();
		tfEnterZPos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tempPosz = piezo.setZPos(Double.parseDouble(tfEnterZPos.getText()));
				refreshGuiElements(tempPosz, tfEnterZPos);
			}
		});
		tfEnterZPos.setBackground(Color.WHITE);
		tfEnterZPos.setHorizontalAlignment(SwingConstants.CENTER);
		tfEnterZPos.setColumns(10);
		panel_3.add(tfEnterZPos);
		
		valZPos = new JLabel("");
		valZPos.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(valZPos);
		
		btnDownSmall = new JButton("");
		btnDownSmall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					tempPosz = piezo.retrieveZPos();
					tempstepsize= Double.parseDouble(tfSmallStep.getText());
					newPosz = tempPosz - tempstepsize;
					tempPosz = piezo.setZPos(newPosz);
					refreshGuiElements(tempPosz, btnDownSmall);
				} catch (Exception e1) {
					System.out.println("can't obtain zPosition");
					e1.printStackTrace();
				}	 
				//refreshGuiElements(tempPosz, btnDownSmall);
				}
			});
		btnDownSmall.setSelectedIcon(new ImageIcon(DstormPluginGui.class.getResource("/org/micromanager/icons/stagecontrol/arrowhead-sdp.png")));
		btnDownSmall.setIcon(new ImageIcon(DstormPluginGui.class.getResource("/org/micromanager/icons/stagecontrol/arrowhead-sd.png")));
		panel_3.add(btnDownSmall);
		
		btnDownBig = new JButton("");
		btnDownBig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tempPosz = piezo.retrieveZPos();
				tempstepsize = Double.parseDouble(tfBigStep2.getText());
				newPosz = tempPosz - tempstepsize;
				tempPosz = piezo.setZPos(newPosz);
				refreshGuiElements(tempPosz, btnDownBig);
			}
		});
		
		
		
		btnDownBig.setSelectedIcon(new ImageIcon(DstormPluginGui.class.getResource("/org/micromanager/icons/stagecontrol/arrowhead-ddp.png")));
		btnDownBig.setIcon(new ImageIcon(DstormPluginGui.class.getResource("/org/micromanager/icons/stagecontrol/arrowhead-dd.png")));
		panel_3.add(btnDownBig);
		
		verticalStrut_11 = Box.createVerticalStrut(20);
		panel_3.add(verticalStrut_11);
		
		tfSmallStep = new JTextField();
		tfSmallStep.setBackground(Color.WHITE);
		tfSmallStep.setText("0.1");
		tfSmallStep.setHorizontalAlignment(SwingConstants.CENTER);
		tfSmallStep.setColumns(10);
		panel_3.add(tfSmallStep);
		
		tfBigStep2 = new JTextField();
		tfBigStep2.setBackground(Color.WHITE);
		tfBigStep2.setText("1");
		tfBigStep2.setHorizontalAlignment(SwingConstants.CENTER);
		tfBigStep2.setColumns(10);
		panel_3.add(tfBigStep2);
		
		slider = new JSlider();
		slider.setValue(100);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (sliderChangeListenerActive) {
					tempPosz = piezo.setZPos((double)slider.getValue());
					refreshGuiElements(tempPosz, slider);
				}
			}

		});
		slider.setToolTipText("");
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setOrientation(SwingConstants.VERTICAL);
		slider.setMaximum(200);
		slider.setMajorTickSpacing(25);
		
		//slider.addChangeListener((ChangeListener) this);
		
		GroupLayout gl_piezoGui = new GroupLayout(piezoGui);
		gl_piezoGui.setHorizontalGroup(
			gl_piezoGui.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_piezoGui.createSequentialGroup()
					.addContainerGap(66, Short.MAX_VALUE)
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addGap(6)
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
					.addGap(6)
					.addComponent(slider, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE))
		);
		gl_piezoGui.setVerticalGroup(
			gl_piezoGui.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_piezoGui.createSequentialGroup()
					.addGroup(gl_piezoGui.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 278, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 278, GroupLayout.PREFERRED_SIZE)
						.addComponent(slider, GroupLayout.PREFERRED_SIZE, 278, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		piezoGui.setLayout(gl_piezoGui);
		
		
		
		
		
		
		
		panel_1.setLayout(gl_panel_1);
		
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane_1);
		
		JPanel Recording = new JPanel();
		tabbedPane_1.addTab("Recording", null, Recording, null);
		Recording.setLayout(new BoxLayout(Recording, BoxLayout.X_AXIS));
		
		JPanel Scanning = new JPanel();
		Recording.add(Scanning);
		Scanning.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED, null, null), "Scanning", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gbl_Scanning = new GridBagLayout();
		gbl_Scanning.columnWidths = new int[]{0, 0};
		gbl_Scanning.rowHeights = new int[]{0, 0, 0, 0};
		gbl_Scanning.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_Scanning.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		Scanning.setLayout(gbl_Scanning);
		
		JPanel panel_13 = new JPanel();
		GridBagConstraints gbc_panel_13 = new GridBagConstraints();
		gbc_panel_13.fill = GridBagConstraints.BOTH;
		gbc_panel_13.insets = new Insets(0, 0, 5, 0);
		gbc_panel_13.gridx = 0;
		gbc_panel_13.gridy = 0;
		Scanning.add(panel_13, gbc_panel_13);
		panel_13.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_13.setLayout(new GridLayout(4, 1, 0, 0));
		
		JButton btnStartScan = new JButton("Start Scan");
		panel_13.add(btnStartScan);
		
		btnStartScan.addActionListener(new ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		    	if (valStartPosS.getText().equals("empty")){
			    	pluginUtils.errorDialog("Start position not defined");
			    	return;
			    }
		    	try { 	
//		        	
		        	
		        	app_.live().setLiveMode(false);
		        	getBtnStartLive().setEnabled(false);
		        	setLblScanRunning("running");
		        	labStorPathScan.setForeground(Color.BLACK);
		        	cameraSelectionbox1.setSelectedCamera(cameraSelectionbox1);
		        	accSettings.scanCamera=(String) cameraSelectionbox1.getSelectedItem();
		        	accSettings.startPositionScan=accSettings.positionBeadsBefore-accSettings.distTocoverslipS;
		        	piezo.setZPos(accSettings.startPositionScan);
		        	Thread.sleep(200);
					valStartPosS.setText(String.valueOf(accSettings.startPositionScan));
					accSettings.endPositionScan=accSettings.startPositionScan-accSettings.scanDepthS;
					valEndPosS.setText(String.valueOf(accSettings.endPositionScan));
		        	
		        	 //write accsettings
		        	 
		        	
		        	accSettings.recordingParadigm = "Scan";
		        	accSettings.imageSizeS = Integer.parseInt(tfImageSize.getText());
		        	accSettings.comments = tpComments.getText();
		        	accSettings.expS = Double.parseDouble(tfExpScan.getText());
		        	accSettings.framesPScanS = Double.parseDouble(tfFramesPScanS.getText());
		        	accSettings.scanSpeedS = Double.parseDouble(tfScanspeedS.getText());
		        	accSettings.emGainS = Integer.parseInt(tfEmGainS.getText());
		        	accSettings.scanDepthS = Double.parseDouble(tfScanDepthS.getText());
		        	accSettings.noScansS = Integer.parseInt(tfNoScansS.getText());
		        	accSettings.framesPMicroS = Double.parseDouble(tfFramesPMicroS.getText());
		        	labStorPathScan.setText(folderName.createScanPath());
		        	
		        	if (!pluginEngine.enoughDiskSpace()){
		        		pluginUtils.errorDialog("Not enough Disk space");
		    			return;	
		    		}
		        		
		        	else{	
		        		Thread thread = new Thread() {
		        		@Override
						public void run() {
							pluginEngine.runSequenceScanacquisition();
							}
		        		
					};
//		        	thread.setPriority(Thread.MAX_PRIORITY);
					thread.start();
		        	}
		        } catch (Exception e1) {
		        	e1.printStackTrace();
		        }
		    }
		});
		
		
		labStorPathScan = new JLabel();
		panel_13.add(labStorPathScan);
		
		btnStop = new JButton("STOP ");
		btnStop.setIcon(new ImageIcon(DstormPluginGui.class.getResource("/de/uniwuerzburg/physiologie/resources/stopklein.gif")));
		
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				accSettings.stopRecording=true;
				 }
		});
		
		verticalStrut_22 = Box.createVerticalStrut(20);
		panel_13.add(verticalStrut_22);
		panel_13.add(btnStop);
		
		
		
		
		
		JPanel panScanSet = new JPanel();
		GridBagConstraints gbc_panScanSet = new GridBagConstraints();
		gbc_panScanSet.fill = GridBagConstraints.BOTH;
		gbc_panScanSet.gridx = 0;
		gbc_panScanSet.gridy = 2;
		Scanning.add(panScanSet, gbc_panScanSet);
		panScanSet.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Settings Scanning", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panScanSet.setLayout(new GridLayout(11, 2, 0, 0));
		
		labExposure = new JLabel("Exposure [ms]");
		panScanSet.add(labExposure);
		
		tfExpScan = new JTextField();
		tfExpScan.setBackground(Color.WHITE);
		tfExpScan.setText("16 ");
		panScanSet.add(tfExpScan);
		tfExpScan.setColumns(10);
		
		labFramesPScanS = new JLabel("frames/scan [fr]");
		panScanSet.add(labFramesPScanS);
		
		tfFramesPScanS = new JTextField();
		tfFramesPScanS.setBackground(Color.WHITE);
		tfFramesPScanS.setText("20000");
		panScanSet.add(tfFramesPScanS);
		tfFramesPScanS.setColumns(10);
		
		
		//abhängige textfelder
		tfFramesPScanS.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                	double vartfFramesPScanS = Double.parseDouble(tfFramesPScanS.getText());
                	double vartfScanDepthS = Double.parseDouble(tfScanDepthS.getText());
                	double vartfFramesPMicroS = Double.parseDouble(tfFramesPMicroS.getText());
                	double vartfScanspeedS = Double.parseDouble(tfScanspeedS.getText());
                	
                	Double tempFramesMicroS = vartfFramesPScanS/vartfScanDepthS;
                	Double tempScanspeedS = vartfScanDepthS*1000/vartfFramesPScanS;
                	
                	String tempFramesMicroSString = String.valueOf(tempFramesMicroS);
                	String tempScanspeedSString = String.valueOf(tempScanspeedS);
                	
                	tfFramesPMicroS.setText(tempFramesMicroSString);
                	tfScanspeedS.setText(tempScanspeedSString);
                	
                } catch (Exception e1) {
                	e1.printStackTrace();
                }
            }
        });
		
		labScanSpeedS = new JLabel("Scanspeed[nm/fr]");
		panScanSet.add(labScanSpeedS);
		
		tfScanspeedS = new JTextField();
		tfScanspeedS.setBackground(Color.WHITE);
		tfScanspeedS.setText("0.5");
		panScanSet.add(tfScanspeedS);
		tfScanspeedS.setColumns(10);
		
		//abhängiges textfeld
		
		tfScanspeedS.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                	double vartfFramesPScanS = Double.parseDouble(tfFramesPScanS.getText());
                	double vartfScanDepthS = Double.parseDouble(tfScanDepthS .getText());
                	double vartfFramesPMicroS = Double.parseDouble(tfFramesPMicroS.getText());
                	double vartfScanspeedS = Double.parseDouble(tfScanspeedS.getText());
                	
                	Double tempFramesPScanS = vartfScanDepthS*1000/vartfScanspeedS;
                	Double tempFramesPMicroS = 1000/vartfScanspeedS;
                	
                	String tempFramesMicroSString = String.valueOf(tempFramesPScanS);
                	String tempScanspeedSString = String.valueOf(tempFramesPMicroS);
                	
                	tfFramesPScanS.setText(tempFramesMicroSString);
                	tfFramesPMicroS.setText(tempScanspeedSString);
                	
                } catch (Exception e1) {
                	e1.printStackTrace();
                }
            }
        });
		
		labEmGainS = new JLabel("EM-Gain");
		panScanSet.add(labEmGainS);
		
		tfEmGainS = new JTextField();
		tfEmGainS.setBackground(Color.WHITE);
		tfEmGainS.setText("200");
		panScanSet.add(tfEmGainS);
		tfEmGainS.setColumns(10);
		
		btnStartDistS = new JButton(" get start distance to coverslip [\u00B5m]");
		btnStartDistS.addActionListener(new ActionListener() {
			

			public void actionPerformed(ActionEvent e) {
				
				tempPosz = piezo.round(piezo.retrieveZPos());
				
				accSettings.distTocoverslipS =piezo.round(accSettings.positionBeadsBefore-tempPosz);
				tfStartDistS.setText(String.valueOf(accSettings.distTocoverslipS));
				
				calculateStartEndPos();
			}
		});
		panScanSet.add(btnStartDistS);
		
		tfStartDistS = new JTextField();
		tfStartDistS.setBackground(Color.WHITE);
		tfStartDistS.setText("1");
		panScanSet.add(tfStartDistS);
		tfStartDistS.setColumns(10);
		
		tfStartDistS.addActionListener(new ActionListener() {
	          public void actionPerformed(java.awt.event.ActionEvent e) {
	              try {
	              	
	            	  calculateStartEndPos();
	              
	              	
	              } catch (Exception e1) {
	              	e1.printStackTrace();
	              }
	          }
	      });
		
		labStartPosS = new JLabel("start position");
		panScanSet.add(labStartPosS);
		
	    valStartPosS = new JLabel();
	    valStartPosS.setText("empty");
	    
	    
	    valStartPosS.setBackground(Color.WHITE);
	    panScanSet.add(valStartPosS);
		
		labEndPosS = new JLabel("end position (calculated)");
		panScanSet.add(labEndPosS);
		
		valEndPosS = new JLabel();
		valEndPosS.setText("empty");
		panScanSet.add(valEndPosS);
		
		
		
		
		
		
		labScanDepthS = new JLabel("scan depth [\u00B5m]");
		panScanSet.add(labScanDepthS);
		
		tfScanDepthS = new JTextField();
		tfScanDepthS.setBackground(Color.WHITE);
		tfScanDepthS.setText("10");
		tfScanDepthS.setColumns(10);
		panScanSet.add(tfScanDepthS);
		
		
		//calculate start end pos
		
		
		
		//abhängiges textfeld
		tfScanDepthS.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                	double vartfFramesPScanS = Double.parseDouble(tfFramesPScanS.getText());
                	double vartfScanDepthS = Double.parseDouble(tfScanDepthS .getText());
                	double vartfFramesPMicroS = Double.parseDouble(tfFramesPMicroS.getText());
                	double vartfScanspeedS = Double.parseDouble(tfScanspeedS.getText());
                	
                	double tempFramesPScanS = vartfFramesPMicroS*vartfScanDepthS;
                	
                	
                	String tempFramesPScanSString = String.valueOf(tempFramesPScanS);
                	
                	
                	tfFramesPScanS.setText(tempFramesPScanSString);
                	calculateStartEndPos();
               	// calculate start end position
//                	double vartfStartDistS = Double.parseDouble(tfStartDistS .getText());
//               		double tempStartPosS = accSettings.startPositionScan - vartfStartDistS;
//            		double tempEndPosS = tempStartPosS - vartfScanDepthS;
//            		
//            		valStartPosSString = String.valueOf(tempStartPosS);
//            		valEndPosSString = String.valueOf(tempEndPosS);
//            		
//            		tfStartPosS.setText(valStartPosSString);
//            		valEndPosS.setText(valEndPosSString);
                
                	
                } catch (Exception e1) {
                	e1.printStackTrace();
                }
            }
        });
		
		
		
		labNoScansS = new JLabel("number of scans (up+down=1 scan)");
		panScanSet.add(labNoScansS);
		
		tfNoScansS = new JTextField();
		tfNoScansS.setBackground(Color.WHITE);
		tfNoScansS.setText("5");
		panScanSet.add(tfNoScansS);
		tfNoScansS.setColumns(10);
		
		labFramesPMicroS = new JLabel("frames/\u00B5m (calculated)");
		panScanSet.add(labFramesPMicroS);
		
		tfFramesPMicroS = new JTextField();
		tfFramesPMicroS.setBackground(Color.WHITE);
		tfFramesPMicroS.setText("2000");
		panScanSet.add(tfFramesPMicroS);
		tfFramesPMicroS.setColumns(10);
		
		lblNewLabel_2 = new JLabel("ScanCamera");
		panScanSet.add(lblNewLabel_2);
		
		cameraSelectionbox1 = new CameraSelectionbox(mmc, pluginUtils, false);
		panScanSet.add(cameraSelectionbox1);
		
		
		//abhängiges textfeld
		tfFramesPMicroS.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                	double vartfFramesPScanS = Double.parseDouble(tfFramesPScanS.getText());
                	double vartfScanDepthS = Double.parseDouble(tfScanDepthS .getText());
                	double vartfFramesPMicroS = Double.parseDouble(tfFramesPMicroS.getText());
                	double vartfScanspeedS = Double.parseDouble(tfScanspeedS.getText());
                	
                	Double tempFramePScanS = vartfFramesPMicroS*vartfScanDepthS;
                	Double tempScanspeedS = 1000/vartfFramesPMicroS;
                	
                	String tempFramesPScanSString  = String.valueOf(tempFramePScanS);
                	String tempScanspeedSString = String.valueOf(tempScanspeedS);
                	
                	tfFramesPScanS.setText(tempFramesPScanSString );
                	tfScanspeedS.setText(tempScanspeedSString);
                	
                } catch (Exception e1) {
                	e1.printStackTrace();
                }
            }
        });
		
		JPanel Beads = new JPanel();
		Recording.add(Beads);
		Beads.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED, null, null), "Beads", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gbl_Beads = new GridBagLayout();
		gbl_Beads.columnWidths = new int[]{0, 0};
		gbl_Beads.rowHeights = new int[]{0, 0, 0, 0};
		gbl_Beads.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_Beads.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		Beads.setLayout(gbl_Beads);
		
		JPanel panel_16 = new JPanel();
		GridBagConstraints gbc_panel_16 = new GridBagConstraints();
		gbc_panel_16.fill = GridBagConstraints.BOTH;
		gbc_panel_16.insets = new Insets(0, 0, 5, 0);
		gbc_panel_16.gridx = 0;
		gbc_panel_16.gridy = 0;
		Beads.add(panel_16, gbc_panel_16);
		panel_16.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_16.setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton btnStartBeadsBefore = new JButton("Start Beads before");
		panel_16.add(btnStartBeadsBefore);
		btnStartBeadsBefore.addActionListener(new ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		        try { 
		        	app_.live().setLiveMode(false);
		        	getBtnStartLive().setEnabled(false);
		        	setLblScanRunning("running");
		        	accSettings.recordingParadigm = "Before";
		        	cameraSelectionbox2.setSelectedCamera(cameraSelectionbox2);		  
		        	accSettings.beadsCamera=(String) cameraSelectionbox2.getSelectedItem();
		        	accSettings.positionBeadsBefore = piezo.retrieveZPos();
		        	
		        	accSettings.imageSizeS = Integer.parseInt(tfImageSize.getText());
		        	accSettings.expBeads = Double.parseDouble(tfExpBeads.getText());
		        	accSettings.framesPScanBeads = Integer.parseInt(tfFramesPScanBeads.getText());
		        	accSettings.emGainBeads = Integer.parseInt(tfEmGainBeads.getText());
		        	accSettings.comments = tpComments.getText();
		        	labStorPathBeadsBef.setText(folderName.createBeadsBeforePath());
		        	labStorPathBeadsBef.setForeground(Color.BLACK);
			        
			        
			        valStartPosBefore.setText(String.valueOf(accSettings.positionBeadsBefore));
			        valStartAfter.setForeground(Color.BLACK);
			        valStartAfter.setText(String.valueOf(accSettings.positionBeadsBefore));
		        	valStartAfter.setForeground(Color.GREEN);
		        	
		        	if (!pluginEngine.enoughDiskSpace()){
		        		pluginUtils.errorDialog("Not enough Disk space");
		    			return;	
		    		}

		        	else{
		        	Thread thread = new Thread(new Runnable() {
						
						@Override
						public void run() {
							pluginEngine.runSequenceBeadsBeforeacquisition();
							
						}
					});
		        	thread.start();
		        	}
		        } catch (Exception e1) {
		        	e1.printStackTrace();
		        }
		    }
		});
		
		labStorPathBeadsBef = new JLabel();
		panel_16.add(labStorPathBeadsBef);
		
		JButton btnStartBeadsAfter = new JButton("Start beads after");
		panel_16.add(btnStartBeadsAfter);
		
		labStorPathBeadsAf = new JLabel();
		panel_16.add(labStorPathBeadsAf);
		
		btnStartBeadsAfter.addActionListener(new ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		        try { 	
		        	app_.live().setLiveMode(false);
		        	getBtnStartLive().setEnabled(false);
		        	setLblScanRunning("running");
		        	accSettings.recordingParadigm = "After";
		        	cameraSelectionbox2.setSelectedCamera(cameraSelectionbox2);
		        	accSettings.beadsCamera=(String) cameraSelectionbox2.getSelectedItem();
		        	accSettings.imageSizeS = Integer.parseInt(tfImageSize.getText());
		        	accSettings.expBeads = Double.parseDouble(tfExpBeads.getText());
		        	accSettings.framesPScanBeads = Integer.parseInt(tfFramesPScanBeads.getText());
		        	accSettings.emGainBeads = Integer.parseInt(tfEmGainS.getText());
		        	accSettings.comments = tpComments.getText();
		        	
		        	labStorPathBeadsAf.setText(folderName.createBeadsAfterPath());
		        	labStorPathBeadsAf.setForeground(Color.BLACK);
		        	
			        accSettings.positionBeadsAfter = piezo.retrieveZPos();
			        
			        
	      			valStartAfter.setText(String.valueOf(accSettings.positionBeadsAfter));
		        	valStartAfter.setForeground(Color.BLACK);
		        	
		        	if (!pluginEngine.enoughDiskSpace()){
		        		pluginUtils.errorDialog("Not enough Disk space");
		    			return;	
		    		}

		        	else{
		        	Thread thread = new Thread(new Runnable() {
						
						@Override
						public void run() {
							pluginEngine.runSequenceBeadsAfteracquisition();
							
						}
					});
		        	thread.start();
		        	
		        	}
		        } catch (Exception e1) {
		        	e1.printStackTrace();
		        }
		    }
		});
		
		JPanel panBeadsSet = new JPanel();
		GridBagConstraints gbc_panBeadsSet = new GridBagConstraints();
		gbc_panBeadsSet.anchor = GridBagConstraints.NORTH;
		gbc_panBeadsSet.fill = GridBagConstraints.HORIZONTAL;
		gbc_panBeadsSet.gridx = 0;
		gbc_panBeadsSet.gridy = 2;
		Beads.add(panBeadsSet, gbc_panBeadsSet);
		panBeadsSet.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Settings beads", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panBeadsSet.setLayout(new GridLayout(7, 2, 0, 0));
		
		labExpBeads = new JLabel("Exposure [ms]");
		panBeadsSet.add(labExpBeads);
		
		tfExpBeads = new JTextField();
		tfExpBeads.setBackground(Color.WHITE);
		tfExpBeads.setText("100");
		tfExpBeads.setColumns(10);
		panBeadsSet.add(tfExpBeads);
		
		labFramesBeads = new JLabel("frames [fr]");
		panBeadsSet.add(labFramesBeads);
		
		tfFramesPScanBeads = new JTextField();
		tfFramesPScanBeads.setBackground(Color.WHITE);
		tfFramesPScanBeads.setText("200");
		tfFramesPScanBeads.setColumns(10);
		panBeadsSet.add(tfFramesPScanBeads);
		
		labEmGainBeads = new JLabel("EM-Gain");
		panBeadsSet.add(labEmGainBeads);
		
		tfEmGainBeads = new JTextField();
		tfEmGainBeads.setBackground(Color.WHITE);
		tfEmGainBeads.setText("200");
		tfEmGainBeads.setColumns(10);
		panBeadsSet.add(tfEmGainBeads);
		
		JButton buttonStartPosBefore = new JButton("get start position before");
		panBeadsSet.add(buttonStartPosBefore);
		buttonStartPosBefore.addActionListener(new ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		       System.out.println("double command");
		    	try { 	
		        	
	    		tempPosz=piezo.round(piezo.retrieveZPos());
		    		//tempPosz=piezo.round(50.0);
		        	
		        	tempPoszS = String.valueOf(tempPosz) ;
		         
		            accSettings.positionBeadsBefore =tempPosz;	
		            valStartPosBefore.setText(tempPoszS);
		            valStartPosBefore.setForeground(Color.BLACK);
		            calculateStartEndPos();
		        	
		        } catch (Exception e1) {
		        	e1.printStackTrace();
		        }
		    }
		});
		
		valStartPosBefore = new JLabel(beadsPosString);
		panBeadsSet.add(valStartPosBefore);
		
		JButton buttonStartPosAfter = new JButton("go to start position after ");
		panBeadsSet.add(buttonStartPosAfter);
		
		buttonStartPosAfter.addActionListener(new ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		        try { 	
		        	tempPosz = accSettings.positionBeadsBefore;
		        	tempPosz=piezo.setZPos(tempPosz);
		        	valStartAfter.setText(String.valueOf(tempPosz));
		        	valStartAfter.setForeground(Color.BLACK);
		        	accSettings.positionBeadsAfter=tempPosz;
		        	refreshGuiElements(tempPosz, buttonStartPosAfter);
		        	
		        } catch (Exception e1) {
		        	e1.printStackTrace();
		        }
		    }
		});
		
		valStartAfter = new JLabel(beadsPosString);
		panBeadsSet.add(valStartAfter);
		
		lblBeads = new JLabel("BeadsCamera");
		panBeadsSet.add(lblBeads);
		
		cameraSelectionbox2 = new CameraSelectionbox(mmc, pluginUtils, false);
		panBeadsSet.add(cameraSelectionbox2);
		
		buttonStartPosBefore.addActionListener(new ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		        try { 	
		        accSettings.positionBeadsBefore =piezo.retrieveZPos();	
		        
		        valStartPosBefore.setText(stringBeadsPos);
      			valStartAfter.setText(stringBeadsPos);
      			valStartAfter.setForeground(Color.MAGENTA);
      			
		        double vartfStartDistS = Double.parseDouble(tfStartDistS.getText());		
		        double vartfScanDepthS = Double.parseDouble(tfScanDepthS.getText());
		        
		        
		        //calculate for scan
		        double tempStartPosS = doubleBeadsPos - vartfStartDistS;
		        double tempEndPosS = tempStartPosS - vartfScanDepthS;
		        
				
				valStartPosSString = String.valueOf(tempStartPosS);
				valEndPosSString = String.valueOf(tempEndPosS);
				
				valStartPosS.setText(valStartPosSString);
				valEndPosS.setText(valEndPosSString);
				 	
		        } catch (Exception e1) {
		        	e1.printStackTrace();
		        }
		    }
		});
		
		
		JPanel EpiReference = new JPanel();
		Recording.add(EpiReference);
		EpiReference.setBorder(new TitledBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED, null, null), "Epi-Reference", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "Epi-Referencee", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagLayout gbl_EpiReference = new GridBagLayout();
		gbl_EpiReference.columnWidths = new int[]{0, 0};
		gbl_EpiReference.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_EpiReference.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_EpiReference.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		EpiReference.setLayout(gbl_EpiReference);
		
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panel_7 = new GridBagConstraints();
		gbc_panel_7.insets = new Insets(0, 0, 5, 0);
		gbc_panel_7.fill = GridBagConstraints.BOTH;
		gbc_panel_7.gridx = 0;
		gbc_panel_7.gridy = 0;
		EpiReference.add(panel_7, gbc_panel_7);
		panel_7.setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton btnStartEpi = new JButton("StartEpi");
		panel_7.add(btnStartEpi);
		
		btnStartEpi.addActionListener(new ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		        try {
		        	app_.live().setLiveMode(false);
		        	accSettings.recordingParadigm = "Epi";
		        	cameraSelectionbox3.setSelectedCamera(cameraSelectionbox3);
		        	accSettings.epiCamera=(String) cameraSelectionbox3.getSelectedItem();
		        	accSettings.positionEpi=Double.parseDouble(valRecordingPositionEpi.getText());
		        	piezo.setZPos(accSettings.positionEpi);
		        	
		        	accSettings.imageSizeS = Integer.parseInt(tfImageSize.getText());
		        	accSettings.expEpi = Double.parseDouble(tfExpEpi.getText());		        	
		        	accSettings.emGainEpi = Integer.parseInt(tfEmGainEpi.getText());
		        	accSettings.comments = tpComments.getText();
		        	labStorPathEpi.setText(folderName.createEpiPath());
		        	labStorPathEpi.setForeground(Color.BLACK);
		        	
		        	
		        	if (!pluginEngine.enoughDiskSpace()){
		        		pluginUtils.errorDialog("Not enough Disk space");
		    			return;	
		    		}

		        	else{
		        	Thread thread = new Thread(new Runnable() {
						
						@Override
						public void run() {
							pluginEngine.runSequenceEpiacquisition();
							
						}
					});
		        	thread.start();
		        	}
		        } catch (Exception e1) {
		        	e1.printStackTrace();
		        }
		    }
		});
		
		
		labStorPathEpi = new JLabel();
		panel_7.add(labStorPathEpi);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		panel_7.add(verticalStrut);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		panel_7.add(verticalStrut_1);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Settings beads", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.insets = new Insets(0, 0, 5, 0);
		gbc_panel_5.fill = GridBagConstraints.BOTH;
		gbc_panel_5.gridx = 0;
		gbc_panel_5.gridy = 2;
		EpiReference.add(panel_5, gbc_panel_5);
		panel_5.setLayout(new GridLayout(6, 2, 0, 0));
		
		labExpEpi = new JLabel("Exposure [ms]");
		panel_5.add(labExpEpi);
		
		tfExpEpi = new JTextField();
		tfExpEpi.setBackground(Color.WHITE);
		tfExpEpi.setText("100");
		tfExpEpi.setColumns(10);
		panel_5.add(tfExpEpi);
		
		labEmGainEpi = new JLabel("EM-Gain");
		panel_5.add(labEmGainEpi);
		
		tfEmGainEpi = new JTextField();
		tfEmGainEpi.setBackground(Color.WHITE);
		tfEmGainEpi.setText("200");
		tfEmGainEpi.setColumns(10);
		panel_5.add(tfEmGainEpi);
		
		labDistToCovEpi = new JLabel("distance to coverslip [\u00B5m]");
		panel_5.add(labDistToCovEpi);
		
		tfDistToCovEpi = new JTextField();
		tfDistToCovEpi.setBackground(Color.WHITE);
		tfDistToCovEpi.setText("5");
		panel_5.add(tfDistToCovEpi);
		tfDistToCovEpi.setColumns(10);
		
		btnsetFocusPosEpi = new JButton("Set Def. Focus Position");
		panel_5.add(btnsetFocusPosEpi);
		btnsetFocusPosEpi.addActionListener(new ActionListener() {
	          public void actionPerformed(java.awt.event.ActionEvent e) {
	              try { 	
	            
	            	  
	            tempPosz = accSettings.positionBeadsBefore;
	            distPosEpi = Double.parseDouble(tfDistToCovEpi.getText());
	            tempPosz=piezo.setZPos(tempPosz - distPosEpi);
	            valRecordingPositionEpi.setText(String.valueOf(tempPosz)); 
      			accSettings.positionEpi=Double.parseDouble(tempPoszS);
      			
      			refreshGuiElements(tempPosz, btnsetFocusPosEpi);
	              } catch (Exception e1) {
	              	e1.printStackTrace();
	              }
	          }
	      });
		
		
		
		JButton btngetFreeFocusPosEpi = new JButton("get Free Focus Position");
		panel_5.add(btngetFreeFocusPosEpi);
		
		btngetFreeFocusPosEpi.addActionListener(new ActionListener() {
	          public void actionPerformed(java.awt.event.ActionEvent e) {
	              try { 	
	      			tempPosz = piezo.retrieveZPos();
	      			valRecordingPositionEpi.setText(String.valueOf(tempPosz));
	      			accSettings.positionEpi = tempPosz;
	              } catch (Exception e1) {
	              	
	            	  e1.printStackTrace();
	              }
	          }
	      });
		
		labEpirecordingposition = new JLabel("epi recording position");
		panel_5.add(labEpirecordingposition);
		
		valRecordingPositionEpi = new JLabel("empty");
		panel_5.add(valRecordingPositionEpi);
		
		lblNewLabel_1 = new JLabel("EpiCamera");
		panel_5.add(lblNewLabel_1);
		
		cameraSelectionbox3 = new CameraSelectionbox(mmc, pluginUtils, false);
		panel_5.add(cameraSelectionbox3);
		
		JPanel Calibration_1 = new JPanel();
		tabbedPane_1.addTab("Calibration", null, Calibration_1, null);
		
		JPanel Calibration = new JPanel();
		Calibration_1.add(Calibration);
		Calibration.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED, null, null), "Calibration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gbl_Calibration = new GridBagLayout();
		gbl_Calibration.columnWidths = new int[]{0, 0};
		gbl_Calibration.rowHeights = new int[]{0, 0, 0, 0};
		gbl_Calibration.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_Calibration.rowWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		Calibration.setLayout(gbl_Calibration);
		
		JPanel startPanCal = new JPanel();
		startPanCal.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagConstraints gbc_startPanCal = new GridBagConstraints();
		gbc_startPanCal.anchor = GridBagConstraints.SOUTH;
		gbc_startPanCal.fill = GridBagConstraints.HORIZONTAL;
		gbc_startPanCal.insets = new Insets(0, 0, 5, 0);
		gbc_startPanCal.gridx = 0;
		gbc_startPanCal.gridy = 0;
		Calibration.add(startPanCal, gbc_startPanCal);
		startPanCal.setLayout(new GridLayout(3, 1, 0, 0));
		
		JButton btnStartCalibrationMeasurement = new JButton("Start calibration measurement");
		startPanCal.add(btnStartCalibrationMeasurement);
		
		btnStartCalibrationMeasurement.addActionListener(new ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		        try { 	
		        	app_.live().setLiveMode(false);
		        	getBtnStartLive().setEnabled(false);
		        	setLblScanRunning("running");
		        	accSettings.recordingParadigm = "Cal";
		        	
		        	// if not yet done by get focus now z pos is retrieved
		        	doubleCalibrationBeadsPos = piezo.retrieveZPos();
		            double vartfScanDepthCal=Double.parseDouble(tfScanDepthCal.getText());
		            tempStartPosCal = doubleCalibrationBeadsPos + vartfScanDepthCal/2;
					tempEndPosCal = tempStartPosCal - vartfScanDepthCal;
					
					valStartPosCalString = String.valueOf(tempStartPosCal);
					valEndPosCalString = String.valueOf(tempEndPosCal);
					calibrationBeadsPos = String.valueOf(doubleCalibrationBeadsPos);
					valStartPosCal.setText(valStartPosCalString);
					valEndPosCal.setText(valEndPosCalString);
					valFocusPosCal.setText(calibrationBeadsPos);  	
		        	///////////////////////////////////////////////////
		        	
		        	System.out.println ("1 ");
		        	cameraSelectionbox4.setSelectedCamera(cameraSelectionbox4);
		        	accSettings.calCamera=(String) cameraSelectionbox4.getSelectedItem();
		        	
		        	accSettings.startPositionCalibration=Double.parseDouble(valStartPosCal.getText());
		        	piezo.setZPos(accSettings.startPositionCalibration);
		        	Thread.sleep(200);
		        	accSettings.endPositionCalibration=Double.parseDouble(valEndPosCal.getText());
		        	
		        	accSettings.imageSizeS = Integer.parseInt(tfImageSize.getText());
		        	
		        	accSettings.expCal = Double.parseDouble(tfExpCal.getText());
		        	
		        	accSettings.framesPScanCal = (int)Double.parseDouble(tfFramesPScanCal.getText());
		        	
		        	accSettings.scanSpeedCal = Double.parseDouble(tfScanspeedCal.getText());
		        	
		        	accSettings.emGainCal = Integer.parseInt(tfEmGainCal.getText());
		        	
		        	accSettings.scanDepthCal = Double.parseDouble(tfScanDepthCal.getText());
		        	
		        	accSettings.framesPmicroCal = Double.parseDouble(tfFramesPMicroCal.getText());
		        	accSettings.comments = tpComments.getText();
		        
		        	
		        	
		        	
		        	labStorPathCal.setText(folderName.createCalPath());
		        	labStorPathCal.setForeground(Color.BLACK);	
		        	if (!pluginEngine.enoughDiskSpace()){
		        		pluginUtils.errorDialog("Not enough Disk space");
		    			return;	
		    		}

		        	else{
		        		Thread thread = new Thread(new Runnable() {
						
						@Override
						public void run() {
							pluginEngine.runSequenceCalacquisition();
							
						}
					});
		        	thread.start();
		        	}
		        } catch (Exception e1) {
		        	e1.printStackTrace();
		        }
		    }
		});
		
		labStorPathCal = new JLabel("");
		startPanCal.add(labStorPathCal);
		
		btnStopCal = new JButton("STOP ");
		btnStopCal.setIcon(new ImageIcon(DstormPluginGui.class.getResource("/de/uniwuerzburg/physiologie/resources/stopklein.gif")));
		startPanCal.add(btnStopCal);
		
		btnStopCal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				accSettings.stopRecording=true;
				 }
		});
		
		JPanel settingsCal = new JPanel();
		GridBagConstraints gbc_settingsCal = new GridBagConstraints();
		gbc_settingsCal.fill = GridBagConstraints.BOTH;
		gbc_settingsCal.gridx = 0;
		gbc_settingsCal.gridy = 2;
		Calibration.add(settingsCal, gbc_settingsCal);
		settingsCal.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Settings Calibration", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		settingsCal.setLayout(new GridLayout(0, 2, 0, 0));
		
		verticalStrut_20 = Box.createVerticalStrut(20);
		settingsCal.add(verticalStrut_20);
		
		chckbxNewCheckBox = new JCheckBox("record Calibration only");
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				accSettings.calOnly=chckbxNewCheckBox.isSelected();
			}
		});
		
		settingsCal.add(chckbxNewCheckBox);
		
		labExpCal = new JLabel("Exposure [ms]");
		settingsCal.add(labExpCal);
		
		tfExpCal = new JTextField();
		tfExpCal.setBackground(Color.WHITE);
		tfExpCal.setText("100");
		tfExpCal.setColumns(10);
		settingsCal.add(tfExpCal);
		
		labFramesPScanCal = new JLabel("frames/scan [fr]");
		settingsCal.add(labFramesPScanCal);
		
		tfFramesPScanCal = new JTextField();
		tfFramesPScanCal.setBackground(Color.WHITE);
		tfFramesPScanCal.setText("2000");
		tfFramesPScanCal.setColumns(10);
		settingsCal.add(tfFramesPScanCal);
		
		tfFramesPScanCal.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
	            try {
	            	double vartfFramesPScanCal = Double.parseDouble(tfFramesPScanCal.getText());
	            	double vartfScanDepthCal = Double.parseDouble(tfScanDepthCal .getText());
	            	double vartfFramesPMicroCal = Double.parseDouble(tfFramesPMicroCal.getText());
	            	double vartfScanSpeedCal = Double.parseDouble(tfScanspeedCal.getText());
	            	
	            	Double tempFramesMicroCal = vartfFramesPScanCal/vartfScanDepthCal;
	            	Double tempScanSpeedCal = vartfScanDepthCal*1000/vartfFramesPScanCal;
	            	
	            	String tempFramesMicroCalString = String.valueOf(tempFramesMicroCal);
	            	String tempScanSpeedCalString = String.valueOf(tempScanSpeedCal);
	            	
	            	tfFramesPMicroCal.setText(tempFramesMicroCalString);
	            	tfScanspeedCal.setText(tempScanSpeedCalString);
	            	
	            } catch (Exception e1) {
	            	e1.printStackTrace();
	            }
	        }
	    });
            
		
		labScanspeedCal = new JLabel("Scanspeed [nm/fr]");
		settingsCal.add(labScanspeedCal);
		
		tfScanspeedCal = new JTextField("1");
		tfScanspeedCal.setBackground(Color.WHITE);
		settingsCal.add(tfScanspeedCal);
		
		tfScanspeedCal.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                	double vartfFramesPScanCal = Double.parseDouble(tfFramesPScanCal.getText());
                	double vartfScanDepthCal = Double.parseDouble(tfScanDepthCal .getText());
                	double vartfFramesPMicroCal = Double.parseDouble(tfFramesPMicroCal.getText());
                	double vartfScanspeedCal = Double.parseDouble(tfScanspeedCal.getText());
                	
                	Double tempFramesPScanCal = vartfScanDepthCal*1000/vartfScanspeedCal;
                	Double tempFramesPMicroCal = 1000/vartfScanspeedCal;
                	
                	String tempFramesMicroCalString = String.valueOf(tempFramesPScanCal);
                	String tempScanspeedCalString = String.valueOf(tempFramesPMicroCal);
                	
                	tfFramesPScanCal.setText(tempFramesMicroCalString);
                	tfFramesPMicroCal.setText(tempScanspeedCalString);
                	
                } catch (Exception e1) {
                	e1.printStackTrace();
                }
            }
        });	
		
		labEmGainCal = new JLabel("EM-Gain");
		settingsCal.add(labEmGainCal);
		
		tfEmGainCal = new JTextField();
		tfEmGainCal.setBackground(Color.WHITE);
		tfEmGainCal.setText("200");
		tfEmGainCal.setColumns(10);
		settingsCal.add(tfEmGainCal);
		
		JButton buttongetFocusPosCal = new JButton("focus position");
		settingsCal.add(buttongetFocusPosCal);
		
		buttongetFocusPosCal.addActionListener(new ActionListener() {
	          public void actionPerformed(java.awt.event.ActionEvent e) {
	              try {
	              	
	            doubleCalibrationBeadsPos = piezo.retrieveZPos();
	            double vartfScanDepthCal=Double.parseDouble(tfScanDepthCal.getText());
	            tempStartPosCal = doubleCalibrationBeadsPos + vartfScanDepthCal/2;
				tempEndPosCal = tempStartPosCal - vartfScanDepthCal;
				
				valStartPosCalString = String.valueOf(tempStartPosCal);
				valEndPosCalString = String.valueOf(tempEndPosCal);
				calibrationBeadsPos = String.valueOf(doubleCalibrationBeadsPos);
				valStartPosCal.setText(valStartPosCalString);
				valEndPosCal.setText(valEndPosCalString);
				valFocusPosCal.setText(calibrationBeadsPos);  	
	              } catch (Exception e1) {
	              	e1.printStackTrace();
	              }
	          }
	      });
		
		
		valFocusPosCal = new JLabel();
		valFocusPosCal.setText("empty");
		settingsCal.add(valFocusPosCal);
		 
		labScanDepthCal = new JLabel("scan depth [\u00B5m]");
		settingsCal.add(labScanDepthCal);
		
		tfScanDepthCal = new JTextField();
		tfScanDepthCal.setBackground(Color.WHITE);
		tfScanDepthCal.setText("2");
		tfScanDepthCal.setColumns(10);
		settingsCal.add(tfScanDepthCal);
		
		tfScanDepthCal.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                	double vartfFramesPScanCal = Double.parseDouble(tfFramesPScanCal.getText());
                	double vartfScanDepthCal = Double.parseDouble(tfScanDepthCal .getText());
                	double vartfFramesPMicroCal = Double.parseDouble(tfFramesPMicroCal.getText());
                	double vartfScanspeedCal= Double.parseDouble(tfScanspeedCal.getText());
                	
                	double tempFramesPScanCal = vartfFramesPMicroCal*vartfScanDepthCal;
                	
                	
                	String tempFramesPScanCalString = String.valueOf(tempFramesPScanCal);
                	
                	
                	tfFramesPScanCal.setText(tempFramesPScanCalString);
                	
               	// calculate start end position

					double tempStartPosCal = doubleCalibrationBeadsPos + vartfScanDepthCal/2;
					double tempEndPosCal = tempStartPosCal - vartfScanDepthCal;
					
					valStartPosCalString = String.valueOf(tempStartPosCal);
					valEndPosCalString = String.valueOf(tempEndPosCal);
					
					valStartPosCal.setText(valStartPosCalString);
					valEndPosCal.setText(valEndPosCalString);
                
                	
                } catch (Exception e1) {
                	e1.printStackTrace();
                }
            }
        });
		
		labStartPosCal = new JLabel("start position (calculated)");
		settingsCal.add(labStartPosCal);
		
		valStartPosCal = new JLabel();
		settingsCal.add(valStartPosCal);
		
		labEndPosCal = new JLabel("end position (calculated)");
		settingsCal.add(labEndPosCal);
		
		valEndPosCal = new JLabel();
		settingsCal.add(valEndPosCal);
		
		labFramesPMicroCal = new JLabel("frames/\u00B5m (calculated)");
		settingsCal.add(labFramesPMicroCal);
		
		tfFramesPMicroCal = new JTextField("1000");
		tfFramesPMicroCal.setBackground(Color.WHITE);
		settingsCal.add(tfFramesPMicroCal);
		tfFramesPMicroCal.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                	double vartfFramesPScanCal = Double.parseDouble(tfFramesPScanCal.getText());
                	double vartfScanDepthCal = Double.parseDouble(tfScanDepthCal .getText());
                	double vartfFramesPMicroCal = Double.parseDouble(tfFramesPMicroCal.getText());
                	double vartfScanspeedCal = Double.parseDouble(tfScanspeedCal.getText());
                	
                	Double tempFramePScanCal = vartfFramesPMicroCal*vartfScanDepthCal;
                	Double tempScanspeedCal = 1000/vartfFramesPMicroCal;
                	
                	String tempFramesPScanCalString  = String.valueOf(tempFramePScanCal);
                	String tempScanspeedCalString = String.valueOf(tempScanspeedCal);
                	
                	tfFramesPScanCal.setText(tempFramesPScanCalString );
                	tfScanspeedCal.setText(tempScanspeedCalString);
                	
                } catch (Exception e1) {
                	e1.printStackTrace();
                }
            }
        });
		
		
		
		
		
		
		
		
		
		//calculate start end pos		
						double vartfScanDepthCal = Double.parseDouble(tfScanDepthCal.getText());
						double tempStartPosCal = doubleCalibrationBeadsPos + vartfScanDepthCal/2;
						double tempEndPosCal = tempStartPosCal - vartfScanDepthCal;
						
						valStartPosCalString = String.valueOf(tempStartPosCal);
						valEndPosCalString = String.valueOf(tempEndPosCal);
						
						lblNewLabel = new JLabel("select camera");
						settingsCal.add(lblNewLabel);
						
						cameraSelectionbox4 = new CameraSelectionbox(mmc, pluginUtils, false);
						settingsCal.add(cameraSelectionbox4);
						
				//abhängiges textfeld
	}
	
	

	public String getPrefix() {
		return prefix;
	}

	public String getRoot() {
		return root;
	}


	public String getChannel() {
		return channel;
	}
	

	public String getParRoot() {
		return parRoot;
	}

	

	
	public boolean isChannel2Selected() {
		channel2Selected=rdbtnChannel_2.isSelected();
		return channel2Selected;
	}

	public void setChannel2Selected(boolean channel2Selected) {
		this.channel2Selected = channel2Selected;
	}


public void setLabelsPreliminary() throws Exception{
		if (!accSettings.calOnly){
		labStorPathScan.setText(folderName.getScanPath());
		labStorPathBeadsBef.setText(folderName.getBeadsBeforePath());
		labStorPathBeadsAf.setText(folderName.getBeadsAfterPath());
		labStorPathEpi.setText(folderName.getEpiPath());
		labStorPathCal.setText(folderName.getCalPath());
		
		labStorPathScan.setForeground(Color.MAGENTA);
		labStorPathBeadsBef.setForeground(Color.MAGENTA);
		labStorPathBeadsAf.setForeground(Color.MAGENTA);
		labStorPathEpi.setForeground(Color.MAGENTA);
		labStorPathCal.setForeground(Color.MAGENTA);	
		}
		
		if (accSettings.calOnly){
			labStorPathCal.setText(folderName.getCalPath());
			labStorPathCal.setForeground(Color.MAGENTA);	
		}
	}		

public void setLabelsNull() throws Exception{
	
	
	labStorPathScan.setText(null);
	labStorPathBeadsBef.setText(null);
	labStorPathBeadsAf.setText(null);
	labStorPathEpi.setText(null);
	labStorPathCal.setText(null);	
	
	
}		

public void setLabelsFinal() throws Exception{
	
	labStorPathScan.setText(folderName.createScanPath());
	labStorPathScan.setForeground(Color.BLACK);
	
	labStorPathBeadsBef.setText(folderName.createBeadsBeforePath());
	labStorPathBeadsBef.setForeground(Color.BLACK);
	
	labStorPathBeadsAf.setText(folderName.createBeadsAfterPath());
	labStorPathBeadsAf.setForeground(Color.BLACK);
	
	labStorPathEpi.setText(folderName.createEpiPath());
	labStorPathEpi.setForeground(Color.BLACK);
	
	labStorPathCal.setText(folderName.createCalPath());
	labStorPathCal.setForeground(Color.BLACK);	
	
	
	
}	

public void setscanblack(){
	labStorPathScan.setForeground(Color.BLACK);
}

	public void setaccSettings(){
		accSettings.imageSizeS=Integer.parseInt(tfImageSize.getText());
		accSettings.expS = Double.parseDouble(tfExpScan.getText());
		accSettings.framesPScanS=Double.parseDouble(tfFramesPScanS.getText());
		accSettings.scanSpeedS=Double.parseDouble(tfScanspeedS.getText());
		accSettings.emGainS= Integer.parseInt(tfEmGainS.getText());
		accSettings.scanDepthS=Integer.parseInt(tfScanDepthS.getText());
		accSettings.noScansS=  Integer.parseInt(tfNoScansS.getText());
		accSettings.framesPMicroS=  Double.parseDouble(tfFramesPMicroS.getText());
		accSettings.expBeads=  Double.parseDouble(tfExpBeads.getText());
		accSettings.framesPScanBeads= Integer.parseInt (tfFramesPScanBeads.getText());
		accSettings.emGainBeads=  Integer.parseInt(tfEmGainBeads.getText());
		accSettings.expEpi= Double.parseDouble(tfExpEpi.getText());
		accSettings.emGainEpi=  Integer.parseInt(tfEmGainEpi.getText());
		accSettings.expCal=  Double.parseDouble(tfExpCal.getText());
		accSettings.framesPScanCal= Integer.parseInt (tfFramesPScanCal.getText());
		accSettings.scanSpeedCal=  Double.parseDouble(tfScanspeedCal.getText());
		accSettings.scanDepthCal= Integer.parseInt (tfScanDepthCal.getText());
		accSettings.emGainCal=  Integer.parseInt(tfEmGainCal.getText());
		accSettings.startPositionScan=  Double.parseDouble(valStartPosS.getText());
		accSettings.endPositionScan=  Double.parseDouble(valEndPosS.getText());
		accSettings.positionBeadsBefore=  Double.parseDouble(valStartPosBefore.getText());
		accSettings.positionBeadsAfter= Double.parseDouble (valStartAfter.getText());
		accSettings.positionEpi=  Double.parseDouble(valRecordingPositionEpi.getText());
		accSettings.startPositionCalibration=  Double.parseDouble(valStartPosCal.getText());
		accSettings.endPositionCalibration=  Double.parseDouble(valEndPosCal.getText());
		accSettings.framesPmicroCal=  Double.parseDouble(tfFramesPMicroCal.getText());
		accSettings.distTocoverslipS =Integer.parseInt(tfStartDistS.getText());
		accSettings.distanceToCoverslipEpi=Integer.parseInt(tfDistToCovEpi.getText());
	
	}
	
//	private class SwingAction extends AbstractAction {
//		public SwingAction() {
//			putValue(NAME, "SwingAction");
//			putValue(SHORT_DESCRIPTION, "Some short description");
//		}
//		public void actionPerformed(ActionEvent e) {
//		}
//	}
	

	public void refreshGuiElements(double zPos, Object source) {
		if(source != slider){
			
				sliderChangeListenerActive= false;
				slider.setValue((int)zPos);
				
				sliderChangeListenerActive= true;

		}
		valZPos.setText(String.valueOf(zPos));
	}
	public void calculateStartEndPos()	{	
		
		accSettings.scanDepthS=Double.parseDouble(tfScanDepthS .getText());;
      	accSettings.distTocoverslipS=Double.parseDouble(tfStartDistS .getText());
      	accSettings.startPositionScan= accSettings.positionBeadsBefore - accSettings.distTocoverslipS;
  		accSettings.endPositionScan=accSettings.startPositionScan - accSettings.scanDepthS;
  		
  		valStartPosS.setText(String.valueOf(accSettings.startPositionScan));
  		valEndPosS.setText(String.valueOf(accSettings.endPositionScan));
		}
public void StopRecording(boolean stop){
	MMStudio.getAcquisitionEngine().stop(true);
}
public JButton getBtnStartLive() {
	return btnStartLive;
}
public void setBtnStartLive(JButton btnStartLive) {
	this.btnStartLive = btnStartLive;
}
}


