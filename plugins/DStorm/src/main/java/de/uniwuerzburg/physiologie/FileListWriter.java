package de.uniwuerzburg.physiologie;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.micromanager.Studio;
import org.micromanager.internal.MMStudio;




public class FileListWriter {
	public static void save(String path)  throws IOException{
		File file = new File(path);
		FileWriter writer = null;
		List listD=MMStudio.getListD();
		List listI=MMStudio.getListI();
		List listS=MMStudio.getListS();
		List listRP=MMStudio.getListRP();
		List listStartTime=MMStudio.getListStartTime();
		List listStopTime=MMStudio.getListStopTime();
		List listStartPosition=MMStudio.getListStartPosition();
		List listStopPosition=MMStudio.getListStopPosition();
		
		try {
			writer = new FileWriter(file);
			writer.write("recording paradigm , ");
			writer.write("filename , ");
			writer.write("scandirection , ");
			writer.write("breakFrame , " );
			writer.write("startTime , ");
			writer.write("stopTime , " );
			writer.write("startPosition , ");
			writer.write("stopPosition \n");
			
			int i =0;
			for( i = 0; i < listI.size(); i++){
				
					writer.write(listRP.get(i) + ", ");
					writer.write(listS.get(i) + ", ");
					writer.write(listD.get(i) + ", ");
					writer.write(listI.get(i)+", " );
					writer.write(listStartTime.get(i)+", " );
					writer.write(listStopTime.get(i)+", " );
					writer.write(listStartPosition.get(i)+", " );
					writer.write(listStopPosition.get(i)+"\n" );
			}
			writer.close();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

}