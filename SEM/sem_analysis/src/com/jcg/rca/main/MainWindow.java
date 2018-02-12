package com.jcg.rca.main;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Group;

public class MainWindow {

	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtMin;
	private Text txtMax;
	private String filepath;
	private File file;
	private Text txtInc;
	private JFrame frame;
	private boolean fileChosen = false;
	private BufferedImage image;

	/**
	 * Launch the application.
	 * @param args
	 */
	
	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();
			window.open();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		//shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		shell.setMinimumSize(new Point(0, 0));
		shell.setSize(371, 333);
		shell.setText("Duty Cycle Analysis");
		shell.setLayout(null);
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		MenuItem mntmOpen = new MenuItem(menu_1, SWT.CASCADE);
		mntmOpen.setText("Open");
		
		Menu menu_2 = new Menu(mntmOpen);
		mntmOpen.setMenu(menu_2);
		
		MenuItem mntmBrowse = new MenuItem(menu_2, SWT.NONE);
		mntmBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//open file browsing when selected  
				//Create a file choose 
				//TODO: add error check for non-image files being chosen 
				String semDirectory = "S:\\Lab\\Meas\\SEM";
				final JFileChooser fc = new JFileChooser(semDirectory);
								
				if (e.getSource() == mntmBrowse) {
					int returnVal = fc.showOpenDialog(fc);
					
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						file = fc.getSelectedFile();
						fileChosen = true;
						
						filepath = file.toString();
					}
					else
					{
						fileChosen = false;
					}
					
					if(fileChosen = true) {
						//display original image once file is picked, in a new window
						ImageLoader img = new ImageLoader();
						img.setImage(file);
					}
				}
				
			}
		});
		
		
		mntmBrowse.setText("Browse");
		
		MenuItem mntmClose = new MenuItem(menu_1, SWT.NONE);
		mntmClose.setText("Close");
		
		Group grpEdgeDetection = new Group(shell, SWT.NONE);
		grpEdgeDetection.setBounds(5, 5, 179, 172);
		grpEdgeDetection.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		grpEdgeDetection.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		grpEdgeDetection.setText("Edge Detection ");
		formToolkit.adapt(grpEdgeDetection);
		formToolkit.paintBordersFor(grpEdgeDetection);
		grpEdgeDetection.setLayout(null);
		
		Label lblMin = formToolkit.createLabel(grpEdgeDetection, "Min", SWT.NONE);
		lblMin.setBounds(8, 43, 21, 15);
		
		txtMin = new Text(grpEdgeDetection, SWT.BORDER);
		txtMin.setBounds(136, 40, 35, 21);
		txtMin.setText(".5");
		formToolkit.adapt(txtMin, true, true);
		
		Label lblMax = formToolkit.createLabel(grpEdgeDetection, "Max", SWT.NONE);
		lblMax.setBounds(8, 89, 22, 15);
		
		txtMax = new Text(grpEdgeDetection, SWT.BORDER);
		txtMax.setBounds(136, 86, 35, 21);
		txtMax.setText("1");
		formToolkit.adapt(txtMax, true, true);
		
		Button btnDisplay = new Button(grpEdgeDetection, SWT.BORDER);
		btnDisplay.setBounds(8, 132, 54, 29);
		btnDisplay.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		btnDisplay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//on button press, display edge detected SEM image on the original image Frame 
				
				ImageLoader img = new ImageLoader();
				//DutyCycle dc = new DutyCycle();
				frame = new JFrame("Edge Image");
				
				
				if(fileChosen == true) {
					//take values from the gui 
				    float minValue = Float.parseFloat(txtMin.getText());
					float maxValue = Float.parseFloat(txtMax.getText());
					
					//display edges
					img.detectEdges(file, minValue, maxValue);
					img.displayImg(frame);
					image = img.getEdgesImage();
				}
				else {
					//do something 
					System.out.print("No image chosen");
					System.exit(0);
				}
			}
		});
		formToolkit.adapt(btnDisplay, true, true);
		btnDisplay.setText("Display");
		
		Button btnDc = new Button(grpEdgeDetection, SWT.BORDER);
		btnDc.addSelectionListener(new SelectionAdapter() {
			//Runs Duty Cycle Calculation on button push
			@Override
			public void widgetSelected(SelectionEvent e) {
			//Calculate duty cycle on button click
				
				PixelCalculation pixels = new PixelCalculation();
				ImageLoader loader = new ImageLoader();
				image = loader.getEdgesImage();
				
				pixels.setEdgeImage(image);
				try {
					pixels.process(image);
					
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				int[] pixelArray = pixels.getpixelValueArr();
				int imageW = pixels.getImageHeight();
				int imageH = pixels.getImageWidth();
				
				DataTable table = new DataTable();
				table.setPixelArray(pixelArray);
				table.setImageWidth(imageW);
				table.setImageHeight(imageH);
				
				//Create array of pixels
				DataMap map = new DataMap();
				int[][] coordsArray = new int[imageW][imageH];
				coordsArray = map.createMap(pixelArray, imageW, imageH);
				//System.out.println(Arrays.deepToString(coordsArray) + "\n");
				
				//Calculate the duty cycle 
				DutyCycle dc = new DutyCycle();
				//System.out.print("Image Height: " + imageH + " Image Width: " + imageW + "\n");
				dc.setCoords(coordsArray);
				dc.process();
				
				DCFrame frame = new DCFrame(null);
				frame.setData();
				frame.open();
				
			}
		});
		btnDc.setBounds(78, 132, 45, 29);
		formToolkit.adapt(btnDc, true, true);
		btnDc.setText("DC");
		
		Group grpCleanup = new Group(shell, SWT.NONE);
		grpCleanup.setBounds(5, 182, 180, 82);
		grpCleanup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		grpCleanup.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		grpCleanup.setText("Clean-up");
		formToolkit.adapt(grpCleanup);
		formToolkit.paintBordersFor(grpCleanup);
		
		Label lblIncrement = new Label(grpCleanup, SWT.NONE);
		lblIncrement.setBounds(10, 23, 55, 15);
		formToolkit.adapt(lblIncrement, true, true);
		lblIncrement.setText("Increment");
		
		txtInc = new Text(grpCleanup, SWT.BORDER);
		txtInc.setText(".5");
		txtInc.setBounds(129, 20, 41, 21);
		formToolkit.adapt(txtInc, true, true);
		
		Button btnUp = new Button(grpCleanup, SWT.BORDER);
		btnUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//increment Max value on button click, 0.5 by default, redisplay image
				
				//get values from text boxes, convert to float
				float incValue = Float.parseFloat(txtInc.getText());
				float minValue = Float.parseFloat(txtMin.getText());
				float maxValue = Float.parseFloat(txtMax.getText());
				
				//add values
				float updatedMax = maxValue + incValue;
				
				//cast back to string for the text box
				String updatedMaxStr = String.valueOf(updatedMax);

				//update text boxes 
				txtMax.setText(updatedMaxStr);
				
				//update image
				ImageLoader img = new ImageLoader();
				img.detectEdges(file, minValue, maxValue);
				
				//img.displayImg(frame);
				img.redrawImg(frame, image);
				image = img.getEdgesImage();
				
			}
		});
		
		btnUp.setBounds(34, 47, 27, 25);
		formToolkit.adapt(btnUp, true, true);
		btnUp.setText("Up");
		
		Button btnDown = new Button(grpCleanup, SWT.BORDER);
		btnDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//decrement Max value on button click, -0.5 by default, redisplay image
				
				//get values from text boxes, convert to float
				//make the incValue a negative value
				float decValue = -(Float.parseFloat(txtInc.getText()));
				float minValue = Float.parseFloat(txtMin.getText());
				float maxValue = Float.parseFloat(txtMax.getText());
				
				//add values
				float updatedMax = maxValue + decValue;
				
				//cast back to string for the text box
				String updatedMaxStr = String.valueOf(updatedMax);

				//update text boxes 
				if (maxValue > 0) {
					txtMax.setText(updatedMaxStr);
					
					//update image
					ImageLoader img = new ImageLoader();
					img.detectEdges(file, minValue, maxValue);
					
					img.redrawImg(frame, image);
					image = img.getEdgesImage();
				}
				else {
					//do something 
				}
			}
		});
		btnDown.setBounds(70, 47, 41, 25);
		formToolkit.adapt(btnDown, true, true);
		btnDown.setText("Down");
		
		Group grpData = new Group(shell, SWT.NONE);
		grpData.setText("Data");
		grpData.setBounds(190, 5, 109, 94);
		formToolkit.adapt(grpData);
		formToolkit.paintBordersFor(grpData);
		
		Button btnTable = new Button(grpData, SWT.BORDER);
		btnTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//open the data table on button click
				DataTable table = new DataTable();
				table.open();
			}
		});
		btnTable.setBounds(10, 34, 48, 25);
		formToolkit.adapt(btnTable, true, true);
		btnTable.setText("Table");

	}
}
