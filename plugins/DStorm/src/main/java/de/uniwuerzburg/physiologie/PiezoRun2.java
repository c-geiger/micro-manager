package de.uniwuerzburg.physiologie;

import java.awt.Point;
//import java.awt.Label;
import java.io.IOException;

import org.micromanager.Studio;
import org.micromanager.internal.MMStudio;

import mmcorej.CMMCore;
import mmcorej.StrVector;

public class PiezoRun2 implements Runnable {

	// Piezo WAC = WAit until Condition

	private Studio app_;

	private AccessorySequenceSettings accSettings;

	// fix variables

	final int segmentLength = 20;

	// variables for GUI interaction
	private int recordedOCFraction = 10;
	private int frames;
	double scanDistance;
	private double upperStart;
	private double lowerStart = upperStart - scanDistance;

	private int scanNumber;

	// variables for methods internal calculations
	private FolderName foldername;
	private CMMCore mmc;
	private PluginEngine pluginEngine;
	private Piezo piezo;
	private DstormPluginGui gui;
	private PluginUtils pluginUtils;
	private int currentFrame;
	private Piezorun piezorun;
	private String scan;

	private String upperStartS;

	private String lowerStartS;

	public PiezoRun2(AccessorySequenceSettings accSettings, Studio app_, PluginEngine pluginEngine, DstormPluginGui gui,
			Piezo piezo, PluginUtils pluginUtils) {
		this.accSettings = accSettings;
		this.app_ = app_;
		this.pluginUtils = pluginUtils;
		this.pluginEngine = pluginEngine;
		this.gui = gui;
		this.piezo = piezo;
		mmc = app_.getCMMCore();
		this.piezorun = new Piezorun(accSettings, app_, piezo, pluginUtils, foldername);

	}

	public void run() {
		// initialize Port
		accSettings.stopRecording = false;

		try {
			mmc.loadDevice("Port", "SerialManager", "COM10");
		}

		catch (Exception ex) {
			System.out.println("load portdevice error ," + ex);

		}

		try {
			mmc.initializeDevice("Port");
		} catch (Exception e1) {
			System.out.println("initialize portdevice error ," + e1);
			e1.printStackTrace();
		}

		frames = (int) accSettings.framesPScanS;
		scanDistance = accSettings.scanDepthS;
		upperStart = accSettings.startPositionScan;
		upperStartS = String.valueOf(accSettings.startPositionScan);
		System.out.println("upperstart position" + upperStart);
		lowerStart = upperStart - scanDistance;
		lowerStartS = String.valueOf(lowerStart);
		System.out.println("lowerstart position" + lowerStart);

		int outputCycles = frames / segmentLength;
		String outputcyclesS = String.valueOf(outputCycles);
		double cycleDistance = scanDistance / outputCycles;
		String cycleDistanceS = String.valueOf(cycleDistance);
		System.out.println("upperstart position" + upperStart);
		scanNumber = accSettings.noScansS;
		;

		String segmentLengthS = "20";
		double tempPosz;

		int positionArrayLength = (((frames / (recordedOCFraction * 20)) + 1) * scanNumber * 2);
		String[][] positionarray = new String[positionArrayLength][4];
		double tempposz;
		int outputCycleIDold = 0;
		int outputCycleID = 0;
		String assPath = accSettings.metadataPath;
		String posPath = accSettings.positionarrayPath;

		try {
			mmc.setSerialPortCommand("Port", "TRI 1 1", "\n"); // set trigger
																// input state
																// triggerinputchannel
																// 1 to prametre
																// mode 1
																// =enabled

			mmc.setSerialPortCommand("Port", "CTI 1 1 0", "\n"); // Set
																	// Configuration
																	// of
																	// trigger
																	// input
																	// Triggerinputchannel
																	// 1
																	// parametre
																	// to edge
																	// triggered

			mmc.setSerialPortCommand("Port", "CTI 1 7 1", "\n"); // Set
																	// Configuration
																	// of
																	// trigger
																	// input
																	// Triggerinputchannel
																	// 1
																	// parametre
																	// polarity
																	// (1)to
																	// active
																	// high (1)

			mmc.setSerialPortCommand("Port", "CTO 1 3 4", "\n"); // Set
																	// Configuration
																	// of
																	// trigger
																	// outputchannel
																	// 2
																	// triggermode(1)
																	// to
																	// trigger
																	// line
																	// action
																	// defined
																	// with TWS
																	// (4)
																	// associates
																	// trigger
																	// output
																	// with
																	// waveform

			// initialize wavegenerator
			mmc.setSerialPortCommand("Port", "WSL 1 5", "\n"); // Set connection
																// to wave table
																// (is defined
																// in
																// Piezosoftware;beware
																// to add manual
																// programmin to
																// this skript)

			mmc.setSerialPortCommand("Port", "WGC 1 " + outputcyclesS, "\n"); // Set
																				// number
																				// of
																				// outputcycles

			mmc.setSerialPortCommand("Port", "WOS 1 " + upperStartS, "\n"); // Set
																			// startvalue
																			// for
																			// piezoscanning

			mmc.setSerialPortCommand("Port", "WTR 1 1 0 ", "\n"); // Set wave
																	// generator
																	// table
																	// rate

			mmc.setSerialPortCommand("Port", "WGO 1 0 ", "\n"); // Set wave
																// generator
																// start stop
																// mode (0) =
																// wave
																// generator
																// stopped

			mmc.setSerialPortCommand("Port", "MOV Z " + upperStartS, "\n"); // mov
																			// to
																			// upper
																			// start
																			// position

			mmc.waitForDevice("Port");
		} catch (Exception e) {
			System.out.println("problem in settings piezo");
			e.printStackTrace();
		}

		String answer6;
		try {
			mmc.setSerialPortCommand("Port", "WAV? 5 1 ", "\n");
			String answer1 = mmc.getSerialPortAnswer("Port", "\n");
			System.out.println("Waveform," + answer1);

			mmc.setSerialPortCommand("Port", "WGC? 1 ", "\n");
			String answer2 = mmc.getSerialPortAnswer("Port", "\n");
			System.out.println("Number of wave generator cycles: ," + answer2);

			mmc.setSerialPortCommand("Port", "WOS? 1 ", "\n");
			String answer3 = mmc.getSerialPortAnswer("Port", "\n");
			System.out.println("B ," + answer3);

			mmc.setSerialPortCommand("Port", "WSL? 1 ", "\n");
			String answer4 = mmc.getSerialPortAnswer("Port", "\n");
			System.out.println("C ," + answer4);

			mmc.setSerialPortCommand("Port", "WTR? 1 ", "\n");
			String answer5 = mmc.getSerialPortAnswer("Port", "\n");
			System.out.println("SD ," + answer5);

			tempPosz = piezo.retrieveZPos();
			System.out.println("Reached upper start position: ," + tempPosz);

			mmc.setSerialPortCommand("Port", "WGO 1 1 ", "\n");
			mmc.setSerialPortCommand("Port", "WGO 1 0 ", "\n");

			outputCycleIDold = piezo.retrieveOutputcycleID();
			System.out.println("completed output cycle number ," + outputCycleIDold);
		} catch (Exception e) {
			System.out.println("problem in getting piezo answers");
			e.printStackTrace();
		}
		int scannumberindex = 0;
		int arrayindex = 0;
		scanloop: for (scannumberindex = 0; scannumberindex < scanNumber; scannumberindex++) {

			try {

				// upscan

				scan = "upscan number: " + (scannumberindex + 1);
				outputCycleIDold = 0;
				mmc.setSerialPortCommand("Port", "WOS 1 " + upperStartS, "\n");
				mmc.setSerialPortCommand("Port", "MOV Z " + upperStartS, "\n");
				mmc.setSerialPortCommand("Port", "WAC ONT? Z = 1", "\n");
				Thread.sleep(50);
				tempPosz = piezo.retrieveZPos();
				gui.refreshGuiElements(tempPosz, null);
				System.out.println("reached upper starttpos");
				mmc.setSerialPortCommand("Port",
						"WAV 5 X LIN " + segmentLengthS + " -" + cycleDistanceS + " 0 " + segmentLengthS + " 0 0 ",
						"\n");
				waitTenSeconds(scan);
				mmc.setSerialPortCommand("Port", "WGO 1 1 ", "\n");
				mmc.setSerialPortCommand("Port", "WGO 1 0 ", "\n");
				System.out.println("ready for upscan");
				mmc.setSerialPortCommand("Port", "WGO 1 258", "\n"); // Set wave
																		// generator
																		// start
																		// stop
																		// mode
																		// start
																		// next
																		// cycle
																		// at
																		// position
																		// of
																		// previous
																		// cycle
				Thread.sleep(100);

				currentFrame = 1;
				positionarray[arrayindex][0] = "upscan_" + scannumberindex;
				positionarray[arrayindex][1] = String.valueOf(currentFrame);
				positionarray[arrayindex][2] = String.valueOf(piezo.retrieveZPos());
				arrayindex++;

				pluginEngine.runDstormAcquisition();

				outputCycleID = piezo.retrieveOutputcycleID();

				upscanloop: do {

					if (accSettings.stopRecording) {

						break scanloop;
					}

					if (MMStudio.AcqError && !app_.getAcquisitionManager().isAcquisitionRunning()) { // outputCycleID
																										// >
																										// outputCycles-2
																										// &&
						MMStudio.AcqError = false;
						System.out.println("Acquisition error in upscan no_" + scannumberindex);
						positionarray[arrayindex][0] = "upscan_" + scannumberindex;
						positionarray[arrayindex][1] = String.valueOf(piezo.retrieveFrameNumber());
						positionarray[arrayindex][2] = String.valueOf(piezo.retrieveZPos());
						arrayindex++;
						mmc.setSerialPortCommand("Port", "WGO 1 0 ", "\n");
						mmc.setSerialPortCommand("Port", "WGO 1 1 ", "\n");
						mmc.setSerialPortCommand("Port", "WGO 1 0 ", "\n");
						System.out.println("Piezo reset due to Acquisition error in upscan no_" + scannumberindex);
						break upscanloop;
					}
					if (app_.getAcquisitionManager().isAcquisitionRunning()) {
						outputCycleID = piezo.retrieveOutputcycleID();

						if (!(outputCycleIDold >= outputCycleID) && (outputCycleID % recordedOCFraction == 0)) {

							tempPosz = piezo.retrieveZPos();

							currentFrame = piezo.retrieveFrameNumber();

							positionarray[arrayindex][0] = "upscan_" + scannumberindex;
							positionarray[arrayindex][1] = String.valueOf(currentFrame);
							positionarray[arrayindex][2] = String.valueOf(tempPosz);
							arrayindex++;
							System.out.println("aktual OCID " + outputCycleID);
							gui.refreshGuiElements(tempPosz, null);
							outputCycleIDold = outputCycleID;
							Thread.sleep(50);
						}
					}
				} while (outputCycleID < outputCycles);
			} catch (NumberFormatException e) {
				System.out.println("problem with upscan number");
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("problem with upscan interrupts");
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("problem with upscan ex");
				e.printStackTrace();
			}

			// downscan
			try {
				outputCycleIDold = 0;
				scan = "downscan number: " + (scannumberindex + 1);
				mmc.setSerialPortCommand("Port", "WOS 1 " + lowerStartS, "\n");
				mmc.setSerialPortCommand("Port", "MOV Z " + lowerStartS, "\n");
				mmc.setSerialPortCommand("Port", "WAC ONT? Z = 1", "\n");
				Thread.sleep(50);
				tempPosz = piezo.retrieveZPos();
				gui.refreshGuiElements(tempPosz, null);
				System.out.println("reached lower starttpos");
				mmc.setSerialPortCommand("Port",
						"WAV 5 X LIN " + segmentLengthS + " " + cycleDistanceS + " 0 " + segmentLengthS + " 0 0 ",
						"\n");
				pluginUtils.waitTenSeconds(scan);
				mmc.setSerialPortCommand("Port", "WGO 1 1 ", "\n");
				mmc.setSerialPortCommand("Port", "WGO 1 0 ", "\n");
				System.out.println("ready for downscan");
				mmc.setSerialPortCommand("Port", "WGO 1 258", "\n"); // Set wave
																		// generator
																		// start
																		// stop
																		// mode
																		// start
																		// next
																		// cycle
																		// at
																		// end
																		// of
																		// old
																		// cycle

				pluginEngine.runDstormAcquisition();

				currentFrame = 1;

				positionarray[arrayindex][0] = "downscan_" + scannumberindex;
				positionarray[arrayindex][1] = String.valueOf(currentFrame);
				positionarray[arrayindex][2] = String.valueOf(piezo.retrieveZPos());
				arrayindex++;
				outputCycleID = piezo.retrieveOutputcycleID();
				downscanloop: do {

					if (accSettings.stopRecording) {
						break scanloop;
					}

					if (MMStudio.AcqError && !app_.getAcquisitionManager().isAcquisitionRunning()) {
						MMStudio.AcqError = false;
						System.out.println("Acquisition error in downscan no_" + scannumberindex);
						positionarray[arrayindex][0] = "downscan_" + scannumberindex;
						positionarray[arrayindex][1] = String.valueOf(piezo.retrieveFrameNumber());
						positionarray[arrayindex][2] = String.valueOf(piezo.retrieveZPos());
						arrayindex++;
						mmc.setSerialPortCommand("Port", "WGO 1 0 ", "\n");
						mmc.setSerialPortCommand("Port", "WGO 1 1 ", "\n");
						mmc.setSerialPortCommand("Port", "WGO 1 0 ", "\n");
						System.out.println("Piezo reset due to Acquisition error in downscan no_" + scannumberindex);
						break downscanloop;
					}

					if (app_.getAcquisitionManager().isAcquisitionRunning()) {
						outputCycleID = piezo.retrieveOutputcycleID();

						if ((outputCycleIDold < outputCycleID) && (outputCycleID % recordedOCFraction == 0)) {

							tempPosz = piezo.retrieveZPos();

							currentFrame = piezo.retrieveFrameNumber();

							positionarray[arrayindex][0] = "downscan_" + scannumberindex;
							positionarray[arrayindex][1] = String.valueOf(currentFrame);
							positionarray[arrayindex][2] = String.valueOf(tempPosz);
							arrayindex++;
							System.out.println("aktual OCID " + outputCycleID);
							gui.refreshGuiElements(tempPosz, null);
							outputCycleIDold = outputCycleID;
						}
					}
				} while (outputCycleID < outputCycles);
			} catch (NumberFormatException e) {
				System.out.println("problem with downscan number");
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("problem with downscan interrupts");
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("problem with downscan ex");
				e.printStackTrace();
			}

		}

		if (accSettings.stopRecording) {
			stopRecording();
		}

		else {
			try {
				ZPositionArrayWriter.save(positionarray, posPath);
				System.out.println("positionarray saving succesfull");
			} catch (IOException e) {
				System.out.println("writing positionarray failed");
			}

			System.out.println("super");
			try {
				mmc.setSerialPortCommand("Port", "WGO 1 0 ", "\n");
				System.out.println("piezo reset succesfull");
			} catch (Exception e) {
				System.out.println("piezo reset failed");
				e.printStackTrace();
			}
		}
	}

	private void waitTenSeconds(String string) throws InterruptedException {
		WaitDialog waitDialog = new WaitDialog(string);
		waitDialog.setLocation((int) gui.getLocationOnScreen().getX() + 600,
				(int) gui.getLocationOnScreen().getY() + 400);
		waitDialog.setVisible(true);
		int i = 0;
		for (i = 0; i < 10; i++) {
			waitDialog.setWaitTimeLabel(10 - i, string);
			Thread.sleep(1000);
		}
		waitDialog.setVisible(false);
		waitDialog.dispose();
		waitDialog = null;
	}

	private void stopRecording() {
		try {

			MMStudio.getInstance().getAcquisitionEngine().stop(true);
			while (MMStudio.getInstance().getAcquisitionEngine().isAcquisitionRunning())
				;
			piezo.stopPiezo();

			System.out.println("Acquisition interrupted by user");
			accSettings.stopRecording = false;
		} catch (Exception e) {
			System.out.println("Big problem with stop");
			e.printStackTrace();
		}
		accSettings.stopRecording = false;
	}

}