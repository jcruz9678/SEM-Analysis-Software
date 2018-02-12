package com.jcg.rca.main;

import java.lang.Math;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.Objects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ListMultimap;
public class DutyCycle {

	private ListMultimap<Integer, Integer> coordsMap = new ArrayListMultimap<Integer, Integer>();
	private int[][] coords;
	
	private static int pillarHeight;
	private static int pillarWidth;
	private static int period;
	private static double dutyCycle;
	
	private static String pillarHeightMM;
	private static String pillarWidthMM;
	private static String periodMM;
	private static String dutyCycleMM;
	private static String dutyCyclePercentMM;

	private int minValue; 
	private int heightMaxY = 0;
	private int heightMinY = 0;
	private int halfHeight = 0;
	private int tempI = 0;
	private int tempJ = 0;
	private int white = -22360;
	private int heightMaxX = 0;
	private int heightMinX = 0;
	private int widthStartY = 0;
	private int widthX1 = 0;
	private static int widthX2 = 0;
	private int maxValue;
	
	private static int periodEndX;
	private int periodStartX;
	
	
	public void setCoords(int[][] coords) {
		this.coords = coords;
	}
	
	public String getPillarHeight() {
		return pillarHeightMM;
	}
	
	public String getPillarWidth() {
		return pillarWidthMM;
	}
	
	public String getPeriod() {
		return periodMM;
		
	}
	
	public String getDutyCycle() {
		return dutyCycleMM;
	}
	
	public String getDcPercent() {
		return dutyCyclePercentMM;
	}

	
	//main process
	public void process() {
		System.out.print("DC Calculation begins.\n");
		getWhiteValues(coords);
		calcHeight();
		calcPillarWidth(coordsMap);
		calcPeriod();
		calcDutyCycle();
		convert();
		
	}
	
	
	public void convert() {
		pillarHeightMM = toMM(pillarHeight);
		pillarWidthMM = toMM(pillarWidth);
		periodMM = toMM(period);
		dutyCycleMM = dcConvert(dutyCycle);
		dutyCyclePercentMM = percentConvert(dutyCycle);
	}
	
	
	//Find the height of the pillars 
	public int calcHeight() {
		//for testing
		/*for(Map.Entry<Integer, Integer> entry : coordsMap.entries()) {
			Integer key = entry.getKey();
			Integer value = entry.getValue();
			
			//System.out.println("(" + key + "," + value + ")");
		}*/

		heightMaxY = getMax(coordsMap);
		System.out.print("Max Pillar Height: " + "(" + heightMaxX + "," +  heightMaxY + ")\n");
		heightMinY = getMin(coordsMap);
		System.out.print("Min Pillar Height: " + "(" + heightMinX + "," + heightMinY + ")\n");
		pillarHeight = Math.abs(heightMaxY - heightMinY);
		System.out.println("Abs Pillar Height: " + pillarHeight);
		
		return pillarHeight;
	}
	
	
	//takes only the pixels that are white and puts them into a multimap
	public Multimap<Integer, Integer> getWhiteValues(int[][] coords) {
		
		int count = 0;
		
		for (int i = 0; i < coords.length; i++) {
			int[] inner = coords[i];
			
			for(int j = 0; j < inner.length; j++) {
				if(coords[i][j] == white) {
					tempI = i;
					tempJ = j;
					coordsMap.put(i + 1, j + 1);
					count++;
				}
			}
		}
		//System.out.println("Map count: " + count);
		return coordsMap;
	}
	
	//get the max height of the pillar
	public int getMax(ListMultimap<Integer, Integer> coordsMap) {
		
		maxValue = (Collections.min(coordsMap.values()));
		
		heightMaxX = getKeyByValue( coordsMap, maxValue);
		
		return maxValue;
	}
	
	//get the min height of the pillar 
	public int getMin(ListMultimap<Integer, Integer> coordsMap) {
		
		minValue = (Collections.max(coordsMap.values()));
		
		heightMinX = getKeyByValue(coordsMap, minValue);
		
		return minValue;
	}
	
	//Find the width of the pillar at the middle of the pillar
	public void calcPillarWidth(ListMultimap<Integer, Integer> coordsMap) {
		//find half the height, in order to find the width of the pillar at 1/2height
		halfHeight = pillarHeight / 2;
		System.out.println("Half Height: " + halfHeight);
		
		widthStartY = heightMaxY + halfHeight;
		System.out.println("\nWidth Start Point: " + "(" + heightMaxX + "," + widthStartY + ")");
		
		widthX1 = getKeyByValue(coordsMap, widthStartY);
		widthX2 = getKeyByValue(coordsMap, widthStartY);
		System.out.println("Width Point1: " + "(" + widthX1 + "," + widthStartY + ")");
		System.out.println("Width Point2: " + "(" + widthX2 + "," + widthStartY + ")"); 
		
		pillarWidth = Math.abs(widthX1 - widthX2);
		
		System.out.println("Pillar Width: " + pillarWidth + "\n");
	}
	
	
	public int calcPeriod() {
		//Finds the period between the two pillars
		//Find the end point of the period using the 2nd point from pillarWidth
		periodEndX = getKeyByValue(coordsMap, widthStartY); 
		
		//Subtract the 2 X point to get the length of the period
		//calcPeriodX();
		periodStartX = widthX1;
		period = periodEndX - periodStartX;
		System.out.println("Period: " + period);
		System.out.println("Period start :(" + periodStartX + "," + widthStartY + ")");
		System.out.println("Period end :(" + periodEndX + "," + widthStartY + ")");
		return period;
	}

	
	//Calculate the duty cycle 
	public double calcDutyCycle() {
		
		System.out.println("DC PillarWidth" + pillarWidth);
		System.out.println("DC PillarHeight" + pillarHeight);
		float dutyCycleFloat = (float) pillarWidth / (float) period;
		dutyCycle = dutyCycleFloat;
		
		//System.out.println("DC Test: " + test);
		System.out.println("\nDutyCycle: " + dutyCycle);
		return dutyCycle;
	}
	
	
	
	//used to find the x value at a specific y point 
	//TODO: when used to find  with x points for the width, it can break b finding 2 pixels on the same size of the pillar
	//if the distance to the next pixel is shorter 
		public static <T, E> T getKeyByValue(ListMultimap<T, E> coordsMap, int value) {
			for(Entry<T, E> entry : coordsMap.entries()) {
				if (Objects.equals(value, entry.getValue())) {
					T xCoord = entry.getKey();
					
					//Removes the point so that when the width points are being found it will find 2 different points instead of the same one twice
					coordsMap.remove(entry.getKey(), entry.getValue());
					//System.out.println("Removing :" + entry + "," + entry);
					
					return xCoord;
				}
			}
			return null;
		}
	
	//used to convert all the data to micrometers from pixels
	//conversion: at a magnification of 40k : 1 pixel = .004748
	//Currently only works with images taken at a mag of 40k
	public String toMM(int value) {
		DecimalFormat df = new DecimalFormat("#.##");
		double mm = 0;
		double pixelNum = value; 
		double k = .004748;
		
		
		mm = ((pixelNum * k) * 1000);
		
		return df.format(mm);
	}
	
	public String dcConvert(double dc) {
		DecimalFormat df = new DecimalFormat("#.#####");
		
		return df.format(dc);
	}
	
	public String percentConvert(double dutyCycle) {
		DecimalFormat df = new DecimalFormat("#.###");
		
		double dcPercent = dutyCycle * 100;
		
		return df.format(dcPercent);
	}
}
