package de.uniwuerzburg.physiologie;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//import org.micromanager.acquisition.SequenceSettings;

public class ZPositionArrayWriter {

	public static void save(String[][] posArray, String path)  throws IOException{
		File file = new File(path);
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			writer.write("scanDirection , ");
			writer.write("frame , ");
			writer.write("zPosition \n" );
			int i =0;
			for( i = 0; i < posArray.length; i++){
				
					
					writer.write(posArray[i][0] + ", ");
					writer.write(posArray[i][1] + ", ");
					writer.write(posArray[i][2] + ", ");
					writer.write(posArray[i][3]+"\n" );
			}
			writer.close();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

}
