package de.uniwuerzburg.physiologie;

import mmcorej.CMMCore;
import mmcorej.StrVector;

import javax.swing.JFrame;

import org.micromanager.MenuPlugin;
import org.micromanager.Studio;
import org.micromanager.internal.MMStudio;
import org.micromanager.internal.utils.ReportingUtils;

import org.scijava.plugin.Plugin;
import org.scijava.plugin.SciJavaPlugin;

@Plugin(type = MenuPlugin.class)
public class DstormPlugin implements MenuPlugin, SciJavaPlugin {
	public static final String menuName = "dStorm volume recording";
	public static final String tooltipDescription = "Surface for generating volume dstorm images";

	
	private Studio app_;
	private CMMCore core_;
	private StrVector cameras;
	//private AccessorySequenceSettings accSettings ;
	//private FolderName foldername;

	public void configurationChanged() {
		// TODO Auto-generated method stub

	}

	public void dispose() {
	}

	@Override
	public String getName() {
		return menuName;
	}

	@Override
	public String getCopyright() {
		// TODO Auto-generated method stub
		return "Julius-Maximilians-University Wuerzburg, 2017. Authors: Martin Pauli, Christian  Geiger";
	}

	@Override
	public String getHelpText() {
		// TODO Auto-generated method stub
		return tooltipDescription;
	}

	@Override
	public String getVersion() {
		return "V1.0";
	}

	@Override
	public void setContext(Studio app) {
		app_ = app;
	}

	@Override
	public String getSubMenu() {
		return "Acquisition Tools";
	}

	@Override
	public void onPluginSelected() {
		try {
			core_=app_.getCMMCore();
			
			cameras=core_.getLoadedDevicesOfType(mmcorej.DeviceType.CameraDevice);
			for(String camera : cameras){
			
			//String camera = core_.getCameraDevice();
			StrVector camProps= core_.getDevicePropertyNames(camera);
			String[] arrayCamProps = camProps.toArray();
				

			String trigger = arrayCamProps [43];	
			core_.setProperty(camera, trigger, "Internal");

			String CCDTemperatureSetPoint = arrayCamProps [8];
			core_.setProperty(camera, CCDTemperatureSetPoint, "-80");

			String FanMode = arrayCamProps [16];
			core_.setProperty(camera, FanMode, "Full");

			String ReadMode = arrayCamProps [27];
			core_.setProperty(camera, ReadMode, "Image");

			String FrameTransfer = arrayCamProps [17];
			core_.setProperty(camera, FrameTransfer, "On");

			String IsolatedCropMode = arrayCamProps [20];
			core_.setProperty(camera, IsolatedCropMode, "Off");

			String VerticalSpeed = arrayCamProps [45];
			core_.setProperty(camera, VerticalSpeed, "3.30");

			String VerticalClockVoltage = arrayCamProps [44];
			core_.setProperty(camera, VerticalClockVoltage, "Normal");

			String Readoutrate = arrayCamProps [28];
			core_.setProperty(camera, Readoutrate, "17.000 MHz");

			String PreAmpGain = arrayCamProps [26];
			core_.setProperty(camera, PreAmpGain, "Gain 1");
						
			String Output_Amplifier = arrayCamProps [24];
			core_.setProperty(camera, Output_Amplifier, "Electron Multiplying");

			String EMSwitch = arrayCamProps [14];
			core_.setProperty(camera, EMSwitch, "On");

			String EMgain = arrayCamProps [19];
			core_.setProperty(camera, EMgain, "200");

			String Shutter_Internal = arrayCamProps [32];
			core_.setProperty(camera, Shutter_Internal, "Open");
			
			if (!camera.equals("Andor 8309 longwave")){
				String FlipY = arrayCamProps [41];
				core_.setProperty(camera, FlipY, "1");
				
			}
			}
			
		} catch (Exception e) {
			System.out.println("camera settings Failed");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		DstormPluginGui frame = new DstormPluginGui(app_, "COM8");
		
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		
		
	}
}
