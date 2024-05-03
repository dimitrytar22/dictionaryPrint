package dictionary.xmlProperty;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import jarResource.JarResource;


public class XMLProperty {
	private Properties properties = null;
	private String path = "settings/settings.xml";
	private Set<Object> keys;
	private JarResource jarResource = new JarResource();
	
	public XMLProperty(String path) {
		this.path = path;
	}
	
	public void init() {
		
		
		try {
			properties = new Properties();
			InputStream is = null;
			
			if(!new File("settings").exists())
				new File("settings").mkdirs();
			
			File settingsFile = new File(path);
			
			if(!settingsFile.exists()) {
				settingsFile.createNewFile();
				setDefaultValues();
			}
			
			is =  (InputStream)new FileInputStream(settingsFile);
			
			try {
				properties.loadFromXML(is);
				
				if(properties.isEmpty())
					setDefaultValues();
			} catch (Exception e) {
				settingsFile.delete();
				settingsFile.createNewFile();
				setDefaultValues();	
				
				is =  (InputStream)new FileInputStream(settingsFile);
				properties.loadFromXML(is);
			}
			
		   } catch (IOException e1) {
			   e1.printStackTrace();
		   }
	}
	
	
	
	public  boolean writeProperty(String key, String value) {
		Properties newProp = new Properties();
		Set<Object> keys = getKeys();
		try {
			for(Object k : keys) {
				newProp.setProperty(k.toString(), getValue(k.toString()));
			}
			newProp.setProperty(key, value);
			newProp.storeToXML(new FileOutputStream(path), null);
			properties.clear();
			properties = newProp;
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isInitialized() {
		return properties != null;
	}
	
	public void clearProperties() {
		properties.clear();
	}
	
	public Set<Object> getKeys(){
		return properties.keySet();
	}
	public String getValue(String key) {
		return properties.getProperty(key);
	}
	public boolean isEmpty() {
		return properties.isEmpty();
	}
	
	public boolean propertyExists(String key) {
		try {
			return properties.getProperty(key) != null;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void setDefaultValues() {
		properties.setProperty("fontName", "Dialog");
		properties.setProperty("fontSize", "30");
		properties.setProperty("wordsColor", "0,0,0,255");
		properties.setProperty("watermarkColor", "255,0,0,35");
		properties.setProperty("watermarkText", "watermark");
		properties.setProperty("theme", "dark");
		
//		properties.setProperty("contentPanel", "255,255,255,255");
//		properties.setProperty("buttonPane", "255,255,255,255");
		
		//default theme - gray
		properties.setProperty("theme", "gray");
		
		// main window element's colors
		properties.setProperty("mainContentPaneColor","128,128,128,255");
		properties.setProperty("mainPrintPanelColor","128,128,128,255");
		properties.setProperty("mainWordPairPanelColor", "128,128,128,255");
		properties.setProperty("mainMenuBarColor", "192,192,192,255");
		properties.setProperty("mainWordPairsListColor", "192,192,192,255");
		properties.setProperty("mainClearAllButtonColor", "192,192,192,255");
		properties.setProperty("mainPrintButtonColor", "192,192,192,255");
		properties.setProperty("mainRemovePairButtonColor", "192,192,192,255");
		properties.setProperty("mainWordPairsListSelectionColor", "192,192,192,255");
		properties.setProperty("mainWordPanelColor", "128,128,128,255");
		properties.setProperty("mainTranslationPanelColor", "128,128,128,255");
		properties.setProperty("mainWordFieldColor", "192,192,192,255");
		properties.setProperty("mainTranslationFieldColor", "192,192,192,255");
		properties.setProperty("mainWordLanguageColor", "192,192,192,255");
		properties.setProperty("mainTranslationLanguageColor", "192,192,192,255");
		properties.setProperty("mainAddWordPairColor", "192,192,192,255");
		properties.setProperty("mainSeparatorColor", "1,1,1,255");
		
		
		
		
		// export dialog element's colors
		properties.setProperty("exportDialogContentPanelColor", "192,192,192,255");
		properties.setProperty("exportDialogButtonPaneColor", "192,192,192,255");

		
		try {
			properties.storeToXML(new FileOutputStream(path), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/***
	 * Writes property like 128,128,128,255 [r,g,b,a] from Color object
	 * @param key - Property key in file
	 * @param color - Color object to write
	 * @return Success of operation
	 */
	public boolean writeColorProperty(String key, Color color) {
		Color fixedColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()){
			@Override
			public String toString() {
				
				return this.getRed() + "," + this.getGreen() + "," + this.getBlue() + "," + this.getAlpha();
			}
		};
		return writeProperty(key, fixedColor.toString()) ;
	}
	
	/***
	 * Returns Color object from properties file
	 * @param key - Property key in file
	 * @return Color rgba
	 */
	public Color getColorValue(String key) {
		String contentPaneColorString[] = getValue(key).split(",");
		return new Color(
				Integer.valueOf(contentPaneColorString[0]),
				Integer.valueOf(contentPaneColorString[1]),
				Integer.valueOf(contentPaneColorString[2]),
				Integer.valueOf(contentPaneColorString[3]));
	}
	
}
