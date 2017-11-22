package de.uniwuerzburg.physiologie;

import org.micromanager.internal.MMStudio;

import mmcorej.CMMCore;

public class Piezo {

	private CMMCore mmc;
	private PluginEngine pluginEngine;
	
	public Piezo(){
		mmc =MMStudio.getInstance().getCMMCore();
		try {
			mmc.loadDevice("Port", "SerialManager", "COM10");
			
		} catch (Exception ex) {
			System.out.println("load portdevice error ," + ex);
		}

		try {
			mmc.initializeDevice("Port");
		} catch (Exception e1) {
			System.out.println("initialize portdevice error ," + e1);
			e1.printStackTrace();
		}
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
			pluginEngine.errorDialog("error in setting zPosOnly");
			e.printStackTrace();
		} catch (Exception e) {
			pluginEngine.errorDialog("error in setting zPosOnly");
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
		} catch (Exception e1) {
			System.out.println("can't obtain zPosition");
			e1.printStackTrace();
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
			mmc.setSerialPortCommand("Port", "WGN?", "\n"); // WGN get number of completed output cycles
			String answer = mmc.getSerialPortAnswer("Port", "\n");
			outputCycleID = Integer.parseInt((answer).substring(2));
		
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
		} catch (Exception e) {
			System.out.println("problem getting Framenumber");			e.printStackTrace();
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
				pluginEngine.errorDialog("error in setting Piezo Trigger mode");
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
			pluginEngine.errorDialog("error in getting Piezo Trigger mode");
			e.printStackTrace();
		} catch (Exception e) {
			pluginEngine.errorDialog("error in getting Piezo Trigger mode");
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
				pluginEngine.errorDialog("error in setting Piezo Trigger Type");
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
			pluginEngine.errorDialog("error in getting Piezo Trigger Type");
			e.printStackTrace();
		} catch (Exception e) {
			pluginEngine.errorDialog("error in getting Piezo Trigger Type");
			e.printStackTrace();
		}
	return triggerInputType;
	}



	//Set Configuration of trigger input Polarity Triggerinputchannel 1 parametre to : String "active high"  or "active low" 	
	public void setPiezoConfigurationOfTriggerInputPolarity (String string){
		
		try {
			if (string.equals("high")){mmc.setSerialPortCommand("Port", "CTI 1 7 1" , "\n"); }
			else{
				mmc.setSerialPortCommand("Port", "CTI 1 7 0" , "\n");
				}
		} catch (Exception e) {
			pluginEngine.errorDialog("error in setting Piezo Trigger Polarity");
			e.printStackTrace();
		}
		}

	//Get Configuration of trigger input Polarity Triggerinputchannel 1 :  "active high"  or "active low"
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
			pluginEngine.errorDialog("error in getting Piezo Trigger Polarity");
			e.printStackTrace();
		} catch (Exception e) {
			pluginEngine.errorDialog("error in getting Piezo Trigger Polarity");
			e.printStackTrace();
		}
		return triggerPolarity;
		}

	// set Piezo rectangular positiv TriggerOutput to a fraction of 1/wavepointfraction
	public void setPiezoTriggerOutput(int wavepointfraction)  {
		
		try {
			mmc.setSerialPortCommand("Port", "TWC" , "\n");
			mmc.setSerialPortCommand("Port", "CTO 1 3 4" , "\n");
			mmc.setSerialPortCommand("Port", "CTO 1 7 1" , "\n");
			mmc.setSerialPortCommand("Port", "WAV? 5 1 ", "\n");
			int wavePoints=Integer.parseInt(mmc.getSerialPortAnswer("Port","\n").substring(4));
			System.out.println( "Number of wavepoints: ," + wavePoints);
			for (int i=0; i < wavePoints; i++){
			int wavepoint=i+1;
			if (i%wavepointfraction==0){
			mmc.setSerialPortCommand("Port", "TWS 1 "+wavepoint+" 1" , "\n");
			}
			else{
			mmc.setSerialPortCommand("Port", "TWS 1 "+wavepoint+" 0" , "\n");
			}
			}
		} catch (NumberFormatException e) {
			pluginEngine.errorDialog("error in setting Piezo output trigger");
			e.printStackTrace();
		} catch (Exception e) {
			pluginEngine.errorDialog("error in setting Piezo output trigger");
			e.printStackTrace();
		}
		}

	//set number of Piezooutputcycles
	public void setPiezoOutputcycles (int outputCycles){
		try {
			mmc.setSerialPortCommand("Port", "WGC 1 " + outputCycles , "\n");
		} catch (Exception e) {
			pluginEngine.errorDialog("error in setting Piezo outPutCycles");
			e.printStackTrace();
		}	
		}

	//get number of Piezooutputcycles
	public int getPiezoOutputcycles (){ 
		int outputCycles=0;
		try {
			mmc.setSerialPortCommand("Port", "WGC? 1 " + outputCycles , "\n");
			outputCycles = Integer.parseInt(mmc.getSerialPortAnswer("Port","\n").substring(2));
		} catch (NumberFormatException e) {
			pluginEngine.errorDialog("error in getting Piezo outPutCycles");
			e.printStackTrace();
		} catch (Exception e) {
			pluginEngine.errorDialog("error in getting Piezo outPutCycles");
			e.printStackTrace();
		}
		return outputCycles;
		}

	//set WavetableID
	public void setWavegeneratorTableID (int wavetableID){
		try {
			mmc.setSerialPortCommand("Port", "WSL 1 " + wavetableID , "\n");
		} catch (Exception e) {
			pluginEngine.errorDialog("error in setting WavegeneratorTableID ");
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
			pluginEngine.errorDialog("error in getting WavegeneratorTableID ");
			e.printStackTrace();
		} catch (Exception e) {
			pluginEngine.errorDialog("error in getting WavegeneratorTableID ");
			e.printStackTrace();
		}
		return wavetableID;
		}

	//Set Wavegenerator start position
	public void setWavegeneratorStartposition (int startPos){
		try {
			mmc.setSerialPortCommand("Port", "WOS 1 " + startPos , "\n");
		} catch (Exception e) {
			pluginEngine.errorDialog("error in setting startposition ");
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
			pluginEngine.errorDialog("error in getting startposition ");
			e.printStackTrace();
		} catch (Exception e) {
			pluginEngine.errorDialog("error in getting startposition ");
			e.printStackTrace();
		}
		return startPos;
		}

	//Set Wavegenerator table rate
	public void setWavegeneratorTableRate (int wavetablerate){
		try {
			mmc.setSerialPortCommand("Port", "WTR 1 " + wavetablerate + " 0", "\n");
		} catch (Exception e) {
			pluginEngine.errorDialog("error in setting WavegeneratorTableRate ");
			e.printStackTrace();
		}	
		}

	//get wavegenerator tableRate
	public int getWavegeneratorTableRate (){ 
		int wavetablerate;
		try {
			mmc.setSerialPortCommand(port, "WTR? 1 ", "\n");
			String answer=mmc.getSerialPortAnswer("Port","\n");
			wavetablerate = Integer.parseInt(answer.substring(2,(answer.length()-2)));
		} catch (NumberFormatException e) {
			pluginEngine.errorDialog("error in setting WavegeneratorTableRate ");
			e.printStackTrace();
		} catch (Exception e) {
			pluginEngine.errorDialog("error in setting WavegeneratorTableRate ");
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
			mmc.setSerialPortCommand("Port", "WGO 1 " + startStopMode+ " 0", "\n");
		} catch (Exception e) {
			pluginEngine.errorDialog("error in setting WavegeneratorStartStopMode ");
			e.printStackTrace();
		}	
		}

	// piezoWait until Piezo reaches position
	public void waitForArriving(double position){
		try {
			mmc.setSerialPortCommand("Port", "WAC ONT? Z = 1", "\n");
		} catch (Exception e) {
			pluginEngine.errorDialog("error in arriving at position");
			e.printStackTrace();
		}	
	}
	 // reset piezo
	public void resetPiezo(){
		try {
			mmc.setSerialPortCommand("Port", "WGO 1 1 ", "\n");	
			mmc.setSerialPortCommand("Port", "WGO 1 0 ", "\n");	
			Thread.sleep(100);
		} catch (InterruptedException e) {
			pluginEngine.errorDialog("error in resetting Piezo");
			e.printStackTrace();
		} catch (Exception e) {
			pluginEngine.errorDialog("error in resetting Piezo");
			e.printStackTrace();
		}
	}

	public void stopPiezo(){
		try {
			mmc.setSerialPortCommand("Port", "HLT", "\n");
			Thread.sleep(200);
			mmc.setSerialPortCommand("Port", "WGO 1 0", "\n");
		} catch (InterruptedException e) {
			pluginEngine.errorDialog("error in stopping Piezo");
			e.printStackTrace();
		} catch (Exception e) {
			pluginEngine.errorDialog("error in stopping Piezo");
			e.printStackTrace();
		}
	}

	//Set Wavetable direction upwards/downwards
	public void setPiezoWavetableParametres(String direction, int wavetableID , int segmentLength, double cycleDistance){ 
		try {
			if (direction.equals("upwards")){
			mmc.setSerialPortCommand("Port","WAV "+ wavetableID + " X LIN " + segmentLength + " " + cycleDistance + " 0 " + segmentLength + " 0 0 ", "\n");}
			if (direction.equals("downwards")){
				mmc.setSerialPortCommand("Port","WAV "+ wavetableID + " X LIN " + segmentLength + " -" + cycleDistance + " 0 " + segmentLength + " 0 0 ", "\n");}
			
		} catch (Exception e) {
			pluginEngine.errorDialog("error in setting wavetable for " + direction +"-scan");
			e.printStackTrace();
		}
		
	}



}
