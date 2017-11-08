///////////////////////////////////////////////////////////////////////////////
//PROJECT:       Micro-Manager
//SUBSYSTEM:     mmstudio
//-----------------------------------------------------------------------------
//               Definition of the Acquisition Protocol to be executed
//               by the acquisition engine
//
// AUTHOR:       Arthur Edelstein, Nenad Amodaj
//
// COPYRIGHT:    University of California, San Francisco, 2013
//
// LICENSE:      This file is distributed under the BSD license.
//               License text is included with the source distribution.
//
//               This file is distributed in the hope that it will be useful,
//               but WITHOUT ANY WARRANTY; without even the implied warranty
//               of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//
//               IN NO EVENT SHALL THE COPYRIGHT OWNER OR
//               CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
//               INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES.
//

package de.uniwuerzburg.physiologie;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.micromanager.acquisition.SequenceSettings;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * SequenceSettings objects contain the parameters describing how to run a
 * single acquisition. Various methods of the AcquisitionManager will consume
 * or generate SequenceSettings, and you can create your own to configure your
 * custom acquisitions.
 */

//version ID for the sequence settings
//public static final double Version = 1.0 ;

//Acquisition protocol
/**
 * new settings file
 */
public class AccessorySequenceSettings {

	/**
	 * chosse recordimng paradigm
	 */
	public String recordingParadigm = "Scan"; // ("Before", "After", "Epi",
												// "Cal");
	/**
	 * force MM to custom acqpath
	 */
	public boolean WPSPath = false;
	/**
	 * froot directory
	 */
	public String root;
	/**
	 * channel name
	 */
	public String channel;
	/**
	 * filename
	 */
	public String prefix;
	/**
	 * parentdirectory for Channel 2
	 */
	public String parRoot;
	/**
	 * boolean if channel two
	 */
	public boolean channel2Selected;
	/**
	 * imagesize
	 */
	public int imageSizeS;

	public String uniqueimagepath;
	// attribute für scanrecording

	/**
	 * exposure time scan
	 */

	public double expS;
	/**
	 * frame number per scan
	 */
	public double framesPScanS;
	/**
	 * Scanspeed Scan
	 */
	public double scanSpeedS;
	/**
	 * EM-Gain Scan
	 */

	public int emGainS;
	/**
	 * Scan depth Scan
	 */

	public double scanDepthS;
	/**
	 * folder Number of scans
	 */
	public int noScansS;
	/**
	 * fframes per micrometer
	 */
	public double framesPMicroS;

	// attribute für beadsrecording
	/**
	 * Exposure beads
	 */
	public double expBeads;
	/**
	 * frames per scan beads
	 */
	public int framesPScanBeads;
	/**
	 * EM- gain beads
	 */
	public int emGainBeads;

	// attribute für Epi
	/**
	 * Exposure Epi
	 */
	public double expEpi;

	/**
	 * EM Gain EPI
	 */
	public int emGainEpi;

	// attribute für §D calibration

	/**
	 * exposure calibration
	 */
	public double expCal;
	/**
	 * frames per scac cal
	 */

	public int framesPScanCal;
	/**
	 * Scanspee cal
	 */
	public double scanSpeedCal;
	/**
	 * Scandepth cal
	 */
	public double scanDepthCal;
	/**
	 * EMgain Cal
	 */
	public int emGainCal;
	
	public boolean manuell;
	
	public double startPositionScan;
	
	public double endPositionScan;
	
	public double positionBeadsBefore;
	
	public double positionBeadsAfter;
	
	public double positionEpi;
	
	public double startPositionCalibration;
	
	public double endPositionCalibration;
	
	public double framesPmicroCal;
	
	public String scanFilename;
	
	public String scanPathname;
	
	public String epiFilename;
	
	public String epiPathname;
	
	public String calFilename;
	
	public String calPathname;
	
	public String beadsBeforeFilename;
	
	public String beadsAfterFilename;
	
	public String beadsPathname;
	
	public String accSettingsFilename;
	
	public String metadataFilename;
	
	public String metadataPath;
	
	public String positionarrayFilename;
	
	public String positionarrayPath;
	
	
	public String camTrigger43;
	public String camCDTemperatureSetPoint8;
	public String camFanMode16;
	public String camReadMode27;
	public String camFrameTransfer17;
	public String camIsolatedCropMode20;
	public String camVerticalSpeed45;
	public String camVerticalClockVoltage44;
	public String camReadoutrate28;
	public String camPreAmpGain26;
	public String camOutput_Amplifier24;
	public String camEMSwitch14;
	public String camEMgain19;
	public String camShutter_Internal32;
	
	public boolean stopRecording=false;
	
	public double distanceToCoverslipEpi;
	public double definedFocusPositionEpi;
	public double freeFocuspositionEpi;
	public double distTocoverslipS;
	public String comments;
	public String channel1AssPath;
	
	
	/**
	 * Create a copy of this SequenceSettings. All parameters will be copied,
	 * with new objects being created as necessary (i.e. this is a deep copy).
	 * 
	 * @return Copy of this SequenceSettings.
	 */
	public AccessorySequenceSettings copy() {
		AccessorySequenceSettings result = new AccessorySequenceSettings();
		result.recordingParadigm = recordingParadigm;
		result.WPSPath = WPSPath;
		result.uniqueimagepath = uniqueimagepath;
		result.manuell = manuell;
		
		result.root = root;
		result.channel = channel;
		result.prefix = prefix;
		result.parRoot = parRoot;
		result.channel2Selected = channel2Selected;	
		result.imageSizeS = imageSizeS;

		result.expS = expS;
		result.framesPScanS = framesPScanS;
		result.scanSpeedS = scanSpeedS;
		result.emGainS = emGainS;
		result.scanDepthS = scanDepthS;
		result.noScansS = noScansS;
		result.framesPMicroS = framesPMicroS;
		result.startPositionScan = startPositionScan;
		result.endPositionScan = endPositionScan;
		

		result.expBeads = expBeads;
		result.framesPScanBeads = framesPScanBeads;
		result.emGainBeads = emGainBeads;
		result.positionBeadsBefore =positionBeadsBefore;
		result.positionBeadsAfter = positionBeadsAfter;
		
		result.expEpi = expEpi;
		result.emGainEpi = emGainEpi;
		result.positionEpi = positionEpi;
		
		
		result.expCal = expCal;
		result.framesPScanCal = framesPScanCal;
		result.scanSpeedCal = scanSpeedCal;
		result.emGainCal = emGainCal;
		result.scanDepthCal = scanDepthCal;
		result.framesPmicroCal= framesPmicroCal;
		result.startPositionCalibration = startPositionCalibration;
		result.endPositionCalibration = endPositionCalibration;

		
		result.scanFilename = scanFilename;
		result.scanPathname = scanPathname;
		result.epiFilename =epiFilename;
		result.epiPathname = epiPathname;
		result.calFilename = calFilename;
		result.calPathname = calPathname;
		result.beadsBeforeFilename = beadsBeforeFilename;
		result.beadsAfterFilename = beadsAfterFilename;
		result.beadsPathname = beadsPathname;
		result.accSettingsFilename = accSettingsFilename;
		result.metadataFilename = metadataFilename ;
		result.metadataPath = metadataPath ;

		
		
		
		result.camTrigger43=camTrigger43 ;
		result.camCDTemperatureSetPoint8=camCDTemperatureSetPoint8 ;
		result.camFanMode16= camFanMode16;
		result.camReadMode27=camReadMode27 ;
		result.camFrameTransfer17=camFrameTransfer17 ;
		result.camIsolatedCropMode20=camIsolatedCropMode20 ;
		result.camVerticalSpeed45=camVerticalSpeed45 ;
		result.camVerticalClockVoltage44=camVerticalClockVoltage44 ;
		result.camReadoutrate28=camReadoutrate28 ;
		result.camPreAmpGain26=camPreAmpGain26 ;
		result.camOutput_Amplifier24=camOutput_Amplifier24 ;
		result.camEMSwitch14=camEMSwitch14 ;
		result.camEMgain19=camEMgain19 ;
		result.camShutter_Internal32=camShutter_Internal32 ;
		
		result.metadataPath=metadataPath;
		result.positionarrayPath=positionarrayPath;
		
		
		result.distanceToCoverslipEpi = distanceToCoverslipEpi;
		result.definedFocusPositionEpi=definedFocusPositionEpi;
		result.freeFocuspositionEpi=freeFocuspositionEpi;
		result.distTocoverslipS = distTocoverslipS;
		
		result.stopRecording=stopRecording;
		result.comments=comments;
		result.channel1AssPath=channel1AssPath;
		
		return result;
	}

	public static AccessorySequenceSettings load(String path) throws IOException {
		return fromJSONStream(Files.toString(new File(path), Charsets.UTF_8));
	}

	public void save(String path) throws IOException {
		File file = new File(path);
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			writer.write(toJSONStream(this));
			writer.close();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	

	
	
	public AccessorySequenceSettings loadSettings(String path) throws IOException {
		AccessorySequenceSettings accSettings = AccessorySequenceSettings.fromJSONStream(Files.toString(new File(path), Charsets.UTF_8));
		
		return accSettings;
	}

	
	

	private static String toJSONStream(AccessorySequenceSettings accsettings) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(accsettings);
	}

	private static AccessorySequenceSettings fromJSONStream(String stream) {
		Gson gson = new Gson();
		return gson.fromJson(stream, AccessorySequenceSettings.class);
	}

}
