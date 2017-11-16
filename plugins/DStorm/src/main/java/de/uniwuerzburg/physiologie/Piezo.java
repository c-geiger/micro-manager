package de.uniwuerzburg.physiologie;

import org.micromanager.internal.MMStudio;

import mmcorej.CMMCore;

public class Piezo {

	private CMMCore mmc;
	
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
}
