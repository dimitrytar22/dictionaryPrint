package fileTypeFilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FileTypeFilter extends FileFilter{
	private String extension;
	private String description;
	
	public FileTypeFilter(String extension, String description){
		this.extension = extension;
		this.description = description;
	}
	
	@Override
	public boolean accept(File f) {
		if(f.isDirectory())
			return true;
		return f.getName().endsWith(extension);
	}
	
	@Override
	public String getDescription() {
		return description + "(*" + extension + ")";
	}
}
