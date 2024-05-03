package serialization;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ReadObject {
	private FileInputStream fis;
	private ObjectInputStream ois;
	private String filePath;
	
	public ReadObject(String path) {
		try {
			this.filePath = path;
			fis = new FileInputStream(this.filePath);
			ois = new ObjectInputStream(fis);
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Object read() throws ClassNotFoundException, IOException {
		Object obj = null;
				obj = ois.readObject();
			
			return obj;

	}
	public void close() {
		try {
			fis.close();
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String getFilePath() {
		return this.filePath;
	}
}
