package serialization;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ReadObject {
	private FileInputStream fis;
	private ObjectInputStream ois;
	
	public ReadObject(String path) {
		try {
			fis = new FileInputStream(path);
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
	
}
