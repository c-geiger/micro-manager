package de.uniwuerzburg.physiologie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import org.micromanager.Studio;

import mmcorej.CMMCore;
import mmcorej.MMCoreJ;
import mmcorej.StrVector;

public class CameraSelectionbox extends JComboBox<String> implements ActionListener{

	private Studio studio;
	private CMMCore core;
	private StrVector cameras;
	private PluginUtils pluginUtils;
	private boolean setCamera;
	private DstormPluginGui gui;
	public CameraSelectionbox (){
		this.addItem("DemoCamera");
	}

	public CameraSelectionbox(CMMCore core, PluginUtils pluginUtils, boolean setCamera, Studio studio, DstormPluginGui gui) {
		this.core=core;
		this.studio=studio;
		this.gui=gui;
		this.setCamera=setCamera;
		cameras=core.getLoadedDevicesOfType(mmcorej.DeviceType.CameraDevice);
		for(String camera : cameras){
			this.addItem(camera);
		}
		this.pluginUtils=pluginUtils;
		this.addActionListener(this);
		this.setSelectedItem(core.getCameraDevice());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String camera="no camera";
		if (setCamera){
		try {
			camera=getSelectedItem().toString();
			if (studio.live().getIsLiveModeOn()){
			studio.live().setLiveMode(false);
			core.setCameraDevice(camera);
			studio.live().setLiveMode(true);
			}
			else{
				core.setCameraDevice(camera);	
			}
		} catch (Exception e1) {
			pluginUtils.errorDialog("Could not set camera " + camera);
			e1.printStackTrace();
		}
		}
		else {System.out.println("setcamera");}
		super.actionPerformed(e);
	}
public String setSelectedCamera(CameraSelectionbox liveCam){
	String camera="no camera";
	try {
		camera=getSelectedItem().toString();
		core.setCameraDevice(camera);
	} catch (Exception e1) {
		pluginUtils.errorDialog("Could not set camera " + camera);
		e1.printStackTrace();
	}
	liveCam.setSelectedItem(camera);
	return camera;
}
}