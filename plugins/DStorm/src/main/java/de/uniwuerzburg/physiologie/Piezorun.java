
package de.uniwuerzburg.physiologie;

import java.io.IOException;
import java.util.regex.Pattern;

import org.micromanager.Studio;
import java.util.regex.Pattern;
import ch.qos.logback.core.boolex.Matcher;
import mmcorej.CMMCore;
import mmcorej.StrVector;

//commandTerminator = "\r"; 
//answerTerminator = "ok"; 
// Piezo WAC = WAit until Condition

public class Piezorun {
	
private Piezo piezo;
private AccessorySequenceSettings accSettings;
private Studio app_;
private PluginUtils pluginUtils;

private double upperstart;
private int frames;
private double scanDistance;
private double upperStart;
private double lowerStart;
private int outputCycles;
private int segmentLength;
private int scanNumber;
private double cycleDistance;
private FolderName folderName;
private String direction;
private int outputCycleID;
private String assPath; 
private String posPath; 

public Piezorun(AccessorySequenceSettings accSettings, Studio app_, Piezo piezo, PluginUtils pluginUtils, FolderName folderName ){
	
	this.piezo=piezo;
	this.accSettings=accSettings;
	this.app_=app_;
	this.pluginUtils=pluginUtils;
	this.folderName=folderName;
}
	
public void runScanacquisition(){
	scanNumber=accSettings.noScansS;
	piezo.InitializePiezoDevice();
	piezo.initializePiezoVariables("Scan");
	piezo.initializePiezoRun();
	int scannumberindex = 0;
	
	
	scanloop: 
		for (scannumberindex = 0; scannumberindex < scanNumber; scannumberindex++) {
		
		//upscan
		direction="upwards";
		piezo.initializePiezoScan(direction);
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				SequenceRun sequenceRun=new SequenceRun(accSettings, folderName, pluginUtils);
				sequenceRun.run();
				piezo.setSequenceRun(sequenceRun);
				
				
			}
		}).start();
		
		piezo.writePosArrayFirstFrame(direction);
		outputCycleID = piezo.retrieveOutputcycleID();
		downscanloop: do {

			if (accSettings.stopRecording) {
				break scanloop;
			}
				piezo.writePosArrayFrames(direction);
		
			} while (outputCycleID < outputCycles);
		
		//downscan
		
		direction="downwards";
		
		piezo.initializePiezoScan(direction);
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				SequenceRun sequenceRun=new SequenceRun(accSettings, folderName, pluginUtils);
				sequenceRun.run();
				piezo.setSequenceRun(sequenceRun);
				
				
			}
		}).start();
		
		piezo.writePosArrayFirstFrame(direction);
		outputCycleID = piezo.retrieveOutputcycleID();
		downscanloop: do {

			if (accSettings.stopRecording) {
				break scanloop;
			}
				piezo.writePosArrayFrames(direction);
		
			} while (outputCycleID < outputCycles);
		
	}
	if (accSettings.stopRecording) {
		stopRecording();
	}
	
	else {
		try {
			ZPositionArrayWriter.save(piezo.getPositionarray(), accSettings.positionarrayPath);
			System.out.println("positionarray saving succesfull");
		} catch (IOException e) {
			System.out.println("writing positionarray failed");
		}
		System.out.println("successfull recording");
		piezo.resetPiezo();
		accSettings.save(accSettings.metadataPath);
	}
}
	
}
	
	
	

	
	

