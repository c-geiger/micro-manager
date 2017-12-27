package de.uniwuerzburg.physiologie;

import org.micromanager.Studio;
import org.micromanager.internal.MMStudio;

import mmcorej.CMMCore;

public class Piezo {

	private CMMCore mmc;
	
	private PluginEngine pluginEngine;
	private PluginUtils pluginUtils;
	private DstormPluginGui gui;
	private AccessorySequenceSettings accSettings;
	private Studio app_;
	private SequenceRun sequenceRun;
	// fix variables

		public void setSequenceRun(SequenceRun sequenceRun) {
		this.sequenceRun = sequenceRun;
	}

		final int segmentLength = 20;
		private String segmentLengthS = String.valueOf(segmentLength);

		// variables for GUI interaction
		private int recordedOCFraction = 2;
		private int frames;
		double scanDistance;
		private double upperStart;
		
		private double lowerStart;
		private double aktPosZ;
		public double getAktPosZ() {
			return aktPosZ;
		}

		private int scanNumber;
		private int outputCycles;
		private String outputCyclesS;
		private double cycleDistance;
		private int wavetableID = 5;
		
		private String wavetableIDS=String.valueOf(wavetableID);
		private String cycleDistanceS;
		private int wavetablerate = 1;
		private String wavetablerateS =String.valueOf(wavetablerate);
		private int wavepointfraction = 5;
		private String wavepointfractionS = String.valueOf(wavepointfraction);
		private int wavepoint;
		private String wavepointS;
		private double tempPosz;
		private int outputcycleIDOld;
		private int outputCycleID;
		public int getOutputCycleID() {
			return outputCycleID;
		}

		private int scannumberindex;
		public void setScannumberindex(int scannumberindex) {
			this.scannumberindex = scannumberindex+1;
		}
		private String com;
		private int currentPiezoFrame;
		private int arrayindex;
		private String loopnametobreak;
		private String upperStartS;
		private String lowerStartS;
		private boolean noSettings;
		private String[][] positionarray ;
		public String[][] getPositionarray() {
			return positionarray;
		}

		private int positionArrayLength;
		private String camera;
		
		
		public Piezo(AccessorySequenceSettings accSettings, Studio app_, DstormPluginGui gui,PluginUtils pluginUtils, String com){
		this.mmc =MMStudio.getInstance().getCMMCore();
		this.pluginUtils=pluginUtils;
		this.gui=gui;
		this.accSettings=accSettings;
		this.app_=app_;
		this.camera = mmc.getCameraDevice();
		this.com=com;
//		try {
//			mmc.loadDevice("Port", "SerialManager", com);
//			
//		} catch (Exception ex) {
//			System.out.println("load portdevice error ," + ex);
//		}
//
//		try {
//			mmc.initializeDevice("Port");
//		} catch (Exception e1) {
//			System.out.println("initialize portdevice error ," + e1);
//			e1.printStackTrace();
//		}
		
		InitializePiezoDevice(com);
		
		
		try {
			mmc.setSerialPortCommand("Port", "WGO 1 0 " , "\n");
		} catch (Exception e) {
			System.out.println("did not stop wavegenerator");
			e.printStackTrace();
		}
		
	}
	
	public void setZPosOnly(double zPos){
		String tempPoszS = String.valueOf(zPos);
		try {
			mmc.setSerialPortCommand("Port", "MOV Z " + tempPoszS, "\n");
			Thread.sleep(50);
		} catch (InterruptedException e) {
			pluginUtils.errorDialog("error in setting zPosOnly");
			e.printStackTrace();
		} catch (Exception e) {
			pluginUtils.errorDialog("error in setting zPosOnly");
			e.printStackTrace();
		}	
	}
	
	public double setZPos(double zPos) {
		String tempPoszS = String.valueOf(zPos);
		try {
			mmc.setSerialPortCommand("Port", "MOV Z " + tempPoszS, "\n");
			Thread.sleep(50);
			mmc.setSerialPortCommand("Port", "POS? z", "\n");
			tempPoszS = mmc.getSerialPortAnswer("Port", "\n");
		} catch (Exception e1) {
			System.out.println("cant set zPosition.\n");
			e1.printStackTrace();
		}

		double tempPosz = round(Double.parseDouble(tempPoszS.substring(2)));
		return tempPosz;
	}

	public double retrieveZPos(){
		double tempPosz = 0;
		
			try {
				mmc.setSerialPortCommand("Port", "POS? z", "\n");
				String tempPoszS = mmc.getSerialPortAnswer("Port", "\n");
				tempPosz = round(Double.parseDouble(tempPoszS.substring(2)));
			} catch (NumberFormatException e) {
				System.out.println("NFE-retrieve zPos again");
				clearPiezoAnswerBuffer();
				retrieveZPos();
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("can't obtain zPosition");
				e.printStackTrace();
			}
		
		return tempPosz;

	}
	
	public double round(double number){
//		double temprounded = number*10.0;
//		temprounded = Math.round(temprounded);
//		double	rounded = temprounded/10.0;
		return number;//rounded;
	}
	
	public int retrieveOutputcycleID(){
		int outputCycleID=0;
		
			try {
				mmc.setSerialPortCommand("Port", "WGN? 1", "\n"); // WGN get number of completed output cycles
				String answer = mmc.getSerialPortAnswer("Port", "\n");
				outputCycleID = Integer.parseInt((answer).substring(2));
			} catch (NumberFormatException e) {
				System.out.println("NFE-retrieve OCID again");
				clearPiezoAnswerBuffer();
				retrieveOutputcycleID();
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("problem getting OCID");
				e.printStackTrace();
			}
		
		
		return outputCycleID;
	}
	
	public int retrieveFrameNumber(){
		int wavepoint=0;
		int currentFrame;
		int outputCycleID=retrieveOutputcycleID();
		
			try {
				mmc.setSerialPortCommand("Port", "WGI? 1", "\n");
				wavepoint = Integer.parseInt((mmc.getSerialPortAnswer("Port", "\n")).substring(2));
			} catch (NumberFormatException e) {
				System.out.println("NFE-retrieve Framenumber again");
				clearPiezoAnswerBuffer();
				retrieveFrameNumber();
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("problem getting Framenumber");
				e.printStackTrace();
			}
		
		
		if (wavepoint<20){
			currentFrame = (outputCycleID*20) + wavepoint  ;
		}
		else{
			currentFrame = (outputCycleID*20);
		}
		return currentFrame;
	}

	// set Piezo Trigger state; true= trigger enabled
		public void setPiezoTriggerInputState (boolean trigger){
			
			try {
				if (trigger){mmc.setSerialPortCommand("Port", "TRI 1 1" , "\n"); }
				else{
					mmc.setSerialPortCommand("Port", "TRI 1 0" , "\n");
					}
			} catch (Exception e) {
				pluginUtils.errorDialog("error in setting Piezo Trigger mode");
				e.printStackTrace();
			}
			}
		
		// get Piezo Trigger state; true= trigger enabled
		public boolean getPiezoTriggerInputState (){ 
			boolean triggerInputState=true;
		try {
			mmc.setSerialPortCommand("Port", "TRI? 1" , "\n");
			if (Integer.parseInt(mmc.getSerialPortAnswer("Port","\n").substring(2))==1 ){
			System.out.println("Trigger Mode enabled");
			triggerInputState=true;
			}
			else{ 
				System.out.println("Trigger Mode enabled");
				triggerInputState=false;
			}
		} catch (NumberFormatException e) {
			pluginUtils.errorDialog("error in getting Piezo Trigger mode");
			e.printStackTrace();
		} catch (Exception e) {
			pluginUtils.errorDialog("error in getting Piezo Trigger mode");
			e.printStackTrace();
		}
		return triggerInputState;
		}

		//Set Configuration of trigger input Type Triggerinputchannel 1 parametre to : String "edge"  or "level" triggered	
		public void setPiezoConfigurationOfTriggerInputType (String string){
			
			try {
				if (string.equals("edge")){mmc.setSerialPortCommand("Port", "CTI 1 1 0", "\n"); }
				else{
					mmc.setSerialPortCommand("Port", "CTI 1 1 1", "\n");
					}
			} catch (Exception e) {
				pluginUtils.errorDialog("error in setting Piezo Trigger Type");
				e.printStackTrace();
			}
			}
		
		//Get Configuration of trigger input Type Triggerinputchannel 1 :  "edge"  or "level" triggered	
		public String getPiezoConfigurationOfTriggerInputType (){ 
		String triggerInputType =null;
		try {
			mmc.setSerialPortCommand("Port", "CTI? 1 1" , "\n");
			if (Integer.parseInt(mmc.getSerialPortAnswer("Port","\n").substring(4))==0 ){
				System.out.println("Trigger Type: edge triggered");
				triggerInputType="edge";
			}
			else{ 
			System.out.println("Trigger Type: edge triggered");
			triggerInputType="level";
	}
		} catch (NumberFormatException e) {
			pluginUtils.errorDialog("error in getting Piezo Trigger Type");
			e.printStackTrace();
		} catch (Exception e) {
			pluginUtils.errorDialog("error in getting Piezo Trigger Type");
			e.printStackTrace();
		}
	return triggerInputType;
	}



	//Set Configuration of trigger input Polarity Triggerinputchannel 1 parametre to :  "high" =active high  or "low" =active low 	
	public void setPiezoConfigurationOfTriggerInputPolarity (String string){
		
		try {
			if (string.equals("high")){mmc.setSerialPortCommand("Port", "CTI 1 7 1" , "\n"); }
			else{
				mmc.setSerialPortCommand("Port", "CTI 1 7 0" , "\n");
				}
		} catch (Exception e) {
			pluginUtils.errorDialog("error in setting Piezo Trigger Polarity");
			e.printStackTrace();
		}
		}

	//Get Configuration of trigger input Polarity Triggerinputchannel 1 :  "activehigh"  or "activelow"
	public String getPiezoConfigurationOfTriggerInputPolarity (){ 
		String triggerPolarity="active high";
		try {
			mmc.setSerialPortCommand("Port", "CTI? 1 7" , "\n");
			if (Integer.parseInt(mmc.getSerialPortAnswer("Port","\n").substring(4))==0 ){
			System.out.println("Trigger Polarity: active low");
			triggerPolarity="active low";
			}
			else{ 
				System.out.println("Trigger Polarity: active high");;
				triggerPolarity="active high";
			}
		} catch (NumberFormatException e) {
			pluginUtils.errorDialog("error in getting Piezo Trigger Polarity");
			e.printStackTrace();
		} catch (Exception e) {
			pluginUtils.errorDialog("error in getting Piezo Trigger Polarity");
			e.printStackTrace();
		}
		return triggerPolarity;
		}

	// set Piezo rectangular positiv TriggerOutput to a fraction of 1/wavepointfraction
	public void setPiezoTriggerOutput(int wavepointfraction, int segmentLength)  {
		
		try {
			mmc.setSerialPortCommand("Port", "TWC" , "\n");
			mmc.setSerialPortCommand("Port", "CTO 1 3 4" , "\n");
			mmc.setSerialPortCommand("Port", "CTO 1 7 1" , "\n");
			//mmc.setSerialPortCommand("Port", "WAV? 5 1 ", "\n");
			//int wavePoints=Integer.parseInt(mmc.getSerialPortAnswer("Port","\n").substring(4));
			//System.out.println( "Number of wavepoints: ," + wavePoints);
			for (int i=0; i < segmentLength; i++){
			wavepoint=i+1;
			String.valueOf(wavepoint);
			if (i%wavepointfraction==0){
			mmc.setSerialPortCommand("Port", "TWS 1 "+String.valueOf(wavepoint)+" 1" , "\n");
			}
			else{
			mmc.setSerialPortCommand("Port", "TWS 1 "+String.valueOf(wavepoint)+" 0" , "\n");
			}
			}
		} catch (NumberFormatException e) {
			pluginUtils.errorDialog("error in setting Piezo output trigger");
			e.printStackTrace();
		} catch (Exception e) {
			pluginUtils.errorDialog("error in setting Piezo output trigger");
			e.printStackTrace();
		}
		}

	//set number of Piezooutputcycles
	public void setPiezoOutputcycles (int outputCycles){
		try {
			mmc.setSerialPortCommand("Port", "WGC 1 " + String.valueOf(outputCycles) , "\n");
		} catch (Exception e) {
			pluginUtils.errorDialog("error in setting Piezo outPutCycles");
			e.printStackTrace();
		}	
		}

	//get number of Piezooutputcycles
	public int getPiezoOutputcycles (){ 
		int outputCycles=0;
		try {
			mmc.setSerialPortCommand("Port", "WGC? 1 " , "\n");
			outputCycles = Integer.parseInt(mmc.getSerialPortAnswer("Port","\n").substring(2));
		} catch (NumberFormatException e) {
			pluginUtils.errorDialog("error in getting Piezo outPutCycles");
			e.printStackTrace();
		} catch (Exception e) {
			pluginUtils.errorDialog("error in getting Piezo outPutCycles");
			e.printStackTrace();
		}
		return outputCycles;
		}

	//set WavetableID
	public void setWavegeneratorTableID (int wavetableID){
		try {
			mmc.setSerialPortCommand("Port", "WSL 1 " + String.valueOf(wavetableID) , "\n");
		} catch (Exception e) {
			pluginUtils.errorDialog("error in setting WavegeneratorTableID ");
			e.printStackTrace();
		}	
		}

	//get WavetableID	
	public int getWavegeneratorTableID (){ 
		int wavetableID=0;
		try {
			mmc.setSerialPortCommand("Port", "WSL? 1 ", "\n");
			wavetableID = Integer.parseInt(mmc.getSerialPortAnswer("Port","\n").substring(2));
		} catch (NumberFormatException e) {
			pluginUtils.errorDialog("error in getting WavegeneratorTableID ");
			e.printStackTrace();
		} catch (Exception e) {
			pluginUtils.errorDialog("error in getting WavegeneratorTableID ");
			e.printStackTrace();
		}
		return wavetableID;
		}

	//Set Wavegenerator start position
	public void setWavegeneratorStartposition (double startPos){
		try {
			mmc.setSerialPortCommand("Port", "WOS 1 " + String.valueOf(startPos) , "\n");
		} catch (Exception e) {
			pluginUtils.errorDialog("error in setting startposition ");
			e.printStackTrace();
		}	
		}

	//get wavegenerator start position
	public double getWavegeneratorStartposition (){ 
		double startPos=100.0;
		try {
			mmc.setSerialPortCommand("Port", "WOS? 1 ", "\n");
			startPos = Double.parseDouble(mmc.getSerialPortAnswer("Port","\n").substring(2));
		} catch (NumberFormatException e) {
			pluginUtils.errorDialog("error in getting startposition ");
			e.printStackTrace();
		} catch (Exception e) {
			pluginUtils.errorDialog("error in getting startposition ");
			e.printStackTrace();
		}
		return startPos;
		}

	//Set Wavegenerator table rate
	public void setWavegeneratorTableRate (int wavetablerate){
		try {
			mmc.setSerialPortCommand("Port", "WTR 1 " + String.valueOf(wavetablerate) + " 0", "\n");
		} catch (Exception e) {
			pluginUtils.errorDialog("error in setting WavegeneratorTableRate ");
			e.printStackTrace();
		}	
		}

	//get wavegenerator tableRate
	public int getWavegeneratorTableRate (){ 
		int wavetablerate=0;
		try {
			mmc.setSerialPortCommand("Port", "WTR? 1 ", "\n");
			String answer=mmc.getSerialPortAnswer("Port","\n");
			wavetablerate = Integer.parseInt(answer.substring(2,(answer.length()-2)));
		} catch (NumberFormatException e) {
			pluginUtils.errorDialog("error in setting WavegeneratorTableRate ");
			e.printStackTrace();
		} catch (Exception e) {
			pluginUtils.errorDialog("error in setting WavegeneratorTableRate ");
			e.printStackTrace();
		}
		return wavetablerate;
		}

	//Set Wavegenerator Start/StopMode : 
	//0=Stop wavegenerator
	//1=start immediately
	//2=Start triggered
	//256+1=257 start at endpoint of last Outputcycle immediately
	//256 + 2 start at endpoint of last outputcycle triggered
	public void setWavegeneratorStartStopMode (int startStopMode){
		try {
			mmc.setSerialPortCommand("Port", "WGO 1 " + String.valueOf(startStopMode), "\n");
		} catch (Exception e) {
			pluginUtils.errorDialog("error in setting WavegeneratorStartStopMode ");
			e.printStackTrace();
		}	
		}

	// piezoWait until Piezo reaches position
	public void waitForArriving(){
		try {
			mmc.setSerialPortCommand("Port", "WAC ONT? Z = 1", "\n");
		} catch (Exception e) {
			pluginUtils.errorDialog("error in arriving at position");
			e.printStackTrace();
		}	
	}
	 // reset piezo
	public void resetPiezo(){
		try {
			mmc.setSerialPortCommand("Port", "WGO 1 0 ", "\n");
			mmc.setSerialPortCommand("Port", "WGO 1 1 ", "\n");	
			mmc.setSerialPortCommand("Port", "WGO 1 0 ", "\n");	
			
			Thread.sleep(100);
		} catch (InterruptedException e) {
			pluginUtils.errorDialog("error in resetting Piezo");
			e.printStackTrace();
		} catch (Exception e) {
			pluginUtils.errorDialog("error in resetting Piezo");
			e.printStackTrace();
		}
		
		//clearPiezoAnswerBuffer();
		
		
		
	}
	
	public void clearPiezoAnswerBuffer(){
		try {
			mmc.getSerialPortAnswer("Port","\n");
//			for (int v=0;v<6;v++){
//			mmc.getSerialPortAnswer("Port","\n");
//			Thread.sleep(100);
//			}
		} catch (Exception e) {
			System.out.println("piezo reset provoked error");
			e.printStackTrace();
		}
	}

	public void stopPiezo(){
		try {
			mmc.setSerialPortCommand("Port", "HLT", "\n");
			Thread.sleep(200);
			resetPiezo();
			System.out.println("Piezo stopped");
		} catch (InterruptedException e) {
			pluginUtils.errorDialog("error in stopping Piezo");
			e.printStackTrace();
		} catch (Exception e) {
			pluginUtils.errorDialog("error in stopping Piezo");
			e.printStackTrace();
		}
	}

	//Set Wavetable direction upwards/downwards
	public void setPiezoWavetableParametres(String direction, int wavetableID , int segmentLength, double cycleDistance){ 
		try {
			if (direction.equals("upwards")){
				mmc.setSerialPortCommand("Port","WAV "+ String.valueOf(wavetableID) + " X LIN " + String.valueOf(segmentLength) + " -" + String.valueOf(cycleDistance) + " 0 " + String.valueOf(segmentLength) + " 0 0 ", "\n");}
			if (direction.equals("downwards")){
				mmc.setSerialPortCommand("Port","WAV "+ String.valueOf(wavetableID) + " X LIN " + String.valueOf(segmentLength) + " " + String.valueOf(cycleDistance) + " 0 " + String.valueOf(segmentLength) + " 0 0 ", "\n");}
			if (direction.equals("calibration")){
				mmc.setSerialPortCommand("Port","WAV "+ String.valueOf(wavetableID) + " X LIN " + String.valueOf(segmentLength) + " -" + String.valueOf(cycleDistance) + " 0 " + String.valueOf(segmentLength) + " 0 0 ", "\n");}
		
		} catch (Exception e) {
			pluginUtils.errorDialog("error in setting wavetable for " + direction +"-scan");
			e.printStackTrace();
		}
		
	}
	
	
	
	public void waitForDevice(String port){
		try {
			mmc.waitForDevice(port);
		} catch (Exception e) {
			pluginUtils.errorDialog("error in setting waiting for device ");
			e.printStackTrace();
		}
	}
	public void sleep(int ms){
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			pluginUtils.errorDialog("error in sleeping ");
			e.printStackTrace();
		}
	}
	
	
//prepare Piezo for Updownscanning
public void initializePiezoRun(){
	resetPiezo();
	setPiezoTriggerInputState(true);
	setPiezoConfigurationOfTriggerInputType("edge");
	setPiezoConfigurationOfTriggerInputPolarity("high");
	setWavegeneratorTableID(wavetableID);
	setPiezoOutputcycles(outputCycles);
	setWavegeneratorTableRate(wavetablerate);
	waitForDevice("Port");
	setPiezoTriggerOutput(5,segmentLength);
	outputCycleID=retrieveOutputcycleID();
	outputcycleIDOld=outputCycleID;
	System.out.println("completed output cycle number ," + outputcycleIDOld);
}
//direction "upwards"/"downwards"
public int initializePiezoScan(String direction){
	double startPos=100.0;
	if (direction.equals("upwards")){startPos=upperStart;}
	if (direction.equals("downwards")){startPos=lowerStart;}
	if (direction.equals("calibration")){startPos=accSettings.startPositionCalibration;}
	
	gui.setLabScannumber(direction + ": " + scannumberindex);
	setWavegeneratorStartposition(startPos);
	setZPosOnly(startPos);
	waitForArriving();
	sleep(50);
	tempPosz =retrieveZPos();
	pluginUtils.wait(50);
	aktPosZ=tempPosz;
	gui.refreshGuiElements(tempPosz, null);
	System.out.println("reached " + direction + " scan start position");
	setPiezoWavetableParametres(direction, wavetableID, segmentLength, cycleDistance);
	try {
		pluginUtils.waitTenSeconds(direction +": " + scannumberindex);
	} catch (InterruptedException e) {
		pluginUtils.errorDialog("error in waitdialog ");
		e.printStackTrace();
	}
	resetPiezo();
	System.out.println("ready for "+ direction + "-scan");
	outputCycleID = retrieveOutputcycleID();
	setWavegeneratorStartStopMode(258);
	return outputCycleID;
	//pluginUtils.wait(200);
	
}
// write first frame of position array; direction "upwards"/"downwards"
public void writePosArrayFirstFrame(String direction){
	positionarray[arrayindex][0] = direction + "scan" + scannumberindex;
	positionarray[arrayindex][1] = String.valueOf(1);
	positionarray[arrayindex][2] = String.valueOf(0);
	positionarray[arrayindex][3] = String.valueOf(retrieveZPos());
	arrayindex++;
}
//write other frames of position array; direction "upwards"/"downwards"
public void writePosArrayFrames(String direction, int outputCycleID ){
		
		System.out.println("ID "+outputCycleID);
			tempPosz = retrieveZPos();
			positionarray[arrayindex][0] = direction + "scan" + scannumberindex;
			positionarray[arrayindex][1] = String.valueOf(retrieveFrameNumber());
			positionarray[arrayindex][2] = String.valueOf(sequenceRun.getCurFrame());
			positionarray[arrayindex][3] = String.valueOf(tempPosz);
			arrayindex++;
			gui.refreshGuiElements(tempPosz, null);
			
		}
	

		
	
public void InitializePiezoDevice(String com){
	try {
			mmc.loadDevice("Port", "SerialManager", com);
		}catch (Exception ex) {
			System.out.println("load portdevice error ," + ex);
		}

	try {
			mmc.initializeDevice("Port");
		} catch (Exception e1) {
			System.out.println("initialize portdevice error ," + e1);
			e1.printStackTrace();
		}
}

public void InitializePiezoDevice(){
	try {
			mmc.loadDevice("Port", "SerialManager", com);
		}catch (Exception ex) {
			System.out.println("load portdevice error ," + ex);
		}

	try {
			mmc.initializeDevice("Port");
		} catch (Exception e1) {
			System.out.println("initialize portdevice error ," + e1);
			e1.printStackTrace();
		}
}

public boolean initializePiezoVariables(){
	setNoSettings(false);
	if (accSettings.recordingParadigm.equals("Scan")){
		frames = (int) accSettings.framesPScanS;
		scanDistance = accSettings.scanDepthS;
		upperStart = accSettings.startPositionScan;
		System.out.println("upperstart position" + upperStart);
		lowerStart = upperStart - scanDistance;
		System.out.println("lowerstart position" + lowerStart);
		recordedOCFraction=2;
		
		outputCycles = frames / segmentLength;
		cycleDistance = scanDistance / outputCycles;
		scanNumber = accSettings.noScansS;
		setNoSettings(true);
		positionArrayLength = (((frames / (recordedOCFraction * 20)) + 1) * scanNumber * 2);
		positionarray = new String[positionArrayLength][4];
		arrayindex=0;
	}
	if (accSettings.recordingParadigm.equals("Cal")){
		frames = (int) accSettings.framesPScanCal;
		scanDistance = accSettings.scanDepthCal;
		upperStart = accSettings.startPositionCalibration;
		System.out.println("upperstart position" + upperStart);
		lowerStart = upperStart - scanDistance;
		System.out.println("lowerstart position" + lowerStart);
		recordedOCFraction=1;
		
		outputCycles = frames / segmentLength;
		cycleDistance = scanDistance / outputCycles;
		System.out.println("upperstart position" + upperStart);
		
		positionArrayLength = ((frames / (recordedOCFraction * 20)) + 1) ;
		positionarray = new String[positionArrayLength][4];
		arrayindex=0;
		setNoSettings(true);
	}
	else{setNoSettings(false);}
	return isNoSettings();	
}

public boolean isNoSettings() {
	return noSettings;
}

public void setNoSettings(boolean noSettings) {
	this.noSettings = noSettings;
}

}	



