package de.uniwuerzburg.physiologie;

import java.io.File;
import org.micromanager.Studio;
import org.micromanager.data.Coords.CoordsBuilder;
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
	
	public SequenceRun (AccessorySequenceSettings accSettings, FolderName folderName){
		this.accSettings=accSettings;
		this.folderName=folderName;


		try{

			camera = core_.getCameraDevice();

			core_.setExposure(16);
			exposureMs = core_.getExposure();
			core_.setROI(0, 0 ,400,400);
			try {
				folderName.createScanPath();
			} catch (Exception e1) {
				System.out.println ("filemaker error");
				e1.printStackTrace();
			}
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
			int curFrame = 0;

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
				// app_.displays().manage(store);
				// store.save(Datastore.SaveMode.MULTIPAGE_TIFF, "
				core_.stopSequenceAcquisition();
				Thread.sleep(100);

//				store.close();
//				store = null;
				
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}