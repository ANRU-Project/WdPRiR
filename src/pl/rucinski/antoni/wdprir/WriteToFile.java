package pl.rucinski.antoni.wdprir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class WriteToFile {
	
	public String filePath;
	public WriteToFile( String filePath){
		this.filePath = filePath;
		
	}

	public void writeToFile(double B, double spin) throws IOException {
		
		
		File file = new File(filePath);
		
		if(file.exists() == false) {
			FileWriter fr = new FileWriter(file, true);
			
			BufferedWriter br = new BufferedWriter(fr);
			br.write("B" + "\t"+"avgSpin"+"\n");
			br.close();
			fr.close();
		}
		FileWriter fr = new FileWriter(file, true);
		BufferedWriter br = new BufferedWriter(fr);
		
		br.write(Double.toString(B)+ "\t" + Double.toString(spin) + "\n");
		
		br.close();
		fr.close();
	
	}

}
