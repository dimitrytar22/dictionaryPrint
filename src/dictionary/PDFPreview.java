package dictionary;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.EmptyBorder;

import dictionary.sheet.Sheet;

import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.MouseMotionAdapter;

public class PDFPreview extends JDialog {

	private final JPanel contentPanel = new JPanel();


	/**
	 * Create the dialog.
	 */
	public PDFPreview(List wordPairs) {
		//setModal(true);
		setBounds(100, 100, 1000, 600);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 984, 561);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		
		scrollPane.setBounds(0, 0,984, 561);
		contentPanel.add(scrollPane);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setAutoscrolls(true);
		scrollPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
		Sheet sheet = null;
		try {
			sheet = new Sheet(wordPairs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ImageIcon image = new ImageIcon((sheet.drawWords(1920, 1080, 30, "Dialog")));
		lblNewLabel.setIcon(image);
		
		scrollPane.setViewportView(lblNewLabel);
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		 MouseAdapter ma = new MouseAdapter() {

             private Point origin;

             @Override
             public void mousePressed(MouseEvent e) {
                 origin = new Point(e.getPoint());
                 scrollPane.setCursor(new Cursor(Cursor.MOVE_CURSOR));
             }

             @Override
             public void mouseReleased(MouseEvent e) {
            	 scrollPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
             }

             @Override
             public void mouseDragged(MouseEvent e) {
                 if (origin != null) {
                     JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, lblNewLabel);
                     if (viewPort != null) {
                         int deltaX = origin.x - e.getX();
                         int deltaY = origin.y - e.getY();

                         Rectangle view = viewPort.getViewRect();
                         view.x += deltaX;
                         view.y += deltaY;

                         lblNewLabel.scrollRectToVisible(view);
                     }
                 }
             }

         };
         lblNewLabel.addMouseListener(ma);
         lblNewLabel.addMouseMotionListener(ma);
		
	}
}
