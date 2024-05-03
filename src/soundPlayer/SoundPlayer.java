package soundPlayer;

import java.io.File;
import java.io.FileInputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class SoundPlayer {
    private File file;          
    
    public SoundPlayer(File file) {
    	this.file = file;
    	
    }
    public SoundPlayer() { this(null); }
    
    
    
    public void setFile(File file) {
    	this.file = file;
    }
    public void play() {
		Thread soundThread = new Thread(() -> {  
			try{
			    FileInputStream fis = new FileInputStream(file.getPath());
			    Player playMP3 = new Player(fis);
			    playMP3.play();
			}
			catch(Exception exc){
			    exc.printStackTrace();
			    System.out.println("Failed to play the file.");
			}
		});
		soundThread.start();
		
	}
    

    @Override
    public String toString() {
    	return "Current song - " + file.getPath();
    }
}
