package dictionary;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.goxr3plus.speech.translator.GoogleTranslate;

import dictionary.dpf.DPF;
import dictionary.item.Item;
import dictionary.sheet.Sheet;
import dictionary.wordPair.WordPair;
import dictionary.xmlProperty.XMLProperty;
import fileTypeFilter.FileTypeFilter;
import jarResource.JarResource;
import serialization.ReadObject;
import soundPlayer.SoundPlayer;


 
public class View {

	private JFrame frame;
	private JTextField wordField;
	private JTextField translationField;
	private List<WordPair> wordPairList = new ArrayList();
	DefaultListModel<String> listModel = new DefaultListModel<String>();
	private static int height = 1080;
	private static int width = 1920;
	private static int fontSize =  30;
	private static String fontName = "Dialog";
	private static int maxWordPairs = height/fontSize*2;
	private static String title = "Dictionary Printer";
	private String editingPair = ".";
	private JButton addWordPair = new JButton("Add");
	private JarResource jarResource = new JarResource();
	private Sheet sheet;
	private XMLProperty xmlProperty = new XMLProperty("settings/settings.xml");
	
	Random rand = new Random();
	SoundPlayer soundPlayer = new SoundPlayer();
	
	String translationn = "";
	private JList wordPairsList;
	private JMenu settingsMenu;
	

	
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
		frame.addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent e) {
			}
			public void windowLostFocus(WindowEvent e) {
			}
		});
		frame.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
								
				
				
			}
		});
		
		
		
		if(!xmlProperty.isInitialized())
			xmlProperty.init();
		
		try {
			sheet = new Sheet(wordPairList);
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		
		soundPlayer.setFile(jarResource.getJarResource("/sounds/interface/load_player.mp3"));
		soundPlayer.play();
		
		frame.getContentPane().setBackground(Color.GRAY);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(View.class.getResource("/icons/logo.png")));
		frame.setResizable(false);
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		JPanel printPanel = new JPanel();
		printPanel.setBackground(Color.GRAY);
		printPanel.setBounds(10, 11, 764, 337);
		frame.getContentPane().add(printPanel);
		printPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setBounds(0, 0, 764, 270);
		printPanel.add(scrollPane);
		
		
		wordPairsList = new JList();
		wordPairsList.setFont(new Font("Consolas", Font.BOLD, 12));
		wordPairsList.setForeground(Color.BLACK);
		wordPairsList.setSelectionBackground(Color.LIGHT_GRAY);
		wordPairsList.setSelectionForeground(Color.RED);
		wordPairsList.setBorder(null);
		wordPairsList.setBackground(Color.LIGHT_GRAY);
		
		scrollPane.setViewportView(wordPairsList);
		wordPairsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		wordPairsList.setModel(new AbstractListModel() {
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		JLabel wordPairsCount = new JLabel("WordPairs: 0");
		wordPairsCount.setForeground(Color.BLACK);
		wordPairsCount.setHorizontalAlignment(SwingConstants.CENTER);
		wordPairsCount.setBounds(675, 281, 89, 14);
		printPanel.add(wordPairsCount);
		
		JButton printButton = new JButton("Print");
		printButton.setFocusPainted(false);
		printButton.setForeground(Color.BLACK);
		printButton.setBackground(Color.LIGHT_GRAY);
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(wordPairsList.getModel().getSize() <= 0)
					return;
				try {
					
					Sheet sheet = new Sheet(wordPairList);
					//sheet.setWordPairList(wordPairList);
					//sheet.setFontName("Dialog");
					
					sheet.compileImage();
					
					DPF dpf = new DPF(wordPairList);
					dpf.compileDPF();

					soundPlayer.setFile(jarResource.getJarResource("/sounds/interface/print.mp3"));
					soundPlayer.play();
					} catch (Exception e1) {
						return;
					}
				
				
				
			}
		});
		printButton.setBounds(337, 282, 89, 45);
		printPanel.add(printButton);
		
		JButton removePairButton = new JButton("Remove");
		removePairButton.setEnabled(true);
		removePairButton.setFocusPainted(false);
		removePairButton.setForeground(Color.BLACK);
		removePairButton.setBackground(Color.LIGHT_GRAY);
		removePairButton.setIcon(null);
		removePairButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(wordPairsList.getSelectedValue() == null)
					return;
				String value = String.valueOf(wordPairsList.getSelectedValue());
				listModel.removeElement(wordPairsList.getSelectedValue());
				for(int i = 0; i < wordPairList.size(); i++) {
					if(wordPairList.get(i).toString().equals(value))
						{
						wordPairList.remove(wordPairList.get(i));
						}
				}
				soundPlayer.setFile(jarResource.getJarResource("/sounds/interface/delete.mp3"));
				soundPlayer.play();
				resetWords(wordPairsList, listModel, wordPairsCount);
				resetEditing(addWordPair);
				
			}
		});
		removePairButton.setBounds(472, 282, 89, 45);
		printPanel.add(removePairButton);
		
		JButton clearAllButton = new JButton("Clear all");
		clearAllButton.setFocusPainted(false);
		clearAllButton.setForeground(Color.BLACK);
		clearAllButton.setBackground(Color.LIGHT_GRAY);
		clearAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(wordPairsList.getModel().getSize() <= 0)
					return;
				
				wordPairList.clear();
				listModel = createDefaultListModelFromList();
				wordPairsList.setModel(listModel);
				soundPlayer.setFile(jarResource.getJarResource("/sounds/interface/clear.mp3"));
				soundPlayer.play();
				wordPairsCount.setText("WordPairs: " + wordPairList.size());
				resetEditing(addWordPair);
				
				
			}
		});
		clearAllButton.setBounds(204, 281, 89, 45);
		printPanel.add(clearAllButton);
		
		
		
		JPanel wordPairPanel = new JPanel();
		wordPairPanel.setBackground(Color.GRAY);
		wordPairPanel.setBounds(0, 368, 784, 172);
		frame.getContentPane().add(wordPairPanel);
		wordPairPanel.setLayout(null);
		
		JPanel translationPanel = new JPanel();
		translationPanel.setBackground(Color.GRAY);
		translationPanel.setLayout(null);
		translationPanel.setBounds(406, 12, 300, 100);
		wordPairPanel.add(translationPanel);
		
		translationField = new JTextField();
		translationField.setBorder(null);
		translationField.setFont(new Font("Consolas", Font.BOLD, 12));
		translationField.setForeground(Color.BLACK);
		translationField.setBackground(Color.LIGHT_GRAY);
		translationField.setColumns(10);
		translationField.setBounds(12, 43, 280, 31);
		translationPanel.add(translationField);
		
		JLabel translationLabel = new JLabel("Translation");
		translationLabel.setForeground(Color.BLACK);
		translationLabel.setBackground(Color.LIGHT_GRAY);
		translationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		translationLabel.setBounds(106, 11, 87, 27);
		translationPanel.add(translationLabel);
		
		JComboBox translationLanguage = new JComboBox();
		
		translationLanguage.addItem(new Item("Russian", "ru"));
		translationLanguage.addItem(new Item("Ukrainian", "uk"));
		translationLanguage.addItem(new Item("English", "en"));
		
		translationLanguage.setForeground(Color.BLACK);
		translationLanguage.setBackground(Color.LIGHT_GRAY);
		translationLanguage.setBounds(196, 75, 96, 25);
		translationPanel.add(translationLanguage);
		
		JPanel wordPanel = new JPanel();
		wordPanel.setBackground(Color.GRAY);
		wordPanel.setBounds(78, 12, 300, 100);
		wordPairPanel.add(wordPanel);
		wordPanel.setLayout(null);
		
		wordField = new JTextField();
		wordField.setBorder(null);
		wordField.setForeground(Color.BLACK);
		wordField.setFont(new Font("Consolas", Font.BOLD, 12));
		wordField.setBackground(Color.LIGHT_GRAY);
		
		wordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {

				
			}
		});
		
		
		wordField.setBounds(12, 43, 280, 31);
		wordPanel.add(wordField);
		wordField.setColumns(10);
//=========================================================================		
		JLabel errorConnection = new JLabel("No connection!");
		errorConnection.setVisible(false);
		errorConnection.setHorizontalAlignment(SwingConstants.CENTER);
		errorConnection.setFont(new Font("Consolas", Font.BOLD, 12));
		errorConnection.setForeground(Color.RED);
		errorConnection.setBounds(597, 114, 151, 42);
		wordPairPanel.add(errorConnection);
//=========================================================================				
		JLabel wordLabel = new JLabel("Word");
		wordLabel.setForeground(Color.BLACK);
		wordLabel.setBackground(Color.LIGHT_GRAY);
		wordLabel.setBounds(119, 11, 61, 27);
		wordPanel.add(wordLabel);
		wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
//=========================================================================		
		JProgressBar translateProgress = new JProgressBar();
		translateProgress.setBorderPainted(false);
		translateProgress.setBorder(null);
		translateProgress.setOpaque(false);
		translateProgress.setBounds(185, -3, 55, 14);	
//=========================================================================				
		JComboBox wordLanguage = new JComboBox();
		wordLanguage.setFocusTraversalKeysEnabled(false);
		wordLanguage.setBorder(null);
		wordLanguage.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				
	                if(e.getStateChange() == e.SELECTED)
	                {			
	                	
	                }
	            
			}
		});
		
		wordLanguage.addItem(new Item("English", "en"));
		wordLanguage.addItem(new Item("Russian", "ru"));
		wordLanguage.addItem(new Item("Ukrainian", "uk"));
//=========================================================================		
		JButton translateButton = new JButton("");
		translateButton.setFocusPainted(false);
		translateButton.setBorderPainted(false);
		translateButton.setBorder(null);
		translateButton.setOpaque(false);
		translateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(wordField.getText().length() <= 0)
					return;
				
					Thread translateRequest = new Thread(new Runnable() {
						
						@Override
						public void run() {
							
							try {
								for(int i = 0; i < translateProgress.getMaximum()/2; i++) {
									translateProgress.setValue(++i);
									Thread.sleep(1);
								}
								
								
								try {
									translationField.setText( 
											GoogleTranslate.translate( 
													((Item)wordLanguage.getSelectedItem()).getDescription(),
													((Item)translationLanguage.getSelectedItem()).getDescription(),
													wordField.getText() 
											)
									);

								} catch (SocketTimeoutException | UnknownHostException e2) {
									Thread error = new Thread(new Runnable() {
										
										@Override
										public void run() {
											errorConnection.setVisible(true);
											try {
												Thread.sleep(2000);
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
											errorConnection.setVisible(false);
											}
										});	
									error.start();
									}
								
							} catch (IOException | InterruptedException e) {
								
								e.printStackTrace();
							}finally {
								for(int i = 0; i < translateProgress.getMaximum(); i++) {
									translateProgress.setValue(++i);
									try {
										Thread.sleep(1);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
								translateProgress.setValue(0);
							}
							
						}
					});
					translateRequest.start();
			}
		});
		translateButton.setBackground(Color.LIGHT_GRAY);
		translateButton.setIcon(new ImageIcon(View.class.getResource("/icons/translation.png")));
		translateButton.setBounds(198, 11, 30, 30);
		wordPanel.add(translateButton);
		
		wordPanel.add(translateProgress);
		
		
		
		
		
		wordLanguage.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		wordLanguage.setBackground(Color.LIGHT_GRAY);
		wordLanguage.setForeground(Color.BLACK);
		wordLanguage.setBounds(12, 75, 96, 25);
		wordPanel.add(wordLanguage);
		addWordPair.setFocusPainted(false);
		addWordPair.setPressedIcon(null);
		addWordPair.setForeground(Color.BLACK);
		addWordPair.setBackground(Color.LIGHT_GRAY);
		
		
		addWordPair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(wordField.getText().length() <= 0 || translationField.getText().length() <= 0 )
					return;
				if(editingPair != null && editingPair.length() > 1) {   // if editing
					String UUIDPairToEdit = findPairByUUIDgInList(editingPair).getUUID();
					UUIDPairToEdit = UUIDPairToEdit;
					findPairByUUIDgInList(UUIDPairToEdit).changePair(wordField.getText(), translationField.getText());
					wordField.setText("");
					translationField.setText("");
					
					listModel = createDefaultListModelFromList();
					wordPairsList.setModel(listModel);
					
					soundPlayer.setFile(jarResource.getJarResource("/sounds/interface/add.mp3"));
					soundPlayer.play();
					
					resetEditing(addWordPair);
					
					
					return;
				}
				
				if(wordPairList.size() >= (height/fontSize)*2) {   //word pairs limit
					JOptionPane.showMessageDialog(printPanel,"You reached the limit of word pairs - " + String.valueOf((height/fontSize )*2), "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				WordPair pair = new WordPair(wordField.getText(), translationField.getText());
				wordPairList.add(pair);
				listModel.addElement(pair.toString());
				wordPairsList.setModel(listModel);
				wordPairsCount.setText("WordPairs: " + wordPairList.size());
				
				soundPlayer.setFile(jarResource.getJarResource("/sounds/interface/add.mp3"));
				soundPlayer.play();
				wordField.setText("");
				translationField.setText("");
			}
		});
		
		wordPairsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				
				if(e.getValueIsAdjusting()) {
					soundPlayer.setFile(jarResource.getJarResource("/sounds/interface/item_selected.mp3"));
					soundPlayer.play();
				
					WordPair selectedPair = findPairByToStringInList((String)wordPairsList.getSelectedValue());
					wordField.setText(selectedPair.getWord());
					translationField.setText(selectedPair.getTranslation());
					
					if(selectedPair.getUUID() == null) {
						resetEditing(addWordPair);
						editingPair = ".";
						return;
					}
					editingPair = selectedPair.getUUID();
					addWordPair.setText("Edit");
					
				}
			}
		});
		
		addWordPair.setBounds(308, 113, 168, 45);
		wordPairPanel.add(addWordPair);
		
		
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.DARK_GRAY);
		separator.setBackground(Color.BLACK);
		separator.setBounds(-16, 355, 810, 2);
		frame.getContentPane().add(separator);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorder(null);
		menuBar.setBorderPainted(false);
		menuBar.setBackground(Color.LIGHT_GRAY);
		menuBar.setForeground(Color.BLACK);
		frame.setJMenuBar(menuBar);
		
		
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setBorderPainted(false);
		fileMenu.setBackground(new Color(238, 238, 238));
		fileMenu.setForeground(Color.BLACK);
		menuBar.add(fileMenu);
		
		JMenuItem openFileItem = new JMenuItem("Open file");
		openFileItem.setBorderPainted(false);
		openFileItem.setBackground(new Color(238, 238, 238));
		openFileItem.setForeground(Color.BLACK);
		openFileItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setApproveButtonText("Open file");
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
				fc.setMultiSelectionEnabled(false);
				
				FileTypeFilter dpfFilter = new FileTypeFilter(".dpf", "Dictionary Printer File");
				fc.addChoosableFileFilter(dpfFilter);   
				fc.setAcceptAllFileFilterUsed(false);
				
				File fileForOpen;
				
				if(fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					fileForOpen = fc.getSelectedFile();
					openFile(fileForOpen);
					resetWords(wordPairsList, listModel, wordPairsCount);
					soundPlayer.setFile(jarResource.getJarResource("/sounds/interface/fileopened.mp3"));
					soundPlayer.play();
				}else {
					return;
				}
			}
		});
		fileMenu.add(openFileItem);
		
		JMenuItem jokeItem = new JMenuItem("???");
		jokeItem.setBorderPainted(false);
		jokeItem.setBackground(new Color(238, 238, 238));
		jokeItem.setForeground(Color.BLACK);
		jokeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				File sound = jarResource.getJarResource("/jokesounds/" + (  (int)(Math.floor((Math.random()*10))+1)   ) + ".mp3");
				System.out.println(sound.getPath());
				soundPlayer.setFile(sound);
				soundPlayer.play();
			}
		});
		
		
		JMenuItem exportItem = new JMenuItem("Export");
		exportItem.setForeground(Color.BLACK);
		exportItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExportDialog dialog = new ExportDialog();
				dialog.showDialog();
			}
		});
		fileMenu.add(exportItem);
		fileMenu.add(jokeItem);
		
		settingsMenu = new JMenu("Settings");
		settingsMenu.setBorderPainted(false);
		settingsMenu.setBackground(Color.LIGHT_GRAY);
		settingsMenu.setForeground(Color.BLACK);
		menuBar.add(settingsMenu);
		
		JMenu sheetMenu = new JMenu("Sheet");
		sheetMenu.setForeground(Color.BLACK);
		settingsMenu.add(sheetMenu);
		
		JMenu wordsMenu = new JMenu("Words");
		wordsMenu.setForeground(Color.BLACK);
		sheetMenu.add(wordsMenu);
		
		JMenuItem wordsFontColorItem = new JMenuItem("Font Color");
		wordsFontColorItem.setForeground(Color.BLACK);
		wordsFontColorItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Color tempColor = JColorChooser.showDialog(null, "Choose a color for words", sheet.getWordsColor()); //cancel = null
				
				if(tempColor == null)
					return;
				
				Color color =  new Color(tempColor.getRed(), tempColor.getGreen(), tempColor.getBlue()) {
					@Override
					public String toString() {
						
						return this.getRed() + "," + this.getGreen() + "," + this.getBlue() + "," + this.getAlpha();
					}
				};
				if(tempColor == null)
					return;
				
				xmlProperty.writeProperty("wordsColor", color.toString());
				sheet.setWordsColor(color);
			}
		});
		wordsMenu.add(wordsFontColorItem);
		
		JMenuItem wordsFontSizeItem = new JMenuItem("Font Size");
		wordsFontSizeItem.setForeground(Color.BLACK);
		wordsFontSizeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = JOptionPane.showInputDialog(null, "Enter font size for words", "Words font size", JOptionPane.QUESTION_MESSAGE);
				if(input == null || input.equals("") ) {
					return;
				}
				
				fontSize = Integer.valueOf(input);
				xmlProperty.writeProperty("fontSize", String.valueOf(fontSize));
			}
		});
		wordsMenu.add(wordsFontSizeItem);
		
		JMenu watermarkMenu = new JMenu("Watermark");
		watermarkMenu.setForeground(Color.BLACK);
		sheetMenu.add(watermarkMenu);
		
		JMenuItem watermarkFontColorItem = new JMenuItem("Font Color");
		watermarkFontColorItem.setForeground(Color.BLACK);
		watermarkFontColorItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				

				Color tempColor = JColorChooser.showDialog(null, "Choose a color for the watermark", sheet.getWatermarkColor());
				
				
				if(tempColor == null)
					return;
				Color color =  new Color(tempColor.getRed(), tempColor.getGreen(), tempColor.getBlue(), sheet.getWatermarkTransparency()) {
					@Override
					public String toString() {
						
						return this.getRed() + "," + this.getGreen() + "," + this.getBlue() + "," + this.getAlpha();
					}
				};
				if(color == null)
					return;	
				
				
				
				xmlProperty.writeProperty("watermarkColor", color.toString());
				sheet.setWatermarkColor(color);
			}
		});
		watermarkMenu.add(watermarkFontColorItem);
		
		JMenuItem watermarkTextItem = new JMenuItem("Text");
		watermarkTextItem.setForeground(Color.BLACK);
		watermarkTextItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = JOptionPane.showInputDialog(null, "Enter text for the watermark", "Watermark text", JOptionPane.QUESTION_MESSAGE);
				if(input == null || input.equals("")) {
					return;
				}
				
				xmlProperty.writeProperty("watermarkText", input);
			}
		});
		watermarkMenu.add(watermarkTextItem);
		
		JMenu themeMenu = new JMenu("Theme");
		themeMenu.setForeground(Color.BLACK);
		settingsMenu.add(themeMenu);
		
		JMenuItem lightThemeItem = new JMenuItem("Light");
		lightThemeItem.setForeground(Color.BLACK);
		lightThemeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!confirmApplyingTheme())
					return;
				//light theme property
				xmlProperty.writeProperty("theme", "light");
				
				
				//main window colors light theme
				xmlProperty.writeColorProperty("mainContentPaneColor", new Color(220,220,220,255));
				xmlProperty.writeColorProperty("mainPrintPanelColor", new Color(220,220,220,255));
				xmlProperty.writeColorProperty("mainWordPairPanelColor", new Color(220,220,220,255));
				xmlProperty.writeColorProperty("mainMenuBarColor", new Color(252,252,252,255));
				xmlProperty.writeColorProperty("mainWordPairsListColor", new Color(252,252,252,255));
				xmlProperty.writeColorProperty("mainWordPairsListSelectionColor", new Color(252,252,252,255));
				xmlProperty.writeColorProperty("mainClearAllButtonColor", new Color(252,252,252,255));
				xmlProperty.writeColorProperty("mainPrintButtonColor", new Color(252,252,252,255));
				xmlProperty.writeColorProperty("mainRemovePairButtonColor",new Color(252,252,252,255));
				xmlProperty.writeColorProperty("mainWordPanelColor", new Color(220,220,220,255));
				xmlProperty.writeColorProperty("mainTranslationPanelColor", new Color(220,220,220,255));
				xmlProperty.writeColorProperty("mainWordFieldColor", new Color(252,252,252,255));
				xmlProperty.writeColorProperty("mainTranslationFieldColor", new Color(252,252,252,255));
				xmlProperty.writeColorProperty("mainWordLanguageColor", new Color(252,252,252,255));
				xmlProperty.writeColorProperty("mainTranslationLanguageColor", new Color(252,252,252,255));
				xmlProperty.writeColorProperty("mainAddWordPairColor", new Color(252,252,252,255));
				xmlProperty.writeColorProperty("mainSeparatorColor", new Color(220,220,220,255));
				
				
			}
		});
		themeMenu.add(lightThemeItem);
		
		JMenuItem grayThemeItem = new JMenuItem("Gray");
		grayThemeItem.setForeground(Color.BLACK);
		grayThemeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				if(!confirmApplyingTheme())
					return;
				//gray theme property
				xmlProperty.writeProperty("theme", "gray");
				
				//main window colors gray theme
				xmlProperty.writeColorProperty("mainContentPaneColor", new Color(128,128,128,255));
				xmlProperty.writeColorProperty("mainPrintPanelColor", new Color(128,128,128,255));
				xmlProperty.writeColorProperty("mainWordPairPanelColor", new Color(128,128,128,255));
				xmlProperty.writeColorProperty("mainMenuBarColor", new Color(192,192,192,255));
				xmlProperty.writeColorProperty("mainWordPairsListColor",  new Color(192,192,192,255));
				xmlProperty.writeColorProperty("mainWordPairsListSelectionColor", new Color(192,192,192,255));
				xmlProperty.writeColorProperty("mainClearAllButtonColor", new Color(192,192,192,255));
				xmlProperty.writeColorProperty("mainPrintButtonColor", new Color(192,192,192,255));
				xmlProperty.writeColorProperty("mainRemovePairButtonColor", new Color(192,192,192,255));
				xmlProperty.writeColorProperty("mainWordPanelColor", new Color(128,128,128,255));
				xmlProperty.writeColorProperty("mainTranslationPanelColor", new Color(128,128,128,255));
				xmlProperty.writeColorProperty("mainWordFieldColor", new Color(192,192,192,255));
				xmlProperty.writeColorProperty("mainTranslationFieldColor", new Color(192,192,192,255));
				xmlProperty.writeColorProperty("mainWordLanguageColor", new Color(192,192,192,255));
				xmlProperty.writeColorProperty("mainTranslationLanguageColor", new Color(192,192,192,255));
				xmlProperty.writeColorProperty("mainAddWordPairColor", new Color(192,192,192,255));
				xmlProperty.writeColorProperty("mainSeparatorColor", new Color(1,1,1,255));
				
			}
		});
		themeMenu.add(grayThemeItem);
		
		//apply colors for main window's components
		try {
			frame.getContentPane().setBackground(	xmlProperty.getColorValue("mainContentPaneColor") );
			printPanel.setBackground(				xmlProperty.getColorValue("mainPrintPanelColor"));
			wordPairPanel.setBackground(			xmlProperty.getColorValue("mainWordPairPanelColor"));
			menuBar.setBackground(					xmlProperty.getColorValue("mainMenuBarColor"));
			wordPairsList.setBackground(			xmlProperty.getColorValue("mainWordPairsListColor"));
			wordPairsList.setSelectionBackground(	xmlProperty.getColorValue("mainWordPairsListSelectionColor"));
			clearAllButton.setBackground(			xmlProperty.getColorValue("mainClearAllButtonColor"));
			printButton.setBackground(				xmlProperty.getColorValue("mainPrintButtonColor"));
			removePairButton.setBackground(			xmlProperty.getColorValue("mainRemovePairButtonColor"));
			wordPanel.setBackground(				xmlProperty.getColorValue("mainWordPanelColor"));
			translationPanel.setBackground(			xmlProperty.getColorValue("mainTranslationPanelColor"));
			wordField.setBackground(				xmlProperty.getColorValue("mainWordFieldColor"));
			translationField.setBackground(			xmlProperty.getColorValue("mainTranslationFieldColor"));
			wordLanguage.setBackground(				xmlProperty.getColorValue("mainWordLanguageColor"));
			translationLanguage.setBackground( 		xmlProperty.getColorValue("mainTranslationLanguageColor"));
			addWordPair.setBackground(				xmlProperty.getColorValue("mainAddWordPairColor"));
			separator.setBackground(				xmlProperty.getColorValue("mainSeparatorColor"));
			if(xmlProperty.getValue("theme").equals("light")) {
				lightThemeItem.setEnabled(false);
				lightThemeItem.setText(lightThemeItem.getText() + " ✓");
				}
			else if(xmlProperty.getValue("theme").equals("gray")) {
				grayThemeItem.setEnabled(false);
				grayThemeItem.setText(grayThemeItem.getText() + " ✓");

			}

		} catch (Exception e) {
			xmlProperty.setDefaultValues();
		}
		
		
	}
	
	private boolean confirmApplyingTheme() {
		int input = JOptionPane.showInternalConfirmDialog(null, "To apply theme you need to restart the app", "Theme",2);
		if(input == 0)
			return true;
		//if(input == 1 || input == 2)
			return false;
	}
	public WordPair findPairByToStringInList(String toString) {
		for(WordPair pairInList : wordPairList) {
			if(pairInList.toString().equals(toString))
				return pairInList;
		}
		return new WordPair();
	}
	public WordPair findPairByUUIDgInList(String UUID) {
		for(WordPair pairInList : wordPairList) {
			if(pairInList.getUUID().equals(UUID))
				return pairInList;
		}
		return new WordPair();
	}
	public DefaultListModel createDefaultListModelFromList() {
		DefaultListModel model = new DefaultListModel();
		for(WordPair pair : wordPairList) {
			model.addElement(pair.toString());
		}
		return model;
	}
	public void resetEditing(JButton button) {
		editingPair = ".";
		wordField.setText("");
		translationField.setText("");
		button.setText("Add");
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	private void openFile(File file){
		List<WordPair> list = new ArrayList<>();
		
		ReadObject ro = new ReadObject(file.getPath());
		
		while(true) {
			try {
				WordPair pair = (WordPair)ro.read();
				list.add(pair);
			} catch (ClassNotFoundException | IOException e) {
				ro.close();
				wordPairList = list;
				listModel = createDefaultListModelFromList();
				return;
				
			}finally {
				resetEditing(addWordPair);
			}
		}
	}
	private void resetWords(JList wordPairs, DefaultListModel model, JLabel wordPairsLabel) {
		wordPairs.setModel(model);
		wordPairsLabel.setText("WordPairs: " + wordPairList.size());
	}	
}
