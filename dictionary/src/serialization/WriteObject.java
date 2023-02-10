package serialization;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class WriteObject {
	private FileOutputStream fos;
	private ObjectOutputStream oos;
	
	public WriteObject(String path) {
		try {
			File file = new File(path);
			if(!file.exists()) 
				file.createNewFile();
			
			
			boolean hasData = file.length() > 0;

			fos = new FileOutputStream(path, true);
			oos = hasData ? new ObjectOutputStream(fos) {
				protected void writeStreamHeader() throws IOException {
					System.out.print(1);
					reset();
				}
			} : new ObjectOutputStream(fos);
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void write(Object obj) {
		try {
			oos.writeObject(obj);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void close() {
		try {
			fos.close();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
