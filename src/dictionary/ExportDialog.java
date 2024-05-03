package dictionary;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ToolBarUI;

import dictionary.sheet.Sheet;
import dictionary.wordPair.WordPair;
import dictionary.xmlProperty.XMLProperty;
import fileTypeFilter.FileTypeFilter;
import serialization.ReadObject;

import java.awt.Font;
import java.awt.Dimension;
import javax.swing.JLabel;


public class ExportDialog extends JDialog {

	public final JPanel contentPanel = new JPanel();
    public JPanel buttonPane = new JPanel();
    private String userInput;
    private String title = "Export";
    List<WordPair> wordPairs = new ArrayList<>();
    File[] files = null;


    private XMLProperty xmlProperty = new XMLProperty("settings/settings.xml");
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ExportDialog dialog = new ExportDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ExportDialog() {
		
		initialize();
		
	}
	
	private void initialize() {
		
		if(!xmlProperty.isInitialized())
			xmlProperty.init();
		
		
		setTitle(title);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(false);

		getContentPane().setBackground(Color.DARK_GRAY);
		setResizable(false);
		setBounds(100, 100, 600, 400);
		getContentPane().setLayout(null);
		contentPanel.setLocation(0, 0);
		contentPanel.setBackground(Color.GRAY);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
			buttonPane.setBackground(Color.GRAY);
			buttonPane.setBounds(0, 296, 584, 65);
			getContentPane().add(buttonPane);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 17));
			
				JButton exportButton = new JButton("Export");
				
				exportButton.setForeground(Color.BLACK);
				exportButton.setBackground(Color.LIGHT_GRAY);
				exportButton.setFocusPainted(false);
				exportButton.setActionCommand("OK");
				buttonPane.add(exportButton);
				getRootPane().setDefaultButton(exportButton);
			
			
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setForeground(Color.BLACK);
				cancelButton.setBackground(Color.LIGHT_GRAY);
				cancelButton.setFocusPainted(false);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				
			
			
			JList list = new JList();
			DefaultListModel<File> defaultListModel = new DefaultListModel<File>();
			list.setModel(defaultListModel);
		    JScrollPane scrollPane = new JScrollPane(list);
		    scrollPane.setBounds(198, 12, 246, 272);
		    
		    
		   
		    
		    
		    contentPanel.add(scrollPane);
		  //  contentPanel.setSize(584,298);
		    contentPanel.setVisible(true);
		
		
		
		
		//export dialog element's colors


		try {
			contentPanel.setBackground(xmlProperty.getColorValue("exportDialogContentPanelColor"));
			
			JButton btnNewButton = new JButton("<html><center>Choose<br>Files</center></html>");
			btnNewButton.addActionListener(new ActionListener() {
				@SuppressWarnings("serial")
				public void actionPerformed(ActionEvent e) {
					
					
					 FileTypeFilter filter = new FileTypeFilter(".dpf", "Dictionary Printer File");
					 JFileChooser fc = new JFileChooser() {
						 @Override
						public void approveSelection() {
							super.approveSelection();
						}
					 };
					 
					 fc.setAcceptAllFileFilterUsed(true);
					 fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
					 fc.setMultiSelectionEnabled(true);
					 fc.setFileFilter(filter);
					 
					    File[] tempFiles = null;
					    
					    if(fc.showOpenDialog(contentPanel) == JFileChooser.APPROVE_OPTION) {
					    	tempFiles = new File[fc.getSelectedFiles().length];
					    	tempFiles = fc.getSelectedFiles();
					    	files = new File[fc.getSelectedFiles().length];
					    	System.out.println(tempFiles.length);
					    	for (int i = 0; i < fc.getSelectedFiles().length; i++) {
								
					    		files[i] = new File(tempFiles[i].getPath()) {
					    			@Override
					    			public String toString() {
										return getName();
									}
					    		};
							
					    		defaultListModel.addElement(files[i]);
					    	
								
					    	}
							
							
							
					    }
					    
					    
				}
			});
			btnNewButton.setMargin(new Insets(0, 0, 0, 0));
			btnNewButton.setFocusPainted(false);
			btnNewButton.setBackground(Color.LIGHT_GRAY);
			btnNewButton.setForeground(Color.BLACK);
			btnNewButton.setBounds(12, 54, 97, 42);
			contentPanel.add(btnNewButton);
			buttonPane.setBackground(xmlProperty.getColorValue("exportDialogButtonPaneColor"));
		} catch (Exception e) {
			xmlProperty.setDefaultValues();
		}
		 
		    contentPanel.add(new JScrollPane(new JList<>()));
		    contentPanel.setSize(584,296);
		    
		    JButton up = new JButton("↑");
		    up.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		if(list.getModel().getSize() <= 0)
		    			return;
		    		
		    		File selectedValue = (File) list.getSelectedValue();
		    		
		    		if(list.getSelectedIndex() == -1) {
		    		try {
						throw new Exception("NO DATA!");
					
					} catch (Exception e2) {
						e2.printStackTrace();
					}	
		    	}
		    		int elementIndex = list.getSelectedIndex();
		    		if(elementIndex <= 0 || elementIndex == defaultListModel.getSize())
		    			return;
		    		
		    		defaultListModel.remove(elementIndex);
		    		defaultListModel.add(elementIndex - 1, selectedValue);
		    		list.setSelectedIndex(elementIndex - 1);
		    		

		    		
		    	}
		    });
		    up.setFocusPainted(false);
		    up.setFocusTraversalKeysEnabled(false);
		    up.setBackground(Color.LIGHT_GRAY);
		    up.setMargin(new Insets(0, 0, 0, 0));
		    up.setForeground(Color.BLACK);
		    up.setFont(new Font("Dialog", Font.BOLD, 18));
		    up.setBounds(452, 111, 35, 35);
		    contentPanel.add(up);
		    
		    JButton down = new JButton("↓");
		    down.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		if(list.getModel().getSize() <= 0)
		    			return;
		    		
		    		File selectedValue = (File) list.getSelectedValue();
		    		
		    		if(list.getSelectedIndex() == -1) {
		    		try {
						throw new Exception("NO DATA!");
					
					} catch (Exception e2) {
						e2.printStackTrace();
					}	
		    	}
		    		int elementIndex = list.getSelectedIndex();
		    		if(elementIndex < 0 || elementIndex == defaultListModel.getSize() - 1)
		    			return;
		    		
		    		defaultListModel.remove(elementIndex);
		    		defaultListModel.add(elementIndex + 1, selectedValue);
		    		list.setSelectedIndex(elementIndex + 1);
		    		
		    	}
		    });
		    
		    exportButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
		    
		    down.setFocusPainted(false);
		    down.setFocusTraversalKeysEnabled(false);
		    down.setBackground(Color.LIGHT_GRAY);
		    down.setMargin(new Insets(0, 0, 0, 0));
		    down.setForeground(Color.BLACK);
		    down.setFont(new Font("Dialog", Font.BOLD, 18));
		    down.setBounds(452, 158, 35, 35);
		    contentPanel.add(down);
		    
		    JButton previewButton = new JButton("Preview");
		    previewButton.setBackground(Color.LIGHT_GRAY);
		    previewButton.setFocusTraversalKeysEnabled(false);
		    previewButton.setFocusPainted(false);
		    previewButton.setForeground(Color.BLACK);
		    previewButton.setBounds(448, 258, 98, 26);
		    contentPanel.add(previewButton);
		   
		    
		    
		    
		    
		    try {
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    contentPanel.setVisible(true);
		    previewButton.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		wordPairs.clear();
		    		for (int i = 0; i < list.getModel().getSize(); i++) {
						//ReadObject ro = new ReadObject(((File)list.getModel().getElementAt(i)).getPath());
		    			ReadObject ro = new ReadObject(((File)(list.getModel().getElementAt(i))).getPath());
						while(true) {
							try {
								WordPair pair = (WordPair)ro.read();
								wordPairs.add(pair);
							} catch (ClassNotFoundException | IOException e1) {

								break;
								
							}
						}
						ro.close();
						
					}
		    		
		    		PDFPreview dialog = new PDFPreview(wordPairs);
		    	}
		    });
	}
	
	public String showDialog() {
        setLocationRelativeTo(getParent());
        setVisible(true);
        return userInput;
    }
	public static String getLookAndFeelClassName(String nameSnippet) {
	    LookAndFeelInfo[] plafs = UIManager.getInstalledLookAndFeels();
	    for (LookAndFeelInfo info : plafs) {
	        if (info.getName().contains(nameSnippet)) {
	            return info.getClassName();
	        }
	    }
	    return null;
	}
}
