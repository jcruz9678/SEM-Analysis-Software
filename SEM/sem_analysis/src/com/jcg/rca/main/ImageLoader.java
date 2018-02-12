package com.jcg.rca.main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;



/**
 * <pre><code>
 *Choose a source image
 *Display the source image
 *Apply Canny Edge Detection to source image
 *</code></pre>
 *
 * @author Justin Cruz
 * @param <JImageComponent>
 */

public class ImageLoader extends JFrame {
	public ImageLoader() {
	}
	
	private static BufferedImage edges;
	private static BufferedImage croppedImg;
	private JPanel panel;
	private int x;
	private int y;
	Point p1 = null;
	Point p2 = null;
	private Rectangle rect;
	
	public void createControls(Composite parent) {
		
	}
	
	
	//returns Edge image of type BufferedEdge
	public BufferedImage getEdgesImage() {
		return edges;
	}
	
	public void setEdgesImage(BufferedImage edges) {
		ImageLoader.edges = edges;
	}

	
	public void setImage(File file) {
		//Sets and displays the original SEM image chosen through the file chooser
		
		BufferedImage img = null;
		String path = file.getPath();
		
		
		//create frame for the original SEM image and displays it
		JFrame frame = new JFrame("Image");
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(path)));
		frame.pack();
		frame.setVisible(true);	
	}
	
	@Override
	public JPanel getContentPane() {
		return panel;
		
	}
	
	
	public BufferedImage detectEdges(File file, float minValue, float maxValue) {
		//Applies Canny Edge Detector to the original SEM image and returns the edges image
		
		BufferedImage img = null;
		
		//create a new detectorS
		CannyEdgeDetector detector = new CannyEdgeDetector();
		
		
		//try to set image
		try
		{
			img = ImageIO.read(file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		detector.setSourceImage(img);
		
		//adjust parameters for the Guassian filter as set in GUI
		detector.setLowThreshold(minValue);
		detector.setHighThreshold(maxValue);
		detector.process();
		
		edges = detector.getEdgesImage();
		return edges;
	}
	
	public void displayImg(JFrame edgeFrame) {
		//Displays the edge image on a new JFrame 
		
		//Create items for the frame
		panel = new JPanel();
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;
		JButton startButton;
		JButton clearButton;
		
		//Create a menu bar
		menuBar = new JMenuBar();
		
		//create buttons
		startButton = new JButton("Rectangle");
		clearButton = new JButton("Crop");
		
		//Build the menu
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(menu);
		menuBar.add(startButton);
		menuBar.add(clearButton);
		
		//menu items
		menuItem = new JMenuItem("Save", KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				//Save edge image on button click
				saveImage();
			}
		});

		//start button, used to create Rectangle defined by 2 mouse clicks
		startButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//on button press listen for clicks on the image
				panel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						//Get end points for rectangle when clicked\
						//TODO: create a way to remove a rectangle, also limit 1 rectangle per button click
						
						drawRectangle(e, edgeFrame);
					}
				});
			}
		});
		
		//Clear button, clears the image of anything outside of the chosen rectangle
		clearButton.addActionListener(new ActionListener() {
			//TODO: Add an undo option 
			@Override
			public void actionPerformed(ActionEvent e) {
				panel = (JPanel) edgeFrame.getContentPane();
				
				//updates to the cropped image 
				clear(edges, rect, edgeFrame);
				panel.removeAll();
				redrawImg(edgeFrame, croppedImg);
				panel.updateUI();
				
				edges = croppedImg;
			}
		});
		
		
		//set the edge image onto a jPanel and set the panel onto the jFrame
		//display frame containing edge image
		edgeFrame.getContentPane().add(panel);
		panel.setLayout(new BorderLayout());
		edgeFrame.getContentPane().add(menuBar, BorderLayout.NORTH);
		panel.add(new JLabel(new ImageIcon(edges)));
		edgeFrame.pack();
		edgeFrame.setVisible(true);
	}
	
	
	
	//Redraws the current edge image
	public JFrame redrawImg(JFrame edgeFrame, BufferedImage img){
		//updates the current JFrame
		
		JPanel newPanel = new JPanel();
		panel = (JPanel) edgeFrame.getContentPane();
		
		edgeFrame.remove(panel);
		edgeFrame.getContentPane().add(newPanel);
		newPanel.setLayout(new BorderLayout());
		newPanel.add(new JLabel(new ImageIcon(img)));
		edgeFrame.pack();
		edgeFrame.setVisible(true);
		
		
		return edgeFrame;
	}
	
	
	//Method to Save the edge image on the frame
	public void saveImage(){
		//Saves the edge image inside of the JFrame using file chooser
		//TODO: Save does not save the most recent image file, it save the first
		BufferedImage image;
		image = edges;
		
		JFileChooser chooser = new JFileChooser();

		int retrival = chooser.showSaveDialog(null);
		if(retrival == JFileChooser.APPROVE_OPTION) {
			try {
				File f = chooser.getSelectedFile();
				String test = f.getAbsolutePath();
				ImageIO.write(image, "png", f);
			} 
			catch(Exception ex) 
			{
				ex.printStackTrace();
			}
		}
	}
	
	
	//Method to draw a Rectangle on the image
	public Rectangle drawRectangle(MouseEvent e, JFrame edgeFrame) {
		
		rect = new Rectangle(); 
		
		if(p1 == null || p2 != null) {
			p1 = e.getPoint();
			p2 = null;
		}
		else {
			p2 = e.getPoint();
		}
	
		
		if(p1 != null && p2 != null) {
			
			Graphics2D g2d = edges.createGraphics();
			
			//create opaque color for rectangle 
			int alpha = 127;
			Color myColor = new Color(255, 80, 80, alpha);
			g2d.setColor(myColor);
			
			
			//create rectangle
			//points not being placed at correct positions 
			int xPoint = p1.x;
			int yPoint = p1.y;
			int width = p2.x - p1.x;
			int height = p2.y - p1.y;

			rect.setFrameFromDiagonal(p1,p2);
			g2d.fillRect(xPoint, yPoint, width, height);
			g2d.dispose();
			edgeFrame.repaint();
		}
		return rect;
	}
	
	
	//Method to clear anything outside of the rectangle defined by user
	public BufferedImage clear(BufferedImage src, Rectangle rect, JFrame edgeFrame) {
		
		int startX = rect.x; 
		int startY = rect.y;
		int endX = rect.width;
		int endY = rect.height;
		
		croppedImg = src.getSubimage(startX, startY, endX, endY); 
		
		return croppedImg; 
	}
}
