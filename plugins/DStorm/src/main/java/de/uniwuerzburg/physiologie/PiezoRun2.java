package de.uniwuerzburg.physiologie;

import java.io.IOException;

import org.micromanager.Studio;
import mmcorej.CMMCore;
import mmcorej.StrVector;

public class PiezoRun2 implements Runnable {

	// Piezo WAC = WAit until Condition

	private Studio app_;

	private AccessorySequenceSettings accSettings;

	// fix variables

	
	final int segmentLength = 20;

	// variables for GUI interaction
	private int frames;
	double scanDistance;
	private double upperStart;
	private double lowerStart= upperStart-scanDistance;
	
	private int scanNumber;

	// variables for methods inter calculations
	private FolderName foldername;
	private CMMCore mmc;
	private PluginEngine pluginEngine;
	

	public PiezoRun2(AccessorySequenceSettings accSettings, Studio app_, PluginEngine pluginEngine) {
		this.accSettings = accSettings;
		this.app_ = app_;
		this.pluginEngine=pluginEngine;
		mmc = app_.getCMMCore();

	}

	public void run() {
		// initialize Port
		accSettings.stopRecording=false;
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

		
		frames = accSettings.framesPScanS;
		scanDistance=accSettings.scanDepthS;
		
		upperStart=accSettings.startPositionScan;
		String upperStartS = String.valueOf(accSettings.startPositionScan);
		System.out.println("upperstart position" + upperStart);
		lowerStart=upperStart-scanDistance;
		String lowerStartS = String.valueOf(lowerStart);
		System.out.println("lowerstart position" + lowerStart);
		
		int outputCycles = frames / segmentLength;
		String outputcyclesS =String.valueOf(outputCycles);
		double cycleDistance = scanDistance / outputCycles;
		String cycleDistanceS = String.valueOf(cycleDistance);
		System.out.println("upperstart position" + upperStart);
		scanNumber=accSettings.noScansS;;
		
		String segmentLengthS = "20";
		String tempPosz;
		
		
		int positionArrayLength = ((frames*scanNumber) / 10);
		String[][] positionarray =new String [positionArrayLength][3];
		double tempposz;
		String outputCycleIDoldS = null;
		String outputCycleIDS = null;
		String assPath = accSettings.metadataPath;
		String posPath = accSettings.positionarrayPath;
		
		try {
			mmc.setSerialPortCommand("Port", "TRI 1 1", "\n"); // set trigger input state triggerinputchannel 1 to prametre mode 1 =enabled
																
			mmc.setSerialPortCommand("Port", "CTI 1 1 0", "\n"); // Set Configuration of trigger input Triggerinputchannel 1 parametre to edge triggered
																	
			mmc.setSerialPortCommand("Port", "CTI 1 7 1", "\n"); // Set Configuration of trigger input Triggerinputchannel 1 parametre polarity (1)to active high (1)
																	
			mmc.setSerialPortCommand("Port", "CTO 1 3 4", "\n"); // Set Configuration of trigger outputchannel 2 triggermode(1) to trigger line action defined with TWS (4) associates trigger output with waveform
																	

			// initialize wavegenerator
			mmc.setSerialPortCommand("Port", "WSL 1 5", "\n"); // Set connection to wave table (is defined in Piezosoftware;beware to add manual programmin to this skript)
																
			mmc.setSerialPortCommand("Port", "WGC 1 " + outputcyclesS, "\n"); // Set number of outputcycles
																				
			mmc.setSerialPortCommand("Port", "WOS 1 " + upperStartS, "\n"); // Set startvalue for piezoscanning
																			
			mmc.setSerialPortCommand("Port", "WTR 1 1 0 ", "\n"); // Set wave generator table rate
																	
			mmc.setSerialPortCommand("Port", "WGO 1 0 ", "\n"); // Set wave generator start stop mode (0) = wave generator stopped
																
			mmc.setSerialPortCommand("Port", "MOV Z " + upperStartS, "\n"); // mov to upper start position
																			
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

			mmc.setSerialPortCommand("Port", "POS? z", "\n");
			tempPosz = mmc.getSerialPortAnswer("Port", "\n");
			System.out.println("Reached upper start position: ," + tempPosz);

			

			mmc.setSerialPortCommand("Port", "WGN?", "\n"); // WGN get number of completed output cycles
															
			outputCycleIDoldS = mmc.getSerialPortAnswer("Port", "\n");
			System.out.println("completed output cycle number ," + outputCycleIDoldS);
		} catch (Exception e) {
			System.out.println("problem in getting piezo answers");
			e.printStackTrace();
		}
		int scannumberindex = 0;
		scanloop :
			for (scannumberindex = 0; scannumberindex < scanNumber; scannumberindex++) {
			
			try {
				
				// upscan
				mmc.setSerialPortCommand("Port", "WOS 1 " + upperStartS, "\n");
				mmc.setSerialPortCommand("Port", "MOV Z " + upperStartS, "\n");
				mmc.setSerialPortCommand("Port", "WAC ONT? Z = 1", "\n");
				mmc.setSerialPortCommand("Port",
						"WAV 5 X LIN " + segmentLengthS + " -" + cycleDistanceS + " 0 " + segmentLengthS + " 0 0 ", "\n");
				Thread.sleep(10000);
				
				mmc.setSerialPortCommand("Port", "WGO 1 1 ", "\n");	
				mmc.setSerialPortCommand("Port", "WGO 1 0 ", "\n");	
				System.out.println("ready for upscan");
				mmc.setSerialPortCommand("Port", "WGO 1 258", "\n"); // Set wave generator start stop mode start next cycle at position of previous cycle
									 
				pluginEngine.runDstormAcquisition();

				
				do {
					if(accSettings.stopRecording){
						try {
							mmc.setSerialPortCommand("Port", "HLT", "\n");
							mmc.setSerialPortCommand("Port", "WGO 1 0 ", "\n");	
							Thread.sleep(5000);
							System.out.println("accSettings.stopRecording "+accSettings.stopRecording);
						} catch (Exception e) {
							System.out.println("Big problem with stop");
							e.printStackTrace();
						}
						break scanloop;
					}
					
					//Thread.sleep(10);
					mmc.setSerialPortCommand("Port", "WGN?", "\n"); // WGN get number of outputcycles
																	
					outputCycleIDS = mmc.getSerialPortAnswer("Port", "\n");

					if (!outputCycleIDoldS.equals(outputCycleIDS)
							&& (Integer.parseInt(outputCycleIDS.substring(2)) % 5 == 0)) {
						
						mmc.setSerialPortCommand("Port", "POS? Z", "\n");
						tempPosz = mmc.getSerialPortAnswer("Port", "\n");
						mmc.setSerialPortCommand("Port", "WGI? 1", "\n");
						String wavepointS = mmc.getSerialPortAnswer("Port", "\n");
						int currentFrame = (Integer.parseInt(outputCycleIDS.substring(2))*20) +   (Integer.parseInt(wavepointS.substring(2)));
						System.out
								.println("Upscan:" + scannumberindex + "Current frame:" + wavepointS +", current position: " + tempPosz); //put data in metadata
						
						positionarray [(frames*scannumberindex)+currentFrame][0]="upscan_"+scannumberindex;
						positionarray [(frames*scannumberindex)+currentFrame][1]=String.valueOf(currentFrame);
						positionarray [(frames*scannumberindex)+currentFrame][2]=String.valueOf(tempPosz);
						
						outputCycleIDoldS = outputCycleIDS;
					}
					
				} while ((Integer.parseInt(outputCycleIDS.substring(2))) < outputCycles);
			} catch (NumberFormatException e) {
				System.out.println("problem with upscan number");
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("problem with upscan interrupts");
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("problem with downscan ex");
				e.printStackTrace();
			}
			
		// downscan
			try {
				mmc.setSerialPortCommand("Port", "WOS 1 " + lowerStartS, "\n");
				mmc.setSerialPortCommand("Port", "MOV Z " + lowerStartS, "\n");
				mmc.setSerialPortCommand("Port", "WAC ONT? Z = 1", "\n");
				mmc.setSerialPortCommand("Port",
						"WAV 5 X LIN " + segmentLengthS + " " + cycleDistanceS + " 0 " + segmentLengthS + " 0 0 ", "\n");
				Thread.sleep(10000);
				mmc.setSerialPortCommand("Port", "WGO 1 1 ", "\n");	
				mmc.setSerialPortCommand("Port", "WGO 1 0 ", "\n");	
				System.out.println("ready for downscan");
				mmc.setSerialPortCommand("Port", "WGO 1 258", "\n"); // Set wave generator start stop mode start next cycle at end of old cycle
				
				
				pluginEngine.runDstormAcquisition();
				
				
				do {
					if(accSettings.stopRecording){
						try {
							mmc.setSerialPortCommand("Port", "HLT", "\n");
							mmc.setSerialPortCommand("Port", "WGO 1 0 ", "\n");	
							Thread.sleep(5000);
							System.out.println("accSettings.stopRecording "+accSettings.stopRecording);
						} catch (Exception e) {
							System.out.println("Big problem with stop");
							e.printStackTrace();
						}
						break scanloop;
					}
					
					
					mmc.setSerialPortCommand("Port", "WGN?", "\n"); // WGN get number of completed output cycles
																	
					outputCycleIDS = mmc.getSerialPortAnswer("Port", "\n");

					if (!outputCycleIDoldS.equals(outputCycleIDS)
							&& (Integer.parseInt(outputCycleIDS.substring(2)) % 5 == 0)) {
						
						mmc.setSerialPortCommand("Port", "POS? Z", "\n");
						tempPosz= mmc.getSerialPortAnswer("Port", "\n");
						mmc.setSerialPortCommand("Port", "WGI? 1", "\n");
						String wavepointS = mmc.getSerialPortAnswer("Port", "\n");
						int currentFrame = (Integer.parseInt(outputCycleIDS.substring(2))*20) +   (Integer.parseInt(wavepointS.substring(2)));
						System.out
								.println("Upscan:" + scannumberindex + "Current frame:" + wavepointS +", current position: " + tempPosz); //put data in metadata
						
						positionarray [(frames*scannumberindex+1)+currentFrame][0]="downscan_"+scannumberindex;
						positionarray [(frames*scannumberindex+1)+currentFrame][1]=String.valueOf(currentFrame);
						positionarray [(frames*scannumberindex+1)+currentFrame][2]=String.valueOf(tempPosz);
						
						outputCycleIDoldS = outputCycleIDS;
					}
					
				} while ((Integer.parseInt(outputCycleIDS.substring(2))) < outputCycles);
			} catch (NumberFormatException e) {
				System.out.println("problem with downscan number");
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("problem with downscan interrupt");
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("problem with downscan ex");
				e.printStackTrace();
			}

		} 
		try {
			ZPositionArrayWriter.save(positionarray, posPath);
		} catch (IOException e) {
			System.out.println("writing positionarray failed");
		}
		
		
		System.out.println("super");
		
	}

	public void stop() {
	 int i =1;
	 do{
		if(accSettings.stopRecording){
			try {
				mmc.setSerialPortCommand("Port", "WGO 1 0 ", "\n");	
				System.out.println("accSettings.stopRecording "+accSettings.stopRecording);
			} catch (Exception e) {
				System.out.println("Big problem with stop");
				e.printStackTrace();
			}
			accSettings.stopRecording=false;
			System.out.println("accSettings.stopRecording "+accSettings.stopRecording);
		};
	 }while (i==1);
	}
}