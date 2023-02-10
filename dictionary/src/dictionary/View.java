package dictionary;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.imageio.ImageIO;
import javax.lang.model.element.NestingKind;
import javax.print.event.PrintJobAttributeEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyledEditorKit.FontSizeAction;
import javax.swing.tree.FixedHeightLayoutCache;

import wordPair.WordPair;

import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.time.*;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ImageIcon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;

public class View {

	private JFrame frame;
	private JTextField word;
	private JTextField translation;
	private List<WordPair> wordPairList = new ArrayList();
	DefaultListModel listModel = new DefaultListModel();
	private static int height = 800;
	private static int width = 1920;
	private static int fontSize =  30;
	private static String fontName = "Dialog";
	private static int maxWordPairs = height/fontSize*2;
	private static String title = "Dictionary Printer";
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					View window = new View();
					window.frame.setVisible(true);
					window.frame.setTitle(title);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public View() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 536, 345);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel printPanel = new JPanel();
		printPanel.setBounds(0, 0, 520, 158);
		frame.getContentPane().add(printPanel);
		printPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 500, 84);
		printPanel.add(scrollPane);
		
		JList wordPairs = new JList();
		scrollPane.setViewportView(wordPairs);
		wordPairs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		wordPairs.setModel(new AbstractListModel() {
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		JLabel wordPairsCount = new JLabel("WordPairs: 0");
		wordPairsCount.setHorizontalAlignment(SwingConstants.CENTER);
		wordPairsCount.setBounds(421, 106, 89, 14);
		printPanel.add(wordPairsCount);
		
		JButton printBtn = new JButton("Print");
		printBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(wordPairs.getModel().getSize() <= 0)
					return;
				
					try {
						String path = createImageName();
						BufferedImage image = drawWords(width, height, fontSize, fontName);
						compileImage(image, path);						
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				
				
				
			}
		});
		printBtn.setBounds(218, 102, 89, 33);
		printPanel.add(printBtn);
		
		JButton deletePairButton = new JButton("");
		deletePairButton.setIcon(new ImageIcon(View.class.getResource("/icons/delete (1).png")));
		deletePairButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(wordPairs.getSelectedValue() == null)
					return;
				String value = String.valueOf(wordPairs.getSelectedValue());
				listModel.removeElement(wordPairs.getSelectedValue());
				for(int i = 0; i < wordPairList.size(); i++) {
					if(wordPairList.get(i).toString().equals(value))
						{
						wordPairList.remove(wordPairList.get(i));
						}
				}
				wordPairs.setModel(listModel);
				wordPairsCount.setText("WordPairs: " + wordPairList.size());
			}
		});
		deletePairButton.setBounds(151, 102, 45, 33);
		printPanel.add(deletePairButton);
		
		JButton clearAllButton = new JButton("Clear");
		clearAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listModel.removeAllElements();
				wordPairs.setModel(listModel);
				wordPairList.clear();
				wordPairsCount.setText("WordPairs: " + wordPairList.size());
			}
		});
		clearAllButton.setBounds(63, 102, 64, 33);
		printPanel.add(clearAllButton);
		
		
		
		JPanel wordPairPanel = new JPanel();
		wordPairPanel.setBounds(0, 177, 520, 119);
		frame.getContentPane().add(wordPairPanel);
		wordPairPanel.setLayout(null);
		
		JPanel translationPanel = new JPanel();
		translationPanel.setLayout(null);
		translationPanel.setBounds(258, 11, 229, 58);
		wordPairPanel.add(translationPanel);
		
		translation = new JTextField();
		translation.setColumns(10);
		translation.setBounds(36, 27, 162, 31);
		translationPanel.add(translation);
		
		JLabel translationLabel = new JLabel("Translation");
		translationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		translationLabel.setBounds(78, 11, 70, 14);
		translationPanel.add(translationLabel);
		
		JPanel wordPanel = new JPanel();
		wordPanel.setBounds(45, 11, 203, 58);
		wordPairPanel.add(wordPanel);
		wordPanel.setLayout(null);
		
		word = new JTextField();
		word.setBounds(21, 27, 144, 31);
		wordPanel.add(word);
		word.setColumns(10);
		
		JLabel wordLabel = new JLabel("Word");
		wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wordLabel.setBounds(75, 11, 39, 14);
		wordPanel.add(wordLabel);
		
		JButton addWordPair = new JButton("Add");
		addWordPair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(wordLabel.getText().length() <= 0 || translation.getText().length() <= 0 )
					return;
				if(wordPairList.size() >= (height/fontSize)*2) {
					JOptionPane.showMessageDialog(printPanel,"You reached the limit of word pairs - " + String.valueOf((height/fontSize )*2), "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				WordPair pair = new WordPair(word.getText(), translation.getText());
				wordPairList.add(pair);
				listModel.addElement(pair.toString());
				wordPairs.setModel(listModel);
				//UUIDsField.setText(UUIDsField.getText() + pair.getUUID() + "\n");
				wordPairsCount.setText("WordPairs: " + wordPairList.size());
			}
		});
		addWordPair.setBounds(209, 80, 89, 39);
		wordPairPanel.add(addWordPair);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 164, 520, 2);
		frame.getContentPane().add(separator);
	}
	
	public BufferedImage drawWords(int imageWidth, int imageHeight, int fontSize, String fontName) {		
		BufferedImage image = new BufferedImage( imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Font font = new Font(fontName, Font.BOLD, fontSize);
		
		Graphics2D    graphics = image.createGraphics();
		graphics.setPaint ( new Color ( 255,255,255 ) );
		graphics.fillRect ( 0, 0, image.getWidth(), image.getHeight() ); //set image BG - White
		
		Graphics g = image.getGraphics();
		g.setFont(font); //set font for drawing string
		g.setColor(Color.BLACK);
		
		int xPos = fontSize;
		int yPos =  fontSize; //coordinates of strings to draw
		
		int count = 0; //iterator for wordPairList
		
			for(int j = 0; count < imageHeight/fontSize && count < wordPairList.size(); j++) {
				g.drawString(wordPairList.get(count).getWord() +
						" - " + 
						wordPairList.get(count).getTranslation(), xPos, yPos);
				count++;
				yPos += fontSize;
			} //draw first column
			yPos = fontSize;
			xPos = (imageWidth -
					(wordPairList.get(count-1).getWord().length()+wordPairList.get(count-1).getTranslation().length()))/2;
			for(int k = 0; count-(imageHeight/fontSize) < imageHeight/fontSize && count < wordPairList.size();  k++) {
				g.drawString(wordPairList.get(count).getWord() +
						" - " + 
						wordPairList.get(count).getTranslation(), xPos, yPos);
				yPos += fontSize;
				count++;
			} // draw second column			
		drawWatermark(g);
		g.dispose();
		
		
		
		return image;
	}
	public boolean compileImage(BufferedImage image, String path) throws IOException {
		return ImageIO.write(image, "png", new File(path));
	}
	public String createImageName() {
		return String.valueOf( LocalDate.now().getDayOfMonth() + ".") +
				String.valueOf( LocalDate.now().getMonthValue() + ".")+
				String.valueOf( LocalDate.now().getYear() + " ") +
				String.valueOf( LocalTime.now().getHour() + "-") +
				String.valueOf( LocalTime.now().getMinute() + "-" +
				String.valueOf( LocalTime.now().getSecond() + ".png"));
	}
	public void drawWatermark(Graphics g) {
		String text = "Made by dimitry_tar";
		g.setFont(new Font(fontName, Font.ITALIC, fontSize));
		g.setColor(new Color(238, 75, 43,35));
		
		g.drawString(text, text.length(), fontSize); //left top
		g.drawString(text, (int)Math.round((width - text.length())/2.5), fontSize); //center top
		g.drawString(text, (width-text.length()*text.length()), fontSize); //right top
		
		g.drawString(text, text.length(), (height-fontSize)/2); //left center
		g.drawString(text, (int)Math.round((width - text.length())/2.5), (height-fontSize)/2); //center center
		g.drawString(text, (width-text.length()*text.length()), (height-fontSize)/2); //right center

		g.drawString(text, text.length(), (height-fontSize)); //left bottom
		g.drawString(text, (int)Math.round((width - text.length())/2.5), (height-fontSize)); //bottom center
		g.drawString(text, (width-text.length()*text.length()), (height-fontSize)); // right bottom
		g.dispose();
	}
}
