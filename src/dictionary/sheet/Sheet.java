package dictionary.sheet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import dictionary.wordPair.WordPair;
import dictionary.xmlProperty.XMLProperty;

public class Sheet {
	private List<WordPair> wordPairList = new ArrayList<WordPair>();
	
	private int height = 1080;
	private int width = 1920;
	private int fontSize =  30;
	private int maxWordPairs = getHeight()/getFontSize()*2; 
	private int watermarkTransparency = 35;
	private int watermarkFontSize;
	private Color wordsColor;
	private Color watermarkColor;
	private String watermarkText;
	private String fontName = "Dialog";
	private String imageName = "";
	private Random rand = new Random();
	private XMLProperty xmlProperty = new XMLProperty("settings/settings.xml");
	
	public Sheet(List<WordPair> wordPairList, int width, int height, int fontSize, String fontName,  Color wordsColor, Color watermarkColor, String watermarkText ) {
		if(!xmlProperty.isInitialized())
			xmlProperty.init();
		
		this.setWordPairList(wordPairList);
		this.setWidth(width);
		this.setHeight(height);
		this.setFontSize(fontSize);
		this.setFontName(fontName);
		
		
		
		this.wordsColor = wordsColor;
		this.watermarkColor = watermarkColor;
		this.watermarkText = watermarkText;

		this.setWatermarkFontSize((int)(fontSize/1.5));
		this.watermarkColor = new Color(this.watermarkColor.getRed(), this.watermarkColor.getGreen(), this.watermarkColor.getBlue(), this.getWatermarkTransparency());
	}
	public Sheet(List<WordPair> wordPairList, int width, int height, int fontSize, String fontName ) {
		this(wordPairList, width, height, fontSize, fontName, Color.BLACK, new Color(238, 75, 43), "Made by dimitry_tar");
		
		
	}
	
	public Sheet(List<WordPair> wordPairList) throws Exception {
		if(!xmlProperty.isInitialized())
			xmlProperty.init();
		
		if(!xmlProperty.propertyExists("fontName") ||
		   !xmlProperty.propertyExists("fontSize") ||
		   !xmlProperty.propertyExists("watermarkColor") ||
		   !xmlProperty.propertyExists("wordsColor") ||
		   !xmlProperty.propertyExists("watermarkText") )
			{
				xmlProperty.setDefaultValues();
			}
		
		this.wordPairList = wordPairList;
		
		setFontName(xmlProperty.getValue("fontName"));
		setFontSize(Integer.valueOf(xmlProperty.getValue("fontSize")));

		String watermarkColorStringArray[] = xmlProperty.getValue("watermarkColor").split(",");
		setWatermarkColor(new Color(Integer.valueOf( watermarkColorStringArray[0]), // r
									Integer.valueOf( watermarkColorStringArray[1]), // g
									Integer.valueOf( watermarkColorStringArray[2]), // b
									this.watermarkTransparency));					// a  35 by default
		
		String wordsColorStringArray[] = xmlProperty.getValue("wordsColor").split(",");
		setWordsColor(new Color(Integer.valueOf( wordsColorStringArray[0]), // r
								Integer.valueOf( wordsColorStringArray[1]), // g
								Integer.valueOf( wordsColorStringArray[2]), // b
								Integer.valueOf( wordsColorStringArray[3])));//a  255 by default (not transparent)
		
		setWatermarkText(xmlProperty.getValue("watermarkText"));
		
		watermarkFontSize = (int) (fontSize/1.5);
		
		
	}
	
	
	
	
	
	
	private String createImageName() {
		return String.valueOf( LocalDate.now().getDayOfMonth() + ".") +
				String.valueOf( LocalDate.now().getMonthValue() + ".")+
				String.valueOf( LocalDate.now().getYear() + " ") +
				String.valueOf( LocalTime.now().getHour() + "-") +
				String.valueOf( LocalTime.now().getMinute() + "-" +
				String.valueOf( LocalTime.now().getSecond() + ".png"));
	}
	private void drawWatermark(Graphics g) {
		
		
		
		g.setFont(new Font(getFontName(), Font.ITALIC, getWatermarkFontSize()));
		g.setColor(watermarkColor);
		
		g.drawString(watermarkText, watermarkText.length(),  g.getFont().getSize()); //left top
		g.drawString(watermarkText, (int)Math.round((getWidth() - watermarkText.length())/2.5),  g.getFont().getSize()); //center top
		g.drawString(watermarkText, (getWidth()-watermarkText.length()*watermarkText.length()), g.getFont().getSize()); //right top
		
		g.drawString(watermarkText, watermarkText.length(), (getHeight()- g.getFont().getSize())/2); //left center
		g.drawString(watermarkText, (int)Math.round((getWidth() - watermarkText.length())/2.5), (getHeight()- g.getFont().getSize())/2); //center center
		g.drawString(watermarkText, (getWidth()-watermarkText.length()*watermarkText.length()), (getHeight()- g.getFont().getSize())/2); //right center

		g.drawString(watermarkText, watermarkText.length(), (getHeight()- g.getFont().getSize())); //left bottom
		g.drawString(watermarkText, (int)Math.round((getWidth() - watermarkText.length())/2.5), (getHeight()- g.getFont().getSize())); //bottom center
		g.drawString(watermarkText, (getWidth()-watermarkText.length()*watermarkText.length()), (getHeight()- g.getFont().getSize())); // right bottom
		g.dispose();
	}
	public BufferedImage drawWords(int imageWidth, int imageHeight, int fontSize, String fontName) {	

		BufferedImage image = new BufferedImage( imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Font font = new Font(fontName, Font.BOLD, fontSize);
		
		Graphics2D    graphics = image.createGraphics();
		graphics.setPaint ( new Color ( 255,255,255 ) );
		graphics.fillRect ( 0, 0, image.getWidth(), image.getHeight() ); //set image BG - White
		
		Graphics g = image.getGraphics();
		g.setFont(font); //set font for drawing string
		g.setColor(wordsColor);
		
		int xPos = fontSize;
		int yPos =  fontSize; //coordinates of strings to draw
		
		int count = 0; //iterator for wordPairList
		
			for(int j = 0; count < imageHeight/fontSize && count < getWordPairList().size(); j++) {
				g.drawString(getWordPairList().get(count).getWord() +
						" - " + 
						getWordPairList().get(count).getTranslation(), xPos, yPos);
				count++;
				yPos += fontSize;
			} //draw first column
			yPos = fontSize;
			xPos = (imageWidth -
					(getWordPairList().get(count-1).getWord().length()+getWordPairList().get(count-1).getTranslation().length()))/2;
			for(int k = 0; count-(imageHeight/fontSize) < imageHeight/fontSize && count < getWordPairList().size();  k++) {
				g.drawString(getWordPairList().get(count).getWord() +
						" - " + 
						getWordPairList().get(count).getTranslation(), xPos, yPos);
				yPos += fontSize;
				count++;
			} // draw second column			
		drawWatermark(g);
		g.dispose();
		
		
		
		return image;
	}
	
	
	public String getImageName() {
		return this.imageName;
	}
	public boolean compileImage() throws IOException {
		imageName = createImageName();
		BufferedImage image = drawWords(getWidth(), getHeight(), getFontSize(), imageName);
		return ImageIO.write(image, "png", new File(imageName));
	}
	
	
	public void setWordsColor(Color wordsColor) {
		this.wordsColor = wordsColor;
	}
	public Color getWordsColor() {
		return this.wordsColor;
	}
	
	public void setWatermarkColor(Color watermarkColor) {
		this.watermarkColor = watermarkColor;
	}
	public Color getWatermarkColor() {
		return this.watermarkColor;
	}
	
	public void setWatermarkText(String watermarkText) {
		this.watermarkText = watermarkText;
	}
	public String getWatermarkText() {
		return this.watermarkText;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public int getWatermarkFontSize() {
		return watermarkFontSize;
	}
	public void setWatermarkFontSize(int watermarkFontSize) {
		this.watermarkFontSize = watermarkFontSize;
	}
	
	public int getWatermarkTransparency() {
		return watermarkTransparency;
	}
	
	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	
	public List<WordPair> getWordPairList() {
		return wordPairList;
	}
	public void setWordPairList(List<WordPair> wordPairList) {
		this.wordPairList = wordPairList;
	}
	private void setHeight(int height) {
		this.height = height;
	}
	private void setWidth(int width) {
		this.width = width;
	}
	
	
}
