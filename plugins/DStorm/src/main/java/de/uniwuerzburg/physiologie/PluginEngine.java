package de.uniwuerzburg.physiologie;

import java.awt.Color;
import java.awt.Toolkit;

import org.micromanager.Studio;
import org.micromanager.acquisition.SequenceSettings;
import org.micromanager.data.Coords;
import org.micromanager.data.Image;
import org.micromanager.data.internal.multipagetiff.StorageMultipageTiff;
import org.micromanager.data.Datastore;
import org.micromanager.display.DisplayWindow;
import org.micromanager.internal.MMStudio;

import mmcorej.CMMCore;
import mmcorej.MMCoreJ;
import mmcorej.StrVector;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PluginEngine {

	private Studio app_;
	private double aktZPos;
	private SequenceSettings settings;
	private AccessorySequenceSettings accSettings;
	private FolderName folderName;
	private PiezoRun2 piezorun2;
	private CMMCore core;
	private Piezo piezo;
	private boolean enoughDiskspace;
	private DstormPluginGui gui;
	
	
	public PluginEngine(Studio app, AccessorySequenceSettings accSettings, FolderName folderName, Piezo piezo, DstormPluginGui gui) {
		this.app_ = app;
		this.accSettings = accSettings;
		this.folderName=folderName;
		this.piezo=piezo;
		this.piezorun2 = new PiezoRun2(accSettings, app, this, gui, piezo);
		settings=app_.acquisitions().getAcquisitionSettings();
		core=app_.getCMMCore();
		this.gui=gui;
	}

	public void runDstormAcquisition() {
		
		
		
		StorageMultipageTiff.setShouldGenerateMetadataFile(false);
		boolean shouldgeneratemetadatafile=StorageMultipageTiff.getShouldGenerateMetadataFile();
		System.out.println("write metadata file: "+shouldgeneratemetadatafile);
		
		
		app_.acquisitions().clearRunnables();
		app_.getAcquisitionManager().clearRunnables();
		
		try {
			folderName.createScanPath();
		} catch (Exception e1) {
			System.out.println ("filemaker error");
			e1.printStackTrace();
		}
    	
		//labStorPathScan.setForeground(Color.BLACK);
    	
    	
		/*
		 * Example of running sequence acquisitions (a.k.a. burst acquisitions).
		 */

		//String zDevice = app_.getCMMCore().getFocusDevice();
		
		//this.settings = app_.acquisitions().getAcquisitionSettings();

		settings.useCustomIntervals = false;
		settings.intervalMs = 0;
		
		MMStudio.USE_CUSTOM_PATH=true;
		MMStudio.CUSTOM_FILE_NAME = accSettings.scanFilename;
		MMStudio.CUSTOM_PATH_NAME = accSettings.scanPathname;
		System.out.println ("accsettings file "+ accSettings.scanFilename);
		System.out.println ("accsettings path "+ accSettings.scanPathname);
		
		settings.cameraTimeout = 2000 ;
		
		settings.numFrames = (int)accSettings.framesPScanS;
		System.out.println ("frames "+ settings.numFrames );
		String camera = app_.getCMMCore().getCameraDevice();
		int roi = accSettings.imageSizeS;
		int roiBorderx = 256 -(roi/2);
		int roibordery = 256 -(roi/2);
		try {
			app_.getCMMCore().setROI(camera,roiBorderx,roibordery,roi,roi);
		} catch (Exception e1) {
			System.out.println("could not set Roi");
			e1.printStackTrace();
		}
		
		
		

		try {
			
			core.setExposure(accSettings.expS);
		
			
			app_.acquisitions().runAcquisitionWithSettings(settings, false);
			
			final File metadatafile = new File(accSettings.metadataPath);
			if(!metadatafile.isFile())
			accSettings.save(accSettings.metadataPath);
			System.out.println("metadatafile stored");
		} catch (Exception e) {
			System.out.println("error in storing metadatafile");
			e.printStackTrace();

		}
		
		
	};

	
	public void runDstormEpiAcquisition() {
		//double der = accSettings.scanDepthCal;
		app_.acquisitions().clearRunnables();
		app_.getAcquisitionManager().clearRunnables();
		

		/*
		 * Example of running sequence acquisitions (a.k.a. burst acquisitions).
		 */

		//String zDevice = app_.getCMMCore().getFocusDevice();

		this.settings = app_.acquisitions().getAcquisitionSettings();
		settings.save=false;
		settings.useCustomIntervals = false;
		settings.intervalMs = 0;
		MMStudio.USE_CUSTOM_PATH=true;
		MMStudio.CUSTOM_FILE_NAME = accSettings.epiFilename;
		MMStudio.CUSTOM_PATH_NAME = accSettings.epiPathname;
		System.out.println ("accsettings file "+ accSettings.epiFilename);
		System.out.println ("accsettings path "+ accSettings.epiPathname);
		//int numFrames = 1;
		settings.numFrames = (int)accSettings.framesPScanS;
		String camera = app_.getCMMCore().getCameraDevice();
		int roi = accSettings.imageSizeS;
		int roiBorderx = 256 -(roi/2);
		int roibordery = 256 -(roi/2);;
		try {
			app_.getCMMCore().setROI(camera,roiBorderx,roibordery,roi,roi);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//enoughDiskSpace(roi, roi, settings.numFrames);
//		double dist = 1;
//		double begin = 100;
//		double tempPos = 0;
//		double movingstep = dist / numFrames;
//		double zPosStart = 100.0;
//		int roi = 400;
//		int roiBorderx = 256 -(roi/2);
//		int roibordery = 256 -(roi/2);;
		try {
//			app_.getCMMCore().setPosition(zDevice, zPosStart);
//
//			aktZPos = app_.getCMMCore().getPosition(zDevice);
//			System.out.println("boogie");

			app_.getCMMCore().setExposure(accSettings.expS);

//			Runnable runnable = new Runnable() {
//				int count = 1;
//
//				public void run() {
//					try {
//						double zPosTemp = zPosStart + (count * movingstep);
//						app_.getCMMCore().setPosition(zDevice, zPosTemp);
//						if (count % 100 == 0) {
//							
//								aktZPos = app_.getCMMCore().getPosition(zDevice);
//							
//							System.out.println(aktZPos);
//						}
//
//						++count;
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			};
//
//			app_.getAcquisitionManager().attachRunnable(-1, 0, 0, 0, runnable);

			Datastore store = app_.acquisitions().runAcquisitionWithSettings(settings, false);

			System.out.println(store);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	};	
	
	public void runDstormBeforeBeadsAcquisition() {

		app_.acquisitions().clearRunnables();
		app_.getAcquisitionManager().clearRunnables();

		/*
		 * Example of running sequence acquisitions (a.k.a. burst acquisitions).
		 */

		String zDevice = app_.getCMMCore().getFocusDevice();

		this.settings = app_.acquisitions().getAcquisitionSettings();

		settings.useCustomIntervals = false;
		settings.intervalMs = 0;
		MMStudio.USE_CUSTOM_PATH=true;
		MMStudio.CUSTOM_FILE_NAME = accSettings.beadsBeforeFilename;
		MMStudio.CUSTOM_PATH_NAME = accSettings.beadsPathname;
		System.out.println ("accsettings file "+ accSettings.beadsBeforeFilename);
		System.out.println ("accsettings path "+ accSettings.beadsPathname);
		int numFrames = 50;
		settings.numFrames = numFrames;

		double dist = 1;
		double begin = 100;
		double tempPos = 0;
		double movingstep = dist / numFrames;
		double zPosStart = 100.0;
		int roi = 400;
		int roiBorderx = 256 -(roi/2);
		int roibordery = 256 -(roi/2);;
		try {
			app_.getCMMCore().setPosition(zDevice, zPosStart);

			aktZPos = app_.getCMMCore().getPosition(zDevice);
			System.out.println("boogie");

			app_.getCMMCore().setExposure(10);

			Runnable runnable = new Runnable() {
				int count = 1;

				public void run() {
					try {
						double zPosTemp = zPosStart + (count * movingstep);
						app_.getCMMCore().setPosition(zDevice, zPosTemp);
						if (count % 100 == 0) {
							
								aktZPos = app_.getCMMCore().getPosition(zDevice);
							
							System.out.println(aktZPos);
						}

						++count;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};

			app_.getAcquisitionManager().attachRunnable(-1, 0, 0, 0, runnable);

			Datastore store = app_.acquisitions().runAcquisitionWithSettings(settings, false);

			System.out.println(store);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	
	public void runDstormAfterBeadsAcquisition() {

		app_.acquisitions().clearRunnables();
		app_.getAcquisitionManager().clearRunnables();

		/*
		 * Example of running sequence acquisitions (a.k.a. burst acquisitions).
		 */

		String zDevice = app_.getCMMCore().getFocusDevice();

		this.settings = app_.acquisitions().getAcquisitionSettings();

		settings.useCustomIntervals = false;
		settings.intervalMs = 0;
		MMStudio.USE_CUSTOM_PATH=true;
		MMStudio.CUSTOM_FILE_NAME = accSettings.beadsAfterFilename;
		MMStudio.CUSTOM_PATH_NAME = accSettings.beadsPathname;
		System.out.println ("accsettings file "+ accSettings.beadsAfterFilename);
		System.out.println ("accsettings path "+ accSettings.beadsPathname);
		int numFrames = 50;
		settings.numFrames = numFrames;

		double dist = 1;
		double begin = 100;
		double tempPos = 0;
		double movingstep = dist / numFrames;
		double zPosStart = 100.0;
		int roi = 400;
		int roiBorderx = 256 -(roi/2);
		int roibordery = 256 -(roi/2);;
		try {
			app_.getCMMCore().setPosition(zDevice, zPosStart);

			aktZPos = app_.getCMMCore().getPosition(zDevice);
			System.out.println("boogie");

			app_.getCMMCore().setExposure(10);

			Runnable runnable = new Runnable() {
				int count = 1;

				public void run() {
					try {
						double zPosTemp = zPosStart + (count * movingstep);
						app_.getCMMCore().setPosition(zDevice, zPosTemp);
						if (count % 100 == 0) {
							
								aktZPos = app_.getCMMCore().getPosition(zDevice);
							
							System.out.println(aktZPos);
						}

						++count;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};

			app_.getAcquisitionManager().attachRunnable(-1, 0, 0, 0, runnable);

			Datastore store = app_.acquisitions().runAcquisitionWithSettings(settings, false);

			System.out.println(store);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
	
	public void runDstormCalAcquisition() {

		app_.acquisitions().clearRunnables();
		app_.getAcquisitionManager().clearRunnables();
		String camera = app_.getCMMCore().getCameraDevice();
		/*
		 * Example of running sequence acquisitions (a.k.a. burst acquisitions).
		 */

		String zDevice = app_.getCMMCore().getFocusDevice();

		this.settings = app_.acquisitions().getAcquisitionSettings();

		settings.useCustomIntervals = false;
		settings.intervalMs = 0;
		MMStudio.USE_CUSTOM_PATH=true;
		MMStudio.CUSTOM_FILE_NAME = accSettings.calFilename;
		MMStudio.CUSTOM_PATH_NAME = accSettings.calPathname;
		System.out.println ("accsettings file "+ accSettings.calFilename);
		System.out.println ("accsettings path "+ accSettings.calPathname);
		int numFrames = 100;
		settings.numFrames = numFrames;

		double dist = 1;
		double begin = 100;
		double tempPos = 0;
		double movingstep = dist / numFrames;
		double zPosStart = 100.0;
		int roi = 400;
		int roiBorderx = 256 -(roi/2);
		int roibordery = 256 -(roi/2);;
		
		
		try {
			
			app_.getCMMCore().setPosition(zDevice, zPosStart);

			aktZPos = app_.getCMMCore().getPosition(zDevice);
			System.out.println("boogie");

			app_.getCMMCore().setExposure(10);

			Runnable runnable = new Runnable() {
				int count = 1;

				public void run() {
					try {
						double zPosTemp = zPosStart + (count * movingstep);
						app_.getCMMCore().setPosition(zDevice, zPosTemp);
						if (count % 100 == 0) {
							
								aktZPos = app_.getCMMCore().getPosition(zDevice);
							
							System.out.println(aktZPos);
						}

						++count;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};

			app_.getAcquisitionManager().attachRunnable(-1, 0, 0, 0, runnable);

			Datastore store = app_.acquisitions().runAcquisitionWithSettings(settings, false);

			System.out.println(store);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	};



public void getStandardCamProps(){
try {
	String camera = app_.getCMMCore().getCameraDevice();
	StrVector camProps= app_.getCMMCore().getDevicePropertyNames(camera);
	String[] arrayCamProps = camProps.toArray();
	
	accSettings.camTrigger43 = app_.getCMMCore().getProperty(camera,arrayCamProps[43]);
	accSettings.camCDTemperatureSetPoint8 = app_.getCMMCore().getProperty(camera,arrayCamProps[8]);
	accSettings.camFanMode16 = app_.getCMMCore().getProperty(camera,arrayCamProps[16]);
	accSettings.camReadMode27 = app_.getCMMCore().getProperty(camera,arrayCamProps[27]);
	accSettings.camFrameTransfer17 = app_.getCMMCore().getProperty(camera,arrayCamProps[17]);
	accSettings.camIsolatedCropMode20 = app_.getCMMCore().getProperty(camera,arrayCamProps[20]);
	accSettings.camVerticalSpeed45 = app_.getCMMCore().getProperty(camera,arrayCamProps[45]);
	accSettings.camVerticalClockVoltage44 = app_.getCMMCore().getProperty(camera,arrayCamProps[44]);
	accSettings.camReadoutrate28 = app_.getCMMCore().getProperty(camera,arrayCamProps[28]);
	accSettings.camPreAmpGain26 = app_.getCMMCore().getProperty(camera,arrayCamProps[26]);
	accSettings.camOutput_Amplifier24= app_.getCMMCore().getProperty(camera,arrayCamProps[24]);
	accSettings.camEMSwitch14 = app_.getCMMCore().getProperty(camera,arrayCamProps[14]);
	accSettings.camEMgain19 = app_.getCMMCore().getProperty(camera,arrayCamProps[19]);
	accSettings.camShutter_Internal32 = app_.getCMMCore().getProperty(camera,arrayCamProps[32]);
	
	
} catch (Exception e) {
	System.out.println("camera settings Failed");
	// TODO Auto-generated catch block
	e.printStackTrace();
}
}


public void setStandardCamProps(){
try {
	String camera = app_.getCMMCore().getCameraDevice();
	StrVector camProps= app_.getCMMCore().getDevicePropertyNames(camera);
	String[] arrayCamProps = camProps.toArray();

	String trigger = arrayCamProps [43];	
	app_.getCMMCore().setProperty(camera, trigger, "Internal");
	accSettings.camTrigger43 = app_.getCMMCore().getProperty(camera,arrayCamProps[43]);
	
	String CCDTemperatureSetPoint = arrayCamProps [8];
	app_.getCMMCore().setProperty(camera, CCDTemperatureSetPoint, "-80");
	accSettings.camCDTemperatureSetPoint8 = app_.getCMMCore().getProperty(camera,arrayCamProps[8]);
	
	String FanMode = arrayCamProps [16];
	app_.getCMMCore().setProperty(camera, FanMode, "Full");
	accSettings.camFanMode16 = app_.getCMMCore().getProperty(camera,arrayCamProps[16]);
	
	String ReadMode = arrayCamProps [27];
	app_.getCMMCore().setProperty(camera, ReadMode, "Image");
	accSettings.camReadMode27 = app_.getCMMCore().getProperty(camera,arrayCamProps[27]);
	
	String FrameTransfer = arrayCamProps [17];
	app_.getCMMCore().setProperty(camera, FrameTransfer, "On");
	accSettings.camFrameTransfer17 = app_.getCMMCore().getProperty(camera,arrayCamProps[17]);
	
	String IsolatedCropMode = arrayCamProps [20];
	app_.getCMMCore().setProperty(camera, IsolatedCropMode, "Off");
	accSettings.camIsolatedCropMode20 = app_.getCMMCore().getProperty(camera,arrayCamProps[20]);
	
	String VerticalSpeed = arrayCamProps [45];
	app_.getCMMCore().setProperty(camera, VerticalSpeed, "3.30");
	accSettings.camVerticalSpeed45 = app_.getCMMCore().getProperty(camera,arrayCamProps[45]);
	
	String VerticalClockVoltage = arrayCamProps [44];
	app_.getCMMCore().setProperty(camera, VerticalClockVoltage, "Normal");
	accSettings.camVerticalClockVoltage44 = app_.getCMMCore().getProperty(camera,arrayCamProps[44]);
	
	String Readoutrate = arrayCamProps [28];
	app_.getCMMCore().setProperty(camera, Readoutrate, "17.000 MHz");
	accSettings.camReadoutrate28 = app_.getCMMCore().getProperty(camera,arrayCamProps[28]);
	
	String PreAmpGain = arrayCamProps [26];
	app_.getCMMCore().setProperty(camera, PreAmpGain, "Gain 1");
	accSettings.camPreAmpGain26 = app_.getCMMCore().getProperty(camera,arrayCamProps[26]);
	
	String Output_Amplifier = arrayCamProps [24];
	app_.getCMMCore().setProperty(camera, Output_Amplifier, "Electron Multiplying");
	accSettings.camOutput_Amplifier24= app_.getCMMCore().getProperty(camera,arrayCamProps[24]);
	
	String EMSwitch = arrayCamProps [14];
	app_.getCMMCore().setProperty(camera, EMSwitch, "On");
	accSettings.camEMSwitch14 = app_.getCMMCore().getProperty(camera,arrayCamProps[14]);
	
	String EMgain = arrayCamProps [19];
	app_.getCMMCore().setProperty(camera, EMgain, "200");
	accSettings.camEMgain19 = app_.getCMMCore().getProperty(camera,arrayCamProps[19]);
	
	String Shutter_Internal = arrayCamProps [32];
	app_.getCMMCore().setProperty(camera, Shutter_Internal, "Open");
	accSettings.camShutter_Internal32 = app_.getCMMCore().getProperty(camera,arrayCamProps[32]);
	
} catch (Exception e) {
	System.out.println("camera settings Failed");
	// TODO Auto-generated catch block
	e.printStackTrace();
}
}


public void piezorun2() {
	new Thread(piezorun2).start();
	
}
public boolean enoughDiskSpace(){
	int numFrames=0;
	if (accSettings.recordingParadigm.equals("Scan")){
		numFrames= (int)accSettings.framesPScanS;
	}
	if (accSettings.recordingParadigm.equals("Beads")){
		numFrames= (int)accSettings.framesPScanBeads;
	}
	if (accSettings.recordingParadigm.equals("Cal")){
		numFrames= (int)accSettings.framesPScanCal;
	}
	int roiX = accSettings.imageSizeS;
	int roiY= accSettings.imageSizeS;
	
	File root = new File(accSettings.root);
	
	// Need to find a file that exists to check space
	while (!root.exists()) {
		root = root.getParentFile();
		if (root == null) {
			return false;
		}
	}
	
	long usableMB = root.getUsableSpace() / (1024 * 1024);
	long totalMB = roiX * roiY * app_.core().getBytesPerPixel() * numFrames / 1048576L;
	
	return (1.25 * totalMB) < usableMB;
}

//	private boolean enoughDiskSpace(int roiX, int roiY, int numFrames) {
//		
//		
//		File root = new File(accSettings.root);
//		
//		// Need to find a file that exists to check space
//		while (!root.exists()) {
//			root = root.getParentFile();
//			if (root == null) {
//				return false;
//			}
//		}
//		
//		long usableMB = root.getUsableSpace() / (1024 * 1024);
//		long totalMB = roiX * roiY * app_.core().getBytesPerPixel() * numFrames / 1048576L;
//		
//		return (1.25 * totalMB) < usableMB;
//	}
	
	public void errorDialog(String message){
		final Runnable runnable =
			     (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
			if (runnable != null) runnable.run();
		
			JOptionPane.showMessageDialog(gui, message , "Dialog",JOptionPane.ERROR_MESSAGE);
	}
}

//StorageMultipageTiff.setShouldGenerateMetadataFile(false);


