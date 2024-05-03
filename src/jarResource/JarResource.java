package jarResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class JarResource {

	
	
	
	/**
	 * Get file from Jar file
	 * @param  path - path to file in Jar
	 * @return Returns File object
	 */
	public File getJarResource(String path) {
		File file = null;
		String resource = path;
		URL res = getClass().getResource(resource);
		try {
	        InputStream input = getClass().getResourceAsStream(resource);
	        file = File.createTempFile("tempfile", ".tmp");
	        OutputStream out = new FileOutputStream(file);
	        int read;
	        byte[] bytes = new byte[1024];

	        while ((read = input.read(bytes)) != -1) {
	            out.write(bytes, 0, read);
	        }
	        out.close();
	        file.deleteOnExit();
	        return file;
	    } catch (IOException ex) {
	    	
	    }
		
		
		
		return null;
	}
}
