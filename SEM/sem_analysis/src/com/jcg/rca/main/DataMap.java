package com.jcg.rca.main;

public class DataMap{
	
	private final int black = -8443864;
	private final int white = -22360;
	private static int[][] coordsArray;
	
	
	public int[][] getCoords() {
		return coordsArray;
	}
	
	//Create a multidimensional array of all the pixels in the chosen setion of the image, 
	//the array will contain all the pixels of the selected image  
	//there is definitely a much better way to do this 
	public int[][] createMap(int[] pixelValue, int imageWidth, int imageHeight) {
		
		int count = 0;
		int mapCount = 0;
		coordsArray = new int[imageWidth][imageHeight];
		
		for(int i = 0; i < imageHeight; i++) {
			for(int j = 0; j < imageWidth; j++) {
				Integer value = pixelValue[count];
				
				if(value == white) {
					coordsArray[j][i] = value;
					mapCount++;
					
				}
				count++;
			}
		}
		//System.out.println("Array Count: " + mapCount);
		return coordsArray;
	}
	
	
	
	
}