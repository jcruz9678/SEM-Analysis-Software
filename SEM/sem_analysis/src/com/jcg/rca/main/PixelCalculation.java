package com.jcg.rca.main;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;

public class PixelCalculation{
	
	private int[] pixelValueArr;
	private BufferedImage edgeImage;
	private static int imageHeight;
	private int imageWidth;
	private int arraySize;
	

	public PixelCalculation() {
		edgeImage = null;
	}
	
	//returns Edge image 
	public BufferedImage getEdgeImage() {
		return edgeImage;
	}
	
	//sets the Edge image
	public void setEdgeImage(BufferedImage edgeImage) {
		this.edgeImage = edgeImage;
	}
	
	//Returns an array of RGB values for each pixel
	public int[] getpixelValueArr() {
		return pixelValueArr;
	}
	
	
	//sets the pixelValueArr
	public void setpixelValueArr(int[] pixelValueArr) {
		this.pixelValueArr = pixelValueArr;
	}
	
	public int getImageHeight() {
		return imageHeight;
	}
	
	public int getImageWidth() {
		return imageWidth;
	}
	
	
	
	
	public void process(BufferedImage img ) throws InterruptedException{
		
		//Image axis are inverted 
		imageHeight = img.getWidth();
		imageWidth = img.getHeight();
		arraySize = imageHeight * imageWidth;
		
		PixelGrabber pg = new PixelGrabber(img, 0, 0, -1, -1, false);
		pg.grabPixels();
		
		//returns the pixel values in binay format
		rgbCalc(img);
		
		try {
			exportArray(pixelValueArr);
		} catch (IOException e) {
			
 			e.printStackTrace();
		}
	}
	
	public int[] rgbCalc(BufferedImage img) {
		
		//Calculates the pixel color value at each coordinate point 
		pixelValueArr = new int[arraySize];
		pixelValueArr = img.getRGB(0, 0, imageHeight, imageWidth, null, 0, imageHeight); 
		
		return pixelValueArr;
	}	
	
	
	public static void exportArray(int[] pixelValueArr) throws IOException {
		//Exports the pixel data to an .csv file for visual purposes
		
		int columnCount = 0;
		int columnMax = imageHeight;
		
		java.io.File intCSV = new java.io.File("PixelData.csv");
		
		java.io.PrintWriter outfile = new java.io.PrintWriter(intCSV);
		
		String strArray[] = new String[pixelValueArr.length]; 
		//System.out.print("Excel array size: " + pixelValueArr.length);
		
		for(int i = 0; i < pixelValueArr.length; i++) {
			strArray[i] = String.valueOf(pixelValueArr[i] + ",");
		}
		for(int i = 0; i < pixelValueArr.length; i++) {
			
			outfile.write(strArray[i].toString());
			columnCount++;
			
			if(columnCount == columnMax) {
				outfile.write("\n");
				columnCount = 0;
			}
			
		}
		
		outfile.close();
		
	}

}
