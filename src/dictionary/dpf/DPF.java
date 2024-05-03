package dictionary.dpf;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import dictionary.wordPair.WordPair;
import serialization.WriteObject;

public class DPF {
	private List<WordPair> wordPairList = new ArrayList();	
	private String fileName;
	
	public DPF(List<WordPair> wordPairList) {
		this.wordPairList = wordPairList;
	}
	
	
	
	
	
	
	
	private String createDPFName() {
		return String.valueOf( LocalDate.now().getDayOfMonth() + ".") +
				String.valueOf( LocalDate.now().getMonthValue() + ".")+
				String.valueOf( LocalDate.now().getYear() + " ") +
				String.valueOf( LocalTime.now().getHour() + "-") +
				String.valueOf( LocalTime.now().getMinute() + "-" + 
				String.valueOf( LocalTime.now().getSecond() + ".dpf"));
	}
	
	
	public String getFileName() {
		return fileName;
	}
	public boolean compileDPF() {
		fileName = createDPFName();
		WriteObject wo = new WriteObject(fileName);
		
		File dpf = new File(fileName);
		
		try {
			
			dpf.createNewFile();
			for(WordPair pair : wordPairList) {
				wo.write(pair);
			}
			wo.close();
		} catch (IOException e) {
			wo.close();
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
