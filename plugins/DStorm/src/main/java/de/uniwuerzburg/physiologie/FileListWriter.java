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
		try {
			writer = new FileWriter(file);
			writer.write("filename , ");
			writer.write("scandirection, ");
			writer.write("breakFrame \n" );
			int i =0;
			for( i = 0; i < listI.size(); i++){
				
					
					writer.write(listS.get(i) + ", ");
					writer.write(listD.get(i) + ", ");
					writer.write(listI.get(i)+"\n" );
			}
			writer.close();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

}