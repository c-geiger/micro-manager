package de.uniwuerzburg.physiologie;

import java.io.File;
import org.micromanager.Studio;
import org.micromanager.acquisition.SequenceSettings;
import org.micromanager.data.Coords.CoordsBuilder;
import org.micromanager.data.internal.multipagetiff.StorageMultipageTiff;
import org.micromanager.data.Datastore;
import org.micromanager.data.Image;
import org.micromanager.display.DisplayWindow;
import org.micromanager.internal.MMStudio;

import mmcorej.CMMCore;
import mmcorej.TaggedImage;

public class SequenceRun implements Runnable {
	
	private Studio app_ = MMStudio.getInstance();
	private CMMCore core_ = app_.getCMMCore();
	private AccessorySequenceSettings accSettings;
	private FolderName folderName;
	private double exposureMs = 10;
	private Datastore store = null;
	String camera = null;
	private SequenceSettings settings=app_.acquisitions().getAcquisitionSettings();
	private int curFrame =0;
	public int getCurFrame() {
		return curFrame;
	}

	private PluginUtils pluginUtils;

	public SequenceRun (AccessorySequenceSettings accSettings, FolderName folderName, PluginUtils pluginUtils){
		this.accSettings=accSettings;
		this.folderName=folderName;
		this.pluginUtils=pluginUtils;

		try{

			settings.useCustomIntervals = false;
			settings.intervalMs = 0;
			
			camera = core_.getCameraDevice();
			core_.setExposure(accSettings.expS);
			exposureMs = core_.getExposure();
			int roi = accSettings.imageSizeS;
			int roiBorderx = 256 -(roi/2);
			int roiBordery = 256 -(roi/2);
			core_.setROI(roiBorderx, roiBordery ,roi,roi);
			
			try {
				folderName.createScanPath();
			} catch (Exception e1) {
				System.out.println ("filemaker error");
				e1.printStackTrace();
			}
			StorageMultipageTiff.setShouldGenerateMetadataFile(false);
			MMStudio.USE_CUSTOM_PATH=true;
			MMStudio.CUSTOM_FILE_NAME = accSettings.scanFilename;
			MMStudio.CUSTOM_PATH_NAME = accSettings.scanPathname;
			accSettings.framesPScanS=15000.0;
			store = app_.data().createMultipageTIFFDatastore(accSettings.scanPathname, false, false);

			// Create a display to show images as they are acquired.
			app_.displays().createDisplay(store);
			app_.displays().manage(store);
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public void run() {
			
		try {

			

				// Create a Datastore for the images to be stored in, in RAM.
				

				// Start collecting images.
				// Arguments are the number of images to collect, the amount of
				// time to wait
				// between images, and whether or not to halt the acquisition if
				// the
				// sequence buffer overflows.
				core_.startSequenceAcquisition((int)accSettings.framesPScanS, 0, true);
			

			// Set up a Coords.CoordsBuilder for applying coordinates to each
			// image.
			CoordsBuilder builder = app_.data().getCoordsBuilder().z(0).channel(0).stagePosition(0);
			curFrame = 0;

			
			
			while (core_.getRemainingImageCount() > 0 || core_.isSequenceRunning(camera)) {
				if (core_.getRemainingImageCount() > 0) {
					TaggedImage tagged = core_.popNextTaggedImage();
					// Convert to an Image at the desired timepoint.
					Image image = app_.data().convertTaggedImage(tagged, builder.time(curFrame).build(), null);
					store.putImage(image);
					curFrame++;

				} else {
					// Wait for another image to arrive.
					core_.sleep(Math.min(.5 * exposureMs, 20));
				}
				}
				// print("#images: " + curFrame);
				store.freeze();

				// Have Micro-Manager handle logic for ensuring data is saved to
				// disk.
				core_.stopSequenceAcquisition();
				Thread.sleep(100);

//				store.close();
//				store = null;
				
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
public void sequenceStop(){
	try {
		core_.stopSequenceAcquisition();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	while(running());
}
	
public boolean running(){
	boolean running=false;
	try {
		running = core_.isSequenceRunning(camera);
	} catch (Exception e) {
		pluginUtils.errorDialog("error in setting waiting for device ");
		e.printStackTrace();
	}
	return running;
}
}