
package de.uniwuerzburg.physiologie;

import org.micromanager.Studio;
import mmcorej.CMMCore;
import mmcorej.StrVector;

//commandTerminator = "\r"; 
//answerTerminator = "ok"; 
// Piezo WAC = WAit until Condition

public class Piezorun {
	
	
	private Studio app_;
	private String answer;
	private AccessorySequenceSettings accSettings;
	
	//fix variables
	
	private String port = "COM10";
	int  segmentLength = 20;
	
	
	//variables for GUI interaction
	private int frames;
	private double upperStart;
	private double lowerStart;
	double scanDistance;
	private int scanNumber;	
	
	
	// variables for methods inter calculations
	private int outputCycleID;	
	int  outputCycles = frames/segmentLength;
	private int frameID = outputCycleID*segmentLength ;
	private int positionArrayLength= ((outputCycles*2*scanNumber)/10);
	private String [][] positionarray ;
	private double tempposz;
	private double cycleDistance=scanDistance/outputCycles ;


	public Piezorun(AccessorySequenceSettings accSettings , Studio app_) {
		this.accSettings = accSettings;
		this.app_=app_;
	}
	
	private CMMCore mmc = app_.getCMMCore();
	private String zDevice= mmc.getFocusDevice();
	
public void piezorun(){
//initialize variables
	frames=(int) accSettings.framesPScanS;
	upperStart=accSettings.startPositionScan ;
	lowerStart=accSettings.endPositionScan ;
	scanDistance=accSettings.scanDepthS ;
	scanNumber=accSettings.noScansS; ;	
	positionarray= new String [positionArrayLength][3];

//initialize Piezotriggerstate
try {
	mmc.setSerialPortCommand(port, "TRI 1 1" , "\n");  				// set trigger input state triggerinputchannel 1  to mode 1 =enabled
	mmc.setSerialPortCommand(port, "CTI 1 1 0" , "\n"); 			//Set Configuration of trigger Input Triggerinputchannel 1 parametre Triggertype(1) to edge triggered(0)
	mmc.setSerialPortCommand(port, "CTI 1 7 1" , "\n"); 			//Set Configuration of trigger Input Triggerinputchannel 1 parametre polarity(1) to active high(1)
	mmc.setSerialPortCommand(port, "CTO 1 3 4" , "\n"); 			//Set Configuration of trigger output Triggerinputchannel 2 triggermode(1) to trigger line action defined with TWS(4) (associates trigger output with waveform
	
	// initialize wavegenarator
	mmc.setSerialPortCommand(port, "WSL 1 5" , "\n"); 				//Set connection to wave table (is defined in Piezosoftware; beware to add manual programmin to this skript)
	mmc.setSerialPortCommand(port, "WGC 1 " + outputCycles , "\n"); //Set number of outputcycles
	mmc.setSerialPortCommand(port, "WOS 1 " + upperStart, "\n"); 	//Set startvalue for piezo scanning
	mmc.setSerialPortCommand(port, "WTR 1 1 0 ", "\n"); 			//Set wave generator table rate
	mmc.setSerialPortCommand(port, "WGO 1 0 " , "\n"); 				//Set wave generator start stop mode: = means wave generator is stopped
	
	
	mmc.setSerialPortCommand(port, "WAV? 5 1 ", "\n");
	answer= mmc.getSerialPortAnswer(port,"\n");
	System.out.println( "Waveform," + answer);
	
	mmc.setSerialPortCommand(port, "WGC? 1 ", "\n");
	answer= mmc.getSerialPortAnswer(port,"\n");
	System.out.println( "Number of wave generator cycles: ," + answer);
	
	mmc.setSerialPortCommand(port, "WOS? 1 ", "\n");
	answer= mmc.getSerialPortAnswer(port,"\n");
	System.out.println( "B ," + answer);
	
	mmc.setSerialPortCommand(port, "WSL? 1 ", "\n");
	answer= mmc.getSerialPortAnswer(port,"\n");
	System.out.println( "C ," + answer);
	
	mmc.setSerialPortCommand(port, "WTR? 1 ", "\n");
	answer= mmc.getSerialPortAnswer(port,"\n");
	System.out.println( "SD ," + answer);
} catch (Exception e) {
	 System.out.println("piezosettingg error!");
	e.printStackTrace();
}

int i = 1;

for(i=1; i<=scanNumber; i++){
	
int tempi = 0;	




//public void piezoRunUpwards()   {
////Upwards scan

try {
	
		
	mmc.setSerialPortCommand(port, "WOS 1 " + upperStart, "\n"); // set scan offset
	mmc.setSerialPortCommand(port, "MOV z " + upperStart, "\n"); // mov to scan offset upper level
	mmc.waitForDevice(zDevice);
	System.out.println( "Reached upper start position: ," +  + mmc.getPosition(zDevice)); 
	mmc.setSerialPortCommand(port, "WAV 5 X LIN " + segmentLength + " -" + cycleDistance + " 0 " + segmentLength + " 0 0 ", "\n");  
																			// set wave form  syntax  wavetableID, Appendwave(X clears wavetable), Wave type (lin is linear); Wave type parametres [Segment length (20 points?), 
																			//Amp (distance pro outputcycle), offset (from startvalue), Wave lenght (selbsterklärend, must be shorter than seg length,Startpoint (temporal offset) Speed up down for non triggeredfor more questions RTFM p 194]
	mmc.waitForDevice(zDevice);
	mmc.setSerialPortCommand(port, "WGO 1 256", "\n"); //Set wave generator start stop mode strat next cycle at position of previous cycle
	
	do{ 
	mmc.setSerialPortCommand(port, "WGN?", "\n");  //WGN get number of completed output cycles
	outputCycleID= Integer.parseInt(mmc.getSerialPortAnswer(port,"\n"));
			
	System.out.println( "completed output cycle number ," + outputCycleID);
	
	
	if (outputCycleID%10==0){													//System.out.println every 100 position; can be replaced by get befehl für Metadatengenerierung.
	System.out.println( "Frame: "+ frameID +", Pos Z: "+ mmc.getPosition(zDevice));
	mmc.setSerialPortCommand(port, "WGN?", "\n"); 
	outputCycleID= Integer.parseInt(mmc.getSerialPortAnswer(port,"\n"));
	System.out.println( "completed output cycle number ," + outputCycleID);
	positionarray [(outputCycles*tempi)+outputCycleID-1][0]=String.valueOf(outputCycleID*200 + (tempi*frames));
	positionarray [(outputCycles*tempi)+outputCycleID-1][1]="upscan_"+i;
	positionarray [(outputCycles*tempi)+outputCycleID-1][2]=String.valueOf(tempposz);
	
	}
	
	mmc.setSerialPortCommand(port, "WGO 1 0 " , "\n"); 				//Stop Wave generator output
	System.out.println( "Reached lower stop position: ," +  + mmc.getPosition(zDevice));
	
	
	} while (outputCycleID<=outputCycles);
	tempi ++;
} catch (NumberFormatException e) {
	System.out.println("numberformatexeption upscan error!");
	e.printStackTrace();
} catch (Exception e) {
	System.out.println("upscan error!");
	e.printStackTrace();
}



//public void piezorunDownwards (){
////Downwards scan

	try {
		do{
mmc.setSerialPortCommand(port, "WOS 1 " + lowerStart, "\n"); 
mmc.setSerialPortCommand(port, "MOV z " + lowerStart, "\n"); 
mmc.waitForDevice(zDevice);
System.out.println( "Reached lower start position: ," +  + mmc.getPosition(zDevice));
mmc.setSerialPortCommand(port, "WAV 5 X LIN " + segmentLength + " " + cycleDistance + " 0 " + segmentLength + " 0 0 ", "\n"); 
mmc.setSerialPortCommand(port, "WGO 1 256", "\n"); 

mmc.setSerialPortCommand(port, "WGN?", "\n"); 
outputCycleID= Integer.parseInt(mmc.getSerialPortAnswer(port,"\n"));
System.out.println( "completed output cycle number ," + outputCycleID);

if (outputCycleID%10==0){													//System.out.println every 100 position; can be replaced by get befehl für Metadatengenerierung.
tempposz=mmc.getPosition(zDevice);
System.out.println( "Frame: "+ frameID +", Pos Z: "+ tempposz);
mmc.setSerialPortCommand(port, "WGN?", "\n"); 
outputCycleID= Integer.parseInt(mmc.getSerialPortAnswer(port,"\n"));
System.out.println( "completed output cycle number ," + outputCycleID);

positionarray [(outputCycles*tempi)+outputCycleID-1][0]=String.valueOf(outputCycleID*200 + (tempi*frames));
positionarray [(outputCycles*tempi)+outputCycleID-1][1]="downscan_"+i;
positionarray [(outputCycles*tempi)+outputCycleID-1][2]=String.valueOf(tempposz);

}

mmc.setSerialPortCommand(port, "WGO 1 0 " , "\n"); 				//Stop Wave generator output
System.out.println( "Reached upper stop position: ," +  + mmc.getPosition(zDevice));

		} while (outputCycleID<=outputCycles);
		tempi ++;
	} catch (NumberFormatException e) {
		System.out.println("numberformatexeption downscan error!");
		e.printStackTrace();
	} catch (Exception e) {
		System.out.println("downscan error!");
		e.printStackTrace();
	}



	}


}
}
