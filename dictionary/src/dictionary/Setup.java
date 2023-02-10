package dictionary;
import java.io.EOFException;
import java.io.IOException;

import serialization.*;
import wordPair.*;
public class Setup {
	public static void main(String[] args) throws ClassNotFoundException, IOException  {

		
		ReadObject ro = new ReadObject(".//data.dat");
		
		while(true) {
			 
			 try {
		           
				 System.out.println((WordPair)ro.read());

		        } catch (EOFException e) {
		        	ro.close();
		        	return;		            
		        }
		}
		
	}

	
	
}