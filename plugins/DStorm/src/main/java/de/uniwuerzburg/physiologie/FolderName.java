package de.uniwuerzburg.physiologie;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
//import org.micromanager.acquisition.SequenceSettings;

//import org.micromanager.internal.utils.JavaUtils;

public class FolderName {

	;
	private String BeadsBeforeDirectory;
	private String BeadsAfterDirectory;
	private String Epidirectory;
	private String Caldirectory;
	private String Scandirectory;
	private String root;
	private String prefix;
	private String channel;
	private String acqDirPath;
	private String imgDirName;
	private String basefilename;
	private String CustomacquisitionPath;

	private String imageDirPath;
	private String imageBeadsPath;
	private String BeadsDirectory;
	private String imageBeadsBefPath;
	private String imageScanPath;
	private String imageBeadsAfPath;
	private String imageEpiPath;
	private String imageCalPath;
	private AccessorySequenceSettings accSettings;

	public FolderName(AccessorySequenceSettings accSettings) {
		this.accSettings = accSettings;
		this.root = accSettings.root;
		this.prefix = accSettings.prefix;
		this.channel = accSettings.channel;

	}

	public int getCurrentMaxDirIndex(File rootDir, String filename) throws NumberFormatException {
		int maxNumber = 0;
		int number;
		// String prefix2 = filename +"_";
		// System.out.println("p: "+ filename);
		String theName;

		// System.out.println("rootdir: "+ rootDir);
		for (File acqDir : rootDir.listFiles()) {
			theName = acqDir.getName();

			// System.out.println("thename: "+theName);
			// System.out.println(theName.startsWith(filename));
			if (theName.startsWith(filename)) {
				try {

					Pattern p = Pattern.compile("\\Q" + filename + "\\E" + "(\\d+).*+");
					Matcher m = p.matcher(theName);
					if (m.matches()) {
						// System.out.println("match");
						number = Integer.parseInt(m.group(1));
						// System.out.println("number"+number);
						if (number >= maxNumber) {
							maxNumber = number;
						}

					}
//					if (!m.matches()) {
//						// System.out.println("nomatch");
//					}
				} catch (NumberFormatException e) {

				} // Do nothing.
			}
		}
		// System.out.println(maxNumber);
		return maxNumber;

	}

	public String getCustomacquisitiondirectory(String root, String prefix) throws Exception {
		File rootDir = createDirectory(root);
		int curIndex = getCurrentMaxDirIndex(rootDir, prefix + "_");

		return prefix + "_" + (1 + curIndex);

	}

	public String getActualCustomAcquisitionDirectory(String root, String prefix) throws Exception {
		File rootDir = new File(root);
		int curIndex = getCurrentMaxDirIndex(rootDir, prefix + "_");

		return prefix + "_" + (1 + curIndex);

	}

	public File createDirectory(String dirPath) throws Exception {
		File dir = new File(dirPath);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new Exception("Unable to create directory " + dirPath);
			}
		}
		// System.out.println(dir);
		return dir;
	}

	// Create Directory Tree
	// root/image(prefix+index)/prefix+channelID+index(index for safety reasons
	// each time start button is pressed)/beads folders, Epi folder, metadata
	// folder,imgtif files;

	public String createImgDirectory(String root, String prefix, String channel) throws Exception {

		this.root = root;
		System.out.println("rootfolder__" + this.root);
		this.prefix = prefix;
		System.out.println("prefixfolder__" + this.prefix);
		this.channel = channel;
		System.out.println("channelfolder__" + this.channel);

		if (this.channel == "channel1") {
			this.imgDirName = getCustomacquisitiondirectory(root, prefix);
			this.acqDirPath = root + File.separator + imgDirName + File.separator + this.imgDirName + "_" + channel;
			
			//accSettings.metadataPath = root + File.separator + filename + File.separator + filename + "_channel1" + File.separator + "Scan_" + filename+ File.separator + filename + ".ass"
					
			accSettings.metadataPath = this.acqDirPath + File.separator + "Scan_" + this.imgDirName+ File.separator + this.imgDirName + ".ass"  ;
			accSettings.positionarrayPath = this.acqDirPath + File.separator + "Scan_" + this.imgDirName+ File.separator + this.imgDirName + ".pos"  ;
			File rootImg = createDirectory(acqDirPath);

			this.BeadsDirectory = this.acqDirPath + File.separator + "beads_" + this.imgDirName;
			accSettings.beadsPathname = this.BeadsDirectory;
			
			this.Epidirectory = this.acqDirPath + File.separator + "Epi_" + this.imgDirName;
			accSettings.epiPathname = this.Epidirectory;
			
			this.Caldirectory = this.acqDirPath + File.separator + "Calibration_" + this.imgDirName;
			accSettings.calPathname = this.Caldirectory;
			
			this.Scandirectory = this.acqDirPath + File.separator + "Scan_" + this.imgDirName;
			accSettings.scanPathname = this.Scandirectory;
			
		}

		if (this.channel == "channel2") {
			this.imgDirName = prefix;
			this.acqDirPath = root + File.separator + imgDirName + File.separator + this.imgDirName + "_" + channel;
			accSettings.metadataPath = this.acqDirPath + File.separator + "Scan_" + this.imgDirName+ File.separator + this.imgDirName + ".ass"  ;
			accSettings.positionarrayPath = this.acqDirPath + File.separator + "Scan_" + this.imgDirName+ File.separator + this.imgDirName + ".pos"  ;
			File rootImg = createDirectory(acqDirPath);

			this.BeadsDirectory = this.acqDirPath + File.separator + "beads_" + this.imgDirName;
			accSettings.beadsPathname = this.BeadsDirectory;
			
			this.Epidirectory = this.acqDirPath + File.separator + "Epi_" + this.imgDirName;
			accSettings.epiPathname = this.Epidirectory;
			
			this.Caldirectory = this.acqDirPath + File.separator + "Calibration_" + this.imgDirName;
			accSettings.calPathname = this.Caldirectory;
			
			this.Scandirectory = this.acqDirPath + File.separator + "Scan_" + this.imgDirName;
			accSettings.scanPathname = this.Scandirectory;
			
		}

		return imageDirPath;

	};

	public String createBeadsBeforePath() throws Exception {

		if (new File(this.BeadsDirectory).isDirectory()) {
			String string = "beads_" + this.imgDirName + "_before";
			System.out.println("beads directory. " + this.BeadsDirectory);
			this.setBasefilename(getActualCustomAcquisitionDirectory(this.BeadsDirectory, string));

		} else {
			File rootbeads = createDirectory(this.BeadsDirectory);
			this.setBasefilename("beads_" + this.imgDirName + "_before_1");
			System.out.println("beads before directorywill be . " + this.BeadsDirectory);
		}

		this.imageBeadsPath = this.BeadsDirectory + File.separator + this.getBasefilename();

		final String fileName = this.getBasefilename();
		accSettings.beadsBeforeFilename = fileName;
		//final File file = new File(this.BeadsDirectory, fileName);
		//file.createNewFile();

		return this.imageBeadsPath;

	}

	public String getBeadsBeforePath() throws Exception {

		if (new File(this.BeadsDirectory).isDirectory()) {

			this.setBasefilename(
					getActualCustomAcquisitionDirectory(this.BeadsDirectory, "beads_" + this.imgDirName + "_before"));
		} else {

			this.setBasefilename("beads_" + this.imgDirName + "_before_1");

		}

		this.imageBeadsBefPath = this.BeadsDirectory + File.separator + this.getBasefilename();

		System.out.println("final image path: " + this.imageBeadsBefPath);
		return this.imageBeadsBefPath;

	};

	public String createBeadsAfterPath() throws Exception {

		if (new File(this.BeadsDirectory).isDirectory()) {
			String string = "beads_" + this.imgDirName + "_after";
			System.out.println("beads directory. " + this.BeadsDirectory);
			this.setBasefilename(getActualCustomAcquisitionDirectory(this.BeadsDirectory, string));

		} else {
			File rootbeads = createDirectory(this.BeadsDirectory);
			this.setBasefilename("beads_" + this.imgDirName + "_after_1");

		}

		this.imageBeadsPath = this.BeadsDirectory + File.separator + this.getBasefilename();

		final String fileName = this.getBasefilename();// + ".txt";
		accSettings.beadsAfterFilename = fileName;
		//final File file = new File(this.BeadsDirectory, fileName);
		//file.createNewFile();

		return this.imageBeadsPath;

	}

	public String getBeadsAfterPath() throws Exception {

		if (new File(this.BeadsDirectory).isDirectory()) {

			this.setBasefilename(
					getActualCustomAcquisitionDirectory(this.BeadsDirectory, "beads_" + this.imgDirName + "_after"));

		} else {

			this.setBasefilename("beads_" + this.imgDirName + "_after_1");

		}

		this.imageBeadsAfPath = this.BeadsDirectory + File.separator + this.getBasefilename();

		System.out.println("final image path: " + this.imageBeadsAfPath);
		return this.imageBeadsAfPath;

	};

	public String createScanPath() throws Exception {

		if (new File(this.Scandirectory).isDirectory()) {
			String string = this.imgDirName;
			System.out.println("scan directory. " + this.Scandirectory);
			this.setBasefilename(getActualCustomAcquisitionDirectory(this.Scandirectory, string));

		} else {
			File rootscan = createDirectory(this.Scandirectory);
			this.setBasefilename(this.imgDirName + "_1");

		}

		this.imageScanPath = this.Scandirectory + File.separator + this.getBasefilename();

		final String fileName = this.getBasefilename();// + ".txt";
				accSettings.scanFilename=fileName;
		// final File file = new File(this.Scandirectory, fileName);
		// file.createNewFile();

		return this.imageScanPath;

	}

	public String getScanPath() throws Exception {

		if (new File(this.Scandirectory).isDirectory()) {

			this.setBasefilename(getActualCustomAcquisitionDirectory(this.Scandirectory, this.imgDirName));

		} else {

			this.setBasefilename(this.imgDirName + "_1");

		}

		this.imageScanPath = this.Scandirectory + File.separator + this.getBasefilename();

		System.out.println("final scan path: " + this.imageScanPath);
		return this.imageScanPath;

	};

	public String createEpiPath() throws Exception {

		if (new File(this.Epidirectory).isDirectory()) {
			String string = "Epi_" + this.imgDirName;
			System.out.println("epi directory. " + this.Epidirectory);
			this.setBasefilename(getActualCustomAcquisitionDirectory(this.Epidirectory, string));

		} else {
			File rootepi = createDirectory(this.Epidirectory);
			this.setBasefilename("Epi_" + this.imgDirName + "_1");

		}

		this.imageEpiPath = this.Epidirectory + File.separator + this.getBasefilename();

		final String fileName = this.getBasefilename();// + ".txt";
		accSettings.epiFilename=fileName;
		//final File file = new File(this.Epidirectory, fileName);
		//file.createNewFile();

		return this.imageEpiPath;

	}

	public String getEpiPath() throws Exception {

		if (new File(this.Epidirectory).isDirectory()) {

			this.setBasefilename(getActualCustomAcquisitionDirectory(this.Epidirectory, "Epi_" + this.imgDirName));

		} else {

			this.setBasefilename("Epi_" + this.imgDirName + "_1");

		}

		this.imageEpiPath = this.Epidirectory + File.separator + this.getBasefilename();

		System.out.println("final epi path: " + this.imageEpiPath);
		return this.imageEpiPath;

	};

	public String createCalPath() throws Exception {

		if (new File(this.Caldirectory).isDirectory()) {
			String string = "Calibration_" + this.imgDirName;
			System.out.println("cal directory. " + this.Caldirectory);
			this.setBasefilename(getActualCustomAcquisitionDirectory(this.Caldirectory, string));

		} else {
			File rootcal = createDirectory(this.Caldirectory);
			this.setBasefilename("Calibration_" + this.imgDirName + "_1");

		}

		this.imageCalPath = this.Caldirectory + File.separator + this.getBasefilename();

		final String fileName = this.getBasefilename();// + ".txt";
		accSettings.calFilename=fileName;
		//final File file = new File(this.Caldirectory, fileName);
		//file.createNewFile();

		return this.imageCalPath;

	}

	public String getCalPath() throws Exception {

		if (new File(this.Caldirectory).isDirectory()) {

			this.setBasefilename(
					getActualCustomAcquisitionDirectory(this.Caldirectory, "Calibration_" + this.imgDirName));

		} else {

			this.setBasefilename("Calibration_" + this.imgDirName + "_1");

		}

		this.imageCalPath = this.Caldirectory + File.separator + this.getBasefilename();

		System.out.println("final cal path: " + this.imageCalPath);
		return this.imageCalPath;

	};

	public String getBasefilename() {
		return basefilename;
	}

	public void setBasefilename(String basefilename) {
		this.basefilename = basefilename;
	}

	public String getCustomacquisitionPath() {
		return CustomacquisitionPath;
	}

	public void setCustomacquisitionPath(String customacquisitionPath) {
		CustomacquisitionPath = customacquisitionPath;
	}

//	public String getAssPath() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public String getPospath() {
//		// TODO Auto-generated method stub
//		return null;
//	}
}